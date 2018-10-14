package com.timbirichi.eltimbirichi.presentation.view.base;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.downloader.PRDownloader;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.timbirichi.eltimbirichi.BuildConfig;
import com.timbirichi.eltimbirichi.ElTimbirichiApplication;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.dagger.component.ActivityComponent;
import com.timbirichi.eltimbirichi.dagger.component.DaggerActivityComponent;
import com.timbirichi.eltimbirichi.dagger.module.ActivityModule;
import com.timbirichi.eltimbirichi.data.service.local.LocalDataBase;
import com.timbirichi.eltimbirichi.presentation.view.activity.UpdateActivity;
import com.timbirichi.eltimbirichi.utils.Utils;

import java.io.File;
import java.util.Calendar;

import butterknife.ButterKnife;

import static com.timbirichi.eltimbirichi.presentation.view.activity.UpdateActivity.APP_PATH;
import static com.timbirichi.eltimbirichi.presentation.view.activity.UpdateActivity.DB_PATH;

/**
 * Created by JM on 11/18/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInject();


        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {



            @Override

            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e("error", ex.toString());
                handleUncaughtException(thread, ex);
            }

        });

    }


    public void handleUncaughtException (Thread thread, Throwable e)
    {
        String stackTrace = Log.getStackTraceString(e);
        String message = e.getMessage();
        Intent intent = new Intent (Intent.ACTION_SEND);

        intent.setType("message/rfc822");
        intent.putExtra (Intent.EXTRA_EMAIL, new String[] {"juanmiguel87@gmail.com"});
        intent.putExtra (Intent.EXTRA_SUBJECT, "Timbirichi v1.0.1 Crash log file");
        intent.putExtra (Intent.EXTRA_TEXT, stackTrace);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);

    }


//    private Thread.UncaughtExceptionHandler handleAppCrash =
//            new Thread.UncaughtExceptionHandler() {
//                @Override
//                public void uncaughtException(Thread thread, Throwable ex) {
//                    Log.e("error", ex.toString());
//                    //send email here
//                }
//            };

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


    public void showDownloadErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_error);


        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startDownload();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PRDownloader.cancelAll();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void showInfoDonwloadDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.has_new_version))
                .setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_update);

        builder.setPositiveButton(R.string.install, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startDownload();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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

    protected ProgressDialog mProgressDialog;

    protected void showLoadingDialog(String message){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle(R.string.app_name);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void showDownloadDialog(String message){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMax(100);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setTitle(R.string.app_name);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void setProgressDialogMessage(String message){
        if(mProgressDialog == null) {
            showDownloadDialog(message);
        }
        mProgressDialog.setMessage(message);
    }

    protected ProgressDialog getmProgressDialog(){
        return mProgressDialog;
    }



    protected void hideProgressDialog(){
        if(mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

    }



    protected void initButterNife(){
        ButterKnife.bind(this);
    }

    public abstract  void initInject();

    public abstract  void startDownload();
}
