package com.timbirichi.eltimbirichi.presentation.view_model.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timbirichi.eltimbirichi.domain.use_case.category.GetCategoriesUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.LoadRandomSubCategoriesUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.province.GetProvincesUseCase;
import com.timbirichi.eltimbirichi.presentation.view_model.CategoryViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.ProvinceViewModel;

import javax.inject.Inject;

public class CategoryViewModelFactory implements ViewModelProvider.Factory {

    GetCategoriesUseCase getCategoriesUseCase;
    LoadRandomSubCategoriesUseCase loadRandomSubCategoriesUseCase;

    @Inject
    public CategoryViewModelFactory(GetCategoriesUseCase categoriesUseCase,
                                    LoadRandomSubCategoriesUseCase loadRandomSubCategoriesUseCase) {
        this.getCategoriesUseCase = categoriesUseCase;
        this.loadRandomSubCategoriesUseCase = loadRandomSubCategoriesUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
            return (T) new CategoryViewModel(getCategoriesUseCase, loadRandomSubCategoriesUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
