package com.example.androidfeature.Activity;


import android.content.Intent;
import android.os.Bundle;

import com.example.androidfeature.R;

public class LifeCycleActivity2 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle2);
        findViewById(R.id.start_new_activity).setOnClickListener(v -> {
            Intent intent = new Intent(this, LifeCycleActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.start_already_activity).setOnClickListener(v -> {
            Intent intent = new Intent(this, LifeCycleActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
        findViewById(R.id.start_multitask_activity).setOnClickListener(v -> {
            Intent intent = new Intent(this, LifeCycleActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(intent);
        });
    }
}