package jas.ufd;

import jas.poly.GenPolynomial;
import jas.poly.PolyUtil;
import jas.structure.Power;
import jas.structure.RingElem;
import jas.structure.RingFactory;

/**
 * Greatest common divisor algorithms with subresultant polynomial remainder
 * sequence.
 *
 * @author Heinz Kredel
 */
public class GreatestCommonDivisorSubres<C extends RingElem<C>> extends GreatestCommonDivisorAbstract<C> {
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
        C g = r.ring.getONECoefficient();
        C h = r.ring.getONECoefficient();
        GenPolynomial<C> x;
        C z;
        while (!r.isZERO()) {
            long delta = q.degree() - r.degree();
            //System.out.println("delta    = " + delta);
            x = PolyUtil.baseDensePseudoRemainder(q, r);
            q = r;
            if (!x.isZERO()) {
                z = g.multiply(power(P.ring.coFac, h, delta));
                //System.out.println("z  = " + z);
                r = x.divide(z);
                g = q.leadingBaseCoefficient();
                z = power(P.ring.coFac, g, delta);
                h = z.divide(power(P.ring.coFac, h, delta - 1));
                //System.out.println("g  = " + g);
                //System.out.println("h  = " + h);
            } else {
                r = x;
            }
        }
        q = basePrimitivePart(q);
        return (q.multiply(c)).abs();
    }

    /**
     * Coefficient power.
     *
     * @param A coefficient
     * @param i exponent.
     * @return A^i.
     */
    C power(RingFactory<C> fac, C A, long i) {
        return Power.power(fac, A, i);
    }
}
