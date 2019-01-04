package com.monotonic.digits.theme

import android.graphics.Color

/**
 * @author Ethan
 */
data class Theme(val fillColor: Color, val primaryColor: Color, val accentColor: Color, val type: ThemeType)

enum class ThemeType {
    DARK, LIGHT
}