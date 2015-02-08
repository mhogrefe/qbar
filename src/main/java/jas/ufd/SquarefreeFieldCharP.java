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
 * Squarefree decomposition for coefficient fields of characteristic p.
 *
 * @author Heinz Kredel
 */

public abstract class SquarefreeFieldCharP<C extends RingElem<C>> extends SquarefreeAbstract<C> {


    /*
     * Squarefree engine for characteristic p base coefficients.
     */
    //protected final SquarefreeAbstract<C> rengine;


    /**
     * Factory for finite field of characteristic p coefficients.
     */
    private final RingFactory<C> coFac;

    /**
     * Constructor.
     */
    @SuppressWarnings("unchecked")
    SquarefreeFieldCharP(RingFactory<C> fac) {
        super(GCDFactory.<C>getProxy(fac));
        fac.isField();
        if (fac.characteristic().signum() == 0) {
            throw new IllegalArgumentException("characterisic(fac) must be non-zero");
        }
        coFac = fac;
    }


    /**
     * Get the String representation.
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getClass().getName() + " with " + engine + " over " + coFac;
    }

    /**
     * GenPolynomial polynomial squarefree factorization.
     *
     * @param A GenPolynomial.
     * @return [p_1 -&gt; e_1, ..., p_k -&gt; e_k] with P = prod_{i=1,...,k}
     * p_i^{e_i} and p_i squarefree.
     */
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
        if (pfac.nvar > 1) {
            throw new IllegalArgumentException(this.getClass().getName() + " only for univariate polynomials");
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
                //System.out.println("iT0 = " + T0);
                //System.out.println("iTp = " + Tp);
                //System.out.println("iT  = " + T);
                //System.out.println("iV  = " + V);
                //System.out.println("const(iV)  = " + V.isConstant());
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
            //System.out.println("W = " + W);
            //System.out.println("z = " + z);
            V = W;
            T = PolyUtil.basePseudoDivide(T, V);
            //System.out.println("V = " + V);
            //System.out.println("T = " + T);
            if (z.degree(0) > 0) {
                if (ldbcf.isONE() && !z.leadingBaseCoefficient().isONE()) {
                    z = z.monic();
                }
                sfactors.put(z, (e * k));
            }
        }
        //      look, a stupid error:
        //         if ( !ldbcf.isONE() ) {
        //             GenPolynomial<C> f1 = sfactors.firstKey();
        //             long e1 = sfactors.remove(f1);
        //             System.out.println("gcda sqf c = " + c);
        //             f1 = f1.multiply(c);
        //             //System.out.println("gcda sqf f1e = " + f1);
        //             sfactors.put(f1,e1);
        //         }
        return sfactors;
    }


    /**
     * GenPolynomial recursive univariate polynomial squarefree factorization.
     *
     * @param P recursive univariate GenPolynomial.
     * @return [p_1 -&gt; e_1, ..., p_k -&gt; e_k] with P = prod_{i=1,...,k}
     * p_i^{e_i} and p_i squarefree.
     */
    SortedMap<GenPolynomial<GenPolynomial<C>>, Long> recursiveUnivariateSquarefreeFactors(
            GenPolynomial<GenPolynomial<C>> P) {
        SortedMap<GenPolynomial<GenPolynomial<C>>, Long> sfactors = new TreeMap<>();
        if (P == null || P.isZERO()) {
            return sfactors;
        }
        GenPolynomialRing<GenPolynomial<C>> pfac = P.ring;
        if (pfac.nvar > 1) {
            // recursiveContent not possible by return type
            throw new IllegalArgumentException(this.getClass().getName() + " only for univariate polynomials");
        }
        // if base coefficient ring is a field, make monic
        GenPolynomialRing<C> cfac = (GenPolynomialRing<C>) pfac.coFac;
        C ldbcf = P.leadingBaseCoefficient().leadingBaseCoefficient();
        if (!ldbcf.isONE()) {
            GenPolynomial<C> lc = cfac.getONE().multiply(ldbcf);
            GenPolynomial<GenPolynomial<C>> pl = pfac.getONE().multiply(lc);
            sfactors.put(pl, 1L);
            C li = ldbcf.inverse();
            //System.out.println("li = " + li);
            P = P.multiply(cfac.getONE().multiply(li));
            //System.out.println("P,monic = " + P);
            ldbcf = P.leadingBaseCoefficient().leadingBaseCoefficient();
        }
        // factors of content
        GenPolynomial<C> Pc = engine.recursiveContent(P);
        Pc = Pc.monic();
        if (!Pc.isONE()) {
            P = PolyUtil.coefficientPseudoDivide(P, Pc);
        }
        SortedMap<GenPolynomial<C>, Long> rsf = squarefreeFactors(Pc);
        // add factors of content
        for (Map.Entry<GenPolynomial<C>, Long> me : rsf.entrySet()) {
            GenPolynomial<C> c = me.getKey();
            if (!c.isONE()) {
                GenPolynomial<GenPolynomial<C>> cr = pfac.getONE().multiply(c);
                Long rk = me.getValue(); //rsf.get(c);
                sfactors.put(cr, rk);
            }
        }

        // factors of recursive polynomial
        GenPolynomial<GenPolynomial<C>> T0 = P;
        long e = 1L;
        GenPolynomial<GenPolynomial<C>> Tp;
        GenPolynomial<GenPolynomial<C>> T = null;
        GenPolynomial<GenPolynomial<C>> V = null;
        long k = 0L;
        long mp = 0L;
        boolean init = true;
        while (true) {
            if (init) {
                if (T0.isConstant() || T0.isZERO()) {
                    break;
                }
                Tp = PolyUtil.recursiveDeriviative(T0);
                T = engine.recursiveUnivariateGcd(T0, Tp);
                T = PolyUtil.monic(T);
                V = PolyUtil.recursivePseudoDivide(T0, T);
                //System.out.println("iT0 = " + T0);
                //System.out.println("iTp = " + Tp);
                //System.out.println("iT = " + T);
                //System.out.println("iV = " + V);
                k = 0L;
                mp = 0L;
                init = false;
            }
            if (V.isConstant()) {
                mp = pfac.characteristic().longValue(); // assert != 0
                //T0 = PolyUtil.<C> recursiveModRoot(T,mp);
                T0 = recursiveUnivariateRootCharacteristic(T);
                if (T0 == null) {
                    //break;
                    T0 = pfac.getZERO();
                }
                e = e * mp;
                init = true;
                //continue;
            }
            k++;
            if (mp != 0L && k % mp == 0L) {
                T = PolyUtil.recursivePseudoDivide(T, V);
                System.out.println("k = " + k);
                //System.out.println("T = " + T);
                k++;
            }
            GenPolynomial<GenPolynomial<C>> W = engine.recursiveUnivariateGcd(T, V);
            W = PolyUtil.monic(W);
            GenPolynomial<GenPolynomial<C>> z = PolyUtil.recursivePseudoDivide(V, W);
            //System.out.println("W = " + W);
            //System.out.println("z = " + z);
            V = W;
            T = PolyUtil.recursivePseudoDivide(T, V);
            //System.out.println("V = " + V);
            //System.out.println("T = " + T);
            //was: if ( z.degree(0) > 0 ) {
            if (!z.isONE() && !z.isZERO()) {
                z = PolyUtil.monic(z);
                sfactors.put(z, (e * k));
            }
        }
        if (sfactors.size() == 0) {
            sfactors.put(pfac.getONE(), 1L);
        }
        return sfactors;
    }

    /**
     * GenPolynomial squarefree factorization.
     *
     * @param P GenPolynomial.
     * @return [p_1 -&gt; e_1, ..., p_k -&gt; e_k] with P = prod_{i=1,...,k}
     * p_i^{e_i} and p_i squarefree.
     */
    SortedMap<GenPolynomial<C>, Long> squarefreeFactors(GenPolynomial<C> P) {
        if (P == null) {
            throw new IllegalArgumentException(this.getClass().getName() + " P != null");
        }
        GenPolynomialRing<C> pfac = P.ring;
        if (pfac.nvar <= 1) {
            return baseSquarefreeFactors(P);
        }
        SortedMap<GenPolynomial<C>, Long> sfactors = new TreeMap<>();
        if (P.isZERO()) {
            return sfactors;
        }
        System.exit(1);
        GenPolynomialRing<C> cfac = null;
        GenPolynomialRing<GenPolynomial<C>> rfac = new GenPolynomialRing<>(cfac);

        GenPolynomial<GenPolynomial<C>> Pr = PolyUtil.recursive(rfac, P);
        SortedMap<GenPolynomial<GenPolynomial<C>>, Long> PP = recursiveUnivariateSquarefreeFactors(Pr);

        for (Map.Entry<GenPolynomial<GenPolynomial<C>>, Long> m : PP.entrySet()) {
            Long i = m.getValue();
            GenPolynomial<GenPolynomial<C>> Dr = m.getKey();
//            GenPolynomial<C> D = PolyUtil.distribute(pfac, Dr);
//            sfactors.put(D, i);
        }
        return sfactors;
    }


    /**
     * Coefficient squarefree factorization.
     *
     * @param coeff coefficient.
     * @return [p_1 -&gt; e_1, ..., p_k -&gt; e_k] with P = prod_{i=1,...,k}
     * p_i^{e_i} and p_i squarefree.
     */
    SortedMap<C, Long> squarefreeFactors(C coeff) {
        if (coeff == null) {
            return null;
        }
        SortedMap<C, Long> factors = new TreeMap<>();
        RingFactory<C> cfac = (RingFactory<C>) coeff.factory();
        if (cfac.isFinite()) {
            SquarefreeFiniteFieldCharP<C> reng = (SquarefreeFiniteFieldCharP) SquarefreeFactory.getImplementation(cfac);
            SortedMap<C, Long> rfactors = reng.rootCharacteristic(coeff); // ??
            factors.putAll(rfactors);
        }
        return factors;
    }

    protected abstract GenPolynomial<C> baseRootCharacteristic(GenPolynomial<C> P);


    /**
     * GenPolynomial char-th root univariate polynomial with polynomial
     * coefficients.
     *
     * @param P recursive univariate GenPolynomial.
     * @return char-th_rootOf(P), or null if P is no char-th root.
     */
    protected abstract GenPolynomial<GenPolynomial<C>> recursiveUnivariateRootCharacteristic(
            GenPolynomial<GenPolynomial<C>> P);

}
