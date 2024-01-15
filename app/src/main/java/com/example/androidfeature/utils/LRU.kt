package com.example.androidfeature.utils

class LRUCache {
    var capacity: Int = 0
    var size = 0
    var head: Node? = null
    var tail: Node? = null
    var hashMap: HashMap<Int, Node>

    constructor(capacity: Int) {
        this.capacity = capacity
        hashMap = HashMap(1.shl(capacity))
        println("hashmap:${hashMap.size}")
    }

    operator fun get(key: Int): Int {
        return hashMap[key]?.let {
            moveToHead(it)
            println(toString())
            it.value
        } ?: -1
    }

    fun put(key: Int, value: Int) {
        if (hashMap[key] == null) {
            println("新节点放入")
            val node = Node(null, null, key, value)
            hashMap[key] = node
            moveToHead(node)
            size++
            if (size > capacity) {
                deleteTail()
            }
        } else {
            val node = hashMap[key]
            node!!.value = value
            moveToHead(node)
        }
        println(toString())
    }

    private fun moveToHead(node: Node) {
        if (head == null) {
            node.next = node
            node.pre = node
            head = node
            return
        }
        if (node == head) {
            return
        }
        // 1.拆节点
        node.pre?.next = node.next
        node.next?.pre = node.pre
        // 2.按节点
        node.next = head
        head?.pre = node
        // 3.更新head
        head = node
    }

    fun deleteTail() {
        head?.let {
            // 只有一个头
            if (it.pre == it) {
                head = null
                return
            }
            it.pre?.pre = it
            it.pre = it.pre?.pre
        }

    }

    override fun toString(): String {
        if (head == null) {
            return "null"
        }
        return hashMap.toString()
    }
}

data class Node(var next: Node?, var pre: Node?, var key: Int, var value: Int){
    override fun toString(): String {
        return "{${key} - ${value}}"
    }
}