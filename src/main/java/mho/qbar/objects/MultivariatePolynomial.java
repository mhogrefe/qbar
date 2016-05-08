package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertEquals;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>A multivariate polynomial with {@link BigInteger} coefficients.</p>
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code MultivariatePolynomial}s using {@code ==}.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class MultivariatePolynomial implements
        Comparable<MultivariatePolynomial>,
        Iterable<Pair<Monomial, BigInteger>> {
    /**
     * 0
     */
    public static final @NotNull MultivariatePolynomial ZERO = new MultivariatePolynomial(Collections.emptyList());

    /**
     * 1
     */
    public static final @NotNull MultivariatePolynomial ONE =
            new MultivariatePolynomial(Collections.singletonList(new Pair<>(Monomial.ONE, BigInteger.ONE)));

    /**
     * The default order of the {@code Monomial}s in {@code this}
     */
    private static final @NotNull MonomialOrder DEFAULT_ORDER = MonomialOrder.GREVLEX;

    /**
     * This {@code MultivariatePolynomial}'s terms. The second element of each pair is the coefficient of the
     * {@code Monomial} in the first slot. The terms are in grevlex order.
     */
    private final @NotNull List<Pair<Monomial, BigInteger>> terms;

    /**
     * Private constructor for {@code MultivariatePolynomial}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code terms} cannot have any null elements. The {@code Monomial}s must be unique and in grevlex
     *  order.</li>
     *  <li>Any {@code MultivariatePolynomial} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param terms the polynomial's terms
     */
    private MultivariatePolynomial(@NotNull List<Pair<Monomial, BigInteger>> terms) {
        this.terms = terms;
    }

    /**
     * Returns an {@code Iterable} over this {@code MultivariatePolynomial}'s terms, from lowest to highest with a
     * given {@code MonomialOrder}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code order} is not null.</li>
     *  <li>The result is finite, and contains no nulls. The {@code Monomial}s are increasing with respect to
     *  {@code order}.</li>
     * </ul>
     *
     * @param order the monomial order with respect to which the terms descend
     * @return an {@code Iterable} over this {@code MultivariatePolynomial}'s terms
     */
    public @NotNull Iterable<Pair<Monomial, BigInteger>> iterable(@NotNull MonomialOrder order) {
        if (order == DEFAULT_ORDER) {
            return new NoRemoveIterable<>(terms);
        } else {
            return new NoRemoveIterable<>(sort((x, y) -> order.compare(x.a, y.a), terms));
        }
    }

    /**
     * Returns an {@code Iterator} over this {@code MultivariatePolynomial}'s terms, from lowest to highest in grevlex
     * ordering. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>The result is finite, and contains no nulls. The {@code Monomial}s are in increasing grevlex order.</li>
     * </ul>
     *
     * @return an {@code Iterator} over this {@code MultivariatePolynomial}'s terms
     */
    @Override
    public @NotNull Iterator<Pair<Monomial, BigInteger>> iterator() {
        return new NoRemoveIterable<>(terms).iterator();
    }

    /**
     * Returns the coefficient of a given {@code monomial}. If the coefficient is not present, 0 is returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code monomial} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param monomial the {@code Monomial} that the coefficient belongs to
     * @return the coefficient of {@code monomial} in {@code this}
     */
    public @NotNull BigInteger coefficient(@NotNull Monomial monomial) {
        return lookupSorted(terms, monomial).orElse(BigInteger.ZERO);
    }

    /**
     * Creates a {@code MultivariatePolynomial} from a list of terms. Throws an exception if any term is null. Makes a
     * defensive copy of {@code terms}. Merges duplicates and throws out zero terms.
     *
     * <ul>
     *  <li>{@code terms} cannot have any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param terms the polynomial's terms
     * @return the {@code MultivariatePolynomial} with the specified terms
     */
    public static @NotNull MultivariatePolynomial of(@NotNull List<Pair<Monomial, BigInteger>> terms) {
        SortedMap<Monomial, BigInteger> termMap = new TreeMap<>();
        for (Pair<Monomial, BigInteger> term : terms) {
            BigInteger coefficient = termMap.get(term.a);
            if (coefficient == null) coefficient = BigInteger.ZERO;
            termMap.put(term.a, coefficient.add(term.b));
        }
        List<Pair<Monomial, BigInteger>> sortedTerms = new ArrayList<>();
        //noinspection Convert2streamapi
        for (Map.Entry<Monomial, BigInteger> entry : termMap.entrySet()) {
            if (!entry.getValue().equals(BigInteger.ZERO)) {
                sortedTerms.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }
        if (sortedTerms.isEmpty()) return ZERO;
        if (sortedTerms.size() == 1) {
            Pair<Monomial, BigInteger> term = head(sortedTerms);
            if (term.a == Monomial.ONE && term.b.equals(BigInteger.ONE)) {
                return ONE;
            }
        }
        return new MultivariatePolynomial(sortedTerms);
    }

    /**
     * Creates a {@code MultivariatePolynomial} containing a single term (or zero terms if the coefficient is zero).
     *
     * <ul>
     *  <li>{@code monomial} cannot be null.</li>
     *  <li>{@code BigInteger} cannot be null.</li>
     *  <li>The result has no more than one term.</li>
     * </ul>
     *
     * @param m an {@code Monomial}
     * @param c the coefficient of {@code m}
     * @return a {@code MultivariatePolynomial} equal to {@code m} multiplied by {@code c}
     */
    public static @NotNull MultivariatePolynomial of(@NotNull Monomial m, @NotNull BigInteger c) {
        if (c.equals(BigInteger.ZERO)) return ZERO;
        if (m == Monomial.ONE && c.equals(BigInteger.ONE)) return ONE;
        return new MultivariatePolynomial(Collections.singletonList(new Pair<>(m, c)));
    }

    /**
     * Creates a constant {@code MultivariatePolynomial} equal to a given {@code BigInteger}.
     *
     * <ul>
     *  <li>{@code c} cannot be null.</li>
     *  <li>The result is constant.</li>
     * </ul>
     *
     * @param c a constant
     * @return the {@code MultivariatePolynomial} equal to c
     */
    public static @NotNull MultivariatePolynomial of(@NotNull BigInteger c) {
        if (c.equals(BigInteger.ZERO)) return ZERO;
        if (c.equals(BigInteger.ONE)) return ONE;
        return new MultivariatePolynomial(Collections.singletonList(new Pair<>(Monomial.ONE, c)));
    }

    /**
     * Creates a constant {@code MultivariatePolynomial} equal to a given {@code int}.
     *
     * <ul>
     *  <li>{@code c} may be any {@code int}.</li>
     *  <li>The result is constant.</li>
     * </ul>
     *
     * @param c a constant
     * @return the {@code MultivariatePolynomial} equal to c
     */
    public static @NotNull MultivariatePolynomial of(int c) {
        if (c == 0) return ZERO;
        if (c == 1) return ONE;
        return new MultivariatePolynomial(
                Collections.singletonList(new Pair<>(Monomial.ONE, BigInteger.valueOf(c)))
        );
    }

    /**
     * Creates a {@code MultivariatePolynomial} from a single {@code Variable}.
     *
     * <ul>
     *  <li>{@code v} cannot be null.</li>
     *  <li>The result contains one term, which is equal to a variable.</li>
     * </ul>
     *
     * @param v a {@code Variable}
     * @return the {@code MultivariatePolynomial} equal to {@code v}
     */
    public static @NotNull MultivariatePolynomial of(@NotNull Variable v) {
        return new MultivariatePolynomial(Collections.singletonList(new Pair<>(Monomial.of(v), BigInteger.ONE)));
    }

    /**
     * Given a (univariate) {@code Polynomial} and a variable, returns a {@code MultivariatePolynomial} equal to the
     * polynomial applied to the variable.
     *
     * <ul>
     *  <li>{@code p} cannot be null.</li>
     *  <li>{@code v} cannot be null.</li>
     *  <li>The result is univariate.</li>
     * </ul>
     *
     * @param p a {@code Polynomial}
     * @param v the independent variable of the result
     * @return {@code p} with {@code v}
     */
    public static @NotNull MultivariatePolynomial of(@NotNull Polynomial p, @NotNull Variable v) {
        if (p == Polynomial.ZERO) return ZERO;
        if (p == Polynomial.ONE) return ONE;
        List<Integer> exponents = toList(replicate(v.getIndex() + 1, 0));
        int lastIndex = exponents.size() - 1;
        List<Pair<Monomial, BigInteger>> terms = new ArrayList<>();
        for (int i = 0; i <= p.degree(); i++) {
            BigInteger coefficient = p.coefficient(i);
            if (!coefficient.equals(BigInteger.ZERO)) {
                exponents.set(lastIndex, i);
                terms.add(new Pair<>(Monomial.of(exponents), p.coefficient(i)));
            }
        }
        return new MultivariatePolynomial(terms);
    }

    /**
     * Converts {@code this} to a univariate {@code Polynomial}.
     *
     * <ul>
     *  <li>{@code this} cannot have more than one variable.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return a {@code Polynomial} equal to {@code this}
     */
    public @NotNull Polynomial toPolynomial() {
        if (this == ZERO) return Polynomial.ZERO;
        if (this == ONE) return Polynomial.ONE;
        List<Variable> variables = variables();
        if (variables.size() > 1) {
            throw new IllegalArgumentException("this cannot have more than one variable. Invalid this: " + this);
        }
        Optional<Variable> variable = variables.isEmpty() ? Optional.empty() : Optional.of(head(variables));
        List<BigInteger> coefficients = toList(replicate(degree() + 1, BigInteger.ZERO));
        for (Pair<Monomial, BigInteger> term : terms) {
            if (term.a == Monomial.ONE) {
                coefficients.set(0, term.b);
            } else {
                int exponent = term.a.exponent(variable.get());
                coefficients.set(exponent, term.b);
            }
        }
        return Polynomial.of(coefficients);
    }

    /**
     * Returns all the variables in {@code this}, in ascending order.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>The result is in ascending order and has no repetitions.</li>
     * </ul>
     *
     * @return the variables in {@code this}
     */
    public @NotNull List<Variable> variables() {
        SortedSet<Variable> variables = new TreeSet<>();
        for (Pair<Monomial, BigInteger> term : terms) {
            variables.addAll(term.a.variables());
        }
        return toList(variables);
    }

    /**
     * Returns the number of variables in {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the number of variables in {@code this}
     */
    public int variableCount() {
        return variables().size();
    }

    /**
     * Returns the number of terms in {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the number of terms in {@code this}
     */
    public int termCount() {
        return terms.size();
    }

    /**
     * Returns the maximum bit length of any coefficient, or 0 if {@code this} is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the maximum coefficient bit length
     */
    public int maxCoefficientBitLength() {
        if (this == ZERO) return 0;
        //noinspection RedundantCast
        return maximum((Iterable<Integer>) map(t -> t.b.abs().bitLength(), terms));
    }

    /**
     * Returns the degree of {@code this}, which is the highest degree of any {@code term}. We consider 0 to have
     * degree –1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>The result is at least –1.</li>
     * </ul>
     *
     * @return deg({@code this}
     */
    @SuppressWarnings("JavaDoc")
    public int degree() {
        return terms.isEmpty() ? -1 : last(terms).a.degree();
    }

    /**
     * Whether {@code this} is homogeneous; whether the degrees of each {@code Monomial} are equal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is a homogeneous polynomial
     */
    public boolean isHomogeneous() {
        return same(map(t -> t.a.degree(), terms));
    }

    /**
     * Expresses {@code this} as c<sub>0</sub>{@code v}<sup>0</sup>+c<sub>1</sub>{@code v}<sup>1</sup>+...+
     * c<sub>n</sub>{@code v}<sup>n</sup> and returns the coefficients c<sub>0</sub>, c<sub>1</sub>, ...,
     * c<sub>n</sub>.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code v} cannot be null.</li>
     *  <li>The result contains no nulls.</li>
     * </ul>
     *
     * @param v a {@code Variable}
     * @return {@code this} as coefficients of powers of {@code v}
     */
    public @NotNull List<MultivariatePolynomial> coefficientsOfVariable(@NotNull Variable v) {
        if (this == ZERO) return Collections.emptyList();
        if (this.degree() == 0) return Collections.singletonList(this);
        Map<Integer, List<Pair<Monomial, BigInteger>>> coefficientMap = new HashMap<>();
        for (Pair<Monomial, BigInteger> term : terms) {
            int vPower = term.a.exponent(v);
            List<Pair<Monomial, BigInteger>> vPowerTerms = coefficientMap.get(vPower);
            if (vPowerTerms == null) {
                vPowerTerms = new ArrayList<>();
                coefficientMap.put(vPower, vPowerTerms);
            }
            vPowerTerms.add(new Pair<>(term.a.removeVariable(v), term.b));
        }
        int maxPower = 0;
        for (int power : coefficientMap.keySet()) {
            if (power > maxPower) maxPower = power;
        }
        List<MultivariatePolynomial> coefficients = new ArrayList<>();
        for (int i = 0; i <= maxPower; i++) {
            List<Pair<Monomial, BigInteger>> powerTerms = coefficientMap.get(i);
            if (powerTerms == null) {
                coefficients.add(ZERO);
            } else {
                coefficients.add(of(powerTerms));
            }
        }
        return coefficients;
    }

    /**
     * Returns the sum of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code MultivariatePolynomial} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull MultivariatePolynomial add(@NotNull MultivariatePolynomial that) {
        if (this == ZERO) return that;
        if (that == ZERO) return this;
        List<Pair<Monomial, BigInteger>> sumTerms = new ArrayList<>();
        Iterator<Pair<Monomial, BigInteger>> thisTerms = terms.iterator();
        Iterator<Pair<Monomial, BigInteger>> thatTerms = that.terms.iterator();
        Pair<Monomial, BigInteger> thisTerm = thisTerms.hasNext() ? thisTerms.next() : null;
        Pair<Monomial, BigInteger> thatTerm = thatTerms.hasNext() ? thatTerms.next() : null;
        while (thisTerm != null || thatTerm != null) {
            if (thisTerm == null) {
                sumTerms.add(thatTerm);
                thatTerm = thatTerms.hasNext() ? thatTerms.next() : null;
            } else if (thatTerm == null) {
                sumTerms.add(thisTerm);
                thisTerm = thisTerms.hasNext() ? thisTerms.next() : null;
            } else {
                int vectorCompare = thisTerm.a.compareTo(thatTerm.a);
                if (vectorCompare == 0) {
                    BigInteger sum = thisTerm.b.add(thatTerm.b);
                    if (!sum.equals(BigInteger.ZERO)) {
                        sumTerms.add(new Pair<>(thisTerm.a, sum));
                    }
                    thisTerm = thisTerms.hasNext() ? thisTerms.next() : null;
                    thatTerm = thatTerms.hasNext() ? thatTerms.next() : null;
                } else if (vectorCompare > 0) {
                    sumTerms.add(thatTerm);
                    thatTerm = thatTerms.hasNext() ? thatTerms.next() : null;
                } else {
                    sumTerms.add(thisTerm);
                    thisTerm = thisTerms.hasNext() ? thisTerms.next() : null;
                }
            }
        }
        if (sumTerms.size() == 0) return ZERO;
        if (sumTerms.size() == 1) {
            Pair<Monomial, BigInteger> term = sumTerms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(BigInteger.ONE)) return ONE;
        }
        return new MultivariatePolynomial(sumTerms);
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return –{@code this}
     */
    public @NotNull MultivariatePolynomial negate() {
        if (this == ZERO) return ZERO;
        if (terms.size() == 1) {
            Pair<Monomial, BigInteger> term = terms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(IntegerUtils.NEGATIVE_ONE)) return ONE;
        }
        return new MultivariatePolynomial(toList(map(t -> new Pair<>(t.a, t.b.negate()), terms)));
    }

    /**
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code MultivariatePolynomial} subtracted by {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull MultivariatePolynomial subtract(@NotNull MultivariatePolynomial that) {
        if (this == ZERO) return that.negate();
        if (that == ZERO) return this;
        List<Pair<Monomial, BigInteger>> differenceTerms = new ArrayList<>();
        Iterator<Pair<Monomial, BigInteger>> thisTerms = terms.iterator();
        Iterator<Pair<Monomial, BigInteger>> thatTerms = that.terms.iterator();
        Pair<Monomial, BigInteger> thisTerm = thisTerms.hasNext() ? thisTerms.next() : null;
        Pair<Monomial, BigInteger> thatTerm = thatTerms.hasNext() ? thatTerms.next() : null;
        while (thisTerm != null || thatTerm != null) {
            if (thisTerm == null) {
                differenceTerms.add(new Pair<>(thatTerm.a, thatTerm.b.negate()));
                thatTerm = thatTerms.hasNext() ? thatTerms.next() : null;
            } else if (thatTerm == null) {
                differenceTerms.add(thisTerm);
                thisTerm = thisTerms.hasNext() ? thisTerms.next() : null;
            } else {
                int vectorCompare = thisTerm.a.compareTo(thatTerm.a);
                if (vectorCompare == 0) {
                    if (!thisTerm.b.equals(thatTerm.b)) {
                        BigInteger difference = thisTerm.b.subtract(thatTerm.b);
                        differenceTerms.add(new Pair<>(thisTerm.a, difference));
                    }
                    thisTerm = thisTerms.hasNext() ? thisTerms.next() : null;
                    thatTerm = thatTerms.hasNext() ? thatTerms.next() : null;
                } else if (vectorCompare > 0) {
                    differenceTerms.add(new Pair<>(thatTerm.a, thatTerm.b.negate()));
                    thatTerm = thatTerms.hasNext() ? thatTerms.next() : null;
                } else {
                    differenceTerms.add(thisTerm);
                    thisTerm = thisTerms.hasNext() ? thisTerms.next() : null;
                }
            }
        }
        if (differenceTerms.size() == 0) return ZERO;
        if (differenceTerms.size() == 1) {
            Pair<Monomial, BigInteger> term = differenceTerms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(BigInteger.ONE)) return ONE;
        }
        return new MultivariatePolynomial(differenceTerms);
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code that} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull MultivariatePolynomial multiply(int that) {
        if (this == ZERO || that == 0) return ZERO;
        if (this == ONE && that == 1) return ONE;
        if (that == -1 && terms.size() == 1) {
            Pair<Monomial, BigInteger> term = terms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(IntegerUtils.NEGATIVE_ONE)) {
                return ONE;
            }
        }
        BigInteger bigThat = BigInteger.valueOf(that);
        return new MultivariatePolynomial(toList(map(t -> new Pair<>(t.a, t.b.multiply(bigThat)), terms)));
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull MultivariatePolynomial multiply(@NotNull BigInteger that) {
        if (this == ZERO || that.equals(BigInteger.ZERO)) return ZERO;
        if (this == ONE && that.equals(BigInteger.ONE)) return ONE;
        if (terms.size() == 1 && that.equals(IntegerUtils.NEGATIVE_ONE)) {
            Pair<Monomial, BigInteger> term = terms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(IntegerUtils.NEGATIVE_ONE)) {
                return ONE;
            }
        }
        return new MultivariatePolynomial(toList(map(t -> new Pair<>(t.a, t.b.multiply(that)), terms)));
    }

    /**
     * Returns the product of {@code this} and a single term, specified by a {@code Monomial} and its coefficient.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code c} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param m the {@code Monomial} of the term {@code this} is multiplied by
     * @return {@code this}×{@code c}×{@code m}
     */
    public @NotNull MultivariatePolynomial multiply(@NotNull Monomial m, @NotNull BigInteger c) {
        if (this == ZERO || c.equals(BigInteger.ZERO)) return ZERO;
        if (this == ONE) return of(m, c);
        if (m == Monomial.ONE) return multiply(c);
        return new MultivariatePolynomial(toList(map(t -> new Pair<>(t.a.multiply(m), t.b.multiply(c)), terms)));
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code MultivariatePolynomial} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull MultivariatePolynomial multiply(@NotNull MultivariatePolynomial that) {
        if (this == ZERO || that == ZERO) return ZERO;
        if (this == ONE) return that;
        if (that == ONE) return this;
        return sum(toList(map(t -> multiply(t.a, t.b), that.terms)));
    }

    /**
     * Returns the {@code this} divided by {@code that}, assuming that {@code that} divides each coefficient of
     * {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>{@code that must divide each coefficient of {@code this}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code BigInteger} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull MultivariatePolynomial divideExact(@NotNull BigInteger that) {
        if (that.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("that cannot be zero.");
        }
        if (this == ZERO) return ZERO;
        if (that.equals(BigInteger.ONE)) return this;
        List<Pair<Monomial, BigInteger>> quotientTerms = toList(map(t -> new Pair<>(t.a, t.b.divide(that)), terms));
        if (quotientTerms.size() == 1) {
            Pair<Monomial, BigInteger> term = quotientTerms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(BigInteger.ONE)) return ONE;
        }
        return new MultivariatePolynomial(quotientTerms);
    }

    /**
     * Returns the {@code this} divided by {@code that}, assuming that {@code that} divides each coefficient of
     * {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>{@code that must divide each coefficient of {@code this}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code int} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull MultivariatePolynomial divideExact(int that) {
        if (that == 0) {
            throw new ArithmeticException("that cannot be zero.");
        }
        if (this == ZERO) return ZERO;
        if (that == 1) return this;
        BigInteger bigThat = BigInteger.valueOf(that);
        List<Pair<Monomial, BigInteger>> quotientTerms = toList(map(t -> new Pair<>(t.a, t.b.divide(bigThat)), terms));
        if (quotientTerms.size() == 1) {
            Pair<Monomial, BigInteger> term = quotientTerms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(BigInteger.ONE)) return ONE;
        }
        return new MultivariatePolynomial(quotientTerms);
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>. Negative
     * {@code bits} are not allowed, even if {@code this} is divisible by a power of 2.
     *
     * <ul>
     *  <li>{@code this} can be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code bits} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull MultivariatePolynomial shiftLeft(int bits) {
        if (bits < 0) {
            throw new ArithmeticException("bits cannot be negative. Invalid bits: " + bits);
        }
        if (this == ZERO || bits == 0) return this;
        List<Pair<Monomial, BigInteger>> shiftedTerms =
                toList(map(t -> new Pair<>(t.a, t.b.shiftLeft(bits)), terms));
        return new MultivariatePolynomial(shiftedTerms);
    }

    /**
     * Returns the sum of all the {@code MultivariatePolynomial}s in {@code xs}. If {@code xs} is empty, 0 is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code MultivariatePolynomial}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code MultivariatePolynomial}s.
     * @return Σxs
     */
    public static @NotNull MultivariatePolynomial sum(@NotNull List<MultivariatePolynomial> xs) {
        return foldl(MultivariatePolynomial::add, ZERO, xs);
    }

    /**
     * Returns the product of all the {@code MultivariatePolynomial}s in {@code xs}. If {@code xs} is empty, 1 is
     * returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code MultivariatePolynomial}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code MultivariatePolynomial}s.
     * @return Πxs
     */
    public static @NotNull MultivariatePolynomial product(@NotNull List<MultivariatePolynomial> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        if (any(x -> x == ZERO, xs)) {
            return ZERO;
        }
        return foldl(MultivariatePolynomial::multiply, ONE, xs);
    }

    /**
     * Returns the differences between successive {@code MultivariatePolynomial}s in {@code xs}. If {@code xs} contains
     * a single {@code MultivariatePolynomial}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does
     * not support removal.
     *
     * <ul>
     *  <li>{@code xs} must not be empty and may not contain any nulls.</li>
     *  <li>The result does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code MultivariatePolynomial}s.
     * @return Δxs
     */
    public static @NotNull Iterable<MultivariatePolynomial> delta(@NotNull Iterable<MultivariatePolynomial> xs) {
        if (isEmpty(xs)) {
            throw new IllegalArgumentException("xs must not be empty.");
        }
        if (head(xs) == null) {
            throw new NullPointerException();
        }
        return adjacentPairsWith((x, y) -> y.subtract(x), xs);
    }

    /**
     * Returns {@code this} raised to the power of {@code p}. 0<sup>0</sup> yields 1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code p} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param p the power that {@code this} is raised to
     * @return {@code this}<sup>{@code p}</sup>
     */
    public @NotNull MultivariatePolynomial pow(int p) {
        if (p < 0) {
            throw new ArithmeticException("p cannot be negative. Invalid p: " + p);
        }
        if (p == 0 || this == ONE) return ONE;
        if (this == ZERO) return ZERO;
        if (terms.size() == 1) {
            Pair<Monomial, BigInteger> term = terms.get(0);
            if (term.a == Monomial.ONE) {
                return of(term.b.pow(p));
            } else {
                return new MultivariatePolynomial(Collections.singletonList(new Pair<>(term.a.pow(p), term.b.pow(p))));
            }
        }
        MultivariatePolynomial result = ONE;
        MultivariatePolynomial powerPower = null; // p^2^i
        for (boolean bit : IntegerUtils.bits(p)) {
            powerPower = powerPower == null ? this : powerPower.multiply(powerPower);
            if (bit) result = result.multiply(powerPower);
        }
        return result;
    }

    /**
     * Given two distinct {@code Variable} {@code a} and {@code b}, returns ({@code a}+{@code b})<sup>{@code p}</sup>.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} cannot equal {@code b}.</li>
     *  <li>The result is a power of the sum of two {@code Variable}s.</li>
     * </ul>
     *
     * @param a the first {@code Variable}
     * @param b the second {@code Variable}
     * @param p the power the binomial is raised to
     * @return ({@code a}+{@code b})<sup>{@code p}</sup>
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull MultivariatePolynomial binomialPower(@NotNull Variable a, @NotNull Variable b, int p) {
        if (p < 0) {
            throw new ArithmeticException("p cannot be negative. Invalid p: " + p);
        }
        int vCompare = a.compareTo(b);
        if (vCompare == 0) {
            throw new IllegalArgumentException("a cannot equal b. Invalid a and b: " + a);
        } else if (vCompare == 1) {
            Variable temp = a;
            a = b;
            b = temp;
        }
        if (p == 0) return ONE;

        int aIndex = a.getIndex();
        int bIndex = b.getIndex();
        List<Integer> exponentVector = toList(replicate(bIndex, 0));
        exponentVector.add(p);

        BigInteger bigP = BigInteger.valueOf(p);
        List<BigInteger> coefficients = new ArrayList<>();
        for (int i = 0; i <= p / 2; i++) {
            coefficients.add(MathUtils.binomialCoefficient(bigP, i));
        }

        List<Pair<Monomial, BigInteger>> terms = new ArrayList<>();
        boolean ascending = true;
        int j = 0;
        for (int i = 0; i <= p; i++) {
            terms.add(new Pair<>(Monomial.of(exponentVector), coefficients.get(j)));
            exponentVector.set(bIndex, exponentVector.get(bIndex) - 1);
            exponentVector.set(aIndex, exponentVector.get(aIndex) + 1);
            if (ascending) {
                j++;
                if (i == p / 2) {
                    ascending = false;
                    j = i;
                    if ((p & 1) == 0) j--;
                }
            } else {
                j--;
            }
        }
        return new MultivariatePolynomial(terms);
    }

    /**
     * Returns the Sylvester matrix of {@code this} and {@code that}, expanded in terms of powers of
     * {@code variableToEliminate}.
     *
     * <ul>
     *  <li>{@code this} cannot be zero and must have no more than two variables.</li>
     *  <li>{@code that} cannot be zero and must have no more than two variables.</li>
     *  <li>{@code variableToEliminate} cannot be null.</li>
     *  <li>Apart from {@code variableToEliminate}, {@code this} and {@code that} must have no more than one other
     *  variable.</li>
     *  <li>The result is a Sylvester matrix.</li>
     * </ul>
     *
     * @param that a {@code MultivariatePolynomial}
     * @return S<sub>{@code this},{@code that}</sub> expanded with respect to {@code variableToEliminate}
     */
    //todo finish testing
    public @NotNull PolynomialMatrix sylvesterMatrix(
            @NotNull MultivariatePolynomial that,
            Variable variableToEliminate
    ) {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        if (that == ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        List<Variable> thisVariables = variables();
        thisVariables.remove(variableToEliminate);
        if (thisVariables.size() > 1) {
            throw new ArithmeticException();
        }
        List<Variable> thatVariables = that.variables();
        thatVariables.remove(variableToEliminate);
        if (thatVariables.size() > 1) {
            throw new ArithmeticException();
        }

        List<Polynomial> thisCoefficients =
                reverse(map(MultivariatePolynomial::toPolynomial, coefficientsOfVariable(variableToEliminate)));
        List<Polynomial> thatCoefficients =
                reverse(map(MultivariatePolynomial::toPolynomial, that.coefficientsOfVariable(variableToEliminate)));
        int thisDegree = thisCoefficients.size() - 1;
        int thatDegree = thatCoefficients.size() - 1;
        List<PolynomialVector> rows = new ArrayList<>();
        for (int i = 0; i < thatDegree; i++) {
            List<Polynomial> row = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                row.add(Polynomial.ZERO);
            }
            row.addAll(thisCoefficients);
            for (int j = 0; j < thatDegree - i - 1; j++) {
                row.add(Polynomial.ZERO);
            }
            rows.add(PolynomialVector.of(row));
        }
        for (int i = 0; i < thisDegree; i++) {
            List<Polynomial> row = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                row.add(Polynomial.ZERO);
            }
            row.addAll(thatCoefficients);
            for (int j = 0; j < thisDegree - i - 1; j++) {
                row.add(Polynomial.ZERO);
            }
            rows.add(PolynomialVector.of(row));
        }
        return PolynomialMatrix.fromRows(rows);
    }

    /**
     * Returns the resultant of {@code this} and {@code that} with respect to {@code variableToEliminate}.
     *
     * <ul>
     *  <li>{@code this} cannot be zero and must have no more than two variables.</li>
     *  <li>{@code that} cannot be zero and must have no more than two variables.</li>
     *  <li>{@code variableToEliminate} cannot be null.</li>
     *  <li>Apart from {@code variableToEliminate}, {@code this} and {@code that} must have no more than one other
     *  variable.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that a {@code Polynomial}
     * @return Res({@code this},{@code that}) with respect to {@code variableToEliminate}
     */
    //todo finish testing
    @SuppressWarnings("JavaDoc")
    public @NotNull Polynomial resultant(@NotNull MultivariatePolynomial that, @NotNull Variable variableToEliminate) {
        return sylvesterMatrix(that, variableToEliminate).determinant();
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code Object} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        return this == that ||
                that != null && getClass() == that.getClass() && terms.equals(((MultivariatePolynomial) that).terms);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return terms.hashCode();
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. The terms are compared lexicographically, starting from the end of the {@code term}s
     * lists.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull MultivariatePolynomial that) {
        if (this == that) return 0;
        if (this == ZERO) return -1;
        if (that == ZERO) return 1;
        int i = terms.size() - 1;
        int j = that.terms.size() - 1;
        while (i >= 0 && j >= 0) {
            Pair<Monomial, BigInteger> thisTerm = terms.get(i);
            Pair<Monomial, BigInteger> thatTerm = that.terms.get(j);
            int evCompare = thisTerm.a.compareTo(thatTerm.a);
            if (evCompare != 0) return evCompare;
            int iCompare = thisTerm.b.compareTo(thatTerm.b);
            if (iCompare != 0) return iCompare;
            i--;
            j--;
        }
        if (i >= 0) return 1;
        if (j >= 0) return -1;
        return 0;
    }

    /**
     * Creates a {@code MultivariatePolynomial} from a {@code String}. Valid input takes the form of a {@code String}
     * that could have been returned by {@link MultivariatePolynomial#toString(MonomialOrder)} applied to
     * {@code order}.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>{@code order} cannot be null.</li>
     *  <li>The result may contain any {@code MultivariatePolynomial}, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of a {@code MultivariatePolynomial}
     * @param order the monomial order with respect to which the input terms must descend
     * @return the {@code MultivariatePolynomial} represented by {@code s}, or an empty {@code Optional} if {@code s}
     * is invalid
     */
    public static @NotNull Optional<MultivariatePolynomial> readStrict(
            @NotNull String s,
            @NotNull MonomialOrder order
    ) {
        if (s.equals("0")) return Optional.of(ZERO);
        if (s.equals("1")) return Optional.of(ONE);
        if (s.isEmpty() || head(s) == '+') return Optional.empty();

        List<String> monomialStrings = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '-' || c == '+') {
                String monomialString = sb.toString();
                if (!monomialString.isEmpty()) {
                    if (head(monomialString) == '+') {
                        monomialString = tail(monomialString);
                    }
                    monomialStrings.add(monomialString);
                    sb = new StringBuilder();
                }
            }
            sb.append(c);
        }
        String monomialString = sb.toString();
        if (!monomialString.isEmpty()) {
            if (head(monomialString) == '+') {
                monomialString = tail(monomialString);
            }
            monomialStrings.add(monomialString);
        }

        List<Pair<Monomial, BigInteger>> terms = new ArrayList<>();
        for (int i = monomialStrings.size() - 1; i >= 0; i--) {
            monomialString = monomialStrings.get(i);
            if (monomialString.isEmpty()) return Optional.empty();
            char head = head(monomialString);
            if (all(c -> c < 'a' || c > 'z', monomialString)) {
                Optional<BigInteger> oConstant = Readers.readBigIntegerStrict(monomialString);
                if (!oConstant.isPresent()) return Optional.empty();
                BigInteger constant = oConstant.get();
                if (constant.equals(BigInteger.ZERO)) return Optional.empty();
                terms.add(new Pair<>(Monomial.ONE, constant));
            } else if (head >= 'a' && head <= 'z') {
                Optional<Monomial> oMonomial = Monomial.readStrict(monomialString);
                if (!oMonomial.isPresent()) return Optional.empty();
                terms.add(new Pair<>(oMonomial.get(), BigInteger.ONE));
            } else {
                if (head == '-') {
                    if (monomialString.length() == 1) return Optional.empty();
                    char second = monomialString.charAt(1);
                    if (second >= 'a' && second <= 'z') {
                        Optional<Monomial> oMonomial = Monomial.readStrict(tail(monomialString));
                        if (!oMonomial.isPresent()) return Optional.empty();
                        terms.add(new Pair<>(oMonomial.get(), IntegerUtils.NEGATIVE_ONE));
                        continue;
                    }
                }
                int starIndex = monomialString.indexOf('*');
                if (starIndex == -1) return Optional.empty();
                Optional<BigInteger> oFactor = Readers.readBigIntegerStrict(monomialString.substring(0, starIndex));
                if (!oFactor.isPresent()) return Optional.empty();
                BigInteger factor = oFactor.get();
                if (factor.equals(BigInteger.ZERO) || factor.equals(BigInteger.ONE) ||
                        factor.equals(IntegerUtils.NEGATIVE_ONE)) {
                    return Optional.empty();
                }
                Optional<Monomial> oMonomial =
                        Monomial.readStrict(monomialString.substring(starIndex + 1));
                if (!oMonomial.isPresent()) return Optional.empty();
                Monomial monomial = oMonomial.get();
                terms.add(new Pair<>(monomial, factor));
            }
        }
        //noinspection RedundantCast
        if (!increasing(order, (Iterable<Monomial>) map(t -> t.a, terms))) return Optional.empty();
        if (order != DEFAULT_ORDER) {
            terms = sort((x, y) -> x.a.compareTo(y.a), terms);
        }
        return Optional.of(new MultivariatePolynomial(terms));
    }

    /**
     * Creates a {@code MultivariatePolynomial} from a {@code String}. Valid input takes the form of a {@code String}
     * that could have been returned by {@link MultivariatePolynomial#toString()}.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result may contain any {@code MultivariatePolynomial}, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of a {@code MultivariatePolynomial}
     * @return the {@code MultivariatePolynomial} represented by {@code s}, or an empty {@code Optional} if {@code s}
     * is invalid
     */
    public static @NotNull Optional<MultivariatePolynomial> readStrict(@NotNull String s) {
        return readStrict(s, DEFAULT_ORDER);
    }

    /**
     * Creates a {@code String} representation of {@code this}. The terms are descending with respect to {@code order}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code order} cannot be null.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @param order the monomial order with respect to which the terms descend
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString(@NotNull MonomialOrder order) {
        if (this == ZERO) return "0";
        List<Pair<Monomial, BigInteger>> sortedTerms = order == DEFAULT_ORDER ?
                terms :
                sort((x, y) -> order.compare(x.a, y.a), terms);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = sortedTerms.size() - 1; i >= 0; i--) {
            Pair<Monomial, BigInteger> term = sortedTerms.get(i);
            if (term.b.signum() == 1 && !first) {
                sb.append('+');
            }
            if (first) {
                first = false;
            }
            if (term.a == Monomial.ONE) {
                sb.append(term.b);
            } else {
                if (term.b.equals(IntegerUtils.NEGATIVE_ONE)) {
                    sb.append('-');
                } else if (!term.b.equals(BigInteger.ONE)) {
                    sb.append(term.b.toString()).append('*');
                }
                sb.append(term.a);
            }
        }
        return sb.toString();
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return toString(DEFAULT_ORDER);
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code MultivariatePolynomial} used
     * outside this class.
     */
    public void validate() {
        assertEquals(this, DEFAULT_ORDER, MonomialOrder.GREVLEX);
        assertTrue(this, all(t -> t != null && t.a != null && t.b != null && !t.b.equals(BigInteger.ZERO), terms));
        //noinspection RedundantCast
        assertTrue(this, increasing((Iterable<Monomial>) map(t -> t.a, terms)));
        if (equals(ZERO)) {
            assertTrue(this, this == ZERO);
        }
        if (equals(ONE)) {
            assertTrue(this, this == ONE);
        }
    }
}
