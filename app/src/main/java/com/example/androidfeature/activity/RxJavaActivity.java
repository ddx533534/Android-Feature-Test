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

    private void testRxjava(){

        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                String name = Thread.currentThread().getName();
                // 点餐 - 餐品1
                Log.d(TAG, "Observable：点餐1" + " - " + name);
                emitter.onNext(1);
                // 点餐 - 餐品2
                Log.d(TAG, "Observable：点餐2" + " - " + name);
                emitter.onNext(2);
                Log.d(TAG, "Observable：点餐3" + " - " + name);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                String name = Thread.currentThread().getName();
                Log.d(TAG, "Observer：onSubscribe -" + name);

            }

            @Override
            public void onNext(Integer integer) {
                String name = Thread.currentThread().getName();
                Log.d(TAG, "Observer：收到顾客点餐动作:" + integer + " - " + name);
                switch (integer) {
                    case 1:
                        Log.d(TAG, "Observer：顾客点餐1，正在制作" + " - " + name);
                        break;
                    case 2:
                        Log.d(TAG, "Observer：顾客点餐2，正在制作" + " - " + name);
                        break;
                    default:
                        Log.d(TAG, "Observer：顾客点餐未知？" + " - " + name);
                        break;
                }
            }

            @Override
            public void onError(Throwable t) {
                String name = Thread.currentThread().getName();
                Log.d(TAG, "Observer：顾客点餐出错!" + " - " + name);

            }

            @Override
            public void onComplete() {
                String name = Thread.currentThread().getName();
                Log.d(TAG, "Observer：顾客点餐完毕!" + " - " + name);
            }
        };
        observable.subscribeOn(Schedulers.io())
                .filter(new Predicate() {
                    @Override
                    public boolean test(Object o) throws Throwable {
                        String name = Thread.currentThread().getName();
                        int order = (int) o;
                        // 筛选顾客的点单号，不满足剔除哦
                        boolean result = order > 0 && order < 3;
                        Log.d(TAG, "Observable：顾客点餐号检查：" + order + (result ? "符合" : "不符合") + name);
                        return result;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .map(new Function() {
                    @Override
                    public Object apply(Object o) throws Throwable {
                        String name = Thread.currentThread().getName();
                        Log.d(TAG, "Observer：顾客点餐号 + 10" + " - " + name);
                        // 顾客点餐统一加10
                        return (Integer) o + 10;
                    }
                })
                .observeOn(Schedulers.newThread())
                .map(new Function() {
                    @Override
                    public Object apply(Object o) throws Throwable {
                        String name = Thread.currentThread().getName();
                        Log.d(TAG, "Observer：顾客点餐号 - 10" + " - " + name);
                        // 顾客点餐统一减10
                        return (Integer) o - 10;
                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribe(observer);
    }
}