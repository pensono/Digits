package com.monotonic.digits.skin

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.monotonic.digits.BillingManager
import com.monotonic.digits.R


/**
 * @author Ethan
 */
fun createSkinPickerDialog(activity: Activity, billingManager: BillingManager, action: (Skin) -> Unit): Dialog {
    val builder = AlertDialog.Builder(activity)
    builder.setNegativeButton("Cancel") { d, _ -> d.cancel() }

    builder.setTitle(R.string.skin_dialog_title)
    val layoutInflater = activity.getSystemService(LayoutInflater::class.java)
    val main_view = layoutInflater.inflate(R.layout.dialog_skin_picker, null, false) as ViewGroup
    builder.setView(main_view)
    val dialog = builder.create()

    fillThemeGrid(activity, dialog, main_view, layoutInflater, R.id.theme_picker_container, R.array.themes, action)

    val proAction = if (billingManager.hasPro) action else { _ -> billingManager.doProPurchase(activity) }
    fillThemeGrid(activity, dialog, main_view, layoutInflater, R.id.theme_picker_container_pro, R.array.themes_pro, proAction)

    return dialog
}

// There's alot of args to this method, sad
fun fillThemeGrid(context: Context, dialog: Dialog, main_view: ViewGroup, layoutInflater: LayoutInflater, gridContainerResId: Int, skinListResId: Int, action: (Skin) -> Unit) {
    val container = main_view.findViewById<ViewGroup>(gridContainerResId)

    val menuResources = context.resources.obtainTypedArray(skinListResId)
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
}
