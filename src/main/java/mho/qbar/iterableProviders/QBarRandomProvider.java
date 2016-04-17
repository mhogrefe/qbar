package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.MathUtils;
import mho.wheels.ordering.Ordering;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.ge;
import static mho.wheels.ordering.Ordering.gt;
import static mho.wheels.ordering.Ordering.le;
import static org.junit.Assert.assertTrue;

/**
 * <p>A {@code QBarRandomProvider} produces {@code Iterable}s that randomly generate some set of values with a
 * specified distribution. A {@code QBarRandomProvider} is deterministic, but not immutable: its state changes every
 * time a random value is generated. It may be reverted to its original state with
 * {@link QBarRandomProvider#reset}.</p>
 *
 * <p>{@code QBarRandomProvider} uses the cryptographically-secure ISAAC pseudorandom number generator, implemented in
 * {@link mho.wheels.random.IsaacPRNG}. The source of its randomness is a {@code int[]} seed. It contains three scale
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
     * A list of all {@code MonomialOrder}s.
     */
    private static final @NotNull List<MonomialOrder> MONOMIAL_ORDERS =
            toList(QBarExhaustiveProvider.INSTANCE.monomialOrders());

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
     * Returns {@code this}'s first scale parameter.
     *
     * <ul>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return the first scale parameter of {@code this}
     */
    public int getScale() {
        return ((RandomProvider) wheelsProvider).getScale();
    }

    /**
     * Returns {@code this}'s second scale parameter.
     *
     * <ul>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return the second scale parameter of {@code this}
     */
    public int getSecondaryScale() {
        return ((RandomProvider) wheelsProvider).getSecondaryScale();
    }

    /**
     * Returns {@code this}'s third scale parameter.
     *
     * <ul>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return the third scale parameter of {@code this}
     */
    public int getTertiaryScale() {
        return ((RandomProvider) wheelsProvider).getTertiaryScale();
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
        return new QBarRandomProvider((RandomProvider) wheelsProvider.withSecondaryScale(secondaryScale));
    }

    /**
     * A {@code QBarRandomProvider} with the same fields as {@code this} except for a new tertiary scale.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>{@code tertiaryScale} mat be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param tertiaryScale the new tertiary scale
     * @return A copy of {@code this} with a new tertiary scale
     */
    @Override
    public @NotNull QBarIterableProvider withTertiaryScale(int tertiaryScale) {
        return new QBarRandomProvider((RandomProvider) wheelsProvider.withTertiaryScale(tertiaryScale));
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
     * the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution with mean
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
        int scale = getScale();
        if (scale < 4) {
            throw new IllegalStateException("this must have a scale of at least 4. Invalid scale: " + scale);
        }
        int leftScale = scale / 2;
        int rightScale = (scale & 1) == 0 ? leftScale : leftScale + 1;
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
        int scale = getScale();
        if (scale < 4) {
            throw new IllegalStateException("this must have a scale of at least 4. Invalid scale: " + scale);
        }
        int leftScale = scale / 2;
        int rightScale = (scale & 1) == 0 ? leftScale : leftScale + 1;
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
        int scale = getScale();
        if (scale < 4) {
            throw new IllegalStateException("this must have a scale of at least 4. Invalid scale: " + scale);
        }
        int leftScale = scale / 2;
        int rightScale = (scale & 1) == 0 ? leftScale : leftScale + 1;
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
        int scale = getScale();
        if (scale < 3) {
            throw new IllegalStateException("this must have a scale of at least 3. Invalid scale: " + scale);
        }
        int leftScale = scale / 2;
        int rightScale = (scale & 1) == 0 ? leftScale : leftScale + 1;
        return map(
                p -> Rational.of(p.a, p.b),
                filterInfinite(
                        q -> q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(withScale(leftScale).bigIntegers(), withScale(rightScale).positiveBigIntegers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Rational}s in the interval [0, 1). Each {@code Rational}'s bit
     * size (defined as the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution
     * with mean approximately {@code scale}. Does not support removal.
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
        int scale = getScale();
        if (scale < 4) {
            throw new IllegalStateException("this must have a scale of at least 4. Invalid scale: " + scale);
        }
        return map(
                p -> Rational.of(p.b, p.a),
                dependentPairsInfinite(
                        withScale(scale / 2).positiveBigIntegers(),
                        d -> filterInfinite(
                                n -> n.gcd(d).equals(BigInteger.ONE),
                                range(BigInteger.ZERO, d.subtract(BigInteger.ONE))
                        )
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Rational}s between {@code a} and {@code b}, inclusive. A larger
     * {@code scale} corresponds to elements with a larger mean bit size (sum of the bit lengths of the numerator and
     * denominator). Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 4.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} cannot be greater than {@code b}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Rational}s between {@code a} and {@code b}, inclusive
     */
    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b) {
        int scale = getScale();
        if (scale < 4) {
            throw new IllegalStateException("this must have a scale of at least 4. Invalid scale: " + scale);
        }
        switch (Ordering.compare(a, b)) {
            case GT: throw new IllegalArgumentException("a cannot be greater than b. Invalid a: " +
                    a + ", and invalid b: " + b);
            case EQ: return repeat(a);
            case LT:
                Rational diameter = b.subtract(a);
                return withElement(b, map(r -> r.multiply(diameter).add(a), nonNegativeRationalsLessThanOne()));
            default: throw new IllegalStateException("unreachable");
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Interval}s with finite bounds. A larger {@code scale} corresponds
     * to elements with a larger mean bit size. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 6.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing finitely-bounded
     *  {@code Interval}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Interval> finitelyBoundedIntervals() {
        int scale = getScale();
        if (scale < 6) {
            throw new IllegalStateException("this must have a scale of at least 6. Invalid scale: " + scale);
        }
        int leftScale = scale / 2;
        int rightScale = (scale & 1) == 0 ? leftScale : leftScale + 1;
        return map(
                p -> Interval.of(p.a, p.b),
                filterInfinite(
                        p -> le(p.a, p.b),
                        pairs(withScale(leftScale).rationals(), withScale(rightScale).rationals())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Interval}s. A larger {@code scale} corresponds to elements with a
     * larger mean bit size. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 6.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Interval}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Interval> intervals() {
        int scale = getScale();
        if (scale < 6) {
            throw new IllegalStateException("this must have a scale of at least 6. Invalid scale: " + scale);
        }
        QBarIterableProvider leftRP = withScale(scale / 2);
        QBarIterableProvider rightRP = withScale((scale & 1) == 0 ? scale / 2 : scale / 2 + 1);
        return map(
                p -> {
                    if (!p.a.isPresent() && !p.b.isPresent()) return Interval.ALL;
                    if (!p.a.isPresent()) return Interval.lessThanOrEqualTo(p.b.get());
                    if (!p.b.isPresent()) return Interval.greaterThanOrEqualTo(p.a.get());
                    return Interval.of(p.a.get(), p.b.get());
                },
                filterInfinite(
                        p -> !p.a.isPresent() || !p.b.isPresent() || le(p.a.get(), p.b.get()),
                        pairs(leftRP.optionals(leftRP.rationals()), rightRP.optionals(rightRP.rationals()))
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Rational}s contained in a given {@code Interval}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 4.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a an {@code Interval}
     * @return {r|r∈{@code a}}
     */
    @Override
    public @NotNull Iterable<Rational> rationalsIn(@NotNull Interval a) {
        int scale = getScale();
        if (scale < 4) {
            throw new IllegalStateException("this must have a scale of at least 4. Invalid scale: " + scale);
        }
        return super.rationalsIn(a);
    }

    /**
     * An {@code Iterable} that generates all {@code Rational}s not contained in a given {@code Interval}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 4.</li>
     *  <li>{@code a} cannot be (–∞, ∞).</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a an {@code Interval}
     * @return {r|r∉{@code a}}
     */
    @Override
    public @NotNull Iterable<Rational> rationalsNotIn(@NotNull Interval a) {
        int scale = getScale();
        if (scale < 4) {
            throw new IllegalStateException("this must have a scale of at least 4. Invalid scale: " + scale);
        }
        List<Interval> complement = a.complement();
        switch (complement.size()) {
            case 0:
                throw new IllegalArgumentException("a cannot be (-Infinity, Infinity).");
            case 1:
                Rational boundary = a.getLower().isPresent() ? a.getLower().get() : a.getUpper().get();
                return filterInfinite(r -> !r.equals(boundary), rationalsIn(complement.get(0)));
            case 2:
                Rational x = complement.get(0).getUpper().get();
                Rational y = complement.get(1).getLower().get();
                //noinspection RedundantCast
                return choose(
                        filterInfinite(r -> !r.equals(x), rangeDown(x)),
                        filterInfinite(r -> !r.equals(y), rangeUp(y))
                );
            default: throw new IllegalStateException("unreachable");
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Vector}s. Each {@code Vector}'s dimension is chosen from a
     * geometric distribution with mean {@code scale}, and each coordinate's bit size is chosen from a geometric
     * distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a positive {@code secondaryScale}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Vector}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Vector> vectors() {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 1) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return map(Vector::of, withScale(secondaryScale).lists(bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all {@code Vector}s with a minimum dimension. Each {@code Vector}'s dimension
     * is chosen from a geometric distribution with mean {@code secondaryScale}, and each coordinate's bit size is
     * chosen from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a {@code secondaryScale} greater than
     *  {@code minDimension}.</li>
     *  <li>{@code minDimension} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Vector}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDimension the minimum dimension of the generated {@code Vector}s
     * @return {@code Vector}s with dimension at least {@code minDimension}
     */
    @Override
    public @NotNull Iterable<Vector> vectorsAtLeast(int minDimension) {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale <= minDimension) {
            throw new IllegalStateException("this must have a secondaryScale greater than minDimension." +
                    " secondaryScale: " + secondaryScale + ", minDimension: " + minDimension);
        }
        return map(Vector::of, withScale(secondaryScale).listsAtLeast(minDimension, bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all {@code RationalVector}s. Each {@code RationalVector}'s dimension is
     * chosen from a geometric distribution with mean {@code secondaryScale}, and each coordinate's bit size is chosen
     * from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3 and a positive {@code secondaryScale}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code RationalVector}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalVector> rationalVectors() {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 1) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return map(RationalVector::of, withScale(secondaryScale).lists(rationals()));
    }

    /**
     * An {@code Iterable} that generates all {@code RationalVector}s with a minimum dimension. Each
     * {@code RationalVector}'s dimension is chosen from a geometric distribution with mean {@code secondaryScale}, and
     * each coordinate's bit size is chosen from a geometric distribution with mean approximately {@code scale}. Does
     * not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3 and a {@code secondaryScale} greater than
     *  {@code minDimension}.</li>
     *  <li>{@code minDimension} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code RationalVector}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDimension the minimum dimension of the generated {@code RationalVector}s
     * @return {@code RationalVector}s with dimension at least {@code minDimension}
     */
    @Override
    public @NotNull Iterable<RationalVector> rationalVectorsAtLeast(int minDimension) {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale <= minDimension) {
            throw new IllegalStateException("this must have a secondaryScale greater than minDimension." +
                    " secondaryScale: " + secondaryScale + ", minDimension: " + minDimension);
        }
        return map(RationalVector::of, withScale(secondaryScale).listsAtLeast(minDimension, rationals()));
    }

    /**
     * An {@code Iterable} that generates all {@code PolynomialVector}s. Each {@code PolynomialVector}'s dimension is
     * chosen from a geometric distribution with mean {@code tertiaryScale}, each coordinate's degree is chosen from a
     * geometric distribution with mean {@code secondaryScale}, and each coordinate's coefficient's bit size is chosen
     * from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}, a non-negative {@code secondaryScale}, and a positive
     *  {@code tertiaryScale}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code PolynomialVector}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<PolynomialVector> polynomialVectors() {
        int tertiaryScale = getTertiaryScale();
        if (tertiaryScale < 1) {
            throw new IllegalStateException("this must have a positive tertiaryScale. Invalid tertiaryScale: " +
                    tertiaryScale);
        }
        return map(PolynomialVector::of, withScale(tertiaryScale).lists(polynomials()));
    }

    /**
     * An {@code Iterable} that generates all {@code PolynomialVector}s with a minimum dimension. Each
     * {@code PolynomialVector}'s dimension is chosen from a geometric distribution with mean {@code tertiaryScale},
     * each coordinate's degree is chosen from a geometric distribution with mean {@code secondaryScale}, and each
     * coordinate's coefficient's bit size is chosen from a geometric distribution with mean approximately
     * {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}, a non-negative {@code secondaryScale}, and a
     *  {@code tertiaryScale} greater than {@code minDimension}.</li>
     *  <li>{@code minDimension} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code PolynomialVector}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDimension the minimum dimension of the generated {@code PolynomialVector}s
     * @return {@code PolynomialVector}s with dimension at least {@code minDimension}
     */
    @Override
    public @NotNull Iterable<PolynomialVector> polynomialVectorsAtLeast(int minDimension) {
        int tertiaryScale = getTertiaryScale();
        if (tertiaryScale <= minDimension) {
            throw new IllegalStateException("this must have a tertiaryScale greater than minDimension." +
                    " tertiaryScale: " + tertiaryScale + ", minDimension: " + minDimension);
        }
        return map(PolynomialVector::of, withScale(tertiaryScale).listsAtLeast(minDimension, polynomials()));
    }

    /**
     * An {@code Iterable} that generates all {@code RationalPolynomialVector}s. Each
     * {@code RationalPolynomialVector}'s dimension is chosen from a geometric distribution with mean
     * {@code tertiaryScale}, each coordinate's degree is chosen from a geometric distribution with mean
     * {@code secondaryScale}, and each coordinate's coefficient's bit size is chosen from a geometric distribution
     * with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3, a non-negative {@code secondaryScale}, and a positive
     *  {@code tertiaryScale}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing
     *  {@code RationalPolynomialVector}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalPolynomialVector> rationalPolynomialVectors() {
        int tertiaryScale = getTertiaryScale();
        if (tertiaryScale < 1) {
            throw new IllegalStateException("this must have a positive tertiaryScale. Invalid tertiaryScale: " +
                    tertiaryScale);
        }
        return map(RationalPolynomialVector::of, withScale(tertiaryScale).lists(rationalPolynomials()));
    }

    /**
     * An {@code Iterable} that generates all {@code RationalPolynomialVector}s with a minimum dimension. Each
     * {@code RationalPolynomialVector}'s dimension is chosen from a geometric distribution with mean
     * {@code tertiaryScale}, each coordinate's degree is chosen from a geometric distribution with mean
     * {@code secondaryScale}, and each coordinate's coefficient's bit size is chosen from a geometric distribution
     * with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3, a non-negative {@code secondaryScale}, and a
     *  {@code tertiaryScale} greater than {@code minDimension}.</li>
     *  <li>{@code minDimension} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing
     *  {@code RationalPolynomialVector}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDimension the minimum dimension of the generated {@code RationalPolynomialVector}s
     * @return {@code RationalPolynomialVector}s with dimension at least {@code minDimension}
     */
    @Override
    public @NotNull Iterable<RationalPolynomialVector> rationalPolynomialVectorsAtLeast(int minDimension) {
        int tertiaryScale = getTertiaryScale();
        if (tertiaryScale <= minDimension) {
            throw new IllegalStateException("this must have a tertiaryScale greater than minDimension." +
                    " tertiaryScale: " + tertiaryScale + ", minDimension: " + minDimension);
        }
        return map(
                RationalPolynomialVector::of,
                withScale(tertiaryScale).listsAtLeast(minDimension, rationalPolynomials())
        );
    }

    /**
     * An {@code Iterable} that generates all reduced {@code RationalVector}s (see {@link RationalVector#reduce()})
     * with a given dimension. A larger {@code scale} corresponds to a larger mean coordinate size. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code minDimension} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing reduced {@code RationalVector}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param dimension the dimension of the generated {@code RationalVector}s
     * @return {@code RationalVector}s with dimension {@code dimension}
     */
    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectors(int dimension) {
        return reducedRationalVectors(lists(dimension, bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all reduced {@code RationalVector}s (see {@link RationalVector#reduce()}).
     * A larger {@code scale} corresponds to a larger mean coordinate size, and a larger {@code secondaryScale}
     * corresponds to a larger mean dimension. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and {@code secondaryScale}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing reduced {@code RationalVector}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectors() {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 1) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return reducedRationalVectors(withScale(getSecondaryScale()).lists(bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all reduced {@code RationalVector}s (see {@link RationalVector#reduce()})
     * with a minimum dimension. A larger {@code scale} corresponds to a larger mean coordinate size, and a larger
     * {@code secondaryScale} corresponds to a larger mean dimension. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a {@code secondaryScale} greater than
     *  {@code minDimension}.</li>
     *  <li>{@code minDimension} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing reduced {@code RationalVectors}.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDimension the minimum dimension of the generated {@code RationalVector}s
     * @return {@code RationalVector}s with dimension at least {@code minDimension}
     */
    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectorsAtLeast(int minDimension) {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale <= minDimension) {
            throw new IllegalStateException("this must have a secondaryScale greater than minDimension." +
                    " secondaryScale: " + secondaryScale + ", minDimension: " + minDimension);
        }
        return reducedRationalVectors(withScale(getSecondaryScale()).listsAtLeast(minDimension, bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all {@code Matrix}es with a given height and width. Each element's bit size
     * is chosen from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Matrix}es.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param height the height (number of rows) of the generated {@code Matrix}es
     * @param width the width (number of columns) of the generated {@code Matrix}es
     * @return {@code Matrix}es with height {@code height} and width {@code width}
     */
    @Override
    public @NotNull Iterable<Matrix> matrices(int height, int width) {
        if (height == 0 || width == 0) {
            int scale = getScale();
            if (scale < 1) {
                throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
            }
            return repeat(Matrix.zero(height, width));
        } else {
            return map(Matrix::fromRows, lists(height, vectors(width)));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Matrix}es. Each {@code Matrix}'s element count is chosen from a
     * geometric distribution with mean approximately {@code secondaryScale}, and each coordinate's bit size is chosen
     * from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a {@code secondaryScale} of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Matrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Matrix> matrices() {
        int scale = getScale();
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 2) {
            throw new IllegalStateException("this must have a secondaryScale of at least 2. Invalid secondaryScale: " +
                    secondaryScale);
        }
        QBarRandomProvider dimensionProvider = (QBarRandomProvider) withScale(
                MathUtils.ceilingRoot(2, BigInteger.valueOf(secondaryScale)).intValueExact()
        );
        return chooseLogarithmicOrder(
                map(
                        p -> Matrix.fromRows(p.b),
                        dependentPairsInfiniteSquareRootOrder(
                                pairs(dimensionProvider.positiveIntegersGeometric()),
                                p -> lists(p.a, vectors(p.b))
                        )
                ),
                choose(
                        map(i -> Matrix.zero(0, i), dimensionProvider.naturalIntegersGeometric()),
                        map(i -> Matrix.zero(i, 0), dimensionProvider.positiveIntegersGeometric())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all square {@code Matrix}es. Each {@code Matrix}'s element count is chosen
     * from a geometric distribution with mean approximately {@code secondaryScale}, and each coordinate's bit size is
     * chosen from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 2 and a {@code secondaryScale} of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing square {@code Matrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Matrix> squareMatrices() {
        int scale = getScale();
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 2) {
            throw new IllegalStateException("this must have a secondaryScale of at least 2. Invalid secondaryScale: " +
                    secondaryScale);
        }
        QBarRandomProvider dimensionProvider = (QBarRandomProvider) withScale(
                MathUtils.ceilingRoot(2, BigInteger.valueOf(secondaryScale)).intValueExact()
        );
        return withElement(
                Matrix.zero(0, 0),
                map(p -> p.b, dependentPairsInfiniteLogarithmicOrder(
                        dimensionProvider.positiveIntegersGeometric(),
                        i -> matrices(i, i))
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code RationalMatrix}es with a given height and width. Each coordinate's
     * bit size is chosen from a geometric distribution with mean approximately {@code scale}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3.</li>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code RationalMatrix}es.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param height the height (number of rows) of the generated {@code RationalMatrix}es
     * @param width the width (number of columns) of the generated {@code RationalMatrix}es
     * @return {@code RationalMatrix}es with height {@code height} and width {@code width}
     */
    @Override
    public @NotNull Iterable<RationalMatrix> rationalMatrices(int height, int width) {
        if (height == 0 || width == 0) {
            int scale = getScale();
            if (scale < 3) {
                throw new IllegalStateException("this must have a scale of at least 3. Invalid scale: " + scale);
            }
            return repeat(RationalMatrix.zero(height, width));
        } else {
            return map(RationalMatrix::fromRows, lists(height, rationalVectors(width)));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code RationalMatrix}es. Each {@code RationalMatrix}'s element count is
     * chosen from a geometric distribution with mean approximately {@code secondaryScale}, and each coordinate's bit
     * size is chosen from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3 and a {@code secondaryScale} of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code RationalMatrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalMatrix> rationalMatrices() {
        int scale = getScale();
        if (scale < 3) {
            throw new IllegalStateException("this must have a scale of at least 3. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 2) {
            throw new IllegalStateException("this must have a secondaryScale of at least 2. Invalid secondaryScale: " +
                    secondaryScale);
        }
        QBarRandomProvider dimensionProvider = (QBarRandomProvider) withScale(
                MathUtils.ceilingRoot(2, BigInteger.valueOf(secondaryScale)).intValueExact()
        );
        return chooseLogarithmicOrder(
                map(
                        p -> RationalMatrix.fromRows(p.b),
                        dependentPairsInfiniteSquareRootOrder(
                                pairs(dimensionProvider.positiveIntegersGeometric()),
                                p -> lists(p.a, rationalVectors(p.b))
                        )
                ),
                choose(
                        map(i -> RationalMatrix.zero(0, i), dimensionProvider.naturalIntegersGeometric()),
                        map(i -> RationalMatrix.zero(i, 0), dimensionProvider.positiveIntegersGeometric())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all square {@code RationalMatrix}es. Each {@code RationalMatrix}'s element
     * count is chosen from a geometric distribution with mean approximately {@code secondaryScale}, and each
     * coordinate's bit size is chosen from a geometric distribution with mean approximately {@code scale}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3 and a {@code secondaryScale} of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing square {@code RationalMatrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalMatrix> squareRationalMatrices() {
        int scale = getScale();
        if (scale < 3) {
            throw new IllegalStateException("this must have a scale of at least 3. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 2) {
            throw new IllegalStateException("this must have a secondaryScale of at least 2. Invalid secondaryScale: " +
                    secondaryScale);
        }
        QBarRandomProvider dimensionProvider = (QBarRandomProvider) withScale(
                MathUtils.ceilingRoot(2, BigInteger.valueOf(secondaryScale)).intValueExact()
        );
        return withElement(
                RationalMatrix.zero(0, 0),
                map(p -> p.b, dependentPairsInfiniteLogarithmicOrder(
                        dimensionProvider.positiveIntegersGeometric(),
                        i -> rationalMatrices(i, i))
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code PolynomialMatrix}es with a given height and width. Each
     * element's coefficient's bit size is chosen from a geometric distribution with mean approximately {@code scale},
     * and each element's degree is chosen from a geometric distribution with mean approximately
     * {@code secondaryScale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a non-negative {@code secondaryScale}.</li>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code PolynomialMatrix}es.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param height the height (number of rows) of the generated {@code PolynomialMatrix}es
     * @param width the width (number of columns) of the generated {@code PolynomialMatrix}es
     * @return {@code PolynomialMatrix}es with height {@code height} and width {@code width}
     */
    @Override
    public @NotNull Iterable<PolynomialMatrix> polynomialMatrices(int height, int width) {
        if (height == 0 || width == 0) {
            int scale = getScale();
            if (scale < 1) {
                throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
            }
            int secondaryScale = getSecondaryScale();
            if (secondaryScale < 0) {
                throw new IllegalStateException("this cannot have a negative secondaryScale. Invalid" +
                        " secondaryScale: " + secondaryScale);
            }
            return repeat(PolynomialMatrix.zero(height, width));
        } else {
            return map(PolynomialMatrix::fromRows, lists(height, polynomialVectors(width)));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code PolynomialMatrix}es. Each {@code PolynomialMatrix}'s element's
     * coefficient's bit size is chosen from a geometric distribution with mean approximately {@code scale}, each
     * element's degree is chosen from a geometric distribution with mean approximately {@code secondaryScale}, and
     * the {@code PolynomialMatrix}'s element count is chosen from a geometric distribution with mean approximately
     * {@code tertiaryScale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}, a non-negative {@code secondaryScale}, and a
     *  {@code tertiaryScale} of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code PolynomialMatrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<PolynomialMatrix> polynomialMatrices() {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 0) {
            throw new IllegalStateException("this cannot have a negative secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        int tertiaryScale = getTertiaryScale();
        if (tertiaryScale < 2) {
            throw new IllegalStateException("this must have a tertiaryScale of at least 2. Invalid tertiaryScale: " +
                    tertiaryScale);
        }
        QBarRandomProvider dimensionProvider = (QBarRandomProvider) withScale(
                MathUtils.ceilingRoot(2, BigInteger.valueOf(tertiaryScale)).intValueExact()
        );
        return chooseLogarithmicOrder(
                map(
                        p -> PolynomialMatrix.fromRows(p.b),
                        dependentPairsInfiniteSquareRootOrder(
                                pairs(dimensionProvider.positiveIntegersGeometric()),
                                p -> lists(p.a, polynomialVectors(p.b))
                        )
                ),
                choose(
                        map(i -> PolynomialMatrix.zero(0, i), dimensionProvider.naturalIntegersGeometric()),
                        map(i -> PolynomialMatrix.zero(i, 0), dimensionProvider.positiveIntegersGeometric())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all square {@code PolynomialMatrix}es. Each element's coefficient's bit size
     * is chosen from a geometric distribution with mean approximately {@code scale}, each element's degree is chosen
     * from a geometric distribution with mean approximately {@code secondaryScale}, and each
     * {@code PolynomialMatrix}'s element count is chosen from a geometric distribution with mean approximately
     * {@code tertiaryScale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 2, a non-negative {@code secondaryScale}, and a
     *  {@code tertiaryScale} of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing square
     *  {@code PolynomialMatrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<PolynomialMatrix> squarePolynomialMatrices() {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 0) {
            throw new IllegalStateException("this cannot have a negative secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        int tertiaryScale = getTertiaryScale();
        if (tertiaryScale < 2) {
            throw new IllegalStateException("this must have a tertiaryScale of at least 2. Invalid tertiaryScale: " +
                    tertiaryScale);
        }
        QBarRandomProvider dimensionProvider = (QBarRandomProvider) withScale(
                MathUtils.ceilingRoot(2, BigInteger.valueOf(tertiaryScale)).intValueExact()
        );
        return withElement(
                PolynomialMatrix.zero(0, 0),
                map(p -> p.b, dependentPairsInfiniteLogarithmicOrder(
                        dimensionProvider.positiveIntegersGeometric(),
                        i -> polynomialMatrices(i, i))
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code RationalPolynomialMatrix}es with a given height and width. Each
     * element's coefficient's bit size is chosen from a geometric distribution with mean approximately {@code scale},
     * and each element's degree is chosen from a geometric distribution with mean approximately
     * {@code secondaryScale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3 and a non-negative {@code secondaryScale}.</li>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing
     *  {@code RationalPolynomialMatrix}es.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param height the height (number of rows) of the generated {@code RationalPolynomialMatrix}es
     * @param width the width (number of columns) of the generated {@code RationalPolynomialMatrix}es
     * @return {@code RationalPolynomialMatrix}es with height {@code height} and width {@code width}
     */
    @Override
    public @NotNull Iterable<RationalPolynomialMatrix> rationalPolynomialMatrices(int height, int width) {
        if (height == 0 || width == 0) {
            int scale = getScale();
            if (scale < 3) {
                throw new IllegalStateException("this must have a scale of at least 3. Invalid scale: " + scale);
            }
            int secondaryScale = getSecondaryScale();
            if (secondaryScale < 0) {
                throw new IllegalStateException("this cannot have a negative secondaryScale. Invalid" +
                        " secondaryScale: " + secondaryScale);
            }
            return repeat(RationalPolynomialMatrix.zero(height, width));
        } else {
            return map(RationalPolynomialMatrix::fromRows, lists(height, rationalPolynomialVectors(width)));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code RationalPolynomialMatrix}es. Each
     * {@code RationalPolynomialMatrix}'s element's coefficient's bit size is chosen from a geometric distribution with
     * mean approximately {@code scale}, each element's degree is chosen from a geometric distribution with mean
     * approximately {@code secondaryScale}, and the {@code RationalPolynomialMatrix}'s element count is chosen from a
     * geometric distribution with mean approximately {@code tertiaryScale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3, a non-negative {@code secondaryScale}, and a
     *  {@code tertiaryScale} of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing
     *  {@code RationalPolynomialMatrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalPolynomialMatrix> rationalPolynomialMatrices() {
        int scale = getScale();
        if (scale < 3) {
            throw new IllegalStateException("this must have a scale of at least 3. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 0) {
            throw new IllegalStateException("this cannot have a negative secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        int tertiaryScale = getTertiaryScale();
        if (tertiaryScale < 2) {
            throw new IllegalStateException("this must have a tertiaryScale of at least 2. Invalid tertiaryScale: " +
                    tertiaryScale);
        }
        QBarRandomProvider dimensionProvider = (QBarRandomProvider) withScale(
                MathUtils.ceilingRoot(2, BigInteger.valueOf(tertiaryScale)).intValueExact()
        );
        return chooseLogarithmicOrder(
                map(
                        p -> RationalPolynomialMatrix.fromRows(p.b),
                        dependentPairsInfiniteSquareRootOrder(
                                pairs(dimensionProvider.positiveIntegersGeometric()),
                                p -> lists(p.a, rationalPolynomialVectors(p.b))
                        )
                ),
                choose(
                        map(i -> RationalPolynomialMatrix.zero(0, i), dimensionProvider.naturalIntegersGeometric()),
                        map(i -> RationalPolynomialMatrix.zero(i, 0), dimensionProvider.positiveIntegersGeometric())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all square {@code RationalPolynomialMatrix}es. Each element's coefficient's
     * bit size is chosen from a geometric distribution with mean approximately {@code scale}, each element's degree is
     * chosen from a geometric distribution with mean approximately {@code secondaryScale}, and each
     * {@code RationalPolynomialMatrix}'s element count is chosen from a geometric distribution with mean approximately
     * {@code tertiaryScale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3, a non-negative {@code secondaryScale}, and a
     *  {@code tertiaryScale} of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing square
     *  {@code PolynomialMatrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalPolynomialMatrix> squareRationalPolynomialMatrices() {
        int scale = getScale();
        if (scale < 3) {
            throw new IllegalStateException("this must have a scale of at least 3. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 0) {
            throw new IllegalStateException("this cannot have a negative secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        int tertiaryScale = getTertiaryScale();
        if (tertiaryScale < 2) {
            throw new IllegalStateException("this must have a tertiaryScale of at least 2. Invalid tertiaryScale: " +
                    tertiaryScale);
        }
        QBarRandomProvider dimensionProvider = (QBarRandomProvider) withScale(
                MathUtils.ceilingRoot(2, BigInteger.valueOf(tertiaryScale)).intValueExact()
        );
        return withElement(
                RationalPolynomialMatrix.zero(0, 0),
                map(p -> p.b, dependentPairsInfiniteLogarithmicOrder(
                        dimensionProvider.positiveIntegersGeometric(),
                        i -> rationalPolynomialMatrices(i, i))
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Polynomial}s. Each {@code Polynomial}'s degree is chosen from a
     * geometric distribution with mean {@code secondaryScale}, and each coefficient's bit size is chosen from a
     * geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a non-negative {@code secondaryScale}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Polynomial> polynomials() {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 0) {
            throw new IllegalStateException("this must have a non-negative secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return map(
                js -> Polynomial.of(toList(js)),
                filterInfinite(
                        is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                        withScale(secondaryScale + 1).lists(bigIntegers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Polynomial}s with a minimum degree. Each {@code Polynomial}'s
     * degree is chosen from a geometric distribution with mean {@code secondaryScale}, and each coefficient's bit size
     * is chosen from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a {@code secondaryScale} greater than
     *  {@code minDegree}.</li>
     *  <li>{@code minDegree} must be at least –1.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     * @return {@code Polynomial}s with degree at least {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> polynomialsAtLeast(int minDegree) {
        if (minDegree < -1) {
            throw new IllegalArgumentException("minDegree must be at least -1. Invalid minDegree: " + minDegree);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale <= minDegree) {
            throw new IllegalStateException("this must have a secondaryScale greater than minDegree." +
                    " secondaryScale: " + secondaryScale + ", minDegree: " + minDegree);
        }
        return map(
                js -> Polynomial.of(toList(js)),
                filterInfinite(
                        is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                        withScale(secondaryScale + 1).listsAtLeast(minDegree + 1, bigIntegers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1)
     * with a given degree. Each coefficient's bit size is chosen from a geometric distribution with mean approximately
     * {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code degree} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing primitive {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param degree the minimum degree of the generated {@code Polynomial}s
     * @return primitive {@code Polynomial}s with degree {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomials(int degree) {
        int scale = getScale();
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        if (degree < 0) {
            throw new IllegalArgumentException("degree cannot be negative. Invalid degree: " + degree);
        }
        if (degree == 0) {
            return uniformSample(Arrays.asList(Polynomial.ONE, Polynomial.ONE.negate()));
        }
        return primitivePolynomials(lists(degree + 1, bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1).
     * Each {@code Polynomial}'s degree is chosen from a geometric distribution with mean approximately
     * {@code secondaryScale}, and each coefficient's bit size is chosen from a geometric distribution with mean
     * approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a positive {@code secondaryScale}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing primitive {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomials() {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 1) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return primitivePolynomials(withScale(secondaryScale + 1).listsAtLeast(1, bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1)
     * with a minimum degree. Each {@code Polynomial}'s degree is chosen from a geometric distribution with mean
     * approximately {@code secondaryScale}, and each coefficient's bit size is chosen from a geometric distribution
     * with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}. It must also have a {@code secondaryScale} that is
     *  positive and greater than {@code minDegree}.</li>
     *  <li>{@code minDegree} must be at least –1.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing primitive {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     * @return primitive {@code Polynomial}s with degree at least {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomialsAtLeast(int minDegree) {
        if (minDegree < -1) {
            throw new IllegalArgumentException("minDegree must be at least -1. Invalid minDegree: " + minDegree);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 1) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        if (secondaryScale <= minDegree) {
            throw new IllegalStateException("this must have a secondaryScale greater than the minDegree." +
                    " secondaryScale: " + secondaryScale + ", minDegree: " + minDegree);
        }
        return primitivePolynomials(
                withScale(secondaryScale + 1).listsAtLeast(minDegree == -1 ? 1 : minDegree + 1, bigIntegers())
        );
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1)
     * with a positive leasing coefficient with a given degree. Each coefficient's bit size is chosen from a geometric
     * distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code degree} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing primitive {@code Polynomial}s with
     *  positive leading coefficients.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param degree the minimum degree of the generated {@code Polynomial}s
     * @return primitive {@code Polynomial}s with positive leading coefficients and degree {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> positivePrimitivePolynomials(int degree) {
        int scale = getScale();
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        if (degree < 0) {
            throw new IllegalArgumentException("degree cannot be negative. Invalid degree: " + degree);
        }
        if (degree == 0) {
            return repeat(Polynomial.ONE);
        }
        return filterInfinite(p -> p.signum() == 1, primitivePolynomials(degree));
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1)
     * with a positive leasing coefficient. Each {@code Polynomial}'s degree is chosen from a geometric distribution
     * with mean approximately {@code secondaryScale}, and each coefficient's bit size is chosen from a geometric
     * distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a positive {@code secondaryScale}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing primitive {@code Polynomial}s with
     *  positive leading coefficients.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Polynomial> positivePrimitivePolynomials() {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 1) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return filterInfinite(p -> p.signum() == 1, primitivePolynomials());
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1)
     * with a positive leasing coefficient with a minimum degree. Each {@code Polynomial}'s degree is chosen from a
     * geometric distribution with mean approximately {@code secondaryScale}, and each coefficient's bit size is chosen
     * from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}. It must also have a {@code secondaryScale} that is
     *  positive and greater than {@code minDegree}.</li>
     *  <li>{@code minDegree} must be at least –1.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing primitive {@code Polynomial}s with
     *  positive leading coefficients.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     * @return primitive {@code Polynomial}s with positive leading coefficients and degree at least {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> positivePrimitivePolynomialsAtLeast(int minDegree) {
        if (minDegree < -1) {
            throw new IllegalArgumentException("minDegree must be at least -1. Invalid minDegree: " + minDegree);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 1) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        if (secondaryScale <= minDegree) {
            throw new IllegalStateException("this must have a secondaryScale greater than the minDegree." +
                    " secondaryScale: " + secondaryScale + ", minDegree: " + minDegree);
        }
        return filterInfinite(p -> p.signum() == 1, primitivePolynomialsAtLeast(minDegree));
    }

    /**
     * An {@code Iterable} that generates all monic {@code Polynomial}s with a given degree. Each coefficient's bit
     * size is chosen from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code degree} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing monic {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param degree the degree of the generated {@code Polynomial}s
     * @return monic {@code Polynomial}s with degree at least {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> monicPolynomials(int degree) {
        if (degree < 0) {
            throw new IllegalArgumentException("degree cannot be negative. Invalid degree: " + degree);
        }
        return map(
                is -> {
                    List<BigInteger> coefficients = new ArrayList<>();
                    coefficients.addAll(is);
                    coefficients.add(BigInteger.ONE);
                    return Polynomial.of(coefficients);
                },
                lists(degree, bigIntegers())
        );
    }

    /**
     * An {@code Iterable} that generates all monic {@code Polynomial}s. Each {@code Polynomial}'s degree is chosen
     * from a geometric distribution with mean {@code secondaryScale}, and each coefficient's bit size is chosen from a
     * geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a positive {@code secondaryScale}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing monic {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Polynomial> monicPolynomials() {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale <= 0) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return map(
                is -> {
                    List<BigInteger> coefficients = new ArrayList<>();
                    coefficients.addAll(is);
                    coefficients.add(BigInteger.ONE);
                    return Polynomial.of(coefficients);
                },
                withScale(secondaryScale).lists(bigIntegers())
        );
    }

    /**
     * An {@code Iterable} that generates all monic {@code Polynomial}s with a minimum degree. Each
     * {@code Polynomial}'s degree is chosen from a geometric distribution with mean {@code secondaryScale}, and each
     * coefficient's bit size is chosen from a geometric distribution with mean approximately {@code scale}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a {@code secondaryScale} that is positive and greater
     *  than or equal to {@code minDegree}.</li>
     *  <li>{@code minDegree} must be at least –1.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing monic {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     * @return monic {@code Polynomial}s with degree at least {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> monicPolynomialsAtLeast(int minDegree) {
        if (minDegree < -1) {
            throw new IllegalArgumentException("minDegree must be at least -1. Invalid minDegree: " + minDegree);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 0 || secondaryScale <= minDegree) {
            throw new IllegalStateException("this must have a secondaryScale that is positive and greater than" +
                    " minDegree. secondaryScale: " + secondaryScale + ", minDegree: " + minDegree);
        }
        return map(
                is -> {
                    List<BigInteger> coefficients = new ArrayList<>();
                    coefficients.addAll(is);
                    coefficients.add(BigInteger.ONE);
                    return Polynomial.of(coefficients);
                },
                withScale(secondaryScale).listsAtLeast(minDegree == -1 ? 0 : minDegree, bigIntegers())
        );
    }

    /**
     * An {@code Iterable} that generates all square-free {@code Polynomial}s. Each coefficient's bit size is chosen
     * from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>{@code degree} cannot be negative.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing square-free {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param degree the minimum degree of the generated {@code Polynomial}s
     * @return square-free {@code Polynomial}s with degree {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> squareFreePolynomials(int degree) {
        if (degree < 0) {
            throw new IllegalArgumentException("degree cannot be negative. Invalid degree: " + degree);
        }
        return filter(Polynomial::isSquareFree, polynomials(degree));
    }

    /**
     * An {@code Iterable} that generates all irreducible {@code Polynomial}s. Each {@code Polynomial}'s degree is
     * chosen from a geometric distribution with mean {@code secondaryScale}, and each coefficient's bit size is chosen
     * from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 2 and a positive {@code secondaryScale} of at least
     *  2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing irreducible {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Polynomial> irreduciblePolynomials() {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 2) {
            throw new IllegalStateException("this must have a secondaryScale of at least 2. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return withElement(
                Polynomial.ONE, map(
                        p -> p.b,
                        dependentPairsInfiniteLogarithmicOrder(
                                withScale(secondaryScale).positiveIntegersGeometric(),
                                this::irreduciblePolynomials
                        )
                )
        );
    }

    /**
     * An {@code Iterable} that generates all irreducible {@code Polynomial}s with a minimum degree. Each
     * {@code Polynomial}'s degree is chosen from a geometric distribution with mean approximately
     * {@code secondaryScale}, and each coefficient's bit size is chosen from a geometric distribution with mean
     * approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 2. It must also have a {@code secondaryScale} that is at
     *  least 2 and greater than {@code minDegree}.</li>
     *  <li>{@code minDegree} must be at least –1.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing irreducible {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     * @return irreducible {@code Polynomial}s with degree at least {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> irreduciblePolynomialsAtLeast(int minDegree) {
        if (minDegree < -1) {
            throw new IllegalArgumentException("minDegree must be at least -1. Invalid minDegree: " + minDegree);
        }
        int scale = getScale();
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        if (minDegree < 1) return irreduciblePolynomials();
        return map(
                p -> p.b,
                dependentPairsInfiniteLogarithmicOrder(
                        withScale(getSecondaryScale()).rangeUpGeometric(minDegree),
                        this::irreduciblePolynomials
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code RationalPolynomial}s. Each {@code RationalPolynomial}'s degree is
     * chosen from a geometric distribution with mean {@code secondaryScale}, and each coefficient's bit size is chosen
     * from a geometric distribution with mean approximately {@code scale}. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3 and a non-negative {@code secondaryScale}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code RationalPolynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomials() {
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 0) {
            throw new IllegalStateException("this must have a non-negative secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filterInfinite(
                        is -> is.isEmpty() || last(is) != Rational.ZERO,
                        withScale(secondaryScale + 1).lists(rationals())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code RationalPolynomial}s with a minimum degree. Each
     * {@code RationalPolynomial}'s degree is chosen from a geometric distribution with mean {@code secondaryScale},
     * and each coefficient's bit size is chosen from a geometric distribution with mean approximately {@code scale}.
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3 and a {@code secondaryScale} greater than
     *  {@code minDegree}.</li>
     *  <li>{@code minDegree} must be at least –1.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code RationalPolynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDegree the minimum degree of the generated {@code RationalPolynomial}s
     * @return {@code RationalPolynomial}s with degree at least {@code minDegree}
     */
    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomialsAtLeast(int minDegree) {
        if (minDegree < -1) {
            throw new IllegalArgumentException("minDegree must be at least -1. Invalid minDegree: " + minDegree);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale <= minDegree) {
            throw new IllegalStateException("this must have a secondaryScale greater than minDegree." +
                    " secondaryScale: " + secondaryScale + ", minDegree: " + minDegree);
        }
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filterInfinite(
                        is -> is.isEmpty() || last(is) != Rational.ZERO,
                        withScale(secondaryScale + 1).listsAtLeast(minDegree + 1, rationals())
                )
        );
    }

    /**
     * An {@code Iterator} that generates all {@code MonomialOrder}s from a uniform distribution. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<MonomialOrder> monomialOrders() {
        return uniformSample(MONOMIAL_ORDERS);
    }

    /**
     * An {@code Iterable} that generates all {@code ExponentVector}s. A larger {@code scale} corresponds to an
     * {@code ExponentVector} with more variables and higher exponents on average. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code ExponentVector}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<ExponentVector> exponentVectors() {
        int scale = getScale();
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        QBarRandomProvider variableCountProvider = (QBarRandomProvider) withScale(
                MathUtils.ceilingRoot(2, BigInteger.valueOf(scale)).intValueExact()
        );
        return map(
                js -> ExponentVector.of(toList(js)),
                filterInfinite(
                        is -> is.isEmpty() || last(is) != 0,
                        variableCountProvider.lists(naturalIntegersGeometric())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code MultivariatePolynomial}s. A larger {@code scale} corresponds to a
     * {@code MultivariatePolynomial} with larger coefficients on average, a larger {@code secondaryScale} corresponds
     * to more variables and higher exponents, and a larger {@code tertiaryScale} corresponds to more terms. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 2.</li>
     *  <li>{@code this} must have a positive {@code secondaryScale}.</li>
     *  <li>{@code this} must have a {@code tertiaryScale} of at least 2.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code MultivariatePolynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<MultivariatePolynomial> multivariatePolynomials() {
        return withElement(
                MultivariatePolynomial.ZERO,
                map(
                        p -> MultivariatePolynomial.of(toList(zip(p.a, p.b))),
                        dependentPairsInfinite(
                                withScale(getTertiaryScale())
                                        .subsetsAtLeast(1, withScale(getSecondaryScale()).exponentVectors()),
                                evs -> lists(evs.size(), nonzeroBigIntegers())
                        )
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code MultivariatePolynomial}s containing only (a subset of) the given
     * variables. A larger {@code scale} corresponds to a {@code MultivariatePolynomial} with larger coefficients on
     * average, a larger {@code secondaryScale} corresponds to more variables and higher exponents, and a larger
     * {@code tertiaryScale} corresponds to more terms. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 2.</li>
     *  <li>{@code this} must have a positive {@code secondaryScale}.</li>
     *  <li>{@code this} must have a {@code tertiaryScale} of at least 2.</li>
     *  <li>{@code variables} must be in increasing order and must contain no repetitions.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code MultivariatePolynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<MultivariatePolynomial> multivariatePolynomials(@NotNull List<Variable> variables) {
        int scale = getScale();
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 1) {
            throw new IllegalStateException("this must have a positive secondaryScale. Invalid secondaryScale: " +
                    secondaryScale);
        }
        int tertiaryScale = getTertiaryScale();
        if (tertiaryScale < 2) {
            throw new IllegalStateException("this must have a tertiaryScale of at least 2. Invalid tertiaryScale: " +
                    tertiaryScale);
        }
        if (variables.isEmpty()) {
            return map(MultivariatePolynomial::of, bigIntegers());
        }
        return withElement(
                MultivariatePolynomial.ZERO,
                map(
                        p -> MultivariatePolynomial.of(toList(zip(p.a, p.b))),
                        dependentPairsInfinite(
                                withScale(tertiaryScale)
                                        .subsetsAtLeast(1, withScale(secondaryScale).exponentVectors(variables)),
                                evs -> lists(evs.size(), nonzeroBigIntegers())
                        )
                )
        );
    }

    @Override
    public @NotNull Iterable<Real> reals() {
        int base = 1 << 30;
        return map(
                rp -> Real.fromDigits(
                        BigInteger.valueOf(base),
                        Collections.emptyList(),
                        map(i -> BigInteger.valueOf(i & (base - 1)), rp.integers())
                ),
                randomProvidersDefault()
        );
    }

    /**
     * An {@code Iterable} that generates all positive {@code Algebraic}s. A larger {@code scale} corresponds to an
     * {@code Algebraic} whose minimal polynomial has larger coefficients, and the {@code secondaryScale} is twice the
     * mean of the {@code Algebraic}s' degrees. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a {@code secondaryScale} of at least 4.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing positive {@code Algebraic}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Algebraic> positiveAlgebraics() {
        int scale = getScale();
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 4) {
            throw new IllegalStateException("this must have a secondaryScale of at least 4. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return map(
                p -> p.b,
                dependentPairsInfiniteLogarithmicOrder(
                        withScale(secondaryScale / 2).positiveIntegersGeometric(),
                        this::positiveAlgebraics
                )
        );
    }

    /**
     * An {@code Iterable} that generates all negative {@code Algebraic}s. A larger {@code scale} corresponds to an
     * {@code Algebraic} whose minimal polynomial has larger coefficients, and the {@code secondaryScale} is twice the
     * mean of the {@code Algebraic}s' degrees. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a negative {@code scale} and a {@code secondaryScale} of at least 4.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing negative {@code Algebraic}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Algebraic> negativeAlgebraics() {
        int scale = getScale();
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 4) {
            throw new IllegalStateException("this must have a secondaryScale of at least 4. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return map(
                p -> p.b,
                dependentPairsInfiniteLogarithmicOrder(
                        withScale(secondaryScale / 2).positiveIntegersGeometric(),
                        this::negativeAlgebraics
                )
        );
    }

    /**
     * An {@code Iterable} that generates all nonzero {@code Algebraic}s. A larger {@code scale} corresponds to an
     * {@code Algebraic} whose minimal polynomial has larger coefficients, and the {@code secondaryScale} is twice the
     * mean of the {@code Algebraic}s' degrees. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a {@code secondaryScale} of at least 4.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing nonzero {@code Algebraic}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Algebraic> nonzeroAlgebraics() {
        return filterInfinite(x -> x != Algebraic.ZERO, algebraics());
    }

    /**
     * An {@code Iterable} that generates all {@code Algebraic}s. A larger {@code scale} corresponds to an
     * {@code Algebraic} whose minimal polynomial has larger coefficients, and the {@code secondaryScale} is twice the
     * mean of the {@code Algebraic}s' degrees. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a {@code secondaryScale} of at least 4.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Algebraic}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Algebraic> algebraics() {
        int scale = getScale();
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 4) {
            throw new IllegalStateException("this must have a secondaryScale of at least 4. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return map(
                p -> p.b,
                dependentPairsInfiniteLogarithmicOrder(
                        withScale(secondaryScale / 2).positiveIntegersGeometric(),
                        this::algebraics
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Algebraic}s in the interval [0, 1). A larger {@code scale}
     * corresponds to an {@code Algebraic} whose minimal polynomial has larger coefficients, and the
     * {@code secondaryScale} is twice the mean of the {@code Algebraic}s' degrees. Does not support removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale} and a {@code secondaryScale} of at least 4.</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Algebraic}s in the interval
     *  [0, 1).</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Algebraic> nonNegativeAlgebraicsLessThanOne() {
        int scale = getScale();
        if (scale < 1) {
            throw new IllegalStateException("this must have a positive scale. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 4) {
            throw new IllegalStateException("this must have a secondaryScale of at least 4. Invalid secondaryScale: " +
                    secondaryScale);
        }
        return map(
                p -> p.b,
                dependentPairsInfiniteLogarithmicOrder(
                        withScale(secondaryScale / 2).positiveIntegersGeometric(),
                        this::nonNegativeAlgebraicsLessThanOne
                )
        );
    }

    @Override
    public @NotNull Iterable<Algebraic> rangeUp(int degree, @NotNull Algebraic a) {
        int scale = getScale();
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        if (a.isRational()) {
            Rational r = a.rationalValueExact();
            if (degree == 1) {
                return withElement(a, map(x -> x.add(r), positiveAlgebraics(degree)));
            } else {
                return map(x -> x.add(r), positiveAlgebraics(degree));
            }
        } else {
            BigInteger floor = a.floor();
            Algebraic fractionalPart = a.subtract(floor);
            return map(
                    x -> x.add(floor),
                    filterInfinite(x -> ge(x, fractionalPart), positiveAlgebraics(degree))
            );
        }
    }

    @Override
    public @NotNull Iterable<Algebraic> rangeUp(@NotNull Algebraic a) {
        int scale = getScale();
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        if (a.isRational()) {
            Rational r = a.rationalValueExact();
            return withElement(a, map(x -> x.add(r), positiveAlgebraics()));
        } else {
            BigInteger floor = a.floor();
            Algebraic fractionalPart = a.subtract(floor);
            return map(x -> x.add(floor), filterInfinite(x -> ge(x, fractionalPart), positiveAlgebraics()));
        }
    }

    @Override
    public @NotNull Iterable<Algebraic> rangeDown(int degree, @NotNull Algebraic a) {
        int scale = getScale();
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        if (a.isRational()) {
            Rational r = a.rationalValueExact();
            if (degree == 1) {
                return withElement(a, map(x -> x.add(r), negativeAlgebraics(degree)));
            } else {
                return map(x -> x.add(r), negativeAlgebraics(degree));
            }
        } else {
            BigInteger ceiling = a.ceiling();
            Algebraic fractionalPart = a.subtract(ceiling);
            return map(x -> x.add(ceiling), filterInfinite(x -> le(x, fractionalPart), negativeAlgebraics(degree)));
        }
    }

    public @NotNull Iterable<Algebraic> rangeDown(@NotNull Algebraic a) {
        int scale = getScale();
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        if (a.isRational()) {
            Rational r = a.rationalValueExact();
            return withElement(a, map(x -> x.add(r), negativeAlgebraics()));
        } else {
            BigInteger ceiling = a.ceiling();
            Algebraic fractionalPart = a.subtract(ceiling);
            return map(x -> x.add(ceiling), filterInfinite(x -> le(x, fractionalPart), negativeAlgebraics()));
        }
    }

    @Override
    public @NotNull Iterable<Algebraic> range(int degree, @NotNull Algebraic a, @NotNull Algebraic b) {
        if (gt(a, b)) {
            throw new IllegalArgumentException("a must be less than or equal to b. a: " + a + ", b: " + b);
        }
        int scale = getScale();
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        if (a.equals(b)) {
            if (a.degree() == degree) {
                return Collections.singletonList(a);
            } else {
                throw new IllegalArgumentException("If a and b are equal, degree must be equal to the degree of a." +
                        " degree: " + degree + ", degree of a: " + a.degree());
            }
        }
        boolean aRational = a.isRational();
        boolean bRational = b.isRational();
        Interval extension = Algebraic.intervalExtension(a, b);
        Rational lower = extension.getLower().get();
        Rational upper = extension.getUpper().get();
        Rational extensionDiameter = upper.subtract(lower);
        Iterable<Algebraic> xs = map(
                x -> x.multiply(extensionDiameter).add(lower),
                nonNegativeAlgebraicsLessThanOne(degree)
        );
        if (b.degree() == degree) {
            return filterInfinite(
                    x -> (aRational || ge(x, a)) && (bRational || le(x, b)),
                    withElement(Algebraic.of(upper), xs)
            );
        } else {
            return filterInfinite(x -> (aRational || ge(x, a)) && (bRational || le(x, b)), xs);
        }
    }

    @Override
    public @NotNull Iterable<Algebraic> range(@NotNull Algebraic a, @NotNull Algebraic b) {
        if (gt(a, b)) {
            throw new IllegalArgumentException("a must be greater than or equal to b. a: " + a + ", b: " + b);
        }
        int scale = getScale();
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        int secondaryScale = getSecondaryScale();
        if (secondaryScale < 4) {
            throw new IllegalStateException("this must have a secondary scale of at least 4. Invalid secondary scale: "
                    + scale);
        }
        if (a.equals(b)) {
            return Collections.singletonList(a);
        }
        boolean aRational = a.isRational();
        boolean bRational = b.isRational();
        Interval extension = Algebraic.intervalExtension(a, b);
        Rational lower = extension.getLower().get();
        Rational upper = extension.getUpper().get();
        Rational extensionDiameter = upper.subtract(lower);
        return filterInfinite(
                x -> (aRational || ge(x, a)) && (bRational || le(x, b)),
                withElement(
                        Algebraic.of(upper),
                        map(x -> x.multiply(extensionDiameter).add(lower), nonNegativeAlgebraicsLessThanOne())
                )
        );
    }

    @Override
    public @NotNull Iterable<Algebraic> algebraicsIn(int degree, @NotNull Interval a) {
        int scale = getScale();
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        if (!a.getLower().isPresent() && !a.getUpper().isPresent()) {
            return algebraics(degree);
        } else if (!a.getLower().isPresent()) {
            return rangeDown(degree, Algebraic.of(a.getUpper().get()));
        } else if (!a.getUpper().isPresent()) {
            return rangeUp(degree, Algebraic.of(a.getLower().get()));
        } else {
            return range(degree, Algebraic.of(a.getLower().get()), Algebraic.of(a.getUpper().get()));
        }
    }

    @Override
    public @NotNull Iterable<Algebraic> algebraicsIn(@NotNull Interval a) {
        int scale = getScale();
        if (scale < 2) {
            throw new IllegalStateException("this must have a scale of at least 2. Invalid scale: " + scale);
        }
        if (!a.getLower().isPresent() && !a.getUpper().isPresent()) {
            return algebraics();
        } else if (!a.getLower().isPresent()) {
            return rangeDown(Algebraic.of(a.getUpper().get()));
        } else if (!a.getUpper().isPresent()) {
            return rangeUp(Algebraic.of(a.getLower().get()));
        } else {
            return range(Algebraic.of(a.getLower().get()), Algebraic.of(a.getUpper().get()));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Algebraic}s not contained in a given {@code Interval} and with a
     * given degree. Does not support removal.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     *  <li>{@code a} cannot be (–∞, ∞).</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Algebraic}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a an {@code Interval}
     * @return {r|r∉{@code a}}
     */
    @Override
    public @NotNull Iterable<Algebraic> algebraicsNotIn(int degree, @NotNull Interval a) {
        List<Interval> complement = a.complement();
        switch (complement.size()) {
            case 0:
                throw new IllegalArgumentException("a cannot be (-Infinity, Infinity).");
            case 1:
                Algebraic boundary = Algebraic.of(a.getLower().isPresent() ? a.getLower().get() : a.getUpper().get());
                return filterInfinite(r -> !r.equals(boundary), algebraicsIn(degree, complement.get(0)));
            case 2:
                Algebraic x = Algebraic.of(complement.get(0).getUpper().get());
                Algebraic y = Algebraic.of(complement.get(1).getLower().get());
                //noinspection RedundantCast
                return choose(
                        filterInfinite(r -> !r.equals(x), rangeDown(degree, x)),
                        filterInfinite(r -> !r.equals(y), rangeUp(degree, y))
                );
            default: throw new IllegalStateException("unreachable");
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Algebraic}s not contained in a given {@code Interval}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be (–∞, ∞).</li>
     *  <li>The result is an infinite, non-removable {@code Iterable} containing {@code Algebraic}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param a an {@code Interval}
     * @return {r|r∉{@code a}}
     */
    @Override
    public @NotNull Iterable<Algebraic> algebraicsNotIn(@NotNull Interval a) {
        List<Interval> complement = a.complement();
        switch (complement.size()) {
            case 0:
                throw new IllegalArgumentException("a cannot be (-Infinity, Infinity).");
            case 1:
                Algebraic boundary = Algebraic.of(a.getLower().isPresent() ? a.getLower().get() : a.getUpper().get());
                return filterInfinite(r -> !r.equals(boundary), algebraicsIn(complement.get(0)));
            case 2:
                Algebraic x = Algebraic.of(complement.get(0).getUpper().get());
                Algebraic y = Algebraic.of(complement.get(1).getLower().get());
                //noinspection RedundantCast
                return choose(
                        filterInfinite(r -> !r.equals(x), rangeDown(x)),
                        filterInfinite(r -> !r.equals(y), rangeUp(y))
                );
            default: throw new IllegalStateException("unreachable");
        }
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
