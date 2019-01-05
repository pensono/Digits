package com.monotonic.digits

import android.app.Activity
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import com.android.billingclient.api.*
import com.android.billingclient.api.BillingClient.BillingResponse
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.evaluator.evaluateExpression
import com.monotonic.digits.human.*
import com.monotonic.digits.theme.CustomTheme
import com.monotonic.digits.theme.ThemeType
import com.monotonic.digits.theme.createThemePickerDialog
import com.monotonic.digits.units.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.button_area.*


class MainActivity : Activity(), PurchasesUpdatedListener {
    val PRO_SKU = "com.monotonic.digits.pro"
    val TAG = "Digits_MainActivity"
    val history = mutableListOf<HistoryItem>()
    val preferredUnits = mutableMapOf<Map<String, Int>, HumanUnit>()

    var floating : View? = null
    var humanizedQuantity = HumanQuantity(SciNumber.Zero, HumanUnit(mapOf())) // Default value that should be overwritten quickly
    var editingUnit = false
    var hasPro = false

    private lateinit var billingClient: BillingClient

    val editingInput
        get() = if (editingUnit) unit_input else input

    // TODO move these colors to a file or something
    private val defaultTheme = CustomTheme(0x212121, 0x26a69a, 0xFFC107, ThemeType.LIGHT)
    private val resources : ThemedResources by lazy {
        ThemedResources(super.getResources(), defaultTheme)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    val unit = parseHumanUnit(unit_input.text.toString()) ?: return

                    if (unit.dimensionallyEqual(humanizedQuantity.unit)) {
                        preferredUnits[unit.dimensions] = unit
                        unit_input.text.clear()
                        editingUnit = false
                        updatePreview()
                    }
                } catch (e: Exception) {
                    result_preview.text = "Error"
                    Log.e(TAG, "Unit parsing error", e)
                }
            }
        })

        populateUnitSelector(UnitSystem.unitAbbreviations.values, unit_selector)
        populateUnitSelector(UnitSystem.prefixAbbreviations.values.filter { it.abbreviation != "" }, prefix_selector)
        prefix_selector_container.post { centerScroll(prefix_selector_container) }

        discipline_dropdown.adapter = GenericSpinnerAdapter(this, R.layout.spinner_item, disciplines) { getString(it.nameResource) }
        discipline_dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                populateUnitSelector(disciplines[pos].units, unit_selector)
            }
        }

        input.showSoftInputOnFocus = false
        unit_input.showSoftInputOnFocus = false

        result_preview.post { updatePreview() }

        billingClient = BillingClient.newBuilder(this).setListener(this).build()
        connectToBillingService()
    }

    fun connectToBillingService() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(@BillingResponse billingResponseCode: Int) {
                if (billingResponseCode == BillingResponse.OK) {
                    refreshPurchases()
                    Log.i(TAG, "Connected to billing service")
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                connectToBillingService()
            }
        })
    }

    private fun refreshPurchases() {
        val purchasesResult: Purchase.PurchasesResult =
                billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        if (purchasesResult.purchasesList != null && purchasesResult.purchasesList.any { it.sku == PRO_SKU })
            hasPro = true
    }

    private fun doProPurchase() {
        val flowParams = BillingFlowParams.newBuilder()
                .setSku(PRO_SKU)
                .setType(BillingClient.SkuType.INAPP)
                .build()
        val responseCode = billingClient.launchBillingFlow(this, flowParams)
    }

    override fun onPurchasesUpdated(@BillingResponse responseCode: Int, purchases: List<Purchase>?) {
        if (responseCode == BillingResponse.OK && purchases != null) {
            refreshPurchases()
        } else if (responseCode == BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
            Log.e(TAG, "Billing error: $responseCode")
        }
    }

    override fun onResume() {
        super.onResume()

        refreshPurchases()
    }

    fun calculatorButtonClick(button: View) {
        val buttonCommand = (button as CalculatorButton).primaryCommand
        if (buttonCommand == "ENT") {
            history += HistoryItem(input.text.toString(), result_preview.text.toString())
            input.text.replace(0, editingInput.text.length, humanizedQuantity.humanString().string)
        } else if (buttonCommand == "DEL") {
            if (editingInput.selectionStart == editingInput.selectionEnd && editingInput.selectionStart != 0) {
                editingInput.text.replace(editingInput.selectionStart-1, editingInput.selectionStart, "")
            } else {
                editingInput.text.replace(editingInput.selectionStart, editingInput.selectionEnd, "")
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

    fun toggleUnitConversion(view: View) {
        editingUnit = !editingUnit
        updatePreview()
    }

    fun openPopupMenu(view: View) {
        val popup = PopupMenu(this, view, Gravity.NO_GRAVITY)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_get_pro -> {
                    doProPurchase()
                    true
                }
                R.id.menu_customize -> {
                    val dialog = createThemePickerDialog(this, this::applyTheme)
                    dialog.show()
                    true
                }
                else -> false
            }
        }
        popup.inflate(R.menu.popup_menu)
        popup.show()
    }

    // It would be pretty schweet if this was in the CalculatorButton class itself
    fun calculatorButtonLongClick(button: CalculatorButton) {
        if (button.primaryCommand == "DEL") {
            editingInput.text.clear()
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

    private fun updatePreview() {
        if (editingUnit) {
            unit_input.visibility = View.VISIBLE
            unit_input.requestFocus()

            val hexColor = hexStringForColor(R.color.detail_text)
            val previewText = "<font color='$hexColor'>Convert </font>${humanizedQuantity.unitString()}<font color='$hexColor'> to:</font>"
            result_preview.setText(htmlToSpannable(previewText), TextView.BufferType.SPANNABLE)
        } else {
            unit_input.visibility = View.GONE
            try {
                val parseResult = evaluateExpression(input.text.toString())

                val preferredUnit = preferredUnits[parseResult.value.unit.dimensions]
                humanizedQuantity = if (preferredUnit == null) humanize(parseResult.value) else convert(parseResult.value, preferredUnit)
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
        var humanString = humanQuantity.humanString()
        val availableSpacePx = result_preview.width - result_preview.paddingRight - result_preview.paddingLeft
        while (result_preview.paint.measureText(humanString.string) >= availableSpacePx && humanString.string.length > 0) {
            humanString = humanQuantity.humanString(humanString.string.length - 1)
        }

        val coloredText = humanString.string
                .replaceRange(humanString.insigfigEnd, humanString.insigfigEnd, "</font>" )
                .replaceRange(humanString.insigfigStart, humanString.insigfigStart, "<font color='$colorStr'>")

        // https://stackoverflow.com/questions/10140893/android-multi-color-in-one-textview
        return htmlToSpannable(coloredText)
    }

    private fun htmlToSpannable(coloredText: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(coloredText, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(coloredText)
        }
    }

    private fun hexStringForColor(colorResourceId: Int) =
            '#' + ResourcesCompat.getColor(resources, colorResourceId, null).toString(16)

    override fun getResources(): Resources = resources

    private fun applyTheme(theme: CustomTheme) {
        resources.customTheme = theme
        mainRootLayout.invalidate()
    }
}