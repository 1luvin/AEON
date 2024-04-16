package com.example.mvvm.data.repository

import com.example.mvvm.R
import com.example.mvvm.data.remote.Api
import com.example.mvvm.data.remote.login.LoginRequest
import com.example.mvvm.domain.repository.AuthRepository
import com.example.mvvm.ui.util.BaseException

class AuthRepositoryImpl(
    private val api: Api
) : AuthRepository {

    override suspend fun login(login: String, password: String): String {
        val request = LoginRequest(login, password)
        val response = api.login(request)
        return response.body()?.token ?: throw BaseException(R.string.error_login_credentials)
    }
}