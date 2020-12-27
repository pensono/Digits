package com.monotonic.digits

import com.monotonic.digits.units.DimensionBag
import com.monotonic.digits.units.NaturalUnit
import org.junit.Assert.assertEquals
import org.junit.Test

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
        assertEquals(NaturalUnit(DimensionBag("length" to 1), Kilo), NaturalUnit(DimensionBag("length" to 2), Kilo) - NaturalUnit(mapOf("length" to 1)))
        assertEquals(NaturalUnit(DimensionBag("length" to 1), Milli), NaturalUnit(mapOf("length" to 2)) - NaturalUnit(DimensionBag("length" to 1), Kilo))
    }
}