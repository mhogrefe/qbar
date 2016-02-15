package jas.poly;

import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.math.BigInteger;
import java.util.Random;

public class GenSolvablePolynomialRing<C extends RingElem<C>> extends GenPolynomialRing<C> {
    public final RelationTable<C> table;

    private final GenSolvablePolynomial<C> ZERO;

    private final GenSolvablePolynomial<C> ONE;

    public GenSolvablePolynomialRing(RingFactory<C> cf, int n, TermOrder t, String[] v) {
        super(cf, n, t, v);
        table = new RelationTable<>(this);
        ZERO = new GenSolvablePolynomial<>(this);
        C coeff = coFac.getONE();
        //evzero = ExpVector.create(nvar); // from super
        ONE = new GenSolvablePolynomial<>(this, coeff, evzero);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (!(other instanceof GenSolvablePolynomialRing)) {
            return false;
        }
        GenSolvablePolynomialRing<C> oring = null;
        try {
            oring = (GenSolvablePolynomialRing<C>) other;
        } catch (ClassCastException ignored) {
        }
        // do a super.equals( )
        if (!super.equals(other)) {
            return false;
        }
        // check same base relations
        return table.equals(oring.table);
    }

    @Override
    public int hashCode() {
        int h;
        h = super.hashCode();
        h = 37 * h + table.hashCode();
        return h;
    }

    @Override
    public GenSolvablePolynomial<C> getZERO() {
        return ZERO;
    }

    @Override
    public GenSolvablePolynomial<C> getONE() {
        return ONE;
    }

    @Override
    public GenSolvablePolynomial<C> fromInteger(long a) {
        return new GenSolvablePolynomial<>(this, coFac.fromInteger(a), evzero);
    }

    @Override
    public GenSolvablePolynomial<C> fromInteger(BigInteger a) {
        return new GenSolvablePolynomial<>(this, coFac.fromInteger(a), evzero);
    }

    @Override
    public GenSolvablePolynomial<C> random(int n, Random rnd) {
        if (nvar == 1) {
            return random(5, n, n, 0.7f, rnd);
        }
        return random(5, n, 3, 0.3f, rnd);
    }

    @Override
    public GenSolvablePolynomial<C> random(int l, int d) {
        return random(17, l, d, 1.0f, random);
    }

    @Override
    GenSolvablePolynomial<C> random(int k, int l, int d, float q, Random rnd) {
        GenSolvablePolynomial<C> r = getZERO(); //.clone();
        long e;
        C a;
        // add random coeffs and exponents
        for (int i = 0; i < l; i++) {
            e = GenPolynomial.EVRAND(nvar, d, q, rnd);
            a = coFac.random(k, rnd);
            r = (GenSolvablePolynomial<C>) r.sum(a, e);
        }
        return r;
    }

    @Override
    public GenSolvablePolynomial<C> univariate(int i) {
        return (GenSolvablePolynomial<C>) super.univariate(i);
    }

    /**
     * Generate univariate solvable polynomial in a given variable with given
     * exponent.
     *
     * @param i the index of the variable.
     * @param e the exponent of the variable.
     * @return X_i^e as solvable univariate polynomial.
     */
    @Override
    public GenSolvablePolynomial<C> univariate(int i, long e) {
        return (GenSolvablePolynomial<C>) super.univariate(i, e);
    }

    /**
     * Generate univariate solvable polynomial in a given variable with given
     * exponent.
     *
     * @param modv number of module variables.
     * @param i    the index of the variable.
     * @param e    the exponent of the variable.
     * @return X_i^e as solvable univariate polynomial.
     */
    @Override
    public GenSolvablePolynomial<C> univariate(int modv, int i, long e) {
        return (GenSolvablePolynomial<C>) super.univariate(modv, i, e);
    }
}
