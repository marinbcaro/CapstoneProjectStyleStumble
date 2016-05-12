package com.chessen.project.stylestumble.data;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by carolinamarin on 3/17/16.
 */
public class ProductRepositories {
    private static ProductsRepository repository = null;

    private ProductRepositories() {
    }

    public synchronized static ProductsRepository getInMemoryRepoInstance(@NonNull ProductsServiceApi productsServiceApi) {
        checkNotNull(productsServiceApi);
        if (null == repository) {
            repository = new InMemoryProductsRepository(productsServiceApi);
        }
        return repository;
    }
}
