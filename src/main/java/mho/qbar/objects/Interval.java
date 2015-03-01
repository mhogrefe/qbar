package mho.qbar.objects;

import mho.wheels.iterables.IterableUtils;
import mho.wheels.misc.FloatingPointUtils;
import mho.wheels.misc.Readers;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;

/**
 * <p>The {@code Interval} class represents an interval of real numbers. If we let p and q be rationals, p≤q, the
 * representable intervals are (–∞, ∞), (–∞, q], [p, ∞), and [p, q]. If p = q, the {@code Interval} represents a single
 * number. The empty interval cannot be represented.
 *
 * <p>In general, f(I), where I is an {@code Interval}, is taken to mean the image of I under f. Often this image is
 * not an {@code Interval} itself, in which case the function might return a set of {@code Interval}s, whose closed
 * union is the image, or it may just return the closure of the image's convex hull. Similar considerations apply for
 * functions of two {@code Interval}s, etc.
 *
 * <p>This class is immutable.
 */
public final class Interval implements Comparable<Interval> {
    /**
     * [0, 0]
     */
    public static final @NotNull Interval ZERO = new Interval(Rational.ZERO, Rational.ZERO);

    /**
     * [1, 1]
     */
    public static final @NotNull Interval ONE = new Interval(Rational.ONE, Rational.ONE);

    /**
     * (–∞, ∞)
     */
    public static final @NotNull Interval ALL = new Interval(null, null);

    /**
     * The lower bound of this interval if the lower bound is finite, or null if the lower bound is –∞
     */
    private final @Nullable Rational lower;

    /**
     * The upper bound of this interval if the upper bound is finite, or null if the upper bound is ∞
     */
    private final @Nullable Rational upper;

    /**
     * Private constructor from {@link Rational}s; assumes arguments are valid. If lower is null, the
     * {@code Interval}'s lower bound is –∞; if upper is null, the {@code Interval}'s upper bound is ∞.
     *
     * <ul>
     *  <li>{@code lower} may be any {@code Rational}, or null.</li>
     *  <li>{@code upper} may be any {@code Rational}, or null.</li>
     *  <li>If {@code lower} and {@code upper} are both non-null, {@code lower} must be less than or equal to
     *  {@code upper}.</li>
     * </ul>
     *
     * @param lower the lower bound
     * @param upper the upper bound
     */
    private Interval(@Nullable Rational lower, @Nullable Rational upper) {
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * Returns this {@code Interval}'s lower bound. If the lower bound is –∞, an empty {@code Optional} is returned.
     *
     * <ul>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return the lower bound
     */
    public @NotNull Optional<Rational> getLower() {
        return lower == null ? Optional.<Rational>empty() : Optional.of(lower);
    }

    /**
     * Returns this {@code Interval}'s upper bound. If the lower bound is ∞, an empty {@code Optional} is returned.
     *
     * <ul>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return the upper bound
     */
    public @NotNull Optional<Rational> getUpper() {
        return upper == null ? Optional.<Rational>empty() : Optional.of(upper);
    }

    /**
     * Creates a finitely-bounded {@code Interval} from {@code Rationals}.
     *
     * <ul>
     *  <li>{@code lower} cannot be null.</li>
     *  <li>{@code upper} cannot be null.</li>
     *  <li>{@code lower} must be less than or equal to {@code upper}.</li>
     * </ul>
     *
     * @param lower the lower bound
     * @param upper the upper bound
     * @return [{@code lower}, {@code upper}]
     */
    public static @NotNull Interval of(@NotNull Rational lower, @NotNull Rational upper) {
        if (gt(lower, upper))
            throw new IllegalArgumentException("lower bound cannot be greater than upper bound");
        return new Interval(lower, upper);
    }

    /**
     * Creates an interval whose lower bound is –∞.
     *
     * <ul>
     *  <li>{@code upper} cannot be null.</li>
     * </ul>
     *
     * @param upper the upper bound
     * @return (–∞, {@code upper}]
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Interval lessThanOrEqualTo(@NotNull Rational upper) {
        return new Interval(null, upper);
    }

    /**
     * Creates an interval whose upper bound is ∞.
     *
     * <ul>
     *  <li>{@code lower} cannot be null.</li>
     * </ul>
     *
     * @param lower the lower bound
     * @return [{@code lower}, ∞)
     */
    public static @NotNull Interval greaterThanOrEqualTo(@NotNull Rational lower) {
        return new Interval(lower, null);
    }

    /**
     * Creates an interval containing exactly one {@code Rational}.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     * </ul>
     *
     * @param x the value contained in this {@code Interval}
     * @return [{@code x}, {@code x}]
     */
    public static @NotNull Interval of(@NotNull Rational x) {
        return new Interval(x, x);
    }

    /**
     * Determines whether {@code this} has finite bounds.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is finitely-bounded
     */
    public boolean isFinitelyBounded() {
        return lower != null && upper != null;
    }

    /**
     * Determines whether {@code this} contains a {@code Rational}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param x the test {@code Rational}
     * @return {@code x}∈{@code this}
     */
    public boolean contains(@NotNull Rational x) {
        if (lower == null && upper == null) return true;
        if (lower == null) return le(x, upper);
        if (upper == null) return ge(x, lower);
        return ge(x, lower) && le(x, upper);
    }

    /**
     * Determines whether {@code this} contains (is a superset of) an {@code Interval}. Every {@code Interval} contains
     * itself.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that the test {@code Interval}
     * @return {@code that}⊆{@code this}
     */
    public boolean contains(@NotNull Interval that) {
        return (lower == null || (that.lower != null && ge(that.lower, lower))) &&
               (upper == null || (that.upper != null && le(that.upper, upper)));
    }

    /**
     * Determines the diameter (length) of {@code this}. If {@code this} has infinite diameter, an empty
     * {@code Optional} is returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result may be empty, or it may contain a non-negative {@code Rational}.</li>
     * </ul>
     *
     * @return the diameter of {@code this}
     */
    public @NotNull Optional<Rational> diameter() {
        if (lower == null || upper == null) return Optional.empty();
        return Optional.of(upper.subtract(lower));
    }

    /**
     * Determines the convex hull of two {@code Interval}s, or the {@code Interval} with minimal diameter that is a
     * superset of the two {@code Interval}s.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Interval} to be hulled with {@code this}
     * @return Conv({@code this}, {@code that})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Interval convexHull(@NotNull Interval that) {
        return new Interval(
                lower == null || that.lower == null ? null : min(lower, that.lower),
                upper == null || that.upper == null ? null : max(upper, that.upper)
        );
    }

    /**
     * Determines the convex hull of a set of {@code Interval}s, or the {@code Interval} with minimal diameter that is
     * a superset of all of the {@code Interval}s.
     *
     * <ul>
     *  <li>{@code as} cannot be empty and may not contain any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param as the {@code Interval}s.
     * @return Conv({@code as})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Interval convexHull(@NotNull List<Interval> as) {
        if (any(a -> a == null, as))
            throw new NullPointerException();
        //noinspection ConstantConditions
        return foldl1(p -> p.a.convexHull(p.b), as);
    }

    /**
     * Returns the intersection of two {@code Interval}s. If the intersection is empty, an empty {@code Optional} is
     * returned.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Interval} to be intersected with {@code this}
     * @return {@code this}∩{@code that}
     */
    public @NotNull Optional<Interval> intersection(@NotNull Interval that) {
        Rational iLower;
        if (lower == null && that.lower == null) {
            iLower = null;
        } else if (lower == null) {
            iLower = that.lower;
        } else if (that.lower == null) {
            iLower = lower;
        } else {
            iLower = max(lower, that.lower);
        }
        Rational iUpper;
        if (upper == null && that.upper == null) {
            iUpper = null;
        } else if (upper == null) {
            iUpper = that.upper;
        } else if (that.upper == null) {
            iUpper = upper;
        } else {
            iUpper = min(upper, that.upper);
        }
        if (iLower != null && iUpper != null && gt(iLower, iUpper)) return Optional.empty();
        return Optional.of(new Interval(iLower, iUpper));
    }

    /**
     * Determines whether two {@code Interval}s are disjoint (whether their intersections are empty).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that the {@code Interval} tested for disjointness with {@code this}
     * @return {@code this}∩{@code that}=∅
     */
    public boolean disjoint(@NotNull Interval that) {
        return !intersection(that).isPresent();
    }

    /**
     * Transforms a list of {@code Interval}s into a list of disjoint {@code Interval}s with the same union as the
     * original set.
     *
     * <ul>
     *  <li>{@code as} cannot be null and may not contain any null elements.</li>
     *  <li>The result is a sorted list of pairwise disjoint {@code Interval}s.</li>
     * </ul>
     *
     * @param as a list of {@code Interval}s
     * @return a list of disjoint {@code Interval}s whose union is the same as the union of {@code as}
     */
    public static @NotNull List<Interval> makeDisjoint(@NotNull List<Interval> as) {
        List<Interval> simplified = new ArrayList<>();
        Interval acc = null;
        for (Interval a : sort(as)) {
            if (acc == null) {
                acc = a;
            } else if (acc.disjoint(a)) {
                simplified.add(acc);
                acc = a;
            } else {
                acc = acc.convexHull(a);
            }
        }
        if (acc != null) simplified.add(acc);
        return simplified;
    }

    /**
     * The complement of an {@code Interval} generally involves open intervals, so it can't be represented as a union
     * of {@code Interval}s. However, we can get close; we can represent the complement's closure. This is equal to the
     * complement, except that it includes the endpoints of the original {@code Interval}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>
     *   The result is in one of these forms:
     *   <ul>
     *    <li>an empty list</li>
     *    <li>a list containing one {@code Interval}, unbounded on one side</li>
     *    <li>a list containing (–∞, ∞); this happens when {@code this} has diameter 0</li>
     *    <li>a list containing two disjoint {@code Intervals}, the first unbounded on the left and the second
     *    unbounded on the right</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @return the closure of ℝ\{@code this}
     */
    public @NotNull List<Interval> complement() {
        if (lower == null && upper == null) return new ArrayList<>();
        if (lower == null) return Arrays.asList(new Interval(upper, null));
        if (upper == null) return Arrays.asList(new Interval(null, lower));
        if (lower.equals(upper)) return Arrays.asList(ALL);
        return Arrays.asList(new Interval(null, lower), new Interval(upper, null));
    }

    /**
     * Returns the midpoint of {@code this}.
     *
     * <ul>
     *  <li>{@code this} must be finitely bounded.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return the average of {@code lower} and {@code upper}.
     */
    public @NotNull Rational midpoint() {
        if (lower == null || upper == null)
            throw new ArithmeticException("an unbounded interval has no midpoint");
        return lower.add(upper).shiftRight(1);
    }

    /**
     * Splits {@code this} into two intervals at {@code x}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code x} cannot be null.</li>
     *  <li>{@code this} must contain {@code x}.</li>
     *  <li>The result is a pair of {@code Interval}s, neither null, such that the upper bound of the first is equal
     *  to the lower bound of the second.</li>
     * </ul>
     *
     * @param x the point at which {@code this} is split.
     * @return the two pieces of {@code this}.
     */
    public @NotNull Pair<Interval, Interval> split(@NotNull Rational x) {
        if (!contains(x))
            throw new ArithmeticException("interval does not contain specified split point");
        return new Pair<>(new Interval(lower, x), new Interval(x, upper));
    }

    /**
     * Splits {@code this} into two equal {@code Interval}s.
     *
     * <ul>
     *  <li>{@code this} must be finitely bounded.</li>
     *  <li>The result is a pair of equal-diameter, finitely-bounded {@code Interval}s such the the upper bound of the
     *  first is equal to the lower bound of the second.</li>
     * </ul>
     *
     * @return the two halves of {@code this}.
     */
    public @NotNull Pair<Interval, Interval> bisect() {
        return split(midpoint());
    }

    /**
     * Returns the smallest {@code Interval} containing all reals that are closer to a given {@code float} than to
     * any other {@code float}. Some special cases must be taken into account:
     * <ul>
     *  <li>If the {@code float} is positive infinity, the result contains all reals that are greater than or equal to
     *  {@code Float.MAX_VALUE}.</li>
     *  <li>If the {@code float} is negative infinity, the result contains all reals that are less than or equal to
     *  {@code -Float.MAX_VALUE}.</li>
     *  <li>If the {@code float} is {@code Float.MAX_VALUE}, the result contains all reals that are less than or equal
     *  to {@code Float.MAX_VALUE} and closer to it than to any other {@code float}.</li>
     *  <li>If the {@code float} is {@code -Float.MAX_VALUE}, the result contains all reals that are greater than or
     *  equal to {@code -Float.MAX_VALUE} and closer to it than to any other {@code float}.</li>
     * </ul>
     *
     * Positive and negative 0 yield the same result.
     *
     * <ul>
     *  <li>{@code f} may be any {@code float} except NaN.</li>
     *  <li>The result is one of
     *   <ul>
     *    <li>[{@code Float.MAX_VALUE}, ∞)</li>
     *    <li>(–∞, –{@code Float.MAX_VALUE}]</li>
     *    <li>[({@code a}+{@code b})/2, {@code b}], where {@code b} is {@code Float.MAX_VALUE} and {@code a} is
     *    {@code b}'s predecessor</li>
     *    <li>[{@code a}, ({@code a}+{@code b})/2], where {@code a} is –{@code Float.MAX_VALUE} and {@code b} is
     *    {@code a}'s successor</li>
     *    <li>[({@code a}+{@code b})/2, ({@code b}+{@code c})/2], where {@code a}, {@code b}, and {@code c} are
     *    equal to three consecutive finite {@code float}s (but + and / correspond to real operations, not
     *    {@code float} operations).</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param f a {@code float}.
     * @return the closure of the preimage of {@code f} with respect to rounding-to-nearest-{@code float}.
     */
    public static @NotNull Interval roundingPreimage(float f) {
        if (Float.isNaN(f))
            throw new ArithmeticException("cannot find preimage of NaN");
        if (Float.isInfinite(f)) {
            if (f > 0) {
                return new Interval(Rational.LARGEST_FLOAT, null);
            } else {
                return new Interval(null, Rational.LARGEST_FLOAT.negate());
            }
        }
        if (f == Float.MAX_VALUE) {
            //noinspection ConstantConditions
            return new Interval(
                    Rational.ofExact(FloatingPointUtils.predecessor(f)).add(Rational.LARGEST_FLOAT).shiftRight(1),
                    Rational.LARGEST_FLOAT
            );
        }
        if (f == -Float.MAX_VALUE) {
            //noinspection ConstantConditions
            return new Interval(
                    Rational.LARGEST_FLOAT.negate(),
                    Rational.ofExact(FloatingPointUtils.successor(f)).subtract(Rational.LARGEST_FLOAT).shiftRight(1)
            );
        }
        Rational r = Rational.ofExact(f);
        float predecessor = FloatingPointUtils.predecessor(f);
        @SuppressWarnings("ConstantConditions")
        Rational lower = predecessor == Float.NEGATIVE_INFINITY ?
                null :
                r.add(Rational.ofExact(predecessor)).shiftRight(1);
        float successor = FloatingPointUtils.successor(f);
        @SuppressWarnings("ConstantConditions")
        Rational upper = successor == Float.POSITIVE_INFINITY ?
                null :
                r.add(Rational.ofExact(successor)).shiftRight(1);
        return new Interval(lower, upper);
    }

    /**
     * Returns the smallest {@code Interval} containing all reals that are closer to a given {@link double} than to any
     * other {@code double}.
     *
     * Some special cases must be taken into account:
     * <ul>
     *  <li>If the {@code double} is positive infinity, the result contains all reals that are greater than or equal to
     *  {@code Double.MAX_VALUE}.</li>
     *  <li>If the {@code double} is negative infinity, the result contains all reals that are less than or equal to
     *  {@code -Double.MAX_VALUE}.</li>
     *  <li>If the {@code double} is {@code Double.MAX_VALUE}, the result contains all reals that are less than or
     *  equal to {@code Double.MAX_VALUE} and closer to it than to any other {@code double}.</li>
     *  <li>If the {@code double} is {@code -Double.MAX_VALUE}, the result contains all reals that are greater than or
     *  equal to {@code -Double.MAX_VALUE} and closer to it than to any other {@code double}.</li>
     * </ul>
     *
     * Positive and negative 0 yield the same result.
     *
     * <ul>
     *  <li>{@code d} may be any {@code double} except {@code NaN}.</li>
     *  <li>The result is one of
     *   <ul>
     *    <li>[{@code Double.MAX_VALUE}, ∞)</li>
     *    <li>(–∞, –{@code Double.MAX_VALUE}]</li>
     *    <li>[({@code a}+{@code b})/2, {@code b}], where {@code b} is {@code Double.MAX_VALUE} and {@code a} is
     *    {@code b}'s predecessor</li>
     *    <li>[{@code a}, ({@code a}+{@code b})/2], where {@code a} is –{@code Double.MAX_VALUE} and {@code b} is
     *    {@code a}'s successor</li>
     *    <li>[({@code a}+{@code b})/2, ({@code b}+{@code c})/2], where {@code a}, {@code b}, and {@code c} are equal
     *    to three consecutive finite {@code double}s (but + and / correspond to real operations, not {@code double}
     *    operations).</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param d a {@code double}.
     * @return the closure of the preimage of {@code d} with respect to rounding-to-nearest-{@code double}.
     */
    public static @NotNull Interval roundingPreimage(double d) {
        if (Double.isNaN(d))
            throw new ArithmeticException("cannot find preimage of NaN");
        if (Double.isInfinite(d)) {
            if (d > 0) {
                return new Interval(Rational.LARGEST_DOUBLE, null);
            } else {
                return new Interval(null, Rational.LARGEST_DOUBLE.negate());
            }
        }
        if (d == Double.MAX_VALUE) {
            //noinspection ConstantConditions
            return new Interval(
                    Rational.ofExact(FloatingPointUtils.predecessor(d)).add(Rational.LARGEST_DOUBLE).shiftRight(1),
                    Rational.LARGEST_DOUBLE
            );
        }
        if (d == -Double.MAX_VALUE) {
            //noinspection ConstantConditions
            return new Interval(
                    Rational.LARGEST_DOUBLE.negate(),
                    Rational.ofExact(FloatingPointUtils.successor(d)).subtract(Rational.LARGEST_DOUBLE).shiftRight(1)
            );
        }
        Rational r = Rational.ofExact(d);
        double predecessor = FloatingPointUtils.predecessor(d);
        @SuppressWarnings("ConstantConditions")
        Rational lower = predecessor == Double.NEGATIVE_INFINITY ?
                null :
                r.add(Rational.ofExact(predecessor)).shiftRight(1);
        double successor = FloatingPointUtils.successor(d);
        @SuppressWarnings("ConstantConditions")
        Rational upper = predecessor == Double.POSITIVE_INFINITY ?
                null :
                r.add(Rational.ofExact(successor)).shiftRight(1);
        return new Interval(lower, upper);
    }

    /**
     * Returns an {@code Interval} representing all real numbers that round to a specified {@link BigDecimal} (taking
     * scale into account).
     *
     * <ul>
     *  <li>{@code bd} cannot be null.</li>
     *  <li>The result is an interval of the form [a×10<sup>b</sup>–5×10<sup>c</sup>,
     *  a×10<sup>b</sup>+5×10<sup>c</sup>], where a, b, and c are integers and c{@literal <}b.</li>
     * </ul>
     *
     * @param bd a {@code BigDecimal}
     * @return the closure of the preimage of {@code bd} with respect to rounding-to-nearest-{@code BigDecimal}.
     */
    public static @NotNull Interval roundingPreimage(@NotNull BigDecimal bd) {
        Rational center = Rational.of(bd);
        Rational maxAbsoluteError = Rational.of(10).pow(-bd.scale()).shiftRight(1);
        return new Interval(center.subtract(maxAbsoluteError), center.add(maxAbsoluteError));
    }

    /**
     * Returns a pair of {@code float}s x, y such that [x, y] is the smallest interval with {@code float} bounds which
     * contains {@code this}. x or y may be infinite if {@code this}'s bounds are infinite or very large in magnitude.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>Neither of the result's elements are null or {@code NaN}. The second element is greater than or equal to
     *  the first. The first element cannot be {@code Infinity} or negative zero. The second element cannot be
     *  {@code -Infinity}. If the second element is negative zero, the first element cannot be positive zero.</li>
     * </ul>
     *
     * @return the smallest {@code float} interval containing {@code this}.
     */
    public @NotNull Pair<Float, Float> floatRange() {
        float fLower = lower == null ? Float.NEGATIVE_INFINITY : lower.floatValue(RoundingMode.FLOOR);
        float fUpper = upper == null ? Float.POSITIVE_INFINITY : upper.floatValue(RoundingMode.CEILING);
        return new Pair<>(fLower, fUpper);
    }

    /**
     * Returns a pair of {@code double}s x, y such that [x, y] is the smallest interval with {@code double} bounds
     * which contains {@code this}. x or y may be infinite if {@code this}'s bounds are infinite or very large in
     * magnitude.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>Neither of the result's elements are null or {@code NaN}. The second element is greater than or equal to
     *  the first. The first element cannot be {@code Infinity} or negative zero. The second element cannot be
     *  {@code -Infinity}. If the second element is negative zero, the first element cannot be positive zero.</li>
     * </ul>
     *
     * @return the smallest {@code double} interval containing {@code this}.
     */
    public @NotNull Pair<Double, Double> doubleRange() {
        double dLower = lower == null ? Double.NEGATIVE_INFINITY : lower.doubleValue(RoundingMode.FLOOR);
        double dUpper = upper == null ? Double.POSITIVE_INFINITY : upper.doubleValue(RoundingMode.CEILING);
        return new Pair<>(dLower, dUpper);
    }

    /**
     * Returns the smallest interval z such that if a∈{@code this} and b∈{@code that}, a+b∈z.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Interval} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull Interval add(@NotNull Interval that) {
        Rational sLower = lower == null || that.lower == null ? null : lower.add(that.lower);
        Rational sUpper = upper == null || that.upper == null ? null : upper.add(that.upper);
        return new Interval(sLower, sUpper);
    }

    /**
     * Returns the smallest interval a such that if x∈{@code this}, –x∈a.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return –{@code this}
     */
    public @NotNull Interval negate() {
        if (lower == null && upper == null) return this;
        if (lower == null) return new Interval(upper.negate(), null);
        if (upper == null) return new Interval(null, lower.negate());
        return new Interval(upper.negate(), lower.negate());
    }

    /**
     * Returns the smallest interval a such that if x∈{@code this}, |x|∈a.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result is an interval whose lower bound is finite and non-negative.</li>
     * </ul>
     *
     * @return |{@code this}|
     */
    public @NotNull Interval abs() {
        if (lower == null && upper == null) return this;
        if (lower == null) {
            return greaterThanOrEqualTo(upper.signum() == -1 ? upper.negate() : Rational.ZERO);
        }
        if (upper == null) {
            return lower.signum() == -1 ? greaterThanOrEqualTo(Rational.ZERO) : this;
        }
        if (lower.signum() == 1 && upper.signum() == 1) return this;
        if (lower.signum() == -1 && upper.signum() == -1) return negate();
        return of(Rational.ZERO, max(lower.negate(), upper));
    }

    /**
     * If every real in {@code this} has the same signum, returns that signum; otherwise, returns an empty
     * {@code Optional}.
     *
     * @return sgn(x) for all x∈{@code this}, if sgn(x) is unique
     */
    public @NotNull Optional<Integer> signum() {
        int lowerSignum = lower == null ? -1 : lower.signum();
        int upperSignum = upper == null ? 1 : upper.signum();
        return lowerSignum == upperSignum ? Optional.of(lowerSignum) : Optional.<Integer>empty();
    }

    /**
     * Returns the smallest interval z such that if a∈{@code this} and b∈{@code that}, a–b∈z.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the second {@code Interval} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull Interval subtract(@NotNull Interval that) {
        return add(that.negate());
    }

    /**
     * Returns the smallest interval z such that if a∈{@code this} and b∈{@code that}, a×b∈z.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the second {@code Interval} that {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Interval multiply(@NotNull Interval that) {
        if ((lower == Rational.ZERO && upper == Rational.ZERO)
                || ((that.lower == Rational.ZERO) && that.upper == Rational.ZERO)) {
            return ZERO;
        }
        int xls = lower == null ? 2 : lower.signum();
        int xus = upper == null ? 2 : upper.signum();
        int yls = that.lower == null ? 2 : that.lower.signum();
        int yus = that.upper == null ? 2 : that.upper.signum();
        boolean containsNegInf = (xls == 2 && yls == 2)
                || (xls == 2 && yus == 1) || (yls == 2 && xus == 1)
                || (xus == 2 && yls == -1) || (yus == 2 && xls == -1);
        boolean containsInf = (xus == 2 && yus == 2)
                || (xls == 2 && yls == -1) || (yls == 2 && xls == -1)
                || (xus == 2 && yus == 1) || (yus == 2 && xus == 1);
        if (containsNegInf && containsInf) return ALL;
        Rational xlyl = xls == 2 || yls == 2 ? null : lower.multiply(that.lower);
        Rational xlyu = xls == 2 || yus == 2 ? null : lower.multiply(that.upper);
        Rational xuyl = xus == 2 || yls == 2 ? null : upper.multiply(that.lower);
        Rational xuyu = xus == 2 || yus == 2 ? null : upper.multiply(that.upper);
        Rational min = xlyl;
        Rational max = xlyl;
        if (xlyu != null) {
            if (min == null || lt(xlyu, min)) min = xlyu;
            if (max == null || gt(xlyu, max)) max = xlyu;
        }
        if (xuyl != null) {
            if (min == null || lt(xuyl, min)) min = xuyl;
            if (max == null || gt(xuyl, max)) max = xuyl;
        }
        if (xuyu != null) {
            if (min == null || lt(xuyu, min)) min = xuyu;
            if (max == null || gt(xuyu, max)) max = xuyu;
        }
        if (containsNegInf) return new Interval(null, max);
        if (containsInf) return new Interval(min, null);
        return new Interval(min, max);
    }

    public @NotNull Interval invert() {
        if ((lower == null && upper == null)
                || (lower == null && upper.signum() == 1)
                || (upper == null && lower.signum() == -1)) return ALL;
        if (lower == null) return new Interval(upper.invert(), Rational.ZERO);
        if (upper == null) return new Interval(Rational.ZERO, lower.invert());
        if (lower.equals(Rational.ZERO) && upper.equals(Rational.ZERO))
            throw new ArithmeticException("division by zero");
        if (lower.equals(Rational.ZERO)) return new Interval(upper.invert(), null);
        if (upper.equals(Rational.ZERO)) return new Interval(null, lower.invert());
        if (lower.signum() != upper.signum()) return ALL;
        return new Interval(upper.invert(), lower.invert());
    }

    public @NotNull Interval divide(@NotNull Interval that) {
        return multiply(that.invert());
    }

    public @NotNull Interval shiftLeft(int bits) {
        return new Interval(
                lower == null ? null : lower.shiftLeft(bits),
                upper == null ? null : upper.shiftLeft(bits)
        );
    }

    public @NotNull Interval shiftRight(int bits) {
        return new Interval(
                lower == null ? null : lower.shiftRight(bits),
                upper == null ? null : upper.shiftRight(bits)
        );
    }

    public @NotNull Interval pow(int p) {
        if (p == 0) return new Interval(Rational.ONE, Rational.ONE);
        if (p == 1) return this;
        if (p < 0) return pow(-p).invert();
        if (p % 2 == 0) {
            int ls = lower == null ? -1 : lower.signum();
            int us = upper == null ? 1 : upper.signum();
            if (ls != -1 && us != -1) {
                return new Interval(lower.pow(p), upper == null ? null : upper.pow(p));
            }
            if (ls != 1 && us != 1) {
                return new Interval(upper.pow(p), lower == null ? null : lower.pow(p));
            }
            Rational a = lower == null ? null : lower.pow(p);
            Rational b = upper == null ? null : upper.pow(p);
            Rational max = a == null || b == null ? null : max(a, b);
            return new Interval(Rational.ZERO, max);
        } else {
            return new Interval(lower == null ? null : lower.pow(p), upper == null ? null : upper.pow(p));
        }
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} may be any {@code Object}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param that The {@code Interval} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Interval interval = (Interval) that;
        return (lower == null ? interval.lower == null : lower.equals(interval.lower)) &&
               (upper == null ? interval.upper == null : upper.equals(interval.upper));
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>@{code this} may be any {@code Interval}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        int result = lower != null ? lower.hashCode() : 0;
        result = 31 * result + (upper != null ? upper.hashCode() : 0);
        return result;
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. {@code Interval}s are ordered on their lower bound, then on their upper bound; –∞ and
     * ∞ behave as expected.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that The {@code Interval} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull Interval that) {
        if (this == that) return 0;
        if (lower == null && that.lower != null) return -1;
        if (lower != null && that.lower == null) return 1;
        Ordering lowerOrdering = lower == null ? EQ : compare(lower, that.lower);
        if (lowerOrdering != EQ) return lowerOrdering.toInt();
        if (upper == null && that.upper != null) return 1;
        if (upper != null && that.upper == null) return -1;
        return (upper == null ? EQ : compare(upper, that.upper)).toInt();
    }

    /**
     * Creates an {@code Interval} from a {@code String}. Valid strings are in one of these four forms:
     * {@code "(-Infinity, Infinity)"}, {@code "(-Infinity, " + q + "]"},
     * {@code "[" + p + ", Infinity)"}, or {@code "[" + p + ", " + q + "]"}, where {@code p} and {@code q} are valid
     * inputs to {@link mho.qbar.objects.Rational#read}, and {@code p}≤{@code q}
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<Interval>}, or null.</li>
     * </ul>
     *
     * @param s a string representation of a {@code Rational}.
     * @return the wrapped {@code Rational} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<Interval> read(@NotNull String s) {
        if (s.isEmpty()) return Optional.empty();
        char left = s.charAt(0);
        if (left != '[' && left != '(') return Optional.empty();
        char right = s.charAt(s.length() - 1);
        if (right != ']' && right != ')') return Optional.empty();
        int commaIndex = s.indexOf(", ");
        if (commaIndex == -1) return Optional.empty();
        String leftString = s.substring(1, commaIndex);
        String rightString = s.substring(commaIndex + 2, s.length() - 1);
        Rational lower = null;
        if (left == '(') {
            if (!leftString.equals("-Infinity")) return Optional.empty();
        } else {
            Optional<Rational> optLower = Rational.read(leftString);
            if (!optLower.isPresent()) return Optional.empty();
            lower = optLower.get();
        }
        Rational upper = null;
        if (right == ')') {
            if (!rightString.equals("Infinity")) return Optional.empty();
        } else {
            Optional<Rational> optUpper = Rational.read(rightString);
            if (!optUpper.isPresent()) return Optional.empty();
            upper = optUpper.get();
        }
        if (lower == null && upper == null) return Optional.of(ALL);
        if (lower == null) return Optional.of(lessThanOrEqualTo(upper));
        if (upper == null) return Optional.of(greaterThanOrEqualTo(lower));
        return le(lower, upper) ? Optional.of(of(lower, upper)) : Optional.<Interval>empty();
    }

    /**
     * Finds the first occurrence of an {@code Interval} in a {@code String}. Returns the {@code Interval} and the
     * index at which it was found. Returns an empty {@code Optional} if no {@code Interval} is found. Only
     * {@code String}s which could have been emitted by {@link Interval#toString} are recognized. The longest possible
     * {@code Interval} is parsed.
     *
     * <ul>
     *  <li>{@code s} must be non-null.</li>
     *  <li>The result is non-null. If it is non-empty, then neither of the {@code Pair}'s components is null, and the
     *  second component is non-negative.</li>
     * </ul>
     *
     * @param s the input {@code String}
     * @return the first {@code Interval} found in {@code s}, and the index at which it was found
     */
    public static @NotNull Optional<Pair<Interval, Integer>> findIn(@NotNull String s) {
        return Readers.genericFindIn(Interval::read, " (),-/0123456789I[]finty", s);
    }

    /**
     * Creates a string representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result is a string in on of four forms: {@code "(-Infinity, Infinity)"},
     *  {@code "(-Infinity, " + q.toString() + "]"}, {@code "[" + p.toString() + ", Infinity)"}, or
     *  {@code "[" + p.toString() + ", " + q.toString() + "]"}, where {@code p} and {@code q} are {@code Rational}s
     *  such that {@code p} is less than or equal to {@code q}.</li>
     * </ul>
     *
     * @return a string representation of {@code this}.
     */
    public @NotNull String toString() {
        return (lower == null ? "(-Infinity" : "[" + lower) + ", " + (upper == null ? "Infinity)" : upper + "]");
    }
}
