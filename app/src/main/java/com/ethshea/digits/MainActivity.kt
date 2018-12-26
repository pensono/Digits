package com.ethshea.digits

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.GravityCompat
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import android.widget.TextView
import com.ethshea.digits.evaluator.HumanQuantity
import com.ethshea.digits.evaluator.evaluateExpression
import com.ethshea.digits.units.AtomicHumanUnit
import com.ethshea.digits.units.UnitSystem
import com.ethshea.digits.units.humanize
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : Activity() {
    val TAG = "Digits_MainActivity"
    val history = mutableListOf<HistoryItem>()
    var floating : View? = null

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
                    val coloredText = formatResultForDisplay(humanizedQuantity, R.color.detail_text)

                    result_preview.setText(coloredText, TextView.BufferType.SPANNABLE)
                    input.errors = parseResult.errors
                } catch (e: TimeoutCancellationException) {
                    result_preview.text = "Timeout"
                } catch (e: Exception) {
                    result_preview.text = "Error"
                    Log.e(TAG, "Calculation error", e)
                }
            }
        })

        displayUnits(UnitSystem.unitAbbreviations.values, unit_selector)
        displayUnits(UnitSystem.prefixAbbreviations.values.filter { it.abbreviation != "" }, prefix_selector)
        prefix_selector_container.post { centerScroll(prefix_selector_container) }

        disciplines.forEach { discipline ->
            val item = nav_view.menu.add(R.id.discipline_menu_group, Menu.NONE, Menu.NONE, discipline.nameResource)
            item.setOnMenuItemClickListener(disciplineListener(discipline))
            if (discipline.iconResource != 0)
                item.setIcon(discipline.iconResource)
        }

        input.showSoftInputOnFocus = false
    }

    private fun centerScroll(container: View) {
        if (container is HorizontalScrollView) {
            val location = (prefix_selector.width - container.width) / 2
            container.scrollTo(location, 0)
        } else if (container is ScrollView) { // No subclass relation between these, sad.
            val location = (prefix_selector.height - container.height) / 2
            container.scrollTo(0, location)
        }
    }

    fun openSideMenu(view: View) {
        drawer_layout.openDrawer(GravityCompat.START)
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

        if (button.secondary.isEmpty()) {
            return
        }

        val layout = createFloatingSecondaryFor(button)
        button.secondary.forEach { pair ->
            val secondaryButton = layoutInflater.inflate(R.layout.button_calc_secondary, layout, false) as CalculatorButton
            secondaryButton.text = pair.first
            secondaryButton.primaryCommand = pair.second

            layout.addView(secondaryButton)
        }
        mainRootLayout.addView(layout)

        button.isPressed = false
    }

    private fun createFloatingSecondaryFor(button: CalculatorButton): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.layout_calc_secondary, mainRootLayout, false) as ViewGroup

        val buttonLoc = intArrayOf(0, 0)
        button.getLocationInWindow(buttonLoc) // Not sure if this or getLocationInScreen is correct.

        val rootLoc = intArrayOf(0, 0)
        mainRootLayout.getLocationInWindow(rootLoc) // Not sure if this or getLocationInScreen is correct.

        // Keep this here in case we want to tweak the offset amount
        val offsetPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, resources.displayMetrics)
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
                }
            }
            false
        }

        if (floating != null) {
            mainRootLayout.removeView(floating)
        }
        floating = layout
        return layout
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val result = super.dispatchTouchEvent(ev)

        if (ev?.action == MotionEvent.ACTION_UP && floating != null) {
            mainRootLayout.removeView(floating)
        }

        return result
    }

    fun displayUnits(units : Collection<AtomicHumanUnit>, container: ViewGroup) {
        container.removeAllViews()
        for (unit in units) {
            val newButton = layoutInflater.inflate(R.layout.button_unit, null) as CalculatorButton
            newButton.primaryCommand = if (unit.abbreviation == "1") "" else unit.abbreviation
            newButton.text = unit.abbreviation
            newButton.setOnLongClickListener {
                val floating = createFloatingSecondaryFor(newButton)
                val info = layoutInflater.inflate(R.layout.floating_info, floating, false) as TextView
                val derivationString =
                        if (unit.unitDerivation == null)
                            ""
                        else
                            " (${unit.unitDerivation})"

                info.text = unit.name + derivationString
                floating.addView(info)
                mainRootLayout.addView(floating)
                true
            }
            container.addView(newButton)
        }
    }

    private fun formatResultForDisplay(humanQuantity: HumanQuantity, colorResourceId: Int) : Spanned {
        val colorStr = ResourcesCompat.getColor(resources, colorResourceId, null).toString(16)

        // Find something that fits
        // This loop may potentially run quite a few times if the starting number is very
        // long, but it should be quick enough
        var humanString = humanQuantity.humanString()
        val availableSpacePx = result_preview.width - result_preview.paddingRight - result_preview.paddingLeft
        while (result_preview.paint.measureText(humanString.string) >= availableSpacePx && humanString.string.length > 0) {
            humanString = humanQuantity.humanString(humanString.string.length - 1)
        }

        val coloredText = humanString.string
                .replaceRange(humanString.insigfigEnd, humanString.insigfigEnd, "</font>" )
                .replaceRange(humanString.insigfigStart, humanString.insigfigStart, "<font color='#$colorStr'>")

        // https://stackoverflow.com/questions/10140893/android-multi-color-in-one-textview
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(coloredText,  Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(coloredText)
        }
    }

    private fun disciplineListener(discipline: Discipline): MenuItem.OnMenuItemClickListener =
        MenuItem.OnMenuItemClickListener {
            drawer_layout.closeDrawers()
            displayUnits(discipline.units, unit_selector)
            true
        }

    companion object {
        // Kinda weird for this to be public, but it makes it testable
        fun shouldShowPrefixes(text: String, caretPosition: Int) : Boolean {
            if (caretPosition == 0) {
                return true
            }

            return caretPosition - 1 < text.length && !text[caretPosition - 1].isLetter()
        }
    }
}
