package com.example.contafacil.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: ExpenseCategory,
    val description: String,
    val amount: Double,
    val paymentMethod: PaymentMethod,
    val isPaid: Boolean = true,
    val date: Long = System.currentTimeMillis()
)

