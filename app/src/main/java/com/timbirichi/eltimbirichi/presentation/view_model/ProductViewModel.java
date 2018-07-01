package com.timbirichi.eltimbirichi.presentation.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCaseObserver;
import com.timbirichi.eltimbirichi.domain.use_case.product.ClearFavoritesUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.FindProductByIdUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.GetFavoritesUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.GetLastedNewProductsUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.LoadCategoryProductsUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.LoadCoverPageProductUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.RemoveProductFromFavoriteUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.SaveToFavoritesUseCase;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.Status;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;

import java.util.List;

public class ProductViewModel extends ViewModel {
    LoadCategoryProductsUseCase loadCategoryProductsUseCase;
    LoadCoverPageProductUseCase loadCoverPageProductUseCase;
    GetLastedNewProductsUseCase getLastedNewProductsUseCase;
    GetFavoritesUseCase getFavoritesUseCase;
    SaveToFavoritesUseCase saveToFavoritesUseCase;
    RemoveProductFromFavoriteUseCase removeProductFromFavoriteUseCase;
    ClearFavoritesUseCase clearFavoritesUseCase;
    FindProductByIdUseCase findProductByIdUseCase;

    public static final String SAVE_ERROR = "save_error";
    public static final String REMOVE_ERROR = "remove_error";
    public static final String CLEAR_ERROR = "clear_error";
    public static final String FIND_ERROR = "find_error";

    public final MutableLiveData<Response<List<Product>>> products = new MutableLiveData<>();
    public final MutableLiveData<Response<List<Product>>> coverPageProducts = new MutableLiveData<>();

    public final MutableLiveData<Response<Boolean>> favorites = new MutableLiveData<>();
    public final MutableLiveData<Response<Boolean>> findFavorite = new MutableLiveData<>();

    public ProductViewModel(LoadCategoryProductsUseCase loadCategoryProductsUseCase,
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


    public void getProducts(String text, int start, int end,
                            SubCategory category, String order, String orderType,
                            boolean image, double minPrice, double maxPrice,
                            ProductState state, long province){

        products.setValue(new Response<List<Product>>(Status.LOADING, null, null));

        if(category.getId() == SubCategory.CATEGORY_LASTED){

            getLastedNewProductsUseCase.setParams(text, start, end, order,
                    orderType, image, minPrice,
                    maxPrice, state, province);
            getLastedNewProductsUseCase.execute(new LoadProductObserver());

        }else if(category.getId() == SubCategory.CATEGORY_FAVORITES){

            getFavoritesUseCase.setParams(text, start, end, order,
                    orderType, image, minPrice,
                    maxPrice, state, province);
            getFavoritesUseCase.execute(new LoadProductObserver());
        }else{
            loadCategoryProductsUseCase.setParams(text, start, end, category,
                    order, orderType, image,
                    minPrice, maxPrice, state,
                    province);
            loadCategoryProductsUseCase.execute(new LoadProductObserver());
        }

    }

    public void loadCoverPageProducts(){
        coverPageProducts.setValue(new Response<List<Product>>(Status.LOADING, null, null));
        loadCoverPageProductUseCase.execute(new LoadCoverPageObserver());
    }

    public void saveFatorite(Product product){
        saveToFavoritesUseCase.setParams(product);
        saveToFavoritesUseCase.execute(new SaveFavoriteObserver());
    }

    public void removeFavorite(long id){
        removeProductFromFavoriteUseCase.setParams(id);
        removeProductFromFavoriteUseCase.execute(new RemoveFavoriteObserver());
    }

    public void clearFavorites(){
        clearFavoritesUseCase.execute(new ClearFavoriteObserver());
    }

    public void findFavorite(long id){
        findProductByIdUseCase.setParams(id);
        findProductByIdUseCase.execute(new FindFavoriteObserver());
    }


    /*Observers*/
    public final class LoadProductObserver extends UseCaseObserver<List<Product>>{
        public LoadProductObserver() {
        }

        @Override
        public void onNext(List<Product> prods) {
            products.setValue(new Response<List<Product>>(Status.SUCCESS, prods, null));
        }

        @Override
        public void onError(Throwable throwable) {
            products.setValue(new Response<List<Product>>(Status.SUCCESS, null, throwable));
        }
    }

    public final class LoadCoverPageObserver extends UseCaseObserver<List<Product>>{
        public LoadCoverPageObserver() {
        }

        @Override
        public void onNext(List<Product> products) {
            coverPageProducts.setValue(new Response<List<Product>>(Status.SUCCESS, products, null));
        }

        @Override
        public void onError(Throwable throwable) {
            coverPageProducts.setValue(new Response<List<Product>>(Status.ERROR, null, throwable));
        }
    }

    public final class SaveFavoriteObserver extends UseCaseObserver<Boolean>{
        public SaveFavoriteObserver() {
        }

        @Override
        public void onNext(Boolean aBoolean) {
            favorites.setValue(new Response<Boolean>(Status.SUCCESS, true, null));
        }

        @Override
        public void onError(Throwable throwable) {
           favorites.setValue(new Response<Boolean>(Status.ERROR, null, new Throwable(SAVE_ERROR)));
        }
    }

    public final class RemoveFavoriteObserver extends UseCaseObserver<Boolean>{
        public RemoveFavoriteObserver() {
        }

        @Override
        public void onNext(Boolean aBoolean) {
            favorites.setValue(new Response<Boolean>(Status.SUCCESS, true, null));
        }

        @Override
        public void onError(Throwable throwable) {
            favorites.setValue(new Response<Boolean>(Status.ERROR, null, new Throwable(REMOVE_ERROR)));
        }
    }


    public final class ClearFavoriteObserver extends UseCaseObserver<Boolean>{
        public ClearFavoriteObserver() {
        }

        @Override
        public void onNext(Boolean aBoolean) {
            favorites.setValue(new Response<Boolean>(Status.SUCCESS, true, null));
        }

        @Override
        public void onError(Throwable throwable) {
            favorites.setValue(new Response<Boolean>(Status.ERROR, null, new Throwable(CLEAR_ERROR)));
        }
    }


    public final class FindFavoriteObserver extends UseCaseObserver<Boolean>{
        public FindFavoriteObserver() {
        }

        @Override
        public void onNext(Boolean aBoolean) {
            findFavorite.setValue(new Response<Boolean>(Status.SUCCESS, aBoolean, null));
        }

        @Override
        public void onError(Throwable throwable) {
            findFavorite.setValue(new Response<Boolean>(Status.ERROR, null, new Throwable(FIND_ERROR)));
        }
    }

}
