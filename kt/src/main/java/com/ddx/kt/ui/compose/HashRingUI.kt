package com.ddx.kt.ui.compose

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ddx.kt.DEFAULT_MASK
import com.ddx.kt.INSERT_DUPLICATE
import com.ddx.kt.INSERT_EXCEED_LIMIT
import com.ddx.kt.INSERT_SUCCEED
import com.ddx.kt.viewmodel.HashRingViewModel

/**
 * 哈希环
 */
@Composable
fun HashRingUI(hashRingViewModel: HashRingViewModel, radius: Int, dotRadius: Int) {
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        var ringSize by remember {
            mutableStateOf("")
        }
        var nodeHash by remember {
            mutableStateOf("")
        }
        var resourceHash by remember {
            mutableStateOf("")
        }
        val size = radius * 2
        val dotSize = dotRadius * 2

        Box(
            modifier = Modifier
                .size(size.dp)
                .border(2.dp, Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            val nodes = hashRingViewModel.getNodes()
            val resources = hashRingViewModel.getResources()
            for (i in nodes + resources) {
                val angle = 360f * i / hashRingViewModel.getRingCapacity()
                val offset = Offset(
                    x = ((radius - dotRadius / 2) * kotlin.math.cos(Math.toRadians(angle.toDouble()))).toFloat(),
                    y = ((radius - dotRadius / 2) * kotlin.math.sin(Math.toRadians(angle.toDouble()))).toFloat()
                )
                Log.d("hashring", offset.toString())
                Box(
                    modifier = Modifier
                        .size(if (hashRingViewModel.isHead(i)) (dotSize.toFloat() * 1.2).dp else dotSize.dp)
                        .offset(
                            x = offset.x.dp,
                            y = offset.y.dp
                        )
                        .background(if (i in nodes) Color.Red else Color.Blue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$i",
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = "哈希环\nsize = ${hashRingViewModel.getRingCapacity()}",
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                modifier = Modifier.weight(4f),
                value = nodeHash,
                onValueChange = {
                    nodeHash = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("请输入节点哈希值") }
            )
            Spacer(modifier = Modifier.size(10.dp))
            Button(
                onClick = {
                    nodeHash.takeIf {
                        it.toIntOrNull() != null && it.isNotEmpty()
                    }?.let {
                        var res = hashRingViewModel.insertNode(it.toInt())
                        Toast.makeText(
                            context, "插入 ${
                                when (res) {
                                    INSERT_SUCCEED -> "成功"
                                    INSERT_DUPLICATE -> "重复"
                                    INSERT_EXCEED_LIMIT -> "超限"
                                    else -> "失败"
                                }
                            }", Toast.LENGTH_SHORT
                        ).show()
                    }
                    nodeHash = ""
                },
                modifier = Modifier.weight(1f),
            ) {
                Text("插入节点")
            }
        }

        Spacer(modifier = Modifier.size(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                modifier = Modifier.weight(4f),
                value = resourceHash,
                onValueChange = {
                    resourceHash = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("请输入资源哈希值") }
            )
            Spacer(modifier = Modifier.size(10.dp))
            Button(
                onClick = {
                    if (resourceHash.isNotEmpty() && resourceHash.toIntOrNull() != null) {
                        var res = hashRingViewModel.addResources(resourceHash.toInt())
                        Toast.makeText(
                            context, "插入 ${
                                when (res) {
                                    INSERT_SUCCEED -> "成功"
                                    INSERT_DUPLICATE -> "重复"
                                    INSERT_EXCEED_LIMIT -> "超限"
                                    else -> "失败"
                                }
                            }", Toast.LENGTH_SHORT
                        ).show()
                    }
                    resourceHash = ""
                },
                modifier = Modifier.weight(1f),
            ) {
                Text("插入资源")
            }
        }
        Spacer(modifier = Modifier.size(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                modifier = Modifier.weight(4f),
                value = ringSize,
                onValueChange = {
                    ringSize = it
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("请输入哈希环大小(1-8)") }
            )
            Spacer(modifier = Modifier.size(10.dp))
            Button(
                onClick = {
                    if (ringSize.isNotEmpty() && ringSize.toIntOrNull() != null) {
                        hashRingViewModel.clear()
                        hashRingViewModel.newInstance(ringSize.toInt())
                        Toast.makeText(context, "重置成功", Toast.LENGTH_SHORT).show()
                    }
                    ringSize = ""
                },
                modifier = Modifier.weight(1f),
            ) {
                Text("重置哈希环")
            }
        }
    }

}
//
//@Composable
//fun DrawArrowOnArc(
//    center: Offset,
//    radius: Float,
//    startAngle: Float,
//    sweepAngle: Float,
//    color: Color,
//    strokeWidth: Dp,
//    arrowSize: Dp
//) {
//    // 计算箭头旋转角度
//    val arrowAngle = remember(startAngle, sweepAngle) {
//        startAngle + sweepAngle
//    }
//
//    // 计算箭头长度
//    val arrowLength = with(LocalDensity.current) { arrowSize.toPx() }
//
//    // 使用 Canvas 绘制圆弧和箭头
//    Canvas(
//        modifier = Modifier
//            .fillMaxSize()
//            .alpha(ContentAlpha.high),
//    ) {
//        val paint = Paint().apply {
//            this.color = color
//            this.strokeWidth = 2f
//            this.style = PaintingStyle.Stroke
//        }
//
//        val rect = Rect(center.x - radius, center.y - radius, center.x + radius, center.y + radius)
//
//        // 绘制圆弧
////        drawArc(
////            rect,
////            startAngle,
////            sweepAngle,
////            useCenter = false,
////            color = color,
////        )
//
//        drawArc(Color.Black, startAngle, sweepAngle, false, topLeft = center)
//        // 计算箭头位置
//        val arrowStart = Offset(
//            center.x + radius * Math.cos(Math.toRadians(arrowAngle.toDouble())).toFloat(),
//            center.y + radius * Math.sin(Math.toRadians(arrowAngle.toDouble())).toFloat()
//        )
//
//        // 绘制箭头
//        drawLine(
//            color = color,
//            start = center,
//            end = arrowStart
//        )
//
//        drawLine(
//            color = color,
//            start = arrowStart,
//            end = Offset(
//                arrowStart.x + arrowLength * Math.cos(Math.toRadians((arrowAngle - 150).toDouble()))
//                    .toFloat(),
//                arrowStart.y + arrowLength * Math.sin(Math.toRadians((arrowAngle - 150).toDouble()))
//                    .toFloat()
//            )
//        )
//
//        drawLine(
//            color = color,
//            start = arrowStart,
//            end = Offset(
//                arrowStart.x + arrowLength * Math.cos(Math.toRadians((arrowAngle + 150).toDouble()))
//                    .toFloat(),
//                arrowStart.y + arrowLength * Math.sin(Math.toRadians((arrowAngle + 150).toDouble()))
//                    .toFloat()
//            ),
//        )
//    }
//}

@Composable
fun toPx(dpValue: Dp): Float {
    return with(LocalDensity.current) { dpValue.toPx() }
}