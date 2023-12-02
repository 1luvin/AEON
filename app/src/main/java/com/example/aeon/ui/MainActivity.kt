package com.example.aeon.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.example.aeon.R
import com.example.aeon.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var fragmentContainerView: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupWindow()
        setContentView(view())
        presentLoginFragment()
    }

    private fun setupWindow() {
        window?.apply {
            statusBarColor = Color.WHITE
            navigationBarColor = Color.WHITE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                navigationBarDividerColor = Color.WHITE
            }
            WindowInsetsControllerCompat(this, decorView).apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }
        }
    }

    private fun view(): View {
        fragmentContainerView = FragmentContainerView(this).apply {
            id = R.id.fragment_container
            setBackgroundColor(getColor(R.color.bg))
        }

        return fragmentContainerView
    }

    private fun presentLoginFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<LoginFragment>(R.id.fragment_container)
        }
    }
}