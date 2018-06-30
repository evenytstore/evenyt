package com.app.evenytstore.Activity;

/**
 * Created by Enrique on 25/08/2017.
 */

import android.app.Dialog;
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
import android.widget.RelativeLayout;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import EvenytServer.model.Address;
import EvenytServer.model.AllProductsXBundles;
import EvenytServer.model.Bundle;
import EvenytServer.model.ProductXBundle;
import EvenytServer.model.Sale;


public class FinishOrderActivity extends AppCompatActivity {

    Spinner daySpinner;
    Spinner timeSpinner;
    Spinner paymentSpinner;
    Button finishButton;
    TextView textAddress;
    TextView textNumber;
    EditText textCash;
    String mCity;
    String mDistrict;
    Address mAddress = new Address();
    private int PROMOTION = 1;
    private RelativeLayout mLoader;
    private boolean skipFirstDay = false;
    private long buttonTime = -1;
    private long buttonTime2 = -1;

    ArrayAdapter<String> mDistrictAdapter;


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
            mLoader.setVisibility(View.GONE);
            if(result.equals("Correct")){
                AppSettings.CURRENT_PROMOTION = null;
                setResult(RESULT_OK, new Intent());
                finish();
            }else if (result.equals("Failed")){
                if(!isFinishing() && !isDestroyed()){
                    Dialog dialog = new AlertDialog.Builder(FinishOrderActivity.this)
                            .setTitle("Error")
                            .setMessage("No se pudo establecer conexión al servidor.")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            }else{ //Not enough stock
                if(!isFinishing() && !isDestroyed()){
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
            buttonTime2 = -1;
        }
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);
        mLoader = findViewById(R.id.loadingPanel);

        Address address = AppSettings.CURRENT_CUSTOMER.getAddress();

        mAddress.setAddressName(address.getAddressName());
        mAddress.setAddressNumber(address.getAddressNumber());
        mAddress.setCity(address.getCity());
        mAddress.setDistrict(address.getDistrict());
        mAddress.setLatitude(address.getLatitude());
        mAddress.setLongitude(address.getLongitude());

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

        finishButton = (Button)findViewById(R.id.finishButton);
        Button promotionButton = findViewById(R.id.promotionButton);
        Button addressButton = findViewById(R.id.addressButton);
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
                if(skipFirstDay)
                    now.add(Calendar.DAY_OF_MONTH, 1);
                int day = now.get(Calendar.DAY_OF_WEEK);
                int time = now.get(Calendar.HOUR_OF_DAY)*60 + now.get(Calendar.MINUTE);

                boolean isWeekday = ((day >= Calendar.MONDAY) && (day <= Calendar.SATURDAY));

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

                    if(i == 0 && !skipFirstDay){
                        int hour = Integer.valueOf(initialHour.substring(0, initialHour.length() - 3));
                        int min = Integer.valueOf(initialHour.substring(3, initialHour.length()));
                        if(time + 60 <= hour * 60 + min){
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

        Calendar initial = Calendar.getInstance();
        initial.set(2018, 3, 3, 0, 0, 0);
        for(int i = 0; i <= 7; i++){
            if(now.compareTo(initial) < 0){
                now.add(Calendar.DAY_OF_MONTH, 1);
                if(i == 0)
                    skipFirstDay = true;
                continue;
            }
            if(i == 0){

                int day = now.get(Calendar.DAY_OF_WEEK);
                int time = now.get(Calendar.HOUR_OF_DAY)*60 + now.get(Calendar.MINUTE);
                boolean isWeekday = ((day >= Calendar.MONDAY) && (day <= Calendar.SATURDAY));

                TypedArray hours;
                if(isWeekday)
                    hours = getResources().obtainTypedArray(R.array.weekday_hours);
                else
                    hours = getResources().obtainTypedArray(R.array.weekend_hours);

                int count = 0;
                for(int j = 0;j < hours.length();j++){
                    int id = hours.getResourceId(j, 0);
                    String[] aux = getResources().getStringArray(id);
                    String initialHour = aux[0];
                    String finalHour = aux[1];

                    int newHour = Integer.valueOf(initialHour.substring(0, initialHour.length() - 3));
                    int min = Integer.valueOf(initialHour.substring(3, initialHour.length()));
                    if(time + 60 <= newHour * 60 + min)
                        count++;
                }

                if(count == 0){
                    now.add(Calendar.DAY_OF_MONTH, 1);
                    skipFirstDay = true;
                    continue;
                }
            }
            keys2.add(DateHandler.toString(now));
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        String[] arraySpinner2 = keys2.toArray(new String[keys2.size()]);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, arraySpinner2);
        daySpinner.setAdapter(adapter2);

        String[] arraySpinner3 = new String[3];
        arraySpinner3[0] = "Elegir forma de pago";
        arraySpinner3[1] = "Efectivo";
        arraySpinner3[2] = "Pago con tarjeta VISA - POS";
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
                else
                    textCash.setVisibility(View.GONE);
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
            textDiscount.setText("S/.-" + String.valueOf(DecimalHandler.round(discount, 2)));
        else
            textDiscount.setText("S/.-" + String.valueOf(DecimalHandler.round(discount + AppSettings.DELIVERY_COST, 2)));
        textAddress.setText(AppSettings.CURRENT_CUSTOMER.getAddress().getAddressName());
        textNumber.setText(AppSettings.CURRENT_CUSTOMER.getAddress().getAddressNumber());
        promotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FinishOrderActivity.this, PromotionActivity.class);
                startActivityForResult(i, PROMOTION);
            }
        });
        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(FinishOrderActivity.this, R.style.Theme_Dialog);
                dialog.setContentView(R.layout.dialog_address);
                /*LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_address, null);*/
                Button addButton = dialog.findViewById(R.id.addButton);
                Button cancelButton = dialog.findViewById(R.id.cancelButton);
                final TextView textAddress2 = dialog.findViewById(R.id.txtAddress);
                final TextView textAddressNumber2 = dialog.findViewById(R.id.txtAddressNumber);
                textAddress2.setText(mAddress.getAddressName());
                textAddressNumber2.setText(mAddress.getAddressNumber());

                final Spinner citySpinner = dialog.findViewById(R.id.citySpinner);
                final Spinner districtSpinner = dialog.findViewById(R.id.districtSpinner);

                Set<String> citiesSet = Shelf.getHashCities().keySet();
                String[] cities = citiesSet.toArray(new String[citiesSet.size()]);

                final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(FinishOrderActivity.this,R.layout.support_simple_spinner_dropdown_item, cities){
                };
                citySpinner.setAdapter(adapter2);
                int cityIndex = 0;
                int districtIndex = 0;

                for(String c : cities){
                    if(c.equals(mAddress.getCity())) {
                        mCity = c;
                        break;
                    }
                    cityIndex += 1;
                }
                citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String newCity = adapter2.getItem(i);
                        if(!newCity.equals(mCity)){
                            mCity = newCity;
                            final List<String> districts = Shelf.getHashCities().get(mCity);
                            mDistrict = districts.get(0);
                            mDistrictAdapter = new ArrayAdapter<String>(FinishOrderActivity.this,R.layout.support_simple_spinner_dropdown_item, districts.toArray(new String[districts.size()])){

                            };
                            districtSpinner.setAdapter(mDistrictAdapter);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                citySpinner.setSelection(cityIndex);
                List<String> districts = Shelf.getHashCities().get(mCity);
                for(String d : districts){
                    if(d.equals(mAddress.getDistrict())) {
                        mDistrict = d;
                        mDistrictAdapter = new ArrayAdapter<String>(FinishOrderActivity.this,R.layout.support_simple_spinner_dropdown_item, districts.toArray(new String[districts.size()])){
                        };
                        districtSpinner.setAdapter(mDistrictAdapter);
                        break;
                    }
                    districtIndex += 1;
                }
                districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        mDistrict = mDistrictAdapter.getItem(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                districtSpinner.setSelection(districtIndex);

                /*AlertDialog.Builder builder = new AlertDialog.Builder(FinishOrderActivity.this);
                builder.setView(dialoglayout);*/

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String address = textAddress2.getText().toString();
                        String addressNumber = textAddressNumber2.getText().toString();
                        if(address.equals("")){
                            textAddress.setError("Debe ingresar una dirección.");
                            return;
                        }

                        LatLng latLng = AddressHandler.getLocationFromAddress(getApplicationContext(), address);

                        dialog.dismiss();

                        textAddress.setText(address);
                        textNumber.setText(addressNumber);

                        if(latLng != null){
                            mAddress.setLongitude(BigDecimal.valueOf(latLng.longitude));
                            mAddress.setLatitude(BigDecimal.valueOf(latLng.latitude));
                        }
                        else{
                            mAddress.setLatitude(null);
                            mAddress.setLongitude(null);
                        }
                        mAddress.setDistrict(mDistrict);
                        mAddress.setCity(mCity);
                        mAddress.setAddressName(address);
                        mAddress.setAddressNumber(addressNumber);
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long currentTime = System.currentTimeMillis();
                if(currentTime - buttonTime < 2000)
                    return;
                buttonTime = currentTime;

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

                double cash = 0;
                if(paymentSpinner.getSelectedItemPosition() == 1){
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

                    double price = CatalogActivity.cart.getTotalWithDiscount();
                    double total = CatalogActivity.cart.getTotal();
                    if(total < AppSettings.FREE_DELIVERY_PRICE)
                        price += AppSettings.DELIVERY_COST;
                    if(cash < price){
                        Dialog dialog = new AlertDialog.Builder(FinishOrderActivity.this)
                                .setTitle("Error")
                                .setMessage("Debe ingresar un valor mayor al monto a pagar.")
                                .setCancelable(false)
                                .setIcon(android.R.drawable.ic_dialog_alert).create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        return;
                    }
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
                hour = hour.substring(0, 5);
                bundle.setPreferredHour(deliveryDay+" "+hour);

                AllProductsXBundles productXBundles = new AllProductsXBundles();
                for(Object o : CatalogActivity.cart.getHashProducts().values()){
                    Item i = (Item)o;
                    ProductXBundle p = new ProductXBundle();
                    p.setAddress(mAddress);
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

                double price = CatalogActivity.cart.getTotalWithDiscount();
                double total = CatalogActivity.cart.getTotal();
                if(total < AppSettings.FREE_DELIVERY_PRICE)
                    price += AppSettings.DELIVERY_COST;
                sale.setTotal(BigDecimal.valueOf(price));
                sale.setBundle(bundle);
                sale.setTypePayment(paymentSpinner.getSelectedItemPosition());
                sale.setAmountToPay(BigDecimal.valueOf(cash));
                if(AppSettings.CURRENT_PROMOTION != null)
                    sale.setPromotion(AppSettings.CURRENT_PROMOTION);

                final Dialog dialog = new Dialog(FinishOrderActivity.this, R.style.Theme_Dialog);
                dialog.setContentView(R.layout.dialog_confirmation);
                /*LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_address, null);*/
                Button okButton = dialog.findViewById(R.id.okButton);
                Button cancelButton = dialog.findViewById(R.id.cancelButton);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        long currentTime = System.currentTimeMillis();
                        if(buttonTime2 != -1)
                            return;
                        buttonTime2 = currentTime;
                        mLoader.setVisibility(View.VISIBLE);
                        ServerSaleTask task = new ServerSaleTask();
                        task.execute(sale);
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(buttonTime2 != -1)
                            return;
                        dialog.dismiss();
                        buttonTime = -1;
                    }
                });

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
                    textDiscount.setText("S/.-" + String.valueOf(DecimalHandler.round(discount, 2)));
                else
                    textDiscount.setText("S/.-" + String.valueOf(DecimalHandler.round(discount + AppSettings.DELIVERY_COST, 2)));
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