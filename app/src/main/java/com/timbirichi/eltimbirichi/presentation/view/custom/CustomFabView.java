package com.timbirichi.eltimbirichi.presentation.view.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timbirichi.eltimbirichi.R;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO: document your custom view class.
 */
public class CustomFabView extends LinearLayout {
    private String text;
    private int color = Color.CYAN;
    private Drawable icon;

    @BindView(R.id.ll_main)
    LinearLayout mainLayout;

    @BindView(R.id.fab_Button)
    FloatingActionButton fabButton;

    @BindView(R.id.tv_text)
    TextView tvText;

    boolean small = false;

    @NonNull
    CustomFabCallback customFabCallback;

    public CustomFabView(Context context) {
        super(context);
        init(null, 0);
    }

    public CustomFabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomFabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.view_custom_fab, this);
        ButterKnife.bind(this);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomFabView, defStyle, 0);

        text = a.getString(
                R.styleable.CustomFabView_text);

        color = a.getColor(
                R.styleable.CustomFabView_color,
                color);

        small = a.getBoolean(
                R.styleable.CustomFabView_small,
                false);


        if (a.hasValue(R.styleable.CustomFabView_icon)) {
            icon = a.getDrawable(
                    R.styleable.CustomFabView_icon);

        }

        setupUi();

        a.recycle();
    }

    @OnClick(R.id.ll_main)
    public void onMainLayoutClick(){
        customFabCallback.onClick();
    }

    public void setCustomFabCallback(CustomFabCallback customFabCallback) {
        this.customFabCallback = customFabCallback;
    }

    private void setupUi(){
        tvText.setText(text);
        fabButton.setImageDrawable(icon);
        fabButton.setBackgroundTintList( ColorStateList.valueOf(color));

        if(small){
          fabButton.setSize(FloatingActionButton.SIZE_MINI);
        }
    }

    public interface CustomFabCallback{
        void onClick();
    }
}
