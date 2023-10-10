package com.example.androidfeature.leakmemory.watcher;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.androidfeature.Application;

public class FragmentWatcher extends ObjectWatcher {
    @Override
    public void install(@NonNull Application application) {
        super.install(application);
        application.registerActivityLifecycleCallbacks(new android.app.Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.getFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                        super.onFragmentViewDestroyed(fm, f);
                        watch(f.getView(), f.getClass().getName() + ".view");
                    }

                    @Override
                    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                        super.onFragmentDestroyed(fm, f);
                        watch(f.getView(), f.getClass().getName());
                    }
                }, true);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
