package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.iterables.NoRemoveIterable;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.le;

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
     * An {@code Iterable} that generates every positive {@link mho.qbar.objects.Rational}. Does not support removal.
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
                concatMap(
                        d -> map(
                                n -> Rational.of(n, d),
                                filter(
                                        n -> n.gcd(d).equals(BigInteger.ONE),
                                        IterableUtils.range(BigInteger.ONE, d.subtract(BigInteger.ONE))
                                )
                        ),
                        IterableUtils.rangeUp(IntegerUtils.TWO)
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Rational}s between {@code a} and {@code b}, inclusive. If
     * {@code a}{@literal >}{@code b}, an empty {@code Iterable} is returned. Does not support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Rational}s.</li>
     * </ul>
     *
     * Length is 0 if a{@literal >}b, 1 if a=b, and infinite otherwise
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return {@code Rational}s between {@code a} and {@code b}, inclusive
     */
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
     * An {@code Iterable} that generates all finitely-bounded {@link mho.qbar.objects.Interval}s. Does not support
     * removal.
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Interval> finitelyBoundedIntervals() {
        return map(p -> Interval.of(p.a, p.b), bagPairs(rationals()));
    }

    /**
     * An {@code Iterable} that generates all {@code Interval}s. Does not support removal.
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
                filterInfinite(
                        p -> !p.a.isPresent() || !p.b.isPresent() || le(p.a.get(), p.b.get()),
                        pairs(optionals(rationals()))
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Rational}s not contained in a given {@code Interval}. Does not
     * support removal.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Rational}s.</li>
     * </ul>
     *
     * Length is 0 if {@code a} is (–∞, ∞), infinite otherwise
     *
     * @param a an {@code Interval}
     * @return {r|r∉{@code a}}
     */
    @Override
    public @NotNull Iterable<Rational> rationalsNotIn(@NotNull Interval a) {
        List<Interval> complement = a.complement();
        switch (complement.size()) {
            case 0:
                return Collections.emptyList();
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
     * An {@code Iterable} that generates all {@code Vector}s.
     *
     * <ul>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Vector}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Vector> vectors() {
        return map(Vector::of, lists(bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all {@code Vector}s with a minimum dimension.
     *
     * <ul>
     *  <li>{@code dimension} cannot be negative.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Vector}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDimension the minimum dimension of the generated {@code Vector}s
     * @return all {@code Vector}s with dimension at least {@code minDimension}
     */
    @Override
    public @NotNull Iterable<Vector> vectorsAtLeast(int minDimension) {
        return map(Vector::of, listsAtLeast(minDimension, bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all {@code RationalVector}s.
     *
     * <ul>
     *  <li>The result is a non-removable {@code Iterable} containing {@code RationalVector}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalVector> rationalVectors() {
        return map(RationalVector::of, lists(rationals()));
    }

    /**
     * An {@code Iterable} that generates all {@code RationalVector}s with a minimum dimension.
     *
     * <ul>
     *  <li>{@code dimension} cannot be negative.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code RationalVector}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDimension the minimum dimension of the generated {@code RationalVector}s
     * @return all {@code RationalVector}s with dimension at least {@code minDimension}
     */
    @Override
    public @NotNull Iterable<RationalVector> rationalVectorsAtLeast(int minDimension) {
        return map(RationalVector::of, listsAtLeast(minDimension, rationals()));
    }

    /**
     * An {@code Iterable} that generates reduced all {@code RationalVector}s with a given dimension (see
     * {@link RationalVector#reduce()}).
     *
     * <ul>
     *  <li>{@code dimension} cannot be negative.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code RationalVector}s.</li>
     * </ul>
     *
     * Length is 1 if {@code dimension} is 0, 2 if {@code dimension} is 1, and infinite otherwise
     *
     * @param dimension the dimension of the generated {@code RationalVector}s
     * @return all {@code RationalVector}s with dimension {@code dimension}
     */
    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectors(int dimension) {
        switch (dimension) {
            case 0:
                return Collections.singletonList(RationalVector.ZERO_DIMENSIONAL);
            case 1:
                return Arrays.asList(RationalVector.of(Rational.ZERO), RationalVector.of(Rational.ONE));
            default:
                return reducedRationalVectors(lists(dimension, bigIntegers()));
        }
    }

    /**
     * An {@code Iterable} that generates all reduced {@code RationalVector}s (see {@link RationalVector#reduce()}).
     *
     * <ul>
     *  <li>The result is a non-removable {@code Iterable} containing {@code RationalVector}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectors() {
        return reducedRationalVectors(lists(bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates reduced all {@code RationalVector}s with a minimum dimension (see
     * {@link RationalVector#reduce()}).
     *
     * <ul>
     *  <li>{@code dimension} cannot be negative.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code RationalVector}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDimension the minimum dimension of the generated {@code RationalVector}s
     * @return all {@code RationalVector}s with dimension at least {@code minDimension}
     */
    @Override
    public @NotNull Iterable<RationalVector> reducedRationalVectorsAtLeast(int minDimension) {
        return reducedRationalVectors(listsAtLeast(minDimension, bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all {@code Matrix}es with a given height and width.
     *
     * <ul>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Matrix}es.</li>
     * </ul>
     *
     * Length is 1 if either {@code height} or {@code width} are 0, infinite otherwise
     *
     * @param height the height (number of rows) of the generated {@code Matrix}es
     * @param width the width (number of columns) of the generated {@code Matrix}es
     * @return all {@code Matrix}es with height {@code height} and width {@code width}
     */
    @Override
    public @NotNull Iterable<Matrix> matrices(int height, int width) {
        if (height == 0 || width == 0) {
            return Collections.singletonList(Matrix.zero(height, width));
        } else {
            return map(Matrix::fromRows, lists(height, vectors(width)));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code Matrix}.
     *
     * <ul>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Matrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Matrix> matrices() {
        return chooseLogarithmicOrder(
                map(
                        p -> Matrix.fromRows(p.b),
                        dependentPairsInfiniteSquareRootOrder(pairs(positiveIntegers()), p -> lists(p.a, vectors(p.b)))
                ),
                choose(map(i -> Matrix.zero(0, i), naturalIntegers()), map(i -> Matrix.zero(i, 0), positiveIntegers()))
        );
    }

    /**
     * An {@code Iterable} that generates all square {@code Matrix}es.
     *
     * <ul>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is a non-removable {@code Iterable} containing square {@code Matrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Matrix> squareMatrices() {
        return cons(
                Matrix.zero(0, 0),
                map(p -> p.b, dependentPairsInfiniteLogarithmicOrder(positiveIntegers(), i -> matrices(i, i)))
        );
    }

    /**
     * An {@code Iterable} that generates all {@code RationalMatrix}es with a given height and width.
     *
     * <ul>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code RationalMatrix}es.</li>
     * </ul>
     *
     * Length is 1 if either {@code height} or {@code width} are 0, infinite otherwise
     *
     * @param height the height (number of rows) of the generated {@code RationalMatrix}es
     * @param width the width (number of columns) of the generated {@code RationalMatrix}es
     * @return all {@code RationalMatrix}es with height {@code height} and width {@code width}
     */
    @Override
    public @NotNull Iterable<RationalMatrix> rationalMatrices(int height, int width) {
        if (height == 0 || width == 0) {
            return Collections.singletonList(RationalMatrix.zero(height, width));
        } else {
            return map(RationalMatrix::fromRows, lists(height, rationalVectors(width)));
        }
    }

    /**
     * An {@code Iterable} that generates all {@code RationalMatrix}.
     *
     * <ul>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code RationalMatrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalMatrix> rationalMatrices() {
        return chooseLogarithmicOrder(
                map(
                        p -> RationalMatrix.fromRows(p.b),
                        dependentPairsInfiniteSquareRootOrder(
                                pairs(positiveIntegers()),
                                p -> lists(p.a, rationalVectors(p.b))
                        )
                ),
                choose(
                        map(i -> RationalMatrix.zero(0, i), naturalIntegers()),
                        map(i -> RationalMatrix.zero(i, 0), positiveIntegers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all square {@code RationalMatrix}es.
     *
     * <ul>
     *  <li>{@code height} cannot be negative.</li>
     *  <li>{@code width} cannot be negative.</li>
     *  <li>The result is a non-removable {@code Iterable} containing square {@code RationalMatrix}es.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalMatrix> squareRationalMatrices() {
        return cons(
                RationalMatrix.zero(0, 0),
                map(p -> p.b, dependentPairsInfiniteLogarithmicOrder(positiveIntegers(), i -> rationalMatrices(i, i)))
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Polynomial}s.
     *
     * <ul>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Polynomial> polynomials() {
        return map(
                js -> Polynomial.of(toList(js)),
                filterInfinite(is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO), lists(bigIntegers()))
        );
    }

    /**
     * An {@code Iterable} that generates all {@code Polynomial}s with a minimum degree.
     *
     * <ul>
     *  <li>{@code degree} must be at least -1.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     * @return all {@code Polynomial}s with degree at least {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> polynomialsAtLeast(int minDegree) {
        return map(
                js -> Polynomial.of(toList(js)),
                filterInfinite(
                        is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                        listsAtLeast(minDegree + 1, bigIntegers())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1)
     * with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be at least -1.</li>
     *  <li>The result is a non-removable {@code Iterable} containing primitive {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is 0 if {@code degree} is –1, 2 if {@code degree} is 0, and infinite otherwise
     *
     * @param degree the degree of the generated {@code Polynomial}s
     * @return all primitive {@code Polynomial}s with degree at {@code degree}
     */
    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomials(int degree) {
        switch (degree) {
            case -1: return Collections.emptyList();
            case 0: return Arrays.asList(Polynomial.ONE, Polynomial.ONE.negate());
            default: return primitivePolynomials(lists(degree + 1, bigIntegers()));
        }
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1).
     *
     * <ul>
     *  <li>The result is a non-removable {@code Iterable} containing primitive {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomials() {
        return primitivePolynomials(listsAtLeast(1, bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1)
     * with a minimum degree.
     *
     * <ul>
     *  <li>{@code minDegree} must be at least -1.</li>
     *  <li>The result is a non-removable {@code Iterable} containing primitive {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     * @return all primitive {@code Polynomial}s with degree at least {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> primitivePolynomialsAtLeast(int minDegree) {
        return primitivePolynomials(listsAtLeast(minDegree == -1 ? 1 : minDegree + 1, bigIntegers()));
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1)
     * with a positive leading coefficient with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be at least -1.</li>
     *  <li>The result is a non-removable {@code Iterable} containing primitive {@code Polynomial}s with positive
     *  leading coefficients.</li>
     * </ul>
     *
     * Length is 0 if {@code degree} is –1, 1 if {@code degree} is 0, and infinite otherwise
     *
     * @param degree the degree of the generated {@code Polynomial}s
     * @return all positive primitive {@code Polynomial}s with positive leading coefficients and degree {@code degree}
     */
    @Override
    public @NotNull Iterable<Polynomial> positivePrimitivePolynomials(int degree) {
        switch (degree) {
            case -1: return Collections.emptyList();
            case 0: return Collections.singletonList(Polynomial.ONE);
            default: return filterInfinite(p -> p.signum() == 1, primitivePolynomials(degree));
        }
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1)
     * with a positive leasing coefficient.
     *
     * <ul>
     *  <li>The result is a non-removable {@code Iterable} containing primitive {@code Polynomial}s with positive
     *  leading coefficients.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Polynomial> positivePrimitivePolynomials() {
        return filterInfinite(p -> p.signum() == 1, primitivePolynomials());
    }

    /**
     * An {@code Iterable} that generates all primitive {@code Polynomial}s (polynomials whose coefficient GCD is 1)
     * with a positive leading coefficient with a minimum degree.
     *
     * <ul>
     *  <li>{@code minDegree} must be at least -1.</li>
     *  <li>The result is a non-removable {@code Iterable} containing primitive {@code Polynomial}s with positive
     *  leading coefficients.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     * @return all positive primitive {@code Polynomial}s with positive leading coefficients and degree at least
     * {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> positivePrimitivePolynomialsAtLeast(int minDegree) {
        return filterInfinite(p -> p.signum() == 1, primitivePolynomialsAtLeast(minDegree));
    }

    /**
     * An {@code Iterable} that generates all square-free {@code Polynomial}s with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be at least -1.</li>
     *  <li>The result is a non-removable {@code Iterable} containing square-free {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is 0 if {@code degree} is –1, infinite otherwise
     *
     * @param degree the degree of the generated {@code Polynomial}s
     * @return all square-free {@code Polynomial}s with degree {@code degree}
     */
    @Override
    public @NotNull Iterable<Polynomial> squareFreePolynomials(int degree) {
        return filter(Polynomial::isSquareFree, polynomials(degree));
    }

    /**
     * An {@code Iterable} that generates all irreducible {@code Polynomial}s.
     *
     * <ul>
     *  <li>The result is a non-removable {@code Iterable} containing irreducible {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Polynomial> irreduciblePolynomials() {
        return withElement(
                Polynomial.ONE, map(
                        p -> p.b,
                        dependentPairsInfiniteLogarithmicOrder(
                                positiveBigIntegers(),
                                i -> irreduciblePolynomials(i.intValueExact())
                        )
                )
        );
    }

    /**
     * An {@code Iterable} that generates all irreducible {@code Polynomial}s with a minimum degree.
     *
     * <ul>
     *  <li>{@code minDegree} must be at least -1.</li>
     *  <li>The result is a non-removable {@code Iterable} containing irreducible {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     * @return all irreducible {@code Polynomial}s with positive leading coefficients and degree at least
     * {@code minDegree}
     */
    @Override
    public @NotNull Iterable<Polynomial> irreduciblePolynomialsAtLeast(int minDegree) {
        if (minDegree < -1) {
            throw new IllegalArgumentException("minDegree must be at least -1. Invalid minDegree: " + minDegree);
        }
        if (minDegree < 1) return irreduciblePolynomials();
        return map(
                p -> p.b,
                dependentPairsInfiniteLogarithmicOrder(
                        rangeUp(BigInteger.valueOf(minDegree)),
                        i -> irreduciblePolynomials(i.intValueExact())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@code RationalPolynomial}s.
     *
     * <ul>
     *  <li>The result is a non-removable {@code Iterable} containing {@code RationalPolynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomials() {
        return map(
                is -> RationalPolynomial.of(toList(is)),
                filterInfinite(is -> is.isEmpty() || last(is) != Rational.ZERO, lists(rationals()))
        );
    }

    /**
     * An {@code Iterable} that generates all {@code RationalPolynomial}s with a minimum degree.
     *
     * <ul>
     *  <li>{@code degree} must be at least -1.</li>
     *  <li>The result is a non-removable {@code Iterable} containing {@code RationalPolynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param minDegree the minimum degree of the generated {@code RationalPolynomial}s
     * @return all {@code RationalPolynomial}s with degree at least {@code minDegree}
     */
    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomialsAtLeast(int minDegree) {
        return map(
                is -> RationalPolynomial.of(toList(is)),
                filterInfinite(
                        is -> is.isEmpty() || last(is) != Rational.ZERO,
                        listsAtLeast(minDegree + 1, rationals())
                )
        );
    }

    /**
     * An {@code Iterable} that generates all {@link MonomialOrder}s. Does not support removal.
     *
     * Length is 3
     */
    @Override
    public @NotNull Iterable<MonomialOrder> monomialOrders() {
        return new NoRemoveIterable<>(Arrays.asList(MonomialOrder.LEX, MonomialOrder.GRLEX, MonomialOrder.GREVLEX));
    }

    /**
     * An {@code Iterable} that generates all {@code ExponentVector}s.
     *
     * <ul>
     *  <li>The result is a non-removable {@code Iterable} containing {@code ExponentVector}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<ExponentVector> exponentVectors() {
        return map(
                js -> ExponentVector.of(toList(js)),
                filterInfinite(
                        is -> is.isEmpty() || last(is) != 0,
                        map(i -> toList(map(p -> p.b - 1, countAdjacent(IntegerUtils.bits(i)))), naturalIntegers())
                )
        );
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
