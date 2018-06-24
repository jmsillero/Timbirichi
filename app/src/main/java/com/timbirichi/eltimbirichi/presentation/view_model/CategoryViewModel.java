package com.timbirichi.eltimbirichi.presentation.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.Province;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCaseObserver;
import com.timbirichi.eltimbirichi.domain.use_case.category.GetCategoriesUseCase;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.Status;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    GetCategoriesUseCase getCategoriesUseCase;

    public final MutableLiveData<Response<List<Category>>> categories = new MutableLiveData<>();

    public CategoryViewModel(GetCategoriesUseCase getCategoriesUseCase) {
        this.getCategoriesUseCase = getCategoriesUseCase;
    }

    public void getCategories(){
        getCategoriesUseCase.execute(new GetCategoriesObserver());
    }


    /**Observers**/
    public final class GetCategoriesObserver extends UseCaseObserver<List<Category>>{
        public GetCategoriesObserver() {
        }

        @Override
        public void onNext(List<Category> cats) {
            categories.setValue(new Response<List<Category>>(Status.SUCCESS, cats, null));
        }

        @Override
        public void onError(Throwable throwable) {
            categories.setValue(new Response<List<Category>>(Status.ERROR, null, throwable));
        }
    }

}
