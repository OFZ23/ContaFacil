package com.example.contafacil.presentation.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contafacil.data.local.entity.ProductEntity
import com.example.contafacil.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class InventoryState(
    val products: List<ProductEntity> = emptyList(),
    val lowStockProducts: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class InventoryViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(InventoryState())
    val state: StateFlow<InventoryState> = _state.asStateFlow()

    init {
        loadProducts()
        loadLowStockProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            productRepository.getAllProducts()
                .collect { products ->
                    _state.value = _state.value.copy(
                        products = products,
                        isLoading = false
                    )
                }
        }
    }

    private fun loadLowStockProducts() {
        viewModelScope.launch {
            productRepository.getLowStockProducts()
                .collect { lowStockProducts ->
                    _state.value = _state.value.copy(
                        lowStockProducts = lowStockProducts
                    )
                }
        }
    }

    fun updateProduct(product: ProductEntity) {
        viewModelScope.launch {
            try {
                productRepository.updateProduct(product)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error al actualizar el producto: ${e.message}"
                )
            }
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch {
            try {
                productRepository.deleteProduct(product)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error al eliminar el producto: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}

