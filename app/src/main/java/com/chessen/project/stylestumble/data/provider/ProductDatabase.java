package com.chessen.project.stylestumble.data.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by carolinamarin on 4/3/16.
 */
@Database(version = ProductDatabase.VERSION)
public final class ProductDatabase {

    public static final int VERSION = 1;

    @Table(ProductColumns.class) public static final String PRODUCTS = "products";
    @Table(WishListColumns.class) public static final String WISHLIST = "wishlist";
    @Table(PreferenceColumns.class) public static final String USERPREFERENCES = "userpreferences";

}
