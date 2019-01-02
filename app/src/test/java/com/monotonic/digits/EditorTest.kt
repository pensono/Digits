package com.monotonic.digits

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
}