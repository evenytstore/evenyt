package com.app.evenytstore.Adapter;

/**
 * Created by Enrique on 25/08/2017.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.app.evenytstore.Activity.CatalogActivity;
import com.app.evenytstore.Activity.CheckoutActivity;
import com.app.evenytstore.Model.Cart;
import com.app.evenytstore.Model.Item;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.DecimalHandler;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

import EvenytServer.model.Product;
import EvenytServer.model.ProductXBundle;
import EvenytServer.model.ProductXSize;
import EvenytServer.model.Size;

public class CheckoutAdapter extends ArrayAdapter<Item> {

    private Context mContext;
    private List<Item> itemList;
    private int layoutResourceId;

    public class MyViewHolder{

        ImageView image;
        TextView name;
        TextView quantity;
        TextView subtotal;
        public ImageView removeButton;
        public int newCount;
        /*public TextView title, price;
        public RecyclerView countRecyclerView;
        public ImageView thumbnail;*/
        public Item item;
    }

    public void removeProduct(Item item){
        itemList.remove(item);
        CatalogActivity.cart.removeItem(item.getCount(), item);
        notifyDataSetChanged();
        if(itemList.size() == 0)
            ((CheckoutActivity)mContext).updatePrice();
        //cart.addItem(1,productXSize );
    }

    public CheckoutAdapter(Context mContext, int layoutResourceId, List<Item> itemList) {
        super(mContext, layoutResourceId, itemList);
        this.mContext = mContext;
        this.itemList = itemList;
        this.layoutResourceId = layoutResourceId;
    }

    private void showTotalDialog(final MyViewHolder holder, Item item){

        Product product = item.getProduct();
        final ProductXSize productSize = item.getProductXSize();
        Size size = Shelf.getSizeByCode(productSize.getSizeCode());
        final Dialog dialog = new Dialog(mContext, R.style.Theme_Dialog);
        dialog.setContentView(R.layout.dialog_item);
                /*LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.dialog_address, null);*/
        Button addButton = dialog.findViewById(R.id.addButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        TextView textDescription = dialog.findViewById(R.id.txtDescription);
        final TextView textPrice = dialog.findViewById(R.id.txtPrice);
        final TextView textCount = dialog.findViewById(R.id.txtCount);
        final TextView textSubtotal = dialog.findViewById(R.id.txtSubtotal);
        ImageView addOne = dialog.findViewById(R.id.addOneImage);
        ImageView subOne = dialog.findViewById(R.id.subOneImage);
        ImageView image = dialog.findViewById(R.id.imageProduct);

        try{
            String imageURL = mContext.getString(R.string.S3MainURL)+product.getImgSrc();
            Glide.with(mContext).load(imageURL).into(image);
        }catch(Exception e){
            //File unavavailable
            e.printStackTrace();
        }
        textDescription.setText(product.getName() + " - " + size.getName());
        textPrice.setText("S/"+DecimalHandler.round(productSize.getPrice().doubleValue(), 2)+"");
        final int currentCount;
        if(!CatalogActivity.cart.getHashProducts().containsKey(productSize))
            currentCount = 0;
        else
            currentCount = ((Item)CatalogActivity.cart.getHashProducts().get(productSize)).getCount();
        if(currentCount <= 1)
            holder.newCount = 1;
        else
            holder.newCount = currentCount;

        textCount.setText(String.valueOf(holder.newCount));
        textSubtotal.setText("S/"+DecimalHandler.round(productSize.getPrice().doubleValue() * holder.newCount, 2)+"");

        addOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.newCount++;
                textCount.setText(String.valueOf(holder.newCount));
                textSubtotal.setText("S/"+DecimalHandler.round(productSize.getPrice().doubleValue() * holder.newCount, 2)+"");
            }
        });

        subOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.newCount == 0)
                    return;
                holder.newCount--;
                textCount.setText(String.valueOf(holder.newCount));
                textSubtotal.setText("S/"+DecimalHandler.round(productSize.getPrice().doubleValue() * holder.newCount, 2)+"");
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.newCount > currentCount)
                    CatalogActivity.cart.addItem(holder.newCount - currentCount, productSize);
                else if(holder.newCount < currentCount)
                    CatalogActivity.cart.removeItem(currentCount - holder.newCount, ((Item)CatalogActivity.cart.getHashProducts().get(productSize)));
                holder.quantity.setText(""+holder.item.getCount());

                holder.subtotal.setText("S/." + String.valueOf(DecimalHandler.round(holder.item.getSubtotal(),2)+""));
                ((CheckoutActivity)mContext).updatePrice();
                dialog.dismiss();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row ;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);
        Log.d("GETVIEW", ""+position);
        final MyViewHolder holder = new MyViewHolder();
        holder.item = itemList.get(position);
        holder.image = row.findViewById(R.id.item_image);
        holder.name =  (TextView)row.findViewById(R.id.item_name);
        holder.quantity = (TextView)row.findViewById(R.id.item_quantity);
        holder.subtotal = (TextView)row.findViewById(R.id.item_subtotal);
        holder.name.setTypeface(null, Typeface.NORMAL);
        holder.quantity.setTypeface(null, Typeface.NORMAL);
        holder.subtotal.setTypeface(null, Typeface.NORMAL);
        holder.removeButton = row.findViewById(R.id.removeButton);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTotalDialog(holder, holder.item);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTotalDialog(holder, holder.item);
            }
        });
        holder.quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTotalDialog(holder, holder.item);
            }
        });
        holder.subtotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTotalDialog(holder, holder.item);
            }
        });
        holder.removeButton.setImageResource(R.drawable.ic_close_button);
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeProduct(holder.item);
            }
        });

        row.setTag(holder);
        setupItem(holder);
        return row;
    }

    private void setupItem(MyViewHolder holder) {
        ProductXSize product = holder.item.getProductXSize();
        Product p = Shelf.getProductByCode(product.getProductCode());
        holder.name.setText(p.getName());
        holder.quantity.setText(""+holder.item.getCount());

        holder.subtotal.setText("S/." + String.valueOf(DecimalHandler.round(holder.item.getSubtotal(),2)+""));
        ((CheckoutActivity)mContext).updatePrice();

        // loading product cover using Glide library
        try{
            String imageURL = mContext.getString(R.string.S3MainURL)+p.getImgSrc();
            Glide.with(mContext).load(imageURL).into(holder.image);
        }catch(Exception e){
            //File unavavailable
            e.printStackTrace();
        }
        //Glide.with(mContext).load(product.getThumbnail()).into(holder.thumbnail);
        /*
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });*/
    }
}
