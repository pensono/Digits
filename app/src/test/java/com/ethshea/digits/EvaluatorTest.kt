package com.ethshea.digits

import com.ethshea.digits.evaluator.Quantity
import com.ethshea.digits.evaluator.SciNumber
import com.ethshea.digits.evaluator.evaluateExpression
import org.antlr.v4.runtime.misc.Interval
import org.junit.Assert
import org.junit.Test

/**
 * @author Ethan
 */

class EvaluatorTest {
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
        evalTest(Quantity(SciNumber("123"), u("m")), "123m")
        evalTest(Quantity(SciNumber("123"), u("m")), "123 m")
        evalTest(Quantity(SciNumber("-123"), u("m")), "-123m")
    }

    @Test
    fun literalWithPerUnits() {
        evalTest(Quantity(SciNumber("123"), u("m") - u("s")), "123m/s")
        evalTest(Quantity(SciNumber("123"), u("m") - u("s")), "123 m/s")
        evalTest(Quantity(SciNumber("-123"), u("m") - u("s")), "-123m/s")
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
    fun exponentiation() {
        evalTest(Quantity(SciNumber("8")), "2^3")
        evalTest(Quantity(SciNumber("8")), "2³")
    }

    @Test
    fun negativeExponentiation() {
        evalTest(Quantity(SciNumber(".125")), "2^-3")
        evalTest(Quantity(SciNumber(".125")), "2⁻³")
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
    fun auxillaryParentheses() {
        evalTest(Quantity(SciNumber("30")), "[4+2]*5")
        evalTest(Quantity(SciNumber("30")), "{4+2}*5")
    }

    // Functionality not yet implemented, but it should be
//    @Test
//    fun mismatchedParentheses() {
//        errorTest(Quantity(SciNumber("30")), "(4+2]*5")
//        errorTest(Quantity(SciNumber("30")), "(4+2}*5")
//        errorTest(Quantity(SciNumber("30")), "[4+2)*5")
//        errorTest(Quantity(SciNumber("30")), "[4+2}*5")
//        errorTest(Quantity(SciNumber("30")), "{4+2)*5")
//        errorTest(Quantity(SciNumber("30")), "{4+2]*5")
//    }
//
//    @Test
//    fun unevenParentheses() {
//        evalTest(Quantity(SciNumber("30")), "(4+2)*5)")
//        evalTest(Quantity(SciNumber("30")), "((4+2)*5") // Evals ok, but a parse error
//    }

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
        correctionTest(Quantity(SciNumber("1")), "1+")
        correctionTest(Quantity(SciNumber("1")), "1+-")
        correctionTest(Quantity(SciNumber("2")), "1+1+")
        correctionTest(Quantity(SciNumber("123")), "123+")
    }

    @Test
    fun partialMultiplication() {
        correctionTest(Quantity(SciNumber("4")), "4*")
        correctionTest(Quantity(SciNumber("-4")), "-4*")

        // -4, 4 or 0 works here in that order of preference
        correctionTest(Quantity(SciNumber("0")), "4*-") // It would be nice to get -4 here, but that's alot of work
    }

    @Test
    fun squared() {
        evalTest(Quantity(SciNumber("4")), "(1+1)²")
        evalTest(Quantity(SciNumber("4")), "2²")
        evalTest(Quantity(SciNumber("4")), "(2)²")

        evalTest(Quantity(SciNumber("4")), "(1+1)^2")
        evalTest(Quantity(SciNumber("4")), "2^2")
        evalTest(Quantity(SciNumber("4")), "(2)^2")
    }

    @Test
    fun functionSquared() {
        evalTest(Quantity(SciNumber(Math.sin(.5) * Math.sin(.5))), "sin(.5)²")
    }

    @Test
    fun squaredWithUnit() {
        evalTest(Quantity(SciNumber("4"), u("m")), "2²m")
        evalTest(Quantity(SciNumber("4"), u("m") + u("m")), "2²m²")
    }

    @Test
    fun unitSquared() {
        evalTest(Quantity(SciNumber("2"), u("m") + u("m")), "2g²")
        evalTest(Quantity(SciNumber("2"), u("m") + u("m")), "2g2")
        evalTest(Quantity(SciNumber("2"), u("m") + u("m")), "2m²")
        evalTest(Quantity(SciNumber("2"), u("m") + u("m")), "2m2")
    }

    @Test
    fun constants() {
        evalTest(Quantity(SciNumber(Math.PI)), "π")
        evalTest(Quantity(SciNumber(Math.E)), "e")

        // These cases are extra hard because of the pico and peta prefixes. Just ignore them for now.
//        evalTest(Quantity(SciNumber(Math.PI)), "PI")
//        evalTest(Quantity(SciNumber(Math.PI)), "pi")
    }

    @Test
    fun trigFunctions() {
        evalTest(Quantity(SciNumber("0")), "sin(0)")
        evalTest(Quantity(SciNumber("1")), "sin(π/2)")
        evalTest(Quantity(SciNumber("1")), "cos(0)")
        evalTest(Quantity(SciNumber("0")), "cos(π/2)")
        evalTest(Quantity(SciNumber("0")), "tan(0)")
        evalTest(Quantity(SciNumber("1")), "tan(π/4)")

        evalTest(Quantity(SciNumber("0")), "sinh(0)")
        evalTest(Quantity(SciNumber("1")), "cosh(0)")
        evalTest(Quantity(SciNumber("0")), "tanh(0)")
    }

    @Test
    fun functionWithNoArgErrors() {
        errorTest(Interval(3, 4), "sin()")
        errorTest(Interval(4, 5), "4sin()")
    }

    @Test
    fun mismatchedUnits() {
        errorTest(Interval(4,5), "10m+2A")
        errorTest(Interval(4,10), "10m+(2A+3A)")
        errorTest(Interval(5,11), "(10m+(2A+3A))/4")
    }

    @Test
    fun invalidConstants() {
        errorTest(Interval(0, 11), "notAConstant")
    }

    @Test
    fun startWithSuperscript() {
        errorTest(Interval(0, 0), "²2")
    }

    @Test
    fun emptyInput() {
        correctionTest(Quantity(SciNumber("0")), "")
    }

    @Test
    fun justAnOperatorGivesError() {
        errorTest(Interval(0,0), "+")
        errorTest(Interval(0,0), "/")
        errorTest(Interval(0,0), "*")
    }

    @Test
    fun weirdInputGivesError() {
        anyErrorTest("+*4")
    }

    @Test
    fun largeExponentError() {
        errorTest(Interval(3,6), "4gm6666")
        errorTest(Interval(2,5), "4m6666")
    }

    private fun evalTest(expected: Quantity, input: String) {
        val result = evaluateExpression(input)
        Assert.assertEquals(expected.value.toDouble(), result.value.value.toDouble(), 1e-6)
        Assert.assertTrue(result.errors.isEmpty())
    }

    private fun correctionTest(expected: Quantity, input: String) {
        val result = evaluateExpression(input)
        Assert.assertEquals(expected.value.toDouble(), result.value.value.toDouble(), 1e-6)
        Assert.assertTrue(result.errors.isNotEmpty())
    }

    private fun errorTest(errorLocation: Interval, input: String) {
        val result = evaluateExpression(input)
        Assert.assertTrue(result.errors.any { err -> err.location == errorLocation })
    }

    private fun anyErrorTest(input: String) {
        val result = evaluateExpression(input)
        Assert.assertTrue(result.errors.isNotEmpty())
    }
}