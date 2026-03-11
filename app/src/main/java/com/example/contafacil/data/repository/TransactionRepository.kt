package com.example.contafacil.data.repository

import com.example.contafacil.data.local.dao.TransactionDao
import com.example.contafacil.data.local.entity.PaymentMethod
import com.example.contafacil.data.local.entity.TransactionEntity
import com.example.contafacil.data.local.entity.TransactionType
import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val transactionDao: TransactionDao) {

    fun getAllTransactions(): Flow<List<TransactionEntity>> =
        transactionDao.getAllTransactions()

    fun getTransactionsByType(type: TransactionType): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByType(type)

    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<TransactionEntity>> =
        transactionDao.getTransactionsByDateRange(startDate, endDate)

    fun getUnpaidTransactions(type: TransactionType): Flow<List<TransactionEntity>> =
        transactionDao.getUnpaidTransactions(type)

    suspend fun getTotalAmountByType(type: TransactionType, startDate: Long, endDate: Long): Double =
        transactionDao.getTotalAmountByType(type, startDate, endDate) ?: 0.0

    suspend fun getTotalAmountByTypeAndPaymentMethod(
        type: TransactionType,
        paymentMethod: PaymentMethod,
        startDate: Long,
        endDate: Long
    ): Double = transactionDao.getTotalAmountByTypeAndPaymentMethod(type, paymentMethod, startDate, endDate) ?: 0.0

    suspend fun getTotalCostByType(type: TransactionType, startDate: Long, endDate: Long): Double =
        transactionDao.getTotalCostByType(type, startDate, endDate) ?: 0.0

    suspend fun getTotalUnpaidByType(type: TransactionType): Double =
        transactionDao.getTotalUnpaidByType(type) ?: 0.0

    /** Solo las ventas/compras que ya están cobradas o pagadas (excluye créditos pendientes) */
    suspend fun getTotalPaidByType(type: TransactionType): Double =
        transactionDao.getTotalPaidByType(type) ?: 0.0

    suspend fun insertTransaction(transaction: TransactionEntity): Long =
        transactionDao.insertTransaction(transaction)

    suspend fun updateTransaction(transaction: TransactionEntity) =
        transactionDao.updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: TransactionEntity) =
        transactionDao.deleteTransaction(transaction)
}
