package com.ddx.kt.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.ddx.kt.datamodel.User
import com.ddx.kt.datamodel.UserDataBase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.Serializable

enum class LoginState(val i: Int) {
    ONLINE(0), //在线
    OFFLINE(1), // 离线
    UNSIGNED(2), // 未登录
    UNKNOWN(3) //未知
}

data class UserInfo(
    val name: String = "",
    val icon: String? = "",
    val intro: String? = "",
    val loginState: LoginState = LoginState.UNSIGNED
) : Serializable


class UserViewModel : ViewModel() {
    private val _userState = MutableStateFlow<UserInfo>(UserInfo())
    val userState = _userState.asStateFlow()


    init {
        viewModelScope.launch {
            suspend {
                val user = UserDataBase.getInstance()?.userDao()?.getUser()
                _userState.value.copy(
                    name = user?.name ?: "",
                    icon = user?.icon ?: "",
                    intro = user?.intro ?: "",
                    loginState = user?.loginState ?: LoginState.UNSIGNED,
                )
            }
        }
    }

    suspend fun login(username: String?, password: String?): UserInfo? {

        if (username == null || password == null) {
            return null
        }
        val user =
            UserDataBase.getInstance()?.userDao()?.checkUser(username, password) ?: return null
        return UserInfo(
            name = user.name,
            icon = user.icon,
            intro = user.intro,
            loginState = LoginState.ONLINE
        )
    }

    suspend fun signIn(username: String?, password: String?, password1: String?): UserInfo? {
        if (username == null || password == null || password != password1) {
            return null
        }
        val user =
            User(username, password, "", "", LoginState.ONLINE, "${System.currentTimeMillis()}")
        UserDataBase.getInstance()?.userDao()?.insert(user)
        return UserInfo(
            name = user.name,
            icon = user.icon,
            intro = user.intro,
            loginState = user.loginState ?: LoginState.OFFLINE
        )
    }

}