package com.example.androidfeature.leakmemory.watcher;

import android.app.Activity;
import android.os.Bundle;

import com.example.androidfeature.Application;

public class ActivityWatcher extends ObjectWatcher {

    @Override
    public void install(Application application) {
        super.install(application);
        application.registerActivityLifecycleCallbacks(new android.app.Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

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
                watch(activity, activity.getClass().getName());
            }
        });
    }
}
