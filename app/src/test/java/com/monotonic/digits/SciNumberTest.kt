package com.monotonic.digits

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

class SciNumberTest {
    @Test
    fun precisionLiteral() {
        assertEquals(sf(1), SciNumber.Real("1").precision)
        assertEquals(Precision.Infinite, SciNumber.Real(10).precision)
        assertEquals(sf(2), SciNumber.Real("10").precision)
        assertEquals(sf(2), SciNumber.Real("10.").precision)
        assertEquals(sf(3), SciNumber.Real("10.0").precision)
        assertEquals(sf(2), SciNumber.Real("11").precision)
        assertEquals(sf(3), SciNumber.Real("110").precision)
        assertEquals(sf(3), SciNumber.Real("1.00").precision)
        assertEquals(sf(3), SciNumber.Real("01.00").precision)

        assertEquals(sf(1), (SciNumber.Real(".01")).precision)
        assertEquals(sf(1), SciNumber.Real(".01").precision)
        assertEquals(sf(2), SciNumber.Real(".011").precision)
        assertEquals(sf(1), SciNumber.Real(".001").precision)
        assertEquals(sf(2), SciNumber.Real(".0010").precision)

        assertEquals(sf(3), SciNumber.Real("99.1").precision)
    }

    @Test
    fun negativeLiteral() {
        assertEquals(sf(1), SciNumber.Real("-1").precision)
        assertEquals(sf(2), SciNumber.Real("-10").precision)
        assertEquals(sf(3), SciNumber.Real("-100").precision)

        assertEquals(sf(1), SciNumber.Real("-.1").precision)
        assertEquals(sf(2), SciNumber.Real("-.010").precision)
        assertEquals(sf(3), SciNumber.Real("-.00100").precision)
    }

    @Test
    fun negate() {
        assertEquals(sf(1), (-SciNumber.Real("4")).precision)
        assertEquals(sf(3), (-SciNumber.Real("400.")).precision)
        assertEquals(Precision.Infinite, (-SciNumber.Real(400)).precision)
    }

    @Test
    fun reciprocal() {
        assertEquals(sf(1), (SciNumber.Real("4").reciprocal()).precision)
        assertEquals(sf(3), (SciNumber.Real("400.").reciprocal()).precision)
        assertEquals(Precision.Infinite, (SciNumber.Real(400).reciprocal()).precision)
    }

    @Test
    fun parseDecimal() {
        assertTrue(SciNumber.Real(".01") < SciNumber.Real("1"))
        assertTrue(SciNumber.Real("00.01") < SciNumber.Real("1"))
    }

    @Test
    fun precisionTranscendental() {
        assertEquals(sf(1), SciNumber.Real("1").sin().precision)
        assertEquals(sf(3), SciNumber.Real("1.00").sin().precision)
        assertEquals(sf(3), SciNumber.Real("01.00").sin().precision)
    }

    @Test
    fun magnitudeCorrect() {
        assertEquals(-2, SciNumber.Real(".001").magnitude)
        assertEquals(-1, SciNumber.Real(".01").magnitude)
        assertEquals(0, SciNumber.Real(".1").magnitude)
        assertEquals(1, SciNumber.Real("1").magnitude)
        assertEquals(2, SciNumber.Real("10").magnitude)
        assertEquals(2, SciNumber.Real("10.").magnitude)
        assertEquals(3, SciNumber.Real("100").magnitude)

        assertEquals(-2, SciNumber.Real(".002").magnitude)
        assertEquals(-1, SciNumber.Real(".02").magnitude)
        assertEquals(0, SciNumber.Real(".2").magnitude)
        assertEquals(1, SciNumber.Real("2").magnitude)
        assertEquals(2, SciNumber.Real("20").magnitude)
        assertEquals(2, SciNumber.Real("20.").magnitude)
        assertEquals(3, SciNumber.Real("200").magnitude)

        assertEquals(-2, SciNumber.Real(".009").magnitude)
        assertEquals(-1, SciNumber.Real(".09").magnitude)
        assertEquals(0, SciNumber.Real(".9").magnitude)
        assertEquals(1, SciNumber.Real("9").magnitude)
        assertEquals(2, SciNumber.Real("90").magnitude)
        assertEquals(2, SciNumber.Real("90.").magnitude)
        assertEquals(3, SciNumber.Real("900").magnitude)
    }

    @Test
    fun precisionAddition() {
        assertEquals(sf(1), (SciNumber.Real("1") + SciNumber.Real("2")).precision)
        assertEquals(sf(2), (SciNumber.Real("10") + SciNumber.Real("20")).precision)
        assertEquals(sf(2), (SciNumber.Real("10.") + SciNumber.Real("20.")).precision)
        assertEquals(sf(3), (SciNumber.Real("10.0") + SciNumber.Real("20.0")).precision)

        assertEquals(sf(1), (SciNumber.Real(".01") + SciNumber.Real("2")).precision)
        assertEquals(sf(1), (SciNumber.Real(".01") + SciNumber.Real(".02")).precision)

        assertEquals(sf(2), (SciNumber.Real("11") + SciNumber.Real("2")).precision)
        assertEquals(sf(2), (SciNumber.Real("11") + SciNumber.Real("20")).precision)
        assertEquals(sf(2), (SciNumber.Real("11.0") + SciNumber.Real("2")).precision)
        assertEquals(sf(2), (SciNumber.Real("1.1") + SciNumber.Real(".2")).precision)
        assertEquals(sf(1), (SciNumber.Real("1.1") + SciNumber.Real("2")).precision)
        assertEquals(sf(2), (SciNumber.Real(".11") + SciNumber.Real(".02")).precision)
        assertEquals(sf(1), (SciNumber.Real(".11") + SciNumber.Real(".2")).precision)
        assertEquals(sf(2), (SciNumber.Real(".0011") + SciNumber.Real(".0002")).precision)
        assertEquals(sf(1), (SciNumber.Real(".0011") + SciNumber.Real(".002")).precision)

        assertEquals(sf(3), (SciNumber.Real("123") + SciNumber.Real(".456")).precision)
    }

    @Test
    fun roundingUpGainsSigfig() {
        assertEquals(sf(2), (SciNumber.Real("6") + SciNumber.Real("8")).precision)
        assertEquals(sf(2), (SciNumber.Real("100") - SciNumber.Real("1")).precision)
    }

    @Test
    fun precisionMinus() {
        assertEquals(sf(1), (SciNumber.Real("1") - SciNumber.Real("2")).precision)
        assertEquals(sf(1), (SciNumber.Real("2") - SciNumber.Real("1")).precision)
        assertEquals(sf(1), (SciNumber.Real("4") - SciNumber.Real("5")).precision)
    }

    @Test
    fun precisionMultiplication() {
        assertEquals(sf(1), (SciNumber.Real("1") * SciNumber.Real("2")).precision)
        assertEquals(sf(1), (SciNumber.Real("8") * SciNumber.Real("4")).precision) // Carry, 8*4 = 32

        assertEquals(sf(1), (SciNumber.Real("1") * SciNumber.Real("20.")).precision)
        assertEquals(sf(1), (SciNumber.Real("8") * SciNumber.Real("40.")).precision)

        assertEquals(sf(2), (SciNumber.Real("10.") * SciNumber.Real("20.")).precision)
        assertEquals(sf(2), (SciNumber.Real("80.") * SciNumber.Real("40.")).precision)
        assertEquals(sf(3), (SciNumber.Real("10.0") * SciNumber.Real("20.0")).precision)
    }

    @Test
    fun precisionDivision() {
        assertEquals(sf(3), (SciNumber.Real("10.0") / SciNumber.Real("20.0")).precision)
        assertEquals(sf(3), (SciNumber.Real("50.0") / SciNumber.Real("10.0")).precision)
    }

    @Test
    fun transcendentalLeavesInsignificantDigits() {
        // Check that non-significant figures are also calculated
        assertEquals(Math.sin(1.0), SciNumber.Real("1").sin().toDouble())
        assertEquals(Math.sin(1.0), SciNumber.Real("1.00").sin().toDouble())
    }

    @Test
    fun arithmeticLeavesInsignificantDigits() {
        assertEquals(123.456, (SciNumber.Real("123") + SciNumber.Real(".456")).toDouble())
    }

    @Test
    fun divisionNontruncating() {
        assertEquals(.001, (SciNumber.Real("1") / SciNumber.Real("1000")).toDouble())
    }

    @Test
    fun trigSigfigs() {
        // Doesn't account for condition numbers
        assertEquals(Precision.Infinite, SciNumber.Real(5).sin().precision)
        assertEquals(sf(2), SciNumber.Real(".5", sf(2)).sin().precision)
        assertEquals(Precision.Infinite, SciNumber.Real(5).cos().precision)
        assertEquals(sf(2), SciNumber.Real(".5", sf(2)).cos().precision)
        assertEquals(Precision.Infinite, SciNumber.Real(5).tan().precision)
        assertEquals(sf(2), SciNumber.Real(".5", sf(2)).tan().precision)
    }

    @Test
    fun basicNans() {
        assertEquals(SciNumber.Nan, SciNumber.Nan + SciNumber.One)
        assertEquals(SciNumber.Nan, SciNumber.Nan + SciNumber.Nan)
        assertEquals(SciNumber.Nan, SciNumber.Nan - SciNumber.One)
        assertEquals(SciNumber.Nan, SciNumber.Nan - SciNumber.Nan)
        assertEquals(SciNumber.Nan, SciNumber.Nan * SciNumber.One)
        assertEquals(SciNumber.Nan, SciNumber.Nan * SciNumber.Nan)
        assertEquals(SciNumber.Nan, SciNumber.Nan / SciNumber.One)
        assertEquals(SciNumber.Nan, SciNumber.Nan / SciNumber.Nan)
    }

    @Test
    fun trigProducesNans() {
        assertEquals(SciNumber.Nan, SciNumber.Real(Math.PI).asin())
    }

    @Test
    fun logPrecision() {
        // Not actually how it should work. For now, just keep the original precision
        assertEquals(SciNumber.Real("2", sf(2)), SciNumber.Real("100", sf(2)).log(BigDecimal.TEN))
    }

    // Test that other operations preserve precision
    @Test
    fun powPrecision() {
        // May not be correct https://en.wikipedia.org/wiki/Significance_arithmetic#Transcendental_functions
        assertEquals(Precision.Infinite, SciNumber.Real(10).pow(5).precision)
        assertEquals(sf(2), SciNumber.Real("10").pow(5).precision)
    }
}