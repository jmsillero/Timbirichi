package com.timbirichi.eltimbirichi.presentation.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.svg.GlideApp;


public class ImageSliderView extends BaseSliderView {

    Bitmap bitmap;
    Drawable drawable;
    byte [] image;
    String base64Img;
    long productId;

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

    public long getProductId() {
        return productId;
    }

    public BaseSliderView setProductId(long productId) {
        this.productId = productId;
        return this;
    }

    public BaseSliderView setImageByteArray(byte [] image){
        this.image = image;
        return this;
    }

    public BaseSliderView setImageBase64(String base64Img){
        this.base64Img = base64Img;
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

        if (image != null){
            GlideApp.with(getContext())
                    .load(image)
                    .override(1000, 406)
                    .centerInside()
                    .into(imageView);
        }

        if(base64Img != null){
            GlideApp.with(getContext())
                    .load(Base64.decode(base64Img, Base64.DEFAULT))
                    .override(1000, 406)
                    .centerInside()
                    .into(imageView);
        }

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnSliderClickListener.onSliderClick(ImageSliderView.this);
            }
        });

        return imageView;
    }
}
