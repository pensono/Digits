package com.monotonic.digits

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.EditText
import com.monotonic.digits.evaluator.ErrorMessage
import java.lang.Math.max


/**
 * @author Ethan
 */
class ErrorInput : EditText, TextWatcher {
    companion object {
        val TAG = "Digits_ErrorInput"
    }

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    var errors: Collection<ErrorMessage> = listOf()
    var underlinePaint: Paint = Paint()
    var rect: Rect = Rect()

    var underlineOffset: Float = 0.0f
    var underlineColor: Int
        get() = underlinePaint.color
        set(value) {
            underlinePaint.color = value
        }
    var underlineThickness: Float
        get() = underlinePaint.strokeWidth
        set(value) {
            underlinePaint.strokeWidth = value
        }

    private var maxTextSizePx: Float = 0.0f
    private var minTextSizePx: Float = 0.0f
    private var textSizeStepPx: Float = 0.0f

    private fun init(context: Context?, attributeSet: AttributeSet?, defStyle: Int) {
        val density = context!!.resources.displayMetrics.density

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ErrorInput, defStyle, 0)
        underlineColor = typedArray.getColor(R.styleable.ErrorInput_underlineColor, -0x10000)
        underlineThickness = typedArray.getDimension(R.styleable.ErrorInput_underlineThickness, density * 2)
        underlineOffset = typedArray.getDimension(R.styleable.ErrorInput_underlineOffset, underlineThickness)

        maxTextSizePx = typedArray.getDimension(R.styleable.ErrorInput_maxTextSize, textSize)
        minTextSizePx = typedArray.getDimension(R.styleable.ErrorInput_minTextSize, textSize)
        textSizeStepPx = typedArray.getDimension(R.styleable.ErrorInput_textSizeStep, 0.0f)

        typedArray.recycle()

        underlinePaint.style = Paint.Style.STROKE

        setShadowLayer(paddingEnd.toFloat(), 0f, 0f, Color.TRANSPARENT)

        addTextChangedListener(this)
    }

    override fun onDraw(canvas: Canvas) {
        val underlineY = getLineBounds(0, rect).toFloat() + underlineOffset
        val left = layout.getLineLeft(0)

        for (error in errors) {
            // Ideally no min/max is needed
            if (error.location.a < 0) {
                Log.w(TAG, "Error location is ${error.location} for input ${this.text}. Snapping a to 0")
                error.location.a = 0
            }

            if (error.location.b + 1 > text.length) {
                Log.w(TAG, "Error location is ${error.location} for input ${this.text}. Snapping b to ${text.length - 1}")
                error.location.b = text.length - 1
            }

            val start = paint.measureText(text, 0, error.location.a)
            val end = paint.measureText(text, 0, error.location.b + 1)
            canvas.drawLine(left + start, underlineY, left + end, underlineY, underlinePaint)
        }

        super.onDraw(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        fitText(text)
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        fitText(s)
    }

    fun fitText(s: CharSequence?) {
        // It would be nice to make the ErrorInput handle scrolling itself.
        if (measuredWidth == 0) {
            return // Likely the inflation process hasn't finished yet.
        }

        setTextSize(TypedValue.COMPLEX_UNIT_PX, maxTextSizePx)

        while (paint.measureText(s.toString()) > measuredWidth - paddingEnd && textSize > minTextSizePx) {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, max(textSize - textSizeStepPx, minTextSizePx))
        }
    }
}