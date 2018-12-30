package com.ethshea.digits

import com.ethshea.digits.evaluator.SciNumber
import com.ethshea.digits.evaluator.SciNumber.Real
import com.ethshea.digits.human.HumanQuantity
import com.ethshea.digits.human.HumanQuantityString
import com.ethshea.digits.evaluator.evaluateExpression
import com.ethshea.digits.human.HumanUnit
import com.ethshea.digits.human.humanize
import junit.framework.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Ethan
 */
class HumanQuantityTest {
    @Test
    fun useDecimalNotation() {
        assertEquals("0.05", humanize(evaluateExpression("1/20").value).humanString().string)
        assertEquals("56000", humanize(evaluateExpression("56k").value).humanString().string)
        assertEquals("1.…ᴇ20", humanize(evaluateExpression("1.0ᴇ20").value).humanString(6).string)
    }

    @Test
    fun engineeringNotation() {
        assertEquals("123", humanize(evaluateExpression("123").value).humanString().string)

        assertEquals("1m", humanize(evaluateExpression("1m").value).humanString().string)
        assertEquals("123m", humanize(evaluateExpression("123m").value).humanString().string)
        assertEquals("12.3m", humanize(evaluateExpression("12.3m").value).humanString().string)
        assertEquals("1.23m", humanize(evaluateExpression("1.23m").value).humanString().string)
        assertEquals("123mm", humanize(evaluateExpression(".123m").value).humanString().string)
    }

    @Test
    fun limitedHumanString() {
        assertEquals("1.2…ᴇ-1", HumanQuantity(SciNumber.Real(".1234567"), HumanUnit(mapOf())).humanString(7).string)
        assertEquals("0.12345", HumanQuantity(SciNumber.Real(".12345"), HumanUnit(mapOf())).humanString(7).string)
        assertEquals("0.12345", HumanQuantity(SciNumber.Real(".12345"), HumanUnit(mapOf())).humanString(10).string)
        assertEquals("1.2…ᴇ-4", HumanQuantity(SciNumber.Real(".00012345"), HumanUnit(mapOf())).humanString(7).string)
        assertEquals("1.2345…ᴇ-4", HumanQuantity(SciNumber.Real(".000123456"), HumanUnit(mapOf())).humanString(10).string)

        assertEquals("12345", HumanQuantity(SciNumber.Real("12345"), HumanUnit(mapOf())).humanString(7).string)
        assertEquals("12345", HumanQuantity(SciNumber.Real("12345"), HumanUnit(mapOf())).humanString(10).string)

        assertEquals("1.234…ᴇ8", HumanQuantity(SciNumber.Real("123456789"), HumanUnit(mapOf())).humanString(8).string)

        // Untested is small maxChars values < 4
    }

    @Test
    fun negativeLimitedHumanString() {
        assertEquals("-1.2…ᴇ-1", HumanQuantity(SciNumber.Real("-.1234567"), HumanUnit(mapOf())).humanString(8).string)
        assertEquals("-0.12345", HumanQuantity(SciNumber.Real("-.12345"), HumanUnit(mapOf())).humanString(8).string)
        assertEquals("-0.12345", HumanQuantity(SciNumber.Real("-.12345"), HumanUnit(mapOf())).humanString(11).string)
        assertEquals("-1.2…ᴇ-4", HumanQuantity(SciNumber.Real("-.00012345"), HumanUnit(mapOf())).humanString(8).string)
        assertEquals("-1.2345…ᴇ-4", HumanQuantity(SciNumber.Real("-.000123456"), HumanUnit(mapOf())).humanString(11).string)

        assertEquals("-12345", HumanQuantity(SciNumber.Real("-12345"), HumanUnit(mapOf())).humanString(8).string)
        assertEquals("-12345", HumanQuantity(SciNumber.Real("-12345"), HumanUnit(mapOf())).humanString(11).string)

        assertEquals("-1.234…ᴇ8", HumanQuantity(SciNumber.Real("-123456789"), HumanUnit(mapOf())).humanString(9).string)

        // Untested is small maxChars values < 4
    }

    @Test
    fun addsZeroes() {
        assertEquals("0.500", HumanQuantity(SciNumber.Real(".5", sf(3)), HumanUnit(mapOf())).humanString().string)
        assertEquals("5.00", HumanQuantity(SciNumber.Real("5", sf(3)), HumanUnit(mapOf())).humanString().string)
        assertEquals("5.00m", HumanQuantity(SciNumber.Real("5", sf(3)), HumanUnit(mapOf(u("m") to 1))).humanString().string)

        assertEquals("-0.500", HumanQuantity(SciNumber.Real("-.5", sf(3)), HumanUnit(mapOf())).humanString().string)
        assertEquals("-5.00", HumanQuantity(SciNumber.Real("-5", sf(3)), HumanUnit(mapOf())).humanString().string)
        assertEquals("-5.00m", HumanQuantity(SciNumber.Real("-5", sf(3)), HumanUnit(mapOf(u("m") to 1))).humanString().string)
    }

    @Test
    fun insignificantPosition() {
        testInsigfigStart(1, "99.99", 1)
        testInsigfigStart(2, "99.99", 2) // Before the period
        testInsigfigStart(3, "0.2", 1)  // With a leading 0
        testInsigfigStart(3, "0.22", 1)
        testInsigfigStart(5, "0.002", 1)
        testInsigfigStart(5, "0.0020", 1)
        testInsigfigStart(5, "0.0022", 1)

        testInsigfigStart(2, "-99.99", 1)
        testInsigfigStart(3, "-99.99", 2) // Before the period
        testInsigfigStart(4, "-0.2", 1)  // With a leading 0
        testInsigfigStart(4, "-0.22", 1)
        testInsigfigStart(6, "-0.002", 1)
        testInsigfigStart(6, "-0.0020", 1)
        testInsigfigStart(6, "-0.0022", 1)
    }


    @Test
    fun insignificantWithEllipsis() {
        testHumanString(HumanQuantityString("1.2345…ᴇ3", 6, 6), "1234.56789", 10, 9)
        testHumanString(HumanQuantityString("1.3333…ᴇ3", 3, 6), "1333.33333", 2, 9)
        testHumanString(HumanQuantityString("1.3333…ᴇ3", 6, 6), "1333.33333", 5, 9)

        testHumanString(HumanQuantityString("-1.2345…ᴇ3", 7, 7), "-1234.56789", 10, 10)
        testHumanString(HumanQuantityString("-1.3333…ᴇ3", 4, 7), "-1333.33333", 2, 10)
        testHumanString(HumanQuantityString("-1.3333…ᴇ3", 7, 7), "-1333.33333", 5, 10)

        // No E for *10^0
        testHumanString(HumanQuantityString("1.333333…", 6, 8), "1.333333333", 5, 9)
    }

    @Test
    fun zeroCharacters() {
        testHumanString(HumanQuantityString("", 0, 0), "-1234.56789", 10, 0)
    }

    private fun testInsigfigStart(expectedPos: Int, value: String, sigFigs: Int) {
        Assert.assertEquals(expectedPos, HumanQuantity(SciNumber.Real(value, sf(sigFigs)), HumanUnit(mapOf())).humanString().insigfigStart)
    }

    private fun testHumanString(expected: HumanQuantityString, value: String, sigFigs: Int, maxChars: Int) {
        Assert.assertEquals(expected, HumanQuantity(SciNumber.Real(value, sf(sigFigs)), HumanUnit(mapOf())).humanString(maxChars))
    }
}