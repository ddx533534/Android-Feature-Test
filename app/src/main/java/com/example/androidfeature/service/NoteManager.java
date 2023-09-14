package com.example.androidfeature.service;

import android.os.IBinder;

import com.example.androidfeature.INoteManager;

/**
 * Note 服务管理类，用于绑定、销毁、使用对应的服务
 */
public class NoteManager {

    private static IBinder mBinder = null;

    public static void init(IBinder binder) {
        mBinder = binder;
    }


    public static INoteManager getService() {
        return INoteManager.Stub.asInterface(mBinder);
    }

    public static void release(){
        mBinder = null;
    }

}
