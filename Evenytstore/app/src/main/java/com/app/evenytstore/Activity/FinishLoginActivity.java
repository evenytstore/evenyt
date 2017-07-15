package com.app.evenytstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.evenytstore.Model.Customer;
import com.app.evenytstore.R;

/**
 * Created by Enrique on 06/07/2017.
 */

public class FinishLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_login);

        final Spinner internationalSpinner = (Spinner)findViewById(R.id.internationalSpinner);
        String[] arraySpinner = new String[]{"+51"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, arraySpinner);
        internationalSpinner.setAdapter(adapter);

        findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textPhone = (TextView)findViewById(R.id.textPhone);
                TextView textAddress = (TextView)findViewById(R.id.textAddress);
                String phone = textPhone.getText().toString();
                String address = textAddress.getText().toString();
                if(phone.equals("") || phone.length() != 9){
                    textPhone.setError("Debe ingresar un número de 9 dígitos.");
                    return;
                }
                if(address.equals("")){
                    textAddress.setError("Debe ingresar una dirección.");
                    return;
                }
                Customer.CURRENT_CUSTOMER.setPhone(internationalSpinner.getSelectedItem()+phone);
                Customer.CURRENT_CUSTOMER.setAddress(address);
                Intent intent = new Intent(FinishLoginActivity.this, InputSmsCodeActivity.class);
                startActivity(intent);
            }
        });
    }
}
