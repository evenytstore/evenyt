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
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import EvenytServer.model.Address;

/**
 * Created by Enrique on 08/10/2017.
 */

public class EditAddressActivity extends AppCompatActivity {

    private int FINISH = 1;
    String birthday;
    private static final int READ_LOCATION_REQUEST = 1;
    Spinner citySpinner;
    Spinner districtSpinner;
    String city = "";
    String district;
    int districtPos;
    ArrayAdapter<String> districtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_address);

        districtSpinner = (Spinner)findViewById(R.id.districtSpinner);

        citySpinner = (Spinner)findViewById(R.id.citySpinner);

        Set<String> citiesSet = Shelf.getHashCities().keySet();
        String[] cities = citiesSet.toArray(new String[citiesSet.size()]);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, cities){
            @Override
            public View getView(int pos, View convertView, ViewGroup parent){
                String newCity = this.getItem(pos);
                if(!newCity.equals(city)){
                    city = newCity;
                    final List<String> districts = Shelf.getHashCities().get(city);
                    district = districts.get(0);
                    districtAdapter = new ArrayAdapter<String>(EditAddressActivity.this,R.layout.support_simple_spinner_dropdown_item, districts.toArray(new String[districts.size()])){
                        @Override
                        public View getView(int pos, View convertView, ViewGroup parent){
                            district = this.getItem(pos);
                            districtPos = pos;
                            return super.getView(pos,convertView,parent);
                        }
                    };
                    districtSpinner.setAdapter(districtAdapter);
                }

                return super.getView(pos,convertView,parent);
            }
        };
        citySpinner.setAdapter(adapter2);
        int cityIndex = 0;
        int districtIndex = 0;

        for(String c : cities){
            if(c.equals(AppSettings.CURRENT_CUSTOMER.getAddress().getCity())) {
                city = c;
                break;
            }
            cityIndex += 1;
        }
        citySpinner.setSelection(cityIndex);
        List<String> districts = Shelf.getHashCities().get(city);
        for(String d : districts){
            if(d.equals(AppSettings.CURRENT_CUSTOMER.getAddress().getDistrict())) {
                district = d;
                districtAdapter = new ArrayAdapter<String>(EditAddressActivity.this,R.layout.support_simple_spinner_dropdown_item, districts.toArray(new String[districts.size()])){
                    @Override
                    public View getView(int pos, View convertView, ViewGroup parent){
                        district = this.getItem(pos);
                        districtPos = pos;
                        return super.getView(pos,convertView,parent);
                    }
                };
                districtSpinner.setAdapter(districtAdapter);
                break;
            }
            districtIndex += 1;
        }
        districtSpinner.setSelection(districtIndex);

        TextView textName = (TextView)findViewById(R.id.textName);
        TextView textLastName = (TextView)findViewById(R.id.textLastName);
        TextView textEmail = (TextView)findViewById(R.id.textEmail);
        TextView textAddress = (TextView)findViewById(R.id.textAddress);
        TextView textAddressNumber = (TextView)findViewById(R.id.textAddressNumber);

        textName.setText(AppSettings.CURRENT_CUSTOMER.getName());
        textLastName.setText(AppSettings.CURRENT_CUSTOMER.getLastName());
        textEmail.setText(AppSettings.CURRENT_CUSTOMER.getEmail());

        final TextView textBirthday = (TextView)findViewById(R.id.textBirthday);
        textBirthday.setText(AppSettings.CURRENT_CUSTOMER.getBirthday());
        birthday = AppSettings.CURRENT_CUSTOMER.getBirthday();

        textAddress.setText(AppSettings.CURRENT_CUSTOMER.getAddress().getAddressName());
        textAddressNumber.setText(AppSettings.CURRENT_CUSTOMER.getAddress().getAddressNumber());


        textBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditAddressActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                //birthDate.set(year,monthOfYear,dayOfMonth);
                                Calendar calBirthday = Calendar.getInstance();
                                calBirthday.set(year, monthOfYear, dayOfMonth);
                                birthday = DateHandler.toString(calBirthday);
                                textBirthday.setText(birthday);
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

        findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textAddress = (TextView)findViewById(R.id.textAddress);
                TextView textName = (TextView)findViewById(R.id.textName);
                TextView textLastName = (TextView)findViewById(R.id.textLastName);
                TextView textEmail = (TextView)findViewById(R.id.textEmail);

                String address = textAddress.getText().toString();
                String name = textName.getText().toString();
                String lastName = textLastName.getText().toString();
                String email = textEmail.getText().toString();

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
                if(email.equals("")){
                    textEmail.setError("Debe ingresar un email.");
                    return;
                }
                if (ContextCompat.checkSelfPermission(EditAddressActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(EditAddressActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Dialog dialog = new AlertDialog.Builder(EditAddressActivity.this)
                                .setTitle("Permisos")
                                .setMessage("Se utilizará el permiso de locación para validar la dirección ingresada.")
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_info).create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }

                    ActivityCompat.requestPermissions(EditAddressActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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
            Dialog dialog = new AlertDialog.Builder(EditAddressActivity.this)
                    .setTitle("Error")
                    .setMessage("La dirección ingresada no es válida.")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }else{
            TextView textName = (TextView)findViewById(R.id.textName);
            TextView textLastName = (TextView)findViewById(R.id.textLastName);
            TextView textEmail = (TextView)findViewById(R.id.textEmail);

            String name = textName.getText().toString();
            String lastName = textLastName.getText().toString();
            String email = textEmail.getText().toString();

            Intent intent = new Intent(EditAddressActivity.this, FinishEditActivity.class);
            intent.putExtra("city", city);
            intent.putExtra("district", district);
            intent.putExtra("address", address);
            intent.putExtra("addressNumber", addressNumber);
            intent.putExtra("latitude", latLng.latitude);
            intent.putExtra("longitude", latLng.longitude);
            intent.putExtra("name", name);
            intent.putExtra("lastName", lastName);
            intent.putExtra("email", email);
            intent.putExtra("birthday", birthday);
            startActivityForResult(intent, FINISH);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FINISH) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
}
