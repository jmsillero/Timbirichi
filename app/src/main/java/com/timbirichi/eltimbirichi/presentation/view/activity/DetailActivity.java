package com.timbirichi.eltimbirichi.presentation.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.glide.slider.library.svg.GlideApp;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.view.base.BaseActivity;
import com.timbirichi.eltimbirichi.presentation.view_model.ProductViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.ProvinceViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.ProductViewModelFactory;
import com.timbirichi.eltimbirichi.utils.Utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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


    @BindView(R.id.tv_new)
    TextView tvNew;

    @BindView(R.id.tv_description)
    TextView tvDescription;

    @BindView(R.id.fb_favorites)
    FloatingActionButton fbFaborites;

    @BindView(R.id.tv_date)
    TextView tvDate;

    @BindView(R.id.btn_sms)
    Button btnSms;

    Product product;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        fbFaborites.setEnabled(false);
        setupProductViewModel();
        product = Utils.productSelected;
        productViewModel.findFavorite(product.getId());
        fillGuiWithProduct();
    }

    private void fillGuiWithProduct(){

        String formatD = "EEEE dd MMMM yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatD, new Locale("ES", "es"));
        tvDate.setText(dateFormat.format(new Date(product.getTime())));

        if(product.getImages() == null){
            ivProduct.setImageDrawable(getResources().getDrawable(R.drawable.no_imagen));
        } else{
            GlideApp.with(this)
                    .load(product.getImages() != null ? Base64.decode(product.getImages().get(0).getBase64Img(), Base64.DEFAULT)  : null)
                    .override(800, 800)
                    .into(ivProduct);
        }

        tvTitle.setText(product.getTitle());


        if(product.getPrice() == 0){
            tvPrice.setVisibility(View.INVISIBLE);
        } else{


            DecimalFormat format = new DecimalFormat("#.00");
            format.setDecimalSeparatorAlwaysShown(true);
            Locale.setDefault(Locale.FRENCH);
            format.setGroupingSize(3);
            format.setGroupingUsed(true);
            tvPrice.setText("$" + format.format(product.getPrice()).replace(",", ".") + " CUC");
          //  tvPrice.setText("$" + Integer.toString((int)product.getPrice()) + ".00 CUC");
        }



        tvProvince.setText(product.getProvince().getName());

        tvNew.setText(product.isNewProduct() ? getString(R.string.new_product) : getString(R.string.used_product));
        tvDescription.setText(product.getDescription());

        boolean isMobile = Utils.isMobileNumber(product.getPhone());
        btnSms.setEnabled(isMobile);
        btnSms.setAlpha(isMobile ? 1f : .5f);
    }

    private void setupProductViewModel(){
        productViewModel = ViewModelProviders.of(this, productViewModelFactory).get(ProductViewModel.class);

        productViewModel.favorites.observe(this, new Observer<Response<Boolean>>() {
            @Override
            public void onChanged(@Nullable Response<Boolean> booleanResponse) {
                switch (booleanResponse.status){
                    case SUCCESS:
                        productViewModel.findFavorite(product.getId());
                        break;

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
                            fbFaborites.setImageDrawable(getResources().getDrawable(R.drawable.ic_saved));
                            fbFaborites.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.a_pink_400)));
                        }else{
                            fbFaborites.setImageDrawable(getResources().getDrawable(R.drawable.ic_saved));
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

    @OnClick(R.id.btn_sms)
    public void onBtnSmsClick(){
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.setData(Uri.parse("sms:" + product.getPhone()));
        startActivity(smsIntent);
    }

    @OnClick(R.id.btn_call)
    public void onBtnCallClick(){
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + product.getPhone()));

        //todo: check permissions
        startActivity(phoneIntent);
    }

    @OnClick(R.id.btn_email)
    public void onBtnEmailClick(){
        String body = getString(R.string.product_title) + product.getTitle();

        String uriText =
                "mailto:" + product.getEmail() +
                        "?subject=" + Uri.encode(getString(R.string.details_mail_subject));

        Uri uri = Uri.parse(uriText);
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setType("text/html");
        sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
        sendIntent.setData(uri);
        startActivity(sendIntent);
    }



    @OnClick(R.id.fb_favorites)
    public void onFbFavoritesClick(){
        if(productViewModel.findFavorite.getValue().data){
            productViewModel.removeFavorite(product.getId());
           // fbFaborites.setImageDrawable(getResources().getDrawable(R.drawable.ic_saved));
           // fbFaborites.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        } else{
            productViewModel.saveFatorite(product);
           // fbFaborites.setImageDrawable(getResources().getDrawable(R.drawable.ic_saved));
           // fbFaborites.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.a_pink_400)));
        }



    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }
}
