package com.example.androidfeature.bean;

import dagger.Component;

@Component
public interface ApplicationGraph {
    UserRepo userRepo();
}
