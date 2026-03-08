package com.example.contafacil.presentation.navigation

sealed class Screen(val route: String) {
    object Income : Screen("income")
    object Purchases : Screen("purchases")
    object Expenses : Screen("expenses")
    object Inventory : Screen("inventory")
    object Reports : Screen("reports")
}

