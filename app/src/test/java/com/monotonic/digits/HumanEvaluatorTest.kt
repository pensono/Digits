package com.monotonic.digits

import com.monotonic.digits.evaluator.Quantity
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.evaluator.evaluateExpression
import com.monotonic.digits.human.HumanQuantity
import com.monotonic.digits.human.HumanUnit
import com.monotonic.digits.human.evaluateHumanized
import org.antlr.v4.runtime.misc.Interval
import org.junit.Assert
import org.junit.Test

/**
 * @author Ethan
 */

class HumanEvaluatorTest {
    @Test
    fun basicTest() {
        evalTest(HumanQuantity(SciNumber.Real(123)), "123")
        evalTest(HumanQuantity(SciNumber.Real(777)), "123+654")
    }

    @Test
    fun prefersUnits() {
        val milliVolts = HumanUnit(mapOf(u("V") to 1), p("m"))
        val volts = HumanUnit(mapOf(u("V") to 1))
        evalTest(HumanQuantity(SciNumber.Real("5000"), milliVolts), "5V", mapOf(volts.dimensions to milliVolts))
        evalTest(HumanQuantity(SciNumber.Real(".005"), volts), "5mV", mapOf(volts.dimensions to volts))
    }

    // TODO make these tests pass. Humanization should be able to accept "preferred units"
//    assertEquals(HumanQuantity(SciNumber.One, HumanUnit(mapOf(u("m") to 2), PrefixUnit("k", "Kilo", "1e6", ""))), humanize(Quantity(Mega, u("m") * 2)))
//    assertEquals(HumanQuantity(SciNumber.One, HumanUnit(mapOf(u("m") to 2), PrefixUnit("m", "Milli", "1e-6", ""))), humanize(Quantity(Micro, u("m") * 2)))

    @Test
    fun usesUnitsInInput() {
        evalTest(HumanQuantity(SciNumber.Real("5"), HumanUnit(mapOf(u("m") to 1, u("s") to -1))), "5m/s")
    }

    private fun evalTest(expected: HumanQuantity, input: String, preferredUnits: Map<Map<String, Int>, HumanUnit> = mapOf()) {
        val result = evaluateHumanized(input, preferredUnits)
        Assert.assertTrue(result.errors.isEmpty())
        Assert.assertEquals(expected, result.value)
    }
}