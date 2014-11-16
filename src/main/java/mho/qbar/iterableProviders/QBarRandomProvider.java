package mho.qbar.iterableProviders;

import mho.wheels.iterables.RandomProvider;
import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.ordering.Ordering.lt;

public class QBarRandomProvider extends RandomProvider implements QBarIterableProvider {
    public QBarRandomProvider() {
        super();
    }

    public QBarRandomProvider(@NotNull Random generator) {
        super(generator);
    }

    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> rangeBy(@NotNull Rational a, @NotNull Rational i) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> rangeBy(@NotNull Rational a, @NotNull Rational i, @NotNull Rational b) {
        return null;
    }

    /**
     * a pseudorandom {@link Iterable} that generates every {@link Rational}. The numerator's and denominator's bit
     * size is chosen from a geometric distribution with mean approximately {@code meanBitSize} (The ratio between the
     * actual mean bit size and {@code meanBitSize} decreases as {@code meanBitSize} increases). Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the {@code Rational}s' numerators and denominators
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Rational> rationals(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        q -> {
                            assert q.a != null;
                            assert q.b != null;
                            return q.a.gcd(q.b).equals(BigInteger.ONE);
                        },
                        pairs(bigIntegers(meanBitSize), positiveBigIntegers(meanBitSize))
                )
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Rational}. The numerator's and denominator's bit
     * size is chosen from a geometric distribution with mean approximately 64. Does not support removal.
     *
     * <ul>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @return the {@code Iterable} described above.
     */
    @Override
    public @NotNull Iterable<Rational> rationals() {
        return rationals(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * a pseudorandom {@code Iterable} that generates every non-negative {@code Rational}. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately {@code meanBitSize} (The
     * ratio between the actual mean bit size and {@code meanBitSize} decreases as {@code meanBitSize} increases). Does
     * not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the {@code Rational}s' numerators and denominators
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Rational> nonNegativeRationals(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        q -> {
                            assert q.a != null;
                            assert q.b != null;
                            return q.a.gcd(q.b).equals(BigInteger.ONE);
                        },
                        pairs(naturalBigIntegers(meanBitSize), positiveBigIntegers(meanBitSize))
                )
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every non-negative {@code Rational}. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately 64. Does not support
     * removal.
     *
     * <ul>
     *  <li>The result is an infinite pseudorandom sequence of all non-negative {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Rational> nonNegativeRationals() {
        return nonNegativeRationals(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * a pseudorandom {@code Iterable} that generates every positive {@code Rational}. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately {@code meanBitSize} (The
     * ratio between the actual mean bit size and {@code meanBitSize} decreases as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all positive {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the {@code Rational}s' numerators and denominators
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Rational> positiveRationals(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        q -> {
                            assert q.a != null;
                            assert q.b != null;
                            return q.a.gcd(q.b).equals(BigInteger.ONE);
                        },
                        pairs(positiveBigIntegers(meanBitSize), positiveBigIntegers(meanBitSize))
                )
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every positive {@code Rational}. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately 64. Does not support
     * removal.
     *
     * <ul>
     *  <li>The result is an infinite pseudorandom sequence of all positive {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Rational> positiveRationals() {
        return positiveRationals(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * a pseudorandom {@code Iterable} that generates every negative {@code Rational}. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately {@code meanBitSize} (The
     * ratio between the actual mean bit size and {@code meanBitSize} decreases as {@code meanBitSize} increases).
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all negative {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the {@code Rational}s' numerators and denominators
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Rational> negativeRationals(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        q -> q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(negativeBigIntegers(meanBitSize), positiveBigIntegers(meanBitSize))
                )
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every negative {@code Rational}. The numerator's and
     * denominator's bit size is chosen from a geometric distribution with mean approximately 64. Does not support
     * removal.
     *
     * <ul>
     *  <li>The result is an infinite pseudorandom sequence of all negative {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Rational> negativeRationals() {
        return negativeRationals(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Rational} in the interval [0, 1). The numerator's
     * and denominator's bit size is chosen from a geometric distribution with mean approximately {@code meanBitSize}
     * (The ratio between the actual mean bit size and {@code meanBitSize} decreases as {@code meanBitSize} increases).
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Rational}s in the interval [0, 1).</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the {@code Rational}s' numerators and denominators
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Rational.of(p.a, p.b);
                },
                filter(
                        q -> {
                            assert q.a != null;
                            assert q.b != null;
                            return lt(q.a, q.b) && q.a.gcd(q.b).equals(BigInteger.ONE);
                        },
                        pairs(naturalBigIntegers(meanBitSize), positiveBigIntegers(meanBitSize))
                )
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Rational} in the interval [0, 1). The numerator's
     * and denominator's bit size is chosen from a geometric distribution with mean approximately 64. Does not support
     * removal.
     *
     * <ul>
     *  <li>The result is an infinite pseudorandom sequence of {@code Rational}s in the interval [0, 1).</li>
     * </ul>
     *
     * Length is infinite
     *
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne() {
        return nonNegativeRationalsLessThanOne(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Interval} with finite bounds. The lower and upper
     * bounds' numerators' and denominators' bit size is chosen from a geometric distribution with mean approximately
     * {@code meanBitSize} (The ratio between the actual mean bit size and {@code meanBitSize} decreases as
     * {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Interval}s with finite bounds.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the {@code Interval}s' lower and upper bounds' numerators
     *                    and denominators
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Interval> finitelyBoundedIntervals(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return Interval.of(p.a, p.b);
                },
                filter(
                        p -> {
                            assert p.a != null;
                            assert p.b != null;
                            return le(p.a, p.b);
                        },
                        pairs(rationals(meanBitSize))
                )
        );   
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Interval} with finite bounds. The lower and upper
     * bounds' numerators' and denominators' bit size is chosen from a geometric distribution with mean approximately
     * 64. Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Interval}s with finite bounds.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @return the {@code Iterable} described above.
     */
    @Override
    public @NotNull Iterable<Interval> finitelyBoundedIntervals() {
        return finitelyBoundedIntervals(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Interval}. The lower and upper bounds' numerators'
     * and denominators' bit size is chosen from a geometric distribution with mean approximately {@code meanBitSize}
     * (The ratio between the actual mean bit size and {@code meanBitSize} decreases as {@code meanBitSize} increases).
     * Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Interval}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param meanBitSize the approximate mean bit size of the {@code Interval}s' lower and upper bounds' numerators
     *                    and denominators
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Interval> intervals(int meanBitSize) {
        return map(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    if (!p.a.isPresent() && !p.b.isPresent()) return Interval.ALL;
                    if (!p.a.isPresent()) return Interval.lessThanOrEqualTo(p.b.get());
                    if (!p.b.isPresent()) return Interval.greaterThanOrEqualTo(p.a.get());
                    return Interval.of(p.a.get(), p.b.get());
                },
                filter(
                        p -> {
                            assert p.a != null;
                            assert p.b != null;
                            return !p.a.isPresent() || !p.b.isPresent() || le(p.a.get(), p.b.get());
                        },
                        (Iterable<Pair<Optional<Rational>, Optional<Rational>>>)
                                pairs(optionals(rationals(meanBitSize)))
                )
        );  
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Interval}. The lower and upper bounds' numerators'
     * and denominators' bit size is chosen from a geometric distribution with mean approximately 64. Does not support
     * removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 2.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Interval}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @return the {@code Iterable} described above.
     */
    @Override
    public @NotNull Iterable<Interval> intervals() {
        return intervals(BIG_INTEGER_MEAN_BIT_SIZE);
    }
}
