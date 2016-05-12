package com.chessen.project.stylestumble.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import com.chessen.project.stylestumble.R;
import com.chessen.project.stylestumble.data.provider.ProductProvider;
import com.chessen.project.stylestumble.products.ProductsActivity;

/**
 * Created by carolinamarin on 5/9/16.
 */
public class WidgetIntentService extends IntentService {

    public WidgetIntentService() {
        super("WidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                WidgetProvider.class));


        Cursor data = getContentResolver().query(ProductProvider.WishList.PRODUCTSALE,
                null, null, null, null);

        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = new RemoteViews(getPackageName(),
                    R.layout.widget_layout);


            Intent svcIntent = new Intent(getApplicationContext(), WidgetService.class);

            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));

            views.setRemoteAdapter(R.id.listViewWidget, svcIntent);

            views.setEmptyView(R.id.listViewWidget, R.id.empty_view);

            Intent startActivityIntent = new Intent(getApplicationContext(), ProductsActivity.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.listViewWidget, startActivityPendingIntent);


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


        if (data == null) {
            return;
        }
        if (!data.moveToFirst()) {
            data.close();
            return;
        }
        data.close();

    }
}
