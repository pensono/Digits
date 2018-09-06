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
        assertEquals(HumanQuantity(SciNumber.One, HumanUnit(mapOf(u("V") to 1), p("k"))), humanize(Quantity(SciNumber.Kilo, u("V"))))
    }

    @Test
    fun zero() {
        assertEquals(HumanQuantity(SciNumber.Zero, HumanUnit(mapOf())), humanize(Quantity(SciNumber.Zero)))
        assertEquals(HumanQuantity(SciNumber.Zero, HumanUnit(mapOf(u("V") to 1))), humanize(Quantity(SciNumber.Zero, u("V"))))
    }

    @Test
    fun noPrefixForVoid() {
        assertEquals("", humanize(Quantity(SciNumber.Kilo)).unit.abbreviation)
        assertEquals("", humanize(Quantity(SciNumber.Milli)).unit.abbreviation)
        assertEquals("", humanize(Quantity(SciNumber.Mega)).unit.abbreviation)
        assertEquals("", humanize(Quantity(SciNumber.Micro)).unit.abbreviation)
    }

    @Test
    fun useDecimalNotation() {
        assertEquals("0.05", humanize(evaluateExpression("1/20").value).humanString())
        assertEquals("56000", humanize(evaluateExpression("56k").value).humanString())
    }
}