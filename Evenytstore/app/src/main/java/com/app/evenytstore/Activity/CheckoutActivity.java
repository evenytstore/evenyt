package com.app.evenytstore.Activity;

/**
 * Created by Enrique on 25/08/2017.
 */

import android.app.Dialog;
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
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.app.evenytstore.Adapter.CheckoutAdapter;
import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.Item;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.DecimalHandler;

import java.util.ArrayList;
import java.util.List;


public class CheckoutActivity extends AppCompatActivity {

    private List<Item> items;
    private Toolbar toolbar;
    private CheckoutAdapter adapter;
    private int SUMMARY = 1;
    private RoundCornerProgressBar progress;
    private TextView progressMessage;
    private TextView cost;
    private TextView discount;

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
            //Item i2 = new Item(i.getProductXSize());
            //i2.sum(i.getCount());
            items.add(i);
        }
        adapter.notifyDataSetChanged();

        //Button to order Items products

        Button finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CatalogActivity.cart.getHashProducts().size() == 0){
                    Dialog dialog = new android.app.AlertDialog.Builder(CheckoutActivity.this)
                            .setTitle("Error")
                            .setMessage("El documento no cuenta con productos agregados.")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    return;
                }
                Intent i = new Intent(CheckoutActivity.this, FinishOrderActivity.class);
                startActivityForResult(i, SUMMARY);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cost = (TextView) findViewById(R.id.cost);
        discount = (TextView) findViewById(R.id.discount);
        progress = (RoundCornerProgressBar) findViewById(R.id.progress);
        progressMessage = (TextView) findViewById(R.id.progressMessage);
        progress.setProgressColor(Color.parseColor("#5ae2e2"));
        progress.setProgressBackgroundColor(Color.parseColor("#808080"));
        progress.setMax(100);
        progress.setProgress(15);

        updatePrice();
    }


    public void updatePrice(){
        double price = CatalogActivity.cart.getTotal();
        double realPrice = CatalogActivity.cart.getTotalWithDiscount();
        double discountPrice = CatalogActivity.cart.getDiscount();
        if(price < AppSettings.FREE_DELIVERY_PRICE){
            cost.setText("S/."+String.valueOf(DecimalHandler.round(price, 2) + AppSettings.DELIVERY_COST));
            discount.setText("-0");
        }
        else{
            cost.setText("S/."+String.valueOf(DecimalHandler.round(realPrice, 2)));
            discount.setText("-"+String.valueOf(DecimalHandler.round(discountPrice + AppSettings.DELIVERY_COST, 2)));
        }
        if(price < AppSettings.FREE_DELIVERY_PRICE){
            progress.setMax(AppSettings.FREE_DELIVERY_PRICE);
            progress.setProgress((float)price);
            progressMessage.setText("Por compras de S/." + AppSettings.FREE_DELIVERY_PRICE + " o más ahorre S/." + AppSettings.DELIVERY_COST +" de envió!");
        }/*else if(price < AppSettings.MIN_FIRST_DISCOUNT){
            progress.setMax(AppSettings.MIN_FIRST_DISCOUNT);
            progress.setProgress((float)price);
            progressMessage.setText("Por compras de S/." + String.valueOf(AppSettings.MIN_FIRST_DISCOUNT) + " o más obtenga un descuento del 3%!");
        }else if(price < AppSettings.MIN_SECOND_DISCOUNT){
            progress.setMax(AppSettings.MIN_SECOND_DISCOUNT);
            progress.setProgress((float)price);
            progressMessage.setText("Por compras de S/." + String.valueOf(AppSettings.MIN_SECOND_DISCOUNT) + " o más obtenga un descuento del 6%!");
        }*/else{
            progress.setMax(AppSettings.MIN_SECOND_DISCOUNT);
            progress.setProgress(AppSettings.MIN_SECOND_DISCOUNT);
            progressMessage.setText("Envío gratuito obtenido!");
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SUMMARY) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
            updatePrice();
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
