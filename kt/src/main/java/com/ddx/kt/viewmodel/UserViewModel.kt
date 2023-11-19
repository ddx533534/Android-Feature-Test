package com.ddx.kt.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.ddx.kt.datamodel.UserDataBase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class LoginState(val i: Int) {
    ONLINE(0), //在线
    OFFLINE(1), // 离线
    UNSIGNED(2), // 未登录
    UNKNOWN(3) //未知
}

data class UserInfo(
    val name: String = "",
    val password: String = "",
    val icon: String = "",
    val intro: String = "",
    val loginState: LoginState = LoginState.UNSIGNED
)


class UserViewModel : ViewModel() {
    private val _userState = MutableStateFlow<UserInfo>(UserInfo())
    val userState = _userState.asStateFlow()


    init {
        viewModelScope.launch {
            val user = UserDataBase.getInstance()?.userDao()?.getUser()
            _userState.value.copy(
                name = user?.name ?: "",
                password = user?.password ?: "",
                icon = user?.icon ?: "",
                intro = user?.intro ?: "",
                loginState = user?.loginState ?: LoginState.UNSIGNED,
            )
        }
    }
}