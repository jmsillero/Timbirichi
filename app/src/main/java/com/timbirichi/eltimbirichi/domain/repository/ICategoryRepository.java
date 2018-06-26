package com.timbirichi.eltimbirichi.domain.repository;


import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.SubCategory;

import java.util.List;

import io.reactivex.Observable;

public interface ICategoryRepository {

    /**
     * @return Lista de categorias ordenadas
     */
    Observable<List<Category>> getCategories();

    Observable<List<SubCategory>> getRandomSubcategories(int limit);

}
