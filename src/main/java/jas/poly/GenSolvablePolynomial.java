package jas.poly;

import jas.structure.RingElem;

import java.util.Map;
import java.util.SortedMap;

/**
 * GenSolvablePolynomial generic solvable polynomials implementing RingElem.
 * n-variate ordered solvable polynomials over C. Objects of this class are
 * intended to be immutable. The implementation is based on TreeMap respectively
 * SortedMap from exponents to coefficients by extension of GenPolybomial. Only
 * the coefficients are modeled with generic types, the exponents are fixed to
 * ExpVector with long entries (this will eventually be changed in the future).
 *
 * @param <C> coefficient type
 * @author Heinz Kredel
 */

public class GenSolvablePolynomial<C extends RingElem<C>> extends GenPolynomial<C> {
    private final GenSolvablePolynomialRing<C> ring;

    public GenSolvablePolynomial(GenSolvablePolynomialRing<C> r) {
        super(r);
        ring = r;
    }

    public GenSolvablePolynomial(GenSolvablePolynomialRing<C> r, C c, ExpVector e) {
        this(r);
        if (c != null && !c.isZERO()) {
            val.put(e, c);
        }
    }

    GenSolvablePolynomial(GenSolvablePolynomialRing<C> r, SortedMap<ExpVector, C> v) {
        this(r);
        val.putAll(v); // assume no zero coefficients
    }

    @Override
    public GenSolvablePolynomialRing<C> factory() {
        return ring;
    }

    /**
     * Clone this GenSolvablePolynomial.
     *
     * @see java.lang.Object#clone()
     */
    @Override
    public GenSolvablePolynomial<C> copy() {
        return new GenSolvablePolynomial<>(ring, this.val);
    }

    /**
     * Comparison with any other object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object B) {
        return B instanceof GenSolvablePolynomial && super.equals(B);
    }

    /**
     * GenSolvablePolynomial multiplication. Product with coefficient ring
     * element.
     *
     * @param b coefficient.
     * @return this*b, where * is coefficient multiplication.
     */
    @Override
    public GenSolvablePolynomial<C> multiply(C b) {
        GenSolvablePolynomial<C> Cp = ring.getZERO().copy();
        if (b == null || b.isZERO()) {
            return Cp;
        }
        Map<ExpVector, C> Cm = Cp.val; //getMap();
        Map<ExpVector, C> Am = val;
        for (Map.Entry<ExpVector, C> y : Am.entrySet()) {
            ExpVector e = y.getKey();
            C a = y.getValue();
            C c = a.multiply(b);
            if (!c.isZERO()) {
                Cm.put(e, c);
            }
        }
        return Cp;
    }

    /**
     * GenSolvablePolynomial multiplication. Left product with coefficient ring
     * element.
     *
     * @param b coefficient.
     * @return b*this, where * is coefficient multiplication.
     */
    GenSolvablePolynomial<C> multiplyLeft(C b) {
        GenSolvablePolynomial<C> Cp = ring.getZERO().copy();
        if (b == null || b.isZERO()) {
            return Cp;
        }
        Map<ExpVector, C> Cm = Cp.val; //getMap();
        Map<ExpVector, C> Am = val;
        for (Map.Entry<ExpVector, C> y : Am.entrySet()) {
            ExpVector e = y.getKey();
            C a = y.getValue();
            C c = b.multiply(a);
            if (!c.isZERO()) {
                Cm.put(e, c);
            }
        }
        return Cp;
    }

    /**
     * GenSolvablePolynomial monic, i.e. leadingCoefficient == 1. If leadingCoefficient
     * is not invertible returns this unmodified.
     *
     * @return monic(this).
     */
    @Override
    public GenSolvablePolynomial<C> monic() {
        if (this.isZERO()) {
            return this;
        }
        C lc = leadingBaseCoefficient();
        //System.out.println("lc = "+lc);
        if (!lc.isUnit()) {
            return this;
        }
        try {
            C lm = lc.inverse();
            //System.out.println("lm = "+lm);
            return multiplyLeft(lm);
        } catch (ArithmeticException e) {
            //e.printStackTrace();
        }
        return this;
    }
}
