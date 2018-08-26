package com.ethshea.digits

import com.ethshea.digits.evaluator.Quantity
import com.ethshea.digits.units.HumanUnit
import com.ethshea.digits.units.UnitSystem
import humanize
import org.junit.Test

import org.junit.Assert.*

/**
 * @author Ethan
 */
class ElectricalDiciplineKtTest {
    @Test
    fun humanize() {
        assertEquals(HumanUnit(mapOf()), humanize(Quantity(SciNumber.One)))
        assertEquals(HumanUnit(mapOf(u("Ω") to 1, u("m") to 1)), humanize(Quantity(SciNumber.One, u("Ω") + u("m"))))
        assertEquals(HumanUnit(mapOf(u("V") to 1), p("M")), humanize(Quantity(SciNumber.One, u("V") + p("M"))))
    }

    @Test
    fun humanizeInverse() {
        assertEquals(HumanUnit(mapOf(u("Ω") to 1, u("m") to -1)), humanize(Quantity(SciNumber.One, u("Ω") - u("m"))))
        assertEquals(HumanUnit(mapOf(u("Ω") to 1, u("m") to -1), p("M")), humanize(Quantity(SciNumber.One, p("M") + u("Ω") - u("m"))))
    }

    @Test
    fun abbreviation() {
        assertEquals("MV", humanize(Quantity(SciNumber.One, p("M") + u("V"))).abbreviation)
        assertEquals("kV", humanize(Quantity(SciNumber.One, p("k") + u("V"))).abbreviation)
        assertEquals("mV", humanize(Quantity(SciNumber.One, p("m") + u("V"))).abbreviation)
        assertEquals("TV", humanize(Quantity(SciNumber.One, p("T") + u("V"))).abbreviation)
    }

    private fun u(abbr: String) = UnitSystem.unitByAbbreviation(abbr)!!
    private fun p(abbr: String) = UnitSystem.prefixByAbbreviation(abbr)!!
}