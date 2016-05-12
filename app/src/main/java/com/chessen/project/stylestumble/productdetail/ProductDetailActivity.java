package com.chessen.project.stylestumble.productdetail;

import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.chessen.project.stylestumble.R;
import com.chessen.project.stylestumble.util.EspressoIdlingResource;
import com.chessen.project.stylestumble.util.StyleStumbleApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ProductDetailActivity extends AppCompatActivity {
    public static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private ProductDetailFragment fragmenttest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String productId = getIntent().getStringExtra(PRODUCT_ID);
        initFragment(ProductDetailFragment.newInstance(productId));

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        // Send a hit to Analytics
        // Create a tracker
        Tracker tracker = ((StyleStumbleApplication) getApplication()).getTracker();
        tracker.setScreenName("Product Detail");
        // Send an event to Google Analytics
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Product Detail")
                .setAction("Show detail of product")
                .setLabel("Detail Label")
                .build());
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initFragment(Fragment detailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.productDetailFrame, detailFragment);
        transaction.commit();
    }

    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
