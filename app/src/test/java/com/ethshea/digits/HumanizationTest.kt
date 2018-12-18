package com.ethshea.digits

import com.ethshea.digits.evaluator.HumanQuantity
import com.ethshea.digits.evaluator.Quantity
import com.ethshea.digits.evaluator.evaluateExpression
import com.ethshea.digits.units.HumanUnit
import com.ethshea.digits.units.NaturalUnit
import com.ethshea.digits.units.UnitSystem
import com.ethshea.digits.units.humanize
import org.junit.Test

import org.junit.Assert.*

/**
 * @author Ethan
 */
class HumanizationTest {
    @Test
    fun humanize() {
        assertEquals(HumanQuantity(SciNumber.One, HumanUnit(mapOf())), humanize(Quantity(SciNumber.One)))
        assertEquals(HumanQuantity(SciNumber.One, HumanUnit(mapOf(u("Ω") to 1, u("m") to 1))), humanize(Quantity(SciNumber.One, u("Ω") + u("m"))))
        assertEquals(HumanQuantity(SciNumber.One, HumanUnit(mapOf(u("V") to 1), p("M"))), humanize(Quantity(SciNumber.One, u("V") + p("M"))))
    }

    @Test
    fun humanizeInverse() {
        assertEquals(HumanQuantity(SciNumber.One, HumanUnit(mapOf(u("Ω") to 1, u("m") to -1))), humanize(Quantity(SciNumber.One, u("Ω") - u("m"))))
        assertEquals(HumanQuantity(SciNumber.One, HumanUnit(mapOf(u("Ω") to 1, u("m") to -1), p("M"))), humanize(Quantity(SciNumber.One, p("M") + u("Ω") - u("m"))))
    }

    @Test
    fun abbreviation() {
        assertEquals("MV", humanize(Quantity(SciNumber.One, p("M") + u("V"))).unit.abbreviation)
        assertEquals("kV", humanize(Quantity(SciNumber.One, p("k") + u("V"))).unit.abbreviation)
        assertEquals("mV", humanize(Quantity(SciNumber.One, p("m") + u("V"))).unit.abbreviation)
        assertEquals("TV", humanize(Quantity(SciNumber.One, p("T") + u("V"))).unit.abbreviation)
    }

    @Test
    fun changeFactor() {
        assertEquals(HumanQuantity(SciNumber.One, HumanUnit(mapOf(u("V") to 1), p("k"))), humanize(Quantity(Kilo, u("V"))))
    }

    @Test
    fun zero() {
        assertEquals(HumanQuantity(SciNumber("0"), HumanUnit(mapOf())), humanize(Quantity(SciNumber("0"))))
        assertEquals(HumanQuantity(SciNumber("0"), HumanUnit(mapOf(u("V") to 1))), humanize(Quantity(SciNumber("0"), u("V"))))
        assertEquals(HumanQuantity(SciNumber("0"), HumanUnit(mapOf(u("V") to 1), p("m"))), humanize(Quantity(SciNumber("0"), p("m") + u("V"))))
    }

    @Test
    fun noPrefixForVoid() {
        assertEquals("", humanize(Quantity(Kilo)).unit.abbreviation)
        assertEquals("", humanize(Quantity(Milli)).unit.abbreviation)
        assertEquals("", humanize(Quantity(Mega)).unit.abbreviation)
        assertEquals("", humanize(Quantity(Micro)).unit.abbreviation)
    }

    @Test
    fun useDecimalNotation() {
        assertEquals("0.05", humanize(evaluateExpression("1/20").value).humanString())
        assertEquals("56000", humanize(evaluateExpression("56k").value).humanString())
    }

    @Test
    fun engineeringNotation() {
        assertEquals("123", humanize(evaluateExpression("123").value).humanString())

        assertEquals("1m", humanize(evaluateExpression("1m").value).humanString())
        assertEquals("123m", humanize(evaluateExpression("123m").value).humanString())
        assertEquals("12.3m", humanize(evaluateExpression("12.3m").value).humanString())
        assertEquals("1.23m", humanize(evaluateExpression("1.23m").value).humanString())
        assertEquals("123mm", humanize(evaluateExpression(".123m").value).humanString())
    }

    @Test
    fun keepsPrecision() {
        assertEquals(sf(4), humanize(Quantity(SciNumber("99.99"))).value.precision)
        assertEquals(sf(4), humanize(Quantity(SciNumber("99.99"), u("m"))).value.precision)
    }
}