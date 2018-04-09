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
        if (!unit.dimensionallyEqual(other.unit)) throw RuntimeException("Bad units") // Todo clean up this error handling
        return doSum(this, other, BigDecimal::plus)
    }

    /**
     * @requires this.unit.dimensionallyEqual(other.unit)
     */
    operator fun minus(other: Quantity) : Quantity {
        if (!unit.dimensionallyEqual(other.unit)) throw RuntimeException("Bad units")
        return doSum(this, other, BigDecimal::minus)
    }

    private fun doSum(a: Quantity, b: Quantity, operation: (BigDecimal, BigDecimal) -> BigDecimal): Quantity {
        return if (b.unit.factor < unit.factor) {
            Quantity(operation.invoke(value, b.value.times(b.unit.factor.divide(unit.factor))), unit)
        } else {
            Quantity(operation.invoke(value, value.times(unit.factor.divide(b.unit.factor))), b.unit)
        }
    }

    operator fun times(other: Quantity) : Quantity {
        return Quantity(value.times(other.value), unit + other.unit)
    }

    operator fun div(other: Quantity) : Quantity {
        return Quantity(value.div(other.value), unit - other.unit)
    }

//    override fun equals(other: Any?): Boolean {
//        return super.equals(other)
//    }

    override fun toString(): String { // Temporary. Should be replaced by actual logic which converts this to something human readable
        return value.toString() + " " + unit.toString()
    }
}