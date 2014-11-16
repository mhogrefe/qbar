package mho.qbar.objects;

import mho.wheels.numbers.Numbers;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

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
    public final @Nullable Rational lower;

    /**
     * The upper bound of this interval if the upper bound is finite, or null if the upper bound is ∞
     */
    public final @Nullable Rational upper;

    /**
     * Private constructor from {@code Rational}s; assumes arguments are valid. If lower is null, the
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
     * Determines whether {@code this} contains {@code x}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param x the test {@code Rational}
     * @return {@code x}&#x2208;{@code this}
     */
    public boolean contains(@NotNull Rational x) {
        if (lower == null && upper == null) return true;
        if (lower == null) return le(x, upper);
        if (upper == null) return ge(x, lower);
        return ge(x, lower) && le(x, upper);
    }

    /**
     * Determines the diameter (length) of {@code this}, or null if {@code this} has infinite diameter.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result may be any non-negative {@code Rational}, or null.</li>
     * </ul>
     *
     * @return &#x03bc;({@code this})
     */
    public @Nullable Rational diameter() {
        if (lower == null || upper == null) return null;
        return Rational.subtract(upper, lower);
    }

    /**
     * Determines the convex hull of two {@code Interval}s, or the {@code Interval} with minimal diameter that is a
     * superset of the two {@code Interval}s.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the first {@code Interval}
     * @param b the second {@code Interval}
     * @return Conv({@code a}, {@code b})
     */
    public static @NotNull Interval convexHull(@NotNull Interval a, @NotNull Interval b) {
        return new Interval(
                a.lower == null || b.lower == null ? null : min(a.lower, b.lower),
                a.upper == null || b.upper == null ? null : max(a.upper, b.upper)
        );
    }

    /**
     * Determines the convex hull of a set of {@code Interval}s, or the {@code Interval} with minimal diameter that is
     * a superset of all of the {@code Interval}s.
     *
     * <ul>
     *  <li>{@code as} cannot be null or empty and may not contain any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param as the {@code Interval}s.
     * @return Conv({@code as})
     */
    public static @NotNull Interval convexHull(@NotNull SortedSet<Interval> as) {
        if (as.isEmpty())
            throw new IllegalArgumentException("cannot take convex hull of empty set");
        Rational lower = as.first().lower;
        Rational upper = null;
        for (Interval a : as) {
            if (a.upper == null) return new Interval(lower, null);
            if (upper == null || gt(a.upper, upper)) {
                upper = a.upper;
            }
        }
        return new Interval(lower, upper);
    }

    /**
     * Returns the intersection of two {@code Interval}s, or null if the intersection is empty.
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the first {@code Interval}
     * @param b the second {@code Interval}
     * @return {@code a}∩{@code b}
     */
    public static @Nullable Interval intersection(@NotNull Interval a, @NotNull Interval b) {
        Rational lower;
        if (a.lower == null && b.lower == null) {
            lower = null;
        } else if (a.lower == null) {
            lower = b.lower;
        } else if (b.lower == null) {
            lower = a.lower;
        } else {
            lower = max(a.lower, b.lower);
        }
        Rational upper;
        if (a.upper == null && b.upper == null) {
            upper = null;
        } else if (a.upper == null) {
            upper = b.upper;
        } else if (b.upper == null) {
            upper = a.upper;
        } else {
            upper = min(a.upper, b.upper);
        }
        if (lower != null && upper != null && gt(lower, upper)) return null;
        return new Interval(lower, upper);
    }

    /**
     * Determines whether two {@code Interval}s are disjoint (whether their intersections are empty).
     *
     * <ul>
     *  <li>{@code a} cannot be null.</li>
     *  <li>{@code b} cannot be null.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a the first {@code Interval}
     * @param b the second {@code Interval}
     * @return {@code a}∩{@code b}=∅
     */
    public static boolean disjoint(@NotNull Interval a, @NotNull Interval b) {
        return intersection(a, b) == null;
    }

    /**
     * Transforms a set of {@code Interval}s into a set of disjoint {@code Interval}s with the same union as the
     * original set.
     *
     * <ul>
     *  <li>{@code as} cannot be null and may not contain any null elements.</li>
     *  <li>The result is a sorted set of pairwise disjoint {@code Interval}s.</li>
     * </ul>
     *
     * @param as a set of {@code Interval}s
     * @return a set of disjoint {@code Interval}s whose union is the same as the union of {@code as}
     */
    public static SortedSet<Interval> makeDisjoint(SortedSet<Interval> as) {
        SortedSet<Interval> simplified = new TreeSet<>();
        Interval acc = null;
        for (Interval a : as) {
            if (acc == null) {
                acc = a;
            } else if (disjoint(acc, a)) {
                simplified.add(acc);
                acc = a;
            } else {
                acc = convexHull(acc, a);
            }
        }
        if (acc != null) simplified.add(acc);
        return simplified;
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
        return Rational.add(lower, upper).shiftRight(1);
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
    public @NotNull
    Pair<Interval, Interval> split(@NotNull Rational x) {
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
     * any other {@code float}; or, if the {@code float} is positive infinity, all reals that are greater than
     * {@code Float.MAX_VALUE}; or, if the {@code float} is negative infinity, all reals that are less than
     * {@code –Float.MAX_VALUE}. Positive and negative 0 yield the same result.
     *
     * <ul>
     *  <li>{@code f} may be any {@code float} except NaN.</li>
     *  <li>The result is one of
     *   <ul>
     *    <li>[{@code Float.MAX_VALUE}, ∞)</li>
     *    <li>(–∞, –{@code Float.MAX_VALUE}]</li>
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
        Rational r = Rational.of(f);
        float predecessor = Numbers.predecessor(f);
        Rational lower = predecessor == Float.NEGATIVE_INFINITY ?
                null :
                Rational.add(r, Rational.of(predecessor)).shiftRight(1);
        float successor = Numbers.successor(f);
        Rational upper = successor == Float.POSITIVE_INFINITY ?
                null :
                Rational.add(r, Rational.of(successor)).shiftRight(1);
        return new Interval(lower, upper);
    }

    /**
     * Returns the smallest {@code Interval} containing all reals that are closer to a given {@link double} than to any
     * other {@code double}; or, if the {@code double} is positive infinity, all reals that are greater than
     * {@code Double.MAX_VALUE}; or, if the {@code double} is negative infinity, all reals that are less than
     * {@code –Double.MAX_VALUE}. Positive and negative 0 yield the same result.
     *
     * <ul>
     *  <li>{@code d} may be any {@code double} except {@code NaN}.</li>
     *  <li>The result is one of
     *   <ul>
     *    <li>[{@code Double.MAX_VALUE}, ∞)</li>
     *    <li>(–∞, –{@code Double.MAX_VALUE}]</li>
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
        Rational r = Rational.of(d);
        double predecessor = Numbers.predecessor(d);
        Rational lower = predecessor == Double.NEGATIVE_INFINITY ?
                null :
                Rational.add(r, Rational.of(predecessor)).shiftRight(1);
        double successor = Numbers.successor(d);
        Rational upper = predecessor == Double.POSITIVE_INFINITY ?
                null :
                Rational.add(r, Rational.of(successor)).shiftRight(1);
        return new Interval(lower, upper);
    }

    //todo finish fixing JavaDoc
    /**
     * Returns an {@code Interval} representing all real numbers that round to a specified {@link BigDecimal} (taking
     * precision into account).
     *
     * <ul>
     *  <li>{@code bd} cannot be null.</li>
     *  <li>The result is an interval of the form [a&#x00D7;10<sup>b</sup>–5&#x00D7;10<sup>c</sup>,
     *  a&#x00D7;10<sup>b</sup>+5&#x00D7;10<sup>c</sup>], where a, b, and c are integers and c&lt;b.</li>
     * </ul>
     *
     * @param bd a {@code BigDecimal}
     * @return the closure of the preimage of {@code bd} with respect to rounding-to-nearest-{@code BigDecimal}.
     */
    public static @NotNull Interval roundingPreimage(@NotNull BigDecimal bd) {
        Rational center = Rational.of(bd);
        Rational maxAbsoluteError = Rational.of(10).pow(-bd.scale()).shiftRight(1);
        return new Interval(Rational.subtract(center, maxAbsoluteError), Rational.add(center, maxAbsoluteError));
    }

    /**
     * Returns a pair of {@code float}s x, y such that [x, y] is the smallest interval with {@code float} bounds
     * which contains {@code this}. x or y may be infinite if {@code this}'s bounds are infinite or very large in
     * magnitude.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result cannot be null, and neither can either of its elements be null.</li>
     * </ul>
     *
     * @return the smallest {@code float} interval containing {@code this}.
     */
    public @NotNull Pair<Float, Float> floatRange() {
        float fLower = lower == null ? Float.NEGATIVE_INFINITY : lower.toFloat(RoundingMode.FLOOR);
        float fUpper = upper == null ? Float.POSITIVE_INFINITY : upper.toFloat(RoundingMode.CEILING);
        return new Pair<>(fLower, fUpper);
    }

    /**
     * Returns a pair of {@code double}s x, y such that [x, y] is the smallest interval with {@code double} bounds
     * which contains {@code this}. x or y may be infinite if {@code this}'s bounds are infinite or very large in
     * magnitude.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result cannot be null, and neither can either of its elements be null.</li>
     * </ul>
     *
     * @return the smallest {@code double} interval containing {@code this}.
     */
    public @NotNull Pair<Double, Double> doubleRange() {
        double dLower = lower == null ? Double.NEGATIVE_INFINITY : lower.toDouble(RoundingMode.FLOOR);
        double dUpper = upper == null ? Double.POSITIVE_INFINITY : upper.toDouble(RoundingMode.CEILING);
        return new Pair<>(dLower, dUpper);
    }

    /**
     * Returns a pair of {@code BigDecimal}s x, y such that [x, y] is the smallest interval with {@code BigDecimal}
     * bounds (where the {@code BigDecimal}s have a number of significant figures at least as large as
     * {@code precision}) which contains {@code this}.
     *
     * <ul>
     *  <li>{@code this} must be a finitely-bounded {@code Interval}.</li>
     *  <li>{@code precision} must be non-negative.</li>
     *  <li>If {@code precision} is 0, both bounds of {@code this} must have a terminating decimal expansion.</li>
     *  <li>The result is either a pair in which at least one {@code BigDecimal} x has minimal scale (that is, the
     *  scale is the smallest non-negative n such that x&#x00D7;10<sup>n</sup> is an integer), or a pair in which each
     *  {@code BigDecimal} has the same number of significant figures.</li>
     * </ul>
     *
     * @param precision the maximum number of significant digits in the elements of the result; or 0, if both elements
     *                  must have full precision
     * @return the smallest {@code BigDecimal} interval containing {@code this}, such that the precision of the
     * {@code BigDecimal}s is at least {@code precision}.
     */
    public @NotNull Pair<BigDecimal, BigDecimal> bigDecimalRange(int precision) {
        if (lower == null || upper == null)
            throw new ArithmeticException("cannot represent infinities with BigDecimal");
        BigDecimal bdLower = lower.toBigDecimal(precision, RoundingMode.FLOOR);
        BigDecimal bdUpper = upper.toBigDecimal(precision, RoundingMode.CEILING);
        return new Pair<>(bdLower, bdUpper);
    }

    /**
     * Returns the smallest interval a such that if x&#x2208;{@code this}, –x&#x2208;a.
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
     * Returns the smallest interval z such that if a&#x2208;{@code x} and b&#x2208;{@code y}, a+b&#x2208;z.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     *  <li>{@code y} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the first {@code Interval}
     * @param y the second {@code Interval}
     * @return {@code x}+{@code y}
     */
    public static @NotNull Interval add(@NotNull Interval x, @NotNull Interval y) {
        Rational sLower = x.lower == null || y.lower == null ? null : Rational.add(x.lower, y.lower);
        Rational sUpper = x.upper == null || y.upper == null ? null : Rational.add(x.upper, y.upper);
        return new Interval(sLower, sUpper);
    }

    /**
     * Returns the smallest interval z such that if a&#x2208;{@code x} and b&#x2208;{@code y}, a–b&#x2208;z.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     *  <li>{@code y} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the first {@code Interval}
     * @param y the second {@code Interval}
     * @return {@code x}–{@code y}
     */
    public static @NotNull Interval subtract(@NotNull Interval x, @NotNull Interval y) {
        return add(x, y.negate());
    }

    /**
     * Returns the smallest interval z such that if a&#x2208;{@code x} and b&#x2208;{@code y}, a&#x00D7;b&#x2208;z.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     *  <li>{@code y} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the first {@code Interval}
     * @param y the second {@code Interval}
     * @return {@code x}&#x00D7;{@code y}
     */
    public static @NotNull Interval multiply(@NotNull Interval x, @NotNull Interval y) {
        if ((x.lower == Rational.ZERO && x.upper == Rational.ZERO)
                || ((y.lower == Rational.ZERO) && y.upper == Rational.ZERO)) {
            return ZERO;
        }
        int xls = x.lower == null ? 2 : x.lower.signum();
        int xus = x.upper == null ? 2 : x.upper.signum();
        int yls = y.lower == null ? 2 : y.lower.signum();
        int yus = y.upper == null ? 2 : y.upper.signum();
        boolean containsNegInf = (xls == 2 && yls == 2)
                || (xls == 2 && yus == 1) || (yls == 2 && xus == 1)
                || (xus == 2 && yls == -1) || (yus == 2 && xls == -1);
        boolean containsInf = (xus == 2 && yus == 2)
                || (xls == 2 && yls == -1) || (yls == 2 && xls == -1)
                || (xus == 2 && yus == 1) || (yus == 2 && xus == 1);
        if (containsNegInf && containsInf) return ALL;
        Rational xlyl = xls == 2 || yls == 2 ? null : Rational.multiply(x.lower, y.lower);
        Rational xlyu = xls == 2 || yus == 2 ? null : Rational.multiply(x.lower, y.upper);
        Rational xuyl = xus == 2 || yls == 2 ? null : Rational.multiply(x.upper, y.lower);
        Rational xuyu = xus == 2 || yus == 2 ? null : Rational.multiply(x.upper, y.upper);
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

    public Interval invert() {
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

    public static Interval divide(Interval x, Interval y) {
        return multiply(x, y.invert());
    }

    public Interval pow(int p) {
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
        if (lower == null && that.lower != null) return -1;
        if (lower != null && that.lower == null) return 1;
        int lowerCompare = lower == null ? 0 : lower.compareTo(that.lower);
        if (lowerCompare != 0) return lowerCompare;
        if (upper == null && that.upper != null) return 1;
        if (upper != null && that.upper == null) return -1;
        return upper == null ? 0 : upper.compareTo(that.upper);
    }

    /**
     * Creates an {@code Interval} from a {@code String}. Valid strings are in one of these four forms:
     * {@code "(-Infinity, Infinity)"}, {@code "(-Infinity, " + q.toString() + "]"},
     * {@code "[" + p.toString() + ", Infinity)"}, or {@code "[" + p.toString() + ", " + q.toString() + "]"}, where
     * {@code p} and {@code q} are {@code Rational}s.
     *
     * <ul>
     *  <li>{@code s} cannot be null and cannot be of the form {@code "[" + p.toString() + ", " + q.toString() + "]"},
     *  where {@code p} and {@code q} are {@code Rational}s such that {@code a} is greater than {@code b}.</li>
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
        return Optional.of(of(lower, upper));
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

//    public Generator<Rational> rationals() {
//        if (lower == null && upper == null) {
//            return Rational.rationals();
//        }
//        if (lower == null) {
//            return Rational.nonnegativeRationals().map(
//                    r -> Rational.subtract(upper, r),
//                    r -> Rational.subtract(upper, r),
//                    this::contains
//            );
//        }
//        if (upper == null) {
//            return Rational.nonnegativeRationals().map(
//                    r -> Rational.add(r, upper),
//                    r -> Rational.subtract(r, upper),
//                    this::contains
//            );
//        }
//        if (lower.equals(upper)) {
//            return Generator.single(lower);
//        }
//        return Generator.cat(Generator.single(upper), Rational.nonnegativeRationalsLessThanOne().map(
//                r -> Rational.add(Rational.multiply(r, diameter()), lower),
//                r -> Rational.divide(Rational.subtract(r, lower), diameter()),
//                this::contains
//        ));
//    }
}
