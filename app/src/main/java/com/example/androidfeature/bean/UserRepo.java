package com.example.androidfeature.bean;

import javax.inject.Inject;

public class UserRepo {
    private UserRemoteDataSource userRemoteDataSource;
    private UserLocalDataSource userLocalDataSource;

    @Inject
    public UserRepo(UserLocalDataSource localDataSource, UserRemoteDataSource remoteDataSource){
        this.userLocalDataSource = localDataSource;
        this.userRemoteDataSource = remoteDataSource;

    }
}
