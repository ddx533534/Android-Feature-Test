package com.example.androidfeature.leakmemory.watcher;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.androidfeature.Application;
import com.example.androidfeature.leakmemory.callbacks.ActivityLifeCycleDefaultCallbacks;

/**
 * Fragment 内存泄露监控者
 */
public class FragmentWatcher implements InstallWatcher {
    private Application application;
    private final android.app.Application.ActivityLifecycleCallbacks lifecycleCallbacks;

    public FragmentWatcher(Application application, ObjectWatcher objectWatcher) {
        this.application = application;
        this.lifecycleCallbacks = new ActivityLifeCycleDefaultCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.getFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                        objectWatcher.watch(f.getView(), f.getClass().getName() + ".view");
                    }

                    @Override
                    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                        objectWatcher.watch(f, f.getClass().getName());
                    }
                }, true);
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
