package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.wheels.math.MathUtils;
import mho.wheels.ordering.Ordering;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Optional;

import static mho.wheels.testing.Testing.*;

public class AlgebraicAngle {
    private static final @NotNull AlgebraicAngle ZERO = new AlgebraicAngle(Rational.ZERO);

    private static final @NotNull AlgebraicAngle PI = new AlgebraicAngle(Rational.ONE_HALF);

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
            if (Algebraic.cosMP(d).equals(mp)) {
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
            cosine = Optional.of(Algebraic.cosHelper(turns.get()));
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
            cosine.ifPresent(x -> assertEquals(this, Algebraic.cosHelper(t), x));
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
