package com.example.androidfeature.leakmemory.watcher;

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

public class ObjectWatcher {
    private static final long DelayedTime = 5000L;
    private static final long GCInterval = 3000L;

    private HashMap<String, LMWeakReference<?>> hashMap;
    private final ReferenceQueue<?> referenceQueue;

    // 主线程-监测内存泄露
    private final Executor executor;
    // 主线程-handler
    private Handler mainHandler;
    // 后台GC线程
    private Handler backgroundHandler;

    public ObjectWatcher() {
        hashMap = new HashMap<>();
        referenceQueue = new ReferenceQueue<>();
        executor = command -> {
            if (mainHandler != null) {
                mainHandler.postDelayed(command, DelayedTime);
            }
        };
        HandlerThread handlerThread = new HandlerThread("GC-Task");
        handlerThread.start();
        backgroundHandler = new Handler(handlerThread.getLooper());

    }

    protected void install(@NonNull Application application) {
        mainHandler = new Handler(application.getMainLooper());
    }


    protected void watch(Object object, String name) {
        clearReferenceQueue();
        String key = UUID.randomUUID().toString();
        LMWeakReference<?> w = new LMWeakReference<>(key, object, referenceQueue);
        hashMap.put(key, w);
        runGc();
        executor.execute(() -> {
            Log.d(TAG, "当前线程：" + Thread.currentThread().getName() + "执行内存泄露检测");
            LMWeakReference<?> w1 = hashMap.get(key);
            if (w1 != null && w1.get() != null) {
                LMWeakReference<?> w2 = hashMap.get(key);
                if (w2 != null && w2.get() != null) {
                    Log.e(TAG, name + "已经泄露!");
                } else {
                    Log.e(TAG, name + "正常释放!");
                }
            } else {
                Log.e(TAG, name + "正常释放!");
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
     * 后台 GC 线程，延迟执行 GC 任务，将弱可达对象强制加入到队列中
     */
    private void runGc() {
        backgroundHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                enforcedGC();
            }
        }, GCInterval);

    }

    /**
     * 强制GC
     */
    private void enforcedGC() {
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

    private static class LMWeakReference<T> extends WeakReference<T> {

        public String key;

        public LMWeakReference(String key, T referent, ReferenceQueue q) {
            super(referent, q);
            this.key = key;
        }
    }
}
