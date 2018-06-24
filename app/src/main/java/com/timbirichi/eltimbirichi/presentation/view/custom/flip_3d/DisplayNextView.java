package com.timbirichi.eltimbirichi.presentation.view.custom.flip_3d;

/**
 * Created by JM on 5/12/2016.
 */
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;


public final class DisplayNextView implements Animation.AnimationListener {
    private boolean mCurrentView;
    View image1;
    View image2;
    private float angle;


    public DisplayNextView(boolean currentView, View image1, View image2, float angle) {
        mCurrentView = currentView;
        this.image1 = image1;
        this.image2 = image2;
        this.angle = angle;
    }




    public void onAnimationStart(Animation animation) {
    }

    public void onAnimationEnd(Animation animation) {
        image1.post(new SwapViews(mCurrentView, image1, image2, angle));
    }

    public void onAnimationRepeat(Animation animation) {
    }
}