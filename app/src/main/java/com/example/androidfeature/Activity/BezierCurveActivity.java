package com.example.androidfeature.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.androidfeature.R;
import com.example.androidfeature.widget.Bezier.BezierCureView;

public class BezierCurveActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_curve);
        BezierCureView bezierCureView= findViewById(R.id.bezier);
        findViewById(R.id.generate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bezierCureView.drawBezierCurve();
            }
        });
    }
}