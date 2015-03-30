package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.IterableProvider;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public interface QBarIterableProvider extends IterableProvider {
    public @NotNull Iterable<Rational> rangeUp(@NotNull Rational a);

    public @NotNull Iterable<Rational> rangeDown(@NotNull Rational a);

    public @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b);

    public @NotNull Iterable<Rational> rationals();

    public @NotNull Iterable<Rational> nonNegativeRationals();

    public @NotNull Iterable<Rational> positiveRationals();

    public @NotNull Iterable<Rational> negativeRationals();

    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne();

    public @NotNull Iterable<Interval> finitelyBoundedIntervals();

    public @NotNull Iterable<Interval> intervals();

    public @NotNull Iterable<Byte> bytes(@NotNull Interval a);

    public @NotNull Iterable<Short> shorts(@NotNull Interval a);

    public @NotNull Iterable<Integer> integers(@NotNull Interval a);

    public @NotNull Iterable<Long> longs(@NotNull Interval a);

    public @NotNull Iterable<BigInteger> bigIntegers(@NotNull Interval a);

    public @NotNull Iterable<Rational> rationals(@NotNull Interval a);

    public @NotNull Iterable<Rational> rationalsNotIn(@NotNull Interval a);

    public @NotNull Iterable<RationalVector> rationalVectors(int dimension);

    public @NotNull Iterable<RationalVector> rationalVectorsAtLeast(int minDimension);

    public @NotNull Iterable<RationalVector> rationalVectors();

    public @NotNull Iterable<RationalVector> reducedRationalVectors(int dimension);

    public @NotNull Iterable<RationalVector> reducedRationalVectorsAtLeast(int minDimension);

    public @NotNull Iterable<RationalVector> reducedRationalVectors();

    public @NotNull Iterable<RationalMatrix> rationalMatrices(int height, int width);

    public @NotNull Iterable<RationalMatrix> rationalMatrices();

    public @NotNull Iterable<Polynomial> polynomials(int degree);

    public @NotNull Iterable<Polynomial> polynomialsAtLeast(int minDegree);

    public @NotNull Iterable<Polynomial> polynomials();

    public @NotNull Iterable<Polynomial> primitivePolynomials(int degree);

    public @NotNull Iterable<Polynomial> primitivePolynomialsAtLeast(int minDegree);

    public @NotNull Iterable<Polynomial> primitivePolynomials();

    public @NotNull Iterable<RationalPolynomial> rationalPolynomials(int degree);

    public @NotNull Iterable<RationalPolynomial> rationalPolynomialsAtLeast(int minDegree);

    public @NotNull Iterable<RationalPolynomial> rationalPolynomials();

    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomials(int degree);

    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomialsAtLeast(int minDegree);

    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomials();
}
