package com.example.carolinamarin.stylestumble.addProductWishList;

import android.support.annotation.NonNull;

/**
 * Created by carolinamarin on 4/4/16.
 */
public interface WishListContract {


    interface View {
        void showDetailProduct(String id);
    }

    interface UserActionsListener {
        void openProductDetails(@NonNull String productId);
    }
}
