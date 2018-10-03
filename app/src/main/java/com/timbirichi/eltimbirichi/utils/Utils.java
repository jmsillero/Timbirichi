package com.timbirichi.eltimbirichi.utils;

import android.util.Log;

import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.data.model.Product;

import io.reactivex.internal.operators.flowable.FlowableAll;

public class Utils {
    public static final String LOCAL_DATABASE_NAMED = "com.timbirichi.eltimbirichi.local_database";
    public static final String PREFERENCES_DATABASE_NAMED = "com.timbirichi.eltimbirichi.pref_database";
    public static final String UI_THREAD_NAMED = "com.timbirichi.eltimbirichi.ui_thread";
    public static final String EXCECUTOR_THREAD_NAMED = "com.timbirichi.eltimbirichi.executor_thread";
    public static final String PREF_DIR = "ccom.timbirichi.eltimbirichi.preferences";

    public static final int DB_LOCAL = 2001;
    public static final int DB_PREFERENCES = 2002;

    public static Meta meta;

    public static int PRODUCT_OFFSET = 0;
    public static int PRODUCT_COUNT = 30;

    public static Product productSelected;

    public static  String removeLastDirectory(String path){
        return path.substring(0, path.lastIndexOf('/'));
    }

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
}
