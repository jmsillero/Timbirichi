package com.timbirichi.eltimbirichi.data.datastore.local_sqlite;

import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.service.local.LocalDataBase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import static com.timbirichi.eltimbirichi.utils.Utils.LOCAL_DATABASE_NAMED;

public class CategoryDataStore {

    LocalDataBase localDataBase;

    @Inject
    public CategoryDataStore(@Named(LOCAL_DATABASE_NAMED) LocalDataBase localDataBase) {
        this.localDataBase = localDataBase;
    }


    public Observable<List<Category>> getCategories(){
        return Observable.create(new ObservableOnSubscribe<List<Category>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Category>> e) throws Exception {
                e.onNext(localDataBase.getCategories());
                e.onComplete();
            }
        });
    }

}
