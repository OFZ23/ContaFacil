package com.example.contafacil.domain.service

import com.example.contafacil.domain.model.BusinessExpense
import com.example.contafacil.domain.model.FinancialReport
import com.example.contafacil.domain.model.Purchase
import com.example.contafacil.domain.model.Sale

object FinancialReportService {
    fun generate(
        sales: List<Sale>,
        purchases: List<Purchase>,
        expenses: List<BusinessExpense>
    ): FinancialReport {
        val totalIncome = sales.sumOf { it.totalAmount }
        val totalPurchases = purchases.sumOf { it.totalAmount }
        val totalExpenses = expenses.sumOf { it.amount }

        val accountsReceivable = sales
            .filter { !it.isPaid }
            .sumOf { it.totalAmount }

        val accountsPayableFromPurchases = purchases
            .filter { !it.isPaid }
            .sumOf { it.totalAmount }

        val accountsPayableFromExpenses = expenses
            .filter { !it.isPaid }
            .sumOf { it.amount }

        val accountsPayable = accountsPayableFromPurchases + accountsPayableFromExpenses
        val cashFlow = totalIncome - totalPurchases - totalExpenses

        return FinancialReport(
            totalIncome = totalIncome,
            totalPurchases = totalPurchases,
            totalExpenses = totalExpenses,
            cashFlow = cashFlow,
            accountsReceivable = accountsReceivable,
            accountsPayable = accountsPayable,
            netResult = cashFlow
        )
    }
}

