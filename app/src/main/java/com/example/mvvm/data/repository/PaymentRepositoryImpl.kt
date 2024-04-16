package com.example.mvvm.data.repository

import com.example.mvvm.R
import com.example.mvvm.data.remote.Api
import com.example.mvvm.domain.model.local.Payment
import com.example.mvvm.domain.repository.PaymentRepository
import com.example.mvvm.ui.util.BaseException

class PaymentRepositoryImpl(
    private val api: Api
) : PaymentRepository {

    override suspend fun getPayments(token: String): List<Payment> {
        val response = api.getPayments(token)
        return response.body()?.map { it.convert() } ?: throw BaseException(R.string.error_payments_load)
    }
}