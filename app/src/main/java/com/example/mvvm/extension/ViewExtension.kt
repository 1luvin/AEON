package com.example.mvvm.extension

import android.util.TypedValue
import android.view.View
import android.widget.TextView

/*
    View
 */

fun View.setPaddingDp(left: Int, top: Int, right: Int, botton: Int) {
    setPadding(left.dp, top.dp, right.dp, botton.dp)
}

/*
    TextView
 */

fun TextView.setTextSizeDp(value: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_DIP, value)
}