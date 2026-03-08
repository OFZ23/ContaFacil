package com.example.contafacil.presentation.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contafacil.data.local.entity.ExpenseCategory
import com.example.contafacil.data.local.entity.ExpenseEntity
import com.example.contafacil.data.local.entity.PaymentMethod
import com.example.contafacil.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExpensesState(
    val expenses: List<ExpenseEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ExpensesViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExpensesState())
    val state: StateFlow<ExpensesState> = _state.asStateFlow()

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            expenseRepository.getAllExpenses()
                .collect { expenses ->
                    _state.value = _state.value.copy(
                        expenses = expenses,
                        isLoading = false
                    )
                }
        }
    }

    fun addExpense(
        category: ExpenseCategory,
        description: String,
        amount: Double,
        paymentMethod: PaymentMethod
    ) {
        viewModelScope.launch {
            try {
                val isPaid = paymentMethod != PaymentMethod.CREDITO

                val expense = ExpenseEntity(
                    category = category,
                    description = description,
                    amount = amount,
                    paymentMethod = paymentMethod,
                    isPaid = isPaid
                )
                expenseRepository.insertExpense(expense)

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error al registrar el gasto: ${e.message}"
                )
            }
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            try {
                expenseRepository.deleteExpense(expense)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = "Error al eliminar el gasto: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(errorMessage = null)
    }
}

