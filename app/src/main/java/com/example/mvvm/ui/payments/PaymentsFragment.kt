package com.example.mvvm.ui.payments

import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.ApplicationConfig
import com.example.mvvm.R
import com.example.mvvm.extension.dp
import com.example.mvvm.extension.setPaddingDp
import com.example.mvvm.extension.setTextSizeDp
import com.example.mvvm.ui.custom.BaseFragment
import com.example.mvvm.ui.login.view.LoadingButton
import com.example.mvvm.ui.util.BaseException
import com.github.luvin1.android.utils.Layout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class PaymentsFragment : BaseFragment<PaymentsViewModel>() {

    override val viewModel: PaymentsViewModel by viewModels()

    private lateinit var rootLayout: LinearLayout
    private lateinit var titleView: TextView
    private lateinit var paymentsView: RecyclerView
    private val paymentsAdapter: PaymentsAdapter = PaymentsAdapter()
    private lateinit var logoutButton: LoadingButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadPayments()
    }

    override fun view(): View {
        titleView = TextView(context).apply {
            setTextColor(context.getColor(R.color.text))
            setTextSizeDp(30f)
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            setText(R.string.Payments)
        }

        paymentsView = RecyclerView(context).apply {
            setPaddingDp(20, 0, 20, 0)
            clipToPadding = true
            overScrollMode = View.OVER_SCROLL_NEVER
            isNestedScrollingEnabled = false
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val indent = 6.dp
                    when (parent.getChildAdapterPosition(view)) {
                        0 -> outRect.bottom = indent
                        parent.childCount -> outRect.top = indent
                        else -> outRect.apply {
                            top = indent
                            bottom = indent
                        }
                    }
                }
            })
            adapter = paymentsAdapter
        }

        logoutButton = LoadingButton(
            context,
            defaultText = context.getString(R.string.SignOut),
            loadingText = context.getString(R.string.SigningOut)
        ).apply {
            setOnClickListener {
                logout()
            }
        }

        rootLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            // Suppressing back button
            isFocusableInTouchMode = true
            requestFocus()
            setOnKeyListener { v, keyCode, event ->
                keyCode == KeyEvent.KEYCODE_BACK
            }
            //

            addView(
                titleView, Layout.linear(
                    Layout.MATCH_PARENT, Layout.WRAP_CONTENT,
                    20, 30, 20, 15
                )
            )

            addView(
                paymentsView, Layout.linear(
                    Layout.MATCH_PARENT, 0,
                    weight = 1f
                )
            )

            addView(
                logoutButton, Layout.linear(
                    Layout.MATCH_PARENT, 56,
                    20, 15, 20, 15
                )
            )
        }

        return rootLayout
    }

    private fun loadPayments() {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val payments = withContext(Dispatchers.IO) {
                    viewModel.getPayments(ApplicationConfig.paymentsToken!!)
                }
                paymentsAdapter.submitList(payments)
            } catch (e: BaseException) {
                showToast(e.messageId)
            }
        }
    }

    private fun logout() {
        parentFragmentManager.popBackStack()
    }
}