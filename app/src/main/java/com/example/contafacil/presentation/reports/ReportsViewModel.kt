package com.example.contafacil.presentation.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contafacil.data.local.entity.TransactionType
import com.example.contafacil.data.repository.ExpenseRepository
import com.example.contafacil.data.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class ReportsState(
    val totalSales: Double = 0.0,
    val costOfSales: Double = 0.0,
    val totalExpenses: Double = 0.0,
    // Flujo de caja
    val cashSales: Double = 0.0,
    val cashPurchases: Double = 0.0,
    val cashExpenses: Double = 0.0,
    val cashFlow: Double = 0.0,
    // Cuentas
    val accountsReceivable: Double = 0.0,
    val accountsPayable: Double = 0.0,
    val netProfit: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ReportsViewModel(
    private val transactionRepository: TransactionRepository,
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReportsState())
    val state: StateFlow<ReportsState> = _state.asStateFlow()

    init {
        loadReports()
    }

    fun loadReports(startDate: Long? = null, endDate: Long? = null) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                val calendar = Calendar.getInstance()
                val start = startDate ?: run {
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.timeInMillis
                }
                val end = endDate ?: System.currentTimeMillis()

                // Estado de resultados
                val totalSales = transactionRepository.getTotalAmountByType(
                    TransactionType.VENTA, start, end
                )
                val costOfSales = transactionRepository.getTotalCostByType(
                    TransactionType.VENTA, start, end
                )
                val totalExpenses = expenseRepository.getTotalExpenses(start, end)
                val netProfit = totalSales - costOfSales - totalExpenses

                // Flujo de caja
                val cashSales = transactionRepository.getTotalPaidByType(TransactionType.VENTA)
                val cashPurchases = transactionRepository.getTotalPaidByType(TransactionType.COMPRA)
                val cashExpenses = expenseRepository.getTotalAllExpenses()
                val cashFlow = cashSales - cashPurchases - cashExpenses

                // Cuentas
                val accountsReceivable = transactionRepository.getTotalUnpaidByType(
                    TransactionType.VENTA
                )
                val accountsPayable = transactionRepository.getTotalUnpaidByType(
                    TransactionType.COMPRA
                ) + expenseRepository.getTotalUnpaidExpenses()

                _state.value = _state.value.copy(
                    totalSales = totalSales,
                    costOfSales = costOfSales,
                    totalExpenses = totalExpenses,
                    cashSales = cashSales,
                    cashPurchases = cashPurchases,
                    cashExpenses = cashExpenses,
                    cashFlow = cashFlow,
                    accountsReceivable = accountsReceivable,
                    accountsPayable = accountsPayable,
                    netProfit = netProfit,
                    isLoading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error al cargar reportes: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}
