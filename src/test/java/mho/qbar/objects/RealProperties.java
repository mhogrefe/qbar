package mho.qbar.objects;

import mho.qbar.testing.QBarTestProperties;

import java.math.BigInteger;

import static mho.qbar.objects.Real.*;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.testing.Testing.assertTrue;

public class RealProperties extends QBarTestProperties {
    public RealProperties() {
        super("Real");
    }

    @Override
    protected void testBothModes() {
        propertiesOf_Rational();
        propertiesOf_BigInteger();
        propertiesOf_long();
        propertiesOf_int();
    }

    private void propertiesOf_Rational() {
        initialize("of(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = of(r);
            x.validate();
            assertTrue(r, x.isExactRational());
        }
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Real x = of(i);
            x.validate();
            assertTrue(i, x.isExactInteger());
        }
    }

    private void propertiesOf_long() {
        initialize("of(long)");
        Rational lowerLimit = Rational.of(Long.MIN_VALUE);
        Rational upperLimit = Rational.of(Long.MAX_VALUE);
        for (long l : take(LIMIT, P.longs())) {
            Real x = of(l);
            x.validate();
            assertTrue(l, x.isExactInteger());
            assertTrue(l, geUnsafe(x, lowerLimit));
            assertTrue(l, leUnsafe(x, upperLimit));
        }
    }

    private void propertiesOf_int() {
        initialize("of(int)");
        Rational lowerLimit = Rational.of(Integer.MIN_VALUE);
        Rational upperLimit = Rational.of(Integer.MAX_VALUE);
        for (int i : take(LIMIT, P.integers())) {
            Real x = of(i);
            x.validate();
            assertTrue(i, x.isExactInteger());
            assertTrue(i, geUnsafe(x, lowerLimit));
            assertTrue(i, leUnsafe(x, upperLimit));
        }
    }
}
