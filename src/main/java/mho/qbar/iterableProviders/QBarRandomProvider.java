package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.RandomProvider;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.ordering.Ordering.lt;
import static org.junit.Assert.assertTrue;

/**
 * <p>A {@code QBarRandomProvider} produces {@code Iterable}s that randomly generate some set of values with a
 * specified distribution. A {@code QBarRandomProvider} is deterministic, but not immutable: its state changes every
 * time a random value is generated. It may be reverted to its original state with
 * {@link QBarRandomProvider#reset}.</p>
 *
 * <p>{@code QBarRandomProvider} uses the cryptographically-secure ISAAC pseudorandom number generator, implemented in
 * {@link mho.wheels.random.IsaacPRNG}. The source of its randomness is a {@code int[]} seed. It contains two scale
 * parameters which some of the distributions depend on; the exact relationship between the parameters and the
 * distributions is specified in the distribution's documentation.</p>
 *
 * <p>To create an instance which shares a generator with {@code this}, use {@link QBarRandomProvider#copy()}. To
 * create an instance which copies the generator, use {@link QBarRandomProvider#deepCopy()}.</p>
 *
 * <p>Note that sometimes the documentation will say things like "returns an {@code Iterable} containing all
 * {@code String}s". This cannot strictly be true, since {@code IsaacPRNG} has a finite period, and will therefore
 * produce only a finite number of {@code String}s. So in general, the documentation often pretends that the source of
 * randomness is perfect (but still deterministic).</p>
 */
@SuppressWarnings("unused")
public final strictfp class QBarRandomProvider extends QBarIterableProvider {
    /**
     * Creates a new {@code QBarRandomProvider} with a {@code RandomProvider}.
     *
     * <ul>
     *  <li>{@code randomProvider} cannot be null.</li>
     *  <li>Any {@code QBarRandomProvider} may be created with this constructor.</li>
     * </ul>
     *
     * @param randomProvider an {@code RandomProvider}
     */
    private QBarRandomProvider(@NotNull RandomProvider randomProvider) {
        super(randomProvider);
    }

    /**
     * Constructs a {@code QBarRandomProvider} with a seed generated from the current system time.
     *
     * <ul>
     *  <li>(conjecture) Any {@code QBarRandomProvider} with default {@code scale} and {@code secondaryScale} may be
     *  constructed with this constructor.</li>
     * </ul>
     */
    public QBarRandomProvider() {
        super(new RandomProvider());
    }

    /**
     * Constructs a {@code QBarRandomProvider} with a given seed.
     *
     * <ul>
     *  <li>{@code seed} must have length {@link mho.wheels.random.IsaacPRNG#SIZE}.</li>
     *  <li>Any {@code QBarRandomProvider} with default {@code scale} and {@code secondaryScale} may be constructed
     *  with this constructor.</li>
     * </ul>
     *
     * @param seed the source of randomness
     */
    public QBarRandomProvider(List<Integer> seed) {
        super(new RandomProvider(seed));
    }

    /**
     * A {@code QBarRandomProvider} used for testing. This allows for deterministic testing without manually setting up
     * a lengthy seed each time.
     */
    public static @NotNull QBarRandomProvider example() {
        return new QBarRandomProvider(RandomProvider.example());
    }

    /**
     * Returns {@code this}'s scale parameter.
     *
     * <ul>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return the scale parameter of {@code this}
     */
    public int getScale() {
        return ((RandomProvider) wheelsProvider).getScale();
    }

    /**
     * Returns {@code this}'s other scale parameter.
     *
     * <ul>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return the other scale parameter of {@code this}
     */
    public int getSecondaryScale() {
        return ((RandomProvider) wheelsProvider).getSecondaryScale();
    }

    /**
     * Returns {@code this}'s seed. Makes a defensive copy.
     *
     * <ul>
     *  <li>The result is an array of {@link mho.wheels.random.IsaacPRNG#SIZE} {@code int}s.</li>
     * </ul>
     *
     * @return the seed of {@code this}
     */
    public @NotNull List<Integer> getSeed() {
        return ((RandomProvider) wheelsProvider).getSeed();
    }

    /**
     * A {@code QBarRandomProvider} with the same fields as {@code this}. The copy shares its PRNG with the original.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return A copy of {@code this}.
     */
    @Override
    public @NotNull QBarRandomProvider copy() {
        return new QBarRandomProvider(((RandomProvider) wheelsProvider).copy());
    }

    /**
     * A {@code QBarRandomProvider} with the same fields as {@code this}. The copy receives a new copy of the PRNG, so
     * generating values from the copy will not affect the state of the original's PRNG.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return A copy of {@code this}.
     */
    @Override
    public @NotNull QBarRandomProvider deepCopy() {
        return new QBarRandomProvider(((RandomProvider) wheelsProvider).deepCopy());
    }

    /**
     * A {@code QBarRandomProvider} with the same fields as {@code this} except for a new scale.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param scale the new scale
     * @return A copy of {@code this} with a new scale
     */
    @Override
    public @NotNull QBarIterableProvider withScale(int scale) {
        return new QBarRandomProvider((RandomProvider) wheelsProvider.withScale(scale));
    }

    /**
     * A {@code QBarRandomProvider} with the same fields as {@code this} except for a new secondary scale.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>{@code secondaryScale} mat be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param secondaryScale the new secondary scale
     * @return A copy of {@code this} with a new secondary scale
     */
    @Override
    public @NotNull QBarIterableProvider withSecondaryScale(int secondaryScale) {
        return new QBarRandomProvider((RandomProvider) wheelsProvider.withScale(secondaryScale));
    }

    /**
     * Returns an id which has a good chance of being different in two instances with unequal {@code prng}s. It's used
     * in {@link QBarRandomProvider#toString()} to distinguish between different {@code QBarRandomProvider} instances.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>{@code this} may be any {@code long}.</li>
     * </ul>
     */
    public long getId() {
        return ((RandomProvider) wheelsProvider).getId();
    }

    /**
     * Shuffles a {@code List} in place using the Fisher-Yates algorithm. If the list's elements are unique, every
     * permutation is an equally likely outcome.
     *
     * <ul>
     *  <li>{@code xs} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param xs the {@code List} to be shuffled
     * @param <T> the type of the {@code List}'s elements
     */
    public <T> void shuffle(@NotNull List<T> xs) {
        ((RandomProvider) wheelsProvider).shuffle(xs);
    }

    /**
     * An {@code Iterable} that generates all positive {@code Rational}s. Each {@code Rational}'s bit size (defined as
     * the sum of the numerator 'sand denominator's bit sizes) is chosen from a geometric distribution with mean
     * approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 4.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing positive {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> positiveRationals() {
        int leftScale = getScale() / 2;
        int rightScale = (getScale() & 1) == 0 ? leftScale : leftScale + 1;
        return map(
                p -> Rational.of(p.a, p.b),
                filterInfinite(
                        q -> q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(withScale(leftScale).positiveBigIntegers(), withScale(rightScale).positiveBigIntegers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all negative {@code Rational}s. Each {@code Rational}'s bit size (defined as
     * the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution with mean
     * approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 4.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing negative {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> negativeRationals() {
        int leftScale = getScale() / 2;
        int rightScale = (getScale() & 1) == 0 ? leftScale : leftScale + 1;
        return map(
                p -> Rational.of(p.a, p.b),
                filterInfinite(
                        q -> q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(withScale(leftScale).negativeBigIntegers(), withScale(rightScale).positiveBigIntegers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code Rational}s. Each {@code Rational}'s bit size (defined as
     * the sum of the numerator and denominator's bit size) is chosen from a geometric distribution with mean
     * approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 4.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing nonzero {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> nonzeroRationals() {
        int leftScale = getScale() / 2;
        int rightScale = (getScale() & 1) == 0 ? leftScale : leftScale + 1;
        return map(
                p -> Rational.of(p.a, p.b),
                filterInfinite(
                        q -> q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(withScale(leftScale).nonzeroBigIntegers(), withScale(rightScale).positiveBigIntegers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@link Rational}s. Each {@code Rational}'s bit size (defined as the sum
     * of the numerator's and denominator's bit sizes) is chosen from a geometric distribution with mean approximately
     * {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> rationals() {
        int leftScale = getScale() / 2;
        int rightScale = (getScale() & 1) == 0 ? leftScale : leftScale + 1;
        return map(
                p -> Rational.of(p.a, p.b),
                filterInfinite(
                        q -> q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(withScale(leftScale).bigIntegers(), withScale(rightScale).positiveBigIntegers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Rational} in the interval [0, 1). Each {@code Rational}'s bit size
     * (defined as the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution with
     * mean approximately {@code size}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 4.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Rational}s in the interval
     *  [0, 1).</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne() {
        int leftScale = getScale() / 2;
        int rightScale = (getScale() & 1) == 0 ? leftScale : leftScale + 1;
        return map(
                p -> Rational.of(p.a, p.b),
                filterInfinite(
                        q -> lt(q.a, q.b) && q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(withScale(leftScale).positiveBigIntegers(), withScale(rightScale).positiveBigIntegers())
                )
        );
    }

    @Override
    public @NotNull Iterable<Rational> rangeUp(@NotNull Rational a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> rangeDown(@NotNull Rational a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b) {
        return null;
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Interval} with finite bounds. Each
     * {@code Interval}'s bit size (defined as the sum of the lower bound's and upper bound's bit sizes) is chosen from
     * a geometric distribution with mean approximately {@code meanBitSize} (The ratio between the actual mean bit size
     * and {@code meanBitSize} decreases as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 11.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Interval}s with finite bounds.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Interval> finitelyBoundedIntervals() {
        Iterable<Rational> bounds = withScale(getScale() / 2).rationals();
        return map(p -> Interval.of(p.a, p.b), filter(p -> le(p.a, p.b), pairs(bounds)));
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Interval}. Each {@code Interval}'s bit size (defined
     * as the sum of the lower bound's and upper bound's bit sizes) is chosen from a geometric distribution with mean
     * approximately {@code meanBitSize} (The ratio between the actual mean bit size and {@code meanBitSize} decreases
     * as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 11.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Interval}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Interval> intervals() {
        Iterable<Rational> bounds = withScale(getScale() / 2).rationals();
        return map(
                p -> {
                    if (!p.a.isPresent() && !p.b.isPresent()) return Interval.ALL;
                    if (!p.a.isPresent()) return Interval.lessThanOrEqualTo(p.b.get());
                    if (!p.b.isPresent()) return Interval.greaterThanOrEqualTo(p.a.get());
                    return Interval.of(p.a.get(), p.b.get());
                },
                filter(p -> !p.a.isPresent() || !p.b.isPresent() || le(p.a.get(), p.b.get()), pairs(optionals(bounds)))
        );
    }

    @Override
    public @NotNull Iterable<Rational> rationals(@NotNull Interval a) {
        if (!a.getLower().isPresent() && !a.getUpper().isPresent()) {
            return rationals();
        } else if (!a.getLower().isPresent()) {
            return map(r -> a.getUpper().get().subtract(r), withElement(Rational.ZERO, positiveRationals()));
        } else if (!a.getUpper().isPresent()) {
            return map(r -> r.add(a.getLower().get()), withElement(Rational.ZERO, positiveRationals()));
        } else {
            Rational diameter = a.diameter().get();
            if (diameter == Rational.ZERO) return repeat(a.getLower().get());
            return concat(
                    Arrays.asList(a.getLower().get(), a.getUpper().get()),
                    tail(
                            map(
                                    r -> r.multiply(diameter).add(a.getLower().get()),
                                    nonNegativeRationalsLessThanOne()
                            )
                    )
            );
        }
    }

    public @NotNull Iterable<Rational> rationalsNotIn(@NotNull Interval a) {
        List<Interval> complement = a.complement();
        switch (complement.size()) {
            case 0:
                return Collections.emptyList();
            case 1:
                Interval x = complement.get(0);
                Rational boundary = a.getLower().isPresent() ? a.getLower().get() : a.getUpper().get();
                return filter(r -> !r.equals(boundary), rationals(x));
            case 2:
                Interval y = complement.get(0);
                Interval z = complement.get(1);
                return mux(
                        (List<Iterable<Rational>>) Arrays.asList(
                                filter(r -> !r.equals(y.getUpper().get()), rationals(y)),
                                filter(r -> !r.equals(z.getLower().get()), rationals(z))
                        )
                );
        }
        return null; //never happens
    }

    @Override
    public @NotNull Iterable<RationalVector> rationalVectors(int dimension) {
        return map(RationalVector::of, withScale(getSecondaryScale()).lists(dimension, rationals()));
    }

    @Override
    public @NotNull Iterable<RationalVector> rationalVectorsAtLeast(int minDimension) {
        return map(RationalVector::of, withScale(getSecondaryScale()).listsAtLeast(minDimension, rationals()));
    }

    @Override
    public @NotNull Iterable<RationalVector> rationalVectors() {
        return map(RationalVector::of, withScale(getSecondaryScale()).lists(rationals()));
    }

    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectors(int dimension) {
        if (dimension == 1) {
            return Arrays.asList(RationalVector.of(Rational.ZERO), RationalVector.of(Rational.ONE));
        }
        return map(
                RationalVector::reduce,
                filter(
                        v -> {
                            Optional<Rational> pivot = v.pivot();
                            return !pivot.isPresent() || pivot.get().signum() == 1;
                        },
                        map(
                                is -> RationalVector.of(toList(map(Rational::of, is))),
                                filter(
                                        js -> {
                                            BigInteger gcd = foldl(BigInteger::gcd, BigInteger.ZERO, js);
                                            return gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE);
                                        },
                                        withScale(getSecondaryScale()).lists(dimension, bigIntegers())
                                )
                        )
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectorsAtLeast(int minDimension) {
        return map(
                RationalVector::reduce,
                filter(
                        v -> {
                            Optional<Rational> pivot = v.pivot();
                            return !pivot.isPresent() || pivot.get().signum() == 1;
                        },
                        map(
                                is -> RationalVector.of(toList(map(Rational::of, is))),
                                filter(
                                        js -> {
                                            BigInteger gcd = foldl(BigInteger::gcd, BigInteger.ZERO, js);
                                            return gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE);
                                        },
                                        withScale(getSecondaryScale()).listsAtLeast(minDimension, bigIntegers())
                                )
                        )
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectors() {
        return map(
                RationalVector::reduce,
                filter(
                        v -> {
                            Optional<Rational> pivot = v.pivot();
                            return !pivot.isPresent() || pivot.get().signum() == 1;
                        },
                        map(
                                is -> RationalVector.of(toList(map(Rational::of, is))),
                                filter(
                                        js -> {
                                            BigInteger gcd = foldl(BigInteger::gcd, BigInteger.ZERO, js);
                                            return gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE);
                                        },
                                        withScale(getSecondaryScale()).lists(bigIntegers())
                                )
                        )
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalMatrix> rationalMatrices(int height, int width) {
        if (height == 0 || width == 0) return repeat(RationalMatrix.zero(height, width));
        return map(RationalMatrix::fromRows, lists(height, rationalVectors(width)));
    }

    @Override
    public @NotNull Iterable<RationalMatrix> rationalMatrices() {
        return null;
    }

    @Override
    public @NotNull Iterable<Polynomial> polynomials(int degree) {
        return map(
                js -> Polynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                        withScale(getSecondaryScale()).lists(degree + 1, bigIntegers())
                )
        );
    }

    @Override
    public @NotNull Iterable<Polynomial> polynomialsAtLeast(int minDegree) {
        return map(
                js -> Polynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                        withScale(getSecondaryScale()).listsAtLeast(minDegree + 1, bigIntegers())
                )
        );
    }

    @Override
    public @NotNull Iterable<Polynomial> polynomials() {
        return map(
                js -> Polynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                        withScale(getSecondaryScale()).lists(bigIntegers())
                )
        );
    }

    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomials(int degree) {
        return null;
    }

    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomialsAtLeast(int minDegree) {
        return null;
    }

    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomials() {
        return null;
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomials(int degree) {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || last(is) != Rational.ZERO,
                        withScale(getSecondaryScale()).lists(degree + 1, rationals())
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomialsAtLeast(int minDegree) {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || last(is) != Rational.ZERO,
                        withScale(getSecondaryScale()).listsAtLeast(minDegree + 1, rationals())
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomials() {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || last(is) != Rational.ZERO,
                        withScale(getSecondaryScale()).lists(rationals())
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomials(int degree) {
        return null;
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomialsAtLeast(int minDegree) {
        return null;
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomials() {
        return null;
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code QBarRandomProvider} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        return this == that || that != null && getClass() == that.getClass() &&
                wheelsProvider.equals(((QBarRandomProvider) that).wheelsProvider);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return wheelsProvider.hashCode();
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public String toString() {
        return "QBar" + wheelsProvider;
    }

    /**
     * Ensures that {@code this} is valid. Must return true for any {@code QBarRandomProvider} used outside this class.
     */
    public void validate() {
        assertTrue(wheelsProvider instanceof RandomProvider);
    }
}
