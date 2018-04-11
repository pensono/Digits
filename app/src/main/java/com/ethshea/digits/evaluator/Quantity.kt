package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import com.ethshea.digits.UnitSystem
import java.math.BigDecimal

/**
 * @author Ethan
 */

data class Quantity(val value:BigDecimal, val unit: NaturalUnit = UnitSystem.void) {

    /**
     * @requires this.unit.dimensionallyEqual(other.unit)
     */
    operator fun plus(other: Quantity) : Quantity {
        if (!unit.dimensionallyEqual(other.unit)) throw RuntimeException("Bad units")
        return doSum(this, other, BigDecimal::plus)
    }

    /**
     * @requires this.unit.dimensionallyEqual(other.unit)
     */
    operator fun minus(other: Quantity) : Quantity {
        if (!unit.dimensionallyEqual(other.unit)) throw RuntimeException("Bad units")
        return doSum(this, other, BigDecimal::minus)
    }

    operator fun times(other: Quantity) : Quantity {
        return Quantity(value.times(other.value), unit + other.unit)
    }

    operator fun div(other: Quantity) : Quantity {
        return Quantity(value.divide(other.value), unit - other.unit)
    }
}

private fun doSum(a: Quantity, b: Quantity, operation: (BigDecimal, BigDecimal) -> BigDecimal): Quantity {
    return if (a.unit.factor > b.unit.factor) {
        Quantity(operation.invoke(a.value, b.value.times(b.unit.factor.divide(a.unit.factor))), a.unit)
    } else {
        Quantity(operation.invoke(b.value, a.value.times(a.unit.factor.divide(b.unit.factor))), b.unit)
    }
}