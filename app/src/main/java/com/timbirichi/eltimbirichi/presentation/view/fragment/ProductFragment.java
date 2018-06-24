package com.timbirichi.eltimbirichi.presentation.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glide.slider.library.Indicators.PagerIndicator;
import com.glide.slider.library.SliderLayout;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.Province;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.presentation.adapter.ProductAdapter;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;
import com.timbirichi.eltimbirichi.presentation.view.base.BaseFragment;
import com.timbirichi.eltimbirichi.presentation.view.custom.ImageSliderView;
import com.timbirichi.eltimbirichi.presentation.view.dialog.FilterDialog;
import com.timbirichi.eltimbirichi.presentation.view_model.DatabaseViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.ProductViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.ProductViewModelFactory;
import com.timbirichi.eltimbirichi.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;


public class ProductFragment extends BaseFragment {

    public static final String EXTRA_CATEGORY = "com.timbirichi.eltimbirichi.extra_category";
    public  final String TAG =  getClass().getSimpleName();
    SubCategory category;

    @BindView(R.id.slider)
    SliderLayout mDemoSlider;

    @BindView(R.id.custom_indicator)
    PagerIndicator pageIndicator;


    ProductAdapter productAdapter;

    @BindView(R.id.rv_products)
    RecyclerView rvProducts;

    FilterDialog filterDialog;

    int start, end, min, max;

    @NonNull
    ProductFragmentCallback productFragmentCallback;

    @Inject
    ProductViewModelFactory productViewModelFactory;
    ProductViewModel productViewModel;

    public ProductFragment() {
        // Required empty public constructor
    }


    public static ProductFragment newInstance(SubCategory category) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.category = getArguments().getParcelable(EXTRA_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        start = Utils.PRODUCT_OFFSET;
        end = Utils.PRODUCT_OFFSET +  Utils.PRODUCT_COUNT;
        min = max = 0;

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setCustomIndicator(pageIndicator);
        mDemoSlider.setDuration(4000);

        rvProducts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvProducts.setNestedScrollingEnabled(false);
        setupProductAdapter();
        setupProductViewModel();

        productViewModel.getProducts(null,
                start, end, category,
                Product.PRODUCT_COL_TIME, Product.ORDER_DESC,
                false, min, max, ProductState.NO_NEW,
                Province.NO_PROVINCE);

        fillSlider();
        return v;
    }

    private void setupProductAdapter(){
        productAdapter = new ProductAdapter(getActivity(), new ArrayList<Product>());
        productAdapter.setProductCallback(new ProductAdapter.ProductCallback() {
            @Override
            public void onItemClick(Product prod) {
                productFragmentCallback.onProductClick(prod);
            }
        });
        rvProducts.setAdapter(productAdapter);
    }

    private void setupProductViewModel(){
        productViewModel = ViewModelProviders.of(this, productViewModelFactory).get(ProductViewModel.class);
        productViewModel.products.observe(this, new Observer<Response<List<Product>>>() {
            @Override
            public void onChanged(@Nullable Response<List<Product>> listResponse) {
                switch (listResponse.status){
                    case LOADING:
                        //todo: Mostrar un cargando
                        break;

                    case SUCCESS:
                        if(listResponse.data != null){
                            productAdapter.addProducts(listResponse.data);
                        }
                        break;


                    case ERROR:
                        showErrorDialog(getString(R.string.loading_product_error));
                        break;
                }
            }
        });
    }

    // TODO: TEMP
    private void fillSlider(){
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.s1);
        file_maps.put("Big Bang Theory",R.drawable.s2);
        file_maps.put("House of Cards",R.drawable.s3);
        file_maps.put("Game of Thrones", R.drawable.s4);

        for(String name : file_maps.keySet()){
            ImageSliderView imageSliderView = new ImageSliderView(getActivity());
            imageSliderView.setImageDrawable(getActivity().getResources().getDrawable(file_maps.get(name)));

            mDemoSlider.addSlider(imageSliderView);
        }
    }



    @OnClick(R.id.btn_filter)
    public void onBtnFilterClick(){
        filterDialog = FilterDialog.newInstance(true, 0, 0, false);
        filterDialog.show(getActivity().getFragmentManager(), TAG);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product;
    }

    @Override
    protected void initInject() {
        getFragementComponent().inject(this);
    }

    public void setProductFragmentCallback(@NonNull ProductFragmentCallback productFragmentCallback) {
        this.productFragmentCallback = productFragmentCallback;
    }

    public interface ProductFragmentCallback{
        void onProductClick(Product prod);
    }
}
