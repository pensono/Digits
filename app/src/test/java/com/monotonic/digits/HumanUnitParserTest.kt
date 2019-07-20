package com.monotonic.digits

import com.monotonic.digits.human.*
import com.monotonic.digits.units.PrefixUnit
import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * @author Ethan
 */
class HumanUnitParserTest {
    @Test
    fun millimeters() {
        assertEquals(HumanUnit(mapOf(u("m") to 1), p("m")), parseHumanUnit("mm"))
    }

    @Test
    fun meters() {
        assertEquals(HumanUnit(mapOf(u("m") to 1)), parseHumanUnit("m"))
        assertEquals(HumanUnit(mapOf(u("m") to 1), p("k")), parseHumanUnit("km"))
    }

    @Test
    fun teslas() {
        assertEquals(HumanUnit(mapOf(u("T") to 1)), parseHumanUnit("T"))
        assertEquals(HumanUnit(mapOf(u("T") to 1), p("T")), parseHumanUnit("TT"))
    }

    @Test
    fun unitStartsWithPrefix() {
        assertEquals(HumanUnit(mapOf(u("ft") to 1)), parseHumanUnit("ft"))
        assertEquals(HumanUnit(mapOf(u("mi") to 1)), parseHumanUnit("mi"))
        assertEquals(HumanUnit(mapOf(u("pc") to 1)), parseHumanUnit("pc"))
    }

    @Test
    fun milliSomething() {
        assertEquals(HumanUnit(mapOf(u("V") to 1), p("m")), parseHumanUnit("mV"))
    }

    @Test
    fun terraSomething() {
        assertEquals(HumanUnit(mapOf(u("V") to 1), p("T")), parseHumanUnit("TV"))
    }

    @Test
    fun withPrefix() {
        assertEquals(HumanUnit(mapOf(u("V") to 1), p("k")), parseHumanUnit("kV"))
        assertEquals(HumanUnit(mapOf(u("m") to 1), p("k")), parseHumanUnit("km"))
    }

    @Test
    fun prefixOnlyErrors() {
        assertEquals(null, parseHumanUnit("k"))
        assertEquals(null, parseHumanUnit("M"))
        assertEquals(null, parseHumanUnit("μ"))
        assertEquals(null, parseHumanUnit("G"))
        assertEquals(null, parseHumanUnit("p"))
    }

    @Test
    fun per() {
        assertEquals(HumanUnit(mapOf(u("V") to 1, u("s") to -1)), parseHumanUnit("V/s"))
        assertEquals(HumanUnit(mapOf(u("m") to 1, u("s") to -1)), parseHumanUnit("m/s"))
        assertEquals(HumanUnit(mapOf(u("m") to 1, u("s") to -2)), parseHumanUnit("m/s2"))
    }

    @Test
    fun exponentUnits() {
        assertEquals(HumanUnit(mapOf(u("g") to 2)), parseHumanUnit("g²"))
        assertEquals(HumanUnit(mapOf(u("g") to 2)), parseHumanUnit("g2"))
        assertEquals(HumanUnit(mapOf(u("g") to 99)), parseHumanUnit("g⁹⁹"))
        assertEquals(HumanUnit(mapOf(u("g") to 99)), parseHumanUnit("g99"))
        assertEquals(HumanUnit(mapOf(u("g") to 1)), parseHumanUnit("g1")) // Silly, but should still be supported

        assertEquals(HumanUnit(mapOf(u("m") to 2)), parseHumanUnit("m²"))
        assertEquals(HumanUnit(mapOf(u("m") to 2)), parseHumanUnit("m2"))
        assertEquals(HumanUnit(mapOf(u("m") to 1)), parseHumanUnit("m1")) // Silly, but should still be supported
    }

    @Test
    fun prefixedSingleExponentUnit() {
        assertEquals(HumanUnit(mapOf(u("m") to 2), PrefixUnit("k", "Kilo", "1e6", "")), parseHumanUnit("km2"))
    }

    @Test
    fun negativeExponentUnits() {
        assertEquals(HumanUnit(mapOf(u("g") to -1)), parseHumanUnit("g⁻¹"))
        assertEquals(HumanUnit(mapOf(u("g") to -99)), parseHumanUnit("g⁻⁹⁹"))
        assertEquals(HumanUnit(mapOf(u("m") to -1)), parseHumanUnit("m⁻¹"))

        assertEquals(HumanUnit(mapOf(u("g") to -1)), parseHumanUnit("g-1"))
        assertEquals(HumanUnit(mapOf(u("g") to -99)), parseHumanUnit("g-99"))
        assertEquals(HumanUnit(mapOf(u("m") to -1)), parseHumanUnit("m-1"))
    }
}