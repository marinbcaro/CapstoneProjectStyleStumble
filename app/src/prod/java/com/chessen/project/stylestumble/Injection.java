package com.chessen.project.stylestumble;

import com.chessen.project.stylestumble.data.ProductRepositories;
import com.chessen.project.stylestumble.data.ProductsRepository;
import com.chessen.project.stylestumble.data.ProductsServiceApiImpl;

/**
 * Created by carolinamarin on 2/25/16.
 */
public class Injection {


    public static ProductsRepository provideProductsRepository() {
        return ProductRepositories.getInMemoryRepoInstance(new ProductsServiceApiImpl());
    }
}
