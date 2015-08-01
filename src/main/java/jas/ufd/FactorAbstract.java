package jas.ufd;

import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.util.*;

public abstract class FactorAbstract<C extends RingElem<C>> {
    /**
     * Gcd engine for base coefficients.
     */
    final GreatestCommonDivisorAbstract<C> engine;

    private final SquarefreeAbstract<C> sengine;
    /**
     *
     * No argument constructor.
     */
    FactorAbstract() {
        throw new IllegalArgumentException("don't use this constructor");
    }

    /**
     * Constructor.
     *
     * @param cfac coefficient ring factory.
     */
    FactorAbstract(RingFactory<C> cfac) {
        engine = GCDFactory.getProxy(cfac);
        //engine = GCDFactory.<C> getImplementation(cfac);
        sengine = SquarefreeFactory.getImplementation(cfac);
    }

    /**
     * Get the String representation.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getName();
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
    public SortedMap<GenPolynomial<C>, Long> baseFactors(GenPolynomial<C> P) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P != null");
        }
        GenPolynomialRing<C> pfac = P.ring;
        SortedMap<GenPolynomial<C>, Long> factors = new TreeMap<>(pfac.getComparator());
        if (P.isZERO()) {
            return factors;
        }
        if (pfac.nvar > 1) {
            throw new IllegalArgumentException(this.getClass().getName() + " only for univariate polynomials");
        }
        if (P.isConstant()) {
            factors.put(P, 1L);
            return factors;
        }
        C c;
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
            GenPolynomial<C> pc = pfac.getONE().multiply(c);
            factors.put(pc, 1L);
            P = P.divide(c); // make primitive or monic
        }
        SortedMap<GenPolynomial<C>, Long> facs = sengine.baseSquarefreeFactors(P);
        if (facs == null || facs.size() == 0) {
            facs = new TreeMap<>();
            facs.put(P, 1L);
        }
        for (Map.Entry<GenPolynomial<C>, Long> me : facs.entrySet()) {
            GenPolynomial<C> g = me.getKey();
            Long k = me.getValue(); //facs.get(g);
            //System.out.println("g       = " + g);
            if (pfac.coFac.isField() && !g.leadingBaseCoefficient().isONE()) {
                g = g.monic(); // how can this happen?
            }
            if (g.degree(0) <= 1) {
                if (!g.isONE()) {
                    factors.put(g, k);
                }
            } else {
                List<GenPolynomial<C>> sfacs = baseFactorsSquarefree(g);
                for (GenPolynomial<C> h : sfacs) {
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
     * Univariate GenPolynomial factorization of a squarefree polynomial.
     *
     * @param P squarefree and primitive! GenPolynomial in one variable.
     * @return [p_1, ..., p_k] with P = prod_{i=1,...,k} p_i.
     */
    protected abstract List<GenPolynomial<C>> baseFactorsSquarefree(GenPolynomial<C> P);

    /**
     * Normalize factorization. p'_i &gt; 0 for i &gt; 1 and p'_1 != 1 if k &gt;
     * 1.
     *
     * @param F = [p_1,...,p_k].
     * @return F' = [p'_1,...,p'_k].
     */
    List<GenPolynomial<C>> normalizeFactorization(List<GenPolynomial<C>> F) {
        if (F == null || F.size() <= 1) {
            return F;
        }
        List<GenPolynomial<C>> Fp = new ArrayList<>(F.size());
        GenPolynomial<C> f0 = F.get(0);
        for (int i = 1; i < F.size(); i++) {
            GenPolynomial<C> fi = F.get(i);
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
}
