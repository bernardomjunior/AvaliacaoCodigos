package com.android.starter.programs;

/*
The Computer Language Benchmarks Game
https://salsa.debian.org/benchmarksgame-team/benchmarksgame/

contributed by Ziad Hatahet
based on the Go entry by K P anonymous
*/

import java.text.DecimalFormat;

public class Spectral {
    private static final DecimalFormat formatter = new DecimalFormat("#.000000000");
    private static final int NCPU = Runtime.getRuntime().availableProcessors();

    public void runCode(final int n) {
        try{
            final double[] u = new double[n];
            for (int i = 0; i < n; i++)
                u[i] = 1.0;
            final double[] v = new double[n];
            for (int i = 0; i < 10; i++) {
                aTimesTransp(v, u);
                aTimesTransp(u, v);
            }

            double vBv = 0.0, vv = 0.0;
            for (int i = 0; i < n; i++) {
                final double vi = v[i];
                vBv += u[i] * vi;
                vv += vi * vi;
            }
            System.out.println(formatter.format(Math.sqrt(vBv / vv)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void aTimesTransp(double[] v, double[] u) throws InterruptedException {
        final double[] x = new double[u.length];
        final Thread[] t = new Thread[NCPU];
        for (int i = 0; i < NCPU; i++) {
            t[i] = new Times(x, i * v.length / NCPU, (i + 1) * v.length / NCPU, u, false);
            t[i].start();
        }
        for (int i = 0; i < NCPU; i++)
            t[i].join();

        for (int i = 0; i < NCPU; i++) {
            t[i] = new Times(v, i * v.length / NCPU, (i + 1) * v.length / NCPU, x, true);
            t[i].start();
        }
        for (int i = 0; i < NCPU; i++)
            t[i].join();
    }

    private final static class Times extends Thread {
        private final double[] v, u;
        private final int ii, n;
        private final boolean transpose;

        public Times(double[] v, int ii, int n, double[] u, boolean transpose) {
            this.v = v;
            this.u = u;
            this.ii = ii;
            this.n = n;
            this.transpose = transpose;
        }

        @Override
        public void run() {
            final int ul = u.length;
            for (int i = ii; i < n; i++) {
                double vi = 0.0;
                for (int j = 0; j < ul; j++) {
                    if (transpose)
                        vi += u[j] / a(j, i);
                    else
                        vi += u[j] / a(i, j);
                }
                v[i] = vi;
            }
        }

        private static int a(int i, int j) {
            return (i + j) * (i + j + 1) / 2 + i + 1;
        }
    }
}