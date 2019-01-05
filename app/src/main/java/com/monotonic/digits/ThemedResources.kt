package com.monotonic.digits

import android.content.res.Resources
import com.monotonic.digits.skin.Skin


/**
 * @author Ethan
 */
// Might be a good idea to replace this class with this: https://github.com/jaredrummler/Cyanea
class ThemedResources(original: Resources, defaultTheme: Skin) : Resources(original.assets, original.displayMetrics, original.configuration) {
    var skin : Skin = defaultTheme

    @Throws(Resources.NotFoundException::class)
    override fun getColor(id: Int): Int {
        return getColor(id, null)
    }

    @Throws(Resources.NotFoundException::class)
    override fun getColor(id: Int, theme: Resources.Theme?): Int {
        return when (getResourceEntryName(id)) {
            "primary" -> skin.primary
            "accent" -> skin.primary
            "fill" -> skin.fill
            else -> super.getColor(id, theme)
        }
    }
}