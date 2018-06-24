package com.timbirichi.eltimbirichi.domain.use_case.product;

import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.domain.repository.IProductRepository;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCase;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.timbirichi.eltimbirichi.utils.Utils.EXCECUTOR_THREAD_NAMED;
import static com.timbirichi.eltimbirichi.utils.Utils.UI_THREAD_NAMED;

public class LoadCategoryProductsUseCase extends UseCase<List<Product>> {

    IProductRepository productRepository;

    String text;
    int start;
    int end;
    SubCategory category;
    String order;
    String orderType;
    boolean image;
    double minPrice;
    double maxPrice;
    ProductState state;
    long province;

    @Inject
    public LoadCategoryProductsUseCase(@Named(EXCECUTOR_THREAD_NAMED) Scheduler executorThread,
                                       @Named(UI_THREAD_NAMED) Scheduler uiThread,
                                       IProductRepository productRepository) {

        super(executorThread, uiThread);
        this.productRepository = productRepository;
    }

    public void setParams(String text, int start, int end,
                          SubCategory category, String order, String orderType,
                          boolean image, double minPrice, double maxPrice,
                          ProductState state, long province){
        this.text = text;
        this.end = end;
        this.start = start;
        this.category = category;
        this.order = order;
        this.orderType = orderType;
        this.image = image;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.state = state;
        this.province = province;
    }

    @Override
    protected Observable<List<Product>> createObservableCaseUse() {
        return productRepository.loadProductsByCategory(text, start, end,
                category, order, orderType, image, minPrice, maxPrice, state, province);
    }
}
