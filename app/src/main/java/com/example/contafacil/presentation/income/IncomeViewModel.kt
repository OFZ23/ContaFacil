package com.example.contafacil.presentation.income

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

data class IncomeState(
    val transactions: List<TransactionEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class IncomeViewModel(
    private val transactionRepository: TransactionRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(IncomeState())
    val state: StateFlow<IncomeState> = _state.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            transactionRepository.getTransactionsByType(TransactionType.VENTA)
                .collect { transactions ->
                    _state.value = _state.value.copy(
                        transactions = transactions,
                        isLoading = false
                    )
                }
        }
    }

    fun addSale(
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

                // Registrar la venta
                val transaction = TransactionEntity(
                    type = TransactionType.VENTA,
                    productName = productName,
                    quantity = quantity,
                    unitPrice = unitPrice,
                    totalAmount = totalAmount,
                    paymentMethod = paymentMethod,
                    isPaid = isPaid,
                    notes = notes
                )
                transactionRepository.insertTransaction(transaction)

                // Actualizar inventario (disminuir stock)
                productRepository.updateOrCreateProduct(
                    productName = productName,
                    quantity = quantity,
                    price = unitPrice,
                    isIncrease = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error al registrar la venta: ${e.message}"
                )
            }
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            try {
                transactionRepository.deleteTransaction(transaction)

                // Restaurar inventario
                productRepository.updateOrCreateProduct(
                    productName = transaction.productName,
                    quantity = transaction.quantity,
                    price = transaction.unitPrice,
                    isIncrease = true
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

