package com.app.evenytstore.Activity;

/**
 * Created by Enrique on 24/10/2017.
 */

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.app.evenytstore.Adapter.CategoryAdapter;
import com.app.evenytstore.Adapter.CheckoutAdapter;
import com.app.evenytstore.Model.Item;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.app.evenytstore.Utility.DecimalHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import EvenytServer.model.Category;


public class CategoriesActivity extends AppCompatActivity {

    public static int CATALOG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        CategoryAdapter adapter = new CategoryAdapter(CategoriesActivity.this, R.layout.category_item, new ArrayList<Category>());

        ListView categoriesList = (ListView)findViewById(R.id.categoriesItems);
        categoriesList.setAdapter(adapter);

        ArrayList<Category> categories =new ArrayList();

        for(Category c : Shelf.getHashCategories().values())
            categories.add(c);

        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        for(Category c : categories)
            adapter.add(c);
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CATALOG) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, new Intent());
                finish();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
