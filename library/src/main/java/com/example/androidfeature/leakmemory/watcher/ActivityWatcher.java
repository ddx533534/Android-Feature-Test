package com.example.androidfeature.leakmemory.watcher;

import android.app.Activity;

import com.example.androidfeature.Application;
import com.example.androidfeature.leakmemory.callbacks.ActivityLifeCycleDefaultCallbacks;

/**
 * Activity 内存泄露监控者
 */
public class ActivityWatcher implements InstallWatcher {
    private Application application;
    private android.app.Application.ActivityLifecycleCallbacks lifecycleCallbacks;

    public ActivityWatcher(Application application, ObjectWatcher objectWatcher) {
        this.application = application;
        this.lifecycleCallbacks = new ActivityLifeCycleDefaultCallbacks() {
            @Override
            public void onActivityDestroyed(Activity activity) {
                if (objectWatcher != null) {
                    objectWatcher.watch(activity, activity.getClass().getName());
                } else {
                    throw new IllegalArgumentException("objectWatcher is null!");
                }
            }
        };
    }

    @Override
    public void install() {
        if (application != null) {
            application.registerActivityLifecycleCallbacks(lifecycleCallbacks);
        }
    }

    @Override
    public void uninstall() {
        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
        }
    }
}
