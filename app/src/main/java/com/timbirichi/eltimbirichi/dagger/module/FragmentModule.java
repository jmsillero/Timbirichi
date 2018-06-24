package com.timbirichi.eltimbirichi.dagger.module;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.timbirichi.eltimbirichi.dagger.scope.FragmentScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.timbirichi.eltimbirichi.utils.Utils.EXCECUTOR_THREAD_NAMED;
import static com.timbirichi.eltimbirichi.utils.Utils.UI_THREAD_NAMED;


/**
 * Created by JM on 11/18/2017.
 */

@Module
public class FragmentModule {

    Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Provides
    @FragmentScope
    Activity provideActivity(){
        return this.mFragment.getActivity();
    }

    @Provides
    @Named(EXCECUTOR_THREAD_NAMED)
    Scheduler provideExecutorThread() {
        return Schedulers.io();
    }

    @Provides
    @Named(UI_THREAD_NAMED)
    Scheduler provideUiThread() {
        return AndroidSchedulers.mainThread();
    }











}
