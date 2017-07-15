package com.app.evenytstore.Model;

import java.text.ParseException;
import java.util.HashMap;

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
}
