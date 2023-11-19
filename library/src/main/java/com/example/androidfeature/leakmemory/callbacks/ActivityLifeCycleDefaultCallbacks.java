package com.example.androidfeature.leakmemory.callbacks;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * ActivityLifecycleCallbacks默认实现类，按需重写对应的方法
 */
public abstract class ActivityLifeCycleDefaultCallbacks implements Application.ActivityLifecycleCallbacks {

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

    }
}
