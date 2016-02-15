package jas.ufd;

import jas.arith.*;
import jas.poly.GenPolynomial;

/**
 * Greatest common divisor algorithms with modular computation and chinese
 * remainder algorithm.
 *
 * @author Heinz Kredel
 */
public class GreatestCommonDivisorModular extends GreatestCommonDivisorAbstract<JasBigInteger> {
    private final GreatestCommonDivisorAbstract<JasBigInteger> iufd = new GreatestCommonDivisorSubres<>();

    @Override
    public GenPolynomial<JasBigInteger> baseGcd(GenPolynomial<JasBigInteger> P, GenPolynomial<JasBigInteger> S) {
        return iufd.baseGcd(P, S);
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
