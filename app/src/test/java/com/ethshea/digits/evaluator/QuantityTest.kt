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
    val meters = UnitSystem.byAbbreviation("m")!!
    val kilometers = NaturalUnit(mapOf("length" to 1), BigDecimal("1e3"))

    @Test
    fun plus() {
        assertEquals(Quantity(BigDecimal("456.123"), kilometers),
                Quantity(BigDecimal("123"), meters) + Quantity(BigDecimal("456"), kilometers))

        assertEquals(Quantity(BigDecimal("123.456"), kilometers),
                Quantity(BigDecimal("123"), kilometers) + Quantity(BigDecimal("456"), meters))
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