package com.app.evenytstore.Adapter;

/**
 * Created by Enrique on 25/08/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.app.evenytstore.Activity.CheckoutActivity;
import com.app.evenytstore.Model.Cart;
import com.app.evenytstore.Model.Item;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.MyViewHolder> {

    private Context mContext;
    private List<Item> itemList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price;
        public RecyclerView countRecyclerView;
        public Button removeButton;
        public ImageView thumbnail;
        public Item item;



        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            countRecyclerView = (RecyclerView) view.findViewById(R.id.countRecyclerView);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 1, LinearLayoutManager.HORIZONTAL, false);
            countRecyclerView.setLayoutManager(mLayoutManager);
            List<String> countValues = new ArrayList<>();
            countValues.add("1");countValues.add("2");countValues.add("3");countValues.add("4");countValues.add("Más");
            final CountAdapter countAdapter = new CountAdapter(countValues, new CountAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String item) {
                    if(item.equals("Más")){
                        //Show a dialog where the user can
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        LayoutInflater inflater = ((CheckoutActivity)mContext).getLayoutInflater();
                        final View dialogLayout = inflater.inflate(R.layout.dialog_number,null);
                        builder.setView(dialogLayout)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //If OK pressed then the quantity is obtained and put in
                                        //the item
                                        EditText prodquantText = (EditText)dialogLayout.findViewById(R.id.product_quantity);
                                        String strQuantity = prodquantText.getText().toString();
                                        if(strQuantity.equals(""))strQuantity = "1";
                                        int quantity = Integer.parseInt(strQuantity);
                                        if(MyViewHolder.this.item.getCount() > quantity)
                                            MyViewHolder.this.item.sub(MyViewHolder.this.item.getCount() - quantity);
                                        else
                                            MyViewHolder.this.item.sum(quantity - MyViewHolder.this.item.getCount());
                                        CheckoutAdapter.this.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .setTitle("Especifique la cantidad deseada");
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else{
                        if(item.equals("1")){
                            if(MyViewHolder.this.item.getCount() > 1)
                                MyViewHolder.this.item.sub(MyViewHolder.this.item.getCount() - 1);
                            else
                                MyViewHolder.this.item.sum(1 - MyViewHolder.this.item.getCount());
                        }else if(item.equals("2")){
                            if(MyViewHolder.this.item.getCount() > 2)
                                MyViewHolder.this.item.sub(MyViewHolder.this.item.getCount() - 2);
                            else
                                MyViewHolder.this.item.sum(2 - MyViewHolder.this.item.getCount());
                        }else if(item.equals("3")){
                            if(MyViewHolder.this.item.getCount() > 3)
                                MyViewHolder.this.item.sub(MyViewHolder.this.item.getCount() - 3);
                            else
                                MyViewHolder.this.item.sum(3 - MyViewHolder.this.item.getCount());
                        }else if(item.equals("4")){
                            if(MyViewHolder.this.item.getCount() > 4)
                                MyViewHolder.this.item.sub(MyViewHolder.this.item.getCount() - 4);
                            else
                                MyViewHolder.this.item.sum(4 - MyViewHolder.this.item.getCount());
                        }
                        CheckoutAdapter.this.notifyDataSetChanged();
                    }
                }
            });
            countRecyclerView.setAdapter(countAdapter);
            removeButton = (Button) view.findViewById(R.id.removeButton);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            //   overflow = (ImageView) view.findViewById(R.id.overflow);
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeProduct(item);
                }
            });
        }
    }

    public void removeProduct(Item item){
        itemList.remove(item);
        notifyDataSetChanged();
        //cart.addItem(1,productXSize );
    }

    public CheckoutAdapter(Context mContext, List<Item> itemList) {
        this.mContext = mContext;
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.checkout_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        ProductXSize product = item.getProductXSize();
        Product p = Shelf.getProductByCode(product.getProductCode());
        holder.title.setText(p.getName());

        holder.price.setText(item.getSubtotal()+"");
        holder.item = item;

        // loading product cover using Glide library
        try{
            String imageURL = mContext.getString(R.string.S3MainURL)+p.getImgSrc();
            Glide.with(mContext).load(imageURL).into(holder.thumbnail);
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

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}