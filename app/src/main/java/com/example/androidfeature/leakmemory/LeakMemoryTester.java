package com.example.androidfeature.leakmemory;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;

import com.example.androidfeature.fragment.GcFragment;

public class LeakMemoryTester {
    @SuppressLint("StaticFieldLeak")
    public static Context TestLeakMemoryContext = null;
    public static GcFragment TestLeakMemoryFragment = null;
    public static Dialog TestLeakMemoryDialog = null;
}
