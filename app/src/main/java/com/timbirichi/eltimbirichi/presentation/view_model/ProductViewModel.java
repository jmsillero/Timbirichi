package com.timbirichi.eltimbirichi.presentation.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCaseObserver;
import com.timbirichi.eltimbirichi.domain.use_case.product.GetLastedNewProductsUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.LoadCategoryProductsUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.LoadCoverPageProductUseCase;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.Status;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;

import java.util.List;

public class ProductViewModel extends ViewModel {
    LoadCategoryProductsUseCase loadCategoryProductsUseCase;
    LoadCoverPageProductUseCase loadCoverPageProductUseCase;
    GetLastedNewProductsUseCase getLastedNewProductsUseCase;


    public final MutableLiveData<Response<List<Product>>> products = new MutableLiveData<>();
    public final MutableLiveData<Response<List<Product>>> coverPageProducts = new MutableLiveData<>();

    public ProductViewModel(LoadCategoryProductsUseCase loadCategoryProductsUseCase,
                            LoadCoverPageProductUseCase loadCoverPageProductUseCase,
                            GetLastedNewProductsUseCase getLastedNewProductsUseCase) {
        this.loadCategoryProductsUseCase = loadCategoryProductsUseCase;
        this.loadCoverPageProductUseCase = loadCoverPageProductUseCase;
        this.getLastedNewProductsUseCase = getLastedNewProductsUseCase;
    }


    public void getProducts(String text, int start, int end,
                            SubCategory category, String order, String orderType,
                            boolean image, double minPrice, double maxPrice,
                            ProductState state, long province){

        products.setValue(new Response<List<Product>>(Status.LOADING, null, null));

        if(category.getId() != SubCategory.CATEGORY_LASTED){
            loadCategoryProductsUseCase.setParams(text, start, end, category,
                    order, orderType, image,
                    minPrice, maxPrice, state,
                    province);
            loadCategoryProductsUseCase.execute(new LoadProductObserver());
        } else{
            getLastedNewProductsUseCase.setParams(text, start, end, order,
                    orderType, image, minPrice,
                    maxPrice, state, province);
            getLastedNewProductsUseCase.execute(new LoadProductObserver());
        }

    }

    public void loadCoverPageProducts(){
        coverPageProducts.setValue(new Response<List<Product>>(Status.LOADING, null, null));
        loadCoverPageProductUseCase.execute(new LoadCoverPageObserver());

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
}
