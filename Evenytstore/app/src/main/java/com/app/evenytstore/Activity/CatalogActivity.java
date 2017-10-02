package com.app.evenytstore.Activity;

/**
 * Created by Enrique on 09/08/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.evenytstore.Adapter.AtomPayListAdapter;
import com.app.evenytstore.Fragment.CatalogFragment;
import com.app.evenytstore.Model.AppSettings;
import com.app.evenytstore.Model.Cart;
import com.app.evenytstore.Model.Item;
import com.app.evenytstore.Model.Shelf;
import com.app.evenytstore.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import EvenytServer.model.Brand;
import EvenytServer.model.Category;
import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;


public class CatalogActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    //private NonSwipeableViewPager viewPager;
    private ViewPager viewPager;
    private PopupWindow popupCart;
    private AtomPayListAdapter adapter;
    ViewGroup container;
    private LayoutInflater layoutInflaterCart;
    private CoordinatorLayout relativeMain;
    public static Cart cart;
    private CatalogFragment sF;
    private double deliveryCost = 0;
    private boolean hasDistrict = false;
    public static String CATEGORY = "cat";
    private int SUMMARY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        //Cart Items

        layoutInflaterCart= (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        container= (ViewGroup) layoutInflaterCart.inflate(R.layout.popup_cart,null);
        adapter = new AtomPayListAdapter(CatalogActivity.this, R.layout.cart_item, new ArrayList<Item>());
        ListView atomPaysListView = (ListView)container.findViewById(R.id.EnterPays_atomPaysList);
        atomPaysListView.setAdapter(adapter);

        if(cart == null)
            cart=new Cart(adapter,container);
        else
            cart.update(adapter,container);
        final EditText textToSearch=(EditText) findViewById(R.id.searcher);

        //Button to clear searcher
        ImageButton clearSearcher=(ImageButton) findViewById(R.id.cleanShelf);
        clearSearcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSearch.getText().clear();
                Shelf.getCategoriesToProducts().remove("100");
                Shelf.getCategoriesToProducts().put("100", new ArrayList<Product>());
                TabLayout.Tab tab = tabLayout.getTabAt(0);
                tab.select();
            }
        });

        textToSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(textToSearch);
                    return true;
                }
                return false;
            }


        });

        //Button to search for product

        ImageButton search= (ImageButton) findViewById(R.id.searchShelfButton);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(textToSearch);
            }
        });

        //Button to order Items products

        Button order= (Button) container.findViewById(R.id.orderItems);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cart.getHashProducts().size() == 0){
                    Dialog dialog = new android.app.AlertDialog.Builder(CatalogActivity.this)
                            .setTitle("Error")
                            .setMessage("El documento no cuenta con productos agregados.")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    return;
                }

                Intent k = new Intent(CatalogActivity.this, CheckoutActivity.class);
                startActivityForResult(k, SUMMARY);
            }
        });

        relativeMain= (CoordinatorLayout) findViewById(R.id.relativeMain);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Intent i = getIntent();
        String category = i.getStringExtra(CatalogActivity.CATEGORY);
        setupViewPager(viewPager, category);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Floating button - cart
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        // fab.setOnClickListener(showPopupWindow());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
                windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
                int deviceWidth = displayMetrics.widthPixels;
                int deviceHeight = displayMetrics.heightPixels;

                popupCart=new PopupWindow(container, deviceWidth - (deviceWidth/100 * 10), deviceHeight - (deviceHeight/100 * 10),true);
                popupCart.showAtLocation(relativeMain, Gravity.CENTER,0,0);
                popupCart.setBackgroundDrawable(new ColorDrawable());
                popupCart.setOutsideTouchable(true);
                popupCart.setFocusable(true);
                //Close button cart

                ImageButton closeButtonCart=(ImageButton) container.findViewById(R.id.btn_close_button);
                closeButtonCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupCart.dismiss();
                    }
                });
            }
        });

        /*for (String district : new ArrayList<String>(Shelf.getDistricts().keySet())){
            Integer clientDistrict =Login.CURRENT_CLIENT.getDistrict();
            String clientDistrictStr = Shelf.getDistrictCostById(clientDistrict).getDistrictName();
            if (district.equals(clientDistrictStr)){
                hasDistrict=true;
                deliveryCost = Shelf.getDistrict(district).getDeliveryCost();
                break;
            }
        }*/
        deliveryCost = AppSettings.DELIVERY_COST;
        TextView deliveryTV = (TextView)container.findViewById(R.id.delivery);
        deliveryTV.setText(String.format("%.2f",deliveryCost));
    }

    //search method

    public void search(EditText textToSearch){
        String keyword = textToSearch.getText().toString().toLowerCase();

        HashMap<String, Product> selects = Shelf.getHashProducts();
        HashMap<String, Brand> brands = Shelf.getHashBrands();

        int k=0;

        ArrayList<Product> resultSearch=new ArrayList<>();

        for(Map.Entry<String,  Product> entry : selects.entrySet()) {
            Product p=entry.getValue();
            if(!Shelf.getProductsToSizes().containsKey(p.getCode()))
                continue;
            String brandName=brands.get(p.getBrandCode()).getName();
            if(p.getName().toLowerCase().contains(keyword) || brandName.toLowerCase().contains(keyword) ||
                    p.getDescription().toLowerCase().contains(keyword))
                resultSearch.add(p);
            k++;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .detach(sF)
                .attach(sF)
                .commit();

        Shelf.getCategoriesToProducts().put("100", resultSearch);

        int position = Shelf.getCategoriesToProducts().size();
        TabLayout.Tab tab1 = tabLayout.getTabAt(position - 1);
        tab1.select();

    }
    //delete item
    public void removeAtomPayOnClickHandler(View v) {
        Item itemToRemove = (Item)v.getTag();
        cart.removeItem(itemToRemove.getCount(),itemToRemove);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(Shelf.getCategoriesToProducts().containsKey("100"))
            Shelf.getCategoriesToProducts().remove("100");
    }


    //less one item
    public void lessAtomPayOnClickHandler(View v) {
        Item itemToRemove = (Item)v.getTag();
        cart.removeItem(1,itemToRemove);
    }

    //add one item
    public void addAtomPayOnClickHandler(View v) {
        Item itemToAdd = (Item)v.getTag();
        cart.addItem(1, itemToAdd.getProductXSize());
    }


    //CatalogFragment for search

    private void setupViewPager(ViewPager viewPager, String selectedCategory) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ArrayList<Category> categories =new ArrayList<Category>();

        for(String categoryCode : Shelf.getCategoriesToProducts().keySet()){
            Category c = Shelf.getCategoryByCode(categoryCode);
            categories.add(c);
        }

        Collections.sort(categories, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        int k=0;
        int selectedIndex=0;

        Category subsec=new Category();
        subsec.setCode("100");
        subsec.setDescription("Searcher");
        subsec.setName("Búsqueda");

        categories.add(subsec);
        Shelf.getCategoriesToProducts().put("100",new ArrayList<Product>());

        for (Category s: categories){
            CatalogFragment catalogFragment =new CatalogFragment();
            catalogFragment.setCart(cart);
            if (s.getCode().compareTo("100")==0){
                sF= catalogFragment;
            }
            if(s.getName().equals(selectedCategory))
                selectedIndex = k;
            k++;
            catalogFragment.setCategoryCode(s.getCode());
            adapter.addFrag(catalogFragment, s.getName());
        }

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(selectedIndex);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SUMMARY) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, new Intent());
                cart = null;
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


    @Override
    public void onBackPressed() {
        cart = null;
        finish();
    }
}
