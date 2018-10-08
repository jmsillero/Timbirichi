package com.timbirichi.eltimbirichi.data.service.local;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.timbirichi.eltimbirichi.data.model.Banner;
import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.Image;
import com.timbirichi.eltimbirichi.data.model.Meta;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.Province;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class LocalDataBase extends SQLiteOpenHelper {

    public static  String DB_NAME = "timbirichi.db";
    public static final String META_CODE = "123456789qwertyuioplkjhgfdsazxcvbnm";

    public static String DB_PATH = "";
    private SQLiteDatabase db;


    @Inject
    public LocalDataBase(Context context) {
        super(context, DB_NAME, null, 2);
    }


    public Meta getDatabaseDate(String data){
        try {
            SQLiteDatabase db = SQLiteDatabase.openDatabase(data, null, SQLiteDatabase.OPEN_READONLY);
            String q = " SELECT *"
                    + " FROM " + Meta.TABLE_META
                    + " WHERE " + Meta.COL_META_CODE + " = '" + META_CODE + "'"
                    + " LIMIT 1";


            Cursor cursor = db.rawQuery(q, null);

            if(cursor.moveToFirst()){
                Meta meta = new Meta();
                meta.setStrDate(cursor.getString(cursor.getColumnIndex(Meta.COL_META_DATE)));
                meta.setTimestamp(cursor.getLong(cursor.getColumnIndex(Meta.COL_META_TIMESTAMP)));
                return meta;
            } else{
                throw new SQLiteException();
            }
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
            db = null;
            throw new SQLiteException();
        }
    }


    public boolean open(){
        try {
            db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);

            String selection = Meta.COL_META_CODE + " = '" + META_CODE + "'";
            Cursor cursor = db.query(true, Meta.TABLE_META,
                    null,selection,
                    null,
                    null,
                    null,
                    null,
                    "1");

            return cursor.moveToFirst();
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
            db = null;
            throw new SQLiteException();
        }
    }


    /**
     * SELECT value FROM meta WHERE key = 'strtotime' LIMIT 1
     * @return La informacion de la base de datos.
     */
    public Meta getMetaInformation() {
        String q = " SELECT *"
                + " FROM " + Meta.TABLE_META
                + " LIMIT 1";

        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH , null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(q, null);
        try {
            cursor.moveToFirst();
            String dateTime = cursor.getString(cursor.getColumnIndex(Meta.COL_META_DATE));
            long timestamp = cursor.getLong(cursor.getColumnIndex(Meta.COL_META_TIMESTAMP));
            return new Meta(timestamp * 1000, null, dateTime);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLiteException();
        } finally {
            cursor.close();
            db.close();
            close();
        }
    }


    /**
     * Busca todas las provincias de la base de datos.
     * @return
     */
    public List<Province> getProvinces() {

        List<Province> provinces = null;

        String q = " SELECT *"
                + " FROM " + Province.PROVINCE_TABLE
                + " ORDER BY " + Province.PROVINCE_COL_ID;

        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH , null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(q, null);
        try {
            if (cursor.moveToFirst()) {
                provinces = new ArrayList<>();
                do {
                    Province province = new Province();
                    province.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(Province.PROVINCE_COL_ID))));
                    province.setName(cursor.getString(cursor.getColumnIndex(Province.PROVINCE_COL_NAME)));
                    provinces.add(province);
                }
                while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLiteException();
        } finally {
            cursor.close();
            db.close();
            close();
        }
        return provinces;
    }


    /**
     * Nota: Lo divido en dos metodos para evitar el join...
     * @return Devuelve la cantidad de productos para una categoria...
     */
    public int getProductCountBySubCat(long catId){

        int count = 0;

        String query = "SELECT count() FROM " + Product.PRODUCT_TABLE
                + " WHERE  " + Product.PRODUCT_COL_SUBCATEGORY + " = " + Long.toString(catId);


        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH , null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query,null);

        try{
            if(cursor.moveToFirst())
            {
                count =   cursor.getInt(0);

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new SQLiteException();
        }
        finally {
            cursor.close();
            db.close();
        }
        close();
        return  count;
    }


    /**
     * Nota: Lo divido en dos metodos para evitar el join...
     * @return Devuelve la lista de categorias
     */
    public List<Category> getCategories(){
        List<Category> categories = null;

        String query = "SELECT * FROM " + Category.CATEGORY_TABLE
                       + " ORDER BY " + Category.CATEGORY_COL_ID;


        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH , null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query,null);

        try{
            if(cursor.moveToFirst())
            {
                categories = new ArrayList<>();
                do{
                      Category category = new Category();
                      category.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(Category.CATEGORY_COL_ID))));
                      category.setName(cursor.getString(cursor.getColumnIndex(Category.CATEGORY_COL_NAME)));

                      category.setSubCategories(getSubCategories(db, category.getId()));


                      categories.add(category);
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new SQLiteException();
        }
        finally {
            cursor.close();
            db.close();
        }
        close();
        return  categories;
    }


    /**
     *
     * select * from quest order by RANDOM();
     * @return
     */
    public List<SubCategory> getRandomSubcategories(int limit){
        List<SubCategory> categories = null;

        String query = "SELECT * FROM " + SubCategory.SUBCATEGORY_TABLE
                + " ORDER BY RANDOM() LIMIT " + Integer.toString(limit);

        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH , null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query,null);

        try{
            if(cursor.moveToFirst())
            {
                categories = new ArrayList<>();
                do{
                    SubCategory category = new SubCategory();
                    category.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(SubCategory.SUBCATEGORY_COL_ID))));
                    category.setParentId(Long.parseLong(cursor.getString(cursor.getColumnIndex(SubCategory.SUBCATEGORY_COL_CATEGORY))));
                    category.setName(cursor.getString(cursor.getColumnIndex(SubCategory.SUBCATEGORY_COL_NAME)));

                    List<Banner> banners =  getBannersByCategoryId(category.getId());
                    if(banners != null && banners.get(0).getBase64Img() != null){
                        String base64Img = banners.get(0).getBase64Img();
                        category.setBase64Img(base64Img);
                    }

                    categories.add(category);
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new SQLiteException();
        }
        finally {
            cursor.close();
            db.close();
        }
        close();
        return  categories;
    }

    /**
     * @return Devuelve la lista de subcategorias
     * dado el Id de la categoria
     *
     * @param catId id de la categoria
     * @param db Base de datos abierta
     */
    public List<SubCategory> getSubCategories( SQLiteDatabase db, long catId){
        List<SubCategory> subcategories = null;

        String query = "SELECT * FROM " + SubCategory.SUBCATEGORY_TABLE
                + " WHERE " + SubCategory.SUBCATEGORY_COL_CATEGORY  + " = " + Long.toString(catId)
                + " ORDER BY " + SubCategory.SUBCATEGORY_COL_ID;

        Cursor cursor = db.rawQuery(query,null);

        try{
            if(cursor.moveToFirst())
            {
                subcategories = new ArrayList<>();
                do{
                    SubCategory subCategory = new SubCategory();
                    subCategory.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(SubCategory.SUBCATEGORY_COL_ID))));
                    subCategory.setName(cursor.getString(cursor.getColumnIndex(SubCategory.SUBCATEGORY_COL_NAME)));
                    subCategory.setProductCount(getProductCountBySubCat(subCategory.getId()));
                    subcategories.add(subCategory);
                }
                while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new SQLiteException();
        }
        finally {
            cursor.close();
        }
        return  subcategories;
    }

    public List<Product> loadUltraProducts(){
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM " + Product.PRODUCT_TABLE
                + " WHERE " + Product.PRODUCT_COL_COVER_PAGE + " = " + Integer.toString(Product.COVER_PAGE)
                + " ORDER BY "
                + " RANDOM() "
                + " LIMIT " + Integer.toString(2);


//        String query = "SELECT * FROM " + Product.PRODUCT_TABLE
//                + " WHERE " + Product.PRODUCT_COL_ULTRA + " = " + Integer.toString(Product.COVER_PAGE)
//                + " ORDER BY " + Product.PRODUCT_COL_ULTRA + Product.ORDER_DESC+ ", " + Product.PRODUCT_COL_COVER_PAGE + Product.ORDER_DESC + ", " + Product.PRODUCT_COL_ID + Product.ORDER_DESC
//                + ", RANDOM() "
//                + " LIMIT " + Integer.toString(2);

        // todo: Cambiar el limite de productos de la portada...


        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query,null);

        int index = 0;

        try{
            if(cursor.moveToFirst())
            {
                do{
                    Product product = createOrdinaryProduct(cursor, db);
                    products.add(product);
                    index ++;
                }
                while (cursor.moveToNext() && index < 6);
            }
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
            throw new SQLiteException();
        }
        finally {
            cursor.close();
            db.close();
        }
        return products;
    }


    /**
     *
     * Carga loa anuncios de la portada dentro de un limite de anuncios
     * @return Anuncios de la portada
     */
    public List<Product> loadCoverPageProducts(){
        List<Product> products = new ArrayList<>();

        products.addAll(loadUltraProducts());

        String query = "SELECT * FROM " + Product.PRODUCT_TABLE
                     + " WHERE " +  Product.PRODUCT_COL_ULTRA + " = " + Integer.toString(Product.COVER_PAGE)
                     + " ORDER BY "
                     + " RANDOM() ";
                   //  + " LIMIT " + Integer.toString(6);

        // todo: Cambiar el limite de productos de la portada...


        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query,null);

        int index = 0;

        try{
            if(cursor.moveToFirst())
            {
                do{
                    Product product = createOrdinaryProduct(cursor, db);
                    products.add(product);
                    index ++;
                }
                while (cursor.moveToNext() && index < 6);
            }
        }
        catch (SQLiteException e)
        {
            e.printStackTrace();
            throw new SQLiteException();
        }
        finally {
            cursor.close();
            db.close();
        }
        return products;
    }

    /**
     * Carga productos por categoria enmarcados en el liminte start to end
     * @param text texto en caso de que sea por buscador
     * @param start limite de inicio
     * @param end   limite de fin
     * @param category id de la categoria
     * @return Listado de productos...
     */
    public List<Product> loadProductsByCategory(String text, int start, int end,
                                                SubCategory category, String order, String orderType,
                                                boolean image, double minPrice, double maxPrice,
                                                ProductState state, long province) {

        List<Product> products = new ArrayList<>();

        String like = (text != null && !text.isEmpty()) ? " AND " + Product.PRODUCT_COL_TITLE  + " LIKE '%" + text + "%' " : " ";

        String orderBy = (order != null && !order.isEmpty()) ? " ORDER BY " + Product.PRODUCT_COL_ULTRA + Product.ORDER_DESC + ", " + Product.PRODUCT_COL_COVER_PAGE + Product.ORDER_DESC +   ", "+ order + orderType : " ";

        String withImage = image ? " AND " + Product.PRODUCT_COL_PHOTO_COUNT + " > 0" : " ";

        String newProd = "";
        if(state == ProductState.NEW){
            newProd = " AND " + Product.PRODUCT_COL_IS_NEW + " = 1 ";
        }

        String prov = province > 0 ? " AND " + Product.PRODUCT_COL_PROVINCE + " = " + Long.toString(province) + " " : " ";


        String min = minPrice > 0 ? " AND " + Product.PRODUCT_COL_PRICE + " > " + Double.toString(minPrice) : " ";

        String max = maxPrice > 0 ? " AND " + Product.PRODUCT_COL_PRICE + " < " + Double.toString(maxPrice) : " ";

        String limit = " LIMIT " + Integer.toString(start) + ", " + Integer.toString(end + 1 );

        String cat = (category.getId() != SubCategory.CATEGORY_LASTED && category.getId() != SubCategory.CATEGORY_COVER_PAGE) ? Product.PRODUCT_COL_SUBCATEGORY + " = " + Long.toString(category.getId()) : Product.PRODUCT_COL_SUBCATEGORY + " <> " + Long.toString(category.getId());

        String query = "SELECT * FROM " + Product.PRODUCT_TABLE
                + " WHERE " + cat
                + newProd + prov
                + like + withImage + min + max
                + orderBy
                + limit;



        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH , null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                String date = "";
                do {
//                    if (!order.equals(Product.PRODUCT_COL_PRICE)){
//                        String headerDate = cursor.getString(cursor.getColumnIndex(Product.PRODUCT_COL_TIME));
//
//                        //agregar cabecera de cada grupo de anuncios, agrupados por dateformated
//                        //siempre y cuando no sea por fotos
//                        if (!headerDate.equals(date)) {
//                            products.add(new ProductBo());
//                            products.get(products.size() - 1).setDateFormated(headerDate);
//                            products.get(products.size() - 1).setHeader(true);
//                            date = headerDate;
//                        }
//                    }


                    Product product = createOrdinaryProduct(cursor, db);
//                    product.setCategory(cursor.getLong(cursor.getColumnIndex(ProductBo.COL_ID)));
//                    product.setType(cursor.getString(cursor.getColumnIndex(ProductBo.COL_TYPE)));
                    product.setSubCategory(category.getName());

                    products.add(product);

                }
                while (cursor.moveToNext());
            }
        }
        catch (SQLiteException e){
         e.printStackTrace();
         throw new SQLiteException();
        }
        finally {
            cursor.close();
            db.close();
        }

        return products;
    }


    public List<Product> loadLastedProducts(String text, int start, int end,
                                            String order, String orderType,
                                            boolean image, double minPrice, double maxPrice,
                                            ProductState state, long province){
        SubCategory cat = new SubCategory();
        cat.setId(SubCategory.CATEGORY_LASTED);

        return loadProductsByCategory(text, start, end,
                                      cat, order, orderType,
                                      image, minPrice, maxPrice,
                                      state, province);

    }




    /**
     * Crea un producto contenido dentro del cursor...
     * @param cursor
     * @return
     */
    private Product createOrdinaryProduct(Cursor cursor, SQLiteDatabase db){
        Product product = new Product();

        product.setId(cursor.getLong(cursor.getColumnIndex(Product.PRODUCT_COL_ID)));
        product.setTitle(cursor.getString(cursor.getColumnIndex(Product.PRODUCT_COL_TITLE)));
        product.setDescription(cursor.getString(cursor.getColumnIndex(Product.PRODUCT_COL_DESCRIPTION)));
        product.setPrice(cursor.getFloat(cursor.getColumnIndex(Product.PRODUCT_COL_PRICE)));
        product.setPriceDown(cursor.getFloat(cursor.getColumnIndex(Product.PRODUCT_COL_DOWN_PRICE)));
        product.setPhone(cursor.getString(cursor.getColumnIndex(Product.PRODUCT_COL_PHONE)));
        product.setEmail(cursor.getString(cursor.getColumnIndex(Product.PRODUCT_COL_EMAIL)));
        product.setName(cursor.getString(cursor.getColumnIndex(Product.PRODUCT_COL_NAME)));
        product.setPhotosCount(cursor.getInt(cursor.getColumnIndex(Product.PRODUCT_COL_PHOTO_COUNT)));
        product.setNewProduct(cursor.getInt(cursor.getColumnIndex(Product.PRODUCT_COL_IS_NEW)) == 1);
        product.setUltra(cursor.getInt(cursor.getColumnIndex(Product.PRODUCT_COL_ULTRA)) == 1);
        product.setMain(cursor.getInt(cursor.getColumnIndex(Product.PRODUCT_COL_COVER_PAGE)) == 1);
        product.setProvince(getProvinceForProduct(cursor.getLong(cursor.getColumnIndex(Product.PRODUCT_COL_PROVINCE)), db));
        product.setTime(cursor.getLong(cursor.getColumnIndex(Product.PRODUCT_COL_TIME)) * 1000);
        product.setImages(loadImageForProduct(product.getId(), db));
        product.setViews(cursor.getInt(cursor.getColumnIndex(Product.PRODUCT_COL_VIEWS)));
        return product;
    }


    /**
     * SELECT * FROM imagenes WHERE anuncio_id = " + anuncio_id
     * @param productId id del producto
     * @return Retorna la imagen que pertenece a un producto dado
     */
    public List<Image> loadImageForProduct(long productId, SQLiteDatabase db){

        List<Image> images = null;

        String query = "SELECT * FROM " + Image.IMAGE_TABLE
                     + " WHERE " + Image.IMAGE_COL_PRODUCT_ID + " = "
                     + Long.toString(productId);


        Cursor cursor = db.rawQuery(query, null);

        Image image = null;
        try{
            if (cursor != null){
                if (cursor.moveToFirst()) {
                    images = new ArrayList<>();
                    do {
                        image = new Image();
                        image.setProductId(cursor.getLong(cursor.getColumnIndex(Image.IMAGE_COL_PRODUCT_ID)));
                      //  image.setImage(cursor.getBlob(cursor.getColumnIndex(Image.IMAGE_COL_IMAGE)));
                        image.setBase64Img(cursor.getString(cursor.getColumnIndex(Image.IMAGE_COL_IMAGE)));
                        images.add(image);
                    }
                    while (cursor.moveToNext());
                }
            }
        }

        catch (SQLiteException e){
            e.printStackTrace();
            throw new SQLiteException();
        }
        finally {
            cursor.close();
        }
        return images;

    }


    /**
     * @param provinceId
     * @param db
     * @return
     */
    public Province getProvinceForProduct(long provinceId, SQLiteDatabase db){

        Province province = null;

        String query = "SELECT * FROM " + Province.PROVINCE_TABLE
                + " WHERE " + Province.PROVINCE_COL_ID + " = "
                + Long.toString(provinceId);


        Cursor cursor = db.rawQuery(query, null);

        try{
            if (cursor != null){
                if (cursor.moveToFirst()) {
                    province = new Province();
                    province.setId(cursor.getLong(cursor.getColumnIndex(Province.PROVINCE_COL_ID)));
                    province.setName(cursor.getString(cursor.getColumnIndex(Province.PROVINCE_COL_NAME)));
                }
            }
        }

        catch (SQLiteException e){
            e.printStackTrace();
            throw new SQLiteException();
        }
        finally {
            cursor.close();
        }
        return province;

    }

    public List<Banner> getDefaultsBanners(){
        List<Banner> banners = null;
        String query = "SELECT * FROM " + Banner.BANNER_TABLE
                + " WHERE " + "\"" +Banner.BANNER_COL_DEFAULT + "\"" + " = " + Integer.toString(Banner.DEFAULT) +
                " AND " +  Banner.BANNER_COL_SUBCATEGORY_ID + " = 0";


        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH , null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query, null);

        Banner banner = null;
        try{
            if (cursor != null){
                if (cursor.moveToFirst()) {
                    banners = new ArrayList<>();
                    do {
                        banner = createOrdinaryBanner(cursor);
                        banners.add(banner);
                    }
                    while (cursor.moveToNext());
                }
            }
        }

        catch (SQLiteException e){
            e.printStackTrace();
            throw new SQLiteException();
        }
        finally {
            cursor.close();
            close();
        }

       return banners;
    }


    /**
     * Buscar un producto segun su ID, cambia la categoria a la cual pertenece
     * @param id Id del producto para buscar
     * @param category Categoria q se le asigna al producto...
     * @return
     */
    public Product selectProductById(long id, SubCategory category){
        String query = "SELECT * FROM " + Product.PRODUCT_TABLE
                + " WHERE " + Product.PRODUCT_COL_ID + " = " + Long.toString(id);

        Product product = null;


        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH , null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                product = createOrdinaryProduct(cursor, db);
                product.setSubCategory(category.getName());
            }

        }
        catch (SQLiteException e){
            e.printStackTrace();
            throw new SQLiteException();
        }
        finally {
            cursor.close();
            db.close();
        }

        return product;
    }


    /**
     * Crea un banner conlos datos obtenidos de la base de datos...
     * @param cursor
     * @return
     */
    private Banner createOrdinaryBanner(Cursor cursor){
        Banner banner = new Banner();
        banner.setId(cursor.getLong(cursor.getColumnIndex(Banner.BANNER_COL_ID)));
        banner.setBase64Img(cursor.getString(cursor.getColumnIndex(Banner.BANNER_COL_IMAGE)));
        banner.setBnDefault(cursor.getInt(cursor.getColumnIndex(Banner.BANNER_COL_DEFAULT)) == 1);
        banner.setProductId(cursor.getLong(cursor.getColumnIndex(Banner.BANNER_COL_PRODUCT_ID)));

        return banner;
    }

    // limpiar los banners en caso de que tenga alguno q no sea default, quitar los defaults..
    private List<Banner> cleaDefaultBanners(List<Banner> banners){
        List<Banner> bnrs = new ArrayList<>();
        if (banners.size() > 1){
           for (Banner bn : banners){
               if (!bn.isBnDefault()){
                   bnrs.add(bn);
               }
           }
        }

        if (bnrs.size() > 0){
            return bnrs;
        }
        return null;
    }

    public List<Banner> getBannersByCategoryId(long catId){
        List<Banner> banners = null;
        String query = "SELECT * FROM " + Banner.BANNER_TABLE
                + " WHERE " + Banner.BANNER_COL_SUBCATEGORY_ID + " = "
                + Long.toString(catId);


        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH , null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query, null);

        Banner banner = null;
        try{
            if (cursor != null){
                if (cursor.moveToFirst()) {
                    Log.d(getClass().getSimpleName(), "Cursor size: " + cursor.getCount());
                    banners = new ArrayList<>();
                    do {
                        banner = createOrdinaryBanner(cursor);
                        banners.add(banner);
                    }
                    while (cursor.moveToNext());
                }
            }

        }

        catch (SQLiteException e){
            e.printStackTrace();
            throw new SQLiteException();
        } finally {
            cursor.close();
            close();
        }

        if (banners == null){
            banners = getDefaultsBanners();
        }
        List<Banner> bnList = cleaDefaultBanners(banners);
        return bnList != null ? bnList : banners;

    }

    @Override
    public synchronized void close() {
        if(db != null){
            db.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
