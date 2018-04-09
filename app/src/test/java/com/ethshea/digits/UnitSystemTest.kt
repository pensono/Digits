package com.ethshea.digits

import org.junit.Test

import org.junit.Assert.*

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
}