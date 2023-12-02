package com.example.aeon.data.repository

import com.example.aeon.R
import com.example.aeon.data.remote.Api
import com.example.aeon.domain.model.local.Payment
import com.example.aeon.domain.repository.PaymentRepository
import com.example.aeon.ui.util.BaseException

class PaymentRepositoryImpl(
    private val api: Api
) : PaymentRepository {

    override suspend fun getPayments(token: String): List<Payment> {
        val response = api.getPayments(token)
        return response.body()?.map { it.convert() } ?: throw BaseException(R.string.error_payments_load)
    }
}