package com.example.carolinamarin.stylestumble.data;

import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Created by carolinamarin on 2/23/16.
 */
public final class Category {
    public final String name;
    private final String mId;
    @Nullable
    private final String mDescription;

    public Category(@Nullable String id, @Nullable String description, String mname) {
        mId = UUID.randomUUID().toString();
        mDescription = description;
        name = mname;
    }

    public String getId() {
        return mId;
    }

    @Nullable
    public String getDescription() {
        return mDescription;
    }


}
