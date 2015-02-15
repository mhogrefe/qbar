package jas.ufd;

import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.poly.PolyUtil;
import jas.structure.RingElem;

/**
 * Greatest common divisor algorithms.
 *
 * @author Heinz Kredel
 */

public abstract class GreatestCommonDivisorAbstract<C extends RingElem<C>> {


    /**
     * Get the String representation.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getName();
    }


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
     * GenPolynomial recursive content.
     *
     * @param P recursive GenPolynomial.
     * @return cont(P).
     */
    public GenPolynomial<C> recursiveContent(GenPolynomial<GenPolynomial<C>> P) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P != null");
        }
        if (P.isZERO()) {
            return P.ring.getZEROCoefficient();
        }
        GenPolynomial<C> d = null;
        for (GenPolynomial<C> c : P.getMap().values()) {
            if (d == null) {
                d = c;
            } else {
                d = gcd(d, c); // go to recursion
            }
            if (d.isONE()) {
                return d;
            }
        }
        return d.abs();
    }


    /**
     * GenPolynomial recursive primitive part.
     *
     * @param P recursive GenPolynomial.
     * @return pp(P).
     */
    GenPolynomial<GenPolynomial<C>> recursivePrimitivePart(GenPolynomial<GenPolynomial<C>> P) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P != null");
        }
        if (P.isZERO()) {
            return P;
        }
        GenPolynomial<C> d = recursiveContent(P);
        if (d.isONE()) {
            return P;
        }
        return PolyUtil.recursiveDivide(P, d);
    }

    /**
     * Univariate GenPolynomial recursive greatest common divisor. Uses
     * pseudoRemainder for remainder.
     *
     * @param P univariate recursive GenPolynomial.
     * @param S univariate recursive GenPolynomial.
     * @return gcd(P, S).
     */
    public abstract GenPolynomial<GenPolynomial<C>> recursiveUnivariateGcd(GenPolynomial<GenPolynomial<C>> P,
                                                                           GenPolynomial<GenPolynomial<C>> S);

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
        GenPolynomialRing<C> pfac = P.ring;
        if (pfac.nvar <= 1) {
            return baseGcd(P, S);
        }
        GenPolynomialRing<C> cfac = pfac.contract(1);
        GenPolynomialRing<GenPolynomial<C>> rfac;
        if (pfac.getVars() != null && pfac.getVars().length > 0) {
            rfac = GenPolynomialRing.make(cfac);
        } else {
            rfac = new GenPolynomialRing<>(cfac);
        }
        GenPolynomial<GenPolynomial<C>> Pr = PolyUtil.recursive(rfac, P);
        GenPolynomial<GenPolynomial<C>> Sr = PolyUtil.recursive(rfac, S);
        GenPolynomial<GenPolynomial<C>> Dr = recursiveUnivariateGcd(Pr, Sr);
        return PolyUtil.distribute(pfac, Dr);
    }


    /**
     * Univariate GenPolynomial resultant.
     *
     * @param P univariate GenPolynomial.
     * @param S univariate GenPolynomial.
     * @return res(P, S).
     * @throws UnsupportedOperationException if there is no implementation in
     *                                       the sub-class.
     */
    @SuppressWarnings("unused")
    public GenPolynomial<C> baseResultant(GenPolynomial<C> P, GenPolynomial<C> S) {
        // can not be abstract
        throw new UnsupportedOperationException("not implemented");
    }


    /**
     * Univariate GenPolynomial recursive resultant.
     *
     * @param P univariate recursive GenPolynomial.
     * @param S univariate recursive GenPolynomial.
     * @return res(P, S).
     * @throws UnsupportedOperationException if there is no implementation in
     *                                       the sub-class.
     */
    @SuppressWarnings("unused")
    public GenPolynomial<GenPolynomial<C>> recursiveUnivariateResultant(GenPolynomial<GenPolynomial<C>> P,
                                                                        GenPolynomial<GenPolynomial<C>> S) {
        // can not be abstract
        throw new UnsupportedOperationException("not implemented");
    }


    /**
     * GenPolynomial recursive resultant.
     *
     * @param P univariate recursive GenPolynomial.
     * @param S univariate recursive GenPolynomial.
     * @return res(P, S).
     * @throws UnsupportedOperationException if there is no implementation in
     *                                       the sub-class.
     */
    GenPolynomial<GenPolynomial<C>> recursiveResultant(GenPolynomial<GenPolynomial<C>> P,
                                                       GenPolynomial<GenPolynomial<C>> S) {
        if (S == null || S.isZERO()) {
            return S;
        }
        if (P == null || P.isZERO()) {
            return P;
        }
        GenPolynomialRing<GenPolynomial<C>> rfac = P.ring;
        GenPolynomialRing<C> cfac = (GenPolynomialRing<C>) rfac.coFac;
        GenPolynomialRing<C> dfac = cfac.extend(rfac.getVars());
        GenPolynomial<C> Pp = PolyUtil.distribute(dfac, P);
        GenPolynomial<C> Sp = PolyUtil.distribute(dfac, S);
        GenPolynomial<C> res = resultant(Pp, Sp);
        return PolyUtil.recursive(rfac, res);
    }


    /**
     * GenPolynomial resultant. The input polynomials are considered as
     * univariate polynomials in the main variable.
     *
     * @param P GenPolynomial.
     * @param S GenPolynomial.
     * @return res(P, S).
     * @throws UnsupportedOperationException if there is no implementation in
     *                                       the sub-class.
     * @see jas.ufd.GreatestCommonDivisorSubres#recursiveResultant
     */
    GenPolynomial<C> resultant(GenPolynomial<C> P, GenPolynomial<C> S) {
        if (S == null || S.isZERO()) {
            return S;
        }
        if (P == null || P.isZERO()) {
            return P;
        }
        // no more hacked: GreatestCommonDivisorSubres<C> ufd_sr = new GreatestCommonDivisorSubres<C>();
        GenPolynomialRing<C> pfac = P.ring;
        if (pfac.nvar <= 1) {
            return baseResultant(P, S);
        }
        GenPolynomialRing<C> cfac = pfac.contract(1);
        GenPolynomialRing<GenPolynomial<C>> rfac = new GenPolynomialRing<>(cfac);

        GenPolynomial<GenPolynomial<C>> Pr = PolyUtil.recursive(rfac, P);
        GenPolynomial<GenPolynomial<C>> Sr = PolyUtil.recursive(rfac, S);

        GenPolynomial<GenPolynomial<C>> Dr = recursiveUnivariateResultant(Pr, Sr);
        return PolyUtil.distribute(pfac, Dr);
    }

}