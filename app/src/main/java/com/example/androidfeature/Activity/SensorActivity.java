package com.example.androidfeature.Activity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.androidfeature.R;

import static android.hardware.SensorManager.SENSOR_DELAY_UI;

public class SensorActivity extends Activity {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;
    private long lastUpdateTime;

    private final static long TIME_THRESHOLD = 70L;    // 时间阈值
    private final static long SPEED_THRESHOLD = 3000L; // 速度阈值

    private float lastX; //上一次 X 轴方向加速度
    private float lastY; //上一次 Y 轴方向加速度
    private float lastZ; //上一次 Z 轴方向加速度


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                long currentTime = System.currentTimeMillis();
                long timeInterval = currentTime - lastUpdateTime;
                if (timeInterval < TIME_THRESHOLD) {
                    // 小于设定时间间隔，直接返回
                    return;
                }
                lastUpdateTime = currentTime;

                float curX = event.values[0];
                float curY = event.values[1];
                float curZ = event.values[2];

                float deltaX = curX - lastX;
                float deltaY = curY - lastY;
                float deltaZ = curZ - lastZ;


                lastX = curX;
                lastY = curY;
                lastZ = curZ;

                float speed = (float) (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000);
//                Toast.makeText(getApplicationContext(), "当前速度为:" + speed, Toast.LENGTH_SHORT).show();
                if (speed > SPEED_THRESHOLD) {
                    Toast.makeText(getApplicationContext(), "触发摇一摇", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, sensor, SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(sensorEventListener);

    }
}