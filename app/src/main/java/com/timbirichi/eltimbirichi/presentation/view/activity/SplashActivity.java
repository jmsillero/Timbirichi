package com.timbirichi.eltimbirichi.presentation.view.activity;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.DashPathEffect;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.view.base.BaseActivity;
import com.timbirichi.eltimbirichi.presentation.view_model.DatabaseViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.DatabaseViewModelFactory;
import com.timbirichi.eltimbirichi.utils.Utils;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private final int TELEPHONY = 0;
    Handler handler;
    Runnable task;

    @Inject
    DatabaseViewModelFactory databaseViewModelFactory;
    DatabaseViewModel databaseViewModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initButterNife();

        setupDatabaseViewModel();
        setupCheckAndCopyDatabase();

        handler = new Handler();
        task = new Runnable() {
            @Override
            public void run() {
                databaseViewModel.checkPreferences();
               // databaseViewModel.getDbPath();
            }
        };

        if( Build.VERSION.SDK_INT >= 23) {
            checkPerissions();
        } else {
            databaseViewModel.checkPreferences();
        }
    }

    private void setupCheckAndCopyDatabase(){
        databaseViewModel.checkPreferences.observe(this, new Observer<Response<Boolean>>() {
            @Override
            public void onChanged(@Nullable Response<Boolean> booleanResponse) {
                switch (booleanResponse.status){
                    case ERROR:
                        showErrorDialog(getString(R.string.check_reference_error));
                        break;
                }
            }
        });

        databaseViewModel.copyDatabase.observe(this, new Observer<Response<Boolean>>() {
            @Override
            public void onChanged(@Nullable Response<Boolean> booleanResponse) {
                switch (booleanResponse.status){
                    case ERROR:
                        showErrorDialog(getString(R.string.copy_error));
                        break;
                }
            }
        });
    }

    private void setupDatabaseViewModel(){
        databaseViewModel = ViewModelProviders.of(this, databaseViewModelFactory).get(DatabaseViewModel.class);

        databaseViewModel.databaseCheck.observe(this, new Observer<Response<Boolean>>() {
            @Override
            public void onChanged(@Nullable Response<Boolean> booleanResponse) {
                switch (booleanResponse.status){
                    case LOADING:
                        break;

                    case SUCCESS:
                        if(booleanResponse.data){
                            databaseViewModel.getMetaInformation();
                        } else{
                            openUpdateActivity();
                        }
                        break;

                    case ERROR:
                        openUpdateActivity();
                        break;
                }
            }
        });


        databaseViewModel.databasePath.observe(this, new Observer<Response<String>>() {
            @Override
            public void onChanged(@Nullable Response<String> stringResponse) {
                switch (stringResponse.status){
                    case LOADING:
                        break;

                    case SUCCESS:
                        if(stringResponse.data != null && !stringResponse.data.trim().isEmpty()){
                            databaseViewModel.checkDatabase();
                        } else{
                            openUpdateActivity();
                        }
                        break;

                    case ERROR:
                        openUpdateActivity();
                        break;
                }
            }
        });


        databaseViewModel.metaInformation.observe(this, new Observer<Response<Meta>>() {
            @Override
            public void onChanged(@Nullable Response<Meta> metaResponse) {
                switch (metaResponse.status){
                    case LOADING:
                        break;

                    case SUCCESS:
                        openMainActivity(metaResponse.data);
                        break;

                    case ERROR:
                        openUpdateActivity();
                        break;
                }
            }
        });
    }


    /*PERMISSIONS*/
    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void checkPerissions(){
        String[] PERMISSIONS = {
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, TELEPHONY);
        } else{
            databaseViewModel.checkPreferences();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == TELEPHONY){
            for (int i = 0; i< permissions.length; i++){
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    finish();
                    return;
                }
            }

            databaseViewModel.checkPreferences();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openMainActivity(Meta meta){
        // todo: pasar la informacion meta de la base de datos al activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(UpdateActivity.EXTRA_META_INFORMATION, meta);
        startActivity(intent);
        finish();
    }


    private void openUpdateActivity(){
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra(UpdateActivity.EXTRA_EXIST_DATABASE, false);
        startActivity(intent);
        finish();
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }
}
