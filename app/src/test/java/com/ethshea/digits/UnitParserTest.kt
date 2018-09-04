package com.ethshea.digits

import com.ethshea.digits.evaluator.parseUnit
import junit.framework.Assert.assertEquals
import org.antlr.v4.runtime.misc.Interval
import org.junit.Test

/**
 * @author Ethan
 */
class UnitParserTest {
    @Test
    fun millimeters() {
        assertEquals(p("m") + u("m"), parseUnit("mm", Interval(0, 0)).value)
    }

    @Test
    fun meters() {
        assertEquals(u("m"), parseUnit("m", Interval(0, 0)).value)
    }

    @Test
    fun milliSomething() {
        assertEquals(u("V") + p("m"), parseUnit("mV", Interval(0, 0)).value)
    }

    @Test
    fun withPrefix() {
        assertEquals(u("V") + p("k"), parseUnit("kV", Interval(0, 0)).value)
    }

    @Test
    fun per() {
        assertEquals(u("V") - u("s"), parseUnit("V/s", Interval(0, 0)).value)
        assertEquals(u("m") - u("s"), parseUnit("m/s", Interval(0, 0)).value)
    }

    @Test
    fun exponentUnits() {
        assertEquals(u("g") + u("g"), parseUnit("g²", Interval(0, 0)).value)
        assertEquals(u("g") + u("g"), parseUnit("g2", Interval(0, 0)).value)
        assertEquals(u("g") * 123, parseUnit("g¹²³", Interval(0, 0)).value)
        assertEquals(u("g") * 123, parseUnit("g123", Interval(0, 0)).value)
        
        assertEquals(u("m") + u("m"), parseUnit("m²", Interval(0, 0)).value)
        assertEquals(u("m") + u("m"), parseUnit("m2", Interval(0, 0)).value)
    }

    @Test
    fun negativeExponentUnits() {
        assertEquals(-u("g"), parseUnit("g⁻¹", Interval(0, 0)).value)
        assertEquals(-u("g"), parseUnit("g-1", Interval(0, 0)).value)
        assertEquals(u("g") * -123, parseUnit("g⁻¹²³", Interval(0, 0)).value)
        assertEquals(u("g") * -123, parseUnit("g-123", Interval(0, 0)).value)

        assertEquals(-u("m"), parseUnit("m⁻¹", Interval(0, 0)).value)
        assertEquals(-u("m"), parseUnit("m-1", Interval(0, 0)).value)
    }
}