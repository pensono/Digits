package com.ethshea.digits

import com.ethshea.digits.evaluator.HumanQuantity
import com.ethshea.digits.evaluator.HumanQuantityString
import com.ethshea.digits.evaluator.evaluateExpression
import com.ethshea.digits.units.HumanUnit
import com.ethshea.digits.units.humanize
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
        assertEquals("1.2…E-1", HumanQuantity(SciNumber(".1234567"), HumanUnit(mapOf())).humanString(7).string)
        assertEquals("0.12345", HumanQuantity(SciNumber(".12345"), HumanUnit(mapOf())).humanString(7).string)
        assertEquals("0.12345", HumanQuantity(SciNumber(".12345"), HumanUnit(mapOf())).humanString(10).string)
        assertEquals("1.2…E-4", HumanQuantity(SciNumber(".00012345"), HumanUnit(mapOf())).humanString(7).string)
        assertEquals("1.2345…E-4", HumanQuantity(SciNumber(".000123456"), HumanUnit(mapOf())).humanString(10).string)

        assertEquals("12345", HumanQuantity(SciNumber("12345"), HumanUnit(mapOf())).humanString(7).string)
        assertEquals("12345", HumanQuantity(SciNumber("12345"), HumanUnit(mapOf())).humanString(10).string)

        assertEquals("1.234…E8", HumanQuantity(SciNumber("123456789"), HumanUnit(mapOf())).humanString(8).string)

        // Untested is small maxChars values < 4
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
    }


    @Test
    fun insignificantWithEllipsis() {
        testHumanString(HumanQuantityString("1.2345…E0", 6, 6), "1.23456789", 10, 9)

        testHumanString(HumanQuantityString("1.3333…E0", 3, 6), "1.33333333", 2, 9)
        testHumanString(HumanQuantityString("1.3333…E0", 6, 6), "1.33333333", 5, 9)
    }

    private fun testInsigfigStart(expectedPos: Int, value: String, sigFigs: Int) {
        Assert.assertEquals(expectedPos, HumanQuantity(SciNumber(value, sf(sigFigs)), HumanUnit(mapOf())).humanString().insigfigStart)
    }

    private fun testHumanString(expected: HumanQuantityString, value: String, sigFigs: Int, maxChars: Int) {
        Assert.assertEquals(expected, HumanQuantity(SciNumber(value, sf(sigFigs)), HumanUnit(mapOf())).humanString(maxChars))
    }
}