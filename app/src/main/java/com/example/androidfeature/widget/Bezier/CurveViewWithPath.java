package com.example.androidfeature.widget.Bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class CurveViewWithPath extends View {
    Paint paint;

    int preX;
    int preY;

    int curX;
    int curY;

    Path path;


    public CurveViewWithPath(Context context) {
        super(context);
        init();
    }

    public CurveViewWithPath(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurveViewWithPath(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

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
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                // move
                curX = x;
                curY = y;
                path.quadTo(preX, preY, curX, curY);
                this.invalidate();
                preX = curX;
                preY = curY;
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
