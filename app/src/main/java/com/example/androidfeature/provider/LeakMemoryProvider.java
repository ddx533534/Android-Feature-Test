package com.example.androidfeature.provider;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.androidfeature.Application;
import com.example.androidfeature.leakmemory.LeakMemoryManager;

public class LeakMemoryProvider extends ContentProvider {
    public static String TAG = "LeakMemoryProvider";

    public LeakMemoryProvider() {
    }


    @Override
    public boolean onCreate() {
        Context context = getContext();
        if (context == null) {
            Log.e(TAG, "context is null");
            return false;
        }
        Application application = (Application) context.getApplicationContext();
        if (application == null) {
            Log.e(TAG, "application is null");
            return false;
        }
        LeakMemoryManager.installManager(application);
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }
}