package com.example.androidfeature.widget.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginLeft
import androidx.core.view.marginTop

const val TAG = "View-Code"

class CustomLinearLayout(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = getStrMeasureSpecMode(MeasureSpec.getMode(widthMeasureSpec))
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = getStrMeasureSpecMode(MeasureSpec.getMode(heightMeasureSpec))
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        Log.d(
            TAG,
            "onMeasure CustomLinearLayout - widthMode:$widthMode, widthSize:$widthSize,heightMode:$heightMode,heightSize:$heightSize"
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(
            TAG,
            "onLayout CustomLinearLayout - left:$left, top:$top,right:$right,bottom:$bottom"
        )
        Log.d(
            TAG,
            "onLayout CustomLinearLayout - marginleft:${marginLeft}, margintop:$marginTop,marginright:$marginEnd,marginbottom:$marginBottom"
        )
        Log.d(
            TAG,
            "onLayout CustomLinearLayout - paddingleft:${paddingLeft}, paddingtop:$paddingTop,paddingright:$paddingEnd,paiddingbottom:${paddingBottom}"
        )

        Log.d(
            TAG,
            "onLayout CustomLinearLayout - l:${l}, t:$t,r:$r,b:${b}"
        )
        super.onLayout(changed, l, t, r, b)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}