package com.example.carolinamarin.stylestumble.addsaleProducts;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.TaskParams;

/**
 * Created by carolinamarin on 4/24/16.
 */
public class ProductSaleIntentService extends IntentService {

    private Context mContext;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ProductSaleIntentService(String name) {
        super(name);
    }

    public ProductSaleIntentService() {
        super(ProductSaleIntentService.class.getName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        ProductSaleTaskService stockTaskService = new ProductSaleTaskService(this);
        Bundle args = new Bundle();
            args.putString("product", "product");
        stockTaskService.onRunTask(new TaskParams("product", args));
    }

}
