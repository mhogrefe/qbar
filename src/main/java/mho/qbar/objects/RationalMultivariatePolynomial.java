package mho.qbar.objects;

import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

/**
 * <p>A multivariate polynomial with {@link Rational} coefficients.</p>
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code RationalMultivariatePolynomial}s using {@code ==}.</p>
 *
 * <p>This class is immutable.</p>
 */
public final class RationalMultivariatePolynomial implements
        Comparable<RationalMultivariatePolynomial>,
        Iterable<Pair<Monomial, Rational>> {
    /**
     * 0
     */
    public static final @NotNull RationalMultivariatePolynomial ZERO =
            new RationalMultivariatePolynomial(Collections.emptyList());

    /**
     * 1
     */
    public static final @NotNull RationalMultivariatePolynomial ONE =
            new RationalMultivariatePolynomial(Collections.singletonList(new Pair<>(Monomial.ONE, Rational.ONE)));

    /**
     * The default order of the {@code Monomial}s in {@code this}
     */
    private static final @NotNull MonomialOrder DEFAULT_ORDER = MonomialOrder.GREVLEX;

    /**
     * This {@code RationalMultivariatePolynomial}'s terms. The second element of each pair is the coefficient of the
     * {@code Monomial} in the first slot. The terms are in grevlex order.
     */
    private final @NotNull List<Pair<Monomial, Rational>> terms;

    /**
     * Private constructor for {@code RationalMultivariatePolynomial}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code terms} cannot have any null elements. The {@code Monomial}s must be unique and in grevlex
     *  order.</li>
     *  <li>Any {@code RationalMultivariatePolynomial} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param terms the polynomial's terms
     */
    private RationalMultivariatePolynomial(@NotNull List<Pair<Monomial, Rational>> terms) {
        this.terms = terms;
    }

    /**
     * Returns an {@code Iterable} over this {@code RationalMultivariatePolynomial}'s terms, from lowest to highest
     * with a given {@code MonomialOrder}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code order} is not null.</li>
     *  <li>The result is finite, and contains no nulls. The {@code Monomial}s are increasing with respect to
     *  {@code order}.</li>
     * </ul>
     *
     * @param order the monomial order with respect to which the terms descend
     * @return an {@code Iterable} over this {@code RationalMultivariatePolynomial}'s terms
     */
    public @NotNull Iterable<Pair<Monomial, Rational>> iterable(@NotNull MonomialOrder order) {
        if (order == DEFAULT_ORDER) {
            return new NoRemoveIterable<>(terms);
        } else {
            return new NoRemoveIterable<>(sort((x, y) -> order.compare(x.a, y.a), terms));
        }
    }

    /**
     * Returns an {@code Iterator} over this {@code RationalMultivariatePolynomial}'s terms, from lowest to highest in
     * grevlex ordering. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>The result is finite, and contains no nulls. The {@code Monomial}s are in increasing grevlex order.</li>
     * </ul>
     *
     * @return an {@code Iterator} over this {@code RationalMultivariatePolynomial}'s terms
     */
    @Override
    public @NotNull Iterator<Pair<Monomial, Rational>> iterator() {
        return new NoRemoveIterable<>(terms).iterator();
    }

    /**
     * Determines whether the coefficients of {@code this} are all integers.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} has integral coefficients.
     */
    public boolean onlyHasIntegralCoefficients() {
        return all(t -> t.b.isInteger(), terms);
    }

    /**
     * Converts {@code this} to a {@code MultivariatePolynomial}.
     *
     * <ul>
     *  <li>{@code this} must only have integral coefficients.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return a {@code MultivariatePolynomial} with the same value as {@code this}
     */
    public @NotNull MultivariatePolynomial toMultivariatePolynomial() {
        if (this == ZERO) return MultivariatePolynomial.ZERO;
        if (this == ONE) return MultivariatePolynomial.ONE;
        return MultivariatePolynomial.of(toList(map(t -> new Pair<>(t.a, t.b.bigIntegerValueExact()), terms)));
    }

    /**
     * Returns the coefficient of a given {@code monomial}. If the coefficient is not present, 0 is returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code monomial} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param monomial the {@code Monomial} that the coefficient belongs to
     * @return the coefficient of {@code monomial} in {@code this}
     */
    public @NotNull Rational coefficient(@NotNull Monomial monomial) {
        return lookupSorted(terms, monomial).orElse(Rational.ZERO);
    }

    /**
     * Creates a {@code RationalMultivariatePolynomial} from a list of terms. Throws an exception if any term is null.
     * Makes a defensive copy of {@code terms}. Merges duplicates and throws out zero terms.
     *
     * <ul>
     *  <li>{@code terms} cannot have any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param terms the polynomial's terms
     * @return the {@code RationalMultivariatePolynomial} with the specified terms
     */
    public static @NotNull RationalMultivariatePolynomial of(@NotNull List<Pair<Monomial, Rational>> terms) {
        SortedMap<Monomial, Rational> termMap = new TreeMap<>();
        for (Pair<Monomial, Rational> term : terms) {
            if (term.a == null || term.b == null) {
                throw new NullPointerException();
            }
            Rational coefficient = termMap.get(term.a);
            if (coefficient == null) coefficient = Rational.ZERO;
            termMap.put(term.a, coefficient.add(term.b));
        }
        List<Pair<Monomial, Rational>> sortedTerms = new ArrayList<>();
        //noinspection Convert2streamapi
        for (Map.Entry<Monomial, Rational> entry : termMap.entrySet()) {
            if (entry.getValue() != Rational.ZERO) {
                sortedTerms.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }
        if (sortedTerms.isEmpty()) return ZERO;
        if (sortedTerms.size() == 1) {
            Pair<Monomial, Rational> term = head(sortedTerms);
            if (term.a == Monomial.ONE && term.b == Rational.ONE) {
                return ONE;
            }
        }
        return new RationalMultivariatePolynomial(sortedTerms);
    }

    /**
     * Creates a {@code RationalMultivariatePolynomial} containing a single term (or zero terms if the coefficient is
     * zero).
     *
     * <ul>
     *  <li>{@code m} cannot be null.</li>
     *  <li>{@code c} cannot be null.</li>
     *  <li>The result has no more than one term.</li>
     * </ul>
     *
     * @param m an {@code Monomial}
     * @param c the coefficient of {@code m}
     * @return a {@code RationalMultivariatePolynomial} equal to {@code m} multiplied by {@code c}
     */
    public static @NotNull RationalMultivariatePolynomial of(@NotNull Monomial m, @NotNull Rational c) {
        if (c == Rational.ZERO) return ZERO;
        if (m == Monomial.ONE && c == Rational.ONE) return ONE;
        return new RationalMultivariatePolynomial(Collections.singletonList(new Pair<>(m, c)));
    }

    /**
     * Creates a constant {@code RationalMultivariatePolynomial} equal to a given {@code Rational}.
     *
     * <ul>
     *  <li>{@code c} cannot be null.</li>
     *  <li>The result is constant.</li>
     * </ul>
     *
     * @param c a constant
     * @return the {@code RationalMultivariatePolynomial} equal to c
     */
    public static @NotNull RationalMultivariatePolynomial of(@NotNull Rational c) {
        if (c == Rational.ZERO) return ZERO;
        if (c == Rational.ONE) return ONE;
        return new RationalMultivariatePolynomial(Collections.singletonList(new Pair<>(Monomial.ONE, c)));
    }

    /**
     * Creates a constant {@code RationalMultivariatePolynomial} equal to a given {@code BigInteger}.
     *
     * <ul>
     *  <li>{@code c} cannot be null.</li>
     *  <li>The result is constant.</li>
     * </ul>
     *
     * @param c a constant
     * @return the {@code RationalMultivariatePolynomial} equal to c
     */
    public static @NotNull RationalMultivariatePolynomial of(@NotNull BigInteger c) {
        if (c.equals(BigInteger.ZERO)) return ZERO;
        if (c.equals(BigInteger.ONE)) return ONE;
        return new RationalMultivariatePolynomial(Collections.singletonList(new Pair<>(Monomial.ONE, Rational.of(c))));
    }

    /**
     * Creates a constant {@code RationalMultivariatePolynomial} equal to a given {@code int}.
     *
     * <ul>
     *  <li>{@code c} may be any {@code int}.</li>
     *  <li>The result is constant.</li>
     * </ul>
     *
     * @param c a constant
     * @return the {@code RationalMultivariatePolynomial} equal to c
     */
    public static @NotNull RationalMultivariatePolynomial of(int c) {
        if (c == 0) return ZERO;
        if (c == 1) return ONE;
        return new RationalMultivariatePolynomial(
                Collections.singletonList(new Pair<>(Monomial.ONE, Rational.of(c)))
        );
    }

    /**
     * Creates a {@code RationalMultivariatePolynomial} from a single {@code Variable}.
     *
     * <ul>
     *  <li>{@code v} cannot be null.</li>
     *  <li>The result contains one term, which is equal to a variable.</li>
     * </ul>
     *
     * @param v a {@code Variable}
     * @return the {@code RationalMultivariatePolynomial} equal to {@code v}
     */
    public static @NotNull RationalMultivariatePolynomial of(@NotNull Variable v) {
        return new RationalMultivariatePolynomial(Collections.singletonList(new Pair<>(Monomial.of(v), Rational.ONE)));
    }

    /**
     * Given a (univariate) {@code RationalPolynomial} and a variable, returns a {@code RationalMultivariatePolynomial}
     * equal to the polynomial applied to the variable.
     *
     * <ul>
     *  <li>{@code p} cannot be null.</li>
     *  <li>{@code v} cannot be null.</li>
     *  <li>The result is univariate.</li>
     * </ul>
     *
     * @param p a {@code RationalPolynomial}
     * @param v the independent variable of the result
     * @return {@code p} with {@code v}
     */
    public static @NotNull RationalMultivariatePolynomial of(@NotNull RationalPolynomial p, @NotNull Variable v) {
        if (p == RationalPolynomial.ZERO) return ZERO;
        if (p == RationalPolynomial.ONE) return ONE;
        List<Integer> exponents = toList(replicate(v.getIndex() + 1, 0));
        int lastIndex = exponents.size() - 1;
        List<Pair<Monomial, Rational>> terms = new ArrayList<>();
        for (int i = 0; i <= p.degree(); i++) {
            Rational coefficient = p.coefficient(i);
            if (coefficient != Rational.ZERO) {
                exponents.set(lastIndex, i);
                terms.add(new Pair<>(Monomial.of(exponents), p.coefficient(i)));
            }
        }
        return new RationalMultivariatePolynomial(terms);
    }

    /**
     * Converts {@code this} to a univariate {@code RationalPolynomial}.
     *
     * <ul>
     *  <li>{@code this} cannot have more than one variable.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return a {@code RationalPolynomial} equal to {@code this}
     */
    public @NotNull RationalPolynomial toRationalPolynomial() {
        if (this == ZERO) return RationalPolynomial.ZERO;
        if (this == ONE) return RationalPolynomial.ONE;
        List<Variable> variables = variables();
        if (variables.size() > 1) {
            throw new IllegalArgumentException("this cannot have more than one variable. Invalid this: " + this);
        }
        Optional<Variable> variable = variables.isEmpty() ? Optional.empty() : Optional.of(head(variables));
        List<Rational> coefficients = toList(replicate(degree() + 1, Rational.ZERO));
        for (Pair<Monomial, Rational> term : terms) {
            if (term.a == Monomial.ONE) {
                coefficients.set(0, term.b);
            } else {
                int exponent = term.a.exponent(variable.get());
                coefficients.set(exponent, term.b);
            }
        }
        return RationalPolynomial.of(coefficients);
    }

    /**
     * Returns all the variables in {@code this}, in ascending order.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>The result is in ascending order and has no repetitions.</li>
     * </ul>
     *
     * @return the variables in {@code this}
     */
    public @NotNull List<Variable> variables() {
        SortedSet<Variable> variables = new TreeSet<>();
        for (Pair<Monomial, Rational> term : terms) {
            variables.addAll(term.a.variables());
        }
        return toList(variables);
    }

    /**
     * Returns the number of variables in {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
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
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
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
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the maximum coefficient bit length
     */
    public int maxCoefficientBitLength() {
        if (this == ZERO) return 0;
        //noinspection RedundantCast
        return maximum((Iterable<Integer>) map(t -> t.b.bitLength(), terms));
    }

    /**
     * Returns the degree of {@code this}, which is the highest degree of any {@code term}. We consider 0 to have
     * degree –1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
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
     * Returns the degree (the highest power) of a variable in {@code this}. If the variable is not present, its degree
     * is –1 if {@code this} is zero and 1 otherwise.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code v} cannot be zero.</li>
     *  <li>The result is at least –1.</li>
     * </ul>
     *
     * @param v a {@code Variable}
     * @return the degree of {@code v} in {@code this}
     */
    public int degree(@NotNull Variable v) {
        int degree = -1;
        for (Pair<Monomial, Rational> term : terms) {
            int termDegree = term.a.exponent(v);
            if (termDegree > degree) {
                degree = termDegree;
            }
        }
        return degree;
    }

    /**
     * Whether {@code this} is homogeneous; whether the degrees of each {@code Monomial} are equal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
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
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code v} cannot be null.</li>
     *  <li>The result contains no nulls.</li>
     * </ul>
     *
     * @param v a {@code Variable}
     * @return {@code this} as coefficients of powers of {@code v}
     */
    public @NotNull List<RationalMultivariatePolynomial> coefficientsOfVariable(@NotNull Variable v) {
        if (this == ZERO) return Collections.emptyList();
        if (this.degree() == 0) return Collections.singletonList(this);
        Map<Integer, List<Pair<Monomial, Rational>>> coefficientMap = new HashMap<>();
        for (Pair<Monomial, Rational> term : terms) {
            int vPower = term.a.exponent(v);
            List<Pair<Monomial, Rational>> vPowerTerms = coefficientMap.get(vPower);
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
        List<RationalMultivariatePolynomial> coefficients = new ArrayList<>();
        for (int i = 0; i <= maxPower; i++) {
            List<Pair<Monomial, Rational>> powerTerms = coefficientMap.get(i);
            if (powerTerms == null) {
                coefficients.add(ZERO);
            } else {
                coefficients.add(of(powerTerms));
            }
        }
        return coefficients;
    }

    /**
     * Given a {@code List} of {@code Variable}s, decomposes {@code this} into a sum of products of pairs where the
     * first element of each pair is a unique monomial in {@code variables} and the second element does not contain
     * {@code variables}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code variables} cannot contain nulls.</li>
     *  <li>{@code order} cannot be null.</li>
     *  <li>The result is a {@code List} of pairs such that no variable in any first element is in any of the second
     *  elements and no variable in any second element is in any of the first elements. The first elements are unique
     *  and in ascending order according to {@code order}. None of the second elements is zero.</li>
     * </ul>
     *
     * @param variables the {@code Variable}s to group
     * @return {@code this} expressed as a sum of polynomial multiples of monomials in {@code variables}
     */
    public @NotNull List<Pair<Monomial, RationalMultivariatePolynomial>> groupVariables(
            @NotNull List<Variable> variables, @NotNull MonomialOrder order
    ) {
        if (any(v -> v == null, variables)) {
            throw new NullPointerException();
        }
        SortedMap<Monomial, RationalMultivariatePolynomial> groupedTerms;
        if (order == DEFAULT_ORDER) {
            groupedTerms = new TreeMap<>();
        } else {
            groupedTerms = new TreeMap<>(order);
        }
        for (Pair<Monomial, Rational> term : terms) {
            Monomial componentWithVariables = term.a.retainVariables(variables);
            Monomial componentWithoutVariables = term.a.removeVariables(variables);
            RationalMultivariatePolynomial withoutVariables = groupedTerms.get(componentWithVariables);
            if (withoutVariables == null) {
                withoutVariables = ZERO;
            }
            withoutVariables = withoutVariables.add(of(componentWithoutVariables, term.b));
            groupedTerms.put(componentWithVariables, withoutVariables);
        }
        return toList(fromMap(groupedTerms));
    }

    /**
     * Given a {@code List} of {@code Variable}s, decomposes {@code this} into a sum of products of pairs where the
     * first element of each pair is a unique monomial in {@code variables} and the second element does not contain
     * {@code variables}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code variables} cannot contain nulls.</li>
     *  <li>The result is a {@code List} of pairs such that no variable in any first element is in any of the second
     *  elements and no variable in any second element is in any of the first elements. The first elements are unique
     *  and in grevlex ascending order. None of the second elements is zero.</li>
     * </ul>
     *
     * @param variables the {@code Variable}s to group
     * @return {@code this} expressed as a sum of polynomial multiples of monomials in {@code variables}
     */
    public @NotNull List<Pair<Monomial, RationalMultivariatePolynomial>> groupVariables(
            @NotNull List<Variable> variables
    ) {
        if (any(v -> v == null, variables)) {
            throw new NullPointerException();
        }
        SortedMap<Monomial, RationalMultivariatePolynomial> groupedTerms = new TreeMap<>();
        for (Pair<Monomial, Rational> term : terms) {
            Monomial componentWithVariables = term.a.retainVariables(variables);
            Monomial componentWithoutVariables = term.a.removeVariables(variables);
            RationalMultivariatePolynomial withoutVariables = groupedTerms.get(componentWithVariables);
            if (withoutVariables == null) {
                withoutVariables = ZERO;
            }
            withoutVariables = withoutVariables.add(of(componentWithoutVariables, term.b));
            groupedTerms.put(componentWithVariables, withoutVariables);
        }
        return toList(fromMap(groupedTerms));
    }

    /**
     * Returns the sum of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalMultivariatePolynomial} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull RationalMultivariatePolynomial add(@NotNull RationalMultivariatePolynomial that) {
        if (this == ZERO) return that;
        if (that == ZERO) return this;
        List<Pair<Monomial, Rational>> sumTerms = new ArrayList<>();
        Iterator<Pair<Monomial, Rational>> thisTerms = terms.iterator();
        Iterator<Pair<Monomial, Rational>> thatTerms = that.terms.iterator();
        Pair<Monomial, Rational> thisTerm = thisTerms.hasNext() ? thisTerms.next() : null;
        Pair<Monomial, Rational> thatTerm = thatTerms.hasNext() ? thatTerms.next() : null;
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
                    Rational sum = thisTerm.b.add(thatTerm.b);
                    if (sum != Rational.ZERO) {
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
            Pair<Monomial, Rational> term = sumTerms.get(0);
            if (term.a == Monomial.ONE && term.b == Rational.ONE) return ONE;
        }
        return new RationalMultivariatePolynomial(sumTerms);
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return –{@code this}
     */
    public @NotNull RationalMultivariatePolynomial negate() {
        if (this == ZERO) return ZERO;
        if (terms.size() == 1) {
            Pair<Monomial, Rational> term = terms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(Rational.NEGATIVE_ONE)) return ONE;
        }
        return new RationalMultivariatePolynomial(toList(map(t -> new Pair<>(t.a, t.b.negate()), terms)));
    }

    /**
     * Returns the difference of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalMultivariatePolynomial} subtracted by {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull RationalMultivariatePolynomial subtract(@NotNull RationalMultivariatePolynomial that) {
        if (this == ZERO) return that.negate();
        if (that == ZERO) return this;
        List<Pair<Monomial, Rational>> differenceTerms = new ArrayList<>();
        Iterator<Pair<Monomial, Rational>> thisTerms = terms.iterator();
        Iterator<Pair<Monomial, Rational>> thatTerms = that.terms.iterator();
        Pair<Monomial, Rational> thisTerm = thisTerms.hasNext() ? thisTerms.next() : null;
        Pair<Monomial, Rational> thatTerm = thatTerms.hasNext() ? thatTerms.next() : null;
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
                        Rational difference = thisTerm.b.subtract(thatTerm.b);
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
            Pair<Monomial, Rational> term = differenceTerms.get(0);
            if (term.a == Monomial.ONE && term.b == Rational.ONE) return ONE;
        }
        return new RationalMultivariatePolynomial(differenceTerms);
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code that} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalMultivariatePolynomial multiply(int that) {
        if (this == ZERO || that == 0) return ZERO;
        if (this == ONE && that == 1) return ONE;
        if (that == -1 && terms.size() == 1) {
            Pair<Monomial, Rational> term = terms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(Rational.NEGATIVE_ONE)) {
                return ONE;
            }
        }
        return new RationalMultivariatePolynomial(toList(map(t -> new Pair<>(t.a, t.b.multiply(that)), terms)));
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalMultivariatePolynomial multiply(@NotNull BigInteger that) {
        if (this == ZERO || that.equals(BigInteger.ZERO)) return ZERO;
        if (this == ONE && that.equals(BigInteger.ONE)) return ONE;
        if (terms.size() == 1 && that.equals(IntegerUtils.NEGATIVE_ONE)) {
            Pair<Monomial, Rational> term = terms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(Rational.NEGATIVE_ONE)) {
                return ONE;
            }
        }
        return new RationalMultivariatePolynomial(toList(map(t -> new Pair<>(t.a, t.b.multiply(that)), terms)));
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Rational} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalMultivariatePolynomial multiply(@NotNull Rational that) {
        if (this == ZERO || that == Rational.ZERO) return ZERO;
        if (this == ONE && that == Rational.ONE) return ONE;
        if (terms.size() == 1 && that.equals(Rational.NEGATIVE_ONE)) {
            Pair<Monomial, Rational> term = terms.get(0);
            if (term.a == Monomial.ONE && term.b.equals(Rational.NEGATIVE_ONE)) {
                return ONE;
            }
        }
        return new RationalMultivariatePolynomial(toList(map(t -> new Pair<>(t.a, t.b.multiply(that)), terms)));
    }

    /**
     * Returns the product of {@code this} and a single term, specified by a {@code Monomial} and its coefficient.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code c} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param m the {@code Monomial} of the term {@code this} is multiplied by
     * @return {@code this}×{@code c}×{@code m}
     */
    public @NotNull RationalMultivariatePolynomial multiply(@NotNull Monomial m, @NotNull Rational c) {
        if (this == ZERO || c == Rational.ZERO) return ZERO;
        if (this == ONE) return of(m, c);
        if (m == Monomial.ONE) return multiply(c);
        return new RationalMultivariatePolynomial(
                toList(map(t -> new Pair<>(t.a.multiply(m), t.b.multiply(c)), terms))
        );
    }

    /**
     * Returns the product of {@code this} and {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code RationalMultivariatePolynomial} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull RationalMultivariatePolynomial multiply(@NotNull RationalMultivariatePolynomial that) {
        if (this == ZERO || that == ZERO) return ZERO;
        if (this == ONE) return that;
        if (that == ONE) return this;
        return sum(toList(map(t -> multiply(t.a, t.b), that.terms)));
    }

    //todo
    public @NotNull Pair<Rational, MultivariatePolynomial> constantFactor() {
        if (this == ZERO) {
            throw new ArithmeticException("this cannot be zero.");
        }
        MultivariatePolynomial positivePrimitive = MultivariatePolynomial.of(
                toList(zip(map(t -> t.a, terms), Rational.cancelDenominators(toList(map(t -> t.b, terms)))))
        );
        //todo use leading
        if (last(terms).b.signum() == -1) {
            positivePrimitive = positivePrimitive.negate();
        }
        return new Pair<>(last(terms).b.divide(last(positivePrimitive).b), positivePrimitive);
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull RationalMultivariatePolynomial shiftLeft(int bits) {
        if (this == ZERO || bits == 0) return this;
        List<Pair<Monomial, Rational>> shiftedTerms = toList(map(t -> new Pair<>(t.a, t.b.shiftLeft(bits)), terms));
        return new RationalMultivariatePolynomial(shiftedTerms);
    }

    /**
     * Returns the right shift of {@code this} by {@code bits}; {@code this}×2<sup>–{@code bits}</sup>.
     *
     * <ul>
     *  <li>{@code this} can be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the number of bits to right-shift by
     * @return {@code this}≫{@code bits}
     */
    public @NotNull RationalMultivariatePolynomial shiftRight(int bits) {
        if (this == ZERO || bits == 0) return this;
        List<Pair<Monomial, Rational>> shiftedTerms = toList(map(t -> new Pair<>(t.a, t.b.shiftRight(bits)), terms));
        return new RationalMultivariatePolynomial(shiftedTerms);
    }

    /**
     * Returns the sum of all the {@code RationalMultivariatePolynomial}s in {@code xs}. If {@code xs} is empty, 0 is
     * returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code RationalMultivariatePolynomial}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code RationalMultivariatePolynomial}s.
     * @return Σxs
     */
    public static @NotNull RationalMultivariatePolynomial sum(@NotNull List<RationalMultivariatePolynomial> xs) {
        return foldl(RationalMultivariatePolynomial::add, ZERO, xs);
    }

    /**
     * Returns the product of all the {@code RationalMultivariatePolynomial}s in {@code xs}. If {@code xs} is empty, 1
     * is returned.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code RationalMultivariatePolynomial}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code RationalMultivariatePolynomial}s.
     * @return Πxs
     */
    public static @NotNull RationalMultivariatePolynomial product(@NotNull List<RationalMultivariatePolynomial> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        if (any(x -> x == ZERO, xs)) {
            return ZERO;
        }
        return foldl(RationalMultivariatePolynomial::multiply, ONE, xs);
    }

    /**
     * Returns the differences between successive {@code RationalMultivariatePolynomial}s in {@code xs}. If {@code xs}
     * contains a single {@code RationalMultivariatePolynomial}, an empty {@code Iterable} is returned. {@code xs}
     * cannot be empty. Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} must not be empty and may not contain any nulls.</li>
     *  <li>The result does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code RationalMultivariatePolynomial}s.
     * @return Δxs
     */
    public static @NotNull Iterable<RationalMultivariatePolynomial> delta(
            @NotNull Iterable<RationalMultivariatePolynomial> xs
    ) {
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
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code p} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param p the power that {@code this} is raised to
     * @return {@code this}<sup>{@code p}</sup>
     */
    public @NotNull RationalMultivariatePolynomial pow(int p) {
        if (p < 0) {
            throw new ArithmeticException("p cannot be negative. Invalid p: " + p);
        }
        if (p == 0 || this == ONE) return ONE;
        if (this == ZERO) return ZERO;
        if (terms.size() == 1) {
            Pair<Monomial, Rational> term = terms.get(0);
            if (term.a == Monomial.ONE) {
                return of(term.b.pow(p));
            } else {
                return new RationalMultivariatePolynomial(
                        Collections.singletonList(new Pair<>(term.a.pow(p), term.b.pow(p)))
                );
            }
        }
        RationalMultivariatePolynomial result = ONE;
        RationalMultivariatePolynomial powerPower = null; // p^2^i
        for (boolean bit : IntegerUtils.bits(p)) {
            powerPower = powerPower == null ? this : powerPower.multiply(powerPower);
            if (bit) result = result.multiply(powerPower);
        }
        return result;
    }

    /**
     * Evaluates {@code this} by substituting a {@code Rational} for each variable. Every variable in {@code this} must
     * have an associated {@code Rational}. Unused variables are allowed.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code xs} may not have any null keys or values.</li>
     *  <li>Every {@code Variable} in {@code this} must be a key in {@code xs}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param xs the values to substitute for each variable in {@code this}
     * @return {@code this}({@code xs})
     */
    public @NotNull Rational applyRational(@NotNull Map<Variable, Rational> xs) {
        return Rational.sum(toList(map(t -> t.a.applyRational(xs).multiply(t.b), terms)));
    }

    /**
     * Substitutes variables in {@code this} with {@code Monomial}s specified by {@code ms}. Not every variable in
     * {@code this} needs to have an associated {@code Monomial}. Unused variables are also allowed.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code ms} may not have any null keys or values.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param ms the {@code Monomial}s to substitute for variables in {@code this}
     * @return {@code this}({@code ms})
     */
    public @NotNull RationalMultivariatePolynomial substituteMonomial(@NotNull Map<Variable, Monomial> ms) {
        return of(toList(map(t -> new Pair<>(t.a.substitute(ms), t.b), terms)));
    }

    /**
     * Substitutes variables in {@code this} with {@code RationalMultivariatePolynomial}s specified by {@code ps}. Not
     * every variable in {@code this} needs to have an associated {@code RationalMultivariatePolynomial}. Unused
     * variables are also allowed.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code ps} may not have any null keys or values.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is 1 if {@code this}=0 or {@code that}=0, deg({@code this})×deg({@code that})+1 otherwise
     *
     * @param ps the {@code RationalMultivariatePolynomial}s to substitute for variables in {@code this}
     * @return {@code this}({@code ps})
     */
    public @NotNull RationalMultivariatePolynomial substitute(
            @NotNull Map<Variable, RationalMultivariatePolynomial> ps
    ) {
        for (Map.Entry<Variable, RationalMultivariatePolynomial> entry : ps.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                throw new NullPointerException();
            }
        }
        RationalMultivariatePolynomial result = ZERO;
        for (Pair<Monomial, Rational> term : terms) {
            RationalMultivariatePolynomial product = of(term.b);
            for (Pair<Variable, Integer> factor : term.a.terms()) {
                RationalMultivariatePolynomial p = ps.get(factor.a);
                if (p == null) {
                    p = of(factor.a);
                }
                product = product.multiply(p.pow(factor.b));
            }
            result = result.add(product);
        }
        return result;
    }

    /**
     * Given some {@code Variable}s v<sub>i</sub> and some monic {@code RationalPolynomials} p<sub>i</sub> such that
     * p<sub>i</sub>(v<sub>i</sub>)=0, reduces {@code this} so that the degree of v<sub>i</sub> in the result is less
     * than deg(p<sub>i</sub>). If a {@code Variable} isn't listed in {@code minimalPolynomials} it won't be reduced.
     * Unused variables are allowed.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code minimalPolynomials} may not have any null keys or values. All values must be monic and
     *  non-constant.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param minimalPolynomials {@code RationalPolynomial}s that define when variables are 0
     * @return {@code this} reduced by {@code minimalPolynomials}
     */
    public @NotNull RationalMultivariatePolynomial powerReduce(
            @NotNull Map<Variable, RationalPolynomial> minimalPolynomials
    ) {
        for (Map.Entry<Variable, RationalPolynomial> entry : minimalPolynomials.entrySet()) {
            RationalPolynomial p = entry.getValue();
            if (entry.getKey() == null || p == null) {
                throw new NullPointerException();
            }
            if (p.degree() < 1) {
                throw new IllegalArgumentException("All values in minimalPolynomials must be non-constant. Illegal" +
                        " value: " + p);
            }
            if (!p.isMonic()) {
                throw new IllegalArgumentException("All values in minimalPolynomials must be monic. Illegal value: " +
                        p);
            }
        }
        if (this == ZERO) return ZERO;
        Map<Variable, List<RationalMultivariatePolynomial>> powerTables = new HashMap<>();
        for (Map.Entry<Variable, RationalPolynomial> entry : minimalPolynomials.entrySet()) {
            Variable v = entry.getKey();
            powerTables.put(v, toList(map(p -> of(p, v), entry.getValue().powerTable(degree(v)))));
        }
        RationalMultivariatePolynomial sum = ZERO;
        for (Pair<Monomial, Rational> term : terms) {
            RationalMultivariatePolynomial product = of(term.b);
            for (Pair<Variable, Integer> factor : term.a.terms()) {
                List<RationalMultivariatePolynomial> powerTable = powerTables.get(factor.a);
                int power = factor.b;
                product = product.multiply(
                        powerTable == null ?
                                of(Monomial.of(factor.a).pow(power), Rational.ONE) :
                                powerTable.get(power)
                );
            }
            sum = sum.add(product);
        }
        return sum;
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
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
                that != null && getClass() == that.getClass() &&
                terms.equals(((RationalMultivariatePolynomial) that).terms);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
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
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that the {@code Polynomial} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull RationalMultivariatePolynomial that) {
        if (this == that) return 0;
        if (this == ZERO) return -1;
        if (that == ZERO) return 1;
        int i = terms.size() - 1;
        int j = that.terms.size() - 1;
        while (i >= 0 && j >= 0) {
            Pair<Monomial, Rational> thisTerm = terms.get(i);
            Pair<Monomial, Rational> thatTerm = that.terms.get(j);
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
     * Creates a {@code RationalMultivariatePolynomial} from a {@code String}. Valid input takes the form of a
     * {@code String} that could have been returned by {@link RationalMultivariatePolynomial#toString(MonomialOrder)}
     * applied to {@code order}.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>{@code order} cannot be null.</li>
     *  <li>The result may contain any {@code RationalMultivariatePolynomial}, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of a {@code RationalMultivariatePolynomial}
     * @param order the monomial order with respect to which the input terms must descend
     * @return the {@code RationalMultivariatePolynomial} represented by {@code s}, or an empty {@code Optional} if
     * {@code s} is invalid
     */
    public static @NotNull Optional<RationalMultivariatePolynomial> readStrict(
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

        List<Pair<Monomial, Rational>> terms = new ArrayList<>();
        for (int i = monomialStrings.size() - 1; i >= 0; i--) {
            monomialString = monomialStrings.get(i);
            if (monomialString.isEmpty()) return Optional.empty();
            char head = head(monomialString);
            if (all(c -> c < 'a' || c > 'z', monomialString)) {
                Optional<Rational> oConstant = Rational.readStrict(monomialString);
                if (!oConstant.isPresent()) return Optional.empty();
                Rational constant = oConstant.get();
                if (constant == Rational.ZERO) return Optional.empty();
                terms.add(new Pair<>(Monomial.ONE, constant));
            } else if (head >= 'a' && head <= 'z') {
                Optional<Monomial> oMonomial = Monomial.readStrict(monomialString);
                if (!oMonomial.isPresent()) return Optional.empty();
                terms.add(new Pair<>(oMonomial.get(), Rational.ONE));
            } else {
                if (head == '-') {
                    if (monomialString.length() == 1) return Optional.empty();
                    char second = monomialString.charAt(1);
                    if (second >= 'a' && second <= 'z') {
                        Optional<Monomial> oMonomial = Monomial.readStrict(tail(monomialString));
                        if (!oMonomial.isPresent()) return Optional.empty();
                        terms.add(new Pair<>(oMonomial.get(), Rational.NEGATIVE_ONE));
                        continue;
                    }
                }
                int starIndex = monomialString.indexOf('*');
                if (starIndex == -1) return Optional.empty();
                Optional<Rational> oFactor = Rational.readStrict(monomialString.substring(0, starIndex));
                if (!oFactor.isPresent()) return Optional.empty();
                Rational factor = oFactor.get();
                if (factor == Rational.ZERO || factor == Rational.ONE || factor.equals(Rational.NEGATIVE_ONE)) {
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
        return Optional.of(new RationalMultivariatePolynomial(terms));
    }

    /**
     * Creates a {@code RationalMultivariatePolynomial} from a {@code String}. Valid input takes the form of a
     * {@code String} that could have been returned by {@link RationalMultivariatePolynomial#toString()}.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result may contain any {@code RationalMultivariatePolynomial}, or be empty.</li>
     * </ul>
     *
     * @param s a string representation of a {@code RationalMultivariatePolynomial}
     * @return the {@code RationalMultivariatePolynomial} represented by {@code s}, or an empty {@code Optional} if
     * {@code s} is invalid
     */
    public static @NotNull Optional<RationalMultivariatePolynomial> readStrict(@NotNull String s) {
        return readStrict(s, DEFAULT_ORDER);
    }

    /**
     * Creates a {@code String} representation of {@code this}. The terms are descending with respect to {@code order}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>{@code order} cannot be null.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @param order the monomial order with respect to which the terms descend
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString(@NotNull MonomialOrder order) {
        if (this == ZERO) return "0";
        List<Pair<Monomial, Rational>> sortedTerms = order == DEFAULT_ORDER ?
                terms :
                sort((x, y) -> order.compare(x.a, y.a), terms);
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = sortedTerms.size() - 1; i >= 0; i--) {
            Pair<Monomial, Rational> term = sortedTerms.get(i);
            if (term.b.signum() == 1 && !first) {
                sb.append('+');
            }
            if (first) {
                first = false;
            }
            if (term.a == Monomial.ONE) {
                sb.append(term.b);
            } else {
                if (term.b.equals(Rational.NEGATIVE_ONE)) {
                    sb.append('-');
                } else if (term.b != Rational.ONE) {
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
     *  <li>{@code this} may be any {@code RationalMultivariatePolynomial}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return toString(DEFAULT_ORDER);
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any
     * {@code RationalMultivariatePolynomial} used outside this class.
     */
    public void validate() {
        assertEquals(this, DEFAULT_ORDER, MonomialOrder.GREVLEX);
        assertTrue(this, all(t -> t != null && t.a != null && t.b != null && t.b != Rational.ZERO, terms));
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
