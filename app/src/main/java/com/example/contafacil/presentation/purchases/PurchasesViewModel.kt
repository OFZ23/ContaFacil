package com.example.contafacil.presentation.purchases

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contafacil.data.local.entity.PaymentMethod
import com.example.contafacil.data.local.entity.TransactionEntity
import com.example.contafacil.data.local.entity.TransactionType
import com.example.contafacil.data.repository.ProductRepository
import com.example.contafacil.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PurchasesState(
    val transactions: List<TransactionEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class PurchasesViewModel(
    private val transactionRepository: TransactionRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PurchasesState())
    val state: StateFlow<PurchasesState> = _state.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            transactionRepository.getTransactionsByType(TransactionType.COMPRA)
                .collect { transactions ->
                    _state.value = _state.value.copy(
                        transactions = transactions,
                        isLoading = false
                    )
                }
        }
    }

    fun addPurchase(
        productName: String,
        quantity: Int,
        unitPrice: Double,
        paymentMethod: PaymentMethod,
        notes: String? = null
    ) {
        viewModelScope.launch {
            try {
                val totalAmount = quantity * unitPrice
                val isPaid = paymentMethod != PaymentMethod.CREDITO

                val transaction = TransactionEntity(
                    type = TransactionType.COMPRA,
                    productName = productName,
                    quantity = quantity,
                    unitPrice = unitPrice,
                    costUnitPrice = unitPrice,
                    totalAmount = totalAmount,
                    paymentMethod = paymentMethod,
                    isPaid = isPaid,
                    notes = notes
                )
                transactionRepository.insertTransaction(transaction)

                productRepository.updateOrCreateProduct(
                    productName = productName,
                    quantity = quantity,
                    isIncrease = true,
                    costPrice = unitPrice
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error al registrar la compra: ${e.message}"
                )
            }
        }
    }

    fun markPurchaseAsPaid(transaction: TransactionEntity) {
        if (transaction.isPaid) return
        viewModelScope.launch {
            try {
                transactionRepository.updateTransaction(transaction.copy(isPaid = true))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error al marcar compra como pagada: ${e.message}"
                )
            }
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            try {
                transactionRepository.deleteTransaction(transaction)

                productRepository.updateOrCreateProduct(
                    productName = transaction.productName,
                    quantity = transaction.quantity,
                    isIncrease = false,
                    costPrice = transaction.costUnitPrice
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error al eliminar la transacción: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}
