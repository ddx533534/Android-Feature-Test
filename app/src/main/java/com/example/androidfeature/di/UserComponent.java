package com.example.androidfeature.di;

import android.app.Activity;

import com.example.androidfeature.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {UserModule.class})
public interface UserComponent {

    // 注入的目标类型必须是确定的！不能是其父类或者子类
    void inject(MainActivity mainActivity);
}
