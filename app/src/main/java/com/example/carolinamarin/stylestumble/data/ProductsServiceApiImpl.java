package com.example.carolinamarin.stylestumble.data;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.example.carolinamarin.stylestumble.data.Product.Brand;
import com.example.carolinamarin.stylestumble.data.Product.Image;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by carolinamarin on 2/25/16.
 */
public class ProductsServiceApiImpl implements ProductsServiceApi {

    private static final String BASE_URL = "/api/v2/products";
    private static final String API_KEY = "?pid=uid9049-30800243-85";
    private static final String API_URL = BASE_URL + API_KEY;
    private ArrayMap<String, Product> DATA = new ArrayMap(2);


    @Override
    public void getProductsCategories(String catId, String search, int offset, final ProductsServiceCallback callback) {
        getData(catId, search, offset, callback);
    }

    @Override
    public void getProduct(final String productId, final GetProductServiceCallback callback) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //  Log.d("MyTAG", "OkHttp: " + message);
            }
        });
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl("http://api.shopstyle.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ShopStyleService mService = retrofit.create(ShopStyleService.class);

        Call<ProductDetail> call = mService.getProduct(productId);
        call.enqueue(new Callback<ProductDetail>() {
            @Override
            public void onResponse(Call<ProductDetail> call, Response<ProductDetail> response) {
                int statusCode = response.code();

                if (response.isSuccess()) {
                    DATA.clear();
                    ProductDetail productInfo = response.body();
                    callback.onProductLoaded(productInfo);

                } else {
                    Log.d("TOTAL ERROR", response.message());
                }
            }

            @Override
            public void onFailure(Call<ProductDetail> call, Throwable t) {
                // Log error here since request failed
                Log.d("Error", t.getMessage());
            }
        });

    }


    public void getData(String catId, String search, int offset, final ProductsServiceCallback callback) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //  Log.d("MyTAG", "OkHttp: " + message);
            }
        });
        OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl("http://api.shopstyle.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        ShopStyleService mService = retrofit.create(ShopStyleService.class);

        Call<ListProducts> call = mService.listProducts(catId, search, offset);
        call.enqueue(new Callback<ListProducts>() {
            @Override
            public void onResponse(Call<ListProducts> call, Response<ListProducts> response) {
                int statusCode = response.code();

                if (response.isSuccess()) {
                    DATA.clear();

                    List<Product> contributors = response.body().products;
                    for (Product productInfo : contributors) {
                        Brand h = productInfo.getBrand();
                        Image ima = productInfo.getImage();

                        String salePrice = "0";
                        String unbranded = "";
                        if (productInfo.getSalePrice() != null) {
                            salePrice = productInfo.getSalePrice();
                        }
                        if (productInfo.getUnbrandedName() != null) {
                            unbranded = productInfo.getUnbrandedName();
                        }

                        //  addProduct(contributor.getId(), contributor.getName(), contributor.getName(), contributor.getUrl(), h, contributor.getPrice(), ima);
                        Product product = new Product(productInfo.getId(), productInfo.getName(), productInfo.getName(), productInfo.getUrl(), h, productInfo.getPrice(), ima, salePrice, unbranded);

                        DATA.put(product.getId(), product);
                    }
                    List<Product> products = new ArrayList<>(DATA.values());
                    callback.onLoaded(products);

                } else {
                    Log.d("TOTAL ERROR", response.message());
                }

            }

            @Override
            public void onFailure(Call<ListProducts> call, Throwable t) {
                // Log error here since request failed
                Log.d("Error", t.getMessage());
            }
        });
    }


    public interface ShopStyleService {
        @GET(API_URL + "&sort=Popular&limit=10")
        Call<ListProducts> listProducts(@Query("cat") String catId, @Query("fts") String search, @Query("offset") int offset);

        @GET(BASE_URL + "/{id}" + API_KEY)
        Call<ProductDetail> getProduct(@Path("id") String id);
    }


}
