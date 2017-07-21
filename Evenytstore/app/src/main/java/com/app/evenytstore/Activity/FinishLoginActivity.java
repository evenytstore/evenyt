package com.app.evenytstore.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.evenytstore.Model.Customer;
import com.app.evenytstore.Model.DatabaseAccess;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.DateHandler;

import java.util.Calendar;

/**
 * Created by Enrique on 06/07/2017.
 */

public class FinishLoginActivity extends AppCompatActivity {

    Calendar birthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_login);

        final Spinner internationalSpinner = (Spinner)findViewById(R.id.internationalSpinner);
        String[] arraySpinner = new String[]{"+51"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, arraySpinner);
        internationalSpinner.setAdapter(adapter);

        TextView textName = (TextView)findViewById(R.id.textName);
        TextView textLastName = (TextView)findViewById(R.id.textName);
        TextView textEmail = (TextView)findViewById(R.id.textName);
        if(Customer.CURRENT_CUSTOMER.getName() != null)
            textName.setText(Customer.CURRENT_CUSTOMER.getName());
        if(Customer.CURRENT_CUSTOMER.getLastName() != null)
            textLastName.setText(Customer.CURRENT_CUSTOMER.getLastName());
        if(Customer.CURRENT_CUSTOMER.getEmail() != null)
            textEmail.setText(Customer.CURRENT_CUSTOMER.getEmail());

        final TextView textBirthday = (TextView)findViewById(R.id.textBirthday);
        if(Customer.CURRENT_CUSTOMER.getBirthday() != null)
            textBirthday.setText(DateHandler.toString(Customer.CURRENT_CUSTOMER.getBirthday()));
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
                                birthday = Calendar.getInstance();
                                birthday.set(year, monthOfYear, dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
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
                String lastName = textName.getText().toString();
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
                if(DNI.equals("") || DNI.length() != 8){
                    textDNI.setError("Debe ingresar su DNI de 8 dígitos.");
                    return;
                }
                if(RUC.equals("") || RUC.length() != 11){
                    textRUC.setError("Debe ingresar su RUC.");
                    return;
                }
                if(email.equals("")){
                    textEmail.setError("Debe ingresar un email.");
                    return;
                }
                Customer.CURRENT_CUSTOMER.setPhone(internationalSpinner.getSelectedItem()+phone);
                Customer.CURRENT_CUSTOMER.setAddress(address);
                Customer.CURRENT_CUSTOMER.setName(name);
                Customer.CURRENT_CUSTOMER.setLastName(lastName);
                Customer.CURRENT_CUSTOMER.setDni(DNI);
                Customer.CURRENT_CUSTOMER.setRuc(RUC);
                Customer.CURRENT_CUSTOMER.setEmail(email);
                Customer.CURRENT_CUSTOMER.setBirthday(birthday);

                DatabaseAccess.getInstance(getApplicationContext()).insertCustomer(Customer.CURRENT_CUSTOMER);
                Intent intent = new Intent(FinishLoginActivity.this, InputSmsCodeActivity.class);
                startActivity(intent);
            }
        });
    }
}
