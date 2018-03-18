package com.app.evenytstore.Model;

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

/**
 * Created by Enrique on 14/07/2017.
 */

public class ShelfFiller{

    public ShelfFiller(){

    }

    public HashMap<String, Customer> fillCustomers(DatabaseAccess databaseAccess)throws ParseException{
        HashMap<String, Customer> hashCustomers;
        databaseAccess.open();
        hashCustomers = databaseAccess.getAllCustomers();
        databaseAccess.close();
        return hashCustomers;
    }

    public HashMap<String, Brand> fillBrands(DatabaseAccess databaseAccess)throws ParseException{
        HashMap<String, Brand> hashBrands;
        databaseAccess.open();
        hashBrands = databaseAccess.getAllBrands();
        databaseAccess.close();
        return hashBrands;
    }

    public HashMap<String, BrandForm> fillBrandForms(DatabaseAccess databaseAccess)throws ParseException{
        HashMap<String, BrandForm> hashBrandForms;
        databaseAccess.open();
        hashBrandForms = databaseAccess.getAllBrandForms();
        databaseAccess.close();
        return hashBrandForms;
    }

    public HashMap<String, Product> fillProducts(DatabaseAccess databaseAccess)throws ParseException{
        HashMap<String, Product> hashProducts;
        databaseAccess.open();
        hashProducts = databaseAccess.getAllProducts();
        databaseAccess.close();
        return hashProducts;
    }

    public HashMap<String, Category> fillCategories(DatabaseAccess databaseAccess)throws ParseException{
        HashMap<String, Category> hashCategories;
        databaseAccess.open();
        hashCategories = databaseAccess.getAllCategories();
        databaseAccess.close();
        return hashCategories;
    }

    public HashMap<String, Size> fillSizes(DatabaseAccess databaseAccess)throws ParseException{
        HashMap<String, Size> hashSizes;
        databaseAccess.open();
        hashSizes = databaseAccess.getAllSizes();
        databaseAccess.close();
        return hashSizes;
    }

    public HashMap<String, Subcategory> fillSubcategories(DatabaseAccess databaseAccess)throws ParseException{
        HashMap<String, Subcategory> hashSubcategories;
        databaseAccess.open();
        hashSubcategories = databaseAccess.getAllSubcategories();
        databaseAccess.close();
        return hashSubcategories;
    }

    public HashMap<String, List<ProductXSize>> fillProductsXSizes(DatabaseAccess databaseAccess)throws ParseException{
        HashMap<String, List<ProductXSize>> hashProductsXSizes;
        databaseAccess.open();
        hashProductsXSizes = databaseAccess.getAllProductsXSizes();
        databaseAccess.close();
        return hashProductsXSizes;
    }

    public HashMap<String, List<String>> fillCities(String[] districtsLima){
        HashMap<String, List<String>> hashCities = new HashMap<>();
        List<String> districts = new ArrayList<>();
        for(String d : districtsLima)
            districts.add(d);
        hashCities.put("Lima", districts);
        return hashCities;
    }
}
