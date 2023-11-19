package com.example.androidfeature.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidfeature.utils.ZxingUtils
import com.google.zxing.BarcodeFormat

const val width = 400
const val height = 400
val defaultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
    this.eraseColor(Color.Gray.toArgb())
}.asImageBitmap()

/**
 * 二维码生成页，输入文案生成对应的二维码。
 */
@SuppressLint("RestrictedApi")
class BarcodeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                TopAppBar(title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "二维码生成页",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }, backgroundColor = Color.Blue, modifier = Modifier.padding(bottom = 15.dp))
                setMainContent()
            }
        }
    }


    @Composable
    fun setMainContent() {
        var bitmap by remember {
            mutableStateOf<ImageBitmap>(defaultBitmap)
        }


        var codeStr by remember {
            mutableStateOf<String>("")
        }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                bitmap = bitmap, "barcode",
                modifier = Modifier
                    .width(width.dp)
                    .height(height.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(all = 10.dp)
            )
            TextField(
                value = codeStr,
                onValueChange = { codeStr = it },
                label = { Text("请输入内容") },
                modifier = Modifier.padding(top = 30.dp)
            )
            Button(
                onClick = {
                    if (codeStr.trim().isNotEmpty()) {
                        bitmap =
                            ZxingUtils.createBarCode(codeStr, BarcodeFormat.UPC_A, width, height)
                                ?.asImageBitmap() ?: defaultBitmap
                    }
                }, modifier = Modifier.padding(top = 30.dp)
            ) {
                Text(
                    "生成二维码", modifier = Modifier
                        .wrapContentWidth()
                        .height(30.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }


}