package com.example.androidfeature.utils;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.androidfeature.utils.Constants.OS_TAG;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class OSUtils {

    /**
     * 输出当前上下文所在的进程信息
     *
     * @param context
     */
    public static void logCurrentProcess(Context context, String extra) {
        int pid = android.os.Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) getSystemService(context, ActivityManager.class);
        List<ActivityManager.RunningAppProcessInfo> processInfos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.pid == pid) {
                processName = processInfo.processName;
                break;
            }
        }
        Log.d(OS_TAG, extra + " Process Info -  name: " + processName + ", pid: " + pid);
    }

    /**
     * 输出当前上下文所在的线程信息
     */
    public static void logCurrentThread(String extra) {
        Thread thread = Thread.currentThread();
        String threadName = thread.getName();
        long threadId = thread.getId();
        Log.d(OS_TAG, extra + " Thread  Info - name: " + threadName + ", threadId: " + threadId);

    }
}
