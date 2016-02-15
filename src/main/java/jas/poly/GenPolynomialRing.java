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

    /**
     * The term order.
     */
    public final TermOrder tord;

    /**
     * The names of the variables. This value can be modified.
     */
    String[] vars;

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
        this(cf, new TermOrder(), null);
    }

    //
    //The constructor creates a polynomial factory object.
    //
    //@param cf factory for coefficients of type C.
    //
    public static <C extends RingElem<C>> GenPolynomialRing<C> make(RingFactory<C> cf) {
        return new GenPolynomialRing<>(cf, new TermOrder(), new String[]{"x"});
    }

    /**
     * The constructor creates a polynomial factory object.
     *
     * @param cf factory for coefficients of type C.
     * @param t  a term order.
     * @param v  names for the variables.
     */
    public GenPolynomialRing(RingFactory<C> cf, TermOrder t, String[] v) {
        coFac = cf;
        tord = t;
        if (v == null) {
            vars = null;
        } else {
            vars = Arrays.copyOf(v, v.length); // > Java-5
        }
        ZERO = new GenPolynomial<>(this);
        C coeff = coFac.getONE();
        evzero = 0L;
        ONE = new GenPolynomial<>(this, coeff, evzero);
        if (vars == null) {
            vars = newVars(1);
        } else {
            if (vars.length != 1) {
                throw new IllegalArgumentException();
            }
            addVars(vars);
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
        this(cf, o.tord, o.vars);
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
        if (!tord.equals(oring.tord)) {
            return false;
        }
        // same variables required ?
        return Arrays.equals(vars, oring.vars);
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
        h += tord.hashCode();
        return h;
    }

    /**
     * Get the variable names.
     *
     * @return vars.
     */
    public String[] getVars() {
        return Arrays.copyOf(vars, vars.length); // > Java-5
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
            e = GenPolynomial.EVRAND(1, d, q, rnd);
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
    public GenPolynomial<C> univariate(int i) {
        return univariate(0, i, 1L);
    }

    /**
     * Generate univariate polynomial in a given variable with given exponent.
     *
     * @param i the index of the variable.
     * @param e the exponent of the variable.
     * @return X_i^e as univariate polynomial.
     */
    public GenPolynomial<C> univariate(int i, long e) {
        return univariate(0, i, e);
    }

    /**
     * Generate univariate polynomial in a given variable with given exponent.
     *
     * @param modv number of module variables.
     * @param i    the index of the variable.
     * @param e    the exponent of the variable.
     * @return X_i^e as univariate polynomial.
     */
    GenPolynomial<C> univariate(int modv, int i, long e) {
        GenPolynomial<C> p = getZERO();
        int r = 1 - modv;
        if (r != 1) {
            System.exit(1);
        }
        if (0 <= i && i < r) {
            C one = coFac.getONE();
            if (modv > 0) {
                System.exit(1);
            }
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
        return new PolynomialComparator<>(tord);
    }

    /**
     * New variable names. Generate new names for variables,
     *
     * @param n number of variables.
     * @return new variable names.
     */
    private static String[] newVars(int n) {
        String[] vars = new String[n];
        int m = knownVars.size();
        String name = "x" + m;
        for (int i = 0; i < n; i++) {
            while (knownVars.contains(name)) {
                m++;
                name = "x" + m;
            }
            vars[i] = name;
            //System.out.println("new variable: " + name);
            knownVars.add(name);
            m++;
            name = "x" + m;
        }
        return vars;
    }

    /**
     * Add variable names.
     *
     * @param vars variable names to be recorded.
     */
    private static void addVars(String[] vars) {
        if (vars == null) {
            return;
        }
        Collections.addAll(knownVars, vars);
    }
}
