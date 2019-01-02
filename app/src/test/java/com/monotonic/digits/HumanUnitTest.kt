package com.monotonic.digits

import com.monotonic.digits.human.HumanQuantity
import com.monotonic.digits.evaluator.Quantity
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.evaluator.evaluateExpression
import com.monotonic.digits.human.HumanUnit
import com.monotonic.digits.human.convert
import com.monotonic.digits.human.humanize
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Ethan
 */
class HumanUnitTest {
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
        assertEquals(HumanQuantity(SciNumber.Real("0"), HumanUnit(mapOf())), humanize(Quantity(SciNumber.Real("0"))))
        assertEquals(HumanQuantity(SciNumber.Real("0"), HumanUnit(mapOf(u("V") to 1))), humanize(Quantity(SciNumber.Real("0"), u("V"))))
        assertEquals(HumanQuantity(SciNumber.Real("0"), HumanUnit(mapOf(u("V") to 1), p("m"))), humanize(Quantity(SciNumber.Real("0"), p("m") + u("V"))))
    }

    @Test
    fun noPrefixForVoid() {
        assertEquals("", humanize(Quantity(Kilo)).unit.abbreviation)
        assertEquals("", humanize(Quantity(Milli)).unit.abbreviation)
        assertEquals("", humanize(Quantity(Mega)).unit.abbreviation)
        assertEquals("", humanize(Quantity(Micro)).unit.abbreviation)
    }

    @Test(timeout = 10000)
    fun largeUnitQuick() {
        val components = humanize(evaluateExpression("4m99").value).unit.components
        assertEquals(1, components.size)
    }


    @Test(timeout = 10000)
    fun largeUnitQuickCached() {
        for (i in 0..1000) {
            val components = humanize(evaluateExpression("4m99").value).unit.components
                assertEquals(1, components.size)
        }
    }

    @Test
    fun keepsPrecision() {
        assertEquals(sf(4), humanize(Quantity(SciNumber.Real("99.99"))).value.precision)
        assertEquals(sf(4), humanize(Quantity(SciNumber.Real("99.99"), u("m"))).value.precision)
    }

    @Test
    fun convertBasic() {
        testCoversion(SciNumber.Real(4000), HumanUnit(mapOf(u("m") to 1)), Quantity(SciNumber.Real(4), u("m") + p("k")))
        testCoversion(SciNumber.Real(4), HumanUnit(mapOf(u("m") to 1),  p("k")), Quantity(SciNumber.Real(4000), u("m")))
    }

    fun testCoversion(destValue: SciNumber.Real, destUnit: HumanUnit, inputValue: Quantity) {
        assertEquals(HumanQuantity(destValue, destUnit), convert(inputValue, destUnit))
    }
}