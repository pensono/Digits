package com.monotonic.digits

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context

fun createProValueDialog(context: Context, action: () -> Unit) : Dialog {
    val builder = AlertDialog.Builder(context)
    builder.setPositiveButton("Buy") { d, _ -> action() }
    builder.setNegativeButton("Cancel") { d, _ -> d.cancel() }
    builder.setTitle(R.string.pro_value_title)
    builder.setMessage(R.string.pro_value_message)
    val dialog = builder.create()

    return dialog
}
