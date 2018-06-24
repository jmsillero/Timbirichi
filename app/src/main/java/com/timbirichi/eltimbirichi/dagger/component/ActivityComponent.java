package com.timbirichi.eltimbirichi.dagger.component;




import com.timbirichi.eltimbirichi.dagger.module.ActivityModule;
import com.timbirichi.eltimbirichi.dagger.scope.ActivityScope;
import com.timbirichi.eltimbirichi.presentation.view.activity.DetailActivity;
import com.timbirichi.eltimbirichi.presentation.view.activity.MainActivity;
import com.timbirichi.eltimbirichi.presentation.view.activity.SplashActivity;
import com.timbirichi.eltimbirichi.presentation.view.activity.UpdateActivity;

import dagger.Component;

/**
 * Created by JM on 11/18/2017.
 */

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

   // Activity getActivity();
    void inject(SplashActivity activity);
    void inject(MainActivity activity);
    void inject(DetailActivity activity);
    void inject(UpdateActivity activity);

}
