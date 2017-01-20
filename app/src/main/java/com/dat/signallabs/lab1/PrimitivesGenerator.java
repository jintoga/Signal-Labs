package com.dat.signallabs.lab1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DAT on 12/4/2016.
 */

public class PrimitivesGenerator {
    public static List<Double> getSaw(double A, double T, double tau, double N) {
        List<Double> saw = new ArrayList<>();

        double step = A / (N / 2.);
        double value = 0.;
        for (int i = 0; i <= N / 2.; i++) {
            saw.add(value);
            value += step;
        }

        value *= (-1);

        for (int i = (int) (N / 2.); i <= N; i++) {
            saw.add(value);
            value += step;
        }

        return saw;
    }

    public static List<Double> getAngle(double A, double T, double tau, double N) {
        List<Double> angle = new ArrayList<>();

        double step = A / (N / 2.);
        double value = 0.;
        for (int i = 0; i <= N / 2.; i++) {
            angle.add(value);
            value += step;
        }

        for (int i = (int) (N / 2.); i <= N; i++) {
            angle.add(value);
            value -= step;
        }

        return angle;
    }

    public static List<Double> getLevels(double A, double T, double tau, double N) {
        List<Double> levels = new ArrayList<>();

        for (int i = 0; i <= 51.; i++) {
            levels.add(10.);
        }

        for (int i = 51; i <= N; i++) {
            levels.add(0.);
        }

        return levels;
    }

    public static List<Double> getFromAssets(InputStream inputStream) {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        List<Double> doubles = new ArrayList<>();
        String mLine;
        try {
            while ((mLine = reader.readLine()) != null) {
                //process line
                double val = Double.parseDouble(mLine);
                doubles.add(val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doubles;
    }
}
