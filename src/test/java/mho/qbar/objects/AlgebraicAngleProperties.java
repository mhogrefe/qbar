package mho.qbar.objects;

import mho.qbar.testing.QBarTestProperties;
import mho.qbar.testing.QBarTesting;
import org.jetbrains.annotations.NotNull;

import static mho.wheels.testing.Testing.MEDIUM_LIMIT;
import static mho.wheels.testing.Testing.propertiesToStringHelper;

public class AlgebraicAngleProperties  extends QBarTestProperties {
    private static final @NotNull String ALGEBRAIC_ANGLE_CHARS = " ()*+-/0123456789^acfiopqrstx";

    public AlgebraicAngleProperties() {
        super("AlgebraicAngle");
    }

    @Override
    protected void testBothModes() {
        propertiesReadStrict();
        propertiesToString();
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
