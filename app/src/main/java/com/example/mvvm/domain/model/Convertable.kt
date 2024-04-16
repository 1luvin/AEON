package com.example.mvvm.domain.model

interface Convertable<T> {

    fun convert(): T
}