package com.example.carolinamarin.stylestumble.products;

import android.support.annotation.NonNull;

import com.example.carolinamarin.stylestumble.data.Product;

import java.util.List;

/**
 * Created by carolinamarin on 2/24/16.
 */
public interface ProductsContract {


    interface View {
        void setProgressIndicator(boolean active);
        void showProducts(List<Product> categories);
        void showDetailProduct(String id);
    }

    interface UserActionsListener {
        void loadProducts(String catId,String search,int offset,boolean forceUpdate);
        void openProductDetails(@NonNull String productId);
    }

}
