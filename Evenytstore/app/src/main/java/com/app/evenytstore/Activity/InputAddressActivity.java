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
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import EvenytServer.model.Address;

/**
 * Created by Enrique on 28/07/2017.
 */

public class InputAddressActivity extends AppCompatActivity {

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
        Set<String> keys = Shelf.getHashCities().keySet();
        String[] arraySpinner2 = keys.toArray(new String[keys.size()]);

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, arraySpinner2){

        };

        citySpinner.setAdapter(adapter2);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newCity = adapter2.getItem(i);
                if(!newCity.equals(city)){
                    city = newCity;
                    final List<String> districts = Shelf.getHashCities().get(city);
                    district = districts.get(0);
                    districtAdapter = new ArrayAdapter<String>(InputAddressActivity.this,R.layout.support_simple_spinner_dropdown_item, districts.toArray(new String[districts.size()])){
                        @Override
                        public View getView(int pos, View convertView, ViewGroup parent){
                            district = this.getItem(pos);
                            districtPos = pos;
                            return super.getView(pos,convertView,parent);
                        }
                    };
                    districtSpinner.setAdapter(districtAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        TextView textName = (TextView)findViewById(R.id.textName);
        TextView textLastName = (TextView)findViewById(R.id.textLastName);
        TextView textEmail = (TextView)findViewById(R.id.textEmail);
        if(AppSettings.CURRENT_CUSTOMER.getName() != null)
            textName.setText(AppSettings.CURRENT_CUSTOMER.getName());
        if(AppSettings.CURRENT_CUSTOMER.getLastName() != null)
            textLastName.setText(AppSettings.CURRENT_CUSTOMER.getLastName());
        if(AppSettings.CURRENT_CUSTOMER.getEmail() != null)
            textEmail.setText(AppSettings.CURRENT_CUSTOMER.getEmail());

        final DatePicker textBirthday = (DatePicker)findViewById(R.id.textBirthday);

        Calendar calBirthday = Calendar.getInstance();
        textBirthday.init(calBirthday.get(Calendar.YEAR), calBirthday.get(Calendar.MONTH), calBirthday.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) { Calendar calBirthday = Calendar.getInstance();
                        calBirthday.set(year, monthOfYear, dayOfMonth);
                        birthday = DateHandler.toString(calBirthday);
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
                if (ContextCompat.checkSelfPermission(InputAddressActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(InputAddressActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Dialog dialog = new AlertDialog.Builder(InputAddressActivity.this)
                                .setTitle("Permisos")
                                .setMessage("Se utilizará el permiso de locación para validar la dirección ingresada.")
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_info).create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }

                    ActivityCompat.requestPermissions(InputAddressActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            READ_LOCATION_REQUEST);
                }else
                    verifyAddress();
            }
        });
    }


    private void showDateDialog(final TextView textBirthday){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(InputAddressActivity.this,
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

        if(AppSettings.CURRENT_CUSTOMER.getBirthday() != null){
            Calendar calBirthday = DateHandler.toDate(AppSettings.CURRENT_CUSTOMER.getBirthday());
            datePickerDialog.updateDate(calBirthday.get(Calendar.YEAR),
                    calBirthday.get(Calendar.MONTH),
                    calBirthday.get(Calendar.DAY_OF_MONTH));
        }
        datePickerDialog.show();
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
            Dialog dialog = new AlertDialog.Builder(InputAddressActivity.this)
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

            Address customerAddress = new Address();
            customerAddress.setCity(city);
            customerAddress.setDistrict(district);
            customerAddress.setAddressName(address);
            customerAddress.setAddressNumber(addressNumber);
            customerAddress.setLatitude(BigDecimal.valueOf(latLng.latitude));
            customerAddress.setLongitude(BigDecimal.valueOf(latLng.longitude));
            AppSettings.CURRENT_CUSTOMER.setAddress(customerAddress);
            AppSettings.CURRENT_CUSTOMER.setName(name);
            AppSettings.CURRENT_CUSTOMER.setLastName(lastName);
            AppSettings.CURRENT_CUSTOMER.setEmail(email);
            AppSettings.CURRENT_CUSTOMER.setBirthday(birthday);

            Intent intent = new Intent(InputAddressActivity.this, FinishLoginActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
}
