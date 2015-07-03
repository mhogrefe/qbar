package mho.qbar.objects;

import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import mho.qbar.testing.QBarTesting;
import mho.wheels.math.MathUtils;
import mho.wheels.misc.FloatingPointUtils;
import mho.wheels.misc.Readers;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

import static mho.qbar.objects.Rational.*;
import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.wheels.testing.Testing.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RationalProperties {
    private static boolean USE_RANDOM;
    private static final @NotNull String RATIONAL_CHARS = "-/0123456789";
    private static final int SMALL_LIMIT = 1000;
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = QBarRandomProvider.example();
            LIMIT = 1000;
        } else {
            P = QBarExhaustiveProvider.INSTANCE;
            LIMIT = 10000;
        }
    }

    @Test
    public void testAllProperties() {
        System.out.println("Rational properties");
        propertiesConstants();
        for (boolean useRandom : Arrays.asList(false, true)) {
            System.out.println("\ttesting " + (useRandom ? "randomly" : "exhaustively"));
            USE_RANDOM = useRandom;
            propertiesGetNumerator();
            propertiesGetDenominator();
            propertiesOf_BigInteger_BigInteger();
            propertiesOf_long_long();
            propertiesOf_int_int();
            propertiesOf_BigInteger();
            propertiesOf_long();
            propertiesOf_int();
            propertiesOf_float();
            propertiesOf_double();
            propertiesOfExact_float();
            propertiesOfExact_double();
            propertiesOf_BigDecimal();
            propertiesIsInteger();
            propertiesBigIntegerValue_RoundingMode();
            propertiesBigIntegerValue();
            propertiesBigIntegerValueExact();
            propertiesByteValueExact();
            propertiesShortValueExact();
            propertiesIntValueExact();
            propertiesLongValueExact();
            propertiesHasTerminatingBaseExpansion();
            propertiesBigDecimalValue_int_RoundingMode();
            propertiesBigDecimalValue_int();
            propertiesBigDecimalValueExact();
            propertiesBinaryExponent();
            propertiesFloatValue_RoundingMode();
            propertiesFloatValue();
            propertiesFloatValueExact();
            propertiesDoubleValue_RoundingMode();
            propertiesDoubleValue();
            propertiesDoubleValueExact();
            propertiesAdd();
            propertiesNegate();
            propertiesAbs();
            propertiesSignum();
            compareImplementationsAdd();
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
            propertiesSum();
            compareImplementationsSum();
            propertiesProduct();
            compareImplementationsProduct();
            propertiesDelta();
            propertiesHarmonicNumber();
            propertiesPow();
            compareImplementationsPow();
            propertiesFloor();
            propertiesCeiling();
            propertiesFractionalPart();
            propertiesRoundToDenominator();
            propertiesShiftLeft();
            compareImplementationsShiftLeft();
            propertiesShiftRight();
            compareImplementationsShiftRight();
            propertiesContinuedFraction();
            propertiesFromContinuedFraction();
            propertiesConvergents();
            propertiesPositionalNotation();
            propertiesFromPositionalNotation();
            propertiesDigits();
            compareImplementationsDigits();
            propertiesToStringBase_BigInteger();
            propertiesToStringBase_BigInteger_int();
            propertiesFromStringBase();
            propertiesCancelDenominators();
            propertiesEquals();
            propertiesHashCode();
            propertiesCompareTo();
            compareImplementationsCompareTo();
            propertiesRead();
            propertiesFindIn();
            propertiesToString();
        }
        System.out.println("Done");
    }

    private static void propertiesConstants() {
        initialize();
        System.out.println("\ttesting constant properties...");

        List<Rational> sample = toList(take(SMALL_LIMIT, HARMONIC_NUMBERS));
        sample.forEach(Rational::validate);
        assertTrue(unique(sample));
        assertTrue(increasing(sample));
        assertTrue(all(r -> !r.isInteger(), tail(sample)));
        try {
            HARMONIC_NUMBERS.iterator().remove();
            fail();
        } catch (UnsupportedOperationException ignored) {}
    }

    private static void propertiesGetNumerator() {
        initialize();
        System.out.println("\t\ttesting getNumerator() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), of(r.getNumerator(), r.getDenominator()), r);
        }
    }

    private static void propertiesGetDenominator() {
        initialize();
        System.out.println("\t\ttesting getDenominator() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.getDenominator().signum(), 1);
        }
    }

    private static void propertiesOf_BigInteger_BigInteger() {
        initialize();
        System.out.println("\t\ttesting of(BigInteger, BigInteger) properties...");

        Iterable<Pair<BigInteger, BigInteger>> ps = filter(
                p -> !p.b.equals(BigInteger.ZERO),
                P.pairs(P.bigIntegers())
        );
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, ps)) {
            Rational r = of(p.a, p.b);
            r.validate();
            assertEquals(p.toString(), of(p.a).divide(p.b), r);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            try {
                of(i, BigInteger.ZERO);
                fail(i.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesOf_long_long() {
        initialize();
        System.out.println("\t\ttesting of(long, long) properties...");

        BigInteger minLong = BigInteger.valueOf(Long.MIN_VALUE);
        BigInteger maxLong = BigInteger.valueOf(Long.MAX_VALUE);
        Iterable<Pair<Long, Long>> ps = filter(p -> p.b != 0, P.pairs(P.longs()));
        for (Pair<Long, Long> p : take(LIMIT, ps)) {
            Rational r = of(p.a, p.b);
            r.validate();
            assertEquals(p.toString(), of(p.a).divide(BigInteger.valueOf(p.b)), r);
            assertTrue(p.toString(), ge(r.getNumerator(), minLong));
            assertTrue(p.toString(), le(r.getNumerator(), maxLong));
            assertTrue(p.toString(), ge(r.getDenominator(), minLong));
            assertTrue(p.toString(), le(r.getDenominator(), maxLong));
        }

        for (long l : take(LIMIT, P.longs())) {
            try {
                of(l, 0L);
                fail(Long.toString(l));
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesOf_int_int() {
        initialize();
        System.out.println("\t\ttesting of(int, int) properties...");

        BigInteger minInt = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
        Iterable<Pair<Integer, Integer>> ps = filter(p -> p.b != 0, P.pairs(P.integers()));
        for (Pair<Integer, Integer> p : take(LIMIT, ps)) {
            Rational r = of(p.a, p.b);
            r.validate();
            assertEquals(p.toString(), of(p.a).divide(p.b), r);
            assertTrue(p.toString(), ge(r.getNumerator(), minInt));
            assertTrue(p.toString(), le(r.getNumerator(), maxInt));
            assertTrue(p.toString(), ge(r.getDenominator(), minInt));
            assertTrue(p.toString(), le(r.getDenominator(), maxInt));
        }

        for (int i : take(LIMIT, P.integers())) {
            try {
                of(i, 0);
                fail(Integer.toString(i));
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesOf_BigInteger() {
        initialize();
        System.out.println("\t\ttesting of(BigInteger) properties...");

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Rational r = of(i);
            r.validate();
            assertEquals(i.toString(), r.getDenominator(), BigInteger.ONE);
        }
    }

    private static void propertiesOf_long() {
        initialize();
        System.out.println("\t\ttesting of(long) properties...");

        BigInteger minLong = BigInteger.valueOf(Long.MIN_VALUE);
        BigInteger maxLong = BigInteger.valueOf(Long.MAX_VALUE);
        for (long l : take(LIMIT, P.longs())) {
            Rational r = of(l);
            r.validate();
            assertEquals(Long.toString(l), r.getDenominator(), BigInteger.ONE);
            assertTrue(Long.toString(l), ge(r.getNumerator(), minLong));
            assertTrue(Long.toString(l), le(r.getNumerator(), maxLong));
        }
    }

    private static void propertiesOf_int() {
        initialize();
        System.out.println("\t\ttesting of(int) properties...");

        BigInteger minInt = BigInteger.valueOf(Integer.MIN_VALUE);
        BigInteger maxInt = BigInteger.valueOf(Integer.MAX_VALUE);
        for (int i : take(LIMIT, P.integers())) {
            Rational r = of(i);
            r.validate();
            assertEquals(Integer.toString(i), r.getDenominator(), BigInteger.ONE);
            assertTrue(Integer.toString(i), ge(r.getNumerator(), minInt));
            assertTrue(Integer.toString(i), le(r.getNumerator(), maxInt));
        }
    }

    private static void propertiesOf_float() {
        initialize();
        System.out.println("\t\ttesting of(float) properties...");

        Iterable<Float> fs = filter(Float::isFinite, P.floats());
        for (float f : take(LIMIT, fs)) {
            Rational r = of(f).get();
            r.validate();
            assertTrue(Float.toString(f), r.hasTerminatingBaseExpansion(BigInteger.valueOf(10)));
        }

        fs = filter(g -> Float.isFinite(g) && !FloatingPointUtils.isNegativeZero(g), P.floats());
        for (float f : take(LIMIT, fs)) {
            Rational r = of(f).get();
            aeqf(Float.toString(f), f, r.floatValue());
            aeq(Float.toString(f), new BigDecimal(Float.toString(f)), r.bigDecimalValueExact());
        }
    }

    private static void propertiesOf_double() {
        initialize();
        System.out.println("\t\ttesting of(double) properties...");

        Iterable<Double> ds = filter(Double::isFinite, P.doubles());
        for (double d : take(LIMIT, ds)) {
            Rational r = of(d).get();
            r.validate();
            assertTrue(Double.toString(d), r.hasTerminatingBaseExpansion(BigInteger.valueOf(10)));
        }

        ds = filter(e -> Double.isFinite(e) && !FloatingPointUtils.isNegativeZero(e), P.doubles());
        for (double d : take(LIMIT, ds)) {
            Rational r = of(d).get();
            aeqd(Double.toString(d), d, r.doubleValue());
            aeq(Double.toString(d), new BigDecimal(Double.toString(d)), r.bigDecimalValueExact());
        }
    }

    private static void propertiesOfExact_float() {
        initialize();
        System.out.println("\t\ttesting ofExact(float) properties...");

        for (float f : take(LIMIT, P.floats())) {
            Optional<Rational> or = ofExact(f);
            assertEquals(Float.toString(f), Float.isFinite(f), or.isPresent());
        }

        for (float f : take(LIMIT, filter(Float::isFinite, P.floats()))) {
            Rational r = ofExact(f).get();
            r.validate();
            assertTrue(Float.toString(f), MathUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(Float.toString(f), le(r.getDenominator(), BigInteger.ONE.shiftLeft(149)));
            assertTrue(
                    Float.toString(f),
                    le(r.getNumerator(),
                    BigInteger.ONE.shiftLeft(128).subtract(BigInteger.ONE.shiftLeft(104)))
            );
        }

        Iterable<Float> fs = filter(g -> Float.isFinite(g) && !FloatingPointUtils.isNegativeZero(g), P.floats());
        for (float f : take(LIMIT, fs)) {
            Rational r = ofExact(f).get();
            aeqf(Float.toString(f), f, r.floatValue());
        }
    }

    private static void propertiesOfExact_double() {
        initialize();
        System.out.println("\t\ttesting ofExact(double) properties...");

        for (double d : take(LIMIT, P.doubles())) {
            Optional<Rational> or = ofExact(d);
            assertEquals(Double.toString(d), Double.isFinite(d), or.isPresent());
        }

        for (double d : take(LIMIT, filter(Double::isFinite, P.doubles()))) {
            Rational r = ofExact(d).get();
            r.validate();
            assertTrue(Double.toString(d), MathUtils.isPowerOfTwo(r.getDenominator()));
            assertTrue(Double.toString(d), le(r.getDenominator(), BigInteger.ONE.shiftLeft(1074)));
            assertTrue(
                    Double.toString(d),
                    le(r.getNumerator(),
                    BigInteger.ONE.shiftLeft(1024).subtract(BigInteger.ONE.shiftLeft(971)))
            );
        }

        Iterable<Double> ds = filter(e -> Double.isFinite(e) && !FloatingPointUtils.isNegativeZero(e), P.doubles());
        for (double d : take(LIMIT, ds)) {
            Rational r = ofExact(d).get();
            aeqd(Double.toString(d), d, r.doubleValue());
        }
    }

    private static void propertiesOf_BigDecimal() {
        initialize();
        System.out.println("\t\ttesting of(BigDecimal) properties...");

        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Rational r = of(bd);
            r.validate();
            aeq(bd.toString(), bd, r.bigDecimalValueExact());
            assertTrue(bd.toString(), r.hasTerminatingBaseExpansion(BigInteger.valueOf(10)));
        }
    }

    private static void propertiesIsInteger() {
        initialize();
        System.out.println("\t\ttesting isInteger() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.isInteger(), of(r.floor()).equals(r));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertTrue(i.toString(), of(i).isInteger());
        }
    }

    private static void propertiesBigIntegerValue_RoundingMode() {
        initialize();
        System.out.println("\t\ttesting bigIntegerValue(RoundingMode) properties...");

        Iterable<Pair<Rational, RoundingMode>> ps = filter(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isInteger(),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            BigInteger rounded = p.a.bigIntegerValue(p.b);
            assertTrue(p.toString(), rounded.equals(BigInteger.ZERO) || rounded.signum() == p.a.signum());
            assertTrue(p.toString(), lt(p.a.subtract(of(rounded)).abs(), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).bigIntegerValue(RoundingMode.UNNECESSARY), i);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.bigIntegerValue(RoundingMode.FLOOR), r.floor());
            assertEquals(r.toString(), r.bigIntegerValue(RoundingMode.CEILING), r.ceiling());
            assertTrue(r.toString(), le(of(r.bigIntegerValue(RoundingMode.DOWN)).abs(), r.abs()));
            assertTrue(r.toString(), ge(of(r.bigIntegerValue(RoundingMode.UP)).abs(), r.abs()));
            assertTrue(r.toString(), le(r.subtract(of(r.bigIntegerValue(RoundingMode.HALF_DOWN))).abs(), of(1, 2)));
            assertTrue(r.toString(), le(r.subtract(of(r.bigIntegerValue(RoundingMode.HALF_UP))).abs(), of(1, 2)));
            assertTrue(r.toString(), le(r.subtract(of(r.bigIntegerValue(RoundingMode.HALF_EVEN))).abs(), of(1, 2)));
        }

        Iterable<Rational> rs = filter(r -> lt(r.abs().fractionalPart(), of(1, 2)), P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), r.bigIntegerValue(RoundingMode.HALF_DOWN), r.bigIntegerValue(RoundingMode.DOWN));
            assertEquals(r.toString(), r.bigIntegerValue(RoundingMode.HALF_UP), r.bigIntegerValue(RoundingMode.DOWN));
            assertEquals(r.toString(), r.bigIntegerValue(RoundingMode.HALF_EVEN), r.bigIntegerValue(RoundingMode.DOWN));
        }

        rs = filter(r -> gt(r.abs().fractionalPart(), of(1, 2)), P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), r.bigIntegerValue(RoundingMode.HALF_DOWN), r.bigIntegerValue(RoundingMode.UP));
            assertEquals(r.toString(), r.bigIntegerValue(RoundingMode.HALF_UP), r.bigIntegerValue(RoundingMode.UP));
            assertEquals(r.toString(), r.bigIntegerValue(RoundingMode.HALF_EVEN), r.bigIntegerValue(RoundingMode.UP));
        }

        //odd multiples of 1/2
        rs = map(i -> of(i.shiftLeft(1).add(BigInteger.ONE), BigInteger.valueOf(2)), P.bigIntegers());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(
                    r.toString(),
                    r.bigIntegerValue(RoundingMode.HALF_DOWN),
                    r.bigIntegerValue(RoundingMode.DOWN))
            ;
            assertEquals(r.toString(), r.bigIntegerValue(RoundingMode.HALF_UP), r.bigIntegerValue(RoundingMode.UP));
            assertFalse(r.toString(), r.bigIntegerValue(RoundingMode.HALF_EVEN).testBit(0));
        }

        for (Rational r : take(LIMIT, filter(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.bigIntegerValue(RoundingMode.UNNECESSARY);
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesBigIntegerValue() {
        initialize();
        System.out.println("\t\ttesting bigIntegerValue() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            BigInteger rounded = r.bigIntegerValue();
            assertTrue(r.toString(), rounded.equals(BigInteger.ZERO) || rounded.signum() == r.signum());
            assertTrue(r.toString(), le(r.subtract(of(r.bigIntegerValue())).abs(), of(1, 2)));
        }

        Iterable<Rational> rs = filter(r -> lt(r.abs().fractionalPart(), of(1, 2)), P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), r.bigIntegerValue(), r.bigIntegerValue(RoundingMode.DOWN));
        }

        rs = filter(r -> gt(r.abs().fractionalPart(), of(1, 2)), P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), r.bigIntegerValue(), r.bigIntegerValue(RoundingMode.UP));
        }

        //odd multiples of 1/2
        rs = map(i -> of(i.shiftLeft(1).add(BigInteger.ONE), BigInteger.valueOf(2)), P.bigIntegers());
        for (Rational r : take(LIMIT, rs)) {
            assertFalse(r.toString(), r.bigIntegerValue().testBit(0));
        }
    }

    private static void propertiesBigIntegerValueExact() {
        initialize();
        System.out.println("\t\ttesting bigIntegerValueExact() properties...");

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).bigIntegerValueExact(), i);
        }

        for (Rational r : take(LIMIT, filter(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.bigIntegerValueExact();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesByteValueExact() {
        initialize();
        System.out.println("\t\ttesting byteValueExact() properties...");

        for (byte b : take(LIMIT, P.bytes())) {
            assertEquals(Byte.toString(b), of(b).byteValueExact(), b);
        }

        for (Rational r : take(LIMIT, filter(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.byteValueExact();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, P.rangeUp(BigInteger.valueOf(Byte.MAX_VALUE).add(BigInteger.ONE)))) {
            try {
                of(i).byteValueExact();
                fail(i.toString());
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> below = rangeBy(
                BigInteger.valueOf(Byte.MIN_VALUE).subtract(BigInteger.ONE),
                BigInteger.valueOf(-1)
        );
        for (BigInteger i : take(LIMIT, below)) {
            try {
                of(i).byteValueExact();
                fail(i.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesShortValueExact() {
        initialize();
        System.out.println("\t\ttesting shortValueExact() properties...");

        for (short s : take(LIMIT, P.shorts())) {
            assertEquals(Short.toString(s), of(s).shortValueExact(), s);
        }

        for (Rational r : take(LIMIT, filter(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.shortValueExact();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, P.rangeUp(BigInteger.valueOf(Short.MAX_VALUE).add(BigInteger.ONE)))) {
            try {
                of(i).shortValueExact();
                fail(i.toString());
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> below = rangeBy(
                BigInteger.valueOf(Short.MIN_VALUE).subtract(BigInteger.ONE),
                BigInteger.valueOf(-1)
        );
        for (BigInteger i : take(LIMIT, below)) {
            try {
                of(i).shortValueExact();
                fail(i.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesIntValueExact() {
        initialize();
        System.out.println("\t\ttesting intValueExact() properties...");

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(Integer.toString(i), of(i).intValueExact(), i);
        }

        for (Rational r : take(LIMIT, filter(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.intValueExact();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> is = P.withScale(33).rangeUp(BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE));
        for (BigInteger i : take(LIMIT, is)) {
            try {
                of(i).intValueExact();
                fail(i.toString());
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> below = rangeBy(
                BigInteger.valueOf(Integer.MIN_VALUE).subtract(BigInteger.ONE),
                BigInteger.valueOf(-1)
        );
        for (BigInteger i : take(LIMIT, below)) {
            try {
                of(i).intValueExact();
                fail(i.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesLongValueExact() {
        initialize();
        System.out.println("\t\ttesting longValueExact() properties...");

        for (long l : take(LIMIT, P.longs())) {
            assertEquals(Long.toString(l), of(l).longValueExact(), l);
        }

        for (Rational r : take(LIMIT, filter(s -> !s.isInteger(), P.rationals()))) {
            try {
                r.longValueExact();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> is = P.withScale(65).rangeUp(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));
        for (BigInteger i : take(LIMIT, is)) {
            try {
                of(i).longValueExact();
                fail(i.toString());
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> below = rangeBy(
                BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE),
                BigInteger.valueOf(-1)
        );
        for (BigInteger i : take(LIMIT, below)) {
            try {
                of(i).longValueExact();
                fail(i.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesHasTerminatingBaseExpansion() {
        initialize();

        System.out.println("\t\ttesting hasTerminatingBaseExpansion(BigInteger) properties...");

        Iterable<Pair<Rational, BigInteger>> ps = P.pairsSquareRootOrder(
                cons(ZERO, P.withScale(20).positiveRationals()),
                map(i -> BigInteger.valueOf(i + 2), P.withScale(20).naturalIntegersGeometric())
        );
        //todo fix hanging
//        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
//            boolean result = p.a.hasTerminatingBaseExpansion(p.b);
//            Iterable<BigInteger> dPrimeFactors = nub(MathUtils.primeFactors(p.a.getDenominator()));
//            Iterable<BigInteger> bPrimeFactors = nub(MathUtils.primeFactors(p.b));
//            assertEquals(p.toString(), result, isSubsetOf(dPrimeFactors, bPrimeFactors));
//        }

        if (!(P instanceof QBarExhaustiveProvider)) {
            ps = P.pairs(
                    cons(ZERO, P.withScale(8).positiveRationals()),
                    map(i -> BigInteger.valueOf(i + 2), P.withScale(20).naturalIntegersGeometric())
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            boolean result = p.a.hasTerminatingBaseExpansion(p.b);
            Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> pn = p.a.positionalNotation(p.b);
            assertEquals(p.toString(), result, pn.c.equals(Collections.singletonList(BigInteger.ZERO)));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.rangeDown(BigInteger.ONE)))) {
            try {
                p.a.hasTerminatingBaseExpansion(p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void propertiesBigDecimalValue_int_RoundingMode() {
        initialize();
        System.out.println("\t\ttesting bigDecimalValue(int, RoundingMode)...");

        Iterable<Pair<Rational, Pair<Integer, RoundingMode>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<Integer, RoundingMode>>) P.pairs(P.naturalIntegers(), P.roundingModes())
            );
        } else {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<Integer, RoundingMode>>) P.pairs(
                            P.withScale(20).naturalIntegersGeometric(),
                            P.roundingModes()
                    )
            );
        }
        ps = filter(
                p -> {
                    try {
                        p.a.bigDecimalValue(p.b.a, p.b.b);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                ps
        );
        for (Pair<Rational, Pair<Integer, RoundingMode>> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValue(p.b.a, p.b.b);
            assertTrue(p.toString(), eq(bd, BigDecimal.ZERO) || bd.signum() == p.a.signum());
        }

        for (Pair<Rational, Pair<Integer, RoundingMode>> p : take(LIMIT, filter(q -> q.b.a != 0 && q.a != ZERO, ps))) {
            BigDecimal bd = p.a.bigDecimalValue(p.b.a, p.b.b);
            assertTrue(p.toString(), bd.precision() == p.b.a);
        }

        Iterable<Pair<Rational, Integer>> pris;
        if (P instanceof QBarExhaustiveProvider) {
            pris = P.pairsSquareRootOrder(P.rationals(), P.naturalIntegers());
        } else {
            pris = P.pairs(P.rationals(), P.withScale(20).naturalIntegersGeometric());
        }
        pris = filter(
                p -> {
                    try {
                        p.a.bigDecimalValue(p.b);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                pris
        );

        Iterable<Pair<Rational, Integer>> priExact = filter(p -> of(p.a.bigDecimalValue(p.b)).equals(p.a), pris);
        for (Pair<Rational, Integer> pri : take(LIMIT, priExact)) {
            BigDecimal bd = pri.a.bigDecimalValue(pri.b, RoundingMode.UNNECESSARY);
            assertEquals(pri.toString(), bd, pri.a.bigDecimalValue(pri.b, RoundingMode.FLOOR));
            assertEquals(pri.toString(), bd, pri.a.bigDecimalValue(pri.b, RoundingMode.CEILING));
            assertEquals(pri.toString(), bd, pri.a.bigDecimalValue(pri.b, RoundingMode.DOWN));
            assertEquals(pri.toString(), bd, pri.a.bigDecimalValue(pri.b, RoundingMode.UP));
            assertEquals(pri.toString(), bd, pri.a.bigDecimalValue(pri.b, RoundingMode.HALF_DOWN));
            assertEquals(pri.toString(), bd, pri.a.bigDecimalValue(pri.b, RoundingMode.HALF_UP));
            assertEquals(pri.toString(), bd, pri.a.bigDecimalValue(pri.b, RoundingMode.HALF_EVEN));
        }

        Iterable<Pair<Rational, Integer>> priInexact = filter(p -> !of(p.a.bigDecimalValue(p.b)).equals(p.a), pris);
        for (Pair<Rational, Integer> pri : take(LIMIT, priInexact)) {
            BigDecimal low = pri.a.bigDecimalValue(pri.b, RoundingMode.FLOOR);
            BigDecimal high = pri.a.bigDecimalValue(pri.b, RoundingMode.CEILING);
            assertTrue(pri.toString(), lt(low, high));
        }

        for (Pair<Rational, Integer> pri : take(LIMIT, filter(p -> p.a.signum() == 1, priInexact))) {
            BigDecimal floor = pri.a.bigDecimalValue(pri.b, RoundingMode.FLOOR);
            BigDecimal down = pri.a.bigDecimalValue(pri.b, RoundingMode.DOWN);
            BigDecimal ceiling = pri.a.bigDecimalValue(pri.b, RoundingMode.CEILING);
            BigDecimal up = pri.a.bigDecimalValue(pri.b, RoundingMode.UP);
            assertEquals(pri.toString(), floor, down);
            assertEquals(pri.toString(), ceiling, up);
        }

        for (Pair<Rational, Integer> pri : take(LIMIT, filter(p -> p.a.signum() == -1, priInexact))) {
            BigDecimal floor = pri.a.bigDecimalValue(pri.b, RoundingMode.FLOOR);
            BigDecimal down = pri.a.bigDecimalValue(pri.b, RoundingMode.DOWN);
            BigDecimal ceiling = pri.a.bigDecimalValue(pri.b, RoundingMode.CEILING);
            BigDecimal up = pri.a.bigDecimalValue(pri.b, RoundingMode.UP);
            assertEquals(pri.toString(), floor, up);
            assertEquals(pri.toString(), ceiling, down);
        }

        Iterable<Pair<BigDecimal, Integer>> notMidpoints;
        if (P instanceof QBarExhaustiveProvider) {
            notMidpoints = P.pairsSquareRootOrder(P.bigDecimals(), P.naturalIntegers());
        } else {
            notMidpoints = P.pairs(P.bigDecimals(), P.withScale(20).naturalIntegersGeometric());
        }
        notMidpoints = filter(
                p -> {
                    //noinspection SimplifiableIfStatement
                    if (p.a.precision() <= 1 || p.b != p.a.precision() - 1) return false;
                    return !p.a.abs().unscaledValue().mod(BigInteger.valueOf(10)).equals(BigInteger.valueOf(5));
                },
                notMidpoints
        );
        for (Pair<BigDecimal, Integer> p : take(LIMIT, notMidpoints)) {
            Rational r = of(p.a);
            BigDecimal down = r.bigDecimalValue(p.b, RoundingMode.DOWN);
            BigDecimal up = r.bigDecimalValue(p.b, RoundingMode.UP);
            BigDecimal halfDown = r.bigDecimalValue(p.b, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = r.bigDecimalValue(p.b, RoundingMode.HALF_UP);
            BigDecimal halfEven = r.bigDecimalValue(p.b, RoundingMode.HALF_EVEN);
            boolean closerToDown = lt(r.subtract(of(down)).abs(), r.subtract(of(up)).abs());
            assertEquals(p.toString(), halfDown, closerToDown ? down : up);
            assertEquals(p.toString(), halfUp, closerToDown ? down : up);
            assertEquals(p.toString(), halfEven, closerToDown ? down : up);
        }

        Iterable<BigDecimal> midpoints = filter(
                x -> x.precision() > 1,
                map(
                        x -> new BigDecimal(
                                x.unscaledValue().multiply(BigInteger.TEN).add(BigInteger.valueOf(5)),
                                x.scale()
                        ),
                        P.bigDecimals()
                )
        );
        for (BigDecimal bd : take(LIMIT, midpoints)) {
            Rational r = of(bd);
            int precision = bd.precision() - 1;
            BigDecimal down = r.bigDecimalValue(precision, RoundingMode.DOWN);
            BigDecimal up = r.bigDecimalValue(precision, RoundingMode.UP);
            BigDecimal halfDown = r.bigDecimalValue(precision, RoundingMode.HALF_DOWN);
            BigDecimal halfUp = r.bigDecimalValue(precision, RoundingMode.HALF_UP);
            BigDecimal halfEven = r.bigDecimalValue(precision, RoundingMode.HALF_EVEN);
            assertEquals(bd.toString(), down, halfDown);
            assertEquals(bd.toString(), up, halfUp);
            assertTrue(bd.toString(), bd.scale() != halfEven.scale() + 1 || !halfEven.unscaledValue().testBit(0));
        }

        Iterable<Pair<Rational, Pair<Integer, RoundingMode>>> psFail = P.pairs(
                P.rationals(),
                (Iterable<Pair<Integer, RoundingMode>>) P.pairs(P.negativeIntegers(), P.roundingModes())
        );
        for (Pair<Rational, Pair<Integer, RoundingMode>> p : take(LIMIT, psFail)) {
            try {
                p.a.bigDecimalValue(p.b.a, p.b.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<Rational> rs = filter(r -> !r.hasTerminatingBaseExpansion(BigInteger.valueOf(10)), P.rationals());
        Iterable<Pair<Rational, Integer>> prisFail;
        if (P instanceof QBarExhaustiveProvider) {
            prisFail = P.pairsSquareRootOrder(rs, P.naturalIntegers());
        } else {
            prisFail = P.pairs(rs, P.withScale(20).naturalIntegersGeometric());
        }
        for (Pair<Rational, Integer> p : take(LIMIT, prisFail)) {
            try {
                p.a.bigDecimalValue(p.b, RoundingMode.UNNECESSARY);
                fail(p.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesBigDecimalValue_int() {
        initialize();
        System.out.println("\t\ttesting bigDecimalValue(int)...");

        Iterable<Pair<Rational, Integer>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsSquareRootOrder(P.rationals(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.rationals(), P.withScale(20).naturalIntegersGeometric());
        }
        ps = filter(
                p -> {
                    try {
                        p.a.bigDecimalValue(p.b);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                ps
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            BigDecimal bd = p.a.bigDecimalValue(p.b);
            assertEquals(p.toString(), bd, p.a.bigDecimalValue(p.b, RoundingMode.HALF_EVEN));
            assertTrue(p.toString(), eq(bd, BigDecimal.ZERO) || bd.signum() == p.a.signum());
        }

        for (Pair<Rational, Integer> p : take(LIMIT, filter(q -> q.b != 0 && q.a != ZERO, ps))) {
            BigDecimal bd = p.a.bigDecimalValue(p.b);
            assertTrue(p.toString(), bd.precision() == p.b);
        }

        Iterable<Pair<BigDecimal, Integer>> notMidpoints;
        if (P instanceof QBarExhaustiveProvider) {
            notMidpoints = P.pairsSquareRootOrder(P.bigDecimals(), P.naturalIntegers());
        } else {
            notMidpoints = P.pairs(P.bigDecimals(), P.withScale(20).naturalIntegersGeometric());
        }
        notMidpoints = filter(
                p -> {
                    //noinspection SimplifiableIfStatement
                    if (p.a.precision() <= 1 || p.b != p.a.precision() - 1) return false;
                    return !p.a.abs().unscaledValue().mod(BigInteger.valueOf(10)).equals(BigInteger.valueOf(5));
                },
                notMidpoints
        );
        for (Pair<BigDecimal, Integer> p : take(LIMIT, notMidpoints)) {
            Rational r = of(p.a);
            BigDecimal down = r.bigDecimalValue(p.b, RoundingMode.DOWN);
            BigDecimal up = r.bigDecimalValue(p.b, RoundingMode.UP);
            BigDecimal halfEven = r.bigDecimalValue(p.b);
            boolean closerToDown = lt(r.subtract(of(down)).abs(), r.subtract(of(up)).abs());
            assertEquals(p.toString(), halfEven, closerToDown ? down : up);
        }

        Iterable<BigDecimal> midpoints = filter(
                x -> x.precision() > 1,
                map(
                        x -> new BigDecimal(
                                x.unscaledValue().multiply(BigInteger.TEN).add(BigInteger.valueOf(5)),
                                x.scale()
                        ),
                        P.bigDecimals()
                )
        );
        for (BigDecimal bd : take(LIMIT, midpoints)) {
            Rational r = of(bd);
            int precision = bd.precision() - 1;
            BigDecimal halfEven = r.bigDecimalValue(precision);
            assertTrue(bd.toString(), bd.scale() != halfEven.scale() + 1 || !halfEven.unscaledValue().testBit(0));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.negativeIntegers()))) {
            try {
                p.a.bigDecimalValue(p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void propertiesBigDecimalValueExact() {
        initialize();
        System.out.println("\t\ttesting bigDecimalValueExact()...");

        Iterable<Rational> rs = filter(r -> r.hasTerminatingBaseExpansion(BigInteger.valueOf(10)), P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            BigDecimal bd = r.bigDecimalValueExact();
            assertEquals(r.toString(), bd, r.bigDecimalValue(0, RoundingMode.UNNECESSARY));
            assertTrue(r.toString(), bd.signum() == r.signum());
            assertEquals(r.toString(), of(bd), r);
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.negativeIntegers()))) {
            try {
                p.a.bigDecimalValue(p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<Rational> rsFail = filter(r -> !r.hasTerminatingBaseExpansion(BigInteger.valueOf(10)), P.rationals());
        for (Rational r : take(LIMIT, rsFail)) {
            try {
                r.bigDecimalValueExact();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesBinaryExponent() {
        initialize();
        System.out.println("\t\ttesting binaryExponent() properties...");

        for (Rational r : take(LIMIT, P.positiveRationals())) {
            int exponent = r.binaryExponent();
            Rational power = ONE.shiftLeft(exponent);
            assertTrue(r.toString(), le(power, r));
            assertTrue(r.toString(), le(r, power.shiftLeft(1)));
        }

        for (Rational r : take(LIMIT, P.rationals(Interval.lessThanOrEqualTo(ZERO)))) {
            try {
                r.binaryExponent();
                fail(r.toString());
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static boolean floatEquidistant(@NotNull Rational r) {
        float below = r.floatValue(RoundingMode.FLOOR);
        float above = r.floatValue(RoundingMode.CEILING);
        if (below == above || Float.isInfinite(below) || Float.isInfinite(above)) return false;
        Rational belowDistance = r.subtract(ofExact(below).get());
        Rational aboveDistance = ofExact(above).get().subtract(r);
        return belowDistance.equals(aboveDistance);
    }

    private static void propertiesFloatValue_RoundingMode() {
        initialize();
        System.out.println("\t\ttesting floatValue(RoundingMode) properties...");

        Iterable<Pair<Rational, RoundingMode>> ps = filter(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.equals(ofExact(p.a.floatValue(RoundingMode.FLOOR))),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            float rounded = p.a.floatValue(p.b);
            assertTrue(p.toString(), !Float.isNaN(rounded));
            assertTrue(p.toString(), rounded == 0.0f || Math.signum(rounded) == p.a.signum());
        }

        Iterable<Rational> rs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !FloatingPointUtils.isNegativeZero(f), P.floats())
        );
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.UNNECESSARY);
            assertEquals(r.toString(), r, ofExact(rounded).get());
            assertTrue(r.toString(), Float.isFinite(rounded));
            assertTrue(r.toString(), !FloatingPointUtils.isNegativeZero(new Float(rounded)));
        }

        rs = filter(r -> !r.equals(LARGEST_FLOAT), P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.FLOOR);
            float successor = FloatingPointUtils.successor(rounded);
            assertTrue(r.toString(), le(ofExact(rounded).get(), r));
            assertTrue(r.toString(), gt(ofExact(successor).get(), r));
            assertTrue(r.toString(), rounded < 0 || Float.isFinite(rounded));
            assertTrue(r.toString(), !FloatingPointUtils.isNegativeZero(new Float(rounded)));
        }

        rs = filter(
                r -> !r.equals(LARGEST_FLOAT.negate()),
                P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT))
        );
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.CEILING);
            float predecessor = FloatingPointUtils.predecessor(rounded);
            assertTrue(r.toString(), ge(ofExact(rounded).get(), r));
            assertTrue(r.toString(), lt(ofExact(predecessor).get(), r));
            assertTrue(r.toString(), rounded > 0 || Float.isFinite(rounded));
        }

        rs = P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT));
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.DOWN);
            assertTrue(r.toString(), le(ofExact(rounded).get().abs(), r.abs()));
            assertTrue(r.toString(), Float.isFinite(rounded));
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.DOWN);
            float successor = FloatingPointUtils.successor(rounded);
            float predecessor = FloatingPointUtils.predecessor(rounded);
            float down = r.signum() == -1 ? successor : predecessor;
            assertTrue(r.toString(), lt(ofExact(down).get().abs(), r.abs()));
        }

        rs = filter(
                r -> !r.abs().equals(LARGEST_FLOAT),
                P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT))
        );
        for (Rational r : take(LIMIT, rs)) {
            assertTrue(r.toString(), !FloatingPointUtils.isNegativeZero(new Float(r.floatValue(RoundingMode.UP))));
        }

        rs = filter(r -> !r.equals(SMALLEST_FLOAT), P.rationals(Interval.of(ZERO, SMALLEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r.toString(), r.floatValue(RoundingMode.FLOOR), 0.0f);
            aeqf(r.toString(), r.floatValue(RoundingMode.DOWN), 0.0f);

            float rounded = r.floatValue(RoundingMode.UP);
            float successor = FloatingPointUtils.successor(rounded);
            float predecessor = FloatingPointUtils.predecessor(rounded);
            float up = r.signum() == -1 ? predecessor : successor;
            assertTrue(r.toString(), gt(ofExact(up).get().abs(), r.abs()));
        }

        rs = filter(
                r -> !r.equals(LARGEST_FLOAT.negate()),
                P.rationals(Interval.lessThanOrEqualTo(LARGEST_FLOAT.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            float floor = r.floatValue(RoundingMode.FLOOR);
            aeqf(r.toString(), floor, Float.NEGATIVE_INFINITY);

            float up = r.floatValue(RoundingMode.UP);
            aeqf(r.toString(), up, Float.NEGATIVE_INFINITY);

            float halfUp = r.floatValue(RoundingMode.HALF_UP);
            aeqf(r.toString(), halfUp, Float.NEGATIVE_INFINITY);

            float halfEven = r.floatValue(RoundingMode.HALF_EVEN);
            aeqf(r.toString(), halfEven, Float.NEGATIVE_INFINITY);
        }

        rs = P.rationals(Interval.greaterThanOrEqualTo(LARGEST_FLOAT));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r.toString(), r.floatValue(RoundingMode.FLOOR), Float.MAX_VALUE);
            aeqf(r.toString(), r.floatValue(RoundingMode.DOWN), Float.MAX_VALUE);
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), Float.MAX_VALUE);
        }

        rs = filter(
                r -> r != ZERO && !r.equals(SMALLEST_FLOAT.negate()),
                P.rationals(Interval.of(SMALLEST_FLOAT.negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r.toString(), r.floatValue(RoundingMode.CEILING), -0.0f);
            aeqf(r.toString(), r.floatValue(RoundingMode.DOWN), -0.0f);
        }

        rs = filter(r -> !r.equals(LARGEST_FLOAT), P.rationals(Interval.greaterThanOrEqualTo(LARGEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            float ceiling = r.floatValue(RoundingMode.CEILING);
            aeqf(r.toString(), ceiling, Float.POSITIVE_INFINITY);

            float up = r.floatValue(RoundingMode.UP);
            aeqf(r.toString(), up, Float.POSITIVE_INFINITY);

            float halfUp = r.floatValue(RoundingMode.HALF_UP);
            aeqf(r.toString(), halfUp, Float.POSITIVE_INFINITY);

            float halfEven = r.floatValue(RoundingMode.HALF_EVEN);
            aeqf(r.toString(), halfEven, Float.POSITIVE_INFINITY);
        }

        rs = P.rationals(Interval.lessThanOrEqualTo(LARGEST_FLOAT.negate()));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r.toString(), r.floatValue(RoundingMode.CEILING), -Float.MAX_VALUE);
            aeqf(r.toString(), r.floatValue(RoundingMode.DOWN), -Float.MAX_VALUE);
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), -Float.MAX_VALUE);
        }

        Iterable<Rational> midpoints = map(
                f -> {
                    Rational lo = ofExact(f).get();
                    Rational hi = ofExact(FloatingPointUtils.successor(f)).get();
                    return lo.add(hi).shiftRight(1);
                },
                filter(
                        f -> Float.isFinite(f) && !FloatingPointUtils.isNegativeZero(f) && f != Float.MAX_VALUE,
                        P.floats()
                )
        );
        for (Rational r : take(LIMIT, midpoints)) {
            float down = r.floatValue(RoundingMode.DOWN);
            float up = r.floatValue(RoundingMode.UP);
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), down);
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_UP), up);
            float halfEven = r.floatValue(RoundingMode.HALF_EVEN);
            assertTrue(r.toString(), ((Float.floatToIntBits(down) & 1) == 0 ? down : up) == halfEven);
        }

        Iterable<Rational> notMidpoints = filter(
                r -> ge(r, LARGEST_FLOAT.negate()) && le(r, LARGEST_FLOAT) && !floatEquidistant(r),
                P.rationals()
        );
        for (Rational r : take(LIMIT, notMidpoints)) {
            float below = r.floatValue(RoundingMode.FLOOR);
            float above = r.floatValue(RoundingMode.CEILING);
            Rational belowDistance = r.subtract(ofExact(below).get());
            Rational aboveDistance = ofExact(above).get().subtract(r);
            float closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), closest);
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_UP), closest);
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_EVEN), closest);
        }

        rs = P.rationals(Interval.of(ZERO, SMALLEST_FLOAT.shiftRight(1)));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), 0.0f);
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_EVEN), 0.0f);
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(SMALLEST_FLOAT.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), -0.0f);
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_EVEN), -0.0f);
        }

        rs = filter(
                r -> !r.equals(SMALLEST_FLOAT.shiftRight(1)),
                P.rationals(Interval.of(ZERO, SMALLEST_FLOAT.shiftRight(1)))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_UP), 0.0f);
        }

        rs = filter(
                r -> r != ZERO && !r.equals(SMALLEST_FLOAT.shiftRight(1).negate()),
                P.rationals(Interval.of(SMALLEST_FLOAT.shiftRight(1).negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r.toString(), r.floatValue(RoundingMode.HALF_UP), -0.0f);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            float floor = r.floatValue(RoundingMode.FLOOR);
            assertFalse(r.toString(), FloatingPointUtils.isNegativeZero(Float.valueOf(floor)));
            assertFalse(r.toString(), floor == Float.POSITIVE_INFINITY);
            float ceiling = r.floatValue(RoundingMode.CEILING);
            assertFalse(r.toString(), ceiling == Float.NEGATIVE_INFINITY);
            float down = r.floatValue(RoundingMode.DOWN);
            assertFalse(r.toString(), down == Float.NEGATIVE_INFINITY);
            assertFalse(r.toString(), down == Float.POSITIVE_INFINITY);
            float up = r.floatValue(RoundingMode.UP);
            assertFalse(r.toString(), FloatingPointUtils.isNegativeZero(Float.valueOf(up)));
            float halfDown = r.floatValue(RoundingMode.HALF_DOWN);
            assertFalse(r.toString(), halfDown == Float.NEGATIVE_INFINITY);
            assertFalse(r.toString(), halfDown == Float.POSITIVE_INFINITY);
        }

        Iterable<Rational> rsFail = filter(
                r -> !ofExact(r.floatValue(RoundingMode.FLOOR)).get().equals(r),
                P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT))
        );
        for (Rational r : take(LIMIT, rsFail)) {
            try {
                r.floatValue(RoundingMode.UNNECESSARY);
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesFloatValue() {
        initialize();
        System.out.println("\t\ttesting floatValue() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            float rounded = r.floatValue();
            aeqf(r.toString(), rounded, r.floatValue(RoundingMode.HALF_EVEN));
            assertTrue(r.toString(), !Float.isNaN(rounded));
            assertTrue(r.toString(), rounded == 0.0f || Math.signum(rounded) == r.signum());
        }

        Iterable<Rational> rs = filter(
                r -> !r.equals(LARGEST_FLOAT.negate()),
                P.rationals(Interval.lessThanOrEqualTo(LARGEST_FLOAT.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue();
            aeqf(r.toString(), rounded, Float.NEGATIVE_INFINITY);
        }

        rs = filter(r -> !r.equals(LARGEST_FLOAT), P.rationals(Interval.greaterThanOrEqualTo(LARGEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue();
            aeqf(r.toString(), rounded, Float.POSITIVE_INFINITY);
        }

        Iterable<Rational> midpoints = map(
                f -> {
                    Rational lo = ofExact(f).get();
                    Rational hi = ofExact(FloatingPointUtils.successor(f)).get();
                    return lo.add(hi).shiftRight(1);
                },
                filter(f -> Float.isFinite(f) && f != Float.MAX_VALUE, P.floats())
        );
        for (Rational r : take(LIMIT, midpoints)) {
            float down = r.floatValue(RoundingMode.DOWN);
            float up = r.floatValue(RoundingMode.UP);
            float rounded = r.floatValue();
            assertTrue(r.toString(), ((Float.floatToIntBits(down) & 1) == 0 ? down : up) == rounded);
        }

        Iterable<Rational> notMidpoints = filter(
                r -> ge(r, LARGEST_FLOAT.negate()) && le(r, LARGEST_FLOAT) && !floatEquidistant(r),
                P.rationals()
        );
        for (Rational r : take(LIMIT, notMidpoints)) {
            float below = r.floatValue(RoundingMode.FLOOR);
            float above = r.floatValue(RoundingMode.CEILING);
            Rational belowDistance = r.subtract(ofExact(below).get());
            Rational aboveDistance = ofExact(above).get().subtract(r);
            float closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqf(r.toString(), r.floatValue(), closest);
        }

        rs = P.rationals(Interval.of(ZERO, SMALLEST_FLOAT.shiftRight(1)));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r.toString(), r.floatValue(), 0.0f);
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(SMALLEST_FLOAT.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeqf(r.toString(), r.floatValue(), -0.0f);
        }
    }

    private static void propertiesFloatValueExact() {
        initialize();
        System.out.println("\t\ttesting floatValueExact() properties...");

        Iterable<Rational> rs = filter(
                r -> {
                    Optional<Rational> fr = ofExact(r.floatValue(RoundingMode.FLOOR));
                    return fr.isPresent() && fr.get().equals(r);
                },
                P.rationals()
        );
        for (Rational r : take(LIMIT, rs)) {
            float f = r.floatValueExact();
            assertTrue(r.toString(), !Float.isNaN(f));
            assertTrue(r.toString(), f == 0.0f || Math.signum(f) == r.signum());
        }

        rs = map(
                f -> ofExact(f).get(),
                filter(f -> Float.isFinite(f) && !FloatingPointUtils.isNegativeZero(f), P.floats())
        );
        for (Rational r : take(LIMIT, rs)) {
            float f = r.floatValueExact();
            assertEquals(r.toString(), r, ofExact(f).get());
            assertTrue(r.toString(), Float.isFinite(f));
            assertTrue(r.toString(), !FloatingPointUtils.isNegativeZero(new Float(f)));
        }

        Iterable<Rational> rsFail = filter(
                r -> !ofExact(r.floatValue(RoundingMode.FLOOR)).get().equals(r),
                P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT))
        );
        for (Rational r : take(LIMIT, rsFail)) {
            try {
                r.floatValueExact();
                fail(r.toString());
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

    private static void propertiesDoubleValue_RoundingMode() {
        initialize();
        System.out.println("\t\ttesting doubleValue(RoundingMode) properties...");

        Iterable<Pair<Rational, RoundingMode>> ps = filter(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.equals(ofExact(p.a.doubleValue(RoundingMode.FLOOR)).get()),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            double rounded = p.a.doubleValue(p.b);
            assertTrue(p.toString(), !Double.isNaN(rounded));
            assertTrue(p.toString(), rounded == 0.0 || Math.signum(rounded) == p.a.signum());
        }

        Iterable<Rational> rs = map(
                d -> ofExact(d).get(),
                filter(d -> Double.isFinite(d) && !FloatingPointUtils.isNegativeZero(d), P.doubles())
        );
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.UNNECESSARY);
            assertEquals(r.toString(), r, ofExact(rounded).get());
            assertTrue(r.toString(), Double.isFinite(rounded));
            assertTrue(r.toString(), !FloatingPointUtils.isNegativeZero(new Double(rounded)));
        }

        rs = filter(r -> !r.equals(LARGEST_DOUBLE), P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.FLOOR);
            double successor = FloatingPointUtils.successor(rounded);
            assertTrue(r.toString(), le(ofExact(rounded).get(), r));
            assertTrue(r.toString(), gt(ofExact(successor).get(), r));
            assertTrue(r.toString(), rounded < 0 || Double.isFinite(rounded));
            assertTrue(r.toString(), !FloatingPointUtils.isNegativeZero(new Double(rounded)));
        }

        rs = filter(
                r -> !r.equals(LARGEST_DOUBLE.negate()),
                P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.CEILING);
            double predecessor = FloatingPointUtils.predecessor(rounded);
            assertTrue(r.toString(), ge(ofExact(rounded).get(), r));
            assertTrue(r.toString(), lt(ofExact(predecessor).get(), r));
            assertTrue(r.toString(), rounded > 0 || Double.isFinite(rounded));
        }

        rs = P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE));
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.DOWN);
            assertTrue(r.toString(), le(ofExact(rounded).get().abs(), r.abs()));
            assertTrue(r.toString(), Double.isFinite(rounded));
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.DOWN);
            double successor = FloatingPointUtils.successor(rounded);
            double predecessor = FloatingPointUtils.predecessor(rounded);
            double down = r.signum() == -1 ? successor : predecessor;
            assertTrue(r.toString(), lt(ofExact(down).get().abs(), r.abs()));
        }

        rs = filter(
                r -> !r.abs().equals(LARGEST_DOUBLE),
                P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rs)) {
            assertTrue(r.toString(), !FloatingPointUtils.isNegativeZero(new Double(r.doubleValue(RoundingMode.UP))));
        }

        rs = filter(r -> !r.equals(SMALLEST_DOUBLE), P.rationals(Interval.of(ZERO, SMALLEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r.toString(), r.doubleValue(RoundingMode.FLOOR), 0.0);
            aeqd(r.toString(), r.doubleValue(RoundingMode.DOWN), 0.0);

            double rounded = r.doubleValue(RoundingMode.UP);
            double successor = FloatingPointUtils.successor(rounded);
            double predecessor = FloatingPointUtils.predecessor(rounded);
            double up = r.signum() == -1 ? predecessor : successor;
            assertTrue(r.toString(), gt(ofExact(up).get().abs(), r.abs()));
        }

        rs = filter(
                r -> !r.equals(LARGEST_DOUBLE.negate()),
                P.rationals(Interval.lessThanOrEqualTo(LARGEST_DOUBLE.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            double floor = r.doubleValue(RoundingMode.FLOOR);
            aeqd(r.toString(), floor, Double.NEGATIVE_INFINITY);

            double up = r.doubleValue(RoundingMode.UP);
            aeqd(r.toString(), up, Double.NEGATIVE_INFINITY);

            double halfUp = r.doubleValue(RoundingMode.HALF_UP);
            aeqd(r.toString(), halfUp, Double.NEGATIVE_INFINITY);

            double halfEven = r.doubleValue(RoundingMode.HALF_EVEN);
            aeqd(r.toString(), halfEven, Double.NEGATIVE_INFINITY);
        }

        rs = P.rationals(Interval.greaterThanOrEqualTo(LARGEST_DOUBLE));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r.toString(), r.doubleValue(RoundingMode.FLOOR), Double.MAX_VALUE);
            aeqd(r.toString(), r.doubleValue(RoundingMode.DOWN), Double.MAX_VALUE);
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), Double.MAX_VALUE);
        }

        rs = filter(
                r -> r != ZERO && !r.equals(SMALLEST_DOUBLE.negate()),
                P.rationals(Interval.of(SMALLEST_DOUBLE.negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r.toString(), r.doubleValue(RoundingMode.CEILING), -0.0);
            aeqd(r.toString(), r.doubleValue(RoundingMode.DOWN), -0.0);
        }

        rs = filter(r -> !r.equals(LARGEST_DOUBLE), P.rationals(Interval.greaterThanOrEqualTo(LARGEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            double ceiling = r.doubleValue(RoundingMode.CEILING);
            aeqd(r.toString(), ceiling, Double.POSITIVE_INFINITY);

            double up = r.doubleValue(RoundingMode.UP);
            aeqd(r.toString(), up, Double.POSITIVE_INFINITY);

            double halfUp = r.doubleValue(RoundingMode.HALF_UP);
            aeqd(r.toString(), halfUp, Double.POSITIVE_INFINITY);

            double halfEven = r.doubleValue(RoundingMode.HALF_EVEN);
            aeqd(r.toString(), halfEven, Double.POSITIVE_INFINITY);
        }

        rs = P.rationals(Interval.lessThanOrEqualTo(LARGEST_DOUBLE.negate()));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r.toString(), r.doubleValue(RoundingMode.CEILING), -Double.MAX_VALUE);
            aeqd(r.toString(), r.doubleValue(RoundingMode.DOWN), -Double.MAX_VALUE);
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), -Double.MAX_VALUE);
        }

        Iterable<Rational> midpoints = map(
                d -> {
                    Rational lo = ofExact(d).get();
                    Rational hi = ofExact(FloatingPointUtils.successor(d)).get();
                    return lo.add(hi).shiftRight(1);
                },
                filter(d -> Double.isFinite(d) && d != Double.MAX_VALUE, P.doubles())
        );
        for (Rational r : take(LIMIT, midpoints)) {
            double down = r.doubleValue(RoundingMode.DOWN);
            double up = r.doubleValue(RoundingMode.UP);
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), down);
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_UP), up);
            double halfEven = r.doubleValue(RoundingMode.HALF_EVEN);
            assertTrue(r.toString(), ((Double.doubleToLongBits(down) & 1) == 0 ? down : up) == halfEven);
        }

        Iterable<Rational> notMidpoints = filter(
                r -> ge(r, LARGEST_DOUBLE.negate()) && le(r, LARGEST_DOUBLE) && !doubleEquidistant(r),
                P.rationals()
        );
        for (Rational r : take(LIMIT, notMidpoints)) {
            double below = r.doubleValue(RoundingMode.FLOOR);
            double above = r.doubleValue(RoundingMode.CEILING);
            Rational belowDistance = r.subtract(ofExact(below).get());
            Rational aboveDistance = ofExact(above).get().subtract(r);
            double closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), closest);
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_UP), closest);
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_EVEN), closest);
        }

        rs = P.rationals(Interval.of(ZERO, SMALLEST_DOUBLE.shiftRight(1)));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), 0.0);
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_EVEN), 0.0);
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(SMALLEST_DOUBLE.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), -0.0);
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_EVEN), -0.0);
        }

        rs = filter(
                r -> !r.equals(SMALLEST_DOUBLE.shiftRight(1)),
                P.rationals(Interval.of(ZERO, SMALLEST_DOUBLE.shiftRight(1)))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_UP), 0.0);
        }

        rs = filter(
                r -> r != ZERO && !r.equals(SMALLEST_DOUBLE.shiftRight(1).negate()),
                P.rationals(Interval.of(SMALLEST_DOUBLE.shiftRight(1).negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r.toString(), r.doubleValue(RoundingMode.HALF_UP), -0.0);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            double floor = r.doubleValue(RoundingMode.FLOOR);
            assertFalse(r.toString(), FloatingPointUtils.isNegativeZero(Double.valueOf(floor)));
            assertFalse(r.toString(), floor == Double.POSITIVE_INFINITY);
            double ceiling = r.doubleValue(RoundingMode.CEILING);
            assertFalse(r.toString(), ceiling == Double.NEGATIVE_INFINITY);
            double down = r.doubleValue(RoundingMode.DOWN);
            assertFalse(r.toString(), down == Double.NEGATIVE_INFINITY);
            assertFalse(r.toString(), down == Double.POSITIVE_INFINITY);
            double up = r.doubleValue(RoundingMode.UP);
            assertFalse(r.toString(), FloatingPointUtils.isNegativeZero(Double.valueOf(up)));
            double halfDown = r.doubleValue(RoundingMode.HALF_DOWN);
            assertFalse(r.toString(), halfDown == Double.NEGATIVE_INFINITY);
            assertFalse(r.toString(), halfDown == Double.POSITIVE_INFINITY);
        }

        Iterable<Rational> rsFail = filter(
                r -> !ofExact(r.doubleValue(RoundingMode.FLOOR)).get().equals(r),
                P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rsFail)) {
            try {
                r.doubleValue(RoundingMode.UNNECESSARY);
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesDoubleValue() {
        initialize();
        System.out.println("\t\ttesting doubleValue() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            double rounded = r.doubleValue();
            aeqd(r.toString(), rounded, r.doubleValue(RoundingMode.HALF_EVEN));
            assertTrue(r.toString(), !Double.isNaN(rounded));
            assertTrue(r.toString(), rounded == 0.0 || Math.signum(rounded) == r.signum());
        }

        Iterable<Rational> rs = filter(
                r -> !r.equals(LARGEST_DOUBLE.negate()),
                P.rationals(Interval.lessThanOrEqualTo(LARGEST_DOUBLE.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue();
            aeqd(r.toString(), rounded, Double.NEGATIVE_INFINITY);
        }

        rs = filter(r -> !r.equals(LARGEST_DOUBLE), P.rationals(Interval.greaterThanOrEqualTo(LARGEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue();
            aeqd(r.toString(), rounded, Double.POSITIVE_INFINITY);
        }

        Iterable<Rational> midpoints = map(
                d -> {
                    Rational lo = ofExact(d).get();
                    Rational hi = ofExact(FloatingPointUtils.successor(d)).get();
                    return lo.add(hi).shiftRight(1);
                },
                filter(d -> Double.isFinite(d) && d != Double.MAX_VALUE, P.doubles())
        );
        for (Rational r : take(LIMIT, midpoints)) {
            double down = r.doubleValue(RoundingMode.DOWN);
            double up = r.doubleValue(RoundingMode.UP);
            double rounded = r.doubleValue();
            assertTrue(r.toString(), ((Double.doubleToLongBits(down) & 1) == 0 ? down : up) == rounded);
        }

        Iterable<Rational> notMidpoints = filter(
                r -> ge(r, LARGEST_DOUBLE.negate()) && le(r, LARGEST_DOUBLE) && !doubleEquidistant(r),
                P.rationals()
        );
        for (Rational r : take(LIMIT, notMidpoints)) {
            double below = r.doubleValue(RoundingMode.FLOOR);
            double above = r.doubleValue(RoundingMode.CEILING);
            Rational belowDistance = r.subtract(ofExact(below).get());
            Rational aboveDistance = ofExact(above).get().subtract(r);
            double closest = lt(belowDistance, aboveDistance) ? below : above;
            aeqd(r.toString(), r.doubleValue(), closest);
        }

        rs = P.rationals(Interval.of(ZERO, SMALLEST_DOUBLE.shiftRight(1)));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r.toString(), r.doubleValue(), 0.0);
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(SMALLEST_DOUBLE.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeqd(r.toString(), r.doubleValue(), -0.0);
        }
    }

    private static void propertiesDoubleValueExact() {
        initialize();
        System.out.println("\t\ttesting doubleValueExact() properties...");

        Iterable<Rational> rs = filter(
                r -> {
                    Optional<Rational> dr = ofExact(r.doubleValue(RoundingMode.FLOOR));
                    return dr.isPresent() && dr.get().equals(r);
                },
                P.rationals()
        );
        for (Rational r : take(LIMIT, rs)) {
            double d = r.doubleValueExact();
            assertTrue(r.toString(), !Double.isNaN(d));
            assertTrue(r.toString(), d == 0.0 || Math.signum(d) == r.signum());
        }

        rs = map(
                d -> ofExact(d).get(),
                filter(d -> Double.isFinite(d) && !FloatingPointUtils.isNegativeZero(d), P.doubles())
        );
        for (Rational r : take(LIMIT, rs)) {
            double d = r.doubleValueExact();
            assertEquals(r.toString(), r, ofExact(d).get());
            assertTrue(r.toString(), Double.isFinite(d));
            assertTrue(r.toString(), !FloatingPointUtils.isNegativeZero(new Double(d)));
        }

        Iterable<Rational> rsFail = filter(
                r -> !ofExact(r.doubleValue(RoundingMode.FLOOR)).get().equals(r),
                P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rsFail)) {
            try {
                r.doubleValueExact();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull Rational add_simplest(@NotNull Rational a, @NotNull Rational b) {
        return of(
                a.getNumerator().multiply(b.getDenominator()).add(a.getDenominator().multiply(b.getNumerator())),
                a.getDenominator().multiply(b.getDenominator())
        );
    }

    private static void propertiesAdd() {
        initialize();
        System.out.println("\t\ttesting add(Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            Rational sum = p.a.add(p.b);
            sum.validate();
            assertEquals(p.toString(), sum, add_simplest(p.a, p.b));
            assertEquals(p.toString(), sum, p.b.add(p.a));
            assertEquals(p.toString(), sum.subtract(p.b), p.a);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), ZERO.add(r), r);
            assertEquals(r.toString(), r.add(ZERO), r);
            assertTrue(r.toString(), r.add(r.negate()) == ZERO);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(P.rationals()))) {
            Rational sum1 = t.a.add(t.b).add(t.c);
            Rational sum2 = t.a.add(t.b.add(t.c));
            assertEquals(t.toString(), sum1, sum2);
        }
    }

    private static void compareImplementationsAdd() {
        initialize();
        System.out.println("\t\tcomparing add(Rational) implementations...");

        long totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            long time = System.nanoTime();
            add_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            long time = System.nanoTime();
            p.a.add(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static void propertiesNegate() {
        initialize();
        System.out.println("\t\ttesting negate() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational negative = r.negate();
            negative.validate();
            assertEquals(r.toString(), r, negative.negate());
            assertTrue(r.toString(), r.add(negative) == ZERO);
        }

        for (Rational r : take(LIMIT, filter(s -> s != ZERO, P.rationals()))) {
            Rational negative = r.negate();
            assertNotEquals(r.toString(), r, negative);
        }
    }

    private static void propertiesAbs() {
        initialize();
        System.out.println("\t\ttesting abs() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational abs = r.abs();
            abs.validate();
            assertEquals(r.toString(), abs, abs.abs());
            assertNotEquals(r.toString(), abs.signum(), -1);
            assertTrue(r.toString(), ge(abs, ZERO));
        }
    }

    private static void propertiesSignum() {
        initialize();
        System.out.println("\t\ttesting signum() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            int signum = r.signum();
            assertEquals(r.toString(), signum, Ordering.compare(r, ZERO).toInt());
            assertTrue(r.toString(), signum == -1 || signum == 0 || signum == 1);
        }
    }

    private static @NotNull Rational subtract_simplest(@NotNull Rational a, @NotNull Rational b) {
        return a.add(b.negate());
    }

    private static void propertiesSubtract() {
        initialize();
        System.out.println("\t\ttesting subtract(Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            Rational difference = p.a.subtract(p.b);
            difference.validate();
            assertEquals(p.toString(), difference, subtract_simplest(p.a, p.b));
            assertEquals(p.toString(), difference, p.b.subtract(p.a).negate());
            assertEquals(p.toString(), p.a, difference.add(p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), ZERO.subtract(r), r.negate());
            assertEquals(r.toString(), r.subtract(ZERO), r);
            assertTrue(r.toString(), r.subtract(r) == ZERO);
        }
    }

    private static void compareImplementationsSubtract() {
        initialize();
        System.out.println("\t\tcomparing subtract(Rational) implementations...");

        long totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            long time = System.nanoTime();
            subtract_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            long time = System.nanoTime();
            p.a.subtract(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Pair<BigInteger, BigInteger> multiply_Rational_Knuth(
            @NotNull Rational a,
            @NotNull Rational b
    ) {
        if (a == ZERO || b == ZERO) return new Pair<>(BigInteger.ZERO, BigInteger.ONE);
        if (a == ONE) return new Pair<>(b.getNumerator(), b.getDenominator());
        if (b == ONE) return new Pair<>(a.getNumerator(), a.getDenominator());
        BigInteger g1 = a.getNumerator().gcd(b.getDenominator());
        BigInteger g2 = b.getNumerator().gcd(a.getDenominator());
        BigInteger mn = a.getNumerator().divide(g1).multiply(b.getNumerator().divide(g2));
        BigInteger md = a.getDenominator().divide(g2).multiply(b.getDenominator().divide(g1));
        if (mn.equals(md)) return new Pair<>(BigInteger.ONE, BigInteger.ONE);
        return new Pair<>(mn, md);
    }

    private static void propertiesMultiply_Rational() {
        initialize();
        System.out.println("\t\ttesting multiply(Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            Rational product = p.a.multiply(p.b);
            product.validate();
            assertEquals(
                    p.toString(),
                    new Pair<>(product.getNumerator(), product.getDenominator()),
                    multiply_Rational_Knuth(p.a, p.b)
            );
            assertEquals(p.toString(), product, p.b.multiply(p.a));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals(), filter(r -> r != ZERO, P.rationals())))) {
            assertEquals(p.toString(), p.a.multiply(p.b).divide(p.b), p.a);
            assertEquals(p.toString(), p.a.multiply(p.b), p.a.divide(p.b.invert()));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), ONE.multiply(r), r);
            assertEquals(r.toString(), r.multiply(ONE), r);
            assertTrue(r.toString(), ZERO.multiply(r) == ZERO);
            assertTrue(r.toString(), r.multiply(ZERO) == ZERO);
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            assertTrue(r.toString(), r.multiply(r.invert()) == ONE);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(P.rationals()))) {
            Rational product1 = t.a.multiply(t.b).multiply(t.c);
            Rational product2 = t.a.multiply(t.b.multiply(t.c));
            assertEquals(t.toString(), product1, product2);
            Rational expression1 = t.a.add(t.b).multiply(t.c);
            Rational expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void compareImplementationsMultiply_Rational() {
        initialize();
        System.out.println("\t\tcomparing multiply(Rational) implementations...");

        long totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            long time = System.nanoTime();
            multiply_Rational_Knuth(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tKnuth: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            long time = System.nanoTime();
            p.a.multiply(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational multiply_BigInteger_simplest(@NotNull Rational a, @NotNull BigInteger b) {
        return of(a.getNumerator().multiply(b), a.getDenominator());
    }

    private static void propertiesMultiply_BigInteger() {
        initialize();
        System.out.println("\t\ttesting multiply(BigInteger) properties...");

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.bigIntegers()))) {
            Rational product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p.toString(), product, multiply_BigInteger_simplest(p.a, p.b));
            assertEquals(p.toString(), product, p.a.multiply(of(p.b)));
            assertEquals(p.toString(), product, of(p.b).multiply(p.a));
        }

        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(
                P.rationals(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.multiply(p.b).divide(p.b), p.a);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), ONE.multiply(i), of(i));
            assertTrue(i.toString(), ZERO.multiply(i) == ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.multiply(BigInteger.ONE), r);
            assertTrue(r.toString(), r.multiply(BigInteger.ZERO) == ZERO);
        }

        for (BigInteger i : take(LIMIT, filter(j -> !j.equals(BigInteger.ZERO), P.bigIntegers()))) {
            assertTrue(i.toString(), of(i).invert().multiply(i) == ONE);
        }

        Iterable<Triple<Rational, Rational, BigInteger>> ts = P.triples(P.rationals(), P.rationals(), P.bigIntegers());
        for (Triple<Rational, Rational, BigInteger> t : take(LIMIT, ts)) {
            Rational expression1 = t.a.add(t.b).multiply(t.c);
            Rational expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void compareImplementationsMultiply_BigInteger() {
        initialize();
        System.out.println("\t\tcomparing multiply(BigInteger) implementations...");

        long totalTime = 0;
        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.bigIntegers()))) {
            long time = System.nanoTime();
            multiply_BigInteger_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.bigIntegers()))) {
            long time = System.nanoTime();
            p.a.multiply(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational multiply_int_simplest(@NotNull Rational a, int b) {
        return of(a.getNumerator().multiply(BigInteger.valueOf(b)), a.getDenominator());
    }

    private static void propertiesMultiply_int() {
        initialize();
        System.out.println("\t\ttesting multiply(int) properties...");

        for (Pair<Rational, Integer> p :take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            Rational product = p.a.multiply(p.b);
            product.validate();
            assertEquals(p.toString(), product, multiply_int_simplest(p.a, p.b));
            assertEquals(p.toString(), product, p.a.multiply(p.b));
            assertEquals(p.toString(), product, p.a.multiply(of(p.b)));
            assertEquals(p.toString(), product, of(p.b).multiply(p.a));
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), filter(i -> i != 0, P.integers())))) {
            assertEquals(p.toString(), p.a.multiply(p.b).divide(p.b), p.a);
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(Integer.toString(i), ONE.multiply(i), of(i));
            assertTrue(Integer.toString(i), ZERO.multiply(i) == ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.multiply(1), r);
            assertTrue(r.toString(), r.multiply(0) == ZERO);
        }

        for (int i : take(LIMIT, filter(j -> j != 0, P.integers()))) {
            assertTrue(Integer.toString(i), of(i).invert().multiply(i) == ONE);
        }

        Iterable<Triple<Rational, Rational, Integer>> ts = P.triples(P.rationals(), P.rationals(), P.integers());
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, ts)) {
            Rational expression1 = t.a.add(t.b).multiply(t.c);
            Rational expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void compareImplementationsMultiply_int() {
        initialize();
        System.out.println("\t\tcomparing multiply(int) implementations...");

        long totalTime = 0;
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            long time = System.nanoTime();
            multiply_int_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            long time = System.nanoTime();
            p.a.multiply(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational invert_simplest(@NotNull Rational r) {
        return of(r.getDenominator(), r.getNumerator());
    }

    private static void propertiesInvert() {
        initialize();
        System.out.println("\t\ttesting invert() properties...");

        for (Rational r : take(LIMIT, filter(s -> s != ZERO, P.rationals()))) {
            Rational inverse = r.invert();
            inverse.validate();
            assertEquals(r.toString(), inverse, invert_simplest(r));
            assertEquals(r.toString(), r, inverse.invert());
            assertTrue(r.toString(), r.multiply(inverse) == ONE);
            assertTrue(r.toString(), inverse != ZERO);
        }

        for (Rational r : take(LIMIT, filter(s -> s != ZERO && s.abs() != ONE, P.rationals()))) {
            Rational inverseR = r.invert();
            assertTrue(r.toString(), !r.equals(inverseR));
        }
    }

    private static void compareImplementationsInvert() {
        initialize();
        System.out.println("\t\tcomparing invert() implementations...");

        long totalTime = 0;
        Iterable<Rational> rs = filter(r -> r != ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            long time = System.nanoTime();
            invert_simplest(r);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Rational r : take(LIMIT, rs)) {
            long time = System.nanoTime();
            r.invert();
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational divide_Rational_simplest(@NotNull Rational a, @NotNull Rational b) {
        return of(a.getNumerator().multiply(b.getDenominator()), a.getDenominator().multiply(b.getNumerator()));
    }

    private static void propertiesDivide_Rational() {
        initialize();
        System.out.println("\t\ttesting divide(Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, filter(q -> q.b != ZERO, P.pairs(P.rationals())))) {
            Rational quotient = p.a.divide(p.b);
            quotient.validate();
            assertEquals(p.toString(), quotient, divide_Rational_simplest(p.a, p.b));
            assertEquals(p.toString(), p.a, quotient.multiply(p.b));
            assertEquals(p.toString(), quotient, p.a.multiply(p.b.invert()));
        }

        Iterable<Pair<Rational, Rational>> ps = filter(p -> p.a != ZERO && p.b != ZERO, P.pairs(P.rationals()));
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.divide(p.b), p.b.divide(p.a).invert());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.divide(ONE), r);
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), ONE.divide(r), r.invert());
            assertTrue(r.toString(), r.divide(r) == ONE);
            assertEquals(r.toString(), ZERO.divide(r), ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            try {
                r.divide(ZERO);
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void compareImplementationsDivide_Rational() {
        initialize();
        System.out.println("\t\tcomparing divide(Rational) implementations...");

        long totalTime = 0;
        Iterable<Pair<Rational, Rational>> ps = filter(p -> p.b != ZERO, P.pairs(P.rationals()));
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            divide_Rational_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            p.a.divide(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational divide_BigInteger_simplest(@NotNull Rational a, @NotNull BigInteger b) {
        return of(a.getNumerator(), a.getDenominator().multiply(b));
    }

    private static void propertiesDivide_BigInteger() {
        initialize();
        System.out.println("\t\ttesting divide(BigInteger) properties...");

        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(
                P.rationals(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            Rational quotient = p.a.divide(p.b);
            quotient.validate();
            assertEquals(p.toString(), quotient, divide_BigInteger_simplest(p.a, p.b));
            assertEquals(p.toString(), p.a, quotient.multiply(p.b));
        }

        ps = P.pairs(
                filter(r -> r != ZERO, P.rationals()),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.divide(p.b), of(p.b).divide(p.a).invert());
        }
        
        for (BigInteger i : take(LIMIT, filter(j -> !j.equals(BigInteger.ZERO), P.bigIntegers()))) {
            assertEquals(i.toString(), ONE.divide(i), of(i).invert());
            assertEquals(i.toString(), of(i).divide(i), ONE);
        }
        
        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.divide(BigInteger.ONE), r);      
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            try {
                r.divide(BigInteger.ZERO);
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void compareImplementationsDivide_BigInteger() {
        initialize();
        System.out.println("\t\tcomparing divide(BigInteger) implementations...");

        long totalTime = 0;
        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(
                P.rationals(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            divide_BigInteger_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            p.a.divide(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational divide_int_simplest(@NotNull Rational a, int b) {
        return of(a.getNumerator(), a.getDenominator().multiply(BigInteger.valueOf(b)));
    }

    private static void propertiesDivide_int() {
        initialize();
        System.out.println("\t\ttesting divide(int) properties...");

        Iterable<Pair<Rational, Integer>> ps = P.pairs(P.rationals(), filter(i -> i != 0, P.integers()));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            Rational quotient = p.a.divide(p.b);
            quotient.validate();
            assertEquals(p.toString(), quotient, divide_int_simplest(p.a, p.b));
            assertEquals(p.toString(), p.a, quotient.multiply(p.b));
        }

        ps = P.pairs(filter(r -> r != ZERO, P.rationals()), filter(i -> i != 0, P.integers()));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assertEquals(p.toString(), p.a.divide(p.b), of(p.b).divide(p.a).invert());
        }

        for (int i : take(LIMIT, filter(j -> j != 0, P.integers()))) {
            assertEquals(Integer.toString(i), ONE.divide(i), of(i).invert());
            assertEquals(Integer.toString(i), of(i).divide(i), ONE);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.divide(1), r);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            try {
                r.divide(0);
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void compareImplementationsDivide_int() {
        initialize();
        System.out.println("\t\tcomparing divide(int) implementations...");

        long totalTime = 0;
        Iterable<Pair<Rational, Integer>> ps = P.pairs(P.rationals(), filter(i -> i != 0, P.integers()));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            divide_int_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            p.a.divide(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational shiftLeft_simplest(@NotNull Rational r, int bits) {
        if (bits < 0) {
            return r.divide(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return r.multiply(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private static void propertiesShiftLeft() {
        initialize();
        System.out.println("\t\ttesting shiftLeft(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric();
        }
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            Rational shifted = p.a.shiftLeft(p.b);
            shifted.validate();
            assertEquals(p.toString(), shifted, shiftLeft_simplest(p.a, p.b));
            assertEquals(p.toString(), p.a.signum(), shifted.signum());
            assertEquals(p.toString(), p.a.negate().shiftLeft(p.b), shifted.negate());
            assertEquals(p.toString(), shifted, p.a.shiftRight(-p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.shiftLeft(0), r);
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = ((QBarRandomProvider) P).naturalIntegersGeometric();
        }
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            Rational shifted = p.a.shiftLeft(p.b);
            assertEquals(p.toString(), shifted, p.a.multiply(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private static void compareImplementationsShiftLeft() {
        initialize();
        System.out.println("\t\tcomparing shiftLeft(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric();
        }
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            long time = System.nanoTime();
            shiftLeft_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            long time = System.nanoTime();
            p.a.shiftLeft(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational shiftRight_simplest(@NotNull Rational r, int bits) {
        if (bits < 0) {
            return r.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return r.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private static void propertiesShiftRight() {
        initialize();
        System.out.println("\t\ttesting shiftRight(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric();
        }
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            Rational shifted = p.a.shiftRight(p.b);
            shifted.validate();
            assertEquals(p.toString(), shifted, shiftRight_simplest(p.a, p.b));
            assertEquals(p.toString(), p.a.signum(), shifted.signum());
            assertEquals(p.toString(), p.a.negate().shiftRight(p.b), shifted.negate());
            assertEquals(p.toString(), shifted, p.a.shiftLeft(-p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.shiftRight(0), r);
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = ((QBarRandomProvider) P).naturalIntegersGeometric();
        }
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            Rational shifted = p.a.shiftRight(p.b);
            assertEquals(p.toString(), shifted, p.a.divide(BigInteger.ONE.shiftLeft(p.b)));
        }
    }

    private static void compareImplementationsShiftRight() {
        initialize();
        System.out.println("\t\tcomparing shiftRight(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric();
        }
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            long time = System.nanoTime();
            shiftRight_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), is))) {
            long time = System.nanoTime();
            p.a.shiftRight(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational sum_alt(@NotNull Iterable<Rational> xs) {
        List<Rational> denominatorSorted = sort(
                (x, y) -> {
                    Ordering o = compare(x.getDenominator(), y.getDenominator());
                    if (o == EQ) {
                        o = compare(x.getNumerator().abs(), y.getNumerator().abs());
                    }
                    if (o == EQ) {
                        o = compare(x.getNumerator().signum(), y.getNumerator().signum());
                    }
                    return o.toInt();
                },
                xs
        );
        Iterable<List<Rational>> denominatorGrouped = group(
                p -> p.a.getDenominator().equals(p.b.getDenominator()),
                denominatorSorted
        );
        Rational sum = ZERO;
        for (List<Rational> group : denominatorGrouped) {
            BigInteger numeratorSum = sumBigInteger(map(Rational::getNumerator, group));
            sum = sum.add(of(numeratorSum, head(group).getDenominator()));
        }
        return sum;
    }

    private static void propertiesSum() {
        initialize();
        System.out.println("\t\ttesting sum(Iterable<Rational>) properties...");

        propertiesFoldHelper(LIMIT, P.getWheelsProvider(), P.rationals(), Rational::add, Rational::sum, r -> {}, true);

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            assertEquals(rs.toString(), sum(rs), sum_alt(rs));
        }
    }

    private static void compareImplementationsSum() {
        initialize();
        System.out.println("\t\tcomparing sum(Iterable<Rational>) implementations...");

        long totalTime = 0;
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            long time = System.nanoTime();
            sum_alt(rs);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\talt: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            long time = System.nanoTime();
            sum(rs);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational product_simplest(@NotNull Iterable<Rational> xs) {
        return foldl(Rational::multiply, ONE, xs);
    }

    private static void propertiesProduct() {
        initialize();
        System.out.println("\t\ttesting product(Iterable<Rational>) properties...");

        propertiesFoldHelper(LIMIT, P.getWheelsProvider(), P.rationals(), Rational::multiply, Rational::product, r -> {}, true);

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            assertEquals(rs.toString(), product(rs), product_simplest(rs));
        }
    }

    private static void compareImplementationsProduct() {
        initialize();
        System.out.println("\t\tcomparing product(Iterable<Rational>) implementations...");

        long totalTime = 0;
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            long time = System.nanoTime();
            product_simplest(rs);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            long time = System.nanoTime();
            product(rs);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static void propertiesDelta() {
        initialize();
        System.out.println("\t\ttesting delta(Iterable<Rational>) properties...");

        propertiesDeltaHelper(
                LIMIT,
                P.getWheelsProvider(),
                P.rationals(),
                Rational::negate,
                Rational::subtract,
                Rational::delta,
                r -> {}
        );
    }

    private static void propertiesHarmonicNumber() {
        initialize();
        System.out.println("\t\ttesting harmonicNumber(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.positiveIntegers();
        } else {
            is = P.withScale(100).positiveIntegersGeometric();
        }
        for (int i : take(SMALL_LIMIT, is)) {
            Rational h = harmonicNumber(i);
            h.validate();
            assertTrue(Integer.toString(i), ge(h, ONE));
        }

        is = map(i -> i + 1, is);
        for (int i : take(SMALL_LIMIT, is)) {
            Rational h = harmonicNumber(i);
            assertTrue(Integer.toString(i), gt(h, harmonicNumber(i - 1)));
            assertFalse(Integer.toString(i), h.isInteger());
        }

        for (int i : take(LIMIT, P.rangeDown(0))) {
            try {
                harmonicNumber(i);
                fail(Integer.toString(i));
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull Rational pow_simplest(@NotNull Rational a, int p) {
        Rational result = product(replicate(Math.abs(p), a));
        return p < 0 ? result.invert() : result;
    }

    private static void propertiesPow() {
        initialize();
        System.out.println("\t\ttesting pow(int) properties...");

        Iterable<Integer> exps;
        if (P instanceof QBarExhaustiveProvider) {
            exps = P.integers();
        } else {
            exps = P.withScale(20).integersGeometric();
        }

        Iterable<Pair<Rational, Integer>> ps = filter(p -> p.b >= 0 || p.a != ZERO, P.pairs(P.rationals(), exps));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            Rational r = p.a.pow(p.b);
            r.validate();
            assertEquals(p.toString(), r, pow_simplest(p.a, p.b));
        }

        ps = P.pairs(filter(r -> r != ZERO, P.rationals()), exps);
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            Rational r = p.a.pow(p.b);
            assertEquals(p.toString(), r, p.a.pow(-p.b).invert());
            assertEquals(p.toString(), r, p.a.invert().pow(-p.b));
        }

        Iterable<Integer> pexps;
        if (P instanceof QBarExhaustiveProvider) {
            pexps = P.positiveIntegers();
        } else {
            pexps = P.withScale(20).positiveIntegersGeometric();
        }
        for (int i : take(LIMIT, pexps)) {
            assertTrue(Integer.toString(i), ZERO.pow(i) == ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r.toString(), r.pow(0) == ONE);
            assertEquals(r.toString(), r.pow(1), r);
            assertEquals(r.toString(), r.pow(2), r.multiply(r));
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), r.pow(-1), r.invert());
        }

        Iterable<Triple<Rational, Integer, Integer>> ts1 = filter(
                p -> p.b >= 0 && p.c >= 0 || p.a != ZERO,
                P.triples(P.rationals(), exps, exps)
        );
        for (Triple<Rational, Integer, Integer> t : take(LIMIT, ts1)) {
            Rational expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            Rational expression2 = t.a.pow(t.b + t.c);
            assertEquals(t.toString(), expression1, expression2);
        }

        ts1 = filter(
                t -> t.a != ZERO || t.c == 0 && t.b >= 0,
                P.triples(P.rationals(), exps, exps)
        );
        for (Triple<Rational, Integer, Integer> t : take(LIMIT, ts1)) {
            Rational expression1 = t.a.pow(t.b).divide(t.a.pow(t.c));
            Rational expression2 = t.a.pow(t.b - t.c);
            assertEquals(t.toString(), expression1, expression2);
        }

        ts1 = filter(
                t -> t.a != ZERO || t.b >= 0 && t.c >= 0,
                P.triples(P.rationals(), exps, exps)
        );
        for (Triple<Rational, Integer, Integer> t : take(LIMIT, ts1)) {
            Rational expression5 = t.a.pow(t.b).pow(t.c);
            Rational expression6 = t.a.pow(t.b * t.c);
            assertEquals(t.toString(), expression5, expression6);
        }

        Iterable<Triple<Rational, Rational, Integer>> ts2 = filter(
                t -> t.a != ZERO && t.b != ZERO || t.c >= 0,
                P.triples(P.rationals(), P.rationals(), exps)
        );
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, ts2)) {
            Rational expression1 = t.a.multiply(t.b).pow(t.c);
            Rational expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }

        ts2 = filter(
                t -> t.a != ZERO || t.c >= 0,
                P.triples(P.rationals(), filter(r -> r != ZERO, P.rationals()), exps)
        );
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, ts2)) {
            Rational expression1 = t.a.divide(t.b).pow(t.c);
            Rational expression2 = t.a.pow(t.c).divide(t.b.pow(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }

        for (int i : take(LIMIT, P.negativeIntegers())) {
            try {
                ZERO.pow(i);
                fail(Integer.toString(i));
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void compareImplementationsPow() {
        initialize();
        System.out.println("\t\tcomparing pow(int) implementations...");

        long totalTime = 0;
        Iterable<Integer> exps;
        if (P instanceof QBarExhaustiveProvider) {
            exps = P.integers();
        } else {
            exps = P.withScale(20).integersGeometric();
        }
        Iterable<Pair<Rational, Integer>> ps = filter(p -> p.b >= 0 || p.a != ZERO, P.pairs(P.rationals(), exps));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            pow_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            p.a.pow(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static void propertiesFloor() {
        initialize();
        System.out.println("\t\ttesting floor() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            BigInteger floor = r.floor();
            assertTrue(r.toString(), le(of(floor), r));
            assertTrue(r.toString(), le(r.subtract(of(floor)), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).floor(), i);
        }
    }

    private static void propertiesCeiling() {
        initialize();
        System.out.println("\t\ttesting ceiling() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            BigInteger ceiling = r.ceiling();
            assertTrue(r.toString(), ge(of(ceiling), r));
            assertTrue(r.toString(), le(of(ceiling).subtract(r), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).ceiling(), i);
        }
    }

    private static void propertiesFractionalPart() {
        initialize();
        System.out.println("\t\ttesting fractionalPart() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational fractionalPart = r.fractionalPart();
            fractionalPart.validate();
            assertTrue(r.toString(), ge(fractionalPart, ZERO));
            assertTrue(r.toString(), lt(fractionalPart, ONE));
            assertEquals(r.toString(), of(r.floor()).add(fractionalPart), r);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).fractionalPart(), ZERO);
        }
    }

    private static void propertiesRoundToDenominator() {
        initialize();
        System.out.println("\t\ttesting roundToDenominator(BigInteger, RoundingMode) properties...");

        Iterable<Triple<Rational, BigInteger, RoundingMode>> ts = filter(
                p -> p.c != RoundingMode.UNNECESSARY || p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO),
                P.triples(P.rationals(), P.positiveBigIntegers(), P.roundingModes())
        );
        for (Triple<Rational, BigInteger, RoundingMode> t : take(LIMIT, ts)) {
            Rational rounded = t.a.roundToDenominator(t.b, t.c);
            rounded.validate();
            assertEquals(t.toString(), t.b.mod(rounded.getDenominator()), BigInteger.ZERO);
            assertTrue(t.toString(), rounded == ZERO || rounded.signum() == t.a.signum());
            assertTrue(t.toString(), lt(t.a.subtract(rounded).abs(), of(BigInteger.ONE, t.b)));
        }

        Iterable<Pair<Rational, RoundingMode>> ps1 = filter(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.isInteger(),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps1)) {
            Rational rounded = p.a.roundToDenominator(BigInteger.ONE, p.b);
            assertEquals(p.toString(), rounded.getNumerator(), p.a.bigIntegerValue(p.b));
            assertEquals(p.toString(), rounded.getDenominator(), BigInteger.ONE);
        }

        Iterable<Pair<Rational, BigInteger>> ps2 = filter(
                p -> p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO),
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assertTrue(p.toString(), p.a.roundToDenominator(p.b, RoundingMode.UNNECESSARY).equals(p.a));
        }

        ps2 = P.pairs(P.rationals(), P.positiveBigIntegers());
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assertTrue(p.toString(), le(p.a.roundToDenominator(p.b, RoundingMode.FLOOR), p.a));
            assertTrue(p.toString(), ge(p.a.roundToDenominator(p.b, RoundingMode.CEILING), p.a));
            assertTrue(p.toString(), le(p.a.roundToDenominator(p.b, RoundingMode.DOWN).abs(), p.a.abs()));
            assertTrue(p.toString(), ge(p.a.roundToDenominator(p.b, RoundingMode.UP).abs(), p.a.abs()));
            assertTrue(
                    p.toString(),
                    le(
                            p.a.subtract(p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN)).abs(),
                            of(p.b).shiftLeft(1).invert()
                    )
            );
            assertTrue(
                    p.toString(),
                    le(
                            p.a.subtract(p.a.roundToDenominator(p.b, RoundingMode.HALF_UP)).abs(),
                            of(p.b).shiftLeft(1).invert()
                    )
            );
            assertTrue(
                    p.toString(),
                    le(
                            p.a.subtract(p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN)).abs(),
                            of(p.b).shiftLeft(1).invert()
                    )
            );
        }

        ps2 = filter(
                p -> lt(p.a.abs().multiply(p.b).fractionalPart(), of(1, 2)),
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );

        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN),
                    p.a.roundToDenominator(p.b, RoundingMode.DOWN)
            );
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_UP),
                    p.a.roundToDenominator(p.b, RoundingMode.DOWN)
            );
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN),
                    p.a.roundToDenominator(p.b, RoundingMode.DOWN)
            );
        }

        ps2 = filter(
                p -> gt(p.a.abs().multiply(p.b).fractionalPart(), of(1, 2)),
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );

        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN),
                    p.a.roundToDenominator(p.b, RoundingMode.UP)
            );
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_UP),
                    p.a.roundToDenominator(p.b, RoundingMode.UP)
            );
            assertEquals(
                    p.toString(),
                    p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN),
                    p.a.roundToDenominator(p.b, RoundingMode.UP)
            );
        }

        Iterable<Rational> rs = filter(r -> !r.getDenominator().testBit(0), P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            BigInteger denominator = r.getDenominator().shiftRight(1);
            assertEquals(r.toString(), r.abs().multiply(denominator).fractionalPart(), of(1, 2));
            Rational hd = r.roundToDenominator(denominator, RoundingMode.HALF_DOWN);
            assertEquals(r.toString(), hd, r.roundToDenominator(denominator, RoundingMode.DOWN));
            Rational hu = r.roundToDenominator(denominator, RoundingMode.HALF_UP);
            assertEquals(r.toString(), hu, r.roundToDenominator(denominator, RoundingMode.UP));
            Rational he = r.roundToDenominator(denominator, RoundingMode.HALF_EVEN);
            assertEquals(r.toString(), he.multiply(denominator).getNumerator().testBit(0), false);
        }

        Iterable<Triple<Rational, BigInteger, RoundingMode>> failTs = P.triples(
                P.rationals(),
                map(BigInteger::negate, P.naturalBigIntegers()),
                P.roundingModes()
        );
        for (Triple<Rational, BigInteger, RoundingMode> t : take(LIMIT, failTs)) {
            try {
                t.a.roundToDenominator(t.b, t.c);
                fail(t.toString());
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Rational, BigInteger>> failPs = filter(
                p -> !p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO),
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, failPs)) {
            try {
                p.a.roundToDenominator(p.b, RoundingMode.UNNECESSARY);
                fail(p.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesContinuedFraction() {
        initialize();
        System.out.println("\t\ttesting continuedFraction() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            List<BigInteger> continuedFraction = r.continuedFraction();
            assertFalse(r.toString(), continuedFraction.isEmpty());
            assertTrue(r.toString(), all(i -> i != null, continuedFraction));
            assertTrue(r.toString(), all(i -> i.signum() == 1, tail(continuedFraction)));
            assertEquals(r.toString(), r, fromContinuedFraction(continuedFraction));
        }

        for (Rational r : take(LIMIT, filter(s -> !s.isInteger(), P.rationals()))) {
            List<BigInteger> continuedFraction = r.continuedFraction();
            assertTrue(r.toString(), gt(last(continuedFraction), BigInteger.ONE));
        }
    }

    private static void propertiesFromContinuedFraction() {
        initialize();
        System.out.println("\t\ttesting fromContinuedFraction(List<BigInteger>) properties...");

        Iterable<List<BigInteger>> iss = map(
                p -> toList(cons(p.a, p.b)),
                (Iterable<Pair<BigInteger, List<BigInteger>>>) P.pairs(
                        P.bigIntegers(),
                        P.lists(P.positiveBigIntegers())
                )
        );
        for (List<BigInteger> is : take(LIMIT, iss)) {
            Rational r = fromContinuedFraction(is);
            r.validate();
        }

        for (List<BigInteger> is : take(LIMIT, filter(js -> !last(js).equals(BigInteger.ONE), iss))) {
            Rational r = fromContinuedFraction(is);
            assertEquals(is.toString(), r.continuedFraction(), is);
        }

        Iterable<List<BigInteger>> failIss = filter(
                is -> any(i -> i.signum() != 1, tail(is)),
                P.listsAtLeast(1, P.bigIntegers())
        );
        for (List<BigInteger> is : take(LIMIT, failIss)) {
            try {
                fromContinuedFraction(is);
                fail(is.toString());
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void propertiesConvergents() {
        initialize();
        System.out.println("\t\ttesting convergents() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            List<Rational> convergents = toList(r.convergents());
            convergents.forEach(Rational::validate);
            assertFalse(r.toString(), convergents.isEmpty());
            assertTrue(r.toString(), all(s -> s != null, convergents));
            assertEquals(r.toString(), head(convergents), of(r.floor()));
            assertEquals(r.toString(), last(convergents), r);
            assertTrue(r.toString(), zigzagging(convergents));
        }
    }

    private static @NotNull <T> Pair<List<T>, List<T>> minimize(@NotNull List<T> a, @NotNull List<T> b) {
        List<T> oldA = new ArrayList<>();
        List<T> oldB = new ArrayList<>();
        while (!a.equals(oldA) || !b.equals(oldB)) {
            int longestCommonSuffixLength = 0;
            for (int i = 0; i < Math.min(a.size(), b.size()); i++) {
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

    private static void propertiesPositionalNotation() {
        initialize();
        System.out.println("\t\ttesting positionalNotation(BigInteger) properties...");

        Iterable<Pair<Rational, BigInteger>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsSquareRootOrder(
                    cons(ZERO, P.positiveRationals()),
                    P.rangeUp(BigInteger.valueOf(2))
            );
        } else {
            ps = P.pairs(
                    cons(ZERO, ((QBarRandomProvider) P).withScale(8).positiveRationals()),
                    map(i -> BigInteger.valueOf(i + 2), P.withScale(20).naturalIntegersGeometric())
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> pn = p.a.positionalNotation(p.b);
            for (List<BigInteger> is : Arrays.asList(pn.a, pn.b, pn.c)) {
                assertTrue(p.toString(), all(i -> i != null && i.signum() != -1 && lt(i, p.b), is));
            }
            assertTrue(p.toString(), pn.a.isEmpty() || !head(pn.a).equals(BigInteger.ZERO));
            assertFalse(p.toString(), pn.c.isEmpty());
            assertNotEquals(p.toString(), pn.c, Collections.singletonList(p.b.subtract(BigInteger.ONE)));
            Pair<List<BigInteger>, List<BigInteger>> minimized = minimize(pn.b, pn.c);
            assertEquals(p.toString(), minimized.a, pn.b);
            assertEquals(p.toString(), minimized.b, pn.c);
            assertEquals(p.toString(), fromPositionalNotation(p.b, pn.a, pn.b, pn.c), p.a);
            assertEquals(
                    p.toString(),
                    pn.c.equals(Collections.singletonList(BigInteger.ZERO)),
                    p.a.hasTerminatingBaseExpansion(p.b)
            );
        }

        Iterable<Pair<Rational, BigInteger>> psFail = P.pairs(P.negativeRationals(), P.rangeUp(BigInteger.valueOf(2)));
        for (Pair<Rational, BigInteger> p : take(LIMIT, psFail)) {
            try {
                p.a.positionalNotation(p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.rangeDown(BigInteger.ONE)))) {
            try {
                p.a.positionalNotation(p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void propertiesFromPositionalNotation() {
        initialize();
        System.out.println(
                "\t\ttesting fromPositionalNotation(BigInteger, List<BigInteger>, List<BigInteger>," +
                " List<BigInteger>) properties...");

        Iterable<BigInteger> bases;
        if (P instanceof QBarExhaustiveProvider) {
            bases = P.rangeUp(BigInteger.valueOf(2));
        } else {
            bases = map(i -> BigInteger.valueOf(i + 2), P.withScale(20).naturalIntegersGeometric());
        }
        Iterable<Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>>> ps = P.dependentPairs(
                bases,
                b -> filter(
                        t -> !t.c.isEmpty(),
                        P.triples(P.lists(P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE))))
                )
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, ps)) {
            Rational r = fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
            r.validate();
            assertNotEquals(p.toString(), r.signum(), -1);
        }

        ps = filter(
                p -> {
                    if (!p.b.a.isEmpty() && head(p.b.a).equals(BigInteger.ZERO)) return false;
                    Pair<List<BigInteger>, List<BigInteger>> minimized = minimize(p.b.b, p.b.c);
                    //noinspection SimplifiableIfStatement
                    if (!minimized.a.equals(p.b.b) || !minimized.b.equals(p.b.c)) return false;
                    return !p.b.c.equals(Collections.singletonList(p.a.subtract(BigInteger.ONE)));
                },
                ps
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, ps)) {
            Rational r = fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
            assertEquals(p.toString(), r.positionalNotation(p.a), p.b);
        }

        Iterable<Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>>> psFail = P.pairs(
                P.rangeDown(BigInteger.ONE),
                filter(t -> !t.c.isEmpty(), P.triples(P.lists(P.bigIntegers())))
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, psFail)) {
            try {
                fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = P.dependentPairs(
                P.rangeUp(BigInteger.valueOf(2)),
                b -> filter(
                        t -> !t.c.isEmpty() && any(x -> x.signum() == -1 || ge(x, b), t.a),
                        P.triples(P.lists(P.bigIntegers()))
                )
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, psFail)) {
            try {
                fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = P.dependentPairs(
                P.rangeUp(BigInteger.valueOf(2)),
                b -> filter(
                        t -> !t.c.isEmpty() && any(x -> x.signum() == -1 || ge(x, b), t.b),
                        P.triples(P.lists(P.bigIntegers()))
                )
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, psFail)) {
            try {
                fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        psFail = P.dependentPairs(
                P.rangeUp(BigInteger.valueOf(2)),
                b -> filter(
                        t -> !t.c.isEmpty() && any(x -> x.signum() == -1 || ge(x, b), t.c),
                        P.triples(P.lists(P.bigIntegers()))
                )
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, psFail)) {
            try {
                fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<Pair<BigInteger, Pair<List<BigInteger>, List<BigInteger>>>> psFail2 = P.pairs(
                P.rangeDown(BigInteger.ONE),
                P.pairs(P.lists(P.bigIntegers()))
        );
        for (Pair<BigInteger, Pair<List<BigInteger>, List<BigInteger>>> p : take(LIMIT, psFail2)) {
            try {
                fromPositionalNotation(p.a, p.b.a, p.b.b, Collections.emptyList());
                fail(p.toString());
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

    private static void propertiesDigits() {
        initialize();
        System.out.println("\t\ttesting digits(BigInteger) properties...");

        Iterable<Pair<Rational, BigInteger>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsSquareRootOrder(
                    cons(ZERO, P.positiveRationals()),
                    P.rangeUp(BigInteger.valueOf(2))
            );
        } else {
            ps = P.pairs(
                    cons(ZERO, P.positiveRationals()),
                    map(i -> BigInteger.valueOf(i + 2), P.withScale(20).naturalIntegersGeometric())
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            Pair<List<BigInteger>, Iterable<BigInteger>> digits = p.a.digits(p.b);
            assertTrue(p.toString(), digits.a.isEmpty() || !head(digits.a).equals(BigInteger.ZERO));
            assertTrue(p.toString(), all(x -> x.signum() != -1 && lt(x, p.b), digits.a));
            assertEquals(p.toString(), MathUtils.fromBigEndianDigits(p.b, digits.a), p.a.floor());
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, filter(q -> q.a.hasTerminatingBaseExpansion(q.b), ps))) {
            Pair<List<BigInteger>, Iterable<BigInteger>> digits = p.a.digits(p.b);
            toList(digits.b);
        }

        if (!(P instanceof QBarExhaustiveProvider)) {
            ps = P.pairs(
                    cons(ZERO, ((QBarRandomProvider) P).withScale(8).positiveRationals()),
                    map(i -> BigInteger.valueOf(i + 2), P.withScale(20).naturalIntegersGeometric())
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            Pair<List<BigInteger>, Iterable<BigInteger>> digits = p.a.digits(p.b);
            Pair<List<BigInteger>, Iterable<BigInteger>> alt = digits_alt(p.a, p.b);
            assertEquals(p.toString(), digits.a, alt.a);
            aeqit(p.toString(), take(100, digits.b), take(100, alt.b));
        }

        Iterable<Pair<Rational, BigInteger>> psFail = P.pairs(P.negativeRationals(), P.rangeUp(BigInteger.valueOf(2)));
        for (Pair<Rational, BigInteger> p : take(LIMIT, psFail)) {
            try {
                p.a.digits(p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.rangeDown(BigInteger.ONE)))) {
            try {
                p.a.digits(p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void compareImplementationsDigits() {
        initialize();
        System.out.println("\t\tcomparing digits(BigInteger) implementations...");

        long totalTime = 0;
        Iterable<Pair<Rational, BigInteger>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsSquareRootOrder(
                    cons(ZERO, P.positiveRationals()),
                    P.rangeUp(BigInteger.valueOf(2))
            );
        } else {
            ps = P.pairs(
                    cons(ZERO, ((QBarRandomProvider) P).withScale(8).positiveRationals()),
                    map(i -> BigInteger.valueOf(i + 2), P.withScale(20).naturalIntegersGeometric())
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            toList(take(20, digits_alt(p.a, p.b).b));
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            toList(take(20, p.a.digits(p.b).b));
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static void propertiesToStringBase_BigInteger() {
        initialize();
        System.out.println("\t\ttesting toStringBase(BigInteger) properties...");

        Iterable<Pair<Rational, BigInteger>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsSquareRootOrder(P.rationals(), P.rangeUp(BigInteger.valueOf(2)));
        } else {
            ps = P.pairs(
                    P.rationals(),
                    map(i -> BigInteger.valueOf(i + 2), P.withScale(20).naturalIntegersGeometric())
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, filter(q -> q.a.hasTerminatingBaseExpansion(q.b), ps))) {
            String s = p.a.toStringBase(p.b);
            assertEquals(p.toString(), fromStringBase(p.b, s), p.a);
        }

        String chars = charsToString(concat(Arrays.asList(fromString("-."), range('0', '9'), range('A', 'Z'))));
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsSquareRootOrder(
                    P.rationals(),
                    P.range(BigInteger.valueOf(2), BigInteger.valueOf(36))
            );
        } else {
            ps = P.pairs(P.rationals(), P.range(BigInteger.valueOf(2), BigInteger.valueOf(36)));
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, filter(q -> q.a.hasTerminatingBaseExpansion(q.b), ps))) {
            String s = p.a.toStringBase(p.b);
            assertTrue(p.toString(), all(c -> elem(c, chars), s));
        }

        String chars2 = charsToString(concat(fromString("-.()"), range('0', '9')));
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairsSquareRootOrder(P.rationals(), P.rangeUp(BigInteger.valueOf(37)));
        } else {
            ps = P.pairs(
                    P.rationals(),
                    map(i -> BigInteger.valueOf(i + 37), P.withScale(20).naturalIntegersGeometric())
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, filter(q -> q.a.hasTerminatingBaseExpansion(q.b), ps))) {
            String s = p.a.toStringBase(p.b);
            assertTrue(p.toString(), all(c -> elem(c, chars2), s));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.rangeDown(BigInteger.ONE)))) {
            try {
                p.a.toStringBase(p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<Pair<Rational, BigInteger>> psFail;
        if (P instanceof QBarExhaustiveProvider) {
            psFail = P.pairs(P.rationals(), P.rangeUp(BigInteger.valueOf(2)));
        } else {
            psFail = P.pairs(
                    P.rationals(),
                    map(i -> BigInteger.valueOf(i + 2), P.withScale(20).naturalIntegersGeometric())
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, filter(q -> !q.a.hasTerminatingBaseExpansion(q.b), psFail))) {
            try {
                p.a.toStringBase(p.b);
                fail(p.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesToStringBase_BigInteger_int() {
        initialize();
        System.out.println("\t\ttesting toStringBase(BigInteger, int) properties...");

        Iterable<Pair<Rational, Pair<BigInteger, Integer>>> ps;
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<BigInteger, Integer>>) P.pairs(P.rangeUp(BigInteger.valueOf(2)), P.integers())
            );
        } else {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<BigInteger, Integer>>) P.pairs(
                            map(i -> BigInteger.valueOf(i + 2), P.withScale(20).naturalIntegersGeometric()),
                            P.withScale(20).integersGeometric()
                    )
            );
        }
        for (Pair<Rational, Pair<BigInteger, Integer>> p : take(LIMIT, ps)) {
            String s = p.a.toStringBase(p.b.a, p.b.b);
            boolean ellipsis = s.endsWith("...");
            if (ellipsis) s = take(s.length() - 3, s);
            Rational error = fromStringBase(p.b.a, s).subtract(p.a).abs();
            assertTrue(p.toString(), lt(error, of(p.b.a).pow(-p.b.b)));
            if (ellipsis) {
                assertTrue(p.toString(), error != ZERO);
            }
        }

        String chars = charsToString(concat(Arrays.asList(fromString("-."), range('0', '9'), range('A', 'Z'))));
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<BigInteger, Integer>>) P.pairs(
                            P.range(BigInteger.valueOf(2), BigInteger.valueOf(36)),
                            P.integers()
                    )
            );
        } else {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<BigInteger, Integer>>) P.pairs(
                            P.range(BigInteger.valueOf(2), BigInteger.valueOf(36)),
                            P.withScale(20).integersGeometric()
                    )
            );
        }
        for (Pair<Rational, Pair<BigInteger, Integer>> p : take(LIMIT, ps)) {
            String s = p.a.toStringBase(p.b.a, p.b.b);
            assertTrue(p.toString(), all(c -> elem(c, chars), s));
        }

        String chars2 = charsToString(concat(fromString("-.()"), range('0', '9')));
        if (P instanceof QBarExhaustiveProvider) {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<BigInteger, Integer>>) P.pairs(P.rangeUp(BigInteger.valueOf(37)), P.integers())
            );
        } else {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<BigInteger, Integer>>) P.pairs(
                            map(i -> BigInteger.valueOf(i + 37), P.withScale(20).naturalIntegersGeometric()),
                            P.withScale(20).integersGeometric()
                    )
            );
        }
        for (Pair<Rational, Pair<BigInteger, Integer>> p : take(LIMIT, ps)) {
            String s = p.a.toStringBase(p.b.a, p.b.b);
            assertTrue(p.toString(), all(c -> elem(c, chars2), s));
        }

        Iterable<Triple<Rational, BigInteger, Integer>> tsFail = P.triples(
                P.rationals(),
                P.rangeDown(BigInteger.ONE),
                P.integers()
        );
        for (Triple<Rational, BigInteger, Integer> t : take(LIMIT, tsFail)) {
            try {
                t.a.toStringBase(t.b, t.c);
                fail(t.toString());
            } catch (IllegalArgumentException ignored) {}
        }
    }

    private static void propertiesFromStringBase() {
        initialize();
        System.out.println("\t\ttesting fromStringBase(BigInteger, String) properties...");

        Iterable<Pair<BigInteger, String>> ps = map(
                p -> new Pair<>(BigInteger.valueOf(p.a), p.b.toStringBase(BigInteger.valueOf(p.a))),
                filter(
                        q -> {
                            BigInteger b = BigInteger.valueOf(q.a);
                            try {
                                q.b.toStringBase(b);
                                return true;
                            } catch (ArithmeticException e) {
                                return false;
                            }
                        },
                        P.pairs(P.rangeUpGeometric(2), P.rationals())
                )
        );
        for (Pair<BigInteger, String> p : take(LIMIT, ps)) {
            Rational r = fromStringBase(p.a, p.b);
            r.validate();
            assertEquals(p.toString(), r.toStringBase(p.a), p.b);
        }

        for (BigInteger i : take(LIMIT, P.rangeUp(BigInteger.valueOf(2)))) {
            assertTrue(i.toString(), fromStringBase(i, "") == ZERO);
        }

        for (Pair<BigInteger, String> p : take(LIMIT, P.pairs(P.rangeDown(BigInteger.ONE), P.strings()))) {
            try {
                fromStringBase(p.a, p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        //improper String left untested
    }

    private static void propertiesCancelDenominators() {
        initialize();
        System.out.println("\t\ttesting cancelDenominators(List<Rational>) properties...");

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            List<BigInteger> canceled = cancelDenominators(rs);
            BigInteger gcd = foldl(BigInteger::gcd, BigInteger.ZERO, canceled);
            assertTrue(rs.toString(), gcd.equals(BigInteger.ZERO) || gcd.equals(BigInteger.ONE));
            assertEquals(rs.toString(), cancelDenominators(toList(map(Rational::of, canceled))), canceled);
            assertTrue(rs.toString(), equal(map(Rational::signum, rs), map(BigInteger::signum, canceled)));
            assertTrue(
                    rs.toString(),
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
            assertEquals(
                    p.toString(),
                    cancelDenominators(p.a),
                    cancelDenominators(toList(map(r -> r.multiply(p.b), p.a)))
            );
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            BigInteger canceled = head(cancelDenominators(Collections.singletonList(r)));
            assertTrue(r.toString(), le(canceled.abs(), BigInteger.ONE));
        }

        for (List<Rational> rs : take(LIMIT, P.listsWithElement(null, P.rationals()))) {
            try {
                cancelDenominators(rs);
                fail(rs.toString());
            } catch (NullPointerException ignored) {}
        }
    }

    private static void propertiesEquals() {
        initialize();
        System.out.println("\t\ttesting equals(Object) properties...");

        QBarTesting.propertiesEqualsHelper(LIMIT, P, QBarIterableProvider::rationals);
    }

    private static void propertiesHashCode() {
        initialize();
        System.out.println("\t\ttesting hashCode() properties...");

        QBarTesting.propertiesHashCodeHelper(LIMIT, P, QBarIterableProvider::rationals);
    }

    private static int compareTo_simplest(@NotNull Rational x, @NotNull Rational y) {
        return x.getNumerator().multiply(y.getDenominator()).compareTo(y.getNumerator().multiply(x.getDenominator()));
    }

    private static void propertiesCompareTo() {
        initialize();
        System.out.println("\t\ttesting compareTo(Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            int compare = p.a.compareTo(p.b);
            assertEquals(p.toString(), compare, compareTo_simplest(p.a, p.b));
            assertEquals(p.toString(), p.a.subtract(p.b).signum(), compare);
        }

        QBarTesting.propertiesCompareToHelper(LIMIT, P, QBarIterableProvider::rationals);
    }

    private static void compareImplementationsCompareTo() {
        initialize();
        System.out.println("\t\tcomparing compareTo(Rational) implementations...");

        long totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            long time = System.nanoTime();
            compareTo_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            long time = System.nanoTime();
            p.a.compareTo(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static void propertiesRead() {
        initialize();
        System.out.println("\t\ttesting read(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            read(s);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            Optional<Rational> or = read(r.toString());
            assertEquals(r.toString(), or.get(), r);
        }

        Iterable<Character> cs;
        if (P instanceof QBarExhaustiveProvider) {
            cs = fromString(RATIONAL_CHARS);
        } else {
            cs = ((QBarRandomProvider) P).uniformSample(RATIONAL_CHARS);
        }
        Iterable<String> ss = filter(s -> read(s).isPresent(), P.strings(cs));
        for (String s : take(LIMIT, ss)) {
            Optional<Rational> or = read(s);
            or.get().validate();
        }

        Pair<Iterable<String>, Iterable<String>> slashPartition = partition(s -> s.contains("/"), ss);
        for (String s : take(LIMIT, slashPartition.a)) {
            int slashIndex = s.indexOf('/');
            String left = s.substring(0, slashIndex);
            String right = s.substring(slashIndex + 1);
            assertTrue(s, Readers.readBigInteger(left).isPresent());
            assertTrue(s, Readers.readBigInteger(right).isPresent());
        }

        for (String s : take(LIMIT, slashPartition.b)) {
            assertTrue(s, Readers.readBigInteger(s).isPresent());
        }
    }

    private static void propertiesFindIn() {
        initialize();
        System.out.println("\t\ttesting findIn(String) properties...");

        propertiesFindInHelper(LIMIT, P.getWheelsProvider(), P.rationals(), Rational::read, Rational::findIn, r -> {});
    }

    private static void propertiesToString() {
        initialize();
        System.out.println("\t\ttesting toString() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            String s = r.toString();
            assertTrue(r.toString(), isSubsetOf(s, RATIONAL_CHARS));
            Optional<Rational> readR = read(s);
            assertTrue(r.toString(), readR.isPresent());
            assertEquals(r.toString(), readR.get(), r);
        }
    }
}
