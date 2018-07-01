package com.timbirichi.eltimbirichi.presentation.view_model.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timbirichi.eltimbirichi.domain.use_case.product.ClearFavoritesUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.FindProductByIdUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.GetFavoritesUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.GetLastedNewProductsUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.LoadCategoryProductsUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.LoadCoverPageProductUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.RemoveProductFromFavoriteUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.SaveToFavoritesUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.province.GetProvincesUseCase;
import com.timbirichi.eltimbirichi.presentation.view_model.ProductViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.ProvinceViewModel;

import javax.inject.Inject;

public class ProductViewModelFactory implements ViewModelProvider.Factory {

    LoadCategoryProductsUseCase loadCategoryProductsUseCase;
    LoadCoverPageProductUseCase loadCoverPageProductUseCase;
    GetLastedNewProductsUseCase getLastedNewProductsUseCase;
    GetFavoritesUseCase getFavoritesUseCase;
    SaveToFavoritesUseCase saveToFavoritesUseCase;
    RemoveProductFromFavoriteUseCase removeProductFromFavoriteUseCase;
    ClearFavoritesUseCase clearFavoritesUseCase;
    FindProductByIdUseCase findProductByIdUseCase;

    @Inject
    public ProductViewModelFactory(LoadCategoryProductsUseCase loadCategoryProductsUseCase,
                                   LoadCoverPageProductUseCase loadCoverPageProductUseCase,
                                   GetLastedNewProductsUseCase getLastedNewProductsUseCase,
                                   GetFavoritesUseCase getFavoritesUseCase,
                                   SaveToFavoritesUseCase saveToFavoritesUseCase,
                                   RemoveProductFromFavoriteUseCase removeProductFromFavoriteUseCase,
                                   ClearFavoritesUseCase clearFavoritesUseCase,
                                   FindProductByIdUseCase findProductByIdUseCase) {
        this.loadCategoryProductsUseCase = loadCategoryProductsUseCase;
        this.loadCoverPageProductUseCase = loadCoverPageProductUseCase;
        this.getLastedNewProductsUseCase = getLastedNewProductsUseCase;
        this.getFavoritesUseCase = getFavoritesUseCase;
        this.saveToFavoritesUseCase = saveToFavoritesUseCase;
        this.removeProductFromFavoriteUseCase = removeProductFromFavoriteUseCase;
        this.clearFavoritesUseCase = clearFavoritesUseCase;
        this.findProductByIdUseCase = findProductByIdUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductViewModel.class)) {
            return (T) new ProductViewModel(loadCategoryProductsUseCase,
                    loadCoverPageProductUseCase,
                    getLastedNewProductsUseCase,
                    getFavoritesUseCase,
                    saveToFavoritesUseCase,
                    removeProductFromFavoriteUseCase,
                    clearFavoritesUseCase,
                    findProductByIdUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
