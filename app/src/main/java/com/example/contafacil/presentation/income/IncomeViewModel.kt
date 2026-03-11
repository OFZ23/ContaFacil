package com.example.contafacil.presentation.income

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contafacil.data.local.entity.PaymentMethod
import com.example.contafacil.data.local.entity.ProductEntity
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
    val products: List<ProductEntity> = emptyList(),
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
        loadProducts()
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

    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts()
                .collect { products ->
                    _state.value = _state.value.copy(products = products)
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
                val normalizedProductName = productName.trim().replace("\\s+".toRegex(), " ")
                val product = productRepository.getProductByName(normalizedProductName)
                    ?: throw IllegalStateException(
                        "El producto '$normalizedProductName' no existe en inventario. Regístralo primero en Compras."
                    )

                val totalAmount = quantity * unitPrice
                val isPaid = paymentMethod != PaymentMethod.CREDITO
                val costUnitPrice = product.costPrice

                val transaction = TransactionEntity(
                    type = TransactionType.VENTA,
                    productName = normalizedProductName,
                    quantity = quantity,
                    unitPrice = unitPrice,
                    costUnitPrice = costUnitPrice,
                    totalAmount = totalAmount,
                    paymentMethod = paymentMethod,
                    isPaid = isPaid,
                    notes = notes
                )
                transactionRepository.insertTransaction(transaction)

                productRepository.updateOrCreateProduct(
                    productName = normalizedProductName,
                    quantity = quantity,
                    isIncrease = false,
                    salePrice = unitPrice
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error al registrar la venta: ${e.message}"
                )
            }
        }
    }

    fun markSaleAsPaid(transaction: TransactionEntity) {
        if (transaction.isPaid) return
        viewModelScope.launch {
            try {
                transactionRepository.updateTransaction(transaction.copy(isPaid = true))
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error al marcar venta como cobrada: ${e.message}"
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
                    isIncrease = true,
                    salePrice = transaction.unitPrice
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
