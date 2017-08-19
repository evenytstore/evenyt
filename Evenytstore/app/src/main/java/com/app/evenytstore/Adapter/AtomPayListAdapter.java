package com.app.evenytstore.Adapter;

/**
 * Created by Enrique on 09/08/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.evenytstore.Model.Item;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.DecimalHandler;

import java.util.List;

public class AtomPayListAdapter extends ArrayAdapter<Item> {

    private List<Item> items;
    private int layoutResourceId;
    private Context context;

    public AtomPayListAdapter(Context context, int layoutResourceId, List<Item> items) {
        super(context, layoutResourceId, items);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AtomPaymentHolder holder = null;

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new AtomPaymentHolder();
        holder.atomPayment = items.get(position);
        holder.removePaymentButton = (ImageButton)row.findViewById(R.id.deleteItem);
        holder.removePaymentButton.setTag(holder.atomPayment);

        holder.lessItem=(ImageButton) row.findViewById(R.id.lessItem);
        holder.lessItem.setTag(holder.atomPayment);

        holder.moreItem=(ImageButton) row.findViewById(R.id.moreItem);
        holder.moreItem.setTag(holder.atomPayment);

        holder.name = (TextView)row.findViewById(R.id.item_name);
        holder.value = (TextView)row.findViewById(R.id.item_subtotal);
        holder.quantity = (TextView)row.findViewById(R.id.item_quantity);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    private void setupItem(AtomPaymentHolder holder) {
        holder.name.setText(holder.atomPayment.getName());
        holder.value.setText(String.valueOf(DecimalHandler.round(holder.atomPayment.getSubtotal(),2)));
        holder.quantity.setText("x"+String.valueOf(holder.atomPayment.getCount()));
    }

    public static class AtomPaymentHolder {
        Item atomPayment;
        TextView name;
        TextView quantity;
        TextView value;

        ImageButton removePaymentButton;
        ImageButton lessItem;
        ImageButton moreItem;
    }
}