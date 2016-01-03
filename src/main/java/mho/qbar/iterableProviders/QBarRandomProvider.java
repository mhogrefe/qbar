package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.List;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.le;
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
        return new QBarRandomProvider((RandomProvider) wheelsProvider.withSecondaryScale(secondaryScale));
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
     * An {@code Iterable} that generates all reduced {@code RationalVector}s (see {@link RationalVector#reduce()})
     * with a given dimension. A larger {@code scale} corresponds to a larger mean coordinate size. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} must have a positive {@code scale}.</li>
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
     * An {@code Iterable} that generates all {@code RationalMatrix}es with a given height and width. Each coordinate's
     * bit size is chosen from a geometric distribution with mean approximately {@code scale}. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code this} must have a {@code scale} of at least 3.</li>
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
     * An {@code Iterable} that generates all {@code RationalMatrix}es with a given height and width. Each
     * {@code RationalMatrix}'s element count is chosen from a geometric distribution with mean approximately
     * {@code secondaryScale}, and each coordinate's bit size is chosen from a geometric distribution with mean
     * approximately {@code scale}. Does not support removal.
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
                MathUtils.ceilingRoot(IntegerUtils.TWO, BigInteger.valueOf(secondaryScale)).intValueExact()
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
