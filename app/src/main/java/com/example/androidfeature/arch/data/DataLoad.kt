package com.example.androidfeature.arch.data

import android.app.Activity
import android.content.Context

interface DataLoad<T> {

    fun syncLoad(context: Context): List<T>

    suspend fun asyncLoad(context: Context): List<T>
}