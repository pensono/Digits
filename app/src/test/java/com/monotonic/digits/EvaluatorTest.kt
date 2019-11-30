package com.monotonic.digits

import com.monotonic.digits.evaluator.Quantity
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.evaluator.evaluateExpression
import org.antlr.v4.runtime.misc.Interval
import org.junit.Assert
import org.junit.Test

/**
 * @author Ethan
 */

class EvaluatorTest {
    @Test
    fun numericLiterals() {
        evalTest(Quantity(SciNumber.Real("1")), "1")
        evalTest(Quantity(SciNumber.Real("123")), "123")
        evalTest(Quantity(SciNumber.Real("123.45")), "123.45")
        evalTest(Quantity(SciNumber.Real("-123")), "-123")

        // TODO test failed parses
        // 12.34.56
    }

    @Test
    fun scientificNotation() {
        evalTest(Quantity(SciNumber.Real("100000", sf(1))), "1ᴇ5")
        evalTest(Quantity(SciNumber.Real("123000", sf(3))), "1.23ᴇ5")
        evalTest(Quantity(SciNumber.Real("100000", sf(3))), "1.00ᴇ5")
        evalTest(Quantity(SciNumber.Real("500000000000", sf(1))), "5ᴇ11") // Double digit exponent

        evalTest(Quantity(SciNumber.Real("-100000", sf(1))), "-1ᴇ5")
        evalTest(Quantity(SciNumber.Real("-123000", sf(3))), "-1.23ᴇ5")
        evalTest(Quantity(SciNumber.Real("-100000", sf(3))), "-1.00ᴇ5")
        evalTest(Quantity(SciNumber.Real("-500000000000", sf(1))), "-5ᴇ11") // Double digit exponent

        // Not actually scientific notation, but it's unambiguous so we'll accept it without complaint
        evalTest(Quantity(SciNumber.Real("10000000", sf(1))), "100ᴇ5")
        evalTest(Quantity(SciNumber.Real("1230000", sf(3))), "12.3ᴇ5")
        evalTest(Quantity(SciNumber.Real("1000000", sf(3))), "10.0ᴇ5")
    }

    @Test
    fun scientificNotationWithoutMantissaFails() {
        errorTest(Interval(0,1), "ᴇ5")
        errorTest(Interval(0,2), "ᴇ-5")
    }

    @Test
    fun literalsWithUnits() {
        evalTest(Quantity(SciNumber.Real("123"), u("m")), "123m")
        evalTest(Quantity(SciNumber.Real("123"), u("m")), "123 m")
        evalTest(Quantity(SciNumber.Real("-123"), u("m")), "-123m")
    }

    @Test
    fun literalWithPerUnits() {
        evalTest(Quantity(SciNumber.Real("123"), u("m") - u("s")), "123m/s")
        evalTest(Quantity(SciNumber.Real("123"), u("m") - u("s")), "123 m/s")
        evalTest(Quantity(SciNumber.Real("-123"), u("m") - u("s")), "-123m/s")
    }

    @Test
    fun addition() {
        evalTest(Quantity(SciNumber.Real("2")), "1+1")
        evalTest(Quantity(SciNumber.Real("3")), "1+1+1")
        evalTest(Quantity(SciNumber.Real("579")), "123+456")
    }

    @Test
    fun multiplication() {
        evalTest(Quantity(SciNumber.Real("8")), "4*2")
        evalTest(Quantity(SciNumber.Real("-8")), "-4*2")
        evalTest(Quantity(SciNumber.Real("-8")), "4*-2")
    }

    @Test
    fun exponentiation() {
        evalTest(Quantity(SciNumber.Real("8")), "2^3")
        evalTest(Quantity(SciNumber.Real("8")), "2³")
        evalTest(Quantity(SciNumber.Real("1024")), "2^10")
    }

    @Test
    fun negativeExponentiation() {
        evalTest(Quantity(SciNumber.Real(".125")), "2^-3")
        evalTest(Quantity(SciNumber.Real(".125")), "2⁻³")
    }

    @Test
    fun alternativeMultiplicationSign() {
        evalTest(Quantity(SciNumber.Real("8")), "4×2")
    }

    @Test
    fun topLevelParentheses() {
        evalTest(Quantity(SciNumber.Real("2")), "(1+1)")
        evalTest(Quantity(SciNumber.Real("8")), "(4*2)")
        evalTest(Quantity(SciNumber.Real("11")), "(4*2+3)")
        evalTest(Quantity(SciNumber.Real("11")), "(3+4*2)")
    }

    @Test
    fun auxillaryParentheses() {
        evalTest(Quantity(SciNumber.Real("30")), "[4+2]*5")
        evalTest(Quantity(SciNumber.Real("30")), "{4+2}*5")
    }

    // Functionality not yet implemented, but it should be
//    @Test
//    fun mismatchedParentheses() {
//        errorTest(Quantity(Real("30")), "(4+2]*5")
//        errorTest(Quantity(Real("30")), "(4+2}*5")
//        errorTest(Quantity(Real("30")), "[4+2)*5")
//        errorTest(Quantity(Real("30")), "[4+2}*5")
//        errorTest(Quantity(Real("30")), "{4+2)*5")
//        errorTest(Quantity(Real("30")), "{4+2]*5")
//    }
//
//    @Test
//    fun unevenParentheses() {
//        evalTest(Quantity(Real("30")), "(4+2)*5)")
//        evalTest(Quantity(Real("30")), "((4+2)*5") // Evals ok, but a parse error
//    }

    @Test
    fun orderOfOperations() {
        evalTest(Quantity(SciNumber.Real("11")), "4*2+3")
        evalTest(Quantity(SciNumber.Real("11")), "3+4*2")
        evalTest(Quantity(SciNumber.Real("-11")), "-3-4*2")

        evalTest(Quantity(SciNumber.Real("20")), "4*(2+3)")
        evalTest(Quantity(SciNumber.Real("11")), "(4*2)+3")
        evalTest(Quantity(SciNumber.Real("11")), "3+(4*2)")
        evalTest(Quantity(SciNumber.Real("14")), "(3+4)*2")
        evalTest(Quantity(SciNumber.Real("5")), "-(3-4*2)")
        evalTest(Quantity(SciNumber.Real("-11")), "-3-(4)*2")
    }

    @Test
    fun partialAddition() {
        evalTest(Quantity(SciNumber.Real("1")), "+1")
        correctionTest(Quantity(SciNumber.Real("1")), "1+")
        correctionTest(Quantity(SciNumber.Real("1")), "1+-")
        correctionTest(Quantity(SciNumber.Real("2")), "1+1+")
        correctionTest(Quantity(SciNumber.Real("123")), "123+")
    }

    @Test
    fun partialMultiplication() {
        correctionTest(Quantity(SciNumber.Real("4")), "4*")
        correctionTest(Quantity(SciNumber.Real("-4")), "-4*")

        // -4, 4 or 0 works here in that order of preference
        correctionTest(Quantity(SciNumber.Real("0")), "4*-") // It would be nice to get -4 here, but that's alot of work
    }

    @Test
    fun squared() {
        evalTest(Quantity(SciNumber.Real("4")), "(1+1)²")
        evalTest(Quantity(SciNumber.Real("4")), "2²")
        evalTest(Quantity(SciNumber.Real("4")), "(2)²")

        evalTest(Quantity(SciNumber.Real("4")), "(1+1)^2")
        evalTest(Quantity(SciNumber.Real("4")), "2^2")
        evalTest(Quantity(SciNumber.Real("4")), "(2)^2")
    }

    @Test
    fun functionSquared() {
        evalTest(Quantity(SciNumber.Real(Math.sin(.5) * Math.sin(.5))), "sin(.5)²")
    }

    @Test
    fun squaredWithUnit() {
        evalTest(Quantity(SciNumber.Real("4"), u("m")), "2²m")
        evalTest(Quantity(SciNumber.Real("4"), u("m") + u("m")), "2²m²")
    }

    @Test
    fun unitSquared() {
        evalTest(Quantity(SciNumber.Real("2"), u("m") + u("m")), "2g²")
        evalTest(Quantity(SciNumber.Real("2"), u("m") + u("m")), "2g2")
        evalTest(Quantity(SciNumber.Real("2"), u("m") + u("m")), "2m²")
        evalTest(Quantity(SciNumber.Real("2"), u("m") + u("m")), "2m2")
    }

    @Test
    fun constants() {
        evalTest(Quantity(SciNumber.Real(Math.PI)), "π")
        evalTest(Quantity(SciNumber.Real(Math.E)), "e")

        // These cases are extra hard because of the pico and peta prefixes. Just ignore them for now.
//        evalTest(Quantity(Real(Math.PI)), "PI")
//        evalTest(Quantity(Real(Math.PI)), "pi")
    }

    @Test
    fun trigFunctions() {
        evalTest(Quantity(SciNumber.Real("0")), "sin(0)")
        evalTest(Quantity(SciNumber.Real("1")), "sin(π/2)")
        evalTest(Quantity(SciNumber.Real("1")), "cos(0)")
        evalTest(Quantity(SciNumber.Real("0")), "cos(π/2)")
        evalTest(Quantity(SciNumber.Real("0")), "tan(0)")
        evalTest(Quantity(SciNumber.Real("1")), "tan(π/4)")

        evalTest(Quantity(SciNumber.Real("0")), "sinh(0)")
        evalTest(Quantity(SciNumber.Real("1")), "cosh(0)")
        evalTest(Quantity(SciNumber.Real("0")), "tanh(0)")
    }

    @Test
    fun nonlinearFunctionsCollapseInput() {
        evalTest(Quantity(SciNumber.Real("1")), "sin(90°)")
        evalTest(Quantity(SciNumber.Real("-1")), "cos(180°)")
        evalTest(Quantity(SciNumber.Real("1")), "tan(45°)")
    }

    @Test
    fun functionWithNoArgErrors() {
        errorTest(Interval(3, 4), "sin()")
        errorTest(Interval(4, 5), "4sin()")
    }

    @Test
    fun unknownFunctionErrors() {
        errorTest(Interval(3,17), "2π+notAFunction(4)") // It would be cool if the error just highlighted the function name
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
        correctionTest(Quantity(SciNumber.Real("0")), "")
    }

    @Test
    fun justAnOperatorGivesError() {
        errorTest(Interval(0,0), "+")
        errorTest(Interval(0,0), "/")
        errorTest(Interval(0,0), "*")
    }

    @Test
    fun justPrefixErrors() {
        errorTest(Interval(0,0), "k")
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

    @Test
    fun squareRoot() {
        evalTest(Quantity(SciNumber.Real("0")), "√(0)")
        evalTest(Quantity(SciNumber.Real("2")), "√(4)")
        evalTest(Quantity(SciNumber.Nan), "√(-4)") // Should eventually be 2i

        evalTest(Quantity(SciNumber.Real("0")), "√(0m2)")
        evalTest(Quantity(SciNumber.Real("2")), "√(4m2)")
        evalTest(Quantity(SciNumber.Nan), "√(-4m2)") // Should eventually be 2im

        // Alternate name
        evalTest(Quantity(SciNumber.Real("0")), "sqrt(0)")
        evalTest(Quantity(SciNumber.Real("2")), "sqrt(4)")
        evalTest(Quantity(SciNumber.Nan), "sqrt(-4)") // Should eventually be 2i
    }

    @Test
    fun squareRootUnEvenUnitsFails() {
        // Unit is not even. This should fail until rational unit exponents are implemented
        errorTest(Interval(2,3), "√(0m)")
        errorTest(Interval(2,3), "√(4m)")
        errorTest(Interval(2,4), "√(-4m)")
    }

    @Test
    fun logarithm() {
        evalTest(Quantity(SciNumber.Real("1")), "ln(e)")
        evalTest(Quantity(SciNumber.Real("2")), "ln(e2)")

        evalTest(Quantity(SciNumber.Real("1")), "log₁₀(10)")
        evalTest(Quantity(SciNumber.Real("2")), "log₁₀(100)")
        evalTest(Quantity(SciNumber.Real("1")), "log(10)")

        evalTest(Quantity(SciNumber.Real("1")), "log₂(2)")
        evalTest(Quantity(SciNumber.Real("4")), "log₂(16)")
    }

    @Test
    fun invalidLogarithm() {
        evalTest(Quantity(SciNumber.Nan), "ln(-e)")
        evalTest(Quantity(SciNumber.Nan), "ln(0)") // Should be -inf? Lets not worry about infinity for now
    }

    @Test
    fun exponential() {
        evalTest(Quantity(SciNumber.Real(Math.E)), "exp(1)")
        evalTest(Quantity(SciNumber.Real(Math.E * Math.E)), "exp(2)")

        evalTest(Quantity(SciNumber.Real(1)), "exp(0)")
        evalTest(Quantity(SciNumber.Real(1/Math.E)), "exp(-1)")
    }

    @Test
    fun twoDots() {
        errorTest(Interval.of(2, 2), "5..")
        errorTest(Interval.of(5, 9), "5.313.2031")
        errorTest(Interval.of(5, 8), "24+5..+25") // 5 to 5 would be nice, but 5 to 8 works too
    }

    @Test
    fun fractionalExponent() {
        errorTest(Interval.of(2, 4), "2e.25")
    }

    private fun evalTest(expected: Quantity, input: String) {
        val result = evaluateExpression(input)
        Assert.assertTrue(result.errors.isEmpty())
        Assert.assertEquals(expected.value.toDouble(), result.value.value.toDouble(), 1e-6)
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