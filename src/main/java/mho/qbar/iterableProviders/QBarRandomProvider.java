package mho.qbar.iterableProviders;

import mho.qbar.objects.RationalVector;
import mho.wheels.iterables.RandomProvider;
import mho.qbar.objects.Interval;
import mho.qbar.objects.Rational;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;

@SuppressWarnings("ConstantConditions")
public class QBarRandomProvider extends RandomProvider implements QBarIterableProvider {
    public QBarRandomProvider() {
        super();
    }

    public QBarRandomProvider(@NotNull Random generator) {
        super(generator);
    }

    public @NotNull Iterable<Rational> rangeUp(int meanBitSize, @NotNull Rational a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> rangeUp(@NotNull Rational a) {
        return rangeUp(BIG_INTEGER_MEAN_BIT_SIZE, a);
    }

    public @NotNull Iterable<Rational> rangeDown(int meanBitSize, @NotNull Rational a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> rangeDown(@NotNull Rational a) {
        return rangeDown(BIG_INTEGER_MEAN_BIT_SIZE, a);
    }

    public @NotNull Iterable<Rational> range(int meanBitSize, @NotNull Rational a, @NotNull Rational b) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b) {
        return range(BIG_INTEGER_MEAN_BIT_SIZE, a, b);
    }

    /**
     * a pseudorandom {@link Iterable} that generates every {@link Rational}. Each {@code Rational}'s bit size (defined
     * as the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution with mean
     * approximately {@code meanBitSize} (The ratio between the actual mean bit size and {@code meanBitSize} decreases
     * as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 5.</li>
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
                p -> Rational.of(p.a, p.b),
                filter(
                        q -> q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(bigIntegers(meanBitSize / 2), positiveBigIntegers(meanBitSize / 2))
                )
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Rational}. Each {@code Rational}'s bit size (defined
     * as the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution with mean
     * approximately 64. Does not support removal.
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
     * a pseudorandom {@code Iterable} that generates every non-negative {@code Rational}. Each {@code Rational}'s bit
     * size (defined as the sum of the numerator and denominator's bit size) is chosen from a geometric distribution
     * with mean approximately {@code meanBitSize} (The ratio between the actual mean bit size and {@code meanBitSize}
     * decreases as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 5.</li>
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
                p -> Rational.of(p.a, p.b),
                filter(
                        q -> q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(naturalBigIntegers(meanBitSize / 2), positiveBigIntegers(meanBitSize / 2))
                )
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every non-negative {@code Rational}. Each {@code Rational}'s bit
     * size (defined as the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution
     * with mean approximately 64. Does not support removal.
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
     * a pseudorandom {@code Iterable} that generates every positive {@code Rational}. Each {@code Rational}'s bit size
     * (defined as the sum of the numerator 'sand denominator's bit sizes) is chosen from a geometric distribution with
     * mean approximately {@code meanBitSize} (The ratio between the actual mean bit size and {@code meanBitSize}
     * decreases as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 5.</li>
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
                p -> Rational.of(p.a, p.b),
                filter(
                        q -> q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(positiveBigIntegers(meanBitSize / 2), positiveBigIntegers(meanBitSize / 2))
                )
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every positive {@code Rational}. Each {@code Rational}'s bit size
     * (defined as the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution with
     * mean approximately 64. Does not support removal.
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
     * a pseudorandom {@code Iterable} that generates every negative {@code Rational}. Each {@code Rational}'s bit size
     * (defined as the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution with
     * mean approximately {@code meanBitSize} (The ratio between the actual mean bit size and {@code meanBitSize}
     * decreases as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 5.</li>
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
                p -> Rational.of(p.a, p.b),
                filter(
                        q -> q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(negativeBigIntegers(meanBitSize / 2), positiveBigIntegers(meanBitSize / 2))
                )
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every negative {@code Rational}. Each {@code Rational}'s bit size
     * (defined as the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution with
     * mean approximately 64. Does not support removal.
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
     * a pseudorandom {@code Iterable} that generates every {@code Rational} in the interval [0, 1). Each
     * {@code Rational}'s bit size (defined as the sum of the numerator's and denominator's bit sizes) is chosen from a
     * geometric distribution with mean approximately {@code meanBitSize} (The ratio between the actual mean bit size
     * and {@code meanBitSize} decreases as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 5.</li>
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
                p -> Rational.of(p.a, p.b),
                filter(
                        q -> lt(q.a, q.b) && q.a.gcd(q.b).equals(BigInteger.ONE),
                        pairs(naturalBigIntegers(meanBitSize / 2), positiveBigIntegers(meanBitSize / 2))
                )
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Rational} in the interval [0, 1). Each
     * {@code Rational}'s bit size (defined as the sum of the numerator's and denominator's bit sizes) is chosen from a
     * geometric distribution with mean approximately 64. Does not support removal.
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
     *
     * @param meanBitSize the approximate mean bit size of the {@code Interval}s' lower and upper bounds' numerators
     *                    and denominators
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Interval> finitelyBoundedIntervals(int meanBitSize) {
        return map(p -> Interval.of(p.a, p.b), filter(p -> le(p.a, p.b), pairs(rationals(meanBitSize / 2))));
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Interval} with finite bounds. Each
     * {@code Interval}'s bit sizes (defined as the sum of the lower bound's and upper bound's bit sizes) is chosen
     * from a geometric distribution with mean approximately 64. Does not support removal.
     *
     * <ul>
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
     *
     * @param meanBitSize the approximate mean bit size of the {@code Interval}s' lower and upper bounds' numerators
     *                    and denominators
     * @return the {@code Iterable} described above.
     */
    public @NotNull Iterable<Interval> intervals(int meanBitSize) {
        return map(
                p -> {
                    if (!p.a.isPresent() && !p.b.isPresent()) return Interval.ALL;
                    if (!p.a.isPresent()) return Interval.lessThanOrEqualTo(p.b.get());
                    if (!p.b.isPresent()) return Interval.greaterThanOrEqualTo(p.a.get());
                    return Interval.of(p.a.get(), p.b.get());
                },
                filter(
                        p -> !p.a.isPresent() || !p.b.isPresent() || le(p.a.get(), p.b.get()),
                        (Iterable<Pair<Optional<Rational>, Optional<Rational>>>)
                                pairs(optionals(rationals(meanBitSize / 2)))
                )
        );  
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Interval}. Each {@code Interval}'s bit size (defined
     * as the sum of the lower bound's and upper bound's bit sizes) is chosen from a geometric distribution with mean
     * approximately 64. Does not support removal.
     *
     * <ul>
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

    @Override
    public @NotNull Iterable<Byte> bytes(@NotNull Interval a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Short> shorts(@NotNull Interval a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Integer> integers(@NotNull Interval a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Long> longs(@NotNull Interval a) {
        return null;
    }

    public @NotNull Iterable<BigInteger> bigIntegers(@NotNull Interval a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Rational> rationals(@NotNull Interval a) {
        if (!a.getLower().isPresent() && !a.getUpper().isPresent()) {
            return rationals();
        } else if (!a.getLower().isPresent()) {
            return map(r -> a.getUpper().get().subtract(r), nonNegativeRationals());
        } else if (!a.getUpper().isPresent()) {
            return map(r -> r.add(a.getLower().get()), nonNegativeRationals());
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
                return new ArrayList<>();
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
        return rationalVectorsBySize(BIG_INTEGER_MEAN_BIT_SIZE, dimension);
    }

    public @NotNull Iterable<RationalVector> rationalVectorsBySize(int elementMeanBitSize, int dimension) {
        return map(RationalVector::of, lists(dimension, rationals(elementMeanBitSize)));
    }

    @Override
    public @NotNull Iterable<RationalVector> rationalVectorsAtLeast(int minDimension) {
        return rationalVectorsBySizeAtLeast(BIG_INTEGER_MEAN_BIT_SIZE, minDimension);
    }

    public @NotNull Iterable<RationalVector> rationalVectorsBySizeAtLeast(int elementMeanBitSize, int minDimension) {
        return map(RationalVector::of, listsAtLeast(minDimension, rationals(elementMeanBitSize)));
    }

    @Override
    public @NotNull Iterable<RationalVector> rationalVectors() {
        return rationalVectorsBySize(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    public @NotNull Iterable<RationalVector> rationalVectorsBySize(int elementMeanBitSize) {
        return map(RationalVector::of, lists(rationals(elementMeanBitSize)));
    }

    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectors(int dimension) {
        return reducedRationalVectorsBySize(BIG_INTEGER_MEAN_BIT_SIZE, dimension);
    }

    public @NotNull Iterable<RationalVector> reducedRationalVectorsBySize(int elementMeanBitSize, int dimension) {
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
                                            BigInteger gcd = foldl(p -> p.a.gcd(p.b), BigInteger.ZERO, js);
                                            return gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE);
                                        },
                                        lists(dimension, bigIntegers(elementMeanBitSize / 2))
                                )
                        )
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectorsAtLeast(int minDimension) {
        return reducedRationalVectorsBySizeAtLeast(BIG_INTEGER_MEAN_BIT_SIZE, minDimension);
    }

    public @NotNull Iterable<RationalVector> reducedRationalVectorsBySizeAtLeast(
            int elementMeanBitSize,
            int minDimension
    ) {
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
                                            BigInteger gcd = foldl(p -> p.a.gcd(p.b), BigInteger.ZERO, js);
                                            return gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE);
                                        },
                                        listsAtLeast(minDimension, bigIntegers(elementMeanBitSize / 2))
                                )
                        )
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectors() {
        return reducedRationalVectorsBySize(BIG_INTEGER_MEAN_BIT_SIZE);
    }

    public @NotNull Iterable<RationalVector> reducedRationalVectorsBySize(int elementMeanBitSize) {
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
                                            BigInteger gcd = foldl(p -> p.a.gcd(p.b), BigInteger.ZERO, js);
                                            return gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE);
                                        },
                                        lists(bigIntegers(elementMeanBitSize / 2))
                                )
                        )
                )
        );
    }
}
