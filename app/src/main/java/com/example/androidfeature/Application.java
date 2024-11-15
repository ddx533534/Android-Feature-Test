package com.example.androidfeature;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

//import com.ddx.kt.datamodel.UserDataBase;
import com.example.androidfeature.service.NoteManager;
import com.example.androidfeature.service.NoteService;

import static com.example.androidfeature.utils.Constants.NOTE_MANAGER_TAG;

public class Application extends android.app.Application {

    private static ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(NOTE_MANAGER_TAG, "onServiceConnected");
            NoteManager.init(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(NOTE_MANAGER_TAG, "onServiceDisconnected");
            NoteManager.release();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, NoteService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
//        UserDataBase.Companion.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unbindService(serviceConnection);
    }
}
