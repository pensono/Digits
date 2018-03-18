package com.ethshea.unitcalculator

import com.ethshea.unitcalculator.evaluator.SIUnit
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleSIUnitTest {
    @Test
    fun parseUnits() {
        assertEquals(SIUnit(0, 0, -1, 0),SIUnit.fromString("Hz"))
        assertEquals(SIUnit(-1, 1, 0, 0, 3),SIUnit.fromString("kg/m"))
        assertEquals(SIUnit(1, 0, -2, 0, 0),SIUnit.fromString("m/s/s"))
    }
}
