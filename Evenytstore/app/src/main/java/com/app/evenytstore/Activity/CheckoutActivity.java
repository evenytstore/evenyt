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
        progress = (RoundCornerProgressBar) findViewById(R.id.progress);
        progressMessage = (TextView) findViewById(R.id.progressMessage);
        progress.setProgressColor(Color.parseColor("#5ae2e2"));
        progress.setProgressBackgroundColor(Color.parseColor("#808080"));
        progress.setMax(100);
        progress.setProgress(15);
    }


    public void updatePrice(){
        double price = CatalogActivity.cart.getTotal();
        double realPrice = CatalogActivity.cart.getTotalWithDiscount();
        if(price < 25)
            cost.setText("S/."+String.valueOf(DecimalHandler.round(price, 2) + 6));
        else
            cost.setText("S/."+String.valueOf(DecimalHandler.round(realPrice, 2)));
        if(price < 25){
            progress.setMax(25);
            progress.setProgress((float)price);
            progressMessage.setText("Por compras de S/.25 o más ahorre S/.6 de envió!");
        }else if(price < 100){
            progress.setMax(100);
            progress.setProgress((float)price);
            progressMessage.setText("Por compras de S/.100 o más obtenga un descuento del 3%!");
        }else if(price < 150){
            progress.setMax(150);
            progress.setProgress((float)price);
            progressMessage.setText("Por compras de S/.150 o más obtenga un descuento del 6%!");
        }else{
            progress.setMax(150);
            progress.setProgress(150);
            progressMessage.setText("Envío gratuito y descuento de 6% obtenidos!");
        }
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
