package jas.poly;

import jas.arith.JasBigInteger;
import jas.arith.ModInteger;
import jas.arith.ModularRingFactory;
import mho.wheels.iterables.IterableUtils;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class PolyUtil_ModInteger {
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
            GenPolynomialRing<JasBigInteger> fac, GenPolynomial<ModInteger> A) {
        return PolyUtil.map(fac, A, new ModSymToInt<>());
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
            final GenPolynomialRing<JasBigInteger> fac,
            List<GenPolynomial<ModInteger>> L
    ) {
        return IterableUtils.toList(IterableUtils.map(c -> integerFromModularCoefficients(fac, c), L));
    }

    /**
     * ModInteger chinese remainder algorithm on coefficients.
     *
     * @param fac GenPolynomial&lt;ModInteger&gt; result factory with
     *            A.coFac.modul*B.coFac.modul = C.coFac.modul.
     * @param A   GenPolynomial&lt;ModInteger&gt;.
     * @param B   other GenPolynomial&lt;ModInteger&gt;.
     * @param mi  inverse of A.coFac.modul in ring B.coFac.
     * @return S = cra(A,B), with S mod A.coFac.modul == A and S mod
     * B.coFac.modul == B.
     */
    public static GenPolynomial<ModInteger> chineseRemainder(
            GenPolynomialRing<ModInteger> fac,
            GenPolynomial<ModInteger> A,
            ModInteger mi,
            GenPolynomial<ModInteger> B
    ) {
        ModularRingFactory<ModInteger> cfac = (ModularRingFactory<ModInteger>) fac.coFac; // get RingFactory
        GenPolynomial<ModInteger> S = fac.getZERO().copy();
        GenPolynomial<ModInteger> Ap = A.copy();
        SortedMap<Long, ModInteger> av = Ap.val; //getMap();
        SortedMap<Long, ModInteger> bv = B.getMap();
        SortedMap<Long, ModInteger> sv = S.val; //getMap();
        ModInteger c;
        for (Map.Entry<Long, ModInteger> me : bv.entrySet()) {
            Long e = me.getKey();
            ModInteger y = me.getValue(); //bv.get(e); // assert y != null
            ModInteger x = av.get(e);
            if (x != null) {
                av.remove(e);
                c = cfac.chineseRemainder(x, mi, y);
                if (!c.isZERO()) { // 0 cannot happen
                    sv.put(e, c);
                }
            } else {
                //c = cfac.fromInteger( y.getVal() );
                c = cfac.chineseRemainder(A.ring.coFac.getZERO(), mi, y);
                if (!c.isZERO()) { // 0 cannot happen
                    sv.put(e, c); // c != null
                }
            }
        }
        // assert bv is empty = done
        for (Map.Entry<Long, ModInteger> me : av.entrySet()) { // rest of av
            long e = me.getKey();
            ModInteger x = me.getValue(); // av.get(e); // assert x != null
            //c = cfac.fromInteger( x.getVal() );
            c = cfac.chineseRemainder(x, mi, B.ring.coFac.getZERO());
            if (!c.isZERO()) { // 0 cannot happen
                sv.put(e, c); // c != null
            }
        }
        return S;
    }
}
