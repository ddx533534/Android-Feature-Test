package com.example.androidfeature.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfeature.Application;
import com.example.androidfeature.R;
import com.example.androidfeature.fragment.GcFragment;
import com.example.androidfeature.leakmemory.LeakMemoryTester;
import com.example.androidfeature.listener.OnRouterResultListener;

import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;

import retrofit2.http.Tag;

import static com.example.androidfeature.provider.LeakMemoryProvider.TAG;

public class GcActivity extends BaseActivity {

    private GcFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gc);
        fragment = GcFragment.newInstance("123", "321");
        getFragmentManager().beginTransaction().add(R.id.content, fragment).commit();

        findViewById(R.id.show_toast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Hello, I am toast!", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                new TextView(v.getContext());
                TextView textView = new TextView(v.getContext());
                textView.setText("!2321312");
                dialog.setTitle("Dialog");
                dialog.setContentView(textView);
                dialog.setOnDismissListener(DialogInterface::dismiss);
                dialog.show();
                LeakMemoryTester.TestLeakMemoryDialog = dialog;
            }
        });
        try {
            Class cl = Class.forName("com.example.androidfeature.listener.OnRouterResultListener");
            Method[] methods = cl.getDeclaredMethods();
            for (Method method : methods) {
                Log.d(TAG, method.getName() + "is defaultMethod?" + method.isDefault());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}