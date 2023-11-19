package com.example.androidfeature.widget.Bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 根据手势绘制曲线，未采用double cache 技术，每次只能绘制一条直线
 */
public class CurveView extends View {

    Paint paint;

    int preX;
    int preY;

    int curX;
    int curY;


    public CurveView(Context context) {
        super(context);
        init();
    }

    public CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(preX,preY,curX,curY,paint);
        preX = curX;
        preY = curY;
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        canvas.drawLine(preX,preY,curX,curY,paint);
//        // 无法保存历史轨迹
//        preX = curX;
//        preY = curY;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // start
                preX = x;
                preY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // move
                curX = x;
                curY = y;
                this.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // end
                this.invalidate();
                break;
            default:
                return false;
        }
        return true;
    }
}