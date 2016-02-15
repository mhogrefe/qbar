package jas.poly;

import jas.structure.RingElem;
import jas.structure.RingFactory;

import java.math.BigInteger;
import java.util.*;

/**
 * GenPolynomialRing generic polynomial factory implementing RingFactory;
 * Factory for n-variate ordered polynomials over C. Almost immutable object,
 * except variable names.
 *
 * @param <C> coefficient type
 * @author Heinz Kredel
 */
public class GenPolynomialRing<C extends RingElem<C>> implements RingFactory<GenPolynomial<C>> {
    /**
     * The factory for the coefficients.
     */
    public final RingFactory<C> coFac;

    String var;

    /**
     * The names of all known variables.
     */
    private static final Set<String> knownVars = new HashSet<>();

    /**
     * The constant polynomial 0 for this ring.
     */
    private final GenPolynomial<C> ZERO;

    /**
     * The constant polynomial 1 for this ring.
     */
    private final GenPolynomial<C> ONE;

    /**
     * The constant exponent vector 0 for this ring.
     */
    public final long evzero;

    /**
     * A default random sequence generator.
     */
    final static Random random = new Random();

    /**
     * Indicator if this ring is a field.
     */
    private int isField = -1; // initially unknown

    /**
     * The constructor creates a polynomial factory object with the default term
     * order.
     *
     * @param cf factory for coefficients of type C.
     */
    public GenPolynomialRing(RingFactory<C> cf) {
        this(cf, (String) null);
    }

    //
    //The constructor creates a polynomial factory object.
    //
    //@param cf factory for coefficients of type C.
    //
    public static <C extends RingElem<C>> GenPolynomialRing<C> make(RingFactory<C> cf) {
        return new GenPolynomialRing<>(cf, "x");
    }

    /**
     * The constructor creates a polynomial factory object.
     *
     * @param cf factory for coefficients of type C.
     * @param v  names for the variables.
     */
    public GenPolynomialRing(RingFactory<C> cf, String v) {
        coFac = cf;
        if (v == null) {
            var = null;
        } else {
            var = v;
        }
        ZERO = new GenPolynomial<>(this);
        C coeff = coFac.getONE();
        evzero = 0L;
        ONE = new GenPolynomial<>(this, coeff, evzero);
        if (v == null) {
            var = newVars();
        } else {
            addVars(var);
        }
    }

    /**
     * The constructor creates a polynomial factory object with the the same
     * term order, number of variables and variable names as the given
     * polynomial factory, only the coefficient factories differ.
     *
     * @param cf factory for coefficients of type C.
     * @param o  other polynomial ring.
     */
    public GenPolynomialRing(RingFactory<C> cf, GenPolynomialRing o) {
        this(cf, o.var);
    }

    /**
     * Comparison with any other object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (!(other instanceof GenPolynomialRing)) {
            return false;
        }
        GenPolynomialRing<C> oring = null;
        try {
            oring = (GenPolynomialRing<C>) other;
        } catch (ClassCastException ignored) {
        }
        if (!coFac.equals(oring.coFac)) {
            return false;
        }
        // same variables required ?
        return var.equals(oring.var);
    }

    /**
     * Hash code for this polynomial ring.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int h;
        h = (1 << 27);
        h += (coFac.hashCode() << 11);
        return h;
    }

    /**
     * Get the zero element from the coefficients.
     *
     * @return 0 as C.
     */
    public C getZEROCoefficient() {
        return coFac.getZERO();
    }

    /**
     * Get the one element from the coefficients.
     *
     * @return 1 as C.
     */
    public C getONECoefficient() {
        return coFac.getONE();
    }

    //*
    // Get the zero element.
    //
    // @return 0 as GenPolynomial<C>.
    ///
    public GenPolynomial<C> getZERO() {
        return ZERO;
    }

    //
    //Get the one element.
    //
    //@return 1 as GenPolynomial<C>.
    //
    public GenPolynomial<C> getONE() {
        return ONE;
    }

    /**
     * Query if this ring is a field.
     *
     * @return false.
     */
    public boolean isField() {
        if (isField > 0) {
            return true;
        }
        if (isField == 0) {
            return false;
        }
        isField = 0;
        return false;
    }

    /**
     * Characteristic of this ring.
     *
     * @return characteristic of this ring.
     */
    public BigInteger characteristic() {
        return coFac.characteristic();
    }

    /**
     * Get a (constant) GenPolynomial&lt;C&gt; element from a long value.
     *
     * @param a long.
     * @return a GenPolynomial&lt;C&gt;.
     */
    public GenPolynomial<C> fromInteger(long a) {
        return new GenPolynomial<>(this, coFac.fromInteger(a), evzero);
    }

    /**
     * Get a (constant) GenPolynomial&lt;C&gt; element from a JasBigInteger value.
     *
     * @param a JasBigInteger.
     * @return a GenPolynomial&lt;C&gt;.
     */
    public GenPolynomial<C> fromInteger(BigInteger a) {
        return new GenPolynomial<>(this, coFac.fromInteger(a), evzero);
    }

    /**
     * Random polynomial. Generates a random polynomial with k = 5, l = n, d =
     * (nvar == 1) ? n : 3, q = (nvar == 1) ? 0.7 : 0.3.
     *
     * @param n   number of terms.
     * @param rnd is a source for random bits.
     * @return a random polynomial.
     */
    public GenPolynomial<C> random(int n, Random rnd) {
        return random(3, n, n, 0.7f, rnd);
    }

    /**
     * Generate a random polynomial.
     *
     * @param l number of terms.
     * @param d maximal degree in each variable.
     * @return a random polynomial.
     */
    public GenPolynomial<C> random(int l, int d) {
        return random(17, l, d, 1.0f, random);
    }

    /**
     * Generate a random polynomial.
     *
     * @param k   bitsize of random coefficients.
     * @param l   number of terms.
     * @param d   maximal degree in each variable.
     * @param q   density of nozero exponents.
     * @param rnd is a source for random bits.
     * @return a random polynomial.
     */
    GenPolynomial<C> random(int k, int l, int d, float q, Random rnd) {
        GenPolynomial<C> r = getZERO(); //.clone() or copy( ZERO );
        long e;
        C a;
        // add l random coeffs and exponents
        for (int i = 0; i < l; i++) {
            e = GenPolynomial.EVRAND(d, q, rnd);
            a = coFac.random(k, rnd);
            r = r.sum(a, e); // somewhat inefficient but clean
            //System.out.println("e = " + e + " a = " + a);
        }
        // System.out.println("r = " + r);
        return r;
    }

    /**
     * Generate univariate polynomial in a given variable.
     *
     * @param i the index of the variable.
     * @return X_i as univariate polynomial.
     */
    public GenPolynomial<C> univariateA(int i) {
        return univariateC(i, 1L);
    }

    /**
     * Generate univariate polynomial in a given variable with given exponent.
     *
     * @param i the index of the variable.
     * @param e the exponent of the variable.
     * @return X_i^e as univariate polynomial.
     */
    public GenPolynomial<C> univariateB(int i, long e) {
        return univariateC(i, e);
    }

    /**
     * Generate univariate polynomial in a given variable with given exponent.
     *
     * @param i    the index of the variable.
     * @param e    the exponent of the variable.
     * @return X_i^e as univariate polynomial.
     */
    public GenPolynomial<C> univariateC(int i, long e) {
        GenPolynomial<C> p = getZERO();
        if (i == 0) {
            C one = coFac.getONE();
            p = p.sum(one, e);
        }
        return p;
    }

    /**
     * Is this structure finite or infinite.
     *
     * @return true if this structure is finite, else false.
     * @see jas.structure.ElemFactory#isFinite()
     */
    public boolean isFinite() {
        return false;
    }

    /**
     * Get PolynomialComparator.
     *
     * @return polynomial comparator.
     */
    public PolynomialComparator<C> getComparator() {
        return new PolynomialComparator<>();
    }

    /**
     * New variable names. Generate new names for variables,
     *
     * @return new variable names.
     */
    private static String newVars() {
        int m = knownVars.size();
        String name = "x" + m;
        while (knownVars.contains(name)) {
            m++;
            name = "x" + m;
        }
        String var = name;
        knownVars.add(name);
        return var;
    }

    /**
     * Add variable names.
     *
     * @param vars variable names to be recorded.
     */
    private static void addVars(String vars) {
        if (vars == null) {
            return;
        }
        knownVars.add(vars);
    }
}
