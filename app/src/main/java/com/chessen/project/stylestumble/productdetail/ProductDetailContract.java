package com.chessen.project.stylestumble.productdetail;

import android.support.annotation.Nullable;

/**
 * Created by carolinamarin on 4/11/16.
 */
public interface ProductDetailContract {

    interface View {
        void setProgressIndicator(boolean active);
        void showTitle(String title,String image);
        void showDescription(String description);
        void showRetailer(String retailer);
        void showPrice(Double price,String salePrice);
        void showShop(String url);
    }

    interface UserActionsListener {
        void openProduct(@Nullable String productId);
    }
}
