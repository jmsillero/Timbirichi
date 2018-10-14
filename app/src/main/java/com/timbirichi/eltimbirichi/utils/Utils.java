package com.timbirichi.eltimbirichi.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.timbirichi.eltimbirichi.BuildConfig;
import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.Timbirichi;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.internal.operators.flowable.FlowableAll;

public class Utils {
    public static final String LOCAL_DATABASE_NAMED = "com.timbirichi.eltimbirichi.local_database";
    public static final String PREFERENCES_DATABASE_NAMED = "com.timbirichi.eltimbirichi.pref_database";
    public static final String UI_THREAD_NAMED = "com.timbirichi.eltimbirichi.ui_thread";
    public static final String EXCECUTOR_THREAD_NAMED = "com.timbirichi.eltimbirichi.executor_thread";
    public static final String PREF_DIR = "ccom.timbirichi.eltimbirichi.preferences";

    public static final int DB_LOCAL = 2001;
    public static final int DB_PREFERENCES = 2002;

    public static final String TIMBIRICHI_API_URL = "http://10.0.2.2/api/";

    public static Meta meta;
    public static Timbirichi timbirichiAppVersionInfo = null;

    public static int PRODUCT_OFFSET = 0;
    public static int PRODUCT_COUNT = 30;

    public static Product productSelected;

    public static  String removeLastDirectory(String path){
        return path.substring(0, path.lastIndexOf('/'));
    }


    /**
     * Calcula segun la fecha de la base de datos y la fecha actual si la base de datos
     * esta actualizada
     * Nota: La BD esta desactualizada con dos dias
     * @param timestamp
     * @return true si la BD esta actualizada, false si la BD esta desactualizada
     */
    public static boolean checkIsDatabaseUpdated(long timestamp){
        Calendar currentCalendar = Calendar.getInstance();
        Date currentDate = currentCalendar.getTime();

        Date databaseDate = new Date(timestamp);
        Calendar databaseCalendar = Calendar.getInstance();
        databaseCalendar.setTime(databaseDate);

        databaseCalendar.add(Calendar.DAY_OF_YEAR, 2);
        if(databaseCalendar.getTime().before(currentDate)){
            return false;
        }

        return true;
    }

    /**
     * Convierte un timestamp en fecha formateada a String
     * al formato dia mes anno
     * @param timestamp
     * @return fecha formateada como String
     */
    public static String convertTimeStampToStrDate(long timestamp){
        String formatD = "dd MMMM yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatD, new Locale("ES", "es"));
        return dateFormat.format(new Date(timestamp));
    }

    /**
     * Determinar si un numero tiene el formato de numeros de celulares validos para cuba
     * @param number
     * @return true si el numero entrado es un numero valido de celular en cuba
     */
    //+53 53
    //5353
    //53
    //+5353
    public static boolean isMobileNumber(String number){
        if (number != null && !number.isEmpty()){
            Log.d("Utils", "Numero para chequear" + number);

            int index = 0;
            int i;
            for (i = number.length() - 1; i > 0 && index < 7; i--){
                if(!Character.toString(number.charAt(i)).equals(" ")){
                    index ++;
                }
            }
            if(Character.toString(number.charAt(i)).equals("5")){
                return true;
            }
        }
        return false;
    }

    public static int HAS_NEW_DOWNLOAD_VERSION = 0;
    public static int NO_HAS_NEW_DOWNLOAD_VERSION = 1;

    public static int compareVersions(String serverVersion, String appVersion){
         if(serverVersion.compareTo(appVersion) > 0){
             return HAS_NEW_DOWNLOAD_VERSION;
         }
         return NO_HAS_NEW_DOWNLOAD_VERSION;
    }

    public static String getApkFilePackage(Context context, File apkFile) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkFile.getPath(), PackageManager.GET_ACTIVITIES);
        if (info != null){
            return info.applicationInfo.packageName;
        }
        return null;
    }


    public static void updateApp(Context context, File file) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(file.getPath(), PackageManager.GET_ACTIVITIES);
        if (info != null){
            if( compareVersions(info.versionName, BuildConfig.VERSION_NAME) == HAS_NEW_DOWNLOAD_VERSION){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        }
    }


    public static void installApp(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void unInstallApp(Context context, String packageName) {
        Uri packageUri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

//    public static boolean isAppInstalled(Context context, String packageName) {
//        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
//        if (!ListUtils.isEmpty(packages)) {
//            for (PackageInfo packageInfo : packages) {
//                if (packageInfo.packageName.equals(packageName)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public static int getMegasFromBytes(int bytes){
        return (bytes / 1024) / 1024;
    }



}
