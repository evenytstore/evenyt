package com.app.evenytstore.Model;

/**
 * Created by Enrique on 09/08/2017.
 */

import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.app.evenytstore.Adapter.AtomPayListAdapter;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.DecimalHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;

public class Cart {
    public double total=0;

    private HashMap<ProductXSize, Item> hashProducts = new HashMap<>(); //key=code
    private AtomPayListAdapter adapter;
    //private ViewGroup container;


    public Cart(AtomPayListAdapter adapter){
        total=0;
        this.adapter=adapter;
        //this.container=container;
    }

    public double getTotal(){
        return total;
    }

    public double getTotalWithDiscount(){
        if(total < 100)
            return total;
        else if(total < 150)
            return total*0.97;
        else
            return total*0.94;
    }

    public double getDiscount(){
        if(total < 100)
            return 0;
        else if(total < 150)
            return total*0.03;
        else
            return total*0.06;
    }

    public boolean addItem(int numItems, ProductXSize product){
        if(product == null) return false;
        if (numItems>0){

            Item aux;
            if (hashProducts.get(product)!=null){
                aux =(Item) hashProducts.get(product);
            }else{
                aux=new Item(product);
                adapter.add(aux);
            }
            if (aux.getPrice()==-1) throw new RuntimeException("Bad price: -1");
            total=total-aux.getSubtotal();

            aux.sum(numItems);
            hashProducts.put(product, aux);

            adapter.notifyDataSetChanged();

            total=total+aux.getSubtotal();
            //double price = DecimalHandler.round(total,2);
            //updateText(price);
        }
        return true;
    }


    public void update(){
        adapter.notifyDataSetChanged();
    }


    /*private void updateMessage(double price){
        TextView progressMessage = (TextView) container.findViewById(R.id.progressMessage);
        RoundCornerProgressBar progress = (RoundCornerProgressBar) container.findViewById(R.id.progress);
        if(price < 25){
            progress.setMax(25);
            progress.setProgress((float)price);
            progressMessage.setText("Por compras de S/.25 o más ahorre S/.6 de envió!");
        }else if(price < 100){
            progress.setMax(100);
            progress.setProgress((float)price);
            progressMessage.setText("Por compras de S/.100 o más obtenga un descuento del 3%!");
        }else if(price < 150){
            progress.setMax(150);
            progress.setProgress((float)price);
            progressMessage.setText("Por compras de S/.150 o más obtenga un descuento del 6%!");
        }else{
            progress.setMax(150);
            progress.setProgress(150);
            progressMessage.setText("Envío gratuito y descuento de 6% obtenidos!");
        }
    }*/

    public void update(AtomPayListAdapter adapter){
        this.adapter = adapter;
        //this.container = container;

        //double price = DecimalHandler.round(total,2);
        //updateText(price);
        for(Object o : hashProducts.values()){
            Item i = (Item)o;
            adapter.add(i);
        }
        adapter.notifyDataSetChanged();
    }

    public boolean removeItem(int numItems, Item item){
        ProductXSize product = item.getProductXSize();
        Product p = Shelf.getProductByCode(product.getProductCode());

        if(p == null) return false;
        if (numItems>0 && hashProducts.get(product)!=null){
            Item aux=null;
            if ( ((Item)hashProducts.get(product)).getCount() - numItems > 0){
                aux =(Item) hashProducts.get(product);
                total=total-aux.getSubtotal();
                aux.sub(numItems);
                hashProducts.put(product, aux);
                total=total+aux.getSubtotal();
            }else{
                aux=(Item)hashProducts.get(product);
                hashProducts.remove(product);
                adapter.remove(item);
                total=total-aux.getSubtotal();
            }
            if (aux!=null && aux.getPrice()==-1) throw new RuntimeException("Bad price: -1");

            adapter.notifyDataSetChanged();
        }

        return true;
    }


    /*private void updateText(double price){
        TextView totalView=(TextView) container.findViewById(R.id.total);
        TextView deliveryTV = (TextView)container.findViewById(R.id.delivery);

        if(price < 25) {
            deliveryTV.setText(new DecimalFormat("#.##").format(DecimalHandler.round(AppSettings.DELIVERY_COST,2)));
            totalView.setText(new DecimalFormat("#.##").format(DecimalHandler.round(getTotalWithDiscount(),2) + AppSettings.DELIVERY_COST));
        }
        else{
            deliveryTV.setText("0");
            totalView.setText(new DecimalFormat("#.##").format(getTotalWithDiscount()));
        }

        updateMessage(price);
    }*/


    public void setAdapter(AtomPayListAdapter adapter) {
        this.adapter = adapter;
    }
    public int numProducts(){
        return hashProducts.size();
    }

    public HashMap getHashProducts() {
        return hashProducts;
    }

}
