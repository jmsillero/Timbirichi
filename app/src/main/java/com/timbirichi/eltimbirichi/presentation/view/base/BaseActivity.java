package com.timbirichi.eltimbirichi.presentation.view.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.timbirichi.eltimbirichi.ElTimbirichiApplication;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.dagger.component.ActivityComponent;
import com.timbirichi.eltimbirichi.dagger.component.DaggerActivityComponent;
import com.timbirichi.eltimbirichi.dagger.module.ActivityModule;

import butterknife.ButterKnife;

/**
 * Created by JM on 11/18/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInject();
    }

    protected ActivityComponent getActivityComponent(){
        return DaggerActivityComponent.builder()
                .applicationComponent(ElTimbirichiApplication.getComponent())
                .activityModule( new ActivityModule(this))
                .build();

    }

    protected void openFragment(BaseFragment fragment, int id_container, boolean isBackStack){
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.replace(id_container, fragment);
        if (isBackStack){
            ft.addToBackStack(null);
        }
        ft.commit();

    }

    protected void showErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_error);


        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    ProgressDialog mProgressDialog;

    protected void showLoadingDialog(String message){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle(R.string.app_name);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void hideProgressDialog(){
        mProgressDialog.dismiss();
    }



    protected void initButterNife(){
        ButterKnife.bind(this);
    }

    public abstract  void initInject();
}
