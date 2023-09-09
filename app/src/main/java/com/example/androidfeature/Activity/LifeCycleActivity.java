package com.example.androidfeature.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toolbar;

import com.example.androidfeature.R;

public class LifeCycleActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);
        findViewById(R.id.start_new_activity2).setOnClickListener(v -> {
            Intent intent = new Intent(this, LifeCycleActivity2.class);
            startActivity(intent);
        });
        findViewById(R.id.start_already_activity2).setOnClickListener(v -> {
            Intent intent = new Intent(this, LifeCycleActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
        findViewById(R.id.start_multitask_activity2).setOnClickListener(v -> {
            Intent intent = new Intent(this, LifeCycleActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(intent);
        });
    }
}