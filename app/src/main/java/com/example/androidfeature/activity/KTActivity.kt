package com.example.androidfeature.activity


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.androidfeature.R
import com.example.androidfeature.bean.Person
import com.example.androidfeature.listener.TypedClass

const val TAG: String = "KTActivity";

class KTActivity : AppCompatActivity(),TypedClass<Int> {

    private lateinit var name:String
    private val name1:String by lazy {
        var a  = name
         a
    }

    val ddx by lazy {
        Person("ddx","1122334455")
    }

    val list:List<Int> by lazy {
        listOf(1,2,3,4)
    }
    val mutableList:MutableList<Int> by lazy {
        mutableListOf(1,2,3,4)
    }
    var nullableString: String? = null

    var nums: IntRange = 1..20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ktactivity)
        list.filter {
            it > 2
        }.map {
            it * 2
        }
        mutableList.add(1)

        // nullableString.length 报错，可为空的变量不能直接调用
        var len = nullableString?.length ?: -1;
    }

    override fun post(parameter: Int) {
        TODO("Not yet implemented")
    }
}