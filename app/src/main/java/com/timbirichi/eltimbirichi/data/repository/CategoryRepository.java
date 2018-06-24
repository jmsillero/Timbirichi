package com.timbirichi.eltimbirichi.data.repository;

import com.timbirichi.eltimbirichi.data.datastore.local_sqlite.CategoryDataStore;
import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.domain.repository.ICategoryRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class CategoryRepository implements ICategoryRepository {

    CategoryDataStore categoryDataStore;

    @Inject
    public CategoryRepository(CategoryDataStore categoryDataStore) {
        this.categoryDataStore = categoryDataStore;
    }

    @Override
    public Observable<List<Category>> getCategories() {
        return categoryDataStore.getCategories();
    }


}
