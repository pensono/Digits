package com.monotonic.digits.human

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber
import kotlin.math.max
import kotlin.math.min


data class HumanQuantity(val value: SciNumber, val unit: HumanUnit) {
    // TODO eventually make this accept a value for engineering or scientific mode
    fun humanString(seperatorType: SeperatorType) : HumanQuantityString {
        val valueString = value.valueString(seperatorType)
        val insigfigStartPos = sigfigEndPos(valueString, value.precision)

        return HumanQuantityString(valueString + unitString(), insigfigStartPos, valueString.length)
    }

    fun unitString() : String = unit.abbreviation

    /**
     * @param maxChars must be non-negative
     */
    fun humanString(seperatorType: SeperatorType, maxChars: Int) : HumanQuantityString {
        if (maxChars == 0) {
            return HumanQuantityString("", 0, 0)
        }

        val unitString =
                if (unitString().length > maxChars)
                    ""
                else
                    unitString()
        val maxValueChars = maxChars - unitString.length

        val regularString = value.valueString(seperatorType)

        return if (regularString.length <= maxValueChars) {
            val insigfigStartPos = sigfigEndPos(regularString, value.precision)
            HumanQuantityString(regularString + unitString, insigfigStartPos, regularString.length)
        } else {
            val magnitude = value.magnitude - 1
            val eNotation = if (magnitude == 0) "…" else "…ᴇ$magnitude"
            val normalizedValue = value / SciNumber.Real(10).pow(magnitude)
            val normalizedString = normalizedValue.valueString(seperatorType)
            val sizedString = normalizedString.substring(0, max(0, min(normalizedString.length, maxValueChars - eNotation.length)))
                    .trimEnd { !(it.isDigit() || it == '.') } // Remove trailing non-numeric characters like separators

            val insigfigStartPos = sigfigEndPos(sizedString, value.precision)
            val combinedString = sizedString + eNotation + unitString
            HumanQuantityString(combinedString.substring(0, min(combinedString.length, maxChars)), insigfigStartPos, sizedString.length)
        }
    }

    /**
     * @param valueString a string containing only digits and one decimal
     */
    private fun sigfigEndPos(valueString: String, precision: Precision): Int {
        val sigfigStartPos = valueString.length - valueString.trimStart('0', '.', '-').length

        val insigfigStartPos = when (precision) {
            is Precision.Infinite -> valueString.length
            is Precision.SigFigs -> {
                val lastPosition = valueString.length
                val decimalAdjustment =
                        if (valueString.substring(sigfigStartPos, Math.min(precision.amount + sigfigStartPos, lastPosition))
                                        .contains('.'))
                            1
                        else
                            0

                Integer.min(precision.amount + sigfigStartPos + decimalAdjustment, lastPosition)
            }
        }
        return insigfigStartPos
    }

    override fun equals(other: Any?): Boolean = other is HumanQuantity && other.unit == unit && other.value.valueEqual(value)
}

enum class SeperatorType(val seperator: String) {
    NONE(""),
    SPACE("\u2009") // Thin space, according to Wikipedia it should be used as a thousand's separator https://en.wikipedia.org/wiki/Whitespace_character
}

data class HumanQuantityString(val string: String, val insigfigStart: Int, val insigfigEnd: Int)