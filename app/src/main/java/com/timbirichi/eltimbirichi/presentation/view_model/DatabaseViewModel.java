package com.timbirichi.eltimbirichi.presentation.view_model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.data.service.local.LocalDataBase;
import com.timbirichi.eltimbirichi.domain.use_case.base.UseCaseObserver;
import com.timbirichi.eltimbirichi.domain.use_case.database.CheckDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.CheckPreferencesDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.ClearDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.CopyDatabaseUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.GetDbPathUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.GetMetaInformationUseCase;
import com.timbirichi.eltimbirichi.domain.use_case.database.SaveDbPathUseCase;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.Status;

public class DatabaseViewModel extends ViewModel {

    CheckDatabaseUseCase checkDatabaseUseCase;
    CheckPreferencesDatabaseUseCase checkPreferencesDatabaseUseCase;
    ClearDatabaseUseCase clearDatabaseUseCase;
    CopyDatabaseUseCase copyDatabaseUseCase;
    GetDbPathUseCase getDbPathUseCase;
    GetMetaInformationUseCase getMetaInformationUseCase;
    SaveDbPathUseCase saveDbPathUseCase;


    public final MutableLiveData<Response<Meta>> metaInformation = new MutableLiveData<>();
    public final MutableLiveData<Response<String>> databasePath = new MutableLiveData<>();
    public final MutableLiveData<Response<Boolean>> databaseCheck = new MutableLiveData<>();
    public final MutableLiveData<Response<Boolean>> databaseSaved = new MutableLiveData<>();

    public final MutableLiveData<Response<Boolean>> checkPreferences = new MutableLiveData<>();
    public final MutableLiveData<Response<Boolean>> copyDatabase = new MutableLiveData<>();


    public DatabaseViewModel(CheckDatabaseUseCase checkDatabaseUseCase,
                             CheckPreferencesDatabaseUseCase checkPreferencesDatabaseUseCase,
                             ClearDatabaseUseCase clearDatabaseUseCase,
                             CopyDatabaseUseCase copyDatabaseUseCase,
                             GetDbPathUseCase getDbPathUseCase,
                             GetMetaInformationUseCase getMetaInformationUseCase,
                             SaveDbPathUseCase saveDbPathUseCase) {

        this.checkDatabaseUseCase = checkDatabaseUseCase;
        this.checkPreferencesDatabaseUseCase = checkPreferencesDatabaseUseCase;
        this.clearDatabaseUseCase = clearDatabaseUseCase;
        this.copyDatabaseUseCase = copyDatabaseUseCase;
        this.getDbPathUseCase = getDbPathUseCase;
        this.getMetaInformationUseCase = getMetaInformationUseCase;
        this.saveDbPathUseCase = saveDbPathUseCase;
    }

    /**
     * Ejecuta el caso de uso que obtiene la informacion meta de la base de datos
     * fecha, codigo
     */
    public void getMetaInformation(){
        getMetaInformationUseCase.execute(new GetMetaInformationObserver());
    }

    /**
     * Ejecuta el caso de uso que obtiene la direccion local de la base de datos
     * Si existe
     */
    public void getDbPath(){
        getDbPathUseCase.execute(new GetDbPathObserver());
    }

    /**
     * Chequea si en la direccion obtenida de la base de datos
     * existe el fichero base de datos y comprueba que sea la base de datos
     * correcta utilizada en timbirichi
     */
    public void checkDatabase(){
        checkDatabaseUseCase.execute(new CheckDatabaseObserver());
    }

    /**
     * Guarda la direccion local de la base de datos una ves que se selecciona
     */
    public void saveDatabasePath(String path){
        LocalDataBase.DB_PATH = path;
        saveDbPathUseCase.setPath(path);
        saveDbPathUseCase.execute(new SaveDbPathObserver());
    }

    public void checkPreferences(){
        checkPreferencesDatabaseUseCase.execute(new UseCaseObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if(!aBoolean){
                    copyDatabasePreferences();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                copyDatabasePreferences();
            }
        });
    }


    public void copyDatabasePreferences(){
        copyDatabaseUseCase.execute(new UseCaseObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                super.onNext(aBoolean);
            }

            @Override
            public void onError(Throwable throwable) {
                copyDatabase.setValue(new Response<Boolean>(Status.ERROR, null, throwable));
            }
        });
    }





    /*Observer*/


    /**
     * Observer para el resultado de la informacion meta de la base de datos.
     */
    public final class GetMetaInformationObserver extends UseCaseObserver<Meta>{
        public GetMetaInformationObserver() {
        }

        @Override
        public void onNext(Meta meta) {
            metaInformation.setValue(new Response<Meta>(Status.SUCCESS, meta, null));
        }

        @Override
        public void onError(Throwable throwable) {
            metaInformation.setValue(new Response<Meta>(Status.ERROR, null, throwable));
        }
    }


    /**
     * Observer para el chequeo de la base de datos
     */
    public final class CheckDatabaseObserver extends UseCaseObserver<Boolean>{
        public CheckDatabaseObserver() {
        }

        @Override
        public void onNext(Boolean aBoolean) {
            databaseCheck.setValue(new Response<Boolean>(Status.SUCCESS, aBoolean, null));
        }

        @Override
        public void onError(Throwable throwable) {
            databaseCheck.setValue(new Response<Boolean>(Status.ERROR, null, throwable));
        }
    }


    /**
     * Observer para obtener la direccion de la base de datos local si existe
     */
    public final class GetDbPathObserver extends UseCaseObserver<String>{
        public GetDbPathObserver() {
        }

        @Override
        public void onNext(String s) {
            databasePath.setValue(new Response<String>(Status.SUCCESS, s, null));
            LocalDataBase.DB_PATH = s;
        }

        @Override
        public void onError(Throwable throwable) {
           databasePath.setValue(new Response<String>(Status.ERROR, null, throwable));
        }
    }


    /**
     * Observer para guardar la direccion de la base de datos local
     */
    public final class SaveDbPathObserver extends UseCaseObserver<Boolean>{
        public SaveDbPathObserver() {
        }

        @Override
        public void onNext(Boolean aBoolean) {
            databaseSaved.setValue(new Response<Boolean>(Status.SUCCESS, aBoolean, null));
        }

        @Override
        public void onError(Throwable throwable) {
            databaseSaved.setValue(new Response<Boolean>(Status.ERROR, null, throwable));
        }
    }


}
