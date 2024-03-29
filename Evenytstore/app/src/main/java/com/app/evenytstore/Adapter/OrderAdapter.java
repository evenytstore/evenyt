package com.app.evenytstore.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.evenytstore.Activity.OrderDetailActivity;
import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.DecimalHandler;

import java.util.List;

import EvenytServer.model.Sale;

/**
 * Created by Enrique on 29/09/2017.
 */

public class OrderAdapter extends ArrayAdapter<Sale> {
    public List<Sale> orders;
    private int layoutResourceId;
    private Context context;

    public OrderAdapter(Context context, int layoutResourceId,List<Sale> orders) {
        super(context, layoutResourceId, orders);

        this.orders = orders;
        this.layoutResourceId = layoutResourceId;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final OrderAdapter.OrderHolder holder;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new OrderAdapter.OrderHolder();
        holder.order = orders.get(position);
        holder.deleteOrder = (ImageButton)row.findViewById(R.id.orderDelete);
        holder.deleteOrder.setTag(holder.order);

        holder.date = (TextView)row.findViewById(R.id.orderDate);
        holder.amount = (TextView)row.findViewById(R.id.orderAmount);
        holder.status = (TextView)row.findViewById(R.id.status);

        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), OrderDetailActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AppSettings.SELECTED_SALE = holder.order;
                view.getContext().startActivity(i);
            }
        });
        holder.amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), OrderDetailActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AppSettings.SELECTED_SALE = holder.order;
                view.getContext().startActivity(i);
            }
        });
        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), OrderDetailActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AppSettings.SELECTED_SALE = holder.order;
                view.getContext().startActivity(i);
            }
        });

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(final OrderAdapter.OrderHolder holder) {
        //holder.date.setText(holder.order.getDate().toString());
        holder.date.setTypeface(null, Typeface.NORMAL);
        holder.amount.setTypeface(null, Typeface.NORMAL);
        holder.status.setTypeface(null, Typeface.NORMAL);
        holder.date.setText(holder.order.getBundle().getPreferredHour());
        holder.amount.setText("S/."+String.valueOf(DecimalHandler.round(holder.order.getTotal().doubleValue(), 2)));
        if (holder.order.getStatus()==2){
            holder.status.setText("Entregado");
        }else if (holder.order.getStatus()==0){
            holder.status.setText("Cancelado");
        }else if (holder.order.getStatus()==1) {
            holder.status.setText("Pendiente");
        }

    }

    public static class OrderHolder {
        Sale order;
        TextView date;
        TextView amount;
        TextView status;

        ImageButton deleteOrder;
    }
}
