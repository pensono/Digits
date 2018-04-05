package com.ethshea.digits

import org.junit.Test

import org.junit.Assert.*

/**
 * @author Ethan
 */
class UnitSystemTest {
    @Test
    fun fromString() {
        assertEquals(NaturalUnit(mapOf(), 1.0), NaturalUnit(mapOf("length" to 1), 1.0) - NaturalUnit(mapOf("length" to 1), 1.0))
        assertEquals(NaturalUnit(mapOf(), 1.0), NaturalUnit(mapOf("length" to 1), 1.0) + NaturalUnit(mapOf("length" to -1), 1.0))
        assertEquals(NaturalUnit(mapOf("length" to 2), 1.0), NaturalUnit(mapOf("length" to 1), 1.0) + NaturalUnit(mapOf("length" to 1), 1.0))
        assertEquals(NaturalUnit(mapOf("length" to 1, "time" to -1), 1.0), NaturalUnit(mapOf("length" to 1), 1.0) - NaturalUnit(mapOf("time" to 1), 1.0))
    }
}