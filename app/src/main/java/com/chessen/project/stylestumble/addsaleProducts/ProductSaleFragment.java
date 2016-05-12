package com.chessen.project.stylestumble.addsaleProducts;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chessen.project.stylestumble.Injection;
import com.chessen.project.stylestumble.R;
import com.chessen.project.stylestumble.data.Category;
import com.chessen.project.stylestumble.data.ProductDetail;
import com.chessen.project.stylestumble.data.provider.PreferenceColumns;
import com.chessen.project.stylestumble.data.provider.ProductColumns;
import com.chessen.project.stylestumble.data.provider.ProductProvider;
import com.chessen.project.stylestumble.productdetail.ProductDetailActivity;
import com.chessen.project.stylestumble.util.CursorRecyclerViewAdapter;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductSaleFragment extends Fragment implements ProductSaleContract.View, LoaderManager.LoaderCallbacks<Cursor> {


    private static final int CURSOR_LOADER_ID = 0;
    private static ProductSaleAdapter mListAdapter;
    private static ProductSaleContract.UserActionsListener mActionsListener;
    ProductItemListener mItemListener = new ProductItemListener() {
        @Override
        public void onProductClick(String productId) {
            mActionsListener.openProductDetails(productId);
        }
    };


    public ProductSaleFragment() {
    }

    public static ProductSaleFragment newInstance() {
        ProductSaleFragment fragment = new ProductSaleFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mActionsListener = new ProductSalePresenter(Injection.provideProductsRepository(), this);
        Cursor c = getActivity().getContentResolver().query(ProductProvider.WishList.PRODUCTSALE,
                null, null, null, null);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ProductProvider.WishList.PRODUCTSALE,
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showDetailProduct(String productId) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.PRODUCT_ID, productId);
        startActivity(intent);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mListAdapter.swapCursor(null);
    }

    @Override
    public void showNotification(ProductDetail p) {

    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_product_sale, container, false);


        final CheckBox checkboxvariable = (CheckBox) root.findViewById(R.id.notification_show);


        Button save = (Button) root.findViewById(R.id.save_notification);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.products_sale_list);
        mListAdapter = new ProductSaleAdapter(getActivity(), null, mItemListener);

        Cursor c = getContext().getContentResolver().query(ProductProvider.UserPreferences.USERPREFERENCES,
                null, null, null, null);
        if (c.getCount() == 0) {
            ContentValues cv = new ContentValues();
            cv.put(PreferenceColumns.SHOWNOTIFICATION, 0);
            getContext().getContentResolver().insert(ProductProvider.UserPreferences.USERPREFERENCES,
                    cv);
        }

        for (int i = 0; i < c.getCount(); i++) {
            c.moveToFirst();
            int status = c.getInt(c.getColumnIndex("showNotification"));
            if (status == 0) {
                checkboxvariable.setChecked(false);
            } else {
                checkboxvariable.setChecked(true);
            }
            c.moveToNext();
        }
    //FOR DEBUGGING
    //    DatabaseUtils.dumpCursor(c);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                int value = 0;
                if (checkboxvariable.isChecked()) {
                    value = 1;
                    cv.put(PreferenceColumns.SHOWNOTIFICATION, value);

                } else {
                    value = 0;
                    cv.put(PreferenceColumns.SHOWNOTIFICATION, value);

                }
                Snackbar.make(root, "Settings saved", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                try {
                    getContext().getContentResolver().update(ProductProvider.UserPreferences.USERPREFERENCES, cv, null, null);
                } catch (Exception e) {
                 //   Log.d("message", e.getMessage());
                }
            }
        });

        recyclerView.setAdapter(mListAdapter);
        int numColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
        return root;
    }


    public interface ProductItemListener {

        void onProductClick(String clickedNote);
    }

    private static class ProductSaleAdapter extends CursorRecyclerViewAdapter<ProductSaleAdapter.ViewHolder>  {

        Context mContext;
        ViewHolder mVh;
        private ProductItemListener mItemListener;
        private List<Category> mCategories;

        public ProductSaleAdapter(Context context, Cursor cursor, ProductItemListener itemListener) {
            super(context, cursor);
            mContext = context;
            mItemListener = itemListener;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View wishListView = inflater.inflate(R.layout.item_product_sale, parent, false);

            ViewHolder vh = new ViewHolder(wishListView, mItemListener);
            mVh = vh;
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
            //   DatabaseUtils.dumpCursor(cursor);

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

        public void showProductDetails(int pos) {

            getCursor().moveToPosition(pos);
            int currentPosition = getCursor().getPosition();
            Cursor c = getCursor();
            c.moveToPosition(currentPosition);

            String id = c.getString(c.getColumnIndex(ProductColumns._ID));

            mItemListener.onProductClick(id);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView title;
            public TextView description;
            private ImageView image;
            private TextView name;
            private TextView price;
            private TextView salePrice;


            public ViewHolder(View itemView, ProductItemListener listener) {
                super(itemView);

                mItemListener = listener;
                name = (TextView) itemView.findViewById(R.id.product_sale_name);
                image = (ImageView) itemView.findViewById(R.id.product_image);
                price = (TextView) itemView.findViewById(R.id.product_regular_price);
                salePrice = (TextView) itemView.findViewById(R.id.product_sale_price);
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
            }

        }
    }
}
