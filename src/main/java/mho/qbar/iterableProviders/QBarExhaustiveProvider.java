package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.math.Combinatorics;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;

@SuppressWarnings("ConstantConditions")
public class QBarExhaustiveProvider extends QBarIterableProvider {
    public static final @NotNull QBarExhaustiveProvider INSTANCE = new QBarExhaustiveProvider();
    private static final @NotNull ExhaustiveProvider EP = ExhaustiveProvider.INSTANCE;

    private QBarExhaustiveProvider() {}
    
    @Override
    public @NotNull Iterable<Boolean> booleans() {
        return EP.booleans();
    }
    
    @Override
    public @NotNull Iterable<Ordering> orderings() {
        return EP.orderings();
    }

    @Override
    public @NotNull Iterable<RoundingMode> roundingModes() {
        return EP.roundingModes();
    }

    @Override
    public @NotNull Iterable<Byte> rangeUp(byte a) {
        return EP.rangeUp(a);
    }
    
    @Override
    public @NotNull Iterable<Short> rangeUp(short a) {
        return EP.rangeUp(a);
    }

    @Override
    public @NotNull Iterable<Integer> rangeUp(int a) {
        return EP.rangeUp(a);
    }

    @Override
    public @NotNull Iterable<Long> rangeUp(long a) {
        return EP.rangeUp(a);
    }

    @Override
    public @NotNull Iterable<BigInteger> rangeUp(@NotNull BigInteger a) {
        return EP.rangeUp(a);
    }

    @Override
    public @NotNull Iterable<Character> rangeUp(char a) {
        return EP.rangeUp(a);
    }

    @Override
    public @NotNull Iterable<Byte> rangeDown(byte a) {
        return EP.rangeDown(a);
    }

    @Override
    public @NotNull Iterable<Short> rangeDown(short a) {
        return EP.rangeDown(a);
    }

    @Override
    public @NotNull Iterable<Integer> rangeDown(int a) {
        return EP.rangeDown(a);
    }

    @Override
    public @NotNull Iterable<Long> rangeDown(long a) {
        return EP.rangeDown(a);
    }

    @Override
    public @NotNull Iterable<BigInteger> rangeDown(@NotNull BigInteger a) {
        return EP.rangeDown(a);
    }

    @Override
public @NotNull Iterable<Character> rangeDown(char a) {
        return EP.rangeDown(a);
    }

    @Override
    public @NotNull Iterable<Byte> range(byte a, byte b) {
        return EP.range(a, b);
    }

    @Override
    public @NotNull Iterable<Short> range(short a, short b) {
        return EP.range(a, b);
    }

    @Override
    public @NotNull Iterable<Integer> range(int a, int b) {
        return EP.range(a, b);
    }

    @Override
    public @NotNull Iterable<Long> range(long a, long b) {
        return EP.range(a, b);
    }

    @Override
    public @NotNull Iterable<BigInteger> range(@NotNull BigInteger a, @NotNull BigInteger b) {
        return EP.range(a, b);
    }

    @Override
    public @NotNull Iterable<Character> range(char a, char b) {
        return EP.range(a, b);
    }

    @Override
    public @NotNull <T> Iterable<T> uniformSample(@NotNull List<T> xs) {
        return EP.uniformSample(xs);
    }

    @Override
    public @NotNull Iterable<Character> uniformSample(@NotNull String s) {
        return EP.uniformSample(s);
    }

    @Override
    public @NotNull Iterable<Byte> positiveBytes() {
        return EP.positiveBytes();
    }

    @Override
    public @NotNull Iterable<Short> positiveShorts() {
        return EP.positiveShorts();
    }

    @Override
    public @NotNull Iterable<Integer> positiveIntegers() {
        return EP.positiveIntegers();
    }

    @Override
    public @NotNull Iterable<Long> positiveLongs() {
        return EP.positiveLongs();
    }

    @Override
    public @NotNull Iterable<BigInteger> positiveBigIntegers() {
        return EP.positiveBigIntegers();
    }

    @Override
    public @NotNull Iterable<Byte> negativeBytes() {
        return EP.negativeBytes();
    }

    @Override
    public @NotNull Iterable<Short> negativeShorts() {
        return EP.negativeShorts();
    }

    @Override
    public @NotNull Iterable<Integer> negativeIntegers() {
        return EP.negativeIntegers();
    }

    @Override
    public @NotNull Iterable<Long> negativeLongs() {
        return EP.negativeLongs();
    }

    @Override
    public @NotNull Iterable<BigInteger> negativeBigIntegers() {
        return EP.negativeBigIntegers();
    }

    @Override
    public @NotNull Iterable<Byte> naturalBytes() {
        return EP.naturalBytes();
    }

    @Override
    public @NotNull Iterable<Short> naturalShorts() {
        return EP.naturalShorts();
    }

    @Override
    public @NotNull Iterable<Integer> naturalIntegers() {
        return EP.naturalIntegers();
    }

    @Override
    public @NotNull Iterable<Long> naturalLongs() {
        return EP.naturalLongs();
    }

    @Override
    public @NotNull Iterable<BigInteger> naturalBigIntegers() {
        return EP.naturalBigIntegers();
    }

    @Override
    public @NotNull Iterable<Byte> bytes() {
        return EP.bytes();
    }

    @Override
    public @NotNull Iterable<Short> shorts() {
        return EP.shorts();
    }

    @Override
    public @NotNull Iterable<Integer> integers() {
        return EP.integers();
    }

    @Override
    public @NotNull Iterable<Long> longs() {
        return EP.longs();
    }

    @Override
    public @NotNull Iterable<BigInteger> bigIntegers() {
        return EP.bigIntegers();
    }

    @Override
    public @NotNull Iterable<Character> asciiCharacters() {
        return EP.asciiCharacters();
    }

    @Override
    public @NotNull Iterable<Character> characters() {
        return EP.characters();
    }

    @Override
    public @NotNull Iterable<Float> positiveOrdinaryFloats() {
        return EP.positiveOrdinaryFloats();
    }

    @Override
    public @NotNull Iterable<Float> negativeOrdinaryFloats() {
        return EP.negativeOrdinaryFloats();
    }

    @Override
    public @NotNull Iterable<Float> ordinaryFloats() {
        return EP.ordinaryFloats();
    }

    @Override
    public @NotNull Iterable<Float> floats() {
        return EP.floats();
    }

    @Override
    public @NotNull Iterable<Double> positiveOrdinaryDoubles() {
        return EP.positiveOrdinaryDoubles();
    }

    @Override
    public @NotNull Iterable<Double> negativeOrdinaryDoubles() {
        return EP.negativeOrdinaryDoubles();
    }

    @Override
    public @NotNull Iterable<Double> ordinaryDoubles() {
        return EP.ordinaryDoubles();
    }

    @Override
    public @NotNull Iterable<Double> doubles() {
        return EP.doubles();
    }

    @Override
    public @NotNull Iterable<BigDecimal> positiveBigDecimals() {
        return EP.positiveBigDecimals();
    }

    @Override
    public @NotNull Iterable<BigDecimal> negativeBigDecimals() {
        return EP.negativeBigDecimals();
    }

    @Override
    public @NotNull Iterable<BigDecimal> bigDecimals() {
        return EP.bigDecimals();
    }

    @Override
    public @NotNull <T> Iterable<T> withNull(@NotNull Iterable<T> xs) {
        return EP.withNull(xs);
    }

    @Override
    public @NotNull <T> Iterable<Optional<T>> optionals(@NotNull Iterable<T> xs) {
        return EP.optionals(xs);
    }

    @Override
    public @NotNull <T> Iterable<NullableOptional<T>> nullableOptionals(@NotNull Iterable<T> xs) {
        return EP.nullableOptionals(xs);
    }

    @Override
    public @NotNull <T> Iterable<Pair<T, T>> pairsLogarithmicOrder(@NotNull Iterable<T> xs) {
        return EP.pairsLogarithmicOrder(xs);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> pairsLogarithmicOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return EP.pairsLogarithmicOrder(as, bs);
    }

    @Override
    public @NotNull <T> Iterable<Pair<T, T>> pairsSquareRootOrder(@NotNull Iterable<T> xs) {
        return EP.pairsSquareRootOrder(xs);
    }

    /**
     * See {@link mho.wheels.math.Combinatorics#pairsSquareRootOrder(Iterable, Iterable)}
     *
     * @param as the {@code Iterable} from which the first components of the pairs are selected
     * @param bs the {@code Iterable} from which the second components of the pairs are selected
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @return all pairs of elements from {@code as} and {@code bs} in square-root order
     */
    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> pairsSquareRootOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return EP.pairsSquareRootOrder(as, bs);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairs(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return EP.dependentPairs(xs, f);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsLogarithmic(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return EP.dependentPairsLogarithmic(xs, f);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsSquareRoot(@NotNull Iterable<A> xs, @NotNull Function<A, Iterable<B>> f) {
        return EP.dependentPairsSquareRoot(
                xs,
                f
        );
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsExponential(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return EP.dependentPairsExponential(xs, f);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsSquare(@NotNull Iterable<A> xs, @NotNull Function<A, Iterable<B>> f) {
        return EP.dependentPairsSquare(xs, f);
    }

    @Override
    public @NotNull <A, B> Iterable<Pair<A, B>> pairs(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        return EP.pairs(as, bs);
    }

    @Override
    public @NotNull <A, B, C> Iterable<Triple<A, B, C>> triples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs
    ) {
        return EP.triples(as, bs, cs);
    }

    @Override
    public @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds
    ) {
        return EP.quadruples(as, bs, cs, ds);
    }

    @Override
    public @NotNull <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> quintuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es
    ) {
        return EP.quintuples(as, bs, cs, ds, es);
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
        return EP.sextuples(as, bs, cs, ds, es, fs);
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
        return EP.septuples(as, bs, cs, ds, es, fs, gs);
    }

    @Override
    public @NotNull <T> Iterable<Pair<T, T>> pairs(@NotNull Iterable<T> xs) {
        return EP.pairs(xs);
    }

    @Override
    public @NotNull <T> Iterable<Triple<T, T, T>> triples(@NotNull Iterable<T> xs) {
        return EP.triples(xs);
    }

    @Override
    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> quadruples(@NotNull Iterable<T> xs) {
        return EP.quadruples(xs);
    }

    @Override
    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> quintuples(@NotNull Iterable<T> xs) {
        return EP.quintuples(xs);
    }

    @Override
    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> sextuples(@NotNull Iterable<T> xs) {
        return EP.sextuples(xs);
    }

    @Override
    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> septuples(@NotNull Iterable<T> xs) {
        return EP.septuples(xs);
    }

    @Override
    public @NotNull <T> Iterable<List<T>> lists(int size, @NotNull Iterable<T> xs) {
        return EP.lists(size, xs);
    }

    @Override
    public @NotNull <T> Iterable<List<T>> listsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return EP.listsAtLeast(minSize, xs);
    }

    @Override
    public @NotNull <T> Iterable<List<T>> lists(@NotNull Iterable<T> xs) {
        return EP.lists(xs);
    }

    @Override
    public @NotNull Iterable<String> strings(int size, @NotNull Iterable<Character> cs) {
        return EP.strings(size, cs);
    }

    @Override
    public @NotNull Iterable<String> stringsAtLeast(int minSize, @NotNull Iterable<Character> cs) {
        return EP.stringsAtLeast(minSize, cs);
    }

    @Override
    public @NotNull Iterable<String> strings(int size) {
        return EP.strings(size);
    }

    @Override
    public @NotNull Iterable<String> stringsAtLeast(int size) {
        return EP.stringsAtLeast(size);
    }

    @Override
    public @NotNull Iterable<String> strings(@NotNull Iterable<Character> cs) {
        return EP.strings(cs);
    }

    @Override
    public @NotNull Iterable<String> strings() {
        return EP.strings();
    }

    @Override
    public @NotNull Iterable<Rational> rangeUp(@NotNull Rational a) {
        return iterate(r -> r.add(Rational.ONE), a);
    }

    @Override
    public @NotNull Iterable<Rational> rangeDown(@NotNull Rational a) {
        return iterate(r -> r.subtract(Rational.ONE), a);
    }

    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b) {
        if (gt(a, b)) return Collections.emptyList();
        return () -> new Iterator<Rational>() {
            private Rational x = a;
            private boolean reachedEnd;

            @Override
            public boolean hasNext() {
                return !reachedEnd;
            }

            @Override
            public Rational next() {
                reachedEnd = x.equals(b);
                Rational oldX = x;
                x = x.add(Rational.ONE);
                return oldX;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("cannot remove from this iterator");
            }
        };
    }

    /**
     * @return an {@link Iterable} that contains every {@link mho.qbar.objects.Rational}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> rationals() {
        return map(
                p -> Rational.of(p.a, p.b),
                filter(p -> p.a.gcd(p.b).equals(BigInteger.ONE), pairs(bigIntegers(), positiveBigIntegers()))
        );
    }

    /**
     * @return an {@code Iterable} that contains every non-negative {@code Rational}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> nonNegativeRationals() {
        return map(
                p -> Rational.of(p.a, p.b),
                filter(p -> p.a.gcd(p.b).equals(BigInteger.ONE), pairs(naturalBigIntegers(), positiveBigIntegers()))
        );
    }

    /**
     * an {@code Iterable} that contains every positive {@code Rational}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> positiveRationals() {
        return map(
                p -> Rational.of(p.a, p.b),
                filter(p -> p.a.gcd(p.b).equals(BigInteger.ONE), pairs(positiveBigIntegers()))
        );
    }

    /**
     * an {@code Iterable} that contains every negative {@code Rational}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> negativeRationals() {
        return map(
                p -> Rational.of(p.a, p.b),
                filter(p -> p.a.gcd(p.b).equals(BigInteger.ONE), pairs(negativeBigIntegers(), positiveBigIntegers()))
        );
    }

    /**
     * an {@code Iterable} that contains every {@code Rational} in the interval [0, 1). Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne() {
        return map(
                p -> Rational.of(p.a, p.b),
                filter(
                        p -> lt(p.a, p.b) && p.a.gcd(p.b).equals(BigInteger.ONE),
                        pairs(naturalBigIntegers(), positiveBigIntegers())
                )
        );
    }

    /**
     * an {@code Iterable} that contains every finitely-bounded {@link mho.qbar.objects.Interval}. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Interval> finitelyBoundedIntervals() {
        return map(p -> Interval.of(p.a, p.b), filter(p -> le(p.a, p.b), pairs(rationals())));
    }

    /**
     * an {@code Iterable} that contains every {@code Interval}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Interval> intervals() {
        return map(
                p -> {
                    if (!p.a.isPresent() && !p.b.isPresent()) return Interval.ALL;
                    if (!p.a.isPresent()) return Interval.lessThanOrEqualTo(p.b.get());
                    if (!p.b.isPresent()) return Interval.greaterThanOrEqualTo(p.a.get());
                    return Interval.of(p.a.get(), p.b.get());
                },
                filter(
                        p -> !p.a.isPresent() || !p.b.isPresent() || le(p.a.get(), p.b.get()),
                        pairs(optionals(rationals()))
                )
        );
    }

    @Override
    public @NotNull Iterable<Byte> bytes(@NotNull Interval a) {
        Optional<Interval> intersection =
                a.intersection(Interval.of(Rational.of(Byte.MIN_VALUE), Rational.of(Byte.MAX_VALUE)));
        if (!intersection.isPresent()) return Collections.emptyList();
        return map(BigInteger::byteValueExact, bigIntegers(intersection.get()));
    }

    @Override
    public @NotNull Iterable<Short> shorts(@NotNull Interval a) {
        Optional<Interval> intersection =
                a.intersection(Interval.of(Rational.of(Short.MIN_VALUE), Rational.of(Short.MAX_VALUE)));
        if (!intersection.isPresent()) return Collections.emptyList();
        return map(BigInteger::shortValueExact, bigIntegers(intersection.get()));
    }

    @Override
    public @NotNull Iterable<Integer> integers(@NotNull Interval a) {
        Optional<Interval> intersection =
                a.intersection(Interval.of(Rational.of(Integer.MIN_VALUE), Rational.of(Integer.MAX_VALUE)));
        if (!intersection.isPresent()) return Collections.emptyList();
        return map(BigInteger::intValueExact, bigIntegers(intersection.get()));
    }

    @Override
    public @NotNull Iterable<Long> longs(@NotNull Interval a) {
        Optional<Interval> intersection = a.intersection(
                Interval.of(
                        Rational.of(BigInteger.valueOf(Long.MIN_VALUE)),
                        Rational.of(BigInteger.valueOf(Long.MAX_VALUE))
                )
        );
        if (!intersection.isPresent()) return Collections.emptyList();
        return map(BigInteger::longValueExact, bigIntegers(intersection.get()));
    }

    @Override
    public @NotNull Iterable<BigInteger> bigIntegers(@NotNull Interval a) {
        if (!a.getLower().isPresent() && !a.getUpper().isPresent()) {
            return bigIntegers();
        } else if (!a.getLower().isPresent()) {
            return rangeDown(a.getUpper().get().floor());
        } else if (!a.getUpper().isPresent()) {
            return rangeUp(a.getLower().get().ceiling());
        } else {
            return range(a.getLower().get().ceiling(), a.getUpper().get().floor());
        }
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
            if (diameter == Rational.ZERO) return Arrays.asList(a.getLower().get());
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
        if (height == 0 || width == 0) return Collections.singletonList(RationalMatrix.zero(height, width));
        return map(RationalMatrix::fromColumns, lists(width, rationalVectors(height)));
    }

    @Override
    public @NotNull Iterable<RationalMatrix> rationalMatrices() {
        return map(q -> q.b, dependentPairsSquare(pairs(naturalIntegers()), p -> rationalMatrices(p.a, p.b)));
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
                filter(is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO), lists(bigIntegers()))
        );
    }

    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomials(int degree) {
        if (degree == 0) return Collections.singletonList(Polynomial.ONE);
        return filter(
                p -> p.signum() == 1,
                map(
                        Polynomial::of,
                        filter(
                                js -> {
                                    if (!js.isEmpty() && last(js).equals(BigInteger.ZERO)) return false;
                                    BigInteger gcd = foldl(BigInteger::gcd, BigInteger.ZERO, js);
                                    return gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE);
                                },
                                lists(degree + 1, bigIntegers())
                        )
                )
        );
    }

    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomialsAtLeast(int minDegree) {
        return filter(
                p -> p.signum() == 1,
                map(
                        Polynomial::of,
                        filter(
                                js -> {
                                    if (!js.isEmpty() && last(js).equals(BigInteger.ZERO)) return false;
                                    BigInteger gcd = foldl(BigInteger::gcd, BigInteger.ZERO, js);
                                    return gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE);
                                },
                                listsAtLeast(minDegree + 1, bigIntegers())
                        )
                )
        );
    }

    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomials() {
        return filter(
                p -> p.signum() == 1,
                map(
                        Polynomial::of,
                        filter(
                                js -> {
                                    if (!js.isEmpty() && last(js).equals(BigInteger.ZERO)) return false;
                                    BigInteger gcd = foldl(BigInteger::gcd, BigInteger.ZERO, js);
                                    return gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE);
                                },
                                lists(bigIntegers())
                        )
                )
        );
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
                filter(
                        is -> is.isEmpty() || last(is) != Rational.ZERO,
                        listsAtLeast(minDegree + 1, rationals())
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomials() {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(is -> is.isEmpty() || last(is) != Rational.ZERO, lists(rationals()))
        );
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomials(int degree) {
        return map(p -> p.toRationalPolynomial().makeMonic(), primitivePolynomials(degree));
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomialsAtLeast(int minDegree) {
        return map(p -> p.toRationalPolynomial().makeMonic(), primitivePolynomialsAtLeast(minDegree));
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomials() {
        return map(p -> p.toRationalPolynomial().makeMonic(), primitivePolynomials());
    }
}
