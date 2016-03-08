package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>A multivariate polynomial with {@link BigInteger} coefficients.</p>
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code MultivariatePolynomial}s using {@code ==}.</p>
 *
 * <p>This class is immutable.</p>
 */
public class MultivariatePolynomial implements
        Comparable<MultivariatePolynomial>,
        Iterable<Pair<ExponentVector, BigInteger>> {
    /**
     * 0
     */
    public static final @NotNull MultivariatePolynomial ZERO = new MultivariatePolynomial(Collections.emptyList());

    /**
     * 1
     */
    public static final @NotNull MultivariatePolynomial ONE =
            new MultivariatePolynomial(Collections.singletonList(new Pair<>(ExponentVector.ONE, BigInteger.ONE)));

    /**
     * This {@code MultivariatePolynomial}'s terms. The second element of each pair is the coefficient of the
     * {@code ExponentVector} in the first slot. The terms are in grevlex order.
     */
    private final @NotNull List<Pair<ExponentVector, BigInteger>> terms;

    /**
     * Private constructor for {@code MultivariatePolynomial}; assumes argument is valid
     *
     * <ul>
     *  <li>{@code terms} cannot have any null elements. The {@code ExponentVector}s must be unique and in grevlex
     *  order.</li>
     *  <li>Any {@code MultivariatePolynomial} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param terms the polynomial's terms
     */
    private MultivariatePolynomial(@NotNull List<Pair<ExponentVector, BigInteger>> terms) {
        this.terms = terms;
    }

    /**
     * Returns an {@code Iterator} over this {@code MultivariatePolynomial}'s terms, from lowest to highest in grevlex
     * ordering. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>The result is finite, and contains no nulls. The {@code ExponentVector}s are in increasing grevlex
     *  order.</li>
     * </ul>
     *
     * @return an {@code Iterator} over this {@code MultivariatePolynomial}'s terms
     */
    @Override
    public @NotNull Iterator<Pair<ExponentVector, BigInteger>> iterator() {
        return new NoRemoveIterable<>(terms).iterator();
    }

    /**
     * Returns the coefficient of a given {@code exponentVector}. If the coefficient is not present, 0 is returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code MultivariatePolynomial}.</li>
     *  <li>{@code exponentVector} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param exponentVector the {@code ExponentVector} that the coefficient belongs to
     * @return the coefficient of {@code exponentVector} in {@code this}
     */
    public @NotNull BigInteger coefficient(@NotNull ExponentVector exponentVector) {
        return lookupSorted(terms, exponentVector).orElse(BigInteger.ZERO);
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
    public static @NotNull MultivariatePolynomial of(@NotNull List<Pair<ExponentVector, BigInteger>> terms) {
        SortedMap<ExponentVector, BigInteger> termMap = new TreeMap<>(Comparator.reverseOrder());
        for (Pair<ExponentVector, BigInteger> term : terms) {
            BigInteger coefficient = termMap.get(term.a);
            if (coefficient == null) coefficient = BigInteger.ZERO;
            termMap.put(term.a, coefficient.add(term.b));
        }
        List<Pair<ExponentVector, BigInteger>> sortedTerms = new ArrayList<>();
        //noinspection Convert2streamapi
        for (Map.Entry<ExponentVector, BigInteger> entry : termMap.entrySet()) {
            if (!entry.getValue().equals(BigInteger.ZERO)) {
                sortedTerms.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }
        return new MultivariatePolynomial(sortedTerms);
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
            Pair<ExponentVector, BigInteger> thisTerm = terms.get(i);
            Pair<ExponentVector, BigInteger> thatTerm = that.terms.get(i);
            int evCompare = thisTerm.a.compareTo(thatTerm.a);
            if (evCompare != 0) return evCompare;
            int iCompare = thisTerm.b.compareTo(thisTerm.b);
            if (iCompare != 0) return iCompare;
            i--;
            j--;
        }
        if (i < 0) return -1;
        if (j < 0) return 1;
        return 0;
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
    private static @NotNull Optional<MultivariatePolynomial> read(@NotNull String s) {
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

        List<Pair<ExponentVector, BigInteger>> terms = new ArrayList<>();
        for (int i = monomialStrings.size() - 1; i >= 0; i--) {
            monomialString = monomialStrings.get(i);
            if (monomialString.isEmpty()) return Optional.empty();
            char head = head(monomialString);
            if (all(c -> c < 'a' || c > 'z', monomialString)) {
                Optional<BigInteger> oConstant = Readers.readBigInteger(monomialString);
                if (!oConstant.isPresent()) return Optional.empty();
                BigInteger constant = oConstant.get();
                if (constant.equals(BigInteger.ZERO)) return Optional.empty();
                terms.add(new Pair<>(ExponentVector.ONE, constant));
            } else if (head >= 'a' && head <= 'z') {
                Optional<ExponentVector> oExponentVector = ExponentVector.read(monomialString);
                if (!oExponentVector.isPresent()) return Optional.empty();
                terms.add(new Pair<>(oExponentVector.get(), BigInteger.ONE));
            } else {
                if (head == '-') {
                    if (monomialString.length() == 1) return Optional.empty();
                    char second = monomialString.charAt(1);
                    if (second >= 'a' && second <= 'z') {
                        Optional<ExponentVector> oExponentVector = ExponentVector.read(tail(monomialString));
                        if (!oExponentVector.isPresent()) return Optional.empty();
                        terms.add(new Pair<>(oExponentVector.get(), IntegerUtils.NEGATIVE_ONE));
                        continue;
                    }
                }
                int starIndex = monomialString.indexOf('*');
                if (starIndex == -1) return Optional.empty();
                Optional<BigInteger> oFactor = Readers.readBigInteger(monomialString.substring(0, starIndex));
                if (!oFactor.isPresent()) return Optional.empty();
                BigInteger factor = oFactor.get();
                if (factor.equals(BigInteger.ZERO) || factor.equals(BigInteger.ONE) ||
                        factor.equals(IntegerUtils.NEGATIVE_ONE)) {
                    return Optional.empty();
                }
                Optional<ExponentVector> oExponentVector =
                        ExponentVector.read(monomialString.substring(starIndex + 1));
                if (!oExponentVector.isPresent()) return Optional.empty();
                ExponentVector exponentVector = oExponentVector.get();
                terms.add(new Pair<>(exponentVector, factor));
            }
        }
        //noinspection RedundantCast
        if (!increasing((Iterable<ExponentVector>) map(t -> t.a, terms))) return Optional.empty();
        return Optional.of(new MultivariatePolynomial(terms));
    }

    /**
     * Finds the first occurrence of a {@code MultivariatePolynomial} in a {@code String}. Returns the
     * {@code MultivariatePolynomial} and the index at which it was found. Returns an empty {@code Optional} if no
     * {@code MultivariatePolynomial} is found. Only {@code String}s which could have been emitted by
     * {@link MultivariatePolynomial#toString} are recognized. The longest possible {@code MultivariatePolynomial} is
     * parsed.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code MultivariatePolynomial} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<MultivariatePolynomial, Integer>> findIn(@NotNull String s) {
        return Readers.genericFindIn(MultivariatePolynomial::read, "*+-0123456789^abcdefghijklmnopqrstuvwxyz")
                .apply(s);
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
        if (this == ZERO) return "0";
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = terms.size() - 1; i >= 0; i--) {
            Pair<ExponentVector, BigInteger> term = terms.get(i);
            if (term.b.signum() == 1 && !first) {
                sb.append('+');
            }
            if (first) {
                first = false;
            }
            if (term.b.equals(IntegerUtils.NEGATIVE_ONE)) {
                sb.append('-');
            } else if (!term.b.equals(BigInteger.ONE)) {
                sb.append(term.b.toString()).append('*');
            }
            sb.append(term.a);
        }
        return sb.toString();
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code MultivariatePolynomial} used
     * outside this class.
     */
    public void validate() {
        assertTrue(this, all(t -> t != null && t.a != null && t.b != null && !t.b.equals(BigInteger.ZERO), terms));
        //noinspection RedundantCast
        assertTrue(this, increasing((Iterable<ExponentVector>) map(t -> t.a, terms)));
        if (equals(ZERO)) {
            assertTrue(this, this == ZERO);
        }
        if (equals(ONE)) {
            assertTrue(this, this == ONE);
        }
    }
}
