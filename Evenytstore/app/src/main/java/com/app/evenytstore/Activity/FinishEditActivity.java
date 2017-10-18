package com.app.evenytstore.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.DatabaseAccess;
import com.app.evenytstore.R;
import com.app.evenytstore.Server.ServerAccess;

import java.math.BigDecimal;

import EvenytServer.model.Address;
import EvenytServer.model.Sale;

/**
 * Created by Enrique on 08/10/2017.
 */

public class FinishEditActivity extends AppCompatActivity {

    Spinner internationalSpinner;
    public class ServerUpdateCustomerTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                ServerAccess.getClient().customersPatch(AppSettings.CURRENT_CUSTOMER);
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
            try {
                DatabaseAccess instance = DatabaseAccess.getInstance(FinishEditActivity.this);
                instance.open();
                instance.updateCustomer(AppSettings.CURRENT_CUSTOMER);
                instance.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            return true;
        }

        protected void onPostExecute(Boolean result){
            if(result){
                setResult(RESULT_OK, new Intent());
                finish();
            }else{
                Dialog dialog = new AlertDialog.Builder(FinishEditActivity.this)
                        .setTitle("Error")
                        .setMessage("No se pudo establecer conexión al servidor.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_login);

        internationalSpinner = (Spinner)findViewById(R.id.internationalSpinner);
        String[] arraySpinner = new String[]{"+51"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, arraySpinner);
        internationalSpinner.setAdapter(adapter);

        TextView textPhone = (TextView)findViewById(R.id.textPhone);
        TextView textDNI = (TextView)findViewById(R.id.textDNI);
        TextView textRUC = (TextView)findViewById(R.id.textRUC);

        textPhone.setText(AppSettings.CURRENT_CUSTOMER.getPhoneNumber().substring(3));
        textDNI.setText(AppSettings.CURRENT_CUSTOMER.getDNI());
        textRUC.setText(AppSettings.CURRENT_CUSTOMER.getRUC());

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

                Intent i = FinishEditActivity.this.getIntent();
                String city = i.getStringExtra("city");
                String district = i.getStringExtra("district");
                String address = i.getStringExtra("address");
                String addressNumber = i.getStringExtra("addressNumber");
                double latitude = i.getDoubleExtra("latitude", 0.0);
                double longitude = i.getDoubleExtra("longitude", 0.0);
                String name = i.getStringExtra("name");
                String lastName = i.getStringExtra("lastName");
                String email = i.getStringExtra("email");
                String birthday = i.getStringExtra("birthday");

                Address customerAddress = new Address();
                customerAddress.setCity(city);
                customerAddress.setDistrict(district);
                customerAddress.setAddressName(address);
                customerAddress.setAddressNumber(addressNumber);
                customerAddress.setLatitude(BigDecimal.valueOf(latitude));
                customerAddress.setLongitude(BigDecimal.valueOf(longitude));

                AppSettings.CURRENT_CUSTOMER.setAddress(customerAddress);
                AppSettings.CURRENT_CUSTOMER.setName(name);
                AppSettings.CURRENT_CUSTOMER.setLastName(lastName);
                AppSettings.CURRENT_CUSTOMER.setEmail(email);
                AppSettings.CURRENT_CUSTOMER.setBirthday(birthday);

                AppSettings.CURRENT_CUSTOMER.setPhoneNumber(internationalSpinner.getSelectedItem()+phone);
                AppSettings.CURRENT_CUSTOMER.setDNI(DNI);
                AppSettings.CURRENT_CUSTOMER.setRUC(RUC);

                ServerUpdateCustomerTask task = new ServerUpdateCustomerTask();
                task.execute();
            }
        });
    }
}
