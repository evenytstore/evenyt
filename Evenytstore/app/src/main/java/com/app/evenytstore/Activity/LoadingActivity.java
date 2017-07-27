package com.app.evenytstore.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.app.evenytstore.Model.DatabaseAccess;
import com.app.evenytstore.Model.ServerSynchronizeTask;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;

import java.text.ParseException;

/**
 * Created by Enrique on 25/07/2017.
 */

public class LoadingActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        try {
            Shelf.ini(DatabaseAccess.getInstance(getApplicationContext()));
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
