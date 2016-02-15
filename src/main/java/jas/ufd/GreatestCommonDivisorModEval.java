package jas.ufd;

import jas.arith.Modular;
import jas.poly.GenPolynomial;
import jas.structure.RingElem;

/**
 * Greatest common divisor algorithms with modular evaluation algorithm for
 * recursion.
 *
 * @author Heinz Kredel
 */
public class GreatestCommonDivisorModEval<MOD extends RingElem<MOD> & Modular>
        extends GreatestCommonDivisorAbstract<MOD> {
    /**
     * Modular gcd algorithm to use.
     */
    private final GreatestCommonDivisorAbstract<MOD> mufd
            = new GreatestCommonDivisorSimple<>();
    // = new GreatestCommonDivisorPrimitive<MOD>();
    // not okay: = new GreatestCommonDivisorSubres<MOD>();

    /**
     * Univariate GenPolynomial greatest common divisor.
     *
     * @param P univariate GenPolynomial.
     * @param S univariate GenPolynomial.
     * @return gcd(P, S).
     */
    @Override
    public GenPolynomial<MOD> baseGcd(GenPolynomial<MOD> P, GenPolynomial<MOD> S) {
        // required as recursion base
        return mufd.baseGcd(P, S);
    }

    /**
     * GenPolynomial greatest common divisor, modular evaluation algorithm.
     *
     * @param P GenPolynomial.
     * @param S GenPolynomial.
     * @return gcd(P, S).
     */
    @Override
    public GenPolynomial<MOD> gcd(GenPolynomial<MOD> P, GenPolynomial<MOD> S) {
        if (S == null || S.isZERO()) {
            return P;
        }
        if (P == null || P.isZERO()) {
            return S;
        }
        return baseGcd(P, S);
    }
}
