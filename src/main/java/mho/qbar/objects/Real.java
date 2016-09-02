package mho.qbar.objects;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.NoRemoveIterator;
import mho.wheels.math.BinaryFraction;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
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
    public static final @NotNull Real ZERO = new Real(Rational.ZERO);

    /**
     * 1
     */
    public static final @NotNull Real ONE = new Real(Rational.ONE);

    /**
     * 10
     */
    public static final @NotNull Real TEN = new Real(Rational.TEN);

    /**
     * 2
     */
    public static final @NotNull Real TWO = new Real(Rational.TWO);

    /**
     * –1
     */
    public static final @NotNull Real NEGATIVE_ONE = new Real(Rational.NEGATIVE_ONE);

    /**
     * 1/2
     */
    public static final @NotNull Real ONE_HALF = new Real(Rational.ONE_HALF);

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
            atan01(Rational.of(1, 5)).shiftLeft(2).subtract(atan01(Rational.of(1, 239))).shiftLeft(2);

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
     * If {@code this} is rational and exact, the {@code Rational} equal to {@code this}; otherwise, empty
     */
    private final @NotNull Optional<Rational> rational;

    /**
     * Constructs a {@code Real} from an {@code Iterable} of {@code Interval}s. Unfortunately, none of the
     * preconditions on the argument can be checked.
     *
     * <ul>
     *  <li>{@code interval}s must be infinite. Every element after the first must be contained in its predecessor. The
     *  diameters of the elements must approach zero. Every element must take a finite amount of computation time to
     *  produce.</li>
     *  <li>Any {@code Real} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param intervals the bounding intervals that define a {@code Real}
     */
    public Real(@NotNull Iterable<Interval> intervals) {
        this.intervals = intervals;
        rational = Optional.empty();
    }

    /**
     * Constructs a {@code Real} from a {@code Rational}.
     *
     * <ul>
     *  <li>{@code r} cannot be null.</li>
     *  <li>Any exact rational {@code Real} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param r a {@code Rational}
     */
    private Real(@NotNull Rational r) {
        intervals = repeat(Interval.of(r));
        rational = Optional.of(r);
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
     * @return the exact {@code Real} equal to {@code r}
     */
    public static @NotNull Real of(@NotNull Rational r) {
        if (r == Rational.ZERO) return ZERO;
        if (r == Rational.ONE) return ONE;
        return new Real(r);
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
     * @return the exact {@code Real} equal to {@code n}
     */
    public static @NotNull Real of(@NotNull BigInteger n) {
        if (n.equals(BigInteger.ZERO)) return ZERO;
        if (n.equals(BigInteger.ONE)) return ONE;
        return new Real(Rational.of(n));
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
     * @return the exact {@code Real} equal to {@code n}
     */
    public static @NotNull Real of(long n) {
        if (n == 0) return ZERO;
        if (n == 1) return ONE;
        return new Real(Rational.of(n));
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
     * @return the exact {@code Real} equal to {@code n}
     */
    public static @NotNull Real of(int n) {
        if (n == 0) return ZERO;
        if (n == 1) return ONE;
        return new Real(Rational.of(n));
    }

    /**
     * Creates a {@code Real} from a {@code BinaryFraction}.
     *
     * <ul>
     *  <li>{@code bf} cannot be null.</li>
     *  <li>The result is a rational number whose denominator is a power of 2, and is exact.</li>
     * </ul>
     *
     * @param bf the {@code BinaryFraction}
     * @return the exact {@code Real} corresponding to {@code bf}
     */
    public static @NotNull Real of(@NotNull BinaryFraction bf) {
        if (bf == BinaryFraction.ZERO) return ZERO;
        if (bf == BinaryFraction.ONE) return ONE;
        return new Real(Rational.of(bf));
    }

    /**
     * Creates a {@code Real} from a {@link float}. This method uses the {@code float}'s {@code String} representation,
     * so that 0.1f becomes 1/10 as might be expected. To get {@code float}'s exact, sometimes-counterintuitive value,
     * use {@link Real#ofExact} instead. Returns empty if the {@code float} is {@code Infinity}, {@code -Infinity}, or
     * {@code NaN}.
     *
     * <ul>
     *  <li>{@code f} may be any {@code float}.</li>
     *  <li>The result is empty or an exact {@code Real} whose decimal expansion is equal to the displayed decimal
     *  expansion of some {@code float}. A necessary but not sufficient condition for this is that the denominator is
     *  of the form 2<sup>m</sup>5<sup>n</sup>, with m,n≥0.</li>
     * </ul>
     *
     * @param f the {@code float}
     * @return the exact {@code Real} corresponding to {@code f}, or empty if {@code f} is {@code Infinity},
     * {@code -Infinity}, or {@code NaN}
     */
    public static @NotNull Optional<Real> of(float f) {
        if (f == 0.0f) return Optional.of(Real.ZERO);
        if (f == 1.0f) return Optional.of(Real.ONE);
        return Rational.of(f).map(Real::new);
    }

    /**
     * Creates a {@code Real} from a {@link double}. This method uses the {@code double}'s {@code String}
     * representation, so that 0.1 becomes 1/10 as might be expected. To get {@code double}'s exact,
     * sometimes-counterintuitive value, use {@link Real#ofExact} instead. Returns empty if the {@code double} is
     * {@code Infinity}, {@code -Infinity}, or {@code NaN}.
     *
     * <ul>
     *  <li>{@code d} may be any {@code double}.</li>
     *  <li>The result is empty or an exact {@code Real} whose decimal expansion is equal to the displayed decimal
     *  expansion of some {@code double}. A necessary but not sufficient conditions for this is that the denominator is
     *  of the form 2<sup>m</sup>5<sup>n</sup>, with m,n≥0.</li>
     * </ul>
     *
     * @param d the {@code double}
     * @return the exact {@code Real} corresponding to {@code d}, or empty if {@code d} is {@code Infinity},
     * {@code -Infinity}, or {@code NaN}
     */
    public static @NotNull Optional<Real> of(double d) {
        if (d == 0.0) return Optional.of(Real.ZERO);
        if (d == 1.0) return Optional.of(Real.ONE);
        return Rational.of(d).map(Real::new);
    }

    /**
     * Creates a {@code Real} from a {@code float}. No rounding occurs; the {@code Real} has exactly the same value as
     * the {@code float}. For example, {@code of(1.0f/3.0f)} yields 11184811/33554432, not 1/3. Returns empty if the
     * {@code float} is {@code Infinity}, {@code -Infinity}, or {@code NaN}.
     *
     * <ul>
     *  <li>{@code f} may be any {@code float}.</li>
     *  <li>
     *   The result is empty or an exact {@code Real} that may be exactly represented as a {@code float}. Here are
     *   some, but not all, of the conditions on the result:
     *   <ul>
     *    <li>The denominator is a power of 2 less than or equal to 2<sup>149</sup>.</li>
     *    <li>The numerator is less than or equal to 2<sup>128</sup>–2<sup>104</sup>.</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param f the {@code float}
     * @return the exact {@code Real} corresponding to {@code f}, or empty if {@code f} is {@code Infinity},
     * {@code -Infinity}, or {@code NaN}
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Optional<Real> ofExact(float f) {
        if (f == 0.0f) return Optional.of(Real.ZERO);
        if (f == 1.0f) return Optional.of(Real.ONE);
        return Rational.ofExact(f).map(Real::new);
    }

    /**
     * Creates a {@code Real} from a {@link double}. No rounding occurs; the {@code Real} has exactly the same value as
     * the {@code double}. For example, {@code of(1.0/3.0)} yields 6004799503160661/18014398509481984, not 1/3. Returns
     * empty if the {@code double} is {@code Infinity}, {@code -Infinity}, or {@code NaN}.
     *
     * <ul>
     *  <li>{@code d} may be any {@code double}.</li>
     *  <li>
     *   The result is empty or a {@code Real} that may be exactly represented as a {@code double}. Here are some, but
     *   not all, of the conditions on the result:
     *   <ul>
     *    <li>The denominator is a power of 2 less than or equal to 2<sup>1074</sup>.</li>
     *    <li>The numerator is less than or equal to 2<sup>1024</sup>–2<sup>971</sup>.</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param d the {@code double}
     * @return the exact {@code Real} corresponding to {@code d}, or empty if {@code d} is {@code Infinity},
     * {@code -Infinity}, or {@code NaN}
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Optional<Real> ofExact(double d) {
        if (d == 0.0) return Optional.of(Real.ZERO);
        if (d == 1.0) return Optional.of(Real.ONE);
        return Rational.ofExact(d).map(Real::new);
    }

    /**
     * Creates a {@code Real} from a {@link BigDecimal}.
     *
     * <ul>
     *  <li>{@code bd} may not be null.</li>
     *  <li>The result is an exact {@code Real} whose denominator may be written as 2<sup>m</sup>5<sup>n</sup>, with
     *  m,n≥0.</li>
     * </ul>
     *
     * @param bd the {@code BigDecimal}
     * @return the exact {@code Real} corresponding to {@code d}
     */
    public static @NotNull Real of(@NotNull BigDecimal bd) {
        return of(Rational.of(bd));
    }

    /**
     * Constructs a fuzzy {@code Real} equal to a {@code Rational}. The resulting {@code Real} is fuzzy on both sides–
     * that is, both its lower and upper bounds approach but never equal its value.
     *
     * <ul>
     *  <li>{@code r} cannot be null.</li>
     *  <li>The result is fuzzy.</li>
     * </ul>
     *
     * @param r a {@code Rational}
     * @return a fuzzy {@code Real} equal to {@code r}
     */
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

    /**
     * Constructs a fuzzy {@code Real} equal to a {@code Rational}. The resulting {@code Real} is fuzzy on the left–
     * that is, its lower bounds approach but never equal its value.
     *
     * <ul>
     *  <li>{@code r} cannot be null.</li>
     *  <li>The result is fuzzy.</li>
     * </ul>
     *
     * @param r a {@code Rational}
     * @return a fuzzy {@code Real} equal to {@code r}
     */
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

    /**
     * Constructs a fuzzy {@code Real} equal to a {@code Rational}. The resulting {@code Real} is fuzzy on the right–
     * that is, its upper bounds approach but never equal its value.
     *
     * <ul>
     *  <li>{@code r} cannot be null.</li>
     *  <li>The result is fuzzy.</li>
     * </ul>
     *
     * @param r a {@code Rational}
     * @return a fuzzy {@code Real} equal to {@code r}
     */
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

    /**
     * Finds a root of a function using bisection, given the ability to test the function's sign at any point and a
     * bounding interval that contains at least one root.
     *
     * <ul>
     *  <li>{@code signTest} can only return 0, 1, or –1.</li>
     *  <li>{@code boundingInterval} must have finite bounds.</li>
     *  <li>{@code signTest} must be defined for all {@code Rational}s in {@code boundingInterval}.</li>
     *  <li>The result may be any {@code Real}.</li>
     * </ul>
     *
     * @param signTest a function which tests the sign of the function whose root we are finding
     * @param boundingInterval an interval that contains at least one root of the function
     * @return a root of the function within the bounding interval
     */
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

    /**
     * Returns the bounding intervals that define {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result is infinite. The diameters of the intervals converge to zero, and every interval (except the
     *  first) is contained in its predecessor.</li>
     * </ul>
     *
     * @return bounding intervals of this {@code Real}
     */
    @Override
    public Iterator<Interval> iterator() {
        return intervals.iterator();
    }

    /**
     * Given a list of target {@code Real}s, returns the 0-based index of the one that is equal to {@code this}. If
     * none are equal to {@code this}, this method will return an incorrect result or throw an exception. If more than
     * one is equal to {@code this}, this method will loop forever.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code targets} cannot be empty and cannot contain nulls.</li>
     *  <li>Exactly one element of {@code targets} must be equal to {@code this}.</li>
     * </ul>
     *
     * @param targets a list of {@code Real}s, one of which is equal to {@code this}
     * @return the index of the {@code Real} in {@code targets} that is equal to {@code this}
     */
    public int match(@NotNull List<Real> targets) {
        if (targets.size() == 1) {
            return 0;
        }
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
                throw new IllegalArgumentException("No element of targets is equal to this. this: " +
                        this + ", targets: " + targets);
            }
            return matchIndex;
        }
    }

    /**
     * A helper function that lifts functions from {@code Rational} to {@code T} to functions from {@code Real} to
     * {@code T}, assuming that the latter can be computed by repeatedly applying the former to increasingly-accurate
     * interval bounds on the {@code Real} argument until f(lower) is equal to f(upper). The function must have any
     * null values. If {@code this} is not exact and the function is discontinuous at {@code this}—that is, if f(lower)
     * will never equal f(upper) no matter how accurate the approximation–then this method will loop forever. To
     * prevent this behavior, use {@link Real#limitValue(Function, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code f} must be defined for all {@code Rational}s and must never return null or throw an exception.</li>
     *  <li>{@code this} must be exact, or {@code f} must be continuous at {@code this}.</li>
     * </ul>
     *
     * @param f a function from {@code Rational} to {@code T}
     * @param <T> the type of the result of {@code f}
     * @return {@code f} applied to {@code this}
     */
    private <T> T limitValueUnsafe(@NotNull Function<Rational, T> f) {
        if (rational.isPresent()) {
            return f.apply(rational.get());
        }
        Rational previousLower = null;
        Rational previousUpper = null;
        T lowerValue = null;
        T upperValue = null;
        Iterator<Interval> as = intervals.iterator();
        Interval a;
        do {
            a = as.next();
        } while (!a.isFinitelyBounded());
        while (true) {
            if (Thread.interrupted()) return null;
            Rational lower = a.getLower().get();
            Rational upper = a.getUpper().get();
            if (previousLower == null || !previousLower.equals(lower)) {
                lowerValue = f.apply(lower);
                if (lowerValue == null) {
                    throw new IllegalArgumentException("f returned null on " + lower);
                }
            }
            if (previousUpper == null || !previousUpper.equals(upper)) {
                upperValue = f.apply(upper);
                if (upperValue == null) {
                    throw new IllegalArgumentException("f returned null on " + upper);
                }
            }
            if (lowerValue.equals(upperValue)) {
                return lowerValue;
            }
            previousLower = lower;
            previousUpper = upper;
            a = as.next();
        }
    }

    /**
     * A helper function that lifts functions from {@code Rational} to {@code T} to functions from {@code Real} to
     * {@code T}, assuming that the latter can be computed by repeatedly applying the former to increasingly-accurate
     * interval bounds on the {@code Real} argument until f(lower) is equal to f(upper). The function must have any
     * null values. If {@code this} is not exact and the function is discontinuous at {@code this}—that is, if f(lower)
     * will never equal f(upper) no matter how accurate the approximation–then this method will give up and return
     * empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>{@code f} must be defined for all {@code Rational}s and must never return null or throw an exception.</li>
     * </ul>
     *
     * @param f a function from {@code Rational} to {@code T}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @param <T> the type of the result of {@code f}
     * @return {@code f} applied to {@code this}
     */
    private @NotNull <T> Optional<T> limitValue(
            @NotNull Function<Rational, T> f,
            @NotNull Rational resolution
    ) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(f.apply(rational.get()));
        }
        Rational previousLower = null;
        Rational previousUpper = null;
        T lowerValue = null;
        T upperValue = null;
        Iterator<Interval> as = intervals.iterator();
        Interval a;
        do {
            a = as.next();
        } while (!a.isFinitelyBounded());
        while (true) {
            if (Thread.interrupted()) return Optional.empty();
            Rational lower = a.getLower().get();
            Rational upper = a.getUpper().get();
            if (previousLower == null || !previousLower.equals(lower)) {
                lowerValue = f.apply(lower);
                if (lowerValue == null) {
                    throw new IllegalArgumentException("f returned null on " + lower);
                }
            }
            if (previousUpper == null || !previousUpper.equals(upper)) {
                upperValue = f.apply(upper);
                if (upperValue == null) {
                    throw new IllegalArgumentException("f returned null on " + upper);
                }
            }
            if (lowerValue.equals(upperValue)) {
                return Optional.of(lowerValue);
            }
            if (Ordering.lt(upper.subtract(lower), resolution)) {
                return Optional.empty();
            }
            previousLower = lower;
            previousUpper = upper;
            a = as.next();
        }
    }

    /**
     * Determines whether {@code this} is an exact integer.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether this is an exact integer
     */
    public boolean isExactInteger() {
        return rational.isPresent() && rational.get().isInteger();
    }

    /**
     * Rounds {@code this} to an integer according to {@code roundingMode}; see {@link java.math.RoundingMode} for
     * details. If {@code this} is a fuzzy integer or a fuzzy half-integer, this method may loop forever, depending on
     * the rounding mode and the nature of the fuzziness. To prevent this behavior, use
     * {@link Real#bigIntegerValue(RoundingMode, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code roundingMode} may be any {@code RoundingMode}.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UP}, {@code this} cannot be a positive integer
     *  that is fuzzy on the right, a negative integer that is fuzzy on the left, or a fuzzy zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#DOWN}, {@code this} cannot be a positive integer
     *  that is fuzzy on the left or a negative integer that is fuzzy on the right. However, it may be a fuzzy
     *  zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#CEILING}, {@code this} cannot be an integer that
     *  is fuzzy on the right.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#FLOOR}, {@code this} cannot be an integer that is
     *  fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_UP}, {@code this} cannot be a positive half-
     *  integer that is fuzzy on the left or a negative half-integer that is fuzzy on the right. However, it may be a
     *  fuzzy integer.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_DOWN}, {@code this} cannot be a positive
     *  half-integer that is fuzzy on the right or a negative half-integer that is fuzzy on the left. However, it may
     *  be a fuzzy integer.</li>
     *  <li>Let k be an integer. If {@code roundingMode} is {@link java.math.RoundingMode#HALF_EVEN}, {@code this}
     *  cannot be equal to (4k+1)/2 and fuzzy on the right, or equal to (4k+3)/2 and fuzzy on the left. However, it may
     *  be a fuzzy integer.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UNNECESSARY}, {@code this} must be an exact
     *  integer.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param roundingMode determines the way in which {@code this} is rounded. Options are
     * {@link java.math.RoundingMode#UP}, {@link java.math.RoundingMode#DOWN}, {@link java.math.RoundingMode#CEILING},
     * {@link java.math.RoundingMode#FLOOR}, {@link java.math.RoundingMode#HALF_UP},
     * {@link java.math.RoundingMode#HALF_DOWN}, {@link java.math.RoundingMode#HALF_EVEN}, and
     * {@link java.math.RoundingMode#UNNECESSARY}.
     * @return {@code this}, rounded
     */
    public @NotNull BigInteger bigIntegerValueUnsafe(@NotNull RoundingMode roundingMode) {
        if (rational.isPresent()) {
            return rational.get().bigIntegerValue(roundingMode);
        }
        if (roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be an exact integer. Invalid" +
                    " this: " + this);
        }
        //noinspection ConstantConditions
        return limitValueUnsafe(r -> r.bigIntegerValue(roundingMode));
    }

    /**
     * Rounds {@code this} to an integer according to {@code roundingMode}; see {@link java.math.RoundingMode} for
     * details. If {@code this} is a fuzzy integer or a fuzzy half-integer, depending on the rounding mode and the
     * nature of the fuzziness (see {@link Real#bigIntegerValueUnsafe(RoundingMode)}, this method will give up and
     * return empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code roundingMode} may be any {@code RoundingMode}.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UNNECESSARY}, {@code this} must be an exact
     *  integer.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param roundingMode determines the way in which {@code this} is rounded. Options are
     * {@link java.math.RoundingMode#UP}, {@link java.math.RoundingMode#DOWN}, {@link java.math.RoundingMode#CEILING},
     * {@link java.math.RoundingMode#FLOOR}, {@link java.math.RoundingMode#HALF_UP},
     * {@link java.math.RoundingMode#HALF_DOWN}, {@link java.math.RoundingMode#HALF_EVEN}, and
     * {@link java.math.RoundingMode#UNNECESSARY}.
     * @return {@code this}, rounded
     */
    public @NotNull Optional<BigInteger> bigIntegerValue(
            @NotNull RoundingMode roundingMode,
            @NotNull Rational resolution
    ) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (roundingMode == RoundingMode.UNNECESSARY && !isExactInteger()) {
            throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be an exact integer. Invalid" +
                    " this: " + this);
        }
        return limitValue(r -> r.bigIntegerValue(roundingMode), resolution);
    }

    /**
     * Rounds {@code this} to an integer, breaking ties with the half-even rule (see
     * {@link java.math.RoundingMode#HALF_EVEN}). If {@code this} is a fuzzy half-integer, this method may loop
     * forever, depending on the nature of the fuzziness. To prevent this behavior, use
     * {@link Real#bigIntegerValue(Rational)} instead.
     *
     * <ul>
     *  <li>Let k be an integer. {@code this} cannot be equal to 2k+1/2 and fuzzy on the right, or equal to 2k+3/2 and
     *  fuzzy on the left. However, it may be a fuzzy integer.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return {@code this}, rounded
     */
    public @NotNull BigInteger bigIntegerValueUnsafe() {
        return bigIntegerValueUnsafe(RoundingMode.HALF_EVEN);
    }

    /**
     * Rounds {@code this} to an integer, breaking ties with the half-even rule (see
     * {@link java.math.RoundingMode#HALF_EVEN}). If {@code this} is a fuzzy half-integer, depending on the nature of
     * the fuzziness (see {@link Real#bigIntegerValueUnsafe(RoundingMode)}, this method will give up and return empty
     * once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return {@code this}, rounded
     */
    public @NotNull Optional<BigInteger> bigIntegerValue(@NotNull Rational resolution) {
        return bigIntegerValue(RoundingMode.HALF_EVEN, resolution);
    }

    /**
     * Returns the floor of {@code this}. If {@code this} is an integer that is fuzzy on the left, this method will
     * loop forever. To prevent this behavior, use {@link Real#floor(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} cannot be an integer that is fuzzy on the left.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return ⌊{@code this}⌋
     */
    public @NotNull BigInteger floorUnsafe() {
        return bigIntegerValueUnsafe(RoundingMode.FLOOR);
    }

    /**
     * Returns the floor of {@code this}. If {@code this} is an integer that is fuzzy on the left, this method will
     * give up and return empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return ⌊{@code this}⌋
     */
    public @NotNull Optional<BigInteger> floor(@NotNull Rational resolution) {
        return bigIntegerValue(RoundingMode.FLOOR, resolution);
    }

    /**
     * Returns the ceiling of {@code this}. If {@code this} is an integer that is fuzzy on the right, this method will
     * loop forever. To prevent this behavior, use {@link Real#ceiling(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} cannot be an integer that is fuzzy on the right.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return ⌈{@code this}⌉
     */
    public @NotNull BigInteger ceilingUnsafe() {
        return bigIntegerValueUnsafe(RoundingMode.CEILING);
    }

    /**
     * Returns the ceiling of {@code this}. If {@code this} is an integer that is fuzzy on the right, this method will
     * give up and return empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return ⌈{@code this}⌉
     */
    public @NotNull Optional<BigInteger> ceiling(@NotNull Rational resolution) {
        return bigIntegerValue(RoundingMode.CEILING, resolution);
    }

    /**
     * Converts {@code this} to a {@code BigInteger}. Throws an {@link java.lang.ArithmeticException} if {@code this}
     * is not an exact integer.
     *
     * <ul>
     *  <li>{@code this} must be an exact integer.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return the {@code BigInteger} value of {@code this}
     */
    public @NotNull BigInteger bigIntegerValueExact() {
        if (rational.isPresent()) {
            return rational.get().bigIntegerValueExact();
        } else {
            throw new ArithmeticException("this must be an integer. Invalid this: " + this);
        }
    }

    /**
     * Converts {@code this} to a {@code byte}. Throws an {@link java.lang.ArithmeticException} if {@code this} is not
     * an exact integer or outside of a {@code byte}'s range.
     *
     * <ul>
     *  <li>{@code this} must be an exact integer within a {@code byte}'s range.</li>
     *  <li>The result can be any {@code byte}.</li>
     * </ul>
     *
     * @return the {@code byte} value of {@code this}
     */
    public byte byteValueExact() {
        return bigIntegerValueExact().byteValueExact();
    }

    /**
     * Converts {@code this} to a {@code short}. Throws an {@link java.lang.ArithmeticException} if {@code this} is not
     * an exact integer or outside of a {@code short}'s range.
     *
     * <ul>
     *  <li>{@code this} must be an exact integer within a {@code short}'s range.</li>
     *  <li>The result can be any {@code short}.</li>
     * </ul>
     *
     * @return the {@code short} value of {@code this}
     */
    public short shortValueExact() {
        return bigIntegerValueExact().shortValueExact();
    }

    /**
     * Converts {@code this} to an {@code int}. Throws an {@link java.lang.ArithmeticException} if {@code this} is not
     * an exact integer or outside of an {@code int}'s range.
     *
     * <ul>
     *  <li>{@code this} must be an exact integer within an {@code int}'s range.</li>
     *  <li>The result can be any {@code int}.</li>
     * </ul>
     *
     * @return the {@code int} value of {@code this}
     */
    public int intValueExact() {
        return bigIntegerValueExact().intValueExact();
    }

    /**
     * Converts {@code this} to a {@code long}. Throws an {@link java.lang.ArithmeticException} if {@code this} is not
     * an exact integer or outside of a {@code long}'s range.
     *
     * <ul>
     *  <li>{@code this} must be an exact integer within a {@code long}'s range.</li>
     *  <li>The result can be any {@code long}.</li>
     * </ul>
     *
     * @return the {@code long} value of {@code this}
     */
    public long longValueExact() {
        return bigIntegerValueExact().longValueExact();
    }

    /**
     * Determines whether {@code this} is an exact integer power of 2.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is an exact integer power of two
     */
    public boolean isExactIntegerPowerOfTwo() {
        if (!rational.isPresent()) {
            return false;
        } else {
            Rational r = rational.get();
            return r.signum() == 1 && r.isPowerOfTwo();
        }
    }

    /**
     * Rounds {@code this} to the next-highest integer power of two. If {@code this} is an integer power of two, it is
     * returned. If {@code this} is a zero or an integer power of two that is fuzzy on the right, this method will loop
     * forever. To prevent this behavior, use {@link Real#roundUpToIntegerPowerOfTwo(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} cannot be negative, a fuzzy zero, or an integer power of two that is fuzzy on the right.</li>
     *  <li>The result is an integer power of two. (That is, it is equal to 2<sup>p</sup> for some integer p, but the
     *  result itself is not necessarily an integer.)</li>
     * </ul>
     *
     * @return the smallest integer power of 2 greater than or equal to {@code this}.
     */
    public @NotNull BinaryFraction roundUpToIntegerPowerOfTwoUnsafe() {
        Rational previousLower = null;
        Rational previousUpper = null;
        BinaryFraction lowerValue = null;
        BinaryFraction upperValue = null;
        Iterator<Interval> as = intervals.iterator();
        Interval a;
        do {
            a = as.next();
        } while (!a.isFinitelyBounded());
        while (true) {
            Rational lower = a.getLower().get();
            Rational upper = a.getUpper().get();
            if (upper.signum() != 1) {
                throw new ArithmeticException();
            }
            if (lower.signum() == 1) {
                if (!lower.equals(previousLower)) {
                    lowerValue = lower.roundUpToPowerOfTwo();
                }
                if (!upper.equals(previousUpper)) {
                    upperValue = upper.roundUpToPowerOfTwo();
                }
                if (lowerValue.equals(upperValue)) {
                    return lowerValue;
                }
                previousLower = lower;
                previousUpper = upper;
            }
            a = as.next();
        }
    }

    /**
     * Rounds {@code this} to the next-highest integer power of two. If {@code this} is an integer power of two, it is
     * returned. If {@code this} is a zero or an integer power of two that is fuzzy on the right, this method will give
     * up and return empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} cannot be negative, an exact zero, or a zero that is only fuzzy on the left.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is empty or an integer power of two. (That is, it is equal to 2<sup>p</sup> for some integer p,
     *  but the result itself is not necessarily an integer.)</li>
     * </ul>
     *
     * @return the smallest integer power of 2 greater than or equal to {@code this}.
     */
    public @NotNull Optional<BinaryFraction> roundUpToIntegerPowerOfTwo(@NotNull Rational resolution) {
        Rational previousLower = null;
        Rational previousUpper = null;
        BinaryFraction lowerValue = null;
        BinaryFraction upperValue = null;
        Iterator<Interval> as = intervals.iterator();
        Interval a;
        do {
            a = as.next();
        } while (!a.isFinitelyBounded());
        while (true) {
            Rational lower = a.getLower().get();
            Rational upper = a.getUpper().get();
            if (upper.signum() != 1) {
                throw new ArithmeticException();
            }
            if (lower.signum() == 1) {
                if (!lower.equals(previousLower)) {
                    lowerValue = lower.roundUpToPowerOfTwo();
                }
                if (!upper.equals(previousUpper)) {
                    upperValue = upper.roundUpToPowerOfTwo();
                }
                if (lowerValue.equals(upperValue)) {
                    return Optional.of(lowerValue);
                }
                previousLower = lower;
                previousUpper = upper;
            }
            if (Ordering.lt(a.diameter().get(), resolution)) {
                return Optional.empty();
            }
            a = as.next();
        }
    }

    /**
     * Determines whether {@code this} is exact and a binary fraction (whether it is rational and its denominator is a
     * power of 2).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is an exact binary fraction
     */
    public boolean isExactBinaryFraction() {
        return rational.isPresent() && rational.get().isBinaryFraction();
    }

    /**
     * Converts {@code this} to a {@code BinaryFraction}. Throws an {@link java.lang.ArithmeticException} if
     * {@code this} is not an exact binary fraction.
     *
     * <ul>
     *  <li>{@code this} must be exact and a binary fraction (its denominator must be a power of 2.)</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return the {@code BinaryFraction} value of {@code this}
     */
    public @NotNull BinaryFraction binaryFractionValueExact() {
        if (rational.isPresent()) {
            return rational.get().binaryFractionValueExact();
        } else {
            throw new ArithmeticException("this must be an exact binary fraction. Invalid this: " + this);
        }
    }

    /**
     * Whether this immediately converges to its value—that is, whether this is rational and all of its intervals have
     * diameter zero.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is an exact {@code Real}
     */
    public boolean isExact() {
        return rational.isPresent();
    }

    /**
     * If {@code this} is exact, returns the {@code Rational} equal to {@code this}; otherwise, returns an empty
     * {@code Optional}.
     *
     * <ul>
     *  <li>{@code this} mat be any {@code Real}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return the {@code Rational} equal to {@code this}, if {@code this} is exact
     */
    public @NotNull Optional<Rational> rationalValueExact() {
        return rational;
    }

    /**
     * This method returns the floor of the base-2 logarithm of {@code this}. In other words, every positive
     * {@code Real} may be written as a×2<sup>b</sup>, where a is a {@code Real} such that 1≤a{@literal <}2 and b is an
     * integer; this method returns b. If {@code this} is a zero that is fuzzy on the right or an integer power of two
     * that is fuzzy on the left, this method will loop forever. To prevent this behavior, use
     * {@link Real#binaryExponent(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} cannot be negative, zero, or an integer power of two that is fuzzy on the left.</li>
     *  <li>The result can be any integer.</li>
     * </ul>
     *
     * @return ⌊{@code log<sub>2</sub>this}⌋
     */
    public int binaryExponentUnsafe() {
        Rational previousLower = null;
        Rational previousUpper = null;
        int lowerValue = 0;
        int upperValue = 0;
        Iterator<Interval> as = intervals.iterator();
        Interval a;
        do {
            a = as.next();
        } while (!a.isFinitelyBounded());
        while (true) {
            Rational lower = a.getLower().get();
            Rational upper = a.getUpper().get();
            if (upper.signum() != 1) {
                throw new ArithmeticException();
            }
            if (lower.signum() == 1) {
                if (!lower.equals(previousLower)) {
                    lowerValue = lower.binaryExponent();
                }
                if (!upper.equals(previousUpper)) {
                    upperValue = upper.binaryExponent();
                }
                if (lowerValue == upperValue) {
                    return lowerValue;
                }
                previousLower = lower;
                previousUpper = upper;
            }
            a = as.next();
        }
    }

    /**
     * This method returns the floor of the base-2 logarithm of {@code this}. In other words, every positive
     * {@code Real} may be written as a×2<sup>b</sup>, where a is a {@code Real} such that 1≤a{@literal <}2 and b is an
     * integer; this method returns b. If {@code this} is a zero that is fuzzy on the right or an integer power of two
     * that is fuzzy on the left, this method will give up and return empty once the approximating interval's diameter
     * is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} cannot be negative, an exact zero, or a zero that is fuzzy on the left.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result can be any integer.</li>
     * </ul>
     *
     * @return ⌊{@code log<sub>2</sub>this}⌋
     */
    public @NotNull Optional<Integer> binaryExponent(@NotNull Rational resolution) {
        Rational previousLower = null;
        Rational previousUpper = null;
        int lowerValue = 0;
        int upperValue = 0;
        Iterator<Interval> as = intervals.iterator();
        Interval a;
        do {
            a = as.next();
        } while (!a.isFinitelyBounded());
        while (true) {
            Rational lower = a.getLower().get();
            Rational upper = a.getUpper().get();
            if (upper.signum() != 1) {
                throw new ArithmeticException();
            }
            if (lower.signum() == 1) {
                if (!lower.equals(previousLower)) {
                    lowerValue = lower.binaryExponent();
                }
                if (!upper.equals(previousUpper)) {
                    upperValue = upper.binaryExponent();
                }
                if (lowerValue == upperValue) {
                    return Optional.of(lowerValue);
                }
                previousLower = lower;
                previousUpper = upper;
            }
            if (Ordering.lt(a.diameter().get(), resolution)) {
                return Optional.empty();
            }
            a = as.next();
        }
    }

    /**
     * Determines whether {@code this} is exact and exactly equal to some {@code float}. If true, the {@code float} may
     * be found using //todo
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is exact and exactly equal to a {@code float}
     */
    public boolean isExactAndEqualToFloat() {
        return rational.isPresent() && rational.get().isEqualToFloat();
    }

    /**
     * Determines whether {@code this} is exact and exactly equal to some {@code double}. If true, the {@code double}
     * may be found using //todo
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is exact and exactly equal to a {@code double}
     */
    public boolean isExactAndEqualToDouble() {
        return rational.isPresent() && rational.get().isEqualToDouble();
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

    public @NotNull Real negate() {
        if (rational.isPresent()) {
            return new Real(rational.get().negate());
        } else {
            return new Real(map(Interval::negate, intervals));
        }
    }

    public @NotNull Real add(@NotNull Real that) {
        return new Real(zipWith(Interval::add, intervals, that.intervals));
    }

    public @NotNull Real subtract(@NotNull Real that) {
        if (this == that) return ZERO;
        return new Real(zipWith(Interval::subtract, intervals, that.intervals));
    }

    public @NotNull Real multiply(@NotNull Real that) {
        if (rational.isPresent()) {
            if (that.rational.isPresent()) {
                return new Real(rational.get().multiply(that.rational.get()));
            } else {
                Rational thisR = rational.get();
                return new Real(map(i -> i.multiply(thisR), that.intervals));
            }
        } else {
            if (that.rational.isPresent()) {
                Rational thatR = that.rational.get();
                return new Real(map(i -> i.multiply(thatR), intervals));
            } else {
                return new Real(zipWith(Interval::multiply, intervals, that.intervals));
            }
        }
    }

    public @NotNull Real multiply(@NotNull Rational that) {
        if (rational.isPresent()) {
            return new Real(rational.get().multiply(that));
        } else {
            return new Real(map(i -> i.multiply(that), intervals));
        }
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

    public @NotNull Real shiftRight(int bits) {
        return new Real(map(a -> a.shiftRight(bits), intervals));
    }

    public int signumUnsafe() {
        return limitValueUnsafe(Rational::signum);
    }

    public @NotNull Optional<Integer> signum(@NotNull Rational resolution) {
        return limitValue(Rational::signum, resolution);
    }

    public @NotNull Real abs() {
        return new Real(map(Interval::abs, intervals));
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
        Optional<Rational> or = rationalValueExact();
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
        Optional<Rational> or = rationalValueExact();
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
        String result;
        String afterDecimal = concatStrings(map(digitFunction, filter(d -> d.signum() != -1, take(scale, digits.b))));
        result = beforeDecimal + "." + afterDecimal;
        return (signumUnsafe() == -1 ? "-" + result : result) + "...";
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

    private static @NotNull Real atan01(@NotNull Rational x) {
        if (x == Rational.ZERO) return ZERO;
        if (x == Rational.ONE) return PI.shiftRight(2);
        return new Real(() -> new Iterator<Interval>() {
            private @NotNull Rational xSquared = x.pow(2);
            private @NotNull Rational partialSum = x;
            private @NotNull Rational xPower = x;
            private @NotNull BigInteger denominator = BigInteger.ONE;
            private boolean first = true;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Interval next() {
                Rational upper;
                if (first) {
                    first = false;
                    upper = partialSum;
                } else {
                    xPower = xPower.multiply(xSquared);
                    denominator = denominator.add(IntegerUtils.TWO);
                    partialSum = partialSum.add(xPower.divide(denominator));
                    upper = partialSum;
                }
                xPower = xPower.multiply(xSquared);
                denominator = denominator.add(IntegerUtils.TWO);
                partialSum = partialSum.subtract(xPower.divide(denominator));
                return Interval.of(partialSum, upper);
            }
        });
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

    public @NotNull Real fractionalPartUnsafe() {
        if (rational.isPresent()) {
            Rational fp = rational.get().fractionalPart();
            return fp == Rational.ZERO ? ZERO : new Real(fp);
        } else {
            return subtract(of(floorUnsafe()));
        }
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

    public static @NotNull Optional<Boolean> eq(@NotNull Real x, @NotNull Real y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c == 0);
    }

    public static @NotNull Optional<Boolean> ne(@NotNull Real x, @NotNull Real y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c != 0);
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
        Optional<Rational> thisRational = rationalValueExact();
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

    public static boolean eqUnsafe(@NotNull Real x, @NotNull Rational y) {
        return x.compareToUnsafe(y) == 0;
    }

    public static boolean neUnsafe(@NotNull Real x, @NotNull Rational y) {
        return x.compareToUnsafe(y) != 0;
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
        Optional<Rational> thisRational = rationalValueExact();
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

    public static @NotNull Optional<Boolean> eq(@NotNull Real x, @NotNull Rational y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c == 0);
    }

    public static @NotNull Optional<Boolean> ne(@NotNull Real x, @NotNull Rational y, @NotNull Rational resolution) {
        return x.compareTo(y, resolution).map(c -> c != 0);
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

    @Override
    public boolean equals(Object that) {
        throw new UnsupportedOperationException("");
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
