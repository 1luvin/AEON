package com.example.aeon.domain.repository

interface AuthRepository {

    suspend fun login(login: String, password: String): String
}