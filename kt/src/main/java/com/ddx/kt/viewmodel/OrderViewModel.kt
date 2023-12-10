package com.ddx.kt.viewmodel

import java.util.SortedSet


data class FoodInfo(
    val name: String,
    val price: String,
    val count: Int
)

data class OrderInfo(
    val foods: MutableList<FoodInfo>
)

class OrderViewModel {
}


var map = mutableMapOf<Int, SortedSet<Int>>()
fun isSubsequence(s: String, t: String): Boolean {

    if (s.isEmpty()) {
        return true
    }
    if (t.isEmpty()) {
        return false
    }
    var temp = 0
    t.forEach {
        if (temp >= s.length) {
            return@forEach
        }
        if (s[temp] == it) {
            temp++
        }

    }
    return temp == s.length
}

fun binarySearch(s: String, t: String) {
    var start = 0
    var end = t.length - 1
    binary(start, end, s, t)
}

var find = false

// 递归容易overflow，但总体的思想是就是二分（二分终止的条件就是找到子序列或者搜索范围小于s的长度）
// 然后合并的时候进行搜索
fun binary(start: Int, end: Int, s: String, t: String) {
    if (find) {
        return
    }
    if (end - start + 1 < s.length) {
        return
    }
    if (end - start + 1 == s.length) {
        searchFromBig(start, end, s, t)
        return
    }
    var middle = start + (end - start) / 2
    binary(start, middle, s, t)
    binary(middle + 1, end, s, t)
    searchFromBig(start, end, s, t)
}

fun searchFromBig(start: Int, end: Int, s: String, t: String) {
    println("子字符串：${t.substring(start..end)}")
    var temp = 0
    for (i in start..end) {
        if (temp >= s.length) {
            break
        }
        if (s[temp] == t[i]) {
            temp++
        }
    }
    if (temp == s.length) {
        find = true
    }
}
