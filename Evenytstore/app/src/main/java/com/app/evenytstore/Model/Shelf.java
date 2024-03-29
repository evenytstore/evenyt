package com.app.evenytstore.Model;

import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import EvenytServer.model.Brand;
import EvenytServer.model.BrandForm;
import EvenytServer.model.Category;
import EvenytServer.model.Customer;
import EvenytServer.model.Product;
import EvenytServer.model.ProductXSize;
import EvenytServer.model.Size;
import EvenytServer.model.Subcategory;
import EvenytServer.model.TopProducts;

/**
 * Created by Enrique on 14/07/2017.
 */

public class Shelf {
    private static HashMap<String,Customer> hashCustomers=new HashMap<>(); //Key=Id / Pair=Customer
    private static HashMap<String,Brand> hashBrands=new HashMap<>(); //Key=Code / Pair=Brand
    private static HashMap<String,BrandForm> hashBrandForms=new HashMap<>(); //Key=Code / Pair=BrandForm
    private static HashMap<String,Product> hashProducts=new HashMap<>(); //Key=Code / Pair=Product
    private static HashMap<String,Category> hashCategories=new HashMap<>(); //Key=Code / Pair=Category
    private static HashMap<String,Subcategory> hashSubcategories=new HashMap<>(); //Key=Code / Pair=Subcategory
    private static HashMap<String,List<Product>> categoriesToProducts=new HashMap<>(); //Key=Category Code / Pair=ProductXSize
    private static HashMap<String,List<ProductXSize>> productsToSizes=new HashMap<>(); //Key=Product Code / Pair=ProductXSize
    private static HashMap<String,Size> hashSizes=new HashMap<>(); //Key=Code / Pair=Size
    private static HashMap<String,List<String>> hashCities=new HashMap<>(); //Key=City / Pair=List of Districts
    private static ArrayList<TopProducts> topProducts=new ArrayList<>();

    public static HashMap<String,Customer> getHashCustomers(){return hashCustomers;}
    public static HashMap<String,Brand> getHashBrands(){return hashBrands;}
    public static HashMap<String,BrandForm> getHashBrandForms(){return hashBrandForms;}
    public static HashMap<String,Product> getHashProducts(){return hashProducts;}
    public static HashMap<String,Category> getHashCategories(){return hashCategories;}
    public static HashMap<String,Subcategory> getHashSubcategories(){return hashSubcategories;}
    public static HashMap<String,Size> getHashSizes(){return hashSizes;}
    public static HashMap<String,List<Product>> getCategoriesToProducts(){return categoriesToProducts;}
    public static HashMap<String,List<ProductXSize>> getProductsToSizes(){return productsToSizes;}
    public static HashMap<String,List<String>> getHashCities(){return hashCities;}
    public static ArrayList<TopProducts> getTopProducts() {return topProducts;}

    public static void  ini(DatabaseAccess databaseAccess, String[] districts) throws ParseException {
        //Initialization
        ShelfFiller shelfFiller=new ShelfFiller();

        hashCustomers = shelfFiller.fillCustomers(databaseAccess);
        hashBrands = shelfFiller.fillBrands(databaseAccess);
        hashBrandForms = shelfFiller.fillBrandForms(databaseAccess);
        hashProducts = shelfFiller.fillProducts(databaseAccess);
        hashCategories = shelfFiller.fillCategories(databaseAccess);
        hashSubcategories = shelfFiller.fillSubcategories(databaseAccess);
        hashSizes = shelfFiller.fillSizes(databaseAccess);
        productsToSizes = shelfFiller.fillProductsXSizes(databaseAccess);
        for(Category c : hashCategories.values())
            categoriesToProducts.put(c.getCode(), new ArrayList<Product>());

        for(Product p : hashProducts.values()){
            if(!categoriesToProducts.containsKey(p.getCategoryCode())){
                Log.e("Ded", "Kuk");
                continue;
            }
            categoriesToProducts.get(p.getCategoryCode()).add(p);
        }
        hashCities = shelfFiller.fillCities(districts);
    }


    public static Product getProductByCode(String code){
        return hashProducts.get(code);
    }
    public static Category getCategoryByCode(String code){ return hashCategories.get(code); }
    public static Brand getBrandByCode(String code){ return hashBrands.get(code); }
    public static Size getSizeByCode(String code){
        return hashSizes.get(code);
    }
}
