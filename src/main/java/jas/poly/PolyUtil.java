package jas.poly;

import jas.arith.JasBigInteger;
import jas.arith.Modular;
import jas.arith.ModularRingFactory;
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
    /**
     * Recursive representation. Represent as polynomial in i variables with
     * coefficients in n-i variables. Works for arbitrary term orders.
     *
     * @param <C>  coefficient type.
     * @param rfac recursive polynomial ring factory.
     * @param A    polynomial to be converted.
     * @return Recursive represenations of this in the ring rfac.
     */
    public static <C extends RingElem<C>> GenPolynomial<GenPolynomial<C>> recursive(
            GenPolynomialRing<GenPolynomial<C>> rfac, GenPolynomial<C> A) {
        GenPolynomial<GenPolynomial<C>> B = rfac.getZERO().copy();
        if (A.isZERO()) {
            return B;
        }
        int i = rfac.nvar;
        GenPolynomial<C> zero = rfac.getZEROCoefficient();
        Map<ExpVector, GenPolynomial<C>> Bv = B.val; //getMap();
        for (Map.Entry<ExpVector, C> y : A.getMap().entrySet()) {
            ExpVector e = y.getKey();
            C a = y.getValue();
            System.exit(1);
            ExpVector f = null;
            ExpVector g = null;
            GenPolynomial<C> p = Bv.get(f);
            if (p == null) {
                p = zero;
            }
            p = p.sum(a, g);
            Bv.put(f, p);
        }
        return B;
    }

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

    //
    //ModInteger chinese remainder algorithm on coefficients.
    //
    //@param fac GenPolynomial&lt;ModInteger&gt; result factory with
    //           A.coFac.modul*B.coFac.modul = C.coFac.modul.
    //@param A   GenPolynomial&lt;ModInteger&gt;.
    //@param B   other GenPolynomial&lt;ModInteger&gt;.
    //@param mi  inverse of A.coFac.modul in ring B.coFac.
    //@return S = cra(A,B), with S mod A.coFac.modul == A and S mod
    //B.coFac.modul == B.
    //
    @Deprecated
    public static <C extends RingElem<C> & Modular> GenPolynomial<C> chineseRemainder(
            GenPolynomialRing<C> fac, GenPolynomial<C> A, C mi, GenPolynomial<C> B) {
        ModularRingFactory<C> cfac = (ModularRingFactory<C>) fac.coFac; // get RingFactory
        GenPolynomial<C> S = fac.getZERO().copy();
        GenPolynomial<C> Ap = A.copy();
        SortedMap<ExpVector, C> av = Ap.val; //getMap();
        SortedMap<ExpVector, C> bv = B.getMap();
        SortedMap<ExpVector, C> sv = S.val; //getMap();
        C c;
        for (Map.Entry<ExpVector, C> me : bv.entrySet()) {
            ExpVector e = me.getKey();
            C y = me.getValue(); //bv.get(e); // assert y != null
            C x = av.get(e);
            if (x != null) {
                av.remove(e);
                c = cfac.chineseRemainder(x, mi, y);
                if (!c.isZERO()) { // 0 cannot happen
                    sv.put(e, c);
                }
            } else {
                //c = cfac.fromInteger( y.getVal() );
                c = cfac.chineseRemainder(A.ring.coFac.getZERO(), mi, y);
                if (!c.isZERO()) { // 0 cannot happen
                    sv.put(e, c); // c != null
                }
            }
        }
        // assert bv is empty = done
        for (Map.Entry<ExpVector, C> me : av.entrySet()) { // rest of av
            ExpVector e = me.getKey();
            C x = me.getValue(); // av.get(e); // assert x != null
            //c = cfac.fromInteger( x.getVal() );
            c = cfac.chineseRemainder(x, mi, B.ring.coFac.getZERO());
            if (!c.isZERO()) { // 0 cannot happen
                sv.put(e, c); // c != null
            }
        }
        return S;
    }

    //GenPolynomial monic, i.e. leadingBaseCoefficient == 1. If
    //leadingBaseCoefficient is not invertible returns this unmodified.
    //
    //@param <C> coefficient type.
    //@param p   recursive GenPolynomial<GenPolynomial<C>>.
    //@return monic(p).
    //
    public static <C extends RingElem<C>> GenPolynomial<GenPolynomial<C>> monic(
            GenPolynomial<GenPolynomial<C>> p) {
        if (p == null || p.isZERO()) {
            return p;
        }
        C lc = p.leadingBaseCoefficient().leadingBaseCoefficient();
        if (!lc.isUnit()) {
            return p;
        }
        C lm = lc.inverse();
        GenPolynomial<C> L = p.ring.coFac.getONE();
        L = L.multiply(lm);
        return p.multiply(L);
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
    public static <C extends RingElem<C>> List<ExpVector> leadingExpVector(List<GenPolynomial<C>> L) {
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
        ExpVector e = S.leadingExpVector();
        GenPolynomial<C> h;
        GenPolynomial<C> r = P;
        while (!r.isZERO()) {
            ExpVector f = r.leadingExpVector();
            if (f.val >= e.val) {
                C a = r.leadingBaseCoefficient();
                f = new ExpVector(f.val - e.val);
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
        long m = P.degree(0);
        long n = S.degree(0);
        C c = S.leadingBaseCoefficient();
        ExpVector e = S.leadingExpVector();
        GenPolynomial<C> h;
        GenPolynomial<C> r = P;
        for (long i = m; i >= n; i--) {
            if (r.isZERO()) {
                return r;
            }
            long k = r.degree(0);
            if (i == k) {
                ExpVector f = r.leadingExpVector();
                C a = r.leadingBaseCoefficient();
                f = new ExpVector(f.val - e.val);
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
        ExpVector e = S.leadingExpVector();
        GenPolynomial<C> h;
        GenPolynomial<C> r = P;
        GenPolynomial<C> q = S.ring.getZERO().copy();

        while (!r.isZERO()) {
            ExpVector f = r.leadingExpVector();
            if (f.val >= e.val) {
                C a = r.leadingBaseCoefficient();
                f = new ExpVector(f.val - e.val);
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
     * GenPolynomial divide. For recursive polynomials. Division by coefficient
     * ring element.
     *
     * @param <C> coefficient type.
     * @param P   recursive GenPolynomial.
     * @param s   GenPolynomial.
     * @return this/s.
     */
    public static <C extends RingElem<C>> GenPolynomial<GenPolynomial<C>> recursiveDivide(
            GenPolynomial<GenPolynomial<C>> P, GenPolynomial<C> s) {
        if (s == null || s.isZERO()) {
            throw new ArithmeticException("division by zero " + P + ", " + s);
        }
        if (P.isZERO()) {
            return P;
        }
        if (s.isONE()) {
            return P;
        }
        GenPolynomial<GenPolynomial<C>> p = P.ring.getZERO().copy();
        SortedMap<ExpVector, GenPolynomial<C>> pv = p.val; //getMap();
        for (Map.Entry<ExpVector, GenPolynomial<C>> m1 : P.getMap().entrySet()) {
            GenPolynomial<C> c1 = m1.getValue();
            ExpVector e1 = m1.getKey();
            GenPolynomial<C> c = PolyUtil.basePseudoDivide(c1, s);
            if (!c.isZERO()) {
                pv.put(e1, c); // or m1.setValue( c )
            } else {
                System.out.println("rDiv, P  = " + P);
                System.out.println("rDiv, c1 = " + c1);
                System.out.println("rDiv, s  = " + s);
                System.out.println("rDiv, c  = " + c);
                throw new RuntimeException("something is wrong");
            }
        }
        return p;
    }

    //
    //GenPolynomial sparse pseudo remainder. For recursive polynomials.
    //
    //@param <C> coefficient type.
    //@param P   recursive GenPolynomial.
    //@param S   nonzero recursive GenPolynomial.
    //@return remainder with ldcf(S)<sup>m'</sup> P = quotient * S + remainder.
    //@see jas.poly.GenPolynomial#remainder(jas.poly.GenPolynomial).
    //@deprecated Use
    //{@link #recursiveSparsePseudoRemainder(jas.poly.GenPolynomial, jas.poly.GenPolynomial)}
    //instead
    //
    @Deprecated
    public static <C extends RingElem<C>> GenPolynomial<GenPolynomial<C>> recursivePseudoRemainder(
            GenPolynomial<GenPolynomial<C>> P, GenPolynomial<GenPolynomial<C>> S) {
        return recursiveSparsePseudoRemainder(P, S);
    }

    //
    //GenPolynomial sparse pseudo remainder. For recursive polynomials.
    //
    //@param <C> coefficient type.
    //@param P   recursive GenPolynomial.
    //@param S   nonzero recursive GenPolynomial.
    //@return remainder with ldcf(S)<sup>m'</sup> P = quotient * S + remainder.
    //@see jas.poly.GenPolynomial#remainder(jas.poly.GenPolynomial).
    //
    public static <C extends RingElem<C>> GenPolynomial<GenPolynomial<C>> recursiveSparsePseudoRemainder(
            GenPolynomial<GenPolynomial<C>> P, GenPolynomial<GenPolynomial<C>> S) {
        if (S == null || S.isZERO()) {
            throw new ArithmeticException(P + " division by zero " + S);
        }
        if (P == null || P.isZERO()) {
            return P;
        }
        if (S.isConstant()) {
            return P.ring.getZERO();
        }
        GenPolynomial<C> c = S.leadingBaseCoefficient();
        ExpVector e = S.leadingExpVector();
        GenPolynomial<GenPolynomial<C>> h;
        GenPolynomial<GenPolynomial<C>> r = P;
        while (!r.isZERO()) {
            ExpVector f = r.leadingExpVector();
            if (f.val >= e.val) {
                GenPolynomial<C> a = r.leadingBaseCoefficient();
                f = new ExpVector(f.val - e.val);
                if (c.isZERO()) {
                    GenPolynomial<C> y = PolyUtil.basePseudoDivide(a, c);
                    h = S.multiply(y, f); // coeff a
                } else {
                    r = r.multiply(c);    // coeff a c
                    h = S.multiply(a, f); // coeff c a
                }
                r = r.subtract(h);
            } else {
                break;
            }
        }
        return r;
    }

    //
    //GenPolynomial dense pseudo remainder. For recursive polynomials.
    //
    //@param P recursive GenPolynomial.
    //@param S nonzero recursive GenPolynomial.
    //@return remainder with ldcf(S)<sup>m'</sup> P = quotient * S + remainder.
    //@see jas.poly.GenPolynomial#remainder(jas.poly.GenPolynomial).
    //
    public static <C extends RingElem<C>> GenPolynomial<GenPolynomial<C>> recursiveDensePseudoRemainder(
            GenPolynomial<GenPolynomial<C>> P, GenPolynomial<GenPolynomial<C>> S) {
        if (S == null || S.isZERO()) {
            throw new ArithmeticException(P + " division by zero " + S);
        }
        if (P == null || P.isZERO()) {
            return P;
        }
        if (S.isConstant()) {
            return P.ring.getZERO();
        }
        long m = P.degree(0);
        long n = S.degree(0);
        GenPolynomial<C> c = S.leadingBaseCoefficient();
        ExpVector e = S.leadingExpVector();
        GenPolynomial<GenPolynomial<C>> h;
        GenPolynomial<GenPolynomial<C>> r = P;
        for (long i = m; i >= n; i--) {
            if (r.isZERO()) {
                return r;
            }
            long k = r.degree(0);
            if (i == k) {
                ExpVector f = r.leadingExpVector();
                GenPolynomial<C> a = r.leadingBaseCoefficient();
                f = new ExpVector(f.val - e.val);
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
        if (pfac.nvar > 1) {
            // baseContent not possible by return type
            throw new IllegalArgumentException(P.getClass().getName() + " only for univariate polynomials");
        }
        RingFactory<C> rf = pfac.coFac;
        GenPolynomial<C> d = pfac.getZERO().copy();
        Map<ExpVector, C> dm = d.val; //getMap();
        for (Map.Entry<ExpVector, C> m : P.getMap().entrySet()) {
            ExpVector f = m.getKey();
            long fl = f.val;
            if (fl > 0) {
                C cf = rf.fromInteger(fl);
                C a = m.getValue();
                C x = a.multiply(cf);
                if (x != null && !x.isZERO()) {
                    ExpVector e = new ExpVector(fl - 1L);
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
    public static JasBigInteger factorBound(ExpVector e) {
        int n = 0;
        BigInteger p = BigInteger.ONE;
        BigInteger v;
        if (e == null || e.val == 0) {
            return JasBigInteger.ONE;
        }
        if (e.val > 0) {
            n += (2 * e.val - 1);
            v = new BigInteger("" + (e.val - 1));
            p = p.multiply(v);
        }
        n += (p.bitCount() + 1); // log2(p)
        n /= 2;
        v = new BigInteger("" + 2);
        v = v.shiftLeft(n);
        return new JasBigInteger(v);
    }

    /**
     * Evaluate at main variable.
     *
     * @param <C>  coefficient type.
     * @param cfac coefficent ring factory.
     * @param A    univariate polynomial to be evaluated.
     * @param a    value to evaluate at.
     * @return A(a).
     */
    public static <C extends RingElem<C>> C evaluateMain(RingFactory<C> cfac, GenPolynomial<C> A, C a) {
        if (A == null || A.isZERO()) {
            return cfac.getZERO();
        }
        if (A.ring.nvar != 1) { // todo assert
            throw new IllegalArgumentException("evaluateMain no univariate polynomial");
        }
        if (a == null || a.isZERO()) {
            return A.trailingBaseCoefficient();
        }
        // assert decreasing exponents, i.e. compatible term order
        Map<ExpVector, C> val = A.getMap();
        C B = null;
        long el1 = -1; // undefined
        long el2 = -1;
        for (Map.Entry<ExpVector, C> me : val.entrySet()) {
            ExpVector e = me.getKey();
            el2 = e.val;
            if (B == null /*el1 < 0*/) { // first turn
                B = me.getValue(); // val.get(e);
            } else {
                for (long i = el2; i < el1; i++) {
                    B = B.multiply(a);
                }
                B = B.sum(me.getValue()); //val.get(e));
            }
            el1 = el2;
        }
        for (long i = 0; i < el2; i++) {
            B = B.multiply(a);
        }
        return B;
    }

    /**
     * Evaluate at first (lowest) variable.
     *
     * @param <C>  coefficient type. Could also be called evaluateFirst(), but
     *             type erasure of A parameter does not allow same name.
     * @param cfac coefficient polynomial ring in first variable C[x_1] factory.
     * @param dfac polynomial ring in n-1 variables. C[x_2, ..., x_n] factory.
     * @param A    recursive polynomial to be evaluated.
     * @param a    value to evaluate at.
     * @return A(a, x_2, ..., x_n).
     */
    public static <C extends RingElem<C>> GenPolynomial<C> evaluateFirstRec(
            GenPolynomialRing<C> cfac,
            GenPolynomialRing<C> dfac,
            GenPolynomial<GenPolynomial<C>> A,
            C a
    ) {
        if (A == null || A.isZERO()) {
            return dfac.getZERO();
        }
        Map<ExpVector, GenPolynomial<C>> Ap = A.getMap();
        GenPolynomial<C> B = dfac.getZERO().copy();
        Map<ExpVector, C> Bm = B.val; //getMap();
        for (Map.Entry<ExpVector, GenPolynomial<C>> m : Ap.entrySet()) {
            ExpVector e = m.getKey();
            GenPolynomial<C> b = m.getValue();
            C d = evaluateMain(cfac.coFac, b, a);
            if (d != null && !d.isZERO()) {
                Bm.put(e, d);
            }
        }
        return B;
    }

    //
    //ModInteger interpolate on first variable.
    //
    //@param <C> coefficient type.
    //@param fac GenPolynomial<C> result factory.
    //@param A   GenPolynomial.
    //@param M   GenPolynomial interpolation modul of A.
    //@param mi  inverse of M(am) in ring fac.coFac.
    //@param B   evaluation of other GenPolynomial.
    //@param am  evaluation point (interpolation modul) of B, i.e. P(am) = B.
    //@return S, with S mod M == A and S(am) == B.
    //
    public static <C extends RingElem<C>> GenPolynomial<GenPolynomial<C>> interpolate(
            GenPolynomialRing<GenPolynomial<C>> fac, GenPolynomial<GenPolynomial<C>> A,
            GenPolynomial<C> M, C mi, GenPolynomial<C> B, C am) {
        GenPolynomial<GenPolynomial<C>> S = fac.getZERO().copy();
        GenPolynomial<GenPolynomial<C>> Ap = A.copy();
        SortedMap<ExpVector, GenPolynomial<C>> av = Ap.val; //getMap();
        SortedMap<ExpVector, C> bv = B.getMap();
        SortedMap<ExpVector, GenPolynomial<C>> sv = S.val; //getMap();
        GenPolynomialRing<C> cfac = (GenPolynomialRing<C>) fac.coFac;
        RingFactory<C> bfac = cfac.coFac;
        GenPolynomial<C> c;
        for (Map.Entry<ExpVector, C> me : bv.entrySet()) {
            ExpVector e = me.getKey();
            C y = me.getValue(); //bv.get(e); // assert y != null
            GenPolynomial<C> x = av.get(e);
            if (x != null) {
                av.remove(e);
                c = PolyUtil.interpolate(cfac, x, M, mi, y, am);
                if (!c.isZERO()) { // 0 cannot happen
                    sv.put(e, c);
                }
            } else {
                c = PolyUtil.interpolate(cfac, cfac.getZERO(), M, mi, y, am);
                if (!c.isZERO()) { // 0 cannot happen
                    sv.put(e, c); // c != null
                }
            }
        }
        // assert bv is empty = done
        for (Map.Entry<ExpVector, GenPolynomial<C>> me : av.entrySet()) { // rest of av
            ExpVector e = me.getKey();
            GenPolynomial<C> x = me.getValue(); //av.get(e); // assert x != null
            c = PolyUtil.interpolate(cfac, x, M, mi, bfac.getZERO(), am);
            if (!c.isZERO()) { // 0 cannot happen
                sv.put(e, c); // c != null
            }
        }
        return S;
    }

    private static <C extends RingElem<C>> GenPolynomial<C> interpolate(
            GenPolynomialRing<C> fac,
            GenPolynomial<C> A,
            GenPolynomial<C> M,
            C mi,
            C a,
            C am
    ) {
        GenPolynomial<C> s;
        C b = PolyUtil.evaluateMain(fac.coFac, A, am);
        C d = a.subtract(b); // a-A mod a.modul
        if (d.isZERO()) {
            return A;
        }
        b = d.multiply(mi); // b = (a-A)*mi mod a.modul
        s = M.multiply(b);
        s = s.sum(A);
        return s;
    }

    //
    //Maximal degree in the coefficient polynomials.
    //
    //@param <C> coefficient type.
    //@return maximal degree in the coefficients.
    //
    public static <C extends RingElem<C>> long coeffMaxDegree(GenPolynomial<GenPolynomial<C>> A) {
        if (A.isZERO()) {
            return 0; // 0 or -1 ?;
        }
        long deg = 0;
        for (GenPolynomial<C> a : A.getMap().values()) {
            long d = a.degree();
            if (d > deg) {
                deg = d;
            }
        }
        return deg;
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
        SortedMap<ExpVector, D> nv = n.val;
        for (Monomial<C> m : p) {
            D c = f.apply(m.c);
            if (c != null && !c.isZERO()) {
                nv.put(m.e, c);
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
