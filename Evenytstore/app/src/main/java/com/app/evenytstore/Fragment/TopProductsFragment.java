package com.app.evenytstore.Fragment;

/**
 * Created by Enrique on 09/08/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.evenytstore.Adapter.TopProductsAdapter;
import com.app.evenytstore.Model.Cart;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.DimensionsHandler;
import com.app.evenytstore.Utility.GridSpacingItemDecoration;

import java.util.ArrayList;

import EvenytServer.model.Product;
import EvenytServer.model.TopProducts;

public class TopProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TopProductsAdapter adapter;
    private ArrayList<Product> productList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        productList = new ArrayList<>();

        adapter = new TopProductsAdapter(this.getContext(), productList);

        // Inflate the layout for this fragment
        View viewRet=inflater.inflate(R.layout.content_top_products, container, false);
        recyclerView = (RecyclerView) viewRet.findViewById(R.id.recycler_view_top_products);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, DimensionsHandler.dpToPx(getResources(), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        // try {
        //   Glide.with(this).load(R.drawable.cover).into((ImageView) viewRet.findViewById(R.id.backdrop));
        //} catch (Exception e) {
        //  e.printStackTrace();
        //}

        return viewRet;
    }
    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */


    /**
     * Adding few albums for testing
     */
    public void updateF(){
        adapter.notifyDataSetChanged();
    }
    public void prepareAlbums() {
        ArrayList<Product> productListAux=new ArrayList<>();

        for (TopProducts s: Shelf.getTopProducts()){
            productListAux.add(Shelf.getProductByCode(s.getCodeProduct()));
        }

        if(productListAux != null){
            productList.addAll(productListAux);
            adapter.notifyDataSetChanged();

        }
    }
}