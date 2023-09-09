package com.example.androidfeature;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.androidfeature.Activity.ARouteActivity;
import com.example.androidfeature.Activity.BaseActivity;
import com.example.androidfeature.Activity.BezierCurveActivity;
import com.example.androidfeature.Activity.ClickTestActivity;
import com.example.androidfeature.Activity.CurvePathActivity;
import com.example.androidfeature.Activity.LifeCycleActivity;
import com.example.androidfeature.Activity.MarqueeActivity;
import com.example.androidfeature.Activity.PicAnimationActivity;
import com.example.androidfeature.Activity.RotationActivity;
import com.example.androidfeature.concurrent.ConcurrentTask;
import com.sankuai.android.jarvis.Jarvis;


import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends BaseActivity {
    private ExecutorService executor = Jarvis.newScheduledThreadPool("hhhh", 2);

    private static String TAG = "MainActivity_Tag";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConcurrentTask.init();
        findViewById(R.id.start_dcep).setOnClickListener(v -> {
            Intent intent = new Intent(this, ARouteActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.start_curve_path).setOnClickListener(v -> {
            Intent intent = new Intent(this, CurvePathActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.start_marquee).setOnClickListener(v -> {
            Intent intent = new Intent(this, MarqueeActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.start_bezier).setOnClickListener(v -> {
            Intent intent = new Intent(this, BezierCurveActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.start_asynTask).setOnClickListener(v -> {
            jarvis_schedule_threadPool();
        });

        findViewById(R.id.start_click_test).setOnClickListener(v -> {
            Intent intent = new Intent(this, ClickTestActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.start_picture_animation).setOnClickListener(v -> {
            Intent intent = new Intent(this, PicAnimationActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.start_picture_animation).setOnClickListener(v -> {
            Intent intent = new Intent(this, PicAnimationActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.start_get_contacts).setOnClickListener(v -> {
            readContacts();
        });
        findViewById(R.id.start_other_app).setOnClickListener(v -> {
            Intent intent = new Intent("");
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
        findViewById(R.id.test_intent_size).setOnClickListener(v -> {
            String content = readJsonFile("");
            Log.d("Intenttest", "size:" + content.getBytes().length / 1024.0 + "kb");
            Intent intent = new Intent(this, PicAnimationActivity.class);
            intent.putExtra("json", content.getBytes());
            startActivity(intent);
        });
        findViewById(R.id.test_rotation).setOnClickListener(v -> {
            Intent intent = new Intent(this, RotationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.test_rotation).setOnClickListener(v -> {
            Intent intent = new Intent(this, RotationActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.start_LifeCycleActivity).setOnClickListener(v -> {
            Intent intent = new Intent(this, LifeCycleActivity.class);
            startActivity(intent);
        });

    }

    public String readJsonFile(String fileName) {
        String jsonStr = "";
        try {

            Reader reader = new InputStreamReader(getAssets().open("88KB.json"), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //自己使用的时候不要忘记在AndroidManifest.xml中添加访问联系人数据的权限
    private void readContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            return;
        }
        Cursor cursor = null;
        try {
            //查询联系人数据
            //ContactsContract.CommonDataKinds.Phone类已经封装好了URI，NAME,NUMBER等信息
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            while (cursor.moveToNext()) {
                //获取联系人姓名
                String displayName = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //获取联系人手机号
                String number = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d("ContentResolver", displayName + " " + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                readContacts();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
    private int count = 2;
    ReentrantLock reentrantLock = new ReentrantLock();

    private void testCyclicBarrier() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reentrantLock.lock();
                try {
                    Log.d("CyclicBarrier 测试", "1已经出来拉");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    reentrantLock.unlock();
                }
            }
        };

        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                reentrantLock.lock();
                try {
                    Thread.sleep(4000);
                    Log.d("CyclicBarrier 测试", "2已经出来拉");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    reentrantLock.unlock();
                }
            }
        };


        new Thread(runnable).start();
        new Thread(runnable1).start();
    }

    private void jarvis_schedule_threadPool() {
        Future<?> task = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Log.d(TAG, "doInBackground 任务执行，线程:" + Thread.currentThread().getName());
                Log.d(TAG, "doInBackground 正在休眠，线程：" + Thread.currentThread().getName());

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Log.d(TAG, "中断~~~");
                    return 0;
                }
                Log.d(TAG, "doInBackground 任务结束，线程：" + Thread.currentThread().getName());
                return 1;
            }
        });

        try {
            task.get(2000, TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            Log.d(TAG, "ExecutionException" + e.getMessage() + e.getCause());
        } catch (InterruptedException e) {
            Log.d(TAG, "InterruptedException" + e.getMessage() + e.getCause());
        } catch (TimeoutException e) {
            Log.d(TAG, "TimeoutException" + e.getMessage() + e.getCause());
        }


    }

    private void handler_concurrentTask() {
        ConcurrentTask<Void, Void, Integer> concurrentTask = new ConcurrentTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                Log.d(TAG, "doInBackground 执行，线程:" + Thread.currentThread().getName());
                Log.d(TAG, "doInBackground 正在休眠，线程：" + Thread.currentThread().getName());

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return 0;
                }
                Log.d(TAG, "doInBackground 休眠结束，线程：" + Thread.currentThread().getName());
                return 1;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                Log.d(TAG, "onPostExecute收到结果" + integer);
                Log.d(TAG, "onPostExecute，线程:" + Thread.currentThread().getName());
            }
        };
        concurrentTask.exe();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (concurrentTask.getStatus().ordinal() == 0 || concurrentTask.getStatus().ordinal() == 1) {
                    Log.d(TAG, "testTask 终止");
                    concurrentTask.cancel(true);
                }
            }
        }, 2000);
    }

    class TestTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground 执行，线程:" + Thread.currentThread().getName());
            Log.d(TAG, "doInBackground 正在休眠，线程：" + Thread.currentThread().getName());

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return 0;
            }
            Log.d(TAG, "doInBackground 休眠结束，线程：" + Thread.currentThread().getName());
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.d(TAG, "onPostExecute收到结果" + integer);
            Log.d(TAG, "onPostExecute，线程:" + Thread.currentThread().getName());
        }
    }


    @Override
    protected String getActivityName() {
        return "MainActivity";
    }
}