package com.timbirichi.eltimbirichi.presentation.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.glide.slider.library.svg.GlideApp;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.presentation.view.base.BaseActivity;
import com.timbirichi.eltimbirichi.utils.Utils;

import butterknife.BindView;

public class DetailActivity extends BaseActivity {

    public static final String EXTRA_PRODUCT = "com.eltimbirichi.timbirichi.product_selected";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.iv_product)
    ImageView ivProduct;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_price)
    TextView tvPrice;

    @BindView(R.id.tv_province)
    TextView tvProvince;

    @BindView(R.id.tv_views)
    TextView tvViews;

    @BindView(R.id.tv_new)
    TextView tvNew;

    @BindView(R.id.tv_description)
    TextView tvDescription;

    Product product;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initButterNife();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.product_detail);

        product = Utils.productSelected;

        fillGuiWithProduct();

    }

    private void fillGuiWithProduct(){
        GlideApp.with(this)
                .load(product.getImages() != null ? product.getImages().get(0).getImage() : null)
                .override(400, 400)
                .into(ivProduct);

        tvTitle.setText(product.getTitle());
        tvPrice.setText(Double.toString(product.getPrice()));
        tvProvince.setText("CAMBIAR");
        tvViews.setText(Integer.toString(product.getViews()));
        tvNew.setText(product.isNewProduct() ? getString(R.string.new_product) : getString(R.string.used_product));
        tvDescription.setText(product.getDescription());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }
}
