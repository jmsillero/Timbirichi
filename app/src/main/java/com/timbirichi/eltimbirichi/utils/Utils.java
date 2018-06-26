package com.timbirichi.eltimbirichi.utils;

import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.data.model.Product;

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
    public static int PRODUCT_COUNT = 10;

    public static Product productSelected;

    public static  String removeLastDirectory(String path){
        return path.substring(0, path.lastIndexOf('/'));
    }
}
