package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import com.ethshea.digits.UnitSystem
import java.math.BigDecimal
import java.math.BigInteger

/**
 * @author Ethan
 */

data class Quantity(val value:BigDecimal, val unit: NaturalUnit = UnitSystem.void) {

    /**
     * @requires this.unit.dimensionallyEqual(other.unit)
     */
    operator fun plus(other: Quantity) : Quantity {
        if (!unit.dimensionallyEqual(other.unit)) throw RuntimeException("Bad units") // Todo clean up this error handling
        return Quantity(value.plus(other.value.times(other.unit.factor / unit.factor)), unit)
    }

    /**
     * @requires this.unit.dimensionallyEqual(other.unit)
     */
    operator fun minus(other: Quantity) : Quantity {
        if (!unit.dimensionallyEqual(other.unit)) throw RuntimeException("Bad units")
        return Quantity(value.minus(other.value), unit)
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