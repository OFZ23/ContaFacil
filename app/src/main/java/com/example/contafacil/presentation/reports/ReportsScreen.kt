package com.example.contafacil.presentation.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.contafacil.data.local.AppDatabase
import com.example.contafacil.data.repository.ExpenseRepository
import com.example.contafacil.data.repository.TransactionRepository
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val viewModel = remember {
        ReportsViewModel(
            TransactionRepository(database.transactionDao()),
            ExpenseRepository(database.expenseDao())
        )
    }

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reportes") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Flujo de Caja
            ReportCard(
                title = "Flujo de Caja",
                icon = Icons.Default.AccountBalanceWallet,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

                ReportRow(
                    label = "Ventas",
                    value = currencyFormat.format(state.cashSales),
                    valueColor = MaterialTheme.colorScheme.primary,
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    footnote = "Ventas cobradas (excluye créditos sin cobrar)"
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                ReportRow(
                    label = "Compras",
                    value = currencyFormat.format(state.cashPurchases),
                    valueColor = MaterialTheme.colorScheme.error,
                    icon = Icons.Default.ShoppingCart,
                    footnote = "Compras pagadas (excluye créditos sin pagar)"
                )

                ReportRow(
                    label = "Gastos",
                    value = currencyFormat.format(state.cashExpenses),
                    valueColor = MaterialTheme.colorScheme.error,
                    icon = Icons.Default.MoneyOff,
                    footnote = "Suma de todos los gastos"
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                ReportRow(
                    label = "Caja a cierre",
                    value = currencyFormat.format(state.cashFlow),
                    valueColor = if (state.cashFlow >= 0)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error,
                    icon = Icons.Default.AccountBalance,
                    isBold = true
                )
            }

            ReportCard(
                title = "Cuentas por Cobrar",
                icon = Icons.Default.MonetizationOn,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            ) {
                val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

                Text(
                    text = "Clientes que deben dinero:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currencyFormat.format(state.accountsReceivable),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )

                if (state.accountsReceivable > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⚠ Ventas a crédito pendientes de pago",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "✓ No hay cuentas pendientes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            ReportCard(
                title = "Cuentas por Pagar",
                icon = Icons.Default.CreditCard,
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            ) {
                val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

                Text(
                    text = "Deudas con proveedores:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currencyFormat.format(state.accountsPayable),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )

                if (state.accountsPayable > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⚠ Compras y gastos pendientes de pago",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "✓ No hay deudas pendientes",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Estado de Resultados
            ReportCard(
                title = "Estado de Resultados",
                icon = Icons.Default.Assessment,
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))

                ReportRow(
                    label = "Ventas",
                    value = currencyFormat.format(state.totalSales),
                    valueColor = MaterialTheme.colorScheme.primary,
                    icon = Icons.Default.Add,
                    footnote = "Suma de todas las ventas realizadas"
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                ReportRow(
                    label = "Costo",
                    value = currencyFormat.format(state.costOfSales),
                    valueColor = MaterialTheme.colorScheme.error,
                    icon = Icons.Default.Remove,
                    footnote = "Cantidad vendida × precio de compra de cada producto"
                )

                ReportRow(
                    label = "Gastos",
                    value = currencyFormat.format(state.totalExpenses),
                    valueColor = MaterialTheme.colorScheme.error,
                    icon = Icons.Default.Remove,
                    footnote = "Suma de todos los gastos"
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                val profitLabel = if (state.netProfit >= 0) "Utilidad" else "Pérdida"

                ReportRow(
                    label = profitLabel,
                    value = currencyFormat.format(state.netProfit),
                    valueColor = if (state.netProfit >= 0)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error,
                    icon = if (state.netProfit >= 0)
                        Icons.AutoMirrored.Filled.TrendingUp
                    else
                        Icons.AutoMirrored.Filled.TrendingDown,
                    isBold = true
                )
            }

            Text(
                text = "* Reportes calculados del mes actual",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }

    state.errorMessage?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = { Text("Error") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { viewModel.clearError() }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun ReportCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    containerColor: androidx.compose.ui.graphics.Color,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}

@Composable
fun ReportRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    isBold: Boolean = false,
    footnote: String? = null
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = valueColor
                    )
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
                )
            }

            Text(
                text = value,
                style = if (isBold) MaterialTheme.typography.titleLarge else MaterialTheme.typography.bodyLarge,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                color = valueColor
            )
        }
        if (!footnote.isNullOrBlank()) {
            Text(
                text = footnote,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = if (icon != null) 28.dp else 0.dp)
            )
        }
    }
}
