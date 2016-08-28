package mho.qbar.objects;

import mho.qbar.testing.QBarTestProperties;
import mho.wheels.math.BinaryFraction;
import mho.wheels.numberUtils.IntegerUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static mho.qbar.objects.Real.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.iterables.IterableUtils.take;
import static mho.wheels.numberUtils.FloatingPointUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;
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
        propertiesOf_BinaryFraction();
        propertiesOf_float();
        propertiesOf_double();
        propertiesOfExact_float();
        propertiesOfExact_double();
        propertiesOf_BigDecimal();
        propertiesFuzzyRepresentation();
        propertiesLeftFuzzyRepresentation();
        propertiesRightFuzzyRepresentation();
        propertiesIterator();
        propertiesIsExact();
        propertiesRationalValue();
    }

    private void propertiesOf_Rational() {
        initialize("of(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = of(r);
            x.validate();
            assertTrue(r, x.isExact());
            inverse(Real::of, (Real y) -> y.rationalValue().get(), r);
        }
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Real x = of(i);
            x.validate();
            assertTrue(i, x.rationalValue().get().isInteger());
        }
    }

    private void propertiesOf_long() {
        initialize("of(long)");
        Rational lowerLimit = Rational.of(Long.MIN_VALUE);
        Rational upperLimit = Rational.of(Long.MAX_VALUE);
        for (long l : take(LIMIT, P.longs())) {
            Real x = of(l);
            x.validate();
            assertTrue(l, x.rationalValue().get().isInteger());
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
            assertTrue(i, x.rationalValue().get().isInteger());
            assertTrue(i, geUnsafe(x, lowerLimit));
            assertTrue(i, leUnsafe(x, upperLimit));
        }
    }

    private void propertiesOf_BinaryFraction() {
        initialize("of(BinaryFraction)");
        for (BinaryFraction bf : take(LIMIT, P.binaryFractions())) {
            Real x = of(bf);
            x.validate();
            assertEquals(
                    bf,
                    of(bf.getMantissa()).multiply(ONE.shiftLeft(bf.getExponent())).rationalValue().get(),
                    x.rationalValue().get()
            );
            assertTrue(bf, x.rationalValue().get().isBinaryFraction());
            inverse(Algebraic::of, Algebraic::binaryFractionValueExact, bf);
        }
    }

    private void propertiesOf_float() {
        initialize("of(float)");
        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Real x = of(f).get();
            x.validate();
            assertTrue(f, x.rationalValue().get().hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            Real x = of(f).get();
            aeqf(f, f, x.floatValue());
            assertTrue(f, eq(new BigDecimal(Float.toString(f)), x.rationalValue().get().bigDecimalValueExact()));
        }
    }

    private void propertiesOf_double() {
        initialize("of(double)");
        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Real x = of(d).get();
            x.validate();
            assertTrue(d, x.rationalValue().get().hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            Real x = of(d).get();
            aeqd(d, d, x.doubleValue());
            assertTrue(d, eq(new BigDecimal(Double.toString(d)), x.rationalValue().get().bigDecimalValueExact()));
        }
    }

    private void propertiesOfExact_float() {
        initialize("ofExact(float)");
        for (float f : take(LIMIT, P.floats())) {
            Optional<Real> ox = ofExact(f);
            assertEquals(f, Float.isFinite(f), ox.isPresent());
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            inverse(g -> ofExact(g).get(), (Real x) -> x.rationalValue().get().floatValueExact(), f);
        }

        int x = 1 << (FLOAT_EXPONENT_WIDTH - 1);
        BigInteger y = BigInteger.ONE.shiftLeft(-MIN_SUBNORMAL_FLOAT_EXPONENT);
        BigInteger z = BigInteger.ONE.shiftLeft(x).subtract(BigInteger.ONE.shiftLeft(x - FLOAT_FRACTION_WIDTH - 1));
        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Real a = ofExact(f).get();
            a.validate();
            Rational r = a.rationalValue().get();
            assertTrue(f, IntegerUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(f, le(r.getDenominator(), y));
            assertTrue(f, le(r.getNumerator(), z));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            Real a = ofExact(f).get();
            aeqf(f, f, a.floatValue());
        }
    }

    private void propertiesOfExact_double() {
        initialize("ofExact(double)");
        for (double d : take(LIMIT, P.doubles())) {
            Optional<Real> ox = ofExact(d);
            assertEquals(d, Double.isFinite(d), ox.isPresent());
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            inverse(e -> ofExact(e).get(), (Real x) -> x.rationalValue().get().doubleValueExact(), d);
        }

        int x = 1 << (DOUBLE_EXPONENT_WIDTH - 1);
        BigInteger y = BigInteger.ONE.shiftLeft(-MIN_SUBNORMAL_DOUBLE_EXPONENT);
        BigInteger z = BigInteger.ONE.shiftLeft(x).subtract(BigInteger.ONE.shiftLeft(x - DOUBLE_FRACTION_WIDTH - 1));
        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Real a = ofExact(d).get();
            a.validate();
            Rational r = a.rationalValue().get();
            assertTrue(d, IntegerUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(d, le(r.getDenominator(), y));
            assertTrue(d, le(r.getNumerator(), z));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            Real a = ofExact(d).get();
            aeqd(d, d, a.doubleValue());
        }
    }

    private void propertiesOf_BigDecimal() {
        initialize("of(BigDecimal)");
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Real x = of(bd);
            x.validate();
            assertTrue(bd, eq(bd, x.rationalValue().get().bigDecimalValueExact()));
            assertTrue(bd, x.rationalValue().get().hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (BigDecimal bd : take(LIMIT, P.canonicalBigDecimals())) {
            inverse(Algebraic::of, Algebraic::bigDecimalValueExact, bd);
        }
    }

    private void propertiesFuzzyRepresentation() {
        initialize("fuzzyRepresentation(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = fuzzyRepresentation(r);
            x.validate();
            assertFalse(r, x.isExact());
        }
    }

    private void propertiesLeftFuzzyRepresentation() {
        initialize("leftFuzzyRepresentation(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = leftFuzzyRepresentation(r);
            x.validate();
            assertFalse(r, x.isExact());
        }
    }

    private void propertiesRightFuzzyRepresentation() {
        initialize("rightFuzzyRepresentation(Rational)");
        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = rightFuzzyRepresentation(r);
            x.validate();
            assertFalse(r, x.isExact());
        }
    }

    private void propertiesIterator() {
        initialize("iterator()");
        for (Real x : take(LIMIT, P.reals())) {
            List<Interval> intervals = toList(take(TINY_LIMIT, x));
            for (int i = 1; i < TINY_LIMIT; i++) {
                assertTrue(x, intervals.get(i - 1).contains(intervals.get(i)));
            }
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            List<Interval> intervals = toList(take(TINY_LIMIT, of(r)));
            assertEquals(r, intervals, toList(replicate(TINY_LIMIT, Interval.of(r))));
        }
    }

    private void propertiesIsExact() {
        initialize("isExact()");
        for (Real x : take(LIMIT, P.reals())) {
            x.isExact();
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r, of(r).isExact());
        }
    }

    private void propertiesRationalValue() {
        initialize("rationalValue()");
        for (Real x : take(LIMIT, P.reals())) {
            x.rationalValue();
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Real x = of(r);
            assertTrue(r, x.rationalValue().isPresent());
        }
    }
}
