package com.monotonic.digits

import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.evaluator.evaluateExpression
import com.monotonic.digits.human.*
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Ethan
 */
class HumanQuantityTest {
    @Test
    fun useDecimalNotation() {
        assertEquals("0.05", humanize(evaluateExpression("1/20").value).humanString(SeparatorType.NONE).string)
        assertEquals("56000", humanize(evaluateExpression("56k").value).humanString(SeparatorType.NONE).string)
        assertEquals("1.…ᴇ20", humanize(evaluateExpression("1.0ᴇ20").value).humanString(6, SeparatorType.NONE, NumberFormat.SCIENTIFIC).string)
    }

    @Test
    fun engineeringNotation() {
        assertEquals("123", humanize(evaluateExpression("123").value).humanString(SeparatorType.NONE).string)

        assertEquals("1m", humanize(evaluateExpression("1m").value).humanString(SeparatorType.NONE).string)
        assertEquals("123m", humanize(evaluateExpression("123m").value).humanString(SeparatorType.NONE).string)
        assertEquals("12.3m", humanize(evaluateExpression("12.3m").value).humanString(SeparatorType.NONE).string)
        assertEquals("1.23m", humanize(evaluateExpression("1.23m").value).humanString(SeparatorType.NONE).string)
        assertEquals("123mm", humanize(evaluateExpression(".123m").value).humanString(SeparatorType.NONE).string)
    }

    @Test
    fun limitedHumanStringNoSeparator() {
        testHumanString("1.2…ᴇ-1", ".1234567", 7, SeparatorType.NONE)
        testHumanString("0.12345", ".12345", 7, SeparatorType.NONE)
        testHumanString("0.12345", ".12345", 10, SeparatorType.NONE)
        testHumanString("1.2…ᴇ-4", ".00012345", 7, SeparatorType.NONE)
        testHumanString("1.2345…ᴇ-4", ".000123456", 10, SeparatorType.NONE)

        testHumanString("12345", "12345", 7, SeparatorType.NONE)
        testHumanString("12345", "12345", 10, SeparatorType.NONE)

        testHumanString("1.234…ᴇ8", "123456789", 8, SeparatorType.NONE)

        // Untested is small maxChars values < 4
    }

    @Test
    fun limitedHumanStringSpace() {
        testHumanString("1.2…ᴇ-1", ".1234567", 7, SeparatorType.SPACE)
        testHumanString("0.123 4", ".1234", 7, SeparatorType.SPACE)
        testHumanString("0.123 4", ".1234", 10, SeparatorType.SPACE)
        testHumanString("0.123", ".123", 7, SeparatorType.SPACE)
        testHumanString("1.234…ᴇ-4", ".000123456", 10, SeparatorType.SPACE)

        testHumanString("12 345", "12345", 7, SeparatorType.SPACE)
        testHumanString("12 345", "12345", 10, SeparatorType.SPACE)

        testHumanString("12 345.678 9", "12345.6789", 15, SeparatorType.SPACE)
        testHumanString("1.234 56…ᴇ4", "12345.6789", 11, SeparatorType.SPACE)
        testHumanString("1.234…ᴇ8", "123456789", 8, SeparatorType.SPACE)
    }

    @Test
    fun negativeLimitedHumanStringNoSeparator() {
        testHumanString("-1.2…ᴇ-1", "-.1234567", 8, SeparatorType.NONE)
        testHumanString("-0.12345", "-.12345", 8, SeparatorType.NONE)
        testHumanString("-0.12345", "-.12345", 11, SeparatorType.NONE)
        testHumanString("-1.2…ᴇ-4", "-.00012345", 8, SeparatorType.NONE)
        testHumanString("-1.2345…ᴇ-4", "-.000123456", 11, SeparatorType.NONE)

        testHumanString("-12345", "-12345", 8, SeparatorType.NONE)
        testHumanString("-12345", "-12345", 11, SeparatorType.NONE)

        testHumanString("-1.234…ᴇ8", "-123456789", 9, SeparatorType.NONE)

        // Untested is small maxChars values < 4
    }

    @Test
    fun negativeLimitedHumanStringSpace() {
        testHumanString("-1.2…ᴇ-1", "-.1234567", 8, SeparatorType.SPACE)
        testHumanString("-0.123 4", "-.1234", 8, SeparatorType.SPACE)
        testHumanString("-0.123 4", "-.1234", 11, SeparatorType.SPACE)
        testHumanString("-1.2…ᴇ-4", "-.00012345", 8, SeparatorType.SPACE)
        testHumanString("-1.234…ᴇ-4", "-.000123456", 11, SeparatorType.SPACE)

        testHumanString("-12 345", "-12345",7, SeparatorType.SPACE)
        testHumanString("-12 345", "-12345",11, SeparatorType.SPACE)

        testHumanString("-1.234…ᴇ8", "-123456789", 9, SeparatorType.SPACE)
        testHumanString("-1.234…ᴇ8", "-123456789", 10, SeparatorType.SPACE)
        testHumanString("-1.234 5…ᴇ8", "-123456789", 11, SeparatorType.SPACE)

        // Untested is small maxChars values < 4
    }
    
    @Test
    fun engineeringExponents() {
        testHumanString("123.4…ᴇ6", "123456789", 8, SeparatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("12.34…ᴇ6", "12345678.0", 8, SeparatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("1.234…ᴇ6", "1234567.00", 8, SeparatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("123.4…ᴇ3", "123456.000", 8, SeparatorType.NONE, NumberFormat.ENGINEERING)

        testHumanString("123.45…ᴇ-6", ".000123456", 10, SeparatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("12.345…ᴇ-6", ".0000123456", 10, SeparatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("1.2345…ᴇ-6", ".00000123456", 10, SeparatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("123.45…ᴇ-9", ".000000123456", 10, SeparatorType.NONE, NumberFormat.ENGINEERING)

        testHumanString("12345", "12345", 7, SeparatorType.NONE, NumberFormat.ENGINEERING)
        testHumanString("12345", "12345", 10, SeparatorType.NONE, NumberFormat.ENGINEERING)
    }

    @Test
    fun addsZeroes() {
        assertEquals("0.500", HumanQuantity(SciNumber.Real(".5", sf(3)), HumanUnit(mapOf())).humanString(SeparatorType.NONE).string)
        assertEquals("5.00", HumanQuantity(SciNumber.Real("5", sf(3)), HumanUnit(mapOf())).humanString(SeparatorType.NONE).string)
        assertEquals("5.00m", HumanQuantity(SciNumber.Real("5", sf(3)), HumanUnit(mapOf(u("m") to 1))).humanString(SeparatorType.NONE).string)

        assertEquals("-0.500", HumanQuantity(SciNumber.Real("-.5", sf(3)), HumanUnit(mapOf())).humanString(SeparatorType.NONE).string)
        assertEquals("-5.00", HumanQuantity(SciNumber.Real("-5", sf(3)), HumanUnit(mapOf())).humanString(SeparatorType.NONE).string)
        assertEquals("-5.00m", HumanQuantity(SciNumber.Real("-5", sf(3)), HumanUnit(mapOf(u("m") to 1))).humanString(SeparatorType.NONE).string)
    }

    @Test
    fun insignificantPosition() {
        testInsigfigStart(1, "9900", 1, SeparatorType.NONE)
        testInsigfigStart(2, "9900", 2, SeparatorType.NONE)
        testInsigfigStart(1, "99.99", 1, SeparatorType.NONE)
        testInsigfigStart(2, "99.99", 2, SeparatorType.NONE) // Before the period
        testInsigfigStart(3, "0.2", 1, SeparatorType.NONE)  // With a leading 0
        testInsigfigStart(3, "0.22", 1, SeparatorType.NONE)
        testInsigfigStart(5, "0.002", 1, SeparatorType.NONE)
        testInsigfigStart(5, "0.0020", 1, SeparatorType.NONE)
        testInsigfigStart(5, "0.0022", 1, SeparatorType.NONE)

        testInsigfigStart(2, "-99.99", 1, SeparatorType.NONE)
        testInsigfigStart(3, "-99.99", 2, SeparatorType.NONE) // Before the period
        testInsigfigStart(4, "-0.2", 1, SeparatorType.NONE)  // With a leading 0
        testInsigfigStart(4, "-0.22", 1, SeparatorType.NONE)
        testInsigfigStart(6, "-0.002", 1, SeparatorType.NONE)
        testInsigfigStart(6, "-0.0020", 1, SeparatorType.NONE)
        testInsigfigStart(6, "-0.0022", 1, SeparatorType.NONE)
    }

    @Test
    fun insignificantPositionSpace() {
        testInsigfigStart(1, "99.99", 1, SeparatorType.SPACE)
        testInsigfigStart(1, "9999.99", 1, SeparatorType.SPACE)
        testInsigfigStart(3, "9999.99", 2, SeparatorType.SPACE)
        testInsigfigStart(5, "9999.99", 4, SeparatorType.SPACE)
        testInsigfigStart(3, "777.9", 3, SeparatorType.SPACE)
        testInsigfigStart(5, "777.9", 4, SeparatorType.SPACE)
        testInsigfigStart(1, "999777.99", 1, SeparatorType.SPACE)
        testInsigfigStart(3, "999777.99", 3, SeparatorType.SPACE)
        testInsigfigStart(11, "9999.9999", 8, SeparatorType.SPACE)
        testInsigfigStart(5, "7.999", 4, SeparatorType.SPACE)

        testInsigfigStart(2, "-99.99", 1, SeparatorType.SPACE)
        testInsigfigStart(2, "-9999.99", 1, SeparatorType.SPACE)
        testInsigfigStart(4, "-9999.99", 2, SeparatorType.SPACE)
        testInsigfigStart(6, "-9999.99", 4, SeparatorType.SPACE)
        testInsigfigStart(12, "-9999.9999", 8, SeparatorType.SPACE)

        testInsigfigStart(4, "0.9999", 2, SeparatorType.SPACE)
        testInsigfigStart(7, "0.99999", 4, SeparatorType.SPACE)
        testInsigfigStart(5, "0.009999", 1, SeparatorType.SPACE)
        testInsigfigStart(8, "0.009999", 3, SeparatorType.SPACE)
        testInsigfigStart(6, "-0.009999", 1, SeparatorType.SPACE)

        testInsigfigStart(3, "9999", 2, SeparatorType.SPACE)
        testInsigfigStart(5, "9999", 4, SeparatorType.SPACE)
        testInsigfigStart(1, "999900", 1, SeparatorType.SPACE)
        testInsigfigStart(3, "999900", 3, SeparatorType.SPACE)
        testInsigfigStart( 4, "-999900", 3, SeparatorType.SPACE)
    }


    @Test
    fun insignificantWithEllipsis() {
        testSigfigString(SigfigString("1.2345…ᴇ3", 6, 6), "1234.56789", 10, 9, SeparatorType.NONE)
        testSigfigString(SigfigString("1.3333…ᴇ3", 3, 6), "1333.33333", 2, 9, SeparatorType.NONE)
        testSigfigString(SigfigString("1.3333…ᴇ3", 6, 6), "1333.33333", 5, 9, SeparatorType.NONE)

        testSigfigString(SigfigString("-1.2345…ᴇ3", 7, 7), "-1234.56789", 10, 10, SeparatorType.NONE)
        testSigfigString(SigfigString("-1.3333…ᴇ3", 4, 7), "-1333.33333", 2, 10, SeparatorType.NONE)
        testSigfigString(SigfigString("-1.3333…ᴇ3", 7, 7), "-1333.33333", 5, 10, SeparatorType.NONE)

        // No E for *10^0
        testSigfigString(SigfigString("1.333333…", 6, 8), "1.333333333", 5, 9, SeparatorType.NONE)
    }

    @Test
    fun basicValueString() {
        testValueString("1.234", "1.234", sf(4), RoundingMode.SigFig())
        testValueString("1.2", "1.234", sf(2), RoundingMode.SigFig())
        testValueString("3.33300000000", "3.333", Precision.Infinite, RoundingMode.SigFig())
        testValueString("3.33333333333", "3.3333333333333333333", Precision.Infinite, RoundingMode.SigFig())

        testValueString("12.3", "12.34", sf(3), RoundingMode.SigFig())
        testValueString("12", "12.34", sf(2), RoundingMode.SigFig())

        testValueString("123400", "123400", sf(4), RoundingMode.SigFig())
        testValueString("120000", "123400", sf(2), RoundingMode.SigFig())
        testValueString("333300.000000", "333300", Precision.Infinite, RoundingMode.SigFig())
        testValueString("333300000000", "333300000000", Precision.Infinite, RoundingMode.SigFig())

        testValueString("0.001234", ".001234", sf(4), RoundingMode.SigFig())
        testValueString("0.0012", ".001234", sf(2), RoundingMode.SigFig())
        testValueString("0.00000003333", ".00000003333", sf(4), RoundingMode.SigFig())
    }

    @Test
    fun valueStringUsesScientificWhenTooLong() {
        testValueString("3.33333333333ᴇ19", "33333333333333333333", Precision.Infinite, RoundingMode.SigFig())
        testValueString("3.333ᴇ-17", ".00000000000000003333", sf(4), RoundingMode.SigFig())
        testValueString("3.33333333333ᴇ-3", ".0033333333333333333333", Precision.Infinite, RoundingMode.SigFig())
        testValueString("3.33300000000ᴇ-3", ".003333", Precision.Infinite, RoundingMode.SigFig())
    }

    @Test
    fun valueStringWithUnit() {
        testValueString("1.234m", "1.234", sf(4), HumanUnit(mapOf(u("m") to 1)), RoundingMode.SigFig())
        testValueString("1.2m", "1.234", sf(2), HumanUnit(mapOf(u("m") to 1)), RoundingMode.SigFig())
        testValueString("3.33300000000m", "3.333", Precision.Infinite, HumanUnit(mapOf(u("m") to 1)), RoundingMode.SigFig())
        testValueString("3.33333333333m", "3.3333333333333333333", Precision.Infinite, HumanUnit(mapOf(u("m") to 1)), RoundingMode.SigFig())
        testValueString("3.333ᴇ-17m", ".00000000000000003333", sf(4), HumanUnit(mapOf(u("m") to 1)), RoundingMode.SigFig())
    }

    @Test
    fun valueStringConstant() {
        assertEquals("3.14159265358m", HumanQuantity(SciNumber.Real(Math.PI), HumanUnit(mapOf(u("m") to 1))).valueString(RoundingMode.SigFig()))
    }

    @Test
    fun valueStringRoundingSigfig() {
        testValueString("1.8", "1.78", sf(2), RoundingMode.SigFig())
        testValueString("1.8", "1.75", sf(2), RoundingMode.SigFig())
        testValueString("1.8", "1.78245", sf(2), RoundingMode.SigFig())
        testValueString("1.8", "1.75245", sf(2), RoundingMode.SigFig())

        testValueString("0.8", "0.78245", sf(1), RoundingMode.SigFig())
        testValueString("0.8", "0.75245", sf(1), RoundingMode.SigFig())

        testValueString("0.008", "0.0078245", sf(1), RoundingMode.SigFig())
        testValueString("0.008", "0.0075245", sf(1), RoundingMode.SigFig())

        testValueString("2", "1.8", sf(1), RoundingMode.SigFig())
        testValueString("2", "1.5", sf(1), RoundingMode.SigFig())

        testValueString("20", "18", sf(1), RoundingMode.SigFig())
        testValueString("20", "15", sf(1), RoundingMode.SigFig())
    }

    @Test
    fun roundingCascades() {
        testValueString("1.800", "1.79999", sf(4), RoundingMode.SigFig())
    }

    @Test
    fun roundingStopAtLastCharacter() {
        testValueString("1.78", "1.78", sf(3), RoundingMode.SigFig())
        testValueString("1.75", "1.75", sf(3), RoundingMode.SigFig())
    }

    @Test
    fun valueStringRoundingRemoveTrailing() {
        testValueString("1.78", "1.78", sf(2), RoundingMode.RemoveTrailing())
        testValueString("1.75", "1.75", sf(2), RoundingMode.RemoveTrailing())

        testValueString("1.8", "1.80000", sf(1), RoundingMode.RemoveTrailing())
        testValueString("1.5", "1.5", sf(1), RoundingMode.RemoveTrailing())

        testValueString("20", "20", sf(1), RoundingMode.RemoveTrailing())
        testValueString("20", "20", sf(1), RoundingMode.RemoveTrailing())
    }

    @Test
    fun unusualBackings() {
        assertEquals(SigfigString("5000", 1, 4), SciNumber.Real("5E+3", sf(1)).valueString(SeparatorType.NONE))
        assertEquals(SigfigString("5000", 4, 4), SciNumber.Real("5E+3", Precision.Infinite).valueString(SeparatorType.NONE))
    }

    @Test
    fun zeroCharacters() {
        testSigfigString(SigfigString("",0), "-1234.56789", 10, 0, SeparatorType.NONE)
    }

    private fun testInsigfigStart(expectedPos: Int, value: String, sigFigs: Int, separatorType: SeparatorType) {
        assertEquals(expectedPos, HumanQuantity(SciNumber.Real(value, sf(sigFigs)), HumanUnit(mapOf())).humanString(separatorType).insigfigStart)
    }

    private fun testHumanString(expected: String, value: String, maxChars: Int, separatorType: SeparatorType, numberFormat: NumberFormat = NumberFormat.SCIENTIFIC) {
        assertEquals(expected, HumanQuantity(SciNumber.Real(value), HumanUnit(mapOf())).humanString(maxChars, separatorType, numberFormat).string)
    }

    private fun testSigfigString(expected: SigfigString, value: String, sigFigs: Int, maxChars: Int, separatorType: SeparatorType, numberFormat: NumberFormat = NumberFormat.SCIENTIFIC) {
        assertEquals(expected, HumanQuantity(SciNumber.Real(value, sf(sigFigs)), HumanUnit(mapOf())).humanString(maxChars, separatorType, numberFormat))
    }

    private fun testValueString(expected: String, value: String, precision: Precision, roundingMode: RoundingMode) {
        assertEquals(expected, HumanQuantity(SciNumber.Real(value, precision), HumanUnit(mapOf())).valueString(roundingMode))
    }

    private fun testValueString(expected: String, value: String, precision: Precision, unit: HumanUnit, roundingMode: RoundingMode) {
        assertEquals(expected, HumanQuantity(SciNumber.Real(value, precision), unit).valueString(roundingMode))
    }
}