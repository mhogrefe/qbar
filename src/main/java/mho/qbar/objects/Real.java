package mho.qbar.objects;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.NoRemoveIterator;
import mho.wheels.math.BinaryFraction;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.NullableOptional;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.testing.Testing.*;

/**
 * <p>The {@code Real} class represents real numbers as infinite, converging sequences of bounding intervals. Every
 * interval in the sequence must be contained in the preceding interval, and the intervals' diameters must converge to
 * zero. The rate of convergence is unspecified. Every interval must take a finite amount of computing time to produce.
 * These conditions cannot be enforced except by the caller, so be careful when constructing your own reals, and prefer
 * to use the methods of this class, which, when given valid {@code Real}s, will always return valid {@code Real}s.</p>
 *
 * <p>Any real number may be represented by infinitely many {@code Real}s. Given a rational number r, the {@code Real}
 * whose intervals are [r, r], [r, r], [r, r], ... is called the exact representation of r, and all other {@code Real}s
 * representing r are called fuzzy representations. Fuzzy {@code Real}s are difficult to work with, but also difficult
 * to avoid. Sometimes, a method that takes a {@code Real} will loop forever given certain fuzzy {@code Real}s. If this
 * is the case, that behavior is documented and the method is labeled unsafe. Often, a safe alternative is provided
 * that gives up after the diameters of the approximating intervals decrease below a certain resolution.</p>
 *
 * <p>Every {@code Real} is either irrational, exact, or fuzzy. Irrational or exact {@code Real}s are also called
 * clean.</p>
 *
 * <p>For example, consider the {@link Real#signumUnsafe()}, which returns the sign of a {@code Real}: –1, 0, or 1. It
 * works by looking at the signs of the lower and upper bounds of the {@code Real}'s intervals, and returning when they
 * are equal. This will always work for irrational {@code Real}s and exact {@code Real}s, but will loop forever on a
 * fuzzy zero whose intervals are [–1, 0], [–1/2, 0], [–1/4, 0], [–1/8, 0], ..., because the method will never know
 * whether the {@code Real} is actually zero or just a negative number with a small magnitude. A safer alternative is
 * {@link Real#signum(Rational)}. When given the fuzzy zero, it will give up (and return an empty {@code Optional})
 * after the diameters of the intervals decrease below the specified resolution. By the {@code Real} contract, a
 * small-enough interval is guaranteed to appear after a finite amount of computing time, so this method never
 * hangs.</p>
 *
 * <p>The above should make it clear that {@code Real}s are not user-friendly. Make sure to read the documentation
 * carefully, and, whenever possible, use {@link Rational} or {@link Algebraic} instead.</p>
 */
public final class Real implements Iterable<Interval> {
    /**
     * 0
     */
    public static final @NotNull Real ZERO = of(Rational.ZERO);

    /**
     * 1
     */
    public static final @NotNull Real ONE = of(Rational.ONE);

    /**
     * 10
     */
    public static final @NotNull Real TEN = of(Rational.TEN);

    /**
     * 2
     */
    public static final @NotNull Real TWO = of(Rational.TWO);

    /**
     * –1
     */
    public static final @NotNull Real NEGATIVE_ONE = of(Rational.NEGATIVE_ONE);

    /**
     * 1/2
     */
    public static final @NotNull Real ONE_HALF = of(Rational.ONE_HALF);

    /**
     * the square root of 2
     */
    public static final @NotNull Real SQRT_TWO = Algebraic.SQRT_TWO.realValue();

    /**
     * φ, the golden ratio
     */
    public static final @NotNull Real PHI = Algebraic.PHI.realValue();

    /**
     * e, the base of the natural logarithm
     */
    public static final @NotNull Real E = exp(Rational.ONE);

    /**
     * π, the ratio of a circle's circumference to its diameter
     */
    public static final @NotNull Real PI =
            atan(Rational.of(1, 5)).shiftLeft(2).subtract(atan(Rational.of(1, 239))).shiftLeft(2);

    /**
     * 2^(-100), the default resolution of those methods which give up a computation after the bounding interval
     * becomes too small.
     */
    public static final @NotNull Rational DEFAULT_RESOLUTION = Rational.ONE.shiftRight(SMALL_LIMIT);

    /**
     * The bounding intervals that define this {@code Real}. See the comment at the top of the class.
     */
    private final @NotNull Iterable<Interval> intervals;

    /**
     * Constructs a {@code Real} from an {@code Iterable} of {@code Interval}s. Unfortunately, none of the
     * preconditions on the argument can be checked.
     *
     * <ul>
     *  <li>{@code interval}s must be infinite. Every element after the first must be contained in its predecessor. The
     *  diameters of the elements must approach zero. Every element must take a finite amount of computation time to
     *  produce.</li>
     *  <li>Any {@code Real} may be generated with this constructor.</li>
     * </ul>
     *
     * @param intervals the bounding intervals that define a {@code Real}
     */
    public Real(@NotNull Iterable<Interval> intervals) {
        this.intervals = intervals;
    }

    /**
     * Creates a {@code Real} equal to a {@code Rational}.
     *
     * <ul>
     *  <li>{@code r} cannot be null.</li>
     *  <li>The result is rational and exact.</li>
     * </ul>
     *
     * @param r a {@code Rational}
     * @return the {@code Real} equal to {@code r}
     */
    public static @NotNull Real of(@NotNull Rational r) {
        return new Real(repeat(Interval.of(r)));
    }

    /**
     * Creates a {@code Real} equal to a {@code BigInteger}.
     *
     * <ul>
     *  <li>{@code n} cannot be null.</li>
     *  <li>The result is an integer and exact.</li>
     * </ul>
     *
     * @param n a {@code BigInteger}
     * @return the {@code Real} equal to {@code n}
     */
    public static @NotNull Real of(@NotNull BigInteger n) {
        return new Real(repeat(Interval.of(Rational.of(n))));
    }

    /**
     * Creates a {@code Real} equal to a {@code long}.
     *
     * <ul>
     *  <li>{@code n} may be any {@code long}.</li>
     *  <li>The result is an integer satisfying –2<sup>63</sup>≤x{@literal <}2<sup>63</sup>, and exact.</li>
     * </ul>
     *
     * @param n a {@code long}
     * @return the {@code Real} equal to {@code n}
     */
    public static @NotNull Real of(long n) {
        return new Real(repeat(Interval.of(Rational.of(n))));
    }

    /**
     * Creates a {@code Real} equal to an {@code int}.
     *
     * <ul>
     *  <li>{@code n} may be any {@code int}.</li>
     *  <li>The result is an integer satisfying –2<sup>31</sup>≤x{@literal <}2<sup>31</sup>, and exact.</li>
     * </ul>
     *
     * @param n an {@code int}
     * @return the {@code Real} equal to {@code n}
     */
    public static @NotNull Real of(int n) {
        return new Real(repeat(Interval.of(Rational.of(n))));
    }

    public static @NotNull Real fuzzyRepresentation(@NotNull Rational r) {
        return new Real(() -> new Iterator<Interval>() {
            private @NotNull Rational radius = Rational.ONE;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Interval next() {
                Interval nextInterval = Interval.of(r.subtract(radius), r.add(radius));
                radius = radius.shiftRight(1);
                return nextInterval;
            }
        });
    }

    public static @NotNull Real leftFuzzyRepresentation(@NotNull Rational r) {
        return new Real(() -> new Iterator<Interval>() {
            private @NotNull Rational radius = Rational.ONE;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Interval next() {
                Interval nextInterval = Interval.of(r.subtract(radius), r);
                radius = radius.shiftRight(1);
                return nextInterval;
            }
        });
    }

    public static @NotNull Real rightFuzzyRepresentation(@NotNull Rational r) {
        return new Real(() -> new Iterator<Interval>() {
            private @NotNull Rational radius = Rational.ONE;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Interval next() {
                Interval nextInterval = Interval.of(r, r.add(radius));
                radius = radius.shiftRight(1);
                return nextInterval;
            }
        });
    }

    public static @NotNull Real root(
            @NotNull Function<Rational, Integer> signTest,
            @NotNull Interval boundingInterval
    ) {
        return new Real(() -> new Iterator<Interval>() {
            private boolean first = true;
            private @NotNull Interval interval = boundingInterval;
            private @NotNull Rational lower = interval.getLower().get();
            private @NotNull Rational upper = interval.getUpper().get();
            private int lowerSign = signTest.apply(lower);
            private int upperSign = signTest.apply(upper);
            private @NotNull Optional<Interval> exact;
            {
                if (lowerSign == 0) {
                    exact = Optional.of(Interval.of(lower));
                } else if (upperSign == 0) {
                    exact = Optional.of(Interval.of(upper));
                } else {
                    exact = Optional.empty();
                }
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (exact.isPresent()) {
                    return exact.get();
                }
                if (first) {
                    first = false;
                    return interval;
                }
                Rational mid = interval.midpoint();
                int midSign = signTest.apply(mid);
                if (midSign == 0) {
                    exact = Optional.of(Interval.of(mid));
                    return exact.get();
                } else if (midSign == lowerSign) {
                    lower = mid;
                } else {
                    upper = mid;
                }
                interval = Interval.of(lower, upper);
                return interval;
            }
        });
    }

    public static @NotNull Real fromDigits(
            @NotNull BigInteger base,
            @NotNull List<BigInteger> beforeDecimal,
            @NotNull Iterable<BigInteger> afterDecimal
    ) {
        return of(Rational.of(IntegerUtils.fromBigEndianDigits(base, beforeDecimal))).add(
                new Real(() -> new Iterator<Interval>() {
                    private final @NotNull Iterator<BigInteger> digits = afterDecimal.iterator();
                    private @NotNull BigInteger acc = BigInteger.ZERO;
                    private @NotNull BigInteger power = BigInteger.ONE;

                    @Override
                    public boolean hasNext() {
                        return true;
                    }

                    @Override
                    public @NotNull Interval next() {
                        acc = acc.multiply(base).add(digits.next());
                        power = power.multiply(base);
                        Rational r = Rational.of(acc);
                        return Interval.of(r.divide(power), r.add(Rational.ONE).divide(power));
                    }
                })
        );
    }

    @Override
    public Iterator<Interval> iterator() {
        return intervals.iterator();
    }

    public boolean isExactRational() {
        Interval first = head(intervals);
        return first.getLower().isPresent() && first.getLower().equals(first.getUpper());
    }

    public boolean isExactInteger() {
        Optional<Rational> rationalValue = rationalValue();
        return rationalValue.isPresent() && rationalValue.get().isInteger();
    }

    public @NotNull Optional<Rational> rationalValue() {
        Interval first = head(intervals);
        if (first.getLower().isPresent() && first.getLower().equals(first.getUpper())) {
            return Optional.of(first.getLower().get());
        } else {
            return Optional.empty();
        }
    }

    public int match(@NotNull List<Real> targets) {
        Set<Integer> skipIndices = new HashSet<>(); //stop refining intervals that are disjoint from this's intervals
        Iterator<Interval> iterator = intervals.iterator();
        List<Iterator<Interval>> targetIterators = toList(map(Iterable::iterator, targets));
        List<Interval> targetIntervals = toList(replicate(targetIterators.size(), null));
        outer:
        while (true) {
            Interval interval = iterator.next();
            for (int i = 0; i < targetIntervals.size(); i++) {
                if (skipIndices.contains(i)) continue;
                targetIntervals.set(i, targetIterators.get(i).next());
            }
            int matchIndex = -1;
            for (int i = 0; i < targetIntervals.size(); i++) {
                if (skipIndices.contains(i)) continue;
                Interval targetInterval = targetIntervals.get(i);
                if (interval.disjoint(targetInterval)) {
                    skipIndices.add(i);
                } else {
                    if (matchIndex != -1) {
                        continue outer;
                    }
                    matchIndex = i;
                }
            }
            if (matchIndex == -1) {
                throw new IllegalArgumentException();
            }
            return matchIndex;
        }
    }

    private <T> NullableOptional<T> limitValue(@NotNull Function<Rational, T> f, @NotNull Rational resolution) {
        Optional<Rational> previousLower = Optional.empty();
        Optional<Rational> previousUpper = Optional.empty();
        T lowerValue = null;
        T upperValue = null;
        for (Interval a : intervals) {
            if (Thread.interrupted()) return null;
            if (a.isFinitelyBounded()) {
                Rational lower = a.getLower().get();
                Rational upper = a.getUpper().get();
                if (!previousLower.isPresent() || !previousLower.get().equals(lower)) {
                    lowerValue = f.apply(lower);
                }
                if (!previousUpper.isPresent() || !previousUpper.get().equals(upper)) {
                    upperValue = f.apply(upper);
                }
                if (Objects.equals(lowerValue, upperValue)) {
                    return NullableOptional.of(lowerValue);
                }
                if (Ordering.lt(upper.subtract(lower), resolution)) {
                    return NullableOptional.empty();
                }
                previousLower = Optional.of(lower);
                previousUpper = Optional.of(upper);
            }
        }
        throw new IllegalStateException("unreachable");
    }

    private <T> T limitValueUnsafe(@NotNull Function<Rational, T> f) {
        Optional<Rational> previousLower = Optional.empty();
        Optional<Rational> previousUpper = Optional.empty();
        T lowerValue = null;
        T upperValue = null;
        for (Interval a : intervals) {
            if (Thread.interrupted()) return null;
            if (a.isFinitelyBounded()) {
                Rational lower = a.getLower().get();
                Rational upper = a.getUpper().get();
                if (!previousLower.isPresent() || !previousLower.get().equals(lower)) {
                    lowerValue = f.apply(lower);
                }
                if (!previousUpper.isPresent() || !previousUpper.get().equals(upper)) {
                    upperValue = f.apply(upper);
                }
                if (Objects.equals(lowerValue, upperValue)) {
                    return lowerValue;
                }
                previousLower = Optional.of(lower);
                previousUpper = Optional.of(upper);
            }
        }
        throw new IllegalStateException("unreachable");
    }

    public @NotNull Real negate() {
        return new Real(map(Interval::negate, intervals));
    }

    public @NotNull Real add(@NotNull Real that) {
        return new Real(zipWith(Interval::add, intervals, that.intervals));
    }

    public @NotNull Real subtract(@NotNull Real that) {
        if (this == that) return ZERO;
        return new Real(zipWith(Interval::subtract, intervals, that.intervals));
    }

    public @NotNull Real multiply(@NotNull Real that) {
        return new Real(zipWith(Interval::multiply, intervals, that.intervals));
    }

    public @NotNull Real multiply(@NotNull Rational that) {
        return new Real(map(i -> i.multiply(that), intervals));
    }

    public @NotNull Real multiply(@NotNull BigInteger that) {
        return new Real(map(i -> i.multiply(that), intervals));
    }

    public @NotNull Real divide(@NotNull Rational that) {
        return new Real(map(i -> i.divide(that), intervals));
    }

    public @NotNull Real divide(@NotNull BigInteger that) {
        return new Real(map(i -> i.divide(that), intervals));
    }

    public @NotNull Real shiftLeft(int bits) {
        return new Real(map(a -> a.shiftLeft(bits), intervals));
    }

    public int signumUnsafe() {
        return limitValueUnsafe(Rational::signum);
    }

    public Optional<Integer> signum(@NotNull Rational resolution) {
        return limitValue(Rational::signum, resolution).toOptional();
    }

    public @NotNull Real abs() {
        return new Real(map(Interval::abs, intervals));
    }

    public @NotNull BigInteger floorUnsafe() {
        return limitValueUnsafe(Rational::floor);
    }

    public @NotNull Optional<BigInteger> floor(@NotNull Rational resolution) {
        return limitValue(Rational::floor, resolution).toOptional();
    }

    public @NotNull Pair<List<BigInteger>, Iterable<BigInteger>> digitsUnsafe(@NotNull BigInteger base) {
        if (signumUnsafe() == -1) {
            throw new IllegalArgumentException("this cannot be negative. Invalid this: " + this);
        }
        BigInteger floor = floorUnsafe();
        List<BigInteger> beforeDecimal = IntegerUtils.bigEndianDigits(base, floor);
        Iterable<BigInteger> afterDecimal = () -> new Iterator<BigInteger>() {
            Iterator<Interval> fractionIntervals = subtract(of(floor)).iterator();
            BigInteger power = base;
            Interval interval;
            {
                do {
                    interval = fractionIntervals.next();
                } while (!interval.isFinitelyBounded());
                interval = interval.multiply(power);
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull BigInteger next() {
                while (true) {
                    BigInteger lowerFloor = interval.getLower().get().floor();
                    BigInteger upperFloor = interval.getUpper().get().floor();
                    if (lowerFloor.equals(upperFloor)) {
                        power = power.multiply(base);
                        interval = interval.multiply(base);
                        return lowerFloor.mod(base);
                    } else {
                        interval = fractionIntervals.next().multiply(power);
                    }
                }
            }
        };
        return new Pair<>(beforeDecimal, afterDecimal);
    }

    public @NotNull Optional<Pair<List<BigInteger>, Iterable<BigInteger>>> digits(
            @NotNull BigInteger base,
            @NotNull Rational resolution
    ) {
        Optional<Integer> oSignum = signum(resolution);
        if (!oSignum.isPresent()) {
            return Optional.empty();
        }
        int signum = oSignum.get();
        if (signum == -1) {
            throw new IllegalArgumentException("this cannot be negative. Invalid this: " + this);
        }
        Optional<BigInteger> oFloor = floor(resolution);
        if (!oFloor.isPresent()) {
            return Optional.empty();
        }
        BigInteger floor = oFloor.get();
        List<BigInteger> beforeDecimal = IntegerUtils.bigEndianDigits(base, floor);
        Iterable<BigInteger> afterDecimal = () -> new Iterator<BigInteger>() {
            private final @NotNull  Iterator<Interval> fractionIntervals = subtract(of(floor)).iterator();
            private BigInteger power = base;
            private Interval interval;
            boolean aborted = false;
            {
                do {
                    interval = fractionIntervals.next();
                } while (!interval.isFinitelyBounded());
                interval = interval.multiply(power);
            }

            @Override
            public boolean hasNext() {
                return !aborted;
            }

            @Override
            public @NotNull BigInteger next() {
                while (true) {
                    BigInteger lowerFloor = interval.getLower().get().floor();
                    BigInteger upperFloor = interval.getUpper().get().floor();
                    if (lowerFloor.equals(upperFloor)) {
                        power = power.multiply(base);
                        interval = interval.multiply(base);
                        return lowerFloor.mod(base);
                    } else {
                        if (Ordering.lt(interval.diameter().get(), resolution)) {
                            aborted = true;
                            return IntegerUtils.NEGATIVE_ONE;
                        }
                        interval = fractionIntervals.next().multiply(power);
                    }
                }
            }
        };
        return Optional.of(new Pair<>(beforeDecimal, afterDecimal));
    }

    public @NotNull String toStringBaseUnsafe(@NotNull BigInteger base, int scale) {
        if (Ordering.lt(base, IntegerUtils.TWO)) {
            throw new IllegalArgumentException("base must be at least 2. Invalid base: " + base);
        }
        Optional<Rational> or = rationalValue();
        if (or.isPresent()) {
            return or.get().toStringBase(base, scale);
        }
        boolean baseIsSmall = Ordering.le(base, BigInteger.valueOf(36));
        Pair<List<BigInteger>, Iterable<BigInteger>> digits = abs().digitsUnsafe(base);
        Function<BigInteger, String> digitFunction = baseIsSmall ?
                i -> Character.toString(IntegerUtils.toDigit(i.intValueExact())) :
                i -> "(" + i + ")";
        String beforeDecimal = digits.a.isEmpty() ?
                (baseIsSmall ? "0" : "(0)") :
                concatStrings(map(digitFunction, digits.a));
        String result;
        String afterDecimal = concatStrings(map(digitFunction, take(scale, digits.b)));
        result = beforeDecimal + "." + afterDecimal;
        return (signumUnsafe() == -1 ? "-" + result : result) + "...";
    }

    public @NotNull String toStringBase(@NotNull BigInteger base, int scale, @NotNull Rational resolution) {
        if (Ordering.lt(base, IntegerUtils.TWO)) {
            throw new IllegalArgumentException("base must be at least 2. Invalid base: " + base);
        }
        Optional<Rational> or = rationalValue();
        if (or.isPresent()) {
            return or.get().toStringBase(base, scale);
        }
        boolean baseIsSmall = Ordering.le(base, BigInteger.valueOf(36));
        Optional<Pair<List<BigInteger>, Iterable<BigInteger>>> oDigits = abs().digits(base, resolution);
        if (!oDigits.isPresent()) {
            Optional<Integer> oSignum = signum(resolution);
            if (!oSignum.isPresent()) {
                return "~0";
            }
            int signum = oSignum.get();
            Iterator<Interval> is = iterator();
            Interval firstInterval;
            do {
                firstInterval = is.next();
            } while (!firstInterval.isFinitelyBounded());
            Rational bound = Ordering.max(firstInterval.getLower().get().abs(), firstInterval.getUpper().get().abs());
            int maxDigitsToTheLeft = 0;
            Rational power = Rational.ONE;
            while (Ordering.le(power, bound)) {
                power = power.multiply(base);
                maxDigitsToTheLeft++;
            }
            power = Rational.ONE;
            Optional<BigInteger> oFloor;
            for (int i = 1; i <= maxDigitsToTheLeft; i++) {
                power = power.multiply(base);
                oFloor = abs().divide(power).floor(resolution);
                if (oFloor.isPresent()) {
                    BigInteger floor = oFloor.get();
                    if (floor.equals(BigInteger.ZERO)) {
                        break;
                    }
                    StringBuilder sb = new StringBuilder();
                    if (signum == -1) {
                        sb.append('-');
                    }
                    sb.append(IntegerUtils.toStringBase(base, floor));
                    for (int j = 0; j < i; j++) {
                        sb.append('?');
                    }
                    sb.append("....");
                    return sb.toString();
                }
            }
            if (signum == 1) {
                return "+...";
            } else {
                return "-...";
            }
        }
        Pair<List<BigInteger>, Iterable<BigInteger>> digits = oDigits.get();
        Function<BigInteger, String> digitFunction = baseIsSmall ?
                i -> Character.toString(IntegerUtils.toDigit(i.intValueExact())) :
                i -> "(" + i + ")";
        String beforeDecimal = digits.a.isEmpty() ?
                (baseIsSmall ? "0" : "(0)") :
                concatStrings(map(digitFunction, digits.a));
        if (its(intervals).startsWith("[[-1/2, 1/2]")) {
            int lll = 0;
        }
        String result;
        String afterDecimal = concatStrings(map(digitFunction, filter(d -> d.signum() != -1, take(scale, digits.b))));
        result = beforeDecimal + "." + afterDecimal;
        return (signumUnsafe() == -1 ? "-" + result : result) + "...";
    }

    public @NotNull BinaryFraction roundUpToIntegerPowerOfTwo() {
        Optional<Rational> previousLower = Optional.empty();
        Optional<Rational> previousUpper = Optional.empty();
        BinaryFraction lowerValue = null;
        BinaryFraction upperValue = null;
        for (Interval a : intervals) {
            if (a.isFinitelyBounded()) {
                Rational lower = a.getLower().get();
                Rational upper = a.getUpper().get();
                if (upper.signum() != 1) {
                    throw new ArithmeticException();
                }
                if (lower.signum() != 1) {
                    continue;
                }
                if (!previousLower.isPresent() || !previousLower.get().equals(lower)) {
                    lowerValue = lower.roundUpToPowerOfTwo();
                }
                if (!previousUpper.isPresent() || !previousUpper.get().equals(upper)) {
                    upperValue = upper.roundUpToPowerOfTwo();
                }
                if (Objects.equals(lowerValue, upperValue)) {
                    //noinspection ConstantConditions
                    return lowerValue;
                }
                previousLower = Optional.of(lower);
                previousUpper = Optional.of(upper);
            }
        }
        throw new IllegalStateException("unreachable");
    }

    public int binaryExponent() {
        Optional<Rational> previousLower = Optional.empty();
        Optional<Rational> previousUpper = Optional.empty();
        int lowerValue = 0;
        int upperValue = 0;
        for (Interval a : intervals) {
            if (a.isFinitelyBounded()) {
                Rational lower = a.getLower().get();
                Rational upper = a.getUpper().get();
                if (upper.signum() != 1) {
                    throw new ArithmeticException();
                }
                if (lower.signum() != 1) {
                    continue;
                }
                if (!previousLower.isPresent() || previousLower.get() != lower) {
                    lowerValue = lower.binaryExponent();
                }
                if (!previousUpper.isPresent() || previousUpper.get() != upper) {
                    upperValue = upper.binaryExponent();
                }
                if (Objects.equals(lowerValue, upperValue)) {
                    //noinspection ConstantConditions
                    return lowerValue;
                }
                previousLower = Optional.of(lower);
                previousUpper = Optional.of(upper);
            }
        }
        throw new IllegalStateException("unreachable");
    }

    public @NotNull BigInteger bigIntegerValue(@NotNull RoundingMode roundingMode) {
        return limitValueUnsafe(r -> r.bigIntegerValue(roundingMode));
    }

    public float floatValue(@NotNull RoundingMode roundingMode) {
        return limitValueUnsafe(r -> r.floatValue(roundingMode));
    }

    public float floatValue() {
        return floatValue(RoundingMode.HALF_EVEN);
    }

    public double doubleValue(@NotNull RoundingMode roundingMode) {
        return limitValueUnsafe(r -> r.doubleValue(roundingMode));
    }

    public double doubleValue() {
        return doubleValue(RoundingMode.HALF_EVEN);
    }

    public @NotNull BigDecimal bigDecimalValueByPrecision(int precision, @NotNull RoundingMode roundingMode) {
        return limitValueUnsafe(r -> r.bigDecimalValueByPrecision(precision, roundingMode));
    }

    public @NotNull BigDecimal bigDecimalValueByScale(int scale, @NotNull RoundingMode roundingMode) {
        return limitValueUnsafe(r -> r.bigDecimalValueByScale(scale, roundingMode));
    }

    public @NotNull BigDecimal bigDecimalValueByPrecision(int precision) {
        return bigDecimalValueByPrecision(precision, RoundingMode.HALF_EVEN);
    }

    public @NotNull BigDecimal bigDecimalValueByScale(int scale) {
        return bigDecimalValueByScale(scale, RoundingMode.HALF_EVEN);
    }

    public static @NotNull Interval intervalExtension(@NotNull Real lower, @NotNull Real upper) {
        Iterator<Interval> lowerIntervals = lower.iterator();
        Iterator<Interval> upperIntervals = upper.iterator();
        while (true) {
            Interval lowerInterval = lowerIntervals.next();
            Interval upperInterval = upperIntervals.next();
            if (!lowerInterval.isFinitelyBounded() || !upperInterval.isFinitelyBounded() ||
                    !lowerInterval.disjoint(upperInterval)) continue;
            Rational gapSize = upperInterval.getLower().get().subtract(lowerInterval.getUpper().get());
            if (gapSize.signum() == -1) {
                throw new IllegalArgumentException();
            }
            Rational diameterSum = lowerInterval.diameter().get().add(upperInterval.diameter().get());
            if (Ordering.ge(gapSize, diameterSum)) {
                return lowerInterval.convexHull(upperInterval);
            }
        }
    }

    public static @NotNull Real sum(@NotNull List<Real> xs) {
        return new Real(map(Interval::sum, transpose(map(r -> r.intervals, xs))));
    }

    public static @NotNull Real product(@NotNull List<Real> xs) {
        return new Real(map(Interval::product, transpose(map(r -> r.intervals, xs))));
    }

    public static @NotNull Real champernowne(@NotNull BigInteger base) {
        return fromDigits(
                base,
                Collections.emptyList(),
                concatMap(
                        i -> IntegerUtils.bigEndianDigits(base, i),
                        ExhaustiveProvider.INSTANCE.rangeUpIncreasing(BigInteger.ONE)
                )
        );
    }

    public static @NotNull Real copelandErdős(@NotNull BigInteger base) {
        return fromDigits(
                base,
                Collections.emptyList(),
                concatMap(i -> IntegerUtils.bigEndianDigits(base, i), MathUtils.primes())
        );
    }

    public static final @NotNull Real BINARY_INDICATOR =
        fromDigits(
                IntegerUtils.TWO,
                Collections.emptyList(),
                map(
                        i -> MathUtils.isPrime(i) ? BigInteger.ONE : BigInteger.ZERO,
                        ExhaustiveProvider.INSTANCE.rangeUpIncreasing(BigInteger.ONE)
                )
        );

    public static @NotNull Real fromMaclaurinSeries(
            @NotNull Iterable<Rational> maclaurinCoefficients,
            @NotNull Function<Integer, Interval> derivativeBound,
            @NotNull Rational x
    ) {
        return new Real(() -> new Iterator<Interval>() {
            private int k = 0;
            private final @NotNull Iterator<Rational> maclaurinIterator = maclaurinCoefficients.iterator();
            private @NotNull Rational center = Rational.ZERO;
            private @NotNull Rational xPower = Rational.ONE;
            private @NotNull BigInteger errorDenominator = BigInteger.ONE;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Interval next() {
                Rational coefficient = maclaurinIterator.next();
                if (coefficient != Rational.ZERO) {
                    center = center.add(xPower.multiply(coefficient));
                }
                xPower = xPower.multiply(x);
                k++;
                errorDenominator = errorDenominator.multiply(BigInteger.valueOf(k));
                Interval error = derivativeBound.apply(k).multiply(xPower.divide(errorDenominator));
                return Interval.of(center).add(error);
            }
        });
    }

    public static @NotNull Real exp(@NotNull Rational x) {
        if (x == Rational.ZERO) return ONE;
        Interval derivativeBounds;
        if (x.signum() == 1) {
            derivativeBounds = Interval.of(Rational.ONE, Rational.of(3).pow(x.ceiling().intValueExact()));
        } else {
            derivativeBounds = Interval.of(Rational.ZERO, Rational.ONE);
        }
        return fromMaclaurinSeries(
                map(
                        i -> Rational.of(BigInteger.ONE, MathUtils.factorial(i)),
                        ExhaustiveProvider.INSTANCE.rangeUpIncreasing(BigInteger.ZERO)
                ),
                k -> derivativeBounds,
                x
        );
    }

    public static @NotNull Real sin(@NotNull Rational x) {
        if (x == Rational.ZERO) return ZERO;
        Interval derivativeBounds = Interval.of(Rational.NEGATIVE_ONE, Rational.ONE);
        return fromMaclaurinSeries(
                map(
                        i -> i.and(BigInteger.ONE).equals(BigInteger.ZERO) ?
                                Rational.ZERO :
                                Rational.of(
                                        i.and(IntegerUtils.TWO).equals(BigInteger.ZERO) ?
                                                BigInteger.ONE :
                                                IntegerUtils.NEGATIVE_ONE,
                                        MathUtils.factorial(i)
                                ),
                        ExhaustiveProvider.INSTANCE.rangeUpIncreasing(BigInteger.ZERO)
                ),
                k -> derivativeBounds,
                x
        );
    }

    public static @NotNull Real atan(@NotNull Rational x) {
        if (x == Rational.ZERO) return ZERO;
        return fromMaclaurinSeries(
                map(
                        i -> i.and(BigInteger.ONE).equals(BigInteger.ZERO) ?
                                Rational.ZERO :
                                Rational.of(
                                        i.and(IntegerUtils.TWO).equals(BigInteger.ZERO) ?
                                                BigInteger.ONE :
                                                IntegerUtils.NEGATIVE_ONE,
                                        i
                                ),
                        ExhaustiveProvider.INSTANCE.rangeUpIncreasing(BigInteger.ZERO)
                ),
                k -> {
                    Rational bound = Rational.of(MathUtils.factorial(k - 1));
                    return Interval.of(bound.negate(), bound);
                },
                x
        );
    }

    private @NotNull Optional<BigInteger> continuedFractionHelper(
            @NotNull Rational approx,
            @NotNull List<BigInteger> termsSoFar
    ) {
        for (BigInteger term : termsSoFar) {
            approx = approx.subtract(Rational.of(term));
            if (approx.signum() != 1 || Ordering.ge(approx, Rational.ONE)) {
                return Optional.empty();
            }
            approx = approx.invert();
        }
        return Optional.of(approx.floor());
    }

    public @NotNull Iterable<BigInteger> continuedFraction() {
        if (this == ZERO) {
            return Collections.singletonList(BigInteger.ZERO);
        }
        return () -> new NoRemoveIterator<BigInteger>() {
            private final @NotNull List<BigInteger> termsSoFar = new ArrayList<>();
            private final @NotNull Iterator<Interval> is = intervals.iterator();
            private @NotNull Interval lastInterval;
            {
                do {
                    lastInterval = is.next();
                } while (!lastInterval.isFinitelyBounded());
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public BigInteger next() {
                Rational lower = null;
                Rational upper = null;
                BigInteger lowerTerm = null;
                BigInteger upperTerm = null;
                while (true) {
                    Rational newLower = lastInterval.getLower().get();
                    Rational newUpper = lastInterval.getUpper().get();
                    if (newLower != lower) {
                        Optional<BigInteger> newLowerTerm = continuedFractionHelper(newLower, termsSoFar);
                        if (newLowerTerm.isPresent()) {
                            lowerTerm = newLowerTerm.get();
                        } else {
                            lastInterval = is.next();
                            continue;
                        }
                    }
                    if (newUpper != upper) {
                        Optional<BigInteger> newUpperTerm = continuedFractionHelper(newUpper, termsSoFar);
                        if (newUpperTerm.isPresent()) {
                            upperTerm = newUpperTerm.get();
                        } else {
                            lastInterval = is.next();
                            continue;
                        }
                    }
                    lower = newLower;
                    upper = newUpper;
                    //noinspection ConstantConditions
                    if (lowerTerm.equals(upperTerm)) {
                        termsSoFar.add(lowerTerm);
                        return lowerTerm;
                    } else {
                        lastInterval = is.next();
                    }
                }
            }
        };
    }

    public @NotNull String toStringUnsafe() {
        return toStringBaseUnsafe(BigInteger.TEN, TINY_LIMIT);
    }

    public @NotNull String toString() {
        return toStringBase(BigInteger.TEN, TINY_LIMIT, DEFAULT_RESOLUTION);
    }

    public int compareToUnsafe(@NotNull Real that) {
        if (this == that) return 0;
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        while (true) {
            Optional<Ordering> o = thisIntervals.next().elementCompare(thatIntervals.next());
            if (o.isPresent()) return o.get().toInt();
        }
    }

    public static boolean ltUnsafe(@NotNull Real x, @NotNull Real y) {
        return x.compareToUnsafe(y) < 0;
    }

    public static boolean gtUnsafe(@NotNull Real x, @NotNull Real y) {
        return x.compareToUnsafe(y) > 0;
    }

    public static boolean leUnsafe(@NotNull Real x, @NotNull Real y) {
        return x.compareToUnsafe(y) <= 0;
    }

    public static boolean geUnsafe(@NotNull Real x, @NotNull Real y) {
        return x.compareToUnsafe(y) >= 0;
    }

    public @NotNull Optional<Integer> compareTo(@NotNull Real that, @NotNull Rational resolution) {
        if (this == that) return Optional.of(0);
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        while (true) {
            Interval thisInterval = thisIntervals.next();
            Interval thatInterval = thatIntervals.next();
            Optional<Ordering> o = thisInterval.elementCompare(thatInterval);
            if (o.isPresent()) {
                return Optional.of(o.get().toInt());
            } else if (thisInterval.isFinitelyBounded() && thatInterval.isFinitelyBounded() &&
                    Ordering.lt(thisInterval.diameter().get(), resolution) &&
                    Ordering.lt(thatInterval.diameter().get(), resolution)) {
                return Optional.empty();
            }
        }
    }

    public static @NotNull Optional<Boolean> lt(@NotNull Real x, @NotNull Real y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c < 0);
    }

    public static @NotNull Optional<Boolean> gt(@NotNull Real x, @NotNull Real y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c > 0);
    }

    public static @NotNull Optional<Boolean> le(@NotNull Real x, @NotNull Real y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c <= 0);
    }

    public static @NotNull Optional<Boolean> ge(@NotNull Real x, @NotNull Real y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c >= 0);
    }

    public int compareToUnsafe(@NotNull Rational that) {
        Optional<Rational> thisRational = rationalValue();
        if (thisRational.isPresent()) {
            return thisRational.get().compareTo(that);
        }
        for (Interval interval : intervals) {
            if (!interval.contains(that)) {
                Rational sample = interval.getLower().isPresent() ?
                        interval.getLower().get() :
                        interval.getUpper().get();
                return sample.compareTo(that);
            }
        }
        throw new IllegalStateException("unreachable");
    }

    public static boolean ltUnsafe(@NotNull Real x, @NotNull Rational y) {
        return x.compareToUnsafe(y) < 0;
    }

    public static boolean gtUnsafe(@NotNull Real x, @NotNull Rational y) {
        return x.compareToUnsafe(y) > 0;
    }

    public static boolean leUnsafe(@NotNull Real x, @NotNull Rational y) {
        return x.compareToUnsafe(y) <= 0;
    }

    public static boolean geUnsafe(@NotNull Real x, @NotNull Rational y) {
        return x.compareToUnsafe(y) >= 0;
    }

    public @NotNull Optional<Integer> compareTo(@NotNull Rational that, @NotNull Rational resolution) {
        Optional<Rational> thisRational = rationalValue();
        if (thisRational.isPresent()) {
            return Optional.of(thisRational.get().compareTo(that));
        }
        for (Interval interval : intervals) {
            if (!interval.contains(that)) {
                Rational sample = interval.getLower().isPresent() ?
                        interval.getLower().get() :
                        interval.getUpper().get();
                return Optional.of(sample.compareTo(that));
            }
            if (interval.isFinitelyBounded() && Ordering.lt(interval.diameter().get(), resolution)) {
                return Optional.empty();
            }
        }
        throw new IllegalStateException("unreachable");
    }

    public static @NotNull Optional<Boolean> lt(@NotNull Real x, @NotNull Rational y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c < 0);
    }

    public static @NotNull Optional<Boolean> gt(@NotNull Real x, @NotNull Rational y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c > 0);
    }

    public static @NotNull Optional<Boolean> le(@NotNull Real x, @NotNull Rational y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c <= 0);
    }

    public static @NotNull Optional<Boolean> ge(@NotNull Real x, @NotNull Rational y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c >= 0);
    }

    /**
     * Tries to check if {@code this} is valid. Must return without exceptions for any {@code Real} used outside this
     * class. Does not catch all invalid {@code Real}s.
     */
    public void validate() {
        Interval previous = null;
        for (Interval a : take(TINY_LIMIT, intervals)) {
            if (previous != null) {
                assertTrue(this, previous.contains(a));
            }
            previous = a;
        }
    }
}
