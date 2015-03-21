package mho.qbar.objects;

import mho.wheels.misc.Readers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.le;

/**
 * <p>A univariate polynomial in x with {@code BigInteger} coefficients.
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code Polynomial}s using {@code ==}. This is not true for {@code X}.
 *
 * <p>This class uses a little-endian dense representation; in other words, the coefficient of x<sup>i</sup> is located
 * at the ith position of the coefficient list. The list contains no trailing zeros. Zero is represented by the empty
 * list.
 *
 * <p>This class is immutable.
 */
public class Polynomial {
    /**
     * 0
     */
    public static final @NotNull Polynomial ZERO = new Polynomial(new ArrayList<>());

    /**
     * 1
     */
    public static final @NotNull Polynomial ONE = new Polynomial(Arrays.asList(BigInteger.ONE));

    /**
     * x
     */
    public static final @NotNull Polynomial X = new Polynomial(Arrays.asList(BigInteger.ZERO, BigInteger.ONE));

    /**
     * The polynomial's coefficients. The coefficient of x<sup>i</sup> is at the ith position.
     */
    private final @NotNull List<BigInteger> coefficients;

    /**
     * Private constructor for {@code Polynomial}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code coefficients} cannot have any null elements and cannot end in a 0.</li>
     *  <li>Any {@code Polynomial} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param coefficients the polynomial's coefficients
     */
    private Polynomial(@NotNull List<BigInteger> coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * Creates a {@code Polynomial} from a list of {@code BigInteger} coefficients. Throws an exception if any
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
     * @return the {@code Polynomial} with the specified coefficients
     */
    public static @NotNull Polynomial of(@NotNull List<BigInteger> coefficients) {
        if (any(i -> i == null, coefficients))
            throw new NullPointerException();
        int actualSize = coefficients.size();
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            if (coefficients.get(i).equals(BigInteger.ZERO)) {
                actualSize--;
            } else {
                break;
            }
        }
        if (actualSize == 0) return ZERO;
        if (actualSize == 1 && coefficients.get(0).equals(BigInteger.ONE)) return ONE;
        return new Polynomial(toList(take(actualSize, coefficients)));
    }

    /**
     * Creates a constant (possibly zero) {@code Polynomial}.
     *
     * <ul>
     *  <li>{@code this} cannot be null.</li>
     *  <li>The result has degree 0 or –1.</li>
     * </ul>
     *
     * Length is 1
     *
     * @param c the constant
     * @return a constant {@code Polynomial} equal to {@code c}
     */
    public static @NotNull Polynomial of(@NotNull BigInteger c) {
        if (c.equals(BigInteger.ZERO)) return ZERO;
        if (c.equals(BigInteger.ONE)) return ONE;
        return new Polynomial(Arrays.asList(c));
    }

    /**
     * Returns this {@code Polynomial}'s degree. We consider 0 to have degree –1.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>The result is at least –1.</li>
     * </ul>
     *
     * @return this {@code Polynomial}'s degree
     */
    public int degree() {
        return coefficients.size() - 1;
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code Polynomial} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        return this == that || that != null && getClass() == that.getClass() &&
                coefficients.equals(((Polynomial) that).coefficients);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
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
     * Creates an {@code Polynomial} from a {@code String}. Valid input takes the form of a {@code String} that could
     * have been returned by {@link mho.qbar.objects.Polynomial#toString}. See that method's tests and demos for
     * examples of valid input. Caution: It's easy to run out of time and memory reading something like
     * {@code "x^1000000000"}.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<Polynomial>}.</li>
     * </ul>
     *
     * @param s a string representation of a {@code Polynomial}.
     * @return the wrapped {@code Polynomial} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<Polynomial> read(@NotNull String s) {
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

        List<Pair<BigInteger, Integer>> monomials = new ArrayList<>();
        for (int i = monomialStrings.size() - 1; i >= 0; i--) {
            monomialString = monomialStrings.get(i);
            int xIndex = monomialString.indexOf('x');
            if (xIndex == -1) {
                Optional<BigInteger> constant = Readers.readBigInteger(monomialString);
                if (!constant.isPresent()) return Optional.empty();
                monomials.add(new Pair<>(constant.get(), 0));
            } else {
                BigInteger coefficient;
                switch (xIndex) {
                    case 0:
                        coefficient = BigInteger.ONE;
                        break;
                    case 1:
                        if (head(monomialString) != '-') return Optional.empty();
                        monomialString = tail(monomialString);
                        coefficient = BigInteger.valueOf(-1);
                        break;
                    default:
                        if (monomialString.charAt(xIndex - 1) != '*') return Optional.empty();
                        String coefficientString = monomialString.substring(0, xIndex - 1);
                        Optional<BigInteger> oCoefficient = Readers.readBigInteger(coefficientString);
                        if (!oCoefficient.isPresent()) return Optional.empty();
                        coefficient = oCoefficient.get();
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
                        break;
                    default:
                        return Optional.empty();
                }
                monomials.add(new Pair<>(coefficient, power));
            }
        }
        if (any(p -> BigInteger.ZERO.equals(p.a), monomials)) return Optional.empty();
        if (!increasing((Iterable<Integer>) map(p -> p.b, monomials))) return Optional.empty();
        @SuppressWarnings("ConstantConditions")
        int degree = last(monomials).b;
        List<BigInteger> coefficients = toList(replicate(degree + 1, BigInteger.ZERO));
        for (Pair<BigInteger, Integer> monomial : monomials) {
            //noinspection ConstantConditions
            coefficients.set(monomial.b, monomial.a);
        }
        return Optional.of(new Polynomial(coefficients));
    }

    /**
     * Finds the first occurrence of an {@code Polynomial} in a {@code String}. Returns the {@code Polynomial} and the
     * index at which it was found. Returns an empty {@code Optional} if no {@code Polynomial} is found. Only
     * {@code String}s which could have been emitted by {@link mho.qbar.objects.Polynomial#toString} are recognized.
     * The longest possible {@code Polynomial} is parsed. Caution: It's easy to run out of time and memory finding
     * something like {@code "x^1000000000"}.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code Polynomial} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<Polynomial, Integer>> findIn(@NotNull String s) {
        return Readers.genericFindIn(Polynomial::read, "*+-0123456789^x").apply(s);
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Polynomial}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        if (this == ZERO) return "0";
        StringBuilder sb = new StringBuilder();
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            BigInteger coefficient = coefficients.get(i);
            if (coefficient.equals(BigInteger.ZERO)) continue;
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
                if (coefficient.equals(BigInteger.ONE)) {
                    monomialString = power;
                } else if (coefficient.equals(BigInteger.valueOf(-1))) {
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
