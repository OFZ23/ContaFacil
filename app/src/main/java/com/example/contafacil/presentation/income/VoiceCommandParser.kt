package com.example.contafacil.presentation.income

import com.example.contafacil.data.local.entity.PaymentMethod
import java.text.Normalizer

data class ParsedSaleCommand(
    val quantity: Int?,
    val productName: String?,
    val paymentMethod: PaymentMethod?
)

object VoiceCommandParser {
    private val paymentKeywords = listOf(
        "en efectivo" to PaymentMethod.EFECTIVO,
        "efectivo" to PaymentMethod.EFECTIVO,
        "transferencia" to PaymentMethod.TRANSFERENCIA,
        "tarjeta" to PaymentMethod.TARJETA,
        "credito" to PaymentMethod.CREDITO,
        "a credito" to PaymentMethod.CREDITO
    )

    fun parseSaleCommand(rawInput: String): ParsedSaleCommand {
        val normalized = normalize(rawInput)
        val quantity = Regex("\\b(\\d+)\\b").find(normalized)?.groupValues?.get(1)?.toIntOrNull()
        val paymentMethod = paymentKeywords.firstOrNull { normalized.contains(it.first) }?.second

        val productName = extractProductName(normalized, quantity)
            ?.takeIf { it.isNotBlank() }

        return ParsedSaleCommand(
            quantity = quantity,
            productName = productName,
            paymentMethod = paymentMethod
        )
    }

    private fun extractProductName(normalizedInput: String, quantity: Int?): String? {
        if (quantity == null) return null

        val numberMatch = Regex("\\b$quantity\\b").find(normalizedInput) ?: return null
        val startIndex = numberMatch.range.last + 1

        val paymentStart = paymentKeywords
            .map { normalizedInput.indexOf(it.first) }
            .filter { it >= startIndex }
            .minOrNull() ?: normalizedInput.length

        val rawProduct = normalizedInput.substring(startIndex, paymentStart)
            .replace(Regex("\\b(vendi|vendo|vendio|vendimos|de|unidades|unidad)\\b"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

        return rawProduct
    }

    private fun normalize(text: String): String {
        val withoutAccents = Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        return withoutAccents.lowercase().trim()
    }
}

