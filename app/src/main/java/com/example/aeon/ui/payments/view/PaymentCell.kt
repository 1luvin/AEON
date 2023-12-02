package com.example.aeon.ui.payments.view

import android.content.Context
import android.graphics.Outline
import android.text.TextUtils
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.TextView
import com.example.aeon.R
import com.example.aeon.domain.model.local.Payment
import com.example.aeon.extension.dp
import com.example.aeon.extension.setPaddingDp
import com.example.aeon.extension.setTextSizeDp
import com.github.luvin1.android.utils.Layout

class PaymentCell(context: Context) : FrameLayout(context) {

    var payment: Payment? = null
        set(value) {
            field = value
            value?.let {
                titleView.text = it.title
                amountView.text = it.amount()
                createdView.text = context.getString(R.string.CreatedAt, it.created())
            }
            requestLayout()
        }

    private val titleView: TextView
    private var amountView: TextView
    private var createdView: TextView

    private val indent: Int = 12.dp
    private val indent2: Int = 3.dp

    init {
        setPaddingDp(15, 10, 15, 10)
        setBackgroundColor(context.getColor(R.color.bgSecondary))
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, view.height / 4f)
            }
        }
        clipToOutline = true

        titleView = TextView(context).apply {
            setTextColor(context.getColor(R.color.text))
            setTextSizeDp(17f)
            maxLines = 2
            ellipsize = TextUtils.TruncateAt.END
        }
        addView(
            titleView, Layout.frame(
                Layout.WRAP_CONTENT, Layout.WRAP_CONTENT
            )
        )

        amountView = TextView(context).apply {
            setTextColor(context.getColor(R.color.accent))
            setTextSizeDp(17f)
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
        }
        addView(
            amountView, Layout.frame(
                Layout.WRAP_CONTENT, Layout.WRAP_CONTENT
            )
        )

        createdView = TextView(context).apply {
            setTextColor(context.getColor(R.color.textSecondary))
            setTextSizeDp(15f)
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
        }
        addView(
            createdView, Layout.frame(
                Layout.WRAP_CONTENT, Layout.WRAP_CONTENT
            )
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val amountWidth = amountView.run {
            measure(
                MeasureSpec.makeMeasureSpec((width - indent) / 2, MeasureSpec.AT_MOST),
                0
            )
            measuredWidth
        }

        var finalHeight = paddingTop + paddingBottom
        titleView.measure(
            MeasureSpec.makeMeasureSpec(width - amountWidth, MeasureSpec.AT_MOST),
            0
        )
        finalHeight += titleView.measuredHeight

        createdView.run {
            measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                0
            )
            finalHeight += indent2 + measuredHeight
        }

        super.onMeasure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        var l: Int
        var t: Int
        amountView.let {
            l = measuredWidth - (it.measuredWidth + paddingRight)
            t = paddingTop
            it.layout(l, t, l + it.measuredWidth, t + it.measuredHeight)
        }

        createdView.let {
            l = paddingLeft
            t = paddingTop + titleView.measuredHeight + indent2
            it.layout(l, t, l + it.measuredWidth, t + it.measuredHeight)
        }
    }
}