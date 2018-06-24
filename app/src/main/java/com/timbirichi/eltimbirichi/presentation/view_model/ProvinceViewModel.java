package com.timbirichi.eltimbirichi.presentation.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timbirichi.eltimbirichi.data.model.Province;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCaseObserver;
import com.timbirichi.eltimbirichi.domain.use_case.province.GetProvincesUseCase;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.Status;

import java.util.List;

public class ProvinceViewModel extends ViewModel {

    GetProvincesUseCase getProvincesUseCase;

    public final MutableLiveData<Response<List<Province>>> provinces = new MutableLiveData<>();

    public ProvinceViewModel(GetProvincesUseCase getProvincesUseCase) {
        this.getProvincesUseCase = getProvincesUseCase;
    }

    public void getProvinces(){
        getProvincesUseCase.execute(new GetProvincesObserver());
    }


    /**
     * Observers
     * */
    public final class GetProvincesObserver extends UseCaseObserver<List<Province>>{
        public GetProvincesObserver() {
        }

        @Override
        public void onNext(List<Province> provs) {
            provinces.setValue(new Response<List<Province>>(Status.SUCCESS, provs, null));
        }

        @Override
        public void onError(Throwable throwable) {
            provinces.setValue(new Response<List<Province>>(Status.ERROR, null, throwable));
        }
    }


}
