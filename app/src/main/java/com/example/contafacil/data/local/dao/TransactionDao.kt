package com.example.contafacil.data.local.dao

import androidx.room.*
import com.example.contafacil.data.local.entity.PaymentMethod
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

    @Query("SELECT SUM(totalAmount) FROM transactions WHERE type = :type AND paymentMethod = :paymentMethod AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalAmountByTypeAndPaymentMethod(
        type: TransactionType,
        paymentMethod: PaymentMethod,
        startDate: Long,
        endDate: Long
    ): Double?

    @Query("SELECT SUM(quantity * costUnitPrice) FROM transactions WHERE type = :type AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalCostByType(type: TransactionType, startDate: Long, endDate: Long): Double?

    @Query("SELECT SUM(totalAmount) FROM transactions WHERE type = :type AND isPaid = 0")
    suspend fun getTotalUnpaidByType(type: TransactionType): Double?

    @Query("SELECT SUM(totalAmount) FROM transactions WHERE type = :type AND isPaid = 0 AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalUnpaidByTypeInRange(type: TransactionType, startDate: Long, endDate: Long): Double?

    @Query("SELECT COALESCE(SUM(totalAmount), 0) FROM transactions WHERE type = :type AND isPaid = 1")
    suspend fun getTotalPaidByType(type: TransactionType): Double?

    @Query("SELECT COALESCE(SUM(totalAmount), 0) FROM transactions WHERE type = :type AND isPaid = 1 AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalPaidByTypeInRange(type: TransactionType, startDate: Long, endDate: Long): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)
}
