package com.ethshea.digits

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView
import android.text.Layout
import android.widget.EditText
import com.ethshea.digits.evaluator.ErrorMessage
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.SpannableString
import android.text.style.UnderlineSpan
import kotlinx.android.synthetic.main.activity_main.*


/**
 * @author Ethan
 */
class ErrorInput: EditText {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr)
    }

    var errors : Collection<ErrorMessage> = listOf()
    var underlinePaint: Paint = Paint()
    var rect : Rect = Rect()

    private fun init(context: Context?, attributeSet: AttributeSet?, defStyle: Int) {
        val density = context!!.resources.displayMetrics.density

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ErrorInput, defStyle, 0)
        val underlineColor = typedArray.getColor(R.styleable.ErrorInput_underlineColor, -0x10000)
        val underlineThickness = typedArray.getDimension(R.styleable.ErrorInput_underlineThickness, density * 2)
        typedArray.recycle()

        underlinePaint.style = Paint.Style.STROKE
        underlinePaint.color = underlineColor
        underlinePaint.strokeWidth = underlineThickness
    }

    override fun onDraw(canvas: Canvas) {
        val baseline = getLineBounds(0, rect).toFloat()
        val left = layout.getLineLeft(0)

        for (error in errors) {
            val start = paint.measureText(text, 0, error.position)
            val end = paint.measureText(text, 0, error.position + 1)
            canvas.drawLine(left + start, baseline, left + end, baseline, underlinePaint)
        }

        System.out.println(errors)

        // Multiline
//        for (i in 0 until count) {
//            val baseline = getLineBounds(i, rect)
//            firstCharInLine = layout.getLineStart(i)
//            lastCharInLine = layout.getLineEnd(i)
//
//            x_start = layout.getPrimaryHorizontal(firstCharInLine)
//            x_diff = layout.getPrimaryHorizontal(firstCharInLine + 1) - x_start
//            x_stop = layout.getPrimaryHorizontal(lastCharInLine - 1) + x_diff
//
//            canvas.drawLine(x_start, baseline + mStrokeWidth, x_stop, baseline + mStrokeWidth, mPaint)
//        }

        super.onDraw(canvas)
    }
}