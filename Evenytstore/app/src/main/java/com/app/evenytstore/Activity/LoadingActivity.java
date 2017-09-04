package com.app.evenytstore.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.DatabaseAccess;
import com.app.evenytstore.Model.ServerSynchronizeTask;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.ImageHandler;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;
import EvenytServer.model.Size;

/**
 * Created by Enrique on 25/07/2017.
 */

public class LoadingActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        AppSettings.DELIVERY_COST = 6;
        AppSettings.IMAGE_HANDLER = new ImageHandler(getApplicationContext());
        AppSettings.IMAGE_HANDLER.setExternal(false);

        try {
            Shelf.ini(DatabaseAccess.getInstance(getApplicationContext()));

            //In order to test the catalog
            int count = 0;
            for(Product p : Shelf.getHashProducts().values()){
                for(Size s : Shelf.getHashSizes().values()){
                    ProductXSize p2 = new ProductXSize();
                    p2.setPrice(BigDecimal.valueOf(10));
                    p2.setProductCode(p.getCode());
                    p2.setSizeCode(s.getCode());
                    List<ProductXSize> newList;
                    if(Shelf.getHashProductsXSizes().containsKey(p.getCategoryCode()))
                        newList = Shelf.getHashProductsXSizes().get(p.getCategoryCode());
                    else
                        newList = new ArrayList<>();
                    newList.add(p2);
                    Shelf.getHashProductsXSizes().put(p.getCategoryCode(), newList);
                    List<ProductXSize> sizeList;
                    if(Shelf.getProductsToSizes().containsKey(p.getCode()))
                        sizeList = Shelf.getHashProductsXSizes().get(p.getCode());
                    else
                        sizeList = new ArrayList<>();
                    sizeList.add(p2);
                    Shelf.getProductsToSizes().put(p.getCode(), sizeList);
                    break;
                }
                count += 1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ServerSynchronizeTask task = new ServerSynchronizeTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, DatabaseAccess.getInstance(getApplicationContext()));

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                Intent intent    = new Intent(LoadingActivity.this, InitialActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 2000);
    }
}
