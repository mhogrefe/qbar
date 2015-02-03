package jas.poly;

import jas.structure.AbelianGroupElem;

import java.util.Arrays;
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
     *
     * @param n length of exponent vector.
     */
    public ExpVector(int n) {
        this(new long[n]);
    }


    /**
     * Constructor for ExpVector. Sets exponent i to e.
     *
     * @param n length of exponent vector.
     * @param i index of exponent to be set.
     * @param e exponent to be set.
     */
    public ExpVector(int n, int i, long e) {
        this(new long[n]);
        val[i] = e;
    }


    /**
     * Constructor for ExpVector. Sets val.
     *
     * @param v internal representation array.
     */
    public ExpVector(long[] v) {
        hash = 0;
        if (v == null) {
            throw new IllegalArgumentException("null val not allowed");
        }
        val = Arrays.copyOf(v, v.length); // > Java-5
    }

    /**
     * Clone this.
     *
     * @see java.lang.Object#clone()
     */
    protected ExpVector copy() {
        long[] w = new long[val.length];
        System.arraycopy(val, 0, w, 0, val.length);
        return new ExpVector(w);
    }


    /**
     * Get the exponent at position i.
     *
     * @param i position.
     * @return val[i].
     */
    public long getVal(int i) {
        return val[i];
    }

    protected void setVal(long e) {
        val[0] = e;
        hash = 0;
    }

    public int length() {
        return val.length;
    }

    public ExpVector extend(int i, long e) {
        long[] w = new long[val.length + i];
        System.arraycopy(val, 0, w, i, val.length);
        if (0 >= i) {
            throw new IllegalArgumentException("i " + i + " <= j " + 0 + " invalid");
        }
        w[0] = e;
        return new ExpVector(w);
    }

    public ExpVector contract(int i, int len) {
        if (i + len > val.length) {
            throw new IllegalArgumentException("len " + len + " > val.len " + val.length);
        }
        long[] w = new long[len];
        System.arraycopy(val, i, w, 0, len);
        return new ExpVector(w);
    }

    public ExpVector combine(ExpVector V) {
        if (V == null || V.length() == 0) {
            return this;
        }
        ExpVector Vl = V;
        if (val.length == 0) {
            return Vl;
        }
        long[] w = new long[val.length + Vl.val.length];
        System.arraycopy(val, 0, w, 0, val.length);
        System.arraycopy(Vl.val, 0, w, val.length, Vl.val.length);
        return new ExpVector(w);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("(");
        for (int i = 0; i < length(); i++) {
            s.append(getVal(i));
            if (i < length() - 1) {
                s.append(",");
            }
        }
        s.append(")");
        return s.toString() + ":long";
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
        long[] u = val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            if (u[i] >= 0L) {
                w[i] = u[i];
            } else {
                w[i] = -u[i];
            }
        }
        return new ExpVector(w);
    }

    @Override
    public ExpVector negate() {
        long[] u = val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            w[i] = -u[i];
        }
        return new ExpVector(w);
    }

    @Override
    public ExpVector sum(ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            w[i] = u[i] + v[i];
        }
        return new ExpVector(w);
    }

    @Override
    public ExpVector subtract(ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            w[i] = u[i] - v[i];
        }
        return new ExpVector(w);
    }

    public ExpVector subst(long d) {
        ExpVector V = this.copy();
        V.setVal(d);
        return V;
    }

    @Override
    public int signum() {
        int t = 0;
        long[] u = val;
        for (long anU : u) {
            if (anU < 0) {
                return -1;
            }
            if (anU > 0) {
                t = 1;
            }
        }
        return t;
    }

    public long maxDeg() {
        long t = 0;
        long[] u = val;
        for (long anU : u) {
            if (anU > t) {
                t = anU;
            }
        }
        return t;
    }

    public ExpVector lcm(ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            w[i] = (u[i] >= v[i] ? u[i] : v[i]);
        }
        return new ExpVector(w);
    }

    public ExpVector gcd(ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            w[i] = (u[i] <= v[i] ? u[i] : v[i]);
        }
        return new ExpVector(w);
    }

    public int[] dependencyOnVariables() {
        long[] u = val;
        int l = 0;
        for (long anU : u) {
            if (anU > 0) {
                l++;
            }
        }
        int[] dep = new int[l];
        if (l == 0) {
            return dep;
        }
        int j = 0;
        for (int i = 0; i < u.length; i++) {
            if (u[i] > 0) {
                dep[j] = i;
                j++;
            }
        }
        return dep;
    }

    public boolean multipleOf(ExpVector V) {
        long[] u = val;
        long[] v = V.val;
        for (int i = 0; i < u.length; i++) {
            if (u[i] < v[i]) {
                return false;
            }
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

    public static ExpVector create(int n) {
        return new ExpVector(n);
    }

    public static ExpVector create(int n, int i, long e) {
        return new ExpVector(n, i, e);
    }

    private static ExpVector create(long[] v) {
        return new ExpVector(v);
    }

    public String toString(String[] vars) {
        StringBuilder s = new StringBuilder();
        boolean pit;
        int r = length();
        if (r != vars.length) {
            //
            return toString();
        }
        if (r == 0) {
            return s.toString();
        }
        long vi;
        for (int i = r - 1; i > 0; i--) {
            vi = getVal(i);
            if (vi != 0) {
                s.append(vars[r - 1 - i]);
                if (vi != 1) {
                    s.append("^").append(vi);
                }
                pit = false;
                for (int j = i - 1; j >= 0; j--) {
                    if (getVal(j) != 0) {
                        pit = true;
                    }
                }
                if (pit) {
                    s.append(" * ");
                }
            }
        }
        vi = getVal(0);
        if (vi != 0) {
            s.append(vars[r - 1]);
            if (vi != 1) {
                s.append("^").append(vi);
            }
        }
        return s.toString();
    }

    public static String varsToString(String[] vars) {
        if (vars == null) {
            return "null";
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < vars.length; i++) {
            s.append(vars[i]);
            if (i < vars.length - 1) {
                s.append(",");
            }
        }
        return s.toString();
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            for (int i = 0; i < length(); i++) {
                hash = hash << 4 + getVal(i);
            }
            if (hash == 0) {
                hash = 1;
            }
        }
        return hash;
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
        return create(w);
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
