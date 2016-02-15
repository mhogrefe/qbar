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
            SquarefreeFiniteFieldCharP<C> reng =
                    (SquarefreeFiniteFieldCharP) SquarefreeFactory.getImplementation(cfac);
            SortedMap<C, Long> rfactors = reng.rootCharacteristic(coeff); // ??
            factors.putAll(rfactors);
        }
        return factors;
    }

    protected abstract GenPolynomial<C> baseRootCharacteristic(GenPolynomial<C> P);
}
