package com.example.aeon.extension

import com.example.aeon.AeonApplication

private val density: Float get() = AeonApplication.getInstance().resources.displayMetrics.density

/*
    Int
 */

val Int.dp get() = if (this == 0) 0 else (this * density).toInt()