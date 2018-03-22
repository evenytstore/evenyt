package com.app.evenytstore.Activity;

/**
 * Created by Enrique on 29/09/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.apigateway.ApiClientException;
import com.app.evenytstore.Adapter.AtomPayListAdapter;
import com.app.evenytstore.Adapter.OrderAdapter;
import com.app.evenytstore.Fragment.CatalogFragment;
import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.Cart;
import com.app.evenytstore.Model.DatabaseAccess;
import com.app.evenytstore.Model.Item;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Server.ServerAccess;
import com.app.evenytstore.Utility.DateHandler;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import EvenytServer.model.AllSales;
import EvenytServer.model.Category;
import EvenytServer.model.Customer;
import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;
import EvenytServer.model.Sale;


public class OrdersActivity extends AppCompatActivity {

    public class GetOrdersTask extends AsyncTask<Void, Void, AllSales> {
        @Override
        protected AllSales doInBackground(Void... params) {
            AllSales sales = null;
            String id = AppSettings.getSerializableId();
            int tries = 0;
            while(sales == null)
                try {
                    sales = ServerAccess.getClient().salesIdCustomerGet(id);
                }catch(Exception e){
                    e.printStackTrace();
                    tries += 1;
                    if(tries == AppSettings.MAX_RETRIES)
                        return null;
                }
            return sales;
        }

        protected void onPostExecute(AllSales sales){
            mLoader.setVisibility(View.GONE);
            if(sales == null) {
                Dialog dialog = new AlertDialog.Builder(OrdersActivity.this)
                        .setTitle("Error")
                        .setMessage("No se pudo establecer conexión al servidor.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                return;
            }
            for (Sale sale : sales) {
                adapter.add(sale);
            }
            adapter.notifyDataSetChanged();
        }
    }


    public class CancelOrderTask extends AsyncTask<Sale, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Sale... params) {

            Sale sale = params[0];

            try {
                ServerAccess.getClient().salesPatch(sale);
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
            return true;
        }


        protected void onPostExecute(Boolean result){
            mLoader.setVisibility(View.GONE);
            if(result){
                Dialog dialog = new android.app.AlertDialog.Builder(OrdersActivity.this)
                        .setTitle("Info")
                        .setMessage("El pedido se ha cancelado correctamente.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_info).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }else{
                Dialog dialog = new AlertDialog.Builder(OrdersActivity.this)
                        .setTitle("Error")
                        .setMessage("No se pudo establecer conexión al servidor.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        }
    }

    private OrderAdapter adapter;
    private RelativeLayout mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mLoader = findViewById(R.id.loadingPanel);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter=new OrderAdapter(OrdersActivity.this, R.layout.order_item, new ArrayList<Sale>());
        ListView orderList=(ListView) findViewById(R.id.listHistoricOrders);
        /*orderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("POSITION",""+position);
            }
        });*/

        orderList.setAdapter(adapter);

        mLoader.setVisibility(View.VISIBLE);
        GetOrdersTask task = new GetOrdersTask();
        task.execute();
    }


    private void delete(View v){
        Sale orderToRemove = (Sale) v.getTag();
        Calendar cal = DateHandler.toDateHour(orderToRemove.getBundle().getPreferredHour().replace('T',' '));
        Calendar curr = Calendar.getInstance();
        long diff = cal.getTimeInMillis() - curr.getTimeInMillis();
        if(orderToRemove.getStatus() == 0){
            Dialog dialog = new AlertDialog.Builder(OrdersActivity.this)
                    .setTitle("Alerta")
                    .setMessage("El pedido ya ha sido cancelado previamente.")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            return;
        }else if(orderToRemove.getStatus() == 2){
            Dialog dialog = new AlertDialog.Builder(OrdersActivity.this)
                    .setTitle("Alerta")
                    .setMessage("El pedido ya ha sido entregado.")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            return;
        }else if(diff < 1000*60){
            Dialog dialog = new AlertDialog.Builder(OrdersActivity.this)
                    .setTitle("Error")
                    .setMessage("No puede cancelar su pedido ya que este está en curso.")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            return;
        }
        orderToRemove.setStatus(0);
        adapter.notifyDataSetChanged();

        //Send change status to the server
        //Update order to cancelled
        mLoader.setVisibility(View.VISIBLE);
        CancelOrderTask task = new CancelOrderTask();
        task.execute(orderToRemove);
    }

    //less one order
    public void deleteOrderClickHandler(final View v) {

        Dialog dialog = new android.support.v7.app.AlertDialog.Builder(OrdersActivity.this)
                .setTitle("Alerta")
                .setMessage("¿Seguro que desea cancelar el pedido?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        delete(v);
                    } })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    } })
                .setIcon(android.R.drawable.ic_dialog_alert).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
