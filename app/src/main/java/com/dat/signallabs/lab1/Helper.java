package com.dat.signallabs.lab1;

import java.util.List;

/**
 * Created by DAT on 12/4/2016.
 */

public class Helper {
    public static String signalSaver(List<Double> signals) {
        String result = "";
        for (double signal : signals) {
            result += String.valueOf(signal) + "\n";
        }
        return result;
    }
}
