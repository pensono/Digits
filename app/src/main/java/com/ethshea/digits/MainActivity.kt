package com.ethshea.digits

import android.app.Activity
import android.graphics.Rect
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
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
        val buttonCommand = (button as CalculatorButton).primaryCommand
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

    // It would be pretty schweet if this was in the CalculatorButton class itself
    fun calculatorButtonLongClick(button: CalculatorButton) {
        val layout = layoutInflater.inflate(R.layout.layout_calc_secondary, mainRootLayout, false) as ViewGroup

        button.secondary.forEach { pair ->
            val secondaryButton = layoutInflater.inflate(R.layout.button_calc_secondary, layout, false) as CalculatorButton
            secondaryButton.text = pair.first
            secondaryButton.primaryCommand = pair.second

            layout.addView(secondaryButton)
        }

        val buttonLoc = intArrayOf(0, 0)
        button.getLocationInWindow(buttonLoc) // Not sure if this or getLocationInScreen is correct.

        val rootLoc = intArrayOf(0, 0)
        mainRootLayout.getLocationInWindow(rootLoc) // Not sure if this or getLocationInScreen is correct.

        // Keep this here in case we want to tweak the offset amount
        val offsetPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0f, resources.displayMetrics)

        // https://stackoverflow.com/a/24035591/2496050
        layout.post {
            layout.x = (buttonLoc[0] + (button.width - layout.width) / 2 - rootLoc[0]).toFloat()
            layout.y = buttonLoc[1] - rootLoc[1] - layout.height - offsetPx
            layout.visibility = View.VISIBLE
        }
        // Hide it until it has been laid out correctly.
        // Must use invisible instead of gone because gone won't lay it out (and width and height won't be calculated)
        layout.visibility = View.INVISIBLE

        mainRootLayout.addView(layout)

        button.setOnTouchListener { view, motionEvent ->
            val buttonRect = Rect()
            val xPos = motionEvent.rawX.toInt()
            val yPos = motionEvent.rawY.toInt()

            val layoutLoc = intArrayOf(0, 0)
            layout.getLocationInWindow(layoutLoc)

            when (motionEvent.action) {
                MotionEvent.ACTION_MOVE -> {
                    for (i in 0 until layout.childCount) {
                        val secondaryButton = layout.getChildAt(i)
                        secondaryButton.getHitRect(buttonRect)
                        buttonRect.offset(layoutLoc[0], layoutLoc[1]) // Rect in parent space, translate to screen
                        secondaryButton.isHovered = buttonRect.contains(xPos, yPos)
                    }
                }
                MotionEvent.ACTION_UP -> {
                    for (i in 0 until layout.childCount) {
                        val secondaryButton = layout.getChildAt(i)
                        secondaryButton.getHitRect(buttonRect)
                        buttonRect.offset(layoutLoc[0], layoutLoc[1]) // Rect in parent space, translate to screen
                        if (buttonRect.contains(xPos, yPos)) {
                            secondaryButton.performClick()
                        }
                    }

                    mainRootLayout.removeView(layout)
                }
            }
            false
        }
    }

    fun updateUnitDisplay() {
        showPrefix = shouldShowPrefixes(input.text.toString(), input.selectionStart)
    }

    fun displayUnits(units : List<String>) {
        unit_selector.removeAllViews()
        for (unit in units) {
            val newButton = layoutInflater.inflate(R.layout.button_unit, null) as CalculatorButton
            newButton.primaryCommand = if (unit == "1") "" else unit
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
