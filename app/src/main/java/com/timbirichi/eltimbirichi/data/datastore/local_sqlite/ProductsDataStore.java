package com.timbirichi.eltimbirichi.data.datastore.local_sqlite;

import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.data.service.local.LocalDataBase;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

import static com.timbirichi.eltimbirichi.utils.Utils.LOCAL_DATABASE_NAMED;

public class ProductsDataStore {

    LocalDataBase localDataBase;

    @Inject
    public ProductsDataStore( @Named(LOCAL_DATABASE_NAMED) LocalDataBase localDataBase) {
        this.localDataBase = localDataBase;
    }

    public Observable<List<Product>> loadBannerProducts(final int limit){
        return Observable.create(new ObservableOnSubscribe<List<Product>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Product>> e) throws Exception {
               // e.onNext(localDataBase.loadBannerProducts(limit));
                e.onComplete();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    public Observable<List<Product>> loadCoverPageProduct(){
        return Observable.create(new ObservableOnSubscribe<List<Product>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Product>> e) throws Exception {
               e.onNext(localDataBase.loadCoverPageProducts());
               e.onComplete();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    public Observable<List<Product>> loadProductsByCategory(final String text, final int start, final int end,
                                                            final SubCategory category, final String order, final String orderType,
                                                            final boolean image, final double minPrice, final double maxPrice,
                                                            final ProductState state, final long province){


        return Observable.create(new ObservableOnSubscribe<List<Product>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Product>> e) throws Exception {
                e.onNext( localDataBase.loadProductsByCategory(text, start, end,
                        category, order, orderType, image, minPrice, maxPrice, state, province));

                e.onComplete();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    public Observable<List<Product>> loadProductsFilteredFromCoverPage(final String text, final int start, final int end,
                                                                             final String order, final String orderType,
                                                                             final boolean image, final double minPrice, final double maxPrice){


        return Observable.create(new ObservableOnSubscribe<List<Product>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Product>> e) throws Exception {
//                e.onNext(localDataBase.loadProductsFilteredFromCoverPage(text, start, end,
//                        order, orderType, image,
//                        minPrice, maxPrice));
                e.onComplete();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    public Observable<Product> getProductById(final long productId){
        return Observable.create(new ObservableOnSubscribe<Product>() {
            @Override
            public void subscribe(ObservableEmitter<Product> e) throws Exception {
             //   e.onNext(localDataBase.getProductById(productId));
                e.onComplete();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }
}
