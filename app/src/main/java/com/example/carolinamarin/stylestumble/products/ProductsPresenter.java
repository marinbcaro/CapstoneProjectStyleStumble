package com.example.carolinamarin.stylestumble.products;

import android.support.annotation.NonNull;

import com.example.carolinamarin.stylestumble.data.Product;
import com.example.carolinamarin.stylestumble.data.ProductDetail;
import com.example.carolinamarin.stylestumble.data.ProductsRepository;
import com.example.carolinamarin.stylestumble.util.EspressoIdlingResource;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by carolinamarin on 2/24/16.
 */
public class ProductsPresenter implements ProductsContract.UserActionsListener {

    private ProductsRepository mProductssRepository;
    private ProductsContract.View mProductsView;

    public ProductsPresenter(@NonNull ProductsRepository productsRepository, @NonNull ProductsContract.View productsView) {

        mProductssRepository = checkNotNull(productsRepository, "products cannot be null");
        mProductsView = checkNotNull(productsView, "productsview cannot be null");
    }

    @Override
    public void loadProducts(String catId, String search, int offset, boolean forceUpdate) {

        mProductsView.setProgressIndicator(true);
        if (forceUpdate) {
            mProductssRepository.refreshData();
        }
        EspressoIdlingResource.increment();
        mProductssRepository.getProducts(catId, search, offset, new ProductsRepository.LoadProductsCallback() {
            @Override
            public void onProductsLoaded(List<Product> categories) {
                EspressoIdlingResource.decrement();
                mProductsView.setProgressIndicator(false);
                mProductsView.showProducts(categories);
            }
        });
    }


    @Override
    public void openProductDetails(final String product) {
        mProductsView.setProgressIndicator(true);
        EspressoIdlingResource.increment();
        mProductssRepository.getProduct(product, new ProductsRepository.GetProductCallback() {
            @Override
            public void onProductLoaded(ProductDetail categories) {
                EspressoIdlingResource.decrement(); // Set app as idle.
                mProductsView.setProgressIndicator(false);
                mProductsView.showDetailProduct(product);
            }
        });
    }

}
