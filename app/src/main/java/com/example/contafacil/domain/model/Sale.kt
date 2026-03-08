package com.example.contafacil.domain.model

import com.example.contafacil.data.local.entity.PaymentMethod

data class Sale(
    val id: Long = 0,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val paymentMethod: PaymentMethod,
    val isPaid: Boolean,
    val date: Long = System.currentTimeMillis(),
    val notes: String? = null
) {
    val totalAmount: Double
        get() = quantity * unitPrice
}

