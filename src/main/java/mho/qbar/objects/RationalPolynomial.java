package mho.qbar.objects;

import mho.wheels.misc.Readers;
import mho.wheels.ordering.comparators.ShortlexComparator;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;

/**
 * <p>A univariate polynomial in x with {@link mho.qbar.objects.Rational} coefficients.
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code RationalPolynomial}s using {@code ==}. This is not true for {@code X}.
 *
 * <p>This class uses a little-endian dense representation; in other words, the coefficient of x<sup>i</sup> is located
 * at the ith position of the coefficient list. The list contains no trailing zeros. Zero is represented by the empty
 * list.
 *
 * <p>This class is immutable.
 */
public class RationalPolynomial implements Comparable<RationalPolynomial>, Iterable<Rational> {
    /**
     * 0
     */
    public static final @NotNull RationalPolynomial ZERO = new RationalPolynomial(new ArrayList<>());

    /**
     * 1
     */
    public static final @NotNull RationalPolynomial ONE = new RationalPolynomial(Arrays.asList(Rational.ONE));

    /**
     * x
     */
    public static final @NotNull RationalPolynomial X = new RationalPolynomial(
            Arrays.asList(Rational.ZERO, Rational.ONE)
    );

    /**
     * Used by {@link mho.qbar.objects.RationalPolynomial#compareTo}
     */
    private static final Comparator<Iterable<Rational>> RATIONAL_ITERABLE_COMPARATOR = new ShortlexComparator<>();

    /**
     * The polynomial's coefficients. The coefficient of x<sup>i</sup> is at the ith position.
     */
    private final @NotNull List<Rational> coefficients;

    /**
     * Private constructor for {@code Polynomial}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code coefficients} cannot have any null elements and cannot end in a 0.</li>
     *  <li>Any {@code RationalPolynomial} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param coefficients the polynomial's coefficients
     */
    private RationalPolynomial(@NotNull List<Rational> coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * Returns an {@code Iterator} over this {@code RationalPolynomial}'s coefficients, from lowest-degree to
     * highest-degree. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is finite, contains no nulls, and does not end with 0.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @return an {@code Iterator} over this {@code RationalPolynomial}'s coefficients
     */
    @Override
    public @NotNull Iterator<Rational> iterator() {
        return new Iterator<Rational>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < coefficients.size();
            }

            @Override
            public Rational next() {
                return coefficients.get(i++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * Gets the coefficient of this {@code RationalPolynomial}'s x<sup>{@code i}</sup> term. If {@code i} is greater
     * than this {@code RationalPolynomial}'s degree, 0 is returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code i} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param i the power that the coefficient belongs to
     * @return the coefficient of x<sup>{@code i}</sup>
     */
    public @NotNull Rational coefficient(int i) {
        return i < coefficients.size() ? coefficients.get(i) : Rational.ZERO;
    }

    /**
     * Creates a {@code RationalPolynomial} from a list of {@code Rational} coefficients. Throws an exception if any
     * coefficient is null. Makes a defensive copy of {@code coefficients}. Throws out any trailing zeros.
     *
     * <ul>
     *  <li>{@code coefficients} cannot have any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * Length is at most |{@code coefficients}|
     *
     * @param coefficients the polynomial's coefficients, from least to most significant
     * @return the {@code RationalPolynomial} with the specified coefficients
     */
    public static @NotNull RationalPolynomial of(@NotNull List<Rational> coefficients) {
        if (any(i -> i == null, coefficients))
            throw new NullPointerException();
        int actualSize = coefficients.size();
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            if (coefficients.get(i) == Rational.ZERO) {
                actualSize--;
            } else {
                break;
            }
        }
        if (actualSize == 0) return ZERO;
        if (actualSize == 1 && coefficients.get(0) == Rational.ONE) return ONE;
        return new RationalPolynomial(toList(take(actualSize, coefficients)));
    }

    /**
     * Creates a constant (possibly zero) {@code RationalPolynomial}.
     *
     * <ul>
     *  <li>{@code c} cannot be null.</li>
     *  <li>The result has degree 0 or –1.</li>
     * </ul>
     *
     * Length is 0 if c is 0, or 1 otherwise
     *
     * @param c the constant
     * @return a constant {@code RationalPolynomial} equal to {@code c}
     */
    public static @NotNull RationalPolynomial of(@NotNull Rational c) {
        if (c == Rational.ZERO) return ZERO;
        if (c == Rational.ONE) return ONE;
        return new RationalPolynomial(Arrays.asList(c));
    }

    /**
     * Creates either a monomial or 0, i.e. a constant times a power of x.
     *
     * <ul>
     *  <li>{@code c} cannot be null.</li>
     *  <li>{@code p} cannot be negative.</li>
     *  <li>The result has 0 or 1 nonzero terms.</li>
     * </ul>
     *
     * Length is 0 if {@code c} is 0, {@code c}+1 otherwise
     *
     * @param c the monomial's coefficient
     * @param p the monomial's degree if {@code c} is nonzero
     * @return {@code c}x<sup>p</sup>
     */
    public static @NotNull RationalPolynomial of(@NotNull Rational c, int p) {
        if (p < 0)
            throw new IllegalArgumentException("power cannot be negative");
        if (c == Rational.ZERO) return ZERO;
        if (p == 0 && c == Rational.ONE) return ONE;
        return new RationalPolynomial(toList(concat(replicate(p, Rational.ZERO), Arrays.asList(c))));
    }

    /**
     * Returns this {@code RationalPolynomial}'s degree. We consider 0 to have degree –1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is at least –1.</li>
     * </ul>
     *
     * @return this {@code RationalPolynomial}'s degree
     */
    public int degree() {
        return coefficients.size() - 1;
    }

    /**
     * Returns the leading coefficient of {@code this}, or an empty {@code Optional} is {@code this} is 0.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result may be empty, or it may contain a nonzero {@code Rational}.</li>
     * </ul>
     *
     * @return the leading coefficient
     */
    public @NotNull Optional<Rational> leading() {
        return this == ZERO ? Optional.<Rational>empty() : Optional.of(coefficients.get(coefficients.size() - 1));
    }

    /**
     * Returns the negative of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * Length is deg({@code this})+1
     *
     * @return –{@code this}
     */
    public @NotNull RationalPolynomial negate() {
        if (this == ZERO) return ZERO;
        if (coefficients.size() == 1 && coefficients.get(0).equals(Rational.of(-1))) return ONE;
        return new RationalPolynomial(toList(map(Rational::negate, coefficients)));
    }

    /**
     * Returns the absolute value of {@code this}. In other words, if the leading coefficient of {@code this} is
     * negative, {@code this} is negated.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>The result is either 0 or a {@code RationalPolynomial} with a positive leading coefficient.</li>
     * </ul>
     *
     * @return |{@code this}|
     */
    public @NotNull RationalPolynomial abs() {
        if (this == ZERO || this == ONE) return this;
        return leading().get().signum() == 1 ? this : negate();
    }

    /**
     * Returns the sign of {@code this}; 0 if {@code this} is 0, or the sign of the leading coefficient.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @return sgn(p(∞))
     */
    public int signum() {
        return this == ZERO ? 0 : leading().get().signum();
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code RationalPolynomial} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        return this == that || that != null && getClass() == that.getClass() &&
                coefficients.equals(((RationalPolynomial) that).coefficients);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return coefficients.hashCode();
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. Asymptotic ordering is used; the ordering of two {@code RationalPolynomial}s is the
     * eventual ordering of their values at a sufficiently large, positive argument.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code RationalPolynomial} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull RationalPolynomial that) {
        if (this == that) return 0;
        int thisSign = signum();
        int thatSign = that.signum();
        if (thisSign > thatSign) return 1;
        if (thisSign < thatSign) return -1;
        List<Rational> thisAbsCoefficients = reverse(abs());
        List<Rational> thatAbsCoefficients = reverse(that.abs());
        int c = RATIONAL_ITERABLE_COMPARATOR.compare(thisAbsCoefficients, thatAbsCoefficients);
        return thisSign == -1 ? -c : c;
    }

    /**
     * Creates a {@code RationalPolynomial} from a {@code String}. Valid input takes the form of a {@code String} that
     * could have been returned by {@link mho.qbar.objects.RationalPolynomial#toString}. See that method's tests and
     * demos for examples of valid input. Caution: It's easy to run out of time and memory reading something like
     * {@code "x^1000000000"}.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<RationalPolynomial>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code RationalPolynomial}.
     * @return the wrapped {@code RationalPolynomial} represented by {@code s}, or {@code empty} if {@code s} is
     * invalid.
     */
    public static @NotNull Optional<RationalPolynomial> read(@NotNull String s) {
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

        List<Pair<Rational, Integer>> monomials = new ArrayList<>();
        for (int i = monomialStrings.size() - 1; i >= 0; i--) {
            monomialString = monomialStrings.get(i);
            int xIndex = monomialString.indexOf('x');
            if (xIndex == -1) {
                Optional<Rational> constant = Rational.read(monomialString);
                if (!constant.isPresent()) return Optional.empty();
                monomials.add(new Pair<>(constant.get(), 0));
            } else {
                Rational coefficient;
                switch (xIndex) {
                    case 0:
                        coefficient = Rational.ONE;
                        break;
                    case 1:
                        if (head(monomialString) != '-') return Optional.empty();
                        monomialString = tail(monomialString);
                        coefficient = Rational.of(-1);
                        break;
                    default:
                        if (monomialString.charAt(xIndex - 1) != '*') return Optional.empty();
                        String coefficientString = monomialString.substring(0, xIndex - 1);
                        Optional<Rational> oCoefficient = Rational.read(coefficientString);
                        if (!oCoefficient.isPresent()) return Optional.empty();
                        coefficient = oCoefficient.get();
                        // no 1*x, -1*x, 1*x^2, -1*x^2, ... allowed
                        if (coefficient.abs() == Rational.ONE) return Optional.empty();
                        monomialString = monomialString.substring(xIndex);
                        break;
                }
                int power;
                int caretIndex = monomialString.indexOf('^');
                switch (caretIndex) {
                    case -1:
                        if (!monomialString.equals("x")) return Optional.empty();
                        power = 1;
                        break;
                    case 1:
                        String powerString = monomialString.substring(caretIndex + 1);
                        Optional<Integer> oPower = Readers.readInteger(powerString);
                        if (!oPower.isPresent()) return Optional.empty();
                        power = oPower.get();
                        if (power < 2) return Optional.empty(); // no x^1, x^0, x^-1, ... allowed
                        break;
                    default:
                        return Optional.empty();
                }
                monomials.add(new Pair<>(coefficient, power));
            }
        }
        if (any(p -> p.a == Rational.ZERO, monomials)) return Optional.empty();
        if (!increasing((Iterable<Integer>) map(p -> p.b, monomials))) return Optional.empty();
        @SuppressWarnings("ConstantConditions")
        int degree = last(monomials).b;
        List<Rational> coefficients = toList(replicate(degree + 1, Rational.ZERO));
        for (Pair<Rational, Integer> monomial : monomials) {
            //noinspection ConstantConditions
            coefficients.set(monomial.b, monomial.a);
        }
        return Optional.of(new RationalPolynomial(coefficients));
    }

    /**
     * Finds the first occurrence of a {@code RationalPolynomial} in a {@code String}. Returns the
     * {@code RationalPolynomial} and the index at which it was found. Returns an empty {@code Optional} if no
     * {@code RationalPolynomial} is found. Only {@code String}s which could have been emitted by
     * {@link mho.qbar.objects.RationalPolynomial#toString} are recognized. The longest possible
     * {@code RationalPolynomial} is parsed. Caution: It's easy to run out of time and memory finding something like
     * {@code "x^1000000000"}.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code RationalPolynomial} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<RationalPolynomial, Integer>> findIn(@NotNull String s) {
        return Readers.genericFindIn(RationalPolynomial::read, "*+-/0123456789^x").apply(s);
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RationalPolynomial}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        if (this == ZERO) return "0";
        StringBuilder sb = new StringBuilder();
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            Rational coefficient = coefficients.get(i);
            if (coefficient == Rational.ZERO) continue;
            String power;
            switch (i) {
                case 0: power = ""; break;
                case 1: power = "x"; break;
                default: power = "x^" + i; break;
            }
            String monomialString;
            if (i == 0) {
                monomialString = coefficient.toString();
            } else {
                if (coefficient == Rational.ONE) {
                    monomialString = power;
                } else if (coefficient.equals(Rational.of(-1))) {
                    monomialString = cons('-', power);
                } else {
                    monomialString = coefficient + "*" + power;
                }
            }
            if (i != coefficients.size() - 1 && head(monomialString) != '-') {
                sb.append('+');
            }
            sb.append(monomialString);
        }
        return sb.toString();
    }
}
