package com.ethshea.digits

import com.ethshea.digits.evaluator.Quantity
import humanize
import org.junit.Test

import org.junit.Assert.*
import java.math.BigDecimal

/**
 * @author Ethan
 */
class ElectricalDiciplineKtTest {
    @Test
    fun humanize() {
        assertEquals(HumanUnit(mapOf()), humanize(Quantity(BigDecimal.ONE)))
        assertEquals(HumanUnit(mapOf(u("Ω") to 1, u("m") to 1)), humanize(Quantity(BigDecimal.ONE, u("Ω") + u("m"))))
        assertEquals(HumanUnit(mapOf(u("M") to 1, u("V") to 1)), humanize(Quantity(BigDecimal.ONE, u("V") + u("M"))))
    }

    @Test
    fun onePrefix() {
        assertEquals(HumanUnit(mapOf(u("k") to 1)), humanize(Quantity(BigDecimal.ONE, u("k"))))
    }

    @Test
    fun abbreviation() {
        assertEquals("MV", humanize(Quantity(BigDecimal.ONE, u("M") + u("V"))).abbreviation)
    }

    @Test
    fun prefixFirst() {
        assert(humanize(Quantity(BigDecimal.ONE, u("M") + u("Ω") + u("m"))).abbreviation.startsWith("M"))
    }

    private fun u(abbr: String) = UnitSystem.byAbbreviation(abbr)!!
}