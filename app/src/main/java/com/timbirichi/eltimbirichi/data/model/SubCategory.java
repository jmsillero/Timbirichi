package com.timbirichi.eltimbirichi.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SubCategory implements Parcelable{

    public static String SUBCATEGORY_TABLE = "subcategorias";
    public static String SUBCATEGORY_COL_ID = "id";
    public static String SUBCATEGORY_COL_CATEGORY = "categoria";
    public static String SUBCATEGORY_COL_NAME = "name";

    public static long CATEGORY_LASTED = -12;
    public static long CATEGORY_FAVORITES = -15;

    long id;
    String name;

    // imagen del banner para mostrar en la portada...
    byte [] image;

    String base64Img;

    int productCount;

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
        base64Img = parcel.readString();
        productCount = parcel.readInt();
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
        dest.writeString(base64Img);
        dest.writeInt(productCount);
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

    public String getBase64Img() {
        return base64Img;
    }

    public void setBase64Img(String base64Img) {
        this.base64Img = base64Img;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}
