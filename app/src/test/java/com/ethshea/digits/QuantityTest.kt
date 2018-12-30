package com.ethshea.digits

import com.ethshea.digits.evaluator.Quantity
import com.ethshea.digits.evaluator.SciNumber
import com.ethshea.digits.units.NaturalUnit
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Ethan
 */
class QuantityTest {
    val meters = NaturalUnit(mapOf("length" to 1))
    val kilometers = NaturalUnit(mapOf("length" to 1), Kilo)

    @Test
    fun plus() {
        assertEquals(q("456.123", kilometers), q("123", meters) + q("456.000", kilometers))
        assertEquals(q("123.456", kilometers),q("123.000", kilometers) + q("456", meters))
    }

    @Test
    fun minus() {
        assertEquals(q("-1", meters), q("4", meters) - q("5", meters))
        assertEquals(q("1", meters), q("5", meters) - q("4", meters))

        assertEquals(Quantity(SciNumber.Real(".999", sf(0)), kilometers), q("1", kilometers) - q("1", meters))
        assertEquals(Quantity(SciNumber.Real(".999", sf(1)), kilometers), q("1.0", kilometers) - q("1", meters))
        assertEquals(Quantity(SciNumber.Real("-999", sf(0)), meters), q("1", meters) - q("1", kilometers)) // Should only have one sigfig
    }

    @Test
    fun times() {
    }

    @Test
    fun div() {
    }
}