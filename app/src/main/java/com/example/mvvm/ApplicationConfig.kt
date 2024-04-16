package com.example.mvvm

object ApplicationConfig {

    const val baseUrl: String = "https://easypay.world/api-test/"

    val apiHeaders: Map<String, String> = mapOf(
        "app-key" to "12345",
        "v" to "1"
    )

    // Must be set after successful sign in
    var paymentsToken: String? = null
}