package com.ethshea.digits

import junit.framework.Assert.*
import org.junit.Test

class EditorTest {
    @Test
    fun prefixWithCaretPosition() {
        assertTrue(MainActivity.shouldShowPrefixes("", 0))
        assertTrue(MainActivity.shouldShowPrefixes("9", 1))
        assertFalse(MainActivity.shouldShowPrefixes("9k", 2))
        assertFalse(MainActivity.shouldShowPrefixes("9kV", 3))
        assertFalse(MainActivity.shouldShowPrefixes("9kV", 2))
        assertTrue(MainActivity.shouldShowPrefixes("9V", 1))
        assertFalse(MainActivity.shouldShowPrefixes("9kV", 2))
    }

    @Test
    fun insignificantPosition() {
        assertEquals(1, MainActivity.insignificantStart("99.99", sf(1)))
        assertEquals(2, MainActivity.insignificantStart("99.99", sf(2))) // Before the period
        assertEquals(3, MainActivity.insignificantStart("0.2", sf(1))) // With a leading 0
        assertEquals(3, MainActivity.insignificantStart("0.22", sf(1)))
        assertEquals(5, MainActivity.insignificantStart("0.002", sf(1)))
        assertEquals(5, MainActivity.insignificantStart("0.0020", sf(1)))
        assertEquals(5, MainActivity.insignificantStart("0.0022", sf(1)))
    }

    @Test
    fun insignificantWithEllipsis() {
        // Put it at the end, all digits are significant
        assertEquals(9, MainActivity.insignificantStart("1.2345…E4", sf(10)))
    }
}