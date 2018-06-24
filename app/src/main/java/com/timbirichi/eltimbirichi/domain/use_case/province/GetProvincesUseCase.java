package com.timbirichi.eltimbirichi.domain.use_case.province;

import com.timbirichi.eltimbirichi.data.model.Province;
import com.timbirichi.eltimbirichi.domain.repository.IProvinceRepository;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCase;
import com.timbirichi.eltimbirichi.utils.Utils;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

public class GetProvincesUseCase extends UseCase<List<Province>> {

    IProvinceRepository provinceRepository;

    @Inject
    public GetProvincesUseCase(@Named(Utils.EXCECUTOR_THREAD_NAMED) Scheduler executorThread,
                               @Named(Utils.UI_THREAD_NAMED) Scheduler uiThread,
                               IProvinceRepository provinceRepository) {
        super(executorThread, uiThread);
        this.provinceRepository = provinceRepository;
    }

    @Override
    protected Observable<List<Province>> createObservableCaseUse() {
        return provinceRepository.getProvinces();
    }
}
