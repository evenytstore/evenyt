package com.app.evenytstore.Activity;

/**
 * Created by Enrique on 25/08/2017.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.app.evenytstore.Adapter.CheckoutAdapter;
import com.app.evenytstore.Fragment.CatalogFragment;
import com.app.evenytstore.Model.Item;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.DimensionsHandler;
import com.app.evenytstore.Utility.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class CheckoutActivity extends AppCompatActivity {

    private List<Item> items;
    private Toolbar toolbar;
    private CheckoutAdapter adapter;
    private double deliveryCost = 0;
    private boolean hasDistrict = false;
    public static String CATEGORY = "cat";
    private int SUMMARY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        items = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        adapter = new CheckoutAdapter(CheckoutActivity.this, items);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        for(Object o : CatalogActivity.cart.getHashProducts().values()){
            Item i = (Item)o;
            Item i2 = new Item(i.getProductXSize());
            i2.sum(i.getCount());
            items.add(i2);
        }
        adapter.notifyDataSetChanged();

        //Button to order Items products

        Button finishButton = (Button) findViewById(R.id.finishButton);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RoundCornerProgressBar progress1 = (RoundCornerProgressBar) findViewById(R.id.progress);
        progress1.setProgressColor(Color.parseColor("#ed3b27"));
        progress1.setProgressBackgroundColor(Color.parseColor("#808080"));
        progress1.setMax(100);
        progress1.setProgress(15);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SUMMARY) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        }
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
