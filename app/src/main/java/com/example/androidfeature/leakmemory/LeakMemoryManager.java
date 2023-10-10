package com.example.androidfeature.leakmemory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.androidfeature.Application;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executor;

import static com.example.androidfeature.provider.LeakMemoryProvider.TAG;


public class LeakMemoryManager {
    private static final LeakMemoryManager instance;
    private HashMap<String, LMWeakReference<?>> hashMap;
    private final ReferenceQueue<?> referenceQueue;
    private Handler mainHandler;
    private final Executor executor;
    private static final long DelayedTime = 5000L;

    private Handler backgroundHandler;

    static {
        instance = new LeakMemoryManager();
    }

    private LeakMemoryManager() {
        referenceQueue = new ReferenceQueue<>();
        hashMap = new HashMap<>();
        executor = command -> {
            if (mainHandler != null) {
                mainHandler.postDelayed(command, DelayedTime);
            }
        };
    }


    public static void installManager(@NonNull Application application) {
        instance.initGcTask(application);
        instance.registerActivityWatcher(application);

    }

    private void initGcTask(@NonNull Application application) {
        HandlerThread handlerThread = new HandlerThread("GC-Task");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());
    }

    private void registerActivityWatcher(@NonNull Application application) {
        mainHandler = new Handler(application.getMainLooper());
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
                clearReferenceQueue();
                String activity_name = activity.getClass().getName();
                String key = UUID.randomUUID().toString();
                LMWeakReference<?> w = new LMWeakReference<>(key, activity, referenceQueue);
                hashMap.put(key, w);
                executor.execute(() -> {
                    Log.d(TAG, "当前线程：" + Thread.currentThread().getName() + "执行内存泄露检测");
                    // 立即运行一次 GC，强制将弱可达对象假如到队列中
                    runGc();
                    LMWeakReference<?> w1 = hashMap.get(key);
                    if (w1 != null && w1.get() != null) {
                        LMWeakReference<?> w2 = hashMap.get(key);
                        if (w2 != null && w2.get() != null) {
                            Log.e(TAG, activity_name + "已经泄露!");
                        } else {
                            Log.e(TAG, activity_name + "正常释放!");
                        }
                    } else {
                        Log.e(TAG, activity_name + "正常释放!");
                    }
                });
            }
        });
    }

    /**
     * 清除弱引用队列中的对象，注意有些对象可能无法及时变得弱可达。
     */
    private void clearReferenceQueue() {
        LMWeakReference<?> weakReference = null;
        do {
            weakReference = (LMWeakReference<?>) referenceQueue.poll();
            // weakReference.get 为空，说明已经被回收，否则说明并没有回收
            if (weakReference != null && weakReference.get() == null) {
                hashMap.remove(weakReference.key);
            }
        } while (weakReference != null);
    }

    /**
     * 后台 GC 线程，执行 GC 任务，将弱可达对象强制假如到队列中
     */
    private void runGc() {
        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                Runtime.getRuntime()
                        .gc();
                Log.d(TAG, "当前线程：" + Thread.currentThread().getName() + "正在休眠");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.runFinalization();
            }
        });

    }

    private static class LMWeakReference<T> extends WeakReference<T> {

        public String key;

        public LMWeakReference(String key, T referent, ReferenceQueue q) {
            super(referent, q);
            this.key = key;
        }
    }
}
