package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.IterableProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.BinaryFraction;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;

@SuppressWarnings("unused")
public strictfp abstract class QBarIterableProvider {
    protected @NotNull IterableProvider wheelsProvider;

    protected QBarIterableProvider(@NotNull IterableProvider wheelsProvider) {
        this.wheelsProvider = wheelsProvider;
    }

    public @NotNull IterableProvider getWheelsProvider() {
        return wheelsProvider;
    }

    /**
     * Returns a shallow copy of {@code this}.
     */
    public @NotNull QBarIterableProvider copy() {
        return this;
    }

    /**
     * Returns a deep copy of {@code this}.
     */
    public @NotNull QBarIterableProvider deepCopy() {
        return this;
    }

    /**
     * Resets {@code this} to its original state (if {@code this} has any state).
     */
    public void reset() {
        wheelsProvider.reset();
    }

    /**
     * Returns a shallow copy of {@code this} with a given primary scale.
     *
     * @param scale the primary scale.
     */
    public @NotNull QBarIterableProvider withScale(int scale) {
        return this;
    }

    /**
     * Returns a shallow copy of {@code this} with a given secondary scale.
     *
     * @param secondaryScale the secondary scale.
     */
    public @NotNull QBarIterableProvider withSecondaryScale(int secondaryScale) {
        return this;
    }

    /**
     * Generates {@code Boolean}s.
     */
    public @NotNull Iterable<Boolean> booleans() {
        return wheelsProvider.booleans();
    }

    /**
     * Generates {@link Ordering}s, in increasing order if applicable.
     */
    public @NotNull Iterable<Ordering> orderingsIncreasing() {
        return wheelsProvider.orderingsIncreasing();
    }

    /**
     * Generates {@link Ordering}s.
     */
    public @NotNull Iterable<Ordering> orderings() {
        return wheelsProvider.orderings();
    }

    /**
     * Generates {@code RoundingMode}s.
     */
    public @NotNull Iterable<RoundingMode> roundingModes() {
        return wheelsProvider.roundingModes();
    }

    /**
     * Generates values from a list, sampled uniformly if applicable.
     *
     * @param xs the source list
     */
    public @NotNull <T> Iterable<T> uniformSample(@NotNull List<T> xs) {
        return wheelsProvider.uniformSample(xs);
    }

    /**
     * Generates {@code Character}s from a {@code String}, sampled uniformly if applicable.
     *
     * @param s the source {@code String}
     */
    public @NotNull Iterable<Character> uniformSample(@NotNull String s) {
        return wheelsProvider.uniformSample(s);
    }

    /**
     * Generates {@code Byte}s, in increasing order if applicable.
     */
    public @NotNull Iterable<Byte> bytesIncreasing() {
        return wheelsProvider.bytesIncreasing();
    }

    /**
     * Generates {@code Short}s, in increasing order if applicable.
     */
    public @NotNull Iterable<Short> shortsIncreasing() {
        return wheelsProvider.shortsIncreasing();
    }

    /**
     * Generates {@code Integer}s, in increasing order if applicable.
     */
    public @NotNull Iterable<Integer> integersIncreasing() {
        return wheelsProvider.integersIncreasing();
    }

    /**
     * Generates {@code Long}s, in increasing order if applicable.
     */
    public @NotNull Iterable<Long> longsIncreasing() {
        return wheelsProvider.longsIncreasing();
    }

    /**
     * Generates positive {@code Byte}s.
     */
    public @NotNull Iterable<Byte> positiveBytes() {
        return wheelsProvider.positiveBytes();
    }

    /**
     * Generates positive {@code Short}s.
     */
    public @NotNull Iterable<Short> positiveShorts() {
        return wheelsProvider.positiveShorts();
    }

    /**
     * Generates positive {@code Integer}s.
     */
    public @NotNull Iterable<Integer> positiveIntegers() {
        return wheelsProvider.positiveIntegers();
    }

    /**
     * Generates positive {@code Long}s.
     */
    public @NotNull Iterable<Long> positiveLongs() {
        return wheelsProvider.positiveLongs();
    }

    /**
     * Generates positive {@code BigInteger}s.
     */
    public @NotNull Iterable<BigInteger> positiveBigIntegers() {
        return wheelsProvider.positiveBigIntegers();
    }

    /**
     * Generates negative {@code Byte}s.
     */
    public @NotNull Iterable<Byte> negativeBytes() {
        return wheelsProvider.negativeBytes();
    }

    /**
     * Generates negative {@code Short}s.
     */
    public @NotNull Iterable<Short> negativeShorts() {
        return wheelsProvider.negativeShorts();
    }

    /**
     * Generates negative {@code Integer}s.
     */
    public @NotNull Iterable<Integer> negativeIntegers() {
        return wheelsProvider.negativeIntegers();
    }

    /**
     * Generates negative {@code Long}s.
     */
    public @NotNull Iterable<Long> negativeLongs() {
        return wheelsProvider.negativeLongs();
    }

    /**
     * Generates negative {@code BigInteger}s.
     */
    public @NotNull Iterable<BigInteger> negativeBigIntegers() {
        return wheelsProvider.negativeBigIntegers();
    }

    /**
     * Generates nonzero {@code Byte}s.
     */
    public @NotNull Iterable<Byte> nonzeroBytes() {
        return wheelsProvider.nonzeroBytes();
    }

    /**
     * Generates nonzero {@code Short}s.
     */
    public @NotNull Iterable<Short> nonzeroShorts() {
        return wheelsProvider.nonzeroShorts();
    }

    /**
     * Generates nonzero {@code Integer}s.
     */
    public @NotNull Iterable<Integer> nonzeroIntegers() {
        return wheelsProvider.nonzeroIntegers();
    }

    /**
     * Generates nonzero {@code Long}s.
     */
    public @NotNull Iterable<Long> nonzeroLongs() {
        return wheelsProvider.nonzeroLongs();
    }

    /**
     * Generates nonzero {@code BigInteger}s.
     */
    public @NotNull Iterable<BigInteger> nonzeroBigIntegers() {
        return wheelsProvider.nonzeroBigIntegers();
    }

    /**
     * Generates natural {@code Byte}s (including 0).
     */
    public @NotNull Iterable<Byte> naturalBytes() {
        return wheelsProvider.naturalBytes();
    }

    /**
     * Generates natural {@code Short}s (including 0).
     */
    public @NotNull Iterable<Short> naturalShorts() {
        return wheelsProvider.naturalShorts();
    }

    /**
     * Generates natural {@code Integer}s (including 0).
     */
    public @NotNull Iterable<Integer> naturalIntegers() {
        return wheelsProvider.naturalIntegers();
    }

    /**
     * Generates natural {@code Long}s (including 0).
     */
    public @NotNull Iterable<Long> naturalLongs() {
        return wheelsProvider.naturalLongs();
    }

    /**
     * Generates natural {@code BigInteger}s (including 0).
     */
    public @NotNull Iterable<BigInteger> naturalBigIntegers() {
        return wheelsProvider.naturalBigIntegers();
    }

    /**
     * Generates {@code Byte}s.
     */
    public @NotNull Iterable<Byte> bytes() {
        return wheelsProvider.bytes();
    }

    /**
     * Generates {@code Short}s.
     */
    public @NotNull Iterable<Short> shorts() {
        return wheelsProvider.shorts();
    }

    /**
     * Generates {@code Integer}s.
     */
    public @NotNull Iterable<Integer> integers() {
        return wheelsProvider.integers();
    }

    /**
     * Generates {@code Long}s.
     */
    public @NotNull Iterable<Long> longs() {
        return wheelsProvider.longs();
    }

    /**
     * Generates {@code BigInteger}s.
     */
    public @NotNull Iterable<BigInteger> bigIntegers() {
        return wheelsProvider.bigIntegers();
    }

    /**
     * Generates ASCII {@code Character}s, in increasing order if applicable.
     */
    public @NotNull Iterable<Character> asciiCharactersIncreasing() {
        return wheelsProvider.asciiCharactersIncreasing();
    }

    /**
     * Generates ASCII {@code Character}s.
     */
    public @NotNull Iterable<Character> asciiCharacters() {
        return wheelsProvider.asciiCharacters();
    }

    /**
     * Generates {@code Character}s, in increasing order if applicable.
     */
    public @NotNull Iterable<Character> charactersIncreasing() {
        return wheelsProvider.charactersIncreasing();
    }

    /**
     * Generates {@code Character}s.
     */
    public @NotNull Iterable<Character> characters() {
        return wheelsProvider.characters();
    }

    /**
     * Generates positive {@code Integer}s from a geometric distribution (if applicable).
     */
    public @NotNull Iterable<Integer> positiveIntegersGeometric() {
        return wheelsProvider.positiveIntegersGeometric();
    }

    /**
     * Generates negative {@code Integer}s from a geometric distribution (if applicable).
     */
    public @NotNull Iterable<Integer> negativeIntegersGeometric() {
        return wheelsProvider.negativeIntegersGeometric();
    }

    /**
     * Generates nonzero {@code Integer}s from a geometric distribution (if applicable).
     */
    public @NotNull Iterable<Integer> nonzeroIntegersGeometric() {
        return wheelsProvider.nonzeroIntegersGeometric();
    }

    /**
     * Generates natural {@code Integer}s (including 0) from a geometric distribution (if applicable).
     */
    public @NotNull Iterable<Integer> naturalIntegersGeometric() {
        return wheelsProvider.naturalIntegersGeometric();
    }

    /**
     * Generates {@code Integer}s from a geometric distribution (if applicable).
     */
    public @NotNull Iterable<Integer> integersGeometric() {
        return wheelsProvider.integersGeometric();
    }

    /**
     * Generates positive {@code Integer}s greater than or equal to a given value from a geometric distribution (if
     * applicable).
     *
     * @param a the inclusive lower bound of the generated {@code Integer}s
     */
    public @NotNull Iterable<Integer> rangeUpGeometric(int a) {
        return wheelsProvider.rangeUpGeometric(a);
    }

    /**
     * Generates positive {@code Integer}s less than or equal to a given value from a geometric distribution (if
     * applicable).
     *
     * @param a the inclusive upper bound of the generated {@code Integer}s
     */
    public @NotNull Iterable<Integer> rangeDownGeometric(int a) {
        return wheelsProvider.rangeDownGeometric(a);
    }

    /**
     * Generates {@code Byte}s greater than or equal to a given value.
     *
     * @param a the inclusive lower bound of the generated {@code Byte}s
     */
    public @NotNull Iterable<Byte> rangeUp(byte a) {
        return wheelsProvider.rangeUp(a);
    }

    /**
     * Generates {@code Short}s greater than or equal to a given value.
     *
     * @param a the inclusive lower bound of the generated {@code Short}s
     */
    public @NotNull Iterable<Short> rangeUp(short a) {
        return wheelsProvider.rangeUp(a);
    }

    /**
     * Generates {@code Integer}s greater than or equal to a given value.
     *
     * @param a the inclusive lower bound of the generated {@code Integer}s
     */
    public @NotNull Iterable<Integer> rangeUp(int a) {
        return wheelsProvider.rangeUp(a);
    }

    /**
     * Generates {@code Long}s greater than or equal to a given value.
     *
     * @param a the inclusive lower bound of the generated {@code Long}s
     */
    public @NotNull Iterable<Long> rangeUp(long a) {
        return wheelsProvider.rangeUp(a);
    }

    /**
     * Generates {@code BigInteger}s greater than or equal to a given value.
     *
     * @param a the inclusive lower bound of the generated {@code BigInteger}s
     */
    public @NotNull Iterable<BigInteger> rangeUp(@NotNull BigInteger a) {
        return wheelsProvider.rangeUp(a);
    }

    /**
     * Generates {@code Character}s greater than or equal to a given value.
     *
     * @param a the inclusive lower bound of the generated {@code Character}s
     */
    public @NotNull Iterable<Character> rangeUp(char a) {
        return wheelsProvider.rangeUp(a);
    }

    /**
     * Generates {@code Byte}s less than or equal to a given value.
     *
     * @param a the inclusive upper bound of the generated {@code Byte}s
     */
    public @NotNull Iterable<Byte> rangeDown(byte a) {
        return wheelsProvider.rangeDown(a);
    }

    /**
     * Generates {@code Short}s less than or equal to a given value.
     *
     * @param a the inclusive upper bound of the generated {@code Short}s
     */
    public @NotNull Iterable<Short> rangeDown(short a) {
        return wheelsProvider.rangeDown(a);
    }

    /**
     * Generates {@code Integer}s less than or equal to a given value.
     *
     * @param a the inclusive upper bound of the generated {@code Integer}s
     */
    public @NotNull Iterable<Integer> rangeDown(int a) {
        return wheelsProvider.rangeDown(a);
    }

    /**
     * Generates {@code Long}s less than or equal to a given value.
     *
     * @param a the inclusive upper bound of the generated {@code Long}s
     */
    public @NotNull Iterable<Long> rangeDown(long a) {
        return wheelsProvider.rangeDown(a);
    }

    /**
     * Generates {@code BigInteger}s less than or equal to a given value.
     *
     * @param a the inclusive upper bound of the generated {@code BigInteger}s
     */
    public @NotNull Iterable<BigInteger> rangeDown(@NotNull BigInteger a) {
        return wheelsProvider.rangeDown(a);
    }

    /**
     * Generates {@code Character}s less than or equal to a given value.
     *
     * @param a the inclusive upper bound of the generated {@code Character}s
     */
    public @NotNull Iterable<Character> rangeDown(char a) {
        return wheelsProvider.rangeDown(a);
    }

    /**
     * Generates {@code Byte}s between {@code a} and {@code b}, inclusive.
     *
     * @param a the inclusive lower bound of the generated {@code Byte}s
     * @param b the inclusive upper bound of the generated {@code Byte}s
     */
    public @NotNull Iterable<Byte> range(byte a, byte b) {
        return wheelsProvider.range(a, b);
    }

    /**
     * Generates {@code Short}s between {@code a} and {@code b}, inclusive.
     *
     * @param a the inclusive lower bound of the generated {@code Short}s
     * @param b the inclusive upper bound of the generated {@code Short}s
     */
    public @NotNull Iterable<Short> range(short a, short b) {
        return wheelsProvider.range(a, b);
    }

    /**
     * Generates {@code Integer}s between {@code a} and {@code b}, inclusive.
     *
     * @param a the inclusive lower bound of the generated {@code Integer}s
     * @param b the inclusive upper bound of the generated {@code Integer}s
     */
    public @NotNull Iterable<Integer> range(int a, int b) {
        return wheelsProvider.range(a, b);
    }

    /**
     * Generates {@code Long}s between {@code a} and {@code b}, inclusive.
     *
     * @param a the inclusive lower bound of the generated {@code Long}s
     * @param b the inclusive upper bound of the generated {@code Long}s
     */
    public @NotNull Iterable<Long> range(long a, long b) {
        return wheelsProvider.range(a, b);
    }

    /**
     * Generates {@code BigInteger}s between {@code a} and {@code b}, inclusive.
     *
     * @param a the inclusive lower bound of the generated {@code BigInteger}s
     * @param b the inclusive upper bound of the generated {@code BigInteger}s
     */
    public @NotNull Iterable<BigInteger> range(@NotNull BigInteger a, @NotNull BigInteger b) {
        return wheelsProvider.range(a, b);
    }

    /**
     * Generates {@code Character}s between {@code a} and {@code b}, inclusive.
     *
     * @param a the inclusive lower bound of the generated {@code Character}s
     * @param b the inclusive upper bound of the generated {@code Character}s
     */
    public @NotNull Iterable<Character> range(char a, char b) {
        return wheelsProvider.range(a, b);
    }

    /**
     * Generates positive {@link BinaryFraction}s.
     */
    public @NotNull Iterable<BinaryFraction> positiveBinaryFractions() {
        return wheelsProvider.positiveBinaryFractions();
    }

    /**
     * Generates negative {@link BinaryFraction}s.
     */
    public @NotNull Iterable<BinaryFraction> negativeBinaryFractions() {
        return wheelsProvider.negativeBinaryFractions();
    }

    /**
     * Generates nonzero {@link BinaryFraction}s.
     */
    public @NotNull Iterable<BinaryFraction> nonzeroBinaryFractions() {
        return wheelsProvider.nonzeroBinaryFractions();
    }

    /**
     * Generates {@link BinaryFraction}s.
     */
    public @NotNull Iterable<BinaryFraction> binaryFractions() {
        return wheelsProvider.binaryFractions();
    }

    /**
     * Generates {@code BinaryFraction}s greater than or equal to a given value.
     *
     * @param a the inclusive lower bound of the generated {@code BinaryFraction}s
     */
    public @NotNull Iterable<BinaryFraction> rangeUp(@NotNull BinaryFraction a) {
        return wheelsProvider.rangeUp(a);
    }

    /**
     * Generates {@code BinaryFraction}s less than or equal to a given value.
     *
     * @param a the inclusive upper bound of the generated {@code BinaryFraction}s
     */
    public @NotNull Iterable<BinaryFraction> rangeDown(@NotNull BinaryFraction a) {
        return wheelsProvider.rangeDown(a);
    }

    /**
     * Generates {@link BinaryFraction}s between {@code a} and {@code b}, inclusive.
     *
     * @param a the inclusive lower bound of the generated {@code BinaryFraction}s
     * @param b the inclusive upper bound of the generated {@code BinaryFraction}s
     */
    public @NotNull Iterable<BinaryFraction> range(@NotNull BinaryFraction a, @NotNull BinaryFraction b) {
        return wheelsProvider.range(a, b);
    }

    /**
     * Generates positive {@code Float}s, including {@code Infinity} but not positive zero.
     */
    public @NotNull Iterable<Float> positiveFloats() {
        return wheelsProvider.positiveFloats();
    }

    /**
     * Generates negative {@code Float}s, including {@code -Infinity} but not negative zero.
     */
    public @NotNull Iterable<Float> negativeFloats() {
        return wheelsProvider.negativeFloats();
    }

    /**
     * Generates nonzero {@code Float}s, including {@code NaN}, positive and negative zeros, {@code Infinity}, and
     * {@code -Infinity}.
     */
    public @NotNull Iterable<Float> nonzeroFloats() {
        return wheelsProvider.nonzeroFloats();
    }

    /**
     * Generates {@code Float}s.
     */
    public @NotNull Iterable<Float> floats() {
        return wheelsProvider.floats();
    }

    /**
     * Generates positive {@code Double}s, including {@code Infinity} but not positive zero.
     */
    public @NotNull Iterable<Double> positiveDoubles() {
        return wheelsProvider.positiveDoubles();
    }

    /**
     * Generates negative {@code Double}s, including {@code -Infinity} but not negative zero.
     */
    public @NotNull Iterable<Double> negativeDoubles() {
        return wheelsProvider.negativeDoubles();
    }

    /**
     * Generates nonzero {@code Double}s, including {@code NaN}, positive and negative zeros, {@code Infinity}, and
     * {@code -Infinity}.
     */
    public @NotNull Iterable<Double> nonzeroDoubles() {
        return wheelsProvider.nonzeroDoubles();
    }

    /**
     * Generates {@code Double}s, including {@code NaN}, positive and negative zeros, {@code Infinity}, and
     * {@code -Infinity}.
     */
    public @NotNull Iterable<Double> doubles() {
        return wheelsProvider.doubles();
    }

    /**
     * Generates positive {@code Float}s selected from a distribution which approximates a uniform distribution over
     * the reals (if applicable).
     */
    public @NotNull Iterable<Float> positiveFloatsUniform() {
        return wheelsProvider.positiveFloatsUniform();
    }

    /**
     * Generates negative {@code Float}s selected from a distribution which approximates a uniform distribution over
     * the reals (if applicable).
     */
    public @NotNull Iterable<Float> negativeFloatsUniform() {
        return wheelsProvider.negativeFloatsUniform();
    }

    /**
     * Generates nonzero {@code Float}s selected from a distribution which approximates a uniform distribution over
     * the reals (if applicable).
     */
    public @NotNull Iterable<Float> nonzeroFloatsUniform() {
        return wheelsProvider.nonzeroFloatsUniform();
    }

    /**
     * Generates {@code Float}s selected from a distribution which approximates a uniform distribution over the reals
     * (if applicable).
     */
    public @NotNull Iterable<Float> floatsUniform() {
        return wheelsProvider.floatsUniform();
    }

    /**
     * Generates positive {@code Double}s selected from a distribution which approximates a uniform distribution over
     * the reals (if applicable).
     */
    public @NotNull Iterable<Double> positiveDoublesUniform() {
        return wheelsProvider.positiveDoublesUniform();
    }

    /**
     * Generates negative {@code Double}s selected from a distribution which approximates a uniform distribution over
     * the reals (if applicable).
     */
    public @NotNull Iterable<Double> negativeDoublesUniform() {
        return wheelsProvider.negativeDoublesUniform();
    }

    /**
     * Generates nonzero {@code Double}s selected from a distribution which approximates a uniform distribution over
     * the reals (if applicable).
     */
    public @NotNull Iterable<Double> nonzeroDoublesUniform() {
        return wheelsProvider.nonzeroDoublesUniform();
    }

    /**
     * Generates {@code Double}s selected from a distribution which approximates a uniform distribution over the reals
     * (if applicable).
     */
    public @NotNull Iterable<Double> doublesUniform() {
        return wheelsProvider.doublesUniform();
    }

    /**
     * Generates positive {@code BigDecimal}s.
     */
    public @NotNull Iterable<BigDecimal> positiveBigDecimals() {
        return wheelsProvider.positiveBigDecimals();
    }

    /**
     * Generates negative {@code BigDecimal}s.
     */
    public @NotNull Iterable<BigDecimal> negativeBigDecimals() {
        return wheelsProvider.negativeBigDecimals();
    }

    /**
     * Generates nonzero {@code BigDecimal}s.
     */
    public @NotNull Iterable<BigDecimal> nonzeroBigDecimals() {
        return wheelsProvider.nonzeroBigDecimals();
    }

    /**
     * Generates {@code BigDecimal}s.
     */
    public @NotNull Iterable<BigDecimal> bigDecimals() {
        return wheelsProvider.bigDecimals();
    }

    /**
     * Generates positive {@code BigDecimal}s in canonical form (see
     * {@link mho.wheels.numberUtils.BigDecimalUtils#canonicalize(BigDecimal)}).
     */
    public @NotNull Iterable<BigDecimal> positiveCanonicalBigDecimals() {
        return wheelsProvider.positiveCanonicalBigDecimals();
    }

    /**
     * Generates negative {@code BigDecimal}s in canonical form (see
     * {@link mho.wheels.numberUtils.BigDecimalUtils#canonicalize(BigDecimal)}).
     */
    public @NotNull Iterable<BigDecimal> negativeCanonicalBigDecimals() {
        return wheelsProvider.negativeCanonicalBigDecimals();
    }

    /**
     * Generates nonzero {@code BigDecimal}s in canonical form (see
     * {@link mho.wheels.numberUtils.BigDecimalUtils#canonicalize(BigDecimal)}).
     */
    public @NotNull Iterable<BigDecimal> nonzeroCanonicalBigDecimals() {
        return wheelsProvider.nonzeroCanonicalBigDecimals();
    }

    /**
     * Generates {@code BigDecimal}s in canonical form (see
     * {@link mho.wheels.numberUtils.BigDecimalUtils#canonicalize(BigDecimal)}).
     */
    public @NotNull Iterable<BigDecimal> canonicalBigDecimals() {
        return wheelsProvider.canonicalBigDecimals();
    }

    /**
     * Generates {@code BigDecimal}s greater than or equal to a given value.
     *
     * @param a the inclusive lower bound of the generated {@code BigDecimal}s
     */
    public @NotNull Iterable<BigDecimal> rangeUp(@NotNull BigDecimal a) {
        return wheelsProvider.rangeUp(a);
    }

    /**
     * Generates {@code BigDecimal}s less than or equal to a given value.
     *
     * @param a the inclusive upper bound of the generated {@code BigDecimal}s
     */
    public @NotNull Iterable<BigDecimal> rangeDown(@NotNull BigDecimal a) {
        return wheelsProvider.rangeDown(a);
    }

    /**
     * Generates {@code BigDecimal}s between {@code a} and {@code b}, inclusive.
     *
     * @param a the inclusive lower bound of the generated {@code BigDecimal}s
     * @param b the inclusive upper bound of the generated {@code BigDecimal}s
     */
    public @NotNull Iterable<BigDecimal> range(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        return wheelsProvider.range(a, b);
    }

    /**
     * Generates {@code BigDecimal}s greater than or equal to a given value, in canonical form (see
     * {@link mho.wheels.numberUtils.BigDecimalUtils#canonicalize(BigDecimal)}).
     *
     * @param a the inclusive lower bound of the generated {@code BigDecimal}s
     */
    public @NotNull Iterable<BigDecimal> rangeUpCanonical(@NotNull BigDecimal a) {
        return wheelsProvider.rangeUpCanonical(a);
    }

    /**
     * Generates {@code BigDecimal}s less than or equal to a given value, in canonical form (see
     * {@link mho.wheels.numberUtils.BigDecimalUtils#canonicalize(BigDecimal)}).
     *
     * @param a the inclusive upper bound of the generated {@code BigDecimal}s
     */
    public @NotNull Iterable<BigDecimal> rangeDownCanonical(@NotNull BigDecimal a) {
        return wheelsProvider.rangeDownCanonical(a);
    }

    /**
     * Generates {@code BigDecimal}s between {@code a} and {@code b}, inclusive, in canonical form (see
     * {@link mho.wheels.numberUtils.BigDecimalUtils#canonicalize(BigDecimal)}).
     *
     * @param a the inclusive lower bound of the generated {@code BigDecimal}s
     * @param b the inclusive upper bound of the generated {@code BigDecimal}s
     */
    public @NotNull Iterable<BigDecimal> rangeCanonical(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        return wheelsProvider.rangeCanonical(a, b);
    }

    public @NotNull <T> Iterable<T> withElement(@Nullable T x, @NotNull Iterable<T> xs) {
        return wheelsProvider.withElement(x, xs);
    }

    public @NotNull <T> Iterable<T> withNull(@NotNull Iterable<T> xs) {
        return wheelsProvider.withNull(xs);
    }

    public @NotNull <T> Iterable<Optional<T>> nonEmptyOptionals(@NotNull Iterable<T> xs) {
        return wheelsProvider.nonEmptyOptionals(xs);
    }

    public @NotNull <T> Iterable<Optional<T>> optionals(@NotNull Iterable<T> xs) {
        return wheelsProvider.optionals(xs);
    }

    public @NotNull <T> Iterable<NullableOptional<T>> nonEmptyNullableOptionals(@NotNull Iterable<T> xs) {
        return wheelsProvider.nonEmptyNullableOptionals(xs);
    }

    public @NotNull <T> Iterable<NullableOptional<T>> nullableOptionals(@NotNull Iterable<T> xs) {
        return wheelsProvider.nullableOptionals(xs);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairs(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return wheelsProvider.dependentPairs(xs, f);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsInfinite(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return wheelsProvider.dependentPairsInfinite(xs, f);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> pairsLogarithmicOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return wheelsProvider.pairsLogarithmicOrder(as, bs);
    }

    public @NotNull <T> Iterable<Pair<T, T>> pairsLogarithmicOrder(@NotNull Iterable<T> xs) {
        return wheelsProvider.pairsLogarithmicOrder(xs);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> pairsSquareRootOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return wheelsProvider.pairsSquareRootOrder(as, bs);
    }

    public @NotNull <T> Iterable<Pair<T, T>> pairsSquareRootOrder(@NotNull Iterable<T> xs) {
        return wheelsProvider.pairsSquareRootOrder(xs);
    }

    public @NotNull <T> Iterable<List<T>> permutations(@NotNull List<T> xs) {
        return wheelsProvider.permutations(xs);
    }

    public @NotNull Iterable<String> stringPermutations(@NotNull String s) {
        return wheelsProvider.stringPermutations(s);
    }

    public @NotNull <T> Iterable<List<T>> lists(int size, @NotNull Iterable<T> xs) {
        return wheelsProvider.lists(size, xs);
    }

    public @NotNull <A, B> Iterable<Pair<A, B>> pairs(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        return wheelsProvider.pairs(as, bs);
    }

    public @NotNull <T> Iterable<Pair<T, T>> pairs(@NotNull Iterable<T> xs) {
        return wheelsProvider.pairs(xs);
    }

    public @NotNull <A, B, C> Iterable<Triple<A, B, C>> triples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs
    ) {
        return wheelsProvider.triples(as, bs, cs);
    }

    public @NotNull <T> Iterable<Triple<T, T, T>> triples(@NotNull Iterable<T> xs) {
        return wheelsProvider.triples(xs);
    }

    public @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds
    ) {
        return wheelsProvider.quadruples(as, bs, cs, ds);
    }

    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> quadruples(@NotNull Iterable<T> xs) {
        return wheelsProvider.quadruples(xs);
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

    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> quintuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.quintuples(xs);
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

    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> sextuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.sextuples(xs);
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

    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> septuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.septuples(xs);
    }

    public @NotNull Iterable<String> strings(int size, @NotNull String s) {
        return wheelsProvider.strings(size, s);
    }

    public @NotNull Iterable<String> strings(int size) {
        return wheelsProvider.strings(size);
    }

    public @NotNull <T> Iterable<List<T>> lists(@NotNull Iterable<T> xs) {
        return wheelsProvider.lists(xs);
    }

    public @NotNull Iterable<String> strings(@NotNull String s) {
        return wheelsProvider.strings(s);
    }

    public @NotNull Iterable<String> strings() {
        return wheelsProvider.strings();
    }

    public @NotNull <T> Iterable<List<T>> listsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return wheelsProvider.listsAtLeast(minSize, xs);
    }

    public @NotNull Iterable<String> stringsAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.stringsAtLeast(minSize, s);
    }

    public @NotNull Iterable<String> stringsAtLeast(int size) {
        return wheelsProvider.stringsAtLeast(size);
    }

    public @NotNull <T> Iterable<List<T>> distinctLists(int size, @NotNull Iterable<T> xs) {
        return wheelsProvider.distinctLists(size, xs);
    }

    public @NotNull <T> Iterable<Pair<T, T>> distinctPairs(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctPairs(xs);
    }

    public @NotNull <T> Iterable<Triple<T, T, T>> distinctTriples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctTriples(xs);
    }

    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> distinctQuadruples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctQuadruples(xs);
    }

    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> distinctQuintuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctQuintuples(xs);
    }

    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> distinctSextuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctSextuples(xs);
    }

    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> distinctSeptuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctSeptuples(xs);
    }

    public @NotNull Iterable<String> distinctStrings(int size, @NotNull String s) {
        return wheelsProvider.distinctStrings(size, s);
    }

    public @NotNull Iterable<String> distinctStrings(int size) {
        return wheelsProvider.distinctStrings(size);
    }

    public @NotNull <T> Iterable<List<T>> distinctLists(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctLists(xs);
    }

    public @NotNull Iterable<String> distinctStrings(@NotNull String s) {
        return wheelsProvider.distinctStrings(s);
    }

    public @NotNull Iterable<String> distinctStrings() {
        return wheelsProvider.distinctStrings();
    }

    public @NotNull <T> Iterable<List<T>> distinctListsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return wheelsProvider.distinctListsAtLeast(minSize, xs);
    }

    public @NotNull Iterable<String> distinctStringsAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.distinctStringsAtLeast(minSize, s);
    }

    public @NotNull Iterable<String> distinctStringsAtLeast(int minSize) {
        return wheelsProvider.distinctStringsAtLeast(minSize);
    }

    public @NotNull <T> Iterable<List<T>> bags(int size, @NotNull Iterable<T> xs) {
        return wheelsProvider.bags(size, xs);
    }

    public @NotNull Iterable<String> stringBags(int size, @NotNull String s) {
        return wheelsProvider.stringBags(size, s);
    }

    public @NotNull Iterable<String> stringBags(int size) {
        return wheelsProvider.stringBags(size);
    }

    public @NotNull <T> Iterable<List<T>> bags(@NotNull Iterable<T> xs) {
        return wheelsProvider.bags(xs);
    }

    public @NotNull Iterable<String> stringBags(@NotNull String s) {
        return wheelsProvider.stringBags(s);
    }

    public @NotNull Iterable<String> stringBags() {
        return wheelsProvider.stringBags();
    }

    public @NotNull <T> Iterable<List<T>> bagsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return wheelsProvider.bagsAtLeast(minSize, xs);
    }

    public @NotNull Iterable<String> stringBagsAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.stringBagsAtLeast(minSize, s);
    }

    public @NotNull Iterable<String> stringBagsAtLeast(int minSize) {
        return wheelsProvider.stringBagsAtLeast(minSize);
    }

    public @NotNull <T> Iterable<List<T>> subsets(int size, @NotNull Iterable<T> xs) {
        return wheelsProvider.subsets(size, xs);
    }

    public @NotNull Iterable<String> stringSubsets(int size, @NotNull String s) {
        return wheelsProvider.stringSubsets(size, s);
    }

    public @NotNull Iterable<String> stringSubsets(int size) {
        return wheelsProvider.stringSubsets(size);
    }

    public @NotNull <T> Iterable<List<T>> subsets(@NotNull Iterable<T> xs) {
        return wheelsProvider.subsets(xs);
    }

    public @NotNull <T> Iterable<List<T>> subsetsLimited(int maxSize, @NotNull Iterable<T> xs) {
        return wheelsProvider.subsetsLimited(maxSize, xs);
    }

    public @NotNull <T> Iterable<List<T>> subsetsUniform(@NotNull List<T> xs) {
        return wheelsProvider.subsetsUniform(xs);
    }

    public @NotNull Iterable<String> stringSubsets(@NotNull String s) {
        return wheelsProvider.stringSubsets(s);
    }

    public @NotNull Iterable<String> stringSubsets() {
        return wheelsProvider.stringSubsets();
    }

    public @NotNull <T> Iterable<List<T>> subsetsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return wheelsProvider.subsetsAtLeast(minSize, xs);
    }

    public @NotNull Iterable<String> stringSubsetsAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.stringSubsetsAtLeast(minSize, s);
    }

    public @NotNull Iterable<String> stringSubsetsAtLeast(int minSize) {
        return wheelsProvider.stringSubsetsAtLeast(minSize);
    }

    public @NotNull Iterable<String> stringsWithChar(char c, @NotNull String s) {
        return wheelsProvider.stringsWithChar(c, s);
    }

    public @NotNull Iterable<String> stringsWithChar(char c) {
        return wheelsProvider.stringsWithChar(c);
    }

    public @NotNull Iterable<String> stringsWithSubstrings(@NotNull Iterable<String> substrings, @NotNull String s) {
        return wheelsProvider.stringsWithSubstrings(substrings, s);
    }

    public @NotNull Iterable<String> stringsWithSubstrings(@NotNull Iterable<String> substrings) {
        return wheelsProvider.stringsWithSubstrings(substrings);
    }

    public @NotNull <T> Iterable<Iterable<T>> repeatingIterables(@NotNull Iterable<T> xs) {
        return wheelsProvider.repeatingIterables(xs);
    }

    public @NotNull <T> Iterable<List<T>> listsWithElement(@Nullable T element, Iterable<T> xs) {
        return wheelsProvider.listsWithElement(element, xs);
    }
    public @NotNull <T> Iterable<List<T>> listsWithSubsequence(
            @NotNull Iterable<Iterable<T>> subsequences,
            @NotNull Iterable<T> xs
    ) {
        return wheelsProvider.listsWithSubsequence(subsequences, xs);
    }

    public @NotNull Iterable<RandomProvider> randomProvidersFixedScales(int scale, int secondaryScale) {
        return wheelsProvider.randomProvidersFixedScales(scale, secondaryScale);
    }

    public @NotNull Iterable<RandomProvider> randomProviders() {
        return wheelsProvider.randomProviders();
    }

    public @NotNull Iterable<RandomProvider> randomProvidersDefault() {
        return wheelsProvider.randomProvidersDefault();
    }

    public @NotNull Iterable<RandomProvider> randomProvidersDefaultSecondaryScale() {
        return wheelsProvider.randomProvidersDefaultSecondaryScale();
    }

    public abstract @NotNull Iterable<Rational> rationals();
    public abstract @NotNull Iterable<Rational> nonNegativeRationals();
    public abstract @NotNull Iterable<Rational> positiveRationals();
    public abstract @NotNull Iterable<Rational> negativeRationals();
    public abstract @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne();
    public abstract @NotNull Iterable<Rational> rangeUp(@NotNull Rational a);
    public abstract @NotNull Iterable<Rational> rangeDown(@NotNull Rational a);
    public abstract @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b);
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
