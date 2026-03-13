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

enum class ReportPeriod {
    DIARIO,
    SEMANAL,
    MENSUAL,
    TODO_EL_TIEMPO
}

data class ReportsState(
    val selectedPeriod: ReportPeriod = ReportPeriod.MENSUAL,
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
        loadReports(ReportPeriod.MENSUAL)
    }

    fun onPeriodSelected(period: ReportPeriod) {
        loadReports(period)
    }

    private fun loadReports(period: ReportPeriod) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true, selectedPeriod = period)

                val (start, end) = getRangeForPeriod(period)

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
                val cashSales = transactionRepository.getTotalPaidByTypeInRange(
                    type = TransactionType.VENTA,
                    startDate = start,
                    endDate = end
                )
                val cashPurchases = transactionRepository.getTotalPaidByTypeInRange(
                    type = TransactionType.COMPRA,
                    startDate = start,
                    endDate = end
                )
                val cashExpenses = totalExpenses
                val cashFlow = cashSales - cashPurchases - cashExpenses

                // Cuentas
                val accountsReceivable = transactionRepository.getTotalUnpaidByTypeInRange(
                    type = TransactionType.VENTA,
                    startDate = start,
                    endDate = end
                )
                val accountsPayable = transactionRepository.getTotalUnpaidByTypeInRange(
                    type = TransactionType.COMPRA,
                    startDate = start,
                    endDate = end
                ) + expenseRepository.getTotalUnpaidExpensesInRange(
                    startDate = start,
                    endDate = end
                )

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

    private fun getRangeForPeriod(period: ReportPeriod): Pair<Long, Long> {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance()

        val start = when (period) {
            ReportPeriod.DIARIO -> {
                calendar.timeInMillis = now
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }

            ReportPeriod.SEMANAL -> {
                calendar.timeInMillis = now
                calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }

            ReportPeriod.MENSUAL -> {
                calendar.timeInMillis = now
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                calendar.timeInMillis
            }

            ReportPeriod.TODO_EL_TIEMPO -> 0L
        }

        return start to now
    }
}
