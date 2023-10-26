package com.example.androidfeature.bean;

import javax.inject.Inject;


public class UserRepo {

    public UserRemoteDataSource userRemoteDataSource;

    public UserLocalDataSource userLocalDataSource;


    @Inject
    public UserRepo(UserLocalDataSource userLocalDataSource, UserRemoteDataSource userRemoteDataSource) {
        this.userLocalDataSource = userLocalDataSource;
        this.userRemoteDataSource = userRemoteDataSource;
    }
}
