package com.monotonic.digits.evaluator

import com.monotonic.digits.units.NaturalUnit
import com.monotonic.digits.units.UnitSystem
import java.math.BigDecimal

/**
 * @author Ethan
 */

class Quantity(val value: SciNumber, val unit: NaturalUnit = UnitSystem.void) {
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

    fun sqrt() = Quantity(value.sqrt(), unit.half())

    // What to put for unit here? Need logarithmic units
    fun log(base: BigDecimal) = Quantity(value.log(base), UnitSystem.void)
    fun exp() = Quantity(value.exp(), UnitSystem.void)

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
    fun sec() = Quantity(value.sec(), unit)
    fun csc() = Quantity(value.csc(), unit)
    fun cot() = Quantity(value.cot(), unit)

    // Designed for use in nonlinear operations
    fun normalized() = Quantity(value * unit.factor, NaturalUnit(unit.dimensions))

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

private fun doSum(a: Quantity, b: Quantity, operation: (SciNumber, SciNumber) -> SciNumber): Quantity {
    return if (a.unit.factor > b.unit.factor) {
        Quantity(operation(a.value, b.value * (b.unit.factor / a.unit.factor)), a.unit)
    } else {
        Quantity(operation(a.value * (a.unit.factor / b.unit.factor), b.value), b.unit)
    }
}