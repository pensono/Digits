package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import com.ethshea.digits.UnitSystem
import org.junit.Test

import org.junit.Assert.*
import java.math.BigDecimal

/**
 * @author Ethan
 */
class ParserTest {
    @Test
    fun evaluateInput() {
        doTest(Quantity(BigDecimal("1")), "1", ::evaluateExpression)
        doTest(Quantity(BigDecimal("2")), "1+1", ::evaluateExpression)
        doTest(Quantity(BigDecimal("3")), "1+1+1", ::evaluateExpression)
        doTest(Quantity(BigDecimal("579")), "123+456", ::evaluateExpression)
        doTest(Quantity(BigDecimal("8")), "4*2", ::evaluateExpression)
        doTest(Quantity(BigDecimal("11")), "4*2+3", ::evaluateExpression)
        doTest(Quantity(BigDecimal("11")), "3+4*2", ::evaluateExpression)
    }

    @Test
    fun parseNumeric() {
        doTest(Quantity(BigDecimal("123")), "123", ::parseNumeric)
        doTest(Quantity(BigDecimal("123.45")), "123.45", ::parseNumeric)
        doTest(null, "123.45.67", ::parseNumeric)
        doTest(Quantity(BigDecimal("123")), "123 +", "+", ::parseNumeric)
        doTest(Quantity(BigDecimal("123")), "123.", "", ::parseNumeric)
        doTest(Quantity(BigDecimal("-123")), "-123", ::parseNumeric)


        doTest(Quantity(BigDecimal("123"), UnitSystem.byAbbreviation("m")!!), "123m", ::parseNumeric)
        doTest(Quantity(BigDecimal("123"), UnitSystem.byAbbreviation("m")!!), "123 m", ::parseNumeric)
        doTest(Quantity(BigDecimal("-123"), UnitSystem.byAbbreviation("m")!!), "-123m", ::parseNumeric)

        //doTest(Quantity(BigDecimal("123"), UnitSystem.byAbbreviation("m")!!), "123m/2", "/2", ::parseNumeric)
    }

    @Test
    fun parseUnit() {
        doTest(NaturalUnit(mapOf("time" to -1), 1.0), "Hz", ::parseUnit)
        doTest(NaturalUnit(mapOf("mass" to 1, "length" to -1), 1e3), "kg/m", ::parseUnit)
        doTest(NaturalUnit(mapOf("length" to 1, "time" to -2), 1.0), "m/s/s", ::parseUnit)
        doTest(NaturalUnit(mapOf(), 1.0), "m/m", ::parseUnit)
    }

    private fun doTest(expected: Any?, input: String, operation: (TokenIterator) -> Any?) {
        doTest(expected, input, "", operation)
    }

    private fun doTest(expected: Any?, input: String, remaining: String, operation: (TokenIterator) -> Any?) {
        var tokens = TokenIterator(input)
        assertEquals(expected, operation.invoke(tokens))
        if (expected != null) {
            assertEquals("Too much or too little input consumed", tokens.remaining, remaining.length)
        }
    }
}