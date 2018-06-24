package com.timbirichi.eltimbirichi.presentation.view.custom.flip_3d;

/**
 * Created by JM on 5/12/2016.
 */
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;


public final class SwapViews implements Runnable {
    private boolean mIsFirstView;
    View image1;
    View image2;
    private float angle;
    private Animation fadeIn;



    public SwapViews(boolean isFirstView, View image1, View image2, float angle) {
        mIsFirstView = isFirstView;
        this.image1 = image1;
        this.image2 = image2;
        this.angle = angle;
    }

    public void run() {
        final float centerX = image1.getWidth() / 2.0f;
        final float centerY = image1.getHeight() / 2.0f;
        Flip3DAnimation rotation;

        if (mIsFirstView) {
            image1.setVisibility(View.INVISIBLE);
            image2.setVisibility(View.VISIBLE);
            image2.requestFocus();
            image2.bringToFront();

         //   rotation = new Flip3DAnimation(-90, 0, centerX, centerY);
        } else {
            image2.setVisibility(View.INVISIBLE);
            image1.setVisibility(View.VISIBLE);
            image1.requestFocus();
            image1.bringToFront();
        }

        rotation = new Flip3DAnimation(-angle, 0, centerX, centerY);

        rotation.setDuration(100);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new DecelerateInterpolator());

        if (mIsFirstView) {
            image2.startAnimation(rotation);
        } else {
            image1.startAnimation(rotation);
        }
    }
}