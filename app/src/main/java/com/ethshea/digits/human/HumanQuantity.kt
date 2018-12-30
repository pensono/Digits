package com.ethshea.digits.human

import com.ethshea.digits.evaluator.Precision
import com.ethshea.digits.evaluator.SciNumber
import kotlin.math.min


data class HumanQuantity(val value: SciNumber, val unit: HumanUnit) {
    // TODO eventually make this accept a value for engineering or scientific mode
    fun humanString() : HumanQuantityString {
        val valueString = value.valueString()
        val insigfigStartPos = sigfigEndPos(valueString, value.precision)

        return HumanQuantityString(valueString + unit.abbreviation, insigfigStartPos, valueString.length)
    }

    /**
     * @param maxChars must be non-negative
     */
    fun humanString(maxChars: Int) : HumanQuantityString {
        val unitString =
                if (unit.abbreviation.length > maxChars)
                    ""
                else
                    unit.abbreviation
        val maxValueChars = maxChars - unitString.length

        val regularString = value.valueString()

        return if (regularString.length <= maxValueChars) {
            val insigfigStartPos = sigfigEndPos(regularString, value.precision)
            HumanQuantityString(regularString + unitString, insigfigStartPos, regularString.length)
        } else {
            val magnitude = value.magnitude - 1
            val eNotation = if (magnitude == 0) "…" else "…ᴇ$magnitude"
            val normalizedValue = value / SciNumber(10).pow(magnitude)
            val normalizedString = normalizedValue.valueString()
            val sizedString = normalizedString.substring(0, min(normalizedString.length, maxValueChars - eNotation.length))

            val insigfigStartPos = sigfigEndPos(sizedString, value.precision)
            HumanQuantityString(sizedString + eNotation + unitString, insigfigStartPos, sizedString.length)
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
}

data class HumanQuantityString(val string: String, val insigfigStart: Int, val insigfigEnd: Int)