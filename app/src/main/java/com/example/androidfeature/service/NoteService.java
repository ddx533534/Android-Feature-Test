package com.example.androidfeature.service;

import android.app.Service;
import android.content.Intent;
import android.database.Observable;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.androidfeature.INoteManager;
import com.example.androidfeature.bean.Note;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class NoteService extends Service {

    private CopyOnWriteArrayList<Note> notes = new CopyOnWriteArrayList<>();
    private Binder mBinder = new INoteManager.Stub() {
        @Override
        public Note getNode(int id) throws RemoteException {
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
        public void addNode(int id, String name) throws RemoteException {
            if (notes == null || notes.size() == 0) {
                return;
            }
            notes.add(new Note(id, name));
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
