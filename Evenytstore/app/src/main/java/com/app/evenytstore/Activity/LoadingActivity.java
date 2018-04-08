package com.app.evenytstore.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.DatabaseAccess;
import com.app.evenytstore.Model.ServerSynchronizeTask;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.ImageHandler;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
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


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_loading);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.logo_highres, options);

        /*ImageView mImageView = findViewById(R.id.imageView2);

        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        mImageView.setImageBitmap(
                decodeSampledBitmapFromResource(getResources(), R.drawable.logo_highres, imageWidth/32, imageHeight/32));*/



        AppSettings.DELIVERY_COST = getResources().getInteger(R.integer.deliveryCost);
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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
