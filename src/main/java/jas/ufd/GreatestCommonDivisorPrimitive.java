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
public class GreatestCommonDivisorPrimitive<C extends RingElem<C>> extends GreatestCommonDivisorAbstract<C> {
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
        long e = P.degree();
        long f = S.degree();
        GenPolynomial<C> q;
        GenPolynomial<C> r;
        if (f > e) {
            r = P;
            q = S;
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
}
