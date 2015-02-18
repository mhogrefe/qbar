package jas.poly;

import jas.structure.RingElem;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * GenSolvablePolynomial generic solvable polynomials implementing RingElem.
 * n-variate ordered solvable polynomials over C. Objects of this class are
 * intended to be immutable. The implementation is based on TreeMap respectively
 * SortedMap from exponents to coefficients by extension of GenPolybomial. Only
 * the coefficients are modeled with generic types, the exponents are fixed to
 * ExpVector with long entries (this will eventually be changed in the future).
 *
 * @param <C> coefficient type
 * @author Heinz Kredel
 */

public class GenSolvablePolynomial<C extends RingElem<C>> extends GenPolynomial<C> {
    private final GenSolvablePolynomialRing<C> ring;

    public GenSolvablePolynomial(GenSolvablePolynomialRing<C> r) {
        super(r);
        ring = r;
    }

    public GenSolvablePolynomial(GenSolvablePolynomialRing<C> r, C c, ExpVector e) {
        this(r);
        if (c != null && !c.isZERO()) {
            val.put(e, c);
        }
    }

    GenSolvablePolynomial(GenSolvablePolynomialRing<C> r, SortedMap<ExpVector, C> v) {
        this(r);
        val.putAll(v); // assume no zero coefficients
    }

    @Override
    public GenSolvablePolynomialRing<C> factory() {
        return ring;
    }


    /**
     * Clone this GenSolvablePolynomial.
     *
     * @see java.lang.Object#clone()
     */
    @Override
    public GenSolvablePolynomial<C> copy() {
        return new GenSolvablePolynomial<>(ring, this.val);
    }


    /**
     * Comparison with any other object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object B) {
        return B instanceof GenSolvablePolynomial && super.equals(B);
    }


    /**
     * GenSolvablePolynomial multiplication.
     *
     * @param Bp GenSolvablePolynomial.
     * @return this*Bp, where * denotes solvable multiplication.
     */
    // not @Override
    public GenSolvablePolynomial<C> multiply(GenSolvablePolynomial<C> Bp) {
        if (Bp == null || Bp.isZERO()) {
            return ring.getZERO();
        }
        if (this.isZERO()) {
            return this;
        }
        assert (ring.nvar == Bp.ring.nvar);
        GenSolvablePolynomial<C> Cp = ring.getZERO().copy();
        GenSolvablePolynomial<C> zero = ring.getZERO().copy();
        C one = ring.getONECoefficient();
        Map<ExpVector, C> A = val;
        Map<ExpVector, C> B = Bp.val;
        Set<Map.Entry<ExpVector, C>> Bk = B.entrySet();
        for (Map.Entry<ExpVector, C> y : A.entrySet()) {
            C a = y.getValue();
            ExpVector e = y.getKey();
            int[] ep = e.dependencyOnVariables();
            int el1 = ring.nvar + 1;
            if (ep.length > 0) {
                el1 = ep[0];
            }
            int el1s = ring.nvar + 1 - el1;
            for (Map.Entry<ExpVector, C> x : Bk) {
                C b = x.getValue();
                ExpVector f = x.getKey();
                int[] fp = f.dependencyOnVariables();
                int fl1 = 0;
                if (fp.length > 0) {
                    fl1 = fp[fp.length - 1];
                }
                int fl1s = ring.nvar + 1 - fl1;
                GenSolvablePolynomial<C> Cs = null;
                if (el1s <= fl1s) { // symmetric
                    ExpVector g = e.sum(f);
                    //if ( debug )
                    Cs = (GenSolvablePolynomial<C>) zero.sum(one, g); // symmetric!
                    //Cs = new GenSolvablePolynomial<C>(ring,one,g); // symmetric!
                } else { // unsymmetric
                    System.exit(1);
                }
                //C c = a.multiply(b);
                Cs = Cs.multiply(a, b); // now non-symmetric // Cs.multiply(c); is symmetric!
                //if ( debug )
                Cp = (GenSolvablePolynomial<C>) Cp.sum(Cs);
            }
        }
        return Cp;
    }

    /**
     * GenSolvablePolynomial multiplication. Product with coefficient ring
     * element.
     *
     * @param b coefficient.
     * @return this*b, where * is coefficient multiplication.
     */
    @Override
    public GenSolvablePolynomial<C> multiply(C b) {
        GenSolvablePolynomial<C> Cp = ring.getZERO().copy();
        if (b == null || b.isZERO()) {
            return Cp;
        }
        Map<ExpVector, C> Cm = Cp.val; //getMap();
        Map<ExpVector, C> Am = val;
        for (Map.Entry<ExpVector, C> y : Am.entrySet()) {
            ExpVector e = y.getKey();
            C a = y.getValue();
            C c = a.multiply(b);
            if (!c.isZERO()) {
                Cm.put(e, c);
            }
        }
        return Cp;
    }


    /**
     * GenSolvablePolynomial left and right multiplication. Product with
     * coefficient ring element.
     *
     * @param b coefficient.
     * @param c coefficient.
     * @return b*this*c, where * is coefficient multiplication.
     */
    GenSolvablePolynomial<C> multiply(C b, C c) {
        GenSolvablePolynomial<C> Cp = ring.getZERO().copy();
        if (b == null || b.isZERO()) {
            return Cp;
        }
        if (c == null || c.isZERO()) {
            return Cp;
        }
        Map<ExpVector, C> Cm = Cp.val; //getMap();
        Map<ExpVector, C> Am = val;
        for (Map.Entry<ExpVector, C> y : Am.entrySet()) {
            ExpVector e = y.getKey();
            C a = y.getValue();
            C d = b.multiply(a).multiply(c);
            if (!d.isZERO()) {
                Cm.put(e, d);
            }
        }
        return Cp;
    }


    /**
     * GenSolvablePolynomial multiplication. Product with exponent vector.
     *
     * @param e exponent.
     * @return this * x<sup>e</sup>, where * denotes solvable multiplication.
     */
    @Override
    public GenSolvablePolynomial<C> multiply(ExpVector e) {
        if (e == null || e.isZERO()) {
            return this;
        }
        C b = ring.getONECoefficient();
        return multiply(b, e);
    }


    /**
     * GenSolvablePolynomial multiplication. Product with ring element and
     * exponent vector.
     *
     * @param b coefficient.
     * @param e exponent.
     * @return this * b x<sup>e</sup>, where * denotes solvable multiplication.
     */
    @Override
    public GenSolvablePolynomial<C> multiply(C b, ExpVector e) {
        if (b == null || b.isZERO()) {
            return ring.getZERO();
        }
        GenSolvablePolynomial<C> Cp = new GenSolvablePolynomial<>(ring, b, e);
        return multiply(Cp);
    }

    /**
     * GenSolvablePolynomial multiplication. Left product with ring element and
     * exponent vector.
     *
     * @param b coefficient.
     * @param e exponent.
     * @return b x<sup>e</sup> * this, where * denotes solvable multiplication.
     */
    GenSolvablePolynomial<C> multiplyLeft(C b, ExpVector e) {
        if (b == null || b.isZERO()) {
            return ring.getZERO();
        }
        GenSolvablePolynomial<C> Cp = new GenSolvablePolynomial<>(ring, b, e);
        return Cp.multiply(this);
    }


    /**
     * GenSolvablePolynomial multiplication. Left product with coefficient ring
     * element.
     *
     * @param b coefficient.
     * @return b*this, where * is coefficient multiplication.
     */
    GenSolvablePolynomial<C> multiplyLeft(C b) {
        GenSolvablePolynomial<C> Cp = ring.getZERO().copy();
        if (b == null || b.isZERO()) {
            return Cp;
        }
        Map<ExpVector, C> Cm = Cp.val; //getMap();
        Map<ExpVector, C> Am = val;
        for (Map.Entry<ExpVector, C> y : Am.entrySet()) {
            ExpVector e = y.getKey();
            C a = y.getValue();
            C c = b.multiply(a);
            if (!c.isZERO()) {
                Cm.put(e, c);
            }
        }
        return Cp;
    }

    /**
     * GenSolvablePolynomial monic, i.e. leadingCoefficient == 1. If leadingCoefficient
     * is not invertible returns this unmodified.
     *
     * @return monic(this).
     */
    @Override
    public GenSolvablePolynomial<C> monic() {
        if (this.isZERO()) {
            return this;
        }
        C lc = leadingBaseCoefficient();
        //System.out.println("lc = "+lc);
        if (!lc.isUnit()) {
            return this;
        }
        try {
            C lm = lc.inverse();
            //System.out.println("lm = "+lm);
            return multiplyLeft(lm);
        } catch (ArithmeticException e) {
            //e.printStackTrace();
        }
        return this;
    }


    //
    //GenSolvablePolynomial left division with remainder. Fails, if exact division by
    //leading base coefficient is not possible. Meaningful only for univariate
    //polynomials over fields, but works in any case.
    //
    //@param S nonzero GenSolvablePolynomial with invertible leading coefficient.
    //@return [ quotient , remainder ] with this = quotient * S + remainder and
    //deg(remainder) &lt; deg(S) or remiander = 0.
    //@see jas.poly.PolyUtil#baseSparsePseudoRemainder(jas.poly.GenPolynomial, jas.poly.GenPolynomial).
    //
    @SuppressWarnings("unchecked")
    public GenSolvablePolynomial<C>[] quotientRemainder(GenSolvablePolynomial<C> S) {
        if (S == null || S.isZERO()) {
            throw new ArithmeticException("division by zero");
        }
        C c = S.leadingBaseCoefficient();
        if (!c.isUnit()) {
            throw new ArithmeticException("lbcf not invertible " + c);
        }
        C ci = c.inverse();
        assert (ring.nvar == S.ring.nvar);
        ExpVector e = S.leadingExpVector();
        GenSolvablePolynomial<C> h;
        GenSolvablePolynomial<C> q = ring.getZERO().copy();
        GenSolvablePolynomial<C> r = this.copy();
        while (!r.isZERO()) {
            ExpVector f = r.leadingExpVector();
            if (f.multipleOf(e)) {
                C a = r.leadingBaseCoefficient();
                //System.out.println("FDQR: f = " + f + ", a = " + a);
                f = f.subtract(e);
                //a = ci.multiply(a); // multiplyLeft
                a = a.multiply(ci); // this is correct!
                q = (GenSolvablePolynomial<C>) q.sum(a, f);
                h = S.multiplyLeft(a, f);
                if (!h.leadingBaseCoefficient().equals(r.leadingBaseCoefficient())) {
                    throw new RuntimeException("something is wrong: r = " + r + ", h = " + h);
                }
                r = (GenSolvablePolynomial<C>) r.subtract(h);
            } else {
                break;
            }
        }
        GenSolvablePolynomial<C>[] ret = new GenSolvablePolynomial[2];
        ret[0] = q;
        ret[1] = r;
        return ret;
    }

}
