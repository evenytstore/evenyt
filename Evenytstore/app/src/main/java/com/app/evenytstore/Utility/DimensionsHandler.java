package com.app.evenytstore.Utility;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Enrique on 25/08/2017.
 */

public class DimensionsHandler {
    public static int dpToPx(Resources r, int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
