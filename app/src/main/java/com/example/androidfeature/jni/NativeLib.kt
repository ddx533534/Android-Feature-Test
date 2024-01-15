package com.example.androidfeature.jni

class NativeLib {

    companion object {
        init {
            System.load("native-lib")
        }
    }

    external fun helloJNI(): String

}
