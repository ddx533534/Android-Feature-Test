package com.example.androidfeature.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static com.example.androidfeature.Constants.LIFECYCLE_TAG;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LIFECYCLE_TAG, getActivityName() + ":onCreate");
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
        return null;
    }
}