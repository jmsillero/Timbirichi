package com.timbirichi.eltimbirichi.presentation.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.manager.DefaultConnectivityMonitorFactory;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.glide.slider.library.Indicators.PagerIndicator;
import com.glide.slider.library.SliderLayout;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Banner;
import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.presentation.adapter.SelectedCategoryAdapter;
import com.timbirichi.eltimbirichi.presentation.adapter.SelectedProductAdapter;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.constant.ShorcutType;
import com.timbirichi.eltimbirichi.presentation.view.activity.MainActivity;
import com.timbirichi.eltimbirichi.presentation.view.base.BaseFragment;
import com.timbirichi.eltimbirichi.presentation.view.custom.ImageSliderView;
import com.timbirichi.eltimbirichi.presentation.view.custom.NewsPanelView;
import com.timbirichi.eltimbirichi.presentation.view.custom.SelectedProductsView;
import com.timbirichi.eltimbirichi.presentation.view.custom.ShortcutPanelView;
import com.timbirichi.eltimbirichi.presentation.view_model.BannerViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.CategoryViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.ProductViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.BannerViewModelFactory;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.CategoryViewModelFactory;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.ProductViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class CoverPageFragment extends BaseFragment {

    public final static String TAG = "CoverPageFragment";
    public final static int COVER_PAGE_ID = 0;

    @BindView(R.id.frameLayout)
    NestedScrollView mainScroll;

    @BindView(R.id.slider)
    SliderLayout mDemoSlider;

    @BindView(R.id.custom_indicator)
    PagerIndicator pageIndicator;

    @BindView(R.id.selectedProductsView)
    SelectedProductsView selectedProductsView;

    @BindView(R.id.selectedCategories)
    SelectedProductsView selecctedCategories;

    SelectedProductAdapter selectedProductAdapter;
    SelectedCategoryAdapter selectedCategoryAdapter;

    @BindView(R.id.shortcut_panel)
    ShortcutPanelView shortcutPanelView;

    @BindView(R.id.newsPanelView)
    NewsPanelView newsPanelView;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;

    @BindView(R.id.shimmer_selected_product)
    ShimmerFrameLayout shimmerSelectedProduct;

    @BindView(R.id.container_banner)
    LinearLayout containerBanner;



    @NonNull
    CoverPageCallback coverPageCallback;

    @Inject
    BannerViewModelFactory bannerViewModelFactory;
    BannerViewModel bannerViewModel;

    @Inject
    ProductViewModelFactory productViewModelFactory;
    ProductViewModel productViewModel;

    @Inject
    CategoryViewModelFactory categoryViewModelFactory;
    CategoryViewModel categoryViewModel;

    @BindView(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;

    public CoverPageFragment() {
        // Required empty public constructor
    }


    public static CoverPageFragment newInstance() {
        CoverPageFragment fragment = new CoverPageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setCoverPageCallback(CoverPageCallback coverPageCallback) {
        this.coverPageCallback = coverPageCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider.setCustomIndicator(pageIndicator);
        mDemoSlider.setDuration(4000);
        mDemoSlider.requestFocus();

        shimmerFrameLayout.startShimmer();
        shimmerSelectedProduct.startShimmer();

        floatingSearchView.attachNavigationDrawerToMenuButton(((MainActivity)getActivity()).drawer);
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                fragmentCallback.onFindProducts(currentQuery);
            }
        });

        setupBannerProductViewModel();
        setupProductViewModel();
        setupCategoryViewModel();

        bannerViewModel.getBannerByCatId(COVER_PAGE_ID);
        productViewModel.loadCoverPageProducts();
        categoryViewModel.loadRandomSubcategories(4);

        setupShorcutPanelView();

        mainScroll.setScrollY(0);

        return v;
    }

    private void setupShorcutPanelView(){

        shortcutPanelView.setShorcutPanelCallback(new ShortcutPanelView.ShorcutPanelCallback() {
            @Override
            public void onButtonClick(ShorcutType shorcutType) {
                switch (shorcutType){
                    case CATEGORY:
                        coverPageCallback.onOpenCategories();
                        break;

                    case THE_LAST:
                        coverPageCallback.openLastNewProductFragment();
                        break;

                    case PUBLISH:
                        coverPageCallback.openPublish();
                        break;

                    case FAVORITES:
                        coverPageCallback.openFavorites();
                        break;
                }
            }
        });
    }

    private void setupCategoryViewModel(){
        categoryViewModel = ViewModelProviders.of(this, categoryViewModelFactory).get(CategoryViewModel.class);

        categoryViewModel.subcategories.observe(this, new Observer<Response<List<SubCategory>>>() {
            @Override
            public void onChanged(@Nullable Response<List<SubCategory>> listResponse) {
                switch (listResponse.status){
                    case LOADING:
                        // todo: Mostrar cargando todavia
                        break;

                    case SUCCESS:
                        if(listResponse.data != null){
                            fillCategories(listResponse.data);
                        }
                        break;

                    case ERROR:
                        showErrorDialog(getString(R.string.loading_category_error));
                        break;
                }
            }
        });
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
                        containerBanner.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);

                        fillSlider(listResponse.data);
                        break;

                    case ERROR:
                        containerBanner.setVisibility(View.VISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        showErrorDialog(getString(R.string.banner_error));
                        break;
                }
            }
        });
    }

    private void setupProductViewModel(){
        productViewModel = ViewModelProviders.of(this, productViewModelFactory).get(ProductViewModel.class);
        productViewModel.coverPageProducts.observe(this, new Observer<Response<List<Product>>>() {
            @Override
            public void onChanged(@Nullable Response<List<Product>> listResponse) {
                switch (listResponse.status){
                    case LOADING:

                        break;

                    case SUCCESS:

                        if(listResponse.data != null){
                            shimmerSelectedProduct.stopShimmer();
                            shimmerSelectedProduct.setVisibility(View.GONE);
                            fillProducts(listResponse.data);

                        }
                        break;


                    case ERROR:

                        showErrorDialog(getString(R.string.loading_product_error));
                        break;
                }
            }
        });
    }

    private void fillSlider(List<Banner> banners){
        mDemoSlider.removeAllSliders();
        for(Banner banner : banners){
            ImageSliderView imageSliderView = new ImageSliderView(getActivity());
            imageSliderView.setImageBase64(banner.getBase64Img());
            mDemoSlider.addSlider(imageSliderView);
        }
    }

    private void fillProducts(List<Product> products){
        if(products.size() >= 2){
            newsPanelView.addProducts(products.get(0), products.get(1));
            newsPanelView.setPanelNewsCallback(new NewsPanelView.PanelNewsCallback() {
                @Override
                public void onNewsClick(Product product) {
                    coverPageCallback.openDetailActivity(product);
                }
            });

            List<Product> newList = products.subList(2, products.size());

            selectedProductAdapter = new SelectedProductAdapter(getActivity(), newList);
            selectedProductAdapter.setSelectProductAdapterCallback(new SelectedProductAdapter.SelectProductAdapterCallback() {
                @Override
                public void onProductClick(Product product) {
                    coverPageCallback.openDetailActivity(product);
                }
            });

            selectedProductsView.setNestedScrollingEnabled(false);
            selectedProductsView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            selectedProductsView.setAdapter(selectedProductAdapter);
        }
    }

    private void fillCategories(List<SubCategory> categories){
        selectedCategoryAdapter = new SelectedCategoryAdapter(getActivity(), categories);
        selectedCategoryAdapter.setSelectedCategoryCallback(new SelectedCategoryAdapter.SelectedCategoryCallback() {
            @Override
            public void onCategoryClick(SubCategory cat) {
                coverPageCallback.openCategorySelected(cat);
            }
        });
        selecctedCategories.setNestedScrollingEnabled(false);
        selecctedCategories.setLayoutManager(new GridLayoutManager(getContext(), 2));
        selecctedCategories.setAdapter(selectedCategoryAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cover_page;
    }

    @Override
    protected void initInject() {
        getFragementComponent().inject(this);
    }

    public interface  CoverPageCallback{
        void onOpenCategories();
        void openUpdateActivity();
        void openDetailActivity(Product prod);
        void openCategorySelected(SubCategory cat);
        void openLastNewProductFragment();
        void openFavorites();
        void openPublish();
    }
}
