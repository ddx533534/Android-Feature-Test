package com.ddx.kt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ddx.kt.HashRing


data class HashRingState(var hashRing: HashRing)

class HashRingViewModel : ViewModel() {
    var hashRingState by mutableStateOf(value = HashRingState(HashRing()))

    fun insertNode(hashValue: Int): Int {
        val res = hashRingState.hashRing.insertNode(hashValue)
        val ring = hashRingState.hashRing
        hashRingState = hashRingState.copy(hashRing = HashRing(ring))
        return res
    }

    fun getNodes(): List<Int> {
        return hashRingState.hashRing.getNodes()
    }

    fun getRingSize(): Int {
        return hashRingState.hashRing.getRingSize()
    }

    fun isHead(hashValue: Int): Boolean = hashRingState.hashRing.isHead(hashValue)

    fun clear() {
        hashRingState = hashRingState.copy(hashRing = HashRing())
    }
}