package jas.ufd;

import jas.poly.GenPolynomial;
import jas.poly.GenPolynomialRing;
import jas.poly.PolyUtil;
import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Squarefree decomposition for coefficient rings of characteristic 0.
 *
 * @author Heinz Kredel
 */
public class SquarefreeRingChar0<C extends RingElem<C>> extends SquarefreeAbstract<C> /*implements Squarefree<C>*/ {
    /**
     * Factory for ring of characteristic 0 coefficients.
     */
    private final RingFactory<C> coFac;

    public SquarefreeRingChar0(RingFactory<C> fac) {
        super(GCDFactory.<C>getProxy(fac));
        if (fac.isField()) {
            throw new IllegalArgumentException("fac is a field: use SquarefreeFieldChar0");
        }
        if (fac.characteristic().signum() != 0) {
            throw new IllegalArgumentException("characterisic(fac) must be zero");
        }
        coFac = fac;
    }

    @Override
    public SortedMap<GenPolynomial<C>, Long> baseSquarefreeFactors(GenPolynomial<C> A) {
        SortedMap<GenPolynomial<C>, Long> sfactors = new TreeMap<>();
        if (A == null || A.isZERO()) {
            return sfactors;
        }
        if (A.isConstant()) {
            sfactors.put(A, 1L);
            return sfactors;
        }
        GenPolynomialRing<C> pfac = A.ring;
        if (pfac.nvar > 1) {
            throw new IllegalArgumentException(this.getClass().getName() + " only for univariate polynomials");
        }
        C ldbcf = A.leadingBaseCoefficient();
        if (!ldbcf.isONE()) {
            C cc = engine.baseContent(A);
            A = A.divide(cc);
            GenPolynomial<C> f1 = pfac.getONE().multiply(cc);
            //System.out.println("gcda sqf f1 = " + f1);
            sfactors.put(f1, 1L);
        }
        GenPolynomial<C> T0 = A;
        GenPolynomial<C> Tp;
        GenPolynomial<C> T = null;
        GenPolynomial<C> V = null;
        long k = 0L;
        boolean init = true;
        while (true) {
            if (init) {
                if (T0.isConstant() || T0.isZERO()) {
                    break;
                }
                Tp = PolyUtil.baseDeriviative(T0);
                T = engine.baseGcd(T0, Tp);
                T = engine.basePrimitivePart(T);
                V = PolyUtil.basePseudoDivide(T0, T);
                //System.out.println("iT0 = " + T0);
                //System.out.println("iTp = " + Tp);
                //System.out.println("iT  = " + T);
                //System.out.println("iV  = " + V);
                k = 0L;
                init = false;
            }
            if (V.isConstant()) {
                break;
            }
            k++;
            GenPolynomial<C> W = engine.baseGcd(T, V);
            W = engine.basePrimitivePart(W);
            GenPolynomial<C> z = PolyUtil.basePseudoDivide(V, W);
            //System.out.println("W = " + W);
            //System.out.println("z = " + z);
            V = W;
            T = PolyUtil.basePseudoDivide(T, V);
            //System.out.println("V = " + V);
            //System.out.println("T = " + T);
            if (z.degree(0) > 0) {
                if (ldbcf.isONE() && !z.leadingBaseCoefficient().isONE()) {
                    z = engine.basePrimitivePart(z);
                }
                sfactors.put(z, k);
            }
        }
        return normalizeFactorization(sfactors);
    }
}
