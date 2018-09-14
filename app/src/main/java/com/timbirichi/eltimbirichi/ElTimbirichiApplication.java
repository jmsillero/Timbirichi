package com.timbirichi.eltimbirichi;

import android.app.Application;

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
    }


    public static synchronized ElTimbirichiApplication getmInstance(){return  mInstance;}


    public static ApplicationComponent getComponent(){
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(mInstance)).build();
    }
}
