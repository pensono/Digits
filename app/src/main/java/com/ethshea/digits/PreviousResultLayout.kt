package com.ethshea.digits

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.support.v4.view.ViewCompat.getMeasuredState
import android.view.View


/**
 * @author Ethan
 */
class PreviousResultLayout(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        // Copied from https://developer.android.com/reference/android/view/ViewGroup
//
//        val count = childCount
//
//        // Measurement will ultimately be computing these values.
//        val maxHeight = MeasureSpec.getSize(widthMeasureSpec)
//        val maxWidth = MeasureSpec.getSize(heightMeasureSpec)
//        var childState = 0
//
//        // Iterate through all children, measuring them and computing our dimensions
//        // from their size.
//        for (i in 0 until count) {
//            val child = getChildAt(i)
//            if (child.visibility != GONE) {
//                // Measure the child.
//                measureChild(child, widthMeasureSpec, heightMeasureSpec)
//                childState = View.combineMeasuredStates(childState, child.measuredState)
//            }
//        }
//
//        // Report our final dimensions.
//        setMeasuredDimension(View.resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
//                View.resolveSizeAndState(maxHeight, heightMeasureSpec,
//                        childState shl View.MEASURED_HEIGHT_STATE_SHIFT))
//    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int)  {
        val contentLeft = paddingLeft
        val contentRight = right - left - paddingRight
        val contentWidth = contentLeft - contentRight
        var currentBottom = bottom - top - paddingBottom

        // Stack vertically from top to bottom
        // Roughly following https://developer.android.com/reference/android/view/ViewGroup
        for (i in (0 until childCount).reversed()) {
            val child = getChildAt(i)

            if (child.visibility != GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(contentWidth, MeasureSpec.AT_MOST),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))

                val childLeft = contentRight - child.measuredWidth
                val childBottom = currentBottom
                val childTop = currentBottom - child.measuredHeight

                child.layout(childLeft, childTop, contentRight, childBottom)

                currentBottom = childTop
            }
        }
    }

}