package com.example.contafacil.data.repository

import com.example.contafacil.data.local.dao.ProductDao
import com.example.contafacil.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ProductRepositoryTest {

    @Test
    fun updateOrCreateProduct_decreasesStockOnSale_andPreservesUpdatedPrices() = runBlocking {
        val dao = FakeProductDao(
            mutableListOf(
                ProductEntity(
                    id = 1,
                    name = "Cafe",
                    price = 1500.0,
                    costPrice = 1000.0,
                    stock = 10,
                    minStockAlert = 5
                )
            )
        )
        val repository = ProductRepository(dao)

        repository.updateOrCreateProduct(
            productName = "Cafe",
            quantity = 3,
            isIncrease = false,
            salePrice = 2000.0
        )

        val updated = dao.getProductByName("Cafe")
        assertNotNull(updated)
        assertEquals(7, updated?.stock)
        assertEquals(2000.0, updated?.price ?: 0.0, 0.0)
        assertEquals(1000.0, updated?.costPrice ?: 0.0, 0.0)
    }

    @Test
    fun updateOrCreateProduct_increasesStockOnPurchase_andUpdatesCostPrice() = runBlocking {
        val dao = FakeProductDao(
            mutableListOf(
                ProductEntity(
                    id = 1,
                    name = "Cafe",
                    price = 2000.0,
                    costPrice = 1000.0,
                    stock = 7,
                    minStockAlert = 5
                )
            )
        )
        val repository = ProductRepository(dao)

        repository.updateOrCreateProduct(
            productName = "Cafe",
            quantity = 5,
            isIncrease = true,
            costPrice = 1200.0
        )

        val updated = dao.getProductByName("Cafe")
        assertNotNull(updated)
        assertEquals(12, updated?.stock)
        assertEquals(2000.0, updated?.price ?: 0.0, 0.0)
        assertEquals(1200.0, updated?.costPrice ?: 0.0, 0.0)
    }
}

private class FakeProductDao(
    initialProducts: MutableList<ProductEntity> = mutableListOf()
) : ProductDao {
    private val products = initialProducts
    private val productsFlow = MutableStateFlow(products.sortedBy { it.name })

    private fun refreshFlow() {
        productsFlow.value = products.sortedBy { it.name }
    }

    override fun getAllProducts(): Flow<List<ProductEntity>> = productsFlow

    override suspend fun getProductById(productId: Long): ProductEntity? =
        products.firstOrNull { it.id == productId }

    override suspend fun getProductByName(productName: String): ProductEntity? =
        products.firstOrNull { it.name == productName }

    override fun getLowStockProducts(): Flow<List<ProductEntity>> =
        MutableStateFlow(products.filter { it.stock <= it.minStockAlert })

    override suspend fun insertProduct(product: ProductEntity): Long {
        val nextId = (products.maxOfOrNull { it.id } ?: 0L) + 1L
        products += product.copy(id = nextId)
        refreshFlow()
        return nextId
    }

    override suspend fun updateProduct(product: ProductEntity) {
        val index = products.indexOfFirst { it.id == product.id }
        if (index >= 0) {
            products[index] = product
            refreshFlow()
        }
    }

    override suspend fun deleteProduct(product: ProductEntity) {
        products.removeIf { it.id == product.id }
        refreshFlow()
    }

    override suspend fun increaseStock(productId: Long, quantity: Int) {
        val product = getProductById(productId) ?: return
        updateProduct(product.copy(stock = product.stock + quantity))
    }

    override suspend fun decreaseStock(productId: Long, quantity: Int) {
        val product = getProductById(productId) ?: return
        updateProduct(product.copy(stock = (product.stock - quantity).coerceAtLeast(0)))
    }
}

