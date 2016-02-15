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

public class FactorModular<MOD extends RingElem<MOD> & Modular> {
    /**
     * Gcd engine for base coefficients.
     */
    final GreatestCommonDivisorAbstract<MOD> engine;

    private final SquarefreeAbstract<MOD> sengine;
    /**
     * Constructor.
     *
     * @param cfac coefficient ring factory.
     */
    FactorModular(RingFactory<MOD> cfac) {
        engine = GCDFactory.getImplementation(cfac);
        sengine = SquarefreeFactory.getImplementation(cfac);
    }

    /**
     * Remove one occurrence of elements.
     *
     * @param a list of objects.
     * @param b list of objects.
     * @return remove every element of b from a, but only one occurrence.
     * <b>Note:</b> not available in java.util.
     */
    static <T> List<T> removeOnce(List<T> a, List<T> b) {
        List<T> res = new ArrayList<>();
        res.addAll(a);
        b.forEach(res::remove);
        return res;
    }

    /**
     * Univariate GenPolynomial factorization.
     *
     * @param P GenPolynomial in one variable.
     * @return [p_1 -&gt; e_1, ..., p_k -&gt; e_k] with P = prod_{i=1,...,k}
     * p_i**e_i.
     */
    public SortedMap<GenPolynomial<MOD>, Long> baseFactors(GenPolynomial<MOD> P) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P != null");
        }
        GenPolynomialRing<MOD> pfac = P.ring;
        SortedMap<GenPolynomial<MOD>, Long> factors = new TreeMap<>(pfac.getComparator());
        if (P.isZERO()) {
            return factors;
        }
        if (P.isConstant()) {
            factors.put(P, 1L);
            return factors;
        }
        MOD c;
        if (pfac.coFac.isField()) { //pfac.characteristic().signum() > 0
            c = P.leadingBaseCoefficient();
        } else {
            c = engine.baseContent(P);
            // move sign to the content
            if (P.signum() < 0 && c.signum() > 0) {
                c = c.negate();
                //P = P.negate();
            }
        }
        if (!c.isONE()) {
            GenPolynomial<MOD> pc = pfac.getONE().multiply(c);
            factors.put(pc, 1L);
            P = P.divide(c); // make primitive or monic
        }
        SortedMap<GenPolynomial<MOD>, Long> facs = sengine.baseSquarefreeFactors(P);
        if (facs == null || facs.size() == 0) {
            facs = new TreeMap<>();
            facs.put(P, 1L);
        }
        for (Map.Entry<GenPolynomial<MOD>, Long> me : facs.entrySet()) {
            GenPolynomial<MOD> g = me.getKey();
            Long k = me.getValue(); //facs.get(g);
            //System.out.println("g       = " + g);
            if (pfac.coFac.isField() && !g.leadingBaseCoefficient().isONE()) {
                g = g.monic(); // how can this happen?
            }
            if (g.degree() <= 1) {
                if (!g.isONE()) {
                    factors.put(g, k);
                }
            } else {
                List<GenPolynomial<MOD>> sfacs = baseFactorsSquarefree(g);
                for (GenPolynomial<MOD> h : sfacs) {
                    Long j = factors.get(h); // evtl. constants
                    if (j != null) {
                        k += j;
                    }
                    if (!h.isONE()) {
                        factors.put(h, k);
                    }
                }
            }
        }
        //System.out.println("factors = " + factors);
        return factors;
    }

    /**
     * Normalize factorization. p'_i &gt; 0 for i &gt; 1 and p'_1 != 1 if k &gt;
     * 1.
     *
     * @param F = [p_1,...,p_k].
     * @return F' = [p'_1,...,p'_k].
     */
    List<GenPolynomial<MOD>> normalizeFactorization(List<GenPolynomial<MOD>> F) {
        if (F == null || F.size() <= 1) {
            return F;
        }
        List<GenPolynomial<MOD>> Fp = new ArrayList<>(F.size());
        GenPolynomial<MOD> f0 = F.get(0);
        for (int i = 1; i < F.size(); i++) {
            GenPolynomial<MOD> fi = F.get(i);
            if (fi.signum() < 0) {
                fi = fi.negate();
                f0 = f0.negate();
            }
            Fp.add(fi);
        }
        if (!f0.isONE()) {
            Fp.add(0, f0);
        }
        return Fp;
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
        GenPolynomial<MOD> x = pfac.univariateA(0);
        GenPolynomial<MOD> h = x;
        GenPolynomial<MOD> f = P;
        GenPolynomial<MOD> g;
        Power<GenPolynomial<MOD>> pow = new Power<>(pfac);
        long d = 0;
        while (d + 1 <= f.degree() / 2) {
            d++;
            h = pow.modPower(h, m, f);
            g = engine.gcd(h.subtract(x), f);
            if (!g.isONE()) {
                facs.put(d, g);
                f = f.divide(g);
            }
        }
        if (!f.isONE()) {
            d = f.degree();
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
        if (P.degree() == deg) {
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
        GenPolynomial<MOD> t = pfac.univariateB(0, 1L);
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
                t = t.multiply(pfac.univariateB(0, 2L));
                //System.out.println("h = " + h);
            } else {
                r = pfac.random(degi, 2 * degi);
                if (r.degree() >= f.degree()) {
                    r = r.remainder(f);
                }
                r = r.monic();
                //System.out.println("r = " + r);
                h = pow.modPower(r, d, f).subtract(one);
                degi++;
            }
            g = engine.gcd(h, f);
            //System.out.println("g = " + g);
        } while (g.degree() == 0 || g.degree() == f.degree());
        f = f.divide(g);
        facs.addAll(baseEqualDegreeFactors(f, deg));
        facs.addAll(baseEqualDegreeFactors(g, deg));
        return facs;
    }

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
