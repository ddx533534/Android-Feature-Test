package com.example.androidfeature.activity;

import static com.example.androidfeature.utils.Constants.LIFECYCLE_TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onCreate");
        LayoutInflater.from(this).setFactory2(new LayoutInflater.Factory2() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                if (!TextUtils.isEmpty(name) && name.contains("TextView")) {
                    return new TextView(context, attrs);
                }
                return null;
            }

            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                return onCreateView(null, name, context, attrs);
            }
        });
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onRestart");
        super.onRestart();

    }

    @Override
    protected void onStart() {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onStart");
        super.onStart();

    }

    @Override
    protected void onResume() {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onPause");
        super.onPause();

    }

    @Override
    protected void onStop() {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onDestroy");
        super.onDestroy();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onNewIntent");
        super.onNewIntent(intent);
    }

    protected String getActivityName() {
        String name = toString();
        String[] array = name.split("\\.");
        return array[array.length - 1];
    }
}