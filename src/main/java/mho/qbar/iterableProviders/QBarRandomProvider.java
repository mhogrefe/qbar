package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.BinaryFraction;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.NullableOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.ordering.Ordering.lt;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unused")
public final strictfp class QBarRandomProvider extends QBarIterableProvider {
    private QBarRandomProvider(@NotNull RandomProvider randomProvider) {
        super(randomProvider);
    }

    public QBarRandomProvider() {
        super(new RandomProvider());
    }

    public QBarRandomProvider(List<Integer> seed) {
        super(new RandomProvider(seed));
    }

    public static @NotNull QBarRandomProvider example() {
        return new QBarRandomProvider(RandomProvider.example());
    }

    /**
     * Returns {@code this}'s scale parameter.
     *
     * <ul>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return the scale parameter of {@code this}
     */
    public int getScale() {
        return ((RandomProvider) wheelsProvider).getScale();
    }

    /**
     * Returns {@code this}'s other scale parameter.
     *
     * <ul>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return the other scale parameter of {@code this}
     */
    public int getSecondaryScale() {
        return ((RandomProvider) wheelsProvider).getSecondaryScale();
    }

    /**
     * Returns {@code this}'s seed. Makes a defensive copy.
     *
     * <ul>
     *  <li>The result is an array of {@link mho.wheels.random.IsaacPRNG#SIZE} {@code int}s.</li>
     * </ul>
     *
     * @return the seed of {@code this}
     */
    public @NotNull List<Integer> getSeed() {
        return ((RandomProvider) wheelsProvider).getSeed();
    }

    /**
     * A {@code QBarRandomProvider} with the same fields as {@code this}. The copy shares its PRNG with the original.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return A copy of {@code this}.
     */
    @Override
    public @NotNull QBarRandomProvider copy() {
        return new QBarRandomProvider(((RandomProvider) wheelsProvider).copy());
    }

    /**
     * A {@code QBarRandomProvider} with the same fields as {@code this}. The copy receives a new copy of the PRNG, so
     * generating values from the copy will not affect the state of the original's PRNG.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return A copy of {@code this}.
     */
    @Override
    public @NotNull QBarRandomProvider deepCopy() {
        return new QBarRandomProvider(((RandomProvider) wheelsProvider).deepCopy());
    }

    /**
     * A {@code QBarRandomProvider} with the same fields as {@code this} except for a new scale.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param scale the new scale
     * @return A copy of {@code this} with a new scale
     */
    @Override
    public @NotNull QBarIterableProvider withScale(int scale) {
        return new QBarRandomProvider((RandomProvider) wheelsProvider.withScale(scale));
    }

    /**
     * A {@code QBarRandomProvider} with the same fields as {@code this} except for a new secondary scale.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>{@code secondaryScale} mat be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param secondaryScale the new secondary scale
     * @return A copy of {@code this} with a new secondary scale
     */
    @Override
    public @NotNull QBarIterableProvider withSecondaryScale(int secondaryScale) {
        return new QBarRandomProvider((RandomProvider) wheelsProvider.withScale(secondaryScale));
    }

    /**
     * Returns an id which has a good chance of being different in two instances with unequal {@code prng}s. It's used
     * in {@link QBarRandomProvider#toString()} to distinguish between different {@code QBarRandomProvider} instances.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>{@code this} may be any {@code long}.</li>
     * </ul>
     */
    public long getId() {
        return ((RandomProvider) wheelsProvider).getId();
    }

    /**
     * Returns a randomly-generated {@code int} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return an {@code int}
     */
    public int nextInt() {
        return ((RandomProvider) wheelsProvider).nextInt();
    }

    /**
     * Returns a randomly-generated {@code long} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>The result may be any {@code long}.</li>
     * </ul>
     *
     * @return a {@code long}
     */
    public long nextLong() {
        return ((RandomProvider) wheelsProvider).nextLong();
    }

    /**
     * Returns a randomly-generated {@code boolean} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return a {@code boolean}
     */
    public boolean nextBoolean() {
        return ((RandomProvider) wheelsProvider).nextBoolean();
    }

    /**
     * Returns a randomly-generated value taken from a given list.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>{@code xs} cannot be empty.</li>
     *  <li>The result may be any value of type {@code T}, or null.</li>
     * </ul>
     *
     * @param xs the source list
     * @param <T> the type of {@code xs}'s elements
     * @return a value from {@code xs}
     */
    public <T> T nextUniformSample(@NotNull List<T> xs) {
        return ((RandomProvider) wheelsProvider).nextUniformSample(xs);
    }

    /**
     * Returns a randomly-generated character taken from a given {@code String}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>{@code s} cannot be empty.</li>
     *  <li>The result may be any {@code char}.</li>
     * </ul>
     *
     * @param s the source {@code String}
     * @return a {@code char} from {@code s}
     */
    public char nextUniformSample(@NotNull String s) {
        return ((RandomProvider) wheelsProvider).nextUniformSample(s);
    }

    /**
     * Returns a randomly-generated {@code Ordering} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return an {@code Ordering}
     */
    public @NotNull Ordering nextOrdering() {
        return ((RandomProvider) wheelsProvider).nextOrdering();
    }

    /**
     * Returns a randomly-generated {@code RoundingMode} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return a {@code RoundingMode}
     */
    public @NotNull RoundingMode nextRoundingMode() {
        return ((RandomProvider) wheelsProvider).nextRoundingMode();
    }

    /**
     * Returns a randomly-generated positive {@code byte} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is positive.</li>
     * </ul>
     *
     * @return a positive {@code byte}
     */
    public byte nextPositiveByte() {
        return ((RandomProvider) wheelsProvider).nextPositiveByte();
    }

    /**
     * Returns a randomly-generated positive {@code short} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is positive.</li>
     * </ul>
     *
     * @return a positive {@code short}
     */
    public short nextPositiveShort() {
        return ((RandomProvider) wheelsProvider).nextPositiveShort();
    }

    /**
     * Returns a randomly-generated positive {@code int} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is positive.</li>
     * </ul>
     *
     * @return a positive {@code int}
     */
    public int nextPositiveInt() {
        return ((RandomProvider) wheelsProvider).nextPositiveInt();
    }

    /**
     * Returns a randomly-generated positive {@code long} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is positive.</li>
     * </ul>
     *
     * @return a positive {@code long}
     */
    public long nextPositiveLong() {
        return ((RandomProvider) wheelsProvider).nextPositiveLong();
    }

    /**
     * Returns a randomly-generated negative {@code byte} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is negative.</li>
     * </ul>
     *
     * @return a negative {@code byte}
     */
    public byte nextNegativeByte() {
        return ((RandomProvider) wheelsProvider).nextNegativeByte();
    }

    /**
     * Returns a randomly-generated negative {@code short} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is negative.</li>
     * </ul>
     *
     * @return a negative {@code short}
     */
    public short nextNegativeShort() {
        return ((RandomProvider) wheelsProvider).nextNegativeShort();
    }

    /**
     * Returns a randomly-generated negative {@code int} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is negative.</li>
     * </ul>
     *
     * @return a negative {@code int}
     */
    public int nextNegativeInt() {
        return ((RandomProvider) wheelsProvider).nextNegativeInt();
    }

    /**
     * Returns a randomly-generated negative {@code long} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is negative.</li>
     * </ul>
     *
     * @return a negative {@code long}
     */
    public long nextNegativeLong() {
        return ((RandomProvider) wheelsProvider).nextNegativeLong();
    }

    /**
     * Returns a randomly-generated nonzero {@code byte} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is not zero.</li>
     * </ul>
     *
     * @return a nonzero {@code byte}
     */
    public byte nextNonzeroByte() {
        return ((RandomProvider) wheelsProvider).nextNonzeroByte();
    }

    /**
     * Returns a randomly-generated nonzero {@code short} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is not zero.</li>
     * </ul>
     *
     * @return a nonzero {@code short}
     */
    public short nextNonzeroShort() {
        return ((RandomProvider) wheelsProvider).nextNonzeroShort();
    }

    /**
     * Returns a randomly-generated nonzero {@code int} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is not zero.</li>
     * </ul>
     *
     * @return a nonzero {@code int}
     */
    public int nextNonzeroInt() {
        return ((RandomProvider) wheelsProvider).nextNonzeroInt();
    }

    /**
     * Returns a randomly-generated nonzero {@code long} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is not zero.</li>
     * </ul>
     *
     * @return a nonzero {@code long}
     */
    public long nextNonzeroLong() {
        return ((RandomProvider) wheelsProvider).nextNonzeroLong();
    }

    /**
     * Returns a randomly-generated natural (non-negative) {@code byte} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @return a natural {@code byte}
     */
    public byte nextNaturalByte() {
        return ((RandomProvider) wheelsProvider).nextNaturalByte();
    }

    /**
     * Returns a randomly-generated natural (non-negative) {@code short} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @return a natural {@code short}
     */
    public short nextNaturalShort() {
        return ((RandomProvider) wheelsProvider).nextNaturalShort();
    }

    /**
     * Returns a randomly-generated natural (non-negative) {@code int} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @return a natural {@code int}
     */
    public int nextNaturalInt() {
        return ((RandomProvider) wheelsProvider).nextNaturalInt();
    }

    /**
     * Returns a randomly-generated natural (non-negative) {@code long} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @return a natural {@code long}
     */
    public long nextNaturalLong() {
        return ((RandomProvider) wheelsProvider).nextNaturalLong();
    }

    /**
     * Returns a randomly-generated {@code byte} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result may be any {@code byte}.</li>
     * </ul>
     *
     * @return a {@code byte}
     */
    public byte nextByte() {
        return ((RandomProvider) wheelsProvider).nextByte();
    }

    /**
     * Returns a randomly-generated {@code short} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result may be any {@code short}.</li>
     * </ul>
     *
     * @return a {@code short}
     */
    public short nextShort() {
        return ((RandomProvider) wheelsProvider).nextShort();
    }

    /**
     * Returns a randomly-generated ASCII {@code char} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result is an ASCII {@code char}.</li>
     * </ul>
     *
     * @return an ASCII {@code char}
     */
    public char nextAsciiChar() {
        return ((RandomProvider) wheelsProvider).nextAsciiChar();
    }

    /**
     * Returns a randomly-generated {@code char} from a uniform distribution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>The result may be any {@code char}.</li>
     * </ul>
     *
     * @return a {@code char}
     */
    public char nextChar() {
        return ((RandomProvider) wheelsProvider).nextChar();
    }

    /**
     * Returns a randomly-generated {@code byte} greater than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code byte}.</li>
     *  <li>The result may be any {@code byte}.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code byte}
     * @return a {@code byte} greater than or equal to {@code a}
     */
    public byte nextFromRangeUp(byte a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeUp(a);
    }

    /**
     * Returns a randomly-generated {@code short} greater than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code short}.</li>
     *  <li>The result may be any {@code short}.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code short}
     * @return a {@code short} greater than or equal to {@code a}
     */
    public short nextFromRangeUp(short a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeUp(a);
    }

    /**
     * Returns a randomly-generated {@code int} greater than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code int}
     * @return an {@code int} greater than or equal to {@code a}
     */
    public int nextFromRangeUp(int a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeUp(a);
    }

    /**
     * Returns a randomly-generated {@code long} greater than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code long}.</li>
     *  <li>The result may be any {@code long}.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code long}
     * @return a {@code long} greater than or equal to {@code a}
     */
    public long nextFromRangeUp(long a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeUp(a);
    }

    /**
     * Returns a randomly-generated {@code char} greater than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code char}.</li>
     *  <li>The result may be any {@code char}.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code char}
     * @return a {@code char} greater than or equal to {@code a}
     */
    public char nextFromRangeUp(char a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeUp(a);
    }

    /**
     * Returns a randomly-generated {@code byte} less than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code byte}.</li>
     *  <li>The result may be any {@code byte}.</li>
     * </ul>
     *
     * @param a the inclusive upper bound of the generated {@code byte}
     * @return a {@code byte} less than or equal to {@code a}
     */
    public byte nextFromRangeDown(byte a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeDown(a);
    }

    /**
     * Returns a randomly-generated {@code short} less than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code short}.</li>
     *  <li>The result may be any {@code short}.</li>
     * </ul>
     *
     * @param a the inclusive upper bound of the generated {@code short}
     * @return a {@code short} less than or equal to {@code a}
     */
    public short nextFromRangeDown(short a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeDown(a);
    }

    /**
     * Returns a randomly-generated {@code int} less than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @param a the inclusive upper bound of the generated {@code int}
     * @return an {@code int} less than or equal to {@code a}
     */
    public int nextFromRangeDown(int a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeDown(a);
    }

    /**
     * Returns a randomly-generated {@code long} less than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code long}.</li>
     *  <li>The result may be any {@code long}.</li>
     * </ul>
     *
     * @param a the inclusive upper bound of the generated {@code long}
     * @return a {@code long} less than or equal to {@code a}
     */
    public long nextFromRangeDown(long a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeDown(a);
    }

    /**
     * Returns a randomly-generated {@code char} less than or equal to {@code a}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code char}.</li>
     *  <li>The result may be any {@code char}.</li>
     * </ul>
     *
     * @param a the inclusive upper bound of the generated {@code char}
     * @return a {@code char} less than or equal to {@code a}
     */
    public char nextFromRangeDown(char a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeDown(a);
    }

    /**
     * Returns a randomly-generated {@code byte} between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code byte}.</li>
     *  <li>{@code b} may be any {@code byte}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result may be any {@code byte}.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code byte}
     * @param b the inclusive upper bound of the generated {@code byte}
     * @return a {@code byte} between {@code a} and {@code b}, inclusive
     */
    public byte nextFromRange(byte a, byte b) {
        return ((RandomProvider) wheelsProvider).nextFromRange(a, b);
    }

    /**
     * Returns a randomly-generated {@code short} between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code short}.</li>
     *  <li>{@code b} may be any {@code short}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result may be any {@code short}.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code short}
     * @param b the inclusive upper bound of the generated {@code short}
     * @return a {@code short} between {@code a} and {@code b}, inclusive
     */
    public short nextFromRange(short a, short b) {
        return ((RandomProvider) wheelsProvider).nextFromRange(a, b);
    }

    /**
     * Returns a randomly-generated {@code int} between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>{@code b} may be any {@code int}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code int}
     * @param b the inclusive upper bound of the generated {@code int}
     * @return an {@code int} between {@code a} and {@code b}, inclusive
     */
    public int nextFromRange(int a, int b) {
        return ((RandomProvider) wheelsProvider).nextFromRange(a, b);
    }

    /**
     * Returns a randomly-generated {@code long} between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code long}.</li>
     *  <li>{@code b} may be any {@code long}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result may be any {@code long}.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code long}
     * @param b the inclusive upper bound of the generated {@code long}
     * @return a {@code long} between {@code a} and {@code b}, inclusive
     */
    public long nextFromRange(long a, long b) {
        return ((RandomProvider) wheelsProvider).nextFromRange(a, b);
    }

    /**
     * Returns a randomly-generated {@code BigInteger} between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code BigInteger}.</li>
     *  <li>{@code b} may be any {@code BigInteger}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code BigInteger}
     * @param b the inclusive upper bound of the generated {@code BigInteger}
     * @return a {@code BigInteger} between {@code a} and {@code b}, inclusive
     */
    public @NotNull BigInteger nextFromRange(@NotNull BigInteger a, @NotNull BigInteger b) {
        return ((RandomProvider) wheelsProvider).nextFromRange(a, b);
    }

    /**
     * Returns a randomly-generated {@code char} between {@code a} and {@code b}, inclusive.
     *
     * <ul>
     *  <li>{@code this} may be any {@code RandomProvider}.</li>
     *  <li>{@code a} may be any {@code char}.</li>
     *  <li>{@code b} may be any {@code char}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result may be any {@code char}.</li>
     * </ul>
     *
     * @param a the inclusive lower bound of the generated {@code char}
     * @param b the inclusive upper bound of the generated {@code char}
     * @return a {@code char} between {@code a} and {@code b}, inclusive
     */
    public char nextFromRange(char a, char b) {
        return ((RandomProvider) wheelsProvider).nextFromRange(a, b);
    }

    /**
     * Returns a randomly-generated positive {@code int} from a geometric distribution with mean {@code scale}.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is positive.</li>
     * </ul>
     *
     * @return a positive {@code int}
     */
    public int nextPositiveIntGeometric() {
        return ((RandomProvider) wheelsProvider).nextPositiveIntGeometric();
    }

    /**
     * Returns a randomly-generated negative {@code int} from a geometric distribution with mean {@code scale}.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is negative.</li>
     * </ul>
     *
     * @return a negative {@code int}
     */
    public int nextNegativeIntGeometric() {
        return ((RandomProvider) wheelsProvider).nextNegativeIntGeometric();
    }

    /**
     * Returns a randomly-generated natural {@code int} from a geometric distribution with mean {@code scale}.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale. The scale cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return a natural {@code int}
     */
    public int nextNaturalIntGeometric() {
        return ((RandomProvider) wheelsProvider).nextNaturalIntGeometric();
    }

    /**
     * Returns a randomly-generated nonzero {@code int} from a geometric distribution with mean {@code scale}.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is nonzero.</li>
     * </ul>
     *
     * @return a nonzero {@code int}
     */
    public int nextNonzeroIntGeometric() {
        return ((RandomProvider) wheelsProvider).nextNonzeroIntGeometric();
    }

    /**
     * Returns a randomly-generated {@code int} from a geometric distribution with mean {@code scale}.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale. The scale cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return a negative {@code int}
     */
    public int nextIntGeometric() {
        return ((RandomProvider) wheelsProvider).nextIntGeometric();
    }

    /**
     * Returns a randomly-generated {@code int} greater than or equal to {@code a}, chosen from a geometric
     * distribution with mean {@code scale}.
     *
     * <ul>
     *  <li>{@code this} must have a scale greater than {@code a} and less than {@code Integer.MAX_VALUE}+a.</li>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return an {@code int} greater than or equal to {@code a}
     */
    public int nextIntGeometricFromRangeUp(int a) {
        return ((RandomProvider) wheelsProvider).nextIntGeometricFromRangeUp(a);
    }

    /**
     * Returns a randomly-generated {@code int} less than or equal to {@code a}, chosen from a geometric distribution
     * with mean {@code scale}.
     *
     * <ul>
     *  <li>{@code this} must have a scale less than {@code a} and greater than
     *  {@code a}–{@code Integer.MAX_VALUE}.</li>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>The result may be any {@code int}.</li>
     * </ul>
     *
     * @return an {@code int} less than or equal to {@code a}
     */
    public int nextIntGeometricFromRangeDown(int a) {
        return ((RandomProvider) wheelsProvider).nextIntGeometricFromRangeDown(a);
    }

    /**
     * Returns a randomly-generated positive {@code BigInteger}. The bit size is chosen from a geometric distribution
     * with mean {@code scale}, and then the {@code BigInteger} is chosen uniformly from all {@code BigInteger}s with
     * that bit size.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is positive.</li>
     * </ul>
     *
     * @return a positive {@code BigInteger}
     */
    public @NotNull BigInteger nextPositiveBigInteger() {
        return ((RandomProvider) wheelsProvider).nextPositiveBigInteger();
    }

    /**
     * Returns a randomly-generated negative {@code BigInteger}. The bit size is chosen from a geometric distribution
     * with mean {@code scale}, and then the {@code BigInteger} is chosen uniformly from all {@code BigInteger}s with
     * that bit size.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is negative.</li>
     * </ul>
     *
     * @return a negative {@code BigInteger}
     */
    public @NotNull BigInteger nextNegativeBigInteger() {
        return ((RandomProvider) wheelsProvider).nextNegativeBigInteger();
    }

    /**
     * Returns a randomly-generated natural {@code BigInteger}. The bit size is chosen from a geometric distribution
     * with mean {@code scale}, and then the {@code BigInteger} is chosen uniformly from all {@code BigInteger}s with
     * that bit size.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale. The scale cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return a natural {@code BigInteger}
     */
    public @NotNull BigInteger nextNaturalBigInteger() {
        return ((RandomProvider) wheelsProvider).nextNaturalBigInteger();
    }

    /**
     * Returns a randomly-generated nonzero {@code BigInteger}. The bit size is chosen from a geometric distribution
     * with mean {@code scale}, and then the {@code BigInteger} is chosen uniformly from all {@code BigInteger}s with
     * that bit size.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2.</li>
     *  <li>The result is not zero.</li>
     * </ul>
     *
     * @return a nonzero {@code BigInteger}
     */
    public @NotNull BigInteger nextNonzeroBigInteger() {
        return ((RandomProvider) wheelsProvider).nextNonzeroBigInteger();
    }

    /**
     * Returns a randomly-generated {@code BigInteger}. The bit size is chosen from a geometric distribution with mean
     * {@code scale}, and then the {@code BigInteger} is chosen uniformly from all {@code BigInteger}s with that bit
     * size.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale. The scale cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return a {@code BigInteger}
     */
    public @NotNull BigInteger nextBigInteger() {
        return ((RandomProvider) wheelsProvider).nextBigInteger();
    }

    /**
     * Returns a randomly-generated {@code BigInteger} greater than or equal to {@code a}. The bit size is chosen from
     * a geometric distribution with mean {@code scale}, and then the {@code BigInteger} is chosen uniformly from all
     * {@code BigInteger}s greater than or equal to {@code a} with that bit size.
     *
     * <ul>
     *  <li>Let {@code minBitLength} be 0 if {@code a} is negative, and ⌊log<sub>2</sub>({@code a})⌋ otherwise.
     *  {@code this} must have a scale greater than {@code minBitLength}. If {@code minBitLength} is 0, {@code scale}
     *  cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>{@code a} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return a {@code BigInteger} greater than or equal to {@code a}
     */
    public @NotNull BigInteger nextFromRangeUp(@NotNull BigInteger a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeUp(a);
    }

    /**
     * Returns a randomly-generated {@code BigInteger} less than or equal to {@code a}. The bit size is chosen from a
     * geometric distribution with mean {@code scale}, and then the {@code BigInteger} is chosen uniformly from all
     * {@code BigInteger}s less than or equal to {@code a} with that bit size.
     *
     * <ul>
     *  <li>Let {@code minBitLength} be 0 if {@code a} is positive, and ⌊log<sub>2</sub>(–{@code a})⌋ otherwise.
     *  {@code this} must have a scale greater than {@code minBitLength}. If {@code minBitLength} is 0, {@code scale}
     *  cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>{@code a} may be any {@code BigInteger}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return a {@code BigInteger} less than or equal to {@code a}
     */
    public @NotNull BigInteger nextFromRangeDown(@NotNull BigInteger a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeDown(a);
    }

    /**
     * Returns a randomly-generated positive {@code BinaryFraction}. The mantissa bit size is chosen from a geometric
     * distribution with mean {@code scale}, and then the mantissa is chosen uniformly from all odd positive
     * {@code BigInteger}s with that bit size. The absolute value of the exponent is chosen from a geometric
     * distribution with mean {@code secondaryScale}, and its sign is chosen uniformly.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is positive.</li>
     * </ul>
     *
     * @return a positive {@code BinaryFraction}
     */
    public @NotNull BinaryFraction nextPositiveBinaryFraction() {
        return ((RandomProvider) wheelsProvider).nextPositiveBinaryFraction();
    }

    /**
     * Returns a randomly-generated negative {@code BinaryFraction}. The mantissa bit size is chosen from a geometric
     * distribution with mean {@code scale}, and then the mantissa is chosen uniformly from all odd negative
     * {@code BigInteger}s with that bit size. The absolute value of the exponent is chosen from a geometric
     * distribution with mean {@code secondaryScale}, and its sign is chosen uniformly.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is negative.</li>
     * </ul>
     *
     * @return a negative {@code BinaryFraction}
     */
    public @NotNull BinaryFraction nextNegativeBinaryFraction() {
        return ((RandomProvider) wheelsProvider).nextNegativeBinaryFraction();
    }

    /**
     * Returns a randomly-generated nonzero {@code BinaryFraction}. The mantissa bit size is chosen from a geometric
     * distribution with mean {@code scale}, and then the mantissa is chosen uniformly from all odd {@code BigInteger}s
     * with that bit size. The absolute value of the exponent is chosen from a geometric distribution with mean
     * {@code secondaryScale}, and its sign is chosen uniformly. Finally, the sign of the {@code BinaryFraction} itself
     * is chosen uniformly.
     *
     * <ul>
     *  <li>{@code this} must have a scale of at least 2 and a positive secondary scale.</li>
     *  <li>The result is not zero.</li>
     * </ul>
     *
     * @return a nonzero {@code BinaryFraction}
     */
    public @NotNull BinaryFraction nextNonzeroBinaryFraction() {
        return ((RandomProvider) wheelsProvider).nextNonzeroBinaryFraction();
    }

    /**
     * Returns a randomly-generated {@code BinaryFraction}. The mantissa bit size is chosen from a geometric
     * distribution with mean {@code scale}. If the bit size is zero, the {@code BinaryFraction} is zero; otherwise,
     * the mantissa is chosen uniformly from all odd {@code BigInteger}s with that bit size, thhe absolute value of the
     * exponent is chosen from a geometric distribution with mean {@code secondaryScale}, the exponent's sign is chosen
     * uniformly, and, finally, the sign of the {@code BinaryFraction} itself is chosen uniformly.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale and a positive secondary scale.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return a {@code BinaryFraction}
     */
    public @NotNull BinaryFraction nextBinaryFraction() {
        return ((RandomProvider) wheelsProvider).nextBinaryFraction();
    }

    /**
     * Returns a randomly-generated {@code BinaryFraction} greater than or equal to {@code a}. A higher {@code scale}
     * corresponds to a higher mantissa bit size and a higher {@code secondaryScale} corresponds to a higher exponent
     * bit size, but the exact relationship is not simple to describe.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale and a positive secondary scale.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return a {@code BinaryFraction} greater than or equal to {@code a}
     */
    public @NotNull BinaryFraction nextFromRangeUp(@NotNull BinaryFraction a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeUp(a);
    }

    /**
     * Returns a randomly-generated {@code BinaryFraction} less than or equal to {@code a}. A higher {@code scale}
     * corresponds to a higher mantissa bit size and a higher {@code secondaryScale} corresponds to a higher exponent
     * bit size, but the exact relationship is not simple to describe.
     *
     * <ul>
     *  <li>{@code this} must have a positive scale and a positive secondary scale.</li>
     *  <li>{@code a} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return a {@code BinaryFraction} less than or equal to {@code a}
     */
    public @NotNull BinaryFraction nextFromRangeDown(@NotNull BinaryFraction a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeDown(a);
    }

    /**
     * <p>Returns a {@code BinaryFraction} between {@code a} and {@code b}, inclusive.</p>
     *
     * <p>Every interval with {@code BinaryFraction} bounds may be broken up into equal blocks whose length is a power
     * of 2. Consider the subdivision with the largest possible block size. We can call points that lie on the
     * boundaries between blocks, along with the lower and upper bounds of the interval, <i>division-0</i> points.
     * Let points within the interval that are halfway between division-0 points be called <i>division-1</i> points;
     * points halfway between division-0 or division-1 points <i>division-2</i> points; and so on. The
     * {@code BinaryFraction}s returned by this method have divisions chosen from a geometric distribution with mean
     * {@code scale}. The distribution of {@code BinaryFraction}s is approximately uniform.</p>
     *
     * <ul>
     *  <li>{@code this} must have a positive scale. The scale cannot be {@code Integer.MAX_VALUE}.</li>
     *  <li>{@code a} may be any {@code BinaryFraction}.</li>
     *  <li>{@code b} may be any {@code BinaryFraction}.</li>
     *  <li>{@code a} must be less than or equal to {@code b}.</li>
     *  <li>The result is empty, or an infinite, non-removable {@code Iterable} containing
     *  {@code BinaryFraction}s.</li>
     * </ul>
     *
     * Length is infinite if a≤b, 0 otherwise
     *
     * @param a the inclusive lower bound of the generated elements
     * @param b the inclusive upper bound of the generated elements
     * @return approximately uniformly-distributed {@code BinaryFraction}s between {@code a} and {@code b}, inclusive
     */
    public @NotNull BinaryFraction nextFromRange(@NotNull BinaryFraction a, @NotNull BinaryFraction b) {
        return ((RandomProvider) wheelsProvider).nextFromRange(a, b);
    }

    public float nextPositiveFloat() {
        return ((RandomProvider) wheelsProvider).nextPositiveFloat();
    }

    public float nextNegativeFloat() {
        return ((RandomProvider) wheelsProvider).nextNegativeFloat();
    }

    public float nextNonzeroFloat() {
        return ((RandomProvider) wheelsProvider).nextNonzeroFloat();
    }

    public float nextFloat() {
        return ((RandomProvider) wheelsProvider).nextFloat();
    }

    public double nextPositiveDouble() {
        return ((RandomProvider) wheelsProvider).nextPositiveDouble();
    }

    public double nextNegativeDouble() {
        return ((RandomProvider) wheelsProvider).nextNegativeDouble();
    }

    public double nextNonzeroDouble() {
        return ((RandomProvider) wheelsProvider).nextNonzeroDouble();
    }

    public double nextDouble() {
        return ((RandomProvider) wheelsProvider).nextDouble();
    }

    public float nextPositiveFloatUniform() {
        return ((RandomProvider) wheelsProvider).nextPositiveFloatUniform();
    }

    public float nextNegativeFloatUniform() {
        return ((RandomProvider) wheelsProvider).nextNegativeFloatUniform();
    }

    public float nextNonzeroFloatUniform() {
        return ((RandomProvider) wheelsProvider).nextNonzeroFloatUniform();
    }

    public float nextFloatUniform() {
        return ((RandomProvider) wheelsProvider).nextFloatUniform();
    }

    public double nextPositiveDoubleUniform() {
        return ((RandomProvider) wheelsProvider).nextPositiveDoubleUniform();
    }

    public double nextNegativeDoubleUniform() {
        return ((RandomProvider) wheelsProvider).nextNegativeDoubleUniform();
    }

    public double nextNonzeroDoubleUniform() {
        return ((RandomProvider) wheelsProvider).nextNonzeroDoubleUniform();
    }

    public double nextDoubleUniform() {
        return ((RandomProvider) wheelsProvider).nextDoubleUniform();
    }

    public @NotNull BigDecimal nextPositiveBigDecimal() {
        return ((RandomProvider) wheelsProvider).nextPositiveBigDecimal();
    }

    public @NotNull BigDecimal nextNegativeBigDecimal() {
        return ((RandomProvider) wheelsProvider).nextNegativeBigDecimal();
    }

    public @NotNull BigDecimal nextNonzeroBigDecimal() {
        return ((RandomProvider) wheelsProvider).nextNonzeroBigDecimal();
    }

    public @NotNull BigDecimal nextBigDecimal() {
        return ((RandomProvider) wheelsProvider).nextBigDecimal();
    }

    public @NotNull BigDecimal nextCanonicalPositiveBigDecimal() {
        return ((RandomProvider) wheelsProvider).nextPositiveCanonicalBigDecimal();
    }

    public @NotNull BigDecimal nextCanonicalNegativeBigDecimal() {
        return ((RandomProvider) wheelsProvider).nextNegativeCanonicalBigDecimal();
    }

    public @NotNull BigDecimal nextCanonicalNonzeroBigDecimal() {
        return ((RandomProvider) wheelsProvider).nextNonzeroCanonicalBigDecimal();
    }

    public @NotNull BigDecimal nextCanonicalBigDecimal() {
        return ((RandomProvider) wheelsProvider).nextCanonicalBigDecimal();
    }

    public @NotNull BigDecimal nextFromRangeUp(@NotNull BigDecimal a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeUp(a);
    }

    public @NotNull BigDecimal nextFromRangeDown(@NotNull BigDecimal a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeDown(a);
    }

    public @NotNull BigDecimal nextFromRange(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        return ((RandomProvider) wheelsProvider).nextFromRange(a, b);
    }

    public @NotNull BigDecimal nextFromRangeUpCanonical(@NotNull BigDecimal a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeUpCanonical(a);
    }

    public @NotNull BigDecimal nextFromRangeDownCanonical(@NotNull BigDecimal a) {
        return ((RandomProvider) wheelsProvider).nextFromRangeDownCanonical(a);
    }

    public @NotNull BigDecimal nextFromRangeCanonical(@NotNull BigDecimal a, @NotNull BigDecimal b) {
        return ((RandomProvider) wheelsProvider).nextFromRangeCanonical(a, b);
    }

    public <T> T nextWithElement(@Nullable T x, @NotNull Supplier<T> sx) {
        return ((RandomProvider) wheelsProvider).nextWithElement(x, sx);
    }

    public <T> T nextWithNull(@NotNull Supplier<T> sx) {
        return ((RandomProvider) wheelsProvider).nextWithNull(sx);
    }

    public @NotNull <T> Optional<T> nextNonEmptyOptional(@NotNull Supplier<T> sx) {
        return Optional.of(sx.get());
    }

    public @NotNull <T> Optional<T> nextOptional(@NotNull Supplier<T> sx) {
        return ((RandomProvider) wheelsProvider).nextOptional(sx);
    }

    public @NotNull <T> NullableOptional<T> nextNonEmptyNullableOptional(@NotNull Supplier<T> sx) {
        return NullableOptional.of(sx.get());
    }

    public @NotNull <T> NullableOptional<T> nextNullableOptional(@NotNull Supplier<T> sx) {
        return ((RandomProvider) wheelsProvider).nextNullableOptional(sx);
    }

    /**
     * a pseudorandom {@link Iterable} that generates every {@link Rational}. Each {@code Rational}'s bit size (defined
     * as the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution with mean
     * approximately {@code meanBitSize} (The ratio between the actual mean bit size and {@code meanBitSize} decreases
     * as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 5.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> rationals() {
        Iterable<BigInteger> numerators = withScale(getScale() / 2).bigIntegers();
        Iterable<BigInteger> denominators = withScale(getScale() / 2).positiveBigIntegers();
        return map(
                p -> Rational.of(p.a, p.b),
                filter(q -> q.a.gcd(q.b).equals(BigInteger.ONE), pairs(numerators, denominators))
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every non-negative {@code Rational}. Each {@code Rational}'s bit
     * size (defined as the sum of the numerator and denominator's bit size) is chosen from a geometric distribution
     * with mean approximately {@code meanBitSize} (The ratio between the actual mean bit size and {@code meanBitSize}
     * decreases as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 5.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> nonNegativeRationals() {
        Iterable<BigInteger> numerators = withScale(getScale() / 2).naturalBigIntegers();
        Iterable<BigInteger> denominators = withScale(getScale() / 2).positiveBigIntegers();
        return map(
                p -> Rational.of(p.a, p.b),
                filter(q -> q.a.gcd(q.b).equals(BigInteger.ONE), pairs(numerators, denominators))
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every positive {@code Rational}. Each {@code Rational}'s bit size
     * (defined as the sum of the numerator 'sand denominator's bit sizes) is chosen from a geometric distribution with
     * mean approximately {@code meanBitSize} (The ratio between the actual mean bit size and {@code meanBitSize}
     * decreases as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 5.</li>
     *  <li>The result is an infinite pseudorandom sequence of all positive {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> positiveRationals() {
        Iterable<BigInteger> components = withScale(getScale() / 2).positiveBigIntegers();
        return map(
                p -> Rational.of(p.a, p.b),
                filter(q -> q.a.gcd(q.b).equals(BigInteger.ONE), pairs(components))
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every negative {@code Rational}. Each {@code Rational}'s bit size
     * (defined as the sum of the numerator's and denominator's bit sizes) is chosen from a geometric distribution with
     * mean approximately {@code meanBitSize} (The ratio between the actual mean bit size and {@code meanBitSize}
     * decreases as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 5.</li>
     *  <li>The result is an infinite pseudorandom sequence of all negative {@code Rational}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> negativeRationals() {
        Iterable<BigInteger> numerators = withScale(getScale() / 2).negativeBigIntegers();
        Iterable<BigInteger> denominators = withScale(getScale() / 2).positiveBigIntegers();
        return map(
                p -> Rational.of(p.a, p.b),
                filter(q -> q.a.gcd(q.b).equals(BigInteger.ONE), pairs(numerators, denominators))
        );
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Rational} in the interval [0, 1). Each
     * {@code Rational}'s bit size (defined as the sum of the numerator's and denominator's bit sizes) is chosen from a
     * geometric distribution with mean approximately {@code meanBitSize} (The ratio between the actual mean bit size
     * and {@code meanBitSize} decreases as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 5.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Rational}s in the interval [0, 1).</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Rational> nonNegativeRationalsLessThanOne() {
        Iterable<BigInteger> numerators = withScale(getScale() / 2).naturalBigIntegers();
        Iterable<BigInteger> denominators = withScale(getScale() / 2).positiveBigIntegers();
        return map(
                p -> Rational.of(p.a, p.b),
                filter(q -> lt(q.a, q.b) && q.a.gcd(q.b).equals(BigInteger.ONE), pairs(numerators, denominators))
        );
    }

    @NotNull
    @Override
    public Iterable<Rational> rangeUp(@NotNull Rational a) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Rational> rangeDown(@NotNull Rational a) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Rational> range(@NotNull Rational a, @NotNull Rational b) {
        return null;
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Interval} with finite bounds. Each
     * {@code Interval}'s bit size (defined as the sum of the lower bound's and upper bound's bit sizes) is chosen from
     * a geometric distribution with mean approximately {@code meanBitSize} (The ratio between the actual mean bit size
     * and {@code meanBitSize} decreases as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 11.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Interval}s with finite bounds.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Interval> finitelyBoundedIntervals() {
        Iterable<Rational> bounds = withScale(getScale() / 2).rationals();
        return map(p -> Interval.of(p.a, p.b), filter(p -> le(p.a, p.b), pairs(bounds)));
    }

    /**
     * a pseudorandom {@code Iterable} that generates every {@code Interval}. Each {@code Interval}'s bit size (defined
     * as the sum of the lower bound's and upper bound's bit sizes) is chosen from a geometric distribution with mean
     * approximately {@code meanBitSize} (The ratio between the actual mean bit size and {@code meanBitSize} decreases
     * as {@code meanBitSize} increases). Does not support removal.
     *
     * <ul>
     *  <li>{@code meanBitSize} must be greater than 11.</li>
     *  <li>The result is an infinite pseudorandom sequence of all {@code Interval}s.</li>
     * </ul>
     *
     * Length is infinite
     */
    @Override
    public @NotNull Iterable<Interval> intervals() {
        Iterable<Rational> bounds = withScale(getScale() / 2).rationals();
        return map(
                p -> {
                    if (!p.a.isPresent() && !p.b.isPresent()) return Interval.ALL;
                    if (!p.a.isPresent()) return Interval.lessThanOrEqualTo(p.b.get());
                    if (!p.b.isPresent()) return Interval.greaterThanOrEqualTo(p.a.get());
                    return Interval.of(p.a.get(), p.b.get());
                },
                filter(p -> !p.a.isPresent() || !p.b.isPresent() || le(p.a.get(), p.b.get()), pairs(optionals(bounds)))
        );
    }

    @Override
    public @NotNull Iterable<Byte> bytes(@NotNull Interval a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Short> shorts(@NotNull Interval a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Integer> integers(@NotNull Interval a) {
        return null;
    }

    @Override
    public @NotNull Iterable<Long> longs(@NotNull Interval a) {
        return null;
    }

    public @NotNull Iterable<BigInteger> bigIntegers(@NotNull Interval a) {
        return null;
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
            if (diameter == Rational.ZERO) return repeat(a.getLower().get());
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
        return map(RationalVector::of, withScale(getSecondaryScale()).lists(dimension, rationals()));
    }

    @Override
    public @NotNull Iterable<RationalVector> rationalVectorsAtLeast(int minDimension) {
        return map(RationalVector::of, withScale(getSecondaryScale()).listsAtLeast(minDimension, rationals()));
    }

    @Override
    public @NotNull Iterable<RationalVector> rationalVectors() {
        return map(RationalVector::of, withScale(getSecondaryScale()).lists(rationals()));
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
                                        withScale(getSecondaryScale()).lists(dimension, bigIntegers())
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
                                        withScale(getSecondaryScale()).listsAtLeast(minDimension, bigIntegers())
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
                                        withScale(getSecondaryScale()).lists(bigIntegers())
                                )
                        )
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalMatrix> rationalMatrices(int height, int width) {
        if (height == 0 || width == 0) return repeat(RationalMatrix.zero(height, width));
        return map(RationalMatrix::fromRows, lists(height, rationalVectors(width)));
    }

    @Override
    public @NotNull Iterable<RationalMatrix> rationalMatrices() {
        return null;
    }

    @Override
    public @NotNull Iterable<Polynomial> polynomials(int degree) {
        return map(
                js -> Polynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                        withScale(getSecondaryScale()).lists(degree + 1, bigIntegers())
                )
        );
    }

    @Override
    public @NotNull Iterable<Polynomial> polynomialsAtLeast(int minDegree) {
        return map(
                js -> Polynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                        withScale(getSecondaryScale()).listsAtLeast(minDegree + 1, bigIntegers())
                )
        );
    }

    @Override
    public @NotNull Iterable<Polynomial> polynomials() {
        return map(
                js -> Polynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO),
                        withScale(getSecondaryScale()).lists(bigIntegers())
                )
        );
    }

    @NotNull
    @Override
    public Iterable<Polynomial> primitivePolynomials(int degree) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Polynomial> primitivePolynomialsAtLeast(int minDegree) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<Polynomial> primitivePolynomials() {
        return null;
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomials(int degree) {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || last(is) != Rational.ZERO,
                        withScale(getSecondaryScale()).lists(degree + 1, rationals())
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomialsAtLeast(int minDegree) {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || last(is) != Rational.ZERO,
                        withScale(getSecondaryScale()).listsAtLeast(minDegree + 1, rationals())
                )
        );
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomials() {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(
                        is -> is.isEmpty() || last(is) != Rational.ZERO,
                        withScale(getSecondaryScale()).lists(rationals())
                )
        );
    }

    @NotNull
    @Override
    public Iterable<RationalPolynomial> monicRationalPolynomials(int degree) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<RationalPolynomial> monicRationalPolynomialsAtLeast(int minDegree) {
        return null;
    }

    @NotNull
    @Override
    public Iterable<RationalPolynomial> monicRationalPolynomials() {
        return null;
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code QBarRandomProvider} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        return this == that || that != null && getClass() == that.getClass() &&
                wheelsProvider.equals(((QBarRandomProvider) that).wheelsProvider);
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        return wheelsProvider.hashCode();
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code QBarRandomProvider}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public String toString() {
        return "QBar" + wheelsProvider;
    }

    /**
     * Ensures that {@code this} is valid. Must return true for any {@code QBarRandomProvider} used outside this class.
     */
    public void validate() {
        assertTrue(wheelsProvider instanceof RandomProvider);
    }
}
