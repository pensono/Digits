package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import com.ethshea.digits.SciNumber
import com.ethshea.digits.UnitSystem
import org.junit.Test

import org.junit.Assert.*
import java.math.BigDecimal

/**
 * @author Ethan
 */
class QuantityTest {
    val meters = NaturalUnit(mapOf("length" to 1))
    val kilometers = NaturalUnit(mapOf("length" to 1), SciNumber.Kilo)

    @Test
    fun plus() {
        assertEquals(q("456.123", kilometers), q("123", meters) + q("456", kilometers))
        assertEquals(q("123.456", kilometers),q("123", kilometers) + q("456", meters))
    }

    @Test
    fun minus() {
        assertEquals(q("-1", meters), q("4", meters) - q("5", meters))
        assertEquals(q("1", meters), q("5", meters) - q("4", meters))

        assertEquals(q("999", meters), q("1", kilometers) - q("1", meters))
        assertEquals(q("-999", meters), q("1", meters) - q("1", kilometers))
    }

    @Test
    fun times() {
    }

    @Test
    fun div() {
    }

    fun q(value: String, unit: NaturalUnit) = Quantity(SciNumber(value), unit)
}