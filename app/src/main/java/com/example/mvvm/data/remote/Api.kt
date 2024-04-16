package com.example.mvvm.data.remote

import com.example.mvvm.data.remote.login.LoginRequest
import com.example.mvvm.data.remote.login.LoginResponse
import com.example.mvvm.domain.model.remote.PaymentDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface Api {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("payments")
    suspend fun getPayments(@Header("token") token: String): Response<List<PaymentDto>>
}