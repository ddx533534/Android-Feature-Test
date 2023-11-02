package com.example.androidfeature.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import java.util.HashMap;

public class CustomText extends AppCompatTextView {
    public CustomText(Context context) {
        super(context);
    }

    public CustomText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextColor(Color.RED);
    }

    public CustomText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextColor(Color.RED);
    }

    class LRUCache {

        int capacity;
        int size;
        Node head;
        Node tail;
        HashMap<Integer, Node> hashMap;

        public LRUCache(int capacity) {
            hashMap = new HashMap<>(capacity << 1);

        }

        public int get(int key) {
            if (hashMap.containsKey(key)) {
                Node n = hashMap.get(key);
                moveToHead(n);
                return n.val;
            }
            System.out.println(toString());
            return -1;
        }

        public void put(int key, int value) {
            Node n = hashMap.get(key);
            if (n == null) {
                Node N = new Node(null, null, key, value);
                hashMap.put(key, N);
                N.next = head;
                head.pre = N;
                head = N;
                size++;
                if (size > capacity) {
                    deleteTail();
                }
            } else {
                moveToHead(n);
            }
            System.out.println(toString());
        }

        public void moveToHead(Node n) {
            if (n == null) {
                return;
            }
            if (n == head) {
                return;
            } else if (n == tail) {
                tail = n.pre;
                tail.next = null;
            } else {
                //非头尾节点
                n.pre.next = n.next;
                n.next.pre = n.pre;
            }
            n.next = head;
            head.pre = n;
            n.pre = null;
            head = n;
        }

        public void deleteTail() {
            if (tail == null) {
                return;
            }
            hashMap.remove(tail.key);
            if (tail.pre == null) {
                head = null;
                tail = null;
                return;
            } else {
                Node pre = tail.pre;
                pre.next = null;
                tail = pre;
            }
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            Node n = head;
            while (n != null) {
                stringBuilder.append(n.key + ":" + n.val + "\n");
                n = n.next;
            }
            return "LRUCache{" +
                    "size=" + size +
                    "\n" + stringBuilder.toString() +
                    '}';
        }
    }

    class Node {
        Node next;
        Node pre;
        int key;
        int val;

        public Node(Node next, Node pre, int key, int val) {
            this.next = next;
            this.pre = pre;
            this.key = key;
            this.val = val;
        }
    }
}
