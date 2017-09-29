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
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ServerSynchronizeTask task = new ServerSynchronizeTask();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getApplicationContext());
    }
}
