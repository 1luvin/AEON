package com.example.aeon.domain.model

interface Convertable<T> {

    fun convert(): T
}