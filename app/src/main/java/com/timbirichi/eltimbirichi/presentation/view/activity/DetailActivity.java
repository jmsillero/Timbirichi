package com.timbirichi.eltimbirichi.presentation.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.glide.slider.library.svg.GlideApp;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.view.base.BaseActivity;
import com.timbirichi.eltimbirichi.presentation.view_model.ProductViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.ProvinceViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.ProductViewModelFactory;
import com.timbirichi.eltimbirichi.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

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

    @BindView(R.id.fb_favorites)
    FloatingActionButton fbFaborites;

    Product product;

    @Inject
    ProductViewModelFactory productViewModelFactory;
    ProductViewModel productViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initButterNife();

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.product_detail);

        fbFaborites.setEnabled(false);
        setupProductViewModel();
        product = Utils.productSelected;
        productViewModel.findFavorite(product.getId());

        fillGuiWithProduct();

    }

    private void fillGuiWithProduct(){
        GlideApp.with(this)
                .load(product.getImages() != null ? product.getImages().get(0).getImage() : null)
                .override(400, 400)
                .into(ivProduct);

        tvTitle.setText(product.getTitle());
        tvPrice.setText(Double.toString(product.getPrice()));
        tvProvince.setText(product.getProvince().getName());
        tvViews.setText(Integer.toString(product.getViews()));
        tvNew.setText(product.isNewProduct() ? getString(R.string.new_product) : getString(R.string.used_product));
        tvDescription.setText(product.getDescription());
    }

    private void setupProductViewModel(){
        productViewModel = ViewModelProviders.of(this, productViewModelFactory).get(ProductViewModel.class);

        productViewModel.favorites.observe(this, new Observer<Response<Boolean>>() {
            @Override
            public void onChanged(@Nullable Response<Boolean> booleanResponse) {
                switch (booleanResponse.status){
                    case ERROR:
                        if(booleanResponse.error.getMessage().equals(ProductViewModel.SAVE_ERROR)){
                            showErrorDialog(getString(R.string.save_favorites_error));
                        } else  if(booleanResponse.error.getMessage().equals(ProductViewModel.REMOVE_ERROR)){
                            showErrorDialog(getString(R.string.remove_favorites_error));
                        }
                        break;
                }
            }
        });


        productViewModel.findFavorite.observe(this, new Observer<Response<Boolean>>() {
            @Override
            public void onChanged(@Nullable Response<Boolean> booleanResponse) {
                switch (booleanResponse.status){
                    case SUCCESS:
                        fbFaborites.setEnabled(true);
                        if(booleanResponse.data){
                            fbFaborites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorites_full));
                            fbFaborites.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.a_pink_400)));
                        }else{
                            fbFaborites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorites_empty));
                            fbFaborites.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        }
                        break;

                    case ERROR:
                        showErrorDialog(getString(R.string.find_favorites_error));
                        break;
                }
            }
        });
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

    @OnClick(R.id.fb_favorites)
    public void onFbFavoritesClick(){
        if(productViewModel.findFavorite.getValue().data){
            productViewModel.removeFavorite(product.getId());
            fbFaborites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorites_empty));
            fbFaborites.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        } else{
            productViewModel.saveFatorite(product);
            fbFaborites.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorites_full));
            fbFaborites.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.a_pink_400)));
        }
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }
}
