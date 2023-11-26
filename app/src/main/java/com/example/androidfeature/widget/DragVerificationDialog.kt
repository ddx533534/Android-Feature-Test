package com.example.androidfeature.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface.OnCancelListener
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.androidfeature.R

/**
 * TODO: document your custom view class.
 */

interface OnDragAction {
    /**
     * 拖动开始
     */
    fun onDragStart()

    /**
     * 拖动成功
     */
    fun onDragSuccess()

    /**
     * 拖动失败
     */
    fun onDragFail()
}

class DragVerificationDialog(context: Context) : Dialog(context) {

    private lateinit var onDragAction: OnDragAction
    private lateinit var lastCoordinate: Coordinate

    // 误差值默认为5
    private var deviation: Int = 6

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drag_verification_dialog)
        val drag = findViewById<LinearLayoutCompat>(R.id.drag)
        val dragLeft = findViewById<LinearLayoutCompat>(R.id.drag_left)
        val target = findViewById<LinearLayoutCompat>(R.id.target)
        val targetDes = findViewById<LinearLayoutCompat>(R.id.target_des)
        drag.setOnTouchListener { drag, motionEvent ->
            motionEvent?.let {
                val x = it.x.toInt()
                val y = it.y.toInt()
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastCoordinate = Coordinate(x, y)
                        onDragAction.onDragStart()
                        true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val offsetX = x - lastCoordinate.x
                        val offsetY = y - lastCoordinate.y
                        val parent = drag.parent as View
                        if (drag.left + offsetX >= parent.left && drag.right + offsetX <= parent.right) {
                            drag.layout(
                                drag.left + offsetX,
                                drag.top,
                                drag.right + offsetX,
                                drag.bottom
                            )
                            dragLeft.layout(
                                dragLeft.left,
                                dragLeft.top,
                                dragLeft.right + offsetX,
                                dragLeft.bottom
                            )
                            target.layout(
                                target.left + offsetX,
                                target.top,
                                target.right + offsetX,
                                target.bottom
                            )
                        }

                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        if (checkPosition(target, targetDes)) {
                            onDragAction.onDragSuccess()
                        } else {
                            onDragAction.onDragFail()
                        }
                        true
                    }

                    else -> super.onTouchEvent(motionEvent)
                }
            } ?: super.onTouchEvent(motionEvent)
        }
    }

    fun checkPosition(view1: View, view2: View): Boolean {
        Log.d("drag","${kotlin.math.abs(view1.left - view2.left)}")
        return kotlin.math.abs(view1.left - view2.left) <= deviation

    }

    class Builder {

        private lateinit var dialog: DragVerificationDialog

        constructor(context: Context) {
            dialog = DragVerificationDialog(context)
        }

        fun setCancelable(cancellable: Boolean): Builder {
            dialog.setCancelable(cancellable)
            return this
        }

        fun setOnCancelListener(onCancelListener: OnCancelListener): Builder {
            dialog.setOnCancelListener(onCancelListener)
            return this
        }

        fun setOnDragAction(onDragAction: OnDragAction): Builder {
            dialog.onDragAction = onDragAction
            return this
        }

        fun setDeviation(deviation: Int): Builder {
            dialog.deviation = deviation
            return this
        }

        fun show() {
            dialog.show()
        }


    }
}