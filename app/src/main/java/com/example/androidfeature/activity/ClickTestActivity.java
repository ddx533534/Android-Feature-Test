package com.example.androidfeature.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.BaseMovementMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

import com.google.gson.Gson;

import com.example.androidfeature.R;
import com.google.gson.JsonObject;

public class ClickTestActivity extends BaseActivity {

    String TAG = "ClickTestActivityTag";

//    private String html = "<p><span style=\"color:#f40e0e\"><em><span style=\"text-decoration:line-through\"><u>detail</u></span></em></span></p><p><span style=\"color:#f40e0e\"><span style=\"color:#000000\"><strong>123</strong></span></span></p>";

    private String html = "<html><body><span style=\"color:black\">In the name of <span style=\"color:blue\"><b> God</b></span>the compassionate, the merciful</span>    " +
            "<img style=\"width: 100; height: 100; \" src=\"https://www.toopic.cn/public/uploads/small/165804440782616580444073.jpg\"/>  " +
            "<a href=\"https://www.toopic.cn/public/uploads/small/165804440782616580444073.jpg\">hello naruto!</a>" +
            "</body><html>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_test);
        TextView textView = findViewById(R.id.text);
        TextView htmlTextView = findViewById(R.id.html_text);
        String outer = "outer";
        String inner = "inner";
        SpannableString spannableString = new SpannableString(outer + inner);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
                ds.bgColor = 0x00FFFFFF;
            }

            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(widget.getContext(), "you click the outer text", Toast.LENGTH_SHORT).show();
            }
        }, 0, outer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.GREEN), 0, outer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(60), 0, outer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
                ds.bgColor = 0x00FFFFFF;
            }

            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(widget.getContext(), "you click the inner text", Toast.LENGTH_SHORT).show();
            }
        }, outer.length(), inner.length() + outer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), outer.length(), inner.length() + outer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(60), outer.length(), inner.length() + outer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 给整体设置一个点击 span ，看起来没有用
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
                ds.bgColor = 0x00FFFFFF;
            }

            @Override
            public void onClick(@NonNull View widget) {
                Toast.makeText(widget.getContext(), "you click the full text", Toast.LENGTH_SHORT).show();
            }
        }, 0, inner.length() + outer.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        String text = "Your text here";
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(text);

        // 设置大范围的 span
        ForegroundColorSpan colorSpanSmall = new ForegroundColorSpan(Color.RED);
        spannableBuilder.setSpan(colorSpanSmall, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置小范围的 span
        ForegroundColorSpan colorSpanLarge = new ForegroundColorSpan(Color.BLUE);
        spannableBuilder.setSpan(colorSpanLarge, 3, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 将 SpannableStringBuilder 设置到 TextView
        textView.setText(spannableBuilder);
//        textView.setText(spannableString);
//        textView.setHighlightColor(0x00FFFFFF);
//        textView.setMovementMethod(LinkMovementMethod.getInstance());
//        String result = "{\"status\":0, \"code\":-1, \"data\":\"\"}";
//        JsonObject jsonResult = new Gson().fromJson(result, JsonObject.class);

        SpannableString spannableString1 = new SpannableString("添加招商银行卡支付立减添加招商银行卡支付立减添加招商银行卡支付立减 left 元添加招商银行卡支付立减 right 元添加招商银行卡支付立减 center 元添加招商银行卡支付立减 start 元添加招商银行卡支付立减 end 元");
        spannableString1.setSpan(new ForegroundColorSpan(-16777216),0,40,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE | (2 << Spanned.SPAN_PRIORITY_SHIFT));
        spannableString1.setSpan(new ForegroundColorSpan(-23296),0,30,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE | (1 << Spanned.SPAN_PRIORITY_SHIFT));
        spannableString1.setSpan(new ForegroundColorSpan(-16777216),40,59,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString1.setSpan(new ForegroundColorSpan(-23296),52,57,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString1.setSpan(new ForegroundColorSpan(-16777216),59,79,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString1.setSpan(new ForegroundColorSpan(-23296),71,77,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString1.setSpan(new ForegroundColorSpan(-16777216),79,98,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString1.setSpan(new ForegroundColorSpan(-23296),91,96,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString1.setSpan(new ForegroundColorSpan(-16777216),98,115,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString1.setSpan(new ForegroundColorSpan(-23296),110,113,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        htmlTextView.setText(spannableString1);

    }
    private static int getSpanPriority(int priority) {
        // priority 决定了 span 的优先级 ，而并非设置 span 的先后顺序
        int spanFlag = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
        spanFlag &= (~Spannable.SPAN_PRIORITY);
        spanFlag |= ((priority << Spannable.SPAN_PRIORITY_SHIFT) & Spannable.SPAN_PRIORITY);
        return spanFlag;
    }


    private String getActionName(MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                return "ACTION_DOWN";
            case (MotionEvent.ACTION_MOVE):
                return "ACTION_MOVE";
            case (MotionEvent.ACTION_CANCEL):
                return "ACTION_CANCEL";
            case (MotionEvent.ACTION_UP):
                return "ACTION_UP";
            default:
                return "null";
        }
    }
}