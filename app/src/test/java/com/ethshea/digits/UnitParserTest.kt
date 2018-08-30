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
}