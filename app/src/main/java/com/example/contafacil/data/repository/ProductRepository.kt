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

    suspend fun updateOrCreateProduct(productName: String, quantity: Int, price: Double, isIncrease: Boolean) {
        val existingProduct = getProductByName(productName)
        if (existingProduct != null) {
            if (isIncrease) {
                increaseStock(existingProduct.id, quantity)
            } else {
                decreaseStock(existingProduct.id, quantity)
            }
            // Actualizar precio si cambió
            if (existingProduct.price != price) {
                updateProduct(existingProduct.copy(price = price))
            }
        } else {
            // Crear nuevo producto
            insertProduct(
                ProductEntity(
                    name = productName,
                    price = price,
                    stock = if (isIncrease) quantity else 0
                )
            )
        }
    }
}

