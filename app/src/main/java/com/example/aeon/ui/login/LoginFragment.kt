package com.example.aeon.ui.login

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.aeon.R
import com.example.aeon.ui.custom.BaseFragment
import com.example.aeon.ui.login.view.InputCell
import com.example.aeon.ui.login.view.LoadingButton
import com.example.aeon.ui.payments.PaymentsFragment
import com.example.aeon.ui.util.BaseException
import com.example.aeon.ui.util.InputRule
import com.github.luvin1.android.utils.Layout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewModel>() {

    override val viewModel: LoginViewModel by viewModels()

    private lateinit var rootLayout: LinearLayout
    private lateinit var logoView: ImageView
    private lateinit var loginInputCell: InputCell
    private lateinit var passwordInputCell: InputCell
    private lateinit var signInButton: LoadingButton

    override fun view(): View {
        logoView = ImageView(context).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
            adjustViewBounds = true
            setImageResource(R.drawable.aeon)
            setColorFilter(context.getColor(R.color.text))
        }

        loginInputCell = InputCell(
            context,
            label = context.getString(R.string.Login),
            inputRules = arrayOf(
                InputRule(
                    regex = Regex("^\\w{4,20}\$"),
                    mismatchText = context.getString(R.string.error_login_length, "4", "20")
                ),
                InputRule(
                    regex = Regex("^[a-zA-Z0-9_.]+\$"),
                    mismatchText = context.getString(R.string.error_login_characters)
                )
            )
        )

        passwordInputCell = InputCell(
            context,
            label = context.getString(R.string.Password),
            inputRules = arrayOf(
                InputRule(
                    regex = Regex("^\\w+\$"),
                    mismatchText = context.getString(R.string.error_EmptyField)
                )
            )
        )

        signInButton = LoadingButton(
            context,
            defaultText = context.getString(R.string.SignIn),
            loadingText = context.getString(R.string.SigningIn)
        ).apply {
            setOnClickListener {
                login()
            }
        }

        rootLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL

            addView(
                logoView, Layout.linear(
                    Layout.MATCH_PARENT, Layout.WRAP_CONTENT,
                    60, 0, 60, 0
                )
            )

            addView(
                loginInputCell, Layout.linear(
                    Layout.MATCH_PARENT, Layout.WRAP_CONTENT,
                    20, 0, 20, 8
                )
            )

            addView(
                passwordInputCell, Layout.linear(
                    Layout.MATCH_PARENT, Layout.WRAP_CONTENT,
                    20, 0, 20, 0
                )
            )

            addView(
                Space(context), Layout.linear(
                    Layout.MATCH_PARENT, 0,
                    weight = 1f
                )
            )

            addView(
                signInButton, Layout.linear(
                    Layout.MATCH_PARENT, 56,
                    20, 15, 20, 15
                )
            )
        }

        return rootLayout
    }

    private fun login() {
        if (!loginInputCell.checkInput() or !passwordInputCell.checkInput()) return

        lifecycleScope.launch(Dispatchers.Main) {
            signInButton.isLoading = true
            try {
                val token = withContext(Dispatchers.IO) {
                    viewModel.login(
                        login = loginInputCell.inputText,
                        password = passwordInputCell.inputText
                    )
                }
                val bundle = bundleOf(PaymentsFragment.KEY_TOKEN to token)
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
                    replace<PaymentsFragment>(R.id.fragment_container, args = bundle)
                    addToBackStack(null)
                }
            } catch (e: BaseException) {
                showToast(e.messageId)
            }
            signInButton.isLoading = false
        }
    }
}