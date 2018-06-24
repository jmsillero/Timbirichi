package com.timbirichi.eltimbirichi.data.model;

public class Province {

    public static String PROVINCE_TABLE = "provincias";
    public static String PROVINCE_COL_ID = "id";
    public static String PROVINCE_COL_NAME = "name";

    public static long NO_PROVINCE = -1;

    long id;
    String name;

    public Province(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Province() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
