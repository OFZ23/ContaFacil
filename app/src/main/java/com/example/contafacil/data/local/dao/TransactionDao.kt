package com.example.contafacil.data.local.dao

import androidx.room.*
import com.example.contafacil.data.local.entity.TransactionEntity
import com.example.contafacil.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: TransactionType): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type AND isPaid = 0")
    fun getUnpaidTransactions(type: TransactionType): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(totalAmount) FROM transactions WHERE type = :type AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalAmountByType(type: TransactionType, startDate: Long, endDate: Long): Double?

    @Query("SELECT SUM(totalAmount) FROM transactions WHERE type = :type AND isPaid = 0")
    suspend fun getTotalUnpaidByType(type: TransactionType): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}

