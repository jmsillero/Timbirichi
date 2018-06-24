package com.timbirichi.eltimbirichi.data.repository;

import com.timbirichi.eltimbirichi.data.datastore.local_sqlite.ProvinceDataStore;
import com.timbirichi.eltimbirichi.data.model.Province;
import com.timbirichi.eltimbirichi.domain.repository.IProvinceRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class ProvinceRepository implements IProvinceRepository {

    ProvinceDataStore provinceDataStore;

    @Inject
    public ProvinceRepository(ProvinceDataStore provinceDataStore) {
        this.provinceDataStore = provinceDataStore;
    }

    @Override
    public Observable<List<Province>> getProvinces() {
        return provinceDataStore.getProvinces();
    }
}
