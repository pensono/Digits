package com.ethshea.unitcalculator

import com.ethshea.unitcalculator.evaluator.SIUnit
import org.junit.Test

import org.junit.Assert.*

class SIUnitTest {
    @Test
    fun parseUnits() {
        assertEquals(SIUnit(0, 0, -1, 0),SIUnit.fromString("Hz"))
        assertEquals(SIUnit(-1, 1, 0, 0, 3),SIUnit.fromString("kg/m"))
        assertEquals(SIUnit(1, 0, -2, 0, 0),SIUnit.fromString("m/s/s"))
    }
}
