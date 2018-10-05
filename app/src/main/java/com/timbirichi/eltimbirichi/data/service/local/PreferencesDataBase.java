package com.timbirichi.eltimbirichi.data.service.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.timbirichi.eltimbirichi.data.model.Image;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.Province;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PreferencesDataBase extends SQLiteOpenHelper {

    public static final  String  DB_NAME = "cache.db";
    public static String DB_PATH = "/data/data/com.timbirichi.eltimbirichi/databases/";
    Context context;
    private SQLiteDatabase db;

    @Inject
    public PreferencesDataBase(Context context) {
        super(context, DB_NAME, null, 2);
        this.context = context;
        DB_PATH = "/data/data/com.timbirichi.eltimbirichi/databases/";

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Busca los productos filtrados en favoritos...
     * @param text texto del buscador
     * @param start limite del inicio
     * @param end limite final
     * @param order orden de los anuncios
     * @param orderType tio de orden, ascendente y descendente
     * @param image si contienen imagenes
     * @param minPrice precio minimo
     * @param maxPrice precio maximo
     *
     * @return Retorna los productos filtrados desed la portada...
     */
    public List<Product> loadProducts(String text, int start, int end,
                                        String order, String orderType,
                                        boolean image, double minPrice, double maxPrice,
                                        ProductState state, long province){

        String where = " WHERE ";

        List<Product> products = new ArrayList<>();

        String like = (text != null && !text.isEmpty()) ? Product.PRODUCT_COL_TITLE + " LIKE '%" + text + "%' " : " ";

        String orderBy = (order != null && !order.isEmpty()) ? " ORDER BY " + order + orderType : " ";

        String withImage = image ? " AND " + Product.PRODUCT_COL_PHOTO_COUNT + " > 0" : " ";

        boolean into = false;
        if(text != null && !text.trim().isEmpty()){
            into = true;
        }

        // Eliminar el AND del withImage
        if (!into && image){
            withImage = Product.PRODUCT_COL_PHOTO_COUNT + " > 0";
            into = true;
        }


        String min = minPrice > 0 ? " AND " + Product.PRODUCT_COL_PRICE + " > " + Double.toString(minPrice) : " ";
        if (!into && !min.trim().isEmpty()){
            min =  Product.PRODUCT_COL_PRICE + " > " + Double.toString(minPrice);
            into = true;
        }


        String max = maxPrice > 0 ? " AND " + Product.PRODUCT_COL_PRICE + " < " + Double.toString(maxPrice) : " ";
        if (!into && !max.trim().isEmpty()){
            max = Product.PRODUCT_COL_PRICE + " > " + Double.toString(maxPrice);
            into = true;
        }

        String limit = " LIMIT " + Integer.toString(start) + ", " + Integer.toString(end);

        String query = "SELECT * FROM " + Product.PRODUCT_TABLE
                + where + like + withImage + min + max
                + orderBy + limit;

        if(!into){
            query = "SELECT * FROM " + Product.PRODUCT_TABLE

                    + orderBy + limit;
        }

        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {

                do {

                   // if (!order.equals(Product.PRODUCT_COL_PRICE)) {

                        Product product = createOrdinaryProduct(cursor, db);
                        products.add(product);
                  //  }
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
            product.setProvince(getProvinceForProduct(cursor.getLong(cursor.getColumnIndex(Product.PRODUCT_COL_PROVINCE))));
            product.setSubCategory(cursor.getString(cursor.getColumnIndex(Product.PRODUCT_COL_SUBCATEGORY)));

            product.setTime(cursor.getLong(cursor.getColumnIndex(Product.PRODUCT_COL_TIME)) * 1000);

            // product.setPriority(cursor.getInt(cursor.getColumnIndex(Product.COL_PRIORITY)));


            //  product.setDateFormated(cursor.getString(cursor.getColumnIndex(Product.COL_DATE_FORMATED)));


            product.setImages(loadImageForProduct(product.getId(), db));

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
         * @return
         */
        public Province getProvinceForProduct(long provinceId){

            SQLiteDatabase db = SQLiteDatabase.openDatabase(LocalDataBase.DB_PATH , null, SQLiteDatabase.OPEN_READONLY);
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




    /**
     * Guarda en la base de datos de preferencias el productos
     * @param productBo
     * @return true si es guardado correctamente...
     */
    public boolean saveProductToFavorite(Product productBo){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        ContentValues fields = new ContentValues();
        fields.put(Product.PRODUCT_COL_ID, productBo.getId());
        fields.put(Product.PRODUCT_COL_NAME, productBo.getName());
        fields.put(Product.PRODUCT_COL_SUBCATEGORY, productBo.getSubCategory() != null ? productBo.getSubCategory() : "");
        fields.put(Product.PRODUCT_COL_PRICE, productBo.getPrice());
        fields.put(Product.PRODUCT_COL_DOWN_PRICE, productBo.getPriceDown());
        fields.put(Product.PRODUCT_COL_TITLE, productBo.getTitle());
        fields.put(Product.PRODUCT_COL_DESCRIPTION, productBo.getDescription());
        fields.put(Product.PRODUCT_COL_PROVINCE, productBo.getProvince().getId());
        fields.put(Product.PRODUCT_COL_EMAIL, productBo.getEmail());
        fields.put(Product.PRODUCT_COL_PHONE, productBo.getPhone());
        fields.put(Product.PRODUCT_COL_TIME, productBo.getTime() / 1000);
        fields.put(Product.PRODUCT_COL_VIEWS, productBo.getViews());
        fields.put(Product.PRODUCT_COL_IS_NEW, productBo.isNewProduct());
        fields.put(Product.PRODUCT_COL_PHOTO_COUNT, productBo.getPhotosCount());


        if(productBo.getImages() != null && productBo.getImages().size() > 0){
            ContentValues imagesFields = new ContentValues();
            imagesFields.put(Image.IMAGE_COL_PRODUCT_ID, productBo.getId());
            imagesFields.put(Image.IMAGE_COL_IMAGE, productBo.getImages().get(0).getBase64Img());
            db.insert(Image.IMAGE_TABLE, null ,imagesFields);
        }

        boolean inserted = db.insert(Product.PRODUCT_TABLE, null, fields) != -1;
        return inserted;
    }


    /**
     * Elimina un producto de los favoritos segun su ID
     * @param id
     * @return
     */
    public boolean removeFromFavorites(long id){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        boolean resp = db.delete(Product.PRODUCT_TABLE, Product.PRODUCT_COL_ID + " = " + Long.toString(id), null) > 0;
        db.close();
        return resp;
    }


    /**
     * buscar  un producto dentro de la base de datos de favoritos..
     * @param id
     * @return
     */
    public boolean findProductById(long id){
        String query = "SELECT " + Product.PRODUCT_COL_ID + " FROM " + Product.PRODUCT_TABLE
                + " WHERE " + Product.PRODUCT_COL_ID + " = " + Long.toString(id);

        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query,null);

        return cursor.moveToFirst();
    }
//
//    /**
//     * Devuelve un producto dado su ID
//     * @param productId
//     * @return
//     */
//    public ProductBo getProductById(long productId){
//
//        String query = "SELECT * FROM " + ProductBo.TABLE_PRODUCTS
//                + " WHERE " + ProductBo.COL_ID + " = " + Long.toString(productId);
//
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = db.rawQuery(query,null);
//        ProductBo productBo = null;
//
//        try{
//            if (cursor.moveToFirst()){
//                productBo = createOrdinaryProduct(cursor, db);
//            }
//        }
//
//        catch (SQLiteException e)
//        {
//            e.printStackTrace();
//            throw new SQLiteException();
//        }
//        finally {
//            cursor.close();
//            db.close();
//        }
//        close();
//
//        return productBo;
//    }
//
    @Override
    public synchronized void close() {
        if(db != null){
            db.close();
        }
        super.close();
    }

    public boolean open(){
        try {
            db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
            return true;
        } catch(SQLiteException sqle) {
            sqle.printStackTrace();
            db = null;
            throw new SQLiteException();
        }
    }

    public boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
            throw new SQLiteException();
        } finally {
            close();
        }

        checkDB.close();

        return true;
    }

    public boolean copyDataBase() throws IOException {
        //String path = DB_PATH + DB_NAME;
        File mFolder = new File(DB_PATH);
        if (!mFolder.exists()) {
            mFolder.mkdirs();
        }

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
        close();

        return true;
    }

    public  Boolean clearDatabase(){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        try{
        db.execSQL("DELETE FROM " + Product.PRODUCT_TABLE);
        db.execSQL("DELETE FROM " + Image.IMAGE_TABLE);
        } catch (SQLException e){
            throw new SQLException();
        }

        return true;
    }



}
