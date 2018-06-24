package com.timbirichi.eltimbirichi.domain.use_case.category;

import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.domain.repository.ICategoryRepository;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCase;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.timbirichi.eltimbirichi.utils.Utils.EXCECUTOR_THREAD_NAMED;
import static com.timbirichi.eltimbirichi.utils.Utils.UI_THREAD_NAMED;

public class GetCategoriesUseCase extends UseCase<List<Category>> {

    ICategoryRepository categoryRepository;

    @Inject
    public GetCategoriesUseCase(@Named (EXCECUTOR_THREAD_NAMED) Scheduler executorThread,
                                @Named (UI_THREAD_NAMED)Scheduler uiThread,
                                ICategoryRepository categoryRepository) {
        super(executorThread, uiThread);
        this.categoryRepository = categoryRepository;
    }


    @Override
    protected Observable<List<Category>> createObservableCaseUse() {
        return categoryRepository.getCategories();
    }
}
