package com.example.aeon.domain.repository

import com.example.aeon.domain.model.local.Payment

interface PaymentRepository {

    suspend fun getPayments(token: String): List<Payment>
}