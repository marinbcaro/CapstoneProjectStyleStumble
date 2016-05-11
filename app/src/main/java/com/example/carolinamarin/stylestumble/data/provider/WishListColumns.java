package com.example.carolinamarin.stylestumble.data.provider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by carolinamarin on 4/5/16.
 */
public interface WishListColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey
    public static final String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String NAME = "name";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String DESCRIPTION = "description";

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String URL = "url";

    @DataType(DataType.Type.TEXT)
    public static final String BRAND = "brand";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String PRICE = "price";

    @DataType(DataType.Type.REAL) @NotNull
    public static final String SALEPRICE = "salePrice";
}
