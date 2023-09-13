package com.example.androidfeature.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {
    public int id;
    public String name;

    public Note(int id, String name){
        this.id = id;
        this.name = name;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }


    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
