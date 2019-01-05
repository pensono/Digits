package com.monotonic.digits.theme

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.monotonic.digits.R


/**
 * @author Ethan
 */
fun createThemePickerDialog(context: Context, action: (CustomTheme) -> Unit) : Dialog {
    val builder = AlertDialog.Builder(context)
    builder.setNegativeButton("Cancel") { d, _ -> d.cancel() }

    builder.setTitle(R.string.theme_dialog_title)
    val layoutInflater = context.getSystemService(LayoutInflater::class.java)
    val main_view = layoutInflater.inflate(R.layout.dialog_theme_picker, null, false) as ViewGroup
    val container = main_view.findViewById<ViewGroup>(R.id.theme_picker_container)
    builder.setView(main_view)
    val dialog = builder.create()

    val menuResources = context.resources.obtainTypedArray(R.array.themes)
    for (i in 0 until menuResources.length()) {
        val resId = menuResources.getResourceId(i, -1)
        if (resId < 0) {
            continue
        }

        val item = context.resources.obtainTypedArray(resId)
        val fill = item.getColor(0,0)
        val primary = item.getColor(1, 0)
        val accent = item.getColor(2, 0)
        // This line is sad. replace with an enum or something rather than a boolean
        val light = if (item.getBoolean(3, true)) ThemeType.LIGHT else ThemeType.DARK
        val theme = CustomTheme(fill, primary, accent, light)
        item.recycle()

        val button = layoutInflater.inflate(R.layout.theme_picker_button, container, false)
        button.background = ThemeDrawable(theme)
        button.setOnClickListener { _ ->
            action(theme)
            dialog.cancel()
        }
        container.addView(button)
    }
    menuResources.recycle()

    return dialog
}