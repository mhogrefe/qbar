package jas.poly;

import jas.arith.BigRational;
import jas.structure.RingFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Random;

public class GenSolvablePolynomialRing_BigRational extends GenSolvablePolynomialRing<BigRational> {
    private final RelationTable<BigRational> table;

    private final GenSolvablePolynomial<BigRational> ZERO;

    private final GenSolvablePolynomial<BigRational> ONE;

    public GenSolvablePolynomialRing_BigRational(RingFactory<BigRational> cf, int n, TermOrder t, String[] v) {
        super(cf, n, t, v);
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
    public boolean isAssociative() {
        GenSolvablePolynomial<BigRational> Xi, Xj, Xk, p, q;
        for (int i = 0; i < nvar; i++) {
            Xi = univariate(i);
            for (int j = i + 1; j < nvar; j++) {
                Xj = univariate(j);
                for (int k = j + 1; k < nvar; k++) {
                    Xk = univariate(k);
                    p = Xk.multiply(Xj).multiply(Xi);
                    q = Xk.multiply(Xj.multiply(Xi));
                    if (!p.equals(q)) {
                        return false;
                    }
                }
            }
        }
        return coFac.isAssociative();
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
        if (nvar == 1) {
            return random(5, n, n, 0.7f, rnd);
        }
        return random(5, n, 3, 0.3f, rnd);
    }

    @Override
    public GenSolvablePolynomial<BigRational> random(int l, int d) {
        return random(17, l, d, 1.0f, random);
    }

    @Override
    GenSolvablePolynomial<BigRational> random(int k, int l, int d, float q, Random rnd) {
        GenSolvablePolynomial<BigRational> r = getZERO(); //.clone();
        ExpVector e;
        BigRational a;
        // add random coeffs and exponents
        for (int i = 0; i < l; i++) {
            e = ExpVector.EVRAND(nvar, d, q, rnd);
            a = coFac.random(k, rnd);
            r = (GenSolvablePolynomial<BigRational>) r.sum(a, e);
            // somewhat inefficient but clean
        }
        return r;
    }

    @Override
    public GenSolvablePolynomial<BigRational> univariate(int i) {
        return super.univariate(i);
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
    public GenSolvablePolynomial<BigRational> univariate(int i, long e) {
        return super.univariate(i, e);
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
    public GenSolvablePolynomial<BigRational> univariate(int modv, int i, long e) {
        return super.univariate(modv, i, e);
    }

    /**
     * Extend variables. Used e.g. in module embedding. Extend number of
     * variables by length(vn). New variables commute with the exiting variables.
     *
     * @param vn names for extended variables.
     * @return extended polynomial ring factory.
     */
    @Override
    public GenSolvablePolynomialRing<BigRational> extend(String[] vn) {
        GenPolynomialRing<BigRational> pfac = super.extend(vn);
        GenSolvablePolynomialRing<BigRational> spfac = new GenSolvablePolynomialRing_BigRational(pfac.coFac, pfac.nvar,
                pfac.tord, pfac.vars);
        spfac.table.extend(this.table);
        return spfac;
    }


    /**
     * Contract variables. Used e.g. in module embedding. Contract number of
     * variables by i.
     *
     * @param i number of variables to remove.
     * @return contracted solvable polynomial ring factory.
     */
    @Override
    public GenSolvablePolynomialRing<BigRational> contract(int i) {
        GenPolynomialRing<BigRational> pfac = super.contract(i);
        GenSolvablePolynomialRing<BigRational> spfac = new GenSolvablePolynomialRing_BigRational(pfac.coFac, pfac.nvar,
                pfac.tord, pfac.vars);
        spfac.table.contract(this.table);
        return spfac;
    }
}
