package com.timbirichi.eltimbirichi.domain.repository;

import com.timbirichi.eltimbirichi.data.model.Category;
import com.timbirichi.eltimbirichi.data.model.Product;
import com.timbirichi.eltimbirichi.data.model.SubCategory;
import com.timbirichi.eltimbirichi.presentation.model.constant.ProductState;

import java.util.List;

import io.reactivex.Observable;

public interface IProductRepository {

    /**
     * Retorna los productos para el banner en caso ded que sea una categoria tiene un limite
     * @param limit
     * @return
     */
    Observable<List<Product>> loadBannerProducts(int limit);

    /**
     * Retorna los productos de la portada
     * @param start limite de inicio
     * @param end limite del fin
     * @return
     */
    Observable<List<Product>> loadCoverPageProduct();

    /**
     * Carga los productos filtrados desde las categorias
     * @param text
     * @param start
     * @param end
     * @param category
     * @param order
     * @param orderType
     * @param image
     * @param minPrice
     * @param maxPrice
     * @return
     */
    Observable<List<Product>> loadProductsByCategory(String text, int start, int end,
                                                     SubCategory category, String order, String orderType,
                                                     boolean image, double minPrice, double maxPrice,
                                                     ProductState state, long province);

    /**
     * Carga los productos filtrados desde la portada y los favoritos
     * @param text
     * @param start
     * @param end
     * @param order
     * @param orderType
     * @param image
     * @param minPrice
     * @param maxPrice
     * @param database Si la busqueda es en la base de datos de preferencias o local
     * @return
     */
    Observable<List<Product>> loadProductsFilteredFromCoverPage(String text, int start, int end,
                                                                  String order, String orderType,
                                                                  boolean image, double minPrice,
                                                                  double maxPrice, int database);


    /**
     * Devuelve un producto dado un ID
     * @param productId
     * @return
     */
    Observable<Product> getProductById(long productId);


    /*
     * Para la base de datos de preferencias....
     * */


    /**
     * Busca un producto segun su iD
     * @param id
     * @return true si el producto existe
     */
    Observable<Boolean> findProductById(long id);

    /**
     * Guarda en la base de datos de preferencias un producto
     * @return
     */
    Observable<Boolean> saveProductToFavorites(Product product);


    Observable<Boolean> removeFromFavorite(long productId);


    /**
     * Busca una base de datos (preferencias)
     * @return true si la base de datos existe, false si la base de datos no existe
     */
    Observable<Boolean> checkDatabase();


    /**
     * Copia la base de datos de preferencia de los assets para la carpeta que utilizara la aplicacion
     * @return
     */
    Observable<Boolean> copyDatabase();


    Observable<Boolean> cleaDatabase(int dbType);





}
