package com.ethshea.digits.dimensional

import org.junit.Test

import org.junit.Assert.*

/**
 * @author Ethan
 */
class UnitSystemTest {
    @Test
    fun fromString() {
        assertEquals(NaturalUnit(mapOf("time" to -1), 1.0), UnitSystem.fromString("Hz"))
        assertEquals(NaturalUnit(mapOf("mass" to 1, "length" to -1), 1e3), UnitSystem.fromString("kg/m"))
        assertEquals(NaturalUnit(mapOf("length" to 1, "time" to -2), 1.0), UnitSystem.fromString("m/s/s"))
    }
}