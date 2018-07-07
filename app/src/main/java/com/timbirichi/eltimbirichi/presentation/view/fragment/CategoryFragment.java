package com.timbirichi.eltimbirichi.presentation.view.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.presentation.adapter.SubCategoryAdapter;
import com.timbirichi.eltimbirichi.presentation.view.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class CategoryFragment extends BaseFragment {

    public final static String EXTRA_CATEGORIES = "com.timbirichi.eltimbirichi.extra_categories";

    List<Category> categories;

    @BindView(R.id.el_categories)
    ExpandableListView elCategories;

    SubCategoryAdapter subCategoryAdapter;



    @NonNull
    CategoryFragmentCallback categoryFragmentCallback;

    public static CategoryFragment newInstance(List<Category> categories) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_CATEGORIES, new ArrayList<Parcelable>(categories));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            categories = getArguments().getParcelableArrayList(EXTRA_CATEGORIES);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);



        subCategoryAdapter = new SubCategoryAdapter(getActivity(), categories);
        elCategories.setAdapter(subCategoryAdapter);
        elCategories.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                categoryFragmentCallback.onCategorySelected(categories.get(groupPosition).getSubCategories().get(childPosition));
                return true;
            }
        });
        return v;
    }

    public void setCategoryFragmentCallback(@NonNull CategoryFragmentCallback categoryFragmentCallback) {
        this.categoryFragmentCallback = categoryFragmentCallback;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initInject() {
        getFragementComponent().inject(this);
    }

    public interface CategoryFragmentCallback{
        void onCategorySelected(SubCategory category);
    }
}
