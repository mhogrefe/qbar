package jas.poly;

import jas.arith.JasBigInteger;
import jas.arith.ModInteger;
import jas.arith.ModLong;
import jas.arith.Modular;
import jas.structure.RingElem;
import jas.structure.RingFactory;
import mho.wheels.iterables.IterableUtils;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.function.Function;

/**
 * Polynomial utilities, for example conversion between different
 * representations, evaluation and interpolation.
 *
 * @author Heinz Kredel
 */
public class PolyUtil {
    //
    //JasBigInteger from ModInteger coefficients, symmetric. Represent as
    //polynomial with JasBigInteger coefficients by removing the modules and
    //making coefficients symmetric to 0.
    //
    //@param fac result polynomial factory.
    //@param A   polynomial with ModInteger coefficients to be converted.
    //@return polynomial with JasBigInteger coefficients.
    //
    @Deprecated
    public static <C extends RingElem<C> & Modular> GenPolynomial<JasBigInteger> integerFromModularCoefficients(
            GenPolynomialRing<JasBigInteger> fac, GenPolynomial<C> A) {
        if (A.elementClass() != ModLong.class && A.elementClass() != ModInteger.class) {
            System.out.println(A.elementClass());
        }
        return PolyUtil.map(fac, A, new ModSymToInt<>());
    }

    //
    //JasBigInteger from ModInteger coefficients, symmetric. Represent as
    //polynomial with JasBigInteger coefficients by removing the modules and
    //making coefficients symmetric to 0.
    //
    //@param fac result polynomial factory.
    //@param L   list of polynomials with ModInteger coefficients to be
    //           converted.
    //@return list of polynomials with JasBigInteger coefficients.
    @Deprecated
    public static <C extends RingElem<C> & Modular> List<GenPolynomial<JasBigInteger>> integerFromModularCoefficients(
            final GenPolynomialRing<JasBigInteger> fac, List<GenPolynomial<C>> L) {
        //C must be ModLong
        return IterableUtils.toList(IterableUtils.map(c -> PolyUtil.integerFromModularCoefficients(fac, c), L));
    }

    /**
     * From JasBigInteger coefficients. Represent as polynomial with type C
     * coefficients, e.g. ModInteger or BigRational.
     *
     * @param <C> coefficient type.
     * @param fac result polynomial factory.
     * @param A   polynomial with JasBigInteger coefficients to be converted.
     * @return polynomial with type C coefficients.
     */
    public static <C extends RingElem<C>> GenPolynomial<C> fromIntegerCoefficients(
            GenPolynomialRing<C> fac,
            GenPolynomial<JasBigInteger> A
    ) {
        return PolyUtil.map(fac, A, new FromInteger<>(fac.coFac));
    }

    /**
     * From JasBigInteger coefficients. Represent as list of polynomials with type
     * C coefficients, e.g. ModInteger or BigRational.
     *
     * @param <C> coefficient type.
     * @param fac result polynomial factory.
     * @param L   list of polynomials with JasBigInteger coefficients to be
     *            converted.
     * @return list of polynomials with type C coefficients.
     */
    public static <C extends RingElem<C>> List<GenPolynomial<C>> fromIntegerCoefficients(
            GenPolynomialRing<C> fac,
            List<GenPolynomial<JasBigInteger>> L
    ) {
        return IterableUtils.toList(IterableUtils.map(new FromIntegerPoly<>(fac), L));
    }

    /**
     * Polynomial list monic.
     *
     * @param <C> coefficient type.
     * @param L   list of polynomials with field coefficients.
     * @return list of polynomials with leading coefficient 1.
     */
    public static <C extends RingElem<C>> List<GenPolynomial<C>> monic(List<GenPolynomial<C>> L) {
        return IterableUtils.toList(IterableUtils.map(c -> c == null ? null : c.monic(), L));
    }

    /**
     * Polynomial list leading exponent vectors.
     *
     * @param <C> coefficient type.
     * @param L   list of polynomials.
     * @return list of leading exponent vectors.
     */
    public static <C extends RingElem<C>> List<Long> leadingExpVector(List<GenPolynomial<C>> L) {
        return IterableUtils.toList(IterableUtils.map(c -> c == null ? null : c.leadingExpVector(), L));
    }

    //
    //GenPolynomial sparse pseudo remainder. For univariate polynomials.
    //
    //@param <C> coefficient type.
    //@param P   GenPolynomial.
    //@param S   nonzero GenPolynomial.
    //@return remainder with ldcf(S)<sup>m'</sup> P = quotient * S + remainder.
    //m' &le; deg(P)-deg(S)
    //@see jas.poly.GenPolynomial#remainder(jas.poly.GenPolynomial).
    //
    public static <C extends RingElem<C>> GenPolynomial<C> baseSparsePseudoRemainder(
            GenPolynomial<C> P,
            GenPolynomial<C> S
    ) {
        if (S == null || S.isZERO()) {
            throw new ArithmeticException(P.toString() + " division by zero " + S);
        }
        if (P.isZERO()) {
            return P;
        }
        if (S.isConstant()) {
            return P.ring.getZERO();
        }
        C c = S.leadingBaseCoefficient();
        long e = S.leadingExpVector();
        GenPolynomial<C> h;
        GenPolynomial<C> r = P;
        while (!r.isZERO()) {
            long f = r.leadingExpVector();
            if (f >= e) {
                C a = r.leadingBaseCoefficient();
                f = f - e;
                C x = a.remainder(c);
                if (x.isZERO()) {
                    C y = a.divide(c);
                    h = S.multiply(y, f); // coeff a
                } else {
                    r = r.multiply(c); // coeff ac
                    h = S.multiply(a, f); // coeff ac
                }
                r = r.subtract(h);
            } else {
                break;
            }
        }
        return r;
    }

    //
    //GenPolynomial dense pseudo remainder. For univariate polynomials.
    //
    //@param P GenPolynomial.
    //@param S nonzero GenPolynomial.
    //@return remainder with ldcf(S)<sup>m</sup> P = quotient * S + remainder.
    //m == deg(P)-deg(S)
    //@see jas.poly.GenPolynomial#remainder(jas.poly.GenPolynomial).
    //
    public static <C extends RingElem<C>> GenPolynomial<C> baseDensePseudoRemainder(GenPolynomial<C> P,
                                                                                    GenPolynomial<C> S) {
        if (S == null || S.isZERO()) {
            throw new ArithmeticException(P + " division by zero " + S);
        }
        if (P.isZERO()) {
            return P;
        }
        if (S.isConstant()) {
            return P.ring.getZERO();
        }
        long m = P.degree();
        long n = S.degree();
        C c = S.leadingBaseCoefficient();
        long e = S.leadingExpVector();
        GenPolynomial<C> h;
        GenPolynomial<C> r = P;
        for (long i = m; i >= n; i--) {
            if (r.isZERO()) {
                return r;
            }
            long k = r.degree();
            if (i == k) {
                long f = r.leadingExpVector();
                C a = r.leadingBaseCoefficient();
                f = f - e;
                //System.out.println("red div = " + f);
                r = r.multiply(c); // coeff ac
                h = S.multiply(a, f); // coeff ac
                r = r.subtract(h);
            } else {
                r = r.multiply(c);
            }
        }
        return r;
    }

    //
    //GenPolynomial sparse pseudo divide. For univariate polynomials or exact
    //division.
    //
    //@param <C> coefficient type.
    //@param P   GenPolynomial.
    //@param S   nonzero GenPolynomial.
    //@return quotient with ldcf(S)<sup>m'</sup> P = quotient * S + remainder.
    //m' &le; deg(P)-deg(S)
    //@see jas.poly.GenPolynomial#divide(jas.poly.GenPolynomial).
    //
    public static <C extends RingElem<C>> GenPolynomial<C> basePseudoDivide(GenPolynomial<C> P,
                                                                            GenPolynomial<C> S) {
        if (S == null || S.isZERO()) {
            throw new ArithmeticException(P.toString() + " division by zero " + S);
        }
        //if (S.ring.nvar != 1) {
        // ok if exact division
        // throw new RuntimeException(this.getClass().getName()
        //                            + " univariate polynomials only");
        //}
        if (P.isZERO() || S.isONE()) {
            return P;
        }
        C c = S.leadingBaseCoefficient();
        long e = S.leadingExpVector();
        GenPolynomial<C> h;
        GenPolynomial<C> r = P;
        GenPolynomial<C> q = S.ring.getZERO().copy();

        while (!r.isZERO()) {
            long f = r.leadingExpVector();
            if (f >= e) {
                C a = r.leadingBaseCoefficient();
                f = f - e;
                C x = a.remainder(c);
                if (x.isZERO()) {
                    C y = a.divide(c);
                    q = q.sum(y, f);
                    h = S.multiply(y, f); // coeff a
                } else {
                    q = q.multiply(c);
                    q = q.sum(a, f);
                    r = r.multiply(c); // coeff ac
                    h = S.multiply(a, f); // coeff ac
                }
                r = r.subtract(h);
            } else {
                break;
            }
        }
        return q;
    }

    /**
     * GenPolynomial polynomial derivative main variable.
     *
     * @param <C> coefficient type.
     * @param P   GenPolynomial.
     * @return deriviative(P).
     */
    public static <C extends RingElem<C>> GenPolynomial<C> baseDeriviative(GenPolynomial<C> P) {
        if (P == null || P.isZERO()) {
            return P;
        }
        GenPolynomialRing<C> pfac = P.ring;
        RingFactory<C> rf = pfac.coFac;
        GenPolynomial<C> d = pfac.getZERO().copy();
        Map<Long, C> dm = d.val; //getMap();
        for (Map.Entry<Long, C> m : P.getMap().entrySet()) {
            long f = m.getKey();
            if (f > 0) {
                C cf = rf.fromInteger(f);
                C a = m.getValue();
                C x = a.multiply(cf);
                if (x != null && !x.isZERO()) {
                    long e = f - 1L;
                    dm.put(e, x);
                }
            }
        }
        return d;
    }

    /**
     * Factor coefficient bound. See SACIPOL.IPFCB: the product of all maxNorms
     * of potential factors is less than or equal to 2**b times the maxNorm of
     * A.
     *
     * @param e degree vector of a GenPolynomial A.
     * @return 2**b.
     */
    public static JasBigInteger factorBound(long e) {
        int n = 0;
        BigInteger p = BigInteger.ONE;
        BigInteger v;
        if (e == -1 || e == 0) {
            return JasBigInteger.ONE;
        }
        if (e > 0) {
            n += (2 * e - 1);
            v = new BigInteger("" + (e - 1));
            p = p.multiply(v);
        }
        n += (p.bitCount() + 1); // log2(p)
        n /= 2;
        v = new BigInteger("" + 2);
        v = v.shiftLeft(n);
        return new JasBigInteger(v);
    }

    //
    //Map a unary function to the coefficients.
    //
    //@param ring result polynomial ring factory.
    //@param p    polynomial.
    //@param f    evaluation functor.
    //@return new polynomial with coefficients f(p(e)).
    //
    public static <C extends RingElem<C>, D extends RingElem<D>> GenPolynomial<D> map(
            GenPolynomialRing<D> ring, GenPolynomial<C> p, Function<C, D> f) {
        GenPolynomial<D> n = ring.getZERO().copy();
        SortedMap<Long, D> nv = n.val;
        for (SortedMap.Entry<Long, C> m : p) {
            D c = f.apply(m.getValue());
            if (c != null && !c.isZERO()) {
                nv.put(m.getKey(), c);
            }
        }
        return n;
    }
}

/**
 * Conversion of symmetric ModInteger to JasBigInteger functor.
 */
class ModSymToInt<C extends RingElem<C> & Modular> implements Function<C, JasBigInteger> {
    public JasBigInteger apply(C c) {
        if (c == null) {
            return new JasBigInteger();
        }
        return c.getSymmetricInteger();
    }
}

/**
 * Conversion from JasBigInteger functor.
 */
class FromInteger<D extends RingElem<D>> implements Function<JasBigInteger, D> {
    private final RingFactory<D> ring;

    public FromInteger(RingFactory<D> ring) {
        this.ring = ring;
    }

    public D apply(JasBigInteger c) {
        if (c == null) {
            return ring.getZERO();
        }
        return ring.fromInteger(c.getVal());
    }
}

/**
 * Conversion from GenPolynomial<JasBigInteger> functor.
 */
class FromIntegerPoly<D extends RingElem<D>> implements
        Function<GenPolynomial<JasBigInteger>, GenPolynomial<D>> {
    private GenPolynomialRing<D> ring;

    private FromInteger<D> fi;

    public FromIntegerPoly(GenPolynomialRing<D> ring) {
        if (ring == null) {
            throw new IllegalArgumentException("ring must not be null");
        }
        this.ring = ring;
        fi = new FromInteger<>(ring.coFac);
    }

    public GenPolynomial<D> apply(GenPolynomial<JasBigInteger> c) {
        if (c == null) {
            return ring.getZERO();
        }
        return PolyUtil.map(ring, c, fi);
    }
}
