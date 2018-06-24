package com.timbirichi.eltimbirichi.data.repository;

import com.timbirichi.eltimbirichi.data.datastore.local_sqlite.MetaDataStore;
import com.timbirichi.eltimbirichi.data.datastore.preferences.PreferencesDataStore;
import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.domain.repository.IMetaRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class MetaRepository implements IMetaRepository {

    PreferencesDataStore preferencesDataStore;
    MetaDataStore metaDataStore;

    @Inject
    public MetaRepository(PreferencesDataStore preferencesDataStore,
                          MetaDataStore metaDataStore) {
        this.preferencesDataStore = preferencesDataStore;
        this.metaDataStore = metaDataStore;
    }

    @Override
    public Observable<Boolean> saveDatabasePath(String path) {
        return preferencesDataStore.saveDatabasePath(path);

    }

    @Override
    public Observable<String> getDatabasePath() {
        return preferencesDataStore.getDatabasePath();
    }

    @Override
    public Observable<Meta> getMetaInformation() {
        return metaDataStore.getMetaInformation();
    }

    @Override
    public Observable<Boolean> checkDatabse() {
        return metaDataStore.checkDatabase();
    }
}
