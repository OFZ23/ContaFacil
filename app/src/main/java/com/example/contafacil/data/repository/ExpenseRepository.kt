package com.example.contafacil.data.repository

import com.example.contafacil.data.local.dao.ExpenseDao
import com.example.contafacil.data.local.entity.ExpenseCategory
import com.example.contafacil.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    fun getAllExpenses(): Flow<List<ExpenseEntity>> =
        expenseDao.getAllExpenses()

    fun getExpensesByCategory(category: ExpenseCategory): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesByCategory(category)

    fun getExpensesByDateRange(startDate: Long, endDate: Long): Flow<List<ExpenseEntity>> =
        expenseDao.getExpensesByDateRange(startDate, endDate)

    fun getUnpaidExpenses(): Flow<List<ExpenseEntity>> =
        expenseDao.getUnpaidExpenses()

    suspend fun getTotalExpenses(startDate: Long, endDate: Long): Double =
        expenseDao.getTotalExpenses(startDate, endDate) ?: 0.0

    suspend fun getTotalUnpaidExpenses(): Double =
        expenseDao.getTotalUnpaidExpenses() ?: 0.0

    suspend fun insertExpense(expense: ExpenseEntity): Long =
        expenseDao.insertExpense(expense)

    suspend fun updateExpense(expense: ExpenseEntity) =
        expenseDao.updateExpense(expense)

    suspend fun deleteExpense(expense: ExpenseEntity) =
        expenseDao.deleteExpense(expense)
}

