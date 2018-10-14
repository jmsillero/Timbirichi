package com.timbirichi.eltimbirichi.dagger.component;

import android.content.Context;

import com.timbirichi.eltimbirichi.dagger.module.ApplicationModule;
import com.timbirichi.eltimbirichi.domain.repository.IBannerRepository;
import com.timbirichi.eltimbirichi.domain.repository.ICategoryRepository;
import com.timbirichi.eltimbirichi.domain.repository.IMetaRepository;
import com.timbirichi.eltimbirichi.domain.repository.IProductRepository;
import com.timbirichi.eltimbirichi.domain.repository.IProvinceRepository;
import com.timbirichi.eltimbirichi.domain.repository.IVersionRepository;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by JM on 11/18/2017.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    Context context();

    ICategoryRepository categoryRepository();
    IMetaRepository metaRepository();
    IProductRepository productRepository();
    IProvinceRepository provinceRepository();
    IBannerRepository provideBannerRepository();
    IVersionRepository provideVersionRepository();




}
