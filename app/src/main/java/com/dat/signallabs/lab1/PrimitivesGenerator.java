package com.dat.signallabs.lab1;

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
}
