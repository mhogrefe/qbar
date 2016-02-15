package jas.ufd;

import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.structure.RingElem;
import jas.structure.RingFactory;
import mho.wheels.structures.Pair;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Squarefree decomposition for finite coefficient fields of characteristic p.
 *
 * @author Heinz Kredel
 */
@SuppressWarnings("ConstantConditions")
public class SquarefreeFiniteFieldCharP<C extends RingElem<C>> extends SquarefreeFieldCharP<C> {
    public SquarefreeFiniteFieldCharP(RingFactory<C> fac) {
        super(fac);
        // isFinite() predicate now present
        if (!fac.isFinite()) {
            throw new IllegalArgumentException("fac must be finite");
        }
    }

    /* --------- char-th roots --------------------- */

    /**
     * Characteristics root of a coefficient. <b>Note:</b> not needed at the
     * moment.
     *
     * @param p coefficient.
     * @return [p -&gt; k] if exists k with e=k*charactristic(c) and c = p**e,
     * else null.
     */
    public SortedMap<C, Long> rootCharacteristic(C p) {
        if (p == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " p == null");
        }
        SortedMap<C, Long> root = new TreeMap<>();
        if (p.isZERO()) {
            return root;
        }
        // true for finite fields:
        root.put(p, 1L);
        return root;
    }

    /**
     * GenPolynomial char-th root univariate polynomial. Base coefficient type
     * must be finite field, that is ModInteger or
     * AlgebraicNumber&lt;ModInteger&gt; etc.
     *
     * @param P GenPolynomial.
     * @return char-th_rootOf(P), or null if no char-th root.
     */
    @Override
    public GenPolynomial<C> baseRootCharacteristic(GenPolynomial<C> P) {
        if (P == null || P.isZERO()) {
            return P;
        }
        GenPolynomialRing<C> pfac = P.ring;
        RingFactory<C> rf = pfac.coFac;
        if (rf.characteristic().signum() != 1) {
            // basePthRoot not possible
            throw new IllegalArgumentException(P.getClass().getName() + " only for char p > 0 " + rf);
        }
        long mp = rf.characteristic().longValue();
        GenPolynomial<C> d = pfac.getZERO().copy();
        for (SortedMap.Entry<Long, C> m : P) {
            long fl = m.getKey();
            if (fl % mp != 0) {
                return null;
            }
            fl = fl / mp;
            long e = fl;
            // for m.c exists a char-th root, since finite field
            C r = m.getValue();
            d.doPutToMap(e, r);
        }
        return d;
    }
}
