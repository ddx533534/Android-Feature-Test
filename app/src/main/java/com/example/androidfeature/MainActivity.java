package com.example.androidfeature;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.androidfeature.activity.ARouteActivity;
import com.example.androidfeature.activity.BaseActivity;
import com.example.androidfeature.activity.BezierCurveActivity;
import com.example.androidfeature.activity.ClickTestActivity;
import com.example.androidfeature.activity.CurvePathActivity;
import com.example.androidfeature.activity.GcActivity;
import com.example.androidfeature.activity.RxJavaActivity;
import com.example.androidfeature.activity.MarqueeActivity;
import com.example.androidfeature.activity.PicAnimationActivity;
import com.example.androidfeature.activity.RotationActivity;
import com.example.androidfeature.activity.ServiceActivity;
import com.example.androidfeature.bean.ApplicationGraph;
import com.example.androidfeature.bean.DaggerApplicationGraph;
import com.example.androidfeature.bean.UserRepo;
import com.squareup.picasso.Picasso;

import java.io.InputStreamReader;
import java.io.Reader;

public class MainActivity extends BaseActivity {

    private static String TAG = "MainActivity_Tag";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.pic);
        Picasso.get().load("https://p0.meituan.net/travelcube/f78b0d491cb9a68382d4154c1a50b6104090560.jpg").into(imageView);
        Picasso.get().load("https://p0.meituan.net/travelcube/f78b0d491cb9a68382d4154c1a50b6104090560.jpg").into(imageView);
        ApplicationGraph applicationGraph = DaggerApplicationGraph.create();
        UserRepo userRepo = applicationGraph.userRepo();
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
            Intent intent = new Intent(this, RxJavaActivity.class);
            startActivity(intent);
        });
        addButton("测试Binder", ServiceActivity.class);
        addButton("测试内存泄露", GcActivity.class);

    }

    private void addButton(String name, Class<?> cla) {
        ViewGroup viewGroup = findViewById(R.id.base_content);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button button = new Button(this);
        button.setText(name);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, cla);
            startActivity(intent);
        });
        viewGroup.addView(button, layoutParams);
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
}