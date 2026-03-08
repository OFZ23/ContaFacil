package com.example.contafacil.domain.model

import com.example.contafacil.data.local.entity.ExpenseCategory
import com.example.contafacil.data.local.entity.PaymentMethod

data class BusinessExpense(
    val id: Long = 0,
    val category: ExpenseCategory,
    val description: String,
    val amount: Double,
    val paymentMethod: PaymentMethod,
    val isPaid: Boolean,
    val date: Long = System.currentTimeMillis()
)

