package com.example.androidfeature.leakmemory.watcher;


import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.example.androidfeature.Application;

import org.jetbrains.annotations.NotNull;

import curtains.Curtains;
import curtains.OnRootViewAddedListener;
import curtains.WindowType;
import curtains.WindowsKt;

import static com.example.androidfeature.provider.LeakMemoryProvider.TAG;

/**
 * RootView 内存泄露监控者，监控Activity 和 fragment 之外显式UI 视图的内存泄露，比如 dialog 等
 */

public class RootViewWatcher implements InstallWatcher {
    private final OnRootViewAddedListener listener;

    public RootViewWatcher(ObjectWatcher objectWatcher) {
        listener = new curtains.OnRootViewAddedListener() {
            @Override
            public void onRootViewsChanged(@NotNull View view, boolean added) {
                if(added){
                    onRootViewAdded(view);
                }
            }

            @Override
            public void onRootViewAdded(@NotNull View view) {
                WindowType windowType = WindowsKt.getWindowType(view);
                boolean shouldWatch = false;
                String rootViewType = windowType.toString();
                switch (windowType) {
                    case PHONE_WINDOW:
                        Window.Callback callback;
                        if (WindowsKt.getPhoneWindow(view) != null
                                && WindowsKt.getPhoneWindow(view).getCallback() != null
                                && (callback = WindowsKt.getWrappedCallback(WindowsKt.getPhoneWindow(view).getCallback())) != null) {
                            if (callback instanceof Dialog) {
                                rootViewType = "dialog";
                                shouldWatch = true;
                            }
                        }
                        break;
                    case TOAST:
                    case TOOLTIP:
                    case UNKNOWN:
                        shouldWatch = true;
                        break;
                    case POPUP_WINDOW:
                        shouldWatch = false;
                        break;
                }
                if (shouldWatch) {
                    String name = rootViewType;
                    view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        private Runnable r = () -> objectWatcher.watch(view, name);
                        private Handler handler = new Handler(Looper.getMainLooper());

                        @Override
                        public void onViewAttachedToWindow(View v) {
                            handler.removeCallbacksAndMessages(r);
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            handler.post(r);
                        }
                    });
                }
            }
        };
    }

    @Override
    public void install() {
        Curtains.getOnRootViewsChangedListeners().add(listener);
    }

    @Override
    public void uninstall() {
        Curtains.getOnRootViewsChangedListeners().remove(listener);
    }
}
