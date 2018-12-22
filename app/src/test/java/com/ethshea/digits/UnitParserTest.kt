package com.ethshea.digits

import com.ethshea.digits.evaluator.parseUnit
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.antlr.v4.runtime.misc.Interval
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
        assertEquals(u("T"), parseUnit("T", Interval(0, 0)).value)
        assertEquals(u("T") + p("T"), parseUnit("TT").value)
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
    fun needsPrefix() {
        assertTrue(parseUnit("k", Interval(0, 0)).errors.size == 1)
        assertTrue(parseUnit("M", Interval(0, 0)).errors.size == 1)
        assertTrue(parseUnit("μ", Interval(0, 0)).errors.size == 1)
        assertTrue(parseUnit("G", Interval(0, 0)).errors.size == 1)
        assertTrue(parseUnit("p", Interval(0, 0)).errors.size == 1)
    }

    @Test
    fun per() {
        assertEquals(u("V") - u("s"), parseUnit("V/s").value)
        assertEquals(u("m") - u("s"), parseUnit("m/s").value)
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
    fun negativeExponentUnits() {
        assertEquals(-u("g"), parseUnit("g⁻¹").value)
        assertEquals(-u("g"), parseUnit("g-1").value)
        assertEquals(u("g") * -99, parseUnit("g⁻⁹⁹").value)
        assertEquals(u("g") * -99, parseUnit("g-99").value)

        assertEquals(-u("m"), parseUnit("m⁻¹").value)
        assertEquals(-u("m"), parseUnit("m-1").value)
    }

    private fun parseUnit(input: String) =
        parseUnit(input, Interval(0, 0)) // We don't care about the interval in this case

}