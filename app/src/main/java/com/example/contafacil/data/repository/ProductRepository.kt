package com.example.contafacil.data.repository

import com.example.contafacil.data.local.dao.ProductDao
import com.example.contafacil.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {

    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    fun getLowStockProducts(): Flow<List<ProductEntity>> = productDao.getLowStockProducts()

    suspend fun getProductById(productId: Long): ProductEntity? =
        productDao.getProductById(productId)

    suspend fun getProductByName(productName: String): ProductEntity? =
        productDao.getProductByName(productName)

    suspend fun insertProduct(product: ProductEntity): Long =
        productDao.insertProduct(product)

    suspend fun updateProduct(product: ProductEntity) =
        productDao.updateProduct(product)

    suspend fun deleteProduct(product: ProductEntity) =
        productDao.deleteProduct(product)

    suspend fun increaseStock(productId: Long, quantity: Int) =
        productDao.increaseStock(productId, quantity)

    suspend fun decreaseStock(productId: Long, quantity: Int) =
        productDao.decreaseStock(productId, quantity)

    suspend fun updateOrCreateProduct(
        productName: String,
        quantity: Int,
        isIncrease: Boolean,
        salePrice: Double? = null,
        costPrice: Double? = null
    ) {
        val normalizedName = productName.trim()
        val existingProduct = getProductByName(normalizedName)

        if (existingProduct != null) {
            val updatedStock = if (isIncrease) {
                existingProduct.stock + quantity
            } else {
                (existingProduct.stock - quantity).coerceAtLeast(0)
            }

            val updatedProduct = existingProduct.copy(
                name = normalizedName,
                price = salePrice ?: existingProduct.price,
                costPrice = costPrice ?: existingProduct.costPrice,
                stock = updatedStock
            )

            updateProduct(updatedProduct)
        } else {
            insertProduct(
                ProductEntity(
                    name = normalizedName,
                    price = salePrice ?: costPrice ?: 0.0,
                    costPrice = costPrice ?: 0.0,
                    stock = if (isIncrease) quantity else 0
                )
            )
        }
    }
}
