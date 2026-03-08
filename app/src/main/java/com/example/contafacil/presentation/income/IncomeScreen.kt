package com.example.contafacil.presentation.income

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.contafacil.data.local.AppDatabase
import com.example.contafacil.data.local.entity.PaymentMethod
import com.example.contafacil.data.local.entity.TransactionEntity
import com.example.contafacil.data.repository.ProductRepository
import com.example.contafacil.data.repository.TransactionRepository
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat

private data class SaleDraft(
    val productName: String = "",
    val quantity: Int? = null,
    val paymentMethod: PaymentMethod = PaymentMethod.EFECTIVO
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val viewModel = remember {
        IncomeViewModel(
            TransactionRepository(database.transactionDao()),
            ProductRepository(database.productDao())
        )
    }

    val state by viewModel.state.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var saleDraft by remember { mutableStateOf(SaleDraft()) }
    var lastRecognizedText by remember { mutableStateOf<String?>(null) }
    var lastParsedSummary by remember { mutableStateOf<String?>(null) }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult

        val spokenText = result.data
            ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            ?.firstOrNull()
            .orEmpty()

        lastRecognizedText = spokenText

        if (spokenText.isBlank()) {
            lastParsedSummary = "No se detecto texto"
            Toast.makeText(context, "No se detecto texto de voz", Toast.LENGTH_SHORT).show()
            return@rememberLauncherForActivityResult
        }

        val parsed = VoiceCommandParser.parseSaleCommand(spokenText)
        if (parsed.quantity == null || parsed.productName.isNullOrBlank()) {
            lastParsedSummary = "No se pudo interpretar producto y cantidad"
            Toast.makeText(
                context,
                "No pude extraer producto/cantidad. Intenta: 'Vendi 3 cafe en efectivo'",
                Toast.LENGTH_LONG
            ).show()
            return@rememberLauncherForActivityResult
        }

        val method = parsed.paymentMethod ?: PaymentMethod.EFECTIVO
        lastParsedSummary = "Producto: ${parsed.productName}, Cantidad: ${parsed.quantity}, Pago: ${method.name}"

        saleDraft = SaleDraft(
            productName = parsed.productName,
            quantity = parsed.quantity,
            paymentMethod = parsed.paymentMethod ?: PaymentMethod.EFECTIVO
        )
        showAddDialog = true
        Toast.makeText(context, "Datos de voz cargados en el formulario", Toast.LENGTH_SHORT).show()
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(context, "Permiso de microfono denegado", Toast.LENGTH_SHORT).show()
            return@rememberLauncherForActivityResult
        }

        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-CO")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Di: 'Vendi 3 cafe en efectivo'")
        }

        try {
            speechLauncher.launch(speechIntent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(context, "Reconocimiento de voz no disponible", Toast.LENGTH_SHORT).show()
        }
    }

    val startVoiceCapture = {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-CO")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Di: 'Vendi 3 cafe en efectivo'")
            }
            try {
                speechLauncher.launch(speechIntent)
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(context, "Reconocimiento de voz no disponible", Toast.LENGTH_SHORT).show()
            }
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ingresos / Ventas") },
                actions = {
                    IconButton(onClick = startVoiceCapture) {
                        Icon(Icons.Default.Mic, contentDescription = "Registrar por voz")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                saleDraft = SaleDraft()
                showAddDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar venta")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (!lastRecognizedText.isNullOrBlank()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Texto reconocido:",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = lastRecognizedText.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (!lastParsedSummary.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = lastParsedSummary.orEmpty(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }

            if (state.transactions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay ventas registradas",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.transactions) { transaction ->
                        TransactionCard(
                            transaction = transaction,
                            onDelete = { viewModel.deleteTransaction(transaction) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddSaleDialog(
            initialProductName = saleDraft.productName,
            initialQuantity = saleDraft.quantity,
            initialPaymentMethod = saleDraft.paymentMethod,
            onDismiss = { showAddDialog = false },
            onConfirm = { productName, quantity, price, paymentMethod, notes ->
                viewModel.addSale(productName, quantity, price, paymentMethod, notes)
                showAddDialog = false
            }
        )
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
fun TransactionCard(
    transaction: TransactionEntity,
    onDelete: () -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "CO"))
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.productName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cantidad: ${transaction.quantity}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Precio unit: ${currencyFormat.format(transaction.unitPrice)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Total: ${currencyFormat.format(transaction.totalAmount)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when (transaction.paymentMethod) {
                            PaymentMethod.EFECTIVO -> Icons.Default.TrendingUp
                            PaymentMethod.TRANSFERENCIA -> Icons.Default.Assessment
                            PaymentMethod.TARJETA -> Icons.Default.ShoppingCart
                            PaymentMethod.CREDITO -> Icons.Default.MoneyOff
                        },
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = transaction.paymentMethod.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    if (!transaction.isPaid) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• PENDIENTE",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = dateFormat.format(Date(transaction.date)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSaleDialog(
    initialProductName: String = "",
    initialQuantity: Int? = null,
    initialPaymentMethod: PaymentMethod = PaymentMethod.EFECTIVO,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Double, PaymentMethod, String?) -> Unit
) {
    var productName by remember(initialProductName) { mutableStateOf(initialProductName) }
    var quantity by remember(initialQuantity) { mutableStateOf(initialQuantity?.toString().orEmpty()) }
    var price by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember(initialPaymentMethod) { mutableStateOf(initialPaymentMethod) }
    var notes by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Venta") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Producto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it.filter { char -> char.isDigit() } },
                    label = { Text("Cantidad") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.filter { char -> char.isDigit() || char == '.' } },
                    label = { Text("Precio unitario") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedPaymentMethod.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Forma de pago") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        PaymentMethod.entries.forEach { method ->
                            DropdownMenuItem(
                                text = { Text(method.name) },
                                onClick = {
                                    selectedPaymentMethod = method
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notas (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val qty = quantity.toIntOrNull() ?: 0
                    val prc = price.toDoubleOrNull() ?: 0.0
                    if (productName.isNotBlank() && qty > 0 && prc > 0) {
                        onConfirm(
                            productName,
                            qty,
                            prc,
                            selectedPaymentMethod,
                            notes.ifBlank { null }
                        )
                    }
                },
                enabled = productName.isNotBlank() &&
                         quantity.toIntOrNull() != null && quantity.toIntOrNull()!! > 0 &&
                         price.toDoubleOrNull() != null && price.toDoubleOrNull()!! > 0
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
