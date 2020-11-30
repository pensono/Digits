package com.monotonic.digits

/**
 * @author Ethan
 */

import android.animation.TimeAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.os.SystemClock
import androidx.annotation.ColorInt
import android.util.Log
import android.view.View

class GrowingCircle(@ColorInt color: Int, private val parent: View) : Drawable(), Animatable2, Runnable {
    private val paint = Paint()

    private var currentRadius = 0.0f
    private var currentAlpha = 255
    private var startTime = 0L

    private val growDuration = 500.0f
    private val fadeDuration = 300.0f
    private val animationDuration = growDuration + fadeDuration
    private val updatePeriodMs = 1000 / 60

    private val endRadius = 1.2f // percent of the canvas width

    private val callbacks = mutableListOf<Animatable2.AnimationCallback>()
    var peakCallback : () -> Unit = {}

    init {
        paint.color = color
    }

    override fun draw(canvas: Canvas) {
        paint.alpha = currentAlpha
        canvas.drawCircle(canvas.width / 2.0f, canvas.height / 2.0f, currentRadius * canvas.width, paint)
    }

    override fun run() {
        val totalTime = SystemClock.uptimeMillis() - startTime
        currentRadius = Math.min(endRadius * totalTime / growDuration, endRadius)
        currentAlpha = Math.min(((1.0 - ((totalTime - growDuration) / fadeDuration)) * 255).toInt(), 255)

//        invalidateSelf()
        parent.invalidate() // For some reason, invalidateSelf doesn't work, so just call it on the parent directly
        scheduleSelf(this, SystemClock.uptimeMillis() + updatePeriodMs)

        if (totalTime > animationDuration) {
            stop()
        }

        if (totalTime > growDuration) {
            peakCallback()
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int { return PixelFormat.OPAQUE; }

    override fun setColorFilter(colorFilter: ColorFilter?) { }

    override fun isRunning(): Boolean {
        return SystemClock.uptimeMillis() < startTime + animationDuration
    }

    override fun start() {
        startTime = SystemClock.uptimeMillis()
        callbacks.forEach { it.onAnimationStart(this) }
        scheduleSelf(this, startTime + updatePeriodMs)
    }

    override fun stop() {
        callbacks.forEach { it.onAnimationEnd(this) }
    }

    override fun registerAnimationCallback(callback: Animatable2.AnimationCallback) {
        callbacks.add(callback)
    }

    override fun clearAnimationCallbacks() {
        callbacks.clear()
    }

    override fun unregisterAnimationCallback(callback: Animatable2.AnimationCallback): Boolean {
        callbacks.remove(callback)
        return true
    }
}