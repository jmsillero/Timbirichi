package com.timbirichi.eltimbirichi.presentation.view_model.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.timbirichi.eltimbirichi.domain.use_case.province.GetProvincesUseCase;
import com.timbirichi.eltimbirichi.presentation.view_model.ProvinceViewModel;

import javax.inject.Inject;

public class ProvinceViewModelFactory implements ViewModelProvider.Factory {

    GetProvincesUseCase pronvincesUseCase;

    @Inject
    public ProvinceViewModelFactory(GetProvincesUseCase pronvincesUseCase) {
        this.pronvincesUseCase = pronvincesUseCase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProvinceViewModel.class)) {
            return (T) new ProvinceViewModel(pronvincesUseCase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
