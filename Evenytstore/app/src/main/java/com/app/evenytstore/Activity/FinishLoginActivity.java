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

                ServerCustomerTask serverCustomerTask = new ServerCustomerTask();
                serverCustomerTask.execute();
            }
        });
    }


    public class ServerCustomerTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ServerAccess.getClient().customersPost(AppSettings.CURRENT_CUSTOMER);
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
            try {
                DatabaseAccess instance = DatabaseAccess.getInstance(FinishLoginActivity.this);
                instance.open();
                instance.insertCustomer(AppSettings.CURRENT_CUSTOMER);
                instance.close();
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }

            return true;
        }

        protected void onPostExecute(Boolean result){
            if(!result){
                Dialog dialog = new AlertDialog.Builder(FinishLoginActivity.this)
                        .setTitle("Error")
                        .setMessage("No se pudo establecer conexión al servidor.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                dialog.setCanceledOnTouchOutside(true);
                if(!FinishLoginActivity.this.isFinishing() && ! FinishLoginActivity.this.isDestroyed())
                    dialog.show();
            }else{
                Intent intent = new Intent(FinishLoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }
}
