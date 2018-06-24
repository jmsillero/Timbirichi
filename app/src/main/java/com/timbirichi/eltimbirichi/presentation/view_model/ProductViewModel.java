package com.timbirichi.eltimbirichi.presentation.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCaseObserver;
import com.timbirichi.eltimbirichi.domain.use_case.product.LoadCategoryProductsUseCase;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.Status;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;

import java.util.List;

public class ProductViewModel extends ViewModel {
    LoadCategoryProductsUseCase loadCategoryProductsUseCase;


    public final MutableLiveData<Response<List<Product>>> products = new MutableLiveData<>();

    public ProductViewModel(LoadCategoryProductsUseCase loadCategoryProductsUseCase) {
        this.loadCategoryProductsUseCase = loadCategoryProductsUseCase;
    }


    public void getProducts(String text, int start, int end,
                            SubCategory category, String order, String orderType,
                            boolean image, double minPrice, double maxPrice,
                            ProductState state, long province){

        products.setValue(new Response<List<Product>>(Status.LOADING, null, null));
        loadCategoryProductsUseCase.setParams(text, start, end, category,
                order, orderType, image,
                minPrice, maxPrice, state,
                province);
        loadCategoryProductsUseCase.execute(new LoadProductObserver());
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
}
