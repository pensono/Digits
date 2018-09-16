package com.ethshea.digits

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.View
import android.widget.Button

/**
 * @author Ethan
 */
class CalculatorButton(context: Context, attrs: AttributeSet) : Button(context, attrs) {
    var primary = ""
        private set

    /**
     * Guarenteed to be the same length as secondaryInsert
     */
    var secondaryText = listOf<String>()
        private set

    var secondaryInsert = listOf<String>()
        private set


    init {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.CalculatorButton)
        for (i in 0 until attributes.indexCount) {
            val attribute = attributes.getIndex(i)
            when (attribute) {
                R.styleable.CalculatorButton_primary -> {
                    primary = attributes.getString(i)
                }
                R.styleable.CalculatorButton_secondary -> {
                    val secondarySettingString = attributes.getString(i)

                    val secondarySettings = secondarySettingString.split(";")
                            .map { it.split(":") }

                    // Just let it error out if the setting isn't correct. It's unlikely to be set at runtime anyways
                    secondaryText = secondarySettings.map { it[0] }
                    secondaryInsert = secondarySettings.map { it[1] }
                }
                R.styleable.CalculatorButton_onLongClick -> {
                    // https://stackoverflow.com/questions/5706038/long-press-definition-at-xml-layout-like-androidonclick-does
                    // Assume the attribute is correct. We don't need to bother with error messages
                    val methodName = attributes.getString(i)
                    setOnLongClickListener(object : OnLongClickListener {
                        override fun onLongClick(view: View?): Boolean {
                            // https://stackoverflow.com/a/42210910/2496050
                            var listenerContext = getContext()
                            if (listenerContext is ContextWrapper) {
                                listenerContext = listenerContext.baseContext
                            }

                            val handler = listenerContext.javaClass.getMethod(methodName, CalculatorButton::class.java)
                            handler.invoke(listenerContext, view)
                            return true
                        }
                    })
                }
            }
        }

        attributes.recycle()
    }
}