package com.ddx.kt.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ddx.kt.ui.widget.ImageTarget
import com.ddx.kt.viewmodel.UserViewModel
import com.example.androidfeature.R

class InfoActivity : ComponentActivity() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        setContent {
            mainContent()
        }
    }

    @Composable
    fun welcome() {
        val userState by userViewModel.userState.collectAsState()
        Row {
            Text(
                text = "Hi ${userState.name}",
                fontSize = 20.dp,
                fontFamily = FontFamily.SansSerif,
                color = Color.Red,
                Modifier.weight(2.0f)
            )
            Image(
            )
        }
    }

    @Composable
    fun CircularAvatar() {
        // 替换成你的头像图片 URL
        val imageUrl = "https://example.com/your_avatar_url.jpg"

        // 使用 rememberGlidePainter 函数加载头像图片
        val painter = rememberGlidePainter(
            request = imageUrl,
            fadeIn = true
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // 使用 Image 组件显示头像图片，并添加圆角效果
            Image(
                painter = painter,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )
        }
    }


    @Preview
    @Composable
    fun mainContent() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp)
        ) {
            welcome()
        }

    }
}