package com.example.mvvm.domain.model.local

import com.example.mvvm.domain.model.Convertable
import com.example.mvvm.domain.model.remote.PaymentDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

data class Payment(
    val id: Int,
    val title: String,
    val amount: Any?,
    val created: Long?
) : Convertable<PaymentDto> {

    private val emptyField: String = "–––"

    fun amount(): String {
        return if (amount == null || (amount is String && amount.isBlank())) {
            emptyField
        } else {
            amount.toString()
        }
    }

    fun created(): String {
        return created?.let {
            val date = Date(TimeUnit.SECONDS.toMillis(it))
            SimpleDateFormat.getDateInstance().format(date)
        } ?: emptyField
    }

    override fun convert(): PaymentDto {
        return PaymentDto(
            id = id,
            title = title,
            amount = amount,
            created = created
        )
    }
}