package mho.qbar.objects;

import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.testing.Testing;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.ordering.Ordering.lt;

public class Real implements Iterable<Interval>, Comparable<Real> {
    public static final @NotNull Real ZERO = of(Rational.ZERO);

    public static final @NotNull Real ONE = of(Rational.ONE);

    public static final @NotNull Real E = exp(Rational.ONE);

    public static final @NotNull Real PI =
            atan(Rational.of(1, 5)).shiftLeft(2).subtract(atan(Rational.of(1, 239))).shiftLeft(2);

    private final @NotNull Iterable<Interval> intervals;

    public Real(@NotNull Iterable<Interval> intervals) {
        this.intervals = intervals;
    }

    public static @NotNull Real of(@NotNull Rational r) {
        return new Real(repeat(Interval.of(r)));
    }

    public static @NotNull Real of(@NotNull BigInteger i) {
        return new Real(repeat(Interval.of(Rational.of(i))));
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

    private <T> T limitValue(@NotNull Function<Rational, T> f) {
        Optional<Rational> previousLower = Optional.empty();
        Optional<Rational> previousUpper = Optional.empty();
        T lowerValue = null;
        T upperValue = null;
        for (Interval a : intervals) {
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

    public @NotNull Real shiftLeft(int bits) {
        return new Real(map(a -> a.shiftLeft(bits), intervals));
    }

    public int signum() {
        return limitValue(Rational::signum);
    }

    public @NotNull Real abs() {
        return signum() == -1 ? negate() : this;
    }

    public @NotNull BigInteger floor() {
        return limitValue(Rational::floor);
    }

    public @NotNull Pair<List<BigInteger>, Iterable<BigInteger>> digits(@NotNull BigInteger base) {
        if (signum() == -1) {
            throw new IllegalArgumentException("this cannot be negative. Invalid this: " + this);
        }
        BigInteger floor = floor();
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

    public @NotNull String toStringBase(@NotNull BigInteger base, int scale) {
        if (lt(base, IntegerUtils.TWO)) {
            throw new IllegalArgumentException("base must be at least 2. Invalid base: " + base);
        }
        boolean baseIsSmall = le(base, BigInteger.valueOf(36));
        Pair<List<BigInteger>, Iterable<BigInteger>> digits = abs().digits(base);
        Function<BigInteger, String> digitFunction = baseIsSmall ?
                i -> Character.toString(IntegerUtils.toDigit(i.intValueExact())) :
                i -> "(" + i + ")";
        String beforeDecimal = digits.a.isEmpty() ?
                (baseIsSmall ? "0" : "(0)") :
                concatStrings(map(digitFunction, digits.a));
        String result;
        String afterDecimal = concatStrings(map(digitFunction, take(scale, digits.b)));
        result = beforeDecimal + "." + afterDecimal;
        return (signum() == -1 ? "-" + result : result) + "...";
    }

    public @NotNull BigInteger bigIntegerValue(@NotNull RoundingMode roundingMode) {
        return limitValue(r -> r.bigIntegerValue(roundingMode));
    }

    public float floatValue(@NotNull RoundingMode roundingMode) {
        return limitValue(r -> r.floatValue(roundingMode));
    }

    public float floatValue() {
        return floatValue(RoundingMode.HALF_EVEN);
    }

    public double doubleValue(@NotNull RoundingMode roundingMode) {
        return limitValue(r -> r.doubleValue(roundingMode));
    }

    public double doubleValue() {
        return doubleValue(RoundingMode.HALF_EVEN);
    }

    public @NotNull BigDecimal bigDecimalValueByPrecision(int precision, @NotNull RoundingMode roundingMode) {
        return limitValue(r -> r.bigDecimalValueByPrecision(precision, roundingMode));
    }

    public @NotNull BigDecimal bigDecimalValueByScale(int scale, @NotNull RoundingMode roundingMode) {
        return limitValue(r -> r.bigDecimalValueByScale(scale, roundingMode));
    }

    public @NotNull BigDecimal bigDecimalValueByPrecision(int precision) {
        return bigDecimalValueByPrecision(precision, RoundingMode.HALF_EVEN);
    }

    public @NotNull BigDecimal bigDecimalValueByScale(int scale) {
        return bigDecimalValueByScale(scale, RoundingMode.HALF_EVEN);
    }

    public static @NotNull Real champernowne(@NotNull BigInteger base) {
        return fromDigits(
                base,
                Collections.emptyList(),
                concatMap(i -> IntegerUtils.bigEndianDigits(base, i), rangeUp(BigInteger.ONE))
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
                map(i -> MathUtils.isPrime(i) ? BigInteger.ONE : BigInteger.ZERO, rangeUp(BigInteger.ONE))
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
                map(i -> Rational.of(BigInteger.ONE, MathUtils.factorial(i)), rangeUp(BigInteger.ZERO)),
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
                        rangeUp(BigInteger.ZERO)
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
                        rangeUp(BigInteger.ZERO)
                ),
                k -> {
                    Rational bound = Rational.of(MathUtils.factorial(k - 1));
                    return Interval.of(bound.negate(), bound);
                },
                x
        );
    }

    public @NotNull String toString() {
        return toStringBase(BigInteger.TEN, Testing.TINY_LIMIT);
    }

    @Override
    public int compareTo(@NotNull Real that) {
        if (this == that) return 0;
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        while (true) {
            Optional<Ordering> o = thisIntervals.next().elementCompare(thatIntervals.next());
            if (o.isPresent()) return o.get().toInt();
        }
    }

    public int compareTo(@NotNull Rational that) {
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
}
