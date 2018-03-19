package com.app.evenytstore.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.DatabaseAccess;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Server.ServerAccess;
import com.app.evenytstore.Utility.AddressHandler;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import EvenytServer.model.Address;
import EvenytServer.model.Customer;

/**
 * Created by DARK EVENYT on 18/03/2018.
 */

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_PHOTO_REQUEST_CODE = 1;
    private static final int READ_LOCATION_REQUEST = 2;
    ImageView mProfileImage;
    EditText mTextName;
    EditText mTextLastName;
    EditText mTextEmail;
    EditText mTextPhone;
    EditText mTextAddress;
    EditText mTextAddressNumber;
    EditText mTextDni;
    EditText mTextRuc;
    RelativeLayout mLoader;
    Spinner mCitySpinner;
    Spinner mDistrictSpinner;
    ArrayAdapter<String> mDistrictAdapter;

    String mDistrict;
    String mCity;
    Uri mSelectedImage;


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
                DatabaseAccess instance = DatabaseAccess.getInstance(ProfileActivity.this);
                instance.open();
                instance.updateCustomer(AppSettings.CURRENT_CUSTOMER);
                instance.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            if(mSelectedImage != null)
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(ProfileActivity.this.getContentResolver(), mSelectedImage);
                    AppSettings.IMAGE_HANDLER.setFileName("profileImage.png");
                    AppSettings.IMAGE_HANDLER.save(image);
                    image.recycle();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            return true;
        }

        protected void onPostExecute(Boolean result){
            if(result){
                mLoader.setVisibility(View.GONE);
                setResult(RESULT_OK, new Intent());
                finish();
            }else{
                Dialog dialog = new AlertDialog.Builder(ProfileActivity.this)
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
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case PICK_PHOTO_REQUEST_CODE:
                if(resultCode == RESULT_OK){
                    mSelectedImage = imageReturnedIntent.getData();
                    mProfileImage.setImageURI(mSelectedImage);
                }
                break;
        }
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


    private void setPencilClickListener(ToggleButton chkState, final EditText textView){
        chkState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()){
                    textView.setEnabled(true);
                    //textView.setSelectAllOnFocus(true);
                    textView.requestFocus();
                }
                else{
                    textView.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mProfileImage = findViewById(R.id.profileImage);
        mTextName = findViewById(R.id.txtName);
        mTextLastName = findViewById(R.id.txtLastName);
        mTextEmail = findViewById(R.id.txtEmail);
        mTextPhone = findViewById(R.id.txtPhone);
        mTextAddress = findViewById(R.id.txtAddress);
        mTextAddressNumber = findViewById(R.id.txtAddressNumber);
        mTextDni = findViewById(R.id.txtDni);
        mTextRuc = findViewById(R.id.txtRuc);
        mCitySpinner = findViewById(R.id.citySpinner);
        mDistrictSpinner = findViewById(R.id.districtSpinner);
        mLoader = findViewById(R.id.loadingPanel);

        mTextName.setEnabled(false);
        mTextLastName.setEnabled(false);
        mTextEmail.setEnabled(false);
        mTextPhone.setEnabled(false);
        mTextAddress.setEnabled(false);
        mTextAddressNumber.setEnabled(false);
        mTextDni.setEnabled(false);
        mTextRuc.setEnabled(false);

        AppSettings.IMAGE_HANDLER.setFileName("profileImage.png");
        File file = AppSettings.IMAGE_HANDLER.getFile();

        if(file.exists()){
            Bitmap bmImg = BitmapFactory.decodeFile(file.getAbsolutePath());
            mProfileImage.setImageBitmap(bmImg);

            mProfileImage.setBackgroundColor(0xffffffff);
        }

        ToggleButton chkState1 = findViewById(R.id.chkState1);
        ToggleButton chkState2 = findViewById(R.id.chkState2);
        ToggleButton chkState3 = findViewById(R.id.chkState3);
        ToggleButton chkState4 = findViewById(R.id.chkState4);
        ToggleButton chkState5 = findViewById(R.id.chkState5);
        ToggleButton chkState6 = findViewById(R.id.chkState6);
        ToggleButton chkState7 = findViewById(R.id.chkState7);
        ToggleButton chkState8 = findViewById(R.id.chkState8);
        ToggleButton chkState9 = findViewById(R.id.chkState9);
        ToggleButton chkState10 = findViewById(R.id.chkState10);

        setPencilClickListener(chkState1, mTextName);
        setPencilClickListener(chkState2, mTextLastName);
        setPencilClickListener(chkState3, mTextEmail);
        setPencilClickListener(chkState4, mTextPhone);
        setPencilClickListener(chkState5, mTextAddress);
        setPencilClickListener(chkState6, mTextAddressNumber);
        setPencilClickListener(chkState9, mTextDni);
        setPencilClickListener(chkState10, mTextRuc);




        //mTextName.setInputType(InputType.TYPE_);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , PICK_PHOTO_REQUEST_CODE);//one can be replaced with any action code
            }
        });

        Customer customer = AppSettings.CURRENT_CUSTOMER;
        Address address = customer.getAddress();

        mTextName.setText(customer.getName());
        mTextLastName.setText(customer.getLastName());
        mTextEmail.setText(customer.getEmail());
        mTextPhone.setText(customer.getPhoneNumber().substring(3));

        mTextAddress.setText(address.getAddressName());
        mTextAddressNumber.setText(address.getAddressNumber());

        Set<String> citiesSet = Shelf.getHashCities().keySet();
        String[] cities = citiesSet.toArray(new String[citiesSet.size()]);

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, cities){
        };
        mCitySpinner.setAdapter(adapter2);
        int cityIndex = 0;
        int districtIndex = 0;

        for(String c : cities){
            if(c.equals(address.getCity())) {
                mCity = c;
                break;
            }
            cityIndex += 1;
        }
        mCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newCity = adapter2.getItem(i);
                if(!newCity.equals(mCity)){
                    mCity = newCity;
                    final List<String> districts = Shelf.getHashCities().get(mCity);
                    mDistrict = districts.get(0);
                    mDistrictAdapter = new ArrayAdapter<String>(ProfileActivity.this,R.layout.support_simple_spinner_dropdown_item, districts.toArray(new String[districts.size()])){

                    };
                    mDistrictSpinner.setAdapter(mDistrictAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mCitySpinner.setSelection(cityIndex);
        List<String> districts = Shelf.getHashCities().get(mCity);
        for(String d : districts){
            if(d.equals(address.getDistrict())) {
                mDistrict = d;
                mDistrictAdapter = new ArrayAdapter<String>(ProfileActivity.this,R.layout.support_simple_spinner_dropdown_item, districts.toArray(new String[districts.size()])){

                };
                mDistrictSpinner.setAdapter(mDistrictAdapter);
                break;
            }
            districtIndex += 1;
        }
        mDistrictSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mDistrict = mDistrictAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mDistrictSpinner.setSelection(districtIndex);

        mTextDni.setText(customer.getDNI());
        mTextRuc.setText(customer.getRUC());


        mTextAddressNumber.setText(customer.getAddress().getAddressNumber());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button confirmButton = findViewById(R.id.finishButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mTextName.getText().toString();
                String lastName = mTextLastName.getText().toString();
                String email = mTextEmail.getText().toString();
                String phone = mTextPhone.getText().toString();
                String address = mTextAddress.getText().toString();
                String dni = mTextDni.getText().toString();
                String ruc = mTextRuc.getText().toString();

                if(address.equals("")){
                    mTextAddress.setError("Debe ingresar una dirección.");
                    return;
                }
                if(name.equals("")){
                    mTextName.setError("Debe ingresar un nombre.");
                    return;
                }
                if(lastName.equals("")){
                    mTextLastName.setError("Debe ingresar un apellido.");
                    return;
                }
                if(email.equals("")){
                    mTextEmail.setError("Debe ingresar un email.");
                    return;
                }
                if(phone.equals("") || phone.length() != 9){
                    mTextPhone.setError("Debe ingresar un número de 9 dígitos.");
                    return;
                }
                if(dni.length() != 8 && dni.length() != 0){
                    mTextDni.setError("El DNI debe ser de 8 dígitos.");
                    return;
                }
                if(ruc.length() != 11 && ruc.length() != 0){
                    mTextRuc.setError("El RUC debe ser de 11 dígitos.");
                    return;
                }
                if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Dialog dialog = new AlertDialog.Builder(ProfileActivity.this)
                                .setTitle("Permisos")
                                .setMessage("Se utilizará el permiso de locación para validar la dirección ingresada.")
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_info).create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }

                    ActivityCompat.requestPermissions(ProfileActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            READ_LOCATION_REQUEST);
                }else
                    verifyAddress();
            }
        });
    }


    private void verifyAddress(){
        String address = mTextAddress.getText().toString();
        String addressNumber = mTextAddressNumber.getText().toString();

        mLoader.setVisibility(View.VISIBLE);

        LatLng latLng = AddressHandler.getLocationFromAddress(getApplicationContext(), address);
        if(latLng == null){
            mLoader.setVisibility(View.GONE);
            Dialog dialog = new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Error")
                    .setMessage("La dirección ingresada no es válida.")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }else{

            String name = mTextName.getText().toString();
            String lastName = mTextLastName.getText().toString();
            String email = mTextEmail.getText().toString();
            String phone = mTextPhone.getText().toString();
            String dni = mTextDni.getText().toString();
            String ruc = mTextRuc.getText().toString();

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
            AppSettings.CURRENT_CUSTOMER.setPhoneNumber(AppSettings.CURRENT_CUSTOMER.getPhoneNumber().substring(0,3) + phone);
            AppSettings.CURRENT_CUSTOMER.setDNI(dni);
            AppSettings.CURRENT_CUSTOMER.setRUC(ruc);

            ServerUpdateCustomerTask task = new ServerUpdateCustomerTask();
            task.execute();
        }
    }
}
