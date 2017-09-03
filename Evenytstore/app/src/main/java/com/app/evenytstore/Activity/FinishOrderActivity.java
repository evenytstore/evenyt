package com.app.evenytstore.Activity;

/**
 * Created by Enrique on 25/08/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.R;

import org.w3c.dom.Text;


public class FinishOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button finishButton = (Button)findViewById(R.id.finishButton);
        TextView textAddress = (TextView)findViewById(R.id.textAddress);
        TextView textNumber = (TextView)findViewById(R.id.textNumber);
        TextView textPrice = (TextView)findViewById(R.id.price);

        double price = CatalogActivity.cart.getTotal();
        textPrice.setText(String.valueOf(price));
        textAddress.setText(AppSettings.CURRENT_CUSTOMER.getAddress().getAddressName());
        textNumber.setText(AppSettings.CURRENT_CUSTOMER.getAddress().getAddressNumber());
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do payment stuff
                setResult(RESULT_OK, new Intent());
                finish();
            }
        });
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
