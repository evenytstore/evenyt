package com.app.evenytstore.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.evenytstore.Activity.CatalogActivity;
import com.app.evenytstore.Activity.CategoriesActivity;
import com.app.evenytstore.R;

import java.util.List;

import EvenytServer.model.Category;

/**
 * Created by Enrique on 24/10/2017.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {

    private List<Category> items;
    private int layoutResourceId;
    private Context context;

    public CategoryAdapter(Context context, int layoutResourceId, List<Category> items){
        super(context,layoutResourceId,items);

        this.items=items;
        this.layoutResourceId=layoutResourceId;
        this.context=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        final CategoryHolder holder = new CategoryHolder();
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder.item = items.get(position);
        holder.name =  (TextView)row.findViewById(R.id.item_name);
        holder.lay = (RelativeLayout)row.findViewById(R.id.lay1) ;

        row.setTag(holder);
        setupItem(holder);

        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("PRODS","PRODS");
                Intent i = new Intent(view.getContext(), CatalogActivity.class);
                i.putExtra(CatalogActivity.CATEGORY, holder.item.getName());
                ((Activity) context).startActivityForResult(i, CategoriesActivity.CATALOG);
            }
        });

        return row;
    }

    private void setupItem(CategoryHolder holder){
        holder.name.setText(holder.item.getName());
    }

    public static class CategoryHolder{
        Category item;
        TextView name;
        RelativeLayout lay;
    }
}
