package com.example.carolinamarin.stylestumble.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by carolinamarin on 5/9/16.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new ListProvider(this.getApplicationContext(), intent, getContentResolver()));
    }
}
