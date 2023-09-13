package com.example.androidfeature.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfeature.INoteManager;
import com.example.androidfeature.R;
import com.example.androidfeature.bean.Note;
import com.example.androidfeature.service.NoteManager;
import com.example.androidfeature.service.NoteService;

public class ServiceActivity extends BaseActivity {

    private Note note = null;
    private IBinder mBinder = null;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            INoteManager noteManager = INoteManager.Stub.asInterface(service);
            try {
                mBinder = service;
                note = noteManager.getNode(1);
                refresh();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private void refresh() {
        TextView textView = findViewById(R.id.note);
        textView.setText(note.name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        findViewById(R.id.bind_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, NoteService.class);
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.add_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NoteManager.getService().addNode(3, "dddd");
//                    INoteManager.Stub.asInterface(mBinder).addNode(3, "dddd");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.get_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(((EditText) findViewById(R.id.input_id)).getText().toString());
                try {
//                    String name = INoteManager.Stub.asInterface(mBinder).getNode(id).name;
                    String name = NoteManager.getService().getNode(id).name;
                    Toast.makeText(ServiceActivity.this, "name:" + name, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}