package com.ethshea.digits

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import java.lang.reflect.Method

/**
 * Command text may contain a single pipe to specify the caret position after the text is inserted.
 * Alternatively, insert text may be "DEL" which signifies a deletion.
 *
 * @author Ethan
 */
class CalculatorButton(context: Context, attrs: AttributeSet) : Button(context, attrs) {
    /**
     * Command for the primary function of the button.
     */
    var primaryCommand = ""

    /**
     * Pair is from display text to command
     */
    var secondary = listOf<Pair<String, String>>()

    /**
     * How far to extend the click rectangle below the normal bounds. This is intended to be used
     * by the
     */
    var targetBottomExtendPx = 0

    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CalculatorButton)
        for (i in 0 until attributes.indexCount) {
            val attribute = attributes.getIndex(i)
            when (attribute) {
                R.styleable.CalculatorButton_primary -> {
                    primaryCommand = attributes.getString(i)
                }
                R.styleable.CalculatorButton_secondary -> {
                    val secondarySettingString = attributes.getString(i)

                    secondary = secondarySettingString.split(";")
                            .map { it.split(":") }
                            .map { it[0] to if (it.size < 2) it[0] else it[1] }
                }
                R.styleable.CalculatorButton_onLongClick -> {
                    // https://stackoverflow.com/questions/5706038/long-press-definition-at-xml-layout-like-androidonclick-does
                    // Assume the attribute is correct. We don't need to bother with error messages
                    val methodName = attributes.getString(i)
                    setOnLongClickListener(object : OnLongClickListener {
                        var handler : Method? = null

                        override fun onLongClick(view: View?): Boolean {
                            // https://stackoverflow.com/a/42210910/2496050
                            var listenerContext = getContext()
                            if (listenerContext is ContextWrapper && listenerContext !is Activity) { // Not sure what I'm doing here with the context
                                listenerContext = listenerContext.baseContext
                            }

                            handler = handler ?: listenerContext.javaClass.getMethod(methodName, CalculatorButton::class.java)
                            handler?.invoke(listenerContext, view) ?: return false
                            return true
                        }
                    })
                }
                R.styleable.CalculatorButton_targetBottomExtend -> {
                    targetBottomExtendPx = attributes.getDimensionPixelSize(attribute, 0)
                }
            }
        }

        attributes.recycle()
    }

    // Android says this needs to be overridden. Ok sure?
    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun getHitRect(outRect: Rect) {
        super.getHitRect(outRect)
        outRect.bottom += targetBottomExtendPx
    }
}