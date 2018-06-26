package com.timbirichi.eltimbirichi.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SubCategory implements Parcelable{

    public static String SUBCATEGORY_TABLE = "subcategorias";
    public static String SUBCATEGORY_COL_ID = "id";
    public static String SUBCATEGORY_COL_CATEGORY = "categoria";
    public static String SUBCATEGORY_COL_NAME = "name";

    long id;
    String name;

    // imagen del banner para mostrar en la portada...
    byte [] image;

    public SubCategory(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SubCategory() {
    }

    public SubCategory(Parcel parcel) {
        id = parcel.readLong();
        name = parcel.readString();
        parcel.readByteArray(image);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeByteArray(image);
    }

    public static final Parcelable.Creator<SubCategory> CREATOR = new Parcelable.Creator<SubCategory>(){
        @Override
        public SubCategory createFromParcel(Parcel source) {
            return new SubCategory(source);
        }

        @Override
        public SubCategory[] newArray(int size) {
            return new SubCategory[size];
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
