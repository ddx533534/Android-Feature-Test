package com.example.androidfeature.widget.Bezier;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 根据手势绘制曲线，采用double cache 技术，保存历史纪录
 */
public class CurveViewWithDoubleCache extends View {

    long lastClickTime;

    Paint paint;

    int preX, preY;

    int curX, curY;

    Bitmap bitmapBuffer;
    Canvas canvasBuffer;


    public CurveViewWithDoubleCache(Context context) {
        super(context);
        init();
    }

    public CurveViewWithDoubleCache(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurveViewWithDoubleCache(Context context, AttributeSet attrs, int defStyle) {
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
                if (canvasBuffer != null) {
//                    canvasBuffer.drawLine(preX, preY, curX, curY, paint);
                    canvasBuffer.drawPoint(curX, curY, paint);
                }
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

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long tD = time - lastClickTime;
        lastClickTime = time;
        return tD > 0 && tD <= 400;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        // 拦截双击事件，这样会导致所有子view 无法收到双击事件
//        if(event.getAction() == MotionEvent.ACTION_DOWN && isFastDoubleClick()){
//            // 清除画布内容
//            canvasBuffer.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            invalidate();
//            return false;
//        }
//        return super.dispatchTouchEvent(event);
//    }
}