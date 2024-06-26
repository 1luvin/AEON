package com.example.mvvm.domain.model.remote

import com.example.mvvm.domain.model.Convertable
import com.example.mvvm.domain.model.local.Payment
import com.google.gson.annotations.SerializedName

data class PaymentDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("amount") val amount: Any?,
    @SerializedName("created") val created: Long?
) : Convertable<Payment> {

    override fun convert(): Payment {
        return Payment(
            id = id,
            title = title,
            amount = amount,
            created = created
        )
    }
}