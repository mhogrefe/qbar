package jas.poly;

import jas.structure.AbelianGroupElem;

import java.util.Random;

/**
 * ExpVectorLong implements exponent vectors for polynomials using arrays of
 * long as storage unit. This class is used by ExpVector internally, there is no
 * need to use this class directly.
 *
 * @author Heinz Kredel
 */
public final class ExpVector implements AbelianGroupElem<ExpVector> {
    int hash = 0;

    /**
     * The data structure is an array of longs.
     */
    /*package*/private final long[] val;

    /**
     * Constructor for ExpVector.
     */
    public ExpVector() {
        val = new long[1];
    }

    /**
     * Constructor for ExpVector. Sets exponent i to e.
     *
     * @param e exponent to be set.
     */
    public ExpVector(long e) {
        val = new long[1];
        val[0] = e;
    }

    public long getVal() {
        return val[0];
    }

    public int length() {
        return 1;
    }

    @Override
    public boolean equals(Object B) {
        if (!(B instanceof ExpVector)) {
            return false;
        }
        ExpVector b = (ExpVector) B;
        int t = this.invLexCompareTo(b);
        return (0 == t);
    }

    @Override
    public ExpVector abs() {
        return null;
    }

    @Override
    public ExpVector negate() {
        return null;
    }

    @Override
    public ExpVector sum(ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        long[] w = new long[1];
        w[0] = u[0] + v[0];
        return new ExpVector(w[0]);
    }

    @Override
    public ExpVector subtract(ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        long[] w = new long[1];
        w[0] = u[0] - v[0];
        return new ExpVector(w[0]);
    }

    @Override
    public int signum() {
        int t = 0;
        if (val[0] < 0) {
            return -1;
        }
        if (val[0] > 0) {
            t = 1;
        }
        return t;
    }

    public long maxDeg() {
        long t = 0;
        if (val[0] > t) {
            t = val[0];
        }
        return t;
    }

    public ExpVector lcm(ExpVector V) {
        long[] w = new long[1];
        w[0] = (val[0] >= V.val[0] ? val[0] : V.val[0]);
        return new ExpVector(w[0]);
    }

    //todo continue usage investigation

    public boolean multipleOf(ExpVector V) {
        if (val[0] < V.val[0]) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(ExpVector V) {
        return this.invLexCompareTo(V);
    }

    protected int invLexCompareTo(ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        int t = 0;
        for (int i = 0; i < u.length; i++) {
            if (u[i] > v[i])
                return 1;
            if (u[i] < v[i])
                return -1;
        }
        return t;
    }

    public int invLexCompareTo(ExpVector V, int begin, int end) {
        long[] u = val;
        long[] v = V.val;
        int t = 0;
        for (int i = begin; i < end; i++) {
            if (u[i] > v[i])
                return 1;
            if (u[i] < v[i])
                return -1;
        }
        return t;
    }

    public int invGradCompareTo(ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        int t = 0;
        int i;
        for (i = 0; i < u.length; i++) {
            if (u[i] > v[i]) {
                t = 1;
                break;
            }
            if (u[i] < v[i]) {
                t = -1;
                break;
            }
        }
        if (t == 0) {
            return t;
        }
        long up = 0;
        long vp = 0;
        for (int j = i; j < u.length; j++) {
            up += u[j];
            vp += v[j];
        }
        if (up > vp) {
            t = 1;
        } else {
            if (up < vp) {
                t = -1;
            }
        }
        return t;
    }

    public int invGradCompareTo(ExpVector V, int begin, int end) {
        long[] u = val;
        long[] v = V.val;
        int t = 0;
        int i;
        for (i = begin; i < end; i++) {
            if (u[i] > v[i]) {
                t = 1;
                break;
            }
            if (u[i] < v[i]) {
                t = -1;
                break;
            }
        }
        if (t == 0) {
            return t;
        }
        long up = 0;
        long vp = 0;
        for (int j = i; j < end; j++) {
            up += u[j];
            vp += v[j];
        }
        if (up > vp) {
            t = 1;
        } else {
            if (up < vp) {
                t = -1;
            }
        }
        return t;
    }

    public int revInvLexCompareTo(ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        int t = 0;
        for (int i = u.length - 1; i >= 0; i--) {
            if (u[i] > v[i])
                return 1;
            if (u[i] < v[i])
                return -1;
        }
        return t;
        //return EVRILCP(this, V);
    }

    public int revInvLexCompareTo(ExpVector V, int begin, int end) {
        long[] u = val;
        long[] v = V.val;
        int t = 0;
        for (int i = end - 1; i >= begin; i--) {
            if (u[i] > v[i])
                return 1;
            if (u[i] < v[i])
                return -1;
        }
        return t;
    }

    public int revInvGradCompareTo(ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        int t = 0;
        int i;
        for (i = u.length - 1; i >= 0; i--) {
            if (u[i] > v[i]) {
                t = 1;
                break;
            }
            if (u[i] < v[i]) {
                t = -1;
                break;
            }
        }
        if (t == 0) {
            return t;
        }
        long up = 0;
        long vp = 0;
        for (int j = i; j >= 0; j--) {
            up += u[j];
            vp += v[j];
        }
        if (up > vp) {
            t = 1;
        } else {
            if (up < vp) {
                t = -1;
            }
        }
        return t;
    }

    public int revInvGradCompareTo(ExpVector V, int begin, int end) {
        long[] u = val;
        long[] v = V.val;
        int t = 0;
        int i;
        for (i = end - 1; i >= begin; i--) {
            if (u[i] > v[i]) {
                t = 1;
                break;
            }
            if (u[i] < v[i]) {
                t = -1;
                break;
            }
        }
        if (t == 0) {
            return t;
        }
        long up = 0;
        long vp = 0;
        for (int j = i; j >= begin; j--) {
            up += u[j];
            vp += v[j];
        }
        if (up > vp) {
            t = 1;
        } else {
            if (up < vp) {
                t = -1;
            }
        }
        return t;
    }

    public int invWeightCompareTo(long[][] w, ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        int t = 0;
        int i;
        for (i = 0; i < u.length; i++) {
            if (u[i] > v[i]) {
                t = 1;
                break;
            }
            if (u[i] < v[i]) {
                t = -1;
                break;
            }
        }
        if (t == 0) {
            return t;
        }
        for (long[] wk : w) {
            long up = 0;
            long vp = 0;
            for (int j = i; j < u.length; j++) {
                up += wk[j] * u[j];
                vp += wk[j] * v[j];
            }
            if (up > vp) {
                return 1;
            } else if (up < vp) {
                return -1;
            }
        }
        return t;
    }

    public static ExpVector create() {
        return new ExpVector();
    }

    public static ExpVector create(long e) {
        return new ExpVector(e);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(val[0]);
    }

    public boolean isZERO() {
        return (0 == this.signum());
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
        return create(w[0]);
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
