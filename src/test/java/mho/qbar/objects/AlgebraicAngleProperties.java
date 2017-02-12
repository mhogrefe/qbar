package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static mho.qbar.objects.AlgebraicAngle.fromDegrees;
import static mho.qbar.objects.AlgebraicAngle.fromTurns;
import static mho.wheels.iterables.IterableUtils.filterInfinite;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.testing.Testing.*;

public class AlgebraicAngleProperties  extends QBarTestProperties {
    private static final @NotNull String ALGEBRAIC_ANGLE_CHARS = " ()*+-/0123456789^acfiopqrstx";

    public AlgebraicAngleProperties() {
        super("AlgebraicAngle");
    }

    @Override
    protected void testBothModes() {
        propertiesFromTurns();
        propertiesFromDegrees();
        propertiesIsRationalMultipleOfPi();
        propertiesRationalTurns();
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
        for (Pair<AlgebraicAngle, AlgebraicAngle> p : take(MEDIUM_LIMIT, ps)) {
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
