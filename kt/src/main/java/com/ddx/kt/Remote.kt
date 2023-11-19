package com.ddx.kt

import android.database.Observable
import kotlin.properties.Delegates.observable
import kotlin.reflect.KProperty

interface Remote {
    val name: String
        get() = "null"

    fun up()
}

abstract class RRemote(var name: String = "ddx") {

}

class PoliteString(var str: String) {
    operator fun getValue(thisRef: Nothing?, property: KProperty<*>): String =
        str.replace("stupid", "*****")


    operator fun setValue(thisRef: Nothing?, property: KProperty<*>, s: String) {
        str = s
    }

}

class TVRemote(val remote: Remote) : Remote by remote {

}

fun getStr(): String = "ddx"
fun test() {
    val str: String by lazy {
        "ddx"
    }
    var count by observable(0) { _, oldValue, newValue ->
        if (oldValue < newValue) 1 else 2
    }

    count++
    println(count)
    count - 2
    println(count)

    var comment: String by PoliteString("you are stupid!")
    println(comment)
}

fun isPrime(n: Int) = n > 1 && (2 until n).none { n % it == 0 }

fun main() {
    test()
}
