package com.timbirichi.eltimbirichi.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.glide.slider.library.Indicators.PagerIndicator;
import com.glide.slider.library.SliderLayout;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.presentation.adapter.SelectedCategoryAdapter;
import com.timbirichi.eltimbirichi.presentation.adapter.SelectedProductAdapter;
import com.timbirichi.eltimbirichi.presentation.model.constant.ShorcutType;
import com.timbirichi.eltimbirichi.presentation.view.base.BaseFragment;
import com.timbirichi.eltimbirichi.presentation.view.custom.ImageSliderView;
import com.timbirichi.eltimbirichi.presentation.view.custom.NewsPanelView;
import com.timbirichi.eltimbirichi.presentation.view.custom.SelectedProductsView;
import com.timbirichi.eltimbirichi.presentation.view.custom.ShortcutPanelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;


public class CoverPageFragment extends BaseFragment {

    public final static String TAG = "CoverPageFragment";

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

    @NonNull
    CoverPageCallback coverPageCallback;

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


        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setCustomIndicator(pageIndicator);
        mDemoSlider.setDuration(4000);

        fillSlider();
        fillProducts();
        fillCategories();

        shortcutPanelView.setShorcutPanelCallback(new ShortcutPanelView.ShorcutPanelCallback() {
            @Override
            public void onButtonClick(ShorcutType shorcutType) {
                switch (shorcutType){
                    case CATEGORY:
                        coverPageCallback.onOpenCategories();
                        break;

                    case CONTACT:
                        // todo: abrir contactos
                        break;

                    case PUBLISH:
                        // todo: abrir publicar
                        break;

                    case FAVORITES:
                        // todo: abrir favoritos
                        break;
                }
            }
        });

        return v;
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

    private void fillProducts(){
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 6; i++){
            Product prod = new Product();
            prod.setPrice(100);
            prod.setTitle("Computadora de escritorio");
            products.add(prod);
        }

        selectedProductAdapter = new SelectedProductAdapter(getActivity(), products);
        selectedProductsView.setNestedScrollingEnabled(false);
        selectedProductsView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        selectedProductsView.setAdapter(selectedProductAdapter);
    }

    private void fillCategories(){
        List<SubCategory> categories = new ArrayList<>();

        SubCategory cat = new SubCategory();
        cat.setName("Computadoras");

        SubCategory cat2 = new SubCategory();
        cat2.setName("Discos Duros");

        SubCategory cat3 = new SubCategory();
        cat3.setName("Celulares");

        SubCategory cat4 = new SubCategory();
        cat4.setName("Comida China");

        categories.add(cat);
        categories.add(cat2);
        categories.add(cat3);
        categories.add(cat4);


        selectedCategoryAdapter = new SelectedCategoryAdapter(getActivity(), categories);

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
    }
}
