package com.app.evenytstore.Adapter;

/**
 * Created by Enrique on 09/08/2017.
 */

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.evenytstore.Model.Cart;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.bumptech.glide.Glide;

import java.util.List;

import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;

public class ShelfAdapter extends RecyclerView.Adapter<ShelfAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductXSize> productList;
    private Cart cart;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,price;
        public ImageView thumbnail;
        public ProductXSize productXSize;


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
                    addProduct(""+title.getText(), productXSize);

                }
            });
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProduct(""+title.getText(), productXSize);
                }
            });
            price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProduct(""+title.getText(), productXSize);
                }
            });
            count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProduct(""+title.getText(), productXSize);
                }
            });
        }
    }

    public void addProduct(String productName, ProductXSize productXSize){
        Toast.makeText(mContext, productName+" Agregado", Toast.LENGTH_SHORT).show();

        cart.addItem(1,productXSize );
    }

    public ShelfAdapter(Context mContext, List<ProductXSize> productList, Cart cart) {
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
        ProductXSize product = productList.get(position);
        Product p = Shelf.getProductByCode(product.getProductCode());
        holder.title.setText(p.getName());

        holder.price.setText(product.getPrice()+"");
        holder.productXSize = product;

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
        return productList.size();
    }
}