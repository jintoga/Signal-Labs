package com.dat.signallabs.lab4;

import com.dat.signallabs.lab1.Helper;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.util.Pair;

public class FourierTransform {

    public static final int CARDIO = 0;
    public static final int REO = 1;
    public static final int VELO = 2;
    public static final int SAW = 3;
    public static final int ANGLE = 4;
    public static final int LEVELS = 5;

    public static final float FREQUENCY = 360.0f;

    private static Complex j = new Complex(0, 1);

    private static Complex w(double k, double N) {
        if (k == 0) {
            return new Complex(1.);
        }
        return new Complex(Math.cos((2. * Math.PI * k) / N), 0).subtract(
            j.multiply(Math.sin((2. * Math.PI * k) / N)));
    }

    public static ArrayList<Complex> DPF(ArrayList<Complex> f) {
        ArrayList<Complex> c = new ArrayList<Complex>();
        double N = f.size();
        for (int k = 0; k < N; k++) {
            Complex ck = new Complex(0, 0);

            for (int i = 0; i < N; i++) {
                Complex tmp = w(i, N).pow(k).multiply(f.get(i));    // c += w^i * f
                ck = ck.add(tmp);
            }
            c.add(ck.divide(N));
        }
        return c;
    }

    public static List<Float> filter(List<Float> c, int function) {

        switch (function) {
            case CARDIO:
                return Filters.cardioFilter(c);
            case REO:
                return Filters.reoFilter(c);
            case VELO:
                return Filters.veloFilter(c);
            case SAW:
                return Filters.standartFilter(c);
            case ANGLE:
                return Filters.standartFilter(c);
            case LEVELS:
                return Filters.standartFilter(c);
        }

        return c;
    }

    public static ArrayList<Complex> ODPF(ArrayList<Complex> c) {
        ArrayList<Complex> f = new ArrayList<Complex>();
        double N = c.size();

        for (int k = 0; k < N; k++) {
            Complex ck = new Complex(0, 0);

            for (int i = 0; i < N; i++) {
                Complex tmp = c.get(i).divide(w(k, N).pow(i));    // f += c / w^i
                ck = ck.add(tmp);
            }
            f.add(ck);
        }
        return f;
    }

    public static ArrayList<Complex> BPF(ArrayList<Complex> f, boolean rationing) {

        ArrayList<Complex> fNew = new ArrayList<Complex>();

        int P = Helper.getPowerOfTwo(f.size());
        int N = (int) Math.pow(2, P);

        System.out.println("2^" + P + "= " + N + "; size = " + f.size());

        for (int i = 0; i < N; i++) {
            fNew.add(f.get(i));
        }

        int n = (int) (Math.log(N) / Math.log(2));

        for (int i = 0; i < N; i++) {
            int j = Helper.reverseBit(i, n);
            fNew.set(j, f.get(i));
        }

        for (int s = 1; s <= P; s++) {
            double m = Math.pow(2, s);
            Complex wm = j.multiply(2. * Math.PI).divide(m).exp();
            //Complex wm = w(1., m);

            for (int k = 0; k < N; k += m) {
                Complex w = new Complex(1.);

                for (int j = 0; j < m / 2; j++) {

                    int id1 = (k + j);
                    int id2 = (int) (id1 + m / 2);

                    if (id2 >= fNew.size()) {
                        break;
                    }

                    System.out.println("id1 = " + id1 + " id2 = " + id2);

                    Complex u = fNew.get(id1);
                    Complex t = fNew.get(id2).multiply(w);

                    fNew.set(id1, u.add(t));
                    fNew.set(id2, u.subtract(t));

                    w = w.multiply(wm);
                }
            }
        }

        if (rationing) {
            for (int i = 0; i < N; i++) {
                fNew.set(i, fNew.get(i).divide(N));
            }
        }

        return fNew;
    }

    public static ArrayList<Complex> BPFn(ArrayList<Complex> f) {

        ArrayList<Complex> fNew = Helper.listCopy(f);

        int P = Helper.getPowerOfTwo(f.size());

        Pair lm = getLandM(P, f.size());

        int M = (int) lm.getSecond();
        int L = (int) lm.getFirst();
        int N = L * M;

        System.out.println("Size = "
            + f.size()
            + "; (2^P = L) * M = "
            + "(2^"
            + P
            + " = "
            + L
            + ") * "
            + M
            + " = "
            + M * L);

        for (int i = 0; i < M; i++) {
            ArrayList<Complex> bpfnStep = getEveryL(fNew, L, M, i);
            fNew = returnEveryL(fNew, BPF(bpfnStep, false), L, M, i);
        }

        ArrayList<Complex> bpfn = new ArrayList<>();
        for (int i = 0; i < M * L; i++) {
            bpfn.add(new Complex(0.));
        }

        for (int s = 0; s < M; s++) {
            for (int r = 0; r < L; r++) {
                bpfn.set(r + s * L, new Complex(0.));
                for (int m = 0; m < M; m++) {
                    bpfn.set(r + s * L, bpfn.get(r + s * L)
                        .add(fNew.get(m + r * M)
                            .multiply(j.multiply(-1)
                                .multiply(2. * Math.PI * m * (r + s * L) / N)
                                .exp())));
                }
            }
        }

        for (int i = 0; i < N; i++) {
            bpfn.set(i, bpfn.get(i).divide(N));
        }

        return bpfn;
    }

    private static Pair getLandM(int P, int size) {
        int M = 1;
        int eps = size;
        int L = 1;

        do {
            int l = (int) Math.pow(2, P);
            int m = 1;
            do {
                if (eps > size - l * m) {
                    eps = size - l * m;
                    M = m;
                    L = l;
                }
                m++;
            } while (m * l < size && m <= 11);

            P--;
        } while (P >= 2);

        return new Pair<Integer, Integer>(L, M);
    }

    private static ArrayList<Complex> getEveryL(ArrayList<Complex> f, int L, int M, int start) {
        ArrayList<Complex> fNew = new ArrayList<Complex>();

        for (int i = 0; i < L; i++) {
            fNew.add(f.get(i * M + start));
        }

        return fNew;
    }

    private static ArrayList<Complex> returnEveryL(ArrayList<Complex> f, ArrayList<Complex> returnF,
        int L, int M, int start) {
        ArrayList<Complex> fNew = Helper.listCopy(f);

        for (int i = 0; i < L; i++) {
            fNew.set(i * M + start, returnF.get(i));
        }

        return fNew;
    }
}

