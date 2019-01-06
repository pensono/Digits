package com.monotonic.digits.skin

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.monotonic.digits.R


/**
 * @author Ethan
 */
fun createSkinPickerDialog(context: Context, action: (Skin) -> Unit) : Dialog {
    val builder = AlertDialog.Builder(context)
    builder.setNegativeButton("Cancel") { d, _ -> d.cancel() }

    builder.setTitle(R.string.skin_dialog_title)
    val layoutInflater = context.getSystemService(LayoutInflater::class.java)
    val main_view = layoutInflater.inflate(R.layout.dialog_skin_picker, null, false) as ViewGroup
    val container = main_view.findViewById<ViewGroup>(R.id.theme_picker_container)
    builder.setView(main_view)
    val dialog = builder.create()

    val menuResources = context.resources.obtainTypedArray(R.array.themes)
    for (i in 0 until menuResources.length()) {
        val resId = menuResources.getResourceId(i, -1)
        if (resId < 0) {
            continue
        }

        val skin = skinFromResource(context, resId)

        val button = layoutInflater.inflate(R.layout.skin_picker_button, container, false)
        button.background = SkinDrawable(skin)
        button.setOnClickListener { _ ->
            action(skin)
            dialog.cancel()
        }
        container.addView(button)
    }
    menuResources.recycle()

    return dialog
}
