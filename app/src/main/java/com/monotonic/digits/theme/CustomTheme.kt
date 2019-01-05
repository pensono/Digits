package com.monotonic.digits.theme

/**
 * @author Ethan
 */
data class CustomTheme(val fillColor: Int, val primaryColor: Int, val accentColor: Int, val type: ThemeType)

enum class ThemeType {
    DARK, LIGHT
}