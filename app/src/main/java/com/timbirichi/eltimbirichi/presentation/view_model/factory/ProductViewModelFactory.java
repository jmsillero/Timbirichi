package com.timbirichi.eltimbirichi.presentation.view_model.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timbirichi.eltimbirichi.domain.use_case.product.LoadCategoryProductsUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.province.GetProvincesUseCase;
import com.timbirichi.eltimbirichi.presentation.view_model.ProductViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.ProvinceViewModel;

import javax.inject.Inject;

public class ProductViewModelFactory implements ViewModelProvider.Factory {

    LoadCategoryProductsUseCase loadCategoryProductsUseCase;

    @Inject
    public ProductViewModelFactory(LoadCategoryProductsUseCase loadCategoryProductsUseCase) {
        this.loadCategoryProductsUseCase = loadCategoryProductsUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductViewModel.class)) {
            return (T) new ProductViewModel(loadCategoryProductsUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
