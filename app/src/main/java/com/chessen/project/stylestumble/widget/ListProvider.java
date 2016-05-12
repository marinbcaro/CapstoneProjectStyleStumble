package com.chessen.project.stylestumble.widget;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.chessen.project.stylestumble.R;
import com.chessen.project.stylestumble.data.provider.ProductColumns;
import com.chessen.project.stylestumble.data.provider.ProductProvider;

import java.util.ArrayList;

/**
 * Created by carolinamarin on 5/9/16.
 */
public class ListProvider implements RemoteViewsService.RemoteViewsFactory {


    private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
    private Context context = null;
    private ContentResolver resolver = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent, ContentResolver resolver) {
        this.context = context;
        this.resolver = resolver;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {


        Cursor data = this.resolver.query(ProductProvider.WishList.PRODUCTSALE,
                null, null, null, null);

        if (data == null) {
            return;
        }

        if (data.moveToFirst()) {
            do {
                String name = data.getString(data.getColumnIndex(ProductColumns.NAME));
                String salePrice = data.getString(data.getColumnIndex(ProductColumns.SALEPRICE));

                ListItem listItem = new ListItem();
                listItem.name = name;
                listItem.price = salePrice;
                listItemList.add(listItem);
            } while (data.moveToNext());
        }
        data.close();
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.widget_list_row);
        ListItem listItem = listItemList.get(position);


        remoteView.setTextViewText(R.id.name, listItem.name);
        remoteView.setTextViewText(R.id.price, "$" + listItem.price);

        Intent fillInIntent = new Intent();
        remoteView.setOnClickFillInIntent(R.id.item_layout, fillInIntent);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
