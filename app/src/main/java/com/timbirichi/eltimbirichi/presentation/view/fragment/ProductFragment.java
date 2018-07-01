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


public class ProductFragment extends BaseProductFragment {

    public static final String EXTRA_CATEGORY = "com.timbirichi.eltimbirichi.extra_category";
    public  final String TAG =  getClass().getSimpleName();

    @BindView(R.id.slider)
    SliderLayout mDemoSlider;

    @BindView(R.id.custom_indicator)
    PagerIndicator pageIndicator;

    @BindView(R.id.container_banner)
    LinearLayout bannerContainer;

    @BindView(R.id.shimmer_banner)
    ShimmerFrameLayout shimmerBanner;

    @Inject
    BannerViewModelFactory bannerViewModelFactory;
    BannerViewModel bannerViewModel;


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

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setCustomIndicator(pageIndicator);
        mDemoSlider.setDuration(4000);

        shimmerBanner.startShimmer();
        setupBannerProductViewModel();

        if(category.getId() == SubCategory.CATEGORY_LASTED){
            bannerViewModel.getBannerByCatId(COVER_PAGE_ID);

        } else{
            bannerViewModel.getBannerByCatId(category.getId());
        }
        return v;
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

    @Override
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
