package com.example.contafacil.domain.model

data class FinancialReport(
    val totalIncome: Double,
    val totalPurchases: Double,
    val totalExpenses: Double,
    val cashFlow: Double,
    val accountsReceivable: Double,
    val accountsPayable: Double,
    val netResult: Double
)

