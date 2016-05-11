package com.example.carolinamarin.stylestumble.products;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.carolinamarin.stylestumble.Injection;
import com.example.carolinamarin.stylestumble.R;
import com.example.carolinamarin.stylestumble.addsaleProducts.ProductSaleTaskService;
import com.example.carolinamarin.stylestumble.categories.CategoriesActivity;
import com.example.carolinamarin.stylestumble.data.Product;
import com.example.carolinamarin.stylestumble.data.provider.ProductColumns;
import com.example.carolinamarin.stylestumble.data.provider.ProductProvider;
import com.example.carolinamarin.stylestumble.data.provider.WishListColumns;
import com.example.carolinamarin.stylestumble.productdetail.ProductDetailActivity;
import com.example.carolinamarin.stylestumble.util.CursorRecyclerViewAdapter;
import com.example.carolinamarin.stylestumble.util.EndlessRecyclerViewScrollListener;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductsFragment extends Fragment implements ProductsContract.View, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARGUMENT_CAT_ID = "CATEGORY_ID";
    public static final String ARG_OBJECT = "object";
    private static final int CURSOR_LOADER_ID = 0;
    private static ProductsContract.UserActionsListener mActionsListener;
    private static String searchQuery;
    private static String cat_id;
    private static int offset = 0;
    public SwipeRefreshLayout swipeRefreshLayout;
    List<Product> mProducts;
    ProductItemListener mItemListener = new ProductItemListener() {
        @Override
        public void onProductClick(String productId) {
            mActionsListener.openProductDetails(productId);
        }
    };
    private BroadcastReceiver mReceiver;
    private ItemTouchHelper mItemTouchHelper;
    private ProductsAdapter mListAdapter;
    private String category;
    private Context mContext;
    private Context mainContext;

    public static ProductsFragment newInstance(String categoryId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_CAT_ID, categoryId);
        ProductsFragment fragment = new ProductsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        offset = 0;
        searchQuery = "";
        mainContext = getActivity();

        Intent intent = new Intent(getContext(), CategoriesActivity.class);
        intent.putExtra("POSITION_KEY", 1);
        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT;
        PendingIntent pIntent = PendingIntent.getActivity(getContext(), requestID, intent, flags);


        final Notification mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Sale Alert")
                        .setContentText("Some products in your wish list are on Sale! grab them before they are gone!")
                        .setContentIntent(pIntent)
                        .build();


        if (savedInstanceState == null) {

            setUpGcmTask(getActivity());
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(ProductSaleTaskService.ACTION_DONE)) {
                        String tag = intent.getStringExtra(ProductSaleTaskService.EXTRA_TAG);
                        String result = intent.getStringExtra(ProductSaleTaskService.EXTRA_RESULT);
                        if (result.equals("display")) {
                            if (getContext() != null) {
                                NotificationManager mNotificationManager =
                                        (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(1, mBuilder);
                            }
                        }
                    }
                }
            };
        }

    }

    @Override
    public void setProgressIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        mActionsListener = new ProductsPresenter(Injection.provideProductsRepository(), this);

        Cursor c = getActivity().getContentResolver().query(ProductProvider.Products.PRODUCTS,
                null, null, null, null);

        Cursor c2 = getActivity().getContentResolver().query(ProductProvider.UserPreferences.USERPREFERENCES,
                null, null, null, null);
        String catId = getArguments().getString(ARGUMENT_CAT_ID);

        if (checkConnection()) {
            if (c == null || c.getCount() == 0) {
                if (savedInstanceState == null) {
                    cat_id = catId;
                    mActionsListener.loadProducts(catId, "", 0, false);
                    getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
                }
            } else {
                if (savedInstanceState == null) {
                    getActivity().getContentResolver().delete(ProductProvider.Products.PRODUCTS,
                            null, null);
                    mActionsListener.loadProducts(catId, searchQuery, 0, true);
                    getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
                }
            }
        } else {
            showMessageNoConnection();
        }
        searchQuery = "";

        IntentFilter filter = new IntentFilter();
        filter.addAction(ProductSaleTaskService.ACTION_DONE);

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getActivity());
        manager.registerReceiver(mReceiver, filter);


        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

    }

    private void setUpGcmTask(Context mContext) {

        if (checkConnection()) {

            //ONLY FOR DEBUGGING UNCOMMENT TO TEST ONE OFF TASK THAT RUNS IMMEDIATELY INTENT SERVICE IS REQUIRED FOR THIS
            //        Intent mServiceIntent = new Intent(getContext(), ProductSaleIntentService.class);
//            getActivity().startService(mServiceIntent);
//
//            OneoffTask myTask = new OneoffTask.Builder()
//                    .setService(ProductSaleTaskService.class)
//                    .setExecutionWindow(0L, 60L)
//                    .setTag("product")
//                    .build();
//            GcmNetworkManager.getInstance(getContext()).schedule(myTask);


            //RUNS TASK EVERY DAY TO GET WHICH PRODUCTS WENT ON SALE
            long period = 86400L;
            String periodicTag = "periodic";
            PeriodicTask periodicTask = new PeriodicTask.Builder()
                    .setService(ProductSaleTaskService.class)
                    .setPeriod(period)
                    .setTag(periodicTag)
                    .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                    .setRequiresCharging(false)
                    .build();

            GcmNetworkManager.getInstance(getContext()).schedule(periodicTask);

            //WHEN PERIODIC TASK RUN JUST THE TASK SERVICE THE INTENT SERVICE IS NOT NEEDED
            ProductSaleTaskService productSaleTaskService = new ProductSaleTaskService(getActivity());
            Bundle args = new Bundle();
            args.putString("product", "product");
            productSaleTaskService.onRunTask(new TaskParams("product", args));

        } else {
            showMessageNoConnection();
        }
    }

    public boolean checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public void showMessageNoConnection() {
        Toast.makeText(getActivity(), "No Internet connection available", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ProductProvider.Products.PRODUCTS,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mListAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mListAdapter.swapCursor(null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_products, menu);

        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchQuery = query;
                String catId = getArguments().getString(ARGUMENT_CAT_ID);

                if (checkConnection()) {
                    getContext().getContentResolver().delete(ProductProvider.Products.PRODUCTS,
                            null, null);
                    offset = 0;
                    mActionsListener.loadProducts(catId, query, offset, true);
                } else {
                    showMessageNoConnection();
                }

                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });


    }

    @Override
    public void showDetailProduct(String productId) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.PRODUCT_ID, productId);
        startActivity(intent);
    }

    @Override
    public void showProducts(List<Product> products) {

        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(products.size());

        for (Product product : products) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                    ProductProvider.Products.PRODUCTS);
            builder.withValue(ProductColumns._ID, product.getId());
            builder.withValue(ProductColumns.NAME, product.getName());
            builder.withValue(ProductColumns.DESCRIPTION, product.getDescription());
            if (product.getBrand() != null) {
                builder.withValue(ProductColumns.BRAND, product.getBrand().name);
            }
            builder.withValue(ProductColumns.SALEPRICE, product.getSalePrice());
            builder.withValue(ProductColumns.PRICE, product.getPrice());
            builder.withValue(ProductColumns.URL, product.getImage().sizes.IPhoneSmall.url);
            batchOperations.add(builder.build());
        }

        try {
            mainContext.getContentResolver().applyBatch(ProductProvider.AUTHORITY, batchOperations);

        } catch (Exception e) {
            Log.e("ERROR Provider", "Error applying batch insert", e);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_products, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.products_list);

        mListAdapter = new ProductsAdapter(getActivity(), null, new ArrayList<Product>(0), mItemListener);
        recyclerView.setAdapter(mListAdapter);

        TextView emptyView = (TextView) root.findViewById(R.id.empty_view);

        swipeRefreshLayout =
                (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));

        swipeRefreshLayout.setEnabled(false);
        final GridLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 2);


        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                swipeRefreshLayout.setEnabled(false);
                String catId = getArguments().getString(ARGUMENT_CAT_ID);
                offset++;

                int totalPages = offset * 50;

                if (totalPages <= 500) {
                    mActionsListener.loadProducts(catId, searchQuery, totalPages, true);
                    int curSize = mListAdapter.getItemCount();
                    mListAdapter.notifyItemRangeInserted(curSize, totalPages - 1);
                }
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);

        return root;
    }

    public interface ProductItemListener {
        void onProductClick(String idProduct);
    }


    private static class ProductsAdapter extends CursorRecyclerViewAdapter<ProductsAdapter.ViewHolder> {
        ViewHolder mVh;
        private List<Product> mProducts;
        private ProductItemListener mItemListener;
        private Context mContext;

        public ProductsAdapter(Context context, Cursor cursor, List<Product> products, ProductItemListener itemListener) {
            super(context, cursor);
            setList(products);
            mContext = context;
            mItemListener = itemListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View noteView = inflater.inflate(R.layout.item_product, parent, false);


            ViewHolder vh = new ViewHolder(noteView, mItemListener);
            mVh = vh;
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {

            viewHolder.price.setText("");
            viewHolder.salePrice.setText("");

            viewHolder.name.setText(cursor.getString(
                    cursor.getColumnIndex(ProductColumns.NAME)));
            viewHolder.price.setText("$" + cursor.getString(
                    cursor.getColumnIndex(ProductColumns.PRICE)));
            if (!cursor.getString(
                    cursor.getColumnIndex(ProductColumns.SALEPRICE)).equals("0")) {

                viewHolder.price.setText("Reg $" + cursor.getString(
                        cursor.getColumnIndex(ProductColumns.PRICE)));
                viewHolder.salePrice.setVisibility(View.VISIBLE);

                viewHolder.salePrice.setText("Sale $" + cursor.getString(
                        cursor.getColumnIndex(ProductColumns.SALEPRICE)));
            }

            Glide.with(viewHolder.itemView.getContext()).load(cursor.getString(
                    cursor.getColumnIndex(ProductColumns.URL)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.image);

        }

        public void replaceData(List<Product> notes) {
            setList(notes);
            notifyDataSetChanged();
        }

        private void setList(List<Product> products) {
            mProducts = checkNotNull(products);
        }


        public Product getItem(int position) {
            return mProducts.get(position);
        }

        public void showProductDetails(int pos) {

            getCursor().moveToPosition(pos);
            int currentPosition = getCursor().getPosition();
            Cursor c = getCursor();
            c.moveToPosition(currentPosition);

            String id = c.getString(c.getColumnIndex(ProductColumns._ID));

            mItemListener.onProductClick(id);
        }

        public void deleteProduct(int position) {
            long cursorId = getItemId(position);
            Cursor c = getCursor();

            mContext.getContentResolver().delete(ProductProvider.Products.withId(cursorId),
                    null, null);

            if (c.getCount() == 1) {
                String catId = cat_id;
                offset++;
                int totalPages = offset * 50;
                mActionsListener.loadProducts(catId, searchQuery, totalPages, true);
            }
        }

        public void saveProductWishList(int position, View view) {

            long cursorId = getItemId(position);
            Cursor c = getCursor();
            ContentValues cv = new ContentValues();


            try {
                cv.put(WishListColumns._ID,
                        c.getString(c.getColumnIndex(ProductColumns._ID)));
                cv.put(WishListColumns.NAME,
                        c.getString(c.getColumnIndex(ProductColumns.NAME)));

                cv.put(WishListColumns.DESCRIPTION,
                        c.getString(c.getColumnIndex(ProductColumns.DESCRIPTION)));

                cv.put(WishListColumns.BRAND,
                        c.getString(c.getColumnIndex(ProductColumns.BRAND)));

                cv.put(WishListColumns.PRICE,
                        c.getString(c.getColumnIndex(ProductColumns.PRICE)));
                cv.put(WishListColumns.SALEPRICE,
                        c.getString(c.getColumnIndex(ProductColumns.SALEPRICE)));


                cv.put(WishListColumns.URL,
                        c.getString(c.getColumnIndex(ProductColumns.URL)));

//                mContext.getContentResolver().delete(ProductProvider.Products.withId(cursorId),
//                        null, null);
                mContext.getContentResolver().insert(ProductProvider.WishList.withId(cursorId),
                        cv);
                Snackbar.make(view, "Product added to the wish list", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } catch (Exception e) {
                if (e.getMessage().contains("UNIQUE constraint failed")) {
                    Snackbar.make(view, "This product is already on your wish list", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                Log.d("message", e.getMessage());
            }


        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView name;

            public TextView description;
            private ProductItemListener mItemListener;
            private ImageView image;
            private ImageView delete;
            private ImageView save;
            private TextView price;
            private TextView salePrice;


            public ViewHolder(View view, ProductItemListener listener) {
                super(view);
                final View viewitem=view;
                mItemListener = listener;
                name = (TextView) view.findViewById(R.id.product_name);
                image = (ImageView) view.findViewById(R.id.product_image);
                save = (ImageView) view.findViewById(R.id.save_to_wishlist);
                price = (TextView) view.findViewById(R.id.product_price);
                salePrice = (TextView) view.findViewById(R.id.product_salePrice1);

                image.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        int pos = getAdapterPosition();
                        showProductDetails(pos);

                    }
                });

                name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        int pos = getAdapterPosition();
                        showProductDetails(pos);

                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        int pos = getAdapterPosition();
                        saveProductWishList(pos,viewitem);

                    }
                });

            }

            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                getCursor().moveToPosition(pos);
                int currentPosition = getCursor().getPosition();
                Cursor c = getCursor();
                c.moveToPosition(currentPosition);
                String id = c.getString(c.getColumnIndex(ProductColumns._ID));
                mItemListener.onProductClick(id);

            }

        }
    }

}