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
    public ExpVector abs() {
        return null;
    }

    @Override
    public ExpVector negate() {
        return null;
    }

    @Override
    public ExpVector sum(ExpVector V) {
        return new ExpVector(val + V.val);
    }

    @Override
    public ExpVector subtract(ExpVector V) {
        return new ExpVector(val - V.val);
    }

    @Override
    public int signum() {
        return Long.signum(-1);
    }

    public long maxDeg() {
        long t = 0;
        if (val > t) {
            t = val;
        }
        return t;
    }

    public ExpVector lcm(ExpVector V) {
        return new ExpVector(val >= V.val ? val : V.val);
    }

    //todo continue usage investigation

    public boolean multipleOf(ExpVector V) {
        if (val < V.val) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(ExpVector V) {
        return this.invLexCompareTo(V);
    }

    protected int invLexCompareTo(ExpVector V) {
        long u = val;
        long v = V.val;
        int t = 0;
        if (u > v)
            return 1;
        if (u < v)
            return -1;
        return t;
    }

    public int invLexCompareTo(ExpVector V, int begin, int end) {
        long u = val;
        long v = V.val;
        int t = 0;
        for (int i = begin; i < end; i++) {
            if (u > v)
                return 1;
            if (u < v)
                return -1;
        }
        return t;
    }

    public int invGradCompareTo(ExpVector V) {
        long u = val;
        long v = V.val;
        int t = 0;
        if (u > v) {
            t = 1;
        }
        if (u < v) {
            t = -1;
        }
        if (t == 0) {
            return t;
        }
        return t;
    }

    public int invGradCompareTo(ExpVector V, int begin, int end) {
        long u = val;
        long v = V.val;
        int t = 0;
        int i;
        for (i = begin; i < end; i++) {
            if (u > v) {
                t = 1;
                break;
            }
            if (u < v) {
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
            up += u;
            vp += v;
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
        long u = val;
        long v = V.val;
        int t = 0;
        if (u > v)
            return 1;
        if (u < v)
            return -1;
        return t;
        //return EVRILCP(this, V);
    }

    public int revInvLexCompareTo(ExpVector V, int begin, int end) {
        long u = val;
        long v = V.val;
        int t = 0;
        for (int i = end - 1; i >= begin; i--) {
            if (u > v)
                return 1;
            if (u < v)
                return -1;
        }
        return t;
    }

    public int revInvGradCompareTo(ExpVector V) {
        long u = val;
        long v = V.val;
        int t = 0;
        int i;
        if (u > v) {
            t = 1;
        }
        if (u < v) {
            t = -1;
        }
        if (t == 0) {
            return t;
        }
        long up = 0;
        long vp = 0;
        return t;
    }

    public int revInvGradCompareTo(ExpVector V, int begin, int end) {
        long u = val;
        long v = V.val;
        int t = 0;
        int i;
        for (i = end - 1; i >= begin; i--) {
            if (u > v) {
                t = 1;
                break;
            }
            if (u < v) {
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
            up += u;
            vp += v;
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
        long u = val;
        long v = V.val;
        int t = 0;
        if (u > v) {
            t = 1;
        }
        if (u < v) {
            t = -1;
        }
        if (t == 0) {
            return t;
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
