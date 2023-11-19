package com.example.androidfeature.arch.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.androidfeature.R

class PhotoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        // 创建一个 Fragment 实例
        val fragment: Fragment = PhotoFragment.newInstance(4)

        // 开始一个 Fragment 事务
        supportFragmentManager.beginTransaction()
            // 将 Fragment 添加到容器中
            .add(R.id.fragmentContainer, fragment)
            // 提交事务
            .commit()
    }
}