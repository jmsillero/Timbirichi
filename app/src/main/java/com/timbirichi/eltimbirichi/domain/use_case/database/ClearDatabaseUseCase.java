package com.timbirichi.eltimbirichi.domain.use_case.database;

import com.timbirichi.eltimbirichi.domain.repository.IProductRepository;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.timbirichi.eltimbirichi.utils.Utils.DB_PREFERENCES;
import static com.timbirichi.eltimbirichi.utils.Utils.EXCECUTOR_THREAD_NAMED;
import static com.timbirichi.eltimbirichi.utils.Utils.UI_THREAD_NAMED;

public class ClearDatabaseUseCase extends UseCase<Boolean> {

    IProductRepository productRepository;

    @Inject
    public ClearDatabaseUseCase(@Named(EXCECUTOR_THREAD_NAMED)Scheduler executorThread,
                                @Named(UI_THREAD_NAMED)Scheduler uiThread,
                                IProductRepository productRepository) {
        super(executorThread, uiThread);
        this.productRepository = productRepository;
    }

    @Override
    protected Observable<Boolean> createObservableCaseUse() {
        return productRepository.cleaDatabase(DB_PREFERENCES);
    }
}
