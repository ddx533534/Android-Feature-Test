package com.example.androidfeature.listener

interface TypedClass<T> {
    suspend fun post(parameter: T)
}