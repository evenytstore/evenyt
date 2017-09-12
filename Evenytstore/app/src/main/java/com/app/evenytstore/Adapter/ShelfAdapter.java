package com.app.evenytstore.Adapter;

/**
 * Created by Enrique on 09/08/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.evenytstore.Activity.CatalogActivity;
import com.app.evenytstore.Activity.CheckoutActivity;
import com.app.evenytstore.Model.Cart;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.DecimalHandler;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;

public class ShelfAdapter extends RecyclerView.Adapter<ShelfAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> productList;
    private Cart cart;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,price;
        public ImageView thumbnail;
        public Product product;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            price = (TextView) view.findViewById(R.id.price);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            //   overflow = (ImageView) view.findViewById(R.id.overflow);
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectSize(product);

                }
            });
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectSize(product);
                }
            });
            price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectSize(product);
                }
            });
            count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectSize(product);
                }
            });
        }

        public void selectSize(final Product p){
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Elija el tamaño deseado");

            ListView sizeList = new ListView(mContext);
            final List<ProductXSize> sizes = Shelf.getProductsToSizes().get(p.getCode());
            List<String> sizesText = new ArrayList<>();
            for(ProductXSize size : sizes)
                sizesText.add(Shelf.getSizeByCode(size.getSizeCode()).getName() + " - S/" + DecimalHandler.round(size.getPrice().doubleValue(), 2));
            ArrayAdapter<String> modeAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, android.R.id.text1, sizesText);
            sizeList.setAdapter(modeAdapter);

            builder.setView(sizeList);
            final Dialog dialog = builder.create();

            sizeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    addProduct(p.getName(), sizes.get(position));
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    public void addProduct(String productName, ProductXSize productXSize){
        Toast.makeText(mContext, productName+" Agregado", Toast.LENGTH_SHORT).show();

        cart.addItem(1,productXSize );
    }

    public ShelfAdapter(Context mContext, List<Product> productList, Cart cart) {
        this.mContext = mContext;
        this.productList = productList;
        this.cart=cart;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_catalog, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.title.setText(product.getName());

        //holder.price.setText(product.getPrice()+"");
        holder.product = product;

        // loading product cover using Glide library
        try{
            String imageURL = mContext.getString(R.string.S3MainURL)+product.getImgSrc();
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
        return productList.size();
    }
}