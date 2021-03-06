package jas;

import jas.arith.JasBigInteger;
import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.ufd.FactorInteger;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.replicate;
import static mho.wheels.iterables.IterableUtils.toList;

public class JasApi {
    public static List<List<BigInteger>> factorSquareFree(List<BigInteger> xs) {
        GenPolynomial<JasBigInteger> tmp = toPolynomial(xs);
        FactorInteger engine = new FactorInteger();
        List<GenPolynomial<JasBigInteger>> factors = engine.baseFactorsSquarefree(tmp);
        List<List<BigInteger>> fs = new ArrayList<>();
        for (GenPolynomial<JasBigInteger> factor : factors) {
            fs.add(fromPolynomial(factor));
        }
        return fs;
    }

    private static List<BigInteger> fromPolynomial(GenPolynomial<JasBigInteger> p) {
        List<BigInteger> cs = toList(replicate((int) p.degree() + 1, BigInteger.ZERO));
        for (Map.Entry<Long, JasBigInteger> e2 : p.val.entrySet()) {
            cs.set(e2.getKey().intValue(), e2.getValue().getVal());
        }
        return cs;
    }

    private static GenPolynomial<JasBigInteger> toPolynomial(List<BigInteger> xs) {
        SortedMap<Long, JasBigInteger> z = new TreeMap<>();
        for (int i = 0; i < xs.size(); i++) {
            if (!xs.get(i).equals(BigInteger.ZERO)) {
                z.put((long) i, new JasBigInteger(xs.get(i)));
            }
        }
        return new GenPolynomial<>(new GenPolynomialRing<>(new JasBigInteger()), z);
    }
}
