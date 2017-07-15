package com.app.evenytstore.Model;

import java.text.ParseException;
import java.util.HashMap;

/**
 * Created by Enrique on 14/07/2017.
 */

public class Shelf {
    private static HashMap<String,Customer> hashCustomers=new HashMap<String, Customer>(); //Key=Id / Pair=Customer

    public static HashMap<String,Customer> getHashCustomers(){return hashCustomers;}

    public static void  ini(DatabaseAccess databaseAccess) throws ParseException {
        //Initialization
        ShelfFiller shelfFiller=new ShelfFiller();

        hashCustomers = shelfFiller.fillCustomers(databaseAccess);
    }
}
