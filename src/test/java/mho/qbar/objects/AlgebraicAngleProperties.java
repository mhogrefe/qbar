package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import mho.wheels.structures.Pair;
import org.jetbrains.annotations.NotNull;

import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.testing.Testing.MEDIUM_LIMIT;
import static mho.wheels.testing.Testing.assertEquals;
import static mho.wheels.testing.Testing.propertiesToStringHelper;

public class AlgebraicAngleProperties  extends QBarTestProperties {
    private static final @NotNull String ALGEBRAIC_ANGLE_CHARS = " ()*+-/0123456789^acfiopqrstx";

    public AlgebraicAngleProperties() {
        super("AlgebraicAngle");
    }

    @Override
    protected void testBothModes() {
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        propertiesReadStrict();
        propertiesToString();
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
