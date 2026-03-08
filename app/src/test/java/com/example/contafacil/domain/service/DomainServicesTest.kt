package com.example.contafacil.domain.service

import com.example.contafacil.data.local.entity.ExpenseCategory
import com.example.contafacil.data.local.entity.PaymentMethod
import com.example.contafacil.domain.model.BusinessExpense
import com.example.contafacil.domain.model.Product
import com.example.contafacil.domain.model.Purchase
import com.example.contafacil.domain.model.Sale
import org.junit.Assert.assertEquals
import org.junit.Test

class DomainServicesTest {

    @Test
    fun applyPurchase_increasesStockAndUpdatesPrice() {
        val product = Product(id = 1, name = "Cafe", price = 1000.0, stock = 10)

        val updated = InventoryService.applyPurchase(product, quantity = 5, unitPrice = 1200.0)

        assertEquals(15, updated.stock)
        assertEquals(1200.0, updated.price, 0.0)
    }

    @Test
    fun applySale_decreasesStock() {
        val product = Product(id = 1, name = "Cafe", price = 1000.0, stock = 10)

        val updated = InventoryService.applySale(product, quantity = 3)

        assertEquals(7, updated.stock)
    }

    @Test
    fun generateReport_calculatesExpectedTotals() {
        val sales = listOf(
            Sale(productName = "Cafe", quantity = 3, unitPrice = 2000.0, paymentMethod = PaymentMethod.EFECTIVO, isPaid = true),
            Sale(productName = "Pan", quantity = 2, unitPrice = 1000.0, paymentMethod = PaymentMethod.CREDITO, isPaid = false)
        )

        val purchases = listOf(
            Purchase(productName = "Cafe", quantity = 10, unitPrice = 1200.0, paymentMethod = PaymentMethod.TRANSFERENCIA, isPaid = true),
            Purchase(productName = "Leche", quantity = 5, unitPrice = 1500.0, paymentMethod = PaymentMethod.CREDITO, isPaid = false)
        )

        val expenses = listOf(
            BusinessExpense(category = ExpenseCategory.LUZ, description = "Factura", amount = 30000.0, paymentMethod = PaymentMethod.EFECTIVO, isPaid = true),
            BusinessExpense(category = ExpenseCategory.INTERNET, description = "Plan", amount = 50000.0, paymentMethod = PaymentMethod.CREDITO, isPaid = false)
        )

        val report = FinancialReportService.generate(sales, purchases, expenses)

        assertEquals(8000.0, report.totalIncome, 0.0)
        assertEquals(19500.0, report.totalPurchases, 0.0)
        assertEquals(80000.0, report.totalExpenses, 0.0)
        assertEquals(2000.0, report.accountsReceivable, 0.0)
        assertEquals(57500.0, report.accountsPayable, 0.0)
        assertEquals(-91500.0, report.cashFlow, 0.0)
        assertEquals(-91500.0, report.netResult, 0.0)
    }
}

