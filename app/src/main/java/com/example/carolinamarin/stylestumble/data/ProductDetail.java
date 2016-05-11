package com.example.carolinamarin.stylestumble.data;

import android.support.annotation.Nullable;

/**
 * Created by carolinamarin on 4/15/16.
 */
public class ProductDetail {

    private final String id;
    @Nullable
    private final String description;
    private final String name;
    private final String clickUrl;
    public final Brand brand;
    public final Retailer retailer;
    private final Double price;
    public final Image image;
    private final String salePrice;

    public class Image {
        public Sizes sizes;
    }

    public class Sizes {
        public IPhone IPhone;
    }

    public class IPhone {
        public String url;
    }

    public class Brand {
        public String name;
    }

    public class Retailer {
        public String name;
    }

    public ProductDetail(@Nullable String mid, @Nullable String mdescription, String mname, String murl, Brand mbrand, Double mprice, Image mimage, Retailer mretailer, String msalePrice) {
        //   mId = UUID.randomUUID().toString();
        id = mid;
        retailer = mretailer;
        description = mdescription;
        name = mname;
        clickUrl = murl;
        brand = mbrand;
        price = mprice;
        image = mimage;
        salePrice = msalePrice;

    }

    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getClickUrl() {
        return clickUrl;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public Image getImage() {
        return image;
    }

    @Nullable
    public double getPrice() {
        return price;
    }

    @Nullable
    public String getSalePrice() {
        return salePrice;
    }

}
