package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.ExhaustiveProvider;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;

@SuppressWarnings("ConstantConditions")
public class QBarExhaustiveProvider extends ExhaustiveProvider implements QBarIterableProvider {
    public static final @NotNull QBarExhaustiveProvider INSTANCE = new QBarExhaustiveProvider();

    protected QBarExhaustiveProvider() {
        super();
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
}
