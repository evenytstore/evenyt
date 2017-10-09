package com.app.evenytstore.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.R;

/**
 * Created by Enrique on 06/07/2017.
 */

public class FinishLoginActivity extends AppCompatActivity {

    Spinner internationalSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_login);

        internationalSpinner = (Spinner)findViewById(R.id.internationalSpinner);
        String[] arraySpinner = new String[]{"+51"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, arraySpinner);
        internationalSpinner.setAdapter(adapter);

        findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textPhone = (TextView)findViewById(R.id.textPhone);
                TextView textDNI = (TextView)findViewById(R.id.textDNI);
                TextView textRUC = (TextView)findViewById(R.id.textRUC);

                String phone = textPhone.getText().toString();
                String DNI = textDNI.getText().toString();
                String RUC = textRUC.getText().toString();

                if(phone.equals("") || phone.length() != 9){
                    textPhone.setError("Debe ingresar un número de 9 dígitos.");
                    return;
                }
                if(DNI.length() != 8 && DNI.length() != 0){
                    textDNI.setError("El DNI debe ser de 8 dígitos.");
                    return;
                }
                if(RUC.length() != 11 && RUC.length() != 0){
                    textRUC.setError("El RUC debe ser de 11 dígitos.");
                    return;
                }

                AppSettings.CURRENT_CUSTOMER.setPhoneNumber(internationalSpinner.getSelectedItem()+phone);
                AppSettings.CURRENT_CUSTOMER.setDNI(DNI);
                AppSettings.CURRENT_CUSTOMER.setRUC(RUC);

                Intent intent = new Intent(FinishLoginActivity.this, InputSmsCodeActivity.class);
                startActivity(intent);
            }
        });
    }
}
