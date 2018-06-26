package com.timbirichi.eltimbirichi.presentation.view_model.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timbirichi.eltimbirichi.domain.use_case.product.LoadCategoryProductsUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.LoadCoverPageProductUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.province.GetProvincesUseCase;
import com.timbirichi.eltimbirichi.presentation.view_model.ProductViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.ProvinceViewModel;

import javax.inject.Inject;

public class ProductViewModelFactory implements ViewModelProvider.Factory {

    LoadCategoryProductsUseCase loadCategoryProductsUseCase;
    LoadCoverPageProductUseCase loadCoverPageProductUseCase;

    @Inject
    public ProductViewModelFactory(LoadCategoryProductsUseCase loadCategoryProductsUseCase,
                                   LoadCoverPageProductUseCase loadCoverPageProductUseCase) {
        this.loadCategoryProductsUseCase = loadCategoryProductsUseCase;
        this.loadCoverPageProductUseCase = loadCoverPageProductUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductViewModel.class)) {
            return (T) new ProductViewModel(loadCategoryProductsUseCase, loadCoverPageProductUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
