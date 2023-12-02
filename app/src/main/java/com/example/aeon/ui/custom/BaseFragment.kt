package com.example.aeon.ui.custom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<T: ViewModel> : Fragment() {

    override fun getContext(): Context = super.getContext()!!

    protected abstract val viewModel: T

    protected abstract fun view(): View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return view()
    }

    protected fun showToast(resId: Int) = Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
}