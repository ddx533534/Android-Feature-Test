package com.example.androidfeature.leakmemory;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.androidfeature.Application;
import com.example.androidfeature.leakmemory.watcher.ActivityWatcher;
import com.example.androidfeature.leakmemory.watcher.FragmentWatcher;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executor;

import static com.example.androidfeature.provider.LeakMemoryProvider.TAG;

/**
 * 内存泄露管理类，负责对Activity和Fragment的泄露监控
 */
public class LeakMemoryManager {
    private static final LeakMemoryManager instance;
    static {
        instance = new LeakMemoryManager();
    }

    public static void installManager(@NonNull Application application) {
        new ActivityWatcher().install(application);
        new FragmentWatcher().install(application);
    }
}
