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
//    @Test
//    fun humanize() {
//        assertEquals(humanize(Quantity(BigDecimal.ONE)), UnitSystem.void)
//        assertEquals(humanize(Quantity(BigDecimal.ONE, u("Ω") + u("m"))),
//                HumanUnit("Ωm", "ohm meter", mapOf("mass" to 1, "length" to 3, "time" to -3, "current" to -2)))
//    }

    private fun u(abbr: String) = UnitSystem.byAbbreviation(abbr)!!
}