package com.monotonic.digits.human

import com.monotonic.digits.evaluator.SciNumber
import kotlin.math.max
import kotlin.math.min


data class HumanQuantity(val value: SciNumber, val unit: HumanUnit) {
    // TODO eventually make this accept a value for engineering or scientific mode
    fun humanString(separatorType: SeperatorType) : SigfigString {
        val valueString = value.valueString(separatorType)

        return SigfigString(valueString.string + unitString(), valueString.insigfigStart, valueString.string.length)
    }

    fun unitString() : String = unit.abbreviation

    /**
     * @param maxChars must be non-negative
     */
    fun humanString(separatorType: SeperatorType, maxChars: Int) : SigfigString {
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
            val magnitude = value.magnitude - 1
            val eNotation = if (magnitude == 0) "…" else "…ᴇ$magnitude"
            val normalizedValue = value / SciNumber.Real(10).pow(magnitude)
            val normalizedString = normalizedValue.valueString(separatorType)
            val sizedString = normalizedString.string.substring(0, max(0, min(normalizedString.string.length, maxValueChars - eNotation.length)))
                    .trimEnd { !(it.isDigit() || it == '.') } // Remove trailing non-numeric characters like separators

            val insigfigStartPos = min(normalizedString.insigfigStart, sizedString.length)
            val combinedString = sizedString + eNotation + unitString
            SigfigString(combinedString.substring(0, min(combinedString.length, maxChars)), insigfigStartPos, sizedString.length)
        }
    }

    override fun equals(other: Any?): Boolean = other is HumanQuantity && other.unit == unit && other.value.valueEqual(value)
}

enum class SeperatorType(val seperator: String) {
    NONE(""),
    SPACE("\u2009") // Thin space, according to Wikipedia it should be used as a thousand's separator https://en.wikipedia.org/wiki/Whitespace_character
}

data class SigfigString(val string: String, val insigfigStart: Int = string.length, val insigfigEnd: Int = string.length)