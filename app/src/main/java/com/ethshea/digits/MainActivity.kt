package com.ethshea.digits

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import com.ethshea.digits.evaluator.evaluateExpression
import com.ethshea.digits.units.humanize
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    private var prefix = true
        set(value) {
            field = value
            if (prefix) {
                displayUnits(listOf("T", "G", "M", "k", "1", "m", "μ", "n", "p"))
            } else {
                displayUnits(listOf("A", "Hz", "s", "m", "g", "V", "Ω", "H"))
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    val parseResult = evaluateExpression(input.text.toString())

                    val quantity = parseResult.value.value
                    val humanUnit = humanize(parseResult.value)

                    result_preview.text = quantity.toString() + humanUnit.abbreviation
                    input.errors = parseResult.errors
                } catch (e: Exception) {
                    result_preview.text = "Error"
                }
            }
        })

        prefix = true

        input.showSoftInputOnFocus = false
    }

    fun calculatorButtonClick(button: View) {
        val buttonCommand = (button as Button).tag.toString()
        if (buttonCommand == "DEL") {
            if (input.selectionStart == input.selectionEnd && input.selectionStart != 0) {
                input.text.replace(input.selectionStart-1, input.selectionStart, "")
            } else {
                input.text.replace(input.selectionStart, input.selectionEnd, "")
            }
        } else {
            val insertText = buttonCommand.replace("|", "")
            input.text.replace(input.selectionStart, input.selectionEnd, insertText)
            if (buttonCommand.contains('|')) {
                val offset = buttonCommand.indexOf('|')
                input.setSelection(input.selectionStart + offset - insertText.length)
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
