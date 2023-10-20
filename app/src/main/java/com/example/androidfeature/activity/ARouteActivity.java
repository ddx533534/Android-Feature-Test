package com.example.androidfeature.activity;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.androidfeature.R;
import com.example.androidfeature.bean.Note;
import com.example.androidfeature.network.DemoRetrofit;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ARouteActivity extends Activity {

    private static final String TAG = "Rxjava-Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

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

                DemoRetrofit.getRetrofit().post(null).enqueue(new Callback<Note>() {
                    @Override
                    public void onResponse(Call<Note> call, Response<Note> response) {
                        if (response.body() == null) {
                            emitter.onError(new Exception("body 为空"));
                        } else {
                            emitter.onNext(response.body().id);
                        }
                        emitter.onComplete();
                    }

                    @Override
                    public void onFailure(Call<Note> call, Throwable t) {
                        emitter.onError(t);
                        emitter.onComplete();
                    }
                });
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
        observable.filter(new Predicate() {
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
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Throwable {
                        String name = Thread.currentThread().getName();
                        Log.d(TAG, "让我们看看谁结束了？" + " - " + name);
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
                        // 顾客点餐统一加10
                        return (Integer) o - 10;
                    }
                })
                .observeOn(Schedulers.newThread())
                .subscribe(observer);

    }
}