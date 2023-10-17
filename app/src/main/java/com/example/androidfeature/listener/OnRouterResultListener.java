package com.example.androidfeature.listener;

public interface OnRouterResultListener {
    public void test();
    public default void default_test(){
    }

    public static void static_test() {
    }
}
