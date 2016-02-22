package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>A multivariate polynomial with {@link BigInteger} coefficients.</p>
 *
 * <p>There is only one instance of {@code ZERO}, and one instance of {@code ONE}, so these may be compared with other
 * {@code MultivariatePolynomial}s using {@code ==}.</p>
 *
 * <p></p>
 *
 * <p>This class is immutable.</p>
 */
public class MultivariatePolynomial {
    /**
     * 0
     */
    public static final @NotNull MultivariatePolynomial ZERO = new MultivariatePolynomial(Collections.emptyList());

    /**
     * 1
     */
    public static final @NotNull MultivariatePolynomial ONE =
            new MultivariatePolynomial(Collections.singletonList(new Pair<>(BigInteger.ONE, ExponentVector.ONE)));

    private final @NotNull List<Pair<BigInteger, ExponentVector>> terms;

    private MultivariatePolynomial(@NotNull List<Pair<BigInteger, ExponentVector>> terms) {
        this.terms = terms;
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

        List<Pair<BigInteger, ExponentVector>> terms = new ArrayList<>();
        for (int i = monomialStrings.size() - 1; i >= 0; i--) {
            monomialString = monomialStrings.get(i);
            if (monomialString.isEmpty()) return Optional.empty();
            char head = head(monomialString);
            if (all(c -> c < 'a' || c > 'z', monomialString)) {
                Optional<BigInteger> oConstant = Readers.readBigInteger(monomialString);
                if (!oConstant.isPresent()) return Optional.empty();
                BigInteger constant = oConstant.get();
                if (constant.equals(BigInteger.ZERO)) return Optional.empty();
                terms.add(new Pair<>(constant, ExponentVector.ONE));
            } else if (head >= 'a' && head <= 'z') {
                Optional<ExponentVector> oExponentVector = ExponentVector.read(monomialString);
                if (!oExponentVector.isPresent()) return Optional.empty();
                terms.add(new Pair<>(BigInteger.ONE, oExponentVector.get()));
            } else {
                if (head == '-') {
                    if (monomialString.length() == 1) return Optional.empty();
                    char second = monomialString.charAt(1);
                    if (second >= 'a' && second <= 'z') {
                        Optional<ExponentVector> oExponentVector = ExponentVector.read(tail(monomialString));
                        if (!oExponentVector.isPresent()) return Optional.empty();
                        terms.add(new Pair<>(IntegerUtils.NEGATIVE_ONE, oExponentVector.get()));
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
                terms.add(new Pair<>(factor, exponentVector));
            }
        }
        //noinspection RedundantCast
        if (!increasing((Iterable<ExponentVector>) map(t -> t.b, terms))) return Optional.empty();
        return Optional.of(new MultivariatePolynomial(terms));
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
            Pair<BigInteger, ExponentVector> term = terms.get(i);
            if (term.a.signum() == 1 && !first) {
                sb.append('+');
            }
            if (first) {
                first = false;
            }
            if (term.a.equals(IntegerUtils.NEGATIVE_ONE)) {
                sb.append('-');
            } else if (!term.a.equals(BigInteger.ONE)) {
                sb.append(term.a.toString()).append('*');
            }
            sb.append(term.b);
        }
        return sb.toString();
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code MultivariatePolynomial} used
     * outside this class.
     */
    public void validate() {
        assertTrue(this, all(t -> t != null && t.a != null && t.b != null && !t.a.equals(BigInteger.ZERO), terms));
        //noinspection RedundantCast
        assertTrue(this, increasing((Iterable<ExponentVector>) map(t -> t.b, terms)));
        if (equals(ZERO)) {
            assertTrue(this, this == ZERO);
        }
        if (equals(ONE)) {
            assertTrue(this, this == ONE);
        }
    }
}
