package com.timbirichi.eltimbirichi.data.datastore.local_sqlite;

import com.timbirichi.eltimbirichi.data.model.Province;
import com.timbirichi.eltimbirichi.data.service.local.LocalDataBase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class ProvinceDataStore {

    LocalDataBase localDataBase;

    @Inject
    public ProvinceDataStore(LocalDataBase localDataBase) {
        this.localDataBase = localDataBase;
    }

    public Observable<List<Province>> getProvinces(){
        return Observable.create(new ObservableOnSubscribe<List<Province>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Province>> e) throws Exception {
                e.onNext(localDataBase.getProvinces());
                e.onComplete();
            }
        });
    }
}
