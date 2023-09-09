package com.example.androidfeature.Activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.example.androidfeature.MainActivity;
import com.example.androidfeature.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        TextView textView = findViewById(R.id.helloWorld);
        // 创建一个透明度动画，从0到1，实现淡入效果
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(
                textView,
                "alpha",
                0f,
                1f
        );
        alphaAnimator.setDuration(1500); // 设置透明度动画的持续时间
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alphaAnimator);
        animatorSet.setInterpolator(new AccelerateInterpolator()); // 添加插值器，使动画加速
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                SplashActivity.this.finish();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }
}