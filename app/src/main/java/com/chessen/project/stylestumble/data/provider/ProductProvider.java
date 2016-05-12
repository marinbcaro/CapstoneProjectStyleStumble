package com.chessen.project.stylestumble.data.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by carolinamarin on 4/3/16.
 */
@ContentProvider(authority = ProductProvider.AUTHORITY, database = ProductDatabase.class)
public class ProductProvider {
    public static final String AUTHORITY = "com.chessen.project.stylestumble.data.provider.ProductProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(String ... paths){
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = ProductDatabase.PRODUCTS)
    public static class Products {

        @ContentUri(
                path = "products",
                type = "vnd.android.cursor.dir/products")
        public static final Uri PRODUCTS = buildUri("products");

        @InexactContentUri(
                name = "PRODUCT_ID",
                path =  "products/#",
                type = "vnd.android.cursor.item/products",
                whereColumn = ProductColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id){

            return buildUri("products", String.valueOf(id));
        }
    }

    @TableEndpoint(table = ProductDatabase.WISHLIST)
    public static class WishList{
        @ContentUri(
                path = "wishlist",
                type = "vnd.android.cursor.dir/wishlist")
        public static final Uri WISHLIST = buildUri("wishlist");


        @ContentUri(
                path = "productssale",
                type = "vnd.android.cursor.dir/productssale",
                where=WishListColumns.SALEPRICE+" > 0")
        public static final Uri PRODUCTSALE = buildUri("productssale");


        @InexactContentUri(
                name = "WISHLIST_ID",
                path = "wishlist/#",
                type = "vnd.android.cursor.item/wishlist",
                whereColumn = WishListColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id){
            return buildUri("wishlist", String.valueOf(id));
        }
    }

    @TableEndpoint(table = ProductDatabase.USERPREFERENCES)
    public static class UserPreferences{
        @ContentUri(
                path = "userpreferences",
                type = "vnd.android.cursor.dir/userpreferences")
        public static final Uri USERPREFERENCES = buildUri("userpreferences");

    }


}
