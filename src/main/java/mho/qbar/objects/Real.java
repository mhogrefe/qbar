package mho.qbar.objects;

import mho.wheels.iterables.NoRemoveIterator;
import mho.wheels.math.BinaryFraction;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Quadruple;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

/**
 * <p>The {@code Real} class represents real numbers as infinite, converging sequences of bounding intervals. Every
 * interval in the sequence must be contained in the preceding interval, and the intervals' diameters must converge to
 * zero. The rate of convergence is unspecified. Every interval must take a finite amount of computing time to produce.
 * These conditions cannot be enforced except by the caller, so be careful when constructing your own reals, and prefer
 * to use the methods of this class, which, when given valid {@code Real}s, will always return valid {@code Real}s.</p>
 *
 * <p>Any real number may be represented by infinitely many {@code Real}s. Given a rational number r, the {@code Real}
 * whose {@code rational} field is set to {@code r} is called the exact representation of r, and all other
 * {@code Real}s representing r are called fuzzy representations. Fuzzy {@code Real}s are difficult to work with, but
 * also difficult to avoid. Sometimes, a method that takes a {@code Real} will loop forever given certain fuzzy
 * {@code Real}s. If this is the case, that behavior is documented and the method is labeled unsafe. Often, a safe
 * alternative is provided that gives up after the diameters of the approximating intervals decrease below a certain
 * resolution.</p>
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
    public static final @NotNull Real SQRT_TWO = rootOfRational(Rational.TWO, 2);

    /**
     * φ, the golden ratio
     */
    public static final @NotNull Real PHI = Algebraic.PHI.realValue();

    /**
     * e, the base of the natural logarithm
     */
    public static final @NotNull Real E = expOfRational(Rational.ONE);

    /**
     * arctan(1), or π/4
     */
    private static final @NotNull Real ARCTAN_ONE =
            arctan01(Rational.of(1, 5)).shiftLeft(2).subtract(arctan01(Rational.of(1, 239)));

    /**
     * π, the ratio of a circle's circumference to its diameter
     */
    public static final @NotNull Real PI = ARCTAN_ONE.shiftLeft(2);

    /**
     * log(2), the unique solution of exp(x)=2
     */
    public static final @NotNull  Real LOG_2 = log12(Rational.of(3, 2)).add(log12(Rational.of(4, 3)));

    /**
     * The prime constant: the number whose nth binary digit after the decimal point is 1 if n is prime and 0
     * otherwise. It is irrational and probably transcendental.
     */
    public static final @NotNull Real PRIME_CONSTANT = fromDigits(
            IntegerUtils.TWO,
            Collections.emptyList(),
            map(i -> MathUtils.isPrime(i) ? BigInteger.ONE : BigInteger.ZERO, EP.rangeUpIncreasing(BigInteger.ONE))
    );

    /**
     * The Thue-Morse constant, or the number produced by interpreting the Thue-Morse sequence (see
     * {@link MathUtils#THUE_MORSE}) as binary digits after the decimal point, with false mapping to 0 and true mapping
     * to 1.
     */
    public static final @NotNull Real THUE_MORSE = fromDigits(
            IntegerUtils.TWO,
            Collections.emptyList(),
            map(b -> b ? BigInteger.ONE : BigInteger.ZERO, MathUtils.THUE_MORSE)
    );

    /**
     * The Kolakoski constant, or the number produced by interpreting the Kolakoski sequence (see
     * {@link MathUtils#KOLAKOSKI}) as binary digits after the decimal point, with 1 mapping to 0 and 2 mapping to 1.
     */
    public static final @NotNull Real KOLAKOSKI = fromDigits(
            IntegerUtils.TWO,
            Collections.emptyList(),
            map(i -> i == 1 ? BigInteger.ZERO : BigInteger.ONE, MathUtils.KOLAKOSKI)
    );

    /**
     * The continued fraction constant C<sub>1</sub> whose continued fraction is [0, 1, 2, 3, …]. It can be expressed
     * exactly in terms of modified Bessel functions.
     */
    public static final @NotNull Real CONTINUED_FRACTION_CONSTANT =
            fromContinuedFraction(EP.rangeUpIncreasing(BigInteger.ZERO));

    /**
     * The sequence used to generate the continued fraction of Cahen's constant.
     */
    private static final @NotNull Iterable<BigInteger> CAHEN_SEQUENCE = () -> new Iterator<BigInteger>() {
        private BigInteger twoBefore = null;
        private BigInteger oneBefore = null;

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public @NotNull BigInteger next() {
            if (twoBefore == null) {
                twoBefore = BigInteger.ONE;
                return BigInteger.ONE;
            } else if (oneBefore == null) {
                oneBefore = BigInteger.ONE;
                return BigInteger.ONE;
            } else {
                BigInteger next = twoBefore.pow(2).multiply(oneBefore).add(twoBefore);
                twoBefore = oneBefore;
                oneBefore = next;
                return next;
            }
        }
    };

    /**
     * Cahen's constant, the sum of the reciprocals of the even terms (starting from 2, the 0th term) of Sylvester's
     * sequence (see {@link MathUtils#SYLVESTER}. It is transcendental.
     */
    public static final @NotNull Real CAHEN = fromContinuedFraction(
            concat(Arrays.asList(BigInteger.ZERO, BigInteger.ONE), map(i -> i.pow(2), CAHEN_SEQUENCE))
    );

    /**
     * 2^(-72), the default resolution of those methods which give up a computation after the bounding interval becomes
     * too small.
     */
    public static final @NotNull Rational DEFAULT_RESOLUTION = Rational.ONE.shiftRight(72);

    /**
     * 36, the number of ASCII alphanumeric characters
     */
    private static final @NotNull BigInteger ASCII_ALPHANUMERIC_COUNT = BigInteger.valueOf(36);

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
    public @NotNull Iterator<Interval> iterator() {
        return intervals.iterator();
    }

    /**
     * Compares the diameters of two {@code Interval}s (see {@link Interval#diameter()}). Empty diameters correspond to
     * unbounded intervals, so they are largest.
     *
     * <ul>
     *  <li>{@code aDiameter} cannot be negative.</li>
     *  <li>{@code bDiameter} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param aDiameter the diameter of the first {@code Interval}
     * @param bDiameter the diameter of the second {@code Interval}
     * @return {@code LT} if the first diameter is smaller than the second diameter, {@code GT} if the first diameter
     * is greater than the second diameter, and {@code EQ} if the diameters are equal
     */
    private static @NotNull Ordering compareDiameters(
            @NotNull Optional<Rational> aDiameter,
            @NotNull Optional<Rational> bDiameter
    ) {
        boolean aFinite = aDiameter.isPresent();
        boolean bFinite = bDiameter.isPresent();
        if (aFinite && bFinite) {
            return Ordering.compare(aDiameter.get(), bDiameter.get());
        } else if (aFinite) {
            return LT;
        } else if (bFinite) {
            return GT;
        } else {
            return EQ;
        }
    }

    /**
     * Given a {@code List} of {@code Interval}s, returns the zero-based index of the one with the largest diameter. If
     * multiple {@code Interval}s share the largest diameter, the index of the first one is returned. A set of indices
     * to skip is provided; the diameters of the {@code Interval}s at those indices are not considered.
     *
     * <ul>
     *  <li>{@code xs} cannot be empty and cannot contain nulls.</li>
     *  <li>{@code skipIndices} cannot contain nulls or negative numbers.</li>
     *  <li>The largest integer in {@code skipIndices} must be less than the size of {@code xs}. {@code skipIndices}
     *  cannot contain every integer from 0 to |{@code xs}|–1, inclusive.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @param xs a list of {@code Interval}s
     * @param skipIndices the indices of the {@code Interval}s to ignore
     * @return the first index of an {@code Interval} with maximal diameter in {@code xs}, excluding those
     * {@code Interval}s at an index in {@code skipIndices}
     */
    private static int largestDiameterIndex(@NotNull List<Interval> xs, Set<Integer> skipIndices) {
        int largestDiameterIndex = -1;
        Optional<Rational> largestDiameter = null;
        for (int i = 0; i < xs.size(); i++) {
            if (skipIndices.contains(i)) continue;
            Optional<Rational> diameter = xs.get(i).diameter();
            if (largestDiameterIndex == -1 || compareDiameters(diameter, largestDiameter) == GT) {
                largestDiameterIndex = i;
                largestDiameter = diameter;
            }
        }
        return largestDiameterIndex;
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
        List<Iterator<Interval>> targetIterators = toList(map(Iterable::iterator, targets));
        targetIterators.add(intervals.iterator());
        List<Interval> targetIntervals = new ArrayList<>();
        //noinspection Convert2streamapi
        for (Iterator<Interval> it : targetIterators) {
            targetIntervals.add(it.next());
        }
        while (true) {
            Interval interval = last(targetIntervals);
            int matchIndex = -1;
            boolean done = true;
            for (int i = 0; i < targetIntervals.size() - 1; i++) {
                if (skipIndices.contains(i)) continue;
                Interval targetInterval = targetIntervals.get(i);
                if (interval.disjoint(targetInterval)) {
                    skipIndices.add(i);
                } else {
                    if (matchIndex != -1) {
                        done = false;
                        break;
                    }
                    matchIndex = i;
                }
            }
            if (done) {
                if (matchIndex == -1) {
                    throw new IllegalArgumentException("No element of targets is equal to this. this: " +
                            this + ", targets: " + targets);
                }
                return matchIndex;
            }
            int largestDiameterIndex = largestDiameterIndex(targetIntervals, skipIndices);
            targetIntervals.set(largestDiameterIndex, targetIterators.get(largestDiameterIndex).next());
        }
    }

    /**
     * A helper function that lifts functions from {@code Rational} to {@code T} to functions from {@code Real} to
     * {@code T}, assuming that the latter can be computed by repeatedly applying the former to increasingly-accurate
     * interval bounds on the {@code Real} argument until f(lower) is equal to f(upper). The function must not have any
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
     * interval bounds on the {@code Real} argument until f(lower) is equal to f(upper). The function must not have any
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
     * A helper function that lifts functions from {@code Rational} to {@code T} to functions from {@code Real} to
     * {@code T}, assuming that the latter can be computed by repeatedly applying the former to increasingly-accurate
     * interval bounds on the {@code Real} argument until f(lower) is equal to f(upper), and with the restriction that
     * the argument must be positive. The function must have not any null values. If {@code this} is not exact and the
     * function is discontinuous at {@code this}—that is, if f(lower) will never equal f(upper) no matter how accurate
     * the approximation–then this method will loop forever. To prevent this behavior, use
     * {@link Real#limitValuePositiveArgument(Function, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may be any positive {@code Real}.</li>
     *  <li>{@code f} must be defined for all positive {@code Rational}s and must never return null or throw an
     *  exception (except for nonpositive arguments).</li>
     *  <li>{@code this} must be positive and either exact or {@code f} continuous at {@code this}.</li>
     * </ul>
     *
     * @param f a function from positive {@code Rational}s to {@code T}
     * @param <T> the type of the result of {@code f}
     * @return {@code f} applied to {@code this}
     */
    private <T> T limitValuePositiveArgumentUnsafe(@NotNull Function<Rational, T> f) {
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
            if (upper.signum() != 1) {
                throw new ArithmeticException("this must be positive. Invalid this: " + this);
            }
            if (lower.signum() == 1) {
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
            }
            a = as.next();
        }
    }

    /**
     * A helper function that lifts functions from {@code Rational} to {@code T} to functions from {@code Real} to
     * {@code T}, assuming that the latter can be computed by repeatedly applying the former to increasingly-accurate
     * interval bounds on the {@code Real} argument until f(lower) is equal to f(upper), and with the restriction that
     * the argument must be positive. The function must not have any null values. If {@code this} is not exact and the
     * function is discontinuous at {@code this}—that is, if f(lower) will never equal f(upper) no matter how accurate
     * the approximation–then this method will give up and return empty once the approximating interval's diameter is
     * less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any positive {@code Real}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>{@code f} must be defined for all positive {@code Rational}s and must never return null or throw an
     *  exception (except for nonpositive arguments).</li>
     * </ul>
     *
     * @param f a function from positive {@code Rational}s to {@code T}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @param <T> the type of the result of {@code f}
     * @return {@code f} applied to {@code this}
     */
    private @NotNull <T> Optional<T> limitValuePositiveArgument(
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
            if (upper.signum() != 1) {
                throw new ArithmeticException("this must be positive. Invalid this: " + this);
            }
            if (lower.signum() == 1) {
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
                previousLower = lower;
                previousUpper = upper;
            }
            if (Ordering.lt(upper.subtract(lower), resolution)) {
                return Optional.empty();
            }
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
     *  <li>{@code roundingMode} cannot be null.</li>
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
     *  <li>{@code roundingMode} cannot be null.</li>
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
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}, rounded
     */
    public @NotNull Optional<BigInteger> bigIntegerValue(
            @NotNull RoundingMode roundingMode,
            @NotNull Rational resolution
    ) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(rational.get().bigIntegerValue(roundingMode));
        }
        if (roundingMode == RoundingMode.UNNECESSARY) {
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
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
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
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
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
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
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
            throw new ArithmeticException("this must be an exact integer. Invalid this: " + this);
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
     *  <li>{@code this} cannot be negative, zero, or an integer power of two that is fuzzy on the right.</li>
     *  <li>The result is an integer power of two. (That is, it is equal to 2<sup>p</sup> for some integer p, but the
     *  result itself is not necessarily an integer.)</li>
     * </ul>
     *
     * @return the smallest integer power of 2 greater than or equal to {@code this}.
     */
    public @NotNull BinaryFraction roundUpToIntegerPowerOfTwoUnsafe() {
        //noinspection ConstantConditions
        return limitValuePositiveArgumentUnsafe(Rational::roundUpToPowerOfTwo);
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
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return the smallest integer power of 2 greater than or equal to {@code this}.
     */
    public @NotNull Optional<BinaryFraction> roundUpToIntegerPowerOfTwo(@NotNull Rational resolution) {
        return limitValuePositiveArgument(Rational::roundUpToPowerOfTwo, resolution);
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
        //noinspection ConstantConditions
        return limitValuePositiveArgumentUnsafe(Rational::binaryExponent);
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
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return ⌊{@code log<sub>2</sub>this}⌋
     */
    public @NotNull Optional<Integer> binaryExponent(@NotNull Rational resolution) {
        return limitValuePositiveArgument(Rational::binaryExponent, resolution);
    }

    /**
     * Determines whether {@code this} is exact and exactly equal to some {@code float}. If true, the {@code float} may
     * be found using {@link Real#floatValueExact()}.
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
     * may be found using {@link Real#doubleValueExact()}.
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

    /**
     * Rounds {@code this} to a {@code float}. The details of the rounding are specified by {@code roundingMode}. If
     * {@code this} is a fuzzy and exactly equal to a {@code float} or a fuzzy and exactly between two adjacent
     * {@code float}s, this method may loop forever, depending on the rounding mode and the nature of the fuzziness. To
     * prevent this behavior, use {@link Real#floatValue(RoundingMode, Rational)} instead.
     * <ul>
     *  <li>{@code RoundingMode.UNNECESSARY}: If {@code this} is exactly equal to a {@code float}, that {@code float}
     *  is returned. Otherwise, an {@link java.lang.ArithmeticException} is thrown. If {@code this} is zero, positive
     *  zero is returned. Negative zero, {@code Infinity}, and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.FLOOR}: The largest {@code float} less than or equal to {@code this} is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Float.MIN_VALUE}, positive zero is returned.
     *  If {@code this} is less than –{@code Float.MAX_VALUE}, {@code -Infinity} is returned. If {@code this} is
     *  greater than or equal to {@code Float.MAX_VALUE}, {@code Float.MAX_VALUE} is returned. Negative zero and
     *  {@code Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.CEILING}: The smallest {@code float} greater than or equal to {@code this} is returned.
     *  If {@code this} is equal to zero, positive zero is returned. If {@code this} is less than zero and greater than
     *  –{@code Float.MIN_VALUE}, negative zero is returned. If {@code this} is greater than {@code Float.MAX_VALUE},
     *  Infinity is returned. If {@code this} is less than or equal to –{@code Float.MAX_VALUE},
     *  –{@code Float.MAX_VALUE} is returned. {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.DOWN}: The first {@code float} going from {@code this} to 0 (possibly equal to
     *  {@code this}) is returned. If {@code this} is greater than or equal to zero and less than
     *  {@code Float.MIN_VALUE}, positive zero is returned. If {@code this} is less than zero and greater than
     *  –{@code Float.MIN_VALUE}, negative zero is returned. If {@code this} is greater than or equal to
     *  {@code Float.MAX_VALUE}, {@code Float.MAX_VALUE} is returned. If {@code this} is less than or equal to
     *  –{@code Float.MAX_VALUE}, –{@code Float.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot
     *  be returned.</li>
     *  <li>{@code RoundingMode.UP}: The first {@code float} going from {@code this} to the infinity of the same sign
     *  (possibly equal to {@code this}) is returned. If {@code this} is equal to zero, positive zero is returned. If
     *  {@code this} is greater than {@code Float.MAX_VALUE}, {@code Infinity} is returned. If {@code this} is less
     *  than {@code Float.MIN_VALUE}, {@code -Infinity} is returned. Negative zero cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_DOWN}: If {@code this} is closest to one {@code float}, that {@code float} is
     *  returned. If there are two closest {@code float}s, the one with the lower absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Float.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Float.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than or equal to {@code Float.MAX_VALUE},
     *  {@code Float.MAX_VALUE} is returned. If {@code this} is less than or equal to –{@code Float.MAX_VALUE},
     *  –{@code Float.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_UP}: If {@code this} is closest to one {@code float}, that {@code float} is
     *  returned. If there are two closest {@code float}s, the one with the higher absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Float.MIN_VALUE}/2, positive zero is
     *  returned. If {@code this} is greater than –{@code Float.MIN_VALUE}/2 and less than zero, negative zero is
     *  returned. If {@code this} is greater than {@code Float.MAX_VALUE}, {@code Infinity} is returned. If
     *  {@code this} is less than –{@code Float.MAX_VALUE}, {@code -Infinity} is returned.</li>
     *  <li>{@code RoundingMode.HALF_EVEN}: If {@code this} is closest to one {@code float}, that {@code float} is
     *  returned. If there are two closest {@code float}s, the one with the unset lowest-order bit is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Float.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Float.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than {@code Float.MAX_VALUE}, {@code Infinity} is
     *  returned. If {@code this} is less than –{@code Float.MAX_VALUE}, {@code -Infinity} is returned.</li>
     * </ul>
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code roundingMode} cannot be null.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UP}, {@code this} cannot be equal to a positive
     *  {@code float} and be fuzzy on the right, a negative {@code float} and fuzzy on the left, or a fuzzy zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#DOWN}, {@code this} cannot equal to a positive
     *  {@code float} and be fuzzy on the left, a negative {@code float} and fuzzy on the right, or a fuzzy zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#CEILING}, {@code this} cannot equal a
     *  {@code float} and be fuzzy on the right, and cannot be a fuzzy zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#FLOOR}, {@code this} cannot equal a {@code float}
     *  and be fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_UP}, {@code this} cannot be halfway between
     *  two adjacent non-negative {@code float}s and be fuzzy on the left, halfway between two adjacent non-positive
     *  {@code float}s and fuzzy on the right, or a zero and fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_DOWN}, {@code this} cannot be halfway between
     *  two adjacent non-positive {@code float}s and be fuzzy on the right, halfway between two adjacent non-positive
     *  {@code float}s and fuzzy on the left, or a zero fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_EVEN}, and {@code this} this is halfway
     *  between two adjacent {@code float}s, then one of the {@code float}s will have a one in its least significant
     *  bit, and {@code this} cannot be fuzzy in the direction closest to that {@code float}. It also cannot be a zero
     *  fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UNNECESSARY}, {@code this} must be exact and
     *  exactly equal to a {@code float}.</li>
     *  <li>The result may be any {@code float} except {@code NaN}.</li>
     * </ul>
     *
     * @param roundingMode specifies the details of how to round {@code this}.
     * @return {@code this}, rounded to a {@code float}
     */
    public float floatValueUnsafe(@NotNull RoundingMode roundingMode) {
        if (rational.isPresent()) {
            return rational.get().floatValue(roundingMode);
        }
        if (roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be exact and exactly equal to a" +
                    " float. Invalid this: " + this);
        }
        //noinspection ConstantConditions
        return limitValueUnsafe(r -> r.floatValue(roundingMode));
    }

    /**
     * Rounds {@code this} to a {@code float}. The details of the rounding are specified by {@code roundingMode}. If
     * {@code this} is a fuzzy and exactly equal to a {@code float} or a fuzzy and exactly between two adjacent
     * {@code float}s, this method will give up and return empty once the approximating interval's diameter is less
     * than the specified resolution.
     * <ul>
     *  <li>{@code RoundingMode.UNNECESSARY}: If {@code this} is exactly equal to a {@code float}, that {@code float}
     *  is returned. Otherwise, an {@link java.lang.ArithmeticException} is thrown. If {@code this} is zero, positive
     *  zero is returned. Negative zero, {@code Infinity}, and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.FLOOR}: The largest {@code float} less than or equal to {@code this} is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Float.MIN_VALUE}, positive zero is returned.
     *  If {@code this} is less than –{@code Float.MAX_VALUE}, {@code -Infinity} is returned. If {@code this} is
     *  greater than or equal to {@code Float.MAX_VALUE}, {@code Float.MAX_VALUE} is returned. Negative zero and
     *  {@code Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.CEILING}: The smallest {@code float} greater than or equal to {@code this} is returned.
     *  If {@code this} is equal to zero, positive zero is returned. If {@code this} is less than zero and greater than
     *  –{@code Float.MIN_VALUE}, negative zero is returned. If {@code this} is greater than {@code Float.MAX_VALUE},
     *  Infinity is returned. If {@code this} is less than or equal to –{@code Float.MAX_VALUE},
     *  –{@code Float.MAX_VALUE} is returned. {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.DOWN}: The first {@code float} going from {@code this} to 0 (possibly equal to
     *  {@code this}) is returned. If {@code this} is greater than or equal to zero and less than
     *  {@code Float.MIN_VALUE}, positive zero is returned. If {@code this} is less than zero and greater than
     *  –{@code Float.MIN_VALUE}, negative zero is returned. If {@code this} is greater than or equal to
     *  {@code Float.MAX_VALUE}, {@code Float.MAX_VALUE} is returned. If {@code this} is less than or equal to
     *  –{@code Float.MAX_VALUE}, –{@code Float.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot
     *  be returned.</li>
     *  <li>{@code RoundingMode.UP}: The first {@code float} going from {@code this} to the infinity of the same sign
     *  (possibly equal to {@code this}) is returned. If {@code this} is equal to zero, positive zero is returned. If
     *  {@code this} is greater than {@code Float.MAX_VALUE}, {@code Infinity} is returned. If {@code this} is less
     *  than {@code Float.MIN_VALUE}, {@code -Infinity} is returned. Negative zero cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_DOWN}: If {@code this} is closest to one {@code float}, that {@code float} is
     *  returned. If there are two closest {@code float}s, the one with the lower absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Float.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Float.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than or equal to {@code Float.MAX_VALUE},
     *  {@code Float.MAX_VALUE} is returned. If {@code this} is less than or equal to –{@code Float.MAX_VALUE},
     *  –{@code Float.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_UP}: If {@code this} is closest to one {@code float}, that {@code float} is
     *  returned. If there are two closest {@code float}s, the one with the higher absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Float.MIN_VALUE}/2, positive zero is
     *  returned. If {@code this} is greater than –{@code Float.MIN_VALUE}/2 and less than zero, negative zero is
     *  returned. If {@code this} is greater than {@code Float.MAX_VALUE}, {@code Infinity} is returned. If
     *  {@code this} is less than –{@code Float.MAX_VALUE}, {@code -Infinity} is returned.</li>
     *  <li>{@code RoundingMode.HALF_EVEN}: If {@code this} is closest to one {@code float}, that {@code float} is
     *  returned. If there are two closest {@code float}s, the one with the unset lowest-order bit is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Float.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Float.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than {@code Float.MAX_VALUE}, {@code Infinity} is
     *  returned. If {@code this} is less than –{@code Float.MAX_VALUE}, {@code -Infinity} is returned.</li>
     * </ul>
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code roundingMode} cannot be null.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UNNECESSARY}, {@code this} must be exact and
     *  exactly equal to a {@code float}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result may be empty or any {@code float} except {@code NaN}.</li>
     * </ul>
     *
     * @param roundingMode specifies the details of how to round {@code this}.
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}, rounded to a {@code float}
     */
    public @NotNull Optional<Float> floatValue(@NotNull RoundingMode roundingMode, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(rational.get().floatValue(roundingMode));
        }
        if (roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be exact and exactly equal to a" +
                    " float. Invalid this: " + this);
        }
        return limitValue(r -> r.floatValue(roundingMode), resolution);
    }

    /**
     * Rounds {@code this} to a {@code float} using {@code RoundingMode.HALF_EVEN}. If {@code this} is closest to one
     * {@code float}, that {@code float} is returned. If there are two closest {@code float}s, the one with the unset
     * lowest-order bit is returned. If {@code this} is greater than or equal to zero and less than or equal to
     * {@code Float.MIN_VALUE}/2, positive zero is returned. If {@code this} is greater than or equal to
     * –{@code Float.MIN_VALUE}/2 and less than zero, negative zero is returned. If {@code this} is greater than
     * {@code Float.MAX_VALUE}, Infinity is returned. If {@code this} is less than –{@code Float.MAX_VALUE},
     * {@code -Infinity} is returned. If {@code this} is fuzzy and exactly between two adjacent {@code float}s, this
     * method may loop forever, depending on the nature of the fuzziness. To prevent this behavior, use
     * {@link Real#floatValue(Rational)} instead.
     *
     * <ul>
     *  <li>If {@code this} this is halfway between two adjacent {@code float}s, then one of the {@code float}s will
     *  have a one in its least significant bit, and {@code this} cannot be fuzzy in the direction closest to that
     *  {@code float}. However, it may be fuzzy and equal to a {@code float}.</li>
     *  <li>The result may be any {@code float}.</li>
     * </ul>
     *
     * @return {@code this}, rounded to a {@code float}
     */
    public float floatValueUnsafe() {
        return floatValueUnsafe(RoundingMode.HALF_EVEN);
    }

    /**
     * Rounds {@code this} to a {@code float} using {@code RoundingMode.HALF_EVEN}. If {@code this} is closest to one
     * {@code float}, that {@code float} is returned. If there are two closest {@code float}s, the one with the unset
     * lowest-order bit is returned. If {@code this} is greater than or equal to zero and less than or equal to
     * {@code Float.MIN_VALUE}/2, positive zero is returned. If {@code this} is greater than or equal to
     * –{@code Float.MIN_VALUE}/2 and less than zero, negative zero is returned. If {@code this} is greater than
     * {@code Float.MAX_VALUE}, Infinity is returned. If {@code this} is less than –{@code Float.MAX_VALUE},
     * {@code -Infinity} is returned. If {@code this} is fuzzy and exactly between two adjacent {@code float}s, this
     * method will give up and return empty once the approximating interval's diameter is less than the specified
     * resolution.
     *
     * <ul>
     *  <li>If {@code this} may be any {@code Real}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result may be any {@code float}.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}, rounded to a {@code float}
     */
    public @NotNull Optional<Float> floatValue(@NotNull Rational resolution) {
        return floatValue(RoundingMode.HALF_EVEN, resolution);
    }

    /**
     * Returns a {@code float} exactly equal to {@code this}. Throws an {@code ArithmeticException} if {@code this} is
     * not exact and exactly equal to a {@code float}.
     *
     * <ul>
     *  <li>{@code this} must be exact and equal to a {@code float}.</li>
     *  <li>The result is not {@code NaN}, infinite, or negative 0.</li>
     * </ul>
     *
     * @return {@code this}, in {@code float} form
     */
    public float floatValueExact() {
        if (rational.isPresent()) {
            return rational.get().floatValueExact();
        } else {
            throw new ArithmeticException("this must be exact and equal to a float. Invalid this: " + this);
        }
    }

    /**
     * Rounds {@code this} to a {@code double}. The details of the rounding are specified by {@code roundingMode}. If
     * {@code this} is a fuzzy and exactly equal to a {@code double} or a fuzzy and exactly between two adjacent
     * {@code double}s, this method may loop forever, depending on the rounding mode and the nature of the fuzziness.
     * To prevent this behavior, use {@link Real#doubleValue(RoundingMode, Rational)} instead.
     * <ul>
     *  <li>{@code RoundingMode.UNNECESSARY}: If {@code this} is exactly equal to a {@code double}, that {@code double}
     *  is returned. Otherwise, an {@link java.lang.ArithmeticException} is thrown. If {@code this} is zero, positive
     *  zero is returned. Negative zero, {@code Infinity}, and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.FLOOR}: The largest {@code double} less than or equal to {@code this} is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Double.MIN_VALUE}, positive zero is
     *  returned. If {@code this} is less than –{@code Double.MAX_VALUE}, {@code -Infinity} is returned. If
     *  {@code this} is greater than or equal to {@code Double.MAX_VALUE}, {@code Double.MAX_VALUE} is returned.
     *  Negative zero and {@code Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.CEILING}: The smallest {@code double} greater than or equal to {@code this} is
     *  returned. If {@code this} is equal to zero, positive zero is returned. If {@code this} is less than zero and
     *  greater than –{@code Double.MIN_VALUE}, negative zero is returned. If {@code this} is greater than
     *  {@code Double.MAX_VALUE}, Infinity is returned. If {@code this} is less than or equal to
     *  –{@code Double.MAX_VALUE}, –{@code Double.MAX_VALUE} is returned. {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.DOWN}: The first {@code double} going from {@code this} to 0 (possibly equal to
     *  {@code this}) is returned. If {@code this} is greater than or equal to zero and less than
     *  {@code Double.MIN_VALUE}, positive zero is returned. If {@code this} is less than zero and greater than
     *  –{@code Double.MIN_VALUE}, negative zero is returned. If {@code this} is greater than or equal to
     *  {@code Double.MAX_VALUE}, {@code Double.MAX_VALUE} is returned. If {@code this} is less than or equal to
     *  –{@code Double.MAX_VALUE}, –{@code Double.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot
     *  be returned.</li>
     *  <li>{@code RoundingMode.UP}: The first {@code double} going from {@code this} to the infinity of the same sign
     *  (possibly equal to {@code this}) is returned. If {@code this} is equal to zero, positive zero is returned. If
     *  {@code this} is greater than {@code Double.MAX_VALUE}, {@code Infinity} is returned. If {@code this} is less
     *  than {@code Double.MIN_VALUE}, {@code -Infinity} is returned. Negative zero cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_DOWN}: If {@code this} is closest to one {@code double}, that {@code double} is
     *  returned. If there are two closest {@code double}s, the one with the lower absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Double.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Double.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than or equal to {@code Double.MAX_VALUE},
     *  {@code Double.MAX_VALUE} is returned. If {@code this} is less than or equal to –{@code Double.MAX_VALUE},
     *  –{@code Double.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_UP}: If {@code this} is closest to one {@code double}, that {@code double} is
     *  returned. If there are two closest {@code double}s, the one with the higher absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Double.MIN_VALUE}/2, positive zero is
     *  returned. If {@code this} is greater than –{@code Double.MIN_VALUE}/2 and less than zero, negative zero is
     *  returned. If {@code this} is greater than {@code Double.MAX_VALUE}, {@code Infinity} is returned. If
     *  {@code this} is less than –{@code Double.MAX_VALUE}, {@code -Infinity} is returned.</li>
     *  <li>{@code RoundingMode.HALF_EVEN}: If {@code this} is closest to one {@code double}, that {@code double} is
     *  returned. If there are two closest {@code double}s, the one with the unset lowest-order bit is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Double.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Double.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than {@code Double.MAX_VALUE}, {@code Infinity} is
     *  returned. If {@code this} is less than –{@code Double.MAX_VALUE}, {@code -Infinity} is returned.</li>
     * </ul>
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code roundingMode} cannot be null.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UP}, {@code this} cannot be equal to a positive
     *  {@code double} and be fuzzy on the right, a negative {@code double} and fuzzy on the left, or a fuzzy
     *  zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#DOWN}, {@code this} cannot equal to a positive
     *  {@code double} and be fuzzy on the left, a negative {@code double} and fuzzy on the right, or a fuzzy
     *  zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#CEILING}, {@code this} cannot equal a
     *  {@code double} and be fuzzy on the right, and cannot be a fuzzy zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#FLOOR}, {@code this} cannot equal a {@code double}
     *  and be fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_UP}, {@code this} cannot be halfway between
     *  two adjacent non-negative {@code double}s and be fuzzy on the left, halfway between two adjacent non-positive
     *  {@code double}s and fuzzy on the right, or a zero and fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_DOWN}, {@code this} cannot be halfway between
     *  two adjacent non-positive {@code double}s and be fuzzy on the right, halfway between two adjacent non-positive
     *  {@code double}s and fuzzy on the left, or a zero fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_EVEN}, and {@code this} this is halfway
     *  between two adjacent {@code double}s, then one of the {@code double}s will have a one in its least significant
     *  bit, and {@code this} cannot be fuzzy in the direction closest to that {@code double}. It also cannot be a zero
     *  fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UNNECESSARY}, {@code this} must be exact and
     *  exactly equal to a {@code double}.</li>
     *  <li>The result may be any {@code double} except {@code NaN}.</li>
     * </ul>
     *
     * @param roundingMode specifies the details of how to round {@code this}.
     * @return {@code this}, rounded to a {@code double}
     */
    public double doubleValueUnsafe(@NotNull RoundingMode roundingMode) {
        if (rational.isPresent()) {
            return rational.get().doubleValue(roundingMode);
        }
        if (roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be exact and exactly equal to a" +
                    " double. Invalid this: " + this);
        }
        //noinspection ConstantConditions
        return limitValueUnsafe(r -> r.doubleValue(roundingMode));
    }

    /**
     * Rounds {@code this} to a {@code double}. The details of the rounding are specified by {@code roundingMode}. If
     * {@code this} is a fuzzy and exactly equal to a {@code double} or a fuzzy and exactly between two adjacent
     * {@code double}s, this method will give up and return empty once the approximating interval's diameter is less
     * than the specified resolution.
     * <ul>
     *  <li>{@code RoundingMode.UNNECESSARY}: If {@code this} is exactly equal to a {@code double}, that {@code double}
     *  is returned. Otherwise, an {@link java.lang.ArithmeticException} is thrown. If {@code this} is zero, positive
     *  zero is returned. Negative zero, {@code Infinity}, and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.FLOOR}: The largest {@code double} less than or equal to {@code this} is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Double.MIN_VALUE}, positive zero is
     *  returned. If {@code this} is less than –{@code Double.MAX_VALUE}, {@code -Infinity} is returned. If
     *  {@code this} is greater than or equal to {@code Double.MAX_VALUE}, {@code Double.MAX_VALUE} is returned.
     *  Negative zero and {@code Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.CEILING}: The smallest {@code double} greater than or equal to {@code this} is
     *  returned. If {@code this} is equal to zero, positive zero is returned. If {@code this} is less than zero and
     *  greater than –{@code Double.MIN_VALUE}, negative zero is returned. If {@code this} is greater than
     *  {@code Double.MAX_VALUE}, Infinity is returned. If {@code this} is less than or equal to
     *  –{@code Double.MAX_VALUE}, –{@code Double.MAX_VALUE} is returned. {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.DOWN}: The first {@code double} going from {@code this} to 0 (possibly equal to
     *  {@code this}) is returned. If {@code this} is greater than or equal to zero and less than
     *  {@code Double.MIN_VALUE}, positive zero is returned. If {@code this} is less than zero and greater than
     *  –{@code Double.MIN_VALUE}, negative zero is returned. If {@code this} is greater than or equal to
     *  {@code Double.MAX_VALUE}, {@code Double.MAX_VALUE} is returned. If {@code this} is less than or equal to
     *  –{@code Double.MAX_VALUE}, –{@code Double.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot
     *  be returned.</li>
     *  <li>{@code RoundingMode.UP}: The first {@code double} going from {@code this} to the infinity of the same sign
     *  (possibly equal to {@code this}) is returned. If {@code this} is equal to zero, positive zero is returned. If
     *  {@code this} is greater than {@code Double.MAX_VALUE}, {@code Infinity} is returned. If {@code this} is less
     *  than {@code Double.MIN_VALUE}, {@code -Infinity} is returned. Negative zero cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_DOWN}: If {@code this} is closest to one {@code double}, that {@code double} is
     *  returned. If there are two closest {@code double}s, the one with the lower absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Double.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Double.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than or equal to {@code Double.MAX_VALUE},
     *  {@code Double.MAX_VALUE} is returned. If {@code this} is less than or equal to –{@code Double.MAX_VALUE},
     *  –{@code Double.MAX_VALUE} is returned. {@code Infinity} and {@code -Infinity} cannot be returned.</li>
     *  <li>{@code RoundingMode.HALF_UP}: If {@code this} is closest to one {@code double}, that {@code double} is
     *  returned. If there are two closest {@code double}s, the one with the higher absolute value is returned. If
     *  {@code this} is greater than or equal to zero and less than {@code Double.MIN_VALUE}/2, positive zero is
     *  returned. If {@code this} is greater than –{@code Double.MIN_VALUE}/2 and less than zero, negative zero is
     *  returned. If {@code this} is greater than {@code Double.MAX_VALUE}, {@code Infinity} is returned. If
     *  {@code this} is less than –{@code Double.MAX_VALUE}, {@code -Infinity} is returned.</li>
     *  <li>{@code RoundingMode.HALF_EVEN}: If {@code this} is closest to one {@code double}, that {@code double} is
     *  returned. If there are two closest {@code double}s, the one with the unset lowest-order bit is returned. If
     *  {@code this} is greater than or equal to zero and less than or equal to {@code Double.MIN_VALUE}/2, positive
     *  zero is returned. If {@code this} is greater than or equal to –{@code Double.MIN_VALUE}/2 and less than zero,
     *  negative zero is returned. If {@code this} is greater than {@code Double.MAX_VALUE}, {@code Infinity} is
     *  returned. If {@code this} is less than –{@code Double.MAX_VALUE}, {@code -Infinity} is returned.</li>
     * </ul>
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code roundingMode} cannot be null.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UNNECESSARY}, {@code this} must be exact and
     *  exactly equal to a {@code double}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result may be empty or any {@code double} except {@code NaN}.</li>
     * </ul>
     *
     * @param roundingMode specifies the details of how to round {@code this}.
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}, rounded to a {@code double}
     */
    public @NotNull Optional<Double> doubleValue(@NotNull RoundingMode roundingMode, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(rational.get().doubleValue(roundingMode));
        }
        if (roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be exact and exactly equal to a" +
                    " double. Invalid this: " + this);
        }
        return limitValue(r -> r.doubleValue(roundingMode), resolution);
    }

    /**
     * Rounds {@code this} to a {@code double} using {@code RoundingMode.HALF_EVEN}. If {@code this} is closest to one
     * {@code double}, that {@code double} is returned. If there are two closest {@code double}s, the one with the
     * unset lowest-order bit is returned. If {@code this} is greater than or equal to zero and less than or equal to
     * {@code Double.MIN_VALUE}/2, positive zero is returned. If {@code this} is greater than or equal to
     * –{@code Double.MIN_VALUE}/2 and less than zero, negative zero is returned. If {@code this} is greater than
     * {@code Double.MAX_VALUE}, Infinity is returned. If {@code this} is less than –{@code Double.MAX_VALUE},
     * {@code -Infinity} is returned. If {@code this} is fuzzy and exactly between two adjacent {@code double}s, this
     * method may loop forever, depending on the nature of the fuzziness. To prevent this behavior, use
     * {@link Real#doubleValue(Rational)} instead.
     *
     * <ul>
     *  <li>If {@code this} this is halfway between two adjacent {@code double}s, then one of the {@code double}s will
     *  have a one in its least significant bit, and {@code this} cannot be fuzzy in the direction closest to that
     *  {@code double}. However, it may be fuzzy and equal to a {@code double}.</li>
     *  <li>The result may be any {@code double}.</li>
     * </ul>
     *
     * @return {@code this}, rounded to a {@code double}
     */
    public double doubleValueUnsafe() {
        return doubleValueUnsafe(RoundingMode.HALF_EVEN);
    }

    /**
     * Rounds {@code this} to a {@code double} using {@code RoundingMode.HALF_EVEN}. If {@code this} is closest to one
     * {@code double}, that {@code double} is returned. If there are two closest {@code double}s, the one with the
     * unset lowest-order bit is returned. If {@code this} is greater than or equal to zero and less than or equal to
     * {@code Double.MIN_VALUE}/2, positive zero is returned. If {@code this} is greater than or equal to
     * –{@code Double.MIN_VALUE}/2 and less than zero, negative zero is returned. If {@code this} is greater than
     * {@code Double.MAX_VALUE}, Infinity is returned. If {@code this} is less than –{@code Double.MAX_VALUE},
     * {@code -Infinity} is returned. If {@code this} is fuzzy and exactly between two adjacent {@code double}s, this
     * method will give up and return empty once the approximating interval's diameter is less than the specified
     * resolution.
     *
     * <ul>
     *  <li>If {@code this} may be any {@code Real}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result may be any {@code double}.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}, rounded to a {@code double}
     */
    public @NotNull Optional<Double> doubleValue(@NotNull Rational resolution) {
        return doubleValue(RoundingMode.HALF_EVEN, resolution);
    }

    /**
     * Returns a {@code double} exactly equal to {@code this}. Throws an {@code ArithmeticException} if {@code this} is
     * not exact and exactly equal to a {@code double}.
     *
     * <ul>
     *  <li>{@code this} must be exact and equal to a {@code double}.</li>
     *  <li>The result is not {@code NaN}, infinite, or negative 0.</li>
     * </ul>
     *
     * @return {@code this}, in {@code double} form
     */
    public double doubleValueExact() {
        if (rational.isPresent()) {
            return rational.get().doubleValueExact();
        } else {
            throw new ArithmeticException("this must be exact and equal to a double. Invalid this: " + this);
        }
    }

    /**
     * Rounds {@code this} to a {@link java.math.BigDecimal} with a specified rounding mode (see documentation for
     * {@code java.math.RoundingMode} for details) and with a specified precision (number of significant digits), or
     * to full precision if {@code precision} is 0. If {@code this} is fuzzy, this method may loop forever,
     * depending on the rounding mode and the nature of the fuzziness. To prevent this behavior, use
     * {@link Real#bigDecimalValueByPrecision(int, RoundingMode, Rational)} instead.
     *
     * Define a {@code Real} to be n-precision-alpha-borderline if its decimal expansion is terminating and it has at
     * most n significant figures after removing all trailing zeros. Define a {@code Real} to be
     * n-precision-beta-borderline if its decimal expansion is terminating, it has exactly n+1 significant figures
     * after removing all trailing zeros, and the least significant digit is 5. 0 is n-precision-alpha-borderline for
     * all non-negative n and not n-precision-beta-borderline for any n.
     *
     * <ul>
     *  <li>{@code this} cannot be a fuzzy zero.</li>
     *  <li>{@code precision} cannot be negative.</li>
     *  <li>{@code roundingMode} cannot be null.</li>
     *  <li>If {@code precision} is 0, then {@code this} must be an exact {@code Real} with a terminating decimal
     *  expansion; that is, its denominator must only have 2 or 5 as prime factors.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UP}, {@code this} cannot be positive,
     *  {@code precision}-precision-alpha-borderline and be fuzzy on the right, or negative,
     *  {@code precision}-precision-alpha-borderline, and fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#DOWN}, {@code this} cannot be positive,
     *  {@code precision}-precision-alpha-borderline and be fuzzy on the left, or negative,
     *  {@code precision}-precision-alpha-borderline, and fuzzy on the right.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#CEILING}, {@code this} cannot be
     *  {@code precision}-precision-alpha-borderline and be fuzzy on the right.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#FLOOR}, {@code this} cannot be
     *  {@code precision}-precision-alpha-borderline and be fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_UP}, {@code this} cannot be positive,
     *  {@code precision}-precision-beta-borderline and be fuzzy on the left, or negative,
     *  {@code precision}-precision-beta-borderline, and fuzzy on the right.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_DOWN}, {@code this} cannot be positive,
     *  {@code precision}-precision-beta-borderline and be fuzzy on the right, or negative,
     *  {@code precision}-precision-beta-borderline, and fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_EVEN}, and {@code this} is
     *  {@code precision}-precision-beta-borderline, then one of its adjacent {@code BigDecimal}s with precision
     *  {@code precision} will have a one in the least significant bit of its unscaled value, and {@code this} cannot
     *  be fuzzy in the direction closest to that {@code BigDecimal}.</li>
     *  <li>If {@code roundingMode} is {@code RoundingMode.UNNECESSARY}, then it must be exact and {@code precision}
     *  must be at least as large as the number of digits in {@code this}'s decimal expansion.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param precision the precision with which to round {@code this}. 0 indicates full precision.
     * @param roundingMode specifies the details of how to round {@code this}.
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull BigDecimal bigDecimalValueByPrecisionUnsafe(int precision, @NotNull RoundingMode roundingMode) {
        if (rational.isPresent()) {
            return rational.get().bigDecimalValueByPrecision(precision, roundingMode);
        }
        if (roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be exact. Invalid this: " + this);
        }
        if (precision == 0) {
            throw new ArithmeticException("If precision is zero, this must be exact. Invalid this: " + this);
        }
        //noinspection ConstantConditions
        return limitValueUnsafe(r -> r.bigDecimalValueByPrecision(precision, roundingMode));
    }

    /**
     * Rounds {@code this} to a {@link java.math.BigDecimal} with a specified rounding mode (see documentation for
     * {@code java.math.RoundingMode} for details) and with a specified precision (number of significant digits), or
     * to full precision if {@code precision} is 0. If {@code this} is fuzzy, depending on the rounding mode and the
     * nature of the fuzziness (see {@link Real#bigDecimalValueByPrecisionUnsafe(int, RoundingMode)}, this method will
     * give up and return empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code precision} cannot be negative.</li>
     *  <li>{@code roundingMode} cannot be null.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If {@code precision} is 0, then {@code this} must be an exact {@code Real} with a terminating decimal
     *  expansion; that is, its denominator must only have 2 or 5 as prime factors.</li>
     *  <li>If {@code roundingMode} is {@code RoundingMode.UNNECESSARY}, then {@code precision} must be at least as
     *  large as the number of digits in {@code this}'s decimal expansion.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param precision the precision with which to round {@code this}. 0 indicates full precision.
     * @param roundingMode specifies the details of how to round {@code this}.
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull Optional<BigDecimal> bigDecimalValueByPrecision(
            int precision,
            @NotNull RoundingMode roundingMode,
            @NotNull Rational resolution
    ) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(rational.get().bigDecimalValueByPrecision(precision, roundingMode));
        }
        if (roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be exact. Invalid this: " + this);
        }
        if (precision == 0) {
            throw new ArithmeticException("If precision is zero, this must be exact. Invalid this: " + this);
        }
        return limitValue(r -> r.bigDecimalValueByPrecision(precision, roundingMode), resolution);
    }

    /**
     * Rounds {@code this} to a {@link java.math.BigDecimal} with a specified rounding mode (see documentation for
     * {@code java.math.RoundingMode} for details) and with a specified scale (number digits after the decimal point).
     * Scale may be negative; for example, {@code 1E+1} has a scale of –1. If {@code this} is fuzzy, this method may
     * loop forever, depending on the rounding mode and the nature of the fuzziness. To prevent this behavior, use
     * {@link Real#bigDecimalValueByScale(int, RoundingMode, Rational)} instead.
     *
     * Define a {@code Real} to be n-scale-alpha-borderline if it is an integer multiple of 10<sup>–n</sup>. Define a
     * {@code Real} to be n-scale-beta-borderline if it is equal to (10k+5)10<sup>–n–1</sup> for some integer k. 0 is
     * n-scale-alpha-borderline for all non-negative n and not n-scale-beta-borderline for any n.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>{@code roundingMode} cannot be null.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UP}, {@code this} cannot be positive,
     *  {@code scale}-scale-alpha-borderline and be fuzzy on the right, or negative,
     *  {@code scale}-scale-alpha-borderline, and fuzzy on the left, or a fuzzy zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#DOWN}, {@code this} cannot be positive,
     *  {@code scale}-scale-alpha-borderline and be fuzzy on the left, or negative,
     *  {@code scale}-scale-alpha-borderline, and fuzzy on the right. It may be a fuzzy zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#CEILING}, {@code this} cannot be
     *  {@code scale}-scale-alpha-borderline and be fuzzy on the right.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#FLOOR}, {@code this} cannot be
     *  {@code scale}-scale-alpha-borderline and be fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_UP}, {@code this} cannot be positive,
     *  {@code scale}-scale-beta-borderline and be fuzzy on the left, or negative,
     *  {@code scale}-scale-beta-borderline, and fuzzy on the right.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_DOWN}, {@code this} cannot be positive,
     *  {@code scale}-scale-beta-borderline and be fuzzy on the right, or negative,
     *  {@code scale}-scale-beta-borderline, and fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_EVEN}, and {@code this} is
     *  {@code scale}-scale-beta-borderline, then one of its adjacent {@code BigDecimal}s with scale {@code scale} will
     *  have a one in the least significant bit of its unscaled value, and {@code this} cannot be fuzzy in the
     *  direction closest to that {@code BigDecimal}.</li>
     *  <li>If {@code roundingMode} is {@code RoundingMode.UNNECESSARY}, then {@code scale} must be at least as large
     *  as the smallest n such that {@code this}×10<sup>n</sup> is an integer (and such an n must exist).</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param scale the scale with which to round {@code this}.
     * @param roundingMode specifies the details of how to round {@code this}.
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull BigDecimal bigDecimalValueByScaleUnsafe(int scale, @NotNull RoundingMode roundingMode) {
        if (rational.isPresent()) {
            return rational.get().bigDecimalValueByScale(scale, roundingMode);
        }
        if (roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be exact. Invalid this: " + this);
        }
        //noinspection ConstantConditions
        return limitValueUnsafe(r -> r.bigDecimalValueByScale(scale, roundingMode));
    }

    /**
     * Rounds {@code this} to a {@link java.math.BigDecimal} with a specified rounding mode (see documentation for
     * {@code java.math.RoundingMode} for details) and with a specified scale (number digits after the decimal point).
     * Scale may be negative; for example, {@code 1E+1} has a scale of –1. If {@code this} is fuzzy, depending on the
     * rounding mode and the nature of the fuzziness (see {@link Real#bigDecimalValueByScaleUnsafe(int, RoundingMode)},
     * this method will give up and return empty once the approximating interval's diameter is less than the specified
     * resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>{@code roundingMode} cannot be null.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If {@code roundingMode} is {@code RoundingMode.UNNECESSARY}, then {@code scale} must be at least as large
     *  as the smallest n such that {@code this}×10<sup>n</sup> is an integer (and such an n must exist).</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param scale the scale with which to round {@code this}.
     * @param roundingMode specifies the details of how to round {@code this}.
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull Optional<BigDecimal> bigDecimalValueByScale(
            int scale,
            @NotNull RoundingMode roundingMode,
            @NotNull Rational resolution
    ) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(rational.get().bigDecimalValueByScale(scale, roundingMode));
        }
        if (roundingMode == RoundingMode.UNNECESSARY) {
            throw new ArithmeticException("If roundingMode is UNNECESSARY, this must be exact. Invalid this: " + this);
        }
        return limitValue(r -> r.bigDecimalValueByScale(scale, roundingMode), resolution);
    }

    /**
     * Rounds {@code this} to a {@link java.math.BigDecimal} using the half-even rounding mode and with a specified
     * precision (number of significant digits), or to full precision if {@code precision} is 0. If {@code this} is
     * fuzzy, this method may loop forever, depending on the nature of the fuzziness. To prevent this behavior, use
     * {@link Real#bigDecimalValueByPrecision(int, Rational)} instead.
     *
     *
     * Define a {@code Real} to be n-precision-beta-borderline if its decimal expansion is terminating, it has exactly
     * n+1 significant figures after removing all trailing zeros, and the least significant digit is 5. 0 is not
     * n-precision-beta-borderline for any n.
     *
     * <ul>
     *  <li>{@code this} cannot be a fuzzy zero.</li>
     *  <li>{@code precision} cannot be negative.</li>
     *  <li>{@code roundingMode} cannot be null.</li>
     *  <li>If {@code precision} is 0, then {@code this} must be an exact {@code Real} with a terminating decimal
     *  expansion; that is, its denominator must only have 2 or 5 as prime factors.</li>
     *  <li>If {@code this} is {@code precision}-precision-beta-borderline, then one of its adjacent
     *  {@code BigDecimal}s with precision {@code precision} will have a one in the least significant bit of its
     *  unscaled value, and {@code this} cannot be fuzzy in the direction closest to that {@code BigDecimal}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param precision the precision with which to round {@code this}. 0 indicates full precision.
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull BigDecimal bigDecimalValueByPrecisionUnsafe(int precision) {
        return bigDecimalValueByPrecisionUnsafe(precision, RoundingMode.HALF_EVEN);
    }

    /**
     * Rounds {@code this} to a {@link java.math.BigDecimal} using the half-even rounding mode and with a specified
     * precision (number of significant digits), or to full precision if {@code precision} is 0. If {@code this} is
     * fuzzy, depending on the nature of the fuzziness (see {@link Real#bigDecimalValueByPrecisionUnsafe(int)}, this
     * method will give up and return empty once the approximating interval's diameter is less than the specified
     * resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code precision} cannot be negative.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If {@code precision} is 0, then {@code this} must be an exact {@code Real} with a terminating decimal
     *  expansion; that is, its denominator must only have 2 or 5 as prime factors.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @param precision the precision with which to round {@code this}. 0 indicates full precision.
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull Optional<BigDecimal> bigDecimalValueByPrecision(int precision, @NotNull Rational resolution) {
        return bigDecimalValueByPrecision(precision, RoundingMode.HALF_EVEN, resolution);
    }

    /**
     * Rounds {@code this} to a {@code BigDecimal} with a specified scale (number digits after the decimal point).
     * Scale may be negative; for example, {@code 1E+1} has a scale of –1. {@code RoundingMode.HALF_EVEN} is used for
     * rounding. If {@code this} is fuzzy, this method may loop forever, depending on the rounding mode and the nature
     * of the fuzziness. To prevent this behavior, use {@link Real#bigDecimalValueByScale(int, Rational)} instead.
     *
     * Define a {@code Real} to be n-scale-alpha-borderline if it is an integer multiple of 10<sup>–n</sup>. Define a
     * {@code Real} to be n-scale-beta-borderline if it is equal to (10k+5)10<sup>–n–1</sup> for some integer k. 0 is
     * n-scale-alpha-borderline for all non-negative n and not n-scale-beta-borderline for any n.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>If  {@code this} is {@code scale}-scale-beta-borderline, then one of its adjacent {@code BigDecimal}s with
     *  scale {@code scale} will have a one in the least significant bit of its unscaled value, and {@code this} cannot
     *  be fuzzy in the direction closest to that {@code BigDecimal}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param scale the scale with which to round {@code this}.
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull BigDecimal bigDecimalValueByScaleUnsafe(int scale) {
        return bigDecimalValueByScaleUnsafe(scale, RoundingMode.HALF_EVEN);
    }

    /**
     * Rounds {@code this} to a {@code BigDecimal} with a specified scale (number digits after the decimal point).
     * Scale may be negative; for example, {@code 1E+1} has a scale of –1. {@code RoundingMode.HALF_EVEN} is used for
     * rounding. If {@code this} is fuzzy, depending on the nature of the fuzziness (see
     * {@link Real#bigDecimalValueByScaleUnsafe(int)}, this method will give up and return empty once the approximating
     * interval's diameter is less than the specified resolution.
     *
     * Define a {@code Real} to be n-scale-alpha-borderline if it is an integer multiple of 10<sup>–n</sup>. Define a
     * {@code Real} to be n-scale-beta-borderline if it is equal to (10k+5)10<sup>–n–1</sup> for some integer k. 0 is
     * n-scale-alpha-borderline for all non-negative n and not n-scale-beta-borderline for any n.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If {@code this} is {@code scale}-scale-beta-borderline, then one of its adjacent {@code BigDecimal}s with
     *  scale {@code scale} will have a one in the least significant bit of its unscaled value, and {@code this} cannot
     *  be fuzzy in the direction closest to that {@code BigDecimal}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param scale the scale with which to round {@code this}.
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull Optional<BigDecimal> bigDecimalValueByScale(int scale, @NotNull Rational resolution) {
        return bigDecimalValueByScale(scale, RoundingMode.HALF_EVEN, resolution);
    }

    /**
     * Returns a {@code BigDecimal} exactly equal to {@code this}. Throws an {@code ArithmeticException} if
     * {@code this} is not exact or cannot be represented as a terminating decimal.
     *
     * <ul>
     *  <li>{@code this} must be an exact {@code Real} with a terminating decimal expansion; that is, it must be
     *  rational and its denominator must only have 2 or 5 as prime factors.</li>
     *  <li>The result is a canonical {@code BigDecimal} (see
     *  {@link mho.wheels.numberUtils.BigDecimalUtils#isCanonical(BigDecimal)}.)</li>
     * </ul>
     *
     * @return {@code this}, in {@code BigDecimal} form
     */
    public @NotNull BigDecimal bigDecimalValueExact() {
        if (rational.isPresent()) {
            return rational.get().bigDecimalValueExact();
        } else {
            throw new ArithmeticException("this must be a Real with a terminating decimal expansion. Invalid this: " +
                    this);
        }
    }

    /**
     * Returns the negative of {@code this}. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result is non-null.</li>
     * </ul>
     *
     * @return –{@code this}
     */
    public @NotNull Real negate() {
        if (rational.isPresent()) {
            Rational r = rational.get();
            return r == Rational.ZERO ? ZERO : new Real(r.negate());
        } else {
            return new Real(map(Interval::negate, intervals));
        }
    }

    /**
     * Returns the absolute value of {@code this}. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result is a non-negative {@code Real}.</li>
     * </ul>
     *
     * @return |{@code this}|
     */
    public @NotNull Real abs() {
        if (rational.isPresent()) {
            Rational r = rational.get();
            return r.signum() == -1 ? new Real(r.negate()) : this;
        }
        return new Real(map(Interval::abs, intervals));
    }

    /**
     * Returns the sign of {@code this}: 1 if positive, –1 if negative, 0 if equal to 0. If {@code this} is a fuzzy
     * zero, this method will loop forever. To prevent this behavior, use {@link Real#signum(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} cannot be a fuzzy zero.</li>
     *  <li>The result is –1, 0, or 1.</li>
     * </ul>
     *
     * @return sgn({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public int signumUnsafe() {
        //noinspection ConstantConditions
        return limitValueUnsafe(Rational::signum);
    }

    /**
     * Returns the sign of {@code this}: 1 if positive, –1 if negative, 0 if equal to 0. If {@code this} is a fuzzy
     * zero, this method will give up and return empty once the approximating interval's diameter is less than the
     * specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result may be empty, –1, 0, or 1.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return sgn({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Optional<Integer> signum(@NotNull Rational resolution) {
        return limitValue(Rational::signum, resolution);
    }

    /**
     * Returns the sum of {@code this} and {@code that}. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Rational} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull Real add(@NotNull Rational that) {
        if (rational.isPresent() && rational.get() == Rational.ZERO) return of(that);
        if (that == Rational.ZERO) return this;
        if (rational.isPresent()) {
            return of(rational.get().add(that));
        }
        Interval ri = Interval.of(that);
        return new Real(map(i -> i.add(ri), intervals));
    }

    /**
     * Given two {@code Real}s, combines their {@code Interval}s using a given function to product a new {@code Real}.
     *
     * <ul>
     *  <li>{@code f} must not return null. As the diameters of its arguments approach zero, the diameter of the result
     *  must approach zero as well.</li>
     *  <li>{@code x} cannot be null.</li>
     *  <li>{@code y} cannot be null.</li>
     *  <li>The result may be any {@code Real}</li>
     * </ul>
     *
     * @param f a function that combines two {@code Interval}s into one
     * @param x the first {@code Real}
     * @param y the second {@code Real}
     * @return a {@code Real} produced from {@code x} and {@code y} using {@code f}
     */
    private static @NotNull Real zipTwoIntervals(
            @NotNull BiFunction<Interval, Interval, Interval> f,
            @NotNull Real x,
            @NotNull Real y
    ) {
        return new Real(() -> new NoRemoveIterator<Interval>() {
            private final @NotNull Iterator<Interval> as = x.intervals.iterator();
            private final @NotNull Iterator<Interval> bs = y.intervals.iterator();
            private Interval a = null;
            private Interval b = null;
            private Optional<Rational> aDiameter = null;
            private Optional<Rational> bDiameter = null;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (a == null) {
                    a = as.next();
                    b = bs.next();
                    aDiameter = a.diameter();
                    bDiameter = b.diameter();
                } else {
                    if (compareDiameters(aDiameter, bDiameter) == LT) {
                        b = bs.next();
                        bDiameter = b.diameter();
                    } else {
                        a = as.next();
                        aDiameter = a.diameter();
                    }
                }
                return f.apply(a, b);
            }
        });
    }

    /**
     * Returns the sum of {@code this} and {@code that}. The result may be fuzzy even if both arguments are clean.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Real} added to {@code this}
     * @return {@code this}+{@code that}
     */
    public @NotNull Real add(@NotNull Real that) {
        if (rational.isPresent() && rational.get() == Rational.ZERO) return that;
        if (that.rational.isPresent() && that.rational.get() == Rational.ZERO) return this;
        if (this == that) return shiftLeft(1);
        if (rational.isPresent()) return that.add(rational.get());
        if (that.rational.isPresent()) return add(that.rational.get());
        return zipTwoIntervals(Interval::add, this, that);
    }

    /**
     * Returns the difference of {@code this} and {@code that}. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Rational} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull Real subtract(@NotNull Rational that) {
        if (that == Rational.ZERO) return this;
        if (rational.isPresent() && rational.get() == Rational.ZERO) return new Real(that.negate());
        if (rational.isPresent()) {
            return of(rational.get().subtract(that));
        }
        Interval ri = Interval.of(that);
        return new Real(map(i -> i.subtract(ri), intervals));
    }

    /**
     * Returns the difference of {@code this} and {@code that}. The result may be fuzzy even if both arguments are
     * clean.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Real} subtracted from {@code this}
     * @return {@code this}–{@code that}
     */
    public @NotNull Real subtract(@NotNull Real that) {
        if (this == that) return ZERO;
        if (rational.isPresent() && rational.get() == Rational.ZERO) return that.negate();
        if (that.rational.isPresent() && that.rational.get() == Rational.ZERO) return this;
        if (rational.isPresent()) return that.subtract(rational.get()).negate();
        if (that.rational.isPresent()) return subtract(that.rational.get());
        return zipTwoIntervals(Interval::subtract, this, that);
    }

    /**
     * Returns the product of {@code this} and {@code that}. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code int} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Real multiply(int that) {
        if (rational.isPresent() && rational.get() == Rational.ZERO || that == 0) return ZERO;
        if (that == 1) return this;
        if (rational.isPresent() && rational.get() == Rational.ONE) return of(that);
        if (rational.isPresent()) {
            return new Real(rational.get().multiply(that));
        }
        return new Real(map(i -> i.multiply(that), intervals));
    }

    /**
     * Returns the product of {@code this} and {@code that}. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code BigInteger} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Real multiply(@NotNull BigInteger that) {
        if (rational.isPresent() && rational.get() == Rational.ZERO || that.equals(BigInteger.ZERO)) return ZERO;
        if (that.equals(BigInteger.ONE)) return this;
        if (rational.isPresent() && rational.get() == Rational.ONE) return of(that);
        if (rational.isPresent()) {
            return new Real(rational.get().multiply(that));
        }
        return new Real(map(i -> i.multiply(that), intervals));
    }

    /**
     * Returns the product of {@code this} and {@code that}. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Rational} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Real multiply(@NotNull Rational that) {
        if (rational.isPresent() && rational.get() == Rational.ZERO || that == Rational.ZERO) return ZERO;
        if (that == Rational.ONE) return this;
        if (rational.isPresent() && rational.get() == Rational.ONE) return of(that);
        if (rational.isPresent()) {
            return new Real(rational.get().multiply(that));
        }
        return new Real(map(i -> i.multiply(that), intervals));
    }

    /**
     * Returns the product of {@code this} and {@code that}. The result may be fuzzy even if both arguments are clean.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Real} {@code this} is multiplied by
     * @return {@code this}×{@code that}
     */
    public @NotNull Real multiply(@NotNull Real that) {
        if (rational.isPresent() && rational.get() == Rational.ZERO ||
                that.rational.isPresent() && that.rational.get() == Rational.ZERO) return ZERO;
        if (rational.isPresent() && rational.get() == Rational.ONE) return that;
        if (that.rational.isPresent() && that.rational.get() == Rational.ONE) return this;
        if (this == that) {
            return powUnsafe(2);
        }
        if (rational.isPresent()) return that.multiply(rational.get());
        if (that.rational.isPresent()) return multiply(that.rational.get());
        return zipTwoIntervals(Interval::multiply, this, that);
    }

    /**
     * Returns the multiplicative inverse of {@code this}. If {@code this} is a fuzzy zero, this method will loop
     * forever. To prevent this behavior, use {@link Real#invert(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} cannot be zero.</li>
     *  <li>The result is a nonzero {@code Real}.</li>
     * </ul>
     *
     * @return 1/{@code this}
     */
    public @NotNull Real invertUnsafe() {
        if (rational.isPresent()) {
            Rational r = rational.get();
            return r == Rational.ONE ? ONE : new Real(r.invert());
        } else {
            signumUnsafe(); // if this is 0, loop forever here instead of returning an invalid Real
            return new Real(map(Interval::invertHull, intervals));
        }
    }

    /**
     * Returns the multiplicative inverse of {@code this}. If {@code this} is a fuzzy zero, this method will give up
     * and return empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} cannot be an exact zero. (If it is a fuzzy zero, an empty {@code Optional} will be
     *  returned.)</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is a nonzero {@code Real}, or empty.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return 1/{@code this}
     */
    public @NotNull Optional<Real> invert(@NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            Rational r = rational.get();
            return Optional.of(r == Rational.ONE ? ONE : new Real(r.invert()));
        } else {
            Optional<Integer> signum = signum(resolution);
            return signum.isPresent() ? Optional.of(new Real(map(Interval::invertHull, intervals))) : Optional.empty();
        }
    }

    /**
     * Returns the quotient of {@code this} and {@code that}. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code int} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull Real divide(int that) {
        if (that == 0) {
            throw new ArithmeticException("that cannot be zero.");
        }
        if (rational.isPresent() && rational.get() == Rational.ZERO) return ZERO;
        if (that == 1) return this;
        if (rational.isPresent()) {
            return new Real(rational.get().divide(that));
        }
        return new Real(map(i -> i.divide(that), intervals));
    }

    /**
     * Returns the quotient of {@code this} and {@code that}. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code BigInteger} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull Real divide(@NotNull BigInteger that) {
        if (that.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("that cannot be zero.");
        }
        if (rational.isPresent() && rational.get() == Rational.ZERO) return ZERO;
        if (that.equals(BigInteger.ONE)) return this;
        if (rational.isPresent()) {
            return new Real(rational.get().divide(that));
        }
        return new Real(map(i -> i.divide(that), intervals));
    }

    /**
     * Returns the quotient of {@code this} and {@code that}. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Rational} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull Real divide(@NotNull Rational that) {
        if (that == Rational.ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        if (rational.isPresent() && rational.get() == Rational.ZERO) return ZERO;
        if (that == Rational.ONE) return this;
        if (rational.isPresent()) {
            return new Real(rational.get().divide(that));
        }
        return new Real(map(i -> i.divide(that), intervals));
    }

    /**
     * Returns the quotient of {@code this} and {@code that}. The result may be fuzzy even if both arguments are clean.
     * If {@code that} is a fuzzy zero, this method will loop forever. To prevent this behavior, use
     * {@link Real#divide(Real, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be zero.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Real} {@code this} is divided by
     * @return {@code this}/{@code that}
     */
    public @NotNull Real divideUnsafe(@NotNull Real that) {
        if (that.rational.isPresent() && that.rational.get() == Rational.ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        that.signumUnsafe(); // if this is 0, loop forever here instead of returning an invalid Real
        if (rational.isPresent() && rational.get() == Rational.ZERO) return ZERO;
        if (this == that) return ONE;
        if (that.rational.isPresent() && that.rational.get() == Rational.ONE) return this;
        if (rational.isPresent()) {
            Rational r = rational.get();
            return r == Rational.ZERO ? ZERO : that.divide(r).invertUnsafe();
        }
        if (that.rational.isPresent()) {
            return divide(that.rational.get());
        }
        return zipTwoIntervals(Interval::divideHull, this, that);
    }

    /**
     * Returns the quotient of {@code this} and {@code that}. The result may be fuzzy even if both arguments are clean.
     * If {@code that} is a fuzzy zero, this method will give up and return empty once the approximating interval's
     * diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>{@code that} cannot be an exact zero. (If it is a fuzzy zero, an empty {@code Optional} will be
     *  returned.)</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param that the {@code Real} {@code this} is divided by
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}/{@code that}
     */
    public @NotNull Optional<Real> divide(@NotNull Real that, @NotNull Rational resolution) {
        if (that.rational.isPresent() && that.rational.get() == Rational.ZERO) {
            throw new ArithmeticException("that cannot be zero.");
        }
        Optional<Integer> thatSignum = that.signum(resolution);
        if (!thatSignum.isPresent()) {
            return Optional.empty();
        }
        if (this == that) return Optional.of(ONE);
        if (rational.isPresent()) {
            Rational r = rational.get();
            return r == Rational.ZERO ? Optional.of(ZERO) : that.divide(rational.get()).invert(resolution);
        }
        if (that.rational.isPresent()) {
            return Optional.of(divide(that.rational.get()));
        }
        return Optional.of(zipTwoIntervals(Interval::divideHull, this, that));
    }

    /**
     * Returns the left shift of {@code this} by {@code bits}; {@code this}×2<sup>{@code bits}</sup>. Negative
     * {@code bits} corresponds to a right shift. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Real}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the number of bits to left-shift by
     * @return {@code this}≪{@code bits}
     */
    public @NotNull Real shiftLeft(int bits) {
        if (rational.isPresent() && rational.get() == Rational.ZERO || bits == 0) return this;
        if (rational.isPresent()) {
            return new Real(rational.get().shiftLeft(bits));
        } else {
            return new Real(map(a -> a.shiftLeft(bits), intervals));
        }
    }

    /**
     * Returns the right shift of {@code this} by {@code bits}; {@code this}×2<sup>–{@code bits}</sup>. Negative
     * {@code bits} corresponds to a left shift. If {@code this} is clean, so is the result.
     *
     * <ul>
     *  <li>{@code this} can be any {@code Real}.</li>
     *  <li>{@code bits} may be any {@code int}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param bits the number of bits to right-shift by
     * @return {@code this}≫{@code bits}
     */
    public @NotNull Real shiftRight(int bits) {
        if (rational.isPresent() && rational.get() == Rational.ZERO || bits == 0) return this;
        if (rational.isPresent()) {
            return new Real(rational.get().shiftRight(bits));
        } else {
            return new Real(map(a -> a.shiftRight(bits), intervals));
        }
    }

    /**
     * Returns the sum of all the {@code Real}s in {@code xs}. If {@code xs} is empty, 0 is returned. The result may be
     * fuzzy even if all elements of the argument are clean.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code Real}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Real}s
     * @return Σxs
     */
    public static @NotNull Real sum(@NotNull List<Real> xs) {
        if (xs.isEmpty()) {
            return ZERO;
        }
        if (any(Objects::isNull, xs)) {
            throw new NullPointerException();
        }
        if (xs.size() == 1) {
            return xs.get(0);
        }
        if (all(Real::isExact, xs)) {
            return new Real(Rational.sum(toList(map(x -> x.rationalValueExact().get(), xs))));
        }
        return new Real(map(Interval::sum, transpose(map(r -> r.intervals, xs))));
    }

    /**
     * Returns the product of all the {@code Real}s in {@code xs}. If {@code xs} is empty, 1 is returned. The result
     * may be fuzzy even if all elements of the argument are clean.
     *
     * <ul>
     *  <li>{@code xs} may not contain any nulls.</li>
     *  <li>The result may be any {@code Real}.</li>
     * </ul>
     *
     * @param xs a {@code List} of {@code Real}s
     * @return Πxs
     */
    public static @NotNull Real product(@NotNull List<Real> xs) {
        if (xs.isEmpty()) {
            return ONE;
        }
        if (any(Objects::isNull, xs)) {
            throw new NullPointerException();
        }
        if (xs.size() == 1) {
            return xs.get(0);
        }
        if (any(x -> x.rational.isPresent() && x.rational.get() == Rational.ZERO, xs)) {
            return ZERO;
        }
        if (all(Real::isExact, xs)) {
            return new Real(Rational.product(toList(map(x -> x.rationalValueExact().get(), xs))));
        }
        return new Real(map(Interval::product, transpose(map(r -> r.intervals, xs))));
    }

    /**
     * Returns the differences between successive {@code Real}s in {@code xs}. If {@code xs} contains a single
     * {@code Real}, an empty {@code Iterable} is returned. {@code xs} cannot be empty. Does not support removal. The
     * results may be fuzzy even if all elements of the argument are clean.
     *
     * <ul>
     *  <li>{@code xs} must not be empty and may not contain any nulls.</li>
     *  <li>The result is does not contain any nulls.</li>
     * </ul>
     *
     * Length is |{@code xs}|–1
     *
     * @param xs an {@code Iterable} of {@code Real}s.
     * @return Δxs
     */
    public static @NotNull Iterable<Real> delta(@NotNull Iterable<Real> xs) {
        if (isEmpty(xs)) {
            throw new IllegalArgumentException("xs must not be empty.");
        }
        if (head(xs) == null) {
            throw new NullPointerException();
        }
        return adjacentPairsWith((x, y) -> y.subtract(x), xs);
    }

    /**
     * Returns {@code this} raised to the power of {@code p}. 0<sup>0</sup> yields 1. If {@code this} is a fuzzy zero
     * and {@code p} is negative, this method will loop forever. To prevent this behavior, use
     * {@link Real#pow(int, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code p} may be any {@code int}.</li>
     *  <li>If {@code p} is negative, {@code this} cannot be 0.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param p the power that {@code this} is raised to
     * @return {@code this}<sup>{@code p}</sup>
     */
    public @NotNull Real powUnsafe(int p) {
        if (p == 0 || rational.isPresent() && rational.get() == Rational.ONE) return ONE;
        if (p == 1) return this;
        if (p < 0) return invertUnsafe().powUnsafe(-p);
        if (rational.isPresent() && rational.get() == Rational.ZERO) return this;
        if (p % 2 == 0 && rational.isPresent() && rational.get().equals(Rational.NEGATIVE_ONE)) return ONE;
        if (rational.isPresent()) {
            return new Real(rational.get().pow(p));
        }
        return new Real(map(a -> a.powHull(p), intervals));
    }

    /**
     * Returns {@code this} raised to the power of {@code p}. 0<sup>0</sup> yields 1. If {@code that} is a fuzzy zero
     * and {@code p} is negative, this method will give up and return empty once the approximating interval's diameter
     * is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code p} may be any {@code int}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If {@code p} is negative, {@code this} cannot be an exact 0. (If it is a fuzzy zero, an empty
     *  {@code Optional} will be returned.)</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param p the power that {@code this} is raised to
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}<sup>{@code p}</sup>
     */
    public @NotNull Optional<Real> pow(int p, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (p == 0 || rational.isPresent() && rational.get() == Rational.ONE) return Optional.of(ONE);
        if (p == 1) return Optional.of(this);
        if (p < 0) return invert(resolution).map(x -> x.powUnsafe(-p));
        if (rational.isPresent() && rational.get() == Rational.ZERO) return Optional.of(this);
        if (p % 2 == 0 && rational.isPresent() && rational.get().equals(Rational.NEGATIVE_ONE)) {
            return Optional.of(ONE);
        }
        if (rational.isPresent()) {
            return Optional.of(new Real(rational.get().pow(p)));
        }
        return Optional.of(new Real(map(a -> a.powHull(p), intervals)));
    }

    /**
     * Given an {@code Iterable} of {@code Interval}s that converge to a {@code Real}, returns that {@code Real}. This
     * method differs from {@link Real#Real(Iterable)} in that the intervals don't have to be ordered such that each
     * interval contains its successor.
     *
     * <ul>
     *  <li>{@code interval}s must be infinite. The diameters of the elements must approach zero. Every element must
     *  take a finite amount of computation time to produce.</li>
     *  <li>Any {@code Real} may be constructed with this constructor.</li>
     * </ul>
     *
     * @param intervals the bounding intervals that define a {@code Real}
     * @return the {@code Real} that {@code intervals} converge to
     */
    public static @NotNull Real forceContainment(@NotNull Iterable<Interval> intervals) {
        return new Real(() -> new NoRemoveIterator<Interval>() {
            private final @NotNull Iterator<Interval> is = intervals.iterator();
            private Interval next = null;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                next = next == null ? is.next() : is.next().intersection(next).get();
                return next;
            }
        });
    }

    /**
     * Returns the {@code r}th root of {@code x}, or {@code x}<sup>1/{@code r}</sup>. If {@code r} is even, the
     * principal (non-negative) root is chosen.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     *  <li>{@code r} cannot be 0.</li>
     *  <li>If {@code r} is negative, {@code x} cannot be zero.</li>
     *  <li>If {@code r} is even, {@code x} cannot be negative.</li>
     *  <li>The result is exact.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @param r the index of the root
     * @return {@code x}<sup>1/{@code r}</sup>
     */
    public static @NotNull Real rootOfRational(@NotNull Rational x, int r) {
        return Algebraic.rootOfRational(x, r).realValue();
    }

    /**
     * Returns the square root of {@code x}. The principal (non-negative) square root is chosen.
     *
     * <ul>
     *  <li>{@code x} cannot be negative.</li>
     *  <li>The result is the square root of a rational number.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return sqrt({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real sqrtOfRational(@NotNull Rational x) {
        return rootOfRational(x, 2);
    }

    /**
     * Returns the cube root of {@code x}.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result is the cube root of a rational number.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return cbrt({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real cbrtOfRational(@NotNull Rational x) {
        return rootOfRational(x, 3);
    }

    /**
     * Returns the {@code r}th root of {@code this}, or {@code this}<sup>1/{@code r}</sup>. If {@code r} is even, the
     * principal (non-negative) root is chosen. If {@code this} is equal to zero, fuzzy on the left, and {@code r} is
     * even, or if {@code this} is equal to zero, fuzzy, and {@code r} is negative, this method will loop forever. To
     * prevent this behavior, use {@link Real#root(int, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code r} cannot be 0.</li>
     *  <li>If {@code r} is negative, {@code this} cannot be zero.</li>
     *  <li>If {@code r} is even, {@code this} cannot be negative or a zero fuzzy on the left.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param r the index of the root
     * @return {@code this}<sup>1/{@code r}</sup>
     */
    public @NotNull Real rootUnsafe(int r) {
        if (r == 0) {
            throw new ArithmeticException("r cannot be zero.");
        }
        if (this == ONE || r == 1) {
            return this;
        }
        if (rational.isPresent()) {
            return rootOfRational(rational.get(), r);
        }
        if (r < 0) {
            return invertUnsafe().rootUnsafe(-r);
        }
        if (this == ZERO) {
            return ZERO;
        }
        boolean rEven = (r & 1) == 0;
        return forceContainment(() -> new NoRemoveIterator<Interval>() {
            private final @NotNull Iterator<Interval> is = intervals.iterator();
            private Interval a;
            private Iterator<Interval> lowerReal = null;
            private Iterator<Interval> upperReal = null;
            private Rational previousDiameter = null;
            private int i = 0;
            {
                do {
                    a = is.next();
                    if (rEven) {
                        Optional<Integer> as = a.signum();
                        if (as.isPresent() && as.get() == -1) {
                            throw new ArithmeticException("If r is even, this cannot be negative. r: " +
                                    r + ", this: " + this);
                        }
                        a = a.abs();
                    }
                } while (!a.isFinitelyBounded());
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (lowerReal == null) {
                    lowerReal = rootOfRational(a.getLower().get(), r).iterator();
                    upperReal = rootOfRational(a.getUpper().get(), r).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                }
                Interval lowerInterval = lowerReal.next();
                Interval upperInterval = upperReal.next();
                Interval nextInterval = lowerInterval.convexHull(upperInterval);
                Rational diameter = nextInterval.diameter().get();
                if (previousDiameter != null && Ordering.gt(diameter, previousDiameter.shiftRight(1))) {
                    a = is.next();
                    if (rEven) {
                        Optional<Integer> as = a.signum();
                        if (as.isPresent() && as.get() == -1) {
                            throw new ArithmeticException("If r is even, this cannot be negative. r: " +
                                    r + ", this: " + this);
                        }
                        a = a.abs();
                    }
                    lowerReal = rootOfRational(a.getLower().get(), r).iterator();
                    upperReal = rootOfRational(a.getUpper().get(), r).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                    lowerInterval = lowerReal.next();
                    upperInterval = upperReal.next();
                    nextInterval = lowerInterval.convexHull(upperInterval);
                    diameter = nextInterval.diameter().get();
                }
                previousDiameter = diameter;
                i++;
                return nextInterval;
            }
        });
    }

    /**
     * Returns the {@code r}th root of {@code this}, or {@code this}<sup>1/{@code r}</sup>. If {@code r} is even, the
     * principal (non-negative) root is chosen. If {@code this} is equal to zero, fuzzy on the left, and {@code r} is
     * even, or if {@code this} is equal to zero, fuzzy, and {@code r} is negative, this method will give up and return
     * empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code r} cannot be 0.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If {@code r} is negative, {@code this} cannot be an exact zero.</li>
     *  <li>If {@code r} is even, {@code this} cannot be negative.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param r the index of the root
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}<sup>1/{@code r}</sup>
     */
    public @NotNull Optional<Real> root(int r, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (r > 0 && (r & 1) == 1) {
            return Optional.of(rootUnsafe(r));
        }
        Optional<Integer> oSign = signum(resolution);
        if (oSign.isPresent()) {
            int sign = oSign.get();
            if (r < 0 && sign == 0) {
                throw new ArithmeticException("If r is negative, this cannot be an exact zero. r: " + r);
            } else if ((r & 1) == 0 && sign == -1) {
                throw new ArithmeticException("If r is even, this cannot be negative. r: " + r + ", this: " + this);
            } else {
                return Optional.of(rootUnsafe(r));
            }
        } else {
            if (r > 0) {
                Optional<Boolean> nonNegative = ge(Rational.ZERO, resolution);
                if (nonNegative.isPresent() && nonNegative.get()) {
                    return Optional.of(rootUnsafe(r));
                }
            }
            return Optional.empty();
        }
    }

    /**
     * Returns the square root of {@code this}. If {@code r} is even, the principal (non-negative) root is chosen. If
     * {@code this} is equal to zero and fuzzy on the left, this method will loop forever. To prevent this behavior,
     * use {@link Real#sqrt(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} cannot be negative or a zero fuzzy on the left.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @return sqrt({@code r})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real sqrtUnsafe() {
        return rootUnsafe(2);
    }

    /**
     * Returns the square root of {@code this}. The principal (non-negative) root is chosen. If {@code this} is equal
     * to zero and fuzzy on the left, this method will give up and return empty once the approximating interval's
     * diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} cannot be negative.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is not negative.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return sqrt({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Optional<Real> sqrt(@NotNull Rational resolution) {
        return root(2, resolution);
    }

    /**
     * Returns the cube root of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return cbrt({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real cbrt() {
        return rootUnsafe(3);
    }

    /**
     * Returns exp({@code x}), or e<sup>{@code x}</sup>, provided that –1≤{@code x}<0.
     *
     * <ul>
     *  <li>{@code x} must be greater than or equal to –1 and less than 0.</li>
     *  <li>The result is e raised to a rational power, and greater than or equal to exp(–1) and less than 1.</li>
     * </ul>
     *
     * @param x a negative {@code Rational} greater than or equal to –1
     * @return exp({@code x})
     */
    @SuppressWarnings("JavaDoc")
    private static @NotNull Real expNeg10(@NotNull Rational x) {
        return new Real(() -> new NoRemoveIterator<Interval>() {
            private @NotNull Rational partialSum = Rational.ZERO;
            private @NotNull BigInteger i = BigInteger.ZERO;
            private @NotNull BigInteger factorial = BigInteger.ONE;
            private @NotNull Rational power = Rational.ONE;
            private boolean first = true;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (first) {
                    first = false;
                } else {
                    i = i.add(BigInteger.ONE);
                    factorial = factorial.multiply(i);
                    power = power.multiply(x);
                }
                partialSum = partialSum.add(power.divide(factorial));
                Rational upper = partialSum;
                i = i.add(BigInteger.ONE);
                factorial = factorial.multiply(i);
                power = power.multiply(x);
                partialSum = partialSum.add(power.divide(factorial));
                return Interval.of(partialSum, upper);
            }
        });
    }

    /**
     * Returns exp({@code x}), or e<sup>{@code x}</sup>.
     *
     * <ul>
     *  <li>The absolute value of {@code x} must be less than 2<sup>32</sup>.</li>
     *  <li>The result is e raised to a rational power. The result is exact.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return exp({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real expOfRational(@NotNull Rational x) {
        if (x == Rational.ZERO) {
            return ONE;
        } else if (x.signum() == -1) {
            if (Ordering.ge(x, Rational.NEGATIVE_ONE)) {
                return expNeg10(x);
            } else {
                int negativeFloor = x.floor().negate().intValueExact();
                return expNeg10(x.divide(negativeFloor)).powUnsafe(negativeFloor);
            }
        } else {
            return expOfRational(x.negate()).invertUnsafe();
        }
    }

    /**
     * Given a {@code Rational x}, returns an {@code Iterable} of bounding intervals of exp({@code x}) that are all
     * finitely bounded.
     *
     * <ul>
     *  <li>The absolute value of {@code x} must be less than 2<sup>32</sup>.</li>
     *  <li>The result is finite bounding intervals e raised to a rational power.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return finite bounding intervals of exp({@code x})
     */
    private static @NotNull Iterable<Interval> expSkipUnbounded(@NotNull Rational x) {
        return dropWhile(a -> !a.isFinitelyBounded(), expOfRational(x));
    }

    /**
     * Returns exp({@code this}), or e<sup>{@code this}</sup>.
     *
     * <ul>
     *  <li>The absolute value of {@code this} must be less than 2<sup>32</sup>.</li>
     *  <li>The result is positive.</li>
     * </ul>
     *
     * @return exp({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real exp() {
        if (rational.isPresent()) {
            return expOfRational(rational.get());
        }
        return forceContainment(() -> new NoRemoveIterator<Interval>() {
            private final @NotNull Iterator<Interval> is = intervals.iterator();
            private Interval a;
            private Iterator<Interval> lowerReal = null;
            private Iterator<Interval> upperReal = null;
            private Rational previousDiameter = null;
            private int i = 0;
            {
                do {
                    a = is.next();
                } while (!a.isFinitelyBounded());
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (lowerReal == null) {
                    lowerReal = expSkipUnbounded(a.getLower().get()).iterator();
                    upperReal = expSkipUnbounded(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                }
                Interval lowerInterval = lowerReal.next();
                Interval upperInterval = upperReal.next();
                Interval nextInterval = lowerInterval.convexHull(upperInterval);
                Rational diameter = nextInterval.diameter().get();
                if (previousDiameter != null && Ordering.gt(diameter, previousDiameter.shiftRight(1))) {
                    a = is.next();
                    lowerReal = expSkipUnbounded(a.getLower().get()).iterator();
                    upperReal = expSkipUnbounded(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                    lowerInterval = lowerReal.next();
                    upperInterval = upperReal.next();
                    nextInterval = lowerInterval.convexHull(upperInterval);
                    diameter = nextInterval.diameter().get();
                }
                previousDiameter = diameter;
                i++;
                return nextInterval;
            }
        });
    }

    /**
     * Returns the natural logarithm of {@code x}, provided that 1<{@code x}<2.
     *
     * <ul>
     *  <li>{@code x} must be greater than 1 and less than 2.</li>
     *  <li>The result is the natural logarithm of a rational number, positive, and less than log(2).</li>
     * </ul>
     *
     * @param x a {@code Rational} strictly between 1 and 2
     * @return log({@code x})
     */
    @SuppressWarnings("JavaDoc")
    private static @NotNull Real log12(@NotNull Rational x) {
        Rational xMinus1 = x.subtract(Rational.ONE);
        return new Real(() -> new NoRemoveIterator<Interval>() {
            private @NotNull Rational partialSum = Rational.ZERO;
            private @NotNull BigInteger i = BigInteger.ZERO;
            private @NotNull Rational power = Rational.ONE;
            private boolean sign = false;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                i = i.add(BigInteger.ONE);
                power = power.multiply(xMinus1);
                sign = !sign;
                Rational term = power.divide(i);
                if (!sign) term = term.negate();
                partialSum = partialSum.add(term);
                Rational upper = partialSum;
                i = i.add(BigInteger.ONE);
                power = power.multiply(xMinus1);
                sign = !sign;
                term = power.divide(i);
                if (!sign) term = term.negate();
                partialSum = partialSum.add(term);
                return Interval.of(partialSum, upper);
            }
        });
    }

    /**
     * Returns the natural logarithm of {@code x}.
     *
     * <ul>
     *  <li>{@code x} must be positive.</li>
     *  <li>The is the natural logarithm of a rational number.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return log({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real logOfRational(@NotNull Rational x) {
        if (x == Rational.ONE) {
            return ZERO;
        }
        if (x.signum() != 1) {
            throw new ArithmeticException("x must be positive. Invalid x: " + x);
        }
        if (x.equals(Rational.TWO)) {
            return LOG_2;
        }
        // log(x) when x ~= 2 converges very slowly, so use log(3/2) + log(2/3 * x) instead
        if (Ordering.le(x.subtract(Rational.TWO).abs(), Rational.TEN.invert())) {
            return logOfRational(Rational.of(3, 2)).add(logOfRational(x.multiply(Rational.of(2, 3))));
        }
        if (Ordering.lt(x, Rational.ONE)) {
            return logOfRational(x.invert()).negate();
        } else if (Ordering.lt(x, Rational.TWO)) {
            return log12(x);
        } else {
            int m = x.binaryExponent();
            return LOG_2.multiply(m).add(logOfRational(x.shiftRight(m)));
        }
    }

    /**
     * Returns the natural logarithm of {@code this}. If {@code this} is equal to zero and fuzzy on the left, this
     * method will loop forever. To prevent this behavior, use {@link Real#log(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} must be positive.</li>
     *  <li>The result is not exact rational unless {@code this} is 1.</li>
     * </ul>
     *
     * @return log({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real logUnsafe() {
        if (this == ONE) {
            return ZERO;
        }
        if (this == E) {
            return ONE;
        }
        if (rational.isPresent()) {
            return logOfRational(rational.get());
        }
        return forceContainment(() -> new NoRemoveIterator<Interval>() {
            private final @NotNull Iterator<Interval> is = intervals.iterator();
            private Interval a;
            private Iterator<Interval> lowerReal = null;
            private Iterator<Interval> upperReal = null;
            private Rational previousDiameter = null;
            private int i = 0;
            {
                do {
                    a = is.next();
                    Optional<Rational> upper = a.getUpper();
                    if (upper.isPresent() && Ordering.le(upper.get(), Rational.ZERO)) {
                        throw new ArithmeticException("this must be positive. Invalid this: " + this);
                    }
                } while (!a.isFinitelyBounded() || a.contains(Rational.ZERO));
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (lowerReal == null) {
                    lowerReal = logOfRational(a.getLower().get()).iterator();
                    upperReal = logOfRational(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                }
                Interval lowerInterval = lowerReal.next();
                Interval upperInterval = upperReal.next();
                Interval nextInterval = lowerInterval.convexHull(upperInterval);
                Rational diameter = nextInterval.diameter().get();
                if (previousDiameter != null && Ordering.gt(diameter, previousDiameter.shiftRight(1))) {
                    a = is.next();
                    Optional<Rational> upper = a.getUpper();
                    if (upper.isPresent() && Ordering.le(upper.get(), Rational.ZERO)) {
                        throw new ArithmeticException("this must be positive. Invalid this: " + this);
                    }
                    lowerReal = logOfRational(a.getLower().get()).iterator();
                    upperReal = logOfRational(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                    lowerInterval = lowerReal.next();
                    upperInterval = upperReal.next();
                    nextInterval = lowerInterval.convexHull(upperInterval);
                    diameter = nextInterval.diameter().get();
                }
                previousDiameter = diameter;
                i++;
                return nextInterval;
            }
        });
    }

    /**
     * Returns the natural logarithm of {@code this}. If {@code this} is equal to zero and fuzzy on the left, this
     * method will give up and return empty once the approximating interval's diameter is less than the specified
     * resolution.
     *
     * <ul>
     *  <li>{@code this} cannot be negative, an exact zero, or a zero fuzzy on the left.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is not exact rational unless {@code this} is an exact 1.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return log({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Optional<Real> log(@NotNull Rational resolution) {
        Optional<Boolean> positive = gt(Rational.ZERO, resolution);
        if (!positive.isPresent()) {
            return Optional.empty();
        }
        if (!positive.get()) {
            throw new ArithmeticException("this must be positive. Invalid this: " + this);
        }
        return Optional.of(logUnsafe());
    }

    /**
     * Returns the base-{@code base} logarithm of {@code x}.
     *
     * <ul>
     *  <li>{@code x} must be positive.</li>
     *  <li>{@code base} must be positive and cannot equal 1.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @param base the base of the logarithm
     * @return log<sub>{@code base}</sub>({@code x})
     */
    public static @NotNull Real logOfRational(@NotNull Rational x, @NotNull Rational base) {
        Optional<Rational> log = x.log(base);
        if (log.isPresent()) {
            return new Real(log.get());
        }
        return logOfRational(x).divideUnsafe(logOfRational(base));
    }

    /**
     * Returns the base-{@code base} logarithm of {@code this}. If {@code this} is equal to zero and fuzzy on the left,
     * this method will loop forever. To prevent this behavior, use {@link Real#log(Rational, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} must be positive.</li>
     *  <li>{@code base} must be positive and cannot equal 1.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return log<sub>{@code base}</sub>({@code this})
     */
    public @NotNull Real logUnsafe(@NotNull Rational base) {
        if (rational.isPresent()) {
            return logOfRational(rational.get(), base);
        }
        return logUnsafe().divideUnsafe(logOfRational(base));
    }

    /**
     * Returns the base-{@code base} logarithm of {@code this}. If {@code this} is equal to zero and fuzzy on the left,
     * this method will give up and return empty once the approximating interval's diameter is less than the specified
     * resolution.
     *
     * <ul>
     *  <li>{@code this} cannot be negative, an exact zero, or a zero fuzzy on the left.</li>
     *  <li>{@code base} must be positive and cannot equal 1.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is not exact rational unless {@code this} is an exact 1.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return log<sub>{@code base}</sub>({@code this})
     */
    public @NotNull Optional<Real> log(@NotNull Rational base, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(logOfRational(rational.get(), base));
        }
        return log(resolution).map(x -> x.divideUnsafe(logOfRational(base)));
    }

    /**
     * Returns the base-{@code base} logarithm of {@code this}. If {@code this} is equal to zero and fuzzy on the left,
     * or {@code base} is equal to zero and fuzzy on the left, or {@code base} is equal to 1, this method will loop
     * forever. To prevent this behavior, use {@link Real#log(Real, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} must be positive.</li>
     *  <li>{@code base} must be positive and cannot equal 1.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @return log<sub>{@code base}</sub>({@code this})
     */
    public @NotNull Real logUnsafe(@NotNull Real base) {
        if (base.rational.isPresent()) {
            return logUnsafe(base.rational.get());
        }
        if (this == base) {
            return ONE;
        }
        return logUnsafe().divideUnsafe(base.logUnsafe());
    }

    /**
     * Returns the base-{@code base} logarithm of {@code this}. If {@code this} is equal to zero and fuzzy on the left,
     * or {@code base} is equal to zero and fuzzy on the left, or {@code base} is equal to 1, this method will give up
     * and return empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} cannot be negative, an exact zero, or a zero fuzzy on the left.</li>
     *  <li>{@code base} cannot be negative, an exact zero, or a zero fuzzy on the left. {@code base} cannot be an
     *  exact 1.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is not exact rational unless {@code this} is an exact 1.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return log<sub>{@code base}</sub>({@code this})
     */
    public @NotNull Optional<Real> log(@NotNull Real base, @NotNull Rational resolution) {
        if (this == base) {
            Optional<Boolean> positive = gt(Rational.ZERO, resolution);
            if (!positive.isPresent()) {
                return Optional.empty();
            }
            if (!positive.get()) {
                throw new ArithmeticException("this cannot be negative, an exact zero, or a zero fuzzy on the left." +
                        " Invalid this: " + this);
            }
            Optional<Boolean> equalTo1 = eq(Rational.ONE, resolution);
            if (!equalTo1.isPresent()) {
                return Optional.empty();
            }
            if (equalTo1.get()) {
                throw new ArithmeticException("base cannot be an exact 1.");
            }
            return Optional.of(ONE);
        }
        if (base.rational.isPresent()) {
            return log(base.rational.get(), resolution);
        }
        Optional<Real> log = log(resolution);
        if (!log.isPresent()) {
            return Optional.empty();
        }
        Optional<Real> baseLog = base.log(resolution);
        if (!baseLog.isPresent()) {
            return Optional.empty();
        }
        return log.get().divide(baseLog.get(), resolution);
    }

    /**
     * Returns {@code this} raised to the power of {@code p}. 0<sup>0</sup> yields 1. If {@code this} is zero and
     * {@code p} is a fuzzy zero, or if {@code this} is a fuzzy zero and {@code p} is negative or a fuzzy zero or not
     * exact rational, or if {@code this} is a zero fuzzy on the left and {@code p} is exact rational with an even
     * denominator, this method will loop forever. To prevent this behavior, use {@link Real#pow(Real, Rational)}
     * instead.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code p} may be any {@code Real}.</li>
     *  <li>If {@code this} is 0, {@code p} cannot be negative or a fuzzy zero.</li>
     *  <li>If {@code this} is negative or a zero fuzzy on the left, {@code p} must be exact rational with an odd
     *  denominator.</li>
     *  <li>If {@code p} is exact rational and {@code this} is not, {@code p} must have a numerator greater than or
     *  equal to –2<sup>31</sup> and less than 2<sup>31</sup> and a denominator less than 2<sup>31</sup>.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param p the power that {@code this} is raised to
     * @return {@code this}<sup>{@code p}</sup>
     */
    public @NotNull Real powUnsafe(@NotNull Real p) {
        if (p == ZERO || this == ONE) return ONE;
        if (p == ONE) return this;
        if (this == ZERO) {
            switch (p.signumUnsafe()) { // this may loop forever
                case 1: return ZERO;
                case 0: return ONE;
                case -1:
                    throw new ArithmeticException("If this is 0, p cannot be negative. this: " + this + ", p: " + p);
                default: throw new IllegalStateException("unreachable");
            }
        }
        if (this == E) return p.exp();
        if (p.rational.isPresent()) {
            if (rational.isPresent()) {
                return Algebraic.of(rational.get()).pow(p.rational.get()).realValue();
            } else {
                int smallNumerator = p.rational.get().getNumerator().intValueExact();
                int smallDenominator = p.rational.get().getDenominator().intValueExact();
                if ((smallDenominator & 1) == 0) {
                    if (ltUnsafe(Rational.ZERO)) {
                        throw new ArithmeticException("If this is negative or a zero fuzzy on the left, p must be an" +
                                " exact rational with an odd denominator. this: " + this + ", p: " + p);
                    }
                }
                return rootUnsafe(smallDenominator).powUnsafe(smallNumerator);
            }
        }
        if (ltUnsafe(Rational.ZERO)) { // this may loop forever
            throw new ArithmeticException("If this is negative or a zero fuzzy on the left, p must be an exact" +
                    " rational with an odd denominator. this: " + this + ", p: " + p);
        }
        return logUnsafe().multiply(p).exp();
    }

    /**
     * Returns {@code this} raised to the power of {@code p}. 0<sup>0</sup> yields 1. If {@code this} is zero and
     * {@code p} is a fuzzy zero, or if {@code this} is a fuzzy zero and {@code p} is negative or a fuzzy zero or not
     * exact rational, or if {@code this} is a zero fuzzy on the left and {@code p} is exact rational with an even
     * denominator, this method will give up and return empty once the approximating interval's diameter is less than
     * the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code p} may be any {@code Real}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If {@code this} is 0, {@code p} cannot be negative.</li>
     *  <li>If {@code this} is a fuzzy zero, {@code p} must be exact rational.</li>
     *  <li>If {@code this} is negative, {@code p} must be exact rational with an odd denominator.</li>
     *  <li>If {@code p} is exact rational and {@code this} is not, {@code p} must have a numerator greater than or
     *  equal to –2<sup>31</sup> and less than 2<sup>31</sup> and a denominator less than 2<sup>31</sup>.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param p the power that {@code this} is raised to
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}<sup>{@code p}</sup>
     */
    public @NotNull Optional<Real> pow(@NotNull Real p, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (p == ZERO || this == ONE) return Optional.of(ONE);
        if (p == ONE) return Optional.of(this);
        if (this == ZERO) {
            Optional<Integer> sign = p.signum(resolution);
            if (!sign.isPresent()) {
                return Optional.empty();
            }
            switch (sign.get()) {
                case 1: return Optional.of(ZERO);
                case 0: return Optional.of(ONE);
                case -1:
                    throw new ArithmeticException("If this is 0, p cannot be negative. this: " + this + ", p: " + p);
                default: throw new IllegalStateException("unreachable");
            }
        }
        if (this == E) return Optional.of(p.exp());
        if (p.rational.isPresent()) {
            if (rational.isPresent()) {
                return Optional.of(Algebraic.of(rational.get()).pow(p.rational.get()).realValue());
            } else {
                int smallNumerator = p.rational.get().getNumerator().intValueExact();
                int smallDenominator = p.rational.get().getDenominator().intValueExact();
                if ((smallDenominator & 1) == 0) {
                    Optional<Boolean> negative = lt(Rational.ZERO, DEFAULT_RESOLUTION);
                    if (!negative.isPresent()) {
                        return Optional.empty();
                    }
                    if (negative.get()) {
                        throw new ArithmeticException("If this is negative or a zero fuzzy on the left, p must be an" +
                                " exact rational with an odd denominator. this: " + this + ", p: " + p);
                    }
                }
                return root(smallDenominator, DEFAULT_RESOLUTION)
                        .flatMap(x -> x.pow(smallNumerator, DEFAULT_RESOLUTION));
            }
        }
        Optional<Boolean> negative = lt(Rational.ZERO, DEFAULT_RESOLUTION);
        if (!negative.isPresent()) {
            return Optional.empty();
        }
        if (negative.get()) {
            throw new ArithmeticException("If this is negative or a zero fuzzy on the left, p must be an exact" +
                    " rational with an odd denominator. this: " + this + ", p: " + p);
        }
        return log(resolution).map(x -> x.multiply(p).exp());
    }

    /**
     * Returns the sine of {@code x}.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result is the sine of a rational number. If it is –1 it is fuzzy on the right and if it is 1 it is
     *  fuzzy on the left. It is strictly between –1 and 1. If it is nonzero, it is not exact.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return sin({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real sinOfRational(@NotNull Rational x) {
        if (x == Rational.ZERO) return ZERO;
        if (x.signum() == -1) return sinOfRational(x.negate()).negate();
        Rational xSquared = x.pow(2);
        return forceContainment(() -> new NoRemoveIterator<Interval>() {
            private @NotNull Rational partialSum = Rational.ZERO;
            private @NotNull BigInteger j = BigInteger.ONE;
            private @NotNull BigInteger factorial = BigInteger.ONE;
            private @NotNull Rational power = x;
            private boolean sign = true;
            private boolean first = true;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (first) {
                    first = false;
                } else {
                    j = j.add(BigInteger.ONE);
                    factorial = factorial.multiply(j);
                    j = j.add(BigInteger.ONE);
                    factorial = factorial.multiply(j);
                    power = power.multiply(xSquared);
                    sign = !sign;
                }
                Rational term = power.divide(factorial);
                if (!sign) term = term.negate();
                partialSum = partialSum.add(term);
                Rational upper = partialSum;

                j = j.add(BigInteger.ONE);
                factorial = factorial.multiply(j);
                j = j.add(BigInteger.ONE);
                factorial = factorial.multiply(j);
                power = power.multiply(xSquared);
                sign = !sign;
                term = power.divide(factorial);
                if (!sign) term = term.negate();
                partialSum = partialSum.add(term);

                return Interval.of(partialSum, upper);
            }
        });
    }

    /**
     * Returns the cosine of {@code x}.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result is the cosine of a rational number. If it is –1 it is fuzzy on the right and if it is 1 it is
     *  fuzzy on the left. It is strictly between –1 and 1. If it is not equal to 1, it is not exact.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return cos({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real cosOfRational(@NotNull Rational x) {
        if (x == Rational.ZERO) return ONE;
        if (x.signum() == -1) return cosOfRational(x.negate());
        Rational xSquared = x.pow(2);
        return forceContainment((() -> new NoRemoveIterator<Interval>() {
            private @NotNull Rational partialSum = Rational.ONE;
            private @NotNull BigInteger j = BigInteger.ZERO;
            private @NotNull BigInteger factorial = BigInteger.ONE;
            private @NotNull Rational power = Rational.ONE;
            private boolean sign = true;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                j = j.add(BigInteger.ONE);
                factorial = factorial.multiply(j);
                j = j.add(BigInteger.ONE);
                factorial = factorial.multiply(j);
                power = power.multiply(xSquared);
                sign = !sign;
                Rational term = power.divide(factorial);
                if (!sign) term = term.negate();
                partialSum = partialSum.add(term);
                Rational lower = partialSum;

                j = j.add(BigInteger.ONE);
                factorial = factorial.multiply(j);
                j = j.add(BigInteger.ONE);
                factorial = factorial.multiply(j);
                power = power.multiply(xSquared);
                sign = !sign;
                term = power.divide(factorial);
                if (!sign) term = term.negate();
                partialSum = partialSum.add(term);

                return Interval.of(lower, partialSum);
            }
        }));
    }

    /**
     * Divide the real line into blocks of length π/2, so that block n is the interval [nπ/2, (n+1)π/2). Given a
     * finitely bounded interval, this method determines which blocks the lower and upper limits fall into.
     *
     * <ul>
     *  <li>{@code a} must be finitely bounded.</li>
     *  <li>The result is a pair whose first element is less than or equal to the second.</li>
     * </ul>
     *
     * @param a a finitely-bounded interval
     * @return the π/2-length blocks that the lower and upper limits of {@code a} fall into
     */
    private static @NotNull Pair<BigInteger, BigInteger> piOver2Blocks(@NotNull Interval a) {
        Rational lower = a.getLower().get();
        Rational upper = a.getUpper().get();
        BigInteger lowerBlock = lower == Rational.ZERO ?
                BigInteger.ZERO :
                ARCTAN_ONE.shiftLeft(1).divide(lower).invertUnsafe().floorUnsafe();
        BigInteger upperBlock = upper == Rational.ZERO ?
                BigInteger.ZERO :
                ARCTAN_ONE.shiftLeft(1).divide(upper).invertUnsafe().floorUnsafe();
        return new Pair<>(lowerBlock, upperBlock);
    }

    /**
     * Determines whether an interval contains any (irrational) element whose sine is 1.
     *
     * <ul>
     *  <li>{@code a} may be any {@code Interval}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a an {@code Interval}
     * @return ∃x∈{@code a}:sin(x)=1
     */
    private static boolean containsElementWhoseSinIs1(@NotNull Interval a) {
        if (!a.isFinitelyBounded()) return true;
        Pair<BigInteger, BigInteger> blocks = piOver2Blocks(a);
        if (Ordering.ge(blocks.b.subtract(blocks.a), BigInteger.valueOf(4))) return true;
        BigInteger three = BigInteger.valueOf(3);
        boolean multipleOf4Seen = false;
        for (BigInteger i = blocks.a; Ordering.le(i, blocks.b); i = i.add(BigInteger.ONE)) {
            int j = i.and(three).intValueExact();
            if (!multipleOf4Seen && j == 0) {
                multipleOf4Seen = true;
            }
            if (multipleOf4Seen && j == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether an interval contains any (irrational) element whose sine is –1.
     *
     * <ul>
     *  <li>{@code a} may be any {@code Interval}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a an {@code Interval}
     * @return ∃x∈{@code a}:sin(x)=–1
     */
    private static boolean containsElementWhoseSinIsNegative1(@NotNull Interval a) {
        if (!a.isFinitelyBounded()) return true;
        Pair<BigInteger, BigInteger> blocks = piOver2Blocks(a);
        if (Ordering.ge(blocks.b.subtract(blocks.a), BigInteger.valueOf(4))) return true;
        BigInteger three = BigInteger.valueOf(3);
        boolean fourMod2Seen = false;
        for (BigInteger i = blocks.a; Ordering.le(i, blocks.b); i = i.add(BigInteger.ONE)) {
            int j = i.and(three).intValueExact();
            if (!fourMod2Seen && j == 2) {
                fourMod2Seen = true;
            }
            if (fourMod2Seen && j == 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether an interval contains any (not necessarily rational) element whose cosine is 1.
     *
     * <ul>
     *  <li>{@code a} may be any {@code Interval}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a an {@code Interval}
     * @return ∃x∈{@code a}:cos(x)=1
     */
    private static boolean containsElementWhoseCosIs1(@NotNull Interval a) {
        if (!a.isFinitelyBounded()) return true;
        if (a.getLower().get() == Rational.ZERO && a.getUpper().get() == Rational.ZERO) return true;
        Pair<BigInteger, BigInteger> blocks = piOver2Blocks(a);
        if (Ordering.ge(blocks.b.subtract(blocks.a), BigInteger.valueOf(4))) return true;
        BigInteger three = BigInteger.valueOf(3);
        boolean fourMod3Seen = false;
        for (BigInteger i = blocks.a; Ordering.le(i, blocks.b); i = i.add(BigInteger.ONE)) {
            int j = i.and(three).intValueExact();
            if (!fourMod3Seen && j == 3) {
                fourMod3Seen = true;
            }
            if (fourMod3Seen && j == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether an interval contains any (irrational) element whose cosine is –1.
     *
     * <ul>
     *  <li>{@code a} may be any {@code Interval}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @param a an {@code Interval}
     * @return ∃x∈{@code a}:cos(x)=–1
     */
    private static boolean containsElementWhoseCosIsNegative1(@NotNull Interval a) {
        if (!a.isFinitelyBounded()) return true;
        Pair<BigInteger, BigInteger> blocks = piOver2Blocks(a);
        if (Ordering.ge(blocks.b.subtract(blocks.a), BigInteger.valueOf(4))) return true;
        BigInteger three = BigInteger.valueOf(3);
        boolean fourMod1Seen = false;
        for (BigInteger i = blocks.a; Ordering.le(i, blocks.b); i = i.add(BigInteger.ONE)) {
            int j = i.and(three).intValueExact();
            if (!fourMod1Seen && j == 1) {
                fourMod1Seen = true;
            }
            if (fourMod1Seen && j == 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the sine of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result is between –1 and 1, inclusive. If it is –1, it is fuzzy on the right. If it is 1, it is fuzzy
     *  on the left. If it is nonzero, it is not exact.</li>
     * </ul>
     *
     * @return sin({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real sin() {
        if (this == ZERO) {
            return ZERO;
        }
        if (rational.isPresent()) {
            return sinOfRational(rational.get());
        }
        return forceContainment(() -> new NoRemoveIterator<Interval>() {
            private final @NotNull Iterator<Interval> is = intervals.iterator();
            private Interval a;
            private Iterator<Interval> lowerReal = null;
            private Iterator<Interval> upperReal = null;
            private Rational previousDiameter = null;
            private int i = 0;
            private boolean sinAContains1;
            private boolean sinAContainsNegative1;
            {
                do {
                    a = is.next();
                } while (!a.isFinitelyBounded());
                sinAContains1 = containsElementWhoseSinIs1(a);
                sinAContainsNegative1 = containsElementWhoseSinIsNegative1(a);
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (lowerReal == null) {
                    lowerReal = sinOfRational(a.getLower().get()).iterator();
                    upperReal = sinOfRational(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                }
                Interval lowerInterval = lowerReal.next();
                Interval upperInterval = upperReal.next();
                Interval nextInterval = lowerInterval.convexHull(upperInterval);
                if (sinAContains1 && sinAContainsNegative1) {
                    nextInterval = Interval.of(Rational.NEGATIVE_ONE, Rational.ONE);
                } else if (sinAContains1) {
                    nextInterval = Interval.of(nextInterval.getLower().get(), Rational.ONE);
                } else if (sinAContainsNegative1) {
                    nextInterval = Interval.of(Rational.NEGATIVE_ONE, nextInterval.getUpper().get());
                }
                Rational diameter = nextInterval.diameter().get();
                if (previousDiameter != null && Ordering.gt(diameter, previousDiameter.shiftRight(1))) {
                    a = is.next();
                    if (sinAContains1) {
                        sinAContains1 = containsElementWhoseSinIs1(a);
                    }
                    if (sinAContainsNegative1) {
                        sinAContainsNegative1 = containsElementWhoseSinIsNegative1(a);
                    }
                    lowerReal = sinOfRational(a.getLower().get()).iterator();
                    upperReal = sinOfRational(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                    lowerInterval = lowerReal.next();
                    upperInterval = upperReal.next();
                    nextInterval = lowerInterval.convexHull(upperInterval);
                    if (sinAContains1 && sinAContainsNegative1) {
                        nextInterval = Interval.of(Rational.NEGATIVE_ONE, Rational.ONE);
                    } else if (sinAContains1) {
                        nextInterval = Interval.of(nextInterval.getLower().get(), Rational.ONE);
                    } else if (sinAContainsNegative1) {
                        nextInterval = Interval.of(Rational.NEGATIVE_ONE, nextInterval.getUpper().get());
                    }
                    diameter = nextInterval.diameter().get();
                }
                previousDiameter = diameter;
                i++;
                return nextInterval;
            }
        });
    }

    /**
     * Returns the cosine of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result is between –1 and 1, inclusive. If it is –1, it is fuzzy on the right. If it is 1, it is exact
     *  or fuzzy on the left. If it is not 1, it is not exact.</li>
     * </ul>
     *
     * @return cos({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real cos() {
        if (this == ZERO) {
            return ONE;
        }
        if (rational.isPresent()) {
            return cosOfRational(rational.get());
        }
        return forceContainment(() -> new NoRemoveIterator<Interval>() {
            private final @NotNull Iterator<Interval> is = intervals.iterator();
            private Interval a;
            private Iterator<Interval> lowerReal = null;
            private Iterator<Interval> upperReal = null;
            private Rational previousDiameter = null;
            private int i = 0;
            private boolean cosAContains1;
            private boolean cosAContainsNegative1;
            {
                do {
                    a = is.next();
                } while (!a.isFinitelyBounded());
                cosAContains1 = containsElementWhoseCosIs1(a);
                cosAContainsNegative1 = containsElementWhoseCosIsNegative1(a);
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (lowerReal == null) {
                    lowerReal = cosOfRational(a.getLower().get()).iterator();
                    upperReal = cosOfRational(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                }
                Interval lowerInterval = lowerReal.next();
                Interval upperInterval = upperReal.next();
                Interval nextInterval = lowerInterval.convexHull(upperInterval);
                if (cosAContains1 && cosAContainsNegative1) {
                    nextInterval = Interval.of(Rational.NEGATIVE_ONE, Rational.ONE);
                } else if (cosAContains1) {
                    nextInterval = Interval.of(nextInterval.getLower().get(), Rational.ONE);
                } else if (cosAContainsNegative1) {
                    nextInterval = Interval.of(Rational.NEGATIVE_ONE, nextInterval.getUpper().get());
                }
                Rational diameter = nextInterval.diameter().get();
                if (previousDiameter != null && Ordering.gt(diameter, previousDiameter.shiftRight(1))) {
                    a = is.next();
                    if (cosAContains1) {
                        cosAContains1 = containsElementWhoseCosIs1(a);
                    }
                    if (cosAContainsNegative1) {
                        cosAContainsNegative1 = containsElementWhoseCosIsNegative1(a);
                    }
                    lowerReal = cosOfRational(a.getLower().get()).iterator();
                    upperReal = cosOfRational(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                    lowerInterval = lowerReal.next();
                    upperInterval = upperReal.next();
                    nextInterval = lowerInterval.convexHull(upperInterval);
                    if (cosAContains1 && cosAContainsNegative1) {
                        nextInterval = Interval.of(Rational.NEGATIVE_ONE, Rational.ONE);
                    } else if (cosAContains1) {
                        nextInterval = Interval.of(nextInterval.getLower().get(), Rational.ONE);
                    } else if (cosAContainsNegative1) {
                        nextInterval = Interval.of(Rational.NEGATIVE_ONE, nextInterval.getUpper().get());
                    }
                    diameter = nextInterval.diameter().get();
                }
                previousDiameter = diameter;
                i++;
                return nextInterval;
            }
        });
    }

    /**
     * Returns the tangent of {@code x}.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result is the tangent of a rational number. If it is nonzero, it is not exact.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return tan({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real tanOfRational(@NotNull Rational x) {
        return sinOfRational(x).divideUnsafe(cosOfRational(x));
    }

    /**
     * Returns the cotangent of {@code x}.
     *
     * <ul>
     *  <li>{@code x} cannot be zero.</li>
     *  <li>The result is the cotangent of a rational number.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return cot({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real cotOfRational(@NotNull Rational x) {
        return cosOfRational(x).divideUnsafe(sinOfRational(x));
    }

    /**
     * Returns the tangent of {@code this}. If {@code this} is an odd multiple of π/2, this method will loop forever.
     * To avoid this behavior, use {@link Real#tan(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may not be an odd multiple of π/2.</li>
     *  <li>If the result is nonzero, it is not exact.</li>
     * </ul>
     *
     * @return tan({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real tanUnsafe() {
        if (rational.isPresent()) {
            return tanOfRational(rational.get());
        }
        return sin().divideUnsafe(cos());
    }

    /**
     * Returns the tangent of {@code this}. If {@code this} is an odd multiple of π/2, this method will give up and
     * return empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} cannot be null.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If the result is nonzero, it is not exact.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return tan({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Optional<Real> tan(@NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(tanOfRational(rational.get()));
        }
        return sin().divide(cos(), resolution);
    }

    /**
     * Returns the cotangent of {@code this}. If {@code this} is a multiple of π that is not an exact zero, this method
     * will loop forever. To avoid this behavior, use {@link Real#cot(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may not a multiple of π.</li>
     *  <li>The result is not exact.</li>
     * </ul>
     *
     * @return cot({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real cotUnsafe() {
        if (rational.isPresent()) {
            return cotOfRational(rational.get());
        }
        return cos().divideUnsafe(sin());
    }

    /**
     * Returns the cotangent of {@code this}. If {@code this} is a multiple of π that is not an exact zero, this method
     * will give up and return empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} cannot be an exact zero.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is not exact.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return tan({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Optional<Real> cot(@NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(cotOfRational(rational.get()));
        }
        return cos().divide(sin(), resolution);
    }

    /**
     * Returns the secant of {@code x}.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result is the secant of a rational number. It is less than –1 or greater than or equal to 1. If it is
     *  not equal to 1, it is not exact.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return sec({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real secOfRational(@NotNull Rational x) {
        return cosOfRational(x).invertUnsafe();
    }

    /**
     * Returns the cosecant of {@code x}.
     *
     * <ul>
     *  <li>{@code x} cannot be zero.</li>
     *  <li>The result is the cosecant of a rational number. It is less than –1 or greater than 1. It is not
     *  exact.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return csc({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real cscOfRational(@NotNull Rational x) {
        return sinOfRational(x).invertUnsafe();
    }

    /**
     * Returns the secant of {@code this}. If {@code this} is an odd multiple of π/2, this method will loop forever. To
     * avoid this behavior, use {@link Real#tan(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may not be an odd multiple of π/2.</li>
     *  <li>If the result is not equal to 1, it is not exact. It is less than or equal to –1 or greater than or equal
     *  to 1.</li>
     * </ul>
     *
     * @return sec({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real secUnsafe() {
        if (rational.isPresent()) {
            return secOfRational(rational.get());
        }
        return cos().invertUnsafe();
    }

    /**
     * Returns the secant of {@code this}. If {@code this} is an odd multiple of π/2, this method will give up and
     * return empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} cannot be null.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If the result is not equal to 1, it is not exact. It is less than or equal to –1 or greater than or equal
     *  to 1.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return sec({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Optional<Real> sec(@NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(secOfRational(rational.get()));
        }
        return cos().invert(resolution);
    }

    /**
     * Returns the cosecant of {@code this}. If {@code this} is a multiple of π that is not an exact zero, this method
     * will loop forever. To avoid this behavior, use {@link Real#cot(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may not a multiple of π. It is less than or equal to –1 or greater than or equal to 1.</li>
     *  <li>The result is not exact.</li>
     * </ul>
     *
     * @return csc({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real cscUnsafe() {
        if (rational.isPresent()) {
            return cscOfRational(rational.get());
        }
        return sin().invertUnsafe();
    }

    /**
     * Returns the cosecant of {@code this}. If {@code this} is a multiple of π that is not an exact zero, this method
     * will give up and return empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} cannot be an exact zero.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is not exact. It is less than or equal to –1 or greater than or equal to 1.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return csc({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Optional<Real> csc(@NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(cscOfRational(rational.get()));
        }
        return sin().invert(resolution);
    }

    /**
     * Returns the arctangent of {@code x}, provided that 0<{@code x}<1.
     *
     * <ul>
     *  <li>{@code x} must be strictly between 0 and 1.</li>
     *  <li>The result is the arctangent of a rational number, positive, and less than π/4.</li>
     * </ul>
     *
     * @param x a {@code Rational} between 0 and 1
     * @return arctan({@code x})
     */
    @SuppressWarnings("JavaDoc")
    private static @NotNull Real arctan01(@NotNull Rational x) {
        Rational xSquared = x.pow(2);
        return new Real(() -> new NoRemoveIterator<Interval>() {
            private @NotNull Rational partialSum = x;
            private @NotNull Rational xPower = x;
            private @NotNull BigInteger denominator = BigInteger.ONE;
            private boolean first = true;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
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

    /**
     * Returns the arctangent of {@code x}.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result is the arctangent of a rational number. It is strictly between –π/2 and π/2. If it is nonzero,
     *  it is not exact.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return arctan({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real arctanOfRational(@NotNull Rational x) {
        if (x == Rational.ZERO) return ZERO;
        if (x == Rational.ONE) return ARCTAN_ONE;
        if (x.signum() == -1) {
            return arctanOfRational(x.negate()).negate();
        }
        // arctan(x) when x ~= 1 converges very slowly, so use 2*arctan(x/2)-arctan(x^3/(3*x^2+4)) instead
        if (Ordering.le(x.subtract(Rational.ONE).abs(), Rational.TEN.invert())) {
            return arctanOfRational(x.shiftRight(1)).shiftLeft(1)
                    .subtract(arctanOfRational(x.pow(3).divide(x.pow(2).multiply(3).add(Rational.of(4)))));
        }
        if (Ordering.gt(x, Rational.ONE)) {
            return ARCTAN_ONE.shiftLeft(1).subtract(arctanOfRational(x.invert()));
        } else {
            return arctan01(x);
        }
    }

    /**
     * Returns the arccotangent of {@code x}.
     *
     * <ul>
     *  <li>{@code x} cannot be null.</li>
     *  <li>The result is the arccotangent of a rational number. It is strictly between 0 and π. It is not exact.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return arccot({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real arccotOfRational(@NotNull Rational x) {
        return PI.shiftRight(1).subtract(arctanOfRational(x));
    }

    /**
     * Returns the arctangent of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result is strictly between –π/2 and π/2. If it is nonzero, it is not exact.</li>
     * </ul>
     *
     * @return arctan({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real arctan() {
        if (rational.isPresent()) {
            return arctanOfRational(rational.get());
        }
        return forceContainment(() -> new NoRemoveIterator<Interval>() {
            private final @NotNull Iterator<Interval> is = intervals.iterator();
            private Interval a;
            private Iterator<Interval> lowerReal = null;
            private Iterator<Interval> upperReal = null;
            private Rational previousDiameter = null;
            private int i = 0;
            {
                do {
                    a = is.next();
                } while (!a.isFinitelyBounded());
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (lowerReal == null) {
                    lowerReal = arctanOfRational(a.getLower().get()).iterator();
                    upperReal = arctanOfRational(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                }
                Interval lowerInterval = lowerReal.next();
                Interval upperInterval = upperReal.next();
                Interval nextInterval = lowerInterval.convexHull(upperInterval);
                Rational diameter = nextInterval.diameter().get();
                if (previousDiameter != null && Ordering.gt(diameter, previousDiameter.shiftRight(1))) {
                    a = is.next();
                    lowerReal = arctanOfRational(a.getLower().get()).iterator();
                    upperReal = arctanOfRational(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                    lowerInterval = lowerReal.next();
                    upperInterval = upperReal.next();
                    nextInterval = lowerInterval.convexHull(upperInterval);
                    diameter = nextInterval.diameter().get();
                }
                previousDiameter = diameter;
                i++;
                return nextInterval;
            }
        });
    }

    /**
     * Returns the arccotangent of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>The result is strictly between 0 and π. It is not exact.</li>
     * </ul>
     *
     * @return arccot({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real arccot() {
        return PI.shiftRight(1).subtract(arctan());
    }

    /**
     * Returns the arcsine of {@code x}, provided that 0<{@code x}<1.
     *
     * <ul>
     *  <li>{@code x} must be greater than 0 and less than 1.</li>
     *  <li>The result is the arcsine of a rational number, positive, and less than π/2.</li>
     * </ul>
     *
     * @param x a {@code Rational} strictly between 0 and 1
     * @return arcsin({@code x})
     */
    @SuppressWarnings("JavaDoc")
    private static @NotNull Real arcsin01(@NotNull Rational x) {
        Real limit = ARCTAN_ONE.shiftLeft(1);
        return new Real(() -> new NoRemoveIterator<Interval>() {
            private boolean first = true;
            private @NotNull Rational lower = Rational.ZERO;
            private @NotNull Rational upper = Rational.TWO;
            private boolean upperOverLimit = true;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (first) {
                    first = false;
                    return Interval.of(lower, upper);
                }
                Rational midpoint = lower.add(upper).shiftRight(1);
                if (upperOverLimit && limit.ltUnsafe(midpoint)) {
                    upper = midpoint;
                } else if (sinOfRational(midpoint).gtUnsafe(x)) {
                    upperOverLimit = false;
                    upper = midpoint;
                } else {
                    lower = midpoint;
                }
                return Interval.of(lower, upper);
            }
        });
    }

    /**
     * Returns the arcsine of {@code x}.
     *
     * <ul>
     *  <li>{@code x} must be greater than or equal to –1 and less than or equal to 1.</li>
     *  <li>The result is the arcsine of a rational number. If it is nonzero, it is not exact. It is between –π/2 and
     *  π/2, inclusive.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return arcsin({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real arcsinOfRational(@NotNull Rational x) {
        if (x == Rational.ZERO) {
            return ZERO;
        } else if (x == Rational.ONE) {
            return ARCTAN_ONE.shiftLeft(1);
        } else if (Ordering.gt(x.abs(), Rational.ONE)) {
            throw new ArithmeticException("x must be between –1 and 1, inclusive. Invalid x: " + x);
        } else if (x.signum() == -1) {
            return arcsinOfRational(x.negate()).negate();
        } else {
            return arcsin01(x);
        }
    }

    /**
     * Returns the arccosine of {@code x}.
     *
     * <ul>
     *  <li>{@code x} must be greater than or equal to –1 and less than or equal to 1.</li>
     *  <li>The result is the arccosine of a rational number. If it is nonzero, it is not exact. It is between 0 and π,
     *  inclusive.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return arccos({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real arccosOfRational(@NotNull Rational x) {
        if (x == Rational.ONE) {
            return ZERO;
        } else {
            return ARCTAN_ONE.shiftLeft(1).subtract(arcsinOfRational(x));
        }
    }

    /**
     * Returns the arcsine of {@code this}. If {@code this} is 1 and fuzzy on the right, or –1 and fuzzy on the left,
     * this method will loop forever. To avoid this behavior, use {@link Real#arcsin(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} must be between –1 and 1. It cannot be 1 and fuzzy on the right or –1 and fuzzy on the
     *  left.</li>
     *  <li>If {@code this} is not an exact zero, the result is not exact. The result is between –π/2 and π/2,
     *  inclusive.</li>
     * </ul>
     *
     * @return arcsin({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real arcsinUnsafe() {
        if (this.rational.isPresent()) {
            return arcsinOfRational(this.rational.get());
        }
        Interval domain = Interval.of(Rational.NEGATIVE_ONE, Rational.ONE);
        return forceContainment(() -> new NoRemoveIterator<Interval>() {
            private final @NotNull Iterator<Interval> is = intervals.iterator();
            private Interval a;
            private Iterator<Interval> lowerReal = null;
            private Iterator<Interval> upperReal = null;
            private Rational previousDiameter = null;
            private int i = 0;
            {
                do {
                    Optional<Interval> oa = is.next().intersection(domain);
                    if (!oa.isPresent() || oa.get().getLower().equals(oa.get().getUpper())) {
                        throw new ArithmeticException("this must be between –1 and 1, inclusive. Invalid this: " +
                                this);
                    }
                    a = oa.get();
                } while (!a.isFinitelyBounded());
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (lowerReal == null) {
                    lowerReal = arcsinOfRational(a.getLower().get()).iterator();
                    upperReal = arcsinOfRational(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                }
                Interval lowerInterval = lowerReal.next();
                Interval upperInterval = upperReal.next();
                Interval nextInterval = lowerInterval.convexHull(upperInterval);
                Rational diameter = nextInterval.diameter().get();
                if (previousDiameter != null && Ordering.gt(diameter, previousDiameter.shiftRight(1))) {
                    Optional<Interval> oa = is.next().intersection(domain);
                    if (!oa.isPresent() || oa.get().getLower().equals(oa.get().getUpper())) {
                        throw new ArithmeticException("this must be between –1 and 1, inclusive. Invalid this: " +
                                this);
                    }
                    a = oa.get();
                    lowerReal = arcsinOfRational(a.getLower().get()).iterator();
                    upperReal = arcsinOfRational(a.getUpper().get()).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                    lowerInterval = lowerReal.next();
                    upperInterval = upperReal.next();
                    nextInterval = lowerInterval.convexHull(upperInterval);
                    diameter = nextInterval.diameter().get();
                }
                previousDiameter = diameter;
                i++;
                return nextInterval;
            }
        });
    }

    /**
     * Returns the arcsine of {@code this}. If {@code this} is 1 and fuzzy on the right, or –1 and fuzzy on the left,
     * this method will give up and return empty once the approximating interval's diameter is less than the specified
     * resolution.
     *
     * <ul>
     *  <li>{@code this} must be between –1 and 1.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If {@code this} is not an exact zero, the result is not exact. The result is between –π/2 and π/2,
     *  inclusive.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return arcsin({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Optional<Real> arcsin(@NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (this.rational.isPresent()) {
            return Optional.of(arcsinOfRational(rational.get()));
        }
        Optional<Boolean> inDomain = abs().le(Rational.ONE, resolution);
        if (!inDomain.isPresent()) {
            return Optional.empty();
        }
        if (!inDomain.get()) {
            throw new ArithmeticException("this must be between –1 and 1, inclusive. Invalid this: " + this);
        }
        return Optional.of(arcsinUnsafe());
    }

    /**
     * Returns the arccosine of {@code this}. If {@code this} is 1 and fuzzy on the right, or –1 and fuzzy on the left,
     * this method will loop forever. To avoid this behavior, use {@link Real#arccos(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} must be between –1 and 1. It cannot be 1 and fuzzy on the right or –1 and fuzzy on the
     *  left.</li>
     *  <li>If {@code this} is not an exact 1, the result is not exact. The result is between 0 and π, inclusive.</li>
     * </ul>
     *
     * @return arccos({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real arccosUnsafe() {
        if (this.rational.isPresent()) {
            return arccosOfRational(this.rational.get());
        }
        return ARCTAN_ONE.shiftLeft(1).subtract(arcsinUnsafe());
    }

    /**
     * Returns the arccosine of {@code this}. If {@code this} is 1 and fuzzy on the right, or –1 and fuzzy on the left,
     * this method will give up and return empty once the approximating interval's diameter is less than the specified
     * resolution.
     *
     * <ul>
     *  <li>{@code this} must be between –1 and 1.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If {@code this} is not an exact 1, the result is not exact. The result is between 0 and π, inclusive.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return arccos({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Optional<Real> arccos(@NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (this.rational.isPresent()) {
            return Optional.of(arccosOfRational(rational.get()));
        }
        return arcsin(resolution).map(x -> ARCTAN_ONE.shiftLeft(1).subtract(x));
    }

    /**
     * Returns the arcsecant of {@code x}.
     *
     * <ul>
     *  <li>{@code x} must be less than or equal to –1 or greater than or equal to 1.</li>
     *  <li>The result is the arcsecant of a rational number. If it is not equal to 1, it is not exact. It is between 0
     *  and π, inclusive.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return arcsec({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real arcsecOfRational(@NotNull Rational x) {
        return arccosOfRational(x.invert());
    }

    /**
     * Returns the arccosecant of {@code x}.
     *
     * <ul>
     *  <li>{@code x} must be less than or equal to –1 or greater than or equal to 1.</li>
     *  <li>The result is the arccosecant of a rational number. If it is nonzero, it is not exact. It is between –π/2
     *  and π/2, inclusive.</li>
     * </ul>
     *
     * @param x a {@code Rational}
     * @return arccsc({@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real arccscOfRational(@NotNull Rational x) {
        return arcsinOfRational(x.invert());
    }

    /**
     * Returns the arcsecant of {@code this}. If {@code this} is 1 and fuzzy on the left, or –1 and fuzzy on the right,
     * or a fuzzy zero, this method will loop forever. To avoid this behavior, use {@link Real#arcsec(Rational)}
     * instead.
     *
     * <ul>
     *  <li>{@code this} must be between less than or equal to –1 or greater than or equal to 1. It cannot be 1 and
     *  fuzzy on the left or –1 and fuzzy on the right.</li>
     *  <li>If {@code this} is not exact and equal to 1, the result is not exact. The result is between 0 and π,
     *  inclusive.</li>
     * </ul>
     *
     * @return arcsec({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real arcsecUnsafe() {
        return invertUnsafe().arccosUnsafe();
    }

    /**
     * Returns the arcsecant of {@code this}. If {@code this} is 1 and fuzzy on the left, or –1 and fuzzy on the right,
     * or a fuzzy zero, this method will give up and return empty once the approximating interval's diameter is less
     * than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} must be less than or equal to –1 or greater than or equal to 1.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If {@code this} is not exact and equal to 1, the result is not exact. The result is between 0 and π,
     *  inclusive.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return arcsec({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Optional<Real> arcsec(@NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (this.rational.isPresent()) {
            return Optional.of(arcsecOfRational(rational.get()));
        }
        Optional<Boolean> inDomain = abs().ge(Rational.ONE, resolution);
        if (!inDomain.isPresent()) {
            return Optional.empty();
        }
        if (!inDomain.get()) {
            throw new ArithmeticException("this must be less than or equal to –1 or greater than or equal to 1." +
                    " Invalid this: " + this);
        }
        return Optional.of(arcsecUnsafe());
    }

    /**
     * Returns the arccosecant of {@code this}. If {@code this} is 1 and fuzzy on the left, or –1 and fuzzy on the
     * right, or a fuzzy zero, this method will loop forever. To avoid this behavior, use {@link Real#arccsc(Rational)}
     * instead.
     *
     * <ul>
     *  <li>{@code this} must be between less than or equal to –1 or greater than or equal to 1. It cannot be 1 and
     *  fuzzy on the left or –1 and fuzzy on the right.</li>
     *  <li>If {@code this} is not exact and equal to 1, the result is not exact. The result is between –π/2 and π/2,
     *  inclusive.</li>
     * </ul>
     *
     * @return arccsc({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Real arccscUnsafe() {
        return invertUnsafe().arcsinUnsafe();
    }

    /**
     * Returns the arccosecant of {@code this}. If {@code this} is 1 and fuzzy on the left, or –1 and fuzzy on the
     * right, or a fuzzy zero, this method will give up and return empty once the approximating interval's diameter is
     * less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} must be less than or equal to –1 or greater than or equal to 1.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>If {@code this} is not exact and equal to 1, the result is not exact. The result is between –π/2 and π/2,
     *  inclusive.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return arccsc({@code this})
     */
    @SuppressWarnings("JavaDoc")
    public @NotNull Optional<Real> arccsc(@NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (this.rational.isPresent()) {
            return Optional.of(arccscOfRational(rational.get()));
        }
        Optional<Boolean> inDomain = abs().ge(Rational.ONE, resolution);
        if (!inDomain.isPresent()) {
            return Optional.empty();
        }
        if (!inDomain.get()) {
            throw new ArithmeticException("this must be less than or equal to –1 or greater than or equal to 1." +
                    " Invalid this: " + this);
        }
        return Optional.of(arccscUnsafe());
    }

    /**
     * Evaluates the atan2 function, or the counterclockwise angle from the positive x-axis of the point (x, y). In
     * agreement with the standard definition, the range is (–π, π] and the argument order is (y, x).
     *
     * <ul>
     *  <li>{@code y} cannot be null.</li>
     *  <li>{@code x} cannot be null.</li>
     *  <li>{@code y} and {@code x} cannot both be zero.</li>
     *  <li>The result is a number greater than –π and less than or equal to π whose tangent is rational.</li>
     * </ul>
     *
     * @param y the y-coordinate of a point
     * @param x the x-coordinate of a point
     * @return atan2({@code y}, {@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real atan2OfRational(@NotNull Rational y, @NotNull Rational x) {
        switch (x.signum()) {
            case 0:
                switch (y.signum()) {
                    case 0:
                        throw new ArithmeticException();
                    case 1:
                        return ARCTAN_ONE.shiftLeft(1);
                    case -1:
                        return ARCTAN_ONE.shiftLeft(1).negate();
                    default:
                        throw new IllegalStateException("unreachable");
                }
            case 1:
                return arctanOfRational(y.divide(x));
            case -1:
                if (y.signum() >= 0) {
                    return arctanOfRational(y.divide(x)).add(PI);
                } else {
                    return arctanOfRational(y.divide(x)).subtract(PI);
                }
            default:
                throw new IllegalStateException("unreachable");
        }
    }

    /**
     * Evaluates the atan2 function, or the counterclockwise angle from the positive x-axis of the point (x, y). In
     * agreement with the standard definition, the range is (–π, π] and the argument order is (y, x). If {@code y} and
     * {@code x} are both equal to zero and at least one is fuzzy, or if {@code x} is less than zero and {@code y} is
     * a fuzzy zero, this method will loop forever. To avoid this behavior, use
     * {@link Real#atan2(Real, Real, Rational)} instead.
     *
     * <ul>
     *  <li>{@code y} cannot be null.</li>
     *  <li>{@code x} cannot be null.</li>
     *  <li>{@code y} and {@code x} cannot both be zero.</li>
     *  <li>If {@code x} is less than zero and {@code y} is equal to zero, {@code y} must be exact.</li>
     *  <li>The result is greater than –π and less than or equal to π.</li>
     * </ul>
     *
     * @param y the y-coordinate of a point
     * @param x the x-coordinate of a point
     * @return atan2({@code y}, {@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Real atan2Unsafe(@NotNull Real y, @NotNull Real x) {
        if (x.rational.isPresent() && y.rational.isPresent()) {
            return atan2OfRational(y.rational.get(), x.rational.get());
        }
        if (x.rational.isPresent()) {
            Rational xr = x.rational.get();
            switch (xr.signum()) {
                case 0:
                    return y.signumUnsafe() == 1 ? ARCTAN_ONE.shiftLeft(1) : ARCTAN_ONE.shiftLeft(1).negate();
                case 1:
                    return y.divide(xr).arctan();
                case -1:
                    Real a = y.divide(xr).arctan();
                    return y.signumUnsafe() == 1 ? a.add(PI) : a.subtract(PI);
                default:
                    throw new IllegalStateException("unreachable");
            }
        }
        if (y.rational.isPresent()) {
            Rational yr = y.rational.get();
            switch (yr.signum()) {
                case 0:
                    return x.signumUnsafe() == 1 ? ZERO : PI;
                case 1:
                    return x.divide(yr).arccot();
                case -1:
                    return x.divide(yr).arccot().subtract(PI);
                default:
                    throw new IllegalStateException("unreachable");
            }
        }
        return forceContainment(() -> new Iterator<Interval>() {
            private final @NotNull Iterator<Interval> xas = x.iterator();
            private final @NotNull Iterator<Interval> yas = y.iterator();
            private Interval xa;
            private Interval ya;
            private Rational xaDiameter;
            private Rational yaDiameter;
            private Quadruple<Rational, Rational, Rational, Rational> boxExtremes;
            private Iterator<Interval> lowerReal;
            private Iterator<Interval> upperReal;
            private int i = 0;
            private Rational previousDiameter = null;
            {
                do {
                    xa = xas.next();
                } while (!xa.isFinitelyBounded());
                do {
                    ya = yas.next();
                } while (!ya.isFinitelyBounded());
                xaDiameter = xa.diameter().get();
                yaDiameter = ya.diameter().get();
                boxExtremes = getBoxExtremes();
                while (boxExtremes == null) {
                    refineIntervals();
                    boxExtremes = getBoxExtremes();
                }
            }

            private void refineIntervals() {
                if (Ordering.lt(xaDiameter, yaDiameter)) {
                    ya = yas.next();
                    yaDiameter = ya.diameter().get();
                } else {
                    xa = xas.next();
                    xaDiameter = xa.diameter().get();
                }
            }

            // Consider the box defined by xa and ya. This method returns (x_0, y_0, x_1, y_1), where (x_0, y_0) is the
            // corner of the box with the smallest θ and (x_1, y_1) is the corner with the largest θ. If the box
            // contains the positive y axis, null is returned.
            private Quadruple<Rational, Rational, Rational, Rational> getBoxExtremes() {
                Rational xl = xa.getLower().get();
                Rational xu = xa.getUpper().get();
                Rational yl = ya.getLower().get();
                Rational yu = ya.getUpper().get();
                int xls = xl.signum();
                int xus = xu.signum();
                int yls = yl.signum();
                int yus = yu.signum();
                if (xls <= 0 && xus >= 0) {
                    if (yls <= 0 && yus >= 0) {
                        return null;
                    } else if (yls > 0) {
                        return new Quadruple<>(xu, yl, xl, yl);
                    } else {
                        return new Quadruple<>(xl, yu, xu, yu);
                    }
                } else if (xls > 0) {
                    if (yls <= 0 && yus >= 0) {
                        return new Quadruple<>(xl, yl, xl, yu);
                    } else if (yls > 0) {
                        return new Quadruple<>(xu, yl, xl, yu);
                    } else {
                        return new Quadruple<>(xl, yl, xu, yu);
                    }
                } else {
                    if (yls <= 0 && yus >= 0) {
                        return null;
                    } else if (yls > 0) {
                        return new Quadruple<>(xu, yu, xl, yl);
                    } else {
                        return new Quadruple<>(xl, yu, xu, yl);
                    }
                }
            }

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Interval next() {
                if (lowerReal == null) {
                    lowerReal = atan2OfRational(boxExtremes.b, boxExtremes.a).iterator();
                    upperReal = atan2OfRational(boxExtremes.d, boxExtremes.c).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                }
                Interval lowerInterval = lowerReal.next();
                Interval upperInterval = upperReal.next();
                Interval nextInterval = lowerInterval.convexHull(upperInterval);
                Rational diameter = nextInterval.diameter().get();
                if (previousDiameter != null && Ordering.gt(diameter, previousDiameter.shiftRight(1))) {
                    refineIntervals();
                    boxExtremes = getBoxExtremes();
                    //noinspection ConstantConditions
                    lowerReal = atan2OfRational(boxExtremes.b, boxExtremes.a).iterator();
                    upperReal = atan2OfRational(boxExtremes.d, boxExtremes.c).iterator();
                    for (int j = 0; j < i; j++) {
                        lowerReal.next();
                        upperReal.next();
                    }
                    lowerInterval = lowerReal.next();
                    upperInterval = upperReal.next();
                    nextInterval = lowerInterval.convexHull(upperInterval);
                    diameter = nextInterval.diameter().get();
                }
                previousDiameter = diameter;
                i++;
                return nextInterval;
            }
        });
    }

    /**
     * Evaluates the atan2 function, or the counterclockwise angle from the positive x-axis of the point (x, y). In
     * agreement with the standard definition, the range is (–π, π] and the argument order is (y, x). If {@code y} and
     * {@code x} are both equal to zero and at least one is fuzzy, or if {@code x} is less than zero and {@code y} is
     * a fuzzy zero, this method will give up and return empty once the approximating interval's diameter is less than
     * the specified resolution.
     *
     * <ul>
     *  <li>{@code y} cannot be null.</li>
     *  <li>{@code x} cannot be null.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>{@code y} and {@code x} cannot both be exact zeros.</li>
     *  <li>The result is greater than –π and less than or equal to π.</li>
     * </ul>
     *
     * @param y the y-coordinate of a point
     * @param x the x-coordinate of a point
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return atan2({@code y}, {@code x})
     */
    @SuppressWarnings("JavaDoc")
    public static @NotNull Optional<Real> atan2(@NotNull Real y, @NotNull Real x, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (x.rational.isPresent() && y.rational.isPresent()) {
            return Optional.of(atan2OfRational(y.rational.get(), x.rational.get()));
        }
        if ((!y.isExact() || x.eq(Rational.ZERO, resolution).orElse(true)) &&
                y.eq(Rational.ZERO, resolution).orElse(true) && x.le(Rational.ZERO, resolution).orElse(true)) {
            return Optional.empty();
        }
        return Optional.of(atan2Unsafe(y, x));
    }

    /**
     * Given an interval [{@code lower}, {@code upper}], returns an {@code Interval} (with rational bounds) containing
     * the given interval and with a diameter no more than twice the given interval's.
     *
     * <ul>
     *  <li>{@code lower} cannot be null.</li>
     *  <li>{@code upper} cannot be null.</li>
     *  <li>{@code lower} must be less than {@code upper}.</li>
     *  <li>The result is finitely bounded.</li>
     * </ul>
     *
     * @param lower the lower bound of an interval
     * @param upper the upper bound of an interval
     * @return a rationally-bounded interval containing the given interval and no more than twice as large
     */
    public static @NotNull Interval intervalExtensionUnsafe(@NotNull Real lower, @NotNull Real upper) {
        if (lower == upper) {
            throw new IllegalArgumentException("lower must be less than upper. lower: " + lower + ", upper: " + upper);
        }
        if (lower.rational.isPresent() && upper.rational.isPresent()) {
            Rational rLower = lower.rational.get();
            Rational rUpper = upper.rational.get();
            if (Ordering.gt(rLower, rUpper)) {
                throw new IllegalArgumentException();
            }
            return Interval.of(rLower, rUpper);
        }
        Iterator<Interval> lowerIntervals = lower.iterator();
        Iterator<Interval> upperIntervals = upper.iterator();
        Interval lowerInterval;
        Interval upperInterval;
        do {
            lowerInterval = lowerIntervals.next();
            upperInterval = upperIntervals.next();
        } while (!lowerInterval.isFinitelyBounded() || !upperInterval.isFinitelyBounded() ||
                !lowerInterval.disjoint(upperInterval));
        Rational lowerDiameter = lowerInterval.diameter().get();
        Rational upperDiameter = upperInterval.diameter().get();
        while (true) {
            Rational gapSize = upperInterval.getLower().get().subtract(lowerInterval.getUpper().get());
            if (gapSize.signum() == -1) {
                throw new IllegalArgumentException();
            }
            Rational diameterSum = lowerDiameter.add(upperDiameter);
            if (Ordering.ge(gapSize, diameterSum)) {
                return lowerInterval.convexHull(upperInterval);
            }
            if (Ordering.lt(lowerDiameter, upperDiameter)) {
                upperInterval = upperIntervals.next();
                upperDiameter = upperInterval.diameter().get();
            } else {
                lowerInterval = lowerIntervals.next();
                lowerDiameter = lowerInterval.diameter().get();
            }
        }
    }

    /**
     * Returns the fractional part of {@code this}; {@code this}–⌊{@code this}⌋. If {@code this} is fuzzy on the left,
     * this method will loop forever. To avoid this behavior, use {@link Real#fractionalPart(Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} cannot be fuzzy on the left.</li>
     *  <li>The result is a {@code Real} x such that 0≤x{@literal <}1.</li>
     * </ul>
     *
     * @return the fractional part of {@code this}
     */
    public @NotNull Real fractionalPartUnsafe() {
        if (rational.isPresent()) {
            Rational fp = rational.get().fractionalPart();
            return fp == Rational.ZERO ? ZERO : new Real(fp);
        } else {
            return subtract(of(floorUnsafe()));
        }
    }

    /**
     * Returns the fractional part of {@code this}; {@code this}–⌊{@code this}⌋. If {@code this} is fuzzy on the left,
     * this method will give up and return empty once the approximating interval's diameter is less than the specified
     * resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is empty or a {@code Real} x such that 0≤x{@literal <}1.</li>
     * </ul>
     *
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return the fractional part of {@code this}
     */
    public @NotNull Optional<Real> fractionalPart(@NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            Rational fp = rational.get().fractionalPart();
            return Optional.of(fp == Rational.ZERO ? ZERO : new Real(fp));
        } else {
            return floor(resolution).map(i -> subtract(of(i)));
        }
    }

    /**
     * Rounds {@code this} to a rational number that is an integer multiple of 1/{@code denominator} according to
     * {@code roundingMode}; see documentation for {@link java.math.RoundingMode} for details.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code denominator} must be positive.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#UP}, {@code this} cannot be a positive integer
     *  multiple of 1/{@code denominator} that is fuzzy on the right, a negative integer multiple of
     *  1/{@code denominator} that is fuzzy on the left, or a fuzzy zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#DOWN}, {@code this} cannot be a positive integer
     *  multiple of 1/{@code denominator} that is fuzzy on the left or a negative integer multiple of
     *  1/{@code denominator} that is fuzzy on the right. However, it may be a fuzzy zero.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#CEILING}, {@code this} cannot be an integer
     *  multiple of 1/{@code denominator} that is fuzzy on the right.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#FLOOR}, {@code this} cannot be an integer multiple
     *  of 1/{@code denominator} that is fuzzy on the left.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_UP}, {@code this} cannot be an odd integer
     *  multiple of 1/(2{@code denominator}) that is fuzzy on the left or a negative half-integer that is fuzzy on the
     *  right. However, it may be a fuzzy integer multiple of 1/{@code denominator}.</li>
     *  <li>If {@code roundingMode} is {@link java.math.RoundingMode#HALF_DOWN}, {@code this} cannot be a positive
     *  odd integer multiple of 1/(2{@code denominator}) that is fuzzy on the right or a negative odd integer multiple
     *  of 1/(2{@code denominator}) that is fuzzy on the left. However, it may be a fuzzy integer multiple of
     *  1/{@code denominator}.</li>
     *  <li>Let k be an integer. If {@code roundingMode} is {@link java.math.RoundingMode#HALF_EVEN}, {@code this}
     *  cannot be equal to (4k+1)/2 and fuzzy on the right, or equal to (4k+3)/2 and fuzzy on the left. However, it may
     *  be a fuzzy integer multiple of 1/{@code denominator}.</li>
     *  <li>If {@code roundingMode} is {@code RoundingMode.UNNECESSARY}, {@code this} must be exact and its denominator
     *  must divide {@code denominator}.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param denominator the denominator which represents the precision that {@code this} is rounded to.
     * @param roundingMode determines the way in which {@code this} is rounded. Options are {@code RoundingMode.UP},
     *                     {@code RoundingMode.DOWN}, {@code RoundingMode.CEILING}, {@code RoundingMode.FLOOR},
     *                     {@code RoundingMode.HALF_UP}, {@code RoundingMode.HALF_DOWN},
     *                     {@code RoundingMode.HALF_EVEN}, and {@code RoundingMode.UNNECESSARY}.
     * @return {@code this}, rounded to an integer multiple of 1/{@code denominator}
     */
    public @NotNull Rational roundToDenominatorUnsafe(
            @NotNull BigInteger denominator,
            @NotNull RoundingMode roundingMode
    ) {
        if (denominator.signum() != 1) {
            throw new ArithmeticException("denominator must be positive. Invalid denominator: " + denominator);
        }
        return Rational.of(multiply(denominator).bigIntegerValueUnsafe(roundingMode)).divide(denominator);
    }

    /**
     * Rounds {@code this} to a rational number that is an integer multiple of 1/{@code denominator} according to
     * {@code roundingMode}; see documentation for {@link java.math.RoundingMode} for details. If {@code this} is a
     * fuzzy integer multiple of 1/{@code denominator} or a fuzzy odd integer multiple of 1/(2{@code denominator}),
     * depending on the rounding mode and the nature of the fuzziness (see
     * {@link Real#roundToDenominatorUnsafe(BigInteger, RoundingMode)}, this method will give up and return empty once
     * the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code denominator} must be positive.</li>
     *  <li>If {@code roundingMode} is {@code RoundingMode.UNNECESSARY}, {@code this} must be exact and its denominator
     *  must divide {@code denominator}.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result is not null.</li>
     * </ul>
     *
     * @param denominator the denominator which represents the precision that {@code this} is rounded to.
     * @param roundingMode determines the way in which {@code this} is rounded. Options are {@code RoundingMode.UP},
     *                     {@code RoundingMode.DOWN}, {@code RoundingMode.CEILING}, {@code RoundingMode.FLOOR},
     *                     {@code RoundingMode.HALF_UP}, {@code RoundingMode.HALF_DOWN},
     *                     {@code RoundingMode.HALF_EVEN}, and {@code RoundingMode.UNNECESSARY}.
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this}, rounded to an integer multiple of 1/{@code denominator}
     */
    public @NotNull Optional<Rational> roundToDenominator(
            @NotNull BigInteger denominator,
            @NotNull RoundingMode roundingMode,
            @NotNull Rational resolution
    ) {
        if (denominator.signum() != 1) {
            throw new ArithmeticException("denominator must be positive. Invalid denominator: " + denominator);
        }
        return multiply(denominator).bigIntegerValue(roundingMode, resolution)
                .map(i -> Rational.of(i).divide(denominator));
    }

    /**
     * A helper method for {@link Real#continuedFractionUnsafe()}. Given a rational approximation of a real number and
     * a prefix of a continued fraction, attempts to find the next continued fraction term. If the approximation is so
     * imprecise that it doesn't match the continued fraction prefix, an empty {@code Optional} is returned.
     *
     * <ul>
     *  <li>{@code approx} cannot be null.</li>
     *  <li>Every element in {@code termsSoFar}, except possibly the first, must be positive.</li>
     * </ul>
     *
     * @param approx an approximation to a real number
     * @param termsSoFar a prefix of the real number's continued fraction
     * @return the next term in the real number's continued fraction, or empty if the approximation is too imprecise.
     */
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

    /**
     * Finds the continued fraction of {@code this}. If we pretend that the result is an array called a, then
     * {@code this}=a[0]+1/(a[1]+1/(a[2]+...+1/a[n-1])...). If {@code this} is not clean, the resulting
     * {@code Iterable} will hang at some point.
     *
     * <ul>
     *  <li>{@code this} must be clean.</li>
     *  <li>The result is non-null and non-empty. None of its elements are null. The first element may be any
     *  {@code BigInteger}; the remaining elements, if any, are all positive. If the result is finite and has more than
     *  one element, the last element is greater than 1.</li>
     * </ul>
     *
     * Length is O(log({@code denominator})) if {@code this} is rational, infinite otherwise
     *
     * @return the continued fraction representation of {@code this}
     */
    public @NotNull Iterable<BigInteger> continuedFractionUnsafe() {
        if (rational.isPresent()) {
            return rational.get().continuedFraction();
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
            public @NotNull BigInteger next() {
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

    /**
     * Returns a {@code Real} with a specified continued fraction. If the continued fraction is passed in as a
     * {@link Collection}, then it is known to be finite and the result will be exact. Otherwise, the result may be
     * fuzzy.
     *
     * <ul>
     *  <li>{@code continuedFraction} cannot be empty, and each of its elements, except possibly the first, must be
     *  positive.</li>
     *  <li>The result may be any {@code Real}.</li>
     * </ul>
     *
     * @param continuedFraction a continued fraction
     * @return the {@code Real} with the specified continued fraction
     */
    public static @NotNull Real fromContinuedFraction(@NotNull Iterable<BigInteger> continuedFraction) {
        if (continuedFraction instanceof Collection) { // then we know that continuedFraction is finite
            Rational r = Rational.fromContinuedFraction(toList(continuedFraction));
            if (r == Rational.ZERO) return ZERO;
            if (r == Rational.ONE) return ONE;
            return new Real(r);
        }
        return new Real(() -> new Iterator<Interval>() {
            private @NotNull Optional<Interval> exactValue = Optional.empty();
            private @NotNull Iterator<BigInteger> is = continuedFraction.iterator();
            private final @NotNull List<BigInteger> prefix = new ArrayList<>();
            private Rational low = null;
            private Rational high = null;
            private boolean nextIsLow = true;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (exactValue.isPresent()) {
                    return exactValue.get();
                } else {
                    if (!is.hasNext()) {
                        Rational r = nextIsLow ? high : low;
                        if (r == null) {
                            throw new IllegalArgumentException("continuedFraction cannot be empty.");
                        }
                        exactValue = Optional.of(Interval.of(r));
                        return exactValue.get();
                    } else {
                        while (true) {
                            prefix.add(is.next());
                            Rational r = Rational.fromContinuedFraction(prefix);
                            if (nextIsLow) {
                                low = r;
                            } else {
                                high = r;
                            }
                            nextIsLow = !nextIsLow;
                            if (high != null) { // this is only false the first time next() is called
                                return Interval.of(low, high);
                            }
                            if (!is.hasNext()) {
                                exactValue = Optional.of(Interval.of(low));
                                return exactValue.get();
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Returns the convergents, or rational approximations of {@code this} formed by truncating its continued fraction
     * at various points. The first element of the result is the floor of {@code this}. If {@code this} is rational,
     * the last element of the result is {@code this}. If {@code this} is not clean, the resulting {@code Iterable}
     * will hang at some point.
     *
     * <ul>
     *  <li>{@code this} must be clean.</li>
     *  <li>The result is a non-null, non-empty {@code Iterable} that consists of the convergents of some real
     *  number.</li>
     * </ul>
     *
     * Length is O(log({@code denominator})) if {@code this} is rational, infinite otherwise.
     *
     * @return the convergents of {@code this}.
     */
    public @NotNull Iterable<Rational> convergentsUnsafe() {
        return map(Rational::fromContinuedFraction, tail(inits(continuedFractionUnsafe())));
    }

    /**
     * Returns the digits of (non-negative) {@code this} in a given base. The return value is a pair consisting of the
     * digits before the decimal point (in a list) and the digits after the decimal point (in a possibly-infinite
     * {@code Iterable}). If {@code this} is exact on the left, fuzzy on the right, and has a terminating
     * base-{@code base} representation, the second element of the result will have infinitely many trailing zeros. If
     * {@code this} is fuzzy on the left and has a terminating base-{@code base} representation, this method will loop
     * forever. To prevent this behavior, use {@link Real#digits(BigInteger, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} must be non-negative.</li>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>If {@code this} has a terminating base-{@code base} expansion, {@code this} cannot be fuzzy on the
     *  left.</li>
     *  <li>Neither element of the result is null or contains nulls. Neither element of the result contains negative
     *  numbers. The first element has no leading zeros. The second element does not end in infinitely many copies of
     *  {@code base}–1. If the second element is finite, it contains no trailing zeros.</li>
     * </ul>
     *
     * @param base the base of the digits
     * @return a pair consisting of the digits before the decimal point and the digits after
     */
    public @NotNull Pair<List<BigInteger>, Iterable<BigInteger>> digitsUnsafe(@NotNull BigInteger base) {
        if (rational.isPresent()) {
            return rational.get().digits(base);
        }
        if (Ordering.lt(base, IntegerUtils.TWO)) {
            throw new IllegalArgumentException("base must be at least 2. Invalid base: " + base);
        }
        BigInteger floor = floorUnsafe();
        if (floor.signum() == -1) {
            throw new IllegalArgumentException("this cannot be negative. Invalid this: " + this);
        }
        List<BigInteger> beforeDecimal = IntegerUtils.bigEndianDigits(base, floor);
        Iterable<BigInteger> afterDecimal = () -> new Iterator<BigInteger>() {
            Iterator<Interval> fractionIntervals = subtract(Rational.of(floor)).iterator();
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

    /**
     * Returns the digits of (non-negative) {@code this} in a given base. The return value is a pair consisting of the
     * digits before the decimal point (in a list) and the digits after the decimal point. If {@code this} is an exact
     * rational with a terminating base-{@code base} expansion, there will be finitely many digits after the decimal
     * point, with no trailing zeros. If {@code this} is an exact rational with a non-terminating base-{@code base},
     * there will be infinitely many digits after the decimal point. If this is irrational or fuzzy, the digits after
     * the decimal point will end once the bounding interval decreases below the specified resolution. A trailing zero
     * is appended to distinguish this from the rational case.
     *
     * <ul>
     *  <li>{@code this} must be non-negative.</li>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>Neither element of the result is null or contains nulls. Neither element of the result contains negative
     *  numbers, except that the last element of the second element may be –1. The first element has no leading zeros.
     *  The second element does not end in infinitely many copies of {@code base}–1. If the second element is finite,
     *  it contains no trailing zeros.</li>
     * </ul>
     *
     * @param base the base of the digits
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return a pair consisting of the digits before the decimal point and the digits after
     */
    public @NotNull Optional<Pair<List<BigInteger>, Iterable<BigInteger>>> digits(
            @NotNull BigInteger base,
            @NotNull Rational resolution
    ) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(rational.get().digits(base));
        }
        Optional<BigInteger> oFloor = floor(resolution);
        if (!oFloor.isPresent()) {
            return Optional.empty();
        }
        BigInteger floor = oFloor.get();
        if (floor.signum() == -1) {
            throw new IllegalArgumentException("this cannot be negative. Invalid this: " + this);
        }
        List<BigInteger> beforeDecimal = IntegerUtils.bigEndianDigits(base, floor);
        Iterable<BigInteger> afterDecimal = () -> new Iterator<BigInteger>() {
            private final @NotNull Iterator<Interval> fractionIntervals = subtract(of(floor)).iterator();
            private @NotNull BigInteger power = base;
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
                            return BigInteger.ZERO;
                        }
                        interval = fractionIntervals.next().multiply(power);
                    }
                }
            }
        };
        return Optional.of(new Pair<>(beforeDecimal, afterDecimal));
    }

    /**
     * Constructs a {@code Real} from a list of digits before the decimal point and a possibly-infinite
     * {@code Iterable} of digits after the decimal point.
     *
     * <ul>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>Every element of {@code beforeDecimal} must be non-negative and less than {@code base}.</li>
     *  <li>Every element of {@code afterDecimal} must be non-negative and less than {@code base}.</li>
     *  <li>The result is non-negative.</li>
     * </ul>
     *
     * @param base the base of the digits
     * @param beforeDecimal the digits before the decimal point
     * @param afterDecimal the digits after the decimal point
     * @return the {@code Real} with the specified digits
     */
    public static @NotNull Real fromDigits(
            @NotNull BigInteger base,
            @NotNull List<BigInteger> beforeDecimal,
            @NotNull Iterable<BigInteger> afterDecimal
    ) {
        return new Real(() -> new Iterator<Interval>() {
            private final @NotNull Iterator<BigInteger> digits = afterDecimal.iterator();
            private @NotNull BigInteger accumulator = BigInteger.ZERO;
            private @NotNull BigInteger power = BigInteger.ONE;
            private @NotNull Optional<Interval> finalInterval = Optional.empty();

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull Interval next() {
                if (digits.hasNext()) {
                    BigInteger digit = digits.next();
                    if (digit.signum() == -1 || Ordering.ge(digit, base)) {
                        throw new IllegalArgumentException("every digit must be at least zero and less than the base");
                    }
                    accumulator = accumulator.multiply(base).add(digit);
                    power = power.multiply(base);
                    Rational r = Rational.of(accumulator);
                    return Interval.of(r.divide(power), r.add(Rational.ONE).divide(power));
                } else if (finalInterval.isPresent()) {
                    return finalInterval.get();
                } else {
                    finalInterval = Optional.of(Interval.of(Rational.of(accumulator).divide(power)));
                    return finalInterval.get();
                }
            }
        }).add(Rational.of(IntegerUtils.fromBigEndianDigits(base, beforeDecimal)));
    }

    /**
     * The base-{@code base} Liouville's constant, or the sum of {@code base}<sup>–i!</sup> as i goes from 1 to
     * infinity. Not to be confused with Liouville's numbers, of which the constants are a particular example. They are
     * all transcendental.
     *
     * <ul>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>The result is a Liouville constant.</li>
     * </ul>
     *
     * @param base the base to which the negative factorials are raised.
     * @return the base-{@code base} Liouville constant
     */
    public static @NotNull Real liouville(@NotNull BigInteger base) {
        return fromDigits(base, Collections.emptyList(), () -> new Iterator<BigInteger>() {
            private @NotNull BigInteger i = BigInteger.ONE;
            private @NotNull BigInteger fi = BigInteger.ONE;
            private @NotNull BigInteger nextFactorial = BigInteger.ONE;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public @NotNull BigInteger next() {
                if (i.equals(nextFactorial)) {
                    i = i.add(BigInteger.ONE);
                    fi = fi.add(BigInteger.ONE);
                    nextFactorial = nextFactorial.multiply(fi);
                    return BigInteger.ONE;
                } else {
                    i = i.add(BigInteger.ONE);
                    return BigInteger.ZERO;
                }
            }
        });
    }

    /**
     * The base-{@code base} Champernowne constant, or the number produced by concatenating all positive integers, in
     * order, in a particular base and placing them after the decimal point. They are all transcendental.
     *
     * <ul>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>The result is a Champernowne constant.</li>
     * </ul>
     *
     * @param base the base in which the positive integers are written.
     * @return C<sub>{@code base}</sub>
     */
    public static @NotNull Real champernowne(@NotNull BigInteger base) {
        return fromDigits(
                base,
                Collections.emptyList(),
                concatMap(i -> IntegerUtils.bigEndianDigits(base, i), EP.rangeUpIncreasing(BigInteger.ONE))
        );
    }

    /**
     * The base-{@code base} Copeland–Erdős constant, or the number produced by concatenating all primes, in order, in
     * a particular base and placing them after the decimal point. They are all irrational and probably transcendental.
     *
     * <ul>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>The result is a Copeland–Erdős constant.</li>
     * </ul>
     *
     * @param base the base in which the primes are written.
     * @return the base-{@code base} Copeland–Erdős constant
     */
    public static @NotNull Real copelandErdos(@NotNull BigInteger base) {
        return fromDigits(
                base,
                Collections.emptyList(),
                concatMap(i -> IntegerUtils.bigEndianDigits(base, i), MathUtils.PRIMES)
        );
    }

    /**
     * A number that is "as normal as possible" in a given base. See {@link MathUtils#greedyNormalSequence(int)}.
     *
     * <ul>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>The result is normal (and therefore irrational), and probably transcendental.</li>
     * </ul>
     *
     * @param base the base that the number is normal in.
     * @return a greedy normal number.
     */
    public static @NotNull Real greedyNormal(int base) {
        //noinspection Convert2MethodRef
        return fromDigits(
                BigInteger.valueOf(base),
                Collections.emptyList(),
                map(i -> BigInteger.valueOf(i), MathUtils.greedyNormalSequence(base))
        );
    }

    /**
     * Converts {@code this} to a {@code String} in any base greater than 1, rounding to {@code scale} digits after the
     * decimal point. A scale of 0 indicates rounding to an integer, and a negative scale indicates rounding to a
     * positive power of {@code base}. If the result is an approximation (that is, not all digits are displayed) and
     * the scale is positive, an ellipsis ("...") is appended. If the base is 36 or less, the digits are '0' through
     * '9' followed by 'A' through 'Z'. If the base is greater than 36, the digits are written in decimal and each
     * digit is surrounded by parentheses. If {@code this} has a fractional part, a decimal point is used. There are no
     * leading zeros before the decimal point (unless |{@code this}| is less than 1, in which case there is exactly one
     * zero) and no trailing zeros after (unless an ellipsis is present, in which case there may be any number of
     * trailing zeros). Scientific notation is not used. If {@code this} has no more than {@code scale} digits after
     * the decimal point in base {@code base}, and {@code this} is fuzzy, this method will loop forever. To prevent
     * this behavior, use {@link Real#toStringBase(BigInteger, int, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>If {@code this} has no more than {@code scale} digits after the decimal point in base {@code base},
     *  {@code this} cannot be fuzzy.</li>
     *  <li>The result is a {@code String} representing an {@code Real} in the manner previously described. See the
     *  unit test and demo for further reference.</li>
     * </ul>
     *
     * @param base the base of the output digits
     * @param scale the maximum number of digits after the decimal point in the result. If {@code this} has a
     *              terminating base-{@code base} expansion, the actual number of digits after the decimal point may be
     *              fewer.
     * @return a {@code String} representation of {@code this} in base {@code base}
     */
    public @NotNull String toStringBaseUnsafe(@NotNull BigInteger base, int scale) {
        if (Ordering.lt(base, IntegerUtils.TWO)) {
            throw new IllegalArgumentException("base must be at least 2. Invalid base: " + base);
        }
        Optional<Rational> or = rationalValueExact();
        if (or.isPresent()) {
            return or.get().toStringBase(base, scale);
        }
        BigInteger power = base.pow(scale >= 0 ? scale : -scale);
        Real scaled = scale >= 0 ? multiply(power) : divide(power);
        Rational rounded = Rational.of(scaled.bigIntegerValueUnsafe(RoundingMode.DOWN));
        rounded = scale >= 0 ? rounded.divide(power) : rounded.multiply(power);
        String result = rounded.toStringBase(base);
        if (scale > 0) { //append ellipsis
            //pad with trailing zeros if necessary
            int dotIndex = result.indexOf('.');
            if (dotIndex == -1) {
                dotIndex = result.length();
                result = result + ".";
            }
            if (Ordering.le(base, ASCII_ALPHANUMERIC_COUNT)) {
                int missingZeros = scale - result.length() + dotIndex + 1;
                result += replicateString(missingZeros, '0');
            } else {
                int missingZeros = scale;
                for (int i = dotIndex + 1; i < result.length(); i++) {
                    if (result.charAt(i) == '(') missingZeros--;
                }
                result += concatStrings(replicate(missingZeros, "(0)"));
            }
            result += "...";
        }
        return result;
    }

    /**
     * <p>Converts {@code this} to a {@code String} in any base greater than 1, rounding to {@code scale} digits after
     * the decimal point. A scale of 0 indicates rounding to an integer, and a negative scale indicates rounding to a
     * positive power of {@code base}. If the result is an approximation (that is, not all digits are displayed) and
     * the scale is positive, an ellipsis ("...") is appended. If the base is 36 or less, the digits are '0' through
     * '9' followed by 'A' through 'Z'. If the base is greater than 36, the digits are written in decimal and each
     * digit is surrounded by parentheses. If {@code this} has a fractional part, a decimal point is used. There are no
     * leading zeros before the decimal point (unless |{@code this}| is less than 1, in which case there is exactly one
     * zero) and no trailing zeros after (unless an ellipsis is present, in which case there may be any number of
     * trailing zeros). Scientific notation is not used.</p>
     *
     * If {@code this} has no more than {@code scale} digits after the decimal point in base {@code base}, and
     * {@code this} is fuzzy, the output is as follows:
     * <ul>
     *  <li>If {@code this} is zero and only fuzzy on the right, the result looks like "0.000...".</li>
     *  <li>If {@code this} is zero and only fuzzy on the left, the result looks like "-0.000...".</li>
     *  <li>If {@code this} is zero and fuzzy on both sides, the result looks like "~0".</li>
     *  <li>If {@code this} is positive and only fuzzy on the right, the result looks like "0.5000...".</li>
     *  <li>If {@code this} is positive and only fuzzy on the left, the result looks like "0.4999...".</li>
     *  <li>If {@code this} is positive and fuzzy on both sides, the result looks like "~0.5".</li>
     *  <li>If {@code this} is negative and only fuzzy on the left, the result looks like "-0.5000...".</li>
     *  <li>If {@code this} is negative and only fuzzy on the right, the result looks like "-0.4999...".</li>
     *  <li>If {@code this} is negative and fuzzy on both sides, the result looks like "~-0.5".</li>
     * </ul>
     * Note that true fuzziness is impossible to detect, so in the preceding discussion, "fuzziness" is relative to
     * {@code resolution}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code base} must be at least 2.</li>
     *  <li>{@code scale} may be any {@code int}.</li>
     *  <li>{@code resolution} must be positive and less than or equal to
     *  1/2*{@code base}<sup>{@code scale}</sup>.</li>
     *  <li>The result is a {@code String} representing an {@code Real} in the manner previously described. See the
     *  unit test and demo for further reference.</li>
     * </ul>
     *
     * @param base the base of the output digits
     * @param scale the maximum number of digits after the decimal point in the result. If {@code this} has a
     *              terminating base-{@code base} expansion, the actual number of digits after the decimal point may be
     *              fewer.
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return a {@code String} representation of {@code this} in base {@code base}
     */
    public @NotNull String toStringBase(@NotNull BigInteger base, int scale, @NotNull Rational resolution) {
        if (Ordering.lt(base, IntegerUtils.TWO)) {
            throw new IllegalArgumentException("base must be at least 2. Invalid base: " + base);
        }
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        BigInteger power = base.pow(scale >= 0 ? scale : -scale);
        Rational adjustedResolution = scale >= 0 ? resolution.multiply(power) : resolution.divide(power);
        if (Ordering.gt(adjustedResolution, Rational.ONE_HALF)) {
            throw new IllegalArgumentException("resolution must be less than or equal to 1/2*base^scale." +
                    " resolution: " + resolution + ", base: " + base + ", scale: " + scale);
        }
        Optional<Rational> or = rationalValueExact();
        if (or.isPresent()) {
            return or.get().toStringBase(base, scale);
        }
        Real scaled = scale >= 0 ? multiply(power) : divide(power);
        Optional<BigInteger> roundedDown = scaled.bigIntegerValue(RoundingMode.DOWN, adjustedResolution);
        Rational rounded;
        boolean fuzzyOnBothSides = false;
        boolean negativeZero = false;
        if (roundedDown.isPresent()) {
            rounded = Rational.of(roundedDown.get());
            if (rounded == Rational.ZERO) {
                boolean fuzzyOnTheLeft = !scaled.bigIntegerValue(RoundingMode.FLOOR, adjustedResolution).isPresent();
                boolean fuzzyOnTheRight =
                        !scaled.bigIntegerValue(RoundingMode.CEILING, adjustedResolution).isPresent();
                fuzzyOnBothSides = fuzzyOnTheLeft && fuzzyOnTheRight;
                negativeZero = fuzzyOnTheLeft && !fuzzyOnTheRight;
            }
        } else {
            Optional<BigInteger> roundedUp = scaled.bigIntegerValue(RoundingMode.UP, adjustedResolution);
            if (roundedUp.isPresent()) {
                BigInteger scaledValue = roundedUp.get();
                if (scaledValue.signum() == 1) {
                    scaledValue = scaledValue.subtract(BigInteger.ONE);
                } else {
                    scaledValue = scaledValue.add(BigInteger.ONE);
                }
                rounded = Rational.of(scaledValue);
            } else {
                fuzzyOnBothSides = true;
                // because resolution is ≤ 1/2 and the finest bounding interval (whose diameter is less than
                // resolution) so far contains an integer, it cannot contain a half-integer, so bigIntegerValueUnsafe,
                // which rounds to the nearest integer, is guaranteed to terminate.
                rounded = Rational.of(scaled.bigIntegerValueUnsafe());
            }
        }
        rounded = scale >= 0 ? rounded.divide(power) : rounded.multiply(power);
        String result = rounded.toStringBase(base);
        if (fuzzyOnBothSides) {
            result = "~" + result;
        } else if (negativeZero) {
            result = "-" + result;
        }
        if (!fuzzyOnBothSides && scale > 0) {
            //pad with trailing zeros if necessary
            int dotIndex = result.indexOf('.');
            if (dotIndex == -1) {
                dotIndex = result.length();
                result = result + ".";
            }
            if (Ordering.le(base, ASCII_ALPHANUMERIC_COUNT)) {
                int missingZeros = scale - result.length() + dotIndex + 1;
                result += replicateString(missingZeros, '0');
            } else {
                int missingZeros = scale;
                for (int i = dotIndex + 1; i < result.length(); i++) {
                    if (result.charAt(i) == '(') missingZeros--;
                }
                result += concatStrings(replicate(missingZeros, "(0)"));
            }
            result += "...";
        }
        return result;
    }

    /**
     * {@code Real}s cannot be compared using {@code equals}. Use one of the other comparison functions.
     *
     * @param that The {@code Object} to be compared with {@code this}
     * @return always throws
     */
    @Override
    public boolean equals(Object that) {
        throw new UnsupportedOperationException("Reals cannot be compared using equals.");
    }

    /**
     * {@code Real}s do not have a hash code. If you need a set or map with {@code Real}s as keys, and you're sure that
     * every pair of {@code Real}s is either unequal or both exact, you can use a {@link TreeSet} or {@link TreeMap}
     * with {@link Real#compareToUnsafe(Real)} as the comparator.
     *
     * @return always throws
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Reals do not have a hash code.");
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. If {@code this} is fuzzy and equal to that, this method will loop forever. To avoid
     * this behavior, use {@link Real#compareTo(Rational, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code this} cannot be fuzzy and equal to {@code that}.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    public int compareToUnsafe(@NotNull Rational that) {
        if (rational.isPresent()) {
            return rational.get().compareTo(that);
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

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. If {@code this} is fuzzy and equal to that, this method will give up and return empty
     * once the approximating interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result may be –1, 0, 1, or empty.</li>
     * </ul>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this} compared to {@code that}
     */
    public @NotNull Optional<Integer> compareTo(@NotNull Rational that, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(rational.get().compareTo(that));
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

    /**
     * Determines whether {@code this} is equal to {@code that}. If {@code this} is fuzzy and equal to that, this
     * method will loop forever. To avoid this behavior, use {@link Real#eq(Rational, Rational)} instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code this} cannot be fuzzy and equal to {@code that}.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @return x=y
     */
    public boolean eqUnsafe(@NotNull Rational that) {
        return compareToUnsafe(that) == 0;
    }

    /**
     * Determines whether {@code this} is unequal to {@code that}. If {@code this} is fuzzy and equal to that, this
     * method will loop forever. To avoid this behavior, use {@link Real#ne(Rational, Rational)} instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code this} cannot be fuzzy and equal to {@code that}.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @return x≠y
     */
    public boolean neUnsafe(@NotNull Rational that) {
        return compareToUnsafe(that) != 0;
    }

    /**
     * Determines whether {@code this} is less than {@code that}. If {@code this} is fuzzy on the left and equal to
     * that, this method will loop forever. To avoid this behavior, use {@link Real#lt(Rational, Rational)} instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code this} cannot be fuzzy on the left and equal to {@code that}.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @return x{@literal <}y
     */
    public boolean ltUnsafe(@NotNull Rational that) {
        if (rational.isPresent()) {
            return Ordering.lt(rational.get(), that);
        }
        for (Interval interval : intervals) {
            if (interval.getLower().isPresent() && interval.getLower().get().equals(that)) {
                return false;
            } else if (!interval.contains(that)) {
                Rational sample = interval.getLower().isPresent() ?
                        interval.getLower().get() :
                        interval.getUpper().get();
                return Ordering.lt(sample, that);
            }
        }
        throw new IllegalStateException("unreachable");
    }

    /**
     * Determines whether {@code this} is greater than {@code that}. If {@code this} is fuzzy on the right and equal to
     * that, this method will loop forever. To avoid this behavior, use {@link Real#gt(Rational, Rational)} instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code this} cannot be fuzzy on the right and equal to {@code that}.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @return x{@literal >}y
     */
    public boolean gtUnsafe(@NotNull Rational that) {
        if (rational.isPresent()) {
            return Ordering.gt(rational.get(), that);
        }
        for (Interval interval : intervals) {
            if (interval.getUpper().isPresent() && interval.getUpper().get().equals(that)) {
                return false;
            } else if (!interval.contains(that)) {
                Rational sample = interval.getLower().isPresent() ?
                        interval.getLower().get() :
                        interval.getUpper().get();
                return Ordering.gt(sample, that);
            }
        }
        throw new IllegalStateException("unreachable");
    }

    /**
     * Determines whether {@code this} is less than or equal to {@code that}. If {@code this} is fuzzy on the right and
     * equal to that, this method will loop forever. To avoid this behavior, use {@link Real#le(Rational, Rational)}
     * instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code this} cannot be fuzzy on the right and equal to {@code that}.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @return x≤y
     */
    public boolean leUnsafe(@NotNull Rational that) {
        if (rational.isPresent()) {
            return Ordering.le(rational.get(), that);
        }
        for (Interval interval : intervals) {
            if (interval.getUpper().isPresent() && interval.getUpper().get().equals(that)) {
                return true;
            } else if (!interval.contains(that)) {
                Rational sample = interval.getLower().isPresent() ?
                        interval.getLower().get() :
                        interval.getUpper().get();
                return Ordering.lt(sample, that);
            }
        }
        throw new IllegalStateException("unreachable");
    }

    /**
     * Determines whether {@code this} is greater than or equal to {@code that}. If {@code this} is fuzzy on the left
     * and equal to that, this method will loop forever. To avoid this behavior, use
     * {@link Real#ge(Rational, Rational)} instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code this} cannot be fuzzy on the left and equal to {@code that}.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @return x≥y
     */
    public boolean geUnsafe(@NotNull Rational that) {
        if (rational.isPresent()) {
            return Ordering.ge(rational.get(), that);
        }
        for (Interval interval : intervals) {
            if (interval.getLower().isPresent() && interval.getLower().get().equals(that)) {
                return true;
            } else if (!interval.contains(that)) {
                Rational sample = interval.getLower().isPresent() ?
                        interval.getLower().get() :
                        interval.getUpper().get();
                return Ordering.gt(sample, that);
            }
        }
        throw new IllegalStateException("unreachable");
    }

    /**
     * Determines whether {@code this} is equal to {@code that}. If {@code this} is fuzzy and equal to that, this
     * method will give up and return empty once the approximating interval's diameter is less than the specified
     * resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x=y
     */
    public @NotNull Optional<Boolean> eq(@NotNull Rational that, @NotNull Rational resolution) {
        return compareTo(that, resolution).map(c -> c == 0);
    }

    /**
     * Determines whether {@code this} is unequal to {@code that}. If {@code this} is fuzzy and equal to that, this
     * method will give up and return empty once the approximating interval's diameter is less than the specified
     * resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x≠y
     */
    public @NotNull Optional<Boolean> ne(@NotNull Rational that, @NotNull Rational resolution) {
        return compareTo(that, resolution).map(c -> c != 0);
    }

    /**
     * Determines whether {@code this} is less than {@code that}. If {@code this} is fuzzy on the left and equal to
     * that, this method will give up and return empty once the approximating interval's diameter is less than the
     * specified resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x{@literal <}y
     */
    public @NotNull Optional<Boolean> lt(@NotNull Rational that, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(Ordering.lt(rational.get(), that));
        }
        for (Interval interval : intervals) {
            if (interval.getLower().isPresent() && interval.getLower().get().equals(that)) {
                return Optional.of(false);
            } else if (!interval.contains(that)) {
                Rational sample = interval.getLower().isPresent() ?
                        interval.getLower().get() :
                        interval.getUpper().get();
                return Optional.of(Ordering.lt(sample, that));
            }
            if (interval.isFinitelyBounded() && Ordering.lt(interval.diameter().get(), resolution)) {
                return Optional.empty();
            }
        }
        throw new IllegalStateException("unreachable");
    }

    /**
     * Determines whether {@code this} is greater than {@code that}. If {@code this} is fuzzy on the right and equal to
     * that, this method will give up and return empty once the approximating interval's diameter is less than the
     * specified resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x{@literal >}y
     */
    public @NotNull Optional<Boolean> gt(@NotNull Rational that, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(Ordering.gt(rational.get(), that));
        }
        for (Interval interval : intervals) {
            if (interval.getUpper().isPresent() && interval.getUpper().get().equals(that)) {
                return Optional.of(false);
            } else if (!interval.contains(that)) {
                Rational sample = interval.getLower().isPresent() ?
                        interval.getLower().get() :
                        interval.getUpper().get();
                return Optional.of(Ordering.gt(sample, that));
            }
            if (interval.isFinitelyBounded() && Ordering.lt(interval.diameter().get(), resolution)) {
                return Optional.empty();
            }
        }
        throw new IllegalStateException("unreachable");
    }

    /**
     * Determines whether {@code this} is less than or equal to {@code that}. If {@code this} is fuzzy on the right and
     * equal to that, this method will give up and return empty once the approximating interval's diameter is less than
     * the specified resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x≤y
     */
    public @NotNull Optional<Boolean> le(@NotNull Rational that, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(Ordering.le(rational.get(), that));
        }
        for (Interval interval : intervals) {
            if (interval.getUpper().isPresent() && interval.getUpper().get().equals(that)) {
                return Optional.of(true);
            } else if (!interval.contains(that)) {
                Rational sample = interval.getLower().isPresent() ?
                        interval.getLower().get() :
                        interval.getUpper().get();
                return Optional.of(Ordering.lt(sample, that));
            }
            if (interval.isFinitelyBounded() && Ordering.lt(interval.diameter().get(), resolution)) {
                return Optional.empty();
            }
        }
        throw new IllegalStateException("unreachable");
    }

    /**
     * Determines whether {@code this} is greater than or equal to {@code that}. If {@code this} is fuzzy on the left
     * and equal to that, this method will give up and return empty once the approximating interval's diameter is less
     * than the specified resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Rational} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x≥y
     */
    public @NotNull Optional<Boolean> ge(@NotNull Rational that, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (rational.isPresent()) {
            return Optional.of(Ordering.ge(rational.get(), that));
        }
        for (Interval interval : intervals) {
            if (interval.getLower().isPresent() && interval.getLower().get().equals(that)) {
                return Optional.of(true);
            } else if (!interval.contains(that)) {
                Rational sample = interval.getLower().isPresent() ?
                        interval.getLower().get() :
                        interval.getUpper().get();
                return Optional.of(Ordering.gt(sample, that));
            }
            if (interval.isFinitelyBounded() && Ordering.lt(interval.diameter().get(), resolution)) {
                return Optional.empty();
            }
        }
        throw new IllegalStateException("unreachable");
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. If {@code this} is equal to {@code that}, but {@code this} and {@code that} are
     * different objects that are not both exact, this method will loop forever. To avoid this behavior, use
     * {@link Real#compareTo(Real, Rational)} instead.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>If {@code this} is equal to {@code that}, they must be either be the same object or both must be
     *  exact.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    public int compareToUnsafe(@NotNull Real that) {
        if (this == that) return 0;
        if (rational.isPresent()) return -that.compareToUnsafe(rational.get());
        if (that.rational.isPresent()) return compareToUnsafe(that.rational.get());
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        Interval a = thisIntervals.next();
        Interval b = thatIntervals.next();
        Optional<Rational> aDiameter = a.diameter();
        Optional<Rational> bDiameter = b.diameter();
        while (true) {
            Optional<Ordering> o = thisIntervals.next().elementCompare(thatIntervals.next());
            if (o.isPresent()) {
                return o.get().toInt();
            }
            if (compareDiameters(aDiameter, bDiameter) == LT) {
                b = thatIntervals.next();
                bDiameter = b.diameter();
            } else {
                a = thisIntervals.next();
                aDiameter = a.diameter();
            }
        }
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively. If {@code this} is equal to {@code that}, but {@code this} and {@code that} are
     * different objects that are not both exact, this method will give up and return empty once the approximating
     * interval's diameter is less than the specified resolution.
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>{@code resolution} must be positive.</li>
     *  <li>The result may be –1, 0, 1, or empty.</li>
     * </ul>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return {@code this} compared to {@code that}
     */
    public @NotNull Optional<Integer> compareTo(@NotNull Real that, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (this == that) return Optional.of(0);
        if (rational.isPresent()) return that.compareTo(rational.get(), resolution).map(i -> -i);
        if (that.rational.isPresent()) return compareTo(that.rational.get(), resolution);
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        Interval a = thisIntervals.next();
        Interval b = thatIntervals.next();
        Optional<Rational> aDiameter = a.diameter();
        Optional<Rational> bDiameter = b.diameter();
        while (true) {
            Optional<Ordering> o = a.elementCompare(b);
            if (o.isPresent()) {
                return Optional.of(o.get().toInt());
            }
            if (aDiameter.isPresent() && bDiameter.isPresent() &&
                    Ordering.lt(aDiameter.get(), resolution) && Ordering.lt(bDiameter.get(), resolution)) {
                return Optional.empty();
            }
            if (compareDiameters(aDiameter, bDiameter) == LT) {
                b = thatIntervals.next();
                bDiameter = b.diameter();
            } else {
                a = thisIntervals.next();
                aDiameter = a.diameter();
            }
        }
    }

    /**
     * Determines whether {@code this} is equal to {@code that}. If {@code this} is equal to {@code that}, but
     * {@code this} and {@code that} are different objects that are not both exact, this method will loop forever. To
     * avoid this behavior, use {@link Real#eq(Real, Rational)} instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>If {@code this} is equal to {@code that}, they must be either be the same object or both must be
     *  exact.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @return x=y
     */
    public boolean eqUnsafe(@NotNull Real that) {
        return compareToUnsafe(that) == 0;
    }

    /**
     * Determines whether {@code this} is unequal to {@code that}. If {@code this} is equal to {@code that}, but
     * {@code this} and {@code that} are different objects that are not both exact, this method will loop forever. To
     * avoid this behavior, use {@link Real#ne(Real, Rational)} instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>If {@code this} is equal to {@code that}, they must be either be the same object or both must be
     *  exact.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @return x≠y
     */
    public boolean neUnsafe(@NotNull Real that) {
        return compareToUnsafe(that) != 0;
    }

    /**
     * Determines whether {@code this} is less than {@code that}. If {@code this} is equal to {@code that}, but
     * {@code this} and {@code that} are different objects that are not both exact, this method will loop forever. To
     * avoid this behavior, use {@link Real#lt(Real, Rational)} instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>If {@code this} is equal to {@code that}, they must be either be the same object or {@code this} must be
     * non-fuzzy on the left and {@code that} must be non-fuzzy on the right.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @return x{@literal <}y
     */
    public boolean ltUnsafe(@NotNull Real that) {
        if (this == that) return false;
        if (rational.isPresent()) return that.gtUnsafe(rational.get());
        if (that.rational.isPresent()) return ltUnsafe(that.rational.get());
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        Interval a = thisIntervals.next();
        Interval b = thatIntervals.next();
        Optional<Rational> aDiameter = a.diameter();
        Optional<Rational> bDiameter = b.diameter();
        while (true) {
            Optional<Rational> aLower = a.getLower();
            Optional<Rational> aUpper = a.getUpper();
            Optional<Rational> bLower = b.getLower();
            Optional<Rational> bUpper = b.getUpper();
            if (aLower.isPresent() && bUpper.isPresent() && Ordering.ge(aLower.get(), bUpper.get())) {
                return false;
            } else if (aUpper.isPresent() && bLower.isPresent() && Ordering.lt(aUpper.get(), bLower.get())) {
                return true;
            }
            if (compareDiameters(aDiameter, bDiameter) == LT) {
                b = thatIntervals.next();
                bDiameter = b.diameter();
            } else {
                a = thisIntervals.next();
                aDiameter = a.diameter();
            }
        }
    }

    /**
     * Determines whether {@code this} is greater than {@code that}. If {@code this} is equal to {@code that}, but
     * {@code this} and {@code that} are different objects that are not both exact, this method will loop forever. To
     * avoid this behavior, use {@link Real#gt(Real, Rational)} instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>If {@code this} is equal to {@code that}, they must be either be the same object or {@code this} must be
     * non-fuzzy on the right and {@code that} must be non-fuzzy on the left.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @return x{@literal >}y
     */
    public boolean gtUnsafe(@NotNull Real that) {
        if (this == that) return false;
        if (rational.isPresent()) return that.ltUnsafe(rational.get());
        if (that.rational.isPresent()) return gtUnsafe(that.rational.get());
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        Interval a = thisIntervals.next();
        Interval b = thatIntervals.next();
        Optional<Rational> aDiameter = a.diameter();
        Optional<Rational> bDiameter = b.diameter();
        while (true) {
            Optional<Rational> aLower = a.getLower();
            Optional<Rational> aUpper = a.getUpper();
            Optional<Rational> bLower = b.getLower();
            Optional<Rational> bUpper = b.getUpper();
            if (aUpper.isPresent() && bLower.isPresent() && Ordering.le(aUpper.get(), bLower.get())) {
                return false;
            } else if (aLower.isPresent() && bUpper.isPresent() && Ordering.gt(aLower.get(), bUpper.get())) {
                return true;
            }
            if (compareDiameters(aDiameter, bDiameter) == LT) {
                b = thatIntervals.next();
                bDiameter = b.diameter();
            } else {
                a = thisIntervals.next();
                aDiameter = a.diameter();
            }
        }
    }

    /**
     * Determines whether {@code this} is less than or equal to {@code that}. If {@code this} is equal to {@code that},
     * but {@code this} and {@code that} are different objects and {@code this} is fuzzy on the right while
     * {@code that} is fuzzy on the left, this method will loop forever. To avoid this behavior, use
     * {@link Real#le(Real, Rational)} instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>If {@code this} is equal to {@code that}, they must be either be the same object or {@code this} must be
     * non-fuzzy on the right and {@code that} must be non-fuzzy on the left.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @return x≤y
     */
    public boolean leUnsafe(@NotNull Real that) {
        if (this == that) return true;
        if (rational.isPresent()) return that.geUnsafe(rational.get());
        if (that.rational.isPresent()) return leUnsafe(that.rational.get());
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        Interval a = thisIntervals.next();
        Interval b = thatIntervals.next();
        Optional<Rational> aDiameter = a.diameter();
        Optional<Rational> bDiameter = b.diameter();
        while (true) {
            Optional<Rational> aLower = a.getLower();
            Optional<Rational> aUpper = a.getUpper();
            Optional<Rational> bLower = b.getLower();
            Optional<Rational> bUpper = b.getUpper();
            if (aUpper.isPresent() && bLower.isPresent() && Ordering.le(aUpper.get(), bLower.get())) {
                return true;
            } else if (aLower.isPresent() && bUpper.isPresent() && Ordering.gt(aLower.get(), bUpper.get())) {
                return false;
            }
            if (compareDiameters(aDiameter, bDiameter) == LT) {
                b = thatIntervals.next();
                bDiameter = b.diameter();
            } else {
                a = thisIntervals.next();
                aDiameter = a.diameter();
            }
        }
    }

    /**
     * Determines whether {@code this} is greater than or equal to {@code that}. If {@code this} is equal to
     * {@code that}, but {@code this} and {@code that} are different objects and {@code this} is fuzzy on the left
     * while {@code that} is fuzzy on the right, this method will loop forever. To avoid this behavior, use
     * {@link Real#ge(Real, Rational)} instead.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>If {@code this} is equal to {@code that}, they must be either be the same object or {@code this} must be
     * non-fuzzy on the left and {@code that} must be non-fuzzy on the right.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @return x≥y
     */
    public boolean geUnsafe(@NotNull Real that) {
        if (this == that) return true;
        if (rational.isPresent()) return that.leUnsafe(rational.get());
        if (that.rational.isPresent()) return geUnsafe(that.rational.get());
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        Interval a = thisIntervals.next();
        Interval b = thatIntervals.next();
        Optional<Rational> aDiameter = a.diameter();
        Optional<Rational> bDiameter = b.diameter();
        while (true) {
            Optional<Rational> aLower = a.getLower();
            Optional<Rational> aUpper = a.getUpper();
            Optional<Rational> bLower = b.getLower();
            Optional<Rational> bUpper = b.getUpper();
            if (aLower.isPresent() && bUpper.isPresent() && Ordering.ge(aLower.get(), bUpper.get())) {
                return true;
            } else if (aUpper.isPresent() && bLower.isPresent() && Ordering.lt(aUpper.get(), bLower.get())) {
                return false;
            }
            if (compareDiameters(aDiameter, bDiameter) == LT) {
                b = thatIntervals.next();
                bDiameter = b.diameter();
            } else {
                a = thisIntervals.next();
                aDiameter = a.diameter();
            }
        }
    }

    /**
     * Determines whether {@code this} is equal to {@code that}. If {@code this} is equal to {@code that}, but
     * {@code this} and {@code that} are different objects that are not both exact, this method will give up and return
     * empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x=y
     */
    public @NotNull Optional<Boolean> eq(@NotNull Real that, @NotNull Rational resolution) {
        return compareTo(that, resolution).map(c -> c == 0);
    }

    /**
     * Determines whether {@code this} is unequal to {@code that}. If {@code this} is equal to {@code that}, but
     * {@code this} and {@code that} are different objects that are not both exact, this method will give up and return
     * empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x≠y
     */
    public @NotNull Optional<Boolean> ne(@NotNull Real that, @NotNull Rational resolution) {
        return compareTo(that, resolution).map(c -> c != 0);
    }

    /**
     * Determines whether {@code this} is less than {@code that}. If {@code this} is equal to {@code that}, but
     * {@code this} and {@code that} are different objects that are not both exact, this method will give up and return
     * empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x{@literal <}y
     */
    public @NotNull Optional<Boolean> lt(@NotNull Real that, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (this == that) return Optional.of(false);
        if (rational.isPresent()) return that.gt(rational.get(), resolution);
        if (that.rational.isPresent()) return lt(that.rational.get(), resolution);
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        Interval a = thisIntervals.next();
        Interval b = thatIntervals.next();
        Optional<Rational> aDiameter = a.diameter();
        Optional<Rational> bDiameter = b.diameter();
        while (true) {
            Interval thisInterval = thisIntervals.next();
            Interval thatInterval = thatIntervals.next();
            Optional<Rational> aLower = thisInterval.getLower();
            Optional<Rational> aUpper = thisInterval.getUpper();
            Optional<Rational> bLower = thatInterval.getLower();
            Optional<Rational> bUpper = thatInterval.getUpper();
            if (aLower.isPresent() && bUpper.isPresent() && Ordering.ge(aLower.get(), bUpper.get())) {
                return Optional.of(false);
            } else if (aUpper.isPresent() && bLower.isPresent() && Ordering.lt(aUpper.get(), bLower.get())) {
                return Optional.of(true);
            } else if (aLower.isPresent() && aUpper.isPresent() && bLower.isPresent() &&
                    bUpper.isPresent() && Ordering.lt(aDiameter.get(), resolution) &&
                    Ordering.lt(bDiameter.get(), resolution)) {
                return Optional.empty();
            }
            if (compareDiameters(aDiameter, bDiameter) == LT) {
                b = thatIntervals.next();
                bDiameter = b.diameter();
            } else {
                a = thisIntervals.next();
                aDiameter = a.diameter();
            }
        }
    }

    /**
     * Determines whether {@code this} is greater than {@code that}. If {@code this} is equal to {@code that}, but
     * {@code this} and {@code that} are different objects that are not both exact, this method will give up and return
     * empty once the approximating interval's diameter is less than the specified resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x{@literal >}y
     */
    public @NotNull Optional<Boolean> gt(@NotNull Real that, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (this == that) return Optional.of(false);
        if (rational.isPresent()) return that.lt(rational.get(), resolution);
        if (that.rational.isPresent()) return gt(that.rational.get(), resolution);
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        Interval a = thisIntervals.next();
        Interval b = thatIntervals.next();
        Optional<Rational> aDiameter = a.diameter();
        Optional<Rational> bDiameter = b.diameter();
        while (true) {
            Optional<Rational> aLower = a.getLower();
            Optional<Rational> aUpper = a.getUpper();
            Optional<Rational> bLower = b.getLower();
            Optional<Rational> bUpper = b.getUpper();
            if (aUpper.isPresent() && bLower.isPresent() && Ordering.le(aUpper.get(), bLower.get())) {
                return Optional.of(false);
            } else if (aLower.isPresent() && bUpper.isPresent() && Ordering.gt(aLower.get(), bUpper.get())) {
                return Optional.of(true);
            } else if (aLower.isPresent() && aUpper.isPresent() && bLower.isPresent() &&
                    bUpper.isPresent() && Ordering.lt(aDiameter.get(), resolution) &&
                    Ordering.lt(bDiameter.get(), resolution)) {
                return Optional.empty();
            }
            if (compareDiameters(aDiameter, bDiameter) == LT) {
                b = thatIntervals.next();
                bDiameter = b.diameter();
            } else {
                a = thisIntervals.next();
                aDiameter = a.diameter();
            }
        }
    }

    /**
     * Determines whether {@code this} is less than or equal to {@code that}. If {@code this} is equal to {@code that},
     * but {@code this} and {@code that} are different objects and {@code this} is fuzzy on the right while
     * {@code that} is fuzzy on the left, this method will give up and return empty once the approximating interval's
     * diameter is less than the specified resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x≤y
     */
    public @NotNull Optional<Boolean> le(@NotNull Real that, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (this == that) return Optional.of(true);
        if (rational.isPresent()) return that.ge(rational.get(), resolution);
        if (that.rational.isPresent()) return le(that.rational.get(), resolution);
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        Interval a = thisIntervals.next();
        Interval b = thatIntervals.next();
        Optional<Rational> aDiameter = a.diameter();
        Optional<Rational> bDiameter = b.diameter();
        while (true) {
            Optional<Rational> aLower = a.getLower();
            Optional<Rational> aUpper = a.getUpper();
            Optional<Rational> bLower = b.getLower();
            Optional<Rational> bUpper = b.getUpper();
            if (aUpper.isPresent() && bLower.isPresent() && Ordering.le(aUpper.get(), bLower.get())) {
                return Optional.of(true);
            } else if (aLower.isPresent() && bUpper.isPresent() && Ordering.gt(aLower.get(), bUpper.get())) {
                return Optional.of(false);
            } else if (aLower.isPresent() && aUpper.isPresent() && bLower.isPresent() &&
                    bUpper.isPresent() && Ordering.lt(aDiameter.get(), resolution) &&
                    Ordering.lt(bDiameter.get(), resolution)) {
                return Optional.empty();
            }
            if (compareDiameters(aDiameter, bDiameter) == LT) {
                b = thatIntervals.next();
                bDiameter = b.diameter();
            } else {
                a = thisIntervals.next();
                aDiameter = a.diameter();
            }
        }
    }

    /**
     * Determines whether {@code this} is greater than or equal to {@code that}. If {@code this} is equal to
     * {@code that}, but {@code this} and {@code that} are different objects and {@code this} is fuzzy on the left
     * while {@code that} is fuzzy on the right, this method will give up and return empty once the approximating
     * interval's diameter is less than the specified resolution.
     *
     * <li>{@code this} may be any {@code Real}.</li>
     * <li>{@code that} cannot be null.</li>
     * <li>{@code resolution} must be positive.</li>
     * <li>The result may be either {@code boolean}.</li>
     *
     * @param that the {@code Real} to be compared with {@code this}
     * @param resolution once the approximating interval's diameter is lower than this value, the method gives up
     * @return x≥y
     */
    public @NotNull Optional<Boolean> ge(@NotNull Real that, @NotNull Rational resolution) {
        if (resolution.signum() != 1) {
            throw new IllegalArgumentException("resolution must be positive. Invalid resolution: " + resolution);
        }
        if (this == that) return Optional.of(true);
        if (rational.isPresent()) return that.le(rational.get(), resolution);
        if (that.rational.isPresent()) return ge(that.rational.get(), resolution);
        Iterator<Interval> thisIntervals = intervals.iterator();
        Iterator<Interval> thatIntervals = that.intervals.iterator();
        Interval a = thisIntervals.next();
        Interval b = thatIntervals.next();
        Optional<Rational> aDiameter = a.diameter();
        Optional<Rational> bDiameter = b.diameter();
        while (true) {
            Interval thisInterval = thisIntervals.next();
            Interval thatInterval = thatIntervals.next();
            Optional<Rational> aLower = thisInterval.getLower();
            Optional<Rational> aUpper = thisInterval.getUpper();
            Optional<Rational> bLower = thatInterval.getLower();
            Optional<Rational> bUpper = thatInterval.getUpper();
            if (aLower.isPresent() && bUpper.isPresent() && Ordering.ge(aLower.get(), bUpper.get())) {
                return Optional.of(true);
            } else if (aUpper.isPresent() && bLower.isPresent() && Ordering.lt(aUpper.get(), bLower.get())) {
                return Optional.of(false);
            } else if (aLower.isPresent() && aUpper.isPresent() && bLower.isPresent() &&
                    bUpper.isPresent() && Ordering.lt(aDiameter.get(), resolution) &&
                    Ordering.lt(bDiameter.get(), resolution)) {
                return Optional.empty();
            }
            if (compareDiameters(aDiameter, bDiameter) == LT) {
                b = thatIntervals.next();
                bDiameter = b.diameter();
            } else {
                a = thisIntervals.next();
                aDiameter = a.diameter();
            }
        }
    }

    /**
     * Creates a {@code String} representation of {@code this} (see
     * {@link Real#toStringBase(BigInteger, int, Rational)} for details).
     *
     * <ul>
     *  <li>{@code this} may be any {@code Real}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        return toStringBase(BigInteger.TEN, TINY_LIMIT, DEFAULT_RESOLUTION);
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
