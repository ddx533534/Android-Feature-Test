package com.example.androidfeature.concurrent;

import android.support.annotation.UiThread;

import com.sankuai.android.jarvis.Jarvis;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * copy from aimeituan
 * Created by liuqi on 15-7-3.
 */
public abstract class ConcurrentTask<Params, Progress, Result> extends ModernAsyncTask<Params, Progress, Result> {
    private static final int CORE_POOL_SIZE = 2;
    private static final int MAXIMUM_POOL_SIZE = 4;
    private static final int KEEP_ALIVE = 1;

    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<>();

    private static final Executor THREAD_POOL_EXECUTOR = Jarvis.newThreadPoolExecutor("Paybase-ConcurrentTask", CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue);
    private Executor executor;
    protected ConcurrentTask() {
        this(THREAD_POOL_EXECUTOR);
    }

    protected ConcurrentTask(Executor executor) {
        this.executor = executor;
    }

    public void exe(Params... params) {
        executeOnExecutor(executor, params);
    }

    /**
     * 1.ModernAsyncTask 类中的 sHandler 必须在拥有 Looper 的线程中创建。
     * 2.sHandler 是 static final 修饰，因此只需创建一次即可。
     * 3.ConcurrentTask 在使用时，并不一定是在 UI 线程中，也可能在子线程中（该使用方式不正确）。
     * 4.因此为了避免 sHandler 初始化失败，需要提前在 UI 线程中初始化 sHandler。
     */
    @UiThread
    public static void init() {
        ModernAsyncTask.init();
    }
}
