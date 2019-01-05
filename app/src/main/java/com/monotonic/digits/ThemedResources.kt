package com.monotonic.digits

import android.content.res.Resources
import android.os.Build
import com.monotonic.digits.theme.CustomTheme


/**
 * @author Ethan
 */
// Might be a good idea to replace this class with this: https://github.com/jaredrummler/Cyanea
class ThemedResources(original: Resources, defaultTheme: CustomTheme) : Resources(original.assets, original.displayMetrics, original.configuration) {
    var customTheme : CustomTheme = defaultTheme

    @Throws(Resources.NotFoundException::class)
    override fun getColor(id: Int): Int {
        return getColor(id, null)
    }

    @Throws(Resources.NotFoundException::class)
    override fun getColor(id: Int, theme: Resources.Theme?): Int {
        return when (getResourceEntryName(id)) {
            "primary" -> customTheme.primaryColor
            "accent" -> customTheme.primaryColor
            "fill" -> customTheme.fillColor
            else -> super.getColor(id, theme)
        }
    }
}