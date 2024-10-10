package com.example.androidfeature.activity

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidfeature.R

class ScrollViewActivity : AppCompatActivity() {
    val TAG = "ScrollViewActivity"
    val count = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_view)
        // 定义一些背景颜色
        val colors = arrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN)

        var viewGroup: LinearLayout = this.findViewById<LinearLayout>(R.id.all)
//        var view1: View = this.findViewById<LinearLayout>(R.id.firstone)
//        var view2: View = this.findViewById<LinearLayout>(R.id.lastone)
        for (i in 0..count) {
            // 创建新的 TextView 作为子视图
            val textView = TextView(this).apply {
                text = "View $i"
                textSize = 20f
                setPadding(16, 16, 16, 16)
            }
            // 创建新的 LinearLayout 作为子视图
            val childLayout = LinearLayout(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        500f,
                        resources.displayMetrics
                    ).toInt()
                )
                setBackgroundColor(colors[i % colors.size]) // 设置背景颜色
            }

            // 监听视图的全局布局变化
            childLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    // 检查视图的可见性
                    checkViewVisibility(childLayout, i)
                    // 移除监听器以避免重复调用
                    childLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

            // 监听视图的滚动变化
            childLayout.viewTreeObserver.addOnScrollChangedListener {
                checkViewVisibility(childLayout, i)
            }
            childLayout.addView(textView)
            // 将子视图添加到父 LinearLayout
            viewGroup.addView(childLayout)
        }
    }

    fun checkViewVisibility(view: View, i: Int) {
        val localRect = Rect()
        val globalRect = Rect()
        view.getLocalVisibleRect(localRect)
        val isVisible = view.getGlobalVisibleRect(globalRect)

        if (!isVisible) {
            Log.i(TAG, "view $i 不可见！")
        } else {
            val height = view.height
            val width = view.width
            val visibleHeight = globalRect.height()
            val visibleWidth = globalRect.width()
            if (visibleWidth <= 0 || visibleHeight <= 0) {
                return
            } else {
                val visibleAreaRadio =
                    (visibleWidth * visibleHeight * 1.0) / (height * width * 1.0)
//                Log.i(
//                    TAG,
//                    "height: $height width: $width visibleHeight: $visibleHeight visibleWidth: $visibleWidth"
//                )
                Log.i(TAG, "view $i visibleAreaRadio: $visibleAreaRadio")

            }
        }
    }

}