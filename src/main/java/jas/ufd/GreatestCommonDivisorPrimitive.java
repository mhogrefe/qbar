package jas.ufd;

import jas.poly.GenPolynomial;
import jas.poly.PolyUtil;
import jas.structure.RingElem;

/**
 * Greatest common divisor algorithms with primitive polynomial remainder
 * sequence.
 *
 * @author Heinz Kredel
 */

public class GreatestCommonDivisorPrimitive<C extends RingElem<C>> extends
        GreatestCommonDivisorAbstract<C> {


    /**
     * Univariate GenPolynomial greatest comon divisor. Uses pseudoRemainder for
     * remainder.
     *
     * @param P univariate GenPolynomial.
     * @param S univariate GenPolynomial.
     * @return gcd(P, S).
     */
    @Override
    public GenPolynomial<C> baseGcd(GenPolynomial<C> P, GenPolynomial<C> S) {
        if (S == null || S.isZERO()) {
            return P;
        }
        if (P == null || P.isZERO()) {
            return S;
        }
        if (P.ring.nvar > 1) {
            throw new IllegalArgumentException(this.getClass().getName() + " no univariate polynomial");
        }
        long e = P.degree(0);
        long f = S.degree(0);
        GenPolynomial<C> q;
        GenPolynomial<C> r;
        if (f > e) {
            r = P;
            q = S;
            long g = f;
        } else {
            q = P;
            r = S;
        }
        r = r.abs();
        q = q.abs();
        C a = baseContent(r);
        C b = baseContent(q);
        C c = gcd(a, b); // indirection
        r = divide(r, a); // indirection
        q = divide(q, b); // indirection
        if (r.isONE()) {
            return r.multiply(c);
        }
        if (q.isONE()) {
            return q.multiply(c);
        }
        GenPolynomial<C> x;
        while (!r.isZERO()) {
            x = PolyUtil.baseSparsePseudoRemainder(q, r);
            q = r;
            r = basePrimitivePart(x);
        }
        return (q.multiply(c)).abs();
    }


    /**
     * Univariate GenPolynomial recursive greatest comon divisor. Uses
     * pseudoRemainder for remainder.
     *
     * @param P univariate recursive GenPolynomial.
     * @param S univariate recursive GenPolynomial.
     * @return gcd(P, S).
     */
    @Override
    public GenPolynomial<GenPolynomial<C>> recursiveUnivariateGcd(GenPolynomial<GenPolynomial<C>> P,
                                                                  GenPolynomial<GenPolynomial<C>> S) {
        if (S == null || S.isZERO()) {
            return P;
        }
        if (P == null || P.isZERO()) {
            return S;
        }
        if (P.ring.nvar > 1) {
            throw new IllegalArgumentException(this.getClass().getName() + " no univariate polynomial");
        }
        long e = P.degree(0);
        long f = S.degree(0);
        GenPolynomial<GenPolynomial<C>> q;
        GenPolynomial<GenPolynomial<C>> r;
        if (f > e) {
            r = P;
            q = S;
            long g = f;
        } else {
            q = P;
            r = S;
        }
        r = r.abs();
        q = q.abs();
        GenPolynomial<C> a = recursiveContent(r);
        GenPolynomial<C> b = recursiveContent(q);

        GenPolynomial<C> c = gcd(a, b); // go to recursion
        //System.out.println("rgcd c = " + c);
        r = PolyUtil.recursiveDivide(r, a);
        q = PolyUtil.recursiveDivide(q, b);
        if (r.isONE()) {
            return r.multiply(c);
        }
        if (q.isONE()) {
            return q.multiply(c);
        }
        GenPolynomial<GenPolynomial<C>> x;
        while (!r.isZERO()) {
            x = PolyUtil.recursivePseudoRemainder(q, r);
            //System.out.println("rgcd x = " + x);
            q = r;
            r = recursivePrimitivePart(x);
        }
        return q.abs().multiply(c); //.abs();
    }

}