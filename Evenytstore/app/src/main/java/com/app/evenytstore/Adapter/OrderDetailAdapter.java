package com.app.evenytstore.Adapter;

/**
 * Created by Enrique on 10/10/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.bumptech.glide.Glide;

import java.util.List;

import EvenytServer.model.Product;
import EvenytServer.model.ProductXBundle;

public class OrderDetailAdapter extends ArrayAdapter<ProductXBundle> {
    public List<ProductXBundle> products;
    private int layoutResourceId;
    private Context mContext;

    public OrderDetailAdapter(Context context, int layoutResourceId, List<ProductXBundle> products){
        super(context, layoutResourceId, products);
        this.products = products;
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row ;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);
        Log.d("GETVIEW", ""+position);
        OrderDetailHolder holder = new OrderDetailHolder();
        holder.item = products.get(position);
        holder.image = row.findViewById(R.id.item_image);
        holder.name =  (TextView)row.findViewById(R.id.item_name);
        holder.quantity = (TextView)row.findViewById(R.id.item_quantity);
        holder.subtotal = (TextView)row.findViewById(R.id.item_subtotal);

        row.setTag(holder);
        setupItem(holder);
        return row;
    }

    private void setupItem(OrderDetailAdapter.OrderDetailHolder holder){
        Product p = Shelf.getProductByCode(holder.item.getProductIdProduct());
        String imageURL = mContext.getString(R.string.S3MainURL)+p.getImgSrc();
        Glide.with(mContext).load(imageURL).into(holder.image);
        holder.name.setText(p.getName());
        holder.quantity.setText(""+holder.item.getQuantity());
        holder.subtotal.setText("S/."+String.format("%.2f",holder.item.getSubtotal()));
        Log.d("SETUPITEM", p.getCode());
    }

    public static class OrderDetailHolder{
        ProductXBundle item;
        ImageView image;
        TextView name;
        TextView quantity;
        TextView subtotal;
    }
}
