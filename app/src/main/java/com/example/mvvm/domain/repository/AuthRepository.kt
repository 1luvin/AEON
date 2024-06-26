package com.example.mvvm.domain.repository

interface AuthRepository {

    suspend fun login(login: String, password: String): String
}