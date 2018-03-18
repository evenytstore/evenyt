package com.app.evenytstore.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

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
    ProgressBar pbarProgreso;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        AppSettings.DELIVERY_COST = getResources().getInteger(R.integer.deliveryCost);;
        AppSettings.FREE_DELIVERY_PRICE = getResources().getInteger(R.integer.freeDelivery);
        AppSettings.MIN_FIRST_DISCOUNT = getResources().getInteger(R.integer.minFirstDiscount);
        AppSettings.MIN_SECOND_DISCOUNT = getResources().getInteger(R.integer.minSecondDiscount);
        AppSettings.MIN_SALE_COST = getResources().getInteger(R.integer.minSaleCost);
        AppSettings.IMAGE_HANDLER = new ImageHandler(getApplicationContext());
        AppSettings.IMAGE_HANDLER.setExternal(false);

        try {
            String[] districts = getResources().getStringArray(R.array.districts_lima);
            Shelf.ini(DatabaseAccess.getInstance(getApplicationContext()), districts);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ServerSynchronizeTask task = new ServerSynchronizeTask();
        task.setProgressBar((ProgressBar) findViewById(R.id.barLoading));
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this);
    }
}
