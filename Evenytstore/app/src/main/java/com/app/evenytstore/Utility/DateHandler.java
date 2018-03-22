package com.app.evenytstore.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Enrique on 20/07/2017.
 */

public class DateHandler {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
    private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Calendar toDate(String value){
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static Calendar toDateHour(String value){
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf4.parse(value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String toString(Calendar value){
        return sdf.format(value.getTime());
    }

    public static Calendar toDateUSA(String value){
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf2.parse(value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static Calendar toDateServer(String value){
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf3.parse(value));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    public static String toStringUSA(Calendar value){
        return sdf2.format(value.getTime());
    }
}
