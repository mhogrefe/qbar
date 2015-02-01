package jas;

import jas.arith.JasBigInteger;
import jas.poly.ExpVector;
import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.ufd.FactorAbstract;
import jas.ufd.FactorInteger;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static mho.wheels.iterables.IterableUtils.*;

public class JasApi {
    public static List<List<BigInteger>> factorPolynomial(String polyString) {
        GenPolynomialRing<JasBigInteger> ring = GenPolynomialRing.make(new JasBigInteger());
        GenPolynomial<JasBigInteger> tmp = ring.parse(polyString);
        FactorAbstract<JasBigInteger> engine = new FactorInteger();
        SortedMap<GenPolynomial<JasBigInteger>, Long> factors = engine.baseFactors(tmp);
        List<List<BigInteger>> fs = new ArrayList<>();
        for (Map.Entry<GenPolynomial<JasBigInteger>, Long> entry : factors.entrySet()) {
            List<BigInteger> cs = toList(replicate((int) entry.getKey().degree() + 1, BigInteger.ZERO));
            for (Map.Entry<ExpVector, JasBigInteger> e2 : entry.getKey().val.entrySet()) {
                cs.set((int) e2.getKey().getVal(0), e2.getValue().getVal());
            }
            for (int j = 0; j < entry.getValue(); j++) {
                fs.add(cs);
            }
        }
        return fs;
    }
}
