package com.example.androidfeature.utils

import kotlin.math.abs

/**
 * 同构字符串
 * 建立映射表
 */
fun isIsomorphic(s: String, t: String): Boolean {
    if (s.length != t.length) {
        return false
    }
    // 字符映射表
    val map = mutableMapOf<Char, Char>()
    // 已经被映射集合
    val array: Array<Int> = Array(128) { 0 }
    s.forEachIndexed { index, c ->
        if (!map.containsKey(c)) {
            if (array[t[index].code] == 1) {
                return false
            }
            map[c] = t[index]
            array[t[index].code] = 1
        } else if (map[c] != t[index]) {
            return false
        }
    }
    return true
}

/**
 * 由于是ASCII码，范围从0-127，因此可以直接使用一个128长度的数组
 * 两个数组，一个代表映射关系，另一个表示对应资源是否被映射
 */
fun isIsomorphic1(s: String, t: String): Boolean {
    if (s.length != t.length) {
        return false
    }
    // 字符映射表
    val map: Array<Int> = Array(128) { -1 }
    // 已经被映射集合
    val array: Array<Int> = Array(128) { 0 }
    s.forEachIndexed { index, c ->
        if (map[c.code] == -1) {
            if (array[t[index].code] == 1) {
                return false
            }
            map[c.code] = t[index].code
            array[t[index].code] = 1
        } else if (map[c.code] != t[index].code) {
            return false
        }
    }
    return true
}

/**
 * 直接使用一个数组，正负表示是否已经被映射过，但是时间上表现不太好，可能是涉及到负数和绝对值原因
 */
fun isIsomorphic2(s: String, t: String): Boolean {
    if (s.length != t.length) {
        return false
    }
    // 字符映射表
    val map: Array<Int> = Array(128) { 128 }
    s.forEachIndexed { index, c ->
        if (abs(map[c.code]) == 128) {
            if (map[t[index].code] < 0) {
                return false
            }
            map[c.code] = t[index].code
            map[t[index].code] *= -1
        } else if (abs(map[c.code]) != t[index].code) {
            return false
        }
    }
    return true
}


/**
 * 单词映射，完全映射关系
 */
fun wordPattern(pattern: String, s: String): Boolean {
    if (pattern.isEmpty()) {
        return false
    }
    if (s.isEmpty()) {
        return false
    }
    val list = s.split(" ")
    if (list.size != pattern.length) {
        return false
    }
    var map = mutableMapOf<Char, String>()
    var rMap = mutableMapOf<String, Char>()
    pattern.forEachIndexed { i, char ->
        if (!map.containsKey(char)) {
            if (rMap.containsKey(list[i])) {
                return false
            }
            map[char] = list[i]
            rMap[list[i]] = char
        } else if (map[char] != list[i]) {
            return false
        }
    }
    return true
}


/**
 * 存在重复元素 II
 */
fun containsNearbyDuplicate(nums: IntArray, k: Int): Boolean {
    if (nums.isEmpty() || k >= nums.size) {
        return false
    }
    val map = mutableMapOf<Int, Int>()
    for (i in nums.indices) {
        var n = nums[i]
        if (map.containsKey(n) && (i - map[n]!!) <= k) {
            return true
        }
        map[n] = i
    }
    return false
}

/**
 * 最长连续序列
 * 输入：nums = [100,4,200,1,3,2]
 * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
 */
fun test() {
    val nums = arrayOf(100, 4, 200, 1, 3, 2)
    longestConsecutive(nums.toIntArray())
}

fun longestConsecutive(nums: IntArray): Int {
    if (nums.size <= 1) {
        return nums.size
    }
    // key - 数字
    var map = mutableMapOf<Int, Int>()
    nums.forEach {
        if (!map.contains(it)) {
            map[it] = 0
        }
    }
    var max = 0
    // key - 遍历的map
    map.keys.forEach {
        if (map[it] == 0) {
            var start = it
            var end = it
            while (map.contains(start - 1) || map.contains(end + 1)) {
                if (map.contains(start - 1)) {
                    start--
                    map[start] = 1
                }
                if (map.contains(end + 1)) {
                    end++
                    map[end] = 1
                }
            }
            max = maxOf(max, end - start + 1)
            println(max)
            println(map)
            map[it] = 1
        }
    }
    return max
}


fun longestConsecutive1(nums: IntArray): Int {
    if (nums.size <= 1) {
        return nums.size
    }
    // key - 数字
    var map = mutableMapOf<Int, Int>()
    nums.forEach {
        if (!map.contains(it)) {
            map[it] = 0
        }
    }
    var max = 0
    // key - 遍历的map
    map.keys.forEach {
        if (map[it] == 0) {
            var start = it
            var end = it
            while (map.contains(start - 1) || map.contains(end + 1)) {
                if (map.contains(start - 1)) {
                    start--
                    map[start] = 1
                }
                if (map.contains(end + 1)) {
                    end++
                    map[end] = 1
                }
            }
            max = maxOf(max, end - start + 1)
            println(max)
            println(map)
            map[it] = 1
        }
    }
    return max
}

// 白名单
var whiteSet = mutableSetOf<Int>()

// 黑名单
var blackSet = mutableSetOf<Int>()

/**
 * 1.第一种方案，set集合判断sum是否出现，出现则表示平方和会陷入死循环。
 * 其中优化方案是加上黑白名单，对一些出现死循环的元素加入到黑名单，另一些符合条件的加入到白名单
 * 但由于添加操作是addAll操作，相比第二种方案的add单个元素性能要差点
 */
fun isHappy(n: Int): Boolean {
    val set = mutableSetOf<Int>()
    var temp = n
    do {
        set.add(temp)
        val sum = temp.toString().map {
            it.toString().toInt()
        }.sumOf {
            it * it
        }
        // 白名单校验
        if (whiteSet.contains(sum)) {
            return true
        }
        if (sum == 1) {
            // 整个路径上的元素都是白名单，添加操作比较耗时
            whiteSet.addAll(set)
            return true
        }
        temp = sum
    } while (!set.contains(temp) && !blackSet.contains(temp))
    // 整个路径上的元素都是黑名单，添加操作比较耗时
    blackSet.addAll(set)
    return false
}

fun isHappy1(n: Int): Boolean = isEqualToOne(n, mutableSetOf())

/**
 * 2.第二种方案则是递归，其实递归和方案一的迭代差不多，但在这个过程中是以最终结果导向而加入路径上所有的元素
 * 不同点就是每个递归出栈时都会 add 单个元素到黑名单或者白名单，比方案一addAll 的一次性添加所有元素性能要好
 */
fun isEqualToOne(n: Int, set: MutableSet<Int>): Boolean {
    if (set.contains(n)) {
        return false
    }
    if (whiteSet.contains(n)) {
        return true
    }
    if (blackSet.contains(n)) {
        return false
    }
    val sum = n.toString().map {
        it.toString().toInt()
    }.sumOf {
        it * it
    }
    set.add(n)
    if (sum == 1) {
        whiteSet.add(n)
        return true
    }
    val res = isEqualToOne(sum, set)
    if (res) {
        // 添加元素比较快
        whiteSet.add(n)
    } else {
        // 添加元素比较快
        blackSet.add(n)
    }
    return res
}


fun twoSum(nums: IntArray, target: Int): IntArray {
    if (nums.size <= 1) {
        return intArrayOf()
    }
    // key - 数组值， value - 索引
    val map = mutableMapOf<Int, Int>()
    for (i in nums.indices) {
        var another = target - nums[i]
        if (map.containsKey(another)) {
            return intArrayOf(i, map[another]!!)
        }
        map[nums[i]] = i
    }
    return intArrayOf()
}


fun summaryRanges(nums: IntArray): List<String> {
    val list = mutableListOf<String>()
    if (nums.isEmpty()) {
        return list
    }
    var start = nums[0]
    var cur: Int
    for (i in nums.indices) {
        cur = nums[i]
        if (i + 1 < nums.size) {
            if (nums[i + 1].toLong() - cur.toLong() > 1) {
                list.add(if (start == cur) "$start" else "$start->$cur")
                start = nums[i + 1]
            }
        } else {
            list.add(if (start == cur) "$start" else "$start->$cur")
        }
    }
    println(list)
    return list
}