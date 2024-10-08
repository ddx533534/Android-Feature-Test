package com.example.androidfeature.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.Arrays;

/**
 * 颜色渐变组件视图。<br/>
 * 和 RN 实现一致，继承 View 实现，暂时无法直接下挂子节点；如果后续有问题比如需要支持子节点，
 * 可以继承 ViewGroup 然后实现 dispatchDraw 方法。
 */
public class RecceLinearGradientView extends View {

    private final int POINT_LENGTH = 2;

    private final int COLORS_MIN_LENGTH = 2;

    private final int TRANSPARENT_COLOR = 0x00000000;

    // 点坐标起点
    public static final float POINT_START_VALUE = 0f;
    // 点坐标中点
    public static final float POINT_CENTER_VALUE = 0.5f;
    // 点坐标终点
    public static final float POINT_END_VALUE = 1f;
    // 角度坐标中点
    public static final float ANGLE_CENTER_VALUE = 0.5f;
    // 默认角度值
    public static final float ANGLE_DEFAULT_VALUE = 45f;

    // 渐变背景色路径
    private Path mPath;

    // 整体背景四边坐标信息
    private RectF mRecF;

    // 区域的宽高
    private int[] mSize;


    // 背景画笔
    private Paint mBgPaint;

    // 着色器
    private LinearGradient mShader;

    // 渐变起始位置
    private float[] start;

    // 渐变结束位置
    private float[] end;

    // 渐变颜色
    private int[] colors;

    // 颜色位置分布
    private float[] locations;

    // 圆角度数
    private float[] mRoundedCornerRadius;

    // 是否使用角度
    private boolean useAngle;

    // 角度
    private float angle;

    // 角度中心位置
    private float[] angleCenter;

    public RecceLinearGradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPath = new Path();
        this.mRecF = new RectF();
        this.mSize = new int[]{0, 0};
        this.mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mShader = null;
        // 起点默认为顶边中点
        this.start = new float[]{POINT_CENTER_VALUE, POINT_START_VALUE};
        // 终点默认为底边中点
        this.end = new float[]{POINT_CENTER_VALUE, POINT_END_VALUE};
        // 默认不使用角度
        this.useAngle = false;
        // 角度中心默认为区域中心
        this.angleCenter = new float[]{ANGLE_CENTER_VALUE, ANGLE_CENTER_VALUE};
        // 角度值默认为45°
        this.angle = ANGLE_DEFAULT_VALUE;
        this.colors = new int[]{-13213214, 23124};
        this.locations = null;
        this.mRoundedCornerRadius = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        if (mSize == null) {
            mSize = new int[]{w, h};
        } else {
            mSize[0] = w;
            mSize[1] = h;
        }
        updateGradient();
        updatePath();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    /**
     * 更新渐变起始点，配置画笔的渐变属性
     */
    private void updateGradient() {
        if (colors == null || (locations != null && locations.length != colors.length)) {
            return;
        }
        // 根据外部传入的坐标转换为最终的起始点
        float[] startPoint;
        float[] endPoint;
        if (useAngle && angleCenter != null) {
            // 转换为笛卡尔坐标系的角度。原先传入的角度是以正北方向顺时针偏移的，要转换成笛卡尔坐标系的角度，因此需要正东方向(X轴正方向)逆时针偏移。
            float angle0 = 90 - angle;
            // 获取一般情况下渐变的相对起点，具体原理可参考该方法注释的图片
            float[] relativePoint = getRelativeStartPoint(mSize, angle0);

            // 获取实际角度中心
            float[] realAngleCenter = new float[]{
                    angleCenter[0] * mSize[0],
                    angleCenter[1] * mSize[1]
            };
            // 默认情况下角度中心都是位于区域的中心，因此需要根据传入的角度中心做下镜像 + 中心转换。
            startPoint = new float[]{realAngleCenter[0] + relativePoint[0], realAngleCenter[1] - relativePoint[1]};
            endPoint = new float[]{realAngleCenter[0] - relativePoint[0], realAngleCenter[1] + relativePoint[1]};
        } else {
            startPoint = new float[]{start[0] * mSize[0], start[1] * mSize[1]};
            endPoint = new float[]{end[0] * mSize[0], end[1] * mSize[1]};
        }
        mShader = new LinearGradient(
                startPoint[0],
                startPoint[1],
                endPoint[0],
                endPoint[1],
                colors,
                locations,
                Shader.TileMode.CLAMP
        );

        mBgPaint.setShader(mShader);
        mBgPaint.setStyle(Paint.Style.FILL);
        invalidate();
    }

    /**
     * 获取渐变角度相对起始点(startPoint)<br/>
     * 具体原理参考：https://drafts.csswg.org/css-images/#linear-gradients<br/>
     * 或者：https://developer.mozilla.org/zh-CN/docs/Web/CSS/gradient/linear-gradient<br/><br/>
     *
     * <img src = "https://developer.mozilla.org/zh-CN/docs/Web/CSS/gradient/linear-gradient/linear-gradient.png"/><br/>
     * <br/>
     *
     * @param size  区域宽高
     * @param angle 渐变线角度
     */
    private float[] getRelativeStartPoint(int[] size, float angle) {
        if (size == null) {
            return new float[POINT_LENGTH];
        }
        // 将 angle 约束到[0,360)
        angle = angle % 360;
        if (angle < 0f) {
            angle += 360f;
        }
        // 如果角度是垂直或者水平，可以直接返回
        if (angle % 90 == 0f) {
            return getHorizontalOrVerticalStartPoint(angle, size);
        }
        // 获取渐变线的斜率
        float slope = (float) Math.tan(angle * Math.PI / 180.0f);

        // 获取与渐变线垂直相交线的斜率
        float perpendicularSlope = -1 / slope;

        // 获取起始角（四个角中的一个）
        float[] startCorner = getStartCornerToIntersect(angle, size);

        // 根据起始角坐标，以及垂直线斜率，计算出垂直线与 y 轴相交点的纵坐标， 即 y = kx + b 中的 b。
        float b = startCorner[1] - perpendicularSlope * startCorner[0];

        // 解析垂直线与渐变线的交点作为相对起始点， 即解方程两条直线相交的点坐标
        float startX = b / (slope - perpendicularSlope);
        float startY = slope * startX;

        return new float[]{startX, startY};
    }

    /**
     * 非水平或者垂直情况下渐变线相交于区域的某个角坐标
     *
     * @param angle 渐变线角度
     * @param size  区域宽高
     */
    private float[] getStartCornerToIntersect(float angle, int[] size) {
        if (size == null) {
            return new float[POINT_LENGTH];
        }
        float halfW = size[0] / 2f;
        float halfH = size[1] / 2f;
        if (angle < 90f) {
            // 左下角坐标
            return new float[]{-halfW, -halfH};
        } else if (angle < 180f) {
            // 右下角坐标
            return new float[]{halfW, -halfH};
        } else if (angle < 270f) {
            // 右上角坐标
            return new float[]{halfW, halfH};
        } else {
            // 左上角坐标
            return new float[]{-halfW, halfH};
        }
    }

    /**
     * 垂直或者水平情况下渐变线与区域边界相交的起点。
     *
     * @param angle 渐变线角度
     * @param size  区域宽高
     */
    private float[] getHorizontalOrVerticalStartPoint(float angle, int[] size) {
        if (size == null) {
            return new float[POINT_LENGTH];
        }
        float halfW = size[0] / 2f;
        float halfH = size[1] / 2f;
        if (floatsEqual(angle, 0f)) {
            // 水平角度，从左到右，起始点就在左边的中点
            return new float[]{-halfW, 0};
        } else if (floatsEqual(angle, 90f)) {
            // 垂直角度，从下到上，起始点就在下边的中点
            return new float[]{0, -halfH};
        } else if (floatsEqual(angle, 180f)) {
            // 水平角度，从右到左，起始点就在右边的中点
            return new float[]{halfW, 0};
        } else {
            // 垂直角度，从上到下，起始点就在上边的中点
            return new float[]{0, halfH};
        }
    }

    public static boolean floatsEqual(float f1, float f2) {
        if (!Float.isNaN(f1) && !Float.isNaN(f2)) {
            return Math.abs(f2 - f1) < 1.0E-5F;
        } else {
            return Float.isNaN(f1) && Float.isNaN(f2);
        }
    }

    /**
     * 更新路径
     */
    private void updatePath() {
        if (mSize == null) {
            mSize = new int[]{0, 0};
        }
        if (mPath == null) {
            mPath = new Path();
        }
        if (mRecF == null) {
            mRecF = new RectF();
        }
        mPath.reset();
        // 配置路径区域
        mRecF.set(0, 0, (float) mSize[0], (float) mSize[1]);
        mPath.addRoundRect(
                mRecF,
                mRoundedCornerRadius,
                Path.Direction.CW
        );
    }


    public void setColors(int[] arr) {
        if (arr == null) {
            return;
        }
        // 如果传入颜色数量小于2个，默认给予两个透明值
        if (arr.length < COLORS_MIN_LENGTH) {
            this.colors = new int[]{TRANSPARENT_COLOR, TRANSPARENT_COLOR};
        } else {
            this.colors = Arrays.copyOf(arr, arr.length);
        }
        updateGradient();
    }

    public void setStart(double[] arr) {
        if (arr == null || arr.length < POINT_LENGTH) {
            return;
        }
        if (start == null) {
            start = new float[POINT_LENGTH];
        }
        for (int i = 0; i < start.length; i++) {
            start[i] = (float) arr[i];
        }
        updateGradient();
    }

    public void setEnd(double[] arr) {
        if (arr == null || arr.length < POINT_LENGTH) {
            return;
        }
        if (end == null) {
            end = new float[POINT_LENGTH];
        }
        for (int i = 0; i < end.length; i++) {
            end[i] = (float) arr[i];
        }
        updateGradient();
    }

    public void setLocations(double[] arr) {
        if (arr == null) {
            return;
        }
        float[] loc = new float[arr.length];
        for (int i = 0; i < loc.length; i++) {
            loc[i] = (float) arr[i];
        }
        locations = loc;
        updateGradient();
    }

    public void setUseAngle(boolean useAngle) {
        this.useAngle = useAngle;
        updateGradient();
    }

    public void setAngle(double angle) {
        this.angle = (float) angle;
        updateGradient();
    }

    public void setAngleCenter(double[] arr) {
        if (arr == null || arr.length < POINT_LENGTH) {
            return;
        }
        if (angleCenter == null) {
            angleCenter = new float[POINT_LENGTH];
        }
        for (int i = 0; i < angleCenter.length; i++) {
            angleCenter[i] = (float) arr[i];
        }
        updateGradient();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制背景
        if (mPath == null) {
            canvas.drawPaint(mBgPaint);
        } else {
            canvas.drawPath(mPath, mBgPaint);
        }
    }

}
