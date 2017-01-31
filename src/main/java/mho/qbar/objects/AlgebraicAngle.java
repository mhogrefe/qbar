package mho.qbar.objects;

import mho.wheels.math.MathUtils;
import mho.wheels.ordering.Ordering;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Optional;

import static mho.wheels.testing.Testing.*;

/**
 * <p>The {@code AlgebraicAngle} class uniquely represents algebraic angles, or numbers in [0, 2π) whose cosine is
 * algebraic (equivalently, numbers t in [0, 2π) such that exp(it) is algebraic). If the angle is a rational multiple
 * of π, {@code turns} will be present, and equal to a fraction of the unit circle, in [0, 1). {@code cosine}, the
 * angle's cosine, may or may not be present in this case. If the angle is not a rational multiple of π, {@code cosine}
 * must be present (and, of course, {@code turns} will not be). {@code quadrant} is the quadrant of the plane that the
 * angle is in: 1 if it is in [0, π/2), 2 if it is in [π/2, π), 3 if it is in [π, 3π/2], and 4 if it is in [3π/2, 2π).
 * {@code quadrant} is always present, but it essential when the angle is not a rational multiple of π, since in this
 * case {@code cosine} is not sufficient to determine the angle.
 *
 * <p>This class is immutable.</p>
 */
public class AlgebraicAngle implements Comparable<AlgebraicAngle> {
    /**
     * 0
     */
    private static final @NotNull AlgebraicAngle ZERO = new AlgebraicAngle(Rational.ZERO);

    /**
     * π/4
     */
    private static final @NotNull AlgebraicAngle PI_OVER_FOUR = new AlgebraicAngle(Rational.of(1, 8));

    /**
     * π/2
     */
    private static final @NotNull AlgebraicAngle PI_OVER_TWO = new AlgebraicAngle(Rational.of(1, 4));

    /**
     * 3π/4
     */
    private static final @NotNull AlgebraicAngle THREE_PI_OVER_FOUR = new AlgebraicAngle(Rational.of(3, 8));

    /**
     * π
     */
    private static final @NotNull AlgebraicAngle PI = new AlgebraicAngle(Rational.ONE_HALF);

    /**
     * 5π/4
     */
    private static final @NotNull AlgebraicAngle FIVE_PI_OVER_FOUR = new AlgebraicAngle(Rational.of(5, 8));

    /**
     * 3π/2
     */
    private static final @NotNull AlgebraicAngle THREE_PI_OVER_TWO = new AlgebraicAngle(Rational.of(3, 4));

    /**
     * 7π/4
     */
    private static final @NotNull AlgebraicAngle SEVEN_PI_OVER_FOUR = new AlgebraicAngle(Rational.of(7, 8));

    private final @NotNull Optional<Rational> turns;

    private @NotNull Optional<Algebraic> cosine;

    private final int quadrant;

    private AlgebraicAngle(@NotNull Rational turns) {
        this.turns = Optional.of(turns);
        this.cosine = Optional.empty();
        quadrant = turns.shiftLeft(2).floor().intValueExact() + 1;
    }

    private AlgebraicAngle(@NotNull Algebraic cosine, int quadrant) {
        this.turns = Optional.empty();
        this.cosine = Optional.of(cosine);
        this.quadrant = quadrant;
    }

    private static @NotNull Optional<Rational> arccosHelper(@NotNull Algebraic x) {
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

    public static @NotNull AlgebraicAngle fromTurns(@NotNull Rational turns) {
        if (turns.signum() == -1) {
            throw new IllegalArgumentException();
        }
        if (Ordering.ge(turns, Rational.ONE)) {
            throw new IllegalArgumentException();
        }
        return new AlgebraicAngle(turns);
    }

    public static @NotNull AlgebraicAngle fromDegrees(@NotNull Rational degrees) {
        return fromTurns(degrees.divide(360));
    }

    public boolean isRationalMultipleOfPi() {
        return turns.isPresent();
    }

    public @NotNull Optional<Rational> rationalTurns() {
        return turns;
    }

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

    public @NotNull Real radians() {
        if (turns.isPresent()) {
            return Real.PI.shiftLeft(1).multiply(turns.get());
        } else if (quadrant <= 2) {
            return cosine.get().realValue().arccosUnsafe();
        } else {
            return cosine.get().realValue().negate().arccosUnsafe().add(Real.PI);
        }
    }

    public @NotNull Real degrees() {
        if (turns.isPresent()) {
            return Real.of(turns.get().multiply(360));
        } else if (quadrant <= 2) {
            return cosine.get().realValue().arccosUnsafe().divide(360);
        } else {
            return cosine.get().realValue().negate().arccosUnsafe().divide(360).add(Rational.of(180));
        }
    }

    public @NotNull AlgebraicAngle negate() {
        if (turns.isPresent()) {
            AlgebraicAngle negative = new AlgebraicAngle(turns.get().negate().fractionalPart());
            negative.cosine = cosine;
            return negative;
        } else {
            return new AlgebraicAngle(cosine.get(), 5 - quadrant);
        }
    }

    public @NotNull AlgebraicAngle reflectAcrossOrigin() {
        if (turns.isPresent()) {
            AlgebraicAngle reflected = new AlgebraicAngle(turns.get().add(Rational.ONE_HALF).fractionalPart());
            if (cosine.isPresent()) {
                reflected.cosine = cosine.map(Algebraic::negate);
            }
            return reflected;
        } else {
            int reflectedQuadrant = quadrant + 2;
            if (reflectedQuadrant > 4) reflectedQuadrant -= 4;
            return new AlgebraicAngle(cosine.get().negate(), reflectedQuadrant);
        }
    }

    public @NotNull Algebraic cos() {
        if (!cosine.isPresent()) {
            cosine = Optional.of(Algebraic.cosOfTurns(turns.get()));
        }
        return cosine.get();
    }

    public static @NotNull AlgebraicAngle arccos(@NotNull Algebraic x) {
        Optional<Rational> ot = arccosHelper(x);
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
        } else if (!turns.isPresent() && !that.turns.isPresent()) {
            int c = cosine.get().compareTo(that.cosine.get());
            return quadrant <= 2 ? -c : c;
        } else if (turns.isPresent()) {
            Real cosThis = cosine.isPresent() ?
                    cosine.get().realValue() :
                    Real.PI.shiftLeft(1).multiply(turns.get()).cos();
            int c = cosThis.compareToUnsafe(that.cosine.get().realValue());
            return quadrant <= 2 ? -c : c;
        } else {
            Real cosThat = that.cosine.isPresent() ?
                    that.cosine.get().realValue() :
                    Real.PI.shiftLeft(1).multiply(that.turns.get()).cos();
            int c = cosine.get().realValue().compareToUnsafe(cosThat);
            return quadrant <= 2 ? -c : c;
        }
    }

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
            assertFalse(this, arccosHelper(x).isPresent());
        }
    }
}
