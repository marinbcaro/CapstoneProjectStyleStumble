package com.example.carolinamarin.stylestumble.data;

import android.support.annotation.Nullable;

/**
 * Created by carolinamarin on 3/15/16.
 */
public final class Product {
    public final Brand brand;
    public final Image image;
    private final String id;
    @Nullable
    private final String description;
    private final String name;
    private final String unbrandedName;
    private final String url;
    private final Double price;
    private final String salePrice;


    public Product(@Nullable String mid, @Nullable String mdescription, String mname, String murl, Brand mbrand, Double mprice, Image mimage, String msalePrice, String munBrandedName) {
        id = mid;
        description = mdescription;
        name = mname;
        url = murl;
        brand = mbrand;
        price = mprice;
        image = mimage;
        salePrice = msalePrice;
        unbrandedName = munBrandedName;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getUrl() {
        return url;
    }


    @Nullable
    public Brand getBrand() {
        return brand;
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

    @Nullable
    public String getUnbrandedName() {
        return unbrandedName;
    }

    public class Brand {
        public String name;
    }

    public class Image {
        public Sizes sizes;
    }

    public class Sizes {
        public IPhoneSmall IPhoneSmall;
    }

    public class IPhoneSmall {
        public String url;
    }

}
