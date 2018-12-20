package com.ethshea.digits.evaluator

import com.ethshea.digits.units.NaturalUnit
import com.ethshea.digits.SciNumber
import com.ethshea.digits.units.HumanUnit
import com.ethshea.digits.units.UnitSystem
import java.lang.Integer.min

/**
 * @author Ethan
 */

class Quantity(val value:SciNumber, val unit: NaturalUnit = UnitSystem.void) {
    val normalizedValue = value * unit.factor

    /**
     * @requires this.unit.dimensionallyEqual(other.unit)
     */
    operator fun plus(other: Quantity) : Quantity {
        if (!unit.dimensionallyEqual(other.unit)) throw RuntimeException("Bad units")
        return doSum(this, other, SciNumber::plus)
    }

    /**
     * @requires this.unit.dimensionallyEqual(other.unit)
     */
    operator fun minus(other: Quantity) : Quantity {
        if (!unit.dimensionallyEqual(other.unit)) throw RuntimeException("Bad units")
        return doSum(this, other, SciNumber::minus)
    }

    operator fun unaryMinus() : Quantity = Quantity(-value, unit)

    operator fun times(other: Quantity) : Quantity {
        return Quantity(value.times(other.value), unit + other.unit)
    }

    operator fun div(other: Quantity) : Quantity {
        return Quantity(value / other.value, unit - other.unit)
    }

    fun pow(exponent: Int) : Quantity {
        return Quantity(value.pow(exponent), unit.times(exponent))
    }

    // Really not sure what to do with units on this one
    fun sin() = Quantity(value.sin(), unit)
    fun cos() = Quantity(value.cos(), unit)
    fun tan() = Quantity(value.tan(), unit)
    fun sinh() = Quantity(value.sinh(), unit)
    fun cosh() = Quantity(value.cosh(), unit)
    fun tanh() = Quantity(value.tanh(), unit)
    fun asin() = Quantity(value.asin(), unit)
    fun acos() = Quantity(value.acos(), unit)
    fun atan() = Quantity(value.atan(), unit)

    override fun equals(other: Any?): Boolean =
        other is Quantity
                && unit.dimensionallyEqual(other.unit)
                && normalizedValue == other.normalizedValue

    override fun hashCode(): Int = value.hashCode() xor unit.hashCode()

    override fun toString(): String = value.toString() + " " + unit.toString()

    companion object {
        val One = Quantity(SciNumber.One)
        val Zero = Quantity(SciNumber.Zero)
    }
}

data class HumanQuantity(val value:SciNumber, val unit: HumanUnit) {
    fun humanString() = humanValueString() + humanUnitString()
    fun humanValueString() = value.valueString()
    fun humanUnitString() = unit.abbreviation

    /**
     * @param maxChars must be non-negative
     */
    fun humanString(maxChars: Int) : String {
        val unitString = humanUnitString()
        return if (unitString.length <= maxChars) {
            // Not enough room for even the units, just show the value
            humanValueString(maxChars)
        } else {
            humanValueString(maxChars - unitString.length) + unitString
        }
    }

    /***
     * @param maxChars must be non-negative
     */
    fun humanValueString(maxChars: Int) : String {
        val regularString = humanValueString()
        if (regularString.length <= maxChars) {
            return regularString
        }

        val magnitude = value.magnitude - 1
        val eNotation = "…E" + magnitude.toString()
        val normalizedValue = value / SciNumber(10).pow(magnitude)
        val normalizedString = normalizedValue.toString()

        return if (eNotation.length > maxChars) {
            normalizedString.substring(0, maxChars)
        } else {
            normalizedString.substring(0, min(normalizedString.length, maxChars - eNotation.length)) + eNotation
        }
    }
}

private fun doSum(a: Quantity, b: Quantity, operation: (SciNumber, SciNumber) -> SciNumber): Quantity {
    return if (a.unit.factor > b.unit.factor) {
        Quantity(operation(a.value, b.value * (b.unit.factor / a.unit.factor)), a.unit)
    } else {
        Quantity(operation(a.value * (a.unit.factor / b.unit.factor), b.value), b.unit)
    }
}