package com.timbirichi.eltimbirichi.presentation.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.presentation.model.constant.ShorcutType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO: document your custom view class.
 */
public class ShortcutPanelView extends LinearLayout {



    @NonNull
    ShorcutPanelCallback shorcutPanelCallback;

    @BindView(R.id.btn_category)
    CustomFabView btnCategory;

    @BindView(R.id.btn_publish)
    CustomFabView btnPublish;

    @BindView(R.id.btn_favorites)
    CustomFabView btnFavorites;

    @BindView(R.id.btn_contact)
    CustomFabView btnContact;



    public ShortcutPanelView(Context context) {
        super(context);
        init(null, 0);
    }

    public ShortcutPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ShortcutPanelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.view_shortcut_panel, this);
        ButterKnife.bind(this);

        btnCategory.setCustomFabCallback(new CustomFabView.CustomFabCallback() {
            @Override
            public void onClick() {
                shorcutPanelCallback.onButtonClick(ShorcutType.CATEGORY);
            }
        });

        btnPublish.setCustomFabCallback(new CustomFabView.CustomFabCallback() {
            @Override
            public void onClick() {
                shorcutPanelCallback.onButtonClick(ShorcutType.PUBLISH);
            }
        });


        btnContact.setCustomFabCallback(new CustomFabView.CustomFabCallback() {
            @Override
            public void onClick() {
                shorcutPanelCallback.onButtonClick(ShorcutType.CONTACT);
            }
        });


        btnFavorites.setCustomFabCallback(new CustomFabView.CustomFabCallback() {
            @Override
            public void onClick() {
                shorcutPanelCallback.onButtonClick(ShorcutType.FAVORITES);
            }
        });
    }


    public void setShorcutPanelCallback(ShorcutPanelCallback shorcutPanelCallback) {
        this.shorcutPanelCallback = shorcutPanelCallback;
    }

    public interface ShorcutPanelCallback{
        void onButtonClick(ShorcutType shorcutType);
    }




}
