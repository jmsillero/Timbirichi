package com.timbirichi.eltimbirichi.data.datastore.preferences.sqlite;


import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.data.service.local.PreferencesDataBase;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.timbirichi.eltimbirichi.utils.Utils.PREFERENCES_DATABASE_NAMED;

public class DbPreferencesDataStore {

    PreferencesDataBase preferencesDataBase;

    @Inject
    public DbPreferencesDataStore(@Named(PREFERENCES_DATABASE_NAMED) PreferencesDataBase preferencesDataBase) {
        this.preferencesDataBase = preferencesDataBase;
    }

    public Observable<Boolean>saveProduct(final Product productBo){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(preferencesDataBase.saveProductToFavorite(productBo));
                e.onComplete();
            }
        });
    }

    public Observable<Product> getProductById(final long id){
        return Observable.create(new ObservableOnSubscribe<Product>() {
            @Override
            public void subscribe(ObservableEmitter<Product> e) throws Exception {
             //   e.onNext(preferencesDataBase.getProductById(id));
                e.onComplete();
            }
        });
    }


    public Observable<List<Product>> getProducts(final String text, final int start, final int end,
                                                 final String order, final String orderType,
                                                 final boolean image, final double minPrice, final double maxPrice,
                                                 final ProductState state, final long province){
        return Observable.create(new ObservableOnSubscribe<List<Product>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Product>> e) throws Exception {
                e.onNext(preferencesDataBase.loadProducts(text, start, end,
                order, orderType,image, minPrice, maxPrice, state, province));

                e.onComplete();
            }
        });
    }

    public Observable<Boolean> checkDatabase(){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(preferencesDataBase.checkDataBase());
                e.onComplete();
            }
        });
    }

    public Observable<Boolean> copyDatabase(){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(preferencesDataBase.copyDataBase());
                e.onComplete();
            }
        });
    }

    public Observable<Boolean> findProductById(final long id){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
              e.onNext(preferencesDataBase.findProductById(id));
              e.onComplete();
            }
        });
    }

    public Observable<Boolean> removeFromFavorite(final long id){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(preferencesDataBase.removeFromFavorites(id));
                e.onComplete();
            }
        });
    }

    public Observable<Boolean> clearDatabase(){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(preferencesDataBase.clearDatabase());
                e.onComplete();
            }
        });
    }

}
