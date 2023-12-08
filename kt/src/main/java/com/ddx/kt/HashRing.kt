package com.ddx.kt

import android.util.Log


/**
 * 节点
 */

const val TAG = "HashRing"
const val DEFAULT_MASK = 5

const val INSERT_SUCCEED = 0
const val INSERT_EXCEED_LIMIT = -1
const val INSERT_DUPLICATE = -2
const val INSERT_ERROR = -3

data class Node(
    val hashValue: Int,
    val resources: MutableMap<Int, String>,
    val fingerTable: MutableMap<Int, Node>
) {
    lateinit var pre: Node
    lateinit var next: Node
}

class HashRing {

    private var head: Node? = null
    private var MASK: Int = DEFAULT_MASK
    private var min = 0
    private var max = 1.shl(MASK) - 1

    constructor() {
    }

    constructor(ringSize: Int) {
        this.MASK = ringSize
        this.max = 1.shl(MASK) - 1
    }


    private fun isHashLegal(hashValue: Int): Boolean = hashValue in min..max


    fun printHashRing(){
        head?.let {
            var temp = it
            do {
                Log.d(TAG,"节点：${it.hashValue}, 资源:${it.resources}")
                temp = temp.next
            } while (temp != it)
        }
    }


    /**
     * distance 按照前后 两个点 逆时针的方向计算距离，即 hash2 向前看 hash1
     */
    private fun distance(hash1: Int, hash2: Int): Int {
        return if (hash2 >= hash1) {
            hash2 - hash1
        } else {
            hash2 - hash1 + 1.shl(MASK)
        }
    }


    /**
     * 寻找资源对应的节点
     * hashValue 为资源的哈希值，离资源最近的节点的下一个节点为当前资源所属节点
     */
    private fun lookupNode(hashValue: Int): Node? {
        if (!isHashLegal(hashValue)) {
            return null
        }
        return head?.let {
            var temp: Node = it
            while (distance(temp.hashValue, hashValue) > distance(temp.next.hashValue, hashValue)) {
                temp = temp.next
            }
            if (temp.hashValue == hashValue) {
                temp
            } else {
                temp.next
            }
        }
    }

    /**
     * 插入节点
     * 插入新的节点，同时需要对资源所属节点进行移动
     */
    fun insertNode(hashValue: Int): Int {
        if (!isHashLegal(hashValue)) {
            return INSERT_EXCEED_LIMIT
        }
        var node = Node(hashValue, mutableMapOf(), mutableMapOf()).also {
            it.next = it
            it.pre = it
        }
        return if (head == null) {
            head = node
            INSERT_SUCCEED
        } else {
            // successor
            var target = lookupNode(node.hashValue)
            target?.let {
                if (target.hashValue != node.hashValue) {
                    target.pre.next = node
                    node.pre = target.pre
                    node.next = target
                    target.pre = node
                    moveResources(node, node.next, false)
                    INSERT_SUCCEED
                } else {
                    INSERT_DUPLICATE
                }
            } ?: INSERT_ERROR
        }
    }


    /**
     * 插入资源
     * 将资源插入到对应节点中
     */

    fun addResource(hashValue: Int): Int {
        if (!isHashLegal(hashValue)) {
            return INSERT_EXCEED_LIMIT
        }
        // successor
        val temp = lookupNode(hashValue)
        return temp?.let {
            var content = "Resource of file : $hashValue"
            if (!it.resources.containsKey(hashValue)) {
                it.resources[hashValue] = content
                INSERT_SUCCEED
            } else {
                INSERT_DUPLICATE
            }

        } ?: INSERT_ERROR
    }


    /**
     * 移动资源
     * 将资源从一个节点移动到另一个节点
     */
    private fun moveResources(ori: Node, des: Node, delete: Boolean) {
        val deleteList = mutableListOf<Int>()
        for ((k, v) in ori.resources) {
            if (distance(des.hashValue, k) < distance(ori.hashValue, k)) {
                des.resources[k] = v
                deleteList.add(k)
            }
        }
        for (i in deleteList) {
            ori.resources.remove(i)
        }
    }

    /**
     * 构建指纹表
     */
    fun buildFingerTable() {
        head?.let {
            var temp = it
            do {
                for (i in 0 until MASK) {
                    var finger = (temp.hashValue + 1.shl(i)) % 1.shl(MASK)
                    var node = lookupNode(finger)
                    node?.let {
                        temp.fingerTable[finger] = node
                    }
                }
                temp = temp.next
            } while (temp != it)
        }
    }

    fun getFingerTable(hashValue: Int): Map<Int, Node> {
        return head?.let {
            var temp = it
            do {
                if (temp.hashValue == hashValue) {
                    return temp.fingerTable
                }
                temp = temp.next
            } while (temp != it)
            mutableMapOf()
        } ?: mutableMapOf()
    }


    fun getNodes(): List<Int> {
        return head?.let {
            var temp = it
            val list = mutableListOf<Int>()
            do {
                list.add(temp.hashValue)
                temp = temp.next
            } while (temp != it)
            list.toList()
        } ?: mutableListOf<Int>()
    }

    fun getResources(): List<Int> {
        return head?.let {
            var temp = it
            val list = mutableListOf<Int>()
            do {
                temp.resources.takeIf { map -> map.isNotEmpty() }.let {
                    list.addAll(temp.resources.keys)
                }
                temp = temp.next
            } while (temp != it)
            list.toList()
        } ?: mutableListOf<Int>()
    }

    fun getRingCapacity(): Int {
        return max - min + 1
    }

    fun isHead(hashValue: Int): Boolean = head?.hashValue == hashValue

    fun clear() {
        head = null
    }
}