package com.timbirichi.eltimbirichi.domain.repository;


import com.timbirichi.eltimbirichi.data.model.Category;

import java.util.List;

import io.reactivex.Observable;

public interface ICategoryRepository {

    /**
     * @return Lista de categorias ordenadas
     */
    Observable<List<Category>> getCategories();

}
