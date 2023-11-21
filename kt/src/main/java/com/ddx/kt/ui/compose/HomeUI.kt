package com.ddx.kt.ui.compose

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ddx.kt.ui.activity.LoginActivity
import com.ddx.kt.viewmodel.LoginState
import com.ddx.kt.viewmodel.UserViewModel
import com.example.androidfeature.R

@Composable
fun welcome(userViewModel: UserViewModel) {
    val userState by userViewModel.userState.collectAsState()
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    userState.copy(
                        name = result.data?.getStringExtra("name") ?: "",
                        icon = result.data?.getStringExtra("name") ?: "",
                        intro = result.data?.getStringExtra("name") ?: "",
                    )
                }

                Activity.RESULT_CANCELED -> {
                    Toast.makeText(context, "cancel", Toast.LENGTH_LONG).show()
                }

                else -> {
                    Toast.makeText(context, "failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    Row {
        Text(
            text = "Hi,${
                when (userState.loginState) {
                    LoginState.OFFLINE -> "Please Sign In"
                    LoginState.ONLINE -> userState.name
                    else -> "Please Register!"
                }
            }",
            fontSize = 20.sp,
            color = Color.Red,
            modifier = Modifier.weight(2.0f)
        )
        // 头像
        Image(
            painter = painterResource(R.drawable.profile_unlog),
            contentDescription = "hello",
            modifier = Modifier
                .size(60.dp)
                .clickable {
                    launcher.launch(Intent(context, LoginActivity::class.java))
                }
                .clip(CircleShape)
                .border(2.dp, Color.Black, CircleShape)
        )
    }
}

@Composable
fun Home(navHostController: NavHostController, userViewModel: UserViewModel) {
    Column {
        welcome(userViewModel = userViewModel)
    }
}