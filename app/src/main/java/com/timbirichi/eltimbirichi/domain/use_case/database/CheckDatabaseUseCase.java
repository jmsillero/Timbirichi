package com.timbirichi.eltimbirichi.domain.use_case.database;

import com.timbirichi.eltimbirichi.domain.repository.IMetaRepository;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.timbirichi.eltimbirichi.utils.Utils.EXCECUTOR_THREAD_NAMED;
import static com.timbirichi.eltimbirichi.utils.Utils.UI_THREAD_NAMED;

public class CheckDatabaseUseCase extends UseCase<Boolean> {
    IMetaRepository metaRepository;

    @Inject
    public CheckDatabaseUseCase(@Named(EXCECUTOR_THREAD_NAMED)Scheduler executorThread,
                                @Named(UI_THREAD_NAMED)Scheduler uiThread, IMetaRepository metaRepository) {
        super(executorThread, uiThread);
        this.metaRepository = metaRepository;
    }

    String path;

    public void setParams(String path){
        this.path = path;
    }

    @Override
    protected Observable<Boolean> createObservableCaseUse() {
        return metaRepository.checkDatabse();
    }


}
