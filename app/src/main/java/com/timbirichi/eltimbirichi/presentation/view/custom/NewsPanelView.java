package com.timbirichi.eltimbirichi.presentation.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Product;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO: document your custom view class.
 */
public class NewsPanelView extends LinearLayout {

    @BindView(R.id.prod_1)
    NewsView prod1;

    @BindView(R.id.prod_2)
    NewsView prod2;

    public NewsPanelView(Context context) {
        super(context);
        init(null, 0);
    }

    public NewsPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public NewsPanelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.view_news_panel, this);
        ButterKnife.bind(this);
    }

    public void addProducts(Product prod1, Product prod2){
        this.prod1.setProduct(prod1);
        this.prod2.setProduct(prod2);
    }


}
