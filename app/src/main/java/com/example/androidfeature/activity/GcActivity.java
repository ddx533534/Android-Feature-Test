package com.example.androidfeature.activity;

import android.os.Bundle;

import com.example.androidfeature.Application;
import com.example.androidfeature.R;

import java.lang.ref.ReferenceQueue;

public class GcActivity extends BaseActivity {

    private ReferenceQueue<?> referenceQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gc);
        Application.TestLeakMemoryContext = this;
    }

}