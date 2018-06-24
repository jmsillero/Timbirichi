package com.timbirichi.eltimbirichi.domain.use_case.database;

import com.timbirichi.eltimbirichi.domain.repository.IMetaRepository;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.timbirichi.eltimbirichi.utils.Utils.EXCECUTOR_THREAD_NAMED;
import static com.timbirichi.eltimbirichi.utils.Utils.UI_THREAD_NAMED;

public class GetDbPathUseCase extends UseCase<String> {

    IMetaRepository metaRepository;

    @Inject
    public GetDbPathUseCase(@Named(EXCECUTOR_THREAD_NAMED)Scheduler executorThread,
                            @Named(UI_THREAD_NAMED)Scheduler uiThread, IMetaRepository metaRepository) {
        super(executorThread, uiThread);
        this.metaRepository = metaRepository;
    }

    @Override
    protected Observable<String> createObservableCaseUse() {
        return metaRepository.getDatabasePath();
    }
}
