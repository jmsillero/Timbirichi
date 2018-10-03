package com.timbirichi.eltimbirichi.presentation.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.hzy.lib7z.ExtractCallback;
import com.hzy.lib7z.Z7Extractor;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.timbirichi.eltimbirichi.R;
import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.data.service.local.LocalDataBase;
import com.timbirichi.eltimbirichi.presentation.adapter.FileAdapter;
import com.timbirichi.eltimbirichi.presentation.model.FileItem;
import com.timbirichi.eltimbirichi.presentation.model.Response;
import com.timbirichi.eltimbirichi.presentation.model.Status;
import com.timbirichi.eltimbirichi.presentation.view.base.BaseActivity;
import com.timbirichi.eltimbirichi.presentation.view.custom.DividerDecoration;
import com.timbirichi.eltimbirichi.presentation.view_model.DatabaseViewModel;
import com.timbirichi.eltimbirichi.presentation.view_model.factory.DatabaseViewModelFactory;
import com.timbirichi.eltimbirichi.utils.Utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class UpdateActivity extends BaseActivity {

    public final static int FILE = 0;
    public final static int FOLDER = 1;
    public final static int EXTERNAL_STORAGE = 2;
    public final static int INTERNAL_STORAGE = 3;
    public final static int GO_BACK = 4;
    public final static int AUTOMATIC_SEARCH = 5;
    public final static int DOWNLOAD = 6;

    public final static String EXTRA_EXIST_DATABASE = "com.timbirichi.eltimbirichi.exist_database";
    public final static String EXTRA_META_INFORMATION = "com.timbirichi.eltimbirichi.meta_information";

    public final static String DB_PATH = "/timbirichidb/";


    // for download database
    public final static String DB_URL = "http://10.0.2.2/test/";
    //public final static String DB_URL = "https://www.timbirichi.com/apk/";
   // public final static String WEB_DB_NAME = "timbirichi.db";

    public final static int DOWNLOAD_COMPLETED = 0;
    public final static int DOWNLOAD_FAILED = 1;
    public final static int FILE_CORRUPTED = 2;
    public final static int DOWNLOAD_CANCELLED = 3;
    public final static int DOWNLOAD_INITED = 4;
    public final static int DOWNLOAD_PAUSED = 5;
    private int downloadStatus;
    private int downloadId;



    private String externalSd;
    private String internalSd;
    private int currentLevel;

    DividerDecoration divider;

   // OnDatabaseSelectedListener onDatabaseSelectedListener;

    boolean isDbSelected;
    boolean isFromBackPressed;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_files)
    RecyclerView rvFiles;

    LinearLayoutManager layoutManager;
    FileAdapter fileAdapter;

    boolean existDatabase;
    String path;

    boolean search;


    @Inject
    DatabaseViewModelFactory databaseViewModelFactory;
    DatabaseViewModel databaseViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initButterNife();
        setSupportActionBar(toolbar);
        search = false;

        setupDownloader();

        existDatabase = getIntent().getBooleanExtra(EXTRA_EXIST_DATABASE, false);

        if(existDatabase){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.update_database);
        } else{
            getSupportActionBar().setTitle(R.string.new_database);
        }

        setupDatabaseViewModel();
        setupUi();
    }


    private void setupDownloader(){
//        // Enabling database for resume support even after the application is killed:
//        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
//                .setDatabaseEnabled(true)
//                .build();
//        PRDownloader.initialize(getApplicationContext(), config);

// Setting timeout globally for the download network requests:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);
    }




    private void setupDatabaseViewModel(){
        databaseViewModel = ViewModelProviders.of(this, databaseViewModelFactory).get(DatabaseViewModel.class);

        databaseViewModel.databaseSaved.observe(this, new Observer<Response<Boolean>>() {
            @Override
            public void onChanged(@Nullable Response<Boolean> booleanResponse) {
                switch (booleanResponse.status){
                    case LOADING:
                        showLoadingDialog(getString(R.string.loading_cover_page));
                        break;

                    case SUCCESS:
                        if(booleanResponse.data){
                            databaseViewModel.getMetaInformation();
                        }
                        break;

                    case ERROR:
                        showErrorDialog(getString(R.string.database_incompatible));
                        hideProgressDialog();
                        break;
                }
            }
        });

        databaseViewModel.databaseCheck.observe(this, new Observer<Response<Boolean>>() {
            @Override
            public void onChanged(@Nullable Response<Boolean> booleanResponse) {
                switch (booleanResponse.status){
                    case LOADING:
                        break;

                    case SUCCESS:
                        if(booleanResponse.data){
                            databaseViewModel.saveDatabasePath(UpdateActivity.this.path);

                        }else{
                            showErrorDialog(getString(R.string.database_incompatible));
                        }
                        break;

                    case ERROR:
                        showErrorDialog(getString(R.string.database_incompatible));
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
                        if(existDatabase) {
                            onDatabaseSavedAndMetaInformationResult(metaResponse.data);
                        } else{
                            openMainActivity(metaResponse.data);
                        }
                        hideProgressDialog();
                        break;

                    case ERROR:
                        showErrorDialog(getString(R.string.database_incompatible));
                        break;
                }
            }
        });

        databaseViewModel.databaseDate.observe(this, new Observer<Response<Pair<String, Integer>>>() {
            @Override
            public void onChanged(@Nullable Response< Pair<String, Integer>> stringResponse) {
                switch (stringResponse.status){
                    case LOADING:
                        break;

                    case SUCCESS:
                        fileAdapter.setFileAt(stringResponse.data.first, stringResponse.data.second);
                        break;

                    case ERROR:
                        showErrorDialog("Error");
                        count ++;
                        if(count == databases.size()){
                            showErrorDialog(getString(R.string.not_databases));
                            fileAdapter.setFiles(getAllsds());
                        }
                        break;
                }
            }
        });
    }

    private void openMainActivity(Meta meta){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(UpdateActivity.EXTRA_META_INFORMATION, meta);
        startActivity(intent);
        finish();
    }

    private void onDatabaseSavedAndMetaInformationResult(Meta meta){
        Intent data = new Intent();
        data.putExtra(EXTRA_META_INFORMATION, meta);
        setResult(RESULT_OK, data);
        finish();
    }

    private void setupUi(){
        divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.a_orange_100)
                .build();

        layoutManager = new LinearLayoutManager(this);
        fileAdapter = new FileAdapter(this);
        fileAdapter.setOnFileItemClickListener(new FileAdapter.OnFileItemClickListener() {
            @Override
            public void onItemClick(int fileType, int pos, String filename, String path) {
                if(fileType == AUTOMATIC_SEARCH){
                    onBtnSearchDatabaseClick();
                }else if (fileType == DOWNLOAD){
                  downloadId =  createDownloadTask().start();
                }else if (fileType != FILE){
                    if (fileType != GO_BACK){
                        currentLevel ++;
                    } else if (currentLevel == 1){
                        fileAdapter.setFiles(getAllsds());
                        currentLevel --;
                        return;
                    } else{
                        currentLevel --;
                    }
                    fileAdapter.setFiles(loadFileList(path));

                } else{
                    isDbSelected = true;
                    UpdateActivity.this.path = path;
                    LocalDataBase.DB_NAME = filename;
                    LocalDataBase.DB_PATH = path;

                    Log.d("UpdateActivity", "DB_PATH" + LocalDataBase.DB_PATH);
                    databaseViewModel.checkDatabase();
                }
            }
        });

        isDbSelected = false;
        currentLevel = 0;
        isFromBackPressed = false;

        rvFiles.addItemDecoration(divider);
        rvFiles.setLayoutManager(layoutManager);
        rvFiles.setAdapter(fileAdapter);
        fileAdapter.setFiles(getAllsds());
    }

    @Override
    public void initInject() {
        getActivityComponent().inject(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                    onBackPressed();
                    finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<FileItem> loadFileList(String filePath){
        List<FileItem> listFiles = new ArrayList<>();

        File path = new File(filePath);
        if (path.toString().isEmpty()){
            path = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        }
        if (path.exists()){
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    // Filters based on whether the file is hidden or not
                    return (sel.isDirectory() || (sel.isFile() && sel.getName().endsWith(".db")))
                            && !sel.isHidden();
                }
            };

            String[] fileList = path.list(filter);
            File currentFile;
            int type = 0;

            filePath =  Utils.removeLastDirectory(filePath);

            listFiles.add(new FileItem(getResources().getString(R.string.go_back), filePath,  GO_BACK, currentLevel));

            for (int i = 0; i < fileList.length; i++){
                currentFile = new File(path ,fileList[i]);
                if (currentFile.isDirectory()){
                    type = FOLDER;
                } else if (currentFile.isFile()){
                    type = FILE;
                }
                listFiles.add(new FileItem(fileList[i], currentFile.getAbsolutePath(), type, currentLevel));
            }
        }
        return  listFiles;
    }

    private String getStoragePath(boolean isRemovable){
        String storagePath = "";
        File[] list = ContextCompat.getExternalFilesDirs(this, null);

        for (int i = 0; i < list.length; i++){
            if (isRemovable && Environment.isExternalStorageRemovable(list[i])){
                return processPath(list[i].getAbsolutePath());
            } else if (!isRemovable && Environment.isExternalStorageEmulated(list[i])){
                return processPath(list[i].getAbsolutePath());
            }
        }
        return storagePath;
    }

    private String processPath (String path){
        String storage = path;
        storage = path.substring(0,path.toString().lastIndexOf("/Android"));
        return storage;
    }

    private static String getStoragePath(Context mContext, boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ArrayList<FileItem> getAllsds(){
        ArrayList<FileItem> list = new ArrayList<>();
        internalSd = getStoragePath(this, false);
        externalSd= getStoragePath(this, true);

        if (internalSd == null && externalSd == null){
            internalSd = getStoragePath(false);
            externalSd = getStoragePath(true);
        }


        File ex = null;
        File in = null;

        if (externalSd != null){
            ex = new File(externalSd);

            if (ex.listFiles() != null){
                list.add(new FileItem(getResources().getString(R.string.external_sd), externalSd, EXTERNAL_STORAGE, currentLevel));
            }
        }

        if (internalSd != null){
            in = new File(internalSd);

            if (in.listFiles() != null){
                list.add(new FileItem(getResources().getString(R.string.internal_sd), internalSd, INTERNAL_STORAGE, currentLevel));
            }
        }

        list.add(new FileItem(getString(R.string.find_database), null, AUTOMATIC_SEARCH, currentLevel));
        list.add(new FileItem(getString(R.string.download_database), null, DOWNLOAD, currentLevel));
        return list;
    }



    int count;
    List<FileItem> databases = new ArrayList<>();


    //@OnClick(R.id.btn_search_database)
    public void onBtnSearchDatabaseClick(){
        search = true;
        count = 0;
        final List<FileItem> sds = getAllsds();

        //eliminar los dos ultimos items para no buscar en ellos...
        sds.remove(sds.size() - 1);
        sds.remove(sds.size() - 1);
        databases = new ArrayList<>();


        final class ListAync extends AsyncTask<Void,Void,Void>
        {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoadingDialog(getString(R.string.serching_databases));
            }

            @Override
            protected Void doInBackground(Void... params) {
                for(FileItem fi : sds){
                    databases.addAll(searchDatabase(fi.getPath()));
                }
              //  hideProgressDialog();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                hideProgressDialog();

                if(databases.isEmpty()){

                    showErrorDialog(getString(R.string.not_databases));
                    fileAdapter.setFiles(getAllsds());
                } else{
                    // Buscar las fechas...
                    fileAdapter.setFiles(databases);
                    int index = 0;
                    for(FileItem database : databases){
                        // chequear las bases de datos...
                        databaseViewModel.getDatabaseDate(database.getPath(),index++);
                    }
                }

            }
        }

        new ListAync().execute();





    }

    // hacerlo dinamico, ir gardando e una lista los folders q valla encontrando
    // a medida q voy recorriendo la lista, hasta q no encuentre mas ninguna,...

    public List<FileItem> searchDatabase(String directory){

       // showLoadingDialog("Iniciando descarga");

        File files = new File(directory);

        int cursor = 0;
        List<File> process = new ArrayList<>();
        List<FileItem> databases = new ArrayList<>();



        FileFilter filter = new FileFilter() {
            String extension = "db";
            String databaseName = "timbirichi";

            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()){
                    return true;
                }
                String ext;
                String name;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                name = path.substring(path.lastIndexOf("/")).toUpperCase();
                return (ext.equals(extension) && name.contains(databaseName.toUpperCase()) );
            }
        };

        List<File> filesArr = Arrays.asList(files.listFiles(filter));
        process.addAll(filesArr);


        for(int i = 0; i < process.size(); i++){
            files = new File(process.get(i).getPath());
            if(files.isFile()){
                FileItem item = new FileItem(files.getName(), files.getPath(), FILE, 0);
                databases.add(item);
            } else{
                process.addAll(Arrays.asList(files.listFiles(filter)));
            }
        }

        Log.d(getClass().getSimpleName(), "Process Count: " + Integer.toString(process.size()));
        return databases;
    }

    public void showDownloadErrorDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle(R.string.app_name);
        builder.setIcon(R.drawable.ic_error);


        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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

    /**
     * Descarga de la base de datos
     * **/
    private BaseDownloadTask createDownloadTask() {

        boolean isDir = false;
        Calendar cal = Calendar.getInstance();
        String currentDate = Integer.toString(cal.get(Calendar.DAY_OF_MONTH))
                + Integer.toString(cal.get(Calendar.MONTH))
                + Integer.toString(cal.get(Calendar.YEAR));


        // crear fichero y carpeta...
       // File file = new File(Environment.getExternalStorageDirectory().getPath() + DB_PATH + "/" + currentDate);
        File file = new File(Environment.getExternalStorageDirectory().getPath() + DB_PATH );
        file.mkdirs();

        final String dirPath = file.getPath();


        return FileDownloader.getImpl().create(DB_URL + LocalDataBase.DB_NAME)
                .setPath(dirPath + "/" + LocalDataBase.DB_NAME, isDir)

                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setAutoRetryTimes(10)

                .setTag(LocalDataBase.DB_NAME)
                .setListener(new FileDownloadSampleListener() {

                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.pending(task, soFarBytes, totalBytes);
                        showDownloadDialog("Iniciando descarga " + task.getFilename());
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.progress(task, soFarBytes, totalBytes);

                        if (totalBytes == -1){
                            mProgressDialog.setIndeterminate(true);
                        } else{
                            mProgressDialog.setMax(totalBytes);
                            mProgressDialog.setProgress(soFarBytes);
                            mProgressDialog.setMessage(String.format("Descargando base de datos. Velocidad: %d KB/s", task.getSpeed()) );
                        }
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        super.error(task, e);
                        mProgressDialog.setIndeterminate(false);
                        setProgressDialogMessage(String.format("Error al descargar base de datos, Velocidad de descarga: %d KB/s",
                                 task.getSpeed()));
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                        hideProgressDialog();
                        showDownloadDialog("Descargando base de datos timbirichi");
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        super.paused(task, soFarBytes, totalBytes);
                        setProgressDialogMessage("Pausado");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        super.completed(task);
                        setProgressDialogMessage(String.format("Quedan por descargar: %d total: %d, Velocidad de descarga: %d KB/s",
                                    task.getSmallFileSoFarBytes(), task.getSmallFileTotalBytes(), task.getSpeed()));

                        mProgressDialog.setIndeterminate(false);
                        mProgressDialog.setMax(task.getSmallFileTotalBytes());
                        mProgressDialog.setProgress(task.getSmallFileSoFarBytes());
                        hideProgressDialog();
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        super.warn(task);

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileDownloader.getImpl().pause(downloadId);
    }





}
