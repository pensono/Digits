package com.monotonic.digits

import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.evaluator.evaluateExpression
import com.monotonic.digits.human.*
import junit.framework.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Ethan
 */
class HumanQuantityTest {
    @Test
    fun useDecimalNotation() {
        assertEquals("0.05", humanize(evaluateExpression("1/20").value).humanString(SeperatorType.NONE).string)
        assertEquals("56000", humanize(evaluateExpression("56k").value).humanString(SeperatorType.NONE).string)
        assertEquals("1.…ᴇ20", humanize(evaluateExpression("1.0ᴇ20").value).humanString(SeperatorType.NONE, 6).string)
    }

    @Test
    fun engineeringNotation() {
        assertEquals("123", humanize(evaluateExpression("123").value).humanString(SeperatorType.NONE).string)

        assertEquals("1m", humanize(evaluateExpression("1m").value).humanString(SeperatorType.NONE).string)
        assertEquals("123m", humanize(evaluateExpression("123m").value).humanString(SeperatorType.NONE).string)
        assertEquals("12.3m", humanize(evaluateExpression("12.3m").value).humanString(SeperatorType.NONE).string)
        assertEquals("1.23m", humanize(evaluateExpression("1.23m").value).humanString(SeperatorType.NONE).string)
        assertEquals("123mm", humanize(evaluateExpression(".123m").value).humanString(SeperatorType.NONE).string)
    }

    @Test
    fun limitedHumanStringNoSeparator() {
        assertEquals("1.2…ᴇ-1", HumanQuantity(SciNumber.Real(".1234567"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 7).string)
        assertEquals("0.12345", HumanQuantity(SciNumber.Real(".12345"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 7).string)
        assertEquals("0.12345", HumanQuantity(SciNumber.Real(".12345"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 10).string)
        assertEquals("1.2…ᴇ-4", HumanQuantity(SciNumber.Real(".00012345"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 7).string)
        assertEquals("1.2345…ᴇ-4", HumanQuantity(SciNumber.Real(".000123456"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 10).string)

        assertEquals("12345", HumanQuantity(SciNumber.Real("12345"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 7).string)
        assertEquals("12345", HumanQuantity(SciNumber.Real("12345"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 10).string)

        assertEquals("1.234…ᴇ8", HumanQuantity(SciNumber.Real("123456789"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 8).string)

        // Untested is small maxChars values < 4
    }

    @Test
    fun limitedHumanStringSpace() {
        assertEquals("1.2…ᴇ-1", HumanQuantity(SciNumber.Real(".1234567"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 7).string)
        assertEquals("0.123 4", HumanQuantity(SciNumber.Real(".1234"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 7).string)
        assertEquals("0.123 4", HumanQuantity(SciNumber.Real(".1234"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 10).string)
        assertEquals("0.123", HumanQuantity(SciNumber.Real(".123"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 7).string)
        assertEquals("1.234…ᴇ-4", HumanQuantity(SciNumber.Real(".000123456"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 10).string)

        assertEquals("12 345", HumanQuantity(SciNumber.Real("12345"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 7).string)
        assertEquals("12 345", HumanQuantity(SciNumber.Real("12345"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 10).string)

        assertEquals("12 345.678 9", HumanQuantity(SciNumber.Real("12345.6789"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 15).string)
        assertEquals("1.234 56…ᴇ4", HumanQuantity(SciNumber.Real("12345.6789"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 11).string)

        assertEquals("1.234…ᴇ8", HumanQuantity(SciNumber.Real("123456789"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 8).string)
    }

    @Test
    fun negativeLimitedHumanStringNoSeparator() {
        assertEquals("-1.2…ᴇ-1", HumanQuantity(SciNumber.Real("-.1234567"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 8).string)
        assertEquals("-0.12345", HumanQuantity(SciNumber.Real("-.12345"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 8).string)
        assertEquals("-0.12345", HumanQuantity(SciNumber.Real("-.12345"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 11).string)
        assertEquals("-1.2…ᴇ-4", HumanQuantity(SciNumber.Real("-.00012345"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 8).string)
        assertEquals("-1.2345…ᴇ-4", HumanQuantity(SciNumber.Real("-.000123456"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 11).string)

        assertEquals("-12345", HumanQuantity(SciNumber.Real("-12345"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 8).string)
        assertEquals("-12345", HumanQuantity(SciNumber.Real("-12345"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 11).string)

        assertEquals("-1.234…ᴇ8", HumanQuantity(SciNumber.Real("-123456789"), HumanUnit(mapOf())).humanString(SeperatorType.NONE, 9).string)

        // Untested is small maxChars values < 4
    }

    @Test
    fun negativeLimitedHumanStringSpace() {
        assertEquals("-1.2…ᴇ-1", HumanQuantity(SciNumber.Real("-.1234567"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 8).string)
        assertEquals("-0.123 4", HumanQuantity(SciNumber.Real("-.1234"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 8).string)
        assertEquals("-0.123 4", HumanQuantity(SciNumber.Real("-.1234"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 11).string)
        assertEquals("-1.2…ᴇ-4", HumanQuantity(SciNumber.Real("-.00012345"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 8).string)
        assertEquals("-1.234…ᴇ-4", HumanQuantity(SciNumber.Real("-.000123456"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 11).string)

        assertEquals("-12 345", HumanQuantity(SciNumber.Real("-12345"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 7).string)
        assertEquals("-12 345", HumanQuantity(SciNumber.Real("-12345"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 11).string)

        assertEquals("-1.234…ᴇ8", HumanQuantity(SciNumber.Real("-123456789"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 9).string)
        assertEquals("-1.234…ᴇ8", HumanQuantity(SciNumber.Real("-123456789"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 10).string)
        assertEquals("-1.234 5…ᴇ8", HumanQuantity(SciNumber.Real("-123456789"), HumanUnit(mapOf())).humanString(SeperatorType.SPACE, 11).string)

        // Untested is small maxChars values < 4
    }

    @Test
    fun addsZeroes() {
        assertEquals("0.500", HumanQuantity(SciNumber.Real(".5", sf(3)), HumanUnit(mapOf())).humanString(SeperatorType.NONE).string)
        assertEquals("5.00", HumanQuantity(SciNumber.Real("5", sf(3)), HumanUnit(mapOf())).humanString(SeperatorType.NONE).string)
        assertEquals("5.00m", HumanQuantity(SciNumber.Real("5", sf(3)), HumanUnit(mapOf(u("m") to 1))).humanString(SeperatorType.NONE).string)

        assertEquals("-0.500", HumanQuantity(SciNumber.Real("-.5", sf(3)), HumanUnit(mapOf())).humanString(SeperatorType.NONE).string)
        assertEquals("-5.00", HumanQuantity(SciNumber.Real("-5", sf(3)), HumanUnit(mapOf())).humanString(SeperatorType.NONE).string)
        assertEquals("-5.00m", HumanQuantity(SciNumber.Real("-5", sf(3)), HumanUnit(mapOf(u("m") to 1))).humanString(SeperatorType.NONE).string)
    }

    @Test
    fun insignificantPosition() {
        testInsigfigStart(1, "9900", 1, SeperatorType.NONE)
        testInsigfigStart(2, "9900", 2, SeperatorType.NONE)
        testInsigfigStart(1, "99.99", 1, SeperatorType.NONE)
        testInsigfigStart(2, "99.99", 2, SeperatorType.NONE) // Before the period
        testInsigfigStart(3, "0.2", 1, SeperatorType.NONE)  // With a leading 0
        testInsigfigStart(3, "0.22", 1, SeperatorType.NONE)
        testInsigfigStart(5, "0.002", 1, SeperatorType.NONE)
        testInsigfigStart(5, "0.0020", 1, SeperatorType.NONE)
        testInsigfigStart(5, "0.0022", 1, SeperatorType.NONE)

        testInsigfigStart(2, "-99.99", 1, SeperatorType.NONE)
        testInsigfigStart(3, "-99.99", 2, SeperatorType.NONE) // Before the period
        testInsigfigStart(4, "-0.2", 1, SeperatorType.NONE)  // With a leading 0
        testInsigfigStart(4, "-0.22", 1, SeperatorType.NONE)
        testInsigfigStart(6, "-0.002", 1, SeperatorType.NONE)
        testInsigfigStart(6, "-0.0020", 1, SeperatorType.NONE)
        testInsigfigStart(6, "-0.0022", 1, SeperatorType.NONE)
    }

    @Test
    fun insignificantPositionSpace() {
        testInsigfigStart(1, "99.99", 1, SeperatorType.SPACE)
        testInsigfigStart(1, "9999.99", 1, SeperatorType.SPACE)
        testInsigfigStart(3, "9999.99", 2, SeperatorType.SPACE)
        testInsigfigStart(5, "9999.99", 4, SeperatorType.SPACE)
        testInsigfigStart(1, "999777.99", 1, SeperatorType.SPACE)
        testInsigfigStart(3, "999777.99", 3, SeperatorType.SPACE)
        testInsigfigStart(11, "9999.9999", 8, SeperatorType.SPACE)

        testInsigfigStart(2, "-99.99", 1, SeperatorType.SPACE)
        testInsigfigStart(2, "-9999.99", 1, SeperatorType.SPACE)
        testInsigfigStart(4, "-9999.99", 2, SeperatorType.SPACE)
        testInsigfigStart(6, "-9999.99", 4, SeperatorType.SPACE)
        testInsigfigStart(12, "-9999.9999", 8, SeperatorType.SPACE)

        testInsigfigStart(4, "0.9999", 2, SeperatorType.SPACE)
        testInsigfigStart(7, "0.99999", 4, SeperatorType.SPACE)
        testInsigfigStart(5, "0.009999", 1, SeperatorType.SPACE)
        testInsigfigStart(8, "0.009999", 3, SeperatorType.SPACE)
        testInsigfigStart(6, "-0.009999", 1, SeperatorType.SPACE)

        testInsigfigStart(3, "9999", 2, SeperatorType.SPACE)
        testInsigfigStart(5, "9999", 4, SeperatorType.SPACE)
        testInsigfigStart(1, "999900", 1, SeperatorType.SPACE)
        testInsigfigStart(3, "999900", 3, SeperatorType.SPACE)
        testInsigfigStart( 4, "-999900", 3, SeperatorType.SPACE)
    }


    @Test
    fun insignificantWithEllipsis() {
        testHumanString(SigfigString("1.2345…ᴇ3", 6, 6), "1234.56789", 10, 9, SeperatorType.NONE)
        testHumanString(SigfigString("1.3333…ᴇ3", 3, 6), "1333.33333", 2, 9, SeperatorType.NONE)
        testHumanString(SigfigString("1.3333…ᴇ3", 6, 6), "1333.33333", 5, 9, SeperatorType.NONE)

        testHumanString(SigfigString("-1.2345…ᴇ3", 7, 7), "-1234.56789", 10, 10, SeperatorType.NONE)
        testHumanString(SigfigString("-1.3333…ᴇ3", 4, 7), "-1333.33333", 2, 10, SeperatorType.NONE)
        testHumanString(SigfigString("-1.3333…ᴇ3", 7, 7), "-1333.33333", 5, 10, SeperatorType.NONE)

        // No E for *10^0
        testHumanString(SigfigString("1.333333…", 6, 8), "1.333333333", 5, 9, SeperatorType.NONE)
    }

    @Test
    fun zeroCharacters() {
        testHumanString(SigfigString("", 0, 0), "-1234.56789", 10, 0, SeperatorType.NONE)
    }

    private fun testInsigfigStart(expectedPos: Int, value: String, sigFigs: Int, seperatorType: SeperatorType) {
        Assert.assertEquals(expectedPos, HumanQuantity(SciNumber.Real(value, sf(sigFigs)), HumanUnit(mapOf())).humanString(seperatorType).insigfigStart)
    }

    private fun testHumanString(expected: SigfigString, value: String, sigFigs: Int, maxChars: Int, seperatorType: SeperatorType) {
        Assert.assertEquals(expected, HumanQuantity(SciNumber.Real(value, sf(sigFigs)), HumanUnit(mapOf())).humanString(seperatorType, maxChars))
    }
}