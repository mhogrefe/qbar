package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.testing.QBarTestProperties;
import mho.wheels.io.Readers;
import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.math.BinaryFraction;
import mho.wheels.math.MathUtils;
import mho.wheels.numberUtils.BigDecimalUtils;
import mho.wheels.numberUtils.IntegerUtils;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static mho.qbar.objects.Rational.*;
import static mho.qbar.objects.Rational.sum;
import static mho.qbar.testing.QBarTesting.*;
import static mho.qbar.testing.QBarTesting.propertiesCompareToHelper;
import static mho.qbar.testing.QBarTesting.propertiesEqualsHelper;
import static mho.qbar.testing.QBarTesting.propertiesHashCodeHelper;
import static mho.qbar.testing.QBarTesting.propertiesReadHelper;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.numberUtils.FloatingPointUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;

public class RationalProperties extends QBarTestProperties {
    private static final @NotNull String RATIONAL_CHARS = "-/0123456789";
    private static final BigInteger ASCII_ALPHANUMERIC_COUNT = BigInteger.valueOf(36);

    public RationalProperties() {
        super("Rational");
    }

    @Override
    protected void testConstant() {
        propertiesConstants();
    }

    @Override
    protected void testBothModes() {
        propertiesGetNumerator();
        propertiesGetDenominator();
        propertiesOf_BigInteger_BigInteger();
        propertiesOf_long_long();
        propertiesOf_int_int();
        propertiesOf_BigInteger();
        propertiesOf_long();
        propertiesOf_int();
        propertiesOf_BinaryFraction();
        propertiesOf_float();
        propertiesOf_double();
        propertiesOfExact_float();
        propertiesOfExact_double();
        propertiesOf_BigDecimal();
        propertiesIsInteger();
        propertiesBigIntegerValue_RoundingMode();
        propertiesBigIntegerValue();
        propertiesFloor();
        propertiesCeiling();
        propertiesBigIntegerValueExact();
        propertiesByteValueExact();
        propertiesShortValueExact();
        propertiesIntValueExact();
        propertiesLongValueExact();
        propertiesIsPowerOfTwo();
        propertiesRoundUpToPowerOfTwo();
        propertiesIsBinaryFraction();
        propertiesBinaryFractionValueExact();
        propertiesBinaryExponent();
        compareImplementationsBinaryExponent();
        propertiesIsEqualToFloat();
        compareImplementationsIsEqualToFloat();
        propertiesIsEqualToDouble();
        compareImplementationsIsEqualToDouble();
        propertiesIsEqualToDouble();
        propertiesFloatValue_RoundingMode();
        propertiesFloatValue();
        propertiesFloatValueExact();
        propertiesDoubleValue_RoundingMode();
        propertiesDoubleValue();
        propertiesDoubleValueExact();
        propertiesHasTerminatingBaseExpansion();
        propertiesBigDecimalValueByPrecision_int_RoundingMode();
        propertiesBigDecimalValueByScale_int_RoundingMode();
        propertiesBigDecimalValueByPrecision_int();
        propertiesBigDecimalValueByScale_int();
        propertiesBigDecimalValueExact();
        propertiesBitLength();
        propertiesAdd();
        compareImplementationsAdd();
        propertiesNegate();
        propertiesAbs();
        propertiesSignum();
        propertiesSubtract();
        compareImplementationsSubtract();
        propertiesMultiply_Rational();
        compareImplementationsMultiply_Rational();
        propertiesMultiply_BigInteger();
        compareImplementationsMultiply_BigInteger();
        propertiesMultiply_int();
        compareImplementationsMultiply_int();
        propertiesInvert();
        compareImplementationsInvert();
        propertiesDivide_Rational();
        compareImplementationsDivide_Rational();
        propertiesDivide_BigInteger();
        compareImplementationsDivide_BigInteger();
        propertiesDivide_int();
        compareImplementationsDivide_int();
        propertiesShiftLeft();
        compareImplementationsShiftLeft();
        propertiesShiftRight();
        compareImplementationsShiftRight();
        propertiesSum();
        compareImplementationsSum();
        propertiesProduct();
        compareImplementationsProduct();
        propertiesSumSign();
        compareImplementationsSumSign();
        propertiesDelta();
        propertiesHarmonicNumber();
        propertiesPow();
        compareImplementationsPow();
        propertiesFractionalPart();
        propertiesRoundToDenominator();
        propertiesContinuedFraction();
        propertiesFromContinuedFraction();
        propertiesConvergents();
        propertiesPositionalNotation();
        propertiesFromPositionalNotation();
        propertiesDigits();
        compareImplementationsDigits();
        propertiesCommonLeadingDigits();
        propertiesToStringBase_BigInteger();
        propertiesToStringBase_BigInteger_int();
        propertiesFromStringBase();
        propertiesCancelDenominators();
        propertiesEquals();
        propertiesHashCode();
        propertiesCompareTo();
        compareImplementationsCompareTo();
        propertiesReadStrict();
        propertiesToString();
    }

    private void propertiesConstants() {
        initializeConstant("constants");
        List<Rational> sample = toList(take(MEDIUM_LIMIT, HARMONIC_NUMBERS));
        sample.forEach(Rational::validate);
        assertTrue("HARMONIC_NUMBERS", unique(sample));
        assertTrue("HARMONIC_NUMBERS", increasing(sample));
        assertTrue("HARMONIC_NUMBERS", all(r -> !r.isInteger(), tail(sample)));
        try {
            HARMONIC_NUMBERS.iterator().remove();
            fail("HARMONIC_NUMBERS");
        } catch (UnsupportedOperationException ignored) {}
    }

    private void propertiesGetNumerator() {
        initialize("getNumerator()");
        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, of(r.getNumerator(), r.getDenominator()), r);
        }
    }

    private void propertiesGetDenominator() {
        initialize("getDenominator()");
        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, r.getDenominator().signum(), 1);
        }
    }

    private void propertiesOf_BigInteger_BigInteger() {
        initialize("of(BigInteger, BigInteger)");
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, P.pairs(P.bigIntegers(), P.nonzeroBigIntegers()))) {
            Rational r = of(p.a, p.b);
            r.validate();
            assertEquals(p, of(p.a).divide(p.b), r);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            try {
                of(i, BigInteger.ZERO);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesOf_long_long() {
        initialize("of(long, long)");
        BigInteger minLong = BigInteger.valueOf(Long.MIN_VALUE);
        BigInteger maxLong = BigInteger.valueOf(Long.MAX_VALUE);
        for (Pair<Long, Long> p : take(LIMIT, P.pairs(P.longs(), P.nonzeroLongs()))) {
            Rational r = of(p.a, p.b);
            r.validate();
            assertEquals(p, of(p.a).divide(BigInteger.valueOf(p.b)), r);
            assertTrue(p, ge(r.getNumerator(), minLong));
            assertTrue(p, le(r.getNumerator(), maxLong));
            assertTrue(p, ge(r.getDenominator(), minLong));
            assertTrue(p, le(r.getDenominator(), maxLong));
        }

        for (long l : take(LIMIT, P.longs())) {
            try {
                of(l, 0L);
                fail(l);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesOf_int_int() {
        initialize("of(int, int)");
        BigInteger minInt = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
        for (Pair<Integer, Integer> p : take(LIMIT, P.pairs(P.integers(), P.nonzeroIntegers()))) {
            Rational r = of(p.a, p.b);
            r.validate();
            assertEquals(p, of(p.a).divide(p.b), r);
            assertTrue(p, ge(r.getNumerator(), minInt));
            assertTrue(p, le(r.getNumerator(), maxInt));
            assertTrue(p, ge(r.getDenominator(), minInt));
            assertTrue(p, le(r.getDenominator(), maxInt));
        }

        for (int i : take(LIMIT, P.integers())) {
            try {
                of(i, 0);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesOf_BigInteger() {
        initialize("of(BigInteger)");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Rational r = of(i);
            r.validate();
            assertEquals(i, r.getDenominator(), BigInteger.ONE);
            inverse(Rational::of, Rational::bigIntegerValueExact, i);
        }
    }

    private void propertiesOf_long() {
        initialize("of(long)");
        for (long l : take(LIMIT, P.longs())) {
            Rational r = of(l);
            r.validate();
            assertEquals(l, r.getDenominator(), BigInteger.ONE);
            assertTrue(l, ge(r.getNumerator(), BigInteger.valueOf(Long.MIN_VALUE)));
            assertTrue(l, le(r.getNumerator(), BigInteger.valueOf(Long.MAX_VALUE)));
            inverse(Rational::of, Rational::longValueExact, l);
        }
    }

    private void propertiesOf_int() {
        initialize("of(int)");
        for (int i : take(LIMIT, P.integers())) {
            Rational r = of(i);
            r.validate();
            assertEquals(i, r.getDenominator(), BigInteger.ONE);
            assertTrue(i, ge(r.getNumerator(), BigInteger.valueOf(Integer.MIN_VALUE)));
            assertTrue(i, le(r.getNumerator(), BigInteger.valueOf(Integer.MAX_VALUE)));
            inverse(Rational::of, Rational::intValueExact, i);
        }
    }

    private void propertiesOf_BinaryFraction() {
        initialize("of(BinaryFraction)");
        for (BinaryFraction bf : take(LIMIT, P.binaryFractions())) {
            Rational r = of(bf);
            r.validate();
            assertEquals(bf, of(bf.getMantissa()).multiply(ONE.shiftLeft(bf.getExponent())), r);
            assertTrue(bf, IntegerUtils.isPowerOfTwo(r.getDenominator()));
            inverse(Rational::of, Rational::binaryFractionValueExact, bf);
        }
    }

    private void propertiesOf_float() {
        initialize("of(float)");
        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Rational r = of(f).get();
            r.validate();
            assertTrue(f, r.hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            Rational r = of(f).get();
            aeqf(f, f, r.floatValue());
            assertTrue(f, eq(new BigDecimal(Float.toString(f)), r.bigDecimalValueExact()));
        }
    }

    private void propertiesOf_double() {
        initialize("of(double)");
        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Rational r = of(d).get();
            r.validate();
            assertTrue(d, r.hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            Rational r = of(d).get();
            aeqd(d, d, r.doubleValue());
            assertTrue(d, eq(new BigDecimal(Double.toString(d)), r.bigDecimalValueExact()));
        }
    }

    private void propertiesOfExact_float() {
        initialize("ofExact(float)");
        for (float f : take(LIMIT, P.floats())) {
            Optional<Rational> or = ofExact(f);
            assertEquals(f, Float.isFinite(f), or.isPresent());
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            inverse(g -> ofExact(g).get(), Rational::floatValueExact, f);
        }

        int x = 1 << (FLOAT_EXPONENT_WIDTH - 1);
        BigInteger y = BigInteger.ONE.shiftLeft(-MIN_SUBNORMAL_FLOAT_EXPONENT);
        BigInteger z = BigInteger.ONE.shiftLeft(x).subtract(BigInteger.ONE.shiftLeft(x - FLOAT_FRACTION_WIDTH - 1));
        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Rational r = ofExact(f).get();
            r.validate();
            assertTrue(f, IntegerUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(f, le(r.getDenominator(), y));
            assertTrue(f, le(r.getNumerator(), z));
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            Rational r = ofExact(f).get();
            aeqf(f, f, r.floatValue());
        }
    }

    private void propertiesOfExact_double() {
        initialize("ofExact(double)");
        for (double d : take(LIMIT, P.doubles())) {
            Optional<Rational> or = ofExact(d);
            assertEquals(d, Double.isFinite(d), or.isPresent());
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            inverse(e -> ofExact(e).get(), Rational::doubleValueExact, d);
        }

        int x = 1 << (DOUBLE_EXPONENT_WIDTH - 1);
        BigInteger y = BigInteger.ONE.shiftLeft(-MIN_SUBNORMAL_DOUBLE_EXPONENT);
        BigInteger z = BigInteger.ONE.shiftLeft(x).subtract(BigInteger.ONE.shiftLeft(x - DOUBLE_FRACTION_WIDTH - 1));
        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Rational r = ofExact(d).get();
            r.validate();
            assertTrue(d, IntegerUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(d, le(r.getDenominator(), y));
            assertTrue(d, le(r.getNumerator(), z));
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            Rational r = ofExact(d).get();
            aeqd(d, d, r.doubleValue());
        }
    }

    private void propertiesOf_BigDecimal() {
        initialize("of(BigDecimal)");
        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Rational r = of(bd);
            r.validate();
            assertTrue(bd, eq(bd, r.bigDecimalValueExact()));
            assertTrue(bd, r.hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (BigDecimal bd : take(LIMIT, P.canonicalBigDecimals())) {
            inverse(Rational::of, Rational::bigDecimalValueExact, bd);
        }
    }

    private void propertiesIsInteger() {
        initialize("isInteger()");
        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, r.isInteger(), of(r.floor()).equals(r));
            homomorphic(Rational::negate, Function.identity(), Rational::isInteger, Rational::isInteger, r);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertTrue(i, of(i).isInteger());
        }
    }

    private void propertiesBigIntegerValue_RoundingMode() {
        initialize("bigIntegerValue(RoundingMode)");
        Iterable<Pair<Rational, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isInteger(),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            BigInteger rounded = p.a.bigIntegerValue(p.b);
            assertTrue(p, rounded.equals(BigInteger.ZERO) || rounded.signum() == p.a.signum());
            assertTrue(p, lt(p.a.subtract(of(rounded)).abs(), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).bigIntegerValue(RoundingMode.UNNECESSARY), i);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, r.bigIntegerValue(RoundingMode.FLOOR), r.floor());
            assertEquals(r, r.bigIntegerValue(RoundingMode.CEILING), r.ceiling());
            assertTrue(r, le(of(r.bigIntegerValue(RoundingMode.DOWN)).abs(), r.abs()));
            assertTrue(r, ge(of(r.bigIntegerValue(RoundingMode.UP)).abs(), r.abs()));
            assertTrue(r, le(r.subtract(of(r.bigIntegerValue(RoundingMode.HALF_DOWN))).abs(), ONE_HALF));
            assertTrue(r, le(r.subtract(of(r.bigIntegerValue(RoundingMode.HALF_UP))).abs(), ONE_HALF));
            assertTrue(r, le(r.subtract(of(r.bigIntegerValue(RoundingMode.HALF_EVEN))).abs(), ONE_HALF));
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> lt(s.abs().fractionalPart(), ONE_HALF), P.rationals()))) {
            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_DOWN), r.bigIntegerValue(RoundingMode.DOWN));
            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_UP), r.bigIntegerValue(RoundingMode.DOWN));
            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_EVEN), r.bigIntegerValue(RoundingMode.DOWN));
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> gt(s.abs().fractionalPart(), ONE_HALF), P.rationals()))) {
            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_DOWN), r.bigIntegerValue(RoundingMode.UP));
            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_UP), r.bigIntegerValue(RoundingMode.UP));
            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_EVEN), r.bigIntegerValue(RoundingMode.UP));
        }

        //odd multiples of 1/2
        Iterable<Rational> rs = map(i -> of(i.shiftLeft(1).add(BigInteger.ONE), IntegerUtils.TWO), P.bigIntegers());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_DOWN), r.bigIntegerValue(RoundingMode.DOWN));
            assertEquals(r, r.bigIntegerValue(RoundingMode.HALF_UP), r.bigIntegerValue(RoundingMode.UP));
            assertFalse(r, r.bigIntegerValue(RoundingMode.HALF_EVEN).testBit(0));
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.bigIntegerValue(RoundingMode.UNNECESSARY);
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesBigIntegerValue() {
        initialize("bigIntegerValue()");
        for (Rational r : take(LIMIT, P.rationals())) {
            BigInteger rounded = r.bigIntegerValue();
            assertTrue(r, rounded.equals(BigInteger.ZERO) || rounded.signum() == r.signum());
            assertTrue(r, le(r.subtract(of(r.bigIntegerValue())).abs(), ONE_HALF));
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> lt(s.abs().fractionalPart(), ONE_HALF), P.rationals()))) {
            assertEquals(r, r.bigIntegerValue(), r.bigIntegerValue(RoundingMode.DOWN));
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> gt(s.abs().fractionalPart(), ONE_HALF), P.rationals()))) {
            assertEquals(r, r.bigIntegerValue(), r.bigIntegerValue(RoundingMode.UP));
        }

        //odd multiples of 1/2
        Iterable<Rational> rs = map(i -> of(i.shiftLeft(1).add(BigInteger.ONE), IntegerUtils.TWO), P.bigIntegers());
        for (Rational r : take(LIMIT, rs)) {
            assertFalse(r, r.bigIntegerValue().testBit(0));
        }
    }

    private void propertiesFloor() {
        initialize("floor()");
        for (Rational r : take(LIMIT, P.rationals())) {
            BigInteger floor = r.floor();
            assertTrue(r, le(of(floor), r));
            assertTrue(r, le(r.subtract(of(floor)), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).floor(), i);
        }
    }

    private void propertiesCeiling() {
        initialize("ceiling()");
        for (Rational r : take(LIMIT, P.rationals())) {
            BigInteger ceiling = r.ceiling();
            assertTrue(r, ge(of(ceiling), r));
            assertTrue(r, le(of(ceiling).subtract(r), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).ceiling(), i);
        }
    }

    private void propertiesBigIntegerValueExact() {
        initialize("bigIntegerValueExact()");
        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Rational r = of(i);
            assertEquals(i, r.bigIntegerValueExact(), i);
            inverse(Rational::bigIntegerValueExact, Rational::of, r);
            homomorphic(
                    Rational::negate,
                    BigInteger::negate,
                    Rational::bigIntegerValueExact,
                    Rational::bigIntegerValueExact,
                    r
            );
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.bigIntegerValueExact();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesByteValueExact() {
        initialize("byteValueExact()");
        for (byte b : take(LIMIT, P.bytes())) {
            Rational r = of(b);
            assertEquals(b, r.byteValueExact(), b);
            inverse(Rational::byteValueExact, c -> of((int) c), r);
        }

        for (byte b : take(LIMIT, filter(c -> c != Byte.MIN_VALUE, P.bytes()))) {
            Rational r = of(b);
            homomorphic(Rational::negate, c -> (byte) -c, Rational::byteValueExact, Rational::byteValueExact, r);
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.byteValueExact();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, P.rangeUp(BigInteger.valueOf(Byte.MAX_VALUE).add(BigInteger.ONE)))) {
            try {
                of(i).byteValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, P.rangeDown(BigInteger.valueOf(Byte.MIN_VALUE).subtract(BigInteger.ONE)))) {
            try {
                of(i).byteValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesShortValueExact() {
        initialize("shortValueExact()");
        for (short s : take(LIMIT, P.shorts())) {
            Rational r = of(s);
            assertEquals(s, r.shortValueExact(), s);
            inverse(Rational::shortValueExact, t -> of((int) t), r);
        }

        for (short s : take(LIMIT, filter(t -> t != Short.MIN_VALUE, P.shorts()))) {
            Rational r = of(s);
            homomorphic(Rational::negate, t -> (short) -t, Rational::shortValueExact, Rational::shortValueExact, r);
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.shortValueExact();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, P.rangeUp(BigInteger.valueOf(Short.MAX_VALUE).add(BigInteger.ONE)))) {
            try {
                of(i).shortValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, P.rangeDown(BigInteger.valueOf(Short.MIN_VALUE).subtract(BigInteger.ONE)))) {
            try {
                of(i).shortValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesIntValueExact() {
        initialize("intValueExact()");
        for (int i : take(LIMIT, P.integers())) {
            Rational r = of(i);
            assertEquals(i, r.intValueExact(), i);
            inverse(Rational::intValueExact, Rational::of, r);
        }

        for (int i : take(LIMIT, filter(j -> j != Integer.MIN_VALUE, P.integers()))) {
            Rational r = of(i);
            homomorphic(Rational::negate, j -> -j, Rational::intValueExact, Rational::intValueExact, r);
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.intValueExact();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> isFail = P.withScale(33)
                .rangeUp(BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE));
        for (BigInteger i : take(LIMIT, isFail)) {
            try {
                of(i).intValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> isFail2 = P.withScale(33)
                .rangeDown(BigInteger.valueOf(Integer.MIN_VALUE).subtract(BigInteger.ONE));
        for (BigInteger i : take(LIMIT, isFail2)) {
            try {
                of(i).intValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesLongValueExact() {
        initialize("longValueExact()");
        for (long l : take(LIMIT, P.longs())) {
            Rational r = of(l);
            assertEquals(l, r.longValueExact(), l);
            inverse(Rational::longValueExact, Rational::of, r);
        }

        for (long l : take(LIMIT, filter(m -> m != Long.MIN_VALUE, P.longs()))) {
            Rational r = of(l);
            homomorphic(Rational::negate, m -> -m, Rational::longValueExact, Rational::longValueExact, r);
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.longValueExact();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> isFail = P.withScale(65).rangeUp(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));
        for (BigInteger i : take(LIMIT, isFail)) {
            try {
                of(i).longValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> isFail2 = P.withScale(65)
                .rangeDown(BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE));
        for (BigInteger i : take(LIMIT, isFail2)) {
            try {
                of(i).longValueExact();
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesIsPowerOfTwo() {
        initialize("isPowerOfTwo()");
        for (Rational r : take(LIMIT, P.positiveRationals())) {
            assertEquals(r, r.isPowerOfTwo(), ONE.shiftLeft(r.binaryExponent()).equals(r));
        }

        for (Rational r : take(LIMIT, P.withElement(ZERO, P.negativeRationals()))) {
            try {
                r.isPowerOfTwo();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesRoundUpToPowerOfTwo() {
        initialize("roundUpToPowerOfTwo()");
        for (Rational r : take(LIMIT, P.positiveRationals())) {
            BinaryFraction powerOfTwo = r.roundUpToPowerOfTwo();
            assertTrue(r, powerOfTwo.isPowerOfTwo());
            assertTrue(r, le(r, of(powerOfTwo)));
            assertTrue(r, lt(of(powerOfTwo.shiftRight(1)), r));
        }

        for (Rational r : take(LIMIT, P.withElement(ZERO, P.negativeRationals()))) {
            try {
                r.roundUpToPowerOfTwo();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesIsBinaryFraction() {
        initialize("isBinaryFraction()");
        for (Rational r : take(LIMIT, P.rationals())) {
            r.isBinaryFraction();
            homomorphic(
                    Rational::negate,
                    Function.identity(),
                    Rational::isBinaryFraction,
                    Rational::isBinaryFraction,
                    r
            );
        }
    }

    private void propertiesBinaryFractionValueExact() {
        initialize("binaryFractionValueExact()");
        for (Rational r : take(LIMIT, filterInfinite(Rational::isBinaryFraction, P.rationals()))) {
            inverse(Rational::binaryFractionValueExact, Rational::of, r);
            homomorphic(
                    Rational::negate,
                    BinaryFraction::negate,
                    Rational::binaryFractionValueExact,
                    Rational::binaryFractionValueExact,
                    r
            );
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isBinaryFraction(), P.rationals()))) {
            try {
                r.binaryFractionValueExact();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static int binaryExponent_alt(@NotNull Rational r) {
        Rational adjusted = r;
        int exponent = 0;
        if (lt(r.getNumerator(), r.getDenominator())) {
            do {
                adjusted = adjusted.shiftLeft(1);
                exponent--;
            } while (lt(adjusted.getNumerator(), adjusted.getDenominator()));
        } else {
            do {
                adjusted = adjusted.shiftRight(1);
                exponent++;
            } while (ge(adjusted.getNumerator(), adjusted.getDenominator()));
            exponent--;
        }
        return exponent;
    }

    private void propertiesBinaryExponent() {
        initialize("binaryExponent()");
        for (Rational r : take(LIMIT, P.positiveRationals())) {
            int exponent = r.binaryExponent();
            assertEquals(r, binaryExponent_alt(r), exponent);
            Rational power = ONE.shiftLeft(exponent);
            assertTrue(r, le(power, r));
            assertTrue(r, le(r, power.shiftLeft(1)));
        }

        for (BinaryFraction bf : take(LIMIT, P.positiveBinaryFractions())) {
            Rational r = of(bf);
            assertEquals(bf, r.binaryExponent(), r.getNumerator().bitLength() - r.getDenominator().bitLength());
        }

        for (Rational r : take(LIMIT, P.withElement(ZERO, P.negativeRationals()))) {
            try {
                r.binaryExponent();
                fail(r);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsBinaryExponent() {
        Map<String, Function<Rational, Integer>> functions = new LinkedHashMap<>();
        functions.put("alt", RationalProperties::binaryExponent_alt);
        functions.put("standard", Rational::binaryExponent);
        compareImplementations("binaryExponent()", take(LIMIT, P.positiveRationals()), functions, v -> P.reset());
    }

    private static boolean isEqualToFloat_simplest(@NotNull Rational r) {
        Optional<Rational> or = ofExact(r.floatValue(RoundingMode.FLOOR));
        return or.isPresent() && or.get().equals(r);
    }

    private static boolean isEqualToFloat_alt(@NotNull Rational r) {
        if (r == ZERO || r == ONE) return true;
        if (r.getNumerator().signum() == -1) {
            return isEqualToFloat_alt(r.negate());
        }
        int exponent = r.binaryExponent();
        if (exponent > Float.MAX_EXPONENT || exponent == Float.MAX_EXPONENT && gt(r, LARGEST_FLOAT)) {
            return false;
        }
        int shift = exponent < Float.MIN_EXPONENT ? MIN_SUBNORMAL_FLOAT_EXPONENT : exponent - FLOAT_FRACTION_WIDTH;
        return r.shiftRight(shift).isInteger();
    }

    private void propertiesIsEqualToFloat() {
        initialize("isEqualToFloat()");
        for (Rational r : take(LIMIT, P.rationals())) {
            boolean ietf = r.isEqualToFloat();
            assertTrue(r, !ietf || r.isBinaryFraction());
            homomorphic(Rational::negate, Function.identity(), Rational::isEqualToFloat, Rational::isEqualToFloat, r);
            assertEquals(r, isEqualToFloat_simplest(r), ietf);
            assertEquals(r, isEqualToFloat_alt(r), ietf);
        }

        for (float f : take(LIMIT, filter(g -> Float.isFinite(g) && !isNegativeZero(g), P.floats()))) {
            assertTrue(f, ofExact(f).get().isEqualToFloat());
        }
    }

    private void compareImplementationsIsEqualToFloat() {
        Map<String, Function<Rational, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", RationalProperties::isEqualToFloat_simplest);
        functions.put("alt", RationalProperties::isEqualToFloat_alt);
        functions.put("standard", Rational::isEqualToFloat);
        compareImplementations("isEqualToFloat()", take(LIMIT, P.rationals()), functions, v -> P.reset());
    }

    private static boolean isEqualToDouble_simplest(@NotNull Rational r) {
        Optional<Rational> or = ofExact(r.doubleValue(RoundingMode.FLOOR));
        return or.isPresent() && or.get().equals(r);
    }

    private static boolean isEqualToDouble_alt(@NotNull Rational r) {
        if (r == ZERO || r == ONE) return true;
        if (r.getNumerator().signum() == -1) {
            return isEqualToDouble_alt(r.negate());
        }
        int exponent = r.binaryExponent();
        if (exponent > Double.MAX_EXPONENT || exponent == Double.MAX_EXPONENT && gt(r, LARGEST_DOUBLE)) {
            return false;
        }
        int shift = exponent < Double.MIN_EXPONENT ? MIN_SUBNORMAL_DOUBLE_EXPONENT : exponent - DOUBLE_FRACTION_WIDTH;
        return r.shiftRight(shift).isInteger();
    }

    private void propertiesIsEqualToDouble() {
        initialize("isEqualToDouble()");
        for (Rational r : take(LIMIT, P.rationals())) {
            boolean ietd = r.isEqualToDouble();
            assertTrue(r, !ietd || r.isBinaryFraction());
            homomorphic(
                    Rational::negate,
                    Function.identity(),
                    Rational::isEqualToDouble,
                    Rational::isEqualToDouble,
                    r
            );
            assertEquals(r, isEqualToDouble_simplest(r), ietd);
            assertEquals(r, isEqualToDouble_alt(r), ietd);
        }

        for (double d : take(LIMIT, filter(e -> Double.isFinite(e) && !isNegativeZero(e), P.doubles()))) {
            assertTrue(d, ofExact(d).get().isEqualToDouble());
        }
    }

    private void compareImplementationsIsEqualToDouble() {
        Map<String, Function<Rational, Boolean>> functions = new LinkedHashMap<>();
        functions.put("simplest", RationalProperties::isEqualToDouble_simplest);
        functions.put("alt", RationalProperties::isEqualToDouble_alt);
        functions.put("standard", Rational::isEqualToDouble);
        compareImplementations("isEqualToDouble()", take(LIMIT, P.rationals()), functions, v -> P.reset());
    }

    private static boolean floatEquidistant(@NotNull Rational r) {
        float below = r.floatValue(RoundingMode.FLOOR);
        float above = r.floatValue(RoundingMode.CEILING);
        if (below == above || Float.isInfinite(below) || Float.isInfinite(above)) return false;
        Rational belowDistance = r.subtract(ofExact(below).get());
        Rational aboveDistance = ofExact(above).get().subtract(r);
        return belowDistance.equals(aboveDistance);
    }

    private void propertiesFloatValue_RoundingMode() {
        initialize("floatValue(RoundingMode)");
        Iterable<Pair<Rational, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isEqualToFloat(),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            float rounded = p.a.floatValue(p.b);
            assertTrue(p, !Float.isNaN(rounded));
            assertTrue(p, rounded == 0.0f || Math.signum(rounded) == p.a.signum());
        }

        Iterable<Rational> rs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !isNegativeZero(f), P.floats())
        );
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.UNNECESSARY);
            assertEquals(r, r, ofExact(rounded).get());
            assertTrue(r, Float.isFinite(rounded));
            assertTrue(r, !isNegativeZero(rounded));
            inverse(s -> s.floatValue(RoundingMode.UNNECESSARY), (Float f) -> ofExact(f).get(), r);
        }

        rs = filterInfinite(
                r -> !r.equals(LARGEST_FLOAT),
                P.rationalsIn(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT))
        );
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.FLOOR);
            float successor = successor(rounded);
            assertTrue(r, le(ofExact(rounded).get(), r));
            assertTrue(r, gt(ofExact(successor).get(), r));
            assertTrue(r, rounded < 0 || Float.isFinite(rounded));
            assertTrue(r, !isNegativeZero(rounded));
        }

        rs = filterInfinite(
                r -> !r.equals(LARGEST_FLOAT.negate()),
                P.rationalsIn(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT))
        );
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.CEILING);
            float predecessor = predecessor(rounded);
            assertTrue(r, ge(ofExact(rounded).get(), r));
            assertTrue(r, lt(ofExact(predecessor).get(), r));
            assertTrue(r, rounded > 0 || Float.isFinite(rounded));
        }

        rs = P.rationalsIn(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT));
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.DOWN);
            assertTrue(r, le(ofExact(rounded).get().abs(), r.abs()));
            assertTrue(r, Float.isFinite(rounded));
        }

        rs = filterInfinite(r -> r != ZERO, P.rationalsIn(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.DOWN);
            float successor = successor(rounded);
            float predecessor = predecessor(rounded);
            float down = r.signum() == -1 ? successor : predecessor;
            assertTrue(r, lt(ofExact(down).get().abs(), r.abs()));
        }

        rs = filterInfinite(
                r -> !r.abs().equals(LARGEST_FLOAT),
                P.rationalsIn(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT))
        );
        for (Rational r : take(LIMIT, rs)) {
            assertTrue(r, !isNegativeZero(r.floatValue(RoundingMode.UP)));
        }

        rs = filterInfinite(r -> !r.equals(SMALLEST_FLOAT), P.rationalsIn(Interval.of(ZERO, SMALLEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r, r.floatValue(RoundingMode.FLOOR), 0.0f);
            aeqf(r, r.floatValue(RoundingMode.DOWN), 0.0f);
            float rounded = r.floatValue(RoundingMode.UP);
            float successor = successor(rounded);
            float predecessor = predecessor(rounded);
            float up = r.signum() == -1 ? predecessor : successor;
            assertTrue(r, gt(ofExact(up).get().abs(), r.abs()));
        }

        rs = filterInfinite(
                r -> !r.equals(LARGEST_FLOAT.negate()),
                P.rationalsIn(Interval.lessThanOrEqualTo(LARGEST_FLOAT.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            float floor = r.floatValue(RoundingMode.FLOOR);
            aeqf(r, floor, Float.NEGATIVE_INFINITY);

            float up = r.floatValue(RoundingMode.UP);
            aeqf(r, up, Float.NEGATIVE_INFINITY);

            float halfUp = r.floatValue(RoundingMode.HALF_UP);
            aeqf(r, halfUp, Float.NEGATIVE_INFINITY);

            float halfEven = r.floatValue(RoundingMode.HALF_EVEN);
            aeqf(r, halfEven, Float.NEGATIVE_INFINITY);
        }

        rs = P.rationalsIn(Interval.greaterThanOrEqualTo(LARGEST_FLOAT));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r, r.floatValue(RoundingMode.FLOOR), Float.MAX_VALUE);
            aeqf(r, r.floatValue(RoundingMode.DOWN), Float.MAX_VALUE);
            aeqf(r, r.floatValue(RoundingMode.HALF_DOWN), Float.MAX_VALUE);
        }

        rs = filterInfinite(
                r -> r != ZERO && !r.equals(SMALLEST_FLOAT.negate()),
                P.rationalsIn(Interval.of(SMALLEST_FLOAT.negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r, r.floatValue(RoundingMode.CEILING), -0.0f);
            aeqf(r, r.floatValue(RoundingMode.DOWN), -0.0f);
        }

        rs = filterInfinite(
                r -> !r.equals(LARGEST_FLOAT),
                P.rationalsIn(Interval.greaterThanOrEqualTo(LARGEST_FLOAT))
        );
        for (Rational r : take(LIMIT, rs)) {
            float ceiling = r.floatValue(RoundingMode.CEILING);
            aeqf(r, ceiling, Float.POSITIVE_INFINITY);

            float up = r.floatValue(RoundingMode.UP);
            aeqf(r, up, Float.POSITIVE_INFINITY);

            float halfUp = r.floatValue(RoundingMode.HALF_UP);
            aeqf(r, halfUp, Float.POSITIVE_INFINITY);

            float halfEven = r.floatValue(RoundingMode.HALF_EVEN);
            aeqf(r, halfEven, Float.POSITIVE_INFINITY);
        }

        rs = P.rationalsIn(Interval.lessThanOrEqualTo(LARGEST_FLOAT.negate()));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r, r.floatValue(RoundingMode.CEILING), -Float.MAX_VALUE);
            aeqf(r, r.floatValue(RoundingMode.DOWN), -Float.MAX_VALUE);
            aeqf(r, r.floatValue(RoundingMode.HALF_DOWN), -Float.MAX_VALUE);
        }

        Iterable<Rational> midpoints = map(
                f -> {
                    Rational lo = ofExact(f).get();
                    Rational hi = ofExact(successor(f)).get();
                    return lo.add(hi).shiftRight(1);
                },
                filter(f -> Float.isFinite(f) && !isNegativeZero(f) && f != Float.MAX_VALUE, P.floats())
        );
        for (Rational r : take(LIMIT, midpoints)) {
            float down = r.floatValue(RoundingMode.DOWN);
            float up = r.floatValue(RoundingMode.UP);
            aeqf(r, r.floatValue(RoundingMode.HALF_DOWN), down);
            aeqf(r, r.floatValue(RoundingMode.HALF_UP), up);
            float halfEven = r.floatValue(RoundingMode.HALF_EVEN);
            assertTrue(r, ((Float.floatToIntBits(down) & 1) == 0 ? down : up) == halfEven);
        }

        Iterable<Rational> notMidpoints = filterInfinite(
                r -> !floatEquidistant(r),
                P.rationalsIn(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT))
        );
        for (Rational r : take(LIMIT, notMidpoints)) {
            float below = r.floatValue(RoundingMode.FLOOR);
            float above = r.floatValue(RoundingMode.CEILING);
            Rational belowDistance = r.subtract(ofExact(below).get());
            Rational aboveDistance = ofExact(above).get().subtract(r);
            float closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqf(r, r.floatValue(RoundingMode.HALF_DOWN), closest);
            aeqf(r, r.floatValue(RoundingMode.HALF_UP), closest);
            aeqf(r, r.floatValue(RoundingMode.HALF_EVEN), closest);
        }

        rs = P.rationalsIn(Interval.of(ZERO, SMALLEST_FLOAT.shiftRight(1)));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r, r.floatValue(RoundingMode.HALF_DOWN), 0.0f);
            aeqf(r, r.floatValue(RoundingMode.HALF_EVEN), 0.0f);
        }

        rs = filterInfinite(r -> r != ZERO, P.rationalsIn(Interval.of(SMALLEST_FLOAT.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r, r.floatValue(RoundingMode.HALF_DOWN), -0.0f);
            aeqf(r, r.floatValue(RoundingMode.HALF_EVEN), -0.0f);
        }

        rs = filterInfinite(
                r -> !r.equals(SMALLEST_FLOAT.shiftRight(1)),
                P.rationalsIn(Interval.of(ZERO, SMALLEST_FLOAT.shiftRight(1)))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r, r.floatValue(RoundingMode.HALF_UP), 0.0f);
        }

        rs = filterInfinite(
                r -> r != ZERO && !r.equals(SMALLEST_FLOAT.shiftRight(1).negate()),
                P.rationalsIn(Interval.of(SMALLEST_FLOAT.shiftRight(1).negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r, r.floatValue(RoundingMode.HALF_UP), -0.0f);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            float floor = r.floatValue(RoundingMode.FLOOR);
            assertFalse(r, isNegativeZero(floor));
            assertFalse(r, floor == Float.POSITIVE_INFINITY);
            float ceiling = r.floatValue(RoundingMode.CEILING);
            assertFalse(r, ceiling == Float.NEGATIVE_INFINITY);
            float down = r.floatValue(RoundingMode.DOWN);
            assertTrue(r, Float.isFinite(down));
            float up = r.floatValue(RoundingMode.UP);
            assertFalse(r, isNegativeZero(up));
            float halfDown = r.floatValue(RoundingMode.HALF_DOWN);
            assertTrue(r, Float.isFinite(halfDown));
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isEqualToFloat(), P.rationals()))) {
            try {
                r.floatValue(RoundingMode.UNNECESSARY);
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesFloatValue() {
        initialize("floatValue()");
        for (Rational r : take(LIMIT, P.rationals())) {
            float rounded = r.floatValue();
            aeqf(r, rounded, r.floatValue(RoundingMode.HALF_EVEN));
            assertTrue(r, !Float.isNaN(rounded));
            assertTrue(r, rounded == 0.0f || Math.signum(rounded) == r.signum());
        }

        Iterable<Rational> rs = filterInfinite(
                r -> !r.equals(LARGEST_FLOAT.negate()),
                P.rationalsIn(Interval.lessThanOrEqualTo(LARGEST_FLOAT.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue();
            aeqf(r, rounded, Float.NEGATIVE_INFINITY);
        }

        rs = filterInfinite(
                r -> !r.equals(LARGEST_FLOAT),
                P.rationalsIn(Interval.greaterThanOrEqualTo(LARGEST_FLOAT)
        ));
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue();
            aeqf(r, rounded, Float.POSITIVE_INFINITY);
        }

        Iterable<Rational> midpoints = map(
                f -> {
                    Rational lo = ofExact(f).get();
                    Rational hi = ofExact(successor(f)).get();
                    return lo.add(hi).shiftRight(1);
                },
                filter(f -> Float.isFinite(f) && f != Float.MAX_VALUE, P.floats())
        );
        for (Rational r : take(LIMIT, midpoints)) {
            float down = r.floatValue(RoundingMode.DOWN);
            float up = r.floatValue(RoundingMode.UP);
            float rounded = r.floatValue();
            assertTrue(r, ((Float.floatToIntBits(down) & 1) == 0 ? down : up) == rounded);
        }

        Iterable<Rational> notMidpoints = filterInfinite(
                r -> ge(r, LARGEST_FLOAT.negate()) && le(r, LARGEST_FLOAT) && !floatEquidistant(r),
                P.rationals()
        );
        for (Rational r : take(LIMIT, notMidpoints)) {
            float below = r.floatValue(RoundingMode.FLOOR);
            float above = r.floatValue(RoundingMode.CEILING);
            Rational belowDistance = r.subtract(ofExact(below).get());
            Rational aboveDistance = ofExact(above).get().subtract(r);
            float closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqf(r, r.floatValue(), closest);
        }

        for (Rational r : take(LIMIT, P.rationalsIn(Interval.of(ZERO, SMALLEST_FLOAT.shiftRight(1))))) {
            aeqf(r, r.floatValue(), 0.0f);
        }

        rs = filterInfinite(r -> r != ZERO, P.rationalsIn(Interval.of(SMALLEST_FLOAT.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r, r.floatValue(), -0.0f);
        }
    }

    private void propertiesFloatValueExact() {
        initialize("floatValueExact()");
        for (Rational r : take(LIMIT, filterInfinite(Rational::isEqualToFloat, P.rationals()))) {
            float f = r.floatValueExact();
            assertTrue(r, !Float.isNaN(f));
            assertTrue(r, f == 0.0f || Math.signum(f) == r.signum());
            homomorphic(
                    Rational::negate,
                    g -> absNegativeZeros(-g),
                    Rational::floatValueExact,
                    Rational::floatValueExact,
                    r
            );
        }

        Iterable<Rational> rs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !isNegativeZero(f), P.floats())
        );
        for (Rational r : take(LIMIT, rs)) {
            float f = r.floatValueExact();
            assertEquals(r, r, ofExact(f).get());
            assertTrue(r, Float.isFinite(f));
            assertTrue(r, !isNegativeZero(f));
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isEqualToFloat(), P.rationals()))) {
            try {
                r.floatValueExact();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static boolean doubleEquidistant(@NotNull Rational r) {
        double below = r.doubleValue(RoundingMode.FLOOR);
        double above = r.doubleValue(RoundingMode.CEILING);
        if (below == above || Double.isInfinite(below) || Double.isInfinite(above)) return false;
        Rational belowDistance = r.subtract(ofExact(below).get());
        Rational aboveDistance = ofExact(above).get().subtract(r);
        return belowDistance.equals(aboveDistance);
    }

    private void propertiesDoubleValue_RoundingMode() {
        initialize("doubleValue(RoundingMode)");
        Iterable<Pair<Rational, RoundingMode>> ps = filter(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isEqualToDouble(),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            double rounded = p.a.doubleValue(p.b);
            assertTrue(p, !Double.isNaN(rounded));
            assertTrue(p, rounded == 0.0 || Math.signum(rounded) == p.a.signum());
        }

        Iterable<Rational> rs = map(
                d -> ofExact(d).get(),
                filter(d -> Double.isFinite(d) && !isNegativeZero(d), P.doubles())
        );
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.UNNECESSARY);
            assertEquals(r, r, ofExact(rounded).get());
            assertTrue(r, Double.isFinite(rounded));
            assertTrue(r, !isNegativeZero(rounded));
            inverse(s -> s.doubleValue(RoundingMode.UNNECESSARY), (Double d) -> ofExact(d).get(), r);
        }

        rs = filterInfinite(
                r -> !r.equals(LARGEST_DOUBLE),
                P.rationalsIn(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.FLOOR);
            double successor = successor(rounded);
            assertTrue(r, le(ofExact(rounded).get(), r));
            assertTrue(r, gt(ofExact(successor).get(), r));
            assertTrue(r, rounded < 0 || Double.isFinite(rounded));
            assertTrue(r, !isNegativeZero(rounded));
        }

        rs = filterInfinite(
                r -> !r.equals(LARGEST_DOUBLE.negate()),
                P.rationalsIn(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.CEILING);
            double predecessor = predecessor(rounded);
            assertTrue(r, ge(ofExact(rounded).get(), r));
            assertTrue(r, lt(ofExact(predecessor).get(), r));
            assertTrue(r, rounded > 0 || Double.isFinite(rounded));
        }

        rs = P.rationalsIn(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE));
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.DOWN);
            assertTrue(r, le(ofExact(rounded).get().abs(), r.abs()));
            assertTrue(r, Double.isFinite(rounded));
        }

        rs = filterInfinite(r -> r != ZERO, P.rationalsIn(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.DOWN);
            double successor = successor(rounded);
            double predecessor = predecessor(rounded);
            double down = r.signum() == -1 ? successor : predecessor;
            assertTrue(r, lt(ofExact(down).get().abs(), r.abs()));
        }

        rs = filterInfinite(
                r -> !r.abs().equals(LARGEST_DOUBLE),
                P.rationalsIn(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rs)) {
            assertTrue(r, !isNegativeZero(r.doubleValue(RoundingMode.UP)));
        }

        rs = filterInfinite(r -> !r.equals(SMALLEST_DOUBLE), P.rationalsIn(Interval.of(ZERO, SMALLEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r, r.doubleValue(RoundingMode.FLOOR), 0.0);
            aeqd(r, r.doubleValue(RoundingMode.DOWN), 0.0);
            double rounded = r.doubleValue(RoundingMode.UP);
            double successor = successor(rounded);
            double predecessor = predecessor(rounded);
            double up = r.signum() == -1 ? predecessor : successor;
            assertTrue(r, gt(ofExact(up).get().abs(), r.abs()));
        }

        rs = filterInfinite(
                r -> !r.equals(LARGEST_DOUBLE.negate()),
                P.rationalsIn(Interval.lessThanOrEqualTo(LARGEST_DOUBLE.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            double floor = r.doubleValue(RoundingMode.FLOOR);
            aeqd(r, floor, Double.NEGATIVE_INFINITY);

            double up = r.doubleValue(RoundingMode.UP);
            aeqd(r, up, Double.NEGATIVE_INFINITY);

            double halfUp = r.doubleValue(RoundingMode.HALF_UP);
            aeqd(r, halfUp, Double.NEGATIVE_INFINITY);

            double halfEven = r.doubleValue(RoundingMode.HALF_EVEN);
            aeqd(r, halfEven, Double.NEGATIVE_INFINITY);
        }

        rs = P.rationalsIn(Interval.greaterThanOrEqualTo(LARGEST_DOUBLE));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r, r.doubleValue(RoundingMode.FLOOR), Double.MAX_VALUE);
            aeqd(r, r.doubleValue(RoundingMode.DOWN), Double.MAX_VALUE);
            aeqd(r, r.doubleValue(RoundingMode.HALF_DOWN), Double.MAX_VALUE);
        }

        rs = filterInfinite(
                r -> r != ZERO && !r.equals(SMALLEST_DOUBLE.negate()),
                P.rationalsIn(Interval.of(SMALLEST_DOUBLE.negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r, r.doubleValue(RoundingMode.CEILING), -0.0);
            aeqd(r, r.doubleValue(RoundingMode.DOWN), -0.0);
        }

        rs = filterInfinite(
                r -> !r.equals(LARGEST_DOUBLE),
                P.rationalsIn(Interval.greaterThanOrEqualTo(LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rs)) {
            double ceiling = r.doubleValue(RoundingMode.CEILING);
            aeqd(r, ceiling, Double.POSITIVE_INFINITY);

            double up = r.doubleValue(RoundingMode.UP);
            aeqd(r, up, Double.POSITIVE_INFINITY);

            double halfUp = r.doubleValue(RoundingMode.HALF_UP);
            aeqd(r, halfUp, Double.POSITIVE_INFINITY);

            double halfEven = r.doubleValue(RoundingMode.HALF_EVEN);
            aeqd(r, halfEven, Double.POSITIVE_INFINITY);
        }

        rs = P.rationalsIn(Interval.lessThanOrEqualTo(LARGEST_DOUBLE.negate()));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r, r.doubleValue(RoundingMode.CEILING), -Double.MAX_VALUE);
            aeqd(r, r.doubleValue(RoundingMode.DOWN), -Double.MAX_VALUE);
            aeqd(r, r.doubleValue(RoundingMode.HALF_DOWN), -Double.MAX_VALUE);
        }

        Iterable<Rational> midpoints = map(
                d -> {
                    Rational lo = ofExact(d).get();
                    Rational hi = ofExact(successor(d)).get();
                    return lo.add(hi).shiftRight(1);
                },
                filter(d -> Double.isFinite(d) && d != Double.MAX_VALUE, P.doubles())
        );
        for (Rational r : take(LIMIT, midpoints)) {
            double down = r.doubleValue(RoundingMode.DOWN);
            double up = r.doubleValue(RoundingMode.UP);
            aeqd(r, r.doubleValue(RoundingMode.HALF_DOWN), down);
            aeqd(r, r.doubleValue(RoundingMode.HALF_UP), up);
            double halfEven = r.doubleValue(RoundingMode.HALF_EVEN);
            assertTrue(r, ((Double.doubleToLongBits(down) & 1) == 0 ? down : up) == halfEven);
        }

        Iterable<Rational> notMidpoints = filterInfinite(
                r -> !doubleEquidistant(r),
                P.rationalsIn(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, notMidpoints)) {
            double below = r.doubleValue(RoundingMode.FLOOR);
            double above = r.doubleValue(RoundingMode.CEILING);
            Rational belowDistance = r.subtract(ofExact(below).get());
            Rational aboveDistance = ofExact(above).get().subtract(r);
            double closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqd(r, r.doubleValue(RoundingMode.HALF_DOWN), closest);
            aeqd(r, r.doubleValue(RoundingMode.HALF_UP), closest);
            aeqd(r, r.doubleValue(RoundingMode.HALF_EVEN), closest);
        }

        rs = P.rationalsIn(Interval.of(ZERO, SMALLEST_DOUBLE.shiftRight(1)));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r, r.doubleValue(RoundingMode.HALF_DOWN), 0.0);
            aeqd(r, r.doubleValue(RoundingMode.HALF_EVEN), 0.0);
        }

        rs = filterInfinite(r -> r != ZERO, P.rationalsIn(Interval.of(SMALLEST_DOUBLE.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r, r.doubleValue(RoundingMode.HALF_DOWN), -0.0);
            aeqd(r, r.doubleValue(RoundingMode.HALF_EVEN), -0.0);
        }

        rs = filterInfinite(
                r -> !r.equals(SMALLEST_DOUBLE.shiftRight(1)),
                P.rationalsIn(Interval.of(ZERO, SMALLEST_DOUBLE.shiftRight(1)))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r, r.doubleValue(RoundingMode.HALF_UP), 0.0);
        }

        rs = filterInfinite(
                r -> r != ZERO && !r.equals(SMALLEST_DOUBLE.shiftRight(1).negate()),
                P.rationalsIn(Interval.of(SMALLEST_DOUBLE.shiftRight(1).negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r, r.doubleValue(RoundingMode.HALF_UP), -0.0);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            double floor = r.doubleValue(RoundingMode.FLOOR);
            assertFalse(r, isNegativeZero(floor));
            assertFalse(r, floor == Double.POSITIVE_INFINITY);
            double ceiling = r.doubleValue(RoundingMode.CEILING);
            assertFalse(r, ceiling == Double.NEGATIVE_INFINITY);
            double down = r.doubleValue(RoundingMode.DOWN);
            assertTrue(r, Double.isFinite(down));
            double up = r.doubleValue(RoundingMode.UP);
            assertFalse(r, isNegativeZero(up));
            double halfDown = r.doubleValue(RoundingMode.HALF_DOWN);
            assertTrue(r, Double.isFinite(halfDown));
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isEqualToDouble(), P.rationals()))) {
            try {
                r.doubleValue(RoundingMode.UNNECESSARY);
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesDoubleValue() {
        initialize("doubleValue()");
        for (Rational r : take(LIMIT, P.rationals())) {
            double rounded = r.doubleValue();
            aeqd(r, rounded, r.doubleValue(RoundingMode.HALF_EVEN));
            assertTrue(r, !Double.isNaN(rounded));
            assertTrue(r, rounded == 0.0 || Math.signum(rounded) == r.signum());
        }

        Iterable<Rational> rs = filterInfinite(
                r -> !r.equals(LARGEST_DOUBLE.negate()),
                P.rationalsIn(Interval.lessThanOrEqualTo(LARGEST_DOUBLE.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue();
            aeqd(r, rounded, Double.NEGATIVE_INFINITY);
        }

        rs = filterInfinite(
                r -> !r.equals(LARGEST_DOUBLE),
                P.rationalsIn(Interval.greaterThanOrEqualTo(LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue();
            aeqd(r, rounded, Double.POSITIVE_INFINITY);
        }

        Iterable<Rational> midpoints = map(
                d -> {
                    Rational lo = ofExact(d).get();
                    Rational hi = ofExact(successor(d)).get();
                    return lo.add(hi).shiftRight(1);
                },
                filter(d -> Double.isFinite(d) && d != Double.MAX_VALUE, P.doubles())
        );
        for (Rational r : take(LIMIT, midpoints)) {
            double down = r.doubleValue(RoundingMode.DOWN);
            double up = r.doubleValue(RoundingMode.UP);
            double rounded = r.doubleValue();
            assertTrue(r, ((Double.doubleToLongBits(down) & 1) == 0 ? down : up) == rounded);
        }

        Iterable<Rational> notMidpoints = filterInfinite(
                r -> ge(r, LARGEST_DOUBLE.negate()) && le(r, LARGEST_DOUBLE) && !doubleEquidistant(r),
                P.rationals()
        );
        for (Rational r : take(LIMIT, notMidpoints)) {
            double below = r.doubleValue(RoundingMode.FLOOR);
            double above = r.doubleValue(RoundingMode.CEILING);
            Rational belowDistance = r.subtract(ofExact(below).get());
            Rational aboveDistance = ofExact(above).get().subtract(r);
            double closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqd(r, r.doubleValue(), closest);
        }

        for (Rational r : take(LIMIT, P.rationalsIn(Interval.of(ZERO, SMALLEST_DOUBLE.shiftRight(1))))) {
            aeqd(r, r.doubleValue(), 0.0);
        }

        rs = filterInfinite(r -> r != ZERO, P.rationalsIn(Interval.of(SMALLEST_DOUBLE.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r, r.doubleValue(), -0.0);
        }
    }

    private void propertiesDoubleValueExact() {
        initialize("doubleValueExact()");
        for (Rational r : take(LIMIT, filterInfinite(Rational::isEqualToDouble, P.rationals()))) {
            double d = r.doubleValueExact();
            assertTrue(r, !Double.isNaN(d));
            assertTrue(r, d == 0.0 || Math.signum(d) == r.signum());
            homomorphic(
                    Rational::negate,
                    e -> absNegativeZeros(-e),
                    Rational::doubleValueExact,
                    Rational::doubleValueExact,
                    r
            );
        }

        Iterable<Rational> rs = map(
                d -> ofExact(d).get(),
                filter(d -> Double.isFinite(d) && !isNegativeZero(d), P.doubles())
        );
        for (Rational r : take(LIMIT, rs)) {
            double d = r.doubleValueExact();
            assertEquals(r, r, ofExact(d).get());
            assertTrue(r, Double.isFinite(d));
            assertTrue(r, !isNegativeZero(d));
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.isEqualToDouble(), P.rationals()))) {
            try {
                r.doubleValueExact();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesHasTerminatingBaseExpansion() {
        initialize("hasTerminatingBaseExpansion(BigInteger)");
        //noinspection Convert2MethodRef
        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(
                P.withScale(8).rationals(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            boolean result = p.a.hasTerminatingBaseExpansion(p.b);
            Iterable<BigInteger> dPrimeFactors = nub(MathUtils.primeFactors(p.a.getDenominator()));
            Iterable<BigInteger> bPrimeFactors = nub(MathUtils.primeFactors(p.b));
            assertEquals(p, result, isSubsetOf(dPrimeFactors, bPrimeFactors));
            Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> pn = p.a.abs().positionalNotation(p.b);
            assertEquals(p, result, pn.c.equals(Collections.singletonList(BigInteger.ZERO)));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.rangeDown(BigInteger.ONE)))) {
            try {
                p.a.hasTerminatingBaseExpansion(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByPrecision_int_RoundingMode() {
        initialize("bigDecimalValueByPrecision(int, RoundingMode)");
        Predicate<Triple<Rational, Integer, RoundingMode>> valid = t -> {
            try {
                t.a.bigDecimalValueByPrecision(t.b, t.c);
                return true;
            } catch (ArithmeticException e) {
                return false;
            }
        };
        Iterable<Triple<Rational, Integer, RoundingMode>> ts = filterInfinite(
                valid,
                P.triples(P.rationals(), P.naturalIntegersGeometric(), P.roundingModes())
        );
        for (Triple<Rational, Integer, RoundingMode> t : take(LIMIT, ts)) {
            BigDecimal bd = t.a.bigDecimalValueByPrecision(t.b, t.c);
            assertTrue(t, eq(bd, BigDecimal.ZERO) || bd.signum() == t.a.signum());
        }

        ts = filterInfinite(valid, P.triples(P.nonzeroRationals(), P.positiveIntegersGeometric(), P.roundingModes()));
        for (Triple<Rational, Integer, RoundingMode> t : take(LIMIT, ts)) {
            BigDecimal bd = t.a.bigDecimalValueByPrecision(t.b, t.c);
            assertTrue(t, bd.precision() == t.b);
        }

        Iterable<Pair<Rational, Integer>> ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.UNNECESSARY)),
                P.pairsSquareRootOrder(P.rationals(), P.naturalIntegersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.UNNECESSARY);
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.FLOOR));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.CEILING));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.UP));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.HALF_DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.HALF_UP));
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.HALF_EVEN));
        }

        ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.FLOOR)) &&
                        !of(q.a.bigDecimalValueByPrecision(q.b)).equals(q.a),
                P.pairsSquareRootOrder(P.rationals(), P.naturalIntegersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            BigDecimal low = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.FLOOR);
            BigDecimal high = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.CEILING);
            assertTrue(p, lt(low, high));
        }

        ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.FLOOR)) &&
                        !of(q.a.bigDecimalValueByPrecision(q.b)).equals(q.a),
                P.pairsSquareRootOrder(P.positiveRationals(), P.naturalIntegersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.UP);
            assertEquals(p, floor, down);
            assertEquals(p, ceiling, up);
        }

        ps = filterInfinite(
                q -> valid.test(new Triple<>(q.a, q.b, RoundingMode.FLOOR)) &&
                        !of(q.a.bigDecimalValueByPrecision(q.b)).equals(q.a),
                P.pairsSquareRootOrder(P.negativeRationals(), P.naturalIntegersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByPrecision(p.b, RoundingMode.UP);
            assertEquals(p, floor, up);
            assertEquals(p, ceiling, down);
        }

        BigInteger five = BigInteger.valueOf(5);
        Iterable<BigDecimal> bds = filterInfinite(
                bd -> bd.precision() > 1 && !bd.abs().unscaledValue().mod(BigInteger.TEN).equals(five),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int precision = bd.precision() - 1;
            Rational r = of(bd);
            BigDecimal down = r.bigDecimalValueByPrecision(precision, RoundingMode.DOWN);
            BigDecimal up = r.bigDecimalValueByPrecision(precision, RoundingMode.UP);
            BigDecimal halfDown = r.bigDecimalValueByPrecision(precision, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = r.bigDecimalValueByPrecision(precision, RoundingMode.HALF_UP);
            BigDecimal halfEven = r.bigDecimalValueByPrecision(precision, RoundingMode.HALF_EVEN);
            BigDecimal closest = lt(r.subtract(of(down)).abs(), r.subtract(of(up)).abs()) ? down : up;
            assertEquals(bd, halfDown, closest);
            assertEquals(bd, halfUp, closest);
            assertEquals(bd, halfEven, closest);
        }

        bds = filterInfinite(
                bd -> bd.precision() > 1,
                map(
                        bd -> new BigDecimal(bd.unscaledValue().multiply(BigInteger.TEN).add(five), bd.scale()),
                        P.bigDecimals()
                )
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int precision = bd.precision() - 1;
            Rational r = of(bd);
            BigDecimal down = r.bigDecimalValueByPrecision(precision, RoundingMode.DOWN);
            BigDecimal up = r.bigDecimalValueByPrecision(precision, RoundingMode.UP);
            BigDecimal halfDown = r.bigDecimalValueByPrecision(precision, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = r.bigDecimalValueByPrecision(precision, RoundingMode.HALF_UP);
            BigDecimal halfEven = r.bigDecimalValueByPrecision(precision, RoundingMode.HALF_EVEN);
            assertEquals(bd, down, halfDown);
            assertEquals(bd, up, halfUp);
            assertTrue(bd, bd.scale() != halfEven.scale() + 1 || !halfEven.unscaledValue().testBit(0));
        }

        Iterable<Triple<Rational, Integer, RoundingMode>> tsFail = P.triples(
                P.nonzeroRationals(),
                P.negativeIntegers(),
                P.roundingModes()
        );
        for (Triple<Rational, Integer, RoundingMode> t : take(LIMIT, tsFail)) {
            try {
                t.a.bigDecimalValueByPrecision(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<Pair<Rational, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(r -> !r.hasTerminatingBaseExpansion(BigInteger.TEN), P.rationals()),
                P.naturalIntegersGeometric()
        );
        for (Pair<Rational, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.bigDecimalValueByPrecision(p.b, RoundingMode.UNNECESSARY);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByScale_int_RoundingMode() {
        initialize("bigDecimalValueByScale(int, RoundingMode)");
        Predicate<Triple<Rational, Integer, RoundingMode>> valid =
                t -> t.c != RoundingMode.UNNECESSARY || t.a.multiply(TEN.pow(t.b)).isInteger();
        Iterable<Triple<Rational, Integer, RoundingMode>> ts = filterInfinite(
                valid,
                P.triples(P.rationals(), P.integersGeometric(), P.roundingModes())
        );
        for (Triple<Rational, Integer, RoundingMode> t : take(LIMIT, ts)) {
            BigDecimal bd = t.a.bigDecimalValueByScale(t.b, t.c);
            assertTrue(t, bd.scale() == t.b);
            assertTrue(t, eq(bd, BigDecimal.ZERO) || bd.signum() == t.a.signum());
        }

        Iterable<Pair<Rational, Integer>> ps = filterInfinite(
                q -> q.a.multiply(TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(P.rationals(), P.integersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByScale(p.b, RoundingMode.UNNECESSARY);
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.FLOOR));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.CEILING));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.UP));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.HALF_DOWN));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.HALF_UP));
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.HALF_EVEN));
        }

        ps = filterInfinite(
                q -> !q.a.multiply(TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(P.rationals(), P.integersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            BigDecimal low = p.a.bigDecimalValueByScale(p.b, RoundingMode.FLOOR);
            BigDecimal high = p.a.bigDecimalValueByScale(p.b, RoundingMode.CEILING);
            Rational lowR = of(low);
            Rational highR = of(high);
            assertTrue(p, gt(p.a, lowR));
            assertTrue(p, lt(p.a, highR));
            assertEquals(p, highR.subtract(lowR), TEN.pow(-p.b));
        }

        ps = filterInfinite(
                q -> !q.a.multiply(TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(P.positiveRationals(), P.integersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByScale(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByScale(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByScale(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByScale(p.b, RoundingMode.UP);
            assertEquals(p, floor, down);
            assertEquals(p, ceiling, up);
        }

        ps = filterInfinite(
                q -> !q.a.multiply(TEN.pow(q.b)).isInteger(),
                P.pairsSquareRootOrder(P.negativeRationals(), P.integersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            BigDecimal floor = p.a.bigDecimalValueByScale(p.b, RoundingMode.FLOOR);
            BigDecimal down = p.a.bigDecimalValueByScale(p.b, RoundingMode.DOWN);
            BigDecimal ceiling = p.a.bigDecimalValueByScale(p.b, RoundingMode.CEILING);
            BigDecimal up = p.a.bigDecimalValueByScale(p.b, RoundingMode.UP);
            assertEquals(p, floor, up);
            assertEquals(p, ceiling, down);
        }

        BigInteger five = BigInteger.valueOf(5);
        Iterable<BigDecimal> bds = filterInfinite(
                bd -> !bd.abs().unscaledValue().mod(BigInteger.TEN).equals(five),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int scale = bd.scale() - 1;
            Rational r = of(bd);
            BigDecimal down = r.bigDecimalValueByScale(scale, RoundingMode.DOWN);
            BigDecimal up = r.bigDecimalValueByScale(scale, RoundingMode.UP);
            BigDecimal halfDown = r.bigDecimalValueByScale(scale, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = r.bigDecimalValueByScale(scale, RoundingMode.HALF_UP);
            BigDecimal halfEven = r.bigDecimalValueByScale(scale, RoundingMode.HALF_EVEN);
            BigDecimal closest = lt(r.subtract(of(down)).abs(), r.subtract(of(up)).abs()) ? down : up;
            assertEquals(bd, halfDown, closest);
            assertEquals(bd, halfUp, closest);
            assertEquals(bd, halfEven, closest);
        }

        bds = map(
                bd -> new BigDecimal(bd.unscaledValue().multiply(BigInteger.TEN).add(five), bd.scale()),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int scale = bd.scale() - 1;
            Rational r = of(bd);
            BigDecimal down = r.bigDecimalValueByScale(scale, RoundingMode.DOWN);
            BigDecimal up = r.bigDecimalValueByScale(scale, RoundingMode.UP);
            BigDecimal halfDown = r.bigDecimalValueByScale(scale, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = r.bigDecimalValueByScale(scale, RoundingMode.HALF_UP);
            BigDecimal halfEven = r.bigDecimalValueByScale(scale, RoundingMode.HALF_EVEN);
            assertEquals(bd, down, halfDown);
            assertEquals(bd, up, halfUp);
            assertTrue(bd, !halfEven.unscaledValue().testBit(0));
        }

        Iterable<Pair<Rational, Integer>> psFail = P.pairsSquareRootOrder(
                filterInfinite(r -> !r.hasTerminatingBaseExpansion(BigInteger.TEN), P.rationals()),
                P.naturalIntegersGeometric()
        );
        for (Pair<Rational, Integer> p : take(LIMIT, psFail)) {
            try {
                p.a.bigDecimalValueByScale(p.b, RoundingMode.UNNECESSARY);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByPrecision_int() {
        initialize("bigDecimalValueByPrecision(int)");
        Predicate<Pair<Rational, Integer>> valid = p -> {
            try {
                p.a.bigDecimalValueByPrecision(p.b);
                return true;
            } catch (ArithmeticException e) {
                return false;
            }
        };
        Iterable<Pair<Rational, Integer>> ps = filterInfinite(
                valid,
                P.pairsSquareRootOrder(P.rationals(), P.naturalIntegersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByPrecision(p.b);
            assertEquals(p, bd, p.a.bigDecimalValueByPrecision(p.b, RoundingMode.HALF_EVEN));
            assertTrue(p, eq(bd, BigDecimal.ZERO) || bd.signum() == p.a.signum());
        }

        ps = filterInfinite(valid::test, P.pairsSquareRootOrder(P.nonzeroRationals(), P.positiveIntegersGeometric()));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValueByPrecision(p.b);
            assertTrue(p, bd.precision() == p.b);
        }

        BigInteger five = BigInteger.valueOf(5);
        Iterable<BigDecimal> bds = filterInfinite(
                bd -> bd.precision() > 1 && !bd.abs().unscaledValue().mod(BigInteger.TEN).equals(five),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int precision = bd.precision() - 1;
            Rational r = of(bd);
            BigDecimal down = r.bigDecimalValueByPrecision(precision, RoundingMode.DOWN);
            BigDecimal up = r.bigDecimalValueByPrecision(precision, RoundingMode.UP);
            BigDecimal halfEven = r.bigDecimalValueByPrecision(precision);
            boolean closerToDown = lt(r.subtract(of(down)).abs(), r.subtract(of(up)).abs());
            assertEquals(bd, halfEven, closerToDown ? down : up);
        }

        bds = filterInfinite(
                bd -> bd.precision() > 1,
                map(
                        bd -> new BigDecimal(bd.unscaledValue().multiply(BigInteger.TEN).add(five), bd.scale()),
                        P.bigDecimals()
                )
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int precision = bd.precision() - 1;
            Rational r = of(bd);
            BigDecimal halfEven = r.bigDecimalValueByPrecision(precision);
            assertTrue(bd, bd.scale() != halfEven.scale() + 1 || !halfEven.unscaledValue().testBit(0));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.negativeIntegers()))) {
            try {
                p.a.bigDecimalValueByPrecision(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesBigDecimalValueByScale_int() {
        initialize("bigDecimalValueByScale(int)");
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairsSquareRootOrder(P.rationals(), P.integersGeometric()))) {
            BigDecimal bd = p.a.bigDecimalValueByScale(p.b);
            assertEquals(p, bd, p.a.bigDecimalValueByScale(p.b, RoundingMode.HALF_EVEN));
            assertTrue(p, eq(bd, BigDecimal.ZERO) || bd.signum() == p.a.signum());
            assertTrue(p, bd.scale() == p.b);
        }

        BigInteger five = BigInteger.valueOf(5);
        Iterable<BigDecimal> bds = filterInfinite(
                bd -> !bd.abs().unscaledValue().mod(BigInteger.TEN).equals(five),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int scale = bd.scale() - 1;
            Rational r = of(bd);
            BigDecimal down = r.bigDecimalValueByScale(scale, RoundingMode.DOWN);
            BigDecimal up = r.bigDecimalValueByScale(scale, RoundingMode.UP);
            BigDecimal halfEven = r.bigDecimalValueByScale(scale);
            BigDecimal closer = lt(r.subtract(of(down)).abs(), r.subtract(of(up)).abs()) ? down : up;
            assertEquals(bd, halfEven, closer);
        }

        bds = map(
                bd -> new BigDecimal(bd.unscaledValue().multiply(BigInteger.TEN).add(five), bd.scale()),
                P.bigDecimals()
        );
        for (BigDecimal bd : take(LIMIT, bds)) {
            int scale = bd.scale() - 1;
            Rational r = of(bd);
            BigDecimal halfEven = r.bigDecimalValueByScale(scale);
            assertTrue(bd, !halfEven.unscaledValue().testBit(0));
        }
    }

    private void propertiesBigDecimalValueExact() {
        initialize("bigDecimalValueExact()");
        Iterable<Rational> rs = filterInfinite(r -> r.hasTerminatingBaseExpansion(BigInteger.TEN), P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            BigDecimal bd = r.bigDecimalValueExact();
            assertTrue(bd, BigDecimalUtils.isCanonical(bd));
            assertEquals(r, bd, r.bigDecimalValueByPrecision(0, RoundingMode.UNNECESSARY));
            assertTrue(r, bd.signum() == r.signum());
            inverse(Rational::bigDecimalValueExact, Rational::of, r);
            homomorphic(
                    Rational::negate,
                    BigDecimal::negate,
                    Rational::bigDecimalValueExact,
                    Rational::bigDecimalValueExact,
                    r
            );
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.negativeIntegers()))) {
            try {
                p.a.bigDecimalValueByPrecision(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<Rational> rsFail = filterInfinite(r -> !r.hasTerminatingBaseExpansion(BigInteger.TEN), P.rationals());
        for (Rational r : take(LIMIT, rsFail)) {
            try {
                r.bigDecimalValueExact();
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesBitLength() {
        initialize("bitLength()");
        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r, r.bitLength() > 0);
            homomorphic(Rational::negate, Function.identity(), Rational::bitLength, Rational::bitLength, r);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            homomorphic(Rational::invert, Function.identity(), Rational::bitLength, Rational::bitLength, r);
        }
    }

    private static @NotNull Rational add_simplest(@NotNull Rational a, @NotNull Rational b) {
        return of(
                a.getNumerator().multiply(b.getDenominator()).add(a.getDenominator().multiply(b.getNumerator())),
                a.getDenominator().multiply(b.getDenominator())
        );
    }

    private void propertiesAdd() {
        initialize("add(Rational)");
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            Rational sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p, sum, add_simplest(p.a, p.b));
            commutative(Rational::add, p);
            inverse(r -> r.add(p.b), (Rational r) -> r.subtract(p.b), p.a);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(ZERO::add, r);
            fixedPoint(s -> r.add(ZERO), r);
            assertTrue(r, r.add(r.negate()) == ZERO);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(P.rationals()))) {
            associative(Rational::add, t);
        }
    }

    private void compareImplementationsAdd() {
        Map<String, Function<Pair<Rational, Rational>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> add_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.add(p.b));
        compareImplementations("add(Rational)", take(LIMIT, P.pairs(P.rationals())), functions, v -> P.reset());
    }

    private void propertiesNegate() {
        initialize("negate()");
        for (Rational r : take(LIMIT, P.rationals())) {
            Rational negative = r.negate();
            negative.validate();
            involution(Rational::negate, r);
            assertTrue(r, r.add(negative) == ZERO);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            assertNotEquals(r, r, r.negate());
        }
    }

    private void propertiesAbs() {
        initialize("abs()");
        for (Rational r : take(LIMIT, P.rationals())) {
            Rational abs = r.abs();
            abs.validate();
            idempotent(Rational::abs, r);
            assertNotEquals(r, abs.signum(), -1);
            assertTrue(r, ge(abs, ZERO));
        }

        for (Rational r : take(LIMIT, P.positiveRationals())) {
            fixedPoint(Rational::abs, r);
        }
    }

    private void propertiesSignum() {
        initialize("testing signum()");
        for (Rational r : take(LIMIT, P.rationals())) {
            int signum = r.signum();
            assertEquals(r, signum, Ordering.compare(r, ZERO).toInt());
            assertTrue(r, signum == -1 || signum == 0 || signum == 1);
        }
    }

    private static @NotNull Rational subtract_simplest(@NotNull Rational a, @NotNull Rational b) {
        return a.add(b.negate());
    }

    private void propertiesSubtract() {
        initialize("subtract(Rational)");
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            Rational difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p, difference, subtract_simplest(p.a, p.b));
            antiCommutative(Rational::subtract, Rational::negate, p);
            inverse(r -> r.add(p.b), (Rational r) -> r.subtract(p.b), p.a);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, ZERO.subtract(r), r.negate());
            fixedPoint(s -> s.subtract(ZERO), r);
            assertTrue(r, r.subtract(r) == ZERO);
        }
    }

    private void compareImplementationsSubtract() {
        Map<String, Function<Pair<Rational, Rational>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> subtract_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.subtract(p.b));
        compareImplementations("subtract(Rational)", take(LIMIT, P.pairs(P.rationals())), functions, v -> P.reset());
    }

    private static @NotNull Pair<BigInteger, BigInteger> multiply_Rational_Knuth(
            @NotNull Rational a,
            @NotNull Rational b
    ) {
        if (a == ZERO || b == ZERO) return new Pair<>(BigInteger.ZERO, BigInteger.ONE);
        if (a == ONE) return new Pair<>(b.getNumerator(), b.getDenominator());
        if (b == ONE) return new Pair<>(a.getNumerator(), a.getDenominator());
        BigInteger numeratorGcd = a.getNumerator().gcd(b.getDenominator());
        BigInteger denominatorGcd = b.getNumerator().gcd(a.getDenominator());
        BigInteger productNumerator =
                a.getNumerator().divide(numeratorGcd).multiply(b.getNumerator().divide(denominatorGcd));
        BigInteger productDenominator =
                a.getDenominator().divide(denominatorGcd).multiply(b.getDenominator().divide(numeratorGcd));
        return productNumerator.equals(productDenominator) ?
                new Pair<>(BigInteger.ONE, BigInteger.ONE) :
                new Pair<>(productNumerator, productDenominator);
    }

    private void propertiesMultiply_Rational() {
        initialize("multiply(Rational)");
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            Rational product = p.a.multiply(p.b);
            product.validate();
            assertEquals(
                    p,
                    new Pair<>(product.getNumerator(), product.getDenominator()),
                    multiply_Rational_Knuth(p.a, p.b)
            );
            commutative(Rational::multiply, p);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals()))) {
            inverse(r -> r.multiply(p.b), (Rational r) -> r.divide(p.b), p.a);
            assertEquals(p, p.a.multiply(p.b), p.a.divide(p.b.invert()));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(r::multiply, ZERO);
            fixedPoint(s -> s.multiply(r), ZERO);
            fixedPoint(ONE::multiply, r);
            fixedPoint(s -> s.multiply(ONE), r);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            assertTrue(r, r.multiply(r.invert()) == ONE);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(P.rationals()))) {
            associative(Rational::multiply, t);
            leftDistributive(Rational::add, Rational::multiply, t);
            rightDistributive(Rational::add, Rational::multiply, t);
        }
    }

    private void compareImplementationsMultiply_Rational() {
        Map<String, Function<Pair<Rational, Rational>, Pair<BigInteger, BigInteger>>> functions =
                new LinkedHashMap<>();
        functions.put("Knuth", p -> multiply_Rational_Knuth(p.a, p.b));
        functions.put(
                "standard",
                p -> {
                    Rational product = p.a.multiply(p.b);
                    return new Pair<>(product.getNumerator(), product.getDenominator());
                }
        );
        compareImplementations("multiply(Rational)", take(LIMIT, P.pairs(P.rationals())), functions, v -> P.reset());
    }

    private static @NotNull Rational multiply_BigInteger_simplest(@NotNull Rational a, @NotNull BigInteger b) {
        return of(a.getNumerator().multiply(b), a.getDenominator());
    }

    private void propertiesMultiply_BigInteger() {
        initialize("multiply(BigInteger)");
        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.bigIntegers()))) {
            Rational product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p, product, multiply_BigInteger_simplest(p.a, p.b));
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroBigIntegers()))) {
            assertEquals(p, p.a.multiply(p.b).divide(p.b), p.a);
            inverse(r -> r.multiply(p.b), (Rational r) -> r.divide(p.b), p.a);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, ONE.multiply(i), of(i));
            fixedPoint(r -> r.multiply(i), ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(s -> s.multiply(BigInteger.ONE), r);
            assertEquals(r, r.multiply(BigInteger.ZERO), ZERO);
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            assertEquals(i, of(i).invert().multiply(i), ONE);
        }

        Iterable<Triple<Rational, Rational, BigInteger>> ts = P.triples(P.rationals(), P.rationals(), P.bigIntegers());
        for (Triple<Rational, Rational, BigInteger> t : take(LIMIT, ts)) {
            rightDistributive(Rational::add, Rational::multiply, new Triple<>(t.c, t.a, t.b));
        }
    }

    private void compareImplementationsMultiply_BigInteger() {
        Map<String, Function<Pair<Rational, BigInteger>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> multiply_BigInteger_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.multiply(p.b));
        compareImplementations(
                "multiply(BigInteger)",
                take(LIMIT, P.pairs(P.rationals(), P.bigIntegers())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Rational multiply_int_simplest(@NotNull Rational a, int b) {
        return of(a.getNumerator().multiply(BigInteger.valueOf(b)), a.getDenominator());
    }

    private void propertiesMultiply_int() {
        initialize("multiply(int)");
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            Rational product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p, product, multiply_int_simplest(p.a, p.b));
            assertEquals(p, product, p.a.multiply(of(p.b)));
            assertEquals(p, product, of(p.b).multiply(p.a));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroIntegers()))) {
            assertEquals(p, p.a.multiply(p.b).divide(p.b), p.a);
            inverse(r -> r.multiply(p.b), (Rational r) -> r.divide(p.b), p.a);
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(i, ONE.multiply(i), of(i));
            fixedPoint(r -> r.multiply(i), ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(s -> s.multiply(1), r);
            assertEquals(r, r.multiply(0), ZERO);
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            assertEquals(i, of(i).invert().multiply(i), ONE);
        }

        Iterable<Triple<Rational, Rational, Integer>> ts = P.triples(P.rationals(), P.rationals(), P.integers());
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, ts)) {
            rightDistributive(Rational::add, Rational::multiply, new Triple<>(t.c, t.a, t.b));
        }
    }

    private void compareImplementationsMultiply_int() {
        Map<String, Function<Pair<Rational, Integer>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> multiply_int_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.multiply(p.b));
        compareImplementations(
                "multiply(int)",
                take(LIMIT, P.pairs(P.rationals(), P.integers())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Rational invert_simplest(@NotNull Rational r) {
        return of(r.getDenominator(), r.getNumerator());
    }

    private void propertiesInvert() {
        initialize("invert()");
        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            Rational inverse = r.invert();
            inverse.validate();
            assertEquals(r, inverse, invert_simplest(r));
            involution(Rational::invert, r);
            assertTrue(r, r.multiply(inverse) == ONE);
            assertTrue(r, inverse != ZERO);
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> s.abs() != ONE, P.nonzeroRationals()))) {
            assertTrue(r, !r.equals(r.invert()));
        }
    }

    private void compareImplementationsInvert() {
        Map<String, Function<Rational, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", RationalProperties::invert_simplest);
        functions.put("standard", Rational::invert);
        compareImplementations("invert()", take(LIMIT, P.nonzeroRationals()), functions, v -> P.reset());
    }

    private static @NotNull Rational divide_Rational_simplest(@NotNull Rational a, @NotNull Rational b) {
        return of(a.getNumerator().multiply(b.getDenominator()), a.getDenominator().multiply(b.getNumerator()));
    }

    private void propertiesDivide_Rational() {
        initialize("divide(Rational)");
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals()))) {
            Rational quotient = p.a.divide(p.b);
            quotient.validate();
            assertEquals(p, quotient, divide_Rational_simplest(p.a, p.b));
            assertEquals(p, quotient, p.a.multiply(p.b.invert()));
            inverse(r -> r.divide(p.b), (Rational r) -> r.multiply(p.b), p.a);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.nonzeroRationals()))) {
            assertEquals(p, p.a.divide(p.b), p.b.divide(p.a).invert());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(s -> s.divide(ONE), r);
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            assertEquals(r, ONE.divide(r), r.invert());
            assertTrue(r, r.divide(r) == ONE);
            fixedPoint(s -> s.divide(r), ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            try {
                r.divide(ZERO);
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsDivide_Rational() {
        Map<String, Function<Pair<Rational, Rational>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> divide_Rational_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.divide(p.b));
        compareImplementations(
                "divide(Rational)",
                take(LIMIT, P.pairs(P.rationals(), P.nonzeroRationals())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Rational divide_BigInteger_simplest(@NotNull Rational a, @NotNull BigInteger b) {
        return of(a.getNumerator(), a.getDenominator().multiply(b));
    }

    private void propertiesDivide_BigInteger() {
        initialize("divide(BigInteger)");
        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroBigIntegers()))) {
            Rational quotient = p.a.divide(p.b);
            quotient.validate();
            assertEquals(p, quotient, divide_BigInteger_simplest(p.a, p.b));
            inverse(r -> r.divide(p.b), (Rational r) -> r.multiply(p.b), p.a);
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.nonzeroRationals(), P.nonzeroBigIntegers()))) {
            assertEquals(p, p.a.divide(p.b), of(p.b).divide(p.a).invert());
        }

        for (BigInteger i : take(LIMIT, P.nonzeroBigIntegers())) {
            assertEquals(i, ONE.divide(i), of(i).invert());
            assertEquals(i, of(i).divide(i), ONE);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(s -> s.divide(BigInteger.ONE), r);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            try {
                r.divide(BigInteger.ZERO);
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsDivide_BigInteger() {
        Map<String, Function<Pair<Rational, BigInteger>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> divide_BigInteger_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.divide(p.b));
        compareImplementations(
                "divide(BigInteger)",
                take(LIMIT, P.pairs(P.rationals(), P.nonzeroBigIntegers())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Rational divide_int_simplest(@NotNull Rational a, int b) {
        return of(a.getNumerator(), a.getDenominator().multiply(BigInteger.valueOf(b)));
    }

    private void propertiesDivide_int() {
        initialize("divide(int)");
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.nonzeroIntegers()))) {
            Rational quotient = p.a.divide(p.b);
            quotient.validate();
            assertEquals(p, quotient, divide_int_simplest(p.a, p.b));
            inverse(r -> r.divide(p.b), (Rational r) -> r.multiply(p.b), p.a);
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.nonzeroRationals(), P.nonzeroIntegers()))) {
            assertEquals(p, p.a.divide(p.b), of(p.b).divide(p.a).invert());
        }

        for (int i : take(LIMIT, P.nonzeroIntegers())) {
            assertEquals(i, ONE.divide(i), of(i).invert());
            assertEquals(i, of(i).divide(i), ONE);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(s -> s.divide(1), r);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            try {
                r.divide(0);
                fail(r);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsDivide_int() {
        Map<String, Function<Pair<Rational, Integer>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> divide_int_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.divide(p.b));
        compareImplementations(
                "divide(int)",
                take(LIMIT, P.pairs(P.rationals(), P.nonzeroIntegers())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Rational shiftLeft_simplest(@NotNull Rational r, int bits) {
        if (bits < 0) {
            return r.divide(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return r.multiply(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftLeft() {
        initialize("shiftLeft(int)");
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            homomorphic(
                    Rational::negate,
                    Function.identity(),
                    Rational::negate,
                    Rational::shiftLeft,
                    Rational::shiftLeft,
                    p
            );
            Rational shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftLeft_simplest(p.a, p.b));
            inverse(r -> r.shiftLeft(p.b), (Rational r) -> r.shiftRight(p.b), p.a);
            assertEquals(p, shifted, p.a.shiftRight(-p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(s -> s.shiftLeft(0), r);
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.naturalIntegersGeometric()))) {
            assertEquals(p, p.a.shiftLeft(p.b), p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftLeft() {
        Map<String, Function<Pair<Rational, Integer>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftLeft_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        compareImplementations(
                "shiftLeft(int)",
                take(LIMIT, P.pairs(P.rationals(), P.integersGeometric())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Rational shiftRight_simplest(@NotNull Rational r, int bits) {
        if (bits < 0) {
            return r.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return r.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private void propertiesShiftRight() {
        initialize("shiftRight(int)");
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integersGeometric()))) {
            homomorphic(
                    Rational::negate,
                    Function.identity(),
                    Rational::negate,
                    Rational::shiftRight,
                    Rational::shiftRight,
                    p
            );
            Rational shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertEquals(p, shifted, shiftRight_simplest(p.a, p.b));
            inverse(r -> r.shiftRight(p.b), (Rational r) -> r.shiftLeft(p.b), p.a);
            assertEquals(p, shifted, p.a.shiftLeft(-p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            fixedPoint(s -> s.shiftRight(0), r);
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.naturalIntegersGeometric()))) {
            assertEquals(p, p.a.shiftRight(p.b), p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private void compareImplementationsShiftRight() {
        Map<String, Function<Pair<Rational, Integer>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> shiftRight_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.shiftLeft(p.b));
        compareImplementations(
                "shiftRight(int)",
                take(LIMIT, P.pairs(P.rationals(), P.integersGeometric())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Rational sum_alt(@NotNull List<Rational> xs) {
        List<Rational> denominatorSorted = sort(
                (x, y) -> {
                    Ordering ordering = compare(x.getDenominator(), y.getDenominator());
                    if (ordering == EQ) {
                        ordering = compare(x.getNumerator().abs(), y.getNumerator().abs());
                    }
                    if (ordering == EQ) {
                        ordering = compare(x.getNumerator().signum(), y.getNumerator().signum());
                    }
                    return ordering.toInt();
                },
                xs
        );
        Iterable<List<Rational>> denominatorGrouped = group(
                p -> p.a.getDenominator().equals(p.b.getDenominator()),
                denominatorSorted
        );
        Rational sum = ZERO;
        for (List<Rational> group : denominatorGrouped) {
            BigInteger numeratorSum = sumBigInteger(toList(map(Rational::getNumerator, group)));
            sum = sum.add(of(numeratorSum, head(group).getDenominator()));
        }
        return sum;
    }

    private void propertiesSum() {
        initialize("sum(Iterable<Rational>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.rationals(),
                Rational::add,
                Rational::sum,
                Rational::validate,
                true,
                true
        );

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            assertEquals(rs, sum(rs), sum_alt(rs));
        }
    }

    private void compareImplementationsSum() {
        Map<String, Function<List<Rational>, Rational>> functions = new LinkedHashMap<>();
        functions.put("alt", RationalProperties::sum_alt);
        functions.put("standard", Rational::sum);
        compareImplementations(
                "sum(Iterable<Rational>)",
                take(LIMIT, P.lists(P.rationals())),
                functions,
                v -> P.reset()
        );
    }

    private static @NotNull Rational product_simplest(@NotNull Iterable<Rational> xs) {
        if (any(x -> x == ZERO, xs)) {
            return ZERO;
        }
        return foldl(Rational::multiply, ONE, xs);
    }

    private void propertiesProduct() {
        initialize("product(Iterable<Rational>)");
        propertiesFoldHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.rationals(),
                Rational::multiply,
                Rational::product,
                Rational::validate,
                true,
                true
        );

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            assertEquals(rs, product(rs), product_simplest(rs));
        }
    }

    private void compareImplementationsProduct() {
        Map<String, Function<List<Rational>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", RationalProperties::product_simplest);
        functions.put("standard", Rational::product);
        compareImplementations(
                "product(Iterable<Rational>)",
                take(LIMIT, P.lists(P.rationals())),
                functions,
                v -> P.reset()
        );
    }

    private static int sumSign_simplest(@NotNull List<Rational> xs) {
        return sum(xs).signum();
    }

    private static int sumSign_alt(@NotNull List<Rational> xs) {
        switch (xs.size()) {
            case 0:
                return 0;
            case 1:
                return xs.get(0).signum();
            default:
                return Integer.signum(sum(tail(xs)).compareTo(head(xs).negate()));
        }
    }

    public static int sumSign_alt2(@NotNull List<Rational> xs) {
        switch (xs.size()) {
            case 0:
                return 0;
            case 1:
                return xs.get(0).signum();
            default:
                int mostComplexBitLength = xs.get(0).bitLength();
                int mostComplexIndex = 0;
                for (int i = 1; i < xs.size(); i++) {
                    int bitLength = xs.get(i).bitLength();
                    if (bitLength > mostComplexBitLength) {
                        mostComplexBitLength = bitLength;
                        mostComplexIndex = i;
                    }
                }
                int j = mostComplexIndex;
                Rational sum = sum(
                        map(
                                xs::get,
                                filter(i -> i != j, ExhaustiveProvider.INSTANCE.rangeIncreasing(0, xs.size() - 1))
                        )
                );
                return Integer.signum(sum.compareTo(xs.get(mostComplexIndex).negate()));
        }
    }

    public static int sumSign_alt3(@NotNull List<Rational> xs) {
        switch (xs.size()) {
            case 0:
                return 0;
            case 1:
                return xs.get(0).signum();
            default:
                int lastPositiveIndex = xs.size();
                int lastNegativeIndex = xs.size();
                boolean positiveSeen = false;
                boolean negativeSeen = false;
                for (int i = xs.size() - 1; i >= 0 && !(positiveSeen && negativeSeen); i--) {
                    int sign = xs.get(i).signum();
                    if (!positiveSeen && sign == 1) {
                        lastPositiveIndex = i;
                        positiveSeen = true;
                    } else if (!negativeSeen && sign == -1) {
                        lastNegativeIndex = i;
                        negativeSeen = true;
                    }
                }
                Rational sum = Rational.ZERO;
                int signum = 0;
                for (int i = 0; i < xs.size(); i++) {
                    sum = sum.add(xs.get(i));
                    signum = sum.signum();
                    if (signum == 1 && i > lastNegativeIndex) return 1;
                    if (signum == -1 && i > lastPositiveIndex) return -1;
                }
                return signum;
        }
    }

    private void propertiesSumSign() {
        initialize("sumSign(List<Rational>)");
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            int sumSign = sumSign(rs);
            assertEquals(rs, sumSign_simplest(rs), sumSign);
            assertEquals(rs, sumSign_alt(rs), sumSign);
            assertEquals(rs, sumSign_alt2(rs), sumSign);
            assertEquals(rs, sumSign_alt3(rs), sumSign);
            assertTrue(rs, sumSign == 0 || sumSign == 1 || sumSign == -1);
            assertEquals(rs, sumSign(toList(map(Rational::negate, rs))), -sumSign);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r, sumSign(Collections.singletonList(r)), r.signum());
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assertEquals(p, sumSign(Pair.toList(p)), Integer.signum(p.a.compareTo(p.b.negate())));
        }

        for (List<Rational> rs : take(LIMIT, P.listsWithElement(null, P.rationals()))) {
            try {
                sumSign(rs);
                fail(rs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void compareImplementationsSumSign() {
        Map<String, Function<List<Rational>, Integer>> functions = new LinkedHashMap<>();
        functions.put("simplest", RationalProperties::sumSign_simplest);
        functions.put("alt", RationalProperties::sumSign_alt);
        functions.put("alt2", RationalProperties::sumSign_alt2);
        functions.put("alt3", RationalProperties::sumSign_alt3);
        functions.put("standard", Rational::sumSign);
        compareImplementations(
                "sumSign(List<Rational>)",
                take(LIMIT, P.lists(P.rationals())),
                functions,
                v -> P.reset()
        );
    }

    private void propertiesDelta() {
        initialize("delta(Iterable<Rational>)");
        propertiesDeltaHelper(
                LIMIT,
                P.getWheelsProvider(),
                QEP.rationals(),
                P.rationals(),
                Rational::negate,
                Rational::subtract,
                Rational::delta,
                Rational::validate
        );
    }

    private void propertiesHarmonicNumber() {
        initialize("harmonicNumber(int)");
        for (int i : take(MEDIUM_LIMIT, P.positiveIntegersGeometric())) {
            Rational h = harmonicNumber(i);
            h.validate();
            assertTrue(i, ge(h, ONE));
        }

        for (int i : take(MEDIUM_LIMIT, P.rangeUpGeometric(2))) {
            Rational h = harmonicNumber(i);
            assertTrue(i, gt(h, harmonicNumber(i - 1)));
            assertFalse(i, h.isInteger());
        }

        for (int i : take(MEDIUM_LIMIT, filterInfinite(j -> j != 6, P.rangeUpGeometric(3)))) {
            assertFalse(i, harmonicNumber(i).hasTerminatingBaseExpansion(BigInteger.TEN));
        }

        for (int i : take(LIMIT, P.rangeDown(0))) {
            try {
                harmonicNumber(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull Rational pow_simplest(@NotNull Rational a, int p) {
        Rational result = product(replicate(Math.abs(p), a));
        return p < 0 ? result.invert() : result;
    }

    private void propertiesPow() {
        initialize("pow(int)");
        Iterable<Pair<Rational, Integer>> ps = filterInfinite(
                p -> p.b >= 0 || p.a != ZERO,
                P.pairs(P.rationals(), P.integersGeometric())
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            Rational r = p.a.pow(p.b);
            r.validate();
            assertEquals(p, r, pow_simplest(p.a, p.b));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.nonzeroRationals(), P.integersGeometric()))) {
            homomorphic(Function.identity(), i -> -i, Rational::invert, Rational::pow, Rational::pow, p);
            homomorphic(Rational::invert, i -> -i, Function.identity(), Rational::pow, Rational::pow, p);
        }

        for (int i : take(LIMIT, P.positiveIntegersGeometric())) {
            assertTrue(i, ZERO.pow(i) == ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r, r.pow(0) == ONE);
            fixedPoint(s -> s.pow(1), r);
            assertEquals(r, r.pow(2), r.multiply(r));
        }

        for (Rational r : take(LIMIT, P.nonzeroRationals())) {
            assertEquals(r, r.pow(-1), r.invert());
        }

        Iterable<Triple<Rational, Integer, Integer>> ts = filterInfinite(
                t -> t.a != ZERO || (t.b >= 0 && t.c >= 0),
                P.triples(P.rationals(), P.integersGeometric(), P.integersGeometric())
        );
        for (Triple<Rational, Integer, Integer> t : take(LIMIT, ts)) {
            Rational expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            Rational expression2 = t.a.pow(t.b + t.c);
            assertEquals(t, expression1, expression2);
            Rational expression3 = t.a.pow(t.b).pow(t.c);
            Rational expression4 = t.a.pow(t.b * t.c);
            assertEquals(t, expression3, expression4);
        }

        ts = filterInfinite(
                t -> t.a != ZERO || (t.c == 0 && t.b >= 0),
                P.triples(P.rationals(), P.integersGeometric(), P.integersGeometric())
        );
        for (Triple<Rational, Integer, Integer> t : take(LIMIT, ts)) {
            Rational expression1 = t.a.pow(t.b).divide(t.a.pow(t.c));
            Rational expression2 = t.a.pow(t.b - t.c);
            assertEquals(t, expression1, expression2);
        }

        Iterable<Triple<Rational, Rational, Integer>> ts2 = filter(
                t -> (t.a != ZERO && t.b != ZERO) || t.c >= 0,
                P.triples(P.rationals(), P.rationals(), P.positiveIntegersGeometric())
        );
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, ts2)) {
            Rational expression1 = t.a.multiply(t.b).pow(t.c);
            Rational expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        ts2 = filterInfinite(
                t -> t.a != ZERO || t.c >= 0,
                P.triples(P.rationals(), P.nonzeroRationals(), P.positiveIntegersGeometric())
        );
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, ts2)) {
            Rational expression1 = t.a.divide(t.b).pow(t.c);
            Rational expression2 = t.a.pow(t.c).divide(t.b.pow(t.c));
            assertEquals(t, expression1, expression2);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                ZERO.pow(i);
                fail(i);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void compareImplementationsPow() {
        Map<String, Function<Pair<Rational, Integer>, Rational>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> pow_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.pow(p.b));
        Iterable<Pair<Rational, Integer>> ps = filterInfinite(
                p -> p.b >= 0 || p.a != ZERO,
                P.pairs(P.rationals(), P.integersGeometric())
        );
        compareImplementations("pow(int", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesFractionalPart() {
        initialize("fractionalPart()");
        for (Rational r : take(LIMIT, P.rationals())) {
            Rational fractionalPart = r.fractionalPart();
            fractionalPart.validate();
            assertTrue(r, ge(fractionalPart, ZERO));
            assertTrue(r, lt(fractionalPart, ONE));
            assertEquals(r, of(r.floor()).add(fractionalPart), r);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i, of(i).fractionalPart(), ZERO);
        }
    }

    private void propertiesRoundToDenominator() {
        initialize("roundToDenominator(BigInteger, RoundingMode)");
        Iterable<Triple<Rational, BigInteger, RoundingMode>> ts = filter(
                p -> p.c != RoundingMode.UNNECESSARY || p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO),
                P.triples(P.rationals(), P.positiveBigIntegers(), P.roundingModes())
        );
        for (Triple<Rational, BigInteger, RoundingMode> t : take(LIMIT, ts)) {
            Rational rounded = t.a.roundToDenominator(t.b, t.c);
            rounded.validate();
            assertEquals(t, t.b.mod(rounded.getDenominator()), BigInteger.ZERO);
            assertTrue(t, rounded == ZERO || rounded.signum() == t.a.signum());
            assertTrue(t, lt(t.a.subtract(rounded).abs(), of(BigInteger.ONE, t.b)));
        }

        Iterable<Pair<Rational, RoundingMode>> ps = filterInfinite(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isInteger(),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            Rational rounded = p.a.roundToDenominator(BigInteger.ONE, p.b);
            assertEquals(p, rounded.getNumerator(), p.a.bigIntegerValue(p.b));
            assertEquals(p, rounded.getDenominator(), BigInteger.ONE);
        }

        Iterable<Pair<Rational, BigInteger>> ps2 = filterInfinite(
                p -> p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO),
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assertTrue(p, p.a.roundToDenominator(p.b, RoundingMode.UNNECESSARY).equals(p.a));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.positiveBigIntegers()))) {
            assertTrue(p, le(p.a.roundToDenominator(p.b, RoundingMode.FLOOR), p.a));
            assertTrue(p, ge(p.a.roundToDenominator(p.b, RoundingMode.CEILING), p.a));
            assertTrue(p, le(p.a.roundToDenominator(p.b, RoundingMode.DOWN).abs(), p.a.abs()));
            assertTrue(p, ge(p.a.roundToDenominator(p.b, RoundingMode.UP).abs(), p.a.abs()));
            Rational resolution = of(p.b).shiftLeft(1).invert();
            assertTrue(p, le(p.a.subtract(p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN)).abs(), resolution));
            assertTrue(p, le(p.a.subtract(p.a.roundToDenominator(p.b, RoundingMode.HALF_UP)).abs(), resolution));
            assertTrue(p, le(p.a.subtract(p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN)).abs(), resolution));
        }

        ps2 = filterInfinite(
                p -> lt(p.a.abs().multiply(p.b).fractionalPart(), ONE_HALF),
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );

        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            Rational down = p.a.roundToDenominator(p.b, RoundingMode.DOWN);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN), down);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_UP), down);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN), down);
        }

        ps2 = filterInfinite(
                p -> gt(p.a.abs().multiply(p.b).fractionalPart(), ONE_HALF),
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            Rational up = p.a.roundToDenominator(p.b, RoundingMode.UP);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN), up);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_UP), up);
            assertEquals(p, p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN), up);
        }

        for (Rational r : take(LIMIT, filterInfinite(s -> !s.getDenominator().testBit(0), P.rationals()))) {
            BigInteger denominator = r.getDenominator().shiftRight(1);
            assertEquals(r, r.abs().multiply(denominator).fractionalPart(), ONE_HALF);
            Rational hd = r.roundToDenominator(denominator, RoundingMode.HALF_DOWN);
            assertEquals(r, hd, r.roundToDenominator(denominator, RoundingMode.DOWN));
            Rational hu = r.roundToDenominator(denominator, RoundingMode.HALF_UP);
            assertEquals(r, hu, r.roundToDenominator(denominator, RoundingMode.UP));
            Rational he = r.roundToDenominator(denominator, RoundingMode.HALF_EVEN);
            assertFalse(r, he.multiply(denominator).getNumerator().testBit(0));
        }

        Iterable<Triple<Rational, BigInteger, RoundingMode>> tsFail = P.triples(
                P.rationals(),
                P.withElement(BigInteger.ZERO, P.negativeBigIntegers()),
                P.roundingModes()
        );
        for (Triple<Rational, BigInteger, RoundingMode> t : take(LIMIT, tsFail)) {
            try {
                t.a.roundToDenominator(t.b, t.c);
                fail(t);
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Rational, BigInteger>> psFail = filterInfinite(
                p -> !p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO),
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, psFail)) {
            try {
                p.a.roundToDenominator(p.b, RoundingMode.UNNECESSARY);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesContinuedFraction() {
        initialize("continuedFraction()");
        for (Rational r : take(LIMIT, P.rationals())) {
            List<BigInteger> continuedFraction = toList(r.continuedFraction());
            assertFalse(r, continuedFraction.isEmpty());
            assertTrue(r, all(i -> i != null, continuedFraction));
            assertTrue(r, all(i -> i.signum() == 1, tail(continuedFraction)));
            inverse(Rational::continuedFraction, xs -> fromContinuedFraction(toList(xs)), r);
        }

        for (Rational r : take(LIMIT, filter(s -> !s.isInteger(), P.rationals()))) {
            assertTrue(r, gt(last(r.continuedFraction()), BigInteger.ONE));
        }
    }

    private void propertiesFromContinuedFraction() {
        initialize("fromContinuedFraction(List<BigInteger>)");
        Iterable<List<BigInteger>> iss = map(
                p -> toList(cons(p.a, p.b)),
                P.pairs(P.bigIntegers(), P.lists(P.positiveBigIntegers()))
        );
        for (List<BigInteger> is : take(LIMIT, iss)) {
            fromContinuedFraction(is).validate();
        }

        for (List<BigInteger> is : take(LIMIT, filter(js -> !last(js).equals(BigInteger.ONE), iss))) {
            inverse(Rational::fromContinuedFraction, r -> toList(r.continuedFraction()), is);
        }

        Iterable<List<BigInteger>> issFail = map(
                p -> toList(cons(p.a, p.b)),
                (Iterable<Pair<BigInteger, List<BigInteger>>>) P.pairs(
                        P.bigIntegers(),
                        P.listsWithSublists(map(Collections::singletonList, P.negativeBigIntegers()), P.bigIntegers())
                )
        );
        for (List<BigInteger> is : take(LIMIT, issFail)) {
            try {
                fromContinuedFraction(is);
                fail(is);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesConvergents() {
        initialize("convergents()");
        for (Rational r : take(LIMIT, P.rationals())) {
            List<Rational> convergents = toList(r.convergents());
            convergents.forEach(Rational::validate);
            assertFalse(r, convergents.isEmpty());
            assertTrue(r, all(s -> s != null, convergents));
            assertEquals(r, head(convergents), of(r.floor()));
            assertEquals(r, last(convergents), r);
            assertTrue(r, zigzagging(convergents));
            if (convergents.size() > 1) {
                assertTrue(r, lt(convergents.get(0), convergents.get(1)));
            }
        }
    }

    private static @NotNull <T> Pair<List<T>, List<T>> minimize(@NotNull List<T> a, @NotNull List<T> b) {
        List<T> oldA = new ArrayList<>();
        List<T> oldB = new ArrayList<>();
        while (!a.equals(oldA) || !b.equals(oldB)) {
            int longestCommonSuffixLength = 0;
            for (int i = 0; i < min(a.size(), b.size()); i++) {
                if (!a.get(a.size() - i - 1).equals(b.get(b.size() - i - 1))) break;
                longestCommonSuffixLength++;
            }
            oldA = a;
            oldB = b;
            a = toList(take(a.size() - longestCommonSuffixLength, a));
            b = unrepeat(rotateRight(longestCommonSuffixLength, b));
        }
        return new Pair<>(a, b);
    }

    private void propertiesPositionalNotation() {
        initialize("positionalNotation(BigInteger)");
        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(
                P.withElement(ZERO, P.withScale(4).positiveRationals()),
                P.withScale(8).rangeUp(IntegerUtils.TWO)
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> pn = p.a.positionalNotation(p.b);
            for (List<BigInteger> is : Triple.toList(pn)) {
                assertTrue(p, all(i -> i != null && i.signum() != -1 && lt(i, p.b), is));
            }
            assertTrue(p, pn.a.isEmpty() || !head(pn.a).equals(BigInteger.ZERO));
            assertFalse(p, pn.c.isEmpty());
            assertNotEquals(p, pn.c, Collections.singletonList(p.b.subtract(BigInteger.ONE)));
            Pair<List<BigInteger>, List<BigInteger>> minimized = minimize(pn.b, pn.c);
            assertEquals(p, minimized.a, pn.b);
            assertEquals(p, minimized.b, pn.c);
            assertEquals(p, fromPositionalNotation(p.b, pn.a, pn.b, pn.c), p.a);
            assertEquals(
                    p,
                    pn.c.equals(Collections.singletonList(BigInteger.ZERO)),
                    p.a.hasTerminatingBaseExpansion(p.b)
            );
            inverse(
                    r -> r.positionalNotation(p.b),
                    (Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> t) ->
                            fromPositionalNotation(p.b, t.a, t.b, t.c),
                    p.a
            );
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.negativeRationals(), P.rangeUp(IntegerUtils.TWO)))) {
            try {
                p.a.positionalNotation(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.rangeDown(BigInteger.ONE)))) {
            try {
                p.a.positionalNotation(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesFromPositionalNotation() {
        initialize("fromPositionalNotation(BigInteger, List<BigInteger>, List<BigInteger>, List<BigInteger>)");
        Iterable<Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>>> ps =
                P.dependentPairsInfinite(
                        P.withScale(8).rangeUp(IntegerUtils.TWO),
                        b -> {
                            Iterable<BigInteger> range = P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE));
                            return P.triples(
                                    P.withScale(4).lists(range),
                                    P.withScale(4).lists(range),
                                    P.withScale(4).listsAtLeast(1, range)
                            );
                        }
                );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, ps)) {
            Rational r = fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
            r.validate();
            assertNotEquals(p, r.signum(), -1);
        }

        Iterable<Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>>> ps2 = filterInfinite(
                p -> {
                    if (!p.b.a.isEmpty() && head(p.b.a).equals(BigInteger.ZERO)) return false;
                    Pair<List<BigInteger>, List<BigInteger>> minimized = minimize(p.b.b, p.b.c);
                    return minimized.a.equals(p.b.b) && minimized.b.equals(p.b.c) &&
                            !p.b.c.equals(Collections.singletonList(p.a.subtract(BigInteger.ONE)));
                },
                ps
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, ps2)) {
            inverse(t -> fromPositionalNotation(p.a, t.a, t.b, t.c), (Rational s) -> s.positionalNotation(p.a), p.b);
        }

        Iterable<Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>>> psFail = P.pairs(
                P.rangeDown(BigInteger.ONE),
                filterInfinite(t -> !t.c.isEmpty(), P.triples(P.lists(P.bigIntegers())))
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, psFail)) {
            try {
                fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = P.dependentPairs(
                P.withScale(8).rangeUp(IntegerUtils.TWO),
                b -> {
                    Iterable<BigInteger> range = P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE));
                    //noinspection RedundantCast
                    return P.triples(
                            P.listsWithSublists(
                                    map(
                                            Collections::singletonList,
                                            P.choose(
                                                    P.negativeBigIntegers(),
                                                    P.withScale(IntegerUtils.ceilingLog2(b) + 2).rangeUp(b)
                                            )
                                    ),
                                    range
                            ),
                            P.withScale(4).lists(range),
                            P.withScale(4).listsAtLeast(1, range)
                    );
                }
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, psFail)) {
            try {
                fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = P.dependentPairs(
                P.withScale(8).rangeUp(IntegerUtils.TWO),
                b -> {
                    Iterable<BigInteger> range = P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE));
                    //noinspection RedundantCast
                    return P.triples(
                            P.withScale(4).lists(range),
                            P.listsWithSublists(
                                    map(
                                            Collections::singletonList,
                                            P.choose(
                                                    P.negativeBigIntegers(),
                                                    P.withScale(IntegerUtils.ceilingLog2(b) + 2).rangeUp(b)
                                            )
                                    ),
                                    range
                            ),
                            P.withScale(4).listsAtLeast(1, range)
                    );
                }
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, psFail)) {
            try {
                fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = P.dependentPairs(
                P.withScale(8).rangeUp(IntegerUtils.TWO),
                b -> {
                    Iterable<BigInteger> range = P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE));
                    //noinspection RedundantCast
                    return P.triples(
                            P.withScale(4).lists(range),
                            P.withScale(4).lists(range),
                            filterInfinite(
                                    xs -> !xs.isEmpty(),
                                    (Iterable<List<BigInteger>>) P.listsWithSublists(
                                            map(
                                                    Collections::singletonList,
                                                    P.choose(
                                                            P.negativeBigIntegers(),
                                                            P.withScale(IntegerUtils.ceilingLog2(b) + 2).rangeUp(b)
                                                    )
                                            ),
                                            range
                                    )
                            )
                    );
                }
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, psFail)) {
            try {
                fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<Triple<BigInteger, List<BigInteger>, List<BigInteger>>> tsFail = P.triples(
                P.rangeDown(BigInteger.ONE),
                P.lists(P.bigIntegers()),
                P.lists(P.bigIntegers())
        );
        for (Triple<BigInteger, List<BigInteger>, List<BigInteger>> t : take(LIMIT, tsFail)) {
            try {
                fromPositionalNotation(t.a, t.b, t.c, Collections.emptyList());
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static @NotNull Pair<List<BigInteger>, Iterable<BigInteger>> digits_alt(
            @NotNull Rational r,
            @NotNull BigInteger base
    ) {
        Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> positionalNotation = r.positionalNotation(base);
        Iterable<BigInteger> afterDecimal;
        if (positionalNotation.c.equals(Collections.singletonList(BigInteger.ZERO))) {
            afterDecimal = positionalNotation.b;
        } else {
            afterDecimal = concat(positionalNotation.b, cycle(positionalNotation.c));
        }
        return new Pair<>(positionalNotation.a, afterDecimal);
    }

    private void propertiesDigits() {
        initialize("digits(BigInteger)");
        //noinspection Convert2MethodRef
        Iterable<Pair<Rational, BigInteger>> ps = P.pairsSquareRootOrder(
                P.withElement(ZERO, P.positiveRationals()),
                P.rangeUp(IntegerUtils.TWO)
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            Pair<List<BigInteger>, Iterable<BigInteger>> digits = p.a.digits(p.b);
            assertTrue(p, digits.a.isEmpty() || !head(digits.a).equals(BigInteger.ZERO));
            assertTrue(p, all(x -> x.signum() != -1 && lt(x, p.b), digits.a));
            assertEquals(p, IntegerUtils.fromBigEndianDigits(p.b, digits.a), p.a.floor());
        }

        ps = P.pairsSquareRootOrder(
                P.withElement(ZERO, P.withScale(8).positiveRationals()),
                P.rangeUp(IntegerUtils.TWO)
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            Pair<List<BigInteger>, Iterable<BigInteger>> digits = p.a.digits(p.b);
            Pair<List<BigInteger>, Iterable<BigInteger>> alt = digits_alt(p.a, p.b);
            assertEquals(p, digits.a, alt.a);
            aeqit(p.toString(), take(TINY_LIMIT, digits.b), take(TINY_LIMIT, alt.b));
        }

        ps = filter(
                q -> q.a.hasTerminatingBaseExpansion(q.b),
                P.pairsSquareRootOrder(
                        P.withElement(ZERO, P.positiveRationals()),
                        P.withScale(4).rangeUp(IntegerUtils.TWO)
                )
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            toList(p.a.digits(p.b).b);
        }

        Iterable<Pair<Rational, BigInteger>> psFail = P.pairs(P.negativeRationals(), P.rangeUp(IntegerUtils.TWO));
        for (Pair<Rational, BigInteger> p : take(LIMIT, psFail)) {
            try {
                p.a.digits(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.rangeDown(BigInteger.ONE)))) {
            try {
                p.a.digits(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void compareImplementationsDigits() {
        Map<String, Function<Pair<Rational, BigInteger>, List<BigInteger>>> functions = new LinkedHashMap<>();
        functions.put("alt", p -> toList(take(TINY_LIMIT, digits_alt(p.a, p.b).b)));
        functions.put("standard", p -> toList(take(TINY_LIMIT, p.a.digits(p.b).b)));
        //noinspection Convert2MethodRef
        Iterable<Pair<Rational, BigInteger>> ps = P.pairsSquareRootOrder(
                P.withElement(ZERO, P.withScale(4).positiveRationals()),
                P.withScale(4).rangeUp(IntegerUtils.TWO)
        );
        compareImplementations("digits(BigInteger)", take(LIMIT, ps), functions, v -> P.reset());
    }

    private void propertiesCommonLeadingDigits() {
        initialize("commonLeadingDigits(BigInteger, Rational, Rational)");
        //noinspection Convert2MethodRef
        Iterable<Triple<BigInteger, Rational, Rational>> ts = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairsSquareRootOrder(
                        filterInfinite(p -> p.a != p.b, P.pairs(P.withElement(ZERO, P.positiveRationals()))),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                )
        );
        for (Triple<BigInteger, Rational, Rational> t : take(LIMIT, ts)) {
            Pair<List<BigInteger>, Integer> cld = commonLeadingDigits(t.a, t.b, t.c);
            assertNotEquals(t, cld.a, null);
            assertNotEquals(t, cld.b, null);
            assertTrue(t, cld.a.isEmpty() || !head(cld.a).equals(BigInteger.ZERO));
            BigInteger approx = IntegerUtils.fromBigEndianDigits(t.a, cld.a);
            Rational pow = of(t.a).pow(cld.b);
            assertTrue(t, le(pow.multiply(approx), t.b));
            assertTrue(t, le(pow.multiply(approx), t.c));
            assertTrue(t, gt(pow.multiply(approx.add(BigInteger.ONE)), t.b));
            assertTrue(t, gt(pow.multiply(approx.add(BigInteger.ONE)), t.c));
        }

        Iterable<Triple<BigInteger, Rational, Rational>> tsFail = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairsSquareRootOrder(
                        filterInfinite(p -> p.a != p.b, P.pairs(P.withElement(ZERO, P.positiveRationals()))),
                        P.rangeDown(BigInteger.valueOf(-2))
                )
        );
        for (Triple<BigInteger, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                commonLeadingDigits(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        //noinspection Convert2MethodRef
        tsFail = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairsSquareRootOrder(
                        P.pairs(P.withElement(ZERO, P.positiveRationals()), P.negativeRationals()),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                )
        );
        for (Triple<BigInteger, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                commonLeadingDigits(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        //noinspection Convert2MethodRef
        tsFail = map(
                p -> new Triple<>(p.b, p.a.a, p.a.b),
                P.pairsSquareRootOrder(
                        P.pairs(P.negativeRationals(), P.withElement(ZERO, P.positiveRationals())),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                )
        );
        for (Triple<BigInteger, Rational, Rational> t : take(LIMIT, tsFail)) {
            try {
                commonLeadingDigits(t.a, t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }

        //noinspection Convert2MethodRef
        Iterable<Pair<Rational, BigInteger>> psFail = P.pairsSquareRootOrder(
                P.withElement(ZERO, P.positiveRationals()),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, psFail)) {
            try {
                commonLeadingDigits(p.b, p.a, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesToStringBase_BigInteger() {
        initialize("toStringBase(BigInteger)");
        //noinspection Convert2MethodRef
        Iterable<Pair<Rational, BigInteger>> ps = filterInfinite(
                q -> q.a.hasTerminatingBaseExpansion(q.b),
                P.pairsSquareRootOrder(
                        P.rationals(),
                        map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2))
                )
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            inverse(r -> r.toStringBase(p.b), (String t) -> fromStringBase(t, p.b), p.a);
        }

        String smallBaseChars = charsToString(
                concat(
                        Arrays.asList(fromString("-."), ExhaustiveProvider.INSTANCE.rangeIncreasing('0', '9'),
                        ExhaustiveProvider.INSTANCE.rangeIncreasing('A', 'Z'))
                )
        );
        ps = filterInfinite(
                q -> q.a.hasTerminatingBaseExpansion(q.b),
                P.pairsSquareRootOrder(P.rationals(), P.range(IntegerUtils.TWO, ASCII_ALPHANUMERIC_COUNT))
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            String s = p.a.toStringBase(p.b);
            assertTrue(p, all(c -> elem(c, smallBaseChars), s));
        }

        String largeBaseChars = charsToString(
                concat(fromString("-.()"), ExhaustiveProvider.INSTANCE.rangeIncreasing('0', '9'))
        );
        //noinspection Convert2MethodRef
        ps = P.pairsSquareRootOrder(
                P.rationals(),
                map(
                        i -> BigInteger.valueOf(i),
                        P.withScale(64).rangeUpGeometric(ASCII_ALPHANUMERIC_COUNT.intValueExact() + 1)
                )
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, filter(q -> q.a.hasTerminatingBaseExpansion(q.b), ps))) {
            String s = p.a.toStringBase(p.b);
            assertTrue(p, all(c -> elem(c, largeBaseChars), s));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.rangeDown(BigInteger.ONE)))) {
            try {
                p.a.toStringBase(p.b);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        //noinspection Convert2MethodRef
        Iterable<Pair<Rational, BigInteger>> psFail = filterInfinite(
                q -> !q.a.hasTerminatingBaseExpansion(q.b),
                P.pairs(P.rationals(), map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)))
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, psFail)) {
            try {
                p.a.toStringBase(p.b);
                fail(p);
            } catch (ArithmeticException ignored) {}
        }
    }

    private void propertiesToStringBase_BigInteger_int() {
        initialize("toStringBase(BigInteger, int)");
        //noinspection Convert2MethodRef
        Iterable<Triple<Rational, BigInteger, Integer>> ts = P.triples(
                P.rationals(),
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                P.integersGeometric()
        );
        for (Triple<Rational, BigInteger, Integer> t : take(LIMIT, ts)) {
            String s = t.a.toStringBase(t.b, t.c);
            boolean ellipsis = s.endsWith("...");
            if (ellipsis) s = take(s.length() - 3, s);
            Rational error = fromStringBase(s, t.b).subtract(t.a).abs();
            assertTrue(t, lt(error, of(t.b).pow(-t.c)));
            if (ellipsis) {
                assertTrue(t, error != ZERO);
            }
        }

        String smallBaseChars = charsToString(
                concat(
                        Arrays.asList(
                                fromString("-."),
                                ExhaustiveProvider.INSTANCE.rangeIncreasing('0', '9'),
                                ExhaustiveProvider.INSTANCE.rangeIncreasing('A', 'Z')
                        )
                )
        );
        ts = P.triples(P.rationals(), P.range(IntegerUtils.TWO, ASCII_ALPHANUMERIC_COUNT), P.integersGeometric());
        for (Triple<Rational, BigInteger, Integer> t : take(LIMIT, ts)) {
            String s = t.a.toStringBase(t.b, t.c);
            assertTrue(t, all(c -> elem(c, smallBaseChars), s));
        }

        String largeBaseChars = charsToString(
                concat(fromString("-.()"), ExhaustiveProvider.INSTANCE.rangeIncreasing('0', '9'))
        );
        //noinspection Convert2MethodRef
        ts = P.triples(
                P.rationals(),
                map(
                        i -> BigInteger.valueOf(i),
                        P.withScale(64).rangeUpGeometric(ASCII_ALPHANUMERIC_COUNT.intValueExact() + 1)
                ),
                P.withScale(20).integersGeometric()
        );
        for (Triple<Rational, BigInteger, Integer> t : take(LIMIT, ts)) {
            String s = t.a.toStringBase(t.b, t.c);
            assertTrue(t, all(c -> elem(c, largeBaseChars), s));
        }

        Iterable<Triple<Rational, BigInteger, Integer>> tsFail = P.triples(
                P.rationals(),
                P.rangeDown(BigInteger.ONE),
                P.integers()
        );
        for (Triple<Rational, BigInteger, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.toStringBase(t.b, t.c);
                fail(t);
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private void propertiesFromStringBase() {
        initialize("fromStringBase(BigInteger, String)");
        Map<Integer, String> baseChars = new HashMap<>();
        baseChars.put(0, ".-()0123456789");
        String chars = ".-0";
        for (int i = 1; i < ASCII_ALPHANUMERIC_COUNT.intValueExact(); i++) {
            chars += IntegerUtils.toDigit(i);
            baseChars.put(i + 1, chars);
        }
        //noinspection Convert2MethodRef
        Iterable<Pair<BigInteger, String>> ps = P.dependentPairsInfiniteSquareRootOrder(
                map(i -> BigInteger.valueOf(i), P.rangeUpGeometric(2)),
                b -> filterInfinite(
                        s -> {
                            try {
                                fromStringBase(s, b);
                                return true;
                            } catch (IllegalArgumentException e) {
                                return false;
                            }
                        },
                        P.strings(baseChars.get(Ordering.le(b, ASCII_ALPHANUMERIC_COUNT) ? b.intValueExact() : 0))
                )
        );
        for (Pair<BigInteger, String> p : take(LIMIT, ps)) {
            Rational r = fromStringBase(p.b, p.a);
            r.validate();
        }

        for (BigInteger i : take(LIMIT, P.rangeUp(IntegerUtils.TWO))) {
            assertTrue(i, fromStringBase("", i) == ZERO);
        }

        for (Pair<BigInteger, String> p : take(LIMIT, P.pairs(P.rangeDown(BigInteger.ONE), P.strings()))) {
            try {
                fromStringBase(p.b, p.a);
                fail(p);
            } catch (IllegalArgumentException ignored) {}
        }

        //improper String left untested
    }

    private void propertiesCancelDenominators() {
        initialize("cancelDenominators(List<Rational>)");
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            List<BigInteger> canceled = cancelDenominators(rs);
            BigInteger gcd = MathUtils.gcd(canceled);
            assertTrue(rs, gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE));
            idempotent(ss -> toList(map(Rational::of, cancelDenominators(ss))), rs);
            assertTrue(rs, equal(map(Rational::signum, rs), map(BigInteger::signum, canceled)));
            assertTrue(
                    rs,
                    same(
                            zipWith(
                                    Rational::divide,
                                    filter(r -> r != ZERO, rs),
                                    filter(i -> !i.equals(BigInteger.ZERO), canceled)
                            )
                    )
            );
        }

        for (Pair<List<Rational>, Rational> p : take(LIMIT, P.pairs(P.lists(P.rationals()), P.positiveRationals()))) {
            assertEquals(p, cancelDenominators(p.a), cancelDenominators(toList(map(r -> r.multiply(p.b), p.a))));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            BigInteger canceled = head(cancelDenominators(Collections.singletonList(r)));
            assertTrue(r, le(canceled.abs(), BigInteger.ONE));
        }

        for (List<Rational> rs : take(LIMIT, P.listsWithElement(null, P.rationals()))) {
            try {
                cancelDenominators(rs);
                fail(rs);
            } catch (NullPointerException ignored) {}
        }
    }

    private void propertiesEquals() {
        initialize("equals(Object)");
        propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::rationals);
    }

    private void propertiesHashCode() {
        initialize("hashCode()");
        propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::rationals);
    }

    private static int compareTo_simplest(@NotNull Rational x, @NotNull Rational y) {
        return x.getNumerator().multiply(y.getDenominator()).compareTo(y.getNumerator().multiply(x.getDenominator()));
    }

    private void propertiesCompareTo() {
        initialize("compareTo(Rational)");
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            int compare = p.a.compareTo(p.b);
            assertEquals(p, compare, compareTo_simplest(p.a, p.b));
            assertEquals(p, p.a.subtract(p.b).signum(), compare);
        }
        propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::rationals);
    }

    private void compareImplementationsCompareTo() {
        Map<String, Function<Pair<Rational, Rational>, Integer>> functions = new LinkedHashMap<>();
        functions.put("simplest", p -> compareTo_simplest(p.a, p.b));
        functions.put("standard", p -> p.a.compareTo(p.b));
        compareImplementations("compareTo(Rational)", take(LIMIT, P.pairs(P.rationals())), functions, v -> P.reset());
    }

    private void propertiesReadStrict() {
        initialize("readStrict(String)");
        propertiesReadHelper(
                LIMIT,
                P,
                RATIONAL_CHARS,
                P.rationals(),
                Rational::readStrict,
                Rational::validate,
                true,
                true
        );

        Pair<Iterable<String>, Iterable<String>> slashPartition = partition(
                s -> s.contains("/"),
                filterInfinite(s -> readStrict(s).isPresent(), P.strings(RATIONAL_CHARS))
        );
        for (String s : take(LIMIT, slashPartition.a)) {
            int slashIndex = s.indexOf('/');
            String left = s.substring(0, slashIndex);
            String right = s.substring(slashIndex + 1);
            assertTrue(s, Readers.readBigIntegerStrict(left).isPresent());
            assertTrue(s, Readers.readBigIntegerStrict(right).isPresent());
        }

        for (String s : take(LIMIT, slashPartition.b)) {
            assertTrue(s, Readers.readBigIntegerStrict(s).isPresent());
        }
    }

    private void propertiesToString() {
        initialize("toString()");
        propertiesToStringHelper(LIMIT, RATIONAL_CHARS, P.rationals(), Rational::readStrict);
    }
}
