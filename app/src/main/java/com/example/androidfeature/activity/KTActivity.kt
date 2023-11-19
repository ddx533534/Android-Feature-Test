package com.example.androidfeature.activity


import android.app.Activity
import com.example.androidfeature.listener.TypedClass
import kotlin.math.min

const val TAG: String = "KTActivity";

open class Fruit

class Banana : Fruit()

class Apple : Fruit()
//class Box<out T>(val value: T)

// 协变只允许作为函数的输出
interface Transformer<in T> {
    fun transform(input: T): Any
}
// 逆变只允许作为函数的输入
interface Transformer1<out T> {
    fun transform(input: Any): T
}


class KTActivity : Activity(), TypedClass<Int> {
    override suspend fun post(parameter: Int) {
    }
//
//    val intBox: Box<Int> = Box(1)
//    val numberBox: Box<Number> = intBox
//
//    val inBox: Box<Int> = Box(1)
//    val number: Box<out Number> = inBox

    fun testCollection() {
        val mutableList = mutableListOf<String>()
        val list = listOf<String>()
        mutableList.add("123")

        val bananas = arrayOf<Banana>()
//        receiveFruits(bananas)

        val apples = listOf<Apple>()
        receiveFruits(apples)


        val bananaArray = arrayOf<Banana>()
        val anyArray = arrayOf<Any>()

        copyToFruits(bananaArray, anyArray)
    }

    fun receiveFruits(fruit: Array<Fruit>) {

    }

    fun receiveFruits(fruit: List<Fruit>) {

    }


//    fun copyToFruits(from: Array<Fruit>, to: Array<Fruit>) {
//        val minSize = min(from?.size ?: 0, to?.size ?: 0)
//        for (i in 0 until minSize) {
//            to[i] = from[i]
//        }
//    }

    // out 协变 允许使用 Fruit 以及 Fruit 的派生类
    fun copyToFruits(from: Array<out Fruit>, to: Array<Fruit>) {
        val minSize = min(from?.size ?: 0, to?.size ?: 0)
        for (i in 0 until minSize) {
            to[i] = from[i]
        }
    }

    // in 逆变 允许使用 Fruit 以及 Fruit 的超类
    fun copyToFruits(from: Array<out Fruit>, to: Array<in Fruit>) {
        val minSize = min(from?.size ?: 0, to?.size ?: 0)
        for (i in 0 until minSize) {
            to[i] = from[i]
        }
    }
}