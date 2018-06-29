package com.timbirichi.eltimbirichi.data.service.local;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

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
                    category.setName(cursor.getString(cursor.getColumnIndex(SubCategory.SUBCATEGORY_COL_NAME)));

                    List<Banner> banners =  getBannersByCategoryId(category.getId());
                    if(banners != null && banners.get(0).getImage() != null){
                        byte [] image = banners.get(0).getImage();
                        category.setImage(image);
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
//
//    /**
//     * Ordena las categorias por padres y sus respectivos hijos.
//     * @param categories
//     * @return
//     */
//    private List<EntityCategory> sortCategories(List<EntityCategory> categories){
//        List<EntityCategory> sorted = new ArrayList<>();
//
//        for (int i = 0; i < categories.size() ; i++){
//            if(categories.get(i).getParent() > 0) break;
//
//            EntityCategory padre = categories.get(i);
//            sorted.add(padre);
//
//            for (int j = i+1; j < categories.size() ; j++){
//                EntityCategory hijo = categories.get(j);
//                if(hijo.getParent() == padre.getId()){
//                    sorted.add(hijo);
//                }
//            }
//        }
//        return sorted;
//    }
//
//
//    /**
//     * SELECT * FROM anuncios WHERE tipo = 'Destacados' AND cat_id = 0 ORDER BY prioridad ASC
//     * SELECT * FROM anuncios WHERE tipo = 'Destacados' AND cat_id = 0 ORDER BY prioridad ASC LIMIT "+limit
//     * Selecciona los productos destacados para el banner, en caso de que sea portada no hay limites.
//     * Si es una categoria el limite es 5
//     * @return Lista de productos del banner
//     */
//    public List<ProductBo> loadBannerProducts(int limit){
//
//        String lim  = limit > 0 ? " LIMIT " + Integer.toString(limit) : "";
//
//        String query = "SELECT * FROM " + ProductBo.TABLE_PRODUCTS
//                       + " WHERE " + ProductBo.COL_TYPE + " = '" + ProductBo.BANNER_PRODUCT
//                       + "' AND " + ProductBo.COL_CATEGORY_ID + " = 0 ORDER BY "
//                       + ProductBo.COL_PRIORITY + " ASC " + lim;
//
//        List<ProductBo> products = new ArrayList<>();
//
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = db.rawQuery(query,null);
//
//        ProductBo product = null;
//        try{
//            if(cursor.moveToFirst())
//            {
//                do{
//                    product = new ProductBo();
//                    product.setId(cursor.getLong(cursor.getColumnIndex(ProductBo.COL_ID)));
//                    product.setTitle(cursor.getString(cursor.getColumnIndex(ProductBo.COL_HEADER)));
//                    product.setBody(cursor.getString(cursor.getColumnIndex(ProductBo.COL_BODY)));
//                    product.setPrice(cursor.getFloat(cursor.getColumnIndex(ProductBo.COL_PRICE)));
//                    product.setPhone(cursor.getString(cursor.getColumnIndex(ProductBo.COL_PHONE)));
//                    product.setEmail(cursor.getString(cursor.getColumnIndex(ProductBo.COL_EMAIL)));
//                    product.setName(cursor.getString(cursor.getColumnIndex(ProductBo.COL_NAME)));
//                    product.setPhotos_count(cursor.getInt(cursor.getColumnIndex(ProductBo.COL_PHOTO_COUNT)));
//                    product.setDate(cursor.getString(cursor.getColumnIndex(ProductBo.COL_DATE)));
//                    product.setPriority(cursor.getInt(cursor.getColumnIndex(ProductBo.COL_PRIORITY)));
//                    product.setDateFormated(cursor.getString(cursor.getColumnIndex(ProductBo.COL_DATE_FORMATED)));
//
//                    byte [] img = cursor.getBlob(cursor.getColumnIndex(ProductBo.COL_BANNER));
//                    product.setType(ProductBo.BANNER_PRODUCT);
//                    product.setCategory(ProductBo.BANNER_CAT_ID);
//
//                        ImageBo image = new ImageBo(product.getId(), img);
//                       // ImageBo image = new ImageBo(product.getId(), Utils.decode2Bitmap(img, 1536,624));
//
//                    product.setImage(image);
//                    product.setCategoryName(ProductBo.BANNER_PRODUCT_CATEGORY_NAME);
//                    products.add(product);
//                }
//                while (cursor.moveToNext());
//            }
//        }
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
//        return products;
//    }
//
//
//    /**
//     * Devuelve un producto dado su ID
//     * @param productId
//     * @return
//     */
//    public ProductBo getProductById(long productId){
//
//        String query = "SELECT " + ProductBo.TABLE_PRODUCTS+ ".*, " + EntityCategory.TABLE_CATEGORY +"."+EntityCategory.COL_NAME +  " FROM " + ProductBo.TABLE_PRODUCTS + " JOIN " + EntityCategory.TABLE_CATEGORY
//                       + " ON " + ProductBo.TABLE_PRODUCTS + "." +ProductBo.COL_CATEGORY_ID  + " = " + EntityCategory.TABLE_CATEGORY + "." + EntityCategory.COL_ID
//                       + " WHERE " + ProductBo.COL_ID + " = " + Long.toString(productId);
//
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
//        Cursor cursor = db.rawQuery(query,null);
//        ProductBo productBo = null;
//
//        try{
//            if (cursor.moveToFirst()){
//              productBo = createOrdinaryProduct(cursor, db);
//              productBo.setCategoryName(cursor.getString(cursor.getColumnIndex(EntityCategory.COL_NAME)));
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
//
    /**
     *
     * Carga loa anuncios de la portada dentro de un limite de anuncios
     * @return Anuncios de la portada
     */
    public List<Product> loadCoverPageProducts(){
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM " + Product.PRODUCT_TABLE
                     + " WHERE " + Product.PRODUCT_COL_COVER_PAGE + " = " + Integer.toString(Product.COVER_PAGE)
                     + " ORDER BY " + Product.PRODUCT_COL_ULTRA + ", " + Product.PRODUCT_COL_COVER_PAGE + ", " + Product.PRODUCT_COL_ID + Product.ORDER_DESC
                     + " LIMIT " + Integer.toString(6);

        // todo: Cambiar el limite de productos de la portada...


        SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery(query,null);

        try{
            if(cursor.moveToFirst())
            {
                do{
                    Product product = createOrdinaryProduct(cursor, db);
                    products.add(product);
                }
                while (cursor.moveToNext());
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
//
//
//    /**
//     * Devuelve una categoria segun el ID
//     * @param id
//     * @return
//     */
//    public CategoryBo getCategoryById(long id, SQLiteDatabase db){
//
//        String query = "SELECT " + EntityCategory.COL_NAME + " FROM " + EntityCategory.TABLE_CATEGORY
//                     + " WHERE " + EntityCategory.COL_ID + " = " + Long.toString(id);
//
//        Cursor cursor = db.rawQuery(query,null);
//
//        CategoryBo categoryBo = new CategoryBo();
//
//        try{
//            if (cursor.moveToFirst()){
//                categoryBo.setName(cursor.getString(cursor.getColumnIndex(EntityCategory.COL_NAME)));
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
//        }
//
//        return categoryBo;
//    }
//
//
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

        String orderBy = (order != null && !order.isEmpty()) ? " ORDER BY " + order + orderType : " ";

        String withImage = image ? " AND " + Product.PRODUCT_COL_PHOTO_COUNT + " > 0" : " ";

        String newProd = "";
        if(state == ProductState.NEW){
            newProd = " AND " + Product.PRODUCT_COL_IS_NEW + " = 1 ";
        }

        String prov = province > 0 ? " AND " + Product.PRODUCT_COL_PROVINCE + " = " + Long.toString(province) + " " : " ";


        String min = minPrice > 0 ? " AND " + Product.PRODUCT_COL_PRICE + " > " + Double.toString(minPrice) : " ";

        String max = maxPrice > 0 ? " AND " + Product.PRODUCT_COL_PRICE + " < " + Double.toString(maxPrice) : " ";

        String limit = " LIMIT " + Integer.toString(start) + ", " + Integer.toString(end);

        String cat = category.getId() != SubCategory.CATEGORY_LASTED ? Product.PRODUCT_COL_SUBCATEGORY + " = " + Long.toString(category.getId()) : Product.PRODUCT_COL_SUBCATEGORY + " <> " + Long.toString(category.getId());

        String query = "SELECT * FROM " + Product.PRODUCT_TABLE
                + " WHERE " + cat
                + newProd + prov
                + like + withImage + min + max + " GROUP BY " + Product.PRODUCT_COL_ID
                + orderBy + ", " + Product.PRODUCT_COL_ULTRA + Product.ORDER_DESC + ", " + Product.PRODUCT_COL_COVER_PAGE + Product.ORDER_DESC
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

//
//
//    /**
//     * Busca los productos filtrados desde la portada de la aplicacion
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
//    public List<ProductBo> loadProductsFilteredFromCoverPage(String text, int start, int end,
//                                                                 String order, String orderType,
//
//                                                                 boolean image, double minPrice, double maxPrice){
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
//        // Eliminar el AND del withImage
//        if (text != null && text.trim().isEmpty() && !image){
//            withImage = ProductBo.COL_PHOTO_COUNT + " > 0";
//            into = true;
//        }
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
//        }
//
//        String limit = " LIMIT " + Integer.toString(start) + ", " + Integer.toString(end);
//
//        String query = "SELECT * FROM " + ProductBo.TABLE_PRODUCTS
//                + where + like + withImage + min + max + " GROUP BY " + ProductBo.COL_ID
//                + orderBy + limit;
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
//
//                    ProductBo product = createOrdinaryProduct(cursor, db);
//                    long catId = cursor.getLong(cursor.getColumnIndex(ProductBo.COL_CATEGORY_ID));
//
//                    String catName;
//
//                    if (catId != 0){
//                        CategoryBo categoryBo = getCategoryById(catId, db);
//                        catName = categoryBo.getName();
//                    }else{
//                        catName = ProductBo.COVER_PAGE_CATEGORY_NAME;
//                    }
//
//                    product.setCategoryName(catName);
//                    product.setType(cursor.getString(cursor.getColumnIndex(ProductBo.COL_TYPE)));
//
//                    products.add(product);
//
//
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
        product.setProvince(getProvinceForProduct(cursor.getLong(cursor.getColumnIndex(Product.PRODUCT_COL_PROVINCE)), db));

        //product.setDate(cursor.getString(cursor.getColumnIndex(Product.COL_DATE)));

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
                        image.setImage(cursor.getBlob(cursor.getColumnIndex(Image.IMAGE_COL_IMAGE)));
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
                    banners = new ArrayList<>();
                    do {
                        banner = new Banner();
                        banner.setId(cursor.getLong(cursor.getColumnIndex(Banner.BANNER_COL_ID)));
                        banner.setImage(cursor.getBlob(cursor.getColumnIndex(Banner.BANNER_COL_IMAGE)));
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
