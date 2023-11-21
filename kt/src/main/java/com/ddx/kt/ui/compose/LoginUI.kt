package com.ddx.kt.ui.compose

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ddx.kt.viewmodel.LoginState
import com.ddx.kt.viewmodel.UserInfo
import com.ddx.kt.viewmodel.UserViewModel
import com.example.androidfeature.R
import kotlinx.coroutines.launch

const val LOGIN = "Login"
const val SIGNIN = "Sign In"

@Composable
fun Login(userViewModel: UserViewModel) {
    YellowThemeLoginScreen(userViewModel)
}

@Composable
fun YellowThemeLoginScreen(userViewModel: UserViewModel) {
    val context = LocalContext.current
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var password1 by remember {
        mutableStateOf("")
    }
    var loginState by remember {
        // 登录状态初始为离线
        mutableStateOf(LoginState.OFFLINE)
    }
    val coroutineScope = rememberCoroutineScope()
    MaterialTheme(
        colors = yellowThemeColors
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Logo or app name
                Image(
                    painter = painterResource(id = R.drawable.food),
                    contentDescription = null,
                    modifier = Modifier.size(96.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Username TextField
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person, contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                // Password TextField
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock, contentDescription = null
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { /* Handle login */ }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                if (loginState == LoginState.UNSIGNED) {
                    // Password TextField
                    OutlinedTextField(
                        value = password1,
                        onValueChange = { password1 = it },
                        label = { Text("Password") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock, contentDescription = null
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = { /* Handle login */ }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
                // Login Button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            var userInfo: UserInfo? = null
                            userInfo = if (loginState == LoginState.UNSIGNED) {
                                userViewModel.login(username, password)
                            } else {
                                userViewModel.signIn(username, password, password1)
                            }
//                            if (context is LoginActivity) {
//                                val intent = Intent()
//                                intent.putExtra("user", userInfo)
//                                context.setResult(Activity.RESULT_OK, intent)
//                            }
                            if(userInfo != null && userInfo.loginState == LoginState.ONLINE){
                                Toast.makeText(
                                    context,
                                    "Login Success",
                                    Toast.LENGTH_LONG
                                ).show()

                            } else {
                                Toast.makeText(
                                    context,
                                    "Login Failed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (loginState == LoginState.UNSIGNED) SIGNIN else LOGIN)
                    }
                }
                Text(text = if (loginState == LoginState.UNSIGNED) "已有账号！立即登录！" else "没有账号？立即注册!",
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 10.dp)
                        .clickable {
                            loginState = when (loginState) {
                                LoginState.UNSIGNED -> {
                                    // 注册态
                                    LoginState.OFFLINE
                                }

                                else -> {
                                    // 登录态
                                    LoginState.UNSIGNED
                                }
                            }

                        })
            }
        }
    }
}



private val yellowThemeColors = lightColors(
    primary = Color(0xEEFF8C00), onPrimary = Color.Black
)