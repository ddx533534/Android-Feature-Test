package com.example.androidfeature.utils


internal class LRUCache(capacity: Int) {
    var capacity = 0
    var size = 0
    var head: Node? = null
    var tail: Node? = null
    var hashMap: HashMap<Int, Node>

    init {
        hashMap = HashMap(capacity shl 1)
    }

    operator fun get(key: Int): Int {
        if (hashMap.containsKey(key)) {
            val n = hashMap[key]
            moveToHead(n)
            return n!!.`val`
        }
        println(toString())
        return -1
    }

    fun put(key: Int, value: Int) {
        val n = hashMap[key]
        if (n == null) {
            val N = Node(null, null, key, value)
            hashMap[key] = N
            N.next = head
            head!!.pre = N
            head = N
            size++
            if (size > capacity) {
                deleteTail()
            }
        } else {
            moveToHead(n)
        }
        println(toString())
    }

    fun moveToHead(n: Node?) {
        if (n == null) {
            return
        }
        if (n === head) {
            return
        } else if (n === tail) {
            tail = n.pre
            tail!!.next = null
        } else {
            //非头尾节点
            n.pre!!.next = n.next
            n.next!!.pre = n.pre
        }
        n.next = head
        head!!.pre = n
        n.pre = null
        head = n
    }

    fun deleteTail() {
        if (tail == null) {
            return
        }
        hashMap.remove(tail!!.key)
        if (tail!!.pre == null) {
            head = null
            tail = null
            return
        } else {
            val pre = tail!!.pre!!
            pre.next = null
            tail = pre
        }
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        var n = head
        while (n != null) {
            stringBuilder.append(
                """
                    ${n.key}:${n.`val`}
                    
                    """.trimIndent()
            )
            n = n.next
        }
        return """
            LRUCache{size=$size
            $stringBuilder}
            """.trimIndent()
    }
}

internal class Node(var next: Node?, var pre: Node?, var key: Int, var `val`: Int)