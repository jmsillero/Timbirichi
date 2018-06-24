package com.timbirichi.eltimbirichi.presentation.view.custom.flip_3d;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by JM on 6/16/2016.
 */
public class FadeAnimationListener implements Animation.AnimationListener {
    private View v1;
    private View v2;
    private View v3;

    public FadeAnimationListener(View v1, View v2, View v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (v1 != null){
            v1.setVisibility(View.INVISIBLE);
        }
        if (v2 != null){
            v2.setVisibility(View.INVISIBLE);
        }
        if (v3 != null){
            v3.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


}
