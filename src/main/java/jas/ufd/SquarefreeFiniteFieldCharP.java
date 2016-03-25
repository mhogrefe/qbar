package jas.ufd;

import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.poly.PolyUtil;
import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Squarefree decomposition for finite coefficient fields of characteristic p.
 *
 * @author Heinz Kredel
 */
@SuppressWarnings("ConstantConditions")
public class SquarefreeFiniteFieldCharP<C extends RingElem<C>> extends SquarefreeAbstract<C> {
    public SquarefreeFiniteFieldCharP(RingFactory<C> fac) {
        super(GCDFactory.getImplementation(fac));
        if (fac.characteristic().signum() == 0) {
            throw new IllegalArgumentException("characterisic(fac) must be non-zero");
        }
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
    private GenPolynomial<C> baseRootCharacteristic(GenPolynomial<C> P) {
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

    @Override
    public SortedMap<GenPolynomial<C>, Long> baseSquarefreeFactors(GenPolynomial<C> A) {
        SortedMap<GenPolynomial<C>, Long> sfactors = new TreeMap<>();
        if (A == null || A.isZERO()) {
            return sfactors;
        }
        GenPolynomialRing<C> pfac = A.ring;
        if (A.isConstant()) {
            C coeff = A.leadingBaseCoefficient();
            //System.out.println("coeff = " + coeff + " @ " + coeff.factory());
            SortedMap<C, Long> rfactors = squarefreeFactors(coeff);
            //System.out.println("rfactors,const = " + rfactors);
            if (rfactors != null && rfactors.size() > 0) {
                for (Map.Entry<C, Long> me : rfactors.entrySet()) {
                    C c = me.getKey();
                    if (!c.isONE()) {
                        GenPolynomial<C> cr = pfac.getONE().multiply(c);
                        Long rk = me.getValue(); // rfactors.get(c);
                        sfactors.put(cr, rk);
                    }
                }
            } else {
                sfactors.put(A, 1L);
            }
            return sfactors;
        }
        C ldbcf = A.leadingBaseCoefficient();
        if (!ldbcf.isONE()) {
            A = A.divide(ldbcf);
            SortedMap<C, Long> rfactors = squarefreeFactors(ldbcf);
            //System.out.println("rfactors,ldbcf = " + rfactors);
            if (rfactors != null && rfactors.size() > 0) {
                for (Map.Entry<C, Long> me : rfactors.entrySet()) {
                    C c = me.getKey();
                    if (!c.isONE()) {
                        GenPolynomial<C> cr = pfac.getONE().multiply(c);
                        Long rk = me.getValue(); //rfactors.get(c);
                        sfactors.put(cr, rk);
                    }
                }
            } else {
                GenPolynomial<C> f1 = pfac.getONE().multiply(ldbcf);
                //System.out.println("gcda sqf f1 = " + f1);
                sfactors.put(f1, 1L);
            }
            ldbcf = pfac.coFac.getONE();
        }
        GenPolynomial<C> T0 = A;
        long e = 1L;
        GenPolynomial<C> Tp;
        GenPolynomial<C> T = null;
        GenPolynomial<C> V = null;
        long k = 0L;
        long mp;
        boolean init = true;
        while (true) {
            //System.out.println("T0 = " + T0);
            if (init) {
                if (T0.isConstant() || T0.isZERO()) {
                    break;
                }
                Tp = PolyUtil.baseDeriviative(T0);
                T = engine.baseGcd(T0, Tp);
                T = T.monic();
                V = PolyUtil.basePseudoDivide(T0, T);
                k = 0L;
                init = false;
            }
            if (V.isConstant()) {
                mp = pfac.characteristic().longValue(); // assert != 0
                //T0 = PolyUtil.<C> baseModRoot(T,mp);
                T0 = baseRootCharacteristic(T);
                if (T0 == null) {
                    //break;
                    T0 = pfac.getZERO();
                }
                e = e * mp;
                init = true;
                continue;
            }
            k++;
            GenPolynomial<C> W = engine.baseGcd(T, V);
            W = W.monic();
            GenPolynomial<C> z = PolyUtil.basePseudoDivide(V, W);
            V = W;
            T = PolyUtil.basePseudoDivide(T, V);
            if (z.degree() > 0) {
                if (ldbcf.isONE() && !z.leadingBaseCoefficient().isONE()) {
                    z = z.monic();
                }
                sfactors.put(z, (e * k));
            }
        }
        return sfactors;
    }

    SortedMap<C, Long> squarefreeFactors(C coeff) {
        if (coeff == null) {
            return null;
        }
        SortedMap<C, Long> factors = new TreeMap<>();
        RingFactory<C> cfac = (RingFactory<C>) coeff.factory();
        if (cfac.isFinite()) {
            SquarefreeFiniteFieldCharP<C> reng =
                    (SquarefreeFiniteFieldCharP<C>) SquarefreeFactory.getImplementation(cfac);
            SortedMap<C, Long> rfactors = reng.rootCharacteristic(coeff); // ??
            factors.putAll(rfactors);
        }
        return factors;
    }
}
