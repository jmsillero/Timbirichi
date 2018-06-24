package com.timbirichi.eltimbirichi.presentation.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.glide.slider.library.SliderTypes.BaseSliderView;


public class ImageSliderView extends BaseSliderView {

    Bitmap bitmap;
    Drawable drawable;
    public ImageSliderView(Context context) {
        super(context);
    }

    public BaseSliderView setImage(Bitmap bitmap){
        this.bitmap = bitmap;
        return this;
    }

    public BaseSliderView setImageDrawable(Drawable drawable){
        this.drawable = drawable;
        return this;
    }

    @Override
    public View getView() {
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }

        if (drawable != null){
            imageView.setImageDrawable(drawable);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
       // this.bindEventAndShow(imageView, imageView);
        return imageView;
    }
}
