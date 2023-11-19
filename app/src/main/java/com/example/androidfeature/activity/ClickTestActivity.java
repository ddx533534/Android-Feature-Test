package com.example.androidfeature.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.androidfeature.R;

public class ClickTestActivity extends Activity {

    String TAG = "ClickTestActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_test);
        LinearLayout linearLayout = findViewById(R.id.view_group);
        Button button = findViewById(R.id.button);

//        linearLayout.setOnTouchListener((v, event) -> {
//            Log.d(TAG,"linearLayout 执行了 onTouch 事件,事件类型" + getActionName(event));
//            return true;
//        });

        linearLayout.setOnClickListener(v -> {
            Log.d(TAG,"linearLayout 执行了 onClick 事件,事件类型：ACTION_UP");
        });

//        button.setOnTouchListener((v, event) -> {
//            Log.d(TAG,"button 执行了 onTouch 事件,事件类型：" + getActionName(event));
//            return false;
//        });

        button.setOnClickListener(v -> {
            Log.d(TAG,"button 执行了 onClick 事件,事件类型: ACTION_UP" );
        });
    }

    private String getActionName(MotionEvent event){
        switch (event.getAction()){
            case (MotionEvent.ACTION_DOWN):
                return "ACTION_DOWN";
            case (MotionEvent.ACTION_MOVE):
                return "ACTION_MOVE";
            case (MotionEvent.ACTION_CANCEL):
                return "ACTION_CANCEL";
            case (MotionEvent.ACTION_UP):
                return "ACTION_UP";
            default:
                return "null";
        }
    }
}