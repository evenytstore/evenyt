package com.app.evenytstore.Model;

import com.app.evenytstore.Utility.ImageHandler;

import EvenytServer.model.Customer;

/**
 * Created by Enrique on 26/07/2017.
 */

public class AppSettings {
    public static Customer CURRENT_CUSTOMER;
    public static ImageHandler IMAGE_HANDLER;
    public static int DELIVERY_COST;

    public static String getSerializableId(){
        String serializableId = CURRENT_CUSTOMER.getIdCustomer().replace(':','_');
        return serializableId;
    }
}
