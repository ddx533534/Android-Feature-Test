package com.example.androidfeature.listener;

public interface OnRouterResultListener {
    String name ="ddx";
    public void test();
    public default void default_test(){
    }

    public static void static_test() {
    }
}

class test implements OnRouterResultListener{

    @Override
    public void test() {
    }
}
