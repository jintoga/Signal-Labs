package com.dat.signallabs.lab2;

import android.graphics.Color;
import android.util.Log;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by DAT on 1/15/2017.
 */

public class Helper {

    public static double FREQUENCY = 360.0;            //частота дискретизации в Гц

    public static ArrayList<Double> siganlLoader(String fileName) {
        ArrayList<Double> signals = new ArrayList<Double>();
        File f = new File(fileName);
        BufferedReader fin;
        int i = 0;
        try {
            fin = new BufferedReader(new FileReader(f));
            String line;

            while ((line = String.valueOf(fin.read())) != null && i <= Math.pow(2, 17) + 44) {
                i++;
                signals.add(Double.parseDouble(line));
            }
            fin.close();
        } catch (Exception e1) {
            System.out.println("i = " + i + "\n\n\n");
            e1.printStackTrace();
        } catch (OutOfMemoryError e1) {
            System.out.println("i = " + i + "\n\n\n");
            e1.printStackTrace();
        }
        return signals;
    }

    public static void wavHeadLoader(String fileName) {
        File f = new File(fileName);
        BufferedReader fin;
        try {
            fin = new BufferedReader(new FileReader(f));
            String line;
            for (int i = 0; i < 44; i++) {
                line = String.valueOf(fin.read());
                System.out.println(line + "; " + line);
            }
            fin.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
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
        if (i < n) b <<= n - i;
        return b;
    }

    public static String signalSaver(List<Double> signals) {
        String result = "";
        for (double signal : signals) {
            result += String.valueOf(signal) + "\n";
        }
        return result;
    }

    public static ArrayList<Complex> doubleToComplex(ArrayList<Double> list) {
        ArrayList<Complex> newList = new ArrayList<Complex>();
        for (Double number : list) {
            newList.add(new Complex(number));
        }
        return newList;
    }

    public static List<Float> complexToDouble(List<Complex> list, char param) {
        ArrayList<Float> newList = new ArrayList<Float>();
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

        Log.d("SIZE", "SIZE: " + series.size());
        LineDataSet lineDataSet = new LineDataSet(series, label);

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(ColorTemplate.getHoloBlue());
        lineDataSet.setCircleColor(Color.WHITE);
        lineDataSet.setLineWidth(0.5f);
        lineDataSet.setFillColor(ColorTemplate.getHoloBlue());
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setCubicIntensity(0);
        lineDataSet.setValueTextSize(0);

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        graphView.setHardwareAccelerationEnabled(true);
        graphView.setData(lineData);
    }

    public static int getPowerOfTwo(int number) {
        int n = 1;
        while (Math.pow(2, n) <= number) {
            n++;
        }
        return n - 1;
    }

    public static <T> ArrayList<T> listCopy(ArrayList<T> toCopy) {
        ArrayList<T> copy = new ArrayList<>();
        for (T element : toCopy) {
            copy.add(element);
        }
        return copy;
    }

    public static ArrayList<Complex> getIdealSignal(int N) {
        ArrayList<Complex> signals = new ArrayList<>();
        signals.add(new Complex(1000));
        for (int i = 1; i < N; i++) {
            signals.add(new Complex(0));
        }

        return signals;
    }

    public static ArrayList<Complex> genSawSignal() {
        double A = 1000;
        double T = 0.01;
        int N = (int) (Math.pow(2., 15.) / 10.) + 1;
        int periods = 5; // original: 10
        double EPSILON = 0.00000001;

        ArrayList<Complex> signals = new ArrayList<>();
        double step = (T) / N;
        double s = 0;
        double dA = 2 * A / T;
        int i = 0;
        for (int p = 0; p < periods; p++) {
            double curS = 0;
            for (int t = 0; t < N; t++) {
                Complex tmp;
                if (curS < T / 2 - step + EPSILON) {
                    tmp = new Complex(dA * curS);
                } else if (curS > T / 2 + step - EPSILON) {
                    tmp = new Complex(dA * curS - 2 * A);
                } else {
                    tmp = new Complex(-A);
                }
                signals.add(tmp);
                s += step;
                curS += step;
                i++;
            }
        }
        return signals;
    }
}
