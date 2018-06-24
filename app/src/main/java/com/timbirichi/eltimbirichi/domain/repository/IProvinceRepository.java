package com.timbirichi.eltimbirichi.domain.repository;

import com.timbirichi.eltimbirichi.data.model.Province;

import java.util.List;

import io.reactivex.Observable;

public interface IProvinceRepository {

    Observable<List<Province>> getProvinces();
}
