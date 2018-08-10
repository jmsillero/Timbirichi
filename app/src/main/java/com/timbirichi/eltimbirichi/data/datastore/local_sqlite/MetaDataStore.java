package com.timbirichi.eltimbirichi.data.datastore.local_sqlite;

import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.data.service.local.LocalDataBase;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;

import static com.timbirichi.eltimbirichi.utils.Utils.LOCAL_DATABASE_NAMED;

public class MetaDataStore {

    LocalDataBase localDataBase;

    @Inject
    public MetaDataStore(@Named(LOCAL_DATABASE_NAMED) LocalDataBase localDataBase) {
        this.localDataBase = localDataBase;
    }


    /**
     * Devuelve la informacion de la base de datos
     * @return Meta Information
     */
    public Observable<Meta> getMetaInformation(){
          return Observable.create(new ObservableOnSubscribe<Meta>() {
              @Override
              public void subscribe(ObservableEmitter<Meta> e) throws Exception {
                  e.onNext(localDataBase.getMetaInformation());
                  e.onComplete();
              }
          });
    }

    /**
     * Chequea si la base de datos es la correcta teniendo en cuenta
     * el codigo
     * @return true si la base de datos es la correcta
     */
    public Observable<Boolean> checkDatabase(){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(localDataBase.open());
                e.onComplete();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }

    /**
     * Devuelve la fecha de una base de datos en cso de que sea
     * valida para timbirichi app.
     * @param path
     * @return
     */
    public Observable<String> getDatabaseDate(final String path){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext(localDataBase.getDatabaseDate(path));
                e.onComplete();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
    }



}
