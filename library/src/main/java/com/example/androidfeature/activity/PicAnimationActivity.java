package com.example.androidfeature.activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.androidfeature.R;

public class PicAnimationActivity extends Activity {

    private Animator currentAnimator;
    private long shortAnimationDuration = 1000;

    final Rect startBounds = new Rect();
    final Rect finalBounds = new Rect();
    final Rect holderBounds = new Rect();
    final Point globalOffset = new Point();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_animation);
//        initAnim();

        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        findViewById(R.id.zoom_out_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomOut(findViewById(R.id.zoom_out_btn), R.drawable.pic);
            }
        });

//        findViewById(R.id.zoom_in_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                zoomIn(findViewById(R.id.zoom_out_btn), R.drawable.pic);
//            }
//        });
    }

    private void zoomOut(final View view, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Rect holderBounds = new Rect();
        final Point globalOffset = new Point();

        findViewById(R.id.img1).getGlobalVisibleRect(startBounds);
        findViewById(R.id.img2).getGlobalVisibleRect(finalBounds);
        findViewById(R.id.container).getGlobalVisibleRect(holderBounds, globalOffset);

        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                < (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) finalBounds.height() / startBounds.height();
            float startWidth = finalBounds.width() / startScale;
            float deltaWidth = (startBounds.width() - startWidth) / 2;

            startBounds.left += deltaWidth;
            startBounds.right -= deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) finalBounds.width() / startBounds.width();
            float startHeight = finalBounds.height() / startScale;
            float deltaHeight = (startBounds.height() - startHeight) / 2;

            startBounds.top += deltaHeight;
            startBounds.bottom -= deltaHeight;
        }

        ImageView toImageView = findViewById(R.id.img1);
        toImageView.setImageResource(imageResId);
        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        toImageView.setPivotX(0f);
        toImageView.setPivotY(0f);
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(toImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(toImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(toImageView, View.SCALE_X,
                        1f, startScale))
                .with(ObjectAnimator.ofFloat(toImageView, View.SCALE_Y,
                        1f, startScale));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new LinearInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });

        set.start();
        currentAnimator = set;

    }
}