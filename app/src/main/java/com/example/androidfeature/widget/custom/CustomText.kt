package com.example.androidfeature.widget.custom;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View.MeasureSpec
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginLeft
import androidx.core.view.marginTop

fun getStrMeasureSpecMode(mode: Int): String {
    return when (mode) {
        MeasureSpec.AT_MOST -> "AT_MOST"
        MeasureSpec.EXACTLY -> "EXACTLY"
        else -> "UNSPECIFIED"
    }
}

class CustomText(context: Context, attributeSet: AttributeSet) :
    AppCompatTextView(context, attributeSet) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = getStrMeasureSpecMode(MeasureSpec.getMode(widthMeasureSpec))
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = getStrMeasureSpecMode(MeasureSpec.getMode(heightMeasureSpec))
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        Log.d(
            TAG,
            "onMeasure CustomText - widthMode:$widthMode, widthSize:$widthSize,heightMode:$heightMode,heightSize:$heightSize"
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        Log.d(
            TAG,
            "onLayout CustomText - left:${getLeft()}, top:${getTop()},right:${getRight()},bottom:${getBottom()}"
        )
        Log.d(
            TAG,
            "onLayout CustomText - marginleft:${marginLeft}, margintop:$marginTop,marginright:$marginEnd,marginbottom:$marginBottom"
        )
        Log.d(
            TAG,
            "onLayout CustomText - paddingleft:${paddingLeft}, paddingtop:$paddingTop,paddingright:$paddingEnd,paiddingbottom:${paddingBottom}"
        )

        Log.d(
            TAG,
            "onLayout CustomText - l:${left}, t:$top,r:$right,b:${bottom}"
        )
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val width = width.toFloat()
            val height = height.toFloat()
            val oldColor = paint.color
            paint.color = Color.RED
            it.drawLine(0f, height / 2, width, height / 2, paint)
            paint.color = oldColor
        }
    }

}
