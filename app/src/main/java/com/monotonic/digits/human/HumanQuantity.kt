package com.monotonic.digits.human

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min


data class HumanQuantity(val value: SciNumber, val unit: HumanUnit = HumanUnit.Void) {
    // TODO eventually make this accept a value for engineering or scientific mode
    fun humanString(separatorType: SeparatorType) : SigfigString {
        val valueString = value.valueString(separatorType)

        return SigfigString(valueString.string + unitString(), valueString.insigfigStart, valueString.string.length)
    }

    fun unitString() : String = unit.abbreviation

    /**
     * @param maxChars must be non-negative
     */
    fun humanString(maxChars: Int, separatorType: SeparatorType, numberFormat: NumberFormat) : SigfigString {
        return humanString(maxChars, separatorType, numberFormat, true)
    }

    private fun humanString(maxChars: Int, separatorType: SeparatorType, numberFormat: NumberFormat, includeEllipsis: Boolean): SigfigString {
        if (maxChars == 0) {
            return SigfigString("", 0, 0)
        }

        val unitString =
                if (unitString().length > maxChars)
                    ""
                else
                    unitString()
        val maxValueChars = maxChars - unitString.length

        val regularString = value.valueString(separatorType)

        return if (regularString.string.length <= maxValueChars) {
            SigfigString(regularString.string + unitString, regularString.insigfigStart, regularString.insigfigEnd)
        } else {
            val ellipsis = if (includeEllipsis) "…" else ""
            val rawMagnitude = value.magnitude - 1.0
            val roundedMagnitude = floor(rawMagnitude / numberFormat.exponentGranularity).toInt() * numberFormat.exponentGranularity
            val exponent = if (abs(roundedMagnitude) >= numberFormat.minimumExponent) roundedMagnitude else 0  // Could this be made into a setting?
            val eNotation = if (exponent == 0) ellipsis else "${ellipsis}ᴇ$exponent"
            val normalizedValue = value / SciNumber.Real(10).pow(exponent)
            val normalizedString = normalizedValue.valueString(separatorType)
            val sizedString = normalizedString.string.substring(0, max(0, min(normalizedString.string.length, maxValueChars - eNotation.length)))
                    .trimEnd { !(it.isDigit() || it == '.') } // Remove trailing non-numeric characters like separators

            val insigfigStartPos = min(normalizedString.insigfigStart, sizedString.length)
            val combinedString = sizedString + eNotation + unitString
            SigfigString(combinedString.substring(0, min(combinedString.length, maxChars)), insigfigStartPos, sizedString.length)
        }
    }

    override fun equals(other: Any?): Boolean = other is HumanQuantity && other.unit == unit && other.value.valueEqual(value)

    /***
     * A string which represents the value of the quantity. It should be parseable and yield the same
     * quantity. Numbers with infinite precision will be capped at 12 digits
     */
    fun valueString(round: RoundingMode): String {
        // Abort if the number is huge
        if (value.magnitude == Int.MIN_VALUE) {
            return "Overflow"
        }

        // TODO refactor this so it makes more sense
        val precision = value.precision
        val figures = when(precision) {
            is Precision.Infinite -> 12
            is Precision.SigFigs -> precision.amount
        }

        val baseString = value.valueString(SeparatorType.NONE)
        val decimalLocation = baseString.string.indexOf('.')

        val sized = if (baseString.insigfigStart >= decimalLocation && decimalLocation != -1) {
            when (round) {
                RoundingMode.REMOVE_TRAILING -> baseString.string.trimEnd('0')
                RoundingMode.SIGFIG -> {
                    val paddingSize =
                            if (baseString.string[0] == '0')
                                "0.".length + baseString.string.substring(decimalLocation + 1).indexOfFirst { c -> c != '0' }
                            else
                                ".".length
                    val figuresInBaseString = baseString.string.length - paddingSize
                    if (figures > figuresInBaseString) { // Needs more
                        round(baseString.string, baseString.insigfigStart) + "0".repeat(figures - figuresInBaseString)
                    } else { // Needs fewer
                        round(baseString.string, figures + paddingSize).trimEnd('.')
                    }
                }
            }
        } else {
            when (round) {
                RoundingMode.REMOVE_TRAILING -> baseString.string
                RoundingMode.SIGFIG -> {
                    val removedInsigfigs = round(baseString.string, baseString.insigfigStart) + "0".repeat(baseString.string.length - baseString.insigfigStart)
                    // potentially add more digits
                    if (figures - removedInsigfigs.length > 0) {
                        removedInsigfigs + "." + "0".repeat(figures - removedInsigfigs.length)
                    } else {
                        removedInsigfigs
                    }
                }
            }
        }

        val maxLength = 12 + when {
            !sized.contains('.') -> 0
            sized[0] == '0' -> 2  // 0.XXX
            else -> 1
        }
        return if (sized.length > maxLength) {
            // Duplicated with humanString :(
            val rawMagnitude = value.magnitude - 1.0
            val roundedMagnitude = Math.floor(rawMagnitude).toInt()
            if (roundedMagnitude == 0) {
                sized.substring(0, maxLength) + unitString()
            } else {
                val normalizedValue = value / SciNumber.Real(10).pow(roundedMagnitude)
                HumanQuantity(normalizedValue, HumanUnit(mapOf())).valueString(round) + "ᴇ$roundedMagnitude" + unitString()
            }
        } else {
            sized + unitString()
        }
    }
}

/**
 * @param index The first index which shouldn't be rounded
 */
private fun round(input: String, index: Int) : String {
    val trimmed = input.substring(0, index)
    val decimalPosition = trimmed.indexOf('.')

    var result = trimmed.replace(".", "").toCharArray().toMutableList()
    if (index < input.length && input[index] >= '5') {
        var currentPos = result.size - 1
        while (result[currentPos] == '9') {
            result[currentPos] = '0'
            currentPos--
        }
        result[currentPos]++
    }

    if (decimalPosition != -1) {
        result.add(decimalPosition, '.')
    }

    return String(result.toCharArray())
}

enum class RoundingMode {
    /**
     * Round to the last significant figure
     */
    SIGFIG,

    /**
     * Remove all trailing zeroes and let the word have a max length of 12
     */
    REMOVE_TRAILING
}

enum class SeparatorType(val separator: String) {
    NONE(""),
    SPACE("\u2009") // Thin space, according to Wikipedia it should be used as a thousand's separator https://en.wikipedia.org/wiki/Whitespace_character
}

data class SigfigString(val string: String, val insigfigStart: Int = string.length, val insigfigEnd: Int = string.length)