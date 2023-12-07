package com.example.androidfeature.widget.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.compose.ui.unit.dp
import com.example.androidfeature.R

/**
 * RectView,custom View
 */
const val namespace = "http://schemas.android.com/apk/res/android"

class RectView : View {

    private val paint by lazy {
        Paint()
    }

    constructor(context: Context) : super(context) {
        init(context, null, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, null)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int?) {
        attrs?.let {
            paint.color = it.getAttributeResourceValue(
                namespace,
                "background",
                Color.BLACK
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val fixedWidth = if (widthMode == MeasureSpec.AT_MOST) 60 else widthSize
        val fixedHeight = if (heightMode == MeasureSpec.AT_MOST) 60 else heightSize

        setMeasuredDimension(fixedWidth, fixedHeight)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // allocations per draw cycle.
        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        canvas.drawRect(
            0f + paddingLeft,
            0f + paddingTop,
            contentWidth.toFloat() + paddingLeft,
            contentHeight.toFloat() + paddingTop,
            paint
        )

    }
}