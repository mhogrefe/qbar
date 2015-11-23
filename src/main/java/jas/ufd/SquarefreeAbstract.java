package jas.ufd;

import jas.poly.GenPolynomial;
import jas.structure.RingElem;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Abstract squarefree decomposition class.
 *
 * @author Heinz Kredel
 */
public abstract class SquarefreeAbstract<C extends RingElem<C>> {
    /**
     * GCD engine for respective base coefficients.
     */
    final GreatestCommonDivisorAbstract<C> engine;

    /**
     * Constructor.
     */
    SquarefreeAbstract(GreatestCommonDivisorAbstract<C> engine) {
        this.engine = engine;
    }

    /**
     * GenPolynomial polynomial squarefree factorization.
     *
     * @param A GenPolynomial.
     * @return [p_1 -&gt; e_1, ..., p_k -&gt; e_k] with P = prod_{i=1,...,k}
     * p_i^{e_i} and p_i squarefree.
     */
    public abstract SortedMap<GenPolynomial<C>, Long> baseSquarefreeFactors(GenPolynomial<C> A);

    /**
     * Normalize factorization. p'_i &gt; 0 for i &gt; 1 and p'_1 != 1 if k &gt;
     * 1.
     *
     * @param F = [p_1-&gt;e_1;, ..., p_k-&gt;e_k].
     * @return F' = [p'_1-&gt;e_1, ..., p'_k-&gt;e_k].
     */
    SortedMap<GenPolynomial<C>, Long> normalizeFactorization(SortedMap<GenPolynomial<C>, Long> F) {
        if (F == null || F.size() <= 1) {
            return F;
        }
        List<GenPolynomial<C>> Fp = new ArrayList<>(F.keySet());
        GenPolynomial<C> f0 = Fp.get(0);
        if (f0.ring.characteristic().signum() != 0) { // only ordered coefficients
            return F;
        }
        long e0 = F.get(f0);
        SortedMap<GenPolynomial<C>, Long> Sp = new TreeMap<>();
        for (int i = 1; i < Fp.size(); i++) {
            GenPolynomial<C> fi = Fp.get(i);
            long ei = F.get(fi);
            if (fi.signum() < 0) {
                //System.out.println("e0 = " + e0 + ", f0 = " + f0);
                //System.out.println("ei = " + ei + ", fi = " + fi);
                fi = fi.negate();
                if (ei % 2 != 0) { // && e0 % 2 != 0
                    f0 = f0.negate();
                }
            }
            Sp.put(fi, ei);
        }
        if (!f0.isONE()) {
            Sp.put(f0, e0);
        }
        return Sp;
    }
}
