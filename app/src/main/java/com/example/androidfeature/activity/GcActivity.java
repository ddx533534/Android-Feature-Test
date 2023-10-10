package com.example.androidfeature.activity;

import android.os.Bundle;

import com.example.androidfeature.Application;
import com.example.androidfeature.R;
import com.example.androidfeature.fragment.GcFragment;

import java.lang.ref.ReferenceQueue;

public class GcActivity extends BaseActivity {

    private GcFragment fragment;
    private ReferenceQueue<?> referenceQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gc);
        fragment = GcFragment.newInstance("123", "321");
        getFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
        Application.TestLeakMemoryContext = this;
    }

}