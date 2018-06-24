package com.timbirichi.eltimbirichi.dagger.module;

import android.app.Activity;
import android.content.Context;

import com.timbirichi.eltimbirichi.dagger.scope.ActivityScope;
import com.timbirichi.eltimbirichi.domain.use_case.category.GetCategoriesUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.CheckDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.CheckPreferencesDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.ClearDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.CopyDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.GetDbPathUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.GetMetaInformationUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.SaveDbPathUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.LoadCategoryProductsUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.province.GetProvincesUseCase;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.CategoryViewModelFactory;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.DatabaseViewModelFactory;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.ProductViewModelFactory;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.ProvinceViewModelFactory;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.timbirichi.eltimbirichi.utils.Utils.EXCECUTOR_THREAD_NAMED;
import static com.timbirichi.eltimbirichi.utils.Utils.UI_THREAD_NAMED;


/**
 * Created by JM on 11/18/2017.
 */

@Module
public class ActivityModule {
    Activity mActivity;

    public ActivityModule(Activity mActivity) {
        this.mActivity = mActivity;
    }


    @Provides
    @ActivityScope
    Context provideContext(){
        return mActivity;
    }

    @Provides
    @ActivityScope
    Context provideActivity(){
        return mActivity;
    }

    @Provides
    @Named(EXCECUTOR_THREAD_NAMED)
    Scheduler provideExecutorThread() {
        return Schedulers.io();
    }

    @Provides
    @Named(UI_THREAD_NAMED)
    Scheduler provideUiThread() {
        return AndroidSchedulers.mainThread();
    }


    @Provides
    CategoryViewModelFactory provideCategoryViewModelFactory(GetCategoriesUseCase categoriesUseCase) {
        return new CategoryViewModelFactory(categoriesUseCase);
    }

    @Provides
    ProvinceViewModelFactory provideProvinceViewModelFactory(GetProvincesUseCase pronvincesUseCase){
        return new ProvinceViewModelFactory(pronvincesUseCase);
    }

    @Provides
    DatabaseViewModelFactory provideBatabaseViewModelFactory(CheckDatabaseUseCase checkDatabaseUseCase,
                                                             CheckPreferencesDatabaseUseCase checkPreferencesDatabaseUseCase,
                                                             ClearDatabaseUseCase clearDatabaseUseCase,
                                                             CopyDatabaseUseCase copyDatabaseUseCase,
                                                             GetDbPathUseCase getDbPathUseCase,
                                                             GetMetaInformationUseCase getMetaInformationUseCase,
                                                             SaveDbPathUseCase saveDbPathUseCase){

        return new DatabaseViewModelFactory(checkDatabaseUseCase,
                checkPreferencesDatabaseUseCase,
                clearDatabaseUseCase,
                copyDatabaseUseCase,
                getDbPathUseCase,
                getMetaInformationUseCase,
                saveDbPathUseCase);
    }


    @Provides
    ProductViewModelFactory providePProductViewModelFactory(LoadCategoryProductsUseCase loadCategoryProductsUseCase){
        return new ProductViewModelFactory(loadCategoryProductsUseCase);
    }




}
