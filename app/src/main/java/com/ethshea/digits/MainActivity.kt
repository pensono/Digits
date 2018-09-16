package com.ethshea.digits

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import com.ethshea.digits.evaluator.evaluateExpression
import com.ethshea.digits.units.UnitSystem
import com.ethshea.digits.units.humanize
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    val TAG = "Digits_MainActivity"

    private var showPrefix = false
        set(value) {
            if (field != value) {
                field = value
                val unitsToDisplay =
                        (if (showPrefix) UnitSystem.prefixAbbreviations.values
                         else listOf()
                        ) + UnitSystem.unitAbbreviations.values
                displayUnits(unitsToDisplay.map { u -> u.abbreviation })
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

                    val humanizedQuantity = humanize(parseResult.value)

                    result_preview.text = humanizedQuantity.humanString()
                    input.errors = parseResult.errors
                } catch (e: Exception) {
                    result_preview.text = "Error"
                    Log.e(TAG, "Calculation error", e)
                }
            }
        })

        input.addSelectionListener { _, _ -> updateUnitDisplay() }

        showPrefix = true

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

    fun updateUnitDisplay() {
        showPrefix = shouldShowPrefixes(input.text.toString(), input.selectionStart)
    }

    fun calculatorUnitClick(button: View) {
        calculatorButtonClick(button)
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

    // Kinda weird for this to be public, but it makes it testable
    companion object {
        fun shouldShowPrefixes(text: String, caretPosition: Int) : Boolean {
            if (caretPosition == 0) {
                return true
            }

            return caretPosition - 1 < text.length && !text[caretPosition - 1].isLetter()
        }
    }
}
