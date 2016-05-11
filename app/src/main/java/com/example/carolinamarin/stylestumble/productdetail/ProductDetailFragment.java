package com.example.carolinamarin.stylestumble.productdetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.carolinamarin.stylestumble.Injection;
import com.example.carolinamarin.stylestumble.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProductDetailFragment extends Fragment implements ProductDetailContract.View {


    public static final String PRODUCT_ID = "PRODUCT_ID";
    private ProductDetailContract.UserActionsListener mActionsListener;
    private TextView mDetailTitle;
    private TextView mDetailDescription;
    private TextView mDetailPrice;
    private TextView mDetailSalePrice;
    private TextView mDetailBrand;
    private TextView mDetailRetailer;
    private ImageView mDetailImage;


    public static ProductDetailFragment newInstance(String productId) {
        Bundle arguments = new Bundle();
        arguments.putString(PRODUCT_ID, productId);
        ProductDetailFragment fragment = new ProductDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        String noteId = getArguments().getString(PRODUCT_ID);
        mActionsListener.openProduct(noteId);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active) {
            // mDetailTitle.setText("");
            // mDetailDescription.setText(getString(R.string.loading));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionsListener = new ProductDetailPresenter(Injection.provideProductsRepository(),
                this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_detail, container, false);

        mDetailTitle = (TextView) root.findViewById(R.id.product_detail_title);
        mDetailPrice = (TextView) root.findViewById(R.id.product_detail_price);
        mDetailSalePrice = (TextView) root.findViewById(R.id.product_detail_sale_price);
        mDetailDescription = (TextView) root.findViewById(R.id.note_detail_description);
        mDetailRetailer = (TextView) root.findViewById(R.id.product_detail_retailer);
        mDetailImage = (ImageView) getActivity().findViewById(R.id.backdrop);

        return root;
    }


    @Override
    public void showTitle(String title, String image) {

        Glide.with(getContext()).load(image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mDetailImage);

        mDetailTitle.setText(title);
    }

    @Override
    public void showDescription(String desc) {
        //Remove html tags from description
        String finalDesc = desc.replaceAll("<[^>]*>", "");
        mDetailDescription.setText(finalDesc);
    }

    @Override
    public void showPrice(Double price, String salePrice) {
        if (!salePrice.equals("0")) {
            mDetailSalePrice.setText("Sale $" + salePrice);
        }
        mDetailPrice.setText("$" + price.toString());
    }


    @Override
    public void showRetailer(String retailer) {
        mDetailRetailer.setText(retailer);
    }

    @Override
    public void showShop(String urlProduct) {

        final String url = urlProduct;

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setContentDescription(new String("Buy Proguct"));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}
