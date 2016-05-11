package com.example.carolinamarin.stylestumble.categories;

import android.support.annotation.NonNull;

import com.example.carolinamarin.stylestumble.data.Category;

/**
 * Created by carolinamarin on 2/22/16.
 */
public interface CategoriesContract {
    interface View {
        void showCategories();

        void showAllProducts(String catId);
    }

    interface UserActionsListener {

        void loadCategories(boolean forceUpdate);

        void showProducts(@NonNull Category requestedCategory);

    }
}
