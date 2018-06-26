package com.timbirichi.eltimbirichi.presentation.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.Province;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCaseObserver;
import com.timbirichi.eltimbirichi.domain.use_case.category.GetCategoriesUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.product.LoadRandomSubCategoriesUseCase;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.Status;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    GetCategoriesUseCase getCategoriesUseCase;
    LoadRandomSubCategoriesUseCase loadRandomSubCategoriesUseCase;

    public final MutableLiveData<Response<List<Category>>> categories = new MutableLiveData<>();
    public final MutableLiveData<Response<List<SubCategory>>> subcategories = new MutableLiveData<>();

    public CategoryViewModel(GetCategoriesUseCase getCategoriesUseCase,
                             LoadRandomSubCategoriesUseCase loadRandomSubCategoriesUseCase) {
        this.getCategoriesUseCase = getCategoriesUseCase;
        this.loadRandomSubCategoriesUseCase = loadRandomSubCategoriesUseCase;
    }

    public void getCategories(){
        getCategoriesUseCase.execute(new GetCategoriesObserver());
    }

    public void loadRandomSubcategories(int limit){
        subcategories.setValue(new Response<List<SubCategory>>(Status.LOADING, null, null));
        loadRandomSubCategoriesUseCase.setParams(limit);
        loadRandomSubCategoriesUseCase.execute(new LoadRandomSubcategories());
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

    public final class LoadRandomSubcategories extends UseCaseObserver<List<SubCategory>>{
        public LoadRandomSubcategories() {
        }

        @Override
        public void onNext(List<SubCategory> categories) {
            subcategories.setValue(new Response<List<SubCategory>>(Status.SUCCESS, categories, null));
        }

        @Override
        public void onError(Throwable throwable) {
            subcategories.setValue(new Response<List<SubCategory>>(Status.ERROR, null, throwable));
        }
    }

}
