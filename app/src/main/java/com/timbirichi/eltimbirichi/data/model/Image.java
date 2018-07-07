package com.timbirichi.eltimbirichi.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable{

    public static String IMAGE_TABLE = "fotos";
    public static String IMAGE_COL_ID = "id";
    public static String IMAGE_COL_PRODUCT_ID = "anuncio_id";
    public static String IMAGE_COL_IMAGE = "foto";

    long id;
    long productId;
    byte [] image;
    String base64Img;

    public Image(long id, long productId, byte[] image, String base64Img) {
        this.id = id;
        this.productId = productId;
        this.image = image;
        this.base64Img = base64Img;
    }

    public Image() {
    }

    public Image(Parcel parcel){
        id = parcel.readLong();
        productId = parcel.readLong();
        parcel.readByteArray(image);
        base64Img = parcel.readString();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>(){
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(productId);
        dest.writeByteArray(image);
        dest.writeString(base64Img);
    }


    public String getBase64Img() {
        return base64Img;
    }

    public void setBase64Img(String base64Img) {
        this.base64Img = base64Img;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
