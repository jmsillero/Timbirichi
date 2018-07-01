package com.timbirichi.eltimbirichi.presentation.view.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.timbirichi.eltimbirichi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO: document your custom view class.
 */
public class BottonBarBtnView extends ConstraintLayout {
    public static final int ORDER_DOWN = 0;
    public static final int ORDER_UP = 1;

    int order = ORDER_DOWN;
    boolean active = false;

    private String text;
    private Drawable indicator;
    private Drawable orderUp;
    private Drawable orderDown;

    @NonNull
    private BotonBtnCallback botonBtnCallback;

    @BindView(R.id.iv_order)
    ImageView ivOrder;

    @BindView(R.id.iv_indicator)
    ImageView ivIndicator;

    @BindView(R.id.tv_order_text)
    TextView tvOrderText;

    @BindView(R.id.main_layout)
    ConstraintLayout mainlayout;

    public BottonBarBtnView(Context context) {
        super(context);
        init(null, 0);
    }

    public BottonBarBtnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BottonBarBtnView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        inflate(getContext(), R.layout.view_botton_bar_btn, this);
        ButterKnife.bind(this);
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.BottonBarBtnView, defStyle, 0);

        text = a.getString(
                R.styleable.BottonBarBtnView_order_text);


        active = a.getBoolean(
                R.styleable.BottonBarBtnView_order_active, false);

        if (a.hasValue(R.styleable.BottonBarBtnView_indicator)) {
            indicator = a.getDrawable(
                    R.styleable.BottonBarBtnView_indicator);
        }

        if (a.hasValue(R.styleable.BottonBarBtnView_order_down)) {
            orderDown = a.getDrawable(
                    R.styleable.BottonBarBtnView_order_down);
        }

        if (a.hasValue(R.styleable.BottonBarBtnView_order_up)) {
            orderUp = a.getDrawable(
                    R.styleable.BottonBarBtnView_order_up);
        }

        a.recycle();

        setupUi();

    }

    @OnClick(R.id.main_layout)
    public void onBtnClick(){
        order = Math.abs(order - 1);
        botonBtnCallback.onClick(order);

        if(order == ORDER_DOWN){
            ivOrder.setImageDrawable(orderDown);
        } else{
            ivOrder.setImageDrawable(orderUp);
        }

        setupColors();
    }

    public void setBotonBtnCallback(@NonNull BotonBtnCallback botonBtnCallback) {
        this.botonBtnCallback = botonBtnCallback;
    }

    public void setBtnEnabled(boolean enabled){
        mainlayout.setEnabled(enabled);


        active = enabled && active;

        setAlpha(enabled ? 1f : .5f);
        setupColors();
    }

    public void setActive(boolean active){
        this.active = active;
        setupColors();
    }

    private void setupUi(){
        ivIndicator.setImageDrawable(indicator);
        ivOrder.setImageDrawable(orderDown);
        tvOrderText.setText(text);
        setupColors();
    }

    private void setupColors(){
        if(active){
            ivIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            ivOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            tvOrderText.setTextColor(getResources().getColor(R.color.colorAccent));
        } else{
            ivIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.a_gray_600)));
            ivOrder.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.a_gray_600)));
            tvOrderText.setTextColor(getResources().getColor(R.color.a_gray_600));
        }
    }



    public interface BotonBtnCallback{
        void onClick(int order);
    }



}
