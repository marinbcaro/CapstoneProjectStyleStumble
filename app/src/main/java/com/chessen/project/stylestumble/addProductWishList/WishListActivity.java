package com.chessen.project.stylestumble.addProductWishList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.chessen.project.stylestumble.R;
import com.chessen.project.stylestumble.util.StyleStumbleApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class WishListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (null == savedInstanceState) {
            initFragment(WishListFragment.newInstance());
        }

        // Send a hit to Analytics
        // Create a tracker
        Tracker tracker = ((StyleStumbleApplication) getApplication()).getTracker();
        tracker.setScreenName("WishList");
        // Send an event to Google Analytics
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("WishList")
                .setAction("Show wish list")
                .setLabel("WishList Label")
                .build());

    }

    private void initFragment(Fragment detailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentWishList, detailFragment);
        transaction.commit();
    }
}
