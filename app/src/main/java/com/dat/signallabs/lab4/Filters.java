package com.dat.signallabs.lab4;

import java.util.ArrayList;
import java.util.List;

public class Filters {

    public static ArrayList<Float> cardioFilter(List<Float> signal) {
        ArrayList<Float> f = new ArrayList<Float>();

        double ratio = signal.size() / FourierTransform.FREQUENCY;

        for (int i = 0; i < signal.size(); i++) {
            if (24. * ratio <= i && i <= 26. * ratio) {
                f.add(signal.get((int) ((24. - 1) * ratio)));
            } else if ((360. - 50.) * ratio <= i && i <= (360. - 49.) * ratio) {
                f.add(signal.get((int) ((49. - 1) * ratio)));
            } else {
                f.add(signal.get(i));
            }
        }

        return f;
    }

    public static ArrayList<Float> reoFilter(List<Float> signal) {
        ArrayList<Float> f = new ArrayList<Float>();

        double ratio = signal.size() / FourierTransform.FREQUENCY;

        for (int i = 0; i < signal.size(); i++) {
            if (22. * ratio <= i && i <= (360. - 22.) * ratio) {
                f.add(signal.get((int) ((21. - 1) * ratio)));
            } else {
                f.add(signal.get(i));
            }
        }

        return f;
    }

    public static ArrayList<Float> veloFilter(List<Float> signal) {
        ArrayList<Float> f = new ArrayList<Float>();

        double ratio = signal.size() / FourierTransform.FREQUENCY;

        for (int i = 0; i < signal.size(); i++) {
            if (0. * ratio <= i && i <= 1. * ratio) {
                f.add(signal.get((int) (2. * ratio)));
            } else if ((360. - 1.) * ratio <= i && i <= 360. * ratio) {
                f.add(signal.get((int) ((360. - 1) * ratio)));
            } else {
                f.add(signal.get(i));
            }
        }

        return f;
    }

    public static ArrayList<Float> standartFilter(List<Float> signal) {
        ArrayList<Float> f = new ArrayList<Float>();

        double ratio = signal.size() / FourierTransform.FREQUENCY;

        for (int i = 0; i < signal.size(); i++) {
            if (0. * ratio <= i && i <= 50. * ratio) {
                f.add(signal.get(i));
            } else {
                f.add(new Float(0));
            }
        }

        return f;
    }
}
