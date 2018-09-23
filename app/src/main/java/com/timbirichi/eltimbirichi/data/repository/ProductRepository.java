package com.timbirichi.eltimbirichi.data.repository;

import com.timbirichi.eltimbirichi.data.datastore.local_sqlite.ProductsDataStore;
import com.timbirichi.eltimbirichi.data.datastore.preferences.sqlite.DbPreferencesDataStore;
import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.domain.repository.IProductRepository;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;
import com.timbirichi.eltimbirichi.utils.Utils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class ProductRepository implements IProductRepository {

    ProductsDataStore productsDataStore;
    DbPreferencesDataStore dbPreferencesDataStore;

    @Inject
    public ProductRepository(ProductsDataStore productsDataStore,
                             DbPreferencesDataStore dbPreferencesDataStore) {

        this.productsDataStore = productsDataStore;
        this.dbPreferencesDataStore = dbPreferencesDataStore;
    }

    @Override
    public Observable<List<Product>> loadBannerProducts(int limit) {
        return productsDataStore.loadBannerProducts(limit);
    }

    @Override
    public Observable<List<Product>> loadCoverPageProduct() {
        return productsDataStore.loadCoverPageProduct();
    }

    @Override
    public Observable<List<Product>> loadProductsByCategory(String text, int start, int end,
                                                            SubCategory category, String order, String orderType,
                                                            boolean image, double minPrice, double maxPrice,
                                                            ProductState state, long province) {

        return productsDataStore.loadProductsByCategory(text, start, end,
                category, order, orderType,
                image, minPrice, maxPrice, state, province);
    }

    @Override
    public Observable<List<Product>> loadLastedProducts(String text, int start, int end, String order, String orderType, boolean image, double minPrice, double maxPrice, ProductState state, long province) {

        return productsDataStore.loadLastedNewProducts(text, start, end,
                order, orderType, image,
                minPrice, maxPrice, state,
                province);
    }

    @Override
    public Observable<List<Product>> loadProductsFilteredFromCoverPage(String text, int start, int end,
                                                                         String order, String orderType,
                                                                         boolean image, double minPrice,
                                                                         double maxPrice, int database) {

        if (database == Utils.DB_LOCAL){
            return productsDataStore.loadProductsFilteredFromCoverPage(text, start, end,
                    order, orderType, image,
                    minPrice, maxPrice);
        }

//        return dbPreferencesDataStore.getProducts(text, start, end,
//                                                      order, orderType, image,
//                                                      minPrice, maxPrice);

        return null;
    }

    @Override
    public Observable<Product> getProductById(long productId, SubCategory subCategory) {
        return productsDataStore.getProductById(productId, subCategory);
    }

    @Override
    public Observable<List<Product>> getFavorites(String text, int start, int end, String order,
                                                  String orderType, boolean image, double minPrice,
                                                  double maxPrice, ProductState state, long province) {
        return dbPreferencesDataStore.getProducts(text, start, end, order,
                orderType, image, minPrice,
                maxPrice, state, province);
    }

    @Override
    public Observable<Boolean> findProductById(long id) {
        return dbPreferencesDataStore.findProductById(id);
    }

    @Override
    public Observable<Boolean> saveProductToFavorites(Product productBo) {
        return dbPreferencesDataStore.saveProduct(productBo);
    }

    @Override
    public Observable<Boolean> removeFromFavorite(long productId) {
        return dbPreferencesDataStore.removeFromFavorite(productId);
    }

    @Override
    public Observable<Boolean> checkDatabase() {
        return dbPreferencesDataStore.checkDatabase();
    }

    @Override
    public Observable<Boolean> copyDatabase() {
        return dbPreferencesDataStore.copyDatabase();
    }

    @Override
    public Observable<Boolean> cleaDatabase() {
        return dbPreferencesDataStore.clearDatabase();
    }
}
