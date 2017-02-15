package mho.qbar.objects;

import mho.wheels.io.Readers;
import mho.wheels.iterables.IterableUtils;
import mho.wheels.math.MathUtils;
import mho.wheels.ordering.Ordering;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Optional;
import java.util.function.Function;

import static mho.wheels.testing.Testing.*;

/**
 * <p>The {@code AlgebraicAngle} class uniquely represents algebraic angles, or numbers in [0, 2π) whose cosine is
 * algebraic (equivalently, numbers t in [0, 2π) such that exp(it) is algebraic). If the angle is a rational multiple
 * of π, {@code turns} will be present, and equal to a fraction of the unit circle, in [0, 1). {@code cosine}, the
 * angle's cosine, may or may not be present in this case. If the angle is not a rational multiple of π, {@code cosine}
 * must be present (and, of course, {@code turns} will not be). {@code quadrant} is the quadrant of the plane that the
 * angle is in: 1 if it is in [0, π/2), 2 if it is in [π/2, π), 3 if it is in [π, 3π/2), and 4 if it is in [3π/2, 2π).
 * {@code quadrant} is always present. If the angle is a rational multiple of π, it's redundant, but when the angle is
 * not a rational multiple of π it is essential since, {@code cosine} alone is not sufficient to determine the angle.
 *
 * <p>This class is immutable.</p>
 */
public final class AlgebraicAngle implements Comparable<AlgebraicAngle> {
    /**
     * 0
     */
    public static final @NotNull AlgebraicAngle ZERO = new AlgebraicAngle(Rational.ZERO);

    /**
     * π/4
     */
    public static final @NotNull AlgebraicAngle PI_OVER_FOUR = new AlgebraicAngle(Rational.of(1, 8));

    /**
     * π/2
     */
    public static final @NotNull AlgebraicAngle PI_OVER_TWO = new AlgebraicAngle(Rational.of(1, 4));

    /**
     * 3π/4
     */
    public static final @NotNull AlgebraicAngle THREE_PI_OVER_FOUR = new AlgebraicAngle(Rational.of(3, 8));

    /**
     * π
     */
    public static final @NotNull AlgebraicAngle PI = new AlgebraicAngle(Rational.ONE_HALF);

    /**
     * 5π/4
     */
    public static final @NotNull AlgebraicAngle FIVE_PI_OVER_FOUR = new AlgebraicAngle(Rational.of(5, 8));

    /**
     * 3π/2
     */
    public static final @NotNull AlgebraicAngle THREE_PI_OVER_TWO = new AlgebraicAngle(Rational.of(3, 4));

    /**
     * 7π/4
     */
    public static final @NotNull AlgebraicAngle SEVEN_PI_OVER_FOUR = new AlgebraicAngle(Rational.of(7, 8));

    /**
     * The fraction of a unit circle that {@code this} is. If {@code this} is not a rational multiple of π,
     * {@code turns} is empty.
     */
    private final @NotNull Optional<Rational> turns;

    /**
     * The cosine of {@code this}. Must be present if {@code turns} is empty; otherwise, it's optional.
     */
    private @NotNull Optional<Algebraic> cosine;

    /**
     * The quadrant of the x-y plane that {@code this} lies in:
     * <ul>
     *  <li>1 if 0≤{@code this}<π/2</li>
     *  <li>2 if π/2≤{@code this}<π</li>
     *  <li>3 if π≤{@code this}<3π/2</li>
     *  <li>4 if 3π/2≤{@code this}<2π</li>
     * </ul>
     */
    private final int quadrant;

    /**
     * Constructs an {@code AlgebraicAngle} from a fraction of the unit circle.
     *
     * <ul>
     *  <li>{@code turns} cannot be negative and must be less than 1.</li>
     *  <li>Any rational multiple of π may be constructed with this constructor.</li>
     * </ul>
     *
     * @param turns a fraction of the unit circle
     */
    private AlgebraicAngle(@NotNull Rational turns) {
        this.turns = Optional.of(turns);
        this.cosine = Optional.empty();
        quadrant = turns.shiftLeft(2).floor().intValueExact() + 1;
    }

    /**
     * Constructs an angle that is not a rational multiple of π from its cosine and quadrant.
     *
     * <ul>
     *  <li>{@code cosine} must be strictly between –1 and 1, and cannot be the cosine of a rational multiple of
     *  π.</li>
     *  <li>{@code quadrant} must be 1, 2, 3, or 4.</li>
     *  <li>Any angle that is not a rational multiple of π may be constructed with this constructor.</li>
     * </ul>
     *
     * @param cosine the cosine of the constructed angle
     * @param quadrant the quadrant that the constructed angle lies in
     */
    private AlgebraicAngle(@NotNull Algebraic cosine, int quadrant) {
        this.turns = Optional.empty();
        this.cosine = Optional.of(cosine);
        this.quadrant = quadrant;
    }

    /**
     * Returns the fraction of the unit circle associated with an angle whose cosine is a given number. If the angle is
     * not a rational multiple of π, an empty result is returned. As with the standard arccos function, the result is
     * in the first or second quadrant.
     *
     * <ul>
     *  <li>{@code x} must be between –1 and 1, inclusive.</li>
     *  <li>The result is empty, or greater than or equal to 0 and less than or equal to 1/2.</li>
     * </ul>
     *
     * @param x the cosine of an angle
     * @return the fraction of a unit circle corresponding to an angle with cosine {@code x}
     */
    private static @NotNull Optional<Rational> rationalArccos(@NotNull Algebraic x) {
        if (x == Algebraic.ZERO) {
            return Optional.of(Rational.of(1, 4));
        }
        if (x == Algebraic.ONE) {
            return Optional.of(Rational.ZERO);
        }
        if (x.equals(Algebraic.NEGATIVE_ONE)) {
            return Optional.of(Rational.ONE_HALF);
        }
        Polynomial mp = x.minimalPolynomial();
        int rootIndex = x.rootIndex();
        for (BigInteger possibleDenominator : MathUtils.inverseTotient(BigInteger.valueOf(mp.degree() << 1))) {
            int d = possibleDenominator.intValueExact();
            if (Algebraic.turnFractionCosineMinimalPolynomial(d).equals(mp)) {
                int j = 0;
                for (int n = d >> 1; n > 0; n--) {
                    if (MathUtils.gcd(n, d) != 1) {
                        continue;
                    }
                    if (j == rootIndex) {
                        return Optional.of(Rational.of(n, d));
                    }
                    j++;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Creates an angle from a number of turns (fraction of the unit circle).
     *
     * <ul>
     *  <li>{@code turns} must be greater than or equal to 0 and less than 1.</li>
     *  <li>The result is a rational multiple of π.</li>
     * </ul>
     *
     * @param turns a fraction of the unit circle
     * @return the angle corresponding to {@code turns}
     */
    public static @NotNull AlgebraicAngle fromTurns(@NotNull Rational turns) {
        if (turns.signum() == -1) {
            throw new IllegalArgumentException("turns cannot be negative. Invalid turns: " + turns);
        }
        if (Ordering.ge(turns, Rational.ONE)) {
            throw new IllegalArgumentException("turns must be less than 1. Invalid turns: " + turns);
        }
        return new AlgebraicAngle(turns);
    }

    /**
     * Creates an angle from a number of degrees
     *
     * <ul>
     *  <li>{@code turns} must be greater than or equal to 0 and less than 360.</li>
     *  <li>The result is a rational multiple of π.</li>
     * </ul>
     *
     * @param degrees some number of degrees
     * @return the angle with the specified number of degrees
     */
    public static @NotNull AlgebraicAngle fromDegrees(@NotNull Rational degrees) {
        return fromTurns(degrees.divide(360));
    }

    /**
     * Determines whether {@code this} is a rational multiple of π.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>The result may be either {@code boolean}.</li>
     * </ul>
     *
     * @return whether {@code this} is a rational multiple of π
     */
    public boolean isRationalMultipleOfPi() {
        return turns.isPresent();
    }

    /**
     * If {@code this} is a rational multiple of π, returns the fraction of the unit circle represented by
     * {@code this}. Otherwise, returns empty.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>The result is empty or non-negative and less than 1.</li>
     * </ul>
     *
     * @return the fraction of the unit circle represented by {@code this} if {@code this} is a rational multiple of π
     */
    public @NotNull Optional<Rational> rationalTurns() {
        return turns;
    }

    /**
     * Returns the quadrant that this lies in:
     * <ul>
     *  <li>1 if 0≤{@code this}<π/2</li>
     *  <li>2 if π/2≤{@code this}<π</li>
     *  <li>3 if π≤{@code this}<3π/2</li>
     *  <li>4 if 3π/2≤{@code this}<2π</li>
     * </ul>
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>The result is 1, 2, 3, or 4.</li>
     * </ul>
     *
     * @return the quadrant of {@code this}
     */
    public int getQuadrant() {
        return quadrant;
    }

    /**
     * Returns the fraction of the unit circle represented by {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>The result is clean. It is non-negative and less than 1.</li>
     * </ul>
     *
     * @return the number of turns that this angle makes
     */
    public @NotNull Real realTurns() {
        if (turns.isPresent()) {
            return Real.of(turns.get());
        } else if (quadrant <= 2) {
            return cosine.get().realValue().arccosUnsafe().divideUnsafe(Real.PI.shiftLeft(1));
        } else {
            return cosine.get().realValue().negate().arccosUnsafe().divideUnsafe(Real.PI.shiftLeft(1))
                    .add(Rational.ONE_HALF);
        }
    }

    /**
     * Returns {@code this} expressed in radians.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>The result is clean. It is non-negative and less than 2π.</li>
     * </ul>
     *
     * @return {@code this} in radians
     */
    public @NotNull Real radians() {
        if (turns.isPresent()) {
            return Real.PI.shiftLeft(1).multiply(turns.get());
        } else if (quadrant <= 2) {
            return cosine.get().realValue().arccosUnsafe();
        } else {
            return cosine.get().realValue().negate().arccosUnsafe().add(Real.PI);
        }
    }

    /**
     * Returns {@code this} expressed in degrees.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>The result is clean. It is non-negative and less than 360.</li>
     * </ul>
     *
     * @return {@code this} in degrees
     */
    public @NotNull Real degrees() {
        if (turns.isPresent()) {
            return Real.of(turns.get().multiply(360));
        } else if (quadrant <= 2) {
            return cosine.get().realValue().arccosUnsafe().divideUnsafe(Real.PI).multiply(180);
        } else {
            return cosine.get().realValue().negate().arccosUnsafe().divideUnsafe(Real.PI).multiply(180)
                    .add(Rational.of(180));
        }
    }

    /**
     * Returns the negation of {@code this} mod 2π, or its reflection in the x-axis.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>The result may be any {@code AlgebraicAngle}.</li>
     * </ul>
     *
     * @return –{@code this}
     */
    public @NotNull AlgebraicAngle negate() {
        if (turns.isPresent()) {
            AlgebraicAngle negative = new AlgebraicAngle(turns.get().negate().fractionalPart());
            negative.cosine = cosine;
            return negative;
        } else {
            return new AlgebraicAngle(cosine.get(), 5 - quadrant);
        }
    }

    /**
     * Returns the supplement of {@code this} mod 2π, or its reflection in the y-axis.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>The result may be any {@code AlgebraicAngle}.</li>
     * </ul>
     *
     * @return π–{@code this}
     */
    public @NotNull AlgebraicAngle supplement() {
        if (turns.isPresent()) {
            AlgebraicAngle supplement = new AlgebraicAngle(Rational.ONE_HALF.subtract(turns.get()).fractionalPart());
            if (cosine.isPresent()) {
                supplement.cosine = cosine.map(Algebraic::negate);
            }
            return supplement;
        } else {
            int supplementQuadrant = 3 - quadrant;
            if (supplementQuadrant <= 0) supplementQuadrant += 4;
            return new AlgebraicAngle(cosine.get().negate(), supplementQuadrant);
        }
    }

    /**
     * Returns ({@code this}+π) mod 2π, or its reflection in the origin.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>The result may be any {@code AlgebraicAngle}.</li>
     * </ul>
     *
     * @return {@code this}+π
     */
    public @NotNull AlgebraicAngle addPi() {
        if (turns.isPresent()) {
            AlgebraicAngle sum = new AlgebraicAngle(turns.get().add(Rational.ONE_HALF).fractionalPart());
            if (cosine.isPresent()) {
                sum.cosine = cosine.map(Algebraic::negate);
            }
            return sum;
        } else {
            int sumQuadrant = quadrant + 2;
            if (sumQuadrant > 4) sumQuadrant -= 4;
            return new AlgebraicAngle(cosine.get().negate(), sumQuadrant);
        }
    }

    public @NotNull Algebraic cos() {
        if (!cosine.isPresent()) {
            cosine = Optional.of(Algebraic.cosOfTurns(turns.get()));
        }
        return cosine.get();
    }

    public static @NotNull AlgebraicAngle arccos(@NotNull Algebraic x) {
        Optional<Rational> ot = rationalArccos(x);
        if (ot.isPresent()) {
            return new AlgebraicAngle(ot.get());
        } else {
            return new AlgebraicAngle(x, x.signum() == 1 ? 1 : 2);
        }
    }

    /**
     * Determines whether {@code this} is equal to {@code that}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
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
        AlgebraicAngle angle = (AlgebraicAngle) that;
        if (turns.isPresent() != angle.turns.isPresent()) return false;
        if (turns.isPresent()) {
            return turns.get().equals(angle.turns.get());
        } else {
            return quadrant == angle.quadrant && cosine.get().equals(angle.cosine.get());
        }
    }

    /**
     * Calculates the hash code of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>(conjecture) The result may be any {@code int}.</li>
     * </ul>
     *
     * @return {@code this}'s hash code.
     */
    @Override
    public int hashCode() {
        if (turns.isPresent()) {
            return 31 + turns.get().hashCode();
        } else {
            return 31 * quadrant + cosine.hashCode();
        }
    }

    /**
     * Compares {@code this} to {@code that}, returning 1, –1, or 0 if the answer is "greater than", "less than", or
     * "equal to", respectively.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>{@code that} cannot be null.</li>
     *  <li>The result may be –1, 0, or 1.</li>
     * </ul>
     *
     * @param that the {@code AlgebraicAngle} to be compared with {@code this}
     * @return {@code this} compared to {@code that}
     */
    @Override
    public int compareTo(@NotNull AlgebraicAngle that) {
        if (this.equals(that)) return 0;
        if (quadrant != that.quadrant) {
            return Integer.compare(quadrant, that.quadrant);
        }
        if (turns.isPresent() && that.turns.isPresent()) {
            return turns.get().compareTo(that.turns.get());
        }
        Real cosThis = cosine.isPresent() ?
                    cosine.get().realValue() :
                    Real.PI.shiftLeft(1).multiply(turns.get()).cos();
        Real cosThat = that.cosine.isPresent() ?
                    that.cosine.get().realValue() :
                    Real.PI.shiftLeft(1).multiply(that.turns.get()).cos();
        int c = cosThis.compareToUnsafe(cosThat);
        return quadrant <= 2 ? -c : c;
    }

    /**
     * Creates an {@code AlgebraicAngle} from a {@code String}. Valid input takes the form of a {@code String} that
     * could have been returned by {@link AlgebraicAngle#toString}. This method also takes {@code algebraicHandler},
     * which reads an {@code Algebraic} from a {@code String} if the {@code String} is valid.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>{@code algebraicHandler} must terminate on all possible {@code String}s without throwing an exception, and
     *  cannot return nulls.</li>
     *  <li>The result may be any {@code Optional<AlgebraicAngle>}.</li>
     * </ul>
     *
     * @param s a string representation of an {@code AlgebraicAngle}.
     * @return the wrapped {@code AlgebraicAngle} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    private static @NotNull Optional<AlgebraicAngle> genericRead(
            @NotNull String s,
            @NotNull Function<String, Optional<Algebraic>> algebraicHandler
    ) {
        if (s.equals("0")) {
            return Optional.of(ZERO);
        }
        int piIndex = s.indexOf("pi");
        if (!s.contains("arccos(")) {
            if (piIndex == -1) {
                return Optional.empty();
            }
            if (s.length() == 2) {
                return Optional.of(PI);
            }
            int slashIndex = piIndex + 2;
            if (slashIndex == s.length() || s.charAt(slashIndex) != '/') {
                return Optional.empty();
            }
            Optional<BigInteger> oDenominator = Readers.readBigIntegerStrict(s.substring(slashIndex + 1));
            if (!oDenominator.isPresent()) {
                return Optional.empty();
            }
            BigInteger denominator = oDenominator.get();
            if (denominator.signum() != 1) {
                return Optional.empty();
            }
            BigInteger numerator;
            if (piIndex == 0) {
                numerator = BigInteger.ONE;
            } else {
                if (s.charAt(piIndex - 1) != '*') {
                    return Optional.empty();
                }
                Optional<BigInteger> oNumerator = Readers.readBigIntegerStrict(s.substring(0, piIndex - 1));
                if (!oNumerator.isPresent()) {
                    return Optional.empty();
                }
                numerator = oNumerator.get();
                if (numerator.signum() != 1) {
                    return Optional.empty();
                }
            }
            if (!numerator.gcd(denominator).equals(BigInteger.ONE)) {
                return Optional.empty();
            }
            Rational r = Rational.of(numerator, denominator).shiftRight(1);
            if (Ordering.ge(r, Rational.ONE)) {
                return Optional.empty();
            }
            return Optional.of(fromTurns(r));
        } else {
            if (IterableUtils.last(s) != ')') {
                return Optional.empty();
            }
            boolean greaterThanPi = s.startsWith("pi+");
            if (greaterThanPi) {
                s = s.substring(3);
            }
            if (!s.startsWith("arccos(")) {
                return Optional.empty();
            }
            Optional<Algebraic> ox = algebraicHandler.apply(s.substring(7, s.length() - 1));
            if (!ox.isPresent()) {
                return Optional.empty();
            }
            Algebraic x = ox.get();
            if (Ordering.ge(x.abs(), Algebraic.ONE)) {
                return Optional.empty();
            }
            if (rationalArccos(x).isPresent()) {
                return Optional.empty();
            }
            AlgebraicAngle t = arccos(x);
            if (greaterThanPi) {
                t = t.addPi();
            }
            return Optional.of(t);
        }
    }

    /**
     * Creates an {@code AlgebraicAngle} from a {@code String}. Valid input takes the form of a {@code String} that
     * could have been returned by {@link AlgebraicAngle#toString}. Caution: It's easy to run out of time and memory
     * reading an angle whose cosine has very high degree. If such an input is possible, consider using
     * {@link AlgebraicAngle#readStrict(int, String)} instead.
     *
     * <ul>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<AlgebraicAngle>}.</li>
     * </ul>
     *
     * @param s a string representation of an {@code AlgebraicAngle}.
     * @return the wrapped {@code AlgebraicAngle} represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<AlgebraicAngle> readStrict(@NotNull String s) {
        return genericRead(s, Algebraic::readStrict);
    }

    /**
     * Creates an {@code AlgebraicAngle} from a {@code String}. Valid input takes the form of a {@code String} that
     * could have been returned by {@link AlgebraicAngle#toString}. If the input {@code AlgebraicAngle} is not a
     * rational multiple of π, its cosine cannot have a degree greater than {@code maxDegree}.
     *
     * <ul>
     *  <li>{@code maxDegree} must be at least 2.</li>
     *  <li>{@code s} cannot be null.</li>
     *  <li>The result may be any {@code Optional<AlgbraicAngle>}.</li>
     * </ul>
     *
     * @param maxDegree the largest accepted degree
     * @param s a string representation of an {@code AlgebraicAngle}.
     * @return the wrapped {@code Algebraic} (if not a rational multiple of π, with a cosine of degree no greater than
     * {@code maxDegree}) represented by {@code s}, or {@code empty} if {@code s} is invalid.
     */
    public static @NotNull Optional<AlgebraicAngle> readStrict(int maxDegree, @NotNull String s) {
        if (maxDegree < 2) {
            throw new IllegalArgumentException("maxDegree must be at least 2. Invalid maxDegree: " + maxDegree);
        }
        return genericRead(s, t -> Algebraic.readStrict(maxDegree, t));
    }

    /**
     * Creates a {@code String} representation of {@code this}.
     *
     * <ul>
     *  <li>{@code this} may be any {@code AlgebraicAngle}.</li>
     *  <li>See tests and demos for example results.</li>
     * </ul>
     *
     * @return a {@code String} representation of {@code this}
     */
    public @NotNull String toString() {
        if (turns.isPresent()) {
            Rational t = turns.get();
            if (t == Rational.ZERO) {
                return "0";
            }
            Rational piMultiple = t.shiftLeft(1);
            if (piMultiple == Rational.ONE) {
                return "pi";
            }
            BigInteger n = piMultiple.getNumerator();
            BigInteger d = piMultiple.getDenominator();
            if (n.equals(BigInteger.ONE)) {
                return "pi/" + d;
            } else {
                return n + "*pi/" + d;
            }
        } else {
            if (quadrant <= 2) {
                return "arccos(" + cosine.get() + ")";
            } else {
                return "pi+arccos(" + cosine.get().negate() + ")";
            }
        }
    }

    /**
     * Ensures that {@code this} is valid. Must return without exceptions for any {@code AlgebraicAngle} used outside
     * this class.
     */
    public void validate() {
        if (turns.isPresent()) {
            Rational t = turns.get();
            assertNotEquals(this, t.signum(), -1);
            assertTrue(this, Ordering.lt(t, Rational.ONE));
            cosine.ifPresent(x -> assertEquals(this, Algebraic.cosOfTurns(t), x));
            assertEquals(this, t.shiftLeft(2).floor().intValueExact() + 1, quadrant);
        } else {
            assertTrue(this, cosine.isPresent());
            Algebraic x = cosine.get();
            assertTrue(this, Ordering.lt(x.abs(), Algebraic.ONE));
            assertTrue(
                    this,
                    x.signum() == 1 && (quadrant == 1 || quadrant == 4) ||
                            x.signum() == -1 && (quadrant == 2 || quadrant == 3)
            );
            assertFalse(this, rationalArccos(x).isPresent());
        }
    }
}
