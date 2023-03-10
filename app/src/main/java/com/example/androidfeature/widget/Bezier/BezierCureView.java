package com.example.androidfeature.widget.Bezier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class BezierCureView extends View {
    private final static float POINT_INNER_RADIUS = 4.0f;
    private final static float POINT_OUT_RADIUS = 16.0f;

    Paint paint;

    Bitmap bitmapBuffer;
    Canvas canvasBuffer;

    ArrayList<Point> points;

    public BezierCureView(Context context) {
        super(context);
        init();
    }

    public BezierCureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierCureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BezierCureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }


    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(9);
        points = new ArrayList<>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (bitmapBuffer == null) {
            bitmapBuffer = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            canvasBuffer = new Canvas(bitmapBuffer);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmapBuffer, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                savePoints(x, y);
                if (canvasBuffer != null) {
                    canvasBuffer.drawCircle(x, y, POINT_INNER_RADIUS, paint);
                    canvasBuffer.drawCircle(x, y, POINT_OUT_RADIUS, paint);
                }
                this.invalidate();
                break;
            default:
                return false;
        }
        return true;
    }

    private void savePoints(float x, float y) {
        Point point = new Point();
        point.x = x;
        point.y = y;
        point.isNeedRefresh = true;
        points.add(point);
    }

    public void drawBezierCurve() {
        Path path = new Path();
        Point prepoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point curpoint = points.get(i);
            path.quadTo(prepoint.x, prepoint.y, curpoint.x, curpoint.y);
            prepoint = curpoint;
        }
        canvasBuffer.drawPath(path, paint);
        this.invalidate();
    }

    class Point {
        float x;
        float y;
        boolean isNeedRefresh;
    }
}
