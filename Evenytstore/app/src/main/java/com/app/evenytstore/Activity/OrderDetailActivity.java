package com.app.evenytstore.Activity;

/**
 * Created by Enrique on 29/09/2017.
 */

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.evenytstore.Adapter.OrderDetailAdapter;
import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import EvenytServer.model.ProductXBundle;
import EvenytServer.model.Sale;

public class OrderDetailActivity extends AppCompatActivity {
    private List<ProductXBundle> products = new ArrayList<>();
    public OrderDetailAdapter adapter;
    public int docId;
    private int IMAGE_VIEW = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Sale sale = AppSettings.SELECTED_SALE;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        docId = sale.getIdSale();
        products = sale.getBundle().getProducts();

        TextView idTV = (TextView)findViewById(R.id.docId);
        idTV.setText(""+docId);

        ListView orderDetailListView = (ListView)findViewById(R.id.listOrderDetail);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.order_detail_item, orderDetailListView, false);
        orderDetailListView.addHeaderView(header);
        adapter = new OrderDetailAdapter(OrderDetailActivity.this, R.layout.order_detail_item, new ArrayList<ProductXBundle>());
        orderDetailListView.setAdapter(adapter);
        //orderDetailListView.setAdapter(adapter);

        for (ProductXBundle p : products){
            adapter.add(p);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
