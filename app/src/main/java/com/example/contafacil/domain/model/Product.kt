package com.example.contafacil.domain.model

data class Product(
    val id: Long = 0,
    val name: String,
    val price: Double,
    val stock: Int,
    val minStockAlert: Int = 5
) {
    val isLowStock: Boolean
        get() = stock <= minStockAlert
}

