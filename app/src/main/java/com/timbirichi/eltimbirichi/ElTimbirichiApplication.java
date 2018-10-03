package com.timbirichi.eltimbirichi;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.timbirichi.eltimbirichi.dagger.component.ApplicationComponent;
import com.timbirichi.eltimbirichi.dagger.component.DaggerApplicationComponent;
import com.timbirichi.eltimbirichi.dagger.module.ApplicationModule;

public class ElTimbirichiApplication extends Application {
    private static  ElTimbirichiApplication mInstance;

    private ElTimbirichiApplication mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                ))
                .commit();
    }


    public static synchronized ElTimbirichiApplication getmInstance(){return  mInstance;}


    public static ApplicationComponent getComponent(){
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(mInstance)).build();
    }
}
