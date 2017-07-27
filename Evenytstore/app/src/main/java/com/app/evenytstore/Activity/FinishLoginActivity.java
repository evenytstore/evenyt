package com.app.evenytstore.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.AddressHandler;
import com.app.evenytstore.Utility.DateHandler;
import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import EvenytServer.model.Address;

/**
 * Created by Enrique on 06/07/2017.
 */

public class FinishLoginActivity extends AppCompatActivity {

    String birthday;
    private static final int READ_LOCATION_REQUEST = 1;
    Spinner internationalSpinner;
    Spinner citySpinner;
    Spinner districtSpinner;
    String city;
    String district;
    ArrayAdapter<String> districtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_login);

        internationalSpinner = (Spinner)findViewById(R.id.internationalSpinner);
        String[] arraySpinner = new String[]{"+51"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, arraySpinner);
        internationalSpinner.setAdapter(adapter);

        citySpinner = (Spinner)findViewById(R.id.citySpinner);
        Set<String> keys = Shelf.getHashCities().keySet();
        String[] arraySpinner2 = keys.toArray(new String[keys.size()]);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, arraySpinner2){
            @Override
            public View getView(int pos, View convertView, ViewGroup parent){
                city = this.getItem(pos);
                List<String> districts = Shelf.getHashCities().get(city);
                districtAdapter.clear();
                districtAdapter.addAll(districts);
                districtAdapter.notifyDataSetChanged();
                districtSpinner.setSelection(0);
                return super.getView(pos,convertView,parent);
            }
        };

        citySpinner.setAdapter(adapter2);

        districtSpinner = (Spinner)findViewById(R.id.districtSpinner);
        districtAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, new ArrayList<String>()){
            @Override
            public View getView(int pos, View convertView, ViewGroup parent){
                district = this.getItem(pos);
                return super.getView(pos,convertView,parent);
            }
        };
        districtSpinner.setAdapter(districtAdapter);

        citySpinner.setSelection(0);

        TextView textName = (TextView)findViewById(R.id.textName);
        TextView textLastName = (TextView)findViewById(R.id.textLastName);
        TextView textEmail = (TextView)findViewById(R.id.textEmail);
        if(AppSettings.CURRENT_CUSTOMER.getName() != null)
            textName.setText(AppSettings.CURRENT_CUSTOMER.getName());
        if(AppSettings.CURRENT_CUSTOMER.getLastName() != null)
            textLastName.setText(AppSettings.CURRENT_CUSTOMER.getLastName());
        if(AppSettings.CURRENT_CUSTOMER.getEmail() != null)
            textEmail.setText(AppSettings.CURRENT_CUSTOMER.getEmail());

        final TextView textBirthday = (TextView)findViewById(R.id.textBirthday);
        if(AppSettings.CURRENT_CUSTOMER.getBirthday() != null){
            textBirthday.setText(AppSettings.CURRENT_CUSTOMER.getBirthday());
            birthday = AppSettings.CURRENT_CUSTOMER.getBirthday();
        }

        TextView textAddress = (TextView)findViewById(R.id.textAddress);
        textAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        textBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(FinishLoginActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                textBirthday.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                                //birthDate.set(year,monthOfYear,dayOfMonth);
                                Calendar calBirthday = Calendar.getInstance();
                                calBirthday.set(year, monthOfYear, dayOfMonth);
                                birthday = DateHandler.toString(calBirthday);
                            }
                        }, mYear, mMonth, mDay);
                Calendar calBirthday = DateHandler.toDate(AppSettings.CURRENT_CUSTOMER.getBirthday());
                if(AppSettings.CURRENT_CUSTOMER.getBirthday() != null)
                    datePickerDialog.updateDate(calBirthday.get(Calendar.YEAR),
                            calBirthday.get(Calendar.MONTH),
                            calBirthday.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        findViewById(R.id.button_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textPhone = (TextView)findViewById(R.id.textPhone);
                TextView textAddress = (TextView)findViewById(R.id.textAddress);
                TextView textName = (TextView)findViewById(R.id.textName);
                TextView textLastName = (TextView)findViewById(R.id.textLastName);
                TextView textDNI = (TextView)findViewById(R.id.textDNI);
                TextView textRUC = (TextView)findViewById(R.id.textRUC);
                TextView textEmail = (TextView)findViewById(R.id.textEmail);

                String phone = textPhone.getText().toString();
                String address = textAddress.getText().toString();
                String name = textName.getText().toString();
                String lastName = textLastName.getText().toString();
                String DNI = textDNI.getText().toString();
                String RUC = textRUC.getText().toString();
                String email = textEmail.getText().toString();

                if(phone.equals("") || phone.length() != 9){
                    textPhone.setError("Debe ingresar un número de 9 dígitos.");
                    return;
                }
                if(address.equals("")){
                    textAddress.setError("Debe ingresar una dirección.");
                    return;
                }
                if(name.equals("")){
                    textName.setError("Debe ingresar un nombre.");
                    return;
                }
                if(lastName.equals("")){
                    textLastName.setError("Debe ingresar un apellido.");
                    return;
                }
                if(DNI.length() != 8 && DNI.length() != 0){
                    textDNI.setError("El DNI debe ser de 8 dígitos.");
                    return;
                }
                if(RUC.length() != 11 && DNI.length() != 0){
                    textRUC.setError("El RUC debe ser de 11 dígitos.");
                    return;
                }
                if(email.equals("")){
                    textEmail.setError("Debe ingresar un email.");
                    return;
                }
                if (ContextCompat.checkSelfPermission(FinishLoginActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(FinishLoginActivity.this,
                            Manifest.permission.READ_CONTACTS)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Dialog dialog = new AlertDialog.Builder(FinishLoginActivity.this)
                                .setTitle("Permisos")
                                .setMessage("Se utilizará el permiso de locación para validar la dirección ingresada.")
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_info).create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }

                    ActivityCompat.requestPermissions(FinishLoginActivity.this,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            READ_LOCATION_REQUEST);
                }else
                    verifyAddress();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_LOCATION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    verifyAddress();

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    finish();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void verifyAddress(){
        TextView textAddress = (TextView)findViewById(R.id.textAddress);
        TextView textAddressNumber = (TextView)findViewById(R.id.textAddressNumber);
        String address = textAddress.getText().toString();
        String addressNumber = textAddressNumber.getText().toString();

        LatLng latLng = AddressHandler.getLocationFromAddress(getApplicationContext(), address);
        if(latLng == null){
            Dialog dialog = new AlertDialog.Builder(FinishLoginActivity.this)
                    .setTitle("Error")
                    .setMessage("La dirección ingresada no es válida.")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }else{
            TextView textPhone = (TextView)findViewById(R.id.textPhone);
            TextView textName = (TextView)findViewById(R.id.textName);
            TextView textLastName = (TextView)findViewById(R.id.textLastName);
            TextView textDNI = (TextView)findViewById(R.id.textDNI);
            TextView textRUC = (TextView)findViewById(R.id.textRUC);
            TextView textEmail = (TextView)findViewById(R.id.textEmail);

            String phone = textPhone.getText().toString();
            String name = textName.getText().toString();
            String lastName = textLastName.getText().toString();
            String DNI = textDNI.getText().toString();
            String RUC = textRUC.getText().toString();
            String email = textEmail.getText().toString();

            AppSettings.CURRENT_CUSTOMER.setPhoneNumber(internationalSpinner.getSelectedItem()+phone);
            Address customerAddress = new Address();
            customerAddress.setCity(city);
            customerAddress.setDistrict(district);
            customerAddress.setAddressName(address);
            customerAddress.setAddressNumber(addressNumber);
            customerAddress.setLatitude(BigDecimal.valueOf(latLng.latitude));
            customerAddress.setLongitude(BigDecimal.valueOf(latLng.longitude));
            AppSettings.CURRENT_CUSTOMER.setAddress(customerAddress);
            AppSettings.CURRENT_CUSTOMER.setAddress(customerAddress);
            AppSettings.CURRENT_CUSTOMER.setName(name);
            AppSettings.CURRENT_CUSTOMER.setLastName(lastName);
            AppSettings.CURRENT_CUSTOMER.setDNI(DNI);
            AppSettings.CURRENT_CUSTOMER.setRUC(RUC);
            AppSettings.CURRENT_CUSTOMER.setEmail(email);
            AppSettings.CURRENT_CUSTOMER.setBirthday(birthday);

            Intent intent = new Intent(FinishLoginActivity.this, InputSmsCodeActivity.class);
            startActivity(intent);
        }
    }
}
