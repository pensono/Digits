package com.ethshea.digits

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ethshea.digits.evaluator.HumanQuantity
import com.ethshea.digits.evaluator.evaluateExpression
import com.ethshea.digits.units.UnitSystem
import com.ethshea.digits.units.humanize
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    val TAG = "Digits_MainActivity"
    val history = mutableListOf<HistoryItem>()

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
                    val coloredText = colorizePrecision(humanizedQuantity)

                    result_preview.setText(coloredText, TextView.BufferType.SPANNABLE)
                    input.errors = parseResult.errors
                } catch (e: Exception) {
                    result_preview.text = "Error"
                    Log.e(TAG, "Calculation error", e)
                }
            }
        })

        displayUnits(UnitSystem.unitAbbreviations.values.map { u -> u.abbreviation }, unit_selector)
        displayUnits(UnitSystem.prefixAbbreviations.values.map { u -> u.abbreviation }, prefix_selector)

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
        } else if (buttonCommand == "ENT") {
            history += HistoryItem(input.text.toString(), result_preview.text.toString())
            input.text.replace(0, input.text.length, result_preview.text)
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
        if (button.primaryCommand == "DEL") {
            input.text.clear()
            return
        }

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
        val horizMarginPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)

        // https://stackoverflow.com/a/24035591/2496050
        layout.post {
            val xPos = (buttonLoc[0] + (button.width - layout.width) / 2 - rootLoc[0]).toFloat()

            layout.x = Math.min(Math.max(horizMarginPx, xPos), resources.displayMetrics.widthPixels - horizMarginPx - layout.width)
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

    fun displayUnits(units : List<String>, container: ViewGroup) {
        container.removeAllViews()
        for (unit in units) {
            val newButton = layoutInflater.inflate(R.layout.button_unit, null) as CalculatorButton
            newButton.primaryCommand = if (unit == "1") "" else unit
            newButton.text = unit
            container.addView(newButton)
        }
    }

    companion object {
        // Kinda weird for this to be public, but it makes it testable
        fun shouldShowPrefixes(text: String, caretPosition: Int) : Boolean {
            if (caretPosition == 0) {
                return true
            }

            return caretPosition - 1 < text.length && !text[caretPosition - 1].isLetter()
        }

        private fun colorizePrecision(humanQuantity: HumanQuantity) : Spanned {
            val rawText = humanQuantity.humanString()
            val precision = humanQuantity.value.precision

            val sigfigsEndLocation = insignificantStart(rawText, precision)
            val coloredText = rawText.replaceRange(sigfigsEndLocation, sigfigsEndLocation, "<font color='blue'>") + "</font>"

            // https://stackoverflow.com/questions/10140893/android-multi-color-in-one-textview
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(coloredText,  Html.FROM_HTML_MODE_LEGACY)
            } else {
                Html.fromHtml(coloredText)
            }
        }

        /***
         * @param rawText Must be produced by HumanQuantity
         */
        fun insignificantStart(rawText: String, precision: Precision): Int {
            val startPos = rawText.length - rawText.trimStart('0', '.').length

            val sigfigsEndLocation = when (precision) {
                is Precision.Infinite -> rawText.length
                is Precision.SigFigs -> {
                    val decimalAdjustment =
                        if (rawText.substring(startPos, Math.min(precision.amount + startPos, rawText.length))
                                .contains('.'))
                            1
                        else
                            0
                    precision.amount + startPos + decimalAdjustment
                }
            }

            return sigfigsEndLocation
        }
    }
}
