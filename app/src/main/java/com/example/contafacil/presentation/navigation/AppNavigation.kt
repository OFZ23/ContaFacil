package com.example.contafacil.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.contafacil.presentation.expenses.ExpensesScreen
import com.example.contafacil.presentation.income.IncomeScreen
import com.example.contafacil.presentation.inventory.InventoryScreen
import com.example.contafacil.presentation.purchases.PurchasesScreen
import com.example.contafacil.presentation.reports.ReportsScreen

@Composable
fun AppNavigation(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Income.route,
        modifier = modifier
    ) {
        composable(Screen.Income.route) {
            IncomeScreen()
        }
        composable(Screen.Purchases.route) {
            PurchasesScreen()
        }
        composable(Screen.Expenses.route) {
            ExpensesScreen()
        }
        composable(Screen.Inventory.route) {
            InventoryScreen()
        }
        composable(Screen.Reports.route) {
            ReportsScreen()
        }
    }
}


