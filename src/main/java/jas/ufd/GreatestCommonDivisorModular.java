package jas.ufd;

import jas.arith.*;
import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.poly.PolyUtil;
import jas.structure.RingElem;

import java.math.BigInteger;

/**
 * Greatest common divisor algorithms with modular computation and chinese
 * remainder algorithm.
 *
 * @author Heinz Kredel
 */
public class GreatestCommonDivisorModular<MOD extends RingElem<MOD> & Modular> extends
        GreatestCommonDivisorAbstract<JasBigInteger> {
    private final GreatestCommonDivisorAbstract<MOD> mufd;

    private final GreatestCommonDivisorAbstract<JasBigInteger> iufd = new GreatestCommonDivisorSubres<>();

    public GreatestCommonDivisorModular() {
        mufd = new GreatestCommonDivisorModEval<>();
    }

    @Override
    public GenPolynomial<JasBigInteger> baseGcd(GenPolynomial<JasBigInteger> P, GenPolynomial<JasBigInteger> S) {
        return iufd.baseGcd(P, S);
    }

    @Override
    public GenPolynomial<GenPolynomial<JasBigInteger>> recursiveUnivariateGcd(
            GenPolynomial<GenPolynomial<JasBigInteger>> P, GenPolynomial<GenPolynomial<JasBigInteger>> S) {
        return iufd.recursiveUnivariateGcd(P, S);
    }

    @Override
    @SuppressWarnings("unchecked")
    public GenPolynomial<JasBigInteger> gcd(GenPolynomial<JasBigInteger> P, GenPolynomial<JasBigInteger> S) {
        if (S == null || S.isZERO()) {
            return P;
        }
        if (P == null || P.isZERO()) {
            return S;
        }
        return baseGcd(P, S);

    }
}
