package com.app.evenytstore.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Enrique on 20/07/2017.
 */

public class DateHandler {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static Calendar toDate(String value){
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String toString(Calendar value){
        return sdf.format(value.getTime());
    }
}
