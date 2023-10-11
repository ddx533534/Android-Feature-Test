package com.example.androidfeature.leakmemory;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.example.androidfeature.Application;
import com.example.androidfeature.leakmemory.watcher.ActivityWatcher;
import com.example.androidfeature.leakmemory.watcher.FragmentWatcher;
import com.example.androidfeature.leakmemory.watcher.InstallWatcher;
import com.example.androidfeature.leakmemory.watcher.ObjectWatcher;
import com.example.androidfeature.leakmemory.watcher.RootViewWatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 内存泄露管理类，负责对Activity和Fragment的泄露监控
 */
public class LeakMemoryManager {
    private static LeakMemoryManager instance;
    private ObjectWatcher objectWatcher;
    private List<InstallWatcher> installWatcherList;
    private Handler mainHandler;

    static {
        instance = new LeakMemoryManager();
    }

    private LeakMemoryManager() {
        objectWatcher = new ObjectWatcher();
        installWatcherList = new ArrayList<>();
    }

    public static void installManager(@NonNull Application application) {
        if (instance != null) {
            instance.mainHandler = new Handler(application.getMainLooper());
            instance.installWatcherList.addAll(Arrays.asList(
                    new ActivityWatcher(application, instance.objectWatcher),
                    new FragmentWatcher(application, instance.objectWatcher),
                    new RootViewWatcher(instance.objectWatcher)
            ));
            new FragmentWatcher(application, instance.objectWatcher);
            for (InstallWatcher installWatcher : instance.installWatcherList) {
                installWatcher.install();
            }
        } else {
            throw new IllegalArgumentException("instance has not initialed yet!");
        }
    }

    public static void uninstallManager() {
        if (instance == null) {
            return;
        }
        if (instance.installWatcherList != null && !instance.installWatcherList.isEmpty()) {
            for (InstallWatcher installWatcher : instance.installWatcherList) {
                installWatcher.uninstall();
            }
        }
        instance = null;
    }

}
