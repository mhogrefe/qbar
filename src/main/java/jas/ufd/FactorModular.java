package jas.ufd;

import jas.arith.JasBigInteger;
import jas.arith.Modular;
import jas.arith.ModularRingFactory;
import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.poly.PolyUtil;
import jas.structure.Power;
import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.math.BigInteger;
import java.util.*;

public class FactorModular<MOD extends RingElem<MOD> & Modular> extends FactorAbsolute<MOD> {
    public FactorModular(RingFactory<MOD> cfac) {
        super(cfac);
    }

    SortedMap<Long, GenPolynomial<MOD>> baseDistinctDegreeFactors(GenPolynomial<MOD> P) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P != null");
        }
        SortedMap<Long, GenPolynomial<MOD>> facs = new TreeMap<>();
        if (P.isZERO()) {
            return facs;
        }
        GenPolynomialRing<MOD> pfac = P.ring;
        ModularRingFactory<MOD> mr = (ModularRingFactory<MOD>) pfac.coFac;
        BigInteger m = mr.getIntegerModul().getVal();
        GenPolynomial<MOD> x = pfac.univariate(0);
        GenPolynomial<MOD> h = x;
        GenPolynomial<MOD> f = P;
        GenPolynomial<MOD> g;
        Power<GenPolynomial<MOD>> pow = new Power<>(pfac);
        long d = 0;
        while (d + 1 <= f.degree(0) / 2) {
            d++;
            h = pow.modPower(h, m, f);
            g = engine.gcd(h.subtract(x), f);
            if (!g.isONE()) {
                facs.put(d, g);
                f = f.divide(g);
            }
        }
        if (!f.isONE()) {
            d = f.degree(0);
            facs.put(d, f);
        }
        return facs;
    }

    List<GenPolynomial<MOD>> baseEqualDegreeFactors(GenPolynomial<MOD> P, long deg) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P != null");
        }
        List<GenPolynomial<MOD>> facs = new ArrayList<>();
        if (P.isZERO()) {
            return facs;
        }
        GenPolynomialRing<MOD> pfac = P.ring;
        if (P.degree(0) == deg) {
            facs.add(P);
            return facs;
        }
        ModularRingFactory<MOD> mr = (ModularRingFactory<MOD>) pfac.coFac;
        BigInteger m = mr.getIntegerModul().getVal();
        boolean p2 = false;
        if (m.equals(BigInteger.valueOf(2L))) {
            p2 = true;
        }
        GenPolynomial<MOD> one = pfac.getONE();
        GenPolynomial<MOD> t = pfac.univariate(0, 1L);
        GenPolynomial<MOD> r;
        GenPolynomial<MOD> h;
        GenPolynomial<MOD> f = P;
        Power<GenPolynomial<MOD>> pow = new Power<>(pfac);
        GenPolynomial<MOD> g;
        int degi = (int) deg; //f.degree(0);
        JasBigInteger di = Power.positivePower(new JasBigInteger(m), deg);
        BigInteger d = di.getVal(); //.longValue()-1;
        d = d.shiftRight(1); // divide by 2
        do {
            if (p2) {
                h = t;
                for (int i = 1; i < degi; i++) {
                    h = t.sum(h.multiply(h));
                    h = h.remainder(f);
                }
                t = t.multiply(pfac.univariate(0, 2L));
                //System.out.println("h = " + h);
            } else {
                r = pfac.random(degi, 2 * degi);
                if (r.degree(0) >= f.degree(0)) {
                    r = r.remainder(f);
                }
                r = r.monic();
                //System.out.println("r = " + r);
                h = pow.modPower(r, d, f).subtract(one);
                degi++;
            }
            g = engine.gcd(h, f);
            //System.out.println("g = " + g);
        } while (g.degree(0) == 0 || g.degree(0) == f.degree(0));
        f = f.divide(g);
        facs.addAll(baseEqualDegreeFactors(f, deg));
        facs.addAll(baseEqualDegreeFactors(g, deg));
        return facs;
    }

    @Override
    public List<GenPolynomial<MOD>> baseFactorsSquarefree(GenPolynomial<MOD> P) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P == null");
        }
        List<GenPolynomial<MOD>> factors = new ArrayList<>();
        if (P.isZERO()) {
            return factors;
        }
        if (P.isONE()) {
            factors.add(P);
            return factors;
        }
        GenPolynomialRing<MOD> pfac = P.ring;
        if (!P.leadingBaseCoefficient().isONE()) {
            throw new IllegalArgumentException("ldcf(P) != 1: " + P);
        }
        SortedMap<Long, GenPolynomial<MOD>> dfacs = baseDistinctDegreeFactors(P);
        for (Map.Entry<Long, GenPolynomial<MOD>> me : dfacs.entrySet()) {
            Long e = me.getKey();
            GenPolynomial<MOD> f = me.getValue(); // dfacs.get(e);
            List<GenPolynomial<MOD>> efacs = baseEqualDegreeFactors(f, e);
            factors.addAll(efacs);
        }
        //System.out.println("factors  = " + factors);
        factors = PolyUtil.monic(factors);
        SortedSet<GenPolynomial<MOD>> ss = new TreeSet<>(factors);
        //System.out.println("sorted   = " + ss);
        factors.clear();
        factors.addAll(ss);
        return factors;
    }
}
