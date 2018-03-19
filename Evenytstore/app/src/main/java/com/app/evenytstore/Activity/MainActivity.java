package com.app.evenytstore.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;


import com.app.evenytstore.Adapter.ExpandableListAdapter;
import com.app.evenytstore.Fragment.TopProductsFragment;
import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.ExpandedMenuModel;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import EvenytServer.model.Category;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader = new ArrayList<>();
    HashMap<ExpandedMenuModel, List<String>> listDataChild = new HashMap<>();
    List<String> categories;

    private int EDIT_CUSTOMER = 1;
    private int SALE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView imageView = findViewById(R.id.peopleImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivityForResult(i2, EDIT_CUSTOMER);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SliderLayout slider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap();
        file_maps.put("A",R.drawable.banner1);
        file_maps.put("B",R.drawable.banner2);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.FitCenterCrop);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            slider.addSlider(textSliderView);
        }

        //slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setDuration(10000);

        Button button = (Button) findViewById(R.id.catalogButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CatalogActivity.class);
                startActivityForResult(i, SALE);
            }
        });

        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        ExpandedMenuModel model = new ExpandedMenuModel();
        model.setIconName("Categorías");
        model.setIconImg(R.drawable.categories);
        listDataHeader.add(model);
        categories = new ArrayList<>();
        for(Category c : Shelf.getHashCategories().values())
            categories.add(c.getName());
        listDataChild.put(model, categories);

        model = new ExpandedMenuModel();
        model.setIconName("Mi cuenta");
        model.setIconImg(R.drawable.people);
        listDataHeader.add(model);
        listDataChild.put(model, new ArrayList<String>());

        model = new ExpandedMenuModel();
        model.setIconName("Mis pedidos");
        model.setIconImg(R.drawable.cart);
        listDataHeader.add(model);
        listDataChild.put(model, new ArrayList<String>());

        model = new ExpandedMenuModel();
        model.setIconName("Términos y condiciones");
        model.setIconImg(R.drawable.heart);
        listDataHeader.add(model);
        listDataChild.put(model, new ArrayList<String>());

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String category = categories.get(i1);
                Intent i2 = new Intent(MainActivity.this, CatalogActivity.class);
                i2.putExtra(CatalogActivity.CATEGORY, category);
                startActivityForResult(i2, SALE);
                //Log.d("DEBUG", "submenu item clicked");
                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");
                String header = listDataHeader.get(i).getIconName();
                if (header.equals("Inicio")) {
                    // Handle the camera action
                } else if (header.equals("Categorías")) {
                } else if (header.equals("Mi cuenta")) {
                    Intent i2 = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivityForResult(i2, EDIT_CUSTOMER);
                } else if (header.equals("Mis pedidos")) {
                    Intent i2 = new Intent(MainActivity.this, OrdersActivity.class);
                    startActivity(i2);
                }else if (header.equals("Términos y condiciones")){
                    Intent i2 = new Intent(MainActivity.this, TermsConditionsActivity.class);
                    startActivity(i2);
                }
                return false;
            }
        });


        mMenuAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, expandableList);
        expandableList.setAdapter(mMenuAdapter);

        viewPager = (ViewPager) findViewById(R.id.viewpagerTopProducts);
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        TopProductsFragment topProductsFragment =new TopProductsFragment();

        adapter.addFrag(topProductsFragment, "Top Products");

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public List<Fragment> getList(){
            return mFragmentList;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.myAccount) {
            Intent i = new Intent(MainActivity.this, EditAddressActivity.class);
            startActivityForResult(i, EDIT_CUSTOMER);
        } else if (id == R.id.myOrders) {
            Intent i = new Intent(MainActivity.this, OrdersActivity.class);
            startActivity(i);
        } else if (id== R.id.terms){
            Intent i = new Intent(MainActivity.this, TermsConditionsActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_CUSTOMER) {
            if (resultCode == RESULT_OK) {
                Dialog dialog = new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("Info")
                        .setMessage("La información se ha actualizado correctamente.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_info).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                return;
            }
        }else if(requestCode == SALE){
            if(resultCode == RESULT_OK){
                String date = AppSettings.SELECTED_SALE.getBundle().getPreferredHour();
                String day = date.substring(0, date.length()-3);
                int hour = Integer.valueOf(date.substring(date.length()-2));
                Dialog dialog = new android.app.AlertDialog.Builder(MainActivity.this)
                        .setTitle("Info")
                        .setMessage("El pedido se ha realizado correctamente y llegará el "+ day +
                                " en el horario de " + hour + ":00 a " + (hour+1) +":00. Para mayor información ingresar a la"
                                +" pestaña Mis pedidos.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_info).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        }
    }
}
