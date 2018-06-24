package com.timbirichi.eltimbirichi.data.model;

public class Image {

    public static String IMAGE_TABLE = "fotos";
    public static String IMAGE_COL_ID = "id";
    public static String IMAGE_COL_PRODUCT_ID = "anuncio_id";
    public static String IMAGE_COL_IMAGE = "foto";

    long id;
    long productId;
    byte [] image;

    public Image(long id, long productId, byte[] image) {
        this.id = id;
        this.productId = productId;
        this.image = image;
    }

    public Image() {
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
