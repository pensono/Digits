package com.ethshea.digits.evaluator

import com.ethshea.digits.SciNumber
import com.ethshea.digits.units.UnitSystem
import org.junit.Assert
import org.junit.Test

/**
 * @author Ethan
 */

class ParserTest {
    @Test
    fun numericLiterals() {
        evalTest(Quantity(SciNumber("1")), "1")
        evalTest(Quantity(SciNumber("123")), "123")
        evalTest(Quantity(SciNumber("123.45")), "123.45")
        evalTest(Quantity(SciNumber("-123")), "-123")

        // TODO test failed parses
        // 12.34.56
    }

    @Test
    fun literalsWithUnits() {
        evalTest(Quantity(SciNumber("123"), UnitSystem.unitByAbbreviation("m")!!), "123m")
        evalTest(Quantity(SciNumber("123"), UnitSystem.unitByAbbreviation("m")!!), "123 m")
        evalTest(Quantity(SciNumber("-123"), UnitSystem.unitByAbbreviation("m")!!), "-123m")
    }

    @Test
    fun addition() {
        evalTest(Quantity(SciNumber("2")), "1+1")
        evalTest(Quantity(SciNumber("3")), "1+1+1")
        evalTest(Quantity(SciNumber("579")), "123+456")
    }

    @Test
    fun multiplication() {
        evalTest(Quantity(SciNumber("8")), "4*2")
        evalTest(Quantity(SciNumber("-8")), "-4*2")
        evalTest(Quantity(SciNumber("-8")), "4*-2")
    }

    @Test
    fun alternativeMultiplicationSign() {
        evalTest(Quantity(SciNumber("8")), "4×2")
    }

    @Test
    fun topLevelParentheses() {
        evalTest(Quantity(SciNumber("2")), "(1+1)")
        evalTest(Quantity(SciNumber("8")), "(4*2)")
        evalTest(Quantity(SciNumber("11")), "(4*2+3)")
        evalTest(Quantity(SciNumber("11")), "(3+4*2)")
    }

    @Test
    fun orderOfOperations() {
        evalTest(Quantity(SciNumber("11")), "4*2+3")
        evalTest(Quantity(SciNumber("11")), "3+4*2")
        evalTest(Quantity(SciNumber("-11")), "-3-4*2")

        evalTest(Quantity(SciNumber("20")), "4*(2+3)")
        evalTest(Quantity(SciNumber("11")), "(4*2)+3")
        evalTest(Quantity(SciNumber("11")), "3+(4*2)")
        evalTest(Quantity(SciNumber("14")), "(3+4)*2")
        evalTest(Quantity(SciNumber("5")), "-(3-4*2)")
        evalTest(Quantity(SciNumber("-11")), "-3-(4)*2")
    }

    @Test
    fun partialAddition() {
        evalTest(Quantity(SciNumber("1")), "+1")
        evalTest(Quantity(SciNumber("1")), "1+")
        evalTest(Quantity(SciNumber("1")), "1+-")
        evalTest(Quantity(SciNumber("2")), "1+1+")
        evalTest(Quantity(SciNumber("123")), "123+")
    }

    @Test
    fun partialMultiplication() {
        evalTest(Quantity(SciNumber("4")), "4*")
        evalTest(Quantity(SciNumber("-4")), "-4*")
        evalTest(Quantity(SciNumber("4")), "4*-") // It would be nice to get -4 here, but that's alot of work
    }

    @Test
    fun emptyInput() {
        evalTest(Quantity(SciNumber("0")), "")
    }

    private fun evalTest(expected: Quantity, input: String) {
        val result = evaluateExpression(input)
        Assert.assertEquals(expected, result.value)
    }
}