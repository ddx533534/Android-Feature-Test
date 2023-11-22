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

const val SUCCESS = 1
const val PASSWORD_WRONG = 2
const val PASSWORD_NOT_CONSISTENCY = 3
const val DUPLICATE_USERNAME = 4
const val INFO_EMPTY = 5
const val USERNAME_NOT_EXIST = 6
const val FAIL = 7

data class LoginResult(
    val userInfo: UserInfo?,
    val loginCode: Int = FAIL
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

    fun login(username: String?, password: String?): LoginResult {
        if (username == null || password == null) {
            return LoginResult(null, INFO_EMPTY)
        }

        val userDao = UserDataBase.getInstance()?.userDao()

        val user = userDao?.getUser(username) ?: return LoginResult(null, USERNAME_NOT_EXIST)
        val isPasswordCorrect = userDao.checkUser(username, password) != null

        return if (isPasswordCorrect) {
            LoginResult(
                UserInfo(
                    name = user.name,
                    icon = user.icon,
                    intro = user.intro,
                    loginState = LoginState.ONLINE
                ), SUCCESS
            )
        } else {
            LoginResult(null, PASSWORD_WRONG)
        }
    }


    fun signIn(username: String?, password: String?, password1: String?): LoginResult? {
        if (username == null || password == null || password1 == null) {
            return LoginResult(null, INFO_EMPTY)
        }
        if (password != password1) {
            return LoginResult(null, PASSWORD_NOT_CONSISTENCY)
        }
        val userDao = UserDataBase.getInstance()?.userDao()

        userDao?.getUser(username)?.let {
            return LoginResult(null, DUPLICATE_USERNAME)
        }

        val user =
            User(username, password, "", "", LoginState.ONLINE, "${System.currentTimeMillis()}")
        userDao?.insert(user)
        return LoginResult(
            UserInfo(
                name = user.name,
                icon = user.icon,
                intro = user.intro,
                loginState = user.loginState ?: LoginState.OFFLINE
            ), SUCCESS
        )
    }

}