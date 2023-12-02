package com.example.aeon.data.repository

import com.example.aeon.R
import com.example.aeon.data.remote.Api
import com.example.aeon.data.remote.login.LoginRequest
import com.example.aeon.domain.repository.AuthRepository
import com.example.aeon.ui.util.BaseException

class AuthRepositoryImpl(
    private val api: Api
) : AuthRepository {

    override suspend fun login(login: String, password: String): String {
        val request = LoginRequest(login, password)
        val response = api.login(request)
        return response.body()?.token ?: throw BaseException(R.string.error_login_credentials)
    }
}