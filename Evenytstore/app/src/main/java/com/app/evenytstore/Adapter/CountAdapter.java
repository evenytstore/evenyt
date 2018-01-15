package com.app.evenytstore.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.evenytstore.R;

import java.util.List;

/**
 * Created by Enrique on 25/08/2017.
 */

public class CountAdapter extends RecyclerView.Adapter<CountAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    private final List<String> items;
    private final OnItemClickListener listener;
    private int initialValue;
    private TextView lastView = null;

    public CountAdapter(int initialValue, List<String> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
        this.initialValue = initialValue;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_count, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void bind(final String item, final OnItemClickListener listener) {
            name.setText(item);
            if(item.equals("Más")){
                if(initialValue > 4){
                    lastView = name;
                    name.setBackgroundResource(R.drawable.rounded_corner_black);
                }
            }else{
                if(Integer.parseInt(item) == initialValue){
                    lastView = name;
                    name.setBackgroundResource(R.drawable.rounded_corner_black);
                }
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    name.setBackgroundResource(R.drawable.rounded_corner_black);
                    if(lastView != null && lastView != name){
                        lastView.setBackgroundResource(R.drawable.rounded_corner);
                    }
                    lastView = name;
                    listener.onItemClick(item);
                }
            });
        }
    }
}