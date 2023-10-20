package com.example.androidfeature.network;

import com.example.androidfeature.bean.Note;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.POST;

public interface RequestService {
    @POST
    Call<Note> post(Map<String,String> map);
}
