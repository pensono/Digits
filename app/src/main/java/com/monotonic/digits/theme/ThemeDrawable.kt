package com.monotonic.digits.theme

import android.graphics.*
import android.graphics.drawable.Drawable



/**
 * @author Ethan
 */
internal class ThemeDrawable(val fill: Int, val primary: Int, val accent: Int, val light: Boolean) : Drawable() {
    private val fillPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val primaryPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val accentPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        fillPaint.color = fill
        primaryPaint.color = primary
        accentPaint.color = accent
    }

    val bigCircleSize = 80.0f / 96.0f
    val smallCircleSize = 40.0f / 96.0f
    val smallCircleOffset = 8.0f / 96.0f

    override fun draw(canvas: Canvas) {
        val intBounds = bounds
        val drawBounds = RectF(intBounds)

        canvas.drawCircle(drawBounds.width() / 2, bigCircleSize * drawBounds.height() / 2, bigCircleSize * drawBounds.height() / 2, primaryPaint)

        val smallBounds = RectF(drawBounds.left, drawBounds.bottom - drawBounds.height() * (smallCircleSize + smallCircleOffset),
                drawBounds.left + drawBounds.width() * smallCircleSize, drawBounds.bottom - drawBounds.height() * smallCircleOffset)
        canvas.drawOval(smallBounds, fillPaint)
        smallBounds.offsetTo(drawBounds.right - drawBounds.width() * smallCircleSize, smallBounds.top)
        canvas.drawOval(smallBounds, accentPaint)
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}