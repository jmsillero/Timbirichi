package com.timbirichi.eltimbirichi.data.service.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;

public class PreferencesDataBase extends SQLiteOpenHelper {

    public static final  String  DB_NAME = "chpreferences.db";
    public static String DB_PATH = "";
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

//    /**
//     * Crea un producto contenido dentro del cursor...
//     * @param cursor
//     * @return
//     */
//    private ProductBo createOrdinaryProduct(Cursor cursor, SQLiteDatabase db){
//        ProductBo product = new ProductBo();
//
//        product.setId(cursor.getLong(cursor.getColumnIndex(ProductBo.COL_ID)));
//        product.setTitle(cursor.getString(cursor.getColumnIndex(ProductBo.COL_HEADER)));
//        product.setBody(cursor.getString(cursor.getColumnIndex(ProductBo.COL_BODY)));
//        product.setPrice(cursor.getFloat(cursor.getColumnIndex(ProductBo.COL_PRICE)));
//        product.setPhone(cursor.getString(cursor.getColumnIndex(ProductBo.COL_PHONE)));
//        product.setEmail(cursor.getString(cursor.getColumnIndex(ProductBo.COL_EMAIL)));
//        product.setName(cursor.getString(cursor.getColumnIndex(ProductBo.COL_NAME)));
//        product.setPhotos_count(cursor.getInt(cursor.getColumnIndex(ProductBo.COL_PHOTO_COUNT)));
//        product.setDate(cursor.getString(cursor.getColumnIndex(ProductBo.COL_DATE)));
//        product.setPriority(cursor.getInt(cursor.getColumnIndex(ProductBo.COL_PRIORITY)));
//        product.setDateFormated(cursor.getString(cursor.getColumnIndex(ProductBo.COL_DATE_FORMATED)));
//        product.setCategoryName(cursor.getString(cursor.getColumnIndex(ProductBo.COL_CATEGORY)));
//        product.setType(cursor.getString(cursor.getColumnIndex(ProductBo.COL_TYPE)));
//
//        if(product.getType() == ProductBo.BANNER_PRODUCT){
//            byte [] img = cursor.getBlob(cursor.getColumnIndex(ProductBo.COL_BANNER));
//            ImageBo image = new ImageBo(product.getId(), img);
//            product.setImage(image);
//        }else{
//            product.setImage(loadImageForProduct(product.getId(), db));
//        }
//        return product;
//    }
//
//    /**
//     * Busca los productos filtrados en favoritos...
//     * @param text texto del buscador
//     * @param start limite del inicio
//     * @param end limite final
//     * @param order orden de los anuncios
//     * @param orderType tio de orden, ascendente y descendente
//     * @param image si contienen imagenes
//     * @param minPrice precio minimo
//     * @param maxPrice precio maximo
//     *
//     * @return Retorna los productos filtrados desed la portada...
//     */
//    public List<ProductBo> loadProducts(String text, int start, int end,
//                                                             String order, String orderType,
//                                                             boolean image, double minPrice, double maxPrice){
//
//        String where = " WHERE ";
//
//        List<ProductBo> products = new ArrayList<>();
//
//        String like = (text != null && !text.isEmpty()) ? ProductBo.COL_HEADER + " LIKE '%" + text + "%' " : " ";
//
//        String orderBy = (order != null && !order.isEmpty()) ? " ORDER BY " + order + orderType : " ";
//
//        String withImage = image ? " AND " + ProductBo.COL_PHOTO_COUNT + " > 0" : " ";
//
//        boolean into = false;
//        if(text != null && !text.trim().isEmpty()){
//            into = true;
//        }
//
//        // Eliminar el AND del withImage
//        if (!into && image){
//            withImage = ProductBo.COL_PHOTO_COUNT + " > 0";
//            into = true;
//        }
//
//
//        String min = minPrice > 0 ? " AND " + ProductBo.COL_PRICE + " > " + Double.toString(minPrice) : " ";
//        if (!into && !min.trim().isEmpty()){
//            min =  ProductBo.COL_PRICE + " > " + Double.toString(minPrice);
//            into = true;
//        }
//
//
//        String max = maxPrice > 0 ? " AND " + ProductBo.COL_PRICE + " < " + Double.toString(maxPrice) : " ";
//        if (!into && !max.trim().isEmpty()){
//            max = ProductBo.COL_PRICE + " > " + Double.toString(maxPrice);
//            into = true;
//        }
//
//        String limit = " LIMIT " + Integer.toString(start) + ", " + Integer.toString(end);
//
//        String query = "SELECT * FROM " + ProductBo.TABLE_PRODUCTS
//                + where + like + withImage + min + max + " GROUP BY " + ProductBo.COL_ID
//                + orderBy + limit;
//
//        if(!into){
//            query = "SELECT * FROM " + ProductBo.TABLE_PRODUCTS
//            + " GROUP BY " + ProductBo.COL_ID
//                    + orderBy + limit;
//        }
//
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = db.rawQuery(query, null);
//        try {
//            if (cursor.moveToFirst()) {
//
//                String date = "";
//                do {
//
//                    if (!order.equals(EntityProduct.COL_PRICE)){
//                        String headerDate = cursor.getString(cursor.getColumnIndex(ProductBo.COL_DATE_FORMATED));
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
//
//                    ProductBo product = createOrdinaryProduct(cursor, db);
//                    products.add(product);
//                }
//                while (cursor.moveToNext());
//            }
//        }
//        catch (SQLiteException e){
//            e.printStackTrace();
//            throw new SQLiteException();
//        }
//        finally {
//            cursor.close();
//            db.close();
//        }
//
//        return products;
//    }
//
//
//    /**
//     * SELECT * FROM imagenes WHERE anuncio_id = " + anuncio_id
//     * @param productId id del producto
//     * @return Retorna la imagen que pertenece a un producto dado
//     */
//    public ImageBo loadImageForProduct(long productId, SQLiteDatabase db){
//        String query = "SELECT * FROM " + ImageBo.TABLE_IMAGES
//                + " WHERE " + ImageBo.COL_PRODUCT_ID + " = "
//                + Long.toString(productId);
//
//        Cursor cursor = db.rawQuery(query, null);
//
//        ImageBo image = null;
//        try{
//            if (cursor != null){
//                if (cursor.moveToFirst()) {
//                    do {
//                        image = new ImageBo();
//                        image.setProductId(cursor.getLong(cursor.getColumnIndex(ImageBo.COL_PRODUCT_ID)));
//                        //image.setBitmapImage(Utils.decode2Bitmap(cursor.getBlob(cursor.getColumnIndex(ImageBo.COL_IMAGE)), 60, 60));
//                        image.setImage(cursor.getBlob(cursor.getColumnIndex(ImageBo.COL_IMAGE)));
//                    }
//                    while (cursor.moveToNext());
//                }
//            }
//        }
//
//        catch (SQLiteException e){
//            e.printStackTrace();
//            throw new SQLiteException();
//        }
//        finally {
//            cursor.close();
//        }
//        return image;
//
//    }
//
//    /**
//     * Guarda en la base de datos de preferencias el productos
//     * @param productBo
//     * @return true si es guardado correctamente...
//     */
//    public boolean saveProductToFavorite(ProductBo productBo){
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
//        ContentValues fields = new ContentValues();
//        fields.put(ProductBo.COL_ID, productBo.getId());
//        fields.put(ProductBo.COL_CATEGORY, productBo.getCategoryName());
//        fields.put(ProductBo.COL_HEADER, productBo.getTitle());
//        fields.put(ProductBo.COL_BODY, productBo.getBody());
//        fields.put(ProductBo.COL_PRICE, productBo.getPrice());
//        fields.put(ProductBo.COL_PHONE, productBo.getPhone());
//        fields.put(ProductBo.COL_EMAIL, productBo.getEmail());
//        fields.put(ProductBo.COL_NAME, productBo.getName());
//        fields.put(ProductBo.COL_PHOTO_COUNT, productBo.getPhotos_count());
//        fields.put(ProductBo.COL_TYPE, productBo.getType());
//        fields.put(ProductBo.COL_DATE, productBo.getDate());
//        fields.put(ProductBo.COL_DATE_FORMATED, productBo.getDateFormated());
//        fields.put(ProductBo.COL_PRIORITY, productBo.getPriority());
//
//        if(productBo.getType() == ProductBo.BANNER_PRODUCT){
//            fields.put(ProductBo.COL_BANNER, productBo.getImage().getImage());
//        } else{
//            ContentValues imagesFields = new ContentValues();
//            imagesFields.put(EntityImage.COL_PRODUCT_ID, productBo.getId());
//            imagesFields.put(EntityImage.COL_IMAGE, productBo.getImage().getImage());
//            db.insert(EntityImage.TABLE_IMAGES, null ,imagesFields);
//        }
//
//        fields.put(ProductBo.COL_TYPE, productBo.getType());
//        fields.put(ProductBo.COL_TYPE, productBo.getType());
//        fields.put(ProductBo.COL_TYPE, productBo.getType());
//
//        boolean inserted = db.insert(ProductBo.TABLE_PRODUCTS, null, fields) != -1;
//        return inserted;
//    }
//
//
//    /**
//     * Elimina un producto de los favoritos segun su ID
//     * @param id
//     * @return
//     */
//    public boolean removeFromFavorites(long id){
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
//        boolean resp = db.delete(ProductBo.TABLE_PRODUCTS, ProductBo.COL_ID + " = " + Long.toString(id), null) > 0;
//        db.close();
//        return resp;
//    }
//
//
//    /**
//     * buscar  un producto dentro de la base de datos de favoritos..
//     * @param id
//     * @return
//     */
//    public boolean findProductById(long id){
//        String query = "SELECT " + ProductBo.COL_ID + " FROM " + ProductBo.TABLE_PRODUCTS
//                + " WHERE " + ProductBo.COL_ID + " = " + Long.toString(id);
//
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = db.rawQuery(query,null);
//
//        return cursor.moveToFirst();
//    }
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
//    @Override
//    public synchronized void close() {
//        if(db != null){
//            db.close();
//        }
//        super.close();
//    }
//
//    public boolean open(){
//        try {
//            db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
//            return true;
//        } catch(SQLiteException sqle) {
//            sqle.printStackTrace();
//            db = null;
//            throw new SQLiteException();
//        }
//    }
//
//    public boolean checkDataBase() {
//        SQLiteDatabase checkDB = null;
//        try {
//            checkDB = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
//        } catch (SQLiteException e) {
//            e.printStackTrace();
//            throw new SQLiteException();
//        } finally {
//            close();
//        }
//
//        checkDB.close();
//
//        return true;
//    }
//
//    public boolean copyDataBase() throws IOException {
//        //String path = DB_PATH + DB_NAME;
//        File mFolder = new File(DB_PATH);
//        if (!mFolder.exists()) {
//            mFolder.mkdirs();
//        }
//
//        InputStream myInput = context.getAssets().open(DB_NAME);
//        String outFileName = DB_PATH + DB_NAME;
//        OutputStream myOutput = new FileOutputStream(outFileName);
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = myInput.read(buffer)) > 0) {
//            myOutput.write(buffer, 0, length);
//        }
//
//        myOutput.flush();
//        myOutput.close();
//        myInput.close();
//        close();
//
//        return true;
//    }
//
//    public  Boolean clearDatabase(){
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
//        try{
//        db.execSQL("DELETE FROM " + ProductBo.TABLE_PRODUCTS);
//        db.execSQL("DELETE FROM " + EntityImage.TABLE_IMAGES);
//        } catch (SQLException e){
//            throw new SQLException();
//        }
//
//        return true;
//    }



}
