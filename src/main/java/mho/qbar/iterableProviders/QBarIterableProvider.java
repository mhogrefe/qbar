package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.IterableProvider;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.map;

public abstract class QBarIterableProvider {
    private @NotNull IterableProvider wheelsProvider;

    protected QBarIterableProvider(@NotNull IterableProvider wheelsProvider) {
        this.wheelsProvider = wheelsProvider;
    }

    public @NotNull IterableProvider alt() {
        return wheelsProvider.alt();
    }

    public @NotNull IterableProvider withScale(int scale) {
        return wheelsProvider.withScale(scale);
    }

    public @NotNull IterableProvider withSecondaryScale(int secondaryScale) {
        return wheelsProvider.withSecondaryScale(secondaryScale);
    }

    public @NotNull Iterable<Boolean> booleans() {
        return wheelsProvider.booleans();
    }

    public @NotNull Iterable<Ordering> orderings() {
        return wheelsProvider.orderings();
    }

    public @NotNull Iterable<RoundingMode> roundingModes() {
        return wheelsProvider.roundingModes();
    }

    public @NotNull <T> Iterable<T> uniformSample(@NotNull List<T> xs) {
        return wheelsProvider.uniformSample(xs);
    }

    public @NotNull Iterable<Character> uniformSample(@NotNull String s) {
        return wheelsProvider.uniformSample(s);
    }

    public @NotNull Iterable<Integer> naturalIntegersGeometric() {
        return wheelsProvider.naturalIntegersGeometric();
    }

    public @NotNull Iterable<Integer> positiveIntegersGeometric() {
        return wheelsProvider.positiveIntegersGeometric();
    }

    public @NotNull Iterable<Integer> negativeIntegersGeometric() {
        return wheelsProvider.negativeIntegersGeometric();
    }

    public @NotNull Iterable<Integer> nonzeroIntegersGeometric() {
        return wheelsProvider.nonzeroIntegersGeometric();
    }

    public @NotNull Iterable<Integer> integersGeometric() {
        return wheelsProvider.integersGeometric();
    }

    public @NotNull Iterable<Integer> rangeUpGeometric(int a) {
        return wheelsProvider.rangeUpGeometric(a);
    }

    public @NotNull Iterable<Integer> rangeDownGeometric(int a) {
        return wheelsProvider.rangeDownGeometric(a);
    }

    public @NotNull Iterable<Byte> positiveBytes() {
        return wheelsProvider.positiveBytes();
    }

    public @NotNull Iterable<Short> positiveShorts() {
        return wheelsProvider.positiveShorts();
    }

    public @NotNull Iterable<Integer> positiveIntegers() {
        return wheelsProvider.positiveIntegers();
    }

    public @NotNull Iterable<Long> positiveLongs() {
        return wheelsProvider.positiveLongs();
    }

    public @NotNull Iterable<BigInteger> positiveBigIntegers() {
        return wheelsProvider.positiveBigIntegers();
    }

    public @NotNull Iterable<Byte> negativeBytes() {
        return wheelsProvider.negativeBytes();
    }

    public @NotNull Iterable<Short> negativeShorts() {
        return wheelsProvider.negativeShorts();
    }

    public @NotNull Iterable<Integer> negativeIntegers() {
        return wheelsProvider.negativeIntegers();
    }

    public @NotNull Iterable<Long> negativeLongs() {
        return wheelsProvider.negativeLongs();
    }

    public @NotNull Iterable<BigInteger> negativeBigIntegers() {
        return wheelsProvider.negativeBigIntegers();
    }

    public @NotNull Iterable<Byte> nonzeroBytes() {
        return wheelsProvider.nonzeroBytes();
    }

    public @NotNull Iterable<Short> nonzeroShorts() {
        return wheelsProvider.nonzeroShorts();
    }

    public @NotNull Iterable<Integer> nonzeroIntegers() {
        return wheelsProvider.nonzeroIntegers();
    }

    public @NotNull Iterable<Long> nonzeroLongs() {
        return wheelsProvider.nonzeroLongs();
    }

    public @NotNull Iterable<BigInteger> nonzeroBigIntegers() {
        return wheelsProvider.nonzeroBigIntegers();
    }

    public @NotNull Iterable<Byte> naturalBytes() {
        return wheelsProvider.naturalBytes();
    }

    public @NotNull Iterable<Short> naturalShorts() {
        return wheelsProvider.naturalShorts();
    }

    public @NotNull Iterable<Integer> naturalIntegers() {
        return wheelsProvider.naturalIntegers();
    }

    public @NotNull Iterable<Long> naturalLongs() {
        return wheelsProvider.naturalLongs();
    }

    public @NotNull Iterable<BigInteger> naturalBigIntegers() {
        return wheelsProvider.naturalBigIntegers();
    }

    public @NotNull Iterable<Byte> bytes() {
        return wheelsProvider.bytes();
    }

    public @NotNull Iterable<Short> shorts() {
        return wheelsProvider.shorts();
    }

    public @NotNull Iterable<Integer> integers() {
        return wheelsProvider.integers();
    }

    public @NotNull Iterable<Long> longs() {
        return wheelsProvider.longs();
    }

    public @NotNull Iterable<BigInteger> bigIntegers() {
        return wheelsProvider.bigIntegers();
    }

    public @NotNull Iterable<Character> asciiCharacters() {
        return wheelsProvider.asciiCharacters();
    }

    public @NotNull Iterable<Character> characters() {
        return wheelsProvider.characters();
    }

    public @NotNull Iterable<Byte> rangeUp(byte a) {
        return wheelsProvider.rangeUp(a);
    }

    public @NotNull Iterable<Short> rangeUp(short a) {
        return wheelsProvider.rangeUp(a);
    }

    public @NotNull Iterable<Integer> rangeUp(int a) {
        return wheelsProvider.rangeUp(a);
    }

    public @NotNull Iterable<Long> rangeUp(long a) {
        return wheelsProvider.rangeUp(a);
    }

    public @NotNull Iterable<BigInteger> rangeUp(@NotNull BigInteger a) {
        return wheelsProvider.rangeUp(a);
    }

    public @NotNull Iterable<Character> rangeUp(char a) {
        return wheelsProvider.rangeUp(a);
    }

    public @NotNull Iterable<Byte> rangeDown(byte a) {
        return wheelsProvider.rangeDown(a);
    }

    public @NotNull Iterable<Short> rangeDown(short a) {
        return wheelsProvider.rangeDown(a);
    }

    public @NotNull Iterable<Integer> rangeDown(int a) {
        return wheelsProvider.rangeDown(a);
    }

    public @NotNull Iterable<Long> rangeDown(long a) {
        return wheelsProvider.rangeDown(a);
    }

    public @NotNull Iterable<BigInteger> rangeDown(@NotNull BigInteger a) {
        return wheelsProvider.rangeDown(a);
    }

    public @NotNull Iterable<Character> rangeDown(char a) {
        return wheelsProvider.rangeDown(a);
    }

    public @NotNull Iterable<Byte> range(byte a, byte b) {
        return wheelsProvider.range(a, b);
    }

    public @NotNull Iterable<Short> range(short a, short b) {
        return wheelsProvider.range(a, b);
    }

    public @NotNull Iterable<Integer> range(int a, int b) {
        return wheelsProvider.range(a, b);
    }

    public @NotNull Iterable<Long> range(long a, long b) {
        return wheelsProvider.range(a, b);
    }

    public @NotNull Iterable<BigInteger> range(@NotNull BigInteger a, @NotNull BigInteger b) {
        return wheelsProvider.range(a, b);
    }

    public @NotNull Iterable<Character> range(char a, char b) {
        return wheelsProvider.range(a, b);
    }

    public @NotNull Iterable<Float> positiveOrdinaryFloats() {
        return wheelsProvider.positiveOrdinaryFloats();
    }

    public @NotNull Iterable<Float> negativeOrdinaryFloats() {
        return wheelsProvider.negativeOrdinaryFloats();
    }

    public @NotNull Iterable<Float> ordinaryFloats() {
        return wheelsProvider.ordinaryFloats();
    }

    public @NotNull Iterable<Float> floats() {
        return wheelsProvider.floats();
    }

    public @NotNull Iterable<Double> positiveOrdinaryDoubles() {
        return wheelsProvider.positiveOrdinaryDoubles();
    }

    public @NotNull Iterable<Double> negativeOrdinaryDoubles() {
        return wheelsProvider.negativeOrdinaryDoubles();
    }

    public @NotNull Iterable<Double> ordinaryDoubles() {
        return wheelsProvider.ordinaryDoubles();
    }

    public @NotNull Iterable<Double> doubles() {
        return wheelsProvider.doubles();
    }

    public @NotNull Iterable<BigDecimal> positiveBigDecimals() {
        return wheelsProvider.positiveBigDecimals();
    }

    public @NotNull Iterable<BigDecimal> negativeBigDecimals() {
        return wheelsProvider.negativeBigDecimals();
    }

    public @NotNull Iterable<BigDecimal> bigDecimals() {
        return wheelsProvider.bigDecimals();
    }

    public @NotNull <T> Iterable<T> withNull(@NotNull Iterable<T> xs) {
        return wheelsProvider.withNull(xs);
    }

    public @NotNull <T> Iterable<Optional<T>> optionals(@NotNull Iterable<T> xs) {
        return wheelsProvider.optionals(xs);
    }

    public @NotNull <T> Iterable<NullableOptional<T>> nullableOptionals(@NotNull Iterable<T> xs) {
        return wheelsProvider.nullableOptionals(xs);
    }

    public @NotNull <T> Iterable<Pair<T, T>> pairsLogarithmicOrder(@NotNull Iterable<T> xs) {
        return wheelsProvider.pairsLogarithmicOrder(xs);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> pairsLogarithmicOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return wheelsProvider.pairsLogarithmicOrder(as, bs);
    }

    public @NotNull <T> Iterable<Pair<T, T>> pairsSquareRootOrder(@NotNull Iterable<T> xs) {
        return wheelsProvider.pairsSquareRootOrder(xs);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> pairsSquareRootOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return wheelsProvider.pairsSquareRootOrder(as, bs);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairs(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return wheelsProvider.dependentPairs(xs, f);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsLogarithmic(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return wheelsProvider.dependentPairsLogarithmic(xs, f);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsSquareRoot(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return wheelsProvider.dependentPairsSquareRoot(xs, f);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsExponential(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return wheelsProvider.dependentPairsExponential(xs, f);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsSquare(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return wheelsProvider.dependentPairsSquare(xs, f);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> pairs(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        return wheelsProvider.pairs(as, bs);
    }

    public @NotNull <A, B, C> Iterable<Triple<A, B, C>> triples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs
    ) {
        return wheelsProvider.triples(as, bs, cs);
    }

    public @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds
    ) {
        return wheelsProvider.quadruples(as, bs, cs, ds);
    }

    public @NotNull <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> quintuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es
    ) {
        return wheelsProvider.quintuples(as, bs, cs, ds, es);
    }

    public @NotNull <A, B, C, D, E, F> Iterable<Sextuple<A, B, C, D, E, F>> sextuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es,
            @NotNull Iterable<F> fs
    ) {
        return wheelsProvider.sextuples(as, bs, cs, ds, es, fs);
    }

    public @NotNull <A, B, C, D, E, F, G> Iterable<Septuple<A, B, C, D, E, F, G>> septuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es,
            @NotNull Iterable<F> fs,
            @NotNull Iterable<G> gs
    ) {
        return wheelsProvider.septuples(as, bs, cs, ds, es, fs, gs);
    }

    public @NotNull <T> Iterable<Pair<T, T>> pairs(@NotNull Iterable<T> xs) {
        return wheelsProvider.pairs(xs);
    }

    public @NotNull <T> Iterable<Triple<T, T, T>> triples(@NotNull Iterable<T> xs) {
        return wheelsProvider.triples(xs);
    }

    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> quadruples(@NotNull Iterable<T> xs) {
        return wheelsProvider.quadruples(xs);
    }

    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> quintuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.quintuples(xs);
    }

    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> sextuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.sextuples(xs);
    }

    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> septuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.septuples(xs);
    }

    public @NotNull <T> Iterable<List<T>> lists(int size, @NotNull Iterable<T> xs) {
        return wheelsProvider.lists(size, xs);
    }

    public @NotNull <T> Iterable<List<T>> listsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return wheelsProvider.listsAtLeast(minSize, xs);
    }

    public @NotNull <T> Iterable<List<T>> lists(@NotNull Iterable<T> xs) {
        return wheelsProvider.lists(xs);
    }

    public @NotNull Iterable<String> strings(int size, @NotNull Iterable<Character> cs) {
        return wheelsProvider.strings(size, cs);
    }

    public @NotNull Iterable<String> stringsAtLeast(int minSize, @NotNull Iterable<Character> cs) {
        return wheelsProvider.stringsAtLeast(minSize, cs);
    }

    public @NotNull Iterable<String> strings(int size) {
        return wheelsProvider.strings(size);
    }

    public @NotNull Iterable<String> stringsAtLeast(int size) {
        return wheelsProvider.stringsAtLeast(size);
    }

    public @NotNull Iterable<String> strings(@NotNull Iterable<Character> cs) {
        return wheelsProvider.strings(cs);
    }

    public @NotNull Iterable<String> strings() {
        return wheelsProvider.strings();
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

    public @NotNull Iterable<QBarRandomProvider> qbarRandomProvidersFixedScales(int scale, int secondaryScale) {
        return map(
                rp -> new QBarRandomProvider(rp.getSeed()),
                wheelsProvider.randomProvidersFixedScales(scale, secondaryScale)
        );
    }

    public @NotNull Iterable<QBarRandomProvider> qbarRandomProviders() {
        return map(rp -> new QBarRandomProvider(rp.getSeed()), wheelsProvider.randomProviders());
    }

    public @NotNull Iterable<QBarRandomProvider> qbarRandomProvidersDefault() {
        return map(rp -> new QBarRandomProvider(rp.getSeed()), wheelsProvider.randomProvidersDefault());
    }

    public @NotNull Iterable<QBarRandomProvider> qbarRandomProvidersDefaultSecondaryScale() {
        return map(
                rp -> new QBarRandomProvider(rp.getSeed()),
                wheelsProvider.randomProvidersDefaultSecondaryScale()
        );
    }
}
