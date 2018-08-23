package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import com.ethshea.digits.SciNumber
import com.ethshea.digits.UnitSystem
import java.math.BigDecimal

/**
 * @author Ethan
 */

data class Quantity(val value:SciNumber, val unit: NaturalUnit = UnitSystem.void) {
    val normalizedValue = value.times(unit.factor)

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

    override fun equals(other: Any?): Boolean =
        other is Quantity
                && unit.dimensionallyEqual(other.unit)
                && normalizedValue == other.normalizedValue

    override fun hashCode(): Int = value.hashCode() xor unit.hashCode()

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