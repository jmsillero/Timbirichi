package com.timbirichi.eltimbirichi.presentation.view_model.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timbirichi.eltimbirichi.domain.use_case.database.CheckDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.CheckPreferencesDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.ClearDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.CopyDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.GetDbPathUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.GetMetaInformationUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.SaveDbPathUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.province.GetProvincesUseCase;
import com.timbirichi.eltimbirichi.presentation.view_model.DatabaseViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.ProvinceViewModel;

import javax.inject.Inject;

public class DatabaseViewModelFactory implements ViewModelProvider.Factory {

    CheckDatabaseUseCase checkDatabaseUseCase;
    CheckPreferencesDatabaseUseCase checkPreferencesDatabaseUseCase;
    ClearDatabaseUseCase clearDatabaseUseCase;
    CopyDatabaseUseCase copyDatabaseUseCase;
    GetDbPathUseCase getDbPathUseCase;
    GetMetaInformationUseCase getMetaInformationUseCase;
    SaveDbPathUseCase saveDbPathUseCase;

    @Inject
    public DatabaseViewModelFactory(CheckDatabaseUseCase checkDatabaseUseCase,
                                    CheckPreferencesDatabaseUseCase checkPreferencesDatabaseUseCase,
                                    ClearDatabaseUseCase clearDatabaseUseCase,
                                    CopyDatabaseUseCase copyDatabaseUseCase,
                                    GetDbPathUseCase getDbPathUseCase,
                                    GetMetaInformationUseCase getMetaInformationUseCase,
                                    SaveDbPathUseCase saveDbPathUseCase) {

        this.checkDatabaseUseCase = checkDatabaseUseCase;
        this.checkPreferencesDatabaseUseCase = checkPreferencesDatabaseUseCase;
        this.clearDatabaseUseCase = clearDatabaseUseCase;
        this.copyDatabaseUseCase = copyDatabaseUseCase;
        this.getDbPathUseCase = getDbPathUseCase;
        this.getMetaInformationUseCase = getMetaInformationUseCase;
        this.saveDbPathUseCase = saveDbPathUseCase;
    }




    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DatabaseViewModel.class)) {
            return (T) new DatabaseViewModel(checkDatabaseUseCase,
                    checkPreferencesDatabaseUseCase,
                    clearDatabaseUseCase,
                    copyDatabaseUseCase,
                    getDbPathUseCase,
                    getMetaInformationUseCase,
                    saveDbPathUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
