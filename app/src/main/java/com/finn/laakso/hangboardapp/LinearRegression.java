package com.finn.laakso.hangboardapp;

import java.util.ArrayList;

/**
 *  The LinearRegression class performs a simple linear regression
 */

public class LinearRegression {
    private final float intercept, slope;
    private final float r2;
    private final float svar0, svar1;


    public LinearRegression(ArrayList<Float> x, ArrayList<Float> y) {

        // Security check, for linear regressiont to work x and y must be same size
        // If not line is same as x-axis.
        if (x.size() != y.size() ) {
            intercept = 0;
            slope = 0;
            r2 = 1;
            svar0 = 1;
            svar1 = 1;
            return;

        }
        int n = x.size();

        // first pass
        float sumx = 0.0f, sumy = 0.0f, sumx2 = 0.0f;
        for (int i = 0; i < n; i++) {
            sumx  += x.get(i);
            sumx2 += x.get(i)*x.get(i);
            sumy  += y.get(i);
        }
        float xbar = sumx / n;
        float ybar = sumy / n;

        // second pass: compute summary statistics
        float xxbar = 0.0f, yybar = 0.0f, xybar = 0.0f;
        for (int i = 0; i < n; i++) {
            xxbar += (x.get(i) - xbar) * (x.get(i) - xbar);
            yybar += (y.get(i) - ybar) * (y.get(i) - ybar);
            xybar += (x.get(i) - xbar) * (y.get(i) - ybar);
        }
        slope  = xybar / xxbar;
        intercept = ybar - slope * xbar;

        // more statistical analysis
        float rss = 0.0f;      // residual sum of squares
        float ssr = 0.0f;      // regression sum of squares
        for (int i = 0; i < n; i++) {
            double fit = slope*x.get(i) + intercept;
            rss += (fit - y.get(i)) * (fit - y.get(i));
            ssr += (fit - ybar) * (fit - ybar);
        }

        int degreesOfFreedom = n-2;
        r2    = ssr / yybar;
        float svar  = rss / degreesOfFreedom;
        svar1 = svar / xxbar;
        svar0 = svar/n + xbar*xbar*svar1;
    }


    public float intercept() {
        return intercept;
    }

    public float slope() {
        return slope;
    }

    public float R2() {
        return r2;
    }

    public float interceptStdErr() {
        return (float) Math.sqrt(svar0);
    }


    public float slopeStdErr() {
        return (float) Math.sqrt(svar1);
    }


    public float predict(float x) {
        return slope*x + intercept;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("%.2f n + %.2f", slope(), intercept()));
        s.append("  (R^2 = " + String.format("%.3f", R2()) + ")");
        return s.toString();
    }

}

