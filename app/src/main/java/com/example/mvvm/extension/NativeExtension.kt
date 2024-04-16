package com.example.mvvm.extension

import com.example.mvvm.Application

private val density: Float get() = Application.getInstance().resources.displayMetrics.density

/*
    Int
 */

val Int.dp get() = if (this == 0) 0 else (this * density).toInt()