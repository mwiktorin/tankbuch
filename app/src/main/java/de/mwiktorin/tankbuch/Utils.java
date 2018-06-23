package de.mwiktorin.tankbuch;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class Utils {

    public static String round(double x, String pattern) {
        DecimalFormat f = new DecimalFormat(pattern);
        return f.format(x);
    }

    public static String round(double x) {
        return round(x,  "0.00");
    }

    public static String roundThree(double x) {
        return round(x, "0.000");
    }

    public static double parse(String x) throws ParseException {
        return NumberFormat.getInstance().parse(x).doubleValue();
    }
}
