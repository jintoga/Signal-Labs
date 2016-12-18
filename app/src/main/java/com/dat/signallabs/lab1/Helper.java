package com.dat.signallabs.lab1;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.complex.Complex;

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

    public static LineGraphSeries<DataPoint> getSeries(List<Double> signals, double multiplier) {
        int i = 0;
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        for (Double signal : signals) {
            DataPoint dataPoint = new DataPoint(((double) i) / multiplier, signal);
            series.appendData(dataPoint, false, signals.size());
            i++;
        }
        return series;
    }

    public static void drawSignal(GraphView graphView, LineGraphSeries<DataPoint> series) {
        graphView.addSeries(series);
    }

    public static List<Double> stringToDoubles(String data) {
        List<Double> doubles = new ArrayList<>();
        String[] split = data.split(System.getProperty("line.separator"));
        for (String a : split) {
            doubles.add(Double.valueOf(a));
        }
        return doubles;
    }

    public static ArrayList<Complex> doubleToComplex(List<Double> list) {
        ArrayList<Complex> newList = new ArrayList<>();
        for (Double number : list) {
            newList.add(new Complex(number));
        }
        return newList;
    }

    public static ArrayList<Double> complexToDouble(List<Complex> list, char param) {
        ArrayList<Double> newList = new ArrayList<>();
        for (Complex number : list) {
            switch (param) {
                case 'a':
                    newList.add(number.abs());
                    break;
                case 'r':
                    newList.add(number.getReal());
                    break;
                case 'i':
                    newList.add(number.getImaginary());
                    break;

                default:
                    break;
            }
        }
        return newList;
    }

    public static int reverseBit(int x, int n) {
        int b = 0;
        int i = 0;
        while (x != 0) {
            b <<= 1;
            b |= (x & 1);
            x >>= 1;
            i++;
        }
        if (i < n) {
            b <<= n - i;
        }
        return b;
    }

    public static int getPowerOfTwo(int number) {
        int n = 1;
        while (Math.pow(2, n) <= number) {
            n++;
        }
        return n - 1;
    }

    public static <T> ArrayList<T> listCopy(List<T> toCopy) {
        ArrayList<T> copy = new ArrayList<>();
        for (T element : toCopy) {
            copy.add(element);
        }
        return copy;
    }
}
