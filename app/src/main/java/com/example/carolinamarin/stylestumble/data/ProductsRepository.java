package com.example.carolinamarin.stylestumble.data;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by carolinamarin on 3/15/16.
 */
public interface ProductsRepository {
    void getProducts(@NonNull String catId, String search, int offset, @NonNull LoadProductsCallback callback);

    void getProduct(@NonNull String productId, @NonNull GetProductCallback callback);

    void refreshData();

    interface LoadProductsCallback {
        void onProductsLoaded(List<Product> products);
    }

    interface GetProductCallback {
        void onProductLoaded(ProductDetail product);

    }
}
