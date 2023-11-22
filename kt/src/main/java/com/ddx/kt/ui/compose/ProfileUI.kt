package com.ddx.kt.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ddx.kt.viewmodel.UserViewModel
import com.example.androidfeature.R
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState

@Composable
fun Profile(userViewModel: UserViewModel) {
    UserProfilePage(userViewModel = userViewModel)
}

@Composable
fun UserProfilePage(userViewModel: UserViewModel) {
    val userState by userViewModel.userState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TopAppBar(
            title = { Text(text = "个人主页") },
            navigationIcon = {
                IconButton(onClick = { /*TODO: Handle navigation*/ }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
        val painter = rememberCoilPainter(
            request = userState.icon,
            fadeIn = true,
        )
        Image(
            painter = if (painter.loadState is ImageLoadState.Success) painter else painterResource(
                id = R.drawable.profile_upload
            ),
            contentDescription = "Profile picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(text = userState.name, style = MaterialTheme.typography.h4)
        Text(text = userState.intro ?: "empty~", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(16.dp))
    }
}