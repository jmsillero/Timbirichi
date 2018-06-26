package com.timbirichi.eltimbirichi.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Province implements Parcelable{

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

    public Province(Parcel parcel){
        parcel.readLong();
        parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Province> CREATOR = new Parcelable.Creator<Province>(){
        @Override
        public Province createFromParcel(Parcel source) {
            return new Province(source);
        }

        @Override
        public Province[] newArray(int size) {
            return new Province[size];
        }
    };

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
