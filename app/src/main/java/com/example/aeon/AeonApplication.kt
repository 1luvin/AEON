package com.example.aeon

import android.app.Application
import com.github.luvin1.android.utils.Layout
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AeonApplication : Application() {

    companion object {
        private lateinit var Instance: AeonApplication
        fun getInstance(): AeonApplication = Instance
    }

    override fun onCreate() {
        super.onCreate()
        Instance = this

        Layout.initialize(context = this)
    }
}