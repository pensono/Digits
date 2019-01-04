package com.monotonic.digits.theme

import android.graphics.Color

/**
 * @author Ethan
 */
val themes = listOf(
        Theme(c(0x212121), c(0x26a69a), c(0xFFC107), ThemeType.LIGHT)
)

private fun c(value: Int) = Color.valueOf(value)