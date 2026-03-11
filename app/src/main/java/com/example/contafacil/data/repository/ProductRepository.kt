package com.example.contafacil.data.repository

import com.example.contafacil.data.local.dao.ProductDao
import com.example.contafacil.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ProductRepository(private val productDao: ProductDao) {

    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    fun getLowStockProducts(): Flow<List<ProductEntity>> = productDao.getLowStockProducts()

    suspend fun getProductById(productId: Long): ProductEntity? =
        productDao.getProductById(productId)

    suspend fun getProductByName(productName: String): ProductEntity? {
        val cleanName = productName.trim().replace("\\s+".toRegex(), " ")
        val normalizedName = normalizeProductName(cleanName)

        // Fast path: query directa (NOCASE en SQL)
        productDao.getProductByName(cleanName)?.let { return it }

        // Fallback: comparación normalizada para cubrir espacios/tildes inconsistentes
        return productDao.getAllProducts().first().firstOrNull {
            normalizeProductName(it.name) == normalizedName
        }
    }

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
        val cleanName = productName.trim().replace("\\s+".toRegex(), " ")
        val existingProduct = getProductByName(cleanName)

        if (existingProduct != null) {
            val updatedStock = if (isIncrease) {
                existingProduct.stock + quantity
            } else {
                (existingProduct.stock - quantity).coerceAtLeast(0)
            }

            val updatedProduct = existingProduct.copy(
                name = existingProduct.name,
                price = salePrice ?: existingProduct.price,
                costPrice = costPrice ?: existingProduct.costPrice,
                stock = updatedStock
            )

            updateProduct(updatedProduct)
        } else {
            // Solo compras pueden crear productos nuevos en inventario.
            if (!isIncrease) {
                throw IllegalStateException("No existe el producto '$cleanName' en inventario para descontar stock")
            }

            insertProduct(
                ProductEntity(
                    name = cleanName,
                    price = salePrice ?: costPrice ?: 0.0,
                    costPrice = costPrice ?: 0.0,
                    stock = quantity
                )
            )
        }
    }

    private fun normalizeProductName(name: String): String {
        val trimmed = name.trim().replace("\\s+".toRegex(), " ")
        return java.text.Normalizer
            .normalize(trimmed, java.text.Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
            .lowercase()
    }
}
