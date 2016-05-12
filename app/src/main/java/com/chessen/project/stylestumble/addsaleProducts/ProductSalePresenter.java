package com.chessen.project.stylestumble.addsaleProducts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chessen.project.stylestumble.data.ProductDetail;
import com.chessen.project.stylestumble.data.ProductsRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by carolinamarin on 4/24/16.
 */
public class ProductSalePresenter implements ProductSaleContract.UserActionsListener {

    private ProductsRepository mProductssRepository;
    private ProductSaleContract.View mProductsDetailView;
    private  ProductDetail pro;

    public ProductSalePresenter(@NonNull ProductsRepository productsRepository,  @NonNull ProductSaleContract.View productsView  ){
        mProductssRepository=checkNotNull(productsRepository,"products cannot be null");
        mProductsDetailView=checkNotNull(productsView,"productsview cannot be null");
    }


    public void showProduct(ProductDetail product) {
        mProductsDetailView.showNotification(product);
    }

    @Override
    public void loadProduct(@Nullable String productId) {
        mProductssRepository.getProduct(productId, new ProductsRepository.GetProductCallback() {

            @Override
            public void onProductLoaded(ProductDetail product) {
                showProduct(product);
            }

        });
    }

    @Override
    public void openProductDetails(final String product) {

        mProductssRepository.getProduct(product, new ProductsRepository.GetProductCallback() {
            @Override
            public void onProductLoaded(ProductDetail categories) {
                mProductsDetailView.showDetailProduct(product);
            }
        });
    }

}
