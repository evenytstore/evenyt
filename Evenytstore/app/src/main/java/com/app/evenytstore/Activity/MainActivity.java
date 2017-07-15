package com.app.evenytstore.Activity;

/**
 * Created by Enrique on 07/07/2017.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.TextView;

import com.app.evenytstore.Model.Customer;
import com.app.evenytstore.R;

/**
 * Created by Enrique on 06/07/2017.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView nameText = (TextView)findViewById(R.id.nameText);
        nameText.setText(Customer.CURRENT_CUSTOMER.getName());
    }
}