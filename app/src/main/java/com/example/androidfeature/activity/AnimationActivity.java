package com.example.androidfeature.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfeature.R;
import com.example.androidfeature.widget.RecceLinearGradientView;

import java.util.ArrayList;
import java.util.List;

public class AnimationActivity extends BaseActivity {
    AnimatorSet animatorSet1;

    AnimatorSet animatorSet2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        View targetView = findViewById(R.id.target);

        EditText editText = findViewById(R.id.editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("InputTest", "beforeTextChanged " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("InputTest", "onTextChanged " + s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("InputTest", "afterTextChanged " + s);
            }
        });

        animatorSet1 = new AnimatorSet();
        animatorSet1.setDuration(1000);
        animatorSet1.setStartDelay(1000);
        float start = 0;
        float end = 45;
        List<Animator> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(ObjectAnimator.ofFloat(targetView, "rotation", new float[]{start, end}));
            start += 45;
            end += 45;
        }
        animatorSet1.playSequentially(list);
        animatorSet1.start();

        View linearGradientView = findViewById(R.id.gradient_p);
        animatorSet2 = new AnimatorSet();
        animatorSet2.setDuration(1000);
        animatorSet2.setStartDelay(1000);
        start = 0;
        end = 45;
        List<Animator> list1 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list1.add(ObjectAnimator.ofFloat(linearGradientView, "rotation", new float[]{start, end}));
            start += 45;
            end += 45;
        }

        animatorSet2.playSequentially(list1);
        animatorSet2.start();
    }
}