package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import java.math.BigDecimal
import java.math.BigInteger

/**
 * @author Ethan
 */

data class Quantity(val value:BigDecimal, val unit: NaturalUnit? = null) {
    operator fun plus(other: Quantity) : Quantity {
        if (other.unit != unit) throw RuntimeException("Bad units") // Todo clean up this error handling
        return Quantity(value.plus(other.value), unit)
    }

    operator fun minus(other: Quantity) : Quantity {
        if (other.unit != unit) throw RuntimeException("Bad units")
        return Quantity(value.minus(other.value), unit)
    }

    operator fun times(other: Quantity) : Quantity {
        if ((other.unit == null) && (unit == null)) {
            return Quantity(value.times(other.value), null)
        } else if ((other.unit != null) && (unit != null)) {
            return Quantity(value.times(other.value), unit + other.unit)
        } else {
            throw RuntimeException("Bad units")
        }
    }

    operator fun div(other: Quantity) : Quantity {
        if ((other.unit == null) && (unit == null)) {
            return Quantity(value.div(other.value), null)
        } else if ((other.unit != null) && (unit != null)) {
            return Quantity(value.div(other.value), unit - other.unit)
        } else {
            throw RuntimeException("Bad units")
        }
    }
}