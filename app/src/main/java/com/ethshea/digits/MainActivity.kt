package com.ethshea.digits

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    private var prefix = true
        set(value) {
            field = value
            if (prefix) {
                displayUnits(listOf("T", "G", "M", "k", "1", "m", "μ", "n", "p"))
            } else {
                displayUnits(listOf("Hz", "s", "m", "g", "V", "Ω", "H"))
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        prefix = true

        result_display.showSoftInputOnFocus = false
    }

    fun calculatorButtonClick(button: View) {
        val buttonCommand = (button as Button).tag.toString()
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

    fun calculatorUnitClick(button: View) {
        calculatorButtonClick(button)
        prefix = !prefix
    }

    fun displayUnits(units : List<String>) {
        unit_selector.removeAllViews()
        for (unit in units) {
            var newButton = layoutInflater.inflate(R.layout.unit_button, null) as Button
            newButton.tag = if (unit == "1") "" else unit
            newButton.text = unit
            unit_selector.addView(newButton)
        }
    }
}
