package com.ethshea.digits

import com.ethshea.digits.evaluator.Precision
import com.ethshea.digits.evaluator.SciNumber
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test

class SciNumberTest {
    @Test
    fun precisionLiteral() {
        assertEquals(sf(1), SciNumber("1").precision)
        assertEquals(Precision.Infinite, SciNumber(10).precision)
        assertEquals(sf(2), SciNumber("10").precision)
        assertEquals(sf(2), SciNumber("10.").precision)
        assertEquals(sf(3), SciNumber("10.0").precision)
        assertEquals(sf(2), SciNumber("11").precision)
        assertEquals(sf(3), SciNumber("110").precision)
        assertEquals(sf(3), SciNumber("1.00").precision)
        assertEquals(sf(3), SciNumber("01.00").precision)

        assertEquals(sf(1), (SciNumber(".01")).precision)
        assertEquals(sf(1), SciNumber(".01").precision)
        assertEquals(sf(2), SciNumber(".011").precision)
        assertEquals(sf(1), SciNumber(".001").precision)
        assertEquals(sf(2), SciNumber(".0010").precision)

        assertEquals(sf(3), SciNumber("99.1").precision)
    }

    @Test
    fun negativeLiteral() {
        assertEquals(sf(1), SciNumber("-1").precision)
        assertEquals(sf(2), SciNumber("-10").precision)
        assertEquals(sf(3), SciNumber("-100").precision)

        assertEquals(sf(1), SciNumber("-.1").precision)
        assertEquals(sf(2), SciNumber("-.010").precision)
        assertEquals(sf(3), SciNumber("-.00100").precision)
    }

    @Test
    fun negate() {
        assertEquals(sf(1), (-SciNumber("4")).precision)
        assertEquals(sf(3), (-SciNumber("400.")).precision)
        assertEquals(Precision.Infinite, (-SciNumber(400)).precision)
    }

    @Test
    fun reciprocal() {
        assertEquals(sf(1), (SciNumber("4").reciprocal()).precision)
        assertEquals(sf(3), (SciNumber("400.").reciprocal()).precision)
        assertEquals(Precision.Infinite, (SciNumber(400).reciprocal()).precision)
    }

    @Test
    fun parseDecimal() {
        assertTrue(SciNumber(".01") < SciNumber("1"))
        assertTrue(SciNumber("00.01") < SciNumber("1"))
    }

    @Test
    fun precisionTranscendental() {
        assertEquals(sf(1), SciNumber("1").sin().precision)
        assertEquals(sf(3), SciNumber("1.00").sin().precision)
        assertEquals(sf(3), SciNumber("01.00").sin().precision)
    }

    @Test
    fun magnitudeCorrect() {
        assertEquals(-2, SciNumber(".001").magnitude)
        assertEquals(-1, SciNumber(".01").magnitude)
        assertEquals(0, SciNumber(".1").magnitude)
        assertEquals(1, SciNumber("1").magnitude)
        assertEquals(2, SciNumber("10").magnitude)
        assertEquals(2, SciNumber("10.").magnitude)
        assertEquals(3, SciNumber("100").magnitude)

        assertEquals(-2, SciNumber(".002").magnitude)
        assertEquals(-1, SciNumber(".02").magnitude)
        assertEquals(0, SciNumber(".2").magnitude)
        assertEquals(1, SciNumber("2").magnitude)
        assertEquals(2, SciNumber("20").magnitude)
        assertEquals(2, SciNumber("20.").magnitude)
        assertEquals(3, SciNumber("200").magnitude)

        assertEquals(-2, SciNumber(".009").magnitude)
        assertEquals(-1, SciNumber(".09").magnitude)
        assertEquals(0, SciNumber(".9").magnitude)
        assertEquals(1, SciNumber("9").magnitude)
        assertEquals(2, SciNumber("90").magnitude)
        assertEquals(2, SciNumber("90.").magnitude)
        assertEquals(3, SciNumber("900").magnitude)
    }

    @Test
    fun precisionAddition() {
        assertEquals(sf(1), (SciNumber("1") + SciNumber("2")).precision)
        assertEquals(sf(2), (SciNumber("10") + SciNumber("20")).precision)
        assertEquals(sf(2), (SciNumber("10.") + SciNumber("20.")).precision)
        assertEquals(sf(3), (SciNumber("10.0") + SciNumber("20.0")).precision)

        assertEquals(sf(1), (SciNumber(".01") + SciNumber("2")).precision)
        assertEquals(sf(1), (SciNumber(".01") + SciNumber(".02")).precision)

        assertEquals(sf(2), (SciNumber("11") + SciNumber("2")).precision)
        assertEquals(sf(2), (SciNumber("11") + SciNumber("20")).precision)
        assertEquals(sf(2), (SciNumber("11.0") + SciNumber("2")).precision)
        assertEquals(sf(2), (SciNumber("1.1") + SciNumber(".2")).precision)
        assertEquals(sf(1), (SciNumber("1.1") + SciNumber("2")).precision)
        assertEquals(sf(2), (SciNumber(".11") + SciNumber(".02")).precision)
        assertEquals(sf(1), (SciNumber(".11") + SciNumber(".2")).precision)
        assertEquals(sf(2), (SciNumber(".0011") + SciNumber(".0002")).precision)
        assertEquals(sf(1), (SciNumber(".0011") + SciNumber(".002")).precision)

        assertEquals(sf(3), (SciNumber("123") + SciNumber(".456")).precision)
    }

    @Test
    fun roundingUpGainsSigfig() {
        assertEquals(sf(2), (SciNumber("6") + SciNumber("8")).precision)
        assertEquals(sf(2), (SciNumber("100") - SciNumber("1")).precision)
    }

    @Test
    fun precisionMinus() {
        assertEquals(sf(1), (SciNumber("1") - SciNumber("2")).precision)
        assertEquals(sf(1), (SciNumber("2") - SciNumber("1")).precision)
        assertEquals(sf(1), (SciNumber("4") - SciNumber("5")).precision)
    }

    @Test
    fun precisionMultiplication() {
        assertEquals(sf(1), (SciNumber("1") * SciNumber("2")).precision)
        assertEquals(sf(1), (SciNumber("8") * SciNumber("4")).precision) // Carry, 8*4 = 32

        assertEquals(sf(1), (SciNumber("1") * SciNumber("20.")).precision)
        assertEquals(sf(1), (SciNumber("8") * SciNumber("40.")).precision)

        assertEquals(sf(2), (SciNumber("10.") * SciNumber("20.")).precision)
        assertEquals(sf(2), (SciNumber("80.") * SciNumber("40.")).precision)
        assertEquals(sf(3), (SciNumber("10.0") * SciNumber("20.0")).precision)
    }

    @Test
    fun precisionDivision() {
        assertEquals(sf(3), (SciNumber("10.0") / SciNumber("20.0")).precision)
        assertEquals(sf(3), (SciNumber("50.0") / SciNumber("10.0")).precision)
    }

    @Test
    fun transcendentalLeavesInsignificantDigits() {
        // Check that non-significant figures are also calculated
        assertEquals(Math.sin(1.0), SciNumber("1").sin().toDouble())
        assertEquals(Math.sin(1.0), SciNumber("1.00").sin().toDouble())
    }

    @Test
    fun arithmeticLeavesInsignificantDigits() {
        assertEquals(123.456, (SciNumber("123") + SciNumber(".456")).toDouble())
    }

    @Test
    fun divisionNontruncating() {
        assertEquals(.001, (SciNumber("1") / SciNumber("1000")).toDouble())
    }

    // Test that other operations preserve precision
    @Test
    fun powPrecision() {
        // May not be correct https://en.wikipedia.org/wiki/Significance_arithmetic#Transcendental_functions
        assertEquals(Precision.Infinite, SciNumber(10).pow(5).precision)
        assertEquals(sf(2), SciNumber("10").pow(5).precision)
    }
}