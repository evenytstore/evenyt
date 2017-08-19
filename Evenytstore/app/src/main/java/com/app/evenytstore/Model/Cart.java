package com.app.evenytstore.Model;

/**
 * Created by Enrique on 09/08/2017.
 */

import android.view.ViewGroup;
import android.widget.TextView;

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
    private ViewGroup container;


    public Cart(AtomPayListAdapter adapter, ViewGroup container){
        total=0;
        this.adapter=adapter;
        this.container=container;
    }

    public double getTotal(){
        return total;
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
            TextView totalView=(TextView) container.findViewById(R.id.total);
            totalView.setText(new DecimalFormat("#.##").format(DecimalHandler.round(total,2)));
        }
        return true;
    }

    public void update(AtomPayListAdapter adapter, ViewGroup container){
        this.adapter = adapter;
        this.container = container;
        TextView totalView=(TextView) container.findViewById(R.id.total);
        totalView.setText(new DecimalFormat("#.##").format(DecimalHandler.round(total,2)));
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
            TextView totalView=(TextView) container.findViewById(R.id.total);
            totalView.setText(new DecimalFormat("#.##").format(DecimalHandler.round(total,2)));

        }

        return true;
    }


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
