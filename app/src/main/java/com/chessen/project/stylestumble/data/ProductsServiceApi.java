package com.chessen.project.stylestumble.data;

import java.util.List;

/**
 * Created by carolinamarin on 3/16/16.
 */
public interface ProductsServiceApi {
    void getProductsCategories(String catId, String search, int offset, ProductsServiceCallback<List<Product>> callback);

    void getProduct(String productId, GetProductServiceCallback<ProductDetail> callback);

    interface ProductsServiceCallback<T> {
        void onLoaded(T products);
    }

    interface GetProductServiceCallback<T> {
        void onProductLoaded(T product);
    }
}
