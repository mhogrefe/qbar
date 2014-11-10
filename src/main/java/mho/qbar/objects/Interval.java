package mho.qbar.objects;

import mho.haskellesque.numbers.Numbers;
import mho.haskellesque.ordering.Ordering;
import mho.haskellesque.structures.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import static mho.haskellesque.ordering.Ordering.*;

/**
 * <p>The <tt>Interval</tt> class represents an interval of real numbers. If we let p and q be rationals, p&#x2264;q,
 * the representable intervals are (&#x2212;&#x221e;, &#x221e;), (&#x2212;&#x221e;, q], [p, &#x221e;), and [p, q]. If
 * p = q, the <tt>Interval</tt> represents a single number. The empty interval cannot be represented.
 *
 * <p>In general, the f(I), where I is an <tt>Interval</tt>, is taken to mean the image of I under f. Often this image
 * is not an <tt>Interval</tt> itself, in which case the function might return a set of <tt>Interval</tt>s, whose
 * closed union is the image, or it may just return the closure of the image's convex hull. Similar considerations
 * apply for functions of two <tt>Interval</tt>s, etc.
 */
public final class Interval implements Comparable<Interval> {
    /**
     * [0, 0]
     */
    public static final @NotNull
    Interval ZERO = new Interval(Rational.ZERO, Rational.ZERO);
    /**
     * [1, 1]
     */
    public static final @NotNull Interval ONE = new Interval(Rational.ONE, Rational.ONE);
    /**
     * (&#x2212;&#x221e;, &#x221e;)
     */
    public static final @NotNull Interval ALL = new Interval(null, null);

    /**
     * The lower bound of this interval if the lower bound is finite, or null if the lower bound is &#x2212;&#x221e;
     */
    public final @Nullable
    Rational lower;
    /**
     * The upper bound of this interval if the upper bound is finite, or null if the upper bound is &#x221e;
     */
    public final @Nullable Rational upper;

    /**
     * Private constructor from <tt>Rational</tt>s; assumes arguments are valid. If lower is null, the
     * <tt>Interval</tt>'s lower bound is &#x2212;&#x221e;; if upper is null, the <tt>Interval</tt>'s upper bound is
     * &#x221e;.
     *
     * <ul>
     *  <li><tt>lower</tt> may be any <tt>Rational</tt>, or null.</li>
     *  <li><tt>upper</tt> may be any <tt>Rational</tt>, or null.</li>
     *  <li>If <tt>lower</tt> and <tt>upper</tt> are both non-null, <tt>lower</tt> must be less than or equal to
     *  <tt>upper</tt>.</li>
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
     * Creates a finitely-bounded <tt>Interval</tt> from <tt>Rationals</tt>.
     *
     * <ul>
     *  <li><tt>lower</tt> cannot be null.</li>
     *  <li><tt>upper</tt> cannot be null.</li>
     *  <li><tt>lower</tt> must be less than or equal to <tt>upper</tt>.</li>
     * </ul>
     *
     * @param lower the lower bound
     * @param upper the upper bound
     * @return [<tt>lower</tt>, <tt>upper</tt>]
     */
    public static @NotNull Interval of(@NotNull Rational lower, @NotNull Rational upper) {
        if (lower.compareTo(upper) > 0)
            throw new IllegalArgumentException("lower bound cannot be greater than upper bound");
        return new Interval(lower, upper);
    }

    /**
     * Creates an interval whose lower bound is &#x2212;&#x221e;.
     *
     * <ul>
     *  <li><tt>upper</tt> cannot be null.</li>
     * </ul>
     *
     * @param upper the upper bound
     * @return (&#x2212;&#x221e;, <tt>upper</tt>]
     */
    public static @NotNull Interval lessThanOrEqualTo(@NotNull Rational upper) {
        return new Interval(null, upper);
    }

    /**
     * Creates an interval whose upper bound is &#x221e;.
     *
     * <ul>
     *  <li><tt>lower</tt> cannot be null.</li>
     * </ul>
     *
     * @param lower the lower bound
     * @return [<tt>lower</tt>, &#x221e;)
     */
    public static @NotNull Interval greaterThanOrEqualTo(@NotNull Rational lower) {
        return new Interval(lower, null);
    }

    /**
     * Creates an interval containing exactly one <tt>Rational</tt>.
     *
     * <ul>
     *  <li><tt>x</tt> cannot be null.</li>
     * </ul>
     *
     * @param x the value contained in this <tt>Interval</tt>
     * @return [<tt>x</tt>, <tt>x</tt>]
     */
    public static @NotNull Interval of(@NotNull Rational x) {
        return new Interval(x, x);
    }

    /**
     * Determines whether <tt>this</tt> has finite bounds.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Interval</tt>.</li>
     *  <li>The result may be either <tt>boolean</tt>.</li>
     * </ul>
     *
     * @return whether <tt>this</tt> is finitely-bounded
     */
    public boolean isFinitelyBounded() {
        return lower != null && upper != null;
    }

    /**
     * Determines whether <tt>this</tt> contains <tt>x</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Interval</tt>.</li>
     *  <li><tt>x</tt> cannot be null.</li>
     *  <li>The result may be either <tt>boolean</tt>.</li>
     * </ul>
     *
     * @param x the test <tt>Rational</tt>
     * @return <tt>x</tt>&#x2208;<tt>this</tt>
     */
    public boolean contains(@NotNull Rational x) {
        if (lower == null && upper == null) return true;
        if (lower == null) return x.compareTo(upper) <= 0;
        if (upper == null) return x.compareTo(lower) >= 0;
        return x.compareTo(lower) >= 0 && x.compareTo(upper) <= 0;
    }

    /**
     * Determines the diameter (length) of <tt>this</tt>, or null if <tt>this</tt> has infinite diameter.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Interval</tt>.</li>
     *  <li>The result may be any non-negative <tt>Rational</tt>, or null.</li>
     * </ul>
     *
     * @return &#x03bc;(<tt>this</tt>)
     */
    public @Nullable Rational diameter() {
        if (lower == null || upper == null) return null;
        return Rational.subtract(upper, lower);
    }

    /**
     * Determines the convex hull of two intervals, or the interval with minimal diameter that is a superset of the two
     * intervals.
     *
     * <ul>
     *  <li><tt>a</tt> cannot be null.</li>
     *  <li><tt>b</tt> cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the first <tt>Interval</tt>
     * @param b the second <tt>Interval</tt>
     * @return Conv(<tt>a</tt>, <tt>b</tt>)
     */
    public static @NotNull Interval convexHull(@NotNull Interval a, @NotNull Interval b) {
        return new Interval(
                a.lower == null || b.lower == null ? null : min(a.lower, b.lower),
                a.upper == null || b.upper == null ? null : max(a.upper, b.upper)
        );
    }

    /**
     * Determines the convex hull of a set of intervals, or the interval with minimal diameter that is a superset of
     * all of the intervals.
     *
     * <ul>
     *  <li><tt>as</tt> cannot be null or empty and may not contain any null elements.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param as the <tt>Interval</tt>s.
     * @return Conv(<tt>as</tt>)
     */
    public static @NotNull Interval convexHull(@NotNull SortedSet<Interval> as) {
        if (as.isEmpty())
            throw new IllegalArgumentException("cannot take convex hull of empty set");
        Rational lower = as.first().lower;
        Rational upper = null;
        for (Interval a : as) {
            if (a.upper == null) return new Interval(lower, null);
            if (upper == null || a.upper.compareTo(upper) > 0) {
                upper = a.upper;
            }
        }
        return new Interval(lower, upper);
    }

    /**
     * Returns the intersection of two <tt>Interval</tt>s, or null if the intersection is empty.
     *
     * <ul>
     *  <li><tt>a</tt> cannot be null.</li>
     *  <li><tt>b</tt> cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param a the first <tt>Interval</tt>
     * @param b the second <tt>Interval</tt>
     * @return <tt>a</tt>&#x2229;<tt>b</tt>
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
        if (lower != null && upper != null && lower.compareTo(upper) > 0) return null;
        return new Interval(lower, upper);
    }

    /**
     * Determines whether two intervals are disjoint (whether their intersections are empty).
     *
     * <ul>
     *  <li><tt>a</tt> cannot be null.</li>
     *  <li><tt>b</tt> cannot be null.</li>
     *  <li>The result may be either <tt>boolean</tt>.</li>
     * </ul>
     *
     * @param a the first <tt>Interval</tt>
     * @param b the second <tt>Interval</tt>
     * @return <tt>a</tt>&#x2229;<tt>b</tt>=&#x2205;
     */
    public static boolean disjoint(@NotNull Interval a, @NotNull Interval b) {
        return intersection(a, b) == null;
    }

    /**
     * Transforms a set of intervals into a set of disjoint intervals with the same union as the original set.
     *
     * <ul>
     *  <li><tt>as</tt> cannot be null and may not contain any null elements.</li>
     *  <li>The result is a sorted set of pairwise disjoint intervals.</li>
     * </ul>
     *
     * @param as a set of intervals
     * @return a set of disjoint intervals whose union is the same as the union of <tt>as</tt>
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
     * Returns the midpoint of <tt>this</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> must be finitely bounded.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return the average of <tt>lower</tt> and <tt>upper</tt>.
     */
    public @NotNull Rational midpoint() {
        if (lower == null || upper == null)
            throw new ArithmeticException("an unbounded interval has no midpoint");
        return Rational.add(lower, upper).shiftRight(1);
    }

    /**
     * Splits <tt>this</tt> into two intervals at <tt>x</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Interval</tt>.</li>
     *  <li><tt>x</tt> cannot be null.</li>
     *  <li><tt>this</tt> must contain <tt>x</tt>.</li>
     *  <li>The result is a pair of <tt>Interval</tt>s, neither null, such that the upper bound of the first is equal
     *  to the lower bound of the second.</li>
     * </ul>
     *
     * @param x the point at which <tt>this</tt> is split.
     * @return the two pieces of <tt>this</tt>.
     */
    public @NotNull
    Pair<Interval, Interval> split(@NotNull Rational x) {
        if (!contains(x))
            throw new ArithmeticException("interval does not contain specified split point");
        return new Pair<>(new Interval(lower, x), new Interval(x, upper));
    }

    /**
     * Splits <tt>this</tt> into two equal <tt>Interval</tt>s.
     *
     * <ul>
     *  <li><tt>this</tt> must be finitely bounded.</li>
     *  <li>The result is a pair of equal-diameter, finitely-bounded <tt>Interval</tt>s such the the upper bound of the
     *  first is equal to the lower bound of the second.</li>
     * </ul>
     *
     * @return the two halves of <tt>this</tt>.
     */
    public @NotNull Pair<Interval, Interval> bisect() {
        return split(midpoint());
    }

    /**
     * Returns the smallest <tt>Interval</tt> containing all reals that are closer to a given <tt>float</tt> than to
     * any other <tt>float</tt>; or, if the <tt>float</tt> is positive infinity, all reals that are greater than
     * <tt>Float.MAX_VALUE</tt>; or, if the <tt>float</tt> is negative infinity, all reals that are less than
     * <tt>&#x2212;Float.MAX_VALUE</tt>. Positive and negative 0 yield the same result.
     *
     * <ul>
     *  <li><tt>f</tt> may be any <tt>float</tt> except NaN.</li>
     *  <li>The result is one of
     *   <ul>
     *    <li>[<tt>Float.MAX_VALUE</tt>, &#x221e;)</li>
     *    <li>(&#x2212;&#x221e;, &#x2212;<tt>Float.MAX_VALUE</tt>]</li>
     *    <li>[(<tt>a</tt>+<tt>b</tt>)/2, (<tt>b</tt>+<tt>c</tt>)/2], where <tt>a</tt>, <tt>b</tt>, and <tt>c</tt> are
     *    equal to three consecutive finite <tt>float</tt>s (but + and / correspond to real operations, not
     *    <tt>float</tt> operations).</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param f a <tt>float</tt>.
     * @return the closure of the preimage of <tt>f</tt> with respect to rounding-to-nearest-<tt>float</tt>.
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
     * Returns the smallest <tt>Interval</tt> containing all reals that are closer to a given <tt>double</tt> than to
     * any other <tt>double</tt>; or, if the <tt>double</tt> is positive infinity, all reals that are greater than
     * <tt>Double.MAX_VALUE</tt>; or, if the <tt>double</tt> is negative infinity, all reals that are less than
     * <tt>&#x2212;Double.MAX_VALUE</tt>. Positive and negative 0 yield the same result.
     *
     * <ul>
     *  <li><tt>d</tt> may be any <tt>double</tt> except NaN.</li>
     *  <li>The result is one of
     *   <ul>
     *    <li>[<tt>Double.MAX_VALUE</tt>, &#x221e;)</li>
     *    <li>(&#x2212;&#x221e;, &#x2212;<tt>Double.MAX_VALUE</tt>]</li>
     *    <li>[(<tt>a</tt>+<tt>b</tt>)/2, (<tt>b</tt>+<tt>c</tt>)/2], where <tt>a</tt>, <tt>b</tt>, and <tt>c</tt> are
     *    equal to three consecutive finite <tt>double</tt>s (but + and / correspond to real operations, not
     *    <tt>double</tt> operations).</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param d a <tt>double</tt>.
     * @return the closure of the preimage of <tt>d</tt> with respect to rounding-to-nearest-<tt>double</tt>.
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

    /**
     * Returns an <tt>Interval</tt> representing all real numbers that round to a specified <tt>BigDecimal</tt> (taking
     * precision into account).
     *
     * <ul>
     *  <li><tt>bd</tt> cannot be null.</li>
     *  <li>The result is an interval of the form [a&#x00D7;10<sup>b</sup>&#x2212;5&#x00D7;10<sup>c</sup>,
     *  a&#x00D7;10<sup>b</sup>+5&#x00D7;10<sup>c</sup>], where a, b, and c are integers and c&lt;b.</li>
     * </ul>
     *
     * @param bd a <tt>BigDecimal</tt>
     * @return the closure of the preimage of <tt>bd</tt> with respect to rounding-to-nearest-<tt>BigDecimal</tt>.
     */
    public static @NotNull Interval roundingPreimage(@NotNull BigDecimal bd) {
        Rational center = Rational.of(bd);
        Rational maxAbsoluteError = Rational.of(10).pow(-bd.scale()).shiftRight(1);
        return new Interval(Rational.subtract(center, maxAbsoluteError), Rational.add(center, maxAbsoluteError));
    }

    /**
     * Returns a pair of <tt>float</tt>s x, y such that [x, y] is the smallest interval with <tt>float</tt> bounds
     * which contains <tt>this</tt>. x or y may be infinite if <tt>this</tt>'s bounds are infinite or very large in
     * magnitude.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Interval</tt>.</li>
     *  <li>The result cannot be null, and neither can either of its elements be null.</li>
     * </ul>
     *
     * @return the smallest <tt>float</tt> interval containing <tt>this</tt>.
     */
    public @NotNull Pair<Float, Float> floatRange() {
        float fLower = lower == null ? Float.NEGATIVE_INFINITY : lower.toFloat(RoundingMode.FLOOR);
        float fUpper = upper == null ? Float.POSITIVE_INFINITY : upper.toFloat(RoundingMode.CEILING);
        return new Pair<>(fLower, fUpper);
    }

    /**
     * Returns a pair of <tt>double</tt>s x, y such that [x, y] is the smallest interval with <tt>double</tt> bounds
     * which contains <tt>this</tt>. x or y may be infinite if <tt>this</tt>'s bounds are infinite or very large in
     * magnitude.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Interval</tt>.</li>
     *  <li>The result cannot be null, and neither can either of its elements be null.</li>
     * </ul>
     *
     * @return the smallest <tt>double</tt> interval containing <tt>this</tt>.
     */
    public @NotNull Pair<Double, Double> doubleRange() {
        double dLower = lower == null ? Double.NEGATIVE_INFINITY : lower.toDouble(RoundingMode.FLOOR);
        double dUpper = upper == null ? Double.POSITIVE_INFINITY : upper.toDouble(RoundingMode.CEILING);
        return new Pair<>(dLower, dUpper);
    }

    /**
     * Returns a pair of <tt>BigDecimal</tt>s x, y such that [x, y] is the smallest interval with <tt>BigDecimal</tt>
     * bounds (where the <tt>BigDecimal</tt>s have a number of significant figures at least as large as
     * <tt>precision</tt>) which contains <tt>this</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> must be a finitely-bounded <tt>Interval</tt>.</li>
     *  <li><tt>precision</tt> must be non-negative.</li>
     *  <li>If <tt>precision</tt> is 0, both bounds of <tt>this</tt> must have a terminating decimal expansion.</li>
     *  <li>The result is either a pair in which at least one <tt>BigDecimal</tt> x has minimal scale (that is, the
     *  scale is the smallest non-negative n such that x&#x00D7;10<sup>n</sup> is an integer), or a pair in which each
     *  <tt>BigDecimal</tt> has the same number of significant figures.</li>
     * </ul>
     *
     * @param precision the maximum number of significant digits in the elements of the result; or 0, if both elements
     *                  must have full precision
     * @return the smallest <tt>BigDecimal</tt> interval containing <tt>this</tt>, such that the precision of the
     * <tt>BigDecimal</tt>s is at least <tt>precision</tt>.
     */
    public @NotNull Pair<BigDecimal, BigDecimal> bigDecimalRange(int precision) {
        if (lower == null || upper == null)
            throw new ArithmeticException("cannot represent infinities with BigDecimal");
        BigDecimal bdLower = lower.toBigDecimal(precision, RoundingMode.FLOOR);
        BigDecimal bdUpper = upper.toBigDecimal(precision, RoundingMode.CEILING);
        return new Pair<>(bdLower, bdUpper);
    }

    /**
     * Returns the smallest interval a such that if x&#x2208;<tt>this</tt>, &#x2212;x&#x2208;a.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Interval</tt>.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return &#x2212;<tt>this</tt>
     */
    public @NotNull Interval negate() {
        if (lower == null && upper == null) return this;
        if (lower == null) return new Interval(upper.negate(), null);
        if (upper == null) return new Interval(null, lower.negate());
        return new Interval(upper.negate(), lower.negate());
    }

    /**
     * Returns the smallest interval z such that if a&#x2208;<tt>x</tt> and b&#x2208;<tt>y</tt>, a+b&#x2208;z.
     *
     * <ul>
     *  <li><tt>x</tt> cannot be null.</li>
     *  <li><tt>y</tt> cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the first <tt>Interval</tt>
     * @param y the second <tt>Interval</tt>
     * @return <tt>x</tt>+<tt>y</tt>
     */
    public static @NotNull Interval add(@NotNull Interval x, @NotNull Interval y) {
        Rational sLower = x.lower == null || y.lower == null ? null : Rational.add(x.lower, y.lower);
        Rational sUpper = x.upper == null || y.upper == null ? null : Rational.add(x.upper, y.upper);
        return new Interval(sLower, sUpper);
    }

    /**
     * Returns the smallest interval z such that if a&#x2208;<tt>x</tt> and b&#x2208;<tt>y</tt>, a&#x2212;b&#x2208;z.
     *
     * <ul>
     *  <li><tt>x</tt> cannot be null.</li>
     *  <li><tt>y</tt> cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the first <tt>Interval</tt>
     * @param y the second <tt>Interval</tt>
     * @return <tt>x</tt>&#x2212;<tt>y</tt>
     */
    public static @NotNull Interval subtract(@NotNull Interval x, @NotNull Interval y) {
        return add(x, y.negate());
    }

    /**
     * Returns the smallest interval z such that if a&#x2208;<tt>x</tt> and b&#x2208;<tt>y</tt>, a&#x00D7;b&#x2208;z.
     *
     * <ul>
     *  <li><tt>x</tt> cannot be null.</li>
     *  <li><tt>y</tt> cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param x the first <tt>Interval</tt>
     * @param y the second <tt>Interval</tt>
     * @return <tt>x</tt>&#x00D7;<tt>y</tt>
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
            if (min == null || xlyu.compareTo(min) < 0) min = xlyu;
            if (max == null || xlyu.compareTo(max) > 0) max = xlyu;
        }
        if (xuyl != null) {
            if (min == null || xuyl.compareTo(min) < 0) min = xuyl;
            if (max == null || xuyl.compareTo(max) > 0) max = xuyl;
        }
        if (xuyu != null) {
            if (min == null || xuyu.compareTo(min) < 0) min = xuyu;
            if (max == null || xuyu.compareTo(max) > 0) max = xuyu;
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
                || (lower == null && upper.compareTo(Rational.ZERO) > 0)
                || (upper == null && lower.compareTo(Rational.ZERO) < 0)) return ALL;
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
     * Determines whether <tt>this</tt> is equal to <tt>that</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Interval</tt>.</li>
     *  <li><tt>that</tt> may be any <tt>Object</tt>.</li>
     *  <li>The result may be either boolean.</li>
     * </ul>
     *
     * @param that The <tt>Interval</tt> to be compared with <tt>this</tt>
     * @return <tt>this</tt>=<tt>that</tt>
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
     * Calculates the hash code of <tt>this</tt>.
     *
     * <ul>
     *  <li>(conjecture) The result may be any <tt>int</tt>.</li>
     * </ul>
     *
     * @return <tt>this</tt>'s hash code.
     */
    @Override
    public int hashCode() {
        int result = lower != null ? lower.hashCode() : 0;
        result = 31 * result + (upper != null ? upper.hashCode() : 0);
        return result;
    }

    /**
     * Compares <tt>this</tt> to <tt>that</tt>, returning 1, &#x2212;1, or 0 if the answer is "greater than", "less
     * than", or "equal to", respectively. <tt>Interval</tt>s are ordered on their lower bound, then on their upper
     * bound; &#x2212;&#x221e; and &#x221e; behave as expected.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Interval</tt>.</li>
     *  <li><tt>that</tt> cannot be null.</li>
     *  <li>The result may be &#x2212;1, 0, or 1.</li>
     * </ul>
     *
     * @param that The <tt>Interval</tt> to be compared with <tt>this</tt>
     * @return <tt>this</tt> compared to <tt>that</tt>
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
     * Creates an <tt>Interval</tt> from a <tt>String</tt>. Valid strings are in one of these four forms:
     * <tt>"(-Infinity, Infinity)"</tt>, <tt>"(-Infinity, " + q.toString() + "]"</tt>,
     * <tt>"[" + p.toString() + ", Infinity)"</tt>, or <tt>"[" + p.toString() + ", " + q.toString() + "]"</tt>, where
     * <tt>p</tt> and <tt>q</tt> are <tt>Rational</tt>s.
     *
     * <ul>
     *  <li><tt>s</tt> cannot be null and cannot be of the form
     *  <tt>"[" + p.toString() + ", " + q.toString() + "]"</tt>, where <tt>p</tt> and <tt>q</tt> are <tt>Rational</tt>s
     *  such that <tt>a</tt> is greater than <tt>b</tt>.</li> <li>The result may be any <tt>Interval</tt>, or
     *  null.</li>
     * </ul>
     *
     * @param s a string representation of a <tt>Rational</tt>.
     * @return the <tt>Rational</tt> represented by <tt>s</tt>, or null if <tt>s</tt> is invalid.
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
            if (!rightString.equals("Infinity")) return null;
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
     * Creates a string representation of <tt>this</tt>.
     *
     * <ul>
     *  <li><tt>this</tt> may be any <tt>Interval</tt>.</li>
     *  <li>The result is a string in on of four forms: <tt>"(-Infinity, Infinity)"</tt>,
     *  <tt>"(-Infinity, " + q.toString() + "]"</tt>, <tt>"[" + p.toString() + ", Infinity)"</tt>, or
     *  <tt>"[" + p.toString() + ", " + q.toString() + "]"</tt>, where <tt>p</tt> and <tt>q</tt> are <tt>Rational</tt>s
     *  such that <tt>p</tt> is less than or equal to <tt>q</tt>.</li>
     * </ul>
     *
     * @return a string representation of <tt>this</tt>.
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
