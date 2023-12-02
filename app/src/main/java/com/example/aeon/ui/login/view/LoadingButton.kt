package com.example.aeon.ui.login.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.text.TextUtils
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.graphics.ColorUtils
import com.example.aeon.extension.setTextSizeDp
import com.github.luvin1.android.utils.Layout
import kotlin.math.abs

@SuppressLint("ViewConstructor")
class LoadingButton(
    context: Context,
    private val defaultText: String,
    private val loadingText: String
) : FrameLayout(context) {

    var isLoading: Boolean = false
        set(value) {
            if (value == isLoading) return
            field = value
            setTextAnimated(value)
            setShimmer(value)
        }

    private val textView: TextView

    private var textAnimator: ValueAnimator? = null
    private val shimmerPercent: Float = 0.5f
    private val shimmerColor: Int = ColorUtils.setAlphaComponent(Color.WHITE, 256 / 4)
    private var shimmerRect: RectF? = null
    private var shimmerPaint: Paint? = null
    private var shimmerAnimator: ObjectAnimator? = null
    private var shimmerProgress: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    init {
        setWillNotDraw(false)
        setBackgroundColor(Color.BLACK)
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, view.height / 2f)
            }
        }
        clipToOutline = true

        textView = TextView(context).apply {
            setTextColor(Color.WHITE)
            setTextSizeDp(18f)
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            text = defaultText
        }
        addView(
            textView, Layout.frame(
                Layout.WRAP_CONTENT, Layout.WRAP_CONTENT,
                Gravity.CENTER
            )
        )
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return isLoading || super.dispatchTouchEvent(ev)
    }

    override fun onDraw(canvas: Canvas) {
        if (!isLoading || shimmerAnimator?.isRunning == false || shimmerRect == null || shimmerPaint == null) return

        canvas.apply {
            save()
            translate(width * shimmerProgress, 0f)
            drawRect(shimmerRect!!, shimmerPaint!!)
            restore()
        }
    }

    private fun setTextAnimated(loading: Boolean) {
        textAnimator?.end()

        var half = false
        textAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 200
            interpolator = DecelerateInterpolator()

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    textAnimator = null
                }
            })

            addUpdateListener {
                val v = it.animatedValue as Float

                if (v <= 0.5f) {
                    textView.apply {
                        alpha = 1 - v * 2
                        translationY = height * v
                    }
                } else {
                    textView.apply {
                        if (!half) {
                            half = true
                            text = if (loading) loadingText else defaultText
                        }
                        alpha = (v - 0.5f) * 2
                        translationY = -height * (0.5f - abs(0.5f - v))
                    }
                }
            }
        }.also {
            it.start()
        }
    }

    private fun setShimmer(loading: Boolean) {
        if (!loading) {
            shimmerRect = null
            shimmerPaint = null
            shimmerAnimator?.run {
                cancel()
                shimmerAnimator = null
            }
            invalidate()
            return
        }

        shimmerRect = RectF(0f, 0f, width * shimmerPercent, height.toFloat())
        shimmerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                shimmerRect!!.left, shimmerRect!!.top,
                shimmerRect!!.right, shimmerRect!!.top,
                intArrayOf(Color.TRANSPARENT, shimmerColor, Color.TRANSPARENT),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        shimmerAnimator = ObjectAnimator.ofFloat(this, "shimmerProgress", -shimmerPercent, 1f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
        }.also {
            it.start()
        }
    }
}