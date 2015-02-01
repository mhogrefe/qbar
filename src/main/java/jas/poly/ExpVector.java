package jas.poly;

import jas.structure.AbelianGroupElem;
import jas.structure.AbelianGroupFactory;

import java.util.Random;

public abstract class ExpVector implements AbelianGroupElem<ExpVector> {
    int hash = 0;

    ExpVector() {
        hash = 0;
    }

    public static ExpVector create(int n) {
        return new ExpVectorLong(n);
    }

    public static ExpVector create(int n, int i, long e) {
        return new ExpVectorLong(n, i, e);
    }

    private static ExpVector create(long[] v) {
        return new ExpVectorLong(v);
    }

    public AbelianGroupFactory<ExpVector> factory() {
        throw new UnsupportedOperationException("no factory implemented for ExpVector");
    }

    protected abstract ExpVector copy();

    public abstract long getVal(int i);

    protected abstract void setVal(long e);

    public abstract int length();

    public abstract ExpVector extend(int i, long e);

    public abstract ExpVector contract(int i, int len);

    public abstract ExpVector combine(ExpVector V);

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
        return s.toString();
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

    public int indexVar(String x, String... vars) {
        for (int i = 0; i < length(); i++) {
            if (x.equals(vars[i])) {
                return length() - i - 1;
            }
        }
        return -1;
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

    public abstract ExpVector abs();

    public abstract ExpVector negate();

    public abstract ExpVector sum(ExpVector V);

    public abstract ExpVector subtract(ExpVector V);

    public ExpVector subst(long d) {
        ExpVector V = this.copy();
        V.setVal(d);
        return V;
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

    public abstract int signum();

    public abstract long maxDeg();

    public abstract ExpVector lcm(ExpVector V);

    public abstract ExpVector gcd(ExpVector V);

    public abstract int[] dependencyOnVariables();

    public abstract boolean multipleOf(ExpVector V);

    @Override
    public int compareTo(ExpVector V) {
        return this.invLexCompareTo(V);
    }

    public static int EVILCP(ExpVector U, ExpVector V) {
        return U.invLexCompareTo(V);
    }

    protected abstract int invLexCompareTo(ExpVector V);

    public static int EVILCP(ExpVector U, ExpVector V, int begin, int end) {
        return U.invLexCompareTo(V, begin, end);
    }

    protected abstract int invLexCompareTo(ExpVector V, int begin, int end);

    public static int EVIGLC(ExpVector U, ExpVector V) {
        return U.invGradCompareTo(V);
    }

    protected abstract int invGradCompareTo(ExpVector V);

    public static int EVIGLC(ExpVector U, ExpVector V, int begin, int end) {
        return U.invGradCompareTo(V, begin, end);
    }

    protected abstract int invGradCompareTo(ExpVector V, int begin, int end);

    public static int EVRILCP(ExpVector U, ExpVector V) {
        return U.revInvLexCompareTo(V);
    }

    protected abstract int revInvLexCompareTo(ExpVector V);

    public static int EVRILCP(ExpVector U, ExpVector V, int begin, int end) {
        return U.revInvLexCompareTo(V, begin, end);
    }

    protected abstract int revInvLexCompareTo(ExpVector V, int begin, int end);

    public static int EVRIGLC(ExpVector U, ExpVector V) {
        return U.revInvGradCompareTo(V);
    }

    protected abstract int revInvGradCompareTo(ExpVector V);

    public static int EVRIGLC(ExpVector U, ExpVector V, int begin, int end) {
        return U.revInvGradCompareTo(V, begin, end);
    }

    protected abstract int revInvGradCompareTo(ExpVector V, int begin, int end);

    public static int EVIWLC(long[][] w, ExpVector U, ExpVector V) {
        return U.invWeightCompareTo(w, V);
    }

    protected abstract int invWeightCompareTo(long[][] w, ExpVector V);
}
