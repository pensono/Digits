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
fun createThemePickerDialog(context: Context) : Dialog {
    val builder = AlertDialog.Builder(context)
    builder.setPositiveButton("Change")  { _, _ -> }
    builder.setNegativeButton("Cancel") { d, _ -> d.cancel() }

    builder.setTitle(R.string.theme_dialog_title)
    val layoutInflater = context.getSystemService(LayoutInflater::class.java)
    val main_view = layoutInflater.inflate(R.layout.dialog_theme_picker, null, false) as ViewGroup
    val container = main_view.findViewById<ViewGroup>(R.id.theme_picker_container)

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
        val light = item.getBoolean(3, true)
        item.recycle()

        val button = layoutInflater.inflate(R.layout.theme_picker_button, container, false)
        button.background = ThemeDrawable(fill, primary, accent, light)
        container.addView(button)
    }
    menuResources.recycle()

    builder.setView(main_view)

    return builder.create()
}