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
    val totalIncome: Double = 0.0,
    val totalPurchases: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val cashFlow: Double = 0.0,
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

                // Si no se especifica fecha, usar el mes actual
                val start = startDate ?: run {
                    calendar.set(Calendar.DAY_OF_MONTH, 1)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.timeInMillis
                }

                val end = endDate ?: System.currentTimeMillis()

                // Obtener totales
                val totalIncome = transactionRepository.getTotalAmountByType(
                    TransactionType.VENTA, start, end
                )

                val totalPurchases = transactionRepository.getTotalAmountByType(
                    TransactionType.COMPRA, start, end
                )

                val totalExpenses = expenseRepository.getTotalExpenses(start, end)

                // Cuentas por cobrar y pagar
                val accountsReceivable = transactionRepository.getTotalUnpaidByType(
                    TransactionType.VENTA
                )

                val accountsPayable = transactionRepository.getTotalUnpaidByType(
                    TransactionType.COMPRA
                ) + expenseRepository.getTotalUnpaidExpenses()

                // Flujo de caja
                val cashFlow = totalIncome - totalPurchases - totalExpenses

                // Estado de resultados (Utilidad/Pérdida)
                val netProfit = totalIncome - totalPurchases - totalExpenses

                _state.value = _state.value.copy(
                    totalIncome = totalIncome,
                    totalPurchases = totalPurchases,
                    totalExpenses = totalExpenses,
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

