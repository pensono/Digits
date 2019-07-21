package com.monotonic.digits

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.preference.PreferenceManager
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.human.*
import com.monotonic.digits.skin.*
import com.monotonic.digits.units.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.button_area.*
import android.content.Intent
import android.graphics.drawable.*


class MainActivity : Activity() {
    companion object {
        public val TAG = "Digits_MainActivity"
    }
    private val history = mutableListOf<HistoryItem>()
    private val preferredUnits = mutableMapOf<Map<String, Int>, HumanUnit>()

    private var floating : View? = null
    private var humanizedQuantity = HumanQuantity(SciNumber.Zero, HumanUnit(mapOf())) // Default value that should be overwritten quickly
    private var editingUnit = false

    private lateinit var billingManager : BillingManager

    private val editingInput
        get() = if (editingUnit) unit_input else input

    private val resultSeparator : SeparatorType
        get() {
            val spaceSeparate = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("space_grouping", true)
            return if (spaceSeparate) SeparatorType.SPACE else SeparatorType.NONE
        }

    private val numberFormat : NumberFormat
        get() {
            val useEngineering = getPreferences(Context.MODE_PRIVATE).getBoolean("use_engineering_format", true)
            return if (useEngineering) NumberFormat.ENGINEERING else NumberFormat.SCIENTIFIC
        }

    private val roundingMode : RoundingMode
        get() {
            val roundResults = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("round_results", false)
            return if (roundResults) RoundingMode.SIGFIG else RoundingMode.REMOVE_TRAILING
        }

    private val sigfigHighlight : Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("sigfig_highlight", false)

    private lateinit var skin : Skin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.setDefaultValues(this, R.xml.settings, false)

        setContentView(R.layout.activity_main)

        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editingUnit = false
                updatePreview()
            }
        })

        unit_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) { }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tryUnitConversion()
            }
        })
        unit_input.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                tryUnitConversion()
            }
        }

        discipline_dropdown.adapter = GenericSpinnerAdapter(this, R.layout.spinner_item, disciplines) { getString(it.nameResource) }
        discipline_dropdown.setSelection(getPreferences(Context.MODE_PRIVATE).getInt(getString(R.string.pref_discipline), 0))
        discipline_dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                populateUnitSelector(disciplines[pos].units, unit_selector)
                with (getPreferences(Context.MODE_PRIVATE).edit()) {
                    putInt(getString(R.string.pref_discipline), pos)
                    apply()
                }
            }
        }

        round
        val rounding_modes
        rounding_dropdown.adapter = GenericSpinnerAdapter(this, R.layout.spinner_item, disciplines) { getString(it.nameResource) }

        input.showSoftInputOnFocus = false
        unit_input.showSoftInputOnFocus = false

        result_preview.post { updatePreview() }

        val skinName = getPreferences(Context.MODE_PRIVATE)
                .getString(getString(R.string.pref_skin), resources.getResourceName(R.array.skin_default_light))
        val skinId = resources.getIdentifier(skinName, "id", packageName)
        val validatedSkinId = if (skinId == 0) R.array.skin_default_light else skinId // Might happen if a skin is no longer available
        applySkin(skinFromResource(this, validatedSkinId))

        number_format_switcher.isChecked = numberFormat == NumberFormat.ENGINEERING

        populateUnitSelector(UnitSystem.unitAbbreviations.values, unit_selector)
        populateUnitSelector(UnitSystem.prefixAbbreviations.values.filter { it.abbreviation != "" }, prefix_selector)
        prefix_selector_container.post { centerScroll(prefix_selector_container) }

        billingManager = BillingManager(this)
    }

    override fun onPause() {
        super.onPause()
        with (getPreferences(Context.MODE_PRIVATE).edit()) {
            putString("input_value", input.text.toString())
            apply()
        }
    }

    override fun onResume() {
        super.onResume()
        input.text.replace(0, input.text.length, getPreferences(Context.MODE_PRIVATE).getString("input_value", ""))

        billingManager.refreshPurchases()
    }

    fun numberFormatToggled(view: View) {
        with (getPreferences(Context.MODE_PRIVATE).edit()) {
            putBoolean("use_engineering_format", number_format_switcher.isChecked)
            commit()
        }
        updatePreview()
    }

    fun toggleUnitConversion(view: View) {
        editingUnit = !editingUnit
        updatePreview()
    }

    fun submitUnitConversion(view: View) {
        val unit = parseHumanUnit(unit_input.text.toString()) ?: return
        addPreferredUnit(unit)
    }

    fun tryUnitConversion() {
        try {
            val input = unit_input.text.toString()
            val unit = parseHumanUnit(input)
            val validUnit = unit != null && unit.dimensionallyEqual(humanizedQuantity.unit)
            val ambiguousInputs = UnitSystem.prefixAbbreviations.keys.intersect(UnitSystem.unitAbbreviations.keys)

            if (validUnit && !ambiguousInputs.contains(input)) {
                addPreferredUnit(unit!!)
            } else {
                submit_conversion.isEnabled = validUnit
                Log.d(TAG, "Enabled: $validUnit")
            }
        } catch (e: Exception) {
            result_preview.text = "Error"
            submit_conversion.isEnabled = false
            Log.e(TAG, "Unit parsing error", e)
        }
    }

    private fun addPreferredUnit(unit: HumanUnit) {
        preferredUnits[unit.dimensions] = unit
        unit_input.text.clear()
        editingUnit = false
        updatePreview()
    }

    fun openPopupMenu(view: View) {
        val popup = PopupMenu(this, view, Gravity.NO_GRAVITY)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_get_pro -> {
                    val dialog = createProValueDialog(this) { billingManager.doProPurchase(this) }
                    dialog.show()
                    true
                }
                R.id.menu_customize -> {
                    val dialog = createSkinPickerDialog(this, billingManager) {
                        with (getPreferences(Context.MODE_PRIVATE).edit()) {
                            putString(getString(R.string.pref_skin), resources.getResourceName(it.id))
                            apply()
                        }
                        applySkin(it)
                    }
                    dialog.show()
                    true
                }
                R.id.menu_settings -> {
                    val a = Intent(this, SettingsActivity::class.java)
                    startActivity(a)
                    true
                }
                else -> false
            }
        }
        popup.inflate(R.menu.popup_menu)

        if (billingManager.hasPro){
            popup.menu.findItem(R.id.menu_get_pro).isVisible = false
        }

        popup.show()
    }

    fun calculatorButtonClick(button: View) {
        val buttonCommand = (button as CalculatorButton).primaryCommand
        if (buttonCommand == "ENT") {
            history += HistoryItem(input.text.toString(), result_preview.text.toString())
            input.text.replace(0, input.text.length, humanizedQuantity.valueString(roundingMode))
            input.setSelection(input.text.length)
        } else if (buttonCommand == "DEL") {
            if (editingInput.selectionStart == editingInput.selectionEnd) { // No selection
                if (editingInput.selectionStart == 0) {
                    if (editingInput.text.isNotEmpty()) {
                        editingInput.text.delete(0, 1) // Delete at front
                    }
                } else {
                    editingInput.text.delete(editingInput.selectionStart - 1, editingInput.selectionStart)
                }
            } else { // Something selected, delete it all
                editingInput.text.delete(editingInput.selectionStart, editingInput.selectionEnd)
            }
        } else {
            val insertText = buttonCommand.replace("|", "")
            editingInput.text.replace(editingInput.selectionStart, editingInput.selectionEnd, insertText)
            if (buttonCommand.contains('|')) {
                val offset = buttonCommand.indexOf('|')
                editingInput.setSelection(editingInput.selectionStart + offset - insertText.length)
            }
        }
    }

    // It would be pretty schweet if this was in the CalculatorButton class itself
    fun calculatorButtonLongClick(button: CalculatorButton) {
        if (button.primaryCommand == "DEL") {
            editingUnit = false
            doClearAnimation()
            return
        }

        if (button.secondary.isEmpty()) {
            return
        }

        val layout = createFloatingSecondaryFor(button)
        button.secondary.forEach { (text, command) ->
            val secondaryButton = layoutInflater.inflate(R.layout.button_calc_secondary, layout, false) as CalculatorButton
            secondaryButton.text = text
            secondaryButton.primaryCommand = command
            if (button.adaptPrimary) {
                secondaryButton.setOnClickListener {
                    run {
                        calculatorButtonClick(secondaryButton)
                        button.text = text
                        button.primaryCommand = command
                    }
                }
            }

            layout.addView(secondaryButton)
        }
        mainRootLayout.addView(layout)

        button.isPressed = false
    }

    private fun createFloatingSecondaryFor(button: CalculatorButton): ViewGroup {
        val layout = layoutInflater.inflate(R.layout.layout_calc_secondary, mainRootLayout, false) as ViewGroup
        skinSecondaryBackground(layout.background as GradientDrawable, skin)

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

        applySkinIn(layout)
        return layout
    }

    private fun updatePreview() {
        input.isFocusable = !editingUnit
        input.isFocusableInTouchMode = !editingUnit
        submit_conversion.visibility = if (editingUnit) View.VISIBLE else View.GONE

        if (editingUnit) {
            unit_input.visibility = View.VISIBLE
            unit_input.requestFocus()

            val hexColor = hexStringForColor(R.color.detail_text)
            val previewText = "<font color='$hexColor'>Convert </font>${humanizedQuantity.unitString()}<font color='$hexColor'> to:</font>"
            result_preview.setText(htmlToSpannable(previewText), TextView.BufferType.SPANNABLE)
        } else {
            unit_input.visibility = View.GONE
            try {
                val parseResult = evaluateHumanized(input.text.toString(), preferredUnits)
                humanizedQuantity = parseResult.value
                val coloredText = formatResultForDisplay(humanizedQuantity, R.color.detail_text)

                result_preview.setText(coloredText, TextView.BufferType.SPANNABLE)
                input.errors = parseResult.errors
            } catch (e: Exception) {
                result_preview.text = "Error"
                Log.e(TAG, "Calculation error", e)
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val result = super.dispatchTouchEvent(ev)

        if (ev?.action == MotionEvent.ACTION_UP && floating != null) {
            mainRootLayout.removeView(floating)
        }

        return result
    }

    fun populateUnitSelector(units : Collection<AtomicHumanUnit>, container: ViewGroup) {
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
        applySkinIn(container)
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

    private fun formatResultForDisplay(humanQuantity: HumanQuantity, colorResourceId: Int) : Spanned {
        val colorStr = hexStringForColor(colorResourceId)

        // Find something that fits
        // This loop may potentially run quite a few times if the starting number is very
        // long, but it should be quick enough
        var humanString = humanQuantity.humanString(resultSeparator)
        val availableSpacePx = result_preview.width - result_preview.paddingRight - result_preview.paddingLeft
        while (result_preview.paint.measureText(humanString.string) >= availableSpacePx && humanString.string.length > 0) {
            humanString = humanQuantity.humanString(humanString.string.length - 1, resultSeparator, numberFormat)
        }

        val coloredText =
                if (sigfigHighlight)
                    humanString.string
                            .replaceRange(humanString.insigfigEnd, humanString.insigfigEnd, "</font>" )
                            .replaceRange(humanString.insigfigStart, humanString.insigfigStart, "<font color='$colorStr'>")
                else humanString.string

        // https://stackoverflow.com/questions/10140893/android-multi-color-in-one-textview
        return htmlToSpannable(coloredText)
    }

    private fun hexStringForColor(colorResourceId: Int) =
            '#' + ResourcesCompat.getColor(resources, colorResourceId, null).toString(16)

    private fun applySkin(skin: Skin) {
        this.skin = skin
        applySkinIn(mainRootLayout)
    }

    private fun applySkinIn(viewGroup: ViewGroup) {
        val colorMap = mapOf(
            "primary" to skin.primary,
            "primary_dim" to skin.primaryDim,
            "primary_dark" to skin.primaryDark,
            "primary_light" to skin.primaryLight,
            "accent" to skin.accent,
            "fill" to skin.fill
        )

        updateSkinIn(mainRootLayout, colorMap)
    }

    private fun doClearAnimation() {
        val circleAnimation = GrowingCircle(skin.primary, editor_area)
        circleAnimation.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable) {
                editor_area.foreground = null
            }
        })
        circleAnimation.peakCallback = {
            editingInput.text.clear()
            preferredUnits.clear()
        }
        editor_area.foreground = circleAnimation

        circleAnimation.start()
    }
}

private fun htmlToSpannable(coloredText: String): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(coloredText, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(coloredText)
    }
}