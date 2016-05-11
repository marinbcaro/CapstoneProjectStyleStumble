package com.example.carolinamarin.stylestumble.data;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by carolinamarin on 3/5/16.
 */
public class FakeCategoriesServiceApiImpl implements CategoriesServiceApi{


    // TODO replace this with a new test specific data set.
 //   private static final ArrayMap<String, Category> NOTES_SERVICE_DATA = new ArrayMap();

    private static List<Category> CATEGORIES = Lists.newArrayList(new Category("123", "Description1","12"),
            new Category("123", "Description2","11"));


    @Override
    public void getAllCategories(CategoriesServiceCallback<List<Category>> callback) {
        callback.onLoaded(Lists.newArrayList(CATEGORIES));
    }

}
