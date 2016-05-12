package com.chessen.project.stylestumble;

import com.chessen.project.stylestumble.data.FakeCategoriesServiceApiImpl;

/**
 * Created by carolinamarin on 3/5/16.
 */
public class Injection {

    public static CategoriesRepository provideCategoriesRepository() {
        return CategoryRepositories.getInMemoryRepoInstance(new FakeCategoriesServiceApiImpl());
    }
}
