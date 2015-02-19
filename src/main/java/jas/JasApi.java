package jas;

import jas.arith.JasBigInteger;
import jas.poly.ExpVector;
import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.ufd.FactorAbstract;
import jas.ufd.FactorInteger;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;

public class JasApi {
    public static List<List<BigInteger>> factorPolynomial(List<BigInteger> xs) {
        GenPolynomial<JasBigInteger> tmp = toPolynomial(xs);
        FactorAbstract<JasBigInteger> engine = new FactorInteger();
        SortedMap<GenPolynomial<JasBigInteger>, Long> factors = engine.baseFactors(tmp);
        List<List<BigInteger>> fs = new ArrayList<>();
        for (Map.Entry<GenPolynomial<JasBigInteger>, Long> entry : factors.entrySet()) {
            List<BigInteger> cs = fromPolynomial(entry.getKey());
            for (int j = 0; j < entry.getValue(); j++) {
                fs.add(cs);
            }
        }
        return fs;
    }

    private static List<BigInteger> fromPolynomial(GenPolynomial<JasBigInteger> p) {
        List<BigInteger> cs = toList(replicate((int) p.degree() + 1, BigInteger.ZERO));
        for (Map.Entry<ExpVector, JasBigInteger> e2 : p.val.entrySet()) {
            cs.set((int) e2.getKey().getVal(0), e2.getValue().getVal());
        }
        return cs;
    }

    private static GenPolynomial<JasBigInteger> toPolynomial(List<BigInteger> xs) {
        SortedMap<ExpVector, JasBigInteger> z = new TreeMap<>();
        for (int i = 0; i < xs.size(); i++) {
            if (!xs.get(i).equals(BigInteger.ZERO)) {
                z.put(new ExpVector(1, 0, i), new JasBigInteger(xs.get(i)));
            }
        }
        return new GenPolynomial<>(GenPolynomialRing.make(new JasBigInteger()), z);
    }
}
