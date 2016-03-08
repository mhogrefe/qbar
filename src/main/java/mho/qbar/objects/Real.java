package mho.qbar.objects;

import mho.wheels.testing.Testing;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;

public class Real implements Iterable<Interval> {
    private final @NotNull Iterable<Interval> intervals;

    public Real(@NotNull Iterable<Interval> intervals) {
        this.intervals = intervals;
    }

    public Real(@NotNull Function<Rational, Integer> signTest, @NotNull Interval boundingInterval) {
        intervals = () -> new Iterator<Interval>() {
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
            public Interval next() {
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
                }
                if (midSign == lowerSign) {
                    lower = mid;
                } else {
                    upper = mid;
                }
                interval = Interval.of(lower, upper);
                return interval;
            }
        };
    }

    @Override
    public Iterator<Interval> iterator() {
        return intervals.iterator();
    }

    public float floatValue(@NotNull RoundingMode roundingMode) {
        for (Interval a : intervals) {
            if (a.isFinitelyBounded()) {
                float lower = a.getLower().get().floatValue(roundingMode);
                float upper = a.getUpper().get().floatValue(roundingMode);
                if (lower == upper) {
                    return lower;
                }
            }
        }
        throw new IllegalStateException("unreachable");
    }

    public float floatValue() {
        return floatValue(RoundingMode.HALF_EVEN);
    }

    public double doubleValue(@NotNull RoundingMode roundingMode) {
        for (Interval a : intervals) {
            if (a.isFinitelyBounded()) {
                double lower = a.getLower().get().doubleValue(roundingMode);
                double upper = a.getUpper().get().doubleValue(roundingMode);
                if (lower == upper) {
                    return lower;
                }
            }
        }
        throw new IllegalStateException("unreachable");
    }

    public double doubleValue() {
        return doubleValue(RoundingMode.HALF_EVEN);
    }

    public @NotNull BigDecimal bigDecimalValueByPrecision(int precision, @NotNull RoundingMode roundingMode) {
        for (Interval a : intervals) {
            if (a.isFinitelyBounded()) {
                BigDecimal lower = a.getLower().get().bigDecimalValueByPrecision(precision, roundingMode);
                BigDecimal upper = a.getUpper().get().bigDecimalValueByPrecision(precision, roundingMode);
                if (lower.equals(upper)) {
                    return lower;
                }
            }
        }
        throw new IllegalStateException("unreachable");
    }

    public @NotNull BigDecimal bigDecimalValueByScale(int scale, @NotNull RoundingMode roundingMode) {
        for (Interval a : intervals) {
            if (a.isFinitelyBounded()) {
                BigDecimal lower = a.getLower().get().bigDecimalValueByScale(scale, roundingMode);
                BigDecimal upper = a.getUpper().get().bigDecimalValueByScale(scale, roundingMode);
                if (lower.equals(upper)) {
                    return lower;
                }
            }
        }
        throw new IllegalStateException("unreachable");
    }

    public @NotNull BigDecimal bigDecimalValueByPrecision(int precision) {
        return bigDecimalValueByPrecision(precision, RoundingMode.HALF_EVEN);
    }

    public @NotNull BigDecimal bigDecimalValueByScale(int scale) {
        return bigDecimalValueByScale(scale, RoundingMode.HALF_EVEN);
    }

    public @NotNull String toStringBase(@NotNull BigInteger base, int scale) {
        for (Interval a : intervals) {
            if (a.isFinitelyBounded()) {
                String lower = a.getLower().get().toStringBase(base, scale);
                String upper = a.getUpper().get().toStringBase(base, scale);
                if (lower.equals(upper)) {
                    return lower;
                }
            }
        }
        throw new IllegalStateException("unreachable");
    }

    public @NotNull String toString() {
        return toStringBase(BigInteger.TEN, Testing.TINY_LIMIT);
    }
}
