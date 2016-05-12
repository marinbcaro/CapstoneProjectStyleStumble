package com.chessen.project.stylestumble.addsaleProducts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chessen.project.stylestumble.data.ProductDetail;

/**
 * Created by carolinamarin on 4/24/16.
 */
public interface ProductSaleContract {

    interface View {

        void showNotification(ProductDetail id);
        void showDetailProduct(String id);
    }

    interface UserActionsListener {
        void loadProduct(@Nullable String productId);
        void openProductDetails(@NonNull String productId);
    }

}
