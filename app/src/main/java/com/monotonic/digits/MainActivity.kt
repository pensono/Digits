package com.monotonic.digits

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.drawable.Animatable2
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.HtmlCompat
import androidx.preference.PreferenceManager
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.human.*
import com.monotonic.digits.skin.*
import com.monotonic.digits.units.DimensionBag
import com.monotonic.digits.units.PrefixUnit
import com.monotonic.digits.units.UnitSystem
import com.monotonic.digits.units.disciplines
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.button_area.*


class MainActivity : Activity() {
    companion object {
        const val TAG = "Digits_MainActivity"
    }

    private val history = mutableListOf<HistoryItem>()
    private val preferredUnits = mutableMapOf<DimensionBag, HumanUnit>()

    private var floating: View? = null
    private var humanizedQuantity = HumanQuantity(SciNumber.Zero, HumanUnit(mapOf())) // Default value that should be overwritten quickly
    private var editingUnit: Boolean = false
        set(value) {
            field = value
            updatePreview()
        }

    private lateinit var billingManager: BillingManager

    private val editingInput
        get() = if (editingUnit) unit_input else input

    private val resultSeparator: SeparatorType
        get() {
            val spaceSeparate = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Preferences.spaceGrouping, true)
            return if (spaceSeparate) SeparatorType.SPACE else SeparatorType.NONE
        }

    private val numberFormat: NumberFormat
        get() {
            val useEngineering = getPreferences(Context.MODE_PRIVATE).getBoolean(Preferences.numberFormat, true)
            return if (useEngineering) NumberFormat.ENGINEERING else NumberFormat.SCIENTIFIC
        }

    private val roundingMode: RoundingMode
        get() {
            val roundResults = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Preferences.roundResult, false)
            return if (roundResults) RoundingMode.SIGFIG else RoundingMode.REMOVE_TRAILING
        }

    private val sigfigHighlight: Boolean
        get() = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Preferences.sigfigHighlight, false)

    private lateinit var skin: Skin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.setDefaultValues(this, R.xml.settings, false)

        setContentView(R.layout.activity_main)

        input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editingUnit = false
            }
        })

        unit_input.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tryUnitConversion()
            }
        })
        unit_input.onFocusChangeListener = View.OnFocusChangeListener { _, _ -> tryUnitConversion() }

        discipline_dropdown.adapter = GenericSpinnerAdapter(this, R.layout.spinner_item, disciplines) { getString(it.nameResource) }
        discipline_dropdown.setSelection(getPreferences(Context.MODE_PRIVATE).getInt(Preferences.discipline, 0))
        discipline_dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                populateUnitSelector(disciplines[pos].units, unit_selector)
                with(getPreferences(Context.MODE_PRIVATE).edit()) {
                    putInt(Preferences.discipline, pos)
                    apply()
                }
            }
        }

        input.showSoftInputOnFocus = false
        unit_input.showSoftInputOnFocus = false

        result_preview.post { updatePreview() }

        val skinName = getPreferences(Context.MODE_PRIVATE)
                .getString(Preferences.skin, resources.getResourceName(R.array.skin_default_light))
        val skinId = resources.getIdentifier(skinName, "id", packageName)
        val validatedSkinId = if (skinId == 0) R.array.skin_default_light else skinId // Might happen if a skin is no longer available
        applySkin(skinFromResource(this, validatedSkinId))

        number_format_switcher.isChecked = numberFormat == NumberFormat.ENGINEERING

        populateUnitSelector(UnitSystem.unitAbbreviations.values, unit_selector)
        populateUnitSelector(UnitSystem.prefixAbbreviations.values.filter { it.abbreviation != "" }.reversed(), prefix_selector)
        prefix_selector_container.post { centerScroll(prefix_selector_container) }

        billingManager = BillingManager(this)
    }

    override fun onPause() {
        super.onPause()
        with(getPreferences(Context.MODE_PRIVATE).edit()) {
            putString(Preferences.inputValue, input.text.toString())
            apply()
        }
    }

    override fun onResume() {
        super.onResume()
        input.text.replace(0, input.text.length, getPreferences(Context.MODE_PRIVATE).getString(Preferences.inputValue, ""))

        billingManager.refreshPurchases()
    }

    fun numberFormatToggled(@Suppress("UNUSED_PARAMETER") view: View) {
        with(getPreferences(Context.MODE_PRIVATE).edit()) {
            putBoolean(Preferences.numberFormat, number_format_switcher.isChecked)
            commit()
        }
        updatePreview()
    }

    fun toggleUnitConversion(@Suppress("UNUSED_PARAMETER") view: View) {
        editingUnit = !editingUnit
        updatePreview()
    }

    fun submitUnitConversion(@Suppress("UNUSED_PARAMETER") view: View) {
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
            result_preview.text = getString(R.string.result_error)
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
                        with(getPreferences(Context.MODE_PRIVATE).edit()) {
                            putString(Preferences.skin, resources.getResourceName(it.id))
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

        if (billingManager.hasPro) {
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

        button.setOnTouchListener { _, motionEvent ->
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

            val detailColorHex = hexStringForColor(R.color.detail_text)
            val fullConvertPrompt = "<font color='$detailColorHex'>Convert </font>${humanizedQuantity.unitString()}<font color='$detailColorHex'> to:</font>"
            val abbreviatedConvertPrompt = "${humanizedQuantity.unitString()}<font color='$detailColorHex'> to:</font>"
            val previewText = if (stringFits("Convert ${humanizedQuantity.unitString()} to:", result_preview)) fullConvertPrompt else abbreviatedConvertPrompt
            val textHtml = HtmlCompat.fromHtml(previewText, HtmlCompat.FROM_HTML_MODE_LEGACY)

            result_preview.setText(textHtml, TextView.BufferType.SPANNABLE)
        } else {
            unit_input.visibility = View.GONE
            try {
                val parseResult = evaluateHumanized(input.text.toString(), preferredUnits)
                humanizedQuantity = parseResult.value
                val coloredText = formatResultForDisplay(humanizedQuantity, R.color.detail_text)

                result_preview.setText(coloredText, TextView.BufferType.SPANNABLE)
                input.errors = parseResult.errors
            } catch (e: Exception) {
                result_preview.text = getString(R.string.result_error)
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

    fun populateUnitSelector(units: Collection<AtomicHumanUnit>, container: ViewGroup) {
        container.removeAllViews()
        for (unit in units) {
            val newButton = layoutInflater.inflate(R.layout.button_unit, null) as CalculatorButton
            newButton.primaryCommand = if (unit.abbreviation == "1") "" else unit.abbreviation
            newButton.text = unit.abbreviation
            newButton.setOnLongClickListener {
                val floating = createFloatingSecondaryFor(newButton)
                val info = layoutInflater.inflate(R.layout.floating_info, floating, false) as TextView

                val tooltipText = when {
                    unit is PrefixUnit -> getString(R.string.unit_derivation_prefix, getString(unit.nameResourceId), getString(unit.descriptionResourceId), unit.exponent)
                    unit.unitDerivation != null -> getString(R.string.unit_derivation_derived, getString(unit.nameResourceId), unit.unitDerivation)
                    else -> getString(R.string.unit_derivation_base, getString(unit.nameResourceId))
                }

                info.text = tooltipText
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

    private fun formatResultForDisplay(humanQuantity: HumanQuantity, colorResourceId: Int): Spanned {
        val colorStr = hexStringForColor(colorResourceId)

        // Find something that fits
        // This loop may potentially run quite a few times if the starting number is very
        // long, but it should be quick enough
        var humanString = humanQuantity.humanString(resultSeparator)
        while (!stringFits(humanString.string, result_preview) && humanString.string.isNotEmpty()) {
            humanString = humanQuantity.humanString(humanString.string.length - 1, resultSeparator, numberFormat)
        }

        val coloredText =
                if (sigfigHighlight)
                    humanString.string
                            .replaceRange(humanString.insigfigEnd, humanString.insigfigEnd, "</font>")
                            .replaceRange(humanString.insigfigStart, humanString.insigfigStart, "<font color='$colorStr'>")
                else humanString.string

        // https://stackoverflow.com/questions/10140893/android-multi-color-in-one-textview
        return HtmlCompat.fromHtml(coloredText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun stringFits(input: String, view: TextView): Boolean {
        val availableSpacePx = view.width - view.paddingRight - view.paddingLeft
        val textSize: Float = view.paint.measureText(input)
        return textSize <= availableSpacePx
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

        updateSkinIn(viewGroup, colorMap)
    }

    private fun doClearAnimation() {
        val circleAnimation = GrowingCircle(skin.primary, editor_area)
        circleAnimation.registerAnimationCallback(object : Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable) {
                editor_area.foreground = null
            }
        })
        circleAnimation.peakCallback = {
            editingUnit = false
            unit_input.text.clear()
            input.text.clear()
            preferredUnits.clear()
        }
        editor_area.foreground = circleAnimation

        circleAnimation.start()
    }
}