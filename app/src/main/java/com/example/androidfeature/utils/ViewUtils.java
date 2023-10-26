package com.example.androidfeature.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.androidfeature.MainActivity;
import com.example.androidfeature.R;

public class ViewUtils {
    /**
     * 添加按钮，并设置回调
     *
     * @param name
     * @param onClickListener
     */
    public static void addButton(Activity activity, String name, View.OnClickListener onClickListener) {
        ViewGroup viewGroup = activity.findViewById(R.id.base_content);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button button = new Button(activity);
        button.setText(name);
        button.setOnClickListener(onClickListener);
        viewGroup.addView(button, layoutParams);
    }


    /**
     * 隐式启动 显式启动 Activity
     *
     * @param name
     * @param cla
     */
    public static void addButton(Activity activity, String name, Class<?> cla) {
        ViewGroup viewGroup = activity.findViewById(R.id.base_content);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button button = new Button(activity);
        button.setText(name);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(activity, cla);
            activity.startActivity(intent);
        });
        viewGroup.addView(button, layoutParams);
    }

    /**
     * 隐式启动 Activity
     *
     * @param name
     * @param intent
     */
    public static void addButton(Activity activity, String name, Intent intent) {
        ViewGroup viewGroup = activity.findViewById(R.id.base_content);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button button = new Button(activity);
        button.setText(name);
        button.setOnClickListener(v -> {
            activity.startActivity(intent);
        });
        viewGroup.addView(button, layoutParams);
    }
}
