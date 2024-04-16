package com.example.mvvm.domain.repository

import com.example.mvvm.domain.model.local.Payment

interface PaymentRepository {

    suspend fun getPayments(token: String): List<Payment>
}