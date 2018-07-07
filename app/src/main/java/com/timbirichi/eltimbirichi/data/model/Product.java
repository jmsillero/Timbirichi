package com.timbirichi.eltimbirichi.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Product implements Parcelable{

    long id;
    String category;
    String subCategory;
    double price;
    double priceDown;
    String title;
    String description;
    byte [] bannerImage;
    Province province;
    List<Image> images;

    //Contact...
    String name;
    String email;
    String phone;
    long time;
    int views;
    boolean newProduct;
    boolean ultra;

    int photosCount;

    boolean favorite;

    boolean showInCoverPage;

    // destacado
    boolean main;

    public static String PRODUCT_TABLE = "anuncios";
    public static String PRODUCT_COL_ID = "id";
    public static String PRODUCT_COL_NAME = "nombre";
    public static String PRODUCT_COL_CATEGORY = "categoria";
    public static String PRODUCT_COL_SUBCATEGORY = "subcategoria";
    public static String PRODUCT_COL_PRICE = "precio";
    public static String PRODUCT_COL_DOWN_PRICE = "rebaja";
    public static String PRODUCT_COL_TITLE = "titulo";
    public static String PRODUCT_COL_DESCRIPTION = "descripcion";
    public static String PRODUCT_COL_PICTURE = "foto";
    public static String PRODUCT_COL_PROVINCE = "prov";
    public static String PRODUCT_COL_EMAIL = "email";
    public static String PRODUCT_COL_PHONE = "telefono";
    public static String PRODUCT_COL_TIME = "time";
    public static String PRODUCT_COL_VIEWS = "views";
    public static String PRODUCT_COL_IS_NEW = "nuevo";
    public static String PRODUCT_COL_ULTRA = "ultra";
    public static String PRODUCT_COL_COVER_PAGE = "destacado";
    public static String PRODUCT_COL_PHOTO_COUNT = "photos_count";
    public static String PRODUCT_COL_SHOW_IN_COVER_PAGE = "cover_page";

    public static final String ORDER_ASC = " ASC ";
    public static final String ORDER_DESC = " DESC ";

    public static final int COVER_PAGE = 0;



    public Product(String category, String subCategory, float price,
                   float priceDown, String title, String description,
                   byte[] bannerImage, Province province, List<Image> images, String name,
                   String email, String phone, long time, int views,
                   boolean newProduct, boolean ultra, boolean main,
                   boolean showInCoverPage) {
        this.category = category;
        this.subCategory = subCategory;
        this.price = price;
        this.priceDown = priceDown;
        this.title = title;
        this.description = description;
        this.bannerImage = bannerImage;
        this.province = province;
        this.images = images;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.time = time;
        this.views = views;
        this.newProduct = newProduct;
        this.ultra = ultra;
        this.main = main;
        this.favorite = false;
        this.showInCoverPage = showInCoverPage;
    }

    public Product() {
    }

    public Product(Parcel parcel){
       id =  parcel.readLong();
       category = parcel.readString();
       subCategory =  parcel.readString();
       price =  parcel.readDouble();
       priceDown =  parcel.readDouble();
       title =  parcel.readString();
       description =  parcel.readString();
       province = parcel.readParcelable(Province.class.getClassLoader());
       parcel.readList(images, Image.class.getClassLoader());
       name =  parcel.readString();
       email =  parcel.readString();
       phone =  parcel.readString();
       time =  parcel.readLong();
       views =  parcel.readInt();
       newProduct = parcel.readInt() == 1;
       ultra = parcel.readInt() == 1;
       photosCount = parcel.readInt();
       favorite = parcel.readInt() == 1;
       main = parcel.readInt() == 1;
       showInCoverPage = parcel.readInt() == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(category);
        dest.writeString(subCategory);
        dest.writeDouble(price);
        dest.writeDouble(priceDown);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeParcelable(province, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        dest.writeList(images);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeLong(time);
        dest.writeInt(views);
        dest.writeInt(newProduct ? 1 : 0);
        dest.writeInt(ultra ? 1 : 0);
        dest.writeInt(photosCount);
        dest.writeInt(favorite ? 1 : 0);
        dest.writeInt(main ? 1 : 0);
        dest.writeInt(showInCoverPage ? 1 : 0);

    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>(){
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceDown() {
        return priceDown;
    }

    public void setPriceDown(double priceDown) {
        this.priceDown = priceDown;
    }

    public void setPriceDown(float priceDown) {
        this.priceDown = priceDown;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(byte[] bannerImage) {
        this.bannerImage = bannerImage;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean isNewProduct() {
        return newProduct;
    }

    public void setNewProduct(boolean newProduct) {
        this.newProduct = newProduct;
    }

    public boolean isUltra() {
        return ultra;
    }

    public void setUltra(boolean ultra) {
        this.ultra = ultra;
    }

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getPhotosCount() {
        return photosCount;
    }

    public void setPhotosCount(int photosCount) {
        this.photosCount = photosCount;
    }

    public boolean isShowInCoverPage() {
        return showInCoverPage;
    }

    public void setShowInCoverPage(boolean showInCoverPage) {
        this.showInCoverPage = showInCoverPage;
    }
}
