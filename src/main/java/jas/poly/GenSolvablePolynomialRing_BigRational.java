package jas.poly;

import jas.arith.BigRational;
import jas.structure.RingFactory;

import java.math.BigInteger;
import java.util.Random;

public class GenSolvablePolynomialRing_BigRational extends GenSolvablePolynomialRing<BigRational> {
    private final RelationTable<BigRational> table;

    private final GenSolvablePolynomial<BigRational> ZERO;

    private final GenSolvablePolynomial<BigRational> ONE;

    public GenSolvablePolynomialRing_BigRational(RingFactory<BigRational> cf, String v) {
        super(cf, v);
        table = new RelationTable<>(this);
        ZERO = new GenSolvablePolynomial<>(this);
        BigRational coeff = coFac.getONE();
        //evzero = ExpVector.create(nvar); // from super
        ONE = new GenSolvablePolynomial<>(this, coeff, evzero);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (!(other instanceof GenSolvablePolynomialRing)) {
            return false;
        }
        GenSolvablePolynomialRing<BigRational> oring = null;
        try {
            oring = (GenSolvablePolynomialRing<BigRational>) other;
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
    public GenSolvablePolynomial<BigRational> getZERO() {
        return ZERO;
    }

    @Override
    public GenSolvablePolynomial<BigRational> getONE() {
        return ONE;
    }

    @Override
    public GenSolvablePolynomial<BigRational> fromInteger(long a) {
        return new GenSolvablePolynomial<>(this, coFac.fromInteger(a), evzero);
    }

    @Override
    public GenSolvablePolynomial<BigRational> fromInteger(BigInteger a) {
        return new GenSolvablePolynomial<>(this, coFac.fromInteger(a), evzero);
    }

    @Override
    public GenSolvablePolynomial<BigRational> random(int n, Random rnd) {
        return random(5, n, n, 0.7f, rnd);
    }

    @Override
    public GenSolvablePolynomial<BigRational> random(int l, int d) {
        return random(17, l, d, 1.0f, random);
    }

    @Override
    GenSolvablePolynomial<BigRational> random(int k, int l, int d, float q, Random rnd) {
        GenSolvablePolynomial<BigRational> r = getZERO(); //.clone();
        long e;
        BigRational a;
        // add random coeffs and exponents
        for (int i = 0; i < l; i++) {
            e = GenPolynomial.EVRAND(d, q, rnd);
            a = coFac.random(k, rnd);
            r = (GenSolvablePolynomial<BigRational>) r.sum(a, e);
            // somewhat inefficient but clean
        }
        return r;
    }

    @Override
    public GenSolvablePolynomial<BigRational> univariateA(int i) {
        return super.univariateA(i);
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
    public GenSolvablePolynomial<BigRational> univariateB(int i, long e) {
        return super.univariateB(i, e);
    }
}
