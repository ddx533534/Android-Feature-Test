package com.example.androidfeature;


import static com.example.androidfeature.utils.ViewUtils.addButton;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.ddx.kt.ui.activity.InfoActivity;
import com.example.androidfeature.activity.AnimationActivity;
import com.example.androidfeature.activity.BarcodeActivity;
import com.example.androidfeature.activity.BaseActivity;
import com.example.androidfeature.activity.BezierCurveActivity;
import com.example.androidfeature.activity.ClickTestActivity;
import com.example.androidfeature.activity.CurvePathActivity;
import com.example.androidfeature.activity.GcActivity;
import com.example.androidfeature.activity.MarqueeActivity;
import com.example.androidfeature.activity.PicAnimationActivity;
import com.example.androidfeature.activity.RotationActivity;
import com.example.androidfeature.activity.RxJavaActivity;
import com.example.androidfeature.activity.ScrollViewActivity;
import com.example.androidfeature.activity.ServiceActivity;
import com.example.androidfeature.arch.ui.PhotoActivity;
import com.example.androidfeature.bean.Message;
import com.example.androidfeature.bean.UserLocalDataSource;
import com.example.androidfeature.bean.UserRepo;
import com.example.androidfeature.widget.DragVerificationDialog;
import com.example.androidfeature.widget.OnDragAction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {

    private static String TAG = "MainActivity_Tag";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Inject
    UserRepo userRepo;

    @Inject
    UserLocalDataSource userLocalDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ImageView imageView = findViewById(R.id.pic);
        Picasso.get().load("https://p0.meituan.net/travelcube/f78b0d491cb9a68382d4154c1a50b6104090560.jpg").into(imageView);
        initButton();
    }

    class TextSpan {
        String content;
        int[] range;
    }

    private void initButton() {
        addButton(this, "测试中文长度", v -> {
            String test = "[{content:123789aaabbb,range:[0, 12]}, {content:789,range:[3, 6]}\", \"{content:aaa,range:[6, 9]}\", \"{content:bbb,range:[9, 12]}\"]";
//            ArrayList list  = new Gson().fromJson(test,new TypeToken<List<TextSpan>>(){}.getType());
            test = "y̆";
            test = "❤︎";
            test = "한국어";
            test = "Русский язык";
            test = "עִבְרִית";
            test = "㊊";
            test = "اللغة العربية";

            Toast.makeText(this, test + "length：" + test.length() + " chars: " + test.chars().count(), Toast.LENGTH_SHORT).show();
        });
        addButton(this, "测试过滤非 GBK 字符", v -> {
            String test = "[{content:123789aaabbb,range:[0, 12]}, {content:789,range:[3, 6]}\", \"{content:aaa,range:[6, 9]}\", \"{content:bbb,range:[9, 12]}\"]";
//            ArrayList list  = new Gson().fromJson(test,new TypeToken<List<TextSpan>>(){}.getType());
            test = "❤︎ 我爱中国🇨🇳，不爱🇯🇵!";
            test = filterNonGBKChar(test);
            Toast.makeText(this, "【" + test + "】", Toast.LENGTH_SHORT).show();
        });
        addButton(this, "测试旋转动画", AnimationActivity.class);
        addButton(this, "测试拖动验证弹窗", v -> {
            new DragVerificationDialog.Builder(this)
                    .setCancelable(true)
                    .setOnDragAction(new OnDragAction() {
                        @Override
                        public void onDragStart() {
                            Log.d("drag", "onDragStart");
                        }

                        @Override
                        public void onDragSuccess() {
                            Log.d("drag", "onDragSuccess");
                        }

                        @Override
                        public void onDragFail() {
                            Log.d("drag", "onDragFail");
                        }
                    })
                    .show();
        });
        addButton(this, "跳转应用商店", new View.OnClickListener() {
            String appPackageName = "com.sankuai.meituan.takeoutnew";

            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + appPackageName);
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                }
            }
        });
        addButton(this, "跳转浏览器", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = "https://www.baidu.com";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        addButton(this, "跳转App应用商店", new View.OnClickListener() {
            String appPackageName = "com.sankuai.meituan.takeoutnew";

            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("appmarket://details?id=" + appPackageName);
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName);
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goToMarket);
                }
            }
        });
        addButton(this, "启动scrollview 页面", ScrollViewActivity.class);
        addButton(this, "测试二维码展示", BarcodeActivity.class);
        addButton(this, "测试 Dagger 注入", v -> {
            Toast.makeText(this, "UserRepo is null?" + (userRepo == null)
                    + "\n userRepo的信息" + (userRepo.userRemoteDataSource.name), Toast.LENGTH_SHORT).show();
        });
        addButton(this, "获取联系人信息", v -> {
            readContacts();
        });
        addButton(this, "启动绘制页面", CurvePathActivity.class);
        addButton(this, "启动跑马灯页面", MarqueeActivity.class);
        addButton(this, "启动贝塞尔曲线页面", BezierCurveActivity.class);
        addButton(this, "启动点击测试页面", ClickTestActivity.class);
        addButton(this, "启动图片动画测试", PicAnimationActivity.class);
        Intent intent = new Intent("");
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        addButton(this, "启动其他 App 的 Activity", intent);
        addButton(this, "测试旋转 Activity", RotationActivity.class);
        addButton(this, "测试Rxjava", RxJavaActivity.class);
        addButton(this, "测试Binder", ServiceActivity.class);
        addButton(this, "测试内存泄露", GcActivity.class);
        addButton(this, "测试图片展示", PhotoActivity.class);
        addButton(this, "测试个人信息", InfoActivity.class);
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
                @SuppressLint("Range") String displayName = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                //获取联系人手机号
                @SuppressLint("Range") String number = cursor.getString(cursor
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message msg) {

    }

    private CharsetEncoder gbkEncoder = Charset.forName("GBK").newEncoder();

    public String filterNonGBKChar(String input) {
        if (TextUtils.isEmpty(input)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (gbkEncoder.canEncode(c)) {
                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}