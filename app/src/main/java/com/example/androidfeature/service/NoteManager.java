package com.example.androidfeature.service;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.androidfeature.INoteManager;
import com.example.androidfeature.activity.ServiceActivity;
import com.example.androidfeature.bean.Note;

import static android.content.Context.BIND_AUTO_CREATE;

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
