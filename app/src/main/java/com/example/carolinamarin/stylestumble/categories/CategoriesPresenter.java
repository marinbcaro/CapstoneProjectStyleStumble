package com.example.carolinamarin.stylestumble.categories;

import android.support.annotation.NonNull;

import com.example.carolinamarin.stylestumble.data.Category;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by carolinamarin on 2/22/16.
 */
public class CategoriesPresenter implements CategoriesContract.UserActionsListener {


    private CategoriesContract.View mCategoriesView;

    public CategoriesPresenter(@NonNull CategoriesContract.View categoriesView) {
        mCategoriesView = checkNotNull(categoriesView, "categoriesview cannot be null");
    }


    @Override
    public void loadCategories(boolean forceUpdate) {
        mCategoriesView.showCategories();
    }

    @Override
    public void showProducts(Category category) {
        mCategoriesView.showAllProducts(category.name);
    }

}
