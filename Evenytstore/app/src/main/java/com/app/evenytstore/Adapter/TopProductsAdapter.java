package com.app.evenytstore.Adapter;

/**
 * Created by Enrique on 09/08/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.evenytstore.Activity.CatalogActivity;
import com.app.evenytstore.Model.Cart;
import com.app.evenytstore.Model.Item;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.DecimalHandler;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;
import EvenytServer.model.Size;

public class TopProductsAdapter extends RecyclerView.Adapter<TopProductsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Product> productList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count,price;
        public ImageView thumbnail;
        public Product product;
        public int newCount;


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

        private void showTotalDialog(final ProductXSize productSize){

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
                newCount = 1;
            else
                newCount = currentCount;

            textCount.setText(String.valueOf(newCount));
            textSubtotal.setText("S/"+DecimalHandler.round(productSize.getPrice().doubleValue() * newCount, 2)+"");

            addOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newCount++;
                    textCount.setText(String.valueOf(newCount));
                    textSubtotal.setText("S/"+DecimalHandler.round(productSize.getPrice().doubleValue() * newCount, 2)+"");
                }
            });

            subOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(newCount == 0)
                        return;
                    newCount--;
                    textCount.setText(String.valueOf(newCount));
                    textSubtotal.setText("S/"+DecimalHandler.round(productSize.getPrice().doubleValue() * newCount, 2)+"");
                }
            });

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(newCount > currentCount)
                        CatalogActivity.cart.addItem(newCount - currentCount, productSize);
                    else if(newCount < currentCount)
                        CatalogActivity.cart.removeItem(currentCount - newCount, ((Item)CatalogActivity.cart.getHashProducts().get(productSize)));
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

        public void selectSize(final Product p){
            if(!Shelf.getProductsToSizes().containsKey(p.getCode()))
                return;
            final List<ProductXSize> sizes = Shelf.getProductsToSizes().get(p.getCode());
            if(sizes.size() == 1){
                showTotalDialog(sizes.get(0));
                return;
            }else if(sizes.size() == 0)
                return;
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("Elija el tamaño deseado");

            ListView sizeList = new ListView(mContext);
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
                    showTotalDialog(sizes.get(position));
                    //addProduct(""+title.getText()+": "+count.getText(), sizes.get(position));
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    public TopProductsAdapter(Context mContext, List<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
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

        List<ProductXSize> sizes = Shelf.getProductsToSizes().get(product.getCode());
        if(sizes.size() == 1){
            holder.price.setText("S/"+DecimalHandler.round(sizes.get(0).getPrice().doubleValue(), 2)+"");
            holder.price.setVisibility(View.VISIBLE);
        }
        else
            holder.price.setVisibility(View.GONE);
        holder.product = product;

        holder.count.setText(Shelf.getBrandByCode(product.getBrandCode()).getName());
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

    @Override
    public int getItemCount() {
        return productList.size();
    }
}