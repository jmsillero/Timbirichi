package com.timbirichi.eltimbirichi.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;

import com.timbirichi.eltimbirichi.ElTimbirichiApplication;
import com.timbirichi.eltimbirichi.data.repository.BannerRepository;
import com.timbirichi.eltimbirichi.data.repository.CategoryRepository;
import com.timbirichi.eltimbirichi.data.repository.MetaRepository;
import com.timbirichi.eltimbirichi.data.repository.ProductRepository;
import com.timbirichi.eltimbirichi.data.repository.ProvinceRepository;
import com.timbirichi.eltimbirichi.data.repository.VersionRepository;
import com.timbirichi.eltimbirichi.data.service.api.ITimbirichiService;
import com.timbirichi.eltimbirichi.data.service.local.LocalDataBase;
import com.timbirichi.eltimbirichi.data.service.local.PreferencesDataBase;
import com.timbirichi.eltimbirichi.domain.repository.IBannerRepository;
import com.timbirichi.eltimbirichi.domain.repository.ICategoryRepository;
import com.timbirichi.eltimbirichi.domain.repository.IMetaRepository;
import com.timbirichi.eltimbirichi.domain.repository.IProductRepository;
import com.timbirichi.eltimbirichi.domain.repository.IProvinceRepository;
import com.timbirichi.eltimbirichi.domain.repository.IVersionRepository;
import com.timbirichi.eltimbirichi.utils.Utils;

import junit.runner.Version;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.timbirichi.eltimbirichi.utils.Utils.LOCAL_DATABASE_NAMED;
import static com.timbirichi.eltimbirichi.utils.Utils.PREFERENCES_DATABASE_NAMED;
import static com.timbirichi.eltimbirichi.utils.Utils.removeLastDirectory;


/**
 * Created by JM on 11/18/2017.
 */

@Module
public class ApplicationModule {
    ElTimbirichiApplication mApplication;

    public ApplicationModule(ElTimbirichiApplication mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Utils.TIMBIRICHI_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    ITimbirichiService createLaChopiService() {
        return createRetrofit()
                .create(ITimbirichiService.class);
    }


    @Provides
    @Singleton
    Context provideContext(){
        return this.mApplication;
    }

    @Provides
    @Named(LOCAL_DATABASE_NAMED)
    LocalDataBase provideLocalDatabase(Context context){
        return new LocalDataBase(context);
    }


    @Provides
    @Named(PREFERENCES_DATABASE_NAMED)
    PreferencesDataBase providePreferencesDatabase(Context context){
        return new PreferencesDataBase(context);
    }

    @Provides
    @Singleton
    SharedPreferences providePreferences(){
        return mApplication.getSharedPreferences(Utils.PREF_DIR, Context.MODE_PRIVATE);
    }


    /*Repositories*/
    @Singleton
    @Provides
    ICategoryRepository provideCategoryRepository(CategoryRepository repository){
        return repository;
    }

    @Singleton
    @Provides
    IMetaRepository provideMetaRepository(MetaRepository repository){
        return repository;
    }


    @Singleton
    @Provides
    IProductRepository provideProductRepository(ProductRepository repository){
        return repository;
    }


    @Singleton
    @Provides
    IProvinceRepository provideProvinceRepository(ProvinceRepository repository){
        return repository;
    }

    @Singleton
    @Provides
    IBannerRepository provideBannerRepository(BannerRepository repository){
        return repository;
    }

    @Singleton
    @Provides
    IVersionRepository provideVersionRepository(VersionRepository versionRepository){return versionRepository;}



}
