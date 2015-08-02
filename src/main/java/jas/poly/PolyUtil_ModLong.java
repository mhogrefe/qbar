package jas.poly;

import jas.arith.JasBigInteger;
import jas.arith.ModLong;
import jas.arith.ModularRingFactory;
import jas.util.ListUtil;
import mho.wheels.iterables.IterableUtils;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.function.Function;

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
    public static GenPolynomial<ModLong> chineseRemainder(
            GenPolynomialRing<ModLong> fac, GenPolynomial<ModLong> A, ModLong mi, GenPolynomial<ModLong> B) {
        ModularRingFactory<ModLong> cfac = (ModularRingFactory<ModLong>) fac.coFac; // get RingFactory
        GenPolynomial<ModLong> S = fac.getZERO().copy();
        GenPolynomial<ModLong> Ap = A.copy();
        SortedMap<ExpVector, ModLong> av = Ap.val; //getMap();
        SortedMap<ExpVector, ModLong> bv = B.getMap();
        SortedMap<ExpVector, ModLong> sv = S.val; //getMap();
        ModLong c;
        for (Map.Entry<ExpVector, ModLong> me : bv.entrySet()) {
            ExpVector e = me.getKey();
            ModLong y = me.getValue(); //bv.get(e); // assert y != null
            ModLong x = av.get(e);
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
        for (Map.Entry<ExpVector, ModLong> me : av.entrySet()) { // rest of av
            ExpVector e = me.getKey();
            ModLong x = me.getValue(); // av.get(e); // assert x != null
            //c = cfac.fromInteger( x.getVal() );
            c = cfac.chineseRemainder(x, mi, B.ring.coFac.getZERO());
            if (!c.isZERO()) { // 0 cannot happen
                sv.put(e, c); // c != null
            }
        }
        return S;
    }
}
