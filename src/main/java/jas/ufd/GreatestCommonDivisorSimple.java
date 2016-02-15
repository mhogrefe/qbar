package jas.ufd;

import jas.poly.GenPolynomial;
import jas.poly.PolyUtil;
import jas.structure.RingElem;

/**
 * Greatest common divisor algorithms with monic polynomial remainder sequence.
 * If C is a field, then the monic PRS (on coefficients) is computed otherwise
 * no simplifications in the reduction are made.
 *
 * @author Heinz Kredel
 */
public class GreatestCommonDivisorSimple<C extends RingElem<C>> extends GreatestCommonDivisorAbstract<C> {
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
        boolean field = P.ring.coFac.isField();
        long e = P.degree(0);
        long f = S.degree(0);
        GenPolynomial<C> q;
        GenPolynomial<C> r;
        if (f > e) {
            r = P;
            q = S;
        } else {
            q = P;
            r = S;
        }
        C c;
        if (field) {
            r = r.monic();
            q = q.monic();
            c = P.ring.getONECoefficient();
        } else {
            r = r.abs();
            q = q.abs();
            C a = baseContent(r);
            C b = baseContent(q);
            c = gcd(a, b); // indirection
            r = divide(r, a); // indirection
            q = divide(q, b); // indirection
        }
        if (r.isONE()) {
            return r.multiply(c);
        }
        if (q.isONE()) {
            return q.multiply(c);
        }
        GenPolynomial<C> x;
        //System.out.println("q = " + q);
        //System.out.println("r = " + r);
        while (!r.isZERO()) {
            x = PolyUtil.baseSparsePseudoRemainder(q, r);
            q = r;
            if (field) {
                r = x.monic();
            } else {
                r = x;
            }
            //System.out.println("q = " + q);
            //System.out.println("r = " + r);
        }
        q = basePrimitivePart(q);
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
    public GenPolynomial<GenPolynomial<C>> recursiveUnivariateGcd(
            GenPolynomial<GenPolynomial<C>> P,
            GenPolynomial<GenPolynomial<C>> S
    ) {
        if (S == null || S.isZERO()) {
            return P;
        }
        if (P == null || P.isZERO()) {
            return S;
        }
        boolean field = P.leadingBaseCoefficient().ring.coFac.isField();
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
        if (field) {
            r = PolyUtil.monic(r);
            q = PolyUtil.monic(q);
        } else {
            r = r.abs();
            q = q.abs();
        }
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
            q = r;
            if (field) {
                r = PolyUtil.monic(x);
            } else {
                r = x;
            }
        }
        q = recursivePrimitivePart(q);
        q = q.abs().multiply(c);
        return q;
    }
}
