package com.example.carolinamarin.stylestumble.addProductWishList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.carolinamarin.stylestumble.Injection;
import com.example.carolinamarin.stylestumble.R;
import com.example.carolinamarin.stylestumble.data.Category;
import com.example.carolinamarin.stylestumble.data.provider.ProductColumns;
import com.example.carolinamarin.stylestumble.data.provider.ProductProvider;
import com.example.carolinamarin.stylestumble.productdetail.ProductDetailActivity;
import com.example.carolinamarin.stylestumble.util.CursorRecyclerViewAdapter;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class WishListFragment extends Fragment implements WishListContract.View, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;
    public static  WishListAdapter mListAdapter;
    private static WishListContract.UserActionsListener mActionsListener;


    public WishListFragment() {
    }

    public static WishListFragment newInstance() {
        WishListFragment fragment = new WishListFragment();
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mActionsListener = new WishListPresenter(Injection.provideProductsRepository(), this);
        Cursor c = getActivity().getContentResolver().query(ProductProvider.WishList.WISHLIST,
                null, null, null, null);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ProductProvider.WishList.WISHLIST,
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
    public void showDetailProduct(String productId) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.PRODUCT_ID, productId);
        startActivity(intent);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_wish_list, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.products_wish_list);


        mListAdapter = new WishListAdapter(getActivity(), null,mItemListener);
        recyclerView.setAdapter(mListAdapter);

        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);

        int numColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));
        return root;

    }

    ProductItemListener mItemListener = new ProductItemListener() {
        @Override
        public void onProductClick(String productId) {
            mActionsListener.openProductDetails(productId);
        }
    };

    private static class WishListAdapter extends CursorRecyclerViewAdapter<WishListAdapter.ViewHolder> {

        private Context mContext;
        ViewHolder mVh;
        private ProductItemListener mItemListener;
        private List<Category> mCategories;

        public WishListAdapter(Context context, Cursor cursor,ProductItemListener itemListener) {
            super(context, cursor);
            mContext = context;
            mItemListener = itemListener;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View wishListView = inflater.inflate(R.layout.item_wishlist, parent, false);

            ViewHolder vh = new ViewHolder(wishListView,mItemListener);
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


            if(!cursor.getString(
                    cursor.getColumnIndex(ProductColumns.SALEPRICE)).equals("0")){

                viewHolder.price.setText("Reg $"+cursor.getString(
                        cursor.getColumnIndex(ProductColumns.PRICE)));
                viewHolder.salePrice.setVisibility(View.VISIBLE);

                viewHolder.salePrice.setText("Sale $"+cursor.getString(
                        cursor.getColumnIndex(ProductColumns.SALEPRICE)));
            }

            Glide.with(viewHolder.itemView.getContext()).load(cursor.getString(
                    cursor.getColumnIndex(ProductColumns.URL)))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.image);
        }


        public void showProductDetails(int pos){

            getCursor().moveToPosition(pos);
            int currentPosition = getCursor().getPosition();
            Cursor c = getCursor();
            c.moveToPosition(currentPosition);

            String id = c.getString(c.getColumnIndex(ProductColumns._ID));

            mItemListener.onProductClick(id);
        }


        public class ViewHolder extends RecyclerView.ViewHolder  {

            public TextView name;

            public TextView description;
            public TextView price;

            private ProductItemListener mItemListener;
            private ImageView image;
            private Button button;
            private ImageButton buttonDelete;
            private TextView salePrice;

            public ViewHolder(View view, ProductItemListener listener) {
                super(view);
                mItemListener = listener;
                name = (TextView) view.findViewById(R.id.product_name);

                //      description = (TextView) itemView.findViewById(R.id.product_detail_description);

                image = (ImageView) view.findViewById(R.id.product_image);
                price=(TextView)view.findViewById(R.id.product_price);
                salePrice=(TextView)view.findViewById(R.id.product_salePrice);


                buttonDelete=(ImageButton)view.findViewById(R.id.delete_item_wishlist);

                image.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        int pos=getAdapterPosition();
                        showProductDetails(pos);

                    }
                });

                name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        int pos=getAdapterPosition();
                        showProductDetails(pos);

                    }
                });

                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        int pos=getAdapterPosition();
                        getCursor().moveToPosition(pos);
                        int currentPosition = getCursor().getPosition();
                        Cursor c = getCursor();
                        c.moveToPosition(currentPosition);
                        String id = c.getString(c.getColumnIndex(ProductColumns._ID));
                        long cursorId = mListAdapter.getItemId(pos);
                        mContext.getContentResolver().delete(ProductProvider.WishList.withId(cursorId),
                                null, null);
                        notifyItemRemoved(currentPosition);
                    }
                });
            }
        }
    }
    public interface ProductItemListener {
         void onProductClick(String clickedNote);
    }
}
