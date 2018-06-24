package com.timbirichi.eltimbirichi.dagger.component;

import android.app.Activity;

import com.timbirichi.eltimbirichi.dagger.module.FragmentModule;
import com.timbirichi.eltimbirichi.dagger.scope.FragmentScope;
import com.timbirichi.eltimbirichi.presentation.view.fragment.CategoryFragment;
import com.timbirichi.eltimbirichi.presentation.view.fragment.CoverPageFragment;
import com.timbirichi.eltimbirichi.presentation.view.fragment.ProductFragment;

import dagger.Component;


/**
 * Created by JM on 11/18/2017.
 */

@FragmentScope
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();

    void inject(CoverPageFragment fragment);
    void inject(CategoryFragment fragment);
    void inject(ProductFragment fragment);

}
