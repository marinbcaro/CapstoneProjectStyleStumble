package com.chessen.project.stylestumble.addsaleProducts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chessen.project.stylestumble.R;
import com.chessen.project.stylestumble.util.StyleStumbleApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class ProductSaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_sale);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (null == savedInstanceState) {
            initFragment(ProductSaleFragment.newInstance());
        }

        // Send a hit to Analytics
        // Create a tracker
        Tracker tracker = ((StyleStumbleApplication) getApplication()).getTracker();
        tracker.setScreenName("Sale");
        // Send an event to Google Analytics
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Sale")
                .setAction("Show list of sales")
                .setLabel("Sales Label")
                .build());
    }


    private void initFragment(Fragment detailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentProductSale, detailFragment);
        transaction.commit();
    }

}
