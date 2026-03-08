package com.example.contafacil.domain.service

import com.example.contafacil.domain.model.Product

object InventoryService {
    fun applyPurchase(product: Product, quantity: Int, unitPrice: Double): Product {
        require(quantity > 0) { "quantity must be greater than zero" }
        return product.copy(
            stock = product.stock + quantity,
            price = unitPrice
        )
    }

    fun applySale(product: Product, quantity: Int): Product {
        require(quantity > 0) { "quantity must be greater than zero" }
        val newStock = (product.stock - quantity).coerceAtLeast(0)
        return product.copy(stock = newStock)
    }
}

