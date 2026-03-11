package com.example.contafacil.data.local.dao

import androidx.room.*
import com.example.contafacil.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Long): ProductEntity?

    @Query("SELECT * FROM products WHERE name = :productName LIMIT 1")
    suspend fun getProductByName(productName: String): ProductEntity?

    @Query("SELECT * FROM products WHERE stock <= minStockAlert")
    fun getLowStockProducts(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("UPDATE products SET stock = stock + :quantity WHERE id = :productId")
    suspend fun increaseStock(productId: Long, quantity: Int)

    @Query("UPDATE products SET stock = MAX(stock - :quantity, 0) WHERE id = :productId")
    suspend fun decreaseStock(productId: Long, quantity: Int)
}
