package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import com.ethshea.digits.UnitSystem
import org.junit.Test

import org.junit.Assert.*
import java.math.BigDecimal

/**
 * @author Ethan
 */
class QuantityTest {
    @Test
    fun plus() {
        assertEquals(Quantity(BigDecimal("123123"), UnitSystem.byAbbreviation("m")!!),
                Quantity(BigDecimal("123"), UnitSystem.byAbbreviation("m")!!) + Quantity(BigDecimal("123"), NaturalUnit(mapOf("length" to 1), BigDecimal("1e3"))))
    }

    @Test
    fun minus() {
    }

    @Test
    fun times() {
    }

    @Test
    fun div() {
    }
}