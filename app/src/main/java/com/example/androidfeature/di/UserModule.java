package com.example.androidfeature.di;

import com.example.androidfeature.bean.UserRemoteDataSource;

import dagger.Module;
import dagger.Provides;

@Module
public class UserModule {

    @Provides
    public UserRemoteDataSource provideRemoteDataSource(){
        return new UserRemoteDataSource("ddx", 123);
    }

}
