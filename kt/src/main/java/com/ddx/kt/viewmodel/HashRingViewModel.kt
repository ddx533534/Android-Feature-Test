package com.ddx.kt.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ddx.kt.HashRing
import com.ddx.kt.Node


data class HashRingState(
    var hashRing: HashRing,
    var curNodeSize: Int,
    var curResourceSize: Int
)

class HashRingViewModel : ViewModel() {
    private var hashRingState by mutableStateOf(value = HashRingState(HashRing(), 0, 0))


    fun newInstance(ringSize: Int) {
        hashRingState =
            hashRingState.copy(hashRing = HashRing(ringSize), curNodeSize = 0, curResourceSize = 0)
    }

    fun insertNode(hashValue: Int): Int {
        val res = hashRingState.hashRing.insertNode(hashValue)
        hashRingState = hashRingState.copy(curNodeSize = ++hashRingState.curNodeSize)
        return res
    }

    fun addResources(hashValue: Int): Int {
        val res = hashRingState.hashRing.addResource(hashValue)
        hashRingState = hashRingState.copy(curResourceSize = ++hashRingState.curResourceSize)
        return res
    }

    fun peekResourceByFingerTable(hashValue: Int):Int? = hashRingState.hashRing.chordLookUp(hashValue)?.hashValue

    fun buildFingerTable() {
        hashRingState.hashRing.buildFingerTable()
        hashRingState.hashRing.printHashRing()
    }

    fun getFingerTable(hashValue: Int): Map<Int, Node> = hashRingState.hashRing.getFingerTable(hashValue)


    fun getNodes(): List<Int> = hashRingState.hashRing.getNodes()
    fun getResources(): List<Int> = hashRingState.hashRing.getResources()

    fun getRingCapacity(): Int {
        return hashRingState.hashRing.getRingCapacity()
    }

    fun isHead(hashValue: Int): Boolean = hashRingState.hashRing.isHead(hashValue)

    fun clear() {
        hashRingState.hashRing.clear()
        hashRingState = hashRingState.copy(curResourceSize = 0, curNodeSize = 0)
    }
}