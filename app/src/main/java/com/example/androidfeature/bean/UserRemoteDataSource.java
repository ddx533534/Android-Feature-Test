package com.example.androidfeature.bean;

import javax.inject.Inject;

public class UserRemoteDataSource {

    public String name;
    public int id;

    public UserRemoteDataSource(String  name , int id){
        this.name = name;
        this.id = id;
    }
}
