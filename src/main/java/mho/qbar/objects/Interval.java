package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.numberUtils.FloatingPointUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.ordering.comparators.WithNullComparator;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.assertTrue;

/**
 * <p>The {@code Interval} class represents an interval of real numbers. If we let p and q be rationals, p≤q, the
 * representable intervals are (–∞, ∞), (–∞, q], [p, ∞), and [p, q]. If p = q, the {@code Interval} represents a single
 * number. The empty interval cannot be represented.</p>
 *
 * <p>In general, f(I), where I is an {@code Interval}, is taken to mean the image of I under f. Often this image is
 * not an {@code Interval} itself, in which case the function might return a set of {@code Interval}s, whose closed
 * union is the image, or it may just return the closure of the image's convex hull. Similar considerations apply for
 * functions of two {@code Interval}s, <i>etc.</i></p>
 *
 * <p>This class is immutable.</p>
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
    private final Rational lower;

    /**
     * The upper bound of this interval if the upper bound is finite, or null if the upper bound is ∞
     */
    private final Rational upper;

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
    private Interval(Rational lower, Rational upper) {
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
        if (gt(lower, upper)) {
            throw new IllegalArgumentException("lower must be less than or equal to upper. lower: " +
                    lower + ", upper: " + upper);
        }
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
     * Returns the bit length of {@code this}, or the sum of the bit lengths of the lower and upper bounds (see
     * {@link Rational#bitLength()}. If either bound is infinite, it does not contribute to the bit length.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @return the bit length of {@code this}
     */
    public int bitLength() {
        return (lower == null ? 0 : lower.bitLength()) + (upper == null ? 0 : upper.bitLength());
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
        return (lower == null || ge(x, lower)) && (upper == null || le(x, upper));
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
               (upper == null || (that.upper != null && le(that.upper, upper))); ///
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
        if (lower == null || upper == null) {
            return Optional.empty();
        } else {
            return Optional.of(upper.subtract(lower));
        }
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
        if (any(a -> a == null, as)) {
            throw new NullPointerException();
        }
        if (as.isEmpty()) {
            throw new IllegalArgumentException("as cannot be empty.");
        }
        return foldl1(Interval::convexHull, as);
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

        if (iLower != null && iUpper != null && gt(iLower, iUpper)) {
            return Optional.empty();
        } else {
            return Optional.of(new Interval(iLower, iUpper));
        }
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
        if (lower == null && upper == null) {
            return false;
        } else if (lower == null) {
            return that.lower != null && lt(upper, that.lower);
        } else if (upper == null) {
            return that.upper != null && lt(that.upper, lower);
        } else {
            return that.lower != null && lt(upper, that.lower) || (that.upper != null && lt(that.upper, lower));
        }
    }

    /**
     * Expresses the union of a list of {@code Interval}s as a list of disjoint {@code Interval}s in ascending order.
     *
     * <ul>
     *  <li>{@code as} cannot be null and may not contain any null elements.</li>
     *  <li>The result is a sorted list of pairwise disjoint {@code Interval}s.</li>
     * </ul>
     *
     * @param as a list of {@code Interval}s
     * @return ⋃{@code as}
     */
    public static @NotNull List<Interval> union(@NotNull List<Interval> as) {
        if (as.size() == 1 && head(as) == null) {
            throw new NullPointerException();
        }
        if (as.size() < 2) return as;
        Map<Rational, Rational> lowerMap = new TreeMap<>(new WithNullComparator<>());
        for (Interval a : as) {
            Rational lower = a.lower;
            Rational upper = a.upper;
            if (lowerMap.containsKey(lower)) {
                Rational previousUpper = lowerMap.get(lower);
                if (previousUpper != null && (upper == null || gt(upper, previousUpper))) {
                    lowerMap.put(lower, upper);
                }
            } else {
                lowerMap.put(lower, upper);
            }
        }
        List<Interval> union = new ArrayList<>();
        Rational currentLower = null;
        Rational currentUpper = null;
        boolean first = true;
        for (Map.Entry<Rational, Rational> entry : lowerMap.entrySet()) {
            Rational lower = entry.getKey();
            Rational upper = entry.getValue();
            if (first) {
                first = false;
                currentLower = lower;
                currentUpper = upper;
            } else {
                if (currentUpper == null || le(lower, currentUpper)) {
                    if (currentUpper != null && (upper == null || gt(upper, currentUpper))) {
                        currentUpper = upper;
                    }
                } else {
                    union.add(new Interval(currentLower, currentUpper));
                    currentLower = lower;
                    currentUpper = upper;
                }
            }
        }
        if (!first) {
            union.add(new Interval(currentLower, currentUpper));
        }
        return union;
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
        if (lower == null && upper == null) return Collections.emptyList();
        if (lower == null) return Collections.singletonList(new Interval(upper, null));
        if (upper == null) return Collections.singletonList(new Interval(null, lower));
        if (lower.equals(upper)) return Collections.singletonList(ALL);
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
        if (lower == null || upper == null) {
            throw new ArithmeticException("this must be finitely bounded. Invalid this: " + this);
        }
        return lower.add(upper).shiftRight(1);
    }

    /**
     * Splits {@code this} into two {@code Interval}s at {@code x}.
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
        if (!contains(x)) {
            throw new ArithmeticException("this must contain x. this: " + this + ", x: " + x);
        }
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
        if (Float.isNaN(f)) {
            throw new ArithmeticException("f cannot be NaN.");
        }
        if (Float.isInfinite(f)) {
            if (f > 0) {
                return new Interval(Rational.LARGEST_FLOAT, null);
            } else {
                return new Interval(null, Rational.LARGEST_FLOAT.negate());
            }
        }
        if (f == Float.MAX_VALUE) {
            return new Interval(
                    Rational.ofExact(FloatingPointUtils.predecessor(f)).get()
                            .add(Rational.LARGEST_FLOAT).shiftRight(1),
                    Rational.LARGEST_FLOAT
            );
        }
        if (f == -Float.MAX_VALUE) {
            return new Interval(
                    Rational.LARGEST_FLOAT.negate(),
                    Rational.ofExact(FloatingPointUtils.successor(f)).get()
                            .subtract(Rational.LARGEST_FLOAT).shiftRight(1)
            );
        }
        Rational r = Rational.ofExact(f).get();
        float predecessor = FloatingPointUtils.predecessor(f);
        Rational lower = predecessor == Float.NEGATIVE_INFINITY ?
                null :
                r.add(Rational.ofExact(predecessor).get()).shiftRight(1);
        float successor = FloatingPointUtils.successor(f);
        Rational upper = successor == Float.POSITIVE_INFINITY ?
                null :
                r.add(Rational.ofExact(successor).get()).shiftRight(1);
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
        if (Double.isNaN(d)) {
            throw new ArithmeticException("d cannot be NaN.");
        }
        if (Double.isInfinite(d)) {
            if (d > 0) {
                return new Interval(Rational.LARGEST_DOUBLE, null);
            } else {
                return new Interval(null, Rational.LARGEST_DOUBLE.negate());
            }
        }
        if (d == Double.MAX_VALUE) {
            return new Interval(
                    Rational.ofExact(FloatingPointUtils.predecessor(d)).get()
                            .add(Rational.LARGEST_DOUBLE).shiftRight(1),
                    Rational.LARGEST_DOUBLE
            );
        }
        if (d == -Double.MAX_VALUE) {
            return new Interval(
                    Rational.LARGEST_DOUBLE.negate(),
                    Rational.ofExact(FloatingPointUtils.successor(d)).get()
                            .subtract(Rational.LARGEST_DOUBLE).shiftRight(1)
            );
        }
        Rational r = Rational.ofExact(d).get();
        double predecessor = FloatingPointUtils.predecessor(d);
        Rational lower = predecessor == Double.NEGATIVE_INFINITY ?
                null :
                r.add(Rational.ofExact(predecessor).get()).shiftRight(1);
        double successor = FloatingPointUtils.successor(d);
        Rational upper = predecessor == Double.POSITIVE_INFINITY ?
                null :
                r.add(Rational.ofExact(successor).get()).shiftRight(1);
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
        Rational maxAbsoluteError = Rational.TEN.pow(-bd.scale()).shiftRight(1);
        return new Interval(center.subtract(maxAbsoluteError), center.add(maxAbsoluteError));
    }

    /**
     * Returns the smallest {@code Interval} z such that if a∈{@code this} and b∈{@code that}, a+b∈z.
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
        Rational lowerSum = lower == null || that.lower == null ? null : lower.add(that.lower);
        Rational upperSum = upper == null || that.upper == null ? null : upper.add(that.upper);
        return new Interval(lowerSum, upperSum);
    }

    /**
     * Returns the smallest {@code Interval} a such that if x∈{@code this}, –x∈a.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return –{@code this}
     */
    public @NotNull Interval negate() {
        if (lower == null && upper == null) {
            return this;
        } else if (lower == null) {
            return new Interval(upper.negate(), null);
        } else if (upper == null) {
            return new Interval(null, lower.negate());
        } else {
            return new Interval(upper.negate(), lower.negate());
        }
    }

    /**
     * Returns the smallest {@code Interval} a such that if x∈{@code this}, |x|∈a.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result is an interval whose lower bound is finite and non-negative.</li>
     * </ul>
     *
     * @return |{@code this}|
     */
    public @NotNull Interval abs() {
        if (lower == null && upper == null) {
            return new Interval(Rational.ZERO, null);
        } else if (lower == null) {
            return new Interval(upper.signum() == -1 ? upper.negate() : Rational.ZERO, null);
        } else if (upper == null) {
            return lower.signum() == -1 ? new Interval(Rational.ZERO, null) : this;
        } else if (lower.signum() != -1 && upper.signum() != -1) {
            return this;
        } else if (lower.signum() == -1 && upper.signum() == -1) {
            return negate();
        } else {
            return new Interval(Rational.ZERO, max(lower.negate(), upper));
        }
    }

    /**
     * If every real in {@code this} has the same sign, returns that sign; otherwise, returns an empty
     * {@code Optional}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>The result is either empty, or contains -1, 0, or 1.</li>
     * </ul>
     *
     * @return sgn(x) for all x∈{@code this}, if sgn(x) is unique
     */
    public @NotNull Optional<Integer> signum() {
        int lowerSignum = lower == null ? -1 : lower.signum();
        int upperSignum = upper == null ? 1 : upper.signum();
        return lowerSignum == upperSignum ? Optional.of(lowerSignum) : Optional.<Integer>empty();
    }

    /**
     * Returns the smallest {@code Interval} z such that if a∈{@code this} and b∈{@code that}, a–b∈z.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Interval} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull Interval subtract(@NotNull Interval that) {
        Rational lowerDifference = lower == null || that.upper == null ? null : lower.subtract(that.upper);
        Rational upperDifference = upper == null || that.lower == null ? null : upper.subtract(that.lower);
        return new Interval(lowerDifference, upperDifference);
    }

    /**
     * Returns the smallest {@code Interval} z such that if a∈{@code this} and b∈{@code that}, a×b∈z. Interval addition
     * does not distribute over interval multiplication: for example, ([0, 1] + (–∞, -1]) × [0, 1] = (–∞, 0], but
     * [0, 1] × [0, 1] + (–∞, -1] × [0, 1] = (–∞, 1].
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Interval} that {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Interval multiply(@NotNull Interval that) {
        if (equals(ZERO) || that.equals(ZERO)) return ZERO;
        boolean thisContainsPositive = upper == null || upper.signum() == 1;
        boolean thatContainsPositive = that.upper == null || that.upper.signum() == 1;
        boolean thisContainsNegative = lower == null || lower.signum() == -1;
        boolean thatContainsNegative = that.lower == null || that.lower.signum() == -1;
        boolean unboundedBelow =
                lower == null && thatContainsPositive ||
                upper == null && thatContainsNegative ||
                that.lower == null && thisContainsPositive ||
                that.upper == null && thisContainsNegative;
        boolean unboundedAbove =
                upper == null && thatContainsPositive ||
                lower == null && thatContainsNegative ||
                that.upper == null && thisContainsPositive ||
                that.lower == null && thisContainsNegative;
        if (unboundedBelow && unboundedAbove) return ALL;
        List<Rational> extremes = new ArrayList<>();
        if (lower != null) {
            if (that.lower != null) extremes.add(lower.multiply(that.lower));
            if (that.upper != null) extremes.add(lower.multiply(that.upper));
        }
        if (upper != null) {
            if (that.lower != null) extremes.add(upper.multiply(that.lower));
            if (that.upper != null) extremes.add(upper.multiply(that.upper));
        }
        if (unboundedBelow) {
            return new Interval(null, maximum(extremes));
        } else if (unboundedAbove) {
            return new Interval(minimum(extremes), null);
        } else {
            Pair<Rational, Rational> minimumMaximum = minimumMaximum(extremes);
            return new Interval(minimumMaximum.a, minimumMaximum.b);
        }
    }

    /**
     * Returns the smallest {@code Interval} a such that if x∈{@code this}, x×{@code that}∈a.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Rational} that {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Interval multiply(@NotNull Rational that) {
        if (that == Rational.ZERO || equals(ZERO)) return ZERO;
        if (that == Rational.ONE) return this;
        if (that.signum() == 1) {
            return new Interval(
                    lower == null ? null : lower.multiply(that),
                    upper == null ? null : upper.multiply(that)
            );
        } else {
            return new Interval(
                    upper == null ? null : upper.multiply(that),
                    lower == null ? null : lower.multiply(that)
            );
        }
    }

    /**
     * Returns the smallest {@code Interval} a such that if x∈{@code this}, x×{@code that}∈a.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code BigInteger} that {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Interval multiply(@NotNull BigInteger that) {
        if (that.equals(BigInteger.ZERO) || equals(ZERO)) return ZERO;
        if (that.equals(BigInteger.ONE)) return this;
        if (that.signum() == 1) {
            return new Interval(
                    lower == null ? null : lower.multiply(that),
                    upper == null ? null : upper.multiply(that)
            );
        } else {
            return new Interval(
                    upper == null ? null : upper.multiply(that),
                    lower == null ? null : lower.multiply(that)
            );
        }
    }

    /**
     * Returns the smallest interval a such that if x∈{@code this}, x×{@code that}∈a.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code int} that {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Interval multiply(int that) {
        if (that == 0 || equals(ZERO)) return ZERO;
        if (that == 1) return this;
        if (that > 0) {
            return new Interval(
                    lower == null ? null : lower.multiply(that),
                    upper == null ? null : upper.multiply(that)
            );
        } else {
            return new Interval(
                    upper == null ? null : upper.multiply(that),
                    lower == null ? null : lower.multiply(that)
            );
        }
    }

    /**
     * Returns the closure of the image of {@code this} under multiplicative inversion. In general this is not one
     * {@code Interval}, so this method returns a list of disjoint {@code Interval}s whose union is the closure of the
     * image.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>
     *   The result is one of the following:
     *   <ul>
     *    <li>[]</li>
     *    <li>[(–∞, ∞)]</li>
     *    <li>[(–∞, q]] where q≤0</li>
     *    <li>[[p, ∞)] where p≥0</li>
     *    <li>[[p, q]] where p≠q, p≥0, q{@literal >}0</li>
     *    <li>[[p, q]] where p≠q, p{@literal <}0, q≤0</li>
     *    <li>[[p, p]] where p≠0</li>
     *    <li>[(–∞, q], [0, ∞)] where q{@literal <}0</li>
     *    <li>[(–∞, 0], [p, ∞)] where p{@literal >}0</li>
     *    <li>[(–∞, q], [p, ∞)] where q{@literal <}0, p{@literal >}0</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @return 1/{@code this}
     */
    public @NotNull List<Interval> invert() {
        List<Interval> intervals = new ArrayList<>();
        if (lower == null && upper == null) {
            intervals.add(ALL);
        } else if (lower == null) {
            if (upper == Rational.ZERO) {
                intervals.add(this);
            } else if (upper.signum() == 1) {
                intervals.add(new Interval(null, Rational.ZERO));
                intervals.add(new Interval(upper.invert(), null));
            } else {
                intervals.add(new Interval(upper.invert(), Rational.ZERO));
            }
            return intervals;
        } else if (upper == null) {
            if (lower == Rational.ZERO) {
                intervals.add(this);
            } else if (lower.signum() == 1) {
                intervals.add(new Interval(Rational.ZERO, lower.invert()));
            } else {
                intervals.add(new Interval(null, lower.invert()));
                intervals.add(new Interval(Rational.ZERO, null));
            }
        } else if (lower == Rational.ZERO) {
            if (upper != Rational.ZERO) {
                intervals.add(new Interval(upper.invert(), null));
            }
        } else if (lower.signum() == 1) {
            intervals.add(new Interval(upper.invert(), lower.invert()));
        } else {
            if (upper == Rational.ZERO) {
                intervals.add(new Interval(null, lower.invert()));
            } else if (upper.signum() == -1) {
                intervals.add(new Interval(upper.invert(), lower.invert()));
            } else {
                intervals.add(new Interval(null, lower.invert()));
                intervals.add(new Interval(upper.invert(), null));
            }
        }
        return intervals;
    }

    /**
     * Returns the smallest {@code Interval} a such that if x∈{@code this}, 1/x∈a.
     *
     * <ul>
     *  <li>{@code this} cannot be [0, 0].</li>
     *  <li>
     *   The result is one of the following:
     *   <ul>
     *    <li>(–∞, ∞)</li>
     *    <li>(–∞, q] where q≤0</li>
     *    <li>[p, ∞) where p≥0</li>
     *    <li>[p, q] where p≠q, p and q both positive</li>
     *    <li>[p, q] where p≠q, p and q both negative</li>
     *    <li>[p, p] where p≠0</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @return Conv(1/{@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Interval invertHull() {
        List<Interval> inverse = invert();
        switch (inverse.size()) {
            case 0:
                throw new ArithmeticException("division by zero");
            case 1:
                return inverse.get(0);
            default:
                return ALL;
        }
    }

    /**
     * Returns the closure of the set z such that if a∈{@code this} and b∈{@code that}, a/b∈z. z is represented as a
     * union of a list of {@code Interval}s.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>
     *   The result is one of the following:
     *   <ul>
     *    <li>[]</li>
     *    <li>A list containing a single, non-null {@code Interval}</li>
     *    <li>[(–∞, q], [0, ∞)] where q{@literal <}0</li>
     *    <li>[(–∞, 0], [p, ∞)] where p{@literal >}0</li>
     *    <li>[(–∞, q], [p, ∞)] where q{@literal <}0, p{@literal >}0</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param that the {@code Interval} that {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull List<Interval> divide(@NotNull Interval that) {
        return union(toList(map(this::multiply, that.invert())));
    }

    /**
     * Returns the smallest {@code Interval} z such that if a∈{@code this} and b∈{@code that}, a/b∈z.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be [0, 0].</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Interval} that {@code this} is divided by
     * @return Conv({@code this}/{@code that})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Interval divideHull(@NotNull Interval that) {
        List<Interval> quotient = divide(that);
        switch (quotient.size()) {
            case 0:
                throw new ArithmeticException("division by zero");
            case 1:
                return quotient.get(0);
            default:
                return ALL;
        }
    }

    /**
     * Returns the smallest {@code Interval} a such that if x∈{@code this}, x/{@code that}∈a.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Rational} that {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull Interval divide(@NotNull Rational that) {
        if (that == Rational.ZERO)
            throw new ArithmeticException("division by zero");
        if (that == Rational.ONE) return this;
        if (that.signum() == 1) {
            return new Interval(
                    lower == null ? null : lower.divide(that),
                    upper == null ? null : upper.divide(that)
            );
        } else {
            return new Interval(
                    upper == null ? null : upper.divide(that),
                    lower == null ? null : lower.divide(that)
            );
        }
    }

    /**
     * Returns the smallest {@code Interval} a such that if x∈{@code this}, x/{@code that}∈a.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code BigInteger} that {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull Interval divide(@NotNull BigInteger that) {
        if (that.equals(BigInteger.ZERO))
            throw new ArithmeticException("division by zero");
        if (that.equals(BigInteger.ONE)) return this;
        if (that.signum() == 1) {
            return new Interval(
                    lower == null ? null : lower.divide(that),
                    upper == null ? null : upper.divide(that)
            );
        } else {
            return new Interval(
                    upper == null ? null : upper.divide(that),
                    lower == null ? null : lower.divide(that)
            );
        }
    }

    /**
     * Returns the smallest {@code Interval} a such that if x∈{@code this}, x/{@code that}∈a.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code int} that {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull Interval divide(int that) {
        if (that == 0)
            throw new ArithmeticException("division by zero");
        if (that == 1) return this;
        if (that > 0) {
            return new Interval(
                    lower == null ? null : lower.divide(that),
                    upper == null ? null : upper.divide(that)
            );
        } else {
            return new Interval(
                    upper == null ? null : upper.divide(that),
                    lower == null ? null : lower.divide(that)
            );
        }
    }

    /**
     * Returns the smallest {@code Interval} a such that if x∈{@code this}, x≪{@code bits}∈a.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Interval}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull Interval shiftLeft(int bits) {
        if (bits == 0) return this;
        return new Interval(
                lower == null ? null : lower.shiftLeft(bits),
                upper == null ? null : upper.shiftLeft(bits)
        );
    }

    /**
     * Returns the smallest {@code Interval} a such that if x∈{@code this}, x≫{@code bits}∈a.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Interval}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the number of bits to right-shift by
     * @return {@code this}≫{@code bits}
     */
    public @NotNull Interval shiftRight(int bits) {
        if (bits == 0) return this;
        return new Interval(
                lower == null ? null : lower.shiftRight(bits),
                upper == null ? null : upper.shiftRight(bits)
        );
    }

    /**
     * Returns the sum of all the {@code Interval}s in {@code xs}. If {@code xs} is empty, [0, 0] is returned.
     *
     * <ul>
     *  <li>{@code xs} must be finite and may not contain any nulls.</li>
     *  <li>The result may be any {@code Interval}.</li>
     * </ul>
     *
     * @param xs an {@code Iterable} of {@code Interval}s.
     * @return Σxs
     */
    public static @NotNull Interval sum(@NotNull Iterable<Interval> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        return foldl(Interval::add, ZERO, xs);
    }

    /**
     * Returns the product of all the {@code Interval}s in {@code xs}. If {@code xs} is empty, [1, 1] is returned.
     *
     * <ul>
     *  <li>{@code xs} must be finite and may not contain any nulls.</li>
     *  <li>The result may be any {@code Interval}.</li>
     * </ul>
     *
     * @param xs an {@code Iterable} of {@code Interval}s.
     * @return Πxs
     */
    public static @NotNull Interval product(@NotNull Iterable<Interval> xs) {
        if (any(x -> x == null, xs)) {
            throw new NullPointerException();
        }
        return foldl(Interval::multiply, ONE, xs);
    }

    /**
     * Returns the differences between successive {@code Interval}s in {@code xs}. If {@code xs} contains a single
     * {@code Interval}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not support removal.
     *
     * <ul>
     *  <li>{@code xs} must not be empty and may not contain any nulls.</li>
     *  <li>The result does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code Interval}s.
     * @return Δxs
     */
    public static @NotNull Iterable<Interval> delta(@NotNull Iterable<Interval> xs) {
        if (isEmpty(xs))
            throw new IllegalArgumentException("cannot get delta of empty Iterable");
        if (head(xs) == null)
            throw new NullPointerException();
        return adjacentPairsWith((x, y) -> y.subtract(x), xs);
    }

    /**
     * Returns the closure of the image of {@code this} under integer exponentiation. In general this is not one
     * {@code Interval} (because raising to a negative power requires inversion), so this method returns a list of
     * disjoint {@code Interval}s whose union is the closure of the image. [0, 0]<sup>0</sup> yields [[1, 1]].
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code p} may be any {@code int}.</li>
     *  <li>
     *   The result is one of the following:
     *   <ul>
     *    <li>[]</li>
     *    <li>A list containing a single, non-null {@code Interval}</li>
     *    <li>[(–∞, q], [0, ∞)] where q{@literal <}0</li>
     *    <li>[(–∞, 0], [p, ∞)] where p{@literal >}0</li>
     *    <li>[(–∞, q], [p, ∞)] where q{@literal <}0, p{@literal >}0</li>
     *   </ul>
     *  </li>
     * </ul>
     *
     * @param p the power that {@code this} is raised to
     * @return {@code this}<sup>{@code p}</sup>
     */
    public @NotNull List<Interval> pow(int p) {
        List<Interval> intervals = new ArrayList<>();
        if (p == 0) {
            intervals.add(ONE);
        } else if (p == 1) {
            intervals.add(this);
        } else if (p < 0) {
            intervals.addAll(pow(-p).get(0).invert());
        } else if (p % 2 == 0) {
            int lowerSign = lower == null ? -1 : lower.signum();
            int upperSign = upper == null ? 1 : upper.signum();
            if (lowerSign != -1 && upperSign != -1) {
                intervals.add(new Interval(lower.pow(p), upper == null ? null : upper.pow(p)));
            } else if (lowerSign != 1 && upperSign != 1) {
                intervals.add(new Interval(upper.pow(p), lower == null ? null : lower.pow(p)));
            } else {
                Rational a = lower == null ? null : lower.pow(p);
                Rational b = upper == null ? null : upper.pow(p);
                Rational max = a == null || b == null ? null : max(a, b);
                intervals.add(new Interval(Rational.ZERO, max));
            }
        } else {
            intervals.add(new Interval(lower == null ? null : lower.pow(p), upper == null ? null : upper.pow(p)));
        }
        return intervals;
    }

    /**
     * Returns the smallest {@code Interval} a such that if x∈{@code this}, x<sup>{@code p}</sup>∈a.
     * [0, 0]<sup>0</sup> yields [1, 1].
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code p} may be any {@code int}.</li>
     *  <li>If {@code this} is [0, 0], {@code p} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param p the power that {@code this} is raised to
     * @return Conv(x<sup>{@code p}</sup>)
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Interval powHull(int p) {
        if (p < 0 && this.equals(ZERO))
            throw new ArithmeticException("division by zero");
        return convexHull(pow(p));
    }

    /**
     * If {@code this} and {@code that} are disjoint, returns the ordering between any element of {@code this} and any
     * element of {@code that}; otherwise, returns an empty {@code Optional}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Interval}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be any {@code Optional<Ordering>}.</li>
     * </ul>
     *
     * @param that the interval whose elements we're comparing {@code this}'s elements to
     * @return compare(x, y) for all x∈{@code this} and y∈{@code that}, if compare(x, y) is unique
     */
    public @NotNull Optional<Ordering> elementCompare(@NotNull Interval that) {
        if (lower != null && lower.equals(upper) && equals(that)) return Optional.of(EQ);
        if (!disjoint(that)) return Optional.empty();
        Rational thisSample = lower == null ? upper : lower;
        Rational thatSample = that.lower == null ? that.upper : that.lower;
        return Optional.of(compare(thisSample, thatSample));
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
     * @param that The {@code Object} to be compared with {@code this}
     * @return {@code this}={@code that}
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;
        Interval interval = (Interval) that;
        return (lower == null ? interval.lower == null : lower.equals(interval.lower)) &&
               (upper == null ? interval.upper == null : upper.equals(interval.upper)); ///
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
     *  <li>The result may be any {@code Optional<Interval>}.</li>
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
        if (lower != null && upper != null && gt(lower, upper)) return Optional.empty();
        return Optional.of(new Interval(lower, upper));
    }

    /**
     * Finds the first occurrence of an {@code Interval} in a {@code String}. Returns the {@code Interval} and the
     * index at which it was found. Returns an empty {@code Optional} if no {@code Interval} is found. Only
     * {@code String}s which could have been emitted by {@link mho.qbar.objects.Interval#toString} are recognized. The
     * longest possible {@code Interval} is parsed.
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
        return Readers.genericFindIn(Interval::read, " (),-/0123456789I[]finty").apply(s);
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

    /**
     * Ensures that {@code this} is valid. Must return true for any {@code Interval} used outside this class.
     */
    public void validate() {
        assertTrue(this, lower == null || upper == null || le(lower, upper));
    }
}
