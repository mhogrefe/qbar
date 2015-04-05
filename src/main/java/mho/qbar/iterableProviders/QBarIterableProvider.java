package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.IterableProvider;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

public abstract class QBarIterableProvider extends IterableProvider {
    public QBarIterableProvider withRationalMeanBitSize(int rationalMeanBitSize) {
        return this;
    }

    public QBarIterableProvider withIntervalMeanBitSize(int intervalMeanBitSize) {
        return this;
    }

    public abstract @NotNull Iterable<Rational> rangeUp(@NotNull Rational a);
    public abstract @NotNull Iterable<Rational> rangeDown(@NotNull Rational a);
    public abstract @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b);
    public abstract @NotNull Iterable<Rational> rationals();
    public abstract @NotNull Iterable<Rational> nonNegativeRationals();
    public abstract @NotNull Iterable<Rational> positiveRationals();
    public abstract @NotNull Iterable<Rational> negativeRationals();
    public abstract @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne();
    public abstract @NotNull Iterable<Interval> finitelyBoundedIntervals();
    public abstract @NotNull Iterable<Interval> intervals();
    public abstract @NotNull Iterable<Byte> bytes(@NotNull Interval a);
    public abstract @NotNull Iterable<Short> shorts(@NotNull Interval a);
    public abstract @NotNull Iterable<Integer> integers(@NotNull Interval a);
    public abstract @NotNull Iterable<Long> longs(@NotNull Interval a);
    public abstract @NotNull Iterable<BigInteger> bigIntegers(@NotNull Interval a);
    public abstract @NotNull Iterable<Rational> rationals(@NotNull Interval a);
    public abstract @NotNull Iterable<Rational> rationalsNotIn(@NotNull Interval a);
    public abstract @NotNull Iterable<RationalVector> rationalVectors(int dimension);
    public abstract @NotNull Iterable<RationalVector> rationalVectorsAtLeast(int minDimension);
    public abstract @NotNull Iterable<RationalVector> rationalVectors();
    public abstract @NotNull Iterable<RationalVector> reducedRationalVectors(int dimension);
    public abstract @NotNull Iterable<RationalVector> reducedRationalVectorsAtLeast(int minDimension);
    public abstract @NotNull Iterable<RationalVector> reducedRationalVectors();
    public abstract @NotNull Iterable<RationalMatrix> rationalMatrices(int height, int width);
    public abstract @NotNull Iterable<RationalMatrix> rationalMatrices();
    public abstract @NotNull Iterable<Polynomial> polynomials(int degree);
    public abstract @NotNull Iterable<Polynomial> polynomialsAtLeast(int minDegree);
    public abstract @NotNull Iterable<Polynomial> polynomials();
    public abstract @NotNull Iterable<Polynomial> primitivePolynomials(int degree);
    public abstract @NotNull Iterable<Polynomial> primitivePolynomialsAtLeast(int minDegree);
    public abstract @NotNull Iterable<Polynomial> primitivePolynomials();
    public abstract @NotNull Iterable<RationalPolynomial> rationalPolynomials(int degree);
    public abstract @NotNull Iterable<RationalPolynomial> rationalPolynomialsAtLeast(int minDegree);
    public abstract @NotNull Iterable<RationalPolynomial> rationalPolynomials();
    public abstract @NotNull Iterable<RationalPolynomial> monicRationalPolynomials(int degree);
    public abstract @NotNull Iterable<RationalPolynomial> monicRationalPolynomialsAtLeast(int minDegree);
    public abstract @NotNull Iterable<RationalPolynomial> monicRationalPolynomials();
}
