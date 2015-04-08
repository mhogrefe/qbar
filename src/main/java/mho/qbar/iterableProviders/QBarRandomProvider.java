package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.ordering.Ordering.lt;

@SuppressWarnings("ConstantConditions")
public class QBarRandomProvider extends QBarIterableProvider {
    private static final int DEFAULT_RATIONAL_MEAN_BIT_SIZE = 64;
    private static final int DEFAULT_INTERVAL_MEAN_BIT_SIZE = 64;
    private @NotNull RandomProvider RP;

    protected final long seed;

    private int rationalMeanBitSize = DEFAULT_RATIONAL_MEAN_BIT_SIZE;
    private int intervalMeanBitSize = DEFAULT_INTERVAL_MEAN_BIT_SIZE;

    public QBarRandomProvider() {
        seed = new Random().nextLong();
        RP = new RandomProvider(seed);
    }

    public QBarRandomProvider(long seed) {
        RP = new RandomProvider(seed);
        this.seed = seed;
    }

    public int getRationalMeanBitSize() {
        return rationalMeanBitSize;
    }

    public int getIntervalMeanBitSize() {
        return intervalMeanBitSize;
    }

    public QBarRandomProvider copy() {
        QBarRandomProvider copy = new QBarRandomProvider(seed);
        copy.RP = RP;
        copy.rationalMeanBitSize = rationalMeanBitSize;
        copy.intervalMeanBitSize = intervalMeanBitSize;
        return copy;
    }

    public QBarRandomProvider alt() {
        long newSeed = new Random(seed).nextLong();
        QBarRandomProvider copy = new QBarRandomProvider(newSeed);
        copy.RP = RP;
        copy.rationalMeanBitSize = rationalMeanBitSize;
        copy.intervalMeanBitSize = intervalMeanBitSize;
        return copy;
    }

    public QBarRandomProvider withBigIntegerMeanBitSize(int bigIntegerMeanBitSize) {
        QBarRandomProvider newRandomProvider = copy();
        newRandomProvider.RP = RP.withBigIntegerMeanBitSize(bigIntegerMeanBitSize);
        return newRandomProvider;
    }

    public QBarRandomProvider withBigDecimalMeanScale(int bigDecimalMeanScale) {
        QBarRandomProvider newRandomProvider = copy();
        newRandomProvider.RP = RP.withBigDecimalMeanScale(bigDecimalMeanScale);
        return newRandomProvider;
    }

    public QBarRandomProvider withMeanListSize(int meanListSize) {
        QBarRandomProvider newRandomProvider = copy();
        newRandomProvider.RP = RP.withMeanListSize(meanListSize);
        return newRandomProvider;
    }

    public QBarRandomProvider withSpecialElementRatio(int specialElementRatio) {
        QBarRandomProvider newRandomProvider = copy();
        newRandomProvider.RP = RP.withSpecialElementRatio(specialElementRatio);
        return newRandomProvider;
    }

    public QBarRandomProvider withRationalMeanBitSize(int bigIntegerMeanBitSize) {
        QBarRandomProvider newRandomProvider = copy();
        newRandomProvider.rationalMeanBitSize = bigIntegerMeanBitSize;
        return newRandomProvider;
    }

    public QBarRandomProvider withIntervalMeanBitSize(int intervalMeanBitSize) {
        QBarRandomProvider newRandomProvider = copy();
        newRandomProvider.intervalMeanBitSize = intervalMeanBitSize;
        return newRandomProvider;
    }

    @Override
    public @NotNull Iterable<Boolean> booleans() {
        return RP.booleans();
    }

    @Override
    public @NotNull Iterable<Ordering> orderings() {
        return RP.orderings();
    }

    @Override
    public @NotNull Iterable<RoundingMode> roundingModes() {
        return RP.roundingModes();
    }

    @Override
    public @NotNull Iterable<Byte> rangeUp(byte a) {
        return RP.rangeUp(a);
    }

    @Override
    public @NotNull Iterable<Short> rangeUp(short a) {
        return RP.rangeUp(a);
    }

    @Override
    public @NotNull Iterable<Integer> rangeUp(int a) {
        return RP.rangeUp(a);
    }

    @Override
    public @NotNull Iterable<Long> rangeUp(long a) {
        return RP.rangeUp(a);
    }

    @Override
    public @NotNull Iterable<BigInteger> rangeUp(@NotNull BigInteger a) {
        return RP.rangeUp(a);
    }

    @Override
    public @NotNull Iterable<Character> rangeUp(char a) {
        return RP.rangeUp(a);
    }

    @Override
    public @NotNull Iterable<Byte> rangeDown(byte a) {
        return RP.rangeDown(a);
    }

    @Override
    public @NotNull Iterable<Short> rangeDown(short a) {
        return RP.rangeDown(a);
    }

    @Override
    public @NotNull Iterable<Integer> rangeDown(int a) {
        return RP.rangeDown(a);
    }

    @Override
    public @NotNull Iterable<Long> rangeDown(long a) {
        return RP.rangeDown(a);
    }

    @Override
    public @NotNull Iterable<BigInteger> rangeDown(@NotNull BigInteger a) {
        return RP.rangeDown(a);
    }

    @Override
    public @NotNull Iterable<Character> rangeDown(char a) {
        return RP.rangeDown(a);
    }

    @Override
    public @NotNull Iterable<Byte> range(byte a, byte b) {
        return RP.range(a, b);
    }

    @Override
    public @NotNull Iterable<Short> range(short a, short b) {
        return RP.range(a, b);
    }

    @Override
    public @NotNull Iterable<Integer> range(int a, int b) {
        return RP.range(a, b);
    }

    @Override
    public @NotNull Iterable<Long> range(long a, long b) {
        return RP.range(a, b);
    }

    @Override
    public @NotNull Iterable<BigInteger> range(@NotNull BigInteger a, @NotNull BigInteger b) {
        return RP.range(a, b);
    }

    @Override
    public @NotNull Iterable<Character> range(char a, char b) {
        return RP.range(a, b);
    }

    @Override
    public @NotNull <T> Iterable<T> uniformSample(@NotNull List<T> xs) {
        return RP.uniformSample(xs);
    }

    @Override
    public @NotNull Iterable<Character> uniformSample(@NotNull String s) {
        return RP.uniformSample(s);
    }

    @Override
    public @NotNull Iterable<Byte> positiveBytes() {
        return RP.positiveBytes();
    }

    @Override
    public @NotNull Iterable<Short> positiveShorts() {
        return RP.positiveShorts();
    }

    @Override
    public @NotNull Iterable<Integer> positiveIntegers() {
        return RP.positiveIntegers();
    }

    @Override
    public @NotNull Iterable<Long> positiveLongs() {
        return RP.positiveLongs();
    }

    @Override
    public @NotNull Iterable<BigInteger> positiveBigIntegers() {
        return RP.positiveBigIntegers();
    }

    @Override
    public @NotNull Iterable<Byte> negativeBytes() {
        return RP.negativeBytes();
    }

    @Override
    public @NotNull Iterable<Short> negativeShorts() {
        return RP.negativeShorts();
    }

    @Override
    public @NotNull Iterable<Integer> negativeIntegers() {
        return RP.negativeIntegers();
    }

    @Override
    public @NotNull Iterable<Long> negativeLongs() {
        return RP.negativeLongs();
    }

    @Override
    public @NotNull Iterable<BigInteger> negativeBigIntegers() {
        return RP.negativeBigIntegers();
    }

    @Override
    public @NotNull Iterable<Byte> naturalBytes() {
        return RP.naturalBytes();
    }

    @Override
    public @NotNull Iterable<Short> naturalShorts() {
        return RP.naturalShorts();
    }

    @Override
    public @NotNull Iterable<Integer> naturalIntegers() {
        return RP.naturalIntegers();
    }

    @Override
    public @NotNull Iterable<Long> naturalLongs() {
        return RP.naturalLongs();
    }

    @Override
    public @NotNull Iterable<BigInteger> naturalBigIntegers() {
        return RP.naturalBigIntegers();
    }

    @Override
    public @NotNull Iterable<Byte> bytes() {
        return RP.bytes();
    }

    @Override
    public @NotNull Iterable<Short> shorts() {
        return RP.shorts();
    }

    @Override
    public @NotNull Iterable<Integer> integers() {
        return RP.integers();
    }

    @Override
    public @NotNull Iterable<Long> longs() {
        return RP.longs();
    }

    @Override
    public @NotNull Iterable<BigInteger> bigIntegers() {
        return RP.bigIntegers();
    }

    public @NotNull Iterable<Integer> naturalIntegersGeometric(int mean) {
        return RP.naturalIntegersGeometric(mean);
    }

    public @NotNull Iterable<Integer> positiveIntegersGeometric(int mean) {
        return RP.positiveIntegersGeometric(mean);
    }

    public @NotNull Iterable<Integer> negativeIntegersGeometric(int mean) {
        return RP.negativeIntegersGeometric(mean);
    }

    public @NotNull Iterable<Integer> nonzeroIntegersGeometric(int mean) {
        return RP.nonzeroIntegersGeometric(mean);
    }

    public @NotNull Iterable<Integer> integersGeometric(int mean) {
        return RP.integersGeometric(mean);
    }

    @Override
    public @NotNull Iterable<Character> asciiCharacters() {
        return RP.asciiCharacters();
    }

    @Override
    public @NotNull Iterable<Character> characters() {
        return RP.characters();
    }

    @Override
    public @NotNull Iterable<Float> positiveOrdinaryFloats() {
        return RP.positiveOrdinaryFloats();
    }

    @Override
    public @NotNull Iterable<Float> negativeOrdinaryFloats() {
        return RP.negativeOrdinaryFloats();
    }

    @Override
    public @NotNull Iterable<Float> ordinaryFloats() {
        return RP.ordinaryFloats();
    }

    @Override
    public @NotNull Iterable<Float> floats() {
        return RP.floats();
    }

    @Override
    public @NotNull Iterable<Double> positiveOrdinaryDoubles() {
        return RP.positiveOrdinaryDoubles();
    }

    @Override
    public @NotNull Iterable<Double> negativeOrdinaryDoubles() {
        return RP.negativeOrdinaryDoubles();
    }

    @Override
    public @NotNull Iterable<Double> ordinaryDoubles() {
        return RP.ordinaryDoubles();
    }

    @Override
    public @NotNull Iterable<Double> doubles() {
        return RP.doubles();
    }

    @Override
    public @NotNull Iterable<BigDecimal> positiveBigDecimals() {
        return RP.positiveBigDecimals();
    }

    @Override
    public @NotNull Iterable<BigDecimal> negativeBigDecimals() {
        return RP.negativeBigDecimals();
    }

    @Override
    public @NotNull Iterable<BigDecimal> bigDecimals() {
        return RP.bigDecimals();
    }

    @Override
    public @NotNull <T> Iterable<T> withNull(@NotNull Iterable<T> xs) {
        return RP.withNull(xs);
    }

    @Override
    public @NotNull <T> Iterable<Optional<T>> optionals(@NotNull Iterable<T> xs) {
        return RP.optionals(xs);
    }

    @Override
    public @NotNull <T> Iterable<NullableOptional<T>> nullableOptionals(@NotNull Iterable<T> xs) {
        return RP.nullableOptionals(xs);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairs(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return RP.dependentPairs(xs, f);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsLogarithmic(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return RP.dependentPairsLogarithmic(xs, f);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsSquareRoot(@NotNull Iterable<A> xs, @NotNull Function<A, Iterable<B>> f) {
        return RP.dependentPairsSquareRoot(
                xs,
                f
        );
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsExponential(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return RP.dependentPairsExponential(xs, f);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsSquare(@NotNull Iterable<A> xs, @NotNull Function<A, Iterable<B>> f) {
        return RP.dependentPairsSquare(xs, f);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> pairs(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        return RP.pairs(as, bs);
    }

    @Override
    public @NotNull <A, B, C> Iterable<Triple<A, B, C>> triples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs
    ) {
        return RP.triples(as, bs, cs);
    }

    @Override
    public @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds
    ) {
        return RP.quadruples(as, bs, cs, ds);
    }

    @Override
    public @NotNull <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> quintuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es
    ) {
        return RP.quintuples(as, bs, cs, ds, es);
    }

    @Override
    public @NotNull <A, B, C, D, E, F> Iterable<Sextuple<A, B, C, D, E, F>> sextuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es,
            @NotNull Iterable<F> fs
    ) {
        return RP.sextuples(as, bs, cs, ds, es, fs);
    }

    @Override
    public @NotNull <A, B, C, D, E, F, G> Iterable<Septuple<A, B, C, D, E, F, G>> septuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es,
            @NotNull Iterable<F> fs,
            @NotNull Iterable<G> gs
    ) {
        return RP.septuples(as, bs, cs, ds, es, fs, gs);
    }

    @Override
    public @NotNull <T> Iterable<Pair<T, T>> pairs(@NotNull Iterable<T> xs) {
        return RP.pairs(xs);
    }

    @Override
    public @NotNull <T> Iterable<Triple<T, T, T>> triples(@NotNull Iterable<T> xs) {
        return RP.triples(xs);
    }

    @Override
    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> quadruples(@NotNull Iterable<T> xs) {
        return RP.quadruples(xs);
    }

    @Override
    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> quintuples(@NotNull Iterable<T> xs) {
        return RP.quintuples(xs);
    }

    @Override
    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> sextuples(@NotNull Iterable<T> xs) {
        return RP.sextuples(xs);
    }

    @Override
    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> septuples(@NotNull Iterable<T> xs) {
        return RP.septuples(xs);
    }

    @Override
    public @NotNull <T> Iterable<List<T>> lists(int size, @NotNull Iterable<T> xs) {
        return RP.lists(size, xs);
    }

    @Override
    public @NotNull <T> Iterable<List<T>> listsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return RP.listsAtLeast(minSize, xs);
    }

    @Override
    public @NotNull <T> Iterable<List<T>> lists(@NotNull Iterable<T> xs) {
        return RP.lists(xs);
    }

    @Override
    public @NotNull Iterable<String> strings(int size, @NotNull Iterable<Character> cs) {
        return RP.strings(size, cs);
    }

    @Override
    public @NotNull Iterable<String> stringsAtLeast(int minSize, @NotNull Iterable<Character> cs) {
        return RP.stringsAtLeast(minSize, cs);
    }

    @Override
    public @NotNull Iterable<String> strings(int size) {
        return RP.strings(size);
    }

    @Override
    public @NotNull Iterable<String> stringsAtLeast(int size) {
        return RP.stringsAtLeast(size);
    }

    @Override
    public @NotNull Iterable<String> strings(@NotNull Iterable<Character> cs) {
        return RP.strings(cs);
    }

    @Override
    public @NotNull Iterable<String> strings() {
        return RP.strings();
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
     * @return the {@code Iterable} described above.
     */
    @Override
    public @NotNull Iterable<Rational> rationals() {
        Iterable<BigInteger> numerators = withBigIntegerMeanBitSize(rationalMeanBitSize / 2).bigIntegers();
        Iterable<BigInteger> denominators = withBigIntegerMeanBitSize(rationalMeanBitSize / 2).positiveBigIntegers();
        return map(
                p -> Rational.of(p.a, p.b),
                filter(q -> q.a.gcd(q.b).equals(BigInteger.ONE), pairs(numerators, denominators))
        );
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
     * @return the {@code Iterable} described above.
     */
    @Override
    public @NotNull Iterable<Rational> nonNegativeRationals() {
        Iterable<BigInteger> numerators = withBigIntegerMeanBitSize(rationalMeanBitSize / 2).naturalBigIntegers();
        Iterable<BigInteger> denominators = withBigIntegerMeanBitSize(rationalMeanBitSize / 2).positiveBigIntegers();
        return map(
                p -> Rational.of(p.a, p.b),
                filter(q -> q.a.gcd(q.b).equals(BigInteger.ONE), pairs(numerators, denominators))
        );
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
     * @return the {@code Iterable} described above.
     */
    @Override
    public @NotNull Iterable<Rational> positiveRationals() {
        Iterable<BigInteger> components = withBigIntegerMeanBitSize(rationalMeanBitSize / 2).positiveBigIntegers();
        return map(
                p -> Rational.of(p.a, p.b),
                filter(q -> q.a.gcd(q.b).equals(BigInteger.ONE), pairs(components))
        );
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
     * @return the {@code Iterable} described above.
     */
    @Override
    public @NotNull Iterable<Rational> negativeRationals() {
        Iterable<BigInteger> numerators = withBigIntegerMeanBitSize(rationalMeanBitSize / 2).negativeBigIntegers();
        Iterable<BigInteger> denominators = withBigIntegerMeanBitSize(rationalMeanBitSize / 2).positiveBigIntegers();
        return map(
                p -> Rational.of(p.a, p.b),
                filter(q -> q.a.gcd(q.b).equals(BigInteger.ONE), pairs(numerators, denominators))
        );
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
     * @return the {@code Iterable} described above.
     */
    @Override
    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne() {
        Iterable<BigInteger> numerators = withBigIntegerMeanBitSize(rationalMeanBitSize / 2).naturalBigIntegers();
        Iterable<BigInteger> denominators = withBigIntegerMeanBitSize(rationalMeanBitSize / 2).positiveBigIntegers();
        return map(
                p -> Rational.of(p.a, p.b),
                filter(q -> lt(q.a, q.b) && q.a.gcd(q.b).equals(BigInteger.ONE), pairs(numerators, denominators))
        );
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
     * @return the {@code Iterable} described above.
     */
    @Override
    public @NotNull Iterable<Interval> finitelyBoundedIntervals() {
        Iterable<Rational> bounds = withRationalMeanBitSize(intervalMeanBitSize / 2).rationals();
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
     *
     * @return the {@code Iterable} described above.
     */
    @Override
    public @NotNull Iterable<Interval> intervals() {
        Iterable<Rational> bounds = withRationalMeanBitSize(intervalMeanBitSize / 2).rationals();
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
        return map(RationalVector::of, lists(dimension, rationals()));
    }

    @Override
    public @NotNull Iterable<RationalVector> rationalVectorsAtLeast(int minDimension) {
        return map(RationalVector::of, listsAtLeast(minDimension, rationals()));
    }

    @Override
    public @NotNull Iterable<RationalVector> rationalVectors() {
        return map(RationalVector::of, lists(rationals()));
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
                                        lists(dimension, bigIntegers())
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
                                        listsAtLeast(minDimension, bigIntegers())
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
                                        lists(bigIntegers())
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
        return map(
                q -> q.b,
                dependentPairsSquare(
                        pairs(naturalIntegersGeometric(5)),
                        p -> rationalMatrices(p.a, p.b)
                )
        );
    }

    @Override
    public @NotNull Iterable<Polynomial> polynomials(int degree) {
        return map(
                js -> Polynomial.of(toList(js)),
                filter(is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO), lists(degree + 1, bigIntegers()))
        );
    }

    @Override
    public @NotNull Iterable<Polynomial> polynomialsAtLeast(int minDegree) {
        return map(
                js -> Polynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                        listsAtLeast(minDegree + 1, bigIntegers())
                )
        );
    }

    @Override
    public @NotNull Iterable<Polynomial> polynomials() {
        return map(
                js -> Polynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                        lists(bigIntegers())
                )
        );
    }

    @NotNull
    @Override
    public Iterable<Polynomial> primitivePolynomials(int degree) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Polynomial> primitivePolynomialsAtLeast(int minDegree) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Polynomial> primitivePolynomials() {
        return null;
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomials(int degree) {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(is -> is.isEmpty() || last(is) != Rational.ZERO, lists(degree + 1, rationals()))
        );
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomialsAtLeast(int minDegree) {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(is -> is.isEmpty() || last(is) != Rational.ZERO, listsAtLeast(minDegree + 1, rationals()))
        );
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomials() {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || last(is) != Rational.ZERO,
                        lists(rationals())
                )
        );
    }

    @NotNull
    @Override
    public Iterable<RationalPolynomial> monicRationalPolynomials(int degree) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<RationalPolynomial> monicRationalPolynomialsAtLeast(int minDegree) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<RationalPolynomial> monicRationalPolynomials() {
        return null;
    }
}
