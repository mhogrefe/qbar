package jas.ufd;

import jas.poly.ExpVector;
import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.poly.Monomial;
import jas.structure.Power;
import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.math.BigInteger;
import java.util.Map;
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
        // already checked in constructor:
        //java.math.JasBigInteger c = p.factory().characteristic();
        //if ( c.signum() == 0 ) {
        //    return null;
        //}
        SortedMap<C, Long> root = new TreeMap<>();
        if (p.isZERO()) {
            return root;
        }
        // true for finite fields:
        root.put(p, 1L);
        return root;
    }

    SortedMap<GenPolynomial<C>, Long> rootCharacteristic(GenPolynomial<C> P) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P == null");
        }
        BigInteger c = P.ring.characteristic();
        if (c.signum() == 0) {
            return null;
        }
        SortedMap<GenPolynomial<C>, Long> root = new TreeMap<>();
        if (P.isZERO()) {
            return root;
        }
        if (P.isONE()) {
            root.put(P, 1L);
            return root;
        }
        SortedMap<GenPolynomial<C>, Long> sf = squarefreeFactors(P);
        // better: test if sf.size() == 1 // not ok
        Long k = null;
        for (Map.Entry<GenPolynomial<C>, Long> me : sf.entrySet()) {
            GenPolynomial<C> p = me.getKey();
            if (p.isConstant()) {
                //System.out.println("p,const = " + p);
                continue;
            }
            Long e = me.getValue(); //sf.get(p);
            BigInteger E = new BigInteger(e.toString());
            BigInteger r = E.remainder(c);
            if (!r.equals(BigInteger.ZERO)) {
                //System.out.println("r = " + r);
                return null;
            }
            if (k == null) {
                k = e;
            } else if (k.compareTo(e) >= 0) {
                k = e;
            }
        }
        // now c divides all exponents
        Long cl = c.longValue();
        GenPolynomial<C> rp = P.ring.getONE();
        for (Map.Entry<GenPolynomial<C>, Long> me : sf.entrySet()) {
            GenPolynomial<C> q = me.getKey();
            Long e = me.getValue(); // sf.get(q);
            if (q.isConstant()) { // ensure p-th root
                C qc = q.leadingBaseCoefficient();
                //System.out.println("qc,const = " + qc + ", e = " + e);
                if (e > 1L) {
                    qc = Power.positivePower(qc, e);
                    //e = 1L;
                }
                C qr = qc;
                //System.out.println("qr,const = " + qr);
                q = P.ring.getONE().multiply(qr);
                root.put(q, 1L);
                continue;
            }
            if (e > k) {
                long ep = e / cl;
                q = Power.positivePower(q, ep);
            }
            rp = rp.multiply(q);
        }
        if (k != null) {
            k = k / cl;
            root.put(rp, k);
        }
        //System.out.println("sf,root = " + root);
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
        if (pfac.nvar > 1) {
            // basePthRoot not possible by return type
            throw new IllegalArgumentException(P.getClass().getName() + " only for univariate polynomials");
        }
        RingFactory<C> rf = pfac.coFac;
        if (rf.characteristic().signum() != 1) {
            // basePthRoot not possible
            throw new IllegalArgumentException(P.getClass().getName() + " only for char p > 0 " + rf);
        }
        long mp = rf.characteristic().longValue();
        GenPolynomial<C> d = pfac.getZERO().copy();
        for (Monomial<C> m : P) {
            ExpVector f = m.e;
            long fl = f.getVal(0);
            if (fl % mp != 0) {
                return null;
            }
            fl = fl / mp;
            ExpVector e = ExpVector.create(1, 0, fl);
            // for m.c exists a char-th root, since finite field
            C r = m.c;
            d.doPutToMap(e, r);
        }
        return d;
    }


    /**
     * GenPolynomial char-th root univariate polynomial with polynomial
     * coefficients.
     *
     * @param P recursive univariate GenPolynomial.
     * @return char-th_rootOf(P), or null if P is no char-th root.
     */
    @Override
    public GenPolynomial<GenPolynomial<C>> recursiveUnivariateRootCharacteristic(
            GenPolynomial<GenPolynomial<C>> P) {
        if (P == null || P.isZERO()) {
            return P;
        }
        GenPolynomialRing<GenPolynomial<C>> pfac = P.ring;
        if (pfac.nvar > 1) {
            // basePthRoot not possible by return type
            throw new IllegalArgumentException(P.getClass().getName() + " only for univariate polynomials");
        }
        RingFactory<GenPolynomial<C>> rf = pfac.coFac;
        if (rf.characteristic().signum() != 1) {
            // basePthRoot not possible
            throw new IllegalArgumentException(P.getClass().getName() + " only for char p > 0 " + rf);
        }
        long mp = rf.characteristic().longValue();
        GenPolynomial<GenPolynomial<C>> d = pfac.getZERO().copy();
        for (Monomial<GenPolynomial<C>> m : P) {
            ExpVector f = m.e;
            long fl = f.getVal(0);
            if (fl % mp != 0) {
                return null;
            }
            fl = fl / mp;
            SortedMap<GenPolynomial<C>, Long> sm = rootCharacteristic(m.c);
            if (sm == null) {
                return null;
            }
            GenPolynomial<C> r = rf.getONE();
            for (Map.Entry<GenPolynomial<C>, Long> me : sm.entrySet()) {
                GenPolynomial<C> rp = me.getKey();
                long gl = me.getValue(); //sm.get(rp);
                if (gl > 1) {
                    rp = Power.positivePower(rp, gl);
                }
                r = r.multiply(rp);
            }
            ExpVector e = ExpVector.create(1, 0, fl);
            //System.out.println("put-root r = " + r + ", e = " + e);
            d.doPutToMap(e, r);
        }
        return d;
    }

}
