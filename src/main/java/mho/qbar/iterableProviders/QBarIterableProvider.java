package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.IterableProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.BinaryFraction;
import mho.wheels.math.MathUtils;
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
import static mho.wheels.ordering.Ordering.lt;

/**
 * This class provides {@code Iterables} for testing.
 */
@SuppressWarnings("unused")
public strictfp abstract class QBarIterableProvider {
    /**
     * An instance of {@code IterableProvider} to access generators from the wheels project.
     */
    protected @NotNull IterableProvider wheelsProvider;

    /**
     * Creates a new {@code QBarIterableProvider} with a {@code IterableProvider}.
     *
     * <ul>
     *  <li>{@code wheelsProvider} cannot be null.</li>
     *  <li>Any {@code QBarIterableProvider} may be created with this constructor.</li>
     * </ul>
     *
     * @param wheelsProvider an {@code IterableProvider}
     */
    protected QBarIterableProvider(@NotNull IterableProvider wheelsProvider) {
        this.wheelsProvider = wheelsProvider;
    }

    /**
     * Returns {@code this}'s wheels provider.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarIterableProvider}.</li>
     *  <li>The result may be any {@code IterableProvider}.</li>
     * </ul>
     *
     * @return the wheels provider of {@code this}
     */
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
     * Returns a shallow copy of {@code this} with a given tertiary scale.
     *
     * @param tertiaryScale the tertiary scale.
     */
    public @NotNull QBarIterableProvider withTertiaryScale(int tertiaryScale) {
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
     * @param minSize the minimum size of the resulting {@code String}s
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
     * Generates all {@code List}s of a given size containing elements from a given {@code Iterable} with no
     * repetitions.
     *
     * @param xs an {@code Iterable} of elements
     * @param size the length of each of the generated {@code List}s
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> distinctLists(int size, @NotNull Iterable<T> xs) {
        return wheelsProvider.distinctLists(size, xs);
    }

    /**
     * Generates all {@code Pair}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T> Iterable<Pair<T, T>> distinctPairs(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctPairs(xs);
    }

    /**
     * Generates all {@code Triple}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T> Iterable<Triple<T, T, T>> distinctTriples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctTriples(xs);
    }

    /**
     * Generates all {@code Quadruple}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T> Iterable<Quadruple<T, T, T, T>> distinctQuadruples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctQuadruples(xs);
    }

    /**
     * Generates all {@code Quintuple}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T> Iterable<Quintuple<T, T, T, T, T>> distinctQuintuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctQuintuples(xs);
    }

    /**
     * Generates all {@code Sextuple}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T> Iterable<Sextuple<T, T, T, T, T, T>> distinctSextuples(@NotNull Iterable<T> xs) {
        return wheelsProvider.distinctSextuples(xs);
    }

    /**
     * Generates all {@code Septuple}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
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
     * Generates all {@code List}s containing elements from a given {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable} of elements
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
     * Generates all {@code List}s with a minimum size containing elements from a given {@code Iterable} with no
     * repetitions.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param xs an {@code Iterable} of elements
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

    /**
     * Generates all unordered {@code List}s of a given size containing elements from a given {@code List}. The
     * {@code List}s are ordered lexicographically, matching the order given by the original {@code List}.
     *
     * @param size the length of each of the generated {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> bagsLex(int size, @NotNull List<T> xs) {
        return wheelsProvider.bagsLex(size, xs);
    }

    /**
     * Generates all unordered {@code Pair}s of elements from a {@code List}. The {@code Pair}s are ordered
     * lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Pair<T, T>> bagPairsLex(@NotNull List<T> xs) {
        return wheelsProvider.bagPairsLex(xs);
    }

    /**
     * Generates all unordered {@code Triple}s of elements from a {@code List}. The {@code Triple}s are ordered
     * lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Triple<T, T, T>> bagTriplesLex(@NotNull List<T> xs) {
        return wheelsProvider.bagTriplesLex(xs);
    }

    /**
     * Generates all unordered {@code Quadruple}s of elements from a {@code List}. The {@code Quadruple}s are ordered
     * lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Quadruple<T, T, T, T>> bagQuadruplesLex(@NotNull List<T> xs) {
        return wheelsProvider.bagQuadruplesLex(xs);
    }

    /**
     * Generates all unordered {@code Quintuple}s of elements from a {@code List}. The {@code Quintuple}s are ordered
     * lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Quintuple<T, T, T, T, T>> bagQuintuplesLex(
            @NotNull List<T> xs
    ) {
        return wheelsProvider.bagQuintuplesLex(xs);
    }

    /**
     * Generates all unordered {@code Sextuple}s of elements from a {@code List}. The {@code Sextuple}s are ordered
     * lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Sextuple<T, T, T, T, T, T>> bagSextuplesLex(
            @NotNull List<T> xs
    ) {
        return wheelsProvider.bagSextuplesLex(xs);
    }

    /**
     * Generates all unordered {@code Septuple}s of elements from a {@code List}. The {@code Septuple}s are ordered
     * lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Septuple<T, T, T, T, T, T, T>> bagSeptuplesLex(
            @NotNull List<T> xs
    ) {
        return wheelsProvider.bagSeptuplesLex(xs);
    }

    /**
     * Generates all unordered {@code String}s containing characters from a given {@code String}. The {@code String}s
     * are ordered lexicographically.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringBagsLex(int size, @NotNull String s) {
        return wheelsProvider.stringBagsLex(size, s);
    }

    /**
     * Generates all unordered {@code List}s containing elements from a given {@code Iterable}. The {@code List}s are
     * ordered in shortlex order (by length, then lexicographically).
     *
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> bagsShortlex(@NotNull List<T> xs) {
        return wheelsProvider.bagsShortlex(xs);
    }

    /**
     * Generates all unordered {@code String}s containing characters from a given {@code String}. The {@code String}s
     * are ordered in shortlex order (by length, then lexicographically).
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringBagsShortlex(@NotNull String s) {
        return wheelsProvider.stringBagsShortlex(s);
    }

    /**
     * Generates all unordered {@code List}s with a minimum size containing elements from a given {@code Iterable}. The
     * {@code List}s are ordered in shortlex order (by length, then lexicographically).
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> bagsShortlexAtLeast(int minSize, @NotNull List<T> xs) {
        return wheelsProvider.bagsShortlexAtLeast(minSize, xs);
    }

    /**
     * Generates all unordered {@code String}s with a minimum size containing characters from a given {@code String}.
     * The {@code String}s are ordered in shortlex order (by length, then lexicographically).
     *
     * @param minSize the minimum size of the resulting {@code String}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringBagsShortlexAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.stringBagsShortlexAtLeast(minSize, s);
    }

    /**
     * Generates all unordered {@code List}s of a given size containing elements from a given {@code Iterable}.
     *
     * @param size the length of each of the generated {@code List}s
     * @param xs an {@code Iterable} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> bags(int size, @NotNull Iterable<T> xs) {
        return wheelsProvider.bags(size, xs);
    }

    /**
     * Generates all unordered {@code Pair}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Pair<T, T>> bagPairs(@NotNull Iterable<T> xs) {
        return wheelsProvider.bagPairs(xs);
    }

    /**
     * Generates all unordered {@code Triple}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Triple<T, T, T>> bagTriples(@NotNull Iterable<T> xs) {
        return wheelsProvider.bagTriples(xs);
    }

    /**
     * Generates all unordered {@code Quadruple}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Quadruple<T, T, T, T>> bagQuadruples(@NotNull Iterable<T> xs) {
        return wheelsProvider.bagQuadruples(xs);
    }

    /**
     * Generates all unordered {@code Quintuple}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Quintuple<T, T, T, T, T>> bagQuintuples(
            @NotNull Iterable<T> xs
    ) {
        return wheelsProvider.bagQuintuples(xs);
    }

    /**
     * Generates all unordered {@code Sextuple}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Sextuple<T, T, T, T, T, T>> bagSextuples(
            @NotNull Iterable<T> xs
    ) {
        return wheelsProvider.bagSextuples(xs);
    }

    /**
     * Generates all unordered {@code Septuple}s of elements from an {@code Iterable}.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Septuple<T, T, T, T, T, T, T>> bagSeptuples(
            @NotNull Iterable<T> xs
    ) {
        return wheelsProvider.bagSeptuples(xs);
    }

    /**
     * Generates all unordered {@code String}s of a given size containing characters from a given {@code String}.
     *
     * @param size the length of each of the generated {@code String}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringBags(int size, @NotNull String s) {
        return wheelsProvider.stringBags(size, s);
    }

    /**
     * Generates all unordered {@code String}s of a given size.
     *
     * @param size the length of each of the generated {@code String}s
     */
    public @NotNull Iterable<String> stringBags(int size) {
        return wheelsProvider.stringBags(size);
    }

    /**
     * Generates all unordered {@code List}s containing elements from a given {@code Iterable}.
     *
     * @param xs an {@code Iterable} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> bags(@NotNull Iterable<T> xs) {
        return wheelsProvider.bags(xs);
    }

    /**
     * Generates all unordered {@code String}s containing characters from a given {@code String}.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringBags(@NotNull String s) {
        return wheelsProvider.stringBags(s);
    }

    /**
     * Generates all unordered {@code String}s.
     */
    public @NotNull Iterable<String> stringBags() {
        return wheelsProvider.stringBags();
    }

    /**
     * Generates all unordered {@code List}s with a minimum size containing elements from a given {@code Iterable}.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param xs an {@code Iterable} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> bagsAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return wheelsProvider.bagsAtLeast(minSize, xs);
    }

    /**
     * Generates all unordered {@code String}s with a minimum size containing characters from a given {@code String}.
     *
     * @param minSize the minimum size of the resulting {@code String}s
     * @param s an {@code String}
     */
    public @NotNull Iterable<String> stringBagsAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.stringBagsAtLeast(minSize, s);
    }

    /**
     * Generates all unordered {@code String}s with a minimum size.
     *
     * @param minSize the minimum size of the resulting {@code String}s
     */
    public @NotNull Iterable<String> stringBagsAtLeast(int minSize) {
        return wheelsProvider.stringBagsAtLeast(minSize);
    }

    /**
     * Generates all unordered {@code List}s of a given size containing elements from a given {@code List} with no
     * repetitions. The {@code List}s are ordered lexicographically.
     *
     * @param size the length of each of the generated {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsetsLex(int size, @NotNull List<T> xs) {
        return wheelsProvider.subsetsLex(size, xs);
    }

    /**
     * Generates all unordered {@code Pair}s of elements from a {@code List} with no repetitions. The {@code Pair}s are
     * ordered lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Pair<T, T>> subsetPairsLex(@NotNull List<T> xs) {
        return wheelsProvider.subsetPairsLex(xs);
    }

    /**
     * Generates all unordered {@code Triple}s of elements from a {@code List} with no repetitions. The {@code Triple}s
     * are ordered lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Triple<T, T, T>> subsetTriplesLex(@NotNull List<T> xs) {
        return wheelsProvider.subsetTriplesLex(xs);
    }

    /**
     * Generates all unordered {@code Quadruple}s of elements from a {@code List} with no repetitions. The
     * {@code Quadruple}s are ordered lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Quadruple<T, T, T, T>> subsetQuadruplesLex(
            @NotNull List<T> xs
    ) {
        return wheelsProvider.subsetQuadruplesLex(xs);
    }

    /**
     * Generates all unordered {@code Quintuple}s of elements from a {@code List} with no repetitions. The
     * {@code Quintuple}s are ordered lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Quintuple<T, T, T, T, T>> subsetQuintuplesLex(
            @NotNull List<T> xs
    ) {
        return wheelsProvider.subsetQuintuplesLex(xs);
    }

    /**
     * Generates all unordered {@code Sextuple}s of elements from a {@code List} with no repetitions. The
     * {@code Sextuple}s are ordered lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Sextuple<T, T, T, T, T, T>> subsetSextuplesLex(
            @NotNull List<T> xs
    ) {
        return wheelsProvider.subsetSextuplesLex(xs);
    }

    /**
     * Generates all unordered {@code Septuple}s of elements from a {@code List} with no repetitions. The
     * {@code Septuple}s are ordered lexicographically.
     *
     * @param xs a {@code List}
     * @param <T> the type of the {@code List}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Septuple<T, T, T, T, T, T, T>> subsetSeptuplesLex(
            @NotNull List<T> xs
    ) {
        return wheelsProvider.subsetSeptuplesLex(xs);
    }

    /**
     * Generates all unordered {@code String}s containing characters from a given {@code String} with no repetitions.
     * The {@code String}s are ordered lexicographically.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringSubsetsLex(int size, @NotNull String s) {
        return wheelsProvider.stringSubsetsLex(size, s);
    }

    /**
     * Generates all unordered {@code List}s containing elements from a given {@code List} with no repetitions. The
     * {@code List}s are ordered lexicographically.
     *
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsetsLex(@NotNull List<T> xs) {
        return wheelsProvider.subsetsLex(xs);
    }

    /**
     * Generates all unordered {@code String}s containing characters from a given {@code String} with no repetitions.
     * The {@code String}s are ordered lexicographically.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringSubsetsLex(@NotNull String s) {
        return wheelsProvider.stringSubsetsLex(s);
    }

    /**
     * Generates all unordered {@code List}s with a minimum size containing elements from a given {@code List} with no
     * repetitions. The {@code List}s are ordered lexicographically.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsetsLexAtLeast(int minSize, @NotNull List<T> xs) {
        return wheelsProvider.subsetsLexAtLeast(minSize, xs);
    }

    /**
     * Generates all unordered {@code String}s with a minimum size containing characters from a given {@code String}
     * with no repetitions. The {@code String}s are ordered lexicographically.
     *
     * @param minSize the minimum size of the resulting {@code String}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringSubsetsLexAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.stringSubsetsLexAtLeast(minSize, s);
    }

    /**
     * Generates all unordered {@code List}s containing elements from a given {@code Iterable} with no repetitions. The
     * {@code List}s are ordered in shortlex order (by length, then lexicographically).
     *
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsetsShortlex(@NotNull List<T> xs) {
        return wheelsProvider.subsetsShortlex(xs);
    }

    /**
     * Generates all unordered {@code String}s containing characters from a given {@code String} with no repetitions.
     * The {@code String}s are ordered in shortlex order (by length, then lexicographically).
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringSubsetsShortlex(@NotNull String s) {
        return wheelsProvider.stringSubsetsShortlex(s);
    }

    /**
     * Generates all unordered {@code List}s with a minimum size containing elements from a given {@code Iterable} with
     * no repetitions. The {@code List}s are ordered in shortlex order (by length, then lexicographically).
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param xs a {@code List} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsetsShortlexAtLeast(
            int minSize,
            @NotNull List<T> xs
    ) {
        return wheelsProvider.subsetsShortlexAtLeast(minSize, xs);
    }

    /**
     * Generates all unordered {@code String}s with a minimum size containing characters from a given {@code String}
     * with no repetitions. The {@code String}s are ordered in shortlex order (by length, then lexicographically).
     *
     * @param minSize the minimum size of the resulting {@code String}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringSubsetsShortlexAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.stringSubsetsShortlexAtLeast(minSize, s);
    }

    /**
     * Generates all unordered {@code List}s of a given size containing elements from a given {@code Iterable} with no
     * repetitions.
     *
     * @param size the length of each of the generated {@code List}s
     * @param xs an {@code Iterable} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsets(int size, @NotNull Iterable<T> xs) {
        return wheelsProvider.subsets(size, xs);
    }

    /**
     * Generates all unordered {@code Pair}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Pair<T, T>> subsetPairs(@NotNull Iterable<T> xs) {
        return wheelsProvider.subsetPairs(xs);
    }

    /**
     * Generates all unordered {@code Triple}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Triple<T, T, T>> subsetTriples(@NotNull Iterable<T> xs) {
        return wheelsProvider.subsetTriples(xs);
    }

    /**
     * Generates all unordered {@code Quadruple}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Quadruple<T, T, T, T>> subsetQuadruples(
            @NotNull Iterable<T> xs
    ) {
        return wheelsProvider.subsetQuadruples(xs);
    }

    /**
     * Generates all unordered {@code Quintuple}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Quintuple<T, T, T, T, T>> subsetQuintuples(
            @NotNull Iterable<T> xs
    ) {
        return wheelsProvider.subsetQuintuples(xs);
    }

    /**
     * Generates all unordered {@code Sextuple}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Sextuple<T, T, T, T, T, T>> subsetSextuples(
            @NotNull Iterable<T> xs
    ) {
        return wheelsProvider.subsetSextuples(xs);
    }

    /**
     * Generates all unordered {@code Septuple}s of elements from an {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable}
     * @param <T> the type of the {@code Iterable}'s elements
     */
    public @NotNull <T extends Comparable<T>> Iterable<Septuple<T, T, T, T, T, T, T>> subsetSeptuples(
            @NotNull Iterable<T> xs
    ) {
        return wheelsProvider.subsetSeptuples(xs);
    }

    /**
     * Generates all unordered {@code String}s of a given size containing characters from a given {@code String} with
     * no repetitions.
     *
     * @param size the length of each of the generated {@code String}s
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringSubsets(int size, @NotNull String s) {
        return wheelsProvider.stringSubsets(size, s);
    }

    /**
     * Generates all unordered {@code String}s of a given size with no repetitions.
     *
     * @param size the length of each of the generated {@code String}s
     */
    public @NotNull Iterable<String> stringSubsets(int size) {
        return wheelsProvider.stringSubsets(size);
    }

    /**
     * Generates all unordered {@code List}s containing elements from a given {@code Iterable} with no repetitions.
     *
     * @param xs an {@code Iterable} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsets(@NotNull Iterable<T> xs) {
        return wheelsProvider.subsets(xs);
    }

    /**
     * Generates all unordered {@code String}s containing characters from a given {@code String} with no repetitions.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringSubsets(@NotNull String s) {
        return wheelsProvider.stringSubsets(s);
    }

    /**
     * Generates all unordered {@code String}s with no repetitions.
     */
    public @NotNull Iterable<String> stringSubsets() {
        return wheelsProvider.stringSubsets();
    }

    /**
     * Generates all unordered {@code List}s with a minimum size containing elements from a given {@code Iterable} with
     * no repetitions.
     *
     * @param minSize the minimum size of the resulting {@code List}s
     * @param xs an {@code Iterable} of elements
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsetsAtLeast(
            int minSize,
            @NotNull Iterable<T> xs
    ) {
        return wheelsProvider.subsetsAtLeast(minSize, xs);
    }

    /**
     * Generates all unordered {@code String}s with a minimum size containing characters from a given {@code String}
     * with no repetitions.
     *
     * @param minSize the minimum size of the resulting {@code String}s
     * @param s an {@code String}
     */
    public @NotNull Iterable<String> stringSubsetsAtLeast(int minSize, @NotNull String s) {
        return wheelsProvider.stringSubsetsAtLeast(minSize, s);
    }

    /**
     * Generates all unordered {@code String}s with a minimum size with no repetitions.
     *
     * @param minSize the minimum size of the resulting {@code String}s
     */
    public @NotNull Iterable<String> stringSubsetsAtLeast(int minSize) {
        return wheelsProvider.stringSubsetsAtLeast(minSize);
    }

    /**
     * Generates all {@code Either}s from two {@code Iterable}s. If applicable, {@code Either}s from the first
     * {@code Iterable} are generated before {@code Either}s from the second.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     */
    public @NotNull <A, B> Iterable<Either<A, B>> eithersSuccessive(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        return wheelsProvider.eithersSuccessive(as, bs);
    }

    /**
     * Generates all {@code Either}s from two {@code Iterable}s. If applicable, the ratio of elements derived from the
     * second {@code Iterable} approaches sqrt(n)/n, where n is the number of elements generated (assuming the two
     * source {@code Iterable}s are infinite).
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     */
    public @NotNull <A, B> Iterable<Either<A, B>> eithersSquareRootOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return wheelsProvider.eithersSquareRootOrder(as, bs);
    }

    /**
     * Generates all {@code Either}s from two {@code Iterable}s. If applicable, the ratio of elements derived from the
     * second {@code Iterable} approaches log<sub>2</sub>(n)/n, where n is the number of elements generated (assuming
     * the two source {@code Iterable}s are infinite).
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     */
    public @NotNull <A, B> Iterable<Either<A, B>> eithersLogarithmicOrder(
            @NotNull Iterable<A> as,
            @NotNull Iterable<B> bs
    ) {
        return wheelsProvider.eithersLogarithmicOrder(as, bs);
    }

    /**
     * Generates all {@code Either}s from two {@code Iterable}s.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <A> the type of the first {@code Iterable}'s elements
     * @param <B> the type of the second {@code Iterable}'s elements
     */
    public @NotNull <A, B> Iterable<Either<A, B>> eithers(@NotNull Iterable<A> as, @NotNull Iterable<B> bs) {
        return wheelsProvider.eithers(as, bs);
    }

    /**
     * Given two {@code Iterable}s, generates an {@code Iterable} containing the elements of both. If applicable,
     * elements from the first {@code Iterable} are returned before elements from the second.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <T> the type of the {@code Iterables}' elements
     */
    public @NotNull <T> Iterable<T> chooseSuccessive(@NotNull Iterable<T> as, @NotNull Iterable<T> bs) {
        return wheelsProvider.chooseSuccessive(as, bs);
    }

    /**
     * Given two {@code Iterable}s, generates an {@code Iterable} containing the elements of both. If applicable, the
     * ratio of elements from the second {@code Iterable} approaches sqrt(n)/n, where n is the number of elements
     * generated (assuming the two source {@code Iterable}s are infinite).
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <T> the type of the {@code Iterables}' elements
     */
    public @NotNull <T> Iterable<T> chooseSquareRootOrder(@NotNull Iterable<T> as, @NotNull Iterable<T> bs) {
        return wheelsProvider.chooseSquareRootOrder(as, bs);
    }

    /**
     * Given two {@code Iterable}s, generates an {@code Iterable} containing the elements of both. If applicable, the
     * ratio of elements from the second {@code Iterable} approaches log<sub>2</sub>(n)/n, where n is the number of
     * elements generated (assuming the two source {@code Iterable}s are infinite).
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <T> the type of the {@code Iterables}' elements
     */
    public @NotNull <T> Iterable<T> chooseLogarithmicOrder(@NotNull Iterable<T> as, @NotNull Iterable<T> bs) {
        return wheelsProvider.chooseLogarithmicOrder(as, bs);
    }

    /**
     * Given two {@code Iterable}s, generates an {@code Iterable} containing the elements of both.
     *
     * @param as the first {@code Iterable}
     * @param bs the second {@code Iterable}
     * @param <T> the type of the {@code Iterables}' elements
     */
    public @NotNull <T> Iterable<T> choose(@NotNull Iterable<T> as, @NotNull Iterable<T> bs) {
        return wheelsProvider.choose(as, bs);
    }

    /**
     * Generates the Cartesian product of a {@code List} of {@code List}s, that is, all possible {@code List}s such
     * that the ith element of the {@code List} comes from the ith input {@code List}.
     *
     * @param xss a {@code List} of {@code List}s
     * @param <T> the type of values in the {@code List}s
     */
    public @NotNull <T> Iterable<List<T>> cartesianProduct(@NotNull List<List<T>> xss) {
        return wheelsProvider.cartesianProduct(xss);
    }

    /**
     * Generates repeating infinite {@code Iterables} whose elements are chosen from a given {@code List}.
     *
     * @param xs the source of elements for the generated {@code Iterable}s
     * @param <T> the type of elements in the {@code Iterable}
     */
    public @NotNull <T> Iterable<Iterable<T>> repeatingIterables(@NotNull Iterable<T> xs) {
        return wheelsProvider.repeatingIterables(xs);
    }

    /**
     * Generates repeating infinite {@code Iterables} whose elements are chosen from a given {@code List}. The
     * {@code Iterable}s have a specified minimum number of distinct elements (at least 2).
     *
     * @param minSize the minimum number of distinct elements in the generated {@code Iterable}s
     * @param xs the source of elements for the {@code Iterable}
     * @param <T> the type of elements in the {@code Iterable}
     */
    public @NotNull <T> Iterable<Iterable<T>> repeatingIterablesDistinctAtLeast(int minSize, @NotNull Iterable<T> xs) {
        return wheelsProvider.repeatingIterablesDistinctAtLeast(minSize, xs);
    }

    /**
     * Generates all sublists of a given {@code List}.
     *
     * @param xs a {@code List}
     * @param <T> the type of the elements in {@code xs}
     */
    public @NotNull <T> Iterable<List<T>> sublists(@NotNull List<T> xs) {
        return wheelsProvider.sublists(xs);
    }

    /**
     * Generates all substrings of a given {@code String}.
     *
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> substrings(@NotNull String s) {
        return wheelsProvider.substrings(s);
    }

    /**
     * Generates all {@code List}s from an {@code Iterable} of elements {@code xs} which contain a particular element.
     * {@code xs} may or may not contain the element.
     *
     * @param x an element that the output {@code List}s must contain
     * @param xs a {@code List}
     * @param <T> the type of the elements in {@code xs}
     */
    public @NotNull <T> Iterable<List<T>> listsWithElement(@Nullable T x, @NotNull Iterable<T> xs) {
        return wheelsProvider.listsWithElement(x, xs);
    }

    /**
     * Generates all {@code String}s from a given {@code String} {@code s} which contain a particular character.
     * {@code s} may or may not contain the character.
     *
     * @param c a character that the output {@code String}s must contain
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringsWithChar(char c, @NotNull String s) {
        return wheelsProvider.stringsWithChar(c, s);
    }

    /**
     * Generates all {@code String}s which contain a particular character.
     *
     * @param c a character that the output {@code String}s must contain
     */
    public @NotNull Iterable<String> stringsWithChar(char c) {
        return wheelsProvider.stringsWithChar(c);
    }

    /**
     * Generates all unordered {@code List}s from an {@code Iterable} of elements {@code xs}, with no repetitions,
     * which contain a particular element. {@code xs} may or may not contain the element.
     *
     * @param x an element that the output {@code List}s must contain
     * @param xs a {@code List}
     * @param <T> the type of the elements in {@code xs}
     */
    public @NotNull <T extends Comparable<T>> Iterable<List<T>> subsetsWithElement(
            T x,
            @NotNull Iterable<T> xs
    ) {
        return wheelsProvider.subsetsWithElement(x, xs);
    }

    /**
     * Generates all unordered {@code String}s containing characters from a given {@code String} {@code s}, with no
     * repetitions, which contain a particular character. {@code s} may or may not contain the character.
     *
     * @param c a character that the output {@code String}s must contain
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringSubsetsWithChar(char c, @NotNull String s) {
        return wheelsProvider.stringSubsetsWithChar(c, s);
    }

    /**
     * Generates all unordered {@code String}s with no repetitions which contain a particular character.
     *
     * @param c a character that the output {@code String}s must contain
     */
    public @NotNull Iterable<String> stringSubsetsWithChar(char c) {
        return wheelsProvider.stringSubsetsWithChar(c);
    }

    /**
     * Generates all {@code List}s containing elements from a given {@code List} {@code xs} which contain at least one
     * of a given {@code Iterable} of sublists.
     *
     * @param sublists {@code List}s, at least one of which must be contained in each result {@code List}
     * @param xs a {@code List}
     * @param <T> the type of elements in {@code xs}
     */
    public @NotNull <T> Iterable<List<T>> listsWithSublists(
            @NotNull Iterable<List<T>> sublists,
            @NotNull Iterable<T> xs
    ) {
        return wheelsProvider.listsWithSublists(sublists, xs);
    }

    /**
     * Generates all {@code String}s containing characters from a given {@code String} {@code s} which contain at least
     * one of a given {@code Iterable} of substrings.
     *
     * @param substrings {@code String}s, at least one of which must be contained in each result {@code String}
     * @param s a {@code String}
     */
    public @NotNull Iterable<String> stringsWithSubstrings(@NotNull Iterable<String> substrings, @NotNull String s) {
        return wheelsProvider.stringsWithSubstrings(substrings, s);
    }

    /**
     * Generates all {@code String}s which contain at least one of a given {@code Iterable} of substrings.
     *
     * @param substrings {@code String}s, at least one of which must be contained in each result {@code String}
     */
    public @NotNull Iterable<String> stringsWithSubstrings(@NotNull Iterable<String> substrings) {
        return wheelsProvider.stringsWithSubstrings(substrings);
    }

    /**
     * Generates all {@code HashMap}s whose keys are {@code ks} and whose values are subsets of {@code vs}.
     *
     * @param ks the keys of the resulting maps
     * @param vs a set where the values of the resulting maps are drawn from
     * @param <K> the type of the maps' keys
     * @param <V> the type of the maps' values
     */
    public @NotNull <K, V> Iterable<Map<K, V>> maps(@NotNull List<K> ks, Iterable<V> vs) {
        return wheelsProvider.maps(ks, vs);
    }

    /**
     * Generates all {@code RandomProvider}s with a fixed {@code scale}, {@code secondaryScale}, and
     * {@code tertiaryScale}.
     *
     * @param scale the {@code scale} of the generated {@code RandomProvider}s
     * @param secondaryScale the {@code secondaryScale} of the generated {@code RandomProvider}s
     * @param tertiaryScale the {@code tertiaryScale} of the generated {@code RandomProvider}s
     */
    public @NotNull Iterable<RandomProvider> randomProvidersFixedScales(
            int scale,
            int secondaryScale,
            int tertiaryScale
    ) {
        return wheelsProvider.randomProvidersFixedScales(scale, secondaryScale, tertiaryScale);
    }

    /**
     * Generates all {@code RandomProvider}s with the default {@code scale} and {@code secondaryScale}.
     */
    public @NotNull Iterable<RandomProvider> randomProvidersDefault() {
        return wheelsProvider.randomProvidersDefault();
    }

    /**
     * Generates all {@code RandomProvider}s with the default {@code secondaryScale} and {@code tertiaryScale}.
     */
    public @NotNull Iterable<RandomProvider> randomProvidersDefaultSecondaryAndTertiaryScale() {
        return wheelsProvider.randomProvidersDefaultSecondaryAndTertiaryScale();
    }

    /**
     * Generates all {@code RandomProvider}s with the default {@code tertiaryScale}.
     */
    public @NotNull Iterable<RandomProvider> randomProvidersDefaultTertiaryScale() {
        return wheelsProvider.randomProvidersDefaultTertiaryScale();
    }

    /**
     * Generates all {@code RandomProvider}s.
     */
    public @NotNull Iterable<RandomProvider> randomProviders() {
        return wheelsProvider.randomProviders();
    }

    /**
     * Generates positive {@code Rational}s.
     */
    public abstract @NotNull Iterable<Rational> positiveRationals();

    /**
     * Generates negative {@code Rational}s.
     */
    public abstract @NotNull Iterable<Rational> negativeRationals();

    /**
     * Generates nonzero {@code Rational}s.
     */
    public abstract @NotNull Iterable<Rational> nonzeroRationals();

    /**
     * Generates {@code Rational}s.
     */
    public abstract @NotNull Iterable<Rational> rationals();

    /**
     * Generates {@code Rational}s in the interval [0, 1).
     */
    public abstract @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne();

    /**
     * Generates {@code Rational}s greater than or equal to a given value.
     *
     * @param a the inclusive lower bound of the generated {@code Rational}s
     */
    public @NotNull Iterable<Rational> rangeUp(@NotNull Rational a) {
        return map(r -> r.add(a), withElement(Rational.ZERO, positiveRationals()));
    }

    /**
     * Generates {@code Rational}s less than or equal to a given value.
     *
     * @param a the inclusive upper bound of the generated {@code Rational}s
     */
    public @NotNull Iterable<Rational> rangeDown(@NotNull Rational a) {
        return map(a::subtract, withElement(Rational.ZERO, positiveRationals()));
    }

    /**
     * Generates {@code Rational}s between {@code a} and {@code b}, inclusive.
     *
     * @param a the inclusive lower bound of the generated {@code Rational}s
     * @param b the inclusive upper bound of the generated {@code Rational}s
     */
    public abstract @NotNull Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b);

    /**
     * Generates {@code Interval}s whose bounds are finite.
     */
    public abstract @NotNull Iterable<Interval> finitelyBoundedIntervals();

    /**
     * Generates {@code Interval}s.
     */
    public abstract @NotNull Iterable<Interval> intervals();

    /**
     * Generates {@code Rational}s contained in a given {@code Interval}.
     *
     * @param a an {@code Interval}
     */
    public @NotNull Iterable<Rational> rationalsIn(@NotNull Interval a) {
        if (!a.getLower().isPresent() && !a.getUpper().isPresent()) {
            return rationals();
        } else if (!a.getLower().isPresent()) {
            return rangeDown(a.getUpper().get());
        } else if (!a.getUpper().isPresent()) {
            return rangeUp(a.getLower().get());
        } else {
            return range(a.getLower().get(), a.getUpper().get());
        }
    }

    /**
     * Generates {@code Rational}s not contained in a given {@code Interval}.
     *
     * @param a an {@code Interval}
     */
    public abstract @NotNull Iterable<Rational> rationalsNotIn(@NotNull Interval a);

    /**
     * Generates {@code Vector}s with a given dimension.
     *
     * @param dimension the dimension of the generated {@code Vector}s
     */
    public @NotNull Iterable<Vector> vectors(int dimension) {
        return map(Vector::of, lists(dimension, bigIntegers()));
    }

    /**
     * Generates {@code Vector}s.
     */
    public abstract @NotNull Iterable<Vector> vectors();

    /**
     * Generates {@code Vector}s with a minimum dimension.
     *
     * @param minDimension the minimum dimension of the generated {@code Vector}s
     */
    public abstract @NotNull Iterable<Vector> vectorsAtLeast(int minDimension);

    /**
     * Generates {@code RationalVector}s with a given dimension.
     *
     * @param dimension the dimension of the generated {@code RationalVector}s
     */
    public @NotNull Iterable<RationalVector> rationalVectors(int dimension) {
        return map(RationalVector::of, lists(dimension, rationals()));
    }

    /**
     * Generates {@code RationalVector}s.
     */
    public abstract @NotNull Iterable<RationalVector> rationalVectors();

    /**
     * Generates {@code PolynomialVector}s with a minimum dimension.
     *
     * @param minDimension the minimum dimension of the generated {@code PolynomialVector}s
     */
    public abstract @NotNull Iterable<PolynomialVector> polynomialVectorsAtLeast(int minDimension);

    /**
     * Generates {@code PolynomialVector}s with a given dimension.
     *
     * @param dimension the dimension of the generated {@code PolynomialVector}s
     */
    public @NotNull Iterable<PolynomialVector> polynomialVectors(int dimension) {
        return map(PolynomialVector::of, lists(dimension, polynomials()));
    }

    /**
     * Generates {@code PolynomialVector}s.
     */
    public abstract @NotNull Iterable<PolynomialVector> polynomialVectors();

    /**
     * Generates {@code RationalPolynomialVector}s with a minimum dimension.
     *
     * @param minDimension the minimum dimension of the generated {@code RationalPolynomialVector}s
     */
    public abstract @NotNull Iterable<RationalPolynomialVector> rationalPolynomialVectorsAtLeast(int minDimension);

    /**
     * Generates {@code RationalPolynomialVector}s with a given dimension.
     *
     * @param dimension the dimension of the generated {@code RationalPolynomialVector}s
     */
    public @NotNull Iterable<RationalPolynomialVector> rationalPolynomialVectors(int dimension) {
        return map(RationalPolynomialVector::of, lists(dimension, rationalPolynomials()));
    }

    /**
     * Generates {@code RationalPolynomialVector}s.
     */
    public abstract @NotNull Iterable<RationalPolynomialVector> rationalPolynomialVectors();

    /**
     * Generates {@code RationalVector}s with a minimum dimension.
     *
     * @param minDimension the minimum dimension of the generated {@code RationalVector}s
     */
    public abstract @NotNull Iterable<RationalVector> rationalVectorsAtLeast(int minDimension);

    /**
     * A helper method which takes an {@code Iterable} of {@code BigInteger} {@code List}s and transforms it into an
     * {@code Iterable} of reduced {@code RationalVector}s.
     *
     * <ul>
     *  <li>{@code bigIntegerLists} cannot contain nulls or elements that contain nulls. It must also contain
     *  infinitely many {@code List}s that have a GCD of zero or one and whose first nonzero value, if it exists, is
     *  positive.</li>
     *  <li>The result is a non-removable {@code Iterable} of reduced {@code RationalVector}s.</li>
     * </ul>
     *
     * Length is infinite.
     *
     * @param bigIntegerLists the source list
     * @return the resulting {@code RationalVector}s
     */
    protected static @NotNull Iterable<RationalVector> reducedRationalVectors(
            @NotNull Iterable<List<BigInteger>> bigIntegerLists
    ) {
        return map(
                RationalVector::reduce,
                filterInfinite(
                        v -> {
                            Optional<Rational> pivot = v.pivot();
                            return !pivot.isPresent() || pivot.get().signum() == 1;
                        },
                        map(
                                is -> RationalVector.of(toList(map(Rational::of, is))),
                                filterInfinite(
                                        js -> {
                                            BigInteger gcd = MathUtils.gcd(js);
                                            return gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE);
                                        },
                                        bigIntegerLists
                                )
                        )
                )
        );
    }

    /**
     * Generates reduced {@code RationalVector}s (see {@link RationalVector#reduce()}) with a given dimension.
     *
     * @param dimension the dimension of the generated {@code RationalVector}s
     */
    public abstract @NotNull Iterable<RationalVector> reducedRationalVectors(int dimension);

    /**
     * Generates reduced {@code RationalVector}s (see {@link RationalVector#reduce()}).
     */
    public abstract @NotNull Iterable<RationalVector> reducedRationalVectors();

    /**
     * Generates reduced {@code RationalVector}s (see {@link RationalVector#reduce()}) with a minimum dimension.
     *
     * @param minDimension the minimum dimension of the generated {@code RationalVector}s
     */
    public abstract @NotNull Iterable<RationalVector> reducedRationalVectorsAtLeast(int minDimension);

    /**
     * Generates {@code Matrix}es with a given {@code height} and {@code width}.
     *
     * @param height the height (number of rows) of the generated {@code Matrix}es
     * @param width the width (number of columns) of the generated {@code Matrix}es
     */
    public abstract @NotNull Iterable<Matrix> matrices(int height, int width);

    /**
     * Generates {@code Matrix}es.
     */
    public abstract @NotNull Iterable<Matrix> matrices();

    /**
     * Generates square {@code Matrix}es.
     */
    public abstract @NotNull Iterable<Matrix> squareMatrices();

    /**
     * Generates invertible {@code Matrix}es.
     */
    public @NotNull Iterable<Matrix> invertibleMatrices() {
        return filter(Matrix::isInvertible, squareMatrices());
    }

    /**
     * Generates {@code RationalMatrix}es with a given {@code height} and {@code width}.
     *
     * @param height the height (number of rows) of the generated {@code RationalMatrix}es
     * @param width the width (number of columns) of the generated {@code RationalMatrix}es
     */
    public abstract @NotNull Iterable<RationalMatrix> rationalMatrices(int height, int width);

    /**
     * Generates {@code RationalMatrix}es.
     */
    public abstract @NotNull Iterable<RationalMatrix> rationalMatrices();

    /**
     * Generates square {@code RationalMatrix}es.
     */
    public abstract @NotNull Iterable<RationalMatrix> squareRationalMatrices();

    /**
     * Generates invertible {@code RationalMatrix}es.
     */
    public @NotNull Iterable<RationalMatrix> invertibleRationalMatrices() {
        return filter(RationalMatrix::isInvertible, squareRationalMatrices());
    }

    /**
     * Generates {@code PolynomialMatrix}es with a given {@code height} and {@code width}.
     *
     * @param height the height (number of rows) of the generated {@code PolynomialMatrix}es
     * @param width the width (number of columns) of the generated {@code PolynomialMatrix}es
     */
    public abstract @NotNull Iterable<PolynomialMatrix> polynomialMatrices(int height, int width);

    /**
     * Generates {@code PolynomialMatrix}es.
     */
    public abstract @NotNull Iterable<PolynomialMatrix> polynomialMatrices();

    /**
     * Generates square {@code PolynomialMatrix}es.
     */
    public abstract @NotNull Iterable<PolynomialMatrix> squarePolynomialMatrices();

    /**
     * Generates {@code RationalPolynomialMatrix}es with a given {@code height} and {@code width}.
     *
     * @param height the height (number of rows) of the generated {@code RationalPolynomialMatrix}es
     * @param width the width (number of columns) of the generated {@code RationalPolynomialMatrix}es
     */
    public abstract @NotNull Iterable<RationalPolynomialMatrix> rationalPolynomialMatrices(int height, int width);

    /**
     * Generates {@code RationalPolynomialMatrix}es.
     */
    public abstract @NotNull Iterable<RationalPolynomialMatrix> rationalPolynomialMatrices();

    /**
     * Generates square {@code RationalPolynomialMatrix}es.
     */
    public abstract @NotNull Iterable<RationalPolynomialMatrix> squareRationalPolynomialMatrices();

    /**
     * Generates {@code Polynomial}s with a given degree.
     *
     * @param degree the degree of the generated {@code Polynomial}s
     */
    public @NotNull Iterable<Polynomial> polynomials(int degree) {
        return map(
                js -> Polynomial.of(toList(js)),
                filter(is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO), lists(degree + 1, bigIntegers()))
        );
    }

    /**
     * Generates {@code Polynomial}s.
     */
    public abstract @NotNull Iterable<Polynomial> polynomials();

    /**
     * Generates {@code Polynomial}s with a minimum degree.
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     */
    public abstract @NotNull Iterable<Polynomial> polynomialsAtLeast(int minDegree);

    /**
     * Generates primitive {@code Polynomial}s with a given degree.
     *
     * @param degree the degree of the generated {@code Polynomial}s
     */
    public abstract @NotNull Iterable<Polynomial> primitivePolynomials(int degree);

    /**
     * A helper method which takes an {@code Iterable} of {@code BigInteger} {@code List}s and transforms it into an
     * {@code Iterable} of primitive {@code Polynomial}s.
     *
     * <ul>
     *  <li>{@code bigIntegerLists} cannot contain nulls or elements that contain nulls. It must also contain
     *  infinitely many {@code List}s that have a GCD of zero or one.</li>
     *  <li>The result is a non-removable {@code Iterable} of primitive {@code Polynomial}s.</li>
     * </ul>
     *
     * Length is infinite
     *
     * @param bigIntegerLists the source list
     * @return the resulting {@code Polynomial}s
     */
    protected static @NotNull Iterable<Polynomial> primitivePolynomials(
            @NotNull Iterable<List<BigInteger>> bigIntegerLists
    ) {
        return map(
                Polynomial::of,
                filterInfinite(
                        is -> !last(is).equals(BigInteger.ZERO) && MathUtils.gcd(is).equals(BigInteger.ONE),
                        bigIntegerLists
                )
        );
    }

    /**
     * Generates primitive {@code Polynomial}s.
     */
    public abstract @NotNull Iterable<Polynomial> primitivePolynomials();

    /**
     * Generates primitive {@code Polynomial}s with a minimum degree.
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     */
    public abstract @NotNull Iterable<Polynomial> primitivePolynomialsAtLeast(int minDegree);

    /**
     * Generates primitive {@code Polynomial}s with positive leading coefficients with a given degree.
     *
     * @param degree the degree of the generated {@code Polynomial}s
     */
    public abstract @NotNull Iterable<Polynomial> positivePrimitivePolynomials(int degree);

    /**
     * Generates primitive {@code Polynomial}s with positive leading coefficients.
     */
    public abstract @NotNull Iterable<Polynomial> positivePrimitivePolynomials();

    /**
     * Generates primitive {@code Polynomial}s with positive leading coefficients with a minimum degree.
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     */
    public abstract @NotNull Iterable<Polynomial> positivePrimitivePolynomialsAtLeast(int minDegree);

    /**
     * Generates monic {@code Polynomial}s with a given degree.
     *
     * @param degree the degree of the generated {@code Polynomial}s
     */
    public abstract @NotNull Iterable<Polynomial> monicPolynomials(int degree);

    /**
     * Generates monic {@code Polynomial}s.
     */
    public abstract @NotNull Iterable<Polynomial> monicPolynomials();

    /**
     * Generates monic {@code Polynomial}s with a minimum degree.
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     */
    public abstract @NotNull Iterable<Polynomial> monicPolynomialsAtLeast(int minDegree);

    /**
     * Generates square-free {@code Polynomial}s with a given degree.
     *
     * @param degree the degree of the generated {@code Polynomial}s
     */
    public abstract @NotNull Iterable<Polynomial> squareFreePolynomials(int degree);

    /**
     * Generates square-free {@code Polynomial}s.
     */
    public @NotNull Iterable<Polynomial> squareFreePolynomials() {
        return filter(Polynomial::isSquareFree, polynomials());
    }

    /**
     * Generates square-free {@code Polynomial}s with a minimum degree.
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     */
    public @NotNull Iterable<Polynomial> squareFreePolynomialsAtLeast(int minDegree) {
        return filterInfinite(Polynomial::isSquareFree, polynomialsAtLeast(minDegree));
    }

    /**
     * Generates primitive square-free {@code Polynomial}s with positive leading coefficients with a given degree.
     *
     * @param degree the degree of the generated {@code Polynomial}s
     */
    public @NotNull Iterable<Polynomial> positivePrimitiveSquareFreePolynomials(int degree) {
        return filter(Polynomial::isSquareFree, positivePrimitivePolynomials(degree));
    }

    /**
     * Generates primitive square-free {@code Polynomial}s with positive leading coefficients.
     */
    public @NotNull Iterable<Polynomial> positivePrimitiveSquareFreePolynomials() {
        return filter(Polynomial::isSquareFree, positivePrimitivePolynomials());
    }

    /**
     * Generates primitive square-free {@code Polynomial}s with positive leading coefficients with a minimum degree.
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     */
    public @NotNull Iterable<Polynomial> positivePrimitiveSquareFreePolynomialsAtLeast(int minDegree) {
        return filterInfinite(Polynomial::isSquareFree, positivePrimitivePolynomialsAtLeast(minDegree));
    }

    /**
     * Generates irreducible {@code Polynomial}s with a given degree.
     *
     * @param degree the degree of the generated {@code Polynomial}s
     */
    public @NotNull Iterable<Polynomial> irreduciblePolynomials(int degree) {
        return filter(p -> p != Polynomial.ZERO && p.isIrreducible(), positivePrimitivePolynomials(degree));
    }

    /**
     * Generates irreducible {@code Polynomial}s.
     */
    public abstract @NotNull Iterable<Polynomial> irreduciblePolynomials();

    /**
     * Generates irreducible {@code Polynomial}s with a minimum degree.
     *
     * @param minDegree the minimum degree of the generated {@code Polynomial}s
     */
    public abstract @NotNull Iterable<Polynomial> irreduciblePolynomialsAtLeast(int minDegree);

    /**
     * Generates {@code RationalPolynomial}s with a given degree.
     *
     * @param degree the degree of the generated {@code RationalPolynomial}s
     */
    public @NotNull Iterable<RationalPolynomial> rationalPolynomials(int degree) {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(is -> is.isEmpty() || last(is) != Rational.ZERO, lists(degree + 1, rationals()))
        );
    }

    /**
     * Generates {@code RationalPolynomial}s.
     */
    public abstract @NotNull Iterable<RationalPolynomial> rationalPolynomials();

    /**
     * Generates {@code RationalPolynomial}s with a minimum degree.
     *
     * @param minDegree the minimum degree of the generated {@code RationalPolynomial}s
     */
    public abstract @NotNull Iterable<RationalPolynomial> rationalPolynomialsAtLeast(int minDegree);

    /**
     * Generates monic {@code RationalPolynomial}s with a given degree.
     *
     * @param degree the degree of the generated {@code RationalPolynomial}s
     */
    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomials(int degree) {
        return map(p -> p.toRationalPolynomial().makeMonic(), positivePrimitivePolynomials(degree));
    }

    /**
     * Generates monic {@code RationalPolynomial}s.
     */
    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomials() {
        return map(p -> p.toRationalPolynomial().makeMonic(), positivePrimitivePolynomials());
    }

    /**
     * Generates monic {@code RationalPolynomial}s with a minimum degree.
     *
     * @param minDegree the minimum degree of the generated {@code RationalPolynomial}s
     */
    public @NotNull Iterable<RationalPolynomial> monicRationalPolynomialsAtLeast(int minDegree) {
        return map(p -> p.toRationalPolynomial().makeMonic(), positivePrimitivePolynomialsAtLeast(minDegree));
    }

    /**
     * Generates {@code Variable}s.
     */
    public @NotNull Iterable<Variable> variables() {
        return map(Variable::of, naturalIntegersGeometric());
    }

    /**
     * Generates {@code MonomialOrder}s.
     */
    public abstract @NotNull Iterable<MonomialOrder> monomialOrders();

    /**
     * Generates {@code Monomial}s.
     */
    public abstract @NotNull Iterable<Monomial> monomials();

    /**
     * Generates {@code Monomial}s containing only (a subset of) the given variables.
     *
     * <ul>
     *  <li>{@code variables} must be in increasing order and cannot contain repetitions.</li>
     * </ul>
     *
     * @param variables the allowed variables in the result
     */
    public @NotNull Iterable<Monomial> monomials(@NotNull List<Variable> variables) {
        if (any(v -> v == null, variables)) {
            throw new NullPointerException();
        }
        if (!increasing(variables)) {
            throw new IllegalArgumentException("variables must be in increasing order and cannot contain " +
                    "repetitions. Invalid variables: " + variables);
        }
        List<Variable> reversed = reverse(variables);
        return map(
                es -> Monomial.fromTerms(toList(filter(p -> p.b != 0, zip(reversed, es)))),
                lists(variables.size(), naturalIntegersGeometric())
        );
    }

    /**
     * Generates {@code MultivariatePolynomials}.
     */
    public abstract @NotNull Iterable<MultivariatePolynomial> multivariatePolynomials();

    /**
     * Generates {@code MultivariatePolynomial}s containing only (a subset of) the given variables.
     *
     * <ul>
     *  <li>{@code variables} must be in increasing order and cannot contain repetitions.</li>
     * </ul>
     *
     * @param variables the allowed variables in the result
     */
    public abstract @NotNull Iterable<MultivariatePolynomial> multivariatePolynomials(
            @NotNull List<Variable> variables
    );

    public abstract @NotNull Iterable<Real> reals();

    /**
     * Generates positive {@code Algebraic}s with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     * </ul>
     *
     * @param degree the degree of the {@code Algebraic}s in the result
     */
    public @NotNull Iterable<Algebraic> positiveAlgebraics(int degree) {
        if (degree < 1) {
            throw new IllegalArgumentException("degree must be positive. Invalid degree: " + degree);
        }
        return map(
                x -> x.negate().invert(),
                filterInfinite(
                        x -> x.signum() == -1,
                        map(
                                p -> Algebraic.of(p.a, p.b),
                                filterInfinite(
                                        q -> q.b != -1,
                                        dependentPairs(
                                                irreduciblePolynomials(degree),
                                                q -> {
                                                    int rootCount = q.rootCount();
                                                    return rootCount == 0 ? range(-1, -1) : range(0, rootCount - 1);
                                                }
                                        )
                                )
                        )
                )
        );
    }

    /**
     * Generates positive {@code Algebraic}s.
     */
    public abstract @NotNull Iterable<Algebraic> positiveAlgebraics();

    /**
     * Generates negative {@code Algebraic}s with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     * </ul>
     *
     * @param degree the degree of the {@code Algebraic}s in the result
     */
    public @NotNull Iterable<Algebraic> negativeAlgebraics(int degree) {
        if (degree < 1) {
            throw new IllegalArgumentException("degree must be positive. Invalid degree: " + degree);
        }
        return map(
                Algebraic::invert,
                filterInfinite(
                        x -> x.signum() == -1,
                        map(
                                p -> Algebraic.of(p.a, p.b),
                                filterInfinite(
                                        q -> q.b != -1,
                                        dependentPairs(
                                                irreduciblePolynomials(degree),
                                                q -> {
                                                    int rootCount = q.rootCount();
                                                    return rootCount == 0 ? range(-1, -1) : range(0, rootCount - 1);
                                                }
                                        )
                                )
                        )
                )
        );
    }

    /**
     * Generates negative {@code Algebraic}s.
     */
    public abstract @NotNull Iterable<Algebraic> negativeAlgebraics();

    /**
     * Generates nonzero {@code Algebraic}s with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     * </ul>
     *
     * @param degree the degree of the {@code Algebraic}s in the result
     */
    public @NotNull Iterable<Algebraic> nonzeroAlgebraics(int degree) {
        return degree == 1 ? filterInfinite(x -> x != Algebraic.ZERO, algebraics(1)) : algebraics(degree);
    }

    /**
     * Generates nonzero {@code Algebraic}s.
     */
    public abstract @NotNull Iterable<Algebraic> nonzeroAlgebraics();

    /**
     * Generates {@code Algebraic}s with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     * </ul>
     *
     * @param degree the degree of the {@code Algebraic}s in the result
     */
    public @NotNull Iterable<Algebraic> algebraics(int degree) {
        if (degree < 1) {
            throw new IllegalArgumentException("degree must be positive. Invalid degree: " + degree);
        }
        return map(
                p -> {
                    Algebraic x = Algebraic.of(p.a, p.b);
                    return x == Algebraic.ZERO ? x : x.negate().invert();
                },
                filterInfinite(
                        q -> q.b != -1,
                        dependentPairs(
                                irreduciblePolynomials(degree),
                                q -> {
                                    int rootCount = q.rootCount();
                                    return rootCount == 0 ? range(-1, -1) : range(0, rootCount - 1);
                                }
                        )
                )
        );
    }

    /**
     * Generates {@code Algebraic}s.
     */
    public abstract @NotNull Iterable<Algebraic> algebraics();

    /**
     * Generates {@code Algebraic}s in the interval [0, 1) with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     * </ul>
     *
     * @param degree the degree of the {@code Algebraic}s in the result
     */
    public @NotNull Iterable<Algebraic> nonNegativeAlgebraicsLessThanOne(int degree) {
        if (degree < 1) {
            throw new IllegalArgumentException("degree must be positive. Invalid degree: " + degree);
        }
        return filterInfinite(
                x -> x.signum() != -1 && lt(x, Algebraic.ONE),
                map(
                        p -> Algebraic.of(p.a, p.b),
                        filterInfinite(
                                q -> q.b != -1,
                                dependentPairs(
                                        irreduciblePolynomials(degree),
                                        q -> {
                                            int rootCount = q.rootCount();
                                            return rootCount == 0 ? range(-1, -1) : range(0, rootCount - 1);
                                        }
                                )
                        )
                )
        );
    }

    /**
     * Generates {@code Algebraic}s in the interval [0, 1).
     */
    public abstract @NotNull Iterable<Algebraic> nonNegativeAlgebraicsLessThanOne();

    /**
     * Generates {@code Algebraic}s greater than or equal to a given value and with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     *  <li>{@code a} cannot be null.</li>
     * </ul>
     *
     * @param degree the degree of the {@code Algebraic}s in the result
     * @param a the inclusive lower bound of the generated {@code Algebraic}s
     */
    public abstract @NotNull Iterable<Algebraic> rangeUp(int degree, @NotNull Algebraic a);

    /**
     * Generates {@code Algebraic}s greater than or equal to a given value.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code Algebraic}s
     */
    public abstract @NotNull Iterable<Algebraic> rangeUp(@NotNull Algebraic a);

    /**
     * Generates {@code Algebraic}s less than or equal to a given value and with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     *  <li>{@code a} cannot be null.</li>
     * </ul>
     *
     * @param degree the degree of the {@code Algebraic}s in the result
     * @param a the inclusive upper bound of the generated {@code Algebraic}s
     */
    public abstract @NotNull Iterable<Algebraic> rangeDown(int degree, @NotNull Algebraic a);

    /**
     * Generates {@code Algebraic}s less than or equal to a given value.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     * </ul>
     *
     * @param a the inclusive upper bound of the generated {@code Algebraic}s
     */
    public abstract @NotNull Iterable<Algebraic> rangeDown(@NotNull Algebraic a);

    /**
     * Generates {@code Algebraic}s between {@code a} and {@code b}, inclusive, and with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     * </ul>
     *
     * @param degree the degree of the {@code Algebraic}s in the result
     * @param a the inclusive lower bound of the generated {@code Algebraic}s
     * @param b the inclusive upper bound of the generated {@code Algebraic}s
     */
    public abstract @NotNull Iterable<Algebraic> range(int degree, @NotNull Algebraic a, @NotNull Algebraic b);

    /**
     * Generates {@code Algebraic}s between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code Algebraic}s
     * @param b the inclusive upper bound of the generated {@code Algebraic}s
     */
    public abstract @NotNull Iterable<Algebraic> range(@NotNull Algebraic a, @NotNull Algebraic b);

    /**
     * Generates {@code Algebraic}s contained in a given {@code Interval} and with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     *  <li>{@code a} cannot be null.</li>
     * </ul>
     *
     * @param degree the degree of the {@code Algebraic}s in the result
     * @param a an {@code Interval}
     */
    public abstract @NotNull Iterable<Algebraic> algebraicsIn(int degree, @NotNull Interval a);

    /**
     * Generates {@code Algebraic}s contained in a given {@code Interval}.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     * </ul>
     *
     * @param a an {@code Interval}
     */
    public abstract @NotNull Iterable<Algebraic> algebraicsIn(@NotNull Interval a);

    /**
     * Generates {@code Algebraic}s not contained in a given {@code Interval} and with a given degree.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     *  <li>{@code a} cannot be null.</li>
     * </ul>
     *
     * @param a an {@code Interval}
     */
    public abstract @NotNull Iterable<Algebraic> algebraicsNotIn(int degree, @NotNull Interval a);

    /**
     * Generates {@code Algebraic}s not contained in a given {@code Interval}.
     *
     * <ul>
     *  <li>{@code degree} must be positive.</li>
     *  <li>{@code a} cannot be (–∞, ∞).</li>
     * </ul>
     *
     * @param a an {@code Interval}
     */
    public abstract @NotNull Iterable<Algebraic> algebraicsNotIn(@NotNull Interval a);

    public @NotNull Iterable<QBarRandomProvider> qbarRandomProvidersFixedScales(
            int scale,
            int secondaryScale,
            int tertiaryScale
    ) {
        return map(
                rp -> (QBarRandomProvider) new QBarRandomProvider(rp.getSeed())
                        .withScale(scale).withSecondaryScale(secondaryScale).withTertiaryScale(tertiaryScale),
                wheelsProvider.randomProvidersDefault()
        );
    }

    public @NotNull Iterable<QBarRandomProvider> qbarRandomProvidersDefault() {
        return map(rp -> new QBarRandomProvider(rp.getSeed()), wheelsProvider.randomProvidersDefault());
    }

    public @NotNull Iterable<QBarRandomProvider> qbarRandomProvidersDefaultSecondaryAndTertiaryScale() {
        return map(
                rp -> (QBarRandomProvider) new QBarRandomProvider(rp.getSeed()).withScale(rp.getScale()),
                wheelsProvider.randomProvidersDefaultSecondaryAndTertiaryScale()
        );
    }

    public @NotNull Iterable<QBarRandomProvider> qbarRandomProvidersDefaultTertiaryScale() {
        return map(
                rp -> (QBarRandomProvider) new QBarRandomProvider(rp.getSeed()).withScale(rp.getScale())
                        .withSecondaryScale(rp.getSecondaryScale()),
                wheelsProvider.randomProvidersDefaultTertiaryScale()
        );
    }

    public @NotNull Iterable<QBarRandomProvider> qbarRandomProviders() {
        return map(
                rp -> (QBarRandomProvider) new QBarRandomProvider(rp.getSeed())
                        .withScale(rp.getScale()).withSecondaryScale(rp.getSecondaryScale())
                        .withTertiaryScale(rp.getTertiaryScale()),
                wheelsProvider.randomProviders()
        );
    }
}
