package com.example.androidfeature.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class CustomText extends android.support.v7.widget.AppCompatTextView {
    public CustomText(Context context) {
        super(context);
    }

    public CustomText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextColor(Color.RED);
    }

    public CustomText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextColor(Color.RED);
    }
}
