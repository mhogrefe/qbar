package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.NoRemoveIterator;
import mho.wheels.ordering.Ordering;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;

/**
 * A {@code QBarExhaustiveProvider} produces {@code Iterable}s that generate some set of values in a specified order.
 * There is only a single instance of this class.
 */
public final strictfp class QBarExhaustiveProvider extends QBarIterableProvider {
    /**
     * The single instance of this class.
     */
    public static final @NotNull QBarExhaustiveProvider INSTANCE = new QBarExhaustiveProvider();

    /**
     * Disallow instantiation
     */
    private QBarExhaustiveProvider() {
        super(ExhaustiveProvider.INSTANCE);
    }

    /**
     * An {@code Iterable} that generates every positive {@code Rational}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> positiveRationals() {
        return map(
                p -> Rational.of(p.a, p.b),
                filterInfinite(p -> p.a.gcd(p.b).equals(BigInteger.ONE), pairs(positiveBigIntegers()))
        );
    }

    /**
     * An {@code Iterable} that generates every negative {@code Rational}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> negativeRationals() {
        return map(
                p -> Rational.of(p.a, p.b),
                filterInfinite(
                        p -> p.a.gcd(p.b).equals(BigInteger.ONE),
                        pairs(negativeBigIntegers(), positiveBigIntegers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates every nonzero {@code Rational}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> nonzeroRationals() {
        return map(
                p -> Rational.of(p.a, p.b),
                filterInfinite(
                        p -> p.a.gcd(p.b).equals(BigInteger.ONE),
                        pairs(nonzeroBigIntegers(), positiveBigIntegers())
                )
        );
    }

    /**
     * An {@link Iterable} that generates every {@code Rational}. Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> rationals() {
        return map(
                p -> Rational.of(p.a, p.b),
                filterInfinite(
                        p -> p.a.gcd(p.b).equals(BigInteger.ONE),
                        pairs(bigIntegers(), positiveBigIntegers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates every {@code Rational} in the interval [0, 1). Does not support removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne() {
        return cons(
                Rational.ZERO,
                map(
                        p -> Rational.of(p.a, p.b),
                        filterInfinite(p -> p.a.gcd(p.b).equals(BigInteger.ONE), subsetPairs(positiveBigIntegers()))
                )
        );
    }

    @Override
    public @NotNull Iterable<Rational> rangeUp(@NotNull Rational a) {
        return map(r -> r.add(a), withElement(Rational.ZERO, positiveRationals()));
    }

    @Override
    public @NotNull Iterable<Rational> rangeDown(@NotNull Rational a) {
        return map(a::subtract, withElement(Rational.ZERO, positiveRationals()));
    }

    @Override
    public @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b) {
        switch (Ordering.compare(a, b)) {
            case GT: return Collections.emptyList();
            case EQ: return Collections.singletonList(a);
            case LT:
                Rational diameter = b.subtract(a);
                return concat(
                        Arrays.asList(a, b),
                        map(r -> r.multiply(diameter).add(a), tail(nonNegativeRationalsLessThanOne()))
                );
            default: throw new IllegalStateException("unreachable");
        }
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
    public @NotNull Iterable<Rational> rationals(@NotNull Interval a) {
        if (!a.getLower().isPresent() && !a.getUpper().isPresent()) {
            return rationals();
        } else if (!a.getLower().isPresent()) {
            return rangeDown(a.getLower().get());
        } else if (!a.getUpper().isPresent()) {
            return rangeUp(a.getUpper().get());
        } else {
            return range(a.getLower().get(), a.getUpper().get());
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
        return null;
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

    /**
     * Determines whether {@code this} is equal to {@code that}. This implementation is the same as in
     * {@link java.lang.Object#equals}, but repeated here for clarity.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarExhaustiveProvider}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code QBarExhaustiveProvider} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        return this == that;
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarExhaustiveProvider}.</li>
     *  <li>The result is 0.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return 0;
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarExhaustiveProvider}.</li>
     *  <li>The result is {@code "QBarExhaustiveProvider"}.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    @Override
    public String toString() {
        return "QBarExhaustiveProvider";
    }
}
