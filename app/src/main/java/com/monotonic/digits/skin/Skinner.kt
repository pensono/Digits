package com.monotonic.digits.skin

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.ViewGroup

fun skinSecondaryBackground(drawable: GradientDrawable, skin: Skin) {
    val color = ColorStateList(
            arrayOf(
                    intArrayOf(android.R.attr.state_hovered),
                    intArrayOf()
            ),
            intArrayOf(
                    skin.accent,
                    skin.primaryLight
            )
    )

    drawable.color = color
}

fun updateSkinIn(viewGroup: ViewGroup, colorMap: Map<String, Int>) {
    for (i in 0 until viewGroup.childCount) {
        val child = viewGroup.getChildAt(i)

        val newColor = colorMap[child.tag]
        if (newColor != null) {
            child.setBackgroundColor(newColor)
        } // Otherwise ignore

        if (child is ViewGroup) {
            updateSkinIn(child, colorMap)
        }
    }
}