package com.ddx.kt.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ddx.kt.ui.activity.InfoActivity
import com.ddx.kt.viewmodel.UserViewModel
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.coroutines.launch


const val REQUEST_CODE_CAPTURE = 111
const val REQUEST_CODE_ALBUM = 112

@Composable
fun Profile(userViewModel: UserViewModel) {
    Column {
        TopAppBar(
            title = { Text(text = "个人主页") },
            navigationIcon = {
                IconButton(onClick = { /*TODO: Handle navigation*/ }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                }
            }
        )
        UserProfilePage(userViewModel = userViewModel)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserProfilePage(userViewModel: UserViewModel) {
    val userState = userViewModel.userState
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val painter = rememberCoilPainter(
            request = userState.icon,
            fadeIn = true,
        )

        ModalBottomSheetLayout(
            sheetState = bottomSheetState,
            sheetContent = {
                Column {
                    TextButton(onClick = {
                        Matisse.from(context as InfoActivity)
                            .choose(MimeType.ofImage())
                            .capture(true)
                            .captureStrategy(CaptureStrategy(false,"com.example.AndroidFeature.FileProvider"))
                            .maxSelectable(1)
                            .forResult(REQUEST_CODE_CAPTURE)
                    }) {
                        Text("拍照")
                    }
                    TextButton(onClick = {
                        Matisse.from(context as InfoActivity)
                            .choose(MimeType.ofImage())
                            .capture(false)
                            .captureStrategy(CaptureStrategy(false,"com.example.AndroidFeature.FileProvider"))
                            .maxSelectable(1)
                            .forResult(REQUEST_CODE_ALBUM)
                    }) {
                        Text("相册")
                    }
                }
            }
        ) {
            Image(
                painter = if (painter.loadState is ImageLoadState.Success) painter else painterResource(
                    id = com.example.androidfeature.R.drawable.profile_upload
                ),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .clickable {
                        coroutineScope.launch { bottomSheetState.show() }
                    },
                contentScale = ContentScale.Crop
            )
        }

        Text(text = userState.name, style = MaterialTheme.typography.h4)
        Text(text = userState.intro ?: "empty~", style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(16.dp))
    }


}