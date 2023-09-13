// INoteManager.aidl
package com.example.androidfeature;
import com.example.androidfeature.bean.Note;

// Declare any non-default types here with import statements

interface INoteManager {

    Note getNode(int id);
    void addNode(int id, String name);

}