package com.example.aeon.ui.login.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.view.View
import android.view.ViewOutlineProvider
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.example.aeon.R
import com.example.aeon.extension.setPaddingDp
import com.example.aeon.extension.setTextSizeDp
import com.example.aeon.ui.util.InputRule
import com.github.luvin1.android.utils.Layout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("ViewConstructor")
class InputCell(
    context: Context,
    label: String,
    private val inputRules: Array<InputRule>? = null,
) : LinearLayout(context) {

    val inputText: String get() = inputView.text.toString()

    private val labelView: TextView
    private val inputView: EditText
    private val errorView: TextView

    private val typingScope: CoroutineScope = MainScope()
    private var typingJob: Job? = null

    private var errorAnimator: ValueAnimator? = null

    init {
        orientation = VERTICAL

        labelView = TextView(context).apply {
            setTextColor(context.getColor(R.color.textSecondary))
            setTextSizeDp(17f)
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            text = label
        }
        addView(
            labelView, Layout.linear(
                Layout.MATCH_PARENT, Layout.WRAP_CONTENT
            )
        )

        inputView = EditText(context).apply {
            setPaddingDp(15, 0, 0, 0)
            setBackgroundColor(context.getColor(R.color.bgSecondary))
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(0, 0, view.width, view.height, view.height / 2f)
                }
            }
            clipToOutline = true

            setTextColor(context.getColor(R.color.text))
            setTextSizeDp(18f)
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            setHintTextColor(context.getColor(R.color.textSecondary))
            hint = label

            inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
                val futureText = dest.replaceRange(dstart, dend, source.subSequence(start, end))
                if (futureText.find { it.isWhitespace() } == null) {
                    inputRules?.let { restartTypingCallback() }
                    null
                } else {
                    ""
                }
            })
        }
        addView(
            inputView, Layout.linear(
                Layout.MATCH_PARENT, 46,
                0, 5, 0, 4
            )
        )

        errorView = TextView(context).apply {
            setTextColor(context.getColor(R.color.textNegative))
            setTextSizeDp(15f)
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
        }
        addView(
            errorView, Layout.linear(
                Layout.MATCH_PARENT, Layout.WRAP_CONTENT
            )
        )
    }

    private fun restartTypingCallback() {
        typingJob?.cancel()
        typingJob = typingScope.launch {
            delay(333)
            checkInput()
        }
    }

    fun checkInput(): Boolean {
        return inputRules?.find { !inputText.matches(it.regex) }?.let {
            setErrorText(it.mismatchText)
            false
        } ?: run {
            setErrorText("")
            true
        }
    }

    private fun setErrorText(text: String) {
        if (text == errorView.text) return

        errorAnimator?.end()

        var half = false
        errorAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 200
            interpolator = DecelerateInterpolator()

            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    errorAnimator = null
                }
            })

            addUpdateListener {
                val v = it.animatedValue as Float

                if (v <= 0.5f) {
                    errorView.apply {
                        alpha = 1 - v * 2
                        translationY = height * v
                    }
                } else {
                    errorView.apply {
                        if (!half) {
                            half = true
                            this.text = text
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
}