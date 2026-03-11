package com.example.contafacil.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: TransactionType, // VENTA o COMPRA
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val costUnitPrice: Double = 0.0,
    val totalAmount: Double,
    val paymentMethod: PaymentMethod,
    val isPaid: Boolean = true, // false si es a crédito
    val date: Long = System.currentTimeMillis(),
    val notes: String? = null
)
