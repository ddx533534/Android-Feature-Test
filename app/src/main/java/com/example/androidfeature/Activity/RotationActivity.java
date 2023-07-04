package com.example.androidfeature.Activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;

import com.example.androidfeature.R;

/**
 * 设备旋转和 activity 旋转时一致性判断，可以用于屏幕旋转场景，比如悬浮球贴边等。<br/>
 * 1. 如果 Activity 支持屏幕旋转，但没有配置 android:configChanges="orientation|screenSize"，会触发销毁重建，无法满足需求，因此都会配置该参数<br/>
 * 2. 如果 Activity 配置参数，屏幕旋转时触发 onConfigurationChanged 回调，这样在屏幕旋转的时候就可以监听到屏幕的方向，缺点是只能判断设备的水平和垂直方向<br/>
 *    无法判断 activity 的方向；该回调在横屏之间的旋转(90->270)也无法触发。<br/>
 * 3. 如果需要判断真实的 Activity 方向与当前设备是否一致，需要监听设备角度变化进行判断<br/>
 * 参考：https://mp.weixin.qq.com/s/QgxDTYkVxLJXrWpuiSIa0w<br/>
 */
public class RotationActivity extends Activity {
    public static final String TAG = "RotationActivity_Log";
    private OrientationEventListener orientationEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                //顺时针旋转，角度从0 - 359
                if (orientation == ORIENTATION_UNKNOWN) {
                    return;
                }
                int deviceOrientation = 0;
                if (orientation >= 70 && orientation < 120) {
                    // 设备旋转90°
                    deviceOrientation = 90;
                } else if (orientation >= 150 && orientation < 210) {
                    // 设备旋转90°
                    deviceOrientation = 180;
                } else if (orientation >= 240 && orientation < 300) {
                    // 设备旋转90°
                    deviceOrientation = 270;
                }
                processOrientation(deviceOrientation);
            }
        };
        orientationEventListener.enable();
        setContentView(R.layout.activity_rotation);
        Log.d(TAG, "onCreate");

    }

    private void processOrientation(int deviceOrientation) {
        // 获取 activity 的旋转方向，该方向和设备真实的旋转角度相反，比如设备顺时针旋转90，此时 activity 的旋转方向就是270
        int activityOrientation = getWindowManager().getDefaultDisplay().getRotation();
        if(deviceOrientation == 0 && activityOrientation == Surface.ROTATION_0){
            // 设备方向和 activity 方向一致，均为自然方向
            Log.d(TAG, "设备方向和 activity 方向一致，均为自然方向");
        } else if(deviceOrientation == 90 && activityOrientation == Surface.ROTATION_270){
            // 设备方向和 activity 方向一致，头部水平朝右
            Log.d(TAG, "设备方向和 activity 方向一致，头部水平朝右");

        } else if(deviceOrientation == 180 && activityOrientation == Surface.ROTATION_180){
            // 设备方向和 activity 方向一致，头部垂直向下
            Log.d(TAG, "设备方向和 activity 方向一致，头部垂直向下");

        } else if(deviceOrientation == 270 && activityOrientation == Surface.ROTATION_90){
            // 设备方向和 activity 方向一致，头部水平朝左
            Log.d(TAG, "设备方向和 activity 方向一致，头部水平朝左");

        } else {
            // 错误
        }
        Log.d(TAG, "设备旋转角度" + deviceOrientation + "activity屏幕方向" + activityOrientation);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "屏幕发生旋转");
        // 只能在横屏和竖屏之间的切换才触发该回调，横屏之间的切换是无法触发回调的！！！因此需要借助角度监听来实现。
        int deviceOrientation  = newConfig.orientation;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        orientationEventListener.disable();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState");
    }
}