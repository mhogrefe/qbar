package jas.poly;

import jas.arith.JasBigInteger;
import jas.arith.ModLong;
import mho.wheels.iterables.IterableUtils;

import java.util.List;

public class PolyUtil_ModLong {
    /**
     * JasBigInteger from ModInteger coefficients, symmetric. Represent as
     * polynomial with JasBigInteger coefficients by removing the modules and
     * making coefficients symmetric to 0.
     *
     * @param fac result polynomial factory.
     * @param A   polynomial with ModInteger coefficients to be converted.
     * @return polynomial with JasBigInteger coefficients.
     */
    public static GenPolynomial<JasBigInteger> integerFromModularCoefficients(
            GenPolynomialRing<JasBigInteger> fac, GenPolynomial<ModLong> A) {
        return PolyUtil.map(fac, A, new ModSymToInt<ModLong>());
    }

    /**
     * JasBigInteger from ModInteger coefficients, symmetric. Represent as
     * polynomial with JasBigInteger coefficients by removing the modules and
     * making coefficients symmetric to 0.
     *
     * @param fac result polynomial factory.
     * @param L   list of polynomials with ModInteger coefficients to be
     *            converted.
     * @return list of polynomials with JasBigInteger coefficients.
     */
    public static List<GenPolynomial<JasBigInteger>> integerFromModularCoefficients(
            final GenPolynomialRing<JasBigInteger> fac, List<GenPolynomial<ModLong>> L) {
        return IterableUtils.toList(IterableUtils.map(c -> integerFromModularCoefficients(fac, c), L));
    }
}
