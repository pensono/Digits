package com.ethshea.unitcalculator

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        result_display.showSoftInputOnFocus = false

//        button_grid.viewTreeObserver.addOnGlobalLayoutListener {
//            val reference = button_grid.getChildAt(0)
//            for (i in 0 until unit_selector.childCount) {
//                unit_selector.getChildAt(i).layoutParams = LinearLayout.LayoutParams(reference.width, reference.height)
//            }
//        }
    }

    fun calculatorButtonClick(button: View) {
        val buttonCommand = (button as Button).tag.toString().replace("x", "")
        if (buttonCommand == "DEL") {
            if (result_display.selectionStart == result_display.selectionEnd && result_display.selectionStart != 0) {
                result_display.text.replace(result_display.selectionStart-1, result_display.selectionStart, "")
            } else {
                result_display.text.replace(result_display.selectionStart, result_display.selectionEnd, "")
            }
        } else {
            var insertText = buttonCommand.replace("|", "")
            result_display.text.replace(result_display.selectionStart, result_display.selectionEnd, insertText)
            if (buttonCommand.contains('|')) {
                var offset = buttonCommand.indexOf('|')
                result_display.setSelection(result_display.selectionStart + offset - insertText.length)
            }
        }
    }
}
