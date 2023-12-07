package com.example.androidfeature.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.androidfeature.widget.custom.Coordinate

/**
 * 滑块拖动验证弹窗布局
 */


class DragVerificationView(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    private lateinit var lastCoordinate: Coordinate


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return event?.let {
            val x = it.x.toInt()
            val y = it.y.toInt()
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastCoordinate = Coordinate(x, y)
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val offsetX = x - lastCoordinate.x
                    val offsetY = y - lastCoordinate.y
                    layout(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY)
                    true
                }

                else -> super.onTouchEvent(event)
            }
        } ?: super.onTouchEvent(event)
    }
}