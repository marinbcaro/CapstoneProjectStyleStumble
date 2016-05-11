package com.example.carolinamarin.stylestumble.data;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by carolinamarin on 3/17/16.
 */
public class InMemoryProductsRepository implements ProductsRepository {

    private final ProductsServiceApi mCategoriesServiceApi;


    @VisibleForTesting
    List<Product> mCachedCategories;

    public InMemoryProductsRepository(@NonNull ProductsServiceApi categoriesServiceApi) {
        mCategoriesServiceApi = checkNotNull(categoriesServiceApi);
    }

    @Override
    public void getProducts(String catdId, String search, int offset, @NonNull final ProductsRepository.LoadProductsCallback callback) {
        checkNotNull(callback);
        // Load from API only if needed.
        if (mCachedCategories == null) {
            mCategoriesServiceApi.getProductsCategories(catdId, search, offset, new ProductsServiceApi.ProductsServiceCallback<List<Product>>() {
                @Override
                public void onLoaded(List<Product> products) {
                    mCachedCategories = ImmutableList.copyOf(products);
                    callback.onProductsLoaded(mCachedCategories);
                }
            });
        } else {
            callback.onProductsLoaded(mCachedCategories);
        }
    }


    @Override
    public void getProduct(@NonNull final String productId, @NonNull final ProductsRepository.GetProductCallback callback) {
        checkNotNull(productId);
        checkNotNull(callback);

        mCategoriesServiceApi.getProduct(productId, new ProductsServiceApi.GetProductServiceCallback<ProductDetail>() {
            @Override
            public void onProductLoaded(ProductDetail product) {
                callback.onProductLoaded(product);
            }
        });
    }


    @Override
    public void refreshData() {
        mCachedCategories = null;
    }
}
