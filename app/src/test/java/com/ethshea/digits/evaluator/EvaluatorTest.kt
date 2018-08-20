package com.ethshea.digits.evaluator

import com.ethshea.digits.UnitSystem
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

/**
 * @author Ethan
 */

class ParserTest {
    @Test
    fun numericLiterals() {
        evalTest(Quantity(BigDecimal("1")), "1")
        evalTest(Quantity(BigDecimal("123")), "123")
        evalTest(Quantity(BigDecimal("123.45")), "123.45")
        evalTest(Quantity(BigDecimal("-123")), "-123")

        // TODO test failed parses
        // 12.34.56
    }

    @Test
    fun literalsWithUnits() {
        evalTest(Quantity(BigDecimal("123"), UnitSystem.byAbbreviation("m")!!), "123m")
        evalTest(Quantity(BigDecimal("123"), UnitSystem.byAbbreviation("m")!!), "123 m")
        evalTest(Quantity(BigDecimal("-123"), UnitSystem.byAbbreviation("m")!!), "-123m")
    }

    @Test
    fun addition() {
        evalTest(Quantity(BigDecimal("2")), "1+1")
        evalTest(Quantity(BigDecimal("3")), "1+1+1")
        evalTest(Quantity(BigDecimal("579")), "123+456")
    }


    @Test
    fun multiplication() {
        evalTest(Quantity(BigDecimal("8")), "4*2")
        evalTest(Quantity(BigDecimal("-8")), "-4*2")
        evalTest(Quantity(BigDecimal("-8")), "4*-2")
    }

    @Test
    fun topLevelParentheses() {
        evalTest(Quantity(BigDecimal("2")), "(1+1)")
        evalTest(Quantity(BigDecimal("8")), "(4*2)")
        evalTest(Quantity(BigDecimal("11")), "(4*2+3)")
        evalTest(Quantity(BigDecimal("11")), "(3+4*2)")
    }

    @Test
    fun orderOfOperations() {
        evalTest(Quantity(BigDecimal("11")), "4*2+3")
        evalTest(Quantity(BigDecimal("11")), "3+4*2")
        evalTest(Quantity(BigDecimal("-11")), "-3-4*2")

        evalTest(Quantity(BigDecimal("20")), "4*(2+3)")
        evalTest(Quantity(BigDecimal("11")), "(4*2)+3")
        evalTest(Quantity(BigDecimal("11")), "3+(4*2)")
        evalTest(Quantity(BigDecimal("14")), "(3+4)*2")
        evalTest(Quantity(BigDecimal("5")), "-(3-4*2)")
        evalTest(Quantity(BigDecimal("-11")), "-3-(4)*2")
    }

    @Test
    fun partialAddition() {
        evalTest(Quantity(BigDecimal("1")), "+1")
        evalTest(Quantity(BigDecimal("1")), "1+")
        evalTest(Quantity(BigDecimal("1")), "1+-")
        evalTest(Quantity(BigDecimal("2")), "1+1+")
        evalTest(Quantity(BigDecimal("123")), "123+")
    }

    @Test
    fun partialMultiplication() {
        evalTest(Quantity(BigDecimal("4")), "4*")
        evalTest(Quantity(BigDecimal("-4")), "-4*")
        evalTest(Quantity(BigDecimal("4")), "4*-") // It would be nice to get -4 here, but that's alot of work
    }

    @Test
    fun emptyInput() {
        evalTest(Quantity(BigDecimal("0")), "")
    }

    private fun evalTest(expected: Quantity, input: String) {
        val result = evaluateExpression(input)
        Assert.assertEquals(expected, result.value)
    }
}