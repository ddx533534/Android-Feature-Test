package com.example.androidfeature.bean;

public class Message {
    public enum MsgType{
        SHOW_DIALOG,
        CLOSE_DIALOG;
    }
    MsgType type;

    public Message(MsgType type){
        this.type = type;
    }
}
