package com.monotonic.digits

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.evaluator.evaluateExpression
import com.monotonic.digits.human.*
import junit.framework.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * @author Ethan
 */
class HumanQuantityTest {
    @Test
    fun useDecimalNotation() {
        assertEquals("0.05", humanize(evaluateExpression("1/20").value).humanString(SeperatorType.NONE).string)
        assertEquals("56000", humanize(evaluateExpression("56k").value).humanString(SeperatorType.NONE).string)
        assertEquals("1.…ᴇ20", humanize(evaluateExpression("1.0ᴇ20").value).humanString(6, SeperatorType.NONE, NumberFormat.SCIENTIFIC).string)
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
        testHumanString("1.2…ᴇ-1", ".1234567", 7, SeperatorType.NONE)
        testHumanString("0.12345", ".12345", 7, SeperatorType.NONE)
        testHumanString("0.12345", ".12345", 10, SeperatorType.NONE)
        testHumanString("1.2…ᴇ-4", ".00012345", 7, SeperatorType.NONE)
        testHumanString("1.2345…ᴇ-4", ".000123456", 10, SeperatorType.NONE)

        testHumanString("12345", "12345", 7, SeperatorType.NONE)
        testHumanString("12345", "12345", 10, SeperatorType.NONE)

        testHumanString("1.234…ᴇ8", "123456789", 8, SeperatorType.NONE)

        // Untested is small maxChars values < 4
    }

    @Test
    fun limitedHumanStringSpace() {
        testHumanString("1.2…ᴇ-1", ".1234567", 7, SeperatorType.SPACE)
        testHumanString("0.123 4", ".1234", 7, SeperatorType.SPACE)
        testHumanString("0.123 4", ".1234", 10, SeperatorType.SPACE)
        testHumanString("0.123", ".123", 7, SeperatorType.SPACE)
        testHumanString("1.234…ᴇ-4", ".000123456", 10, SeperatorType.SPACE)

        testHumanString("12 345", "12345", 7, SeperatorType.SPACE)
        testHumanString("12 345", "12345", 10, SeperatorType.SPACE)

        testHumanString("12 345.678 9", "12345.6789", 15, SeperatorType.SPACE)
        testHumanString("1.234 56…ᴇ4", "12345.6789", 11, SeperatorType.SPACE)
        testHumanString("1.234…ᴇ8", "123456789", 8, SeperatorType.SPACE)
    }

    @Test
    fun negativeLimitedHumanStringNoSeparator() {
        testHumanString("-1.2…ᴇ-1", "-.1234567", 8, SeperatorType.NONE)
        testHumanString("-0.12345", "-.12345", 8, SeperatorType.NONE)
        testHumanString("-0.12345", "-.12345", 11, SeperatorType.NONE)
        testHumanString("-1.2…ᴇ-4", "-.00012345", 8, SeperatorType.NONE)
        testHumanString("-1.2345…ᴇ-4", "-.000123456", 11, SeperatorType.NONE)

        testHumanString("-12345", "-12345", 8, SeperatorType.NONE)
        testHumanString("-12345", "-12345", 11, SeperatorType.NONE)

        testHumanString("-1.234…ᴇ8", "-123456789", 9, SeperatorType.NONE)

        // Untested is small maxChars values < 4
    }

    @Test
    fun negativeLimitedHumanStringSpace() {
        testHumanString("-1.2…ᴇ-1", "-.1234567", 8, SeperatorType.SPACE)
        testHumanString("-0.123 4", "-.1234", 8, SeperatorType.SPACE)
        testHumanString("-0.123 4", "-.1234", 11, SeperatorType.SPACE)
        testHumanString("-1.2…ᴇ-4", "-.00012345", 8, SeperatorType.SPACE)
        testHumanString("-1.234…ᴇ-4", "-.000123456", 11, SeperatorType.SPACE)

        testHumanString("-12 345", "-12345",7, SeperatorType.SPACE)
        testHumanString("-12 345", "-12345",11, SeperatorType.SPACE)

        testHumanString("-1.234…ᴇ8", "-123456789", 9, SeperatorType.SPACE)
        testHumanString("-1.234…ᴇ8", "-123456789", 10, SeperatorType.SPACE)
        testHumanString("-1.234 5…ᴇ8", "-123456789", 11, SeperatorType.SPACE)

        // Untested is small maxChars values < 4
    }
    
    @Test
    fun engineeringExponents() {
        testHumanString("123.4…ᴇ6", "123456789", 8, SeperatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("12.34…ᴇ6", "12345678.0", 8, SeperatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("1.234…ᴇ6", "1234567.00", 8, SeperatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("123.4…ᴇ3", "123456.000", 8, SeperatorType.NONE, NumberFormat.ENGINEERING)

        testHumanString("123.45…ᴇ-6", ".000123456", 10, SeperatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("12.345…ᴇ-6", ".0000123456", 10, SeperatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("1.2345…ᴇ-6", ".00000123456", 10, SeperatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("123.45…ᴇ-9", ".000000123456", 10, SeperatorType.NONE, NumberFormat.ENGINEERING)

        testHumanString("12345", "12345", 7, SeperatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("12345", "12345", 10, SeperatorType.NONE, NumberFormat.ENGINEERING)
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
        testInsigfigStart(3, "777.9", 3, SeperatorType.SPACE)
        testInsigfigStart(5, "777.9", 4, SeperatorType.SPACE)
        testInsigfigStart(1, "999777.99", 1, SeperatorType.SPACE)
        testInsigfigStart(3, "999777.99", 3, SeperatorType.SPACE)
        testInsigfigStart(11, "9999.9999", 8, SeperatorType.SPACE)
        testInsigfigStart(5, "7.999", 4, SeperatorType.SPACE)

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
        testSigfigString(SigfigString("1.2345…ᴇ3", 6, 6), "1234.56789", 10, 9, SeperatorType.NONE)
        testSigfigString(SigfigString("1.3333…ᴇ3", 3, 6), "1333.33333", 2, 9, SeperatorType.NONE)
        testSigfigString(SigfigString("1.3333…ᴇ3", 6, 6), "1333.33333", 5, 9, SeperatorType.NONE)

        testSigfigString(SigfigString("-1.2345…ᴇ3", 7, 7), "-1234.56789", 10, 10, SeperatorType.NONE)
        testSigfigString(SigfigString("-1.3333…ᴇ3", 4, 7), "-1333.33333", 2, 10, SeperatorType.NONE)
        testSigfigString(SigfigString("-1.3333…ᴇ3", 7, 7), "-1333.33333", 5, 10, SeperatorType.NONE)

        // No E for *10^0
        testSigfigString(SigfigString("1.333333…", 6, 8), "1.333333333", 5, 9, SeperatorType.NONE)
    }

    @Test
    fun basicValueString() {
        testValueString("1.234", "1.234", sf(4))
        testValueString("1.2", "1.234", sf(2))
        testValueString("3.33300000000", "3.333", Precision.Infinite)
        testValueString("3.33333333333", "3.3333333333333333333", Precision.Infinite)

        testValueString("12.3", "12.34", sf(3))
        testValueString("12", "12.34", sf(2))

        testValueString("123400", "123400", sf(4))
        testValueString("120000", "123400", sf(2))
        testValueString("333300.000000", "333300", Precision.Infinite)
        testValueString("333300000000", "333300000000", Precision.Infinite)

        testValueString("0.001234", ".001234", sf(4))
        testValueString("0.0012", ".001234", sf(2))
        testValueString("0.00000003333", ".00000003333", sf(4))
    }

    @Test
    fun valueStringUsesScientificWhenTooLong() {
        testValueString("3.33333333333ᴇ19", "33333333333333333333", Precision.Infinite)
        testValueString("3.333ᴇ-17", ".00000000000000003333", sf(4))
        testValueString("3.33333333333ᴇ-3", ".0033333333333333333333", Precision.Infinite)
        testValueString("3.33300000000ᴇ-3", ".003333", Precision.Infinite)
    }

    @Test
    fun valueStringWithUnit() {
        testValueString("1.234m", "1.234", sf(4), HumanUnit(mapOf(u("m") to 1)))
        testValueString("1.2m", "1.234", sf(2), HumanUnit(mapOf(u("m") to 1)))
        testValueString("3.33300000000m", "3.333", Precision.Infinite, HumanUnit(mapOf(u("m") to 1)))
        testValueString("3.33333333333m", "3.3333333333333333333", Precision.Infinite, HumanUnit(mapOf(u("m") to 1)))
        testValueString("3.333ᴇ-17m", ".00000000000000003333", sf(4), HumanUnit(mapOf(u("m") to 1)))
    }

    @Test
    fun valueStringConstant() {
        Assert.assertEquals("3.14159265358", HumanQuantity(SciNumber.Real(Math.PI), HumanUnit(mapOf())).valueString())
        Assert.assertEquals("3.14159265358m", HumanQuantity(SciNumber.Real(Math.PI), HumanUnit(mapOf(u("m") to 1))).valueString())
    }

    @Test
    fun zeroCharacters() {
        testSigfigString(SigfigString("",0), "-1234.56789", 10, 0, SeperatorType.NONE)
    }

    private fun testInsigfigStart(expectedPos: Int, value: String, sigFigs: Int, seperatorType: SeperatorType) {
        Assert.assertEquals(expectedPos, HumanQuantity(SciNumber.Real(value, sf(sigFigs)), HumanUnit(mapOf())).humanString(seperatorType).insigfigStart)
    }

    private fun testHumanString(expected: String, value: String, maxChars: Int, seperatorType: SeperatorType, numberFormat: NumberFormat = NumberFormat.SCIENTIFIC) {
        Assert.assertEquals(expected, HumanQuantity(SciNumber.Real(value), HumanUnit(mapOf())).humanString(maxChars, seperatorType, numberFormat).string)
    }

    private fun testSigfigString(expected: SigfigString, value: String, sigFigs: Int, maxChars: Int, seperatorType: SeperatorType, numberFormat: NumberFormat = NumberFormat.SCIENTIFIC) {
        Assert.assertEquals(expected, HumanQuantity(SciNumber.Real(value, sf(sigFigs)), HumanUnit(mapOf())).humanString(maxChars, seperatorType, numberFormat))
    }

    private fun testValueString(expected: String, value: String, precision: Precision) {
        Assert.assertEquals(expected, HumanQuantity(SciNumber.Real(value, precision), HumanUnit(mapOf())).valueString())
    }

    private fun testValueString(expected: String, value: String, precision: Precision, unit: HumanUnit) {
        Assert.assertEquals(expected, HumanQuantity(SciNumber.Real(value, precision), unit).valueString())
    }
}