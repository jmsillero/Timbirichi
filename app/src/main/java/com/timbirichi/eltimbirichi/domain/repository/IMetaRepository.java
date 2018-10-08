package com.timbirichi.eltimbirichi.domain.repository;

import com.timbirichi.eltimbirichi.data.model.Meta;

import io.reactivex.Observable;

/**
 * Repositorio para trabajar con la informacion meta y global de la aplicacion
 *
 * Direccion de la base de datos...
 * Fecha de la base de datos...
 * Codigo de la base de datos...
 * */

public interface IMetaRepository {

    Observable<Boolean> saveDatabasePath(String path);
    Observable<String> getDatabasePath();
    Observable<Meta> getMetaInformation();
    Observable<Boolean> checkDatabse();
    Observable<Meta>getDatabaseDate(String path);
}
