package com.app.evenytstore.Utility;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Enrique on 09/08/2017.
 */

public class DecimalHandler {

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
