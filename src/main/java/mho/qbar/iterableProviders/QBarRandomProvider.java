package mho.qbar.iterableProviders;

import mho.qbar.objects.*;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.ordering.Ordering;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.ordering.Ordering.lt;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("unused")
public class QBarRandomProvider extends QBarIterableProvider {
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

    @Override
    public @NotNull QBarRandomProvider copy() {
        return new QBarRandomProvider(((RandomProvider) wheelsProvider).copy());
    }

    @Override
    public @NotNull QBarRandomProvider deepCopy() {
        return new QBarRandomProvider(((RandomProvider) wheelsProvider).deepCopy());
    }

    @Override
    public @NotNull QBarIterableProvider withScale(int scale) {
        return new QBarRandomProvider((RandomProvider) wheelsProvider.withScale(scale));
    }

    @Override
    public @NotNull QBarIterableProvider withSecondaryScale(int secondaryScale) {
        return new QBarRandomProvider((RandomProvider) wheelsProvider.withScale(secondaryScale));
    }

    public long getId() {
        return ((RandomProvider) wheelsProvider).getId();
    }

    public int nextInt() {
        return ((RandomProvider) wheelsProvider).nextInt();
    }

    public long nextLong() {
        return ((RandomProvider) wheelsProvider).nextLong();
    }

    public boolean nextBoolean() {
        return ((RandomProvider) wheelsProvider).nextBoolean();
    }

    public <T> T nextUniformSample(@NotNull List<T> xs) {
        return ((RandomProvider) wheelsProvider).nextUniformSample(xs);
    }

    public char nextUniformSample(@NotNull String s) {
        return ((RandomProvider) wheelsProvider).nextUniformSample(s);
    }

    public @NotNull Ordering nextOrdering() {
        return ((RandomProvider) wheelsProvider).nextOrdering();
    }

    public @NotNull RoundingMode nextRoundingMode() {
        return ((RandomProvider) wheelsProvider).nextRoundingMode();
    }

    public byte nextPositiveByte() {
        return ((RandomProvider) wheelsProvider).nextPositiveByte();
    }

    public short nextPositiveShort() {
        return ((RandomProvider) wheelsProvider).nextPositiveShort();
    }

    public int nextPositiveInt() {
        return ((RandomProvider) wheelsProvider).nextPositiveInt();
    }

    public long nextPositiveLong() {
        return ((RandomProvider) wheelsProvider).nextPositiveLong();
    }

    public byte nextNegativeByte() {
        return ((RandomProvider) wheelsProvider).nextNegativeByte();
    }

    public short nextNegativeShort() {
        return ((RandomProvider) wheelsProvider).nextNegativeShort();
    }

    public int nextNegativeInt() {
        return ((RandomProvider) wheelsProvider).nextNegativeInt();
    }

    public long nextNegativeLong() {
        return ((RandomProvider) wheelsProvider).nextNegativeLong();
    }

    public byte nextNonzeroByte() {
        return ((RandomProvider) wheelsProvider).nextNonzeroByte();
    }

    public short nextNonzeroShort() {
        return ((RandomProvider) wheelsProvider).nextNonzeroShort();
    }

    public int nextNonzeroInt() {
        return ((RandomProvider) wheelsProvider).nextNonzeroInt();
    }

    public long nextNonzeroLong() {
        return ((RandomProvider) wheelsProvider).nextNonzeroLong();
    }

    public byte nextNaturalByte() {
        return ((RandomProvider) wheelsProvider).nextNaturalByte();
    }

    public short nextNaturalShort() {
        return ((RandomProvider) wheelsProvider).nextNaturalShort();
    }

    public int nextNaturalInt() {
        return ((RandomProvider) wheelsProvider).nextNaturalInt();
    }

    public long nextNaturalLong() {
        return ((RandomProvider) wheelsProvider).nextNaturalLong();
    }

    public byte nextByte() {
        return ((RandomProvider) wheelsProvider).nextByte();
    }

    public short nextShort() {
        return ((RandomProvider) wheelsProvider).nextShort();
    }

    public char nextAsciiChar() {
        return ((RandomProvider) wheelsProvider).nextAsciiChar();
    }

    public char nextChar() {
        return ((RandomProvider) wheelsProvider).nextChar();
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
                filter(is -> is.isEmpty() || !last(is).equals(BigInteger.ZERO), withScale(getSecondaryScale()).lists(degree + 1, bigIntegers()))
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
                filter(is -> is.isEmpty() || last(is) != Rational.ZERO, withScale(getSecondaryScale()).lists(degree + 1, rationals()))
        );
    }

    @Override
    public @NotNull Iterable<RationalPolynomial> rationalPolynomialsAtLeast(int minDegree) {
        return map(
                js -> RationalPolynomial.of(toList(js)),
                filter(is -> is.isEmpty() || last(is) != Rational.ZERO, withScale(getSecondaryScale()).listsAtLeast(minDegree + 1, rationals()))
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
