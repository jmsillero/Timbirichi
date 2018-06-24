package com.timbirichi.eltimbirichi.data.datastore.preferences;

import android.content.SharedPreferences;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class PreferencesDataStore {

    public static final String KEY_DATABASE= "com.timbirichi.preferences_datastore.databasepath";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Inject
    public PreferencesDataStore(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }

    public Observable<Boolean> saveDatabasePath(String path){
        editor.putString(KEY_DATABASE, path);
        final boolean resp = editor.commit();

        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(resp);
                e.onComplete();
            }
        });
    }

    public Observable<String> getDatabasePath(){
        final String path = sharedPreferences.getString(KEY_DATABASE, "");
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext(path);
                e.onComplete();
            }
        });
    }
}
