package com.example.mvvm

import com.github.luvin1.android.utils.Layout
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : android.app.Application() {

    companion object {
        private lateinit var application: Application
        fun getInstance(): Application = application
    }

    override fun onCreate() {
        super.onCreate()
        application = this

        Layout.initialize(context = this)
    }
}