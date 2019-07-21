package com.monotonic.digits

import com.monotonic.digits.evaluator.ParseResult
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.evaluator.evaluateExpression
import com.monotonic.digits.units.NaturalUnit
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test

/**
 * @author Ethan
 */
class UnitParserTest {
    @Test
    fun millimeters() {
        assertEquals(p("m") + u("m"), parseUnit("mm").value)
    }

    @Test
    fun meters() {
        assertEquals(u("m"), parseUnit("m").value)
    }

    @Test
    fun teslas() {
        assertEquals(u("T"), parseUnit("T").value)
        assertEquals(u("T") + p("T"), parseUnit("TT").value)
    }

    @Test
    fun unitStartsWithPrefix() {
        assertEquals(u("pc"), parseUnit("pc").value)
        assertEquals(u("ft"), parseUnit("ft").value)
        assertEquals(u("mi"), parseUnit("mi").value)
    }

    @Test
    fun milliSomething() {
        assertEquals(u("V") + p("m"), parseUnit("mV").value)
    }

    @Test
    fun terraSomething() {
        assertEquals(u("V") + p("T"), parseUnit("TV").value)
    }

    @Test
    fun withPrefix() {
        assertEquals(u("V") + p("k"), parseUnit("kV").value)
        assertEquals(u("m") + p("k"), parseUnit("km").value)
    }

    @Test
    fun prefixOnlyErrors() {
        assertTrue(parseUnit("k").errors.size == 1)
        assertTrue(parseUnit("M").errors.size == 1)
        assertTrue(parseUnit("μ").errors.size == 1)
        assertTrue(parseUnit("G").errors.size == 1)
        assertTrue(parseUnit("p").errors.size == 1)
    }

    @Test
    fun per() {
        assertEquals(u("V") - u("s"), parseUnit("V/s").value)
        assertEquals(u("m") - u("s"), parseUnit("m/s").value)
        assertEquals(u("m") - u("s") - u("s"), parseUnit("m/s2").value)
    }

    @Test
    fun exponentUnits() {
        assertEquals(u("g") + u("g"), parseUnit("g²").value)
        assertEquals(u("g") + u("g"), parseUnit("g2").value)
        assertEquals(u("g") * 99, parseUnit("g⁹⁹").value)
        assertEquals(u("g") * 99, parseUnit("g99").value)
        
        assertEquals(u("m") + u("m"), parseUnit("m²").value)
        assertEquals(u("m") + u("m"), parseUnit("m2").value)
    }

    @Test
    fun prefixedExponentUnits() {
        // Should parse as (km)^2 rather than k(m^2)
        assertEquals(NaturalUnit(mapOf("length" to 2), SciNumber.Real(1000000)), parseUnit("km²").value)
        assertEquals(NaturalUnit(mapOf("length" to 2), SciNumber.Real(1000000)), parseUnit("km2").value)
    }

    @Test
    fun negativeExponentUnits() {
        assertEquals(-u("g"), parseUnit("g⁻¹").value)
        assertEquals(u("g") * -99, parseUnit("g⁻⁹⁹").value)

        assertEquals(-u("m"), parseUnit("m⁻¹").value)

    }

    private fun parseUnit(input: String) : ParseResult<NaturalUnit> =
            evaluateExpression("1$input").invoke { it.unit } // We don't care about the interval in this case

}