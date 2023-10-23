package com.example.androidfeature.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.androidfeature.R;
import com.example.androidfeature.bean.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxJavaActivity extends BaseActivity {

    private static final String TAG = "Rxjava-Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_cycle);
        findViewById(R.id.start_new_activity2).setOnClickListener(v -> {
            Intent intent = new Intent(this, LifeCycleActivity2.class);
            startActivity(intent);
        });
        findViewById(R.id.start_already_activity2).setOnClickListener(v -> {
            Intent intent = new Intent(this, LifeCycleActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
        findViewById(R.id.start_multitask_activity2).setOnClickListener(v -> {
            Intent intent = new Intent(this, LifeCycleActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            startActivity(intent);
        });
        Observable customerA = Observable.create(new ObservableOnSubscribe<Order>() {
            // 1-1
            @Override
            public void subscribe(@NonNull ObservableEmitter<Order> emitter) throws Throwable {
                logWithThread("jack：点餐");
                emitter.onNext(new Order(Arrays.asList(1, 2), "jack"));
                emitter.onComplete();
            }
        });

        Observable customerB = Observable.create(new ObservableOnSubscribe<Order>() {
            // 1-1
            @Override
            public void subscribe(@NonNull ObservableEmitter<Order> emitter) throws Throwable {
                logWithThread("lucy：点餐");
                emitter.onNext(new Order(Arrays.asList(2, 3), "lucy"));
                emitter.onComplete();
            }
        });
        Observer<Order> restaurant = new Observer<Order>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                logWithThread("restaurant：有顾客开始点单啦 ");
            }

            @Override
            public void onNext(Order order) {
                logWithThread("restaurant：收到菜单，来自：" + order.order_owner);
                for (int foodId : order.foodIds) {
                    switch (foodId) {
                        case 1:
                            logWithThread("restaurant：" + order.order_owner + "，点餐 1， 正在制作");
                            break;
                        case 2:
                            logWithThread("restaurant：" + order.order_owner + "，点餐 2， 正在制作");
                            break;
                        case 3:
                            logWithThread("restaurant：" + order.order_owner + "，点餐 3， 正在制作");
                            break;
                        case 4:
                            logWithThread("restaurant：" + order.order_owner + "，点餐 4， 正在制作");
                            break;
                        default:
                            onError(new Exception(order.order_owner + "点的菜不存在!"));
                            break;
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                logWithThread("restaurant：" + t.getMessage());

            }

            @Override
            public void onComplete() {
                logWithThread("restaurant：制作完毕!");
            }
        };

        submitOrder(customerA, restaurant);
        submitOrder(customerB, restaurant);
        RxJavaPlugins.setOnObservableAssembly(new Function<Observable, Observable>() {
            @Override
            public Observable apply(Observable observable) throws Throwable {
                Log.d(TAG,"Hook create 方法");
                return observable;
            }
        });
    }


    // 提交订单
    private void submitOrder(Observable customer, Observer restaurant) {
        customer.filter(new Predicate() {
                    @Override
                    public boolean test(Object o) throws Throwable {
                        Order order = (Order) o;
                        boolean result = order.foodIds != null && order.foodIds.size() > 0
                                && order.order_owner != null && order.order_owner.length() > 0;
                        logWithThread(order.order_owner + "订单信息" + (result ? "合理" : "不合理"));
                        return result;
                    }
                })
                .map(new Function<Order, Order>() {
                    @Override
                    public Order apply(Order o) throws Throwable {
                        Order order = (Order) o;
                        List<Integer> foodIds = new ArrayList<>(order.foodIds); // 转换为可修改的集合类型
                        Iterator<Integer> iterator = foodIds.iterator();
                        while (iterator.hasNext()) {
                            // 信息处理，去除餐品号为3，因为餐品不供应了~
                            int id = iterator.next();
                            if (id == 3) {
                                iterator.remove();
                            }
                        }
                        logWithThread(order.order_owner + "订单信息" + foodIds);
                        order.foodIds = foodIds;
                        return order;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.single())
                .subscribe(restaurant);
    }

    public static void log(String msg) {
        Log.d(TAG, msg);
    }

    public static void logWithThread(String msg) {
        String name = Thread.currentThread().getName();
        Log.d(TAG, msg + "-所在线程" + name);
    }
}