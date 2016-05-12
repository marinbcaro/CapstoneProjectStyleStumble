package com.chessen.project.stylestumble.addProductWishList;

import android.support.annotation.NonNull;

import com.chessen.project.stylestumble.data.ProductDetail;
import com.chessen.project.stylestumble.data.ProductsRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by carolinamarin on 4/4/16.
 */
public class WishListPresenter implements WishListContract.UserActionsListener {

    private ProductsRepository mProductsRepository;
    private WishListContract.View mProductsView;


    public WishListPresenter(@NonNull ProductsRepository productsRepository,   @NonNull WishListContract.View productsView ){
        mProductsRepository=checkNotNull(productsRepository,"products cannot be null");
        mProductsView=checkNotNull(productsView,"productsview cannot be null");
    }

    @Override
    public void openProductDetails(final String product) {
        mProductsRepository.getProduct(product, new ProductsRepository.GetProductCallback() {
            @Override
            public void onProductLoaded(ProductDetail categories) {
                mProductsView.showDetailProduct(product);
            }
        });
    }
}

