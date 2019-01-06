package com.monotonic.digits.skin

import android.graphics.*
import android.graphics.drawable.Drawable



/**
 * @author Ethan
 */
internal class SkinDrawable(val theme: Skin) : Drawable() {
    private val fillPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val primaryPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val accentPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        fillPaint.color = theme.fill
        primaryPaint.color = theme.primaryDark
        accentPaint.color = theme.primary
    }

    val bigCircleSize = 80.0f / 96.0f
    val smallCircleSize = 40.0f / 96.0f
    val smallCircleOffset = 0.0f / 96.0f

    override fun draw(canvas: Canvas) {
        val intBounds = bounds
        val drawBounds = RectF(intBounds)

        canvas.drawCircle(drawBounds.width() / 2, bigCircleSize * drawBounds.width() / 2, bigCircleSize * drawBounds.width() / 2, primaryPaint)

        val smallBounds = RectF(drawBounds.left, drawBounds.bottom - drawBounds.width() * (smallCircleSize + smallCircleOffset),
                drawBounds.left + drawBounds.width() * smallCircleSize, drawBounds.bottom - drawBounds.width() * smallCircleOffset)
        canvas.drawOval(smallBounds, fillPaint)
        smallBounds.offsetTo(drawBounds.right - drawBounds.width() * smallCircleSize, smallBounds.top)
        canvas.drawOval(smallBounds, accentPaint)
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT
}