package com.app.evenytstore.Model;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import EvenytServer.model.Brand;
import EvenytServer.model.Customer;

/**
 * Created by Enrique on 14/07/2017.
 */

public class Shelf {
    private static HashMap<String,Customer> hashCustomers=new HashMap<String, Customer>(); //Key=Id / Pair=Customer
    private static HashMap<String,Brand> hashBrands=new HashMap<String, Brand>(); //Key=Id / Pair=Customer
    private static HashMap<String,List<String>> hashCities=new HashMap<String, List<String>>(); //Key=City / Pair=List of Districts

    public static HashMap<String,Customer> getHashCustomers(){return hashCustomers;}
    public static HashMap<String,Brand> getHashBrands(){return hashBrands;}
    public static HashMap<String,List<String>> getHashCities(){return hashCities;}

    public static void  ini(DatabaseAccess databaseAccess) throws ParseException {
        //Initialization
        ShelfFiller shelfFiller=new ShelfFiller();

        hashCustomers = shelfFiller.fillCustomers(databaseAccess);
        hashBrands = shelfFiller.fillBrands(databaseAccess);
        hashCities = shelfFiller.fillCities();
    }
}
