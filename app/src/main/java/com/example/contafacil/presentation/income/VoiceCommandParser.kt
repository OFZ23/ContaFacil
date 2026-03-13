package com.example.contafacil.presentation.income

import com.example.contafacil.data.local.entity.PaymentMethod
import java.text.Normalizer

data class ParsedSaleCommand(
    val quantity: Int?,
    val productName: String?,
    val unitPrice: Double?,
    val paymentMethod: PaymentMethod
)

/**
 * Formato esperado: "Vendí [cantidad] [producto] a [precio unitario]"
 * Ejemplo: "Vendí 3 café volcán a 5000"
 *
 * La forma de pago siempre será EFECTIVO por defecto.
 * Si el usuario necesita otro método de pago, debe cambiarlo manualmente en el formulario.
 */
object VoiceCommandParser {

    private val simpleNumbers = mapOf(
        "cero" to 0,
        "un" to 1,
        "uno" to 1,
        "una" to 1,
        "dos" to 2,
        "tres" to 3,
        "cuatro" to 4,
        "cinco" to 5,
        "seis" to 6,
        "siete" to 7,
        "ocho" to 8,
        "nueve" to 9,
        "diez" to 10,
        "once" to 11,
        "doce" to 12,
        "trece" to 13,
        "catorce" to 14,
        "quince" to 15,
        "dieciseis" to 16,
        "diecisiete" to 17,
        "dieciocho" to 18,
        "diecinueve" to 19,
        "veinte" to 20,
        "veintiuno" to 21,
        "veintidos" to 22,
        "veintitres" to 23,
        "veinticuatro" to 24,
        "veinticinco" to 25,
        "veintiseis" to 26,
        "veintisiete" to 27,
        "veintiocho" to 28,
        "veintinueve" to 29
    )

    private val tensNumbers = mapOf(
        "treinta" to 30,
        "cuarenta" to 40,
        "cincuenta" to 50,
        "sesenta" to 60,
        "setenta" to 70,
        "ochenta" to 80,
        "noventa" to 90
    )

    fun parseSaleCommand(rawInput: String): ParsedSaleCommand {
        val normalized = normalize(rawInput)
        val cleaned = fixCommonSpeechTypos(normalized)

        // Formato objetivo: "vendi cantidad producto a precio"
        val mainPattern = Regex("""(?:vendi|vendo|vendio|vendimos|venti)?\s*(.+?)\s+a\s+(\d[\d.,]*)$""")
        val match = mainPattern.find(cleaned)

        if (match != null) {
            val leftPart = match.groupValues[1].trim()
            val unitPrice = parsePrice(match.groupValues[2])
            val (quantity, productName) = extractQuantityAndProduct(leftPart)

            return ParsedSaleCommand(
                quantity = quantity,
                productName = productName,
                unitPrice = unitPrice,
                paymentMethod = PaymentMethod.EFECTIVO
            )
        }

        // Fallback sin precio al final
        val fallback = cleaned.replace(Regex("""^(vendi|vendo|vendio|vendimos|venti)\s+"""), "")
        val (quantity, productName) = extractQuantityAndProduct(fallback)

        return ParsedSaleCommand(
            quantity = quantity,
            productName = productName,
            unitPrice = null,
            paymentMethod = PaymentMethod.EFECTIVO
        )
    }

    /**
     * Convierte precios con separadores de miles/decimales en formato flexible.
     * Ejemplos: 31460, 31.460, 31,460 -> 31460 ; 31,46 / 31.46 -> 31.46
     */
    private fun parsePrice(raw: String): Double? {
        val value = raw.trim()
        if (value.isBlank()) return null

        val hasDot = value.contains('.')
        val hasComma = value.contains(',')

        val normalized = when {
            hasDot && hasComma -> {
                val lastDot = value.lastIndexOf('.')
                val lastComma = value.lastIndexOf(',')
                if (lastComma > lastDot) {
                    // 1.234,56 -> decimal ','
                    value.replace(".", "").replace(",", ".")
                } else {
                    // 1,234.56 -> decimal '.'
                    value.replace(",", "")
                }
            }

            hasComma -> {
                val parts = value.split(',')
                if (parts.size == 2 && parts[1].length == 3) {
                    // 31,460 -> separador de miles
                    value.replace(",", "")
                } else {
                    // 31,46 -> decimal
                    value.replace(",", ".")
                }
            }

            hasDot -> {
                val parts = value.split('.')
                if (parts.size == 2 && parts[1].length == 3) {
                    // 31.460 -> separador de miles
                    value.replace(".", "")
                } else {
                    // 31.46 -> decimal
                    value
                }
            }

            else -> value
        }

        return normalized.toDoubleOrNull()
    }

    private fun extractQuantityAndProduct(text: String): Pair<Int?, String?> {
        val tokens = text.split(Regex("""\s+""")).filter { it.isNotBlank() }
        if (tokens.isEmpty()) return null to null

        for (count in 3 downTo 1) {
            if (tokens.size < count) continue
            val quantityPhrase = tokens.take(count).joinToString(" ")
            val quantity = parseSpanishNumberPhrase(quantityPhrase)
            if (quantity != null) {
                val product = tokens.drop(count).joinToString(" ")
                    .replace(Regex("""\b(de|unidades|unidad)\b"""), " ")
                    .replace(Regex("""\s+"""), " ")
                    .trim()
                    .ifBlank { null }
                return quantity to product
            }
        }

        val numeric = tokens.firstOrNull()?.toIntOrNull()
        if (numeric != null) {
            val product = tokens.drop(1).joinToString(" ")
                .replace(Regex("""\b(de|unidades|unidad)\b"""), " ")
                .replace(Regex("""\s+"""), " ")
                .trim()
                .ifBlank { null }
            return numeric to product
        }

        return null to null
    }

    private fun parseSpanishNumberPhrase(rawPhrase: String): Int? {
        val phrase = rawPhrase.trim().replace(Regex("""\s+"""), " ")
        phrase.toIntOrNull()?.let { return it }

        simpleNumbers[phrase]?.let { return it }

        if (phrase.startsWith("veinti ")) {
            val unitWord = phrase.removePrefix("veinti ").trim()
            val unit = simpleNumbers[unitWord]
            if (unit != null && unit in 1..9) return 20 + unit
        }

        val parts = phrase.split(" ")
        if (parts.size == 3 && parts[1] == "y") {
            val tens = tensNumbers[parts[0]]
            val unit = simpleNumbers[parts[2]]
            if (tens != null && unit != null && unit in 1..9) return tens + unit
        }

        if (parts.size == 2) {
            val tens = tensNumbers[parts[0]]
            val unit = simpleNumbers[parts[1]]
            if (tens != null && unit != null && unit in 1..9) return tens + unit
        }

        return null
    }

    private fun fixCommonSpeechTypos(text: String): String {
        return text
            .replace(Regex("""\bventi\b"""), "vendi")
            .replace(Regex("""\bveinti\b"""), "veinti")
            .replace(Regex("""\s+"""), " ")
            .trim()
    }

    private fun normalize(text: String): String {
        val withoutAccents = Normalizer.normalize(text, Normalizer.Form.NFD)
            .replace(Regex("""\p{InCombiningDiacriticalMarks}+"""), "")
        return withoutAccents
            .lowercase()
            .replace(Regex("""[^a-z0-9.,\s]"""), " ")
            .replace(Regex("""\s+"""), " ")
            .trim()
    }
}

