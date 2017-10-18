package com.app.evenytstore.Activity;

/**
 * Created by Enrique on 25/08/2017.
 */

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.DatabaseAccess;
import com.app.evenytstore.Model.Item;
import com.app.evenytstore.R;
import com.app.evenytstore.Server.ServerAccess;
import com.app.evenytstore.Utility.AddressHandler;
import com.app.evenytstore.Utility.DateHandler;
import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import EvenytServer.model.Address;
import EvenytServer.model.AllProductsXBundles;
import EvenytServer.model.Bundle;
import EvenytServer.model.ProductXBundle;
import EvenytServer.model.ProductXSize;
import EvenytServer.model.Sale;


public class FinishOrderActivity extends AppCompatActivity {

    Spinner daySpinner;
    Spinner timeSpinner;
    TextView textAddress;
    TextView textNumber;


    public class ServerSaleTask extends AsyncTask<Sale, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Sale... params) {
            Sale sale = params[0];
            try {
                Sale s = ServerAccess.getClient().salesPost(sale);
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean result){
            if(result){
                setResult(RESULT_OK, new Intent());
                finish();
            }else{
                Dialog dialog = new AlertDialog.Builder(FinishOrderActivity.this)
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
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button finishButton = (Button)findViewById(R.id.finishButton);
        textAddress = (TextView)findViewById(R.id.textAddress);
        textNumber = (TextView)findViewById(R.id.textNumber);
        TextView textPrice = (TextView)findViewById(R.id.price);

        timeSpinner = (Spinner)findViewById(R.id.timeSpinner);
        List<String> keys = new ArrayList<>();
        for(int i = 7; i < 22; i++)
            if(i < 9)
                keys.add("0"+String.valueOf(i)+":00 - " + "0"+String.valueOf(i+1)+":00");
            else if(i == 9)
                keys.add("0"+String.valueOf(i)+":00 - " +String.valueOf(i+1)+":00");
            else
                keys.add(String.valueOf(i)+":00 - " +String.valueOf(i+1)+":00");

        String[] arraySpinner = keys.toArray(new String[keys.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, arraySpinner);
        timeSpinner.setAdapter(adapter);
        //Default selected time period
        timeSpinner.setSelection(13);

        daySpinner = (Spinner)findViewById(R.id.daySpinner);
        List<String> keys2 = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        for(int i = 1; i <= 7; i++){
            now.add(Calendar.DAY_OF_MONTH, 1);
            keys2.add(DateHandler.toString(now));
        }

        String[] arraySpinner2 = keys2.toArray(new String[keys2.size()]);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, arraySpinner2);
        daySpinner.setAdapter(adapter2);

        double price = CatalogActivity.cart.getTotalWithDiscount();
        if(CatalogActivity.cart.getTotal() < 25)
            price += AppSettings.DELIVERY_COST;
        textPrice.setText(String.valueOf(price));
        textAddress.setText(AppSettings.CURRENT_CUSTOMER.getAddress().getAddressName());
        textNumber.setText(AppSettings.CURRENT_CUSTOMER.getAddress().getAddressNumber());
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do payment stuff
                String address = textAddress.getText().toString();
                LatLng latLng = AddressHandler.getLocationFromAddress(getApplicationContext(), address);
                if(latLng == null) {
                    Dialog dialog = new AlertDialog.Builder(FinishOrderActivity.this)
                            .setTitle("Error")
                            .setMessage("La dirección ingresada no es válida.")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    return;
                }

                Sale sale = new Sale();
                Bundle bundle = new Bundle();
                bundle.setCustomerIdCustomer(AppSettings.CURRENT_CUSTOMER.getIdCustomer());
                bundle.setDescription("OTO");
                bundle.setName("One Time Only");
                bundle.setFrequencyDays(0);
                Calendar today = Calendar.getInstance();
                bundle.setLastOrdered(DateHandler.toString(today));
                String deliveryDay = (String)daySpinner.getSelectedItem();
                bundle.setNextDelivery(deliveryDay);
                String hour = (String)timeSpinner.getSelectedItem();
                hour = hour.substring(0, 2);
                bundle.setPreferredHour(deliveryDay+" "+hour);

                AllProductsXBundles productXBundles = new AllProductsXBundles();
                for(Object o : CatalogActivity.cart.getHashProducts().values()){
                    Item i = (Item)o;
                    ProductXBundle p = new ProductXBundle();
                    Address customerAddress = new Address();
                    customerAddress.setCity(AppSettings.CURRENT_CUSTOMER.getAddress().getCity());
                    customerAddress.setDistrict(AppSettings.CURRENT_CUSTOMER.getAddress().getDistrict());
                    customerAddress.setAddressName(address);
                    customerAddress.setAddressNumber(textNumber.getText().toString());
                    customerAddress.setLatitude(BigDecimal.valueOf(latLng.latitude));
                    customerAddress.setLongitude(BigDecimal.valueOf(latLng.longitude));
                    p.setAddress(customerAddress);
                    p.setDateDefault(deliveryDay);
                    p.setDateOrder(DateHandler.toString(today));
                    p.setProductIdProduct(i.getProductXSize().getProductCode());
                    p.setProductSize(i.getProductXSize().getSizeCode());
                    p.setQuantity(i.getCount());
                    p.setSubtotal(BigDecimal.valueOf(i.getSubtotal()));

                    productXBundles.add(p);
                }
                bundle.setProducts(productXBundles);

                sale.setBundleCustomerIdCustomer(AppSettings.CURRENT_CUSTOMER.getIdCustomer());
                sale.setEvenerIdEvener(1);
                sale.setRating(null);
                sale.setStatus(1);
                sale.setTypeSaleIdtypeSale(2);
                sale.setTotal(BigDecimal.valueOf(CatalogActivity.cart.getTotalWithDiscount()));
                sale.setBundle(bundle);

                ServerSaleTask task = new ServerSaleTask();
                task.execute(sale);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
