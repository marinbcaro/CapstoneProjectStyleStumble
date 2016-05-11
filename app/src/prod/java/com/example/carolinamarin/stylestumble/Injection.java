package com.example.carolinamarin.stylestumble;

import com.example.carolinamarin.stylestumble.data.ProductRepositories;
import com.example.carolinamarin.stylestumble.data.ProductsRepository;
import com.example.carolinamarin.stylestumble.data.ProductsServiceApiImpl;

/**
 * Created by carolinamarin on 2/25/16.
 */
public class Injection {


    public static ProductsRepository provideProductsRepository() {
        return ProductRepositories.getInMemoryRepoInstance(new ProductsServiceApiImpl());
    }
}
