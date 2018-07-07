package com.timbirichi.eltimbirichi.presentation.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.glide.slider.library.svg.GlideApp;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.presentation.view.custom.flip_3d.DisplayNextView;
import com.timbirichi.eltimbirichi.presentation.view.custom.flip_3d.Flip3DAnimation;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO: document your custom view class.
 */
public class NewsView extends ConstraintLayout {
    private String text;
    private Drawable mainImage;
    private Drawable secondaryImage;
    private int backgroundColor = Color.CYAN;
    private boolean rotate = false;

    private boolean isFirstImage = true;

    @BindView(R.id.tv_main_text)
    TextView tvMainText;

    @BindView(R.id.iv_main_image)
    ImageView ivMainImage;

    @BindView(R.id.iv_secondary_image)
    ImageView ivSecondaryImage;

    @BindView(R.id.cv_main)
    CardView cvMain;

    @BindView(R.id.shimmer)
    ShimmerFrameLayout shimmerFrameLayout;

    @BindView(R.id.main_layout)
    ConstraintLayout mainLayout;


    @NonNull
    NewsCallback newsCallback;


    public NewsView(Context context) {
        super(context);
        init(null, 0);
    }

    public NewsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public NewsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        inflate(getContext(), R.layout.view_news, this);
        ButterKnife.bind(this);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.NewsView, defStyle, 0);

        text = a.getString(
                R.styleable.NewsView_main_text);

        backgroundColor = a.getColor(
                R.styleable.NewsView_background_color,
                backgroundColor);




        if (a.hasValue(R.styleable.NewsView_main_image)) {
            mainImage = a.getDrawable(
                    R.styleable.NewsView_main_image);

        }

        if (a.hasValue(R.styleable.NewsView_secondary_image)) {
            secondaryImage = a.getDrawable(
                    R.styleable.NewsView_secondary_image);

        }

        rotate = a.getBoolean(R.styleable.NewsView_rotate,
                false);

        setupUi();

        a.recycle();

    }

    public void setNewsCallback(@NonNull NewsCallback newsCallback) {
        this.newsCallback = newsCallback;
    }

    public void setProduct(final Product prod){
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(GONE);
        tvMainText.setText(prod.getTitle());

        if(prod.getImages() != null && prod.getImages().get(0) != null){
            GlideApp.with(getContext())
                    .load(Base64.decode(prod.getImages().get(0).getBase64Img(), Base64.DEFAULT))
                    .override(200, 200)
                    .circleCrop()
                    .thumbnail(.5f)
                    .into(ivMainImage);
        } else{
            ivMainImage.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.ic_launcher_round));
        }

        cvMain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newsCallback.onNewsClick(prod);
            }
        });
        mainLayout.setVisibility(VISIBLE);
    }

    private void setupUi(){

        shimmerFrameLayout.startShimmer();

        if(mainImage != null){
            GlideApp.with(getContext())
                    .load(mainImage)
                    .override(200, 200)
                    .circleCrop()
                    .thumbnail(.5f)
                    .into(ivMainImage);
        }

        if(secondaryImage != null){
            GlideApp.with(getContext())
                    .load(secondaryImage)
                    .override(200, 200)
                    .circleCrop()
                    .thumbnail(.5f)
                    .into(ivSecondaryImage);
        }

        tvMainText.setText(text);

        cvMain.setCardBackgroundColor(backgroundColor);

        final float startA = 0;
        final float endA = -90;
        final Handler handler = new Handler();
        Runnable task = new Runnable() {
            @Override
            public void run() {
               applyRotation(startA, endA);
               isFirstImage = !isFirstImage;

               handler.postDelayed(this, 2000);
            }
        };

        if(rotate && secondaryImage != null){
               handler.postDelayed(task, 2000);
        }
    }


    private void applyRotation(float start, float end) {
        // Find the center of image
        final float centerX = ivMainImage.getWidth() / 2.0f;
        final float centerY = ivMainImage.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final Flip3DAnimation rotation =
                new Flip3DAnimation(start, end, centerX, centerY);
        rotation.setDuration(200);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(isFirstImage, ivMainImage,ivSecondaryImage, end));

        if (isFirstImage)
        {
            ivMainImage.startAnimation(rotation);

        } else {
            ivSecondaryImage.startAnimation(rotation);
        }
    }

    public interface NewsCallback{
        void onNewsClick(Product product);
    }


}
