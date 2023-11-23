package com.ddx.kt.viewmodel

import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddx.kt.datamodel.User
import com.ddx.kt.datamodel.UserDataBase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.io.Serializable

enum class LoginState(val i: Int) {
    ONLINE(0), //在线
    OFFLINE(1), // 离线
    UNSIGNED(2), // 未登录
    UNKNOWN(3) //未知
}

@Parcelize
data class UserInfo(
    val name: String = "",
    val icon: String? = "",
    val intro: String? = "",
    val loginState: LoginState = LoginState.UNSIGNED
) : Parcelable

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
    var userState by mutableStateOf(value = UserInfo())


    init {
        viewModelScope.launch {
            Log.d("userinfo", "init")
            suspend {
                UserDataBase.getInstance()?.userDao()?.getUser("ddx")?.let {
                    Log.d("userinfo", it.toString())
                    userState = userState.copy(
                        name = it.name,
                        icon = it.icon,
                        intro = it.intro,
                        loginState = it.loginState ?: LoginState.UNSIGNED,
                    )
                }
            }
        }
    }

    fun onUserInfoChanged(userInfo: UserInfo){
        if(userState != userInfo){
            userState = userState.copy(
                name = userInfo.name,
                icon = userInfo.icon,
                intro = userInfo.intro,
                loginState = userInfo.loginState
            )
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