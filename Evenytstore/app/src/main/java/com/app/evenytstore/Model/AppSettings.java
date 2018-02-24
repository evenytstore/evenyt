package com.app.evenytstore.Model;

import com.app.evenytstore.Utility.ImageHandler;

import EvenytServer.model.Customer;
import EvenytServer.model.Promotions;
import EvenytServer.model.Sale;

/**
 * Created by Enrique on 26/07/2017.
 */

public class AppSettings {
    public static Customer CURRENT_CUSTOMER;
    public static ImageHandler IMAGE_HANDLER;
    public static Promotions CURRENT_PROMOTION;
    public static Sale SELECTED_SALE;
    public static int DELIVERY_COST;
    public static int FREE_DELIVERY_PRICE;
    public static int MIN_SALE_COST;
    public static int MIN_FIRST_DISCOUNT;
    public static int MIN_SECOND_DISCOUNT;
    public static int MAX_RETRIES = 10;

    public static String getSerializableId(){
        String serializableId = CURRENT_CUSTOMER.getIdCustomer().replace(':','_');
        return serializableId;
    }
}
