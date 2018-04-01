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
import android.support.v7.widget.Toolbar;
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

import static java.util.Calendar.DAY_OF_YEAR;

/**
 * Created by Enrique on 28/07/2017.
 */

public class InputAddressActivity extends AppCompatActivity {

    Calendar calBirthday;
    String birthday;
    private static final int READ_LOCATION_REQUEST = 1;
    private int INITIAL_YEAR = 1900;
    Spinner mCitySpinner;
    Spinner mDistrictSpinner;
    Spinner mInternationalSpinner;
    Spinner mDaySpinner;
    Spinner mMonthSpinner;
    Spinner mYearSpinner;
    String mCity = "";
    String mDistrict;
    int districtPos;
    ArrayAdapter<String> mDistrictAdapter;


    private int getDaysInMonth(int i){
        if(i == 0 || i == 2 || i == 4 || i == 6 || i == 7 || i == 9 || i == 11){
            return 31;
        }else if(i == 1){
            if(calBirthday.getActualMaximum(DAY_OF_YEAR) > 365){
                return 29;
            }else{
                return 28;
            }
        }else{
            return 30;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mInternationalSpinner = (Spinner)findViewById(R.id.internationalSpinner);
        mDistrictSpinner = findViewById(R.id.districtSpinner);
        mDaySpinner = findViewById(R.id.daySpinner);
        mMonthSpinner = findViewById(R.id.monthSpinner);
        mYearSpinner = findViewById(R.id.yearSpinner);
        mCitySpinner = findViewById(R.id.citySpinner);
        String[] arraySpinner = new String[]{"+51"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, arraySpinner);
        mInternationalSpinner.setAdapter(adapter);
        mInternationalSpinner.setSelection(0);

        if(AppSettings.CURRENT_CUSTOMER.getBirthday() != null)
            calBirthday = DateHandler.toDate(AppSettings.CURRENT_CUSTOMER.getBirthday());
        else calBirthday = Calendar.getInstance();

        arraySpinner = new String[]{"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Deciembre"};
        adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, arraySpinner);
        mMonthSpinner.setAdapter(adapter);

        mDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                calBirthday.set(Calendar.DAY_OF_MONTH, i + 1);
                birthday = DateHandler.toString(calBirthday);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                List<String> keys = new ArrayList<>();

                int days = getDaysInMonth(i);

                int currentSelection = calBirthday.get(Calendar.DAY_OF_MONTH) - 1;
                calBirthday.set(Calendar.MONTH, i);

                for(int j=0;j<days;j++)
                    keys.add(String.valueOf(j + 1));
                if(currentSelection >= days){
                    currentSelection = days - 1;
                    calBirthday.set(Calendar.DAY_OF_MONTH, currentSelection + 1);
                }

                String [] arraySpinner = keys.toArray(new String[keys.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(InputAddressActivity.this,R.layout.support_simple_spinner_dropdown_item, arraySpinner);
                mDaySpinner.setAdapter(adapter);
                mDaySpinner.setSelection(currentSelection);
                birthday = DateHandler.toString(calBirthday);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> keys = new ArrayList<>();
        for(int i = INITIAL_YEAR; i <= Calendar.getInstance().get(Calendar.YEAR); i++){
            keys.add(String.valueOf(i));
        }
        arraySpinner = keys.toArray(new String[keys.size()]);
        adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, arraySpinner);
        mYearSpinner.setAdapter(adapter);
        mYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int month = calBirthday.get(Calendar.MONTH);
                int year = i + INITIAL_YEAR;
                int currentDay = calBirthday.get(Calendar.DAY_OF_MONTH) - 1;
                calBirthday.set(Calendar.YEAR, year);

                if(month == 1){
                    int days = getDaysInMonth(1);
                    List<String> keys = new ArrayList<>();
                    for(int j = 0;j<days;j++){
                        keys.add(String.valueOf(j + 1));
                    }
                    String [] arraySpinner = keys.toArray(new String[keys.size()]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(InputAddressActivity.this,R.layout.support_simple_spinner_dropdown_item, arraySpinner);
                    mDaySpinner.setAdapter(adapter);

                    if(currentDay >= days){
                        currentDay = days - 1;
                        calBirthday.set(Calendar.DAY_OF_MONTH, currentDay + 1);
                    }

                    mDaySpinner.setSelection(currentDay);
                }
                birthday = DateHandler.toString(calBirthday);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        keys = new ArrayList<>();
        for(int i = 0 ; i < calBirthday.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
            keys.add(String.valueOf(i + 1));
        }
        adapter = new ArrayAdapter<>(InputAddressActivity.this,R.layout.support_simple_spinner_dropdown_item, arraySpinner);
        mDaySpinner.setAdapter(adapter);
        mYearSpinner.setSelection(calBirthday.get(Calendar.YEAR) - INITIAL_YEAR);
        mMonthSpinner.setSelection(calBirthday.get(Calendar.MONTH));
        mDaySpinner.setSelection(calBirthday.get(Calendar.DAY_OF_MONTH) - 1);

        /*List<String> keys = new ArrayList<>();
        for(int i=0;i<;i++)
        arraySpinner = keys.toArray(new String[keys.size()]);*/

        Set<String> citiesSet = Shelf.getHashCities().keySet();
        String[] cities = citiesSet.toArray(new String[citiesSet.size()]);

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, cities){
        };
        mCitySpinner.setAdapter(adapter2);


        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newCity = adapter2.getItem(i);
                if(!newCity.equals(mCity)){
                    mCity = newCity;
                    final List<String> districts = Shelf.getHashCities().get(mCity);
                    mDistrict = districts.get(0);
                    mDistrictAdapter = new ArrayAdapter<String>(InputAddressActivity.this,R.layout.support_simple_spinner_dropdown_item, districts.toArray(new String[districts.size()])){

                    };
                    mDistrictSpinner.setAdapter(mDistrictAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mCitySpinner.setSelection(0);

        mDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mDistrict = mDistrictAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mDistrictSpinner.setSelection(0);

        TextView textName = (TextView)findViewById(R.id.textName);
        TextView textLastName = (TextView)findViewById(R.id.textLastName);
        TextView textEmail = (TextView)findViewById(R.id.textEmail);
        if(AppSettings.CURRENT_CUSTOMER.getName() != null)
            textName.setText(AppSettings.CURRENT_CUSTOMER.getName());
        if(AppSettings.CURRENT_CUSTOMER.getLastName() != null)
            textLastName.setText(AppSettings.CURRENT_CUSTOMER.getLastName());
        if(AppSettings.CURRENT_CUSTOMER.getEmail() != null)
            textEmail.setText(AppSettings.CURRENT_CUSTOMER.getEmail());


        /*final DatePicker textBirthday = (DatePicker)findViewById(R.id.textBirthday);

        Calendar calBirthday = Calendar.getInstance();
        birthday = DateHandler.toString(calBirthday);
        textBirthday.init(calBirthday.get(Calendar.YEAR), calBirthday.get(Calendar.MONTH), calBirthday.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) { Calendar calBirthday = Calendar.getInstance();
                        calBirthday.set(year, monthOfYear, dayOfMonth);
                        birthday = DateHandler.toString(calBirthday);
                    }
                });*/

        findViewById(R.id.button_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textAddress = (TextView)findViewById(R.id.textAddress);
                TextView textName = (TextView)findViewById(R.id.textName);
                TextView textLastName = (TextView)findViewById(R.id.textLastName);
                TextView textEmail = (TextView)findViewById(R.id.textEmail);
                TextView textPhone = (TextView)findViewById(R.id.textPhone);
                TextView textDNI = (TextView)findViewById(R.id.textDNI);
                TextView textRUC = (TextView)findViewById(R.id.textRUC);

                String address = textAddress.getText().toString();
                String name = textName.getText().toString();
                String lastName = textLastName.getText().toString();
                String email = textEmail.getText().toString();
                String phone = textPhone.getText().toString();
                String DNI = textDNI.getText().toString();
                String RUC = textRUC.getText().toString();


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
            TextView textPhone = (TextView)findViewById(R.id.textPhone);
            TextView textDNI = (TextView)findViewById(R.id.textDNI);
            TextView textRUC = (TextView)findViewById(R.id.textRUC);

            String name = textName.getText().toString();
            String lastName = textLastName.getText().toString();
            String email = textEmail.getText().toString();
            String phone = textPhone.getText().toString();
            String DNI = textDNI.getText().toString();
            String RUC = textRUC.getText().toString();

            Address customerAddress = new Address();
            customerAddress.setCity(mCity);
            customerAddress.setDistrict(mDistrict);
            customerAddress.setAddressName(address);
            customerAddress.setAddressNumber(addressNumber);
            customerAddress.setLatitude(BigDecimal.valueOf(latLng.latitude));
            customerAddress.setLongitude(BigDecimal.valueOf(latLng.longitude));
            AppSettings.CURRENT_CUSTOMER.setAddress(customerAddress);
            AppSettings.CURRENT_CUSTOMER.setName(name);
            AppSettings.CURRENT_CUSTOMER.setLastName(lastName);
            AppSettings.CURRENT_CUSTOMER.setEmail(email);
            AppSettings.CURRENT_CUSTOMER.setBirthday(birthday);
            AppSettings.CURRENT_CUSTOMER.setPhoneNumber(mInternationalSpinner.getSelectedItem()+phone);
            AppSettings.CURRENT_CUSTOMER.setDNI(DNI);
            AppSettings.CURRENT_CUSTOMER.setRUC(RUC);

            Intent intent = new Intent(InputAddressActivity.this, InputSmsCodeActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
}
