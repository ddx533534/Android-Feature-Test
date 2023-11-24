package com.example.androidfeature.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.androidfeature.R

data class Coordinate(var x: Int, var y: Int)

/**
 * TODO: document your custom view class.
 */
class CustomScrollView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {


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