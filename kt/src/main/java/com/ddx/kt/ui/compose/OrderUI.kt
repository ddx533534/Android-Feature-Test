package com.ddx.kt.ui.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


@Composable
fun Order(radius: Int, dotRadius: Int) {
    val size = radius * 2
    val dotSize = dotRadius * 2
    Box(
        modifier = Modifier
            .size(size.dp)
            .background(Color.Gray)
            .border(2.dp, Color.Black, CircleShape),
        contentAlignment = Alignment.Center
    ) {

        val angleInterval = 360f / 8  // 假设我们在环上放置8个红色的圆
        for (i in 0 until 8) {
            val angle = angleInterval * i
            val offset = Offset(
                x = ((radius - dotRadius / 2) * cos(Math.toRadians(angle.toDouble()))).toFloat(),
                y = ((radius - dotRadius / 2) * sin(Math.toRadians(angle.toDouble()))).toFloat()
            )
            Log.d("hashring", offset.toString())
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .offset(
                        x = offset.x.dp,
                        y = offset.y.dp
                    )
                    .background(Color.Red, CircleShape)
            ){
                Text(text = "$i")
            }
        }
    }
}