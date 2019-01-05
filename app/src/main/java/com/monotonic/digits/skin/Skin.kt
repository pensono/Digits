package com.monotonic.digits.skin

import android.content.Context
import android.support.annotation.ColorInt

/**
 * @author Ethan
 */
data class Skin(@ColorInt val fill: Int,
                @ColorInt val primary: Int,
                @ColorInt val primaryDim: Int,
                @ColorInt val primaryDark: Int,
                @ColorInt val primaryLight: Int,
                @ColorInt val accent: Int,
                val type: SkinType)

enum class SkinType {
    DARK, LIGHT
}


fun skinFromResource(context: Context, resId: Int): Skin {
    val item = context.resources.obtainTypedArray(resId)
    val fill = item.getColor(0, 0)
    val primary = item.getColor(1, 0)
    val primaryDim = item.getColor(2, 0)
    val primaryDark = item.getColor(3, 0)
    val primaryLight = item.getColor(4, 0)
    val accent = item.getColor(5, 0)

    // This line is sad. replace with an enum or something rather than a boolean
    val light = if (item.getBoolean(6, true)) SkinType.LIGHT else SkinType.DARK
    val skin = Skin(fill, primary, primaryDim, primaryDark, primaryLight, accent, light)
    item.recycle()

    return skin
}