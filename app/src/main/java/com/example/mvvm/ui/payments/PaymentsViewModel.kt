package com.example.mvvm.ui.payments

import androidx.lifecycle.ViewModel
import com.example.mvvm.domain.model.local.Payment
import com.example.mvvm.domain.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    suspend fun getPayments(token: String): List<Payment> {
        return repository.getPayments(token)
    }
}