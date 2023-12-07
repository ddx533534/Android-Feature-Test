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
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import kotlin.math.abs
import kotlin.math.exp

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

    fun onUserInfoChanged(userInfo: UserInfo) {
        if (userState != userInfo) {
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

    fun test() {
        val names = listOf<Int>()
        names.filter { it % 2 == 0 }.map { it * 2 }.sorted()
        onLoad({ i -> exp(i.toDouble()).toInt() }, 1)
        // 调用时可以放在括号外边，，高灵活性带来低的可维护性，到这儿已经几乎看不懂了
        onLoad(1) { return@onLoad exp(it.toDouble()).toInt() }

//        val primes = generateSequence(5, null).take(6)
    }

    // 1.高阶函数接受一个函数作为参数
    fun onLoad(action: (Int) -> Int, n: Int) {
        var res: Int = 0
        for (i in 0..n) {
            res = action(i)
        }
    }

    // 2.看起来有点乱，一般将函数参数放在末尾
    fun onLoad(n: Int, action: (Int) -> Int) {
        var res: Int = 0
        for (i in 0..n) {
            res = action(i)
        }
    }


}

inline fun invoke(n: Int, action: (Int) -> Unit) {
    action(n)
}

val check5 = { str: String -> str.length == 5 }

// 这玩意写出来，谁能看懂，，，
fun checkAny(length: Int): (String) -> Boolean {
    return { str: String -> str.length == length }
}

fun test() {

    (0..3).forEach {
        invoke(it) {
            println("enter invoke lambda")
            println(it)
            println(it)
            return
        }
    }

    println("exit test")

    val complex = Complex(1, -1) * Complex(1, -1)
    println(complex)
    complex.pureImaginary()
}


data class Complex(val real: Int, val imaginary: Int) {
    operator fun plus(complex: Complex): Complex {
        println("I am class instance!")
        return Complex(real + complex.real, imaginary + complex.imaginary)
    }

    operator fun times(complex: Complex): Complex {
        val r = real * complex.real - (imaginary * imaginary)
        val i = real * complex.imaginary + imaginary * complex.real
        return Complex(r, i)
    }

    override fun toString(): String {
        return "$real${if (imaginary > 0) "+" else "-"}${abs(imaginary)}i"
    }

    fun pureImaginary(): Boolean {
        println("I am class instance!")
        return real == 0
    }

    companion object {
        fun pureImaginary(): Boolean {
            println("I am companion object!")
            return false
        }
    }
}

// 扩展函数
fun Complex.pureImaginary(): Boolean {
    println("I am class extention!")
    return real == 0
}

// 扩展函数注入操作符
operator fun Complex.plus(complex: Complex): Complex {
    println("I am class extention!")
    return Complex(real + complex.real, imaginary + complex.imaginary)
}

// 扩展属性
var Complex.isReal: Boolean
    get() = imaginary == 0
    set(value) {
        isReal = value
    }

// 注入第三方类
fun String.revert(): String {
    val res = StringBuilder()
    for (i in length - 1 downTo 0) {
        res.append(get(i))
    }
    return res.toString()
}

fun testString() {
    for (i in "ddx".."ddxx") {
        println(i)
    }
    String.toURL("https：//www.baidu.com/")
}

// 修复 String 的迭代问题
operator fun ClosedRange<String>.iterator() = object : Iterator<String> {

    private val next = StringBuilder(start)
    private val last = endInclusive

    override fun hasNext(): Boolean {
        return last >= next.toString() && last.length >= next.length
    }

    override fun next(): String {
        val result = next.toString()
        val lastCharacter = last.last()
        if (lastCharacter < Char.MAX_VALUE) {
            next.setCharAt(next.length - 1, lastCharacter + 1)
        } else {
            next.append(Char.MAX_VALUE)
        }
        return result
    }

}

// 伴生对象注入方法
fun String.Companion.toURL(str: String) = java.net.URL(str)
