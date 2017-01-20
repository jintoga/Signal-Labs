package com.dat.signallabs.lab4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PrimitivesGenerator {

    public static ArrayList<Float> getSaw(float A, float T, float tau, float N) {
        ArrayList<Float> saw = new ArrayList<Float>();

        float step = A / (N / 2.f);
        float value = 0.f;
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

    public static ArrayList<Float> getAngle(float A, float T, float tau, float N) {
        ArrayList<Float> angle = new ArrayList<Float>();

        float step = A / (N / 2.f);
        float value = 0.f;
        for (int i = 0; i <= N / 2.f; i++) {
            angle.add(value);
            value += step;
        }

        for (int i = (int) (N / 2.f); i <= N; i++) {
            angle.add(value);
            value -= step;
        }

        return angle;
    }

    public static ArrayList<Float> getLevels(double A, double T, double tau, double N) {
        ArrayList<Float> levels = new ArrayList<Float>();

        for (int i = 0; i <= 51.; i++) {
            levels.add(10.f);
        }

        for (int i = 51; i <= N; i++) {
            levels.add(0.f);
        }

        return levels;
    }

    public static ArrayList<Float> getFromAssets(InputStream inputStream) {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(inputStream));
        ArrayList<Float> doubles = new ArrayList<>();
        String mLine;
        try {
            while ((mLine = reader.readLine()) != null) {
                //process line
                float val = Float.parseFloat(mLine);
                doubles.add(val);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doubles;
    }
}
