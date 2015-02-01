package jas.poly;

import java.util.Arrays;

/**
 * ExpVectorLong implements exponent vectors for polynomials using arrays of
 * long as storage unit. This class is used by ExpVector internally, there is no
 * need to use this class directly.
 *
 * @author Heinz Kredel
 * @see ExpVector
 */

public final class ExpVectorLong extends ExpVector
/*implements AbelianGroupElem<ExpVectorLong>*/ {


    /**
     * The data structure is an array of longs.
     */
    /*package*/private final long[] val;


    /**
     * Constructor for ExpVector.
     *
     * @param n length of exponent vector.
     */
    public ExpVectorLong(int n) {
        this(new long[n]);
    }


    /**
     * Constructor for ExpVector. Sets exponent i to e.
     *
     * @param n length of exponent vector.
     * @param i index of exponent to be set.
     * @param e exponent to be set.
     */
    public ExpVectorLong(int n, int i, long e) {
        this(new long[n]);
        val[i] = e;
    }


    /**
     * Constructor for ExpVector. Sets val.
     *
     * @param v internal representation array.
     */
    public ExpVectorLong(long[] v) {
        super();
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
    @Override
    protected ExpVectorLong copy() {
        long[] w = new long[val.length];
        System.arraycopy(val, 0, w, 0, val.length);
        return new ExpVectorLong(w);
    }


    /**
     * Get the exponent at position i.
     *
     * @param i position.
     * @return val[i].
     */
    @Override
    public long getVal(int i) {
        return val[i];
    }

    @Override
    protected void setVal(long e) {
        val[0] = e;
        hash = 0;
    }

    @Override
    public int length() {
        return val.length;
    }

    @Override
    public ExpVectorLong extend(int i, long e) {
        long[] w = new long[val.length + i];
        System.arraycopy(val, 0, w, i, val.length);
        if (0 >= i) {
            throw new IllegalArgumentException("i " + i + " <= j " + 0 + " invalid");
        }
        w[0] = e;
        return new ExpVectorLong(w);
    }

    @Override
    public ExpVectorLong contract(int i, int len) {
        if (i + len > val.length) {
            throw new IllegalArgumentException("len " + len + " > val.len " + val.length);
        }
        long[] w = new long[len];
        System.arraycopy(val, i, w, 0, len);
        return new ExpVectorLong(w);
    }

    @Override
    public ExpVectorLong combine(ExpVector V) {
        if (V == null || V.length() == 0) {
            return this;
        }
        ExpVectorLong Vl = (ExpVectorLong) V;
        if (val.length == 0) {
            return Vl;
        }
        long[] w = new long[val.length + Vl.val.length];
        System.arraycopy(val, 0, w, 0, val.length);
        System.arraycopy(Vl.val, 0, w, val.length, Vl.val.length);
        return new ExpVectorLong(w);
    }

    @Override
    public String toString() {
        return super.toString() + ":long";
    }

    @Override
    public boolean equals(Object B) {
        if (!(B instanceof ExpVectorLong)) {
            return false;
        }
        ExpVectorLong b = (ExpVectorLong) B;
        int t = this.invLexCompareTo(b);
        return (0 == t);
    }

    @Override
    public ExpVectorLong abs() {
        long[] u = val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            if (u[i] >= 0L) {
                w[i] = u[i];
            } else {
                w[i] = -u[i];
            }
        }
        return new ExpVectorLong(w);
    }

    @Override
    public ExpVectorLong negate() {
        long[] u = val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            w[i] = -u[i];
        }
        return new ExpVectorLong(w);
    }

    @Override
    public ExpVectorLong sum(ExpVector V) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            w[i] = u[i] + v[i];
        }
        return new ExpVectorLong(w);
    }

    @Override
    public ExpVectorLong subtract(ExpVector V) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            w[i] = u[i] - v[i];
        }
        return new ExpVectorLong(w);
    }

    @Override
    public ExpVectorLong subst(long d) {
        ExpVectorLong V = this.copy();
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

    @Override
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

    @Override
    public ExpVectorLong lcm(ExpVector V) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            w[i] = (u[i] >= v[i] ? u[i] : v[i]);
        }
        return new ExpVectorLong(w);
    }

    @Override
    public ExpVectorLong gcd(ExpVector V) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
        long[] w = new long[u.length];
        for (int i = 0; i < u.length; i++) {
            w[i] = (u[i] <= v[i] ? u[i] : v[i]);
        }
        return new ExpVectorLong(w);
    }

    @Override
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

    @Override
    public boolean multipleOf(ExpVector V) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
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

    @Override
    protected int invLexCompareTo(ExpVector V) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
        int t = 0;
        for (int i = 0; i < u.length; i++) {
            if (u[i] > v[i])
                return 1;
            if (u[i] < v[i])
                return -1;
        }
        return t;
    }

    @Override
    public int invLexCompareTo(ExpVector V, int begin, int end) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
        int t = 0;
        for (int i = begin; i < end; i++) {
            if (u[i] > v[i])
                return 1;
            if (u[i] < v[i])
                return -1;
        }
        return t;
    }

    @Override
    public int invGradCompareTo(ExpVector V) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
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

    @Override
    public int invGradCompareTo(ExpVector V, int begin, int end) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
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

    @Override
    public int revInvLexCompareTo(ExpVector V) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
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

    @Override
    public int revInvLexCompareTo(ExpVector V, int begin, int end) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
        int t = 0;
        for (int i = end - 1; i >= begin; i--) {
            if (u[i] > v[i])
                return 1;
            if (u[i] < v[i])
                return -1;
        }
        return t;
    }

    @Override
    public int revInvGradCompareTo(ExpVector V) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
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

    @Override
    public int revInvGradCompareTo(ExpVector V, int begin, int end) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
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

    @Override
    public int invWeightCompareTo(long[][] w, ExpVector V) {
        long[] u = val;
        long[] v = ((ExpVectorLong) V).val;
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

}
