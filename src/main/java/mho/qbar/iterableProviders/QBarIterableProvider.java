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
import java.util.Map;
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

    /**
     * Generates all the elements in a given {@code Iterable}, along with an extra element.
     *
     * @param x an extra element
     * @param xs an {@code Iterable}
     * @param <T> the type of element contained in {@code xs}
     */
    public @NotNull <T> Iterable<T> withElement(@Nullable T x, @NotNull Iterable<T> xs) {
        return wheelsProvider.withElement(x, xs);
    }

    /**
     * Generates all the elements in a given {@code Iterable}, along with null.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of element contained in {@code xs}
     * @return an {@code Iterable} including null and every element in {@code xs}
     */
    public @NotNull <T> Iterable<T> withNull(@NotNull Iterable<T> xs) {
        return wheelsProvider.withNull(xs);
    }

    /**
     * Generates nonempty {@link Optional}s; the result contains wrapped values of {@code xs}.
     *
     * @param xs an {@code Iterable}.
     * @param <T> the type of element contained in {@code xs}
     */
    public @NotNull <T> Iterable<Optional<T>> nonEmptyOptionals(@NotNull Iterable<T> xs) {
        return wheelsProvider.nonEmptyOptionals(xs);
    }

    /**
     * Generates {@link Optional}s; the result contains wrapped values of {@code xs}, along with the empty
     * {@code Optional}.
     *
     * @param xs an {@code Iterable}.
     * @param <T> the type of element contained in {@code xs}
     */
    public @NotNull <T> Iterable<Optional<T>> optionals(@NotNull Iterable<T> xs) {
        return wheelsProvider.optionals(xs);
    }

    /**
     * Generates nonempty {@link NullableOptional}s; the result contains wrapped values of {@code xs}.
     *
     * @param xs an {@code Iterable}.
     * @param <T> the type of element contained in {@code xs}
     */
    public @NotNull <T> Iterable<NullableOptional<T>> nonEmptyNullableOptionals(@NotNull Iterable<T> xs) {
        return wheelsProvider.nonEmptyNullableOptionals(xs);
    }

    /**
     * Generates {@link NullableOptional}s; the result contains wrapped values of {@code xs}, along with the empty
     * {@code NullableOptional}.
     *
     * @param xs an {@code Iterable}.
     * @param <T> the type of element contained in {@code xs}
     */
    public @NotNull <T> Iterable<NullableOptional<T>> nullableOptionals(@NotNull Iterable<T> xs) {
        return wheelsProvider.nullableOptionals(xs);
    }

    /**
     * Generates pairs of values where the second value depends on the first.
     *
     * @param xs an {@code Iterable} of values
     * @param f a function from a value of type {@code a} to an {@code Iterable} of type-{@code B} values
     * @param <A> the type of values in the first slot
     * @param <B> the type of values in the second slot
     */
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairs(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return wheelsProvider.dependentPairs(xs, f);
    }

    /**
     * Generates pairs of values where the second value depends on the first. There must be an infinite number of
     * possible first values, and every first value must be associated with an infinite number of possible second
     * values.
     *
     * @param xs an {@code Iterable} of values
     * @param f a function from a value of type {@code a} to an infinite {@code Iterable} of type-{@code B} values
     * @param <A> the type of values in the first slot
     * @param <B> the type of values in the second slot
     */
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsInfinite(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return wheelsProvider.dependentPairsInfinite(xs, f);
    }

    /**
     * Generates pairs of values where the second value depends on the first and the second value grows linearly, but
     * the first grows logarithmically (if applicable). There must be an infinite number of possible first values, and
     * every first value must be associated with an infinite number of possible second values.
     *
     * @param xs an {@code Iterable} of values
     * @param f a function from a value of type {@code a} to an infinite {@code Iterable} of type-{@code B} values
     * @param <A> the type of values in the first slot
     * @param <B> the type of values in the second slot
     */
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsInfiniteLogarithmicOrder(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return wheelsProvider.dependentPairsInfiniteLogarithmicOrder(xs, f);
    }

    /**
     * Generates pairs of values where the second value depends on the first and the second value grows as
     * O(n<sup>2/3</sup>), but the first grows as O(n<sup>1/3</sup>) (if applicable). There must be an infinite number
     * of possible first values, and every first value must be associated with an infinite number of possible second
     * values.
     *
     * @param xs an {@code Iterable} of values
     * @param f a function from a value of type {@code a} to an infinite {@code Iterable} of type-{@code B} values
     * @param <A> the type of values in the first slot
     * @param <B> the type of values in the second slot
     */
    public @NotNull <A, B> Iterable<Pair<A, B>> dependentPairsInfiniteSquareRootOrder(
            @NotNull Iterable<A> xs,
            @NotNull Function<A, Iterable<B>> f
    ) {
        return wheelsProvider.dependentPairsInfiniteSquareRootOrder(xs, f);
    }

    /**
     * Generates pairs of elements where the first component grows linearly but the second grows logarithmically (if
     * applicable).
     *
     * @param as the source of values in the first slot
     * @param bs the source of values in the second slot
     * @param <A> the type of values in the first slot
     * @param <B> the type of values in the second slot
     */
    public @NotNull <A, B> Iterable<Pair<A, B>> pairsLogarithmicOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return wheelsProvider.pairsLogarithmicOrder(as, bs);
    }

    /**
     * Generates pairs of elements where the first component grows linearly but the second grows logarithmically (if
     * applicable).
     *
     * @param xs the source of values
     * @param <T> the type of values in the both slots of the result pairs
     */
    public @NotNull <T> Iterable<Pair<T, T>> pairsLogarithmicOrder(@NotNull Iterable<T> xs) {
        return wheelsProvider.pairsLogarithmicOrder(xs);
    }

    /**
     * Generates pairs of elements where the first component grows as O(n<sup>2/3</sup>) but the second grows as
     * O(n<sup>1/3</sup>) (if applicable).
     *
     * @param as the source of values in the first slot
     * @param bs the source of values in the second slot
     * @param <A> the type of values in the first slot
     * @param <B> the type of values in the second slot
     */
    public @NotNull <A, B> Iterable<Pair<A, B>> pairsSquareRootOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return wheelsProvider.pairsSquareRootOrder(as, bs);
    }

    /**
     * Generates pairs of elements where the first component grows as O(n<sup>2/3</sup>) but the second grows as
     * O(n<sup>1/3</sup>) (if applicable).
     *
     * @param xs the source of values
     * @param <T> the type of values in the both slots of the result pairs
     */
    public @NotNull <T> Iterable<Pair<T, T>> pairsSquareRootOrder(@NotNull Iterable<T> xs) {
        return wheelsProvider.pairsSquareRootOrder(xs);
    }

    /**
     * Generates all permutations of a {@code List}.
     *
     * @param xs a list of elements
     * @param <T> the type of values in the permutations
     */
    public @NotNull <T> Iterable<List<T>> permutationsFinite(@NotNull List<T> xs) {
        return wheelsProvider.permutationsFinite(xs);
    }

    /**
     * Generates all permutations of a {@code String}.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringPermutations(@NotNull String s) {
        return wheelsProvider.stringPermutations(s);
    }

    /**
     * Generates permutations of an {@code Iterable}. If the {@code Iterable} is finite, all permutations are
     * generated; if it is infinite, then only permutations that are equal to the identity except in a finite prefix
     * are generated.
     *
     * @param xs an {@code Iterable} of elements
     * @param <T> the type of values in the permutations
     */
    public @NotNull <T> Iterable<Iterable<T>> prefixPermutations(@NotNull Iterable<T> xs) {
        return wheelsProvider.prefixPermutations(xs);
    }

    /**
     * Generates all {@code List}s of a given size containing elements from a given {@code List}. The {@code List}s are
     * ordered lexicographically, matching the order given by the original {@code List}.
     *
     * @param size the length of each of the generated {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> listsLex(int size, @NotNull List<T> xs) {
        return wheelsProvider.listsLex(size, xs);
    }

    /**
     * Given two {@code Iterable}s, returns all {@code Pair}s of elements from these {@code Iterable}s. The
     * {@code Pair}s are ordered lexicographically, matching the order given by the original {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     */
    public @NotNull <A, B> Iterable<Pair<A, B>> pairsLex(@NotNull Iterable<A> as, @NotNull List<B> bs) {
        return wheelsProvider.pairsLex(as, bs);
    }

    /**
     * Given three {@code Iterable}s, returns all {@code Triple}s of elements from these {@code Iterable}s. The
     * {@code Triple}s are ordered lexicographically, matching the order given by the original {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     */
    public @NotNull <A, B, C> Iterable<Triple<A, B, C>> triplesLex(
            @NotNull Iterable<A> as,
            @NotNull List<B> bs,
            @NotNull List<C> cs
    ) {
        return wheelsProvider.triplesLex(as, bs, cs);
    }

    /**
     * Given four {@code Iterable}s, returns all {@code Quadruple}s of elements from these {@code Iterable}s. The
     * {@code Quadruple}s are ordered lexicographically, matching the order given by the original {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     */
    public @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruplesLex(
            @NotNull Iterable<A> as,
            @NotNull List<B> bs,
            @NotNull List<C> cs,
            @NotNull List<D> ds
    ) {
        return wheelsProvider.quadruplesLex(as, bs, cs, ds);
    }

    /**
     * Given five {@code Iterable}s, returns all {@code Quintuple}s of elements from these {@code Iterable}s. The
     * {@code Quintuple}s are ordered lexicographically, matching the order given by the original {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param es the fifth {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     */
    public @NotNull <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> quintuplesLex(
            @NotNull Iterable<A> as,
            @NotNull List<B> bs,
            @NotNull List<C> cs,
            @NotNull List<D> ds,
            @NotNull List<E> es
    ) {
        return wheelsProvider.quintuplesLex(as, bs, cs, ds, es);
    }

    /**
     * Given six {@code Iterable}s, returns all {@code Sextuple}s of elements from these {@code Iterable}s. The
     * {@code Sextuple}s are ordered lexicographically, matching the order given by the original {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param es the fifth {@code Iterable}
     * @param fs the sixth {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     * @param <F> the type of the sixth {@code Iterable}'s elements
     */
    public @NotNull <A, B, C, D, E, F> Iterable<Sextuple<A, B, C, D, E, F>> sextuplesLex(
            @NotNull Iterable<A> as,
            @NotNull List<B> bs,
            @NotNull List<C> cs,
            @NotNull List<D> ds,
            @NotNull List<E> es,
            @NotNull List<F> fs
    ) {
        return wheelsProvider.sextuplesLex(as, bs, cs, ds, es, fs);
    }

    /**
     * Given seven {@code Iterable}s, returns all {@code Septuple}s of elements from these {@code Iterable}s. The
     * {@code Septuple}s are ordered lexicographically, matching the order given by the original {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param es the fifth {@code Iterable}
     * @param fs the sixth {@code Iterable}
     * @param gs the seventh {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     * @param <F> the type of the sixth {@code Iterable}'s elements
     * @param <G> the type of the seventh {@code Iterable}'s elements
     */
    public @NotNull <A, B, C, D, E, F, G> Iterable<Septuple<A, B, C, D, E, F, G>> septuplesLex(
            @NotNull Iterable<A> as,
            @NotNull List<B> bs,
            @NotNull List<C> cs,
            @NotNull List<D> ds,
            @NotNull List<E> es,
            @NotNull List<F> fs,
            @NotNull List<G> gs
    ) {
        return wheelsProvider.septuplesLex(as, bs, cs, ds, es, fs, gs);
    }

    /**
     * Generates all {@code String}s of a given size containing elements from a given {@code String}. The elements are
     * ordered lexicographically, matching the order given by the original {@code String}.
     *
     * @param size the length of each of the generated {@code String}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringsLex(int size, @NotNull String s) {
        return wheelsProvider.stringsLex(size, s);
    }

    /**
     * Generates all {@code List}s containing elements from a given {@code Iterable}. The {@code List}s are ordered in
     * shortlex order (by length, then lexicographically), matching the order given by the original {@code Iterable}.
     *
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public <T> Iterable<List<T>> listsShortlex(@NotNull List<T> xs) {
        return wheelsProvider.listsShortlex(xs);
    }

    /**
     * Generates all {@code String}s containing characters from a given {@code String}. The {@code String}s are ordered
     * in shortlex order (by length, then lexicographically), matching the order given by the original {@code String}.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringsShortlex(@NotNull String s) {
        return wheelsProvider.stringsShortlex(s);
    }

    /**
     * Generates all {@code List}s with a minimum size containing elements from a given {@code Iterable}. The
     * {@code List}s are ordered in shortlex order (by length, then lexicographically), matching the order given by the
     * original {@code Iterable}.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> listsShortlexAtLeast(int minSize, @NotNull List<T> xs) {
        return wheelsProvider.listsShortlexAtLeast(minSize, xs);
    }

    /**
     * Generates all {@code String}s with a minimum size containing characters from a given {@code String}. The
     * {@code String}s are ordered in shortlex order (by length, then lexicographically), matching the order given by
     * the original {@code String}.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringsShortlexAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.stringsShortlexAtLeast(minSize, s);
    }

    /**
     * Generates all {@code List}s of a given size containing elements from a given {@code Iterable}.
     *
     * @param size the length of each of the generated {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> lists(int size, @NotNull Iterable<T> xs) {
        return wheelsProvider.lists(size, xs);
    }

    /**
     * Given two {@code Iterable}s, generates all {@code Pair}s of elements from these {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     */
    public @NotNull <A, B> Iterable<Pair<A, B>> pairs(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        return wheelsProvider.pairs(as, bs);
    }

    /**
     * Generates all {@code Pair}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T> Iterable<Pair<T, T>> pairs(@NotNull Iterable<T> xs) {
        return wheelsProvider.pairs(xs);
    }

    /**
     * Given three {@code Iterable}s, generates all {@code Triple}s of elements from these {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     */
    public @NotNull <A, B, C> Iterable<Triple<A, B, C>> triples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs
    ) {
        return wheelsProvider.triples(as, bs, cs);
    }

    /**
     * Generates all {@code Triple}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T> Iterable<Triple<T, T, T>> triples(@NotNull Iterable<T> xs) {
        return wheelsProvider.triples(xs);
    }

    /**
     * Given four {@code Iterable}s, generates all {@code Quadruple}s of elements from these {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     */
    public @NotNull <A, B, C, D> Iterable<Quadruple<A, B, C, D>> quadruples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds
    ) {
        return wheelsProvider.quadruples(as, bs, cs, ds);
    }

    /**
     * Generates all {@code Quadruple}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> quadruples(@NotNull Iterable<T> xs) {
        return wheelsProvider.quadruples(xs);
    }

    /**
     * Given five {@code Iterable}s, generates all {@code Quintuple}s of elements from these {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param es the fifth {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     */
    public @NotNull <A, B, C, D, E> Iterable<Quintuple<A, B, C, D, E>> quintuples(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs,
            @NotNull Iterable<C> cs,
            @NotNull Iterable<D> ds,
            @NotNull Iterable<E> es
    ) {
        return wheelsProvider.quintuples(as, bs, cs, ds, es);
    }

    /**
     * Generates all {@code Quintuple}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> quintuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.quintuples(xs);
    }

    /**
     * Given six {@code Iterable}s, generates all {@code Sextuple}s of elements from these {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param es the fifth {@code Iterable}
     * @param fs the sixth {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     * @param <F> the type of the sixth {@code Iterable}'s elements
     */
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

    /**
     * Generates all {@code Sextuple}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> sextuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.sextuples(xs);
    }

    /**
     * Given seven {@code Iterable}s, generates all {@code Septuple}s of elements from these {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param cs the third {@code Iterable}
     * @param ds the fourth {@code Iterable}
     * @param es the fifth {@code Iterable}
     * @param fs the sixth {@code Iterable}
     * @param gs the seventh {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     * @param <C> the type of the third {@code Iterable}'s elements
     * @param <D> the type of the fourth {@code Iterable}'s elements
     * @param <E> the type of the fifth {@code Iterable}'s elements
     * @param <F> the type of the sixth {@code Iterable}'s elements
     * @param <G> the type of the seventh {@code Iterable}'s elements
     */
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

    /**
     * Generates all {@code Septuple}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> septuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.septuples(xs);
    }

    /**
     * Generates all {@code String}s of a given size containing characters from a given {@code String}.
     *
     * @param size the length of each of the generated {@code String}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> strings(int size, @NotNull String s) {
        return wheelsProvider.strings(size, s);
    }

    /**
     * Generates all {@code String}s of a given size.
     *
     * @param size the length of each of the generated {@code String}s
     */
    public @NotNull Iterable<String> strings(int size) {
        return wheelsProvider.strings(size);
    }

    /**
     * Generates all {@code List}s of a containing elements from a given {@code Iterable}.
     *
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> lists(@NotNull Iterable<T> xs) {
        return wheelsProvider.lists(xs);
    }

    /**
     * Generates all {@code String}s of a containing characters from a given {@code String}.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> strings(@NotNull String s) {
        return wheelsProvider.strings(s);
    }

    /**
     * Generates all {@code String}s.
     */
    public @NotNull Iterable<String> strings() {
        return wheelsProvider.strings();
    }

    /**
     * Generates all {@code List}s with a minimum size containing elements from a given {@code Iterable}.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> listsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return wheelsProvider.listsAtLeast(minSize, xs);
    }

    /**
     * Generates all {@code String}s with a minimum size containing characters from a given {@code String}.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringsAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.stringsAtLeast(minSize, s);
    }

    /**
     * Generates all {@code String}s with a minimum size.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     */
    public @NotNull Iterable<String> stringsAtLeast(int minSize) {
        return wheelsProvider.stringsAtLeast(minSize);
    }

    /**
     * Generates all {@code List}s of a given size containing elements from a given {@code List} with no repetitions.
     * The {@code List}s are ordered lexicographically, matching the order given by the original {@code List}.
     *
     * @param size the length of each of the generated {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> distinctListsLex(int size, @NotNull List<T> xs) {
        return wheelsProvider.distinctListsLex(size, xs);
    }

    /**
     * Generates all {@code Pair}s of elements from a {@code List} with no repetitions. The {@code Pair}s are ordered
     * lexicographically, matching the order given by the original {@code List}.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Pair<T, T>> distinctPairsLex(@NotNull List<T> xs) {
        return wheelsProvider.distinctPairsLex(xs);
    }

    /**
     * Generates all {@code Triple}s of elements from a {@code List} with no repetitions. The {@code Triple}s are
     * ordered lexicographically, matching the order given by the original {@code List}.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Triple<T, T, T>> distinctTriplesLex(@NotNull List<T> xs) {
        return wheelsProvider.distinctTriplesLex(xs);
    }

    /**
     * Generates all {@code Quadruple}s of elements from a {@code List} with no repetitions. The {@code Quadruple}s are
     * ordered lexicographically, matching the order given by the original {@code List}.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> distinctQuadruplesLex(@NotNull List<T> xs) {
        return wheelsProvider.distinctQuadruplesLex(xs);
    }

    /**
     * Generates all {@code Quintuple}s of elements from a {@code List} with no repetitions. The {@code Quintuple}s are
     * ordered lexicographically, matching the order given by the original {@code List}.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> distinctQuintuplesLex(@NotNull List<T> xs) {
        return wheelsProvider.distinctQuintuplesLex(xs);
    }

    /**
     * Generates all {@code Sextuple}s of elements from a {@code List} with no repetitions. The {@code Sextuple}s are
     * ordered lexicographically, matching the order given by the original {@code List}.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> distinctSextuplesLex(@NotNull List<T> xs) {
        return wheelsProvider.distinctSextuplesLex(xs);
    }

    /**
     * Generates all {@code Septuple}s of elements from a {@code List} with no repetitions. The {@code Septuple}s are
     * ordered lexicographically, matching the order given by the original {@code List}.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> distinctSeptuplesLex(@NotNull List<T> xs) {
        return wheelsProvider.distinctSeptuplesLex(xs);
    }

    /**
     * Generates all {@code String}s of a given size containing characters from a given {@code String} with no
     * repetitions. The {@code String}s are ordered lexicographically, matching the order given by the original
     * {@code String}.
     *
     * @param size the length of each of the generated {@code String}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> distinctStringsLex(int size, @NotNull String s) {
        return wheelsProvider.distinctStringsLex(size, s);
    }

    /**
     * Generates all {@code List}s containing elements from a given {@code List} with no repetitions. The {@code List}s
     * are ordered lexicographically, matching the order given by the original {@code List}.
     *
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> distinctListsLex(@NotNull List<T> xs) {
        return wheelsProvider.distinctListsLex(xs);
    }

    /**
     * Generates all {@code String}s containing characters from a given {@code String} with no repetitions. The
     * {@code String}s are ordered lexicographically, matching the order given by the original {@code String}.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> distinctStringsLex(@NotNull String s) {
        return wheelsProvider.distinctStringsLex(s);
    }

    /**
     * Generates all {@code List}s with a minimum size containing elements from a given {@code List} with no
     * repetitions. The {@code List}s are ordered lexicographically, matching the order given by the original
     * {@code List}.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> distinctListsLexAtLeast(int minSize, @NotNull List<T> xs) {
        return wheelsProvider.distinctListsLexAtLeast(minSize, xs);
    }

    /**
     * Generates all {@code String}s with a minimum size containing characters from a given {@code String} with no
     * repetitions. The {@code String}s are ordered lexicographically, matching the order given by the original
     * {@code String}.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> distinctStringsLexAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.distinctStringsLexAtLeast(minSize, s);
    }

    /**
     * Generates all {@code List}s containing elements from a given {@code Iterable} with no repetitions. The
     * {@code List}s are ordered in shortlex order (by length, then lexicographically), matching the order given by the
     * original {@code Iterable}.
     *
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> distinctListsShortlex(@NotNull List<T> xs) {
        return wheelsProvider.distinctListsShortlex(xs);
    }

    /**
     * Generates all {@code String}s containing characters from a given {@code String} with no repetitions. The
     * {@code String}s are ordered in shortlex order (by length, then lexicographically), matching the order given by
     * the original {@code String}.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> distinctStringsShortlex(@NotNull String s) {
        return wheelsProvider.distinctStringsShortlex(s);
    }

    /**
     * Generates all {@code List}s with a minimum size containing elements from a given {@code Iterable} with no
     * repetitions. The {@code List}s are ordered in shortlex order (by length, then lexicographically), matching the
     * order given by the original {@code Iterable}.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> distinctListsShortlexAtLeast(int minSize, @NotNull List<T> xs) {
        return wheelsProvider.distinctListsShortlexAtLeast(minSize, xs);
    }

    /**
     * Generates all {@code String}s with a minimum size containing characters from a given {@code String} with no
     * repetitions. The {@code String}s are ordered in shortlex order (by length, then lexicographically), matching the
     * order given by the original {@code String}.
     *
     * @param minSize the minimum size of the resulting {@code String}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> distinctStringsShortlexAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.distinctStringsShortlexAtLeast(minSize, s);
    }

    /**
     * Generates all {@code List}s of a given size containing elements from a given {@code List} with no repetitions.
     *
     * @param xs a {@code List} of elements
     * @param size the length of each of the generated {@code List}s
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> distinctLists(int size, @NotNull Iterable<T> xs) {
        return wheelsProvider.distinctLists(size, xs);
    }

    /**
     * Generates all {@code Pair}s of elements from a {@code List} with no repetitions.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Pair<T, T>> distinctPairs(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctPairs(xs);
    }

    /**
     * Generates all {@code Triple}s of elements from a {@code List} with no repetitions.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Triple<T, T, T>> distinctTriples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctTriples(xs);
    }

    /**
     * Generates all {@code Quadruple}s of elements from a {@code List} with no repetitions.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> distinctQuadruples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctQuadruples(xs);
    }

    /**
     * Generates all {@code Quintuple}s of elements from a {@code List} with no repetitions.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> distinctQuintuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctQuintuples(xs);
    }

    /**
     * Generates all {@code Sextuple}s of elements from a {@code List} with no repetitions.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> distinctSextuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctSextuples(xs);
    }

    /**
     * Generates all {@code Septuple}s of elements from a {@code List} with no repetitions.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T> Iterable<Septuple<T, T, T, T, T, T, T>> distinctSeptuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctSeptuples(xs);
    }

    /**
     * Generates all {@code String}s of a given size containing characters from a given {@code String} with no
     * repetitions.
     *
     * @param size the length of each of the generated {@code String}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> distinctStrings(int size, @NotNull String s) {
        return wheelsProvider.distinctStrings(size, s);
    }

    /**
     * Generates all {@code String}s of a given size containing characters from a given {@code String}.
     *
     * @param size the length of each of the generated {@code String}s
     */
    public @NotNull Iterable<String> distinctStrings(int size) {
        return wheelsProvider.distinctStrings(size);
    }

    /**
     * Generates all {@code List}s containing elements from a given {@code List} with no repetitions.
     *
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> distinctLists(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctLists(xs);
    }

    /**
     * Generates all {@code String}s containing characters from a given {@code String} with no repetitions.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> distinctStrings(@NotNull String s) {
        return wheelsProvider.distinctStrings(s);
    }

    /**
     * Generates all {@code String}s containing characters from a given {@code String}.
     */
    public Iterable<String> distinctStrings()  {
        return wheelsProvider.distinctStrings();
    }

    /**
     * Generates all {@code List}s with a minimum size containing elements from a given {@code List} with no
     * repetitions.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> distinctListsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return wheelsProvider.distinctListsAtLeast(minSize, xs);
    }

    /**
     * Generates all {@code String}s with a minimum size containing characters from a given {@code String} with no
     * repetitions.
     *
     * @param minSize the minimum size of the resulting {@code String}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> distinctStringsAtLeast(int minSize, @NotNull String s)  {
        return wheelsProvider.distinctStringsAtLeast(minSize, s);
    }

    /**
     * Generates all {@code String}s with a minimum size with no repetitions.
     *
     * @param minSize the minimum size of the resulting {@code String}s
     */
    public @NotNull Iterable<String> distinctStringsAtLeast(int minSize)  {
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

    public @NotNull <K, V> Iterable<Map<K, V>> maps(@NotNull List<K> ks, Iterable<V> vs) {
        return wheelsProvider.maps(ks, vs);
    }

    public @NotNull Iterable<String> substrings(@NotNull String s) {
        return wheelsProvider.substrings(s);
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
