package com.example.androidfeature.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfeature.R;
import com.example.androidfeature.service.NoteManager;
import com.example.androidfeature.service.NoteService;

import static com.example.androidfeature.utils.OSUtils.logCurrentProcess;
import static com.example.androidfeature.utils.OSUtils.logCurrentThread;

public class ServiceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        findViewById(R.id.bind_service).setOnClickListener(v -> {
        });

        findViewById(R.id.add_note).setOnClickListener(v -> {
            logCurrentProcess(this,"ServiceActivity");
            logCurrentThread("ServiceActivity");
            try {
                NoteManager.getService().addNode(3, "dddd");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        findViewById(R.id.get_note).setOnClickListener(v -> {
            logCurrentProcess(this,"ServiceActivity");
            logCurrentThread("ServiceActivity");
            try {
                int id = Integer.parseInt(((EditText) findViewById(R.id.input_id)).getText().toString());
                String name = NoteManager.getService().getNode(id).name;
                Toast.makeText(ServiceActivity.this, "name:" + name, Toast.LENGTH_SHORT).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}