package jas.poly;

import java.util.Random;

/**
 * ExpVectorLong implements exponent vectors for polynomials using arrays of
 * long as storage unit. This class is used by ExpVector internally, there is no
 * need to use this class directly.
 *
 * @author Heinz Kredel
 */
public final class ExpVector implements Comparable<ExpVector> {
    public final long val;

    /**
     * Constructor for ExpVector.
     */
    public ExpVector() {
        val = 0;
    }

    /**
     * Constructor for ExpVector. Sets exponent i to e.
     *
     * @param e exponent to be set.
     */
    public ExpVector(long e) {
        val = e;
    }

    @Override
    public int compareTo(ExpVector V) {
        return this.invLexCompareTo(V);
    }

    protected int invLexCompareTo(ExpVector V) {
        return Long.compare(val, V.val);
    }

    public int invLexCompareTo(ExpVector V, int begin, int end) {
        return Long.compare(val, V.val);
    }

    public int invGradCompareTo(ExpVector V) {
        return Long.compare(val, V.val);
    }

    public int invGradCompareTo(ExpVector V, int begin, int end) {
        return Long.compare(val, V.val);
    }

    public int revInvLexCompareTo(ExpVector V) {
        return Long.compare(val, V.val);
    }

    public int revInvLexCompareTo(ExpVector V, int begin, int end) {
        return Long.compare(val, V.val);
    }

    public int revInvGradCompareTo(ExpVector V) {
        return Long.compare(val, V.val);
    }

    public int revInvGradCompareTo(ExpVector V, int begin, int end) {
        return Long.compare(val, V.val);
    }

    public int invWeightCompareTo(long[][] w, ExpVector V) {
        return Long.compare(val, V.val);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExpVector expVector = (ExpVector) o;

        return val == expVector.val;

    }

    @Override
    public int hashCode() {
        return (int) (val ^ (val >>> 32));
    }

    public static ExpVector EVRAND(int r, long k, float q, Random rnd) {
        long[] w = new long[r];
        long e;
        float f;
        for (int i = 0; i < w.length; i++) {
            f = rnd.nextFloat();
            if (f > q) {
                e = 0;
            } else {
                e = rnd.nextLong() % k;
                if (e < 0) {
                    e = -e;
                }
            }
            w[i] = e;
        }
        return new ExpVector(w[0]);
        //return new ExpVector( w );
    }

    public static int EVILCP(ExpVector U, ExpVector V) {
        return U.invLexCompareTo(V);
    }

    public static int EVILCP(ExpVector U, ExpVector V, int begin, int end) {
        return U.invLexCompareTo(V, begin, end);
    }

    public static int EVIGLC(ExpVector U, ExpVector V) {
        return U.invGradCompareTo(V);
    }

    public static int EVIGLC(ExpVector U, ExpVector V, int begin, int end) {
        return U.invGradCompareTo(V, begin, end);
    }

    public static int EVRILCP(ExpVector U, ExpVector V) {
        return U.revInvLexCompareTo(V);
    }

    public static int EVRILCP(ExpVector U, ExpVector V, int begin, int end) {
        return U.revInvLexCompareTo(V, begin, end);
    }

    public static int EVRIGLC(ExpVector U, ExpVector V) {
        return U.revInvGradCompareTo(V);
    }

    public static int EVRIGLC(ExpVector U, ExpVector V, int begin, int end) {
        return U.revInvGradCompareTo(V, begin, end);
    }

    public static int EVIWLC(long[][] w, ExpVector U, ExpVector V) {
        return U.invWeightCompareTo(w, V);
    }
}
