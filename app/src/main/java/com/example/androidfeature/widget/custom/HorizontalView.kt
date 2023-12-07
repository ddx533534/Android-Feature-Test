package com.example.androidfeature.widget.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import androidx.core.view.children
import androidx.core.view.marginEnd
import androidx.core.view.marginLeft
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min

/**
 * TODO: document your custom view class.
 */
const val TAG1 = "HorizontalView-view"

class HorizontalView : ViewGroup {

    private val paint by lazy {
        Paint()
    }

    private lateinit var coordinate: Pair<Float, Float>

    private lateinit var scroller: Scroller

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        attrs?.let {
            paint.color = it.getAttributeResourceValue(
                namespace,
                "background",
                Color.BLACK
            )
        }
        scroller = Scroller(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        // step1 measureChildren
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        // step2 fix height and width of parent viewGroup
        var fixedWidth = when (widthMode) {
            MeasureSpec.AT_MOST -> children.sumOf { it.measuredWidth }
            else -> widthSize
        }
        var fixedHeight = when (heightMode) {
            MeasureSpec.AT_MOST -> children.maxOfOrNull { it.measuredHeight } ?: 0
            else -> heightSize
        }
        Log.d(TAG1, "$fixedWidth, $fixedHeight")
        setMeasuredDimension(fixedWidth, fixedHeight)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        children.toList().takeIf { it.isNotEmpty() }?.filter { it.visibility != GONE }?.forEach {
            val width = it.measuredWidth
            val height = it.measuredHeight
            Log.d(TAG1, " ${it.paddingLeft},${it.paddingRight}")
            Log.d(TAG1, "${it.marginLeft},${it.marginEnd}")
            it.layout(left, 0, left + width, 0 + height)
            left += width
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }

    // horizontal scroll intercept
    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return event?.let {
            coordinate = Pair(it.x, it.y)
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    false
                }

                MotionEvent.ACTION_MOVE -> {
                    isHorizontalMove(motionEvent = it)
                }

                else -> super.onInterceptHoverEvent(event)
            }
        } ?: false
    }

    private fun isHorizontalMove(motionEvent: MotionEvent?): Boolean {
        return motionEvent?.let {
            val distance =
                hypot(motionEvent.x - coordinate.first, motionEvent.y - coordinate.second)
            val tangle = atan2(
                motionEvent.x - coordinate.first, motionEvent.y - coordinate.second
            ) * 180 / PI
            when {
                // right
                -45 < tangle && tangle <= 45 -> true
                // up
                45 < tangle && tangle <= 135 -> false
                // down
                -135 < tangle && tangle <= -45 -> false
                // left
                else -> true
            }
        } ?: false
    }

    private var currentIndex = 0
    private var childWidth = 0
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    coordinate = Pair(it.x, it.y)
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isHorizontalMove(it)) {
                        scrollBy(-(it.x - coordinate.first).toInt(), 0)
                    }
                }

                MotionEvent.ACTION_UP -> {
                    childWidth = children.toList()[currentIndex].measuredWidth
                    val distance =
                        scrollX - currentIndex * childWidth
                    if (abs(distance) > childWidth / 2) {
                        if (distance > 0) {
                            currentIndex++
                        } else {
                            currentIndex--
                        }
                    }
                    smoothScrollTo(currentIndex * childWidth, 0)
                }

                else -> {
                    return@let
                }
            }
        }

        return super.onTouchEvent(event)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
        }
    }

    private fun smoothScrollTo(destX: Int, destY: Int) {
        scroller.startScroll(scrollX, scrollY, destX - scrollX, destY - scrollY, 1000)
        invalidate()
    }

}