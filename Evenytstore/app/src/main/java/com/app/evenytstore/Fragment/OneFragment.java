package com.app.evenytstore.Fragment;

/**
 * Created by Enrique on 09/08/2017.
 */

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.evenytstore.Adapter.ShelfAdapter;
import com.app.evenytstore.Model.Cart;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;

import java.util.ArrayList;

import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;

public class OneFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShelfAdapter adapter;
    private ArrayList<ProductXSize> productList;
    private String subsectorID;
    private Cart cart;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        productList = new ArrayList<>();

        adapter = new ShelfAdapter(this.getContext(), productList,cart);

        // Inflate the layout for this fragment
        View viewRet=inflater.inflate(R.layout.content_one, container, false);
        recyclerView = (RecyclerView) viewRet.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
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
        ArrayList<ProductXSize> productListAux=(ArrayList<ProductXSize>) Shelf.getHashProductsXSizes().get(getSubsectorID());

        if(productListAux != null){
            for(ProductXSize p : productListAux)
                productList.add(p);
            adapter.notifyDataSetChanged();

        }
    }

    public String getSubsectorID() {
        return subsectorID;
    }

    public void setSubsectorID(String subsectorID) {
        this.subsectorID = subsectorID;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}