package de.mwiktorin.tankbuch;

import java.text.DecimalFormat;

public class Utils {

    public static String round(double x, String pattern) {
        DecimalFormat f = new DecimalFormat(pattern);
        return f.format(x);
    }

    public static String round(String x, String pattern) {
        return round(Double.parseDouble(x), pattern);
    }

    public static String round(double x) {
        return round(x,  "0.00");
    }

    public static String round(String x) {
        return round(Double.parseDouble(x));
    }

    public static String roundThree(double x) {
        return round(x, "0.000");
    }

    public static String roundThree(String x) {
        return roundThree(Double.parseDouble(x));
    }
}
