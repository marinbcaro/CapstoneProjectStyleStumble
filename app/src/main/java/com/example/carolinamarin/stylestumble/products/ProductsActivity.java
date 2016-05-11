package com.example.carolinamarin.stylestumble.products;

import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.TabLayout;
import android.support.test.espresso.IdlingResource;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toolbar;

import com.example.carolinamarin.stylestumble.R;
import com.example.carolinamarin.stylestumble.addProductWishList.WishListFragment;
import com.example.carolinamarin.stylestumble.addsaleProducts.ProductSaleFragment;
import com.example.carolinamarin.stylestumble.util.EspressoIdlingResource;
import com.example.carolinamarin.stylestumble.util.StyleStumbleApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

//import android.support.v7.widget.Toolbar;


public class ProductsActivity extends AppCompatActivity {
    public static final String CAT_ID = "CAT_ID";
    private static int setMenu = 1;
    private ProductsContract.UserActionsListener mActionsListener;
    private int number_products = 0;
    private int offset = 0;
    private String searchQuery;
    private Context mContext;
    private BroadcastReceiver mReceiver;
    private String categoryId;
    private Menu menu;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        mContext = this;
        categoryId = getIntent().getStringExtra(CAT_ID);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setTitle("Style Stumble");


        final CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.tabanim_viewpager);
        if (viewPager != null) {
            setUpViewPager(viewPager);
            viewPager.setPagingEnabled(false);
        }


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabanim_tabs);
        tabLayout.setupWithViewPager(viewPager);


        int position = getIntent().getIntExtra("POSITION_KEY", 0);
        if (position != 0) {
            viewPager.setCurrentItem(position);
        }


//
//        // Send data to Tag Manager
//        // Get the data layer
//        TagManager tagManager = ((StyleStumbleApplication) getApplication()).getTagManager();
//        DataLayer dl = tagManager.getDataLayer();
//        // Push an event into the data layer
//        // which will trigger sending a hit to Analytics
//        dl.pushEvent("loadProducts",
//                DataLayer.mapOf(
//                        "screen-name", "Products"));


        // Send a hit to Analytics
        // Create a tracker
        Tracker tracker = ((StyleStumbleApplication) getApplication()).getTracker();
        tracker.setScreenName("Products");
        // Send an event to Google Analytics
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Products")
                .setAction("Show list of products")
                .setLabel("Products Label")
                .build());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

        if (setMenu == 1) {
            getMenuInflater().inflate(R.menu.menu_products, menu);
            MenuItem item = menu.findItem(R.id.search);
            item.setVisible(true);
        }
        return true;
    }


    private void setUpViewPager(final CustomViewPager viewPager) {
        final TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ProductsFragment.newInstance(categoryId), "Popular");
        adapter.addFragment(WishListFragment.newInstance(), "Wish List");
        adapter.addFragment(ProductSaleFragment.newInstance(), "Sales");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                MenuItem item = menu.findItem(R.id.search);
                setMenu = 0;
                item.setVisible(false);
                if (position == 0) {
                    setMenu = 1;
                    item.setVisible(true);
                }
                if(position==2){
                    adapter.getItem(position).onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }


    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
