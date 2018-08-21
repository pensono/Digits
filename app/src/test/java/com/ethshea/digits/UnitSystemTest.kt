package com.ethshea.digits

import org.junit.Test

import org.junit.Assert.*
import java.math.BigDecimal
import java.math.MathContext

/**
 * @author Ethan
 */
class UnitSystemTest {
    @Test
    fun fromString() {
        assertEquals(NaturalUnit(), NaturalUnit(mapOf("length" to 1)) - NaturalUnit(mapOf("length" to 1)))
        assertEquals(NaturalUnit(), NaturalUnit(mapOf("length" to 1)) + NaturalUnit(mapOf("length" to -1)))
        assertEquals(NaturalUnit(mapOf("length" to 2)), NaturalUnit(mapOf("length" to 1)) + NaturalUnit(mapOf("length" to 1)))
        assertEquals(NaturalUnit(mapOf("length" to 1, "time" to -1)), NaturalUnit(mapOf("length" to 1)) - NaturalUnit(mapOf("time" to 1)))
    }

    @Test
    fun subtract() {
        assertEquals(NaturalUnit(mapOf("length" to 1), BigDecimal("1000")), NaturalUnit(mapOf("length" to 2), BigDecimal("1000")) - NaturalUnit(mapOf("length" to 1)))
        assertEquals(NaturalUnit(mapOf("length" to 1), BigDecimal(".001")), NaturalUnit(mapOf("length" to 2)) - NaturalUnit(mapOf("length" to 1), BigDecimal("1000")))
    }

    @Test
    fun fitsWithinBasic() {
        assertTrue(NaturalUnit(mapOf("length" to 1)).fitsWithin(NaturalUnit(mapOf("length" to 2))))
        assertTrue(NaturalUnit(mapOf("length" to 2)).fitsWithin(NaturalUnit(mapOf("length" to 2))))
    }

    @Test
    fun fitsWithinAdditionalDimension() {
        assertTrue(NaturalUnit(mapOf("length" to 2)).fitsWithin(NaturalUnit(mapOf("length" to 2, "mass" to 1))))
        assertFalse(NaturalUnit(mapOf("length" to 2, "mass" to 1)).fitsWithin(NaturalUnit(mapOf("length" to 2))))
    }

    @Test
    fun fitsWithinDifferentFactor() {
        val kilo = BigDecimal("1e3", MathContext.DECIMAL128)
        val mega = BigDecimal("1e6", MathContext.DECIMAL128)
        val milli = BigDecimal("1e-3", MathContext.DECIMAL128)

        assertTrue(NaturalUnit(mapOf("length" to 1)).fitsWithin(NaturalUnit(mapOf("length" to 1), kilo)))
        assertFalse(NaturalUnit(mapOf("length" to 1), kilo).fitsWithin(NaturalUnit(mapOf("length" to 1))))

        assertFalse(NaturalUnit(mapOf("length" to 1), milli).fitsWithin(NaturalUnit(mapOf("length" to 1))))
        assertTrue(NaturalUnit(mapOf("length" to 1)).fitsWithin(NaturalUnit(mapOf("length" to 1), milli)))

        assertFalse(NaturalUnit(mapOf("length" to 1), mega).fitsWithin(NaturalUnit(mapOf("length" to 1), kilo)))
        assertTrue(NaturalUnit(mapOf("length" to 1), kilo).fitsWithin(NaturalUnit(mapOf("length" to 1), mega)))
    }
}