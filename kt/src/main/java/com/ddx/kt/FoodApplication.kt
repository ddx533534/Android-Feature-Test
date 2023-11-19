package com.ddx.kt

import android.app.Application
import com.ddx.kt.datamodel.UserDataBase

class FoodApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化数据库
        UserDataBase.init(this)
    }
}