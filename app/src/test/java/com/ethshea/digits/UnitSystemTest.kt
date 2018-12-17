package com.ethshea.digits

import com.ethshea.digits.units.NaturalUnit
import org.junit.Test

import org.junit.Assert.*

/**
 * @author Ethan
 */
class UnitSystemTest {
    @Test
    fun unitMath() {
        assertEquals(NaturalUnit(), NaturalUnit(mapOf("length" to 1)) - NaturalUnit(mapOf("length" to 1)))
        assertEquals(NaturalUnit(), NaturalUnit(mapOf("length" to 1)) + NaturalUnit(mapOf("length" to -1)))
        assertEquals(NaturalUnit(mapOf("length" to 2)), NaturalUnit(mapOf("length" to 1)) + NaturalUnit(mapOf("length" to 1)))
        assertEquals(NaturalUnit(mapOf("length" to 1, "time" to -1)), NaturalUnit(mapOf("length" to 1)) - NaturalUnit(mapOf("time" to 1)))
    }

    @Test
    fun subtract() {
        assertEquals(NaturalUnit(mapOf("length" to 1), Kilo), NaturalUnit(mapOf("length" to 2), Kilo) - NaturalUnit(mapOf("length" to 1)))
        assertEquals(NaturalUnit(mapOf("length" to 1), Milli), NaturalUnit(mapOf("length" to 2)) - NaturalUnit(mapOf("length" to 1), Kilo))
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
        assertTrue(NaturalUnit(mapOf("length" to 1)).fitsWithin(NaturalUnit(mapOf("length" to 1), Kilo)))
        assertFalse(NaturalUnit(mapOf("length" to 1), Kilo).fitsWithin(NaturalUnit(mapOf("length" to 1))))

        assertFalse(NaturalUnit(mapOf("length" to 1), Milli).fitsWithin(NaturalUnit(mapOf("length" to 1))))
        assertTrue(NaturalUnit(mapOf("length" to 1)).fitsWithin(NaturalUnit(mapOf("length" to 1), Milli)))
    }
}