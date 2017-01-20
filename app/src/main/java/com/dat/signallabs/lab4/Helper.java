package com.dat.signallabs.lab4;

import android.graphics.Color;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by DAT on 1/21/2017.
 */

public class Helper {
    public static String signalSaver(List<Float> signals) {
        String result = "";
        for (double signal : signals) {
            result += String.valueOf(signal) + "\n";
        }
        return result;
    }

    public static List<Entry> getSeries(List<Float> signals, float multiplier) {
        int i = 0;
        List<Entry> series = new ArrayList<>();
        for (Float signal : signals) {
            Entry entry = new Entry(((float) i) / multiplier, signal);
            series.add(entry);
            i++;
        }
        return series;
    }

    public static void drawSignal(LineChart graphView, List<Entry> series, String label) {

        LineDataSet lineDataSet = new LineDataSet(series, label);

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(ColorTemplate.getHoloBlue());
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setFillColor(ColorTemplate.getHoloBlue());
        lineDataSet.setDrawCircles(false);

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        graphView.setData(lineData);
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

    public static ArrayList<Float> complexToDouble(List<Complex> list, char param) {
        ArrayList<Float> newList = new ArrayList<>();
        for (Complex number : list) {
            switch (param) {
                case 'a':
                    newList.add((float) number.abs());
                    break;
                case 'r':
                    newList.add((float) number.getReal());
                    break;
                case 'i':
                    newList.add((float) number.getImaginary());
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

    public static List<Double> siganlLoader(String s) {
        List<Double> signals = new ArrayList<Double>();

        return null;
    }
}
