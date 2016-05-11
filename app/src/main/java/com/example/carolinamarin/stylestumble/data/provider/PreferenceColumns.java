package com.example.carolinamarin.stylestumble.data.provider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;

/**
 * Created by carolinamarin on 4/30/16.
 */
public class PreferenceColumns {

    @DataType(DataType.Type.TEXT) @NotNull
    public static final String SHOWNOTIFICATION = "showNotification";

}
