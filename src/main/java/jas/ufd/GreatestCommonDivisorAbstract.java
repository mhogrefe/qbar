package jas.ufd;

import jas.poly.GenPolynomial;
import jas.structure.RingElem;

/**
 * Greatest common divisor algorithms.
 *
 * @author Heinz Kredel
 */
public abstract class GreatestCommonDivisorAbstract<C extends RingElem<C>> {
    /**
     * GenPolynomial base coefficient content.
     *
     * @param P GenPolynomial.
     * @return cont(P).
     */
    public C baseContent(GenPolynomial<C> P) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P != null");
        }
        if (P.isZERO()) {
            return P.ring.getZEROCoefficient();
        }
        C d = null;
        for (C c : P.getMap().values()) {
            if (d == null) {
                d = c;
            } else {
                d = d.gcd(c);
            }
            if (d.isONE()) {
                return d;
            }
        }
        if (d.signum() < 0) {
            d = d.negate();
        }
        return d;
    }

    /**
     * GenPolynomial base coefficient primitive part.
     *
     * @param P GenPolynomial.
     * @return pp(P).
     */
    public GenPolynomial<C> basePrimitivePart(GenPolynomial<C> P) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P != null");
        }
        if (P.isZERO()) {
            return P;
        }
        C d = baseContent(P);
        if (d.isONE()) {
            return P;
        }
        return P.divide(d);
    }

    /**
     * Univariate GenPolynomial greatest common divisor. Uses sparse
     * pseudoRemainder for remainder.
     *
     * @param P univariate GenPolynomial.
     * @param S univariate GenPolynomial.
     * @return gcd(P, S).
     */
    public abstract GenPolynomial<C> baseGcd(GenPolynomial<C> P, GenPolynomial<C> S);

    /**
     * GenPolynomial division. Indirection to GenPolynomial method.
     *
     * @param a GenPolynomial.
     * @param b coefficient.
     * @return a/b.
     */
    GenPolynomial<C> divide(GenPolynomial<C> a, C b) {
        if (b == null || b.isZERO()) {
            throw new IllegalArgumentException("division by zero");
        }
        if (a == null || a.isZERO()) {
            return a;
        }
        return a.divide(b);
    }

    /**
     * Coefficient greatest common divisor. Indirection to coefficient method.
     *
     * @param a coefficient.
     * @param b coefficient.
     * @return gcd(a, b).
     */
    C gcd(C a, C b) {
        if (b == null || b.isZERO()) {
            return a;
        }
        if (a == null || a.isZERO()) {
            return b;
        }
        return a.gcd(b);
    }

    /**
     * GenPolynomial greatest common divisor.
     *
     * @param P GenPolynomial.
     * @param S GenPolynomial.
     * @return gcd(P, S).
     */
    public GenPolynomial<C> gcd(GenPolynomial<C> P, GenPolynomial<C> S) {
        if (S == null || S.isZERO()) {
            return P;
        }
        if (P == null || P.isZERO()) {
            return S;
        }
        return baseGcd(P, S);
    }
}
