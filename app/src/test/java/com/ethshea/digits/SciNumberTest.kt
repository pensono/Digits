package com.ethshea.digits

import junit.framework.Assert.assertEquals
import org.junit.Test

class SciNumberTest {
    @Test
    fun sigFigsIrrational() {
        assertEquals(1, SciNumber("1").sin().sigFigs)
        assertEquals(3, SciNumber("1.00").sin().sigFigs)
        assertEquals(3, SciNumber("01.00").sin().sigFigs)
    }

    @Test
    fun sigFigsAddition() {
        assertEquals(1, (SciNumber("1") + SciNumber("2")).sigFigs)
        // assertEquals(1, (SciNumber("10") + SciNumber("20")).sigFigs) // What should this actually be?
        assertEquals(2, (SciNumber("10.") + SciNumber("20.")).sigFigs)
        assertEquals(3, (SciNumber("10.0") + SciNumber("20.0")).sigFigs)

        assertEquals(1, (SciNumber(".01")).sigFigs)
        assertEquals(1, (SciNumber(".01") + SciNumber("2")).sigFigs)
        assertEquals(1, (SciNumber(".01") + SciNumber(".02")).sigFigs)

        assertEquals(2, (SciNumber("11") + SciNumber("2")).sigFigs)
        assertEquals(3, (SciNumber("11.0") + SciNumber("2")).sigFigs)
        assertEquals(2, (SciNumber(".11") + SciNumber(".02")).sigFigs)
    }

    @Test
    fun calculatesExtra() {
        assertEquals(Math.sin(1.0), SciNumber("1").sin().toDouble())
        assertEquals(Math.sin(1.0), SciNumber("1.00").sin().toDouble())
    }
}