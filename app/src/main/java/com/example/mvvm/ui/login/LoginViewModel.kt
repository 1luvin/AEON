package com.example.mvvm.ui.login

import androidx.lifecycle.ViewModel
import com.example.mvvm.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    val loginRegex: Regex = Regex("^(?=.{4,20}\$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])\$")

    suspend fun login(login: String, password: String): String {
        return repository.login(login, password)
    }
}