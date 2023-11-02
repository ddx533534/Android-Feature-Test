package com.example.androidfeature.activity


import androidx.appcompat.app.AppCompatActivity
import com.example.androidfeature.listener.TypedClass

const val TAG: String = "KTActivity";

class KTActivity : AppCompatActivity(),TypedClass<Int> {
    override suspend fun post(parameter: Int) {
    }

}