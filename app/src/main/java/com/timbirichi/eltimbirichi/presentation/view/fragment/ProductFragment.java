package com.timbirichi.eltimbirichi.presentation.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.glide.slider.library.Indicators.PagerIndicator;
import com.glide.slider.library.SliderLayout;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Banner;
import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.Province;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.presentation.adapter.ProductAdapter;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;
import com.timbirichi.eltimbirichi.presentation.view.base.BaseFragment;
import com.timbirichi.eltimbirichi.presentation.view.custom.BottonBarBtnView;
import com.timbirichi.eltimbirichi.presentation.view.custom.ImageSliderView;
import com.timbirichi.eltimbirichi.presentation.view.dialog.FilterDialog;
import com.timbirichi.eltimbirichi.presentation.view_model.BannerViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.DatabaseViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.ProductViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.BannerViewModelFactory;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.ProductViewModelFactory;
import com.timbirichi.eltimbirichi.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dagger.Provides;

import static com.timbirichi.eltimbirichi.presentation.view.fragment.CoverPageFragment.COVER_PAGE_ID;


public class ProductFragment extends BaseFragment implements NestedScrollView.OnScrollChangeListener{

    public static final String EXTRA_CATEGORY = "com.timbirichi.eltimbirichi.extra_category";
    public static final String EXTRA_FIND_TEXT = "com.timbirichi.eltimbirichi.extra_find_text";
    public  final String TAG =  getClass().getSimpleName();
    SubCategory category;

    @BindView(R.id.slider)
    SliderLayout mDemoSlider;

    @BindView(R.id.custom_indicator)
    PagerIndicator pageIndicator;


    ProductAdapter productAdapter;

    @BindView(R.id.rv_products)
    RecyclerView rvProducts;

    @BindView(R.id.container_banner)
    LinearLayout bannerContainer;


    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;


    @BindView(R.id.shimmer_banner)
    ShimmerFrameLayout shimmerBanner;

    @BindView(R.id.main_scroll)
    NestedScrollView mainScroll;


    /*Filters*/
    @BindView(R.id.btn_filter_price)
    BottonBarBtnView btnFilterPrice;

    @BindView(R.id.btn_filter_date)
    BottonBarBtnView btnFilterDate;


    @BindView(R.id.btn_filter_views)
    BottonBarBtnView btnFilterViews;

    FilterDialog filterDialog;

    int start, end, min, max;
    String findText;
    boolean newFilter = true;

    protected boolean photos = false;
    protected boolean filterApply = false;
    protected boolean image = false;

    LinearLayoutManager  layoutManager;

    @NonNull
    ProductFragmentCallback productFragmentCallback;

    @Inject
    ProductViewModelFactory productViewModelFactory;
    ProductViewModel productViewModel;


    @Inject
    BannerViewModelFactory bannerViewModelFactory;
    BannerViewModel bannerViewModel;

    protected boolean loadMore = false;
    protected int visibleItemCount;
    protected int totalItemCount;
    protected int pastVisiblesItems;
    protected int firstVisibleItem;

    protected String orderBy = Product.PRODUCT_COL_TIME;
    protected String order = Product.ORDER_DESC;

    protected long province = Province.NO_PROVINCE;
    protected ProductState prodState = ProductState.NO_NEW;

    public ProductFragment() {
        // Required empty public constructor
    }


    public static ProductFragment newInstance(SubCategory category, String text) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CATEGORY, category);
        args.putString(EXTRA_FIND_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.category = getArguments().getParcelable(EXTRA_CATEGORY);
            this.findText = getArguments().getString(EXTRA_FIND_TEXT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        start = Utils.PRODUCT_OFFSET;
        end = Utils.PRODUCT_OFFSET +  Utils.PRODUCT_COUNT;

        min = max = 0;

        mainScroll.setOnScrollChangeListener(this);

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setCustomIndicator(pageIndicator);
        mDemoSlider.setDuration(4000);

        shimmerFrameLayout.startShimmer();
        shimmerBanner.startShimmer();

        layoutManager = new LinearLayoutManager(getActivity());
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setNestedScrollingEnabled(false);
        setupProductAdapter();

        setupProductViewModel();
        setupBannerProductViewModel();

        if(category.getId() == SubCategory.CATEGORY_LASTED){
            bannerViewModel.getBannerByCatId(COVER_PAGE_ID);

        } else{
            bannerViewModel.getBannerByCatId(category.getId());
        }


        productViewModel.getProducts(findText,
                Utils.PRODUCT_OFFSET, Utils.PRODUCT_COUNT, category,
                orderBy,order,
                image, min, max, prodState,
                province);


        setupFilterButtons();


        return v;
    }


    private void setupFilterButtons(){
        btnFilterPrice.setBotonBtnCallback(new BottonBarBtnView.BotonBtnCallback() {
            @Override
            public void onClick(int order) {
                Utils.PRODUCT_OFFSET = 0;
                newFilter = true;
                orderBy = Product.PRODUCT_COL_PRICE;
                ProductFragment.this.order = order == BottonBarBtnView.ORDER_DOWN ? Product.ORDER_DESC :  Product.ORDER_ASC;
                productViewModel.getProducts(findText,
                        Utils.PRODUCT_OFFSET, Utils.PRODUCT_COUNT, category,
                        orderBy, ProductFragment.this.order,
                        image, min, max, prodState,
                        province);

                btnFilterPrice.setActive(true);
                btnFilterDate.setActive(false);
                btnFilterViews.setActive(false);
            }
        });

        btnFilterDate.setBotonBtnCallback(new BottonBarBtnView.BotonBtnCallback() {
            @Override
            public void onClick(int order) {
                Utils.PRODUCT_OFFSET = 0;
                newFilter = true;
                orderBy = Product.PRODUCT_COL_TIME;
                ProductFragment.this.order = order == BottonBarBtnView.ORDER_DOWN ? Product.ORDER_DESC :  Product.ORDER_ASC;
                productViewModel.getProducts(findText,
                        Utils.PRODUCT_OFFSET, Utils.PRODUCT_COUNT, category,
                        orderBy, ProductFragment.this.order,
                        image, min, max, prodState,
                        province);

                btnFilterPrice.setActive(false);
                btnFilterDate.setActive(true);
                btnFilterViews.setActive(false);
            }
        });

        btnFilterViews.setBotonBtnCallback(new BottonBarBtnView.BotonBtnCallback() {
            @Override
            public void onClick(int order) {
                Utils.PRODUCT_OFFSET = 0;
                newFilter = true;
                orderBy = Product.PRODUCT_COL_TIME;
                ProductFragment.this.order = order == BottonBarBtnView.ORDER_DOWN ? Product.ORDER_DESC :  Product.ORDER_ASC;
                productViewModel.getProducts(findText,
                        Utils.PRODUCT_OFFSET, Utils.PRODUCT_COUNT, category,
                        orderBy, ProductFragment.this.order,
                        image, min, max, prodState,
                        province);

                btnFilterPrice.setActive(false);
                btnFilterDate.setActive(false);
                btnFilterViews.setActive(true);
            }
        });
    }

    private void setupProductAdapter(){
        productAdapter = new ProductAdapter(getActivity(), new ArrayList<Product>(), true);
        productAdapter.setProductCallback(new ProductAdapter.ProductCallback() {
            @Override
            public void onItemClick(Product prod) {
                productFragmentCallback.onProductClick(prod);
            }
        });
        rvProducts.setAdapter(productAdapter);
    }

    private void setBottonBarEnabled(boolean enabled){
        btnFilterDate.setBtnEnabled(false);
        btnFilterPrice.setBtnEnabled(false);
        btnFilterViews.setBtnEnabled(false);
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
                        if(newFilter && (listResponse.data == null ||  listResponse.data.isEmpty())){
                            // todo: POner un NO ANUNCIOS...

                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.setVisibility(View.GONE);
                            productAdapter.removeAllProducts();

                            setBottonBarEnabled(false);

                        }

                        if(listResponse.data != null){
                            loadMore = listResponse.data.size() >= Utils.PRODUCT_COUNT;
                            if (newFilter){
                                mainScroll.setScrollY(0);
                                shimmerFrameLayout.stopShimmer();
                                shimmerFrameLayout.setVisibility(View.GONE);
                                productAdapter.removeAllProducts();
                            }
                            productAdapter.addProducts(listResponse.data, loadMore);
                            Utils.PRODUCT_OFFSET += Utils.PRODUCT_COUNT;
                        }
                        break;


                    case ERROR:
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        showErrorDialog(getString(R.string.loading_product_error));
                        break;
                }
            }
        });
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//        if (etFind.hasFocus()){
//            etFind.clearFocus();
//        }

        if(v.getChildAt(v.getChildCount() - 1) != null) {
            if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                    scrollY > oldScrollY) {

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                firstVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (loadMore) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        Handler hadler = new Handler();
                        Runnable task = new Runnable() {
                            @Override
                            public void run() {
                                newFilter = false;
                                loadModeProducts(findText,
                                        Utils.PRODUCT_OFFSET,
                                        Utils.PRODUCT_COUNT,
                                        category,
                                        orderBy,order,
                                        image, min, max, prodState,
                                        province);
                            }
                        };
                        hadler.postDelayed(task, 1000);
                    }
                }
            }
        }
    }



    private void setupBannerProductViewModel(){
        bannerViewModel = ViewModelProviders.of(this, bannerViewModelFactory).get(BannerViewModel.class);
        bannerViewModel.banners.observe(this, new Observer<Response<List<Banner>>>() {
            @Override
            public void onChanged(@Nullable Response<List<Banner>> listResponse) {
                switch (listResponse.status){
                    case LOADING:
                        break;

                    case SUCCESS:
                        fillSlider(listResponse.data);
                        shimmerBanner.stopShimmer();
                        bannerContainer.setVisibility(View.VISIBLE);
                        shimmerBanner.setVisibility(View.GONE);

                        break;

                    case ERROR:
                        showErrorDialog(getString(R.string.banner_error));
                        break;
                }
            }
        });
    }


    private void fillSlider(List<Banner> banners){
        for(Banner banner : banners){
            ImageSliderView imageSliderView = new ImageSliderView(getActivity());
            imageSliderView.setImageByteArray(banner.getImage());
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

    protected void loadModeProducts(String text, int start, int end,
                                    SubCategory category, String order, String orderType,
                                    boolean image, double minPrice, double maxPrice,
                                    ProductState state, long province){

        productViewModel.getProducts(findText,
                start, end, category,
                order, orderType,
                image, min, max, prodState,
                province);



    }

    public interface ProductFragmentCallback{
        void onProductClick(Product prod);
    }
}
