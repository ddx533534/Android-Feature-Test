package com.example.androidfeature.arch.data

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class BlankViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            return PhotoViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

const val LIMIT = 1000000
const val LOOP = 100
fun testDownTo() {
    val start = System.currentTimeMillis()
    var i = 0
    var res = 0
    while(true) {
        if(i > LIMIT){
            break
        }
        i++
        for (j in 0..LOOP) {
            res += j
        }
        res = 0
    }
    val end = System.currentTimeMillis()
    println("time cost ${end - start} ms")
}