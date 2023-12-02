package com.example.aeon.domain.model.local

import com.example.aeon.domain.model.Convertable
import com.example.aeon.domain.model.remote.PaymentDto
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
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            formatter.format(date)
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