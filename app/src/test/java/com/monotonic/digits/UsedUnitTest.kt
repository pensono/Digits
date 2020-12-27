package com.monotonic.digits

import com.monotonic.digits.human.HumanUnit
import com.monotonic.digits.human.usedUnits
import org.junit.Assert
import org.junit.Test

/**
 * @author Ethan
 */

class UsedUnitTest {
    @Test
    fun basic() {
        usedUnitTest(setOf(), "123")
        usedUnitTest(setOf(HumanUnit(mapOf(u("m") to 1))), "123m")
    }

    @Test
    fun unitsWithModulus() {
        usedUnitTest(setOf(HumanUnit(mapOf(u("m") to 1, u("s") to -1))), "123m/s")
    }

    @Test
    fun operations() {
        usedUnitTest(setOf(), "123+654")
        usedUnitTest(setOf(HumanUnit(mapOf(u("m") to 1))), "123m+654m")
        usedUnitTest(setOf(HumanUnit(mapOf(u("m") to 1))), "123m+654")
        usedUnitTest(setOf(HumanUnit(mapOf(u("m") to 1))), "123+654m")
    }

    private fun usedUnitTest(expected: Set<HumanUnit>, input: String) {
        val result = usedUnits(input)
        Assert.assertEquals(expected, result)
    }
}