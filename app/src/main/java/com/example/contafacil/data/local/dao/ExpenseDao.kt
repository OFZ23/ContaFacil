package com.example.contafacil.data.local.dao

import androidx.room.*
import com.example.contafacil.data.local.entity.ExpenseEntity
import com.example.contafacil.data.local.entity.ExpenseCategory
import com.example.contafacil.data.local.entity.PaymentMethod
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE isPaid = 0")
    fun getUnpaidExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT SUM(amount) FROM expenses WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpenses(startDate: Long, endDate: Long): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE paymentMethod = :paymentMethod AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalExpensesByPaymentMethod(
        paymentMethod: PaymentMethod,
        startDate: Long,
        endDate: Long
    ): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE isPaid = 0")
    suspend fun getTotalUnpaidExpenses(): Double?

    @Query("SELECT SUM(amount) FROM expenses WHERE isPaid = 0 AND date BETWEEN :startDate AND :endDate")
    suspend fun getTotalUnpaidExpensesInRange(startDate: Long, endDate: Long): Double?

    @Query("SELECT COALESCE(SUM(amount),0) FROM expenses")
    suspend fun getTotalAllExpenses(): Double?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)
}
