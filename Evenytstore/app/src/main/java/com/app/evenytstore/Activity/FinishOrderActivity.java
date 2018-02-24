package com.app.evenytstore.Activity;

/**
 * Created by Enrique on 25/08/2017.
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.apigateway.ApiClientException;
import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.Item;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Server.ServerAccess;
import com.app.evenytstore.Utility.AddressHandler;
import com.app.evenytstore.Utility.DateHandler;
import com.app.evenytstore.Utility.DecimalHandler;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import EvenytServer.model.Address;
import EvenytServer.model.AllProductsXBundles;
import EvenytServer.model.Bundle;
import EvenytServer.model.ProductXBundle;
import EvenytServer.model.Sale;


public class FinishOrderActivity extends AppCompatActivity {

    Spinner daySpinner;
    Spinner timeSpinner;
    Spinner paymentSpinner;
    TextView textAddress;
    TextView textNumber;
    EditText textCash;
    private int PROMOTION = 1;


    public class ServerSaleTask extends AsyncTask<Sale, Void, String> {
        @Override
        protected String doInBackground(Sale... params) {
            Sale sale = params[0];
            try {
                AppSettings.SELECTED_SALE = sale;
                Sale s = ServerAccess.getClient().salesPost(sale);
            }catch(ApiClientException e){
                if(e.getStatusCode() == 400)
                    return e.getErrorMessage();
                else
                    return "Failed";
            }catch(Exception e){
                e.printStackTrace();
                return "Failed";
            }
            return "Correct";
        }

        protected void onPostExecute(String result){
            if(result.equals("Correct")){
                AppSettings.CURRENT_PROMOTION = null;
                setResult(RESULT_OK, new Intent());
                finish();
            }else if (result.equals("Failed")){
                Dialog dialog = new AlertDialog.Builder(FinishOrderActivity.this)
                        .setTitle("Error")
                        .setMessage("No se pudo establecer conexión al servidor.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }else{ //Not enough stock
                String productCode = result.substring(result.lastIndexOf(" ") + 1, result.length() - 2);
                Dialog dialog = new AlertDialog.Builder(FinishOrderActivity.this)
                        .setTitle("Error")
                        .setMessage("No hay suficiente stock para el producto " + Shelf.getProductByCode(productCode).getName())
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
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button finishButton = (Button)findViewById(R.id.finishButton);
        Button promotionButton = findViewById(R.id.promotionButton);
        textAddress = (TextView)findViewById(R.id.textAddress);
        textNumber = (TextView)findViewById(R.id.textNumber);
        TextView textPrice = (TextView)findViewById(R.id.price);
        TextView textDiscount = (TextView)findViewById(R.id.discount);

        timeSpinner = (Spinner)findViewById(R.id.timeSpinner);
        paymentSpinner = findViewById(R.id.paymentSpinner);

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);

        daySpinner = (Spinner)findViewById(R.id.daySpinner);
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<String> keys = new ArrayList<>();
                Calendar now = Calendar.getInstance();

                now.add(Calendar.DAY_OF_MONTH, i);
                int day = now.get(Calendar.DAY_OF_WEEK);
                int time = now.get(Calendar.HOUR_OF_DAY)*60 + now.get(Calendar.MINUTE);

                boolean isWeekday = ((day >= Calendar.MONDAY) && (day <= Calendar.FRIDAY));

                TypedArray hours;
                if(isWeekday)
                    hours = getResources().obtainTypedArray(R.array.weekday_hours);
                else
                    hours = getResources().obtainTypedArray(R.array.weekend_hours);

                for(int j = 0;j < hours.length();j++){
                    int id = hours.getResourceId(j, 0);
                    String[] aux = getResources().getStringArray(id);
                    String initialHour = aux[0];
                    String finalHour = aux[1];

                    if(i == 0){
                        int hour = Integer.valueOf(initialHour.substring(0, initialHour.length() - 3));
                        int min = Integer.valueOf(initialHour.substring(3, initialHour.length()));
                        if(time + 120 <= hour * 60 + min){
                            keys.add(initialHour + " - " + finalHour);
                        }
                    }else keys.add(initialHour + " - " + finalHour);
                }
                hours.recycle();

                timeSpinner.setSelection(0);
                String[] arraySpinner = keys.toArray(new String[keys.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(FinishOrderActivity.this,R.layout.support_simple_spinner_dropdown_item, arraySpinner);
                timeSpinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        List<String> keys2 = new ArrayList<>();

        for(int i = 0; i <= 3; i++){
            if(hour >= 22 && i == 0)
                continue;
            keys2.add(DateHandler.toString(now));
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        String[] arraySpinner2 = keys2.toArray(new String[keys2.size()]);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, arraySpinner2);
        daySpinner.setAdapter(adapter2);

        String[] arraySpinner3 = new String[2];
        arraySpinner3[0] = "Elegir forma de pago";
        arraySpinner3[1] = "Efectivo";
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, arraySpinner3){
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                return view;
            }
        };

        textCash = findViewById(R.id.amountCash);

        paymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 1)
                    textCash.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        paymentSpinner.setAdapter(adapter3);
        paymentSpinner.setSelection(0);

        double price = CatalogActivity.cart.getTotalWithDiscount();
        double total = CatalogActivity.cart.getTotal();
        double discount = CatalogActivity.cart.getDiscount();
        if(total < AppSettings.FREE_DELIVERY_PRICE)
            price += AppSettings.DELIVERY_COST;
        textPrice.setText("S/." + String.valueOf(DecimalHandler.round(price, 2)));
        if(total < AppSettings.FREE_DELIVERY_PRICE)
            textDiscount.setText("0");
        else
            textDiscount.setText("-" + String.valueOf(DecimalHandler.round(discount + 6, 2)));
        textAddress.setText(AppSettings.CURRENT_CUSTOMER.getAddress().getAddressName());
        textNumber.setText(AppSettings.CURRENT_CUSTOMER.getAddress().getAddressNumber());
        promotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FinishOrderActivity.this, PromotionActivity.class);
                startActivityForResult(i, PROMOTION);
            }
        });
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

                if(paymentSpinner.getSelectedItemPosition() == 0){
                    Dialog dialog = new AlertDialog.Builder(FinishOrderActivity.this)
                            .setTitle("Error")
                            .setMessage("Debe seleccionar una forma de pago.")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    return;
                }

                double cash;
                try {
                    cash = Double.valueOf(textCash.getText().toString());
                }catch(Exception e){
                    Dialog dialog = new AlertDialog.Builder(FinishOrderActivity.this)
                            .setTitle("Error")
                            .setMessage("Debe ingresar un valor numérico como monto a pagar.")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    return;
                }

                if(cash < CatalogActivity.cart.getTotalWithDiscount()){
                    Dialog dialog = new AlertDialog.Builder(FinishOrderActivity.this)
                            .setTitle("Error")
                            .setMessage("Debe ingresar un valor mayor al monto a pagar.")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    return;
                }

                if(timeSpinner.getSelectedItem() == null){
                    Dialog dialog = new AlertDialog.Builder(FinishOrderActivity.this)
                            .setTitle("Error")
                            .setMessage("Debe seleccionar un horario válido.")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    return;
                }


                final Sale sale = new Sale();
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
                sale.setTypePayment(paymentSpinner.getSelectedItemPosition());
                sale.setAmountToPay(BigDecimal.valueOf(cash));
                if(AppSettings.CURRENT_PROMOTION != null)
                    sale.setPromotion(AppSettings.CURRENT_PROMOTION);

                Dialog dialog = new AlertDialog.Builder(FinishOrderActivity.this)
                        .setTitle("Confirmación")
                        .setMessage("¿Desea terminar la compra?")
                        .setPositiveButton("Terminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ServerSaleTask task = new ServerSaleTask();
                                task.execute(sale);
                            }
                        })
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_info).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PROMOTION){
            if(resultCode == RESULT_OK){
                TextView textPrice = (TextView)findViewById(R.id.price);
                TextView textDiscount = (TextView)findViewById(R.id.discount);

                double price = CatalogActivity.cart.getTotalWithDiscount();
                double total = CatalogActivity.cart.getTotal();
                double discount = CatalogActivity.cart.getDiscount();
                if(total < AppSettings.FREE_DELIVERY_PRICE)
                    price += AppSettings.DELIVERY_COST;
                textPrice.setText("S/." + String.valueOf(DecimalHandler.round(price, 2)));
                if(total < AppSettings.FREE_DELIVERY_PRICE)
                    textDiscount.setText("0");
                else
                    textDiscount.setText("-" + String.valueOf(DecimalHandler.round(discount + 6, 2)));
            }
        }
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
