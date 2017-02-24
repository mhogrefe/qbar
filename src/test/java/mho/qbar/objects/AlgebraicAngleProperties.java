package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Optional;

import static mho.qbar.objects.AlgebraicAngle.*;
import static mho.wheels.iterables.IterableUtils.filterInfinite;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.ordering.Ordering.le;
import static mho.wheels.testing.Testing.*;

public class AlgebraicAngleProperties  extends QBarTestProperties {
    private static final @NotNull String ALGEBRAIC_ANGLE_CHARS = " ()*+-/0123456789^acfiopqrstx";
    private static final @NotNull Rational SMALL_RESOLUTION = Rational.of(1, 256);

    public AlgebraicAngleProperties() {
        super("AlgebraicAngle");
    }

    @Override
    protected void testBothModes() {
        propertiesFromTurns();
        propertiesFromDegrees();
        propertiesIsRationalMultipleOfPi();
        propertiesRationalTurns();
        propertiesGetQuadrant();
        propertiesRealTurns();
        propertiesRadians();
        propertiesDegrees();
        propertiesNegate();
        propertiesSupplement();
        propertiesAddPi();
        propertiesSin();
        propertiesCos();
        propertiesTan();
        propertiesCot();
        propertiesSec();
        propertiesCsc();
        propertiesComplement();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesReadStrict();
        propertiesToString();
    }

    private void propertiesFromTurns() {
        initialize("fromTurns(Rational)");
        for (Rational r : take(LIMIT, filterInfinite(x -> x != Rational.ONE, P.range(Rational.ZERO, Rational.ONE)))) {
            AlgebraicAngle t = fromTurns(r);
            t.validate();
            inverse(AlgebraicAngle::fromTurns, x -> x.rationalTurns().get(), r);
        }

        for (Rational r : take(LIMIT, P.negativeRationals())) {
            try {
                fromTurns(r);
                fail(r);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Rational r : take(LIMIT, P.rangeUp(Rational.ONE))) {
            try {
                fromTurns(r);
                fail(r);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesFromDegrees() {
        initialize("fromDegrees(Rational)");
        Rational limit = Rational.of(360);
        for (Rational r : take(LIMIT, filterInfinite(x -> !x.equals(limit), P.range(Rational.ZERO, limit)))) {
            AlgebraicAngle t = fromDegrees(r);
            t.validate();
            inverse(AlgebraicAngle::fromDegrees, x -> x.degrees().rationalValueExact().get(), r);
        }

        for (Rational r : take(LIMIT, P.negativeRationals())) {
            try {
                fromDegrees(r);
                fail(r);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Rational r : take(LIMIT, P.rangeUp(limit))) {
            try {
                fromDegrees(r);
                fail(r);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesIsRationalMultipleOfPi() {
        initialize("isRationalMultipleOfPi()");
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            assertEquals(t, t.isRationalMultipleOfPi(), t.rationalTurns().isPresent());
        }
    }

    private void propertiesRationalTurns() {
        initialize("rationalTurns()");
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            Optional<Rational> or = t.rationalTurns();
            if (or.isPresent()) {
                Rational r = or.get();
                assertTrue(t, r.signum() != -1 && Ordering.lt(r, Rational.ONE));
            }
        }
    }

    private void propertiesGetQuadrant() {
        initialize("getQuadrant()");
        for (AlgebraicAngle t : take(MEDIUM_LIMIT, P.withScale(4).algebraicAngles())) {
            int q = t.getQuadrant();
            assertTrue(t, q >= 1 && q <= 4);
            AlgebraicAngle lower;
            AlgebraicAngle upper;
            switch (q) {
                case 1:
                    lower = ZERO;
                    upper = PI_OVER_TWO;
                    break;
                case 2:
                    lower = PI_OVER_TWO;
                    upper = PI;
                    break;
                case 3:
                    lower = PI;
                    upper = THREE_PI_OVER_TWO;
                    break;
                case 4:
                    lower = THREE_PI_OVER_TWO;
                    upper = null;
                    break;
                default:
                    throw new IllegalStateException("unreachable");
            }
            assertTrue(t, Ordering.ge(t, lower));
            if (upper != null) {
                assertTrue(t, Ordering.lt(t, upper));
            }
        }
    }

    private void propertiesRealTurns() {
        initialize("realTurns()");
        for (AlgebraicAngle t : take(SMALL_LIMIT, P.withScale(4).algebraicAngles())) {
            Real r = t.realTurns();
            assertTrue(t, r.signumUnsafe() != -1);
            assertTrue(t, r.ltUnsafe(Rational.ONE));
        }
    }

    private void propertiesRadians() {
        initialize("radians()");
        Real limit = Real.PI.shiftLeft(1);
        for (AlgebraicAngle t : take(SMALL_LIMIT, P.withScale(4).algebraicAngles())) {
            Real r = t.radians();
            assertTrue(t, r.signumUnsafe() != -1);
            assertTrue(t, r.ltUnsafe(limit));
            assertTrue(t, r.eq(t.realTurns().multiply(Real.PI.shiftLeft(1)), Real.DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesDegrees() {
        initialize("degrees()");
        Rational limit = Rational.of(360);
        for (AlgebraicAngle t : take(SMALL_LIMIT, P.withScale(4).algebraicAngles())) {
            Real r = t.radians();
            assertTrue(t, r.signumUnsafe() != -1);
            assertTrue(t, r.ltUnsafe(limit));
            assertTrue(t, r.eq(t.realTurns().multiply(Real.PI.shiftLeft(1)), Real.DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (AlgebraicAngle t : take(SMALL_LIMIT, P.withScale(4).algebraicAngles())) {
            AlgebraicAngle u = t.negate();
            u.validate();
            involution(AlgebraicAngle::negate, t);
            assertEquals(u, t.supplement().addPi(), u);
            Real realU = t.radians().negate();
            if (realU.ltUnsafe(Rational.ZERO)) {
                realU = realU.add(Real.PI.shiftLeft(1));
            }
            assertTrue(t, u.radians().eq(realU, Real.DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesSupplement() {
        initialize("supplement()");
        for (AlgebraicAngle t : take(SMALL_LIMIT, P.withScale(4).algebraicAngles())) {
            AlgebraicAngle u = t.supplement();
            u.validate();
            involution(AlgebraicAngle::supplement, t);
            assertEquals(u, t.addPi().negate(), u);
            Real realU = Real.of(Rational.ONE_HALF).subtract(t.realTurns());
            if (realU.ltUnsafe(Rational.ZERO)) {
                realU = realU.add(Rational.ONE);
            }
            assertTrue(t, u.realTurns().eq(realU, Real.DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesAddPi() {
        initialize("addPi()");
        for (AlgebraicAngle t : take(SMALL_LIMIT, P.withScale(4).algebraicAngles())) {
            AlgebraicAngle u = t.addPi();
            u.validate();
            involution(AlgebraicAngle::addPi, t);
            assertEquals(u, t.negate().supplement(), u);
            Real realU = t.realTurns().add(Rational.ONE_HALF);
            if (realU.geUnsafe(Rational.ONE)) {
                realU = realU.subtract(Rational.ONE);
            }
            assertTrue(t, u.realTurns().eq(realU, Real.DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesSin() {
        initialize("sin()");
        BigInteger maxDenominator = BigInteger.valueOf(25);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(SMALL_LIMIT, ts)) {
            Algebraic x = t.sin();
            assertTrue(t, t.radians().sin().eq(x.realValue(), SMALL_RESOLUTION).orElse(true));
            assertEquals(t, x.pow(2).add(t.cos().pow(2)), Algebraic.ONE);
            assertEquals(t, t.complement().cos(), x);
            assertEquals(t, t.negate().sin(), x.negate());
            assertEquals(t, t.supplement().sin(), x);
            assertEquals(t, t.addPi().sin(), x.negate());
        }

        Iterable<AlgebraicAngle> ts2 = filterInfinite(
                t -> !t.equals(ZERO) && !t.equals(PI) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(SMALL_LIMIT, ts2)) {
            assertEquals(t, t.sin(), t.csc().invert());
        }
    }

    private void propertiesCos() {
        initialize("cos()");
        BigInteger maxDenominator = BigInteger.valueOf(25);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(SMALL_LIMIT, ts)) {
            Algebraic x = t.cos();
            assertTrue(t, t.radians().cos().eq(x.realValue(), SMALL_RESOLUTION).orElse(true));
            assertEquals(t, t.complement().sin(), x);
            assertEquals(t, t.negate().cos(), x);
            assertEquals(t, t.supplement().cos(), x.negate());
            assertEquals(t, t.addPi().cos(), x.negate());
        }

        Iterable<AlgebraicAngle> ts2 = filterInfinite(
                t -> !t.equals(PI_OVER_TWO) && !t.equals(THREE_PI_OVER_TWO) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(SMALL_LIMIT, ts2)) {
            assertEquals(t, t.cos(), t.sec().invert());
        }
    }

    private void propertiesTan() {
        initialize("tan()");
        BigInteger maxDenominator = BigInteger.valueOf(15);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> !t.equals(PI_OVER_TWO) && !t.equals(THREE_PI_OVER_TWO) &&
                        (t.isRationalMultipleOfPi() || t.cos().degree() < 6) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(SMALL_LIMIT, ts)) {
            Algebraic x = t.tan();
            assertTrue(t, t.radians().tanUnsafe().eq(x.realValue(), SMALL_RESOLUTION).orElse(true));
            assertEquals(t, t.complement().cot(), x);
            assertEquals(t, t.negate().tan(), x.negate());
            assertEquals(t, t.supplement().tan(), x.negate());
            assertEquals(t, t.addPi().tan(), x);
        }

        Iterable<AlgebraicAngle> ts2 = filterInfinite(
                t -> !t.equals(ZERO) && !t.equals(PI) && !t.equals(PI_OVER_TWO) && !t.equals(THREE_PI_OVER_TWO) &&
                        (t.isRationalMultipleOfPi() || t.cos().degree() < 6) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(SMALL_LIMIT, ts2)) {
            assertEquals(t, t.tan(), t.cot().invert());
        }
    }

    private void propertiesCot() {
        initialize("cot()");
        BigInteger maxDenominator = BigInteger.valueOf(15);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> !t.equals(ZERO) && !t.equals(PI) &&
                        (t.isRationalMultipleOfPi() || t.cos().degree() < 6) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(SMALL_LIMIT, ts)) {
            Algebraic x = t.cot();
            assertTrue(t, t.radians().cotUnsafe().eq(x.realValue(), SMALL_RESOLUTION).orElse(true));
            assertEquals(t, t.complement().tan(), x);
            assertEquals(t, t.negate().cot(), x.negate());
            assertEquals(t, t.supplement().cot(), x.negate());
            assertEquals(t, t.addPi().cot(), x);
        }

        Iterable<AlgebraicAngle> ts2 = filterInfinite(
                t -> !t.equals(ZERO) && !t.equals(PI) && !t.equals(PI_OVER_TWO) && !t.equals(THREE_PI_OVER_TWO) &&
                        (t.isRationalMultipleOfPi() || t.cos().degree() < 6) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(SMALL_LIMIT, ts2)) {
            assertEquals(t, t.cot(), t.tan().invert());
        }
    }

    private void propertiesSec() {
        initialize("sec()");
        BigInteger maxDenominator = BigInteger.valueOf(100);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> !t.equals(PI_OVER_TWO) && !t.equals(THREE_PI_OVER_TWO) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(SMALL_LIMIT, ts)) {
            Algebraic x = t.sec();
            assertTrue(t, t.radians().secUnsafe().eq(x.realValue(), SMALL_RESOLUTION).orElse(true));
            assertEquals(t, t.complement().csc(), x);
            assertEquals(t, t.sec(), t.cos().invert());
            assertEquals(t, t.negate().sec(), x);
            assertEquals(t, t.supplement().sec(), x.negate());
            assertEquals(t, t.addPi().sec(), x.negate());
        }
    }

    private void propertiesCsc() {
        initialize("csc()");
        BigInteger maxDenominator = BigInteger.valueOf(25);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> !t.equals(ZERO) && !t.equals(PI) &&
                        t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(SMALL_LIMIT, ts)) {
            Algebraic x = t.csc();
            assertTrue(t, t.radians().cscUnsafe().eq(x.realValue(), SMALL_RESOLUTION).orElse(true));
            assertEquals(t, t.complement().sec(), x);
            assertEquals(t, t.csc(), t.sin().invert());
            assertEquals(t, t.negate().csc(), x.negate());
            assertEquals(t, t.supplement().csc(), x);
            assertEquals(t, t.addPi().csc(), x.negate());
        }
    }

    private void propertiesComplement() {
        initialize("complement()");
        BigInteger maxDenominator = BigInteger.valueOf(25);
        Iterable<AlgebraicAngle> ts = filterInfinite(
                t -> t.rationalTurns().map(x -> le(x.getDenominator(), maxDenominator)).orElse(true),
                P.withScale(4).withSecondaryScale(4).algebraicAngles()
        );
        for (AlgebraicAngle t : take(SMALL_LIMIT, ts)) {
            AlgebraicAngle u = t.complement();
            u.validate();
            involution(AlgebraicAngle::complement, t);
            Real realU = Real.of(Rational.of(1, 4)).subtract(t.realTurns());
            if (realU.ltUnsafe(Rational.ZERO)) {
                realU = realU.add(Rational.ONE);
            }
            assertTrue(t, u.realTurns().eq(realU, Real.DEFAULT_RESOLUTION).orElse(true));
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        QBarTesting.propertiesEqualsHelper(MEDIUM_LIMIT, P, QBarIterableProvider::algebraicAngles);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        QBarTesting.propertiesHashCodeHelper(MEDIUM_LIMIT, P, QBarIterableProvider::algebraicAngles);
    }

    private void propertiesCompareTo() {
        initialize("compareTo(AlgebraicAngle)");
        QBarTesting.propertiesCompareToHelper(MEDIUM_LIMIT, P, QBarIterableProvider::algebraicAngles);
        Iterable<Pair<AlgebraicAngle, AlgebraicAngle>> ps = P.subsetPairs(P.withScale(4).algebraicAngles());
        for (Pair<AlgebraicAngle, AlgebraicAngle> p : take(SMALL_LIMIT, ps)) {
            assertEquals(p, p.a.compareTo(p.b), p.a.radians().compareToUnsafe(p.b.radians()));
        }
    }

    private void propertiesReadStrict() {
        initialize("readStrict(String)");
        QBarTesting.propertiesReadHelper(
                MEDIUM_LIMIT,
                P,
                ALGEBRAIC_ANGLE_CHARS,
                P.algebraicAngles(),
                AlgebraicAngle::readStrict,
                AlgebraicAngle::validate,
                false,
                true
        );
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(MEDIUM_LIMIT, ALGEBRAIC_ANGLE_CHARS, P.algebraicAngles(), AlgebraicAngle::readStrict);
    }
}
