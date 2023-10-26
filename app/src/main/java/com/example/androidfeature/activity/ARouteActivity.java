package com.example.androidfeature.activity;


import android.app.Activity;
import android.os.Bundle;

import com.example.androidfeature.R;
import com.example.androidfeature.bean.Message;

import org.greenrobot.eventbus.EventBus;


public class ARouteActivity extends Activity {

    private static final String TAG = "Rxjava-Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        EventBus.getDefault().post(new Message(Message.MsgType.CLOSE_DIALOG));
    }
}