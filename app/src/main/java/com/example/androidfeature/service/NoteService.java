package com.example.androidfeature.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.androidfeature.INoteManager;
import com.example.androidfeature.bean.Note;

import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.androidfeature.utils.OSUtils.logCurrentProcess;
import static com.example.androidfeature.utils.OSUtils.logCurrentThread;

/**
 * note笔记服务，运行在独立的进程中
 *
 * @author dudongxu
 */
public class NoteService extends Service {

    private CopyOnWriteArrayList<Note> notes = new CopyOnWriteArrayList<>();
    public Binder mBinder = new INoteManager.Stub() {
        @Override
        public Note getNode(int id) throws RemoteException {
            logCurrentProcess(NoteService.this, "NoteService");
            logCurrentThread("NoteService");
            if (notes == null || notes.size() == 0) {
                return null;
            }
            for (Note n : notes) {
                if (n.id == id) {
                    return n;
                }
            }
            return null;
        }

        @Override
        public void addNode(int id, String name) {
            logCurrentProcess(NoteService.this, "NoteService");
            logCurrentThread("NoteService");
            if (notes == null || notes.size() == 0) {
                return;
            }
            notes.add(new Note(id, name));
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        notes.add(new Note(1, "ddx"));
        notes.add(new Note(2, "ccc"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
