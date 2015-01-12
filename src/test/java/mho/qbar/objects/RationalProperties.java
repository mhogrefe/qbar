package mho.qbar.objects;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.Combinatorics;
import mho.wheels.math.MathUtils;
import mho.wheels.misc.FloatingPointUtils;
import mho.wheels.misc.Readers;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

import static mho.wheels.iterables.IterableUtils.*;
import static mho.wheels.ordering.Ordering.*;
import static mho.qbar.objects.Rational.*;
import static org.junit.Assert.*;

public class RationalProperties {
    private static boolean USE_RANDOM;
    private static final String RATIONAL_CHARS = "-/0123456789";
    private static final int SMALL_LIMIT = 1000;
    private static int LIMIT;

    private static QBarIterableProvider P;

    private static void initialize() {
        if (USE_RANDOM) {
            P = new QBarRandomProvider(new Random(0x6af477d9a7e54fcaL));
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
            propertiesNegate();
            propertiesInvert();
            compareImplementationsInvert();
            propertiesAbs();
            propertiesSignum();
            propertiesAdd();
            compareImplementationsAdd();
            propertiesSubtract();
            propertiesMultiply_Rational();
            compareImplementationsMultiply_Rational();
            propertiesMultiply_BigInteger();
            compareImplementationsMultiply_BigInteger();
            propertiesMultiply_int();
            compareImplementationsMultiply_int();
            propertiesDivide_Rational();
            compareImplementationsDivide_Rational();
            propertiesDivide_BigInteger();
            compareImplementationsDivide_BigInteger();
            propertiesDivide_int();
            compareImplementationsDivide_int();
            propertiesSum();
            propertiesProduct();
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
            propertiesEquals();
            propertiesHashCode();
            propertiesCompareTo();
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
                p -> {
                    assert p.b != null;
                    return !p.b.equals(BigInteger.ZERO);
                },
                P.pairs(P.bigIntegers())
        );
        for (Pair<BigInteger, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational r = of(p.a, p.b);
            validate(r);
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
            assert p.a != null;
            assert p.b != null;
            Rational r = of(p.a, p.b);
            validate(r);
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
            assert p.a != null;
            assert p.b != null;
            Rational r = of(p.a, p.b);
            validate(r);
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
            validate(r);
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
            validate(r);
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
            validate(r);
            assertEquals(Integer.toString(i), r.getDenominator(), BigInteger.ONE);
            assertTrue(Integer.toString(i), ge(r.getNumerator(), minInt));
            assertTrue(Integer.toString(i), le(r.getNumerator(), maxInt));
        }
    }

    private static void propertiesOf_float() {
        initialize();
        System.out.println("\t\ttesting of(float) properties...");

        Iterable<Float> fs = filter(f -> Float.isFinite(f) && !Float.isNaN(f), P.floats());
        for (float f : take(LIMIT, fs)) {
            Rational r = of(f);
            assert r != null;
            validate(r);
            assertTrue(Float.toString(f), r.hasTerminatingBaseExpansion(BigInteger.valueOf(10)));
        }

        for (float f : take(LIMIT, P.ordinaryFloats())) {
            Rational r = of(f);
            assert r != null;
            aeq(Float.toString(f), f, r.floatValue());
            aeq(Float.toString(f), new BigDecimal(Float.toString(f)), r.bigDecimalValueExact());
        }
    }

    private static void propertiesOf_double() {
        initialize();
        System.out.println("\t\ttesting of(double) properties...");

        Iterable<Double> ds = filter(d -> Double.isFinite(d) && !Double.isNaN(d), P.doubles());
        for (double d : take(LIMIT, ds)) {
            Rational r = of(d);
            assert r != null;
            validate(r);
            assertTrue(Double.toString(d), r.hasTerminatingBaseExpansion(BigInteger.valueOf(10)));
        }

        for (double d : take(LIMIT, P.ordinaryDoubles())) {
            Rational r = of(d);
            assert r != null;
            aeq(Double.toString(d), d, r.doubleValue());
            aeq(Double.toString(d), new BigDecimal(Double.toString(d)), r.bigDecimalValueExact());
        }
    }

    private static void propertiesOfExact_float() {
        initialize();
        System.out.println("\t\ttesting ofExact(float) properties...");

        BigInteger denominatorLimit = BigInteger.ONE.shiftLeft(149);
        BigInteger numeratorLimit = BigInteger.ONE.shiftLeft(128).subtract(BigInteger.ONE.shiftLeft(104));
        Iterable<Float> fs = filter(f -> Float.isFinite(f) && !Float.isNaN(f), P.floats());
        for (float f : take(LIMIT, fs)) {
            Rational r = ofExact(f);
            assert r != null;
            validate(r);
            assertTrue(Float.toString(f), MathUtils.isAPowerOfTwo(r.getDenominator()));
            assertTrue(Float.toString(f), le(r.getDenominator(), denominatorLimit));
            assertTrue(Float.toString(f), le(r.getNumerator(), numeratorLimit));
        }

        for (float f : take(LIMIT, P.ordinaryFloats())) {
            Rational r = ofExact(f);
            assert r != null;
            aeq(Float.toString(f), f, r.floatValue());
        }
    }

    private static void propertiesOfExact_double() {
        initialize();
        System.out.println("\t\ttesting ofExact(double) properties...");

        BigInteger denominatorLimit = BigInteger.ONE.shiftLeft(1074);
        BigInteger numeratorLimit = BigInteger.ONE.shiftLeft(1024).subtract(BigInteger.ONE.shiftLeft(971));
        Iterable<Double> ds = filter(d -> Double.isFinite(d) && !Double.isNaN(d), P.doubles());
        for (double d : take(LIMIT, ds)) {
            Rational r = ofExact(d);
            assert r != null;
            validate(r);
            assertTrue(Double.toString(d), MathUtils.isAPowerOfTwo(r.getDenominator()));
            assertTrue(Double.toString(d), le(r.getDenominator(), denominatorLimit));
            assertTrue(Double.toString(d), le(r.getNumerator(), numeratorLimit));
        }

        for (double d : take(LIMIT, P.ordinaryDoubles())) {
            Rational r = ofExact(d);
            assert r != null;
            aeq(Double.toString(d), d, r.doubleValue());
        }
    }

    private static void propertiesOf_BigDecimal() {
        initialize();
        System.out.println("\t\ttesting of(BigDecimal) properties...");

        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Rational r = of(bd);
            validate(r);
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
            assertTrue(of(i).isInteger());
        }
    }

    private static void propertiesBigIntegerValue_RoundingMode() {
        initialize();
        System.out.println("\t\ttesting bigIntegerValue(RoundingMode) properties...");

        Iterable<Pair<Rational, RoundingMode>> ps = filter(
                p -> {
                    assert p.a != null;
                    return p.b != RoundingMode.UNNECESSARY || p.a.isInteger();
                },
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
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

        for (BigInteger i : take(LIMIT, P.rangeUp(BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE)))) {
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

        for (BigInteger i : take(LIMIT, P.rangeUp(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE)))) {
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

        Iterable<Pair<Rational, BigInteger>> ps;
        if (P instanceof ExhaustiveProvider) {
            ps = ((ExhaustiveProvider) P).pairsSquareRootOrder(
                    cons(ZERO, P.positiveRationals()),
                    P.rangeUp(BigInteger.valueOf(2))
            );
        } else {
            ps = P.pairs(
                    cons(ZERO, ((QBarRandomProvider) P).positiveRationals(20)),
                    map(i -> BigInteger.valueOf(i + 2), ((RandomProvider) P).naturalIntegersGeometric(20))
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            boolean result = p.a.hasTerminatingBaseExpansion(p.b);
            Iterable<BigInteger> dPrimeFactors = nub(MathUtils.primeFactors(p.a.getDenominator()));
            Iterable<BigInteger> bPrimeFactors = nub(MathUtils.primeFactors(p.b));
            assertEquals(p.toString(), result, isSubsetOf(dPrimeFactors, bPrimeFactors));
        }

        if (!(P instanceof ExhaustiveProvider)) {
            ps = P.pairs(
                    cons(ZERO, ((QBarRandomProvider) P).positiveRationals(8)),
                    map(i -> BigInteger.valueOf(i + 2), ((RandomProvider) P).naturalIntegersGeometric(20))
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            boolean result = p.a.hasTerminatingBaseExpansion(p.b);
            Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> pn = p.a.positionalNotation(p.b);
            assert pn.c != null;
            assertEquals(p.toString(), result, pn.c.equals(Arrays.asList(BigInteger.ZERO)));
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.rangeDown(BigInteger.ONE)))) {
            assert p.a != null;
            assert p.b != null;
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
        if (P instanceof ExhaustiveProvider) {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<Integer, RoundingMode>>) P.pairs(P.naturalIntegers(), P.roundingModes())
            );
        } else {
            ps = P.pairs(
                    P.rationals(),
                    (Iterable<Pair<Integer, RoundingMode>>) P.pairs(
                            ((RandomProvider) P).naturalIntegersGeometric(20),
                            P.roundingModes()
                    )
            );
        }
        ps = filter(
                p -> {
                    try {
                        assert p.a != null;
                        assert p.b != null;
                        assert p.b.a != null;
                        assert p.b.b != null;
                        p.a.bigDecimalValue(p.b.a, p.b.b);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                ps
        );
        for (Pair<Rational, Pair<Integer, RoundingMode>> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assert p.b.a != null;
            assert p.b.b != null;
            BigDecimal bd = p.a.bigDecimalValue(p.b.a, p.b.b);
            assertTrue(p.toString(), eq(bd, BigDecimal.ZERO) || bd.signum() == p.a.signum());
        }

        for (Pair<Rational, Pair<Integer, RoundingMode>> p : take(LIMIT, filter(q -> q.b.a != 0 && q.a != ZERO, ps))) {
            assert p.a != null;
            assert p.b != null;
            assert p.b.a != null;
            assert p.b.b != null;
            BigDecimal bd = p.a.bigDecimalValue(p.b.a, p.b.b);
            assertTrue(p.toString(), bd.precision() == p.b.a);
        }

        Iterable<Pair<Rational, Integer>> pris;
        if (P instanceof ExhaustiveProvider) {
            pris = ((ExhaustiveProvider) P).pairsSquareRootOrder(P.rationals(), P.naturalIntegers());
        } else {
            pris = P.pairs(P.rationals(), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        pris = filter(
                p -> {
                    try {
                        assert p.a != null;
                        assert p.b != null;
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
            assert pri.a != null;
            assert pri.b != null;
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
            assert pri.a != null;
            assert pri.b != null;
            BigDecimal low = pri.a.bigDecimalValue(pri.b, RoundingMode.FLOOR);
            BigDecimal high = pri.a.bigDecimalValue(pri.b, RoundingMode.CEILING);
            assertTrue(pri.toString(), lt(low, high));
        }

        for (Pair<Rational, Integer> pri : take(LIMIT, filter(p -> p.a.signum() == 1, priInexact))) {
            assert pri.a != null;
            assert pri.b != null;
            BigDecimal floor = pri.a.bigDecimalValue(pri.b, RoundingMode.FLOOR);
            BigDecimal down = pri.a.bigDecimalValue(pri.b, RoundingMode.DOWN);
            BigDecimal ceiling = pri.a.bigDecimalValue(pri.b, RoundingMode.CEILING);
            BigDecimal up = pri.a.bigDecimalValue(pri.b, RoundingMode.UP);
            assertEquals(pri.toString(), floor, down);
            assertEquals(pri.toString(), ceiling, up);
        }

        for (Pair<Rational, Integer> pri : take(LIMIT, filter(p -> p.a.signum() == -1, priInexact))) {
            assert pri.a != null;
            assert pri.b != null;
            BigDecimal floor = pri.a.bigDecimalValue(pri.b, RoundingMode.FLOOR);
            BigDecimal down = pri.a.bigDecimalValue(pri.b, RoundingMode.DOWN);
            BigDecimal ceiling = pri.a.bigDecimalValue(pri.b, RoundingMode.CEILING);
            BigDecimal up = pri.a.bigDecimalValue(pri.b, RoundingMode.UP);
            assertEquals(pri.toString(), floor, up);
            assertEquals(pri.toString(), ceiling, down);
        }

        Iterable<Pair<BigDecimal, Integer>> notMidpoints;
        if (P instanceof ExhaustiveProvider) {
            notMidpoints = ((ExhaustiveProvider) P).pairsSquareRootOrder(P.bigDecimals(), P.naturalIntegers());
        } else {
            notMidpoints = P.pairs(P.bigDecimals(), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        notMidpoints = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    if (p.a.precision() <= 1) return false;
                    if (p.b != p.a.precision() - 1) return false;
                    return !p.a.abs().unscaledValue().mod(BigInteger.valueOf(10)).equals(BigInteger.valueOf(5));
                },
                notMidpoints
        );
        for (Pair<BigDecimal, Integer> p : take(LIMIT, notMidpoints)) {
            assert p.a != null;
            assert p.b != null;
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
            assert p.a != null;
            assert p.b != null;
            assert p.b.a != null;
            assert p.b.b != null;
            try {
                p.a.bigDecimalValue(p.b.a, p.b.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        Iterable<Rational> rs = filter(r -> !r.hasTerminatingBaseExpansion(BigInteger.valueOf(10)), P.rationals());
        Iterable<Pair<Rational, Integer>> prisFail;
        if (P instanceof ExhaustiveProvider) {
            prisFail = ((ExhaustiveProvider) P).pairsSquareRootOrder(rs, P.naturalIntegers());
        } else {
            prisFail = P.pairs(rs, ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        for (Pair<Rational, Integer> p : take(LIMIT, prisFail)) {
            assert p.a != null;
            assert p.b != null;
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
        if (P instanceof ExhaustiveProvider) {
            ps = ((ExhaustiveProvider) P).pairsSquareRootOrder(P.rationals(), P.naturalIntegers());
        } else {
            ps = P.pairs(P.rationals(), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        ps = filter(
                p -> {
                    try {
                        assert p.a != null;
                        assert p.b != null;
                        p.a.bigDecimalValue(p.b);
                        return true;
                    } catch (ArithmeticException e) {
                        return false;
                    }
                },
                ps
        );
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            BigDecimal bd = p.a.bigDecimalValue(p.b);
            assertEquals(p.toString(), bd, p.a.bigDecimalValue(p.b, RoundingMode.HALF_EVEN));
            assertTrue(p.toString(), eq(bd, BigDecimal.ZERO) || bd.signum() == p.a.signum());
        }

        for (Pair<Rational, Integer> p : take(LIMIT, filter(q -> q.b != 0 && q.a != ZERO, ps))) {
            assert p.a != null;
            assert p.b != null;
            BigDecimal bd = p.a.bigDecimalValue(p.b);
            assertTrue(p.toString(), bd.precision() == p.b);
        }

        Iterable<Pair<BigDecimal, Integer>> notMidpoints;
        if (P instanceof ExhaustiveProvider) {
            notMidpoints = ((ExhaustiveProvider) P).pairsSquareRootOrder(P.bigDecimals(), P.naturalIntegers());
        } else {
            notMidpoints = P.pairs(P.bigDecimals(), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        notMidpoints = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    if (p.a.precision() <= 1) return false;
                    if (p.b != p.a.precision() - 1) return false;
                    return !p.a.abs().unscaledValue().mod(BigInteger.valueOf(10)).equals(BigInteger.valueOf(5));
                },
                notMidpoints
        );
        for (Pair<BigDecimal, Integer> p : take(LIMIT, notMidpoints)) {
            assert p.a != null;
            assert p.b != null;
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
            assert p.a != null;
            assert p.b != null;
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
            assertTrue(r.toString(), eq(bd, BigDecimal.ZERO) || bd.signum() == r.signum());
            assertEquals(r.toString(), of(bd), r);
        }

        for (Pair<Rational, Integer> p : take(LIMIT, P.pairs(P.rationals(), P.negativeIntegers()))) {
            assert p.a != null;
            assert p.b != null;
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
            assertTrue(r.toString(), power.compareTo(r) <= 0);
            assertTrue(r.toString(), r.compareTo(power.shiftLeft(1)) < 0);
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
        Rational belowDistance = r.subtract(ofExact(below));
        Rational aboveDistance = ofExact(above).subtract(r);
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
            assert p.a != null;
            assert p.b != null;
            float rounded = p.a.floatValue(p.b);
            assertTrue(p.toString(), !Float.isNaN(rounded));
            assertTrue(p.toString(), rounded == 0.0f || Math.signum(rounded) == p.a.signum());
        }

        Iterable<Rational> rs = map(
                Rational::ofExact,
                filter(f -> !Float.isNaN(f) && Float.isFinite(f) && !f.equals(-0.0f), P.floats())
        );
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.UNNECESSARY);
            assertEquals(r.toString(), r, ofExact(rounded));
            assertTrue(r.toString(), Float.isFinite(rounded));
            assertTrue(r.toString(), !new Float(rounded).equals(-0.0f));
        }

        rs = filter(r -> !r.equals(LARGEST_FLOAT), P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.FLOOR);
            float successor = FloatingPointUtils.successor(rounded);
            assertTrue(r.toString(), le(ofExact(rounded), r));
            assertTrue(r.toString(), gt(ofExact(successor), r));
            assertTrue(r.toString(), rounded < 0 || Float.isFinite(rounded));
            assertTrue(r.toString(), !new Float(rounded).equals(-0.0f));
        }

        rs = filter(
                r -> !r.equals(LARGEST_FLOAT.negate()),
                P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT))
        );
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.CEILING);
            float predecessor = FloatingPointUtils.predecessor(rounded);
            assertTrue(r.toString(), ge(ofExact(rounded), r));
            assertTrue(r.toString(), lt(ofExact(predecessor), r));
            assertTrue(r.toString(), rounded > 0 || Float.isFinite(rounded));
        }

        rs = P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT));
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.DOWN);
            assertTrue(r.toString(), le(ofExact(rounded).abs(), r.abs()));
            assertTrue(r.toString(), Float.isFinite(rounded));
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue(RoundingMode.DOWN);
            float successor = FloatingPointUtils.successor(rounded);
            float predecessor = FloatingPointUtils.predecessor(rounded);
            float down = r.signum() == -1 ? successor : predecessor;
            assertTrue(r.toString(), lt(ofExact(down).abs(), r.abs()));
        }

        rs = filter(
                r -> !r.abs().equals(LARGEST_FLOAT),
                P.rationals(Interval.of(LARGEST_FLOAT.negate(), LARGEST_FLOAT))
        );
        for (Rational r : take(LIMIT, rs)) {
            assertTrue(r.toString(), !new Float(r.floatValue(RoundingMode.UP)).equals(-0.0f));
        }

        rs = filter(r -> !r.equals(SMALLEST_FLOAT), P.rationals(Interval.of(ZERO, SMALLEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.floatValue(RoundingMode.FLOOR), 0.0f);
            aeq(r.toString(), r.floatValue(RoundingMode.DOWN), 0.0f);

            float rounded = r.floatValue(RoundingMode.UP);
            float successor = FloatingPointUtils.successor(rounded);
            float predecessor = FloatingPointUtils.predecessor(rounded);
            float up = r.signum() == -1 ? predecessor : successor;
            assertTrue(r.toString(), gt(ofExact(up).abs(), r.abs()));
        }

        rs = filter(
                r -> !r.equals(LARGEST_FLOAT.negate()),
                P.rationals(Interval.lessThanOrEqualTo(LARGEST_FLOAT.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            float floor = r.floatValue(RoundingMode.FLOOR);
            aeq(r.toString(), floor, Float.NEGATIVE_INFINITY);

            float up = r.floatValue(RoundingMode.UP);
            aeq(r.toString(), up, Float.NEGATIVE_INFINITY);

            float halfUp = r.floatValue(RoundingMode.HALF_UP);
            aeq(r.toString(), halfUp, Float.NEGATIVE_INFINITY);

            float halfEven = r.floatValue(RoundingMode.HALF_EVEN);
            aeq(r.toString(), halfEven, Float.NEGATIVE_INFINITY);
        }

        rs = P.rationals(Interval.greaterThanOrEqualTo(LARGEST_FLOAT));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.floatValue(RoundingMode.FLOOR), Float.MAX_VALUE);
            aeq(r.toString(), r.floatValue(RoundingMode.DOWN), Float.MAX_VALUE);
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), Float.MAX_VALUE);
        }

        rs = filter(
                r -> r != ZERO && !r.equals(SMALLEST_FLOAT.negate()),
                P.rationals(Interval.of(SMALLEST_FLOAT.negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.floatValue(RoundingMode.CEILING), -0.0f);
            aeq(r.toString(), r.floatValue(RoundingMode.DOWN), -0.0f);
        }

        rs = filter(r -> !r.equals(LARGEST_FLOAT), P.rationals(Interval.greaterThanOrEqualTo(LARGEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            float ceiling = r.floatValue(RoundingMode.CEILING);
            aeq(r.toString(), ceiling, Float.POSITIVE_INFINITY);

            float up = r.floatValue(RoundingMode.UP);
            aeq(r.toString(), up, Float.POSITIVE_INFINITY);

            float halfUp = r.floatValue(RoundingMode.HALF_UP);
            aeq(r.toString(), halfUp, Float.POSITIVE_INFINITY);

            float halfEven = r.floatValue(RoundingMode.HALF_EVEN);
            aeq(r.toString(), halfEven, Float.POSITIVE_INFINITY);
        }

        rs = P.rationals(Interval.lessThanOrEqualTo(LARGEST_FLOAT.negate()));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.floatValue(RoundingMode.CEILING), -Float.MAX_VALUE);
            aeq(r.toString(), r.floatValue(RoundingMode.DOWN), -Float.MAX_VALUE);
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), -Float.MAX_VALUE);
        }

        Iterable<Rational> midpoints = map(
                f -> {
                    Rational lo = ofExact(f);
                    Rational hi = ofExact(FloatingPointUtils.successor(f));
                    assert lo != null;
                    assert hi != null;
                    return lo.add(hi).shiftRight(1);
                },
                filter(f -> !f.equals(-0.0f) && f != Float.MAX_VALUE, P.ordinaryFloats())
        );
        for (Rational r : take(LIMIT, midpoints)) {
            float down = r.floatValue(RoundingMode.DOWN);
            float up = r.floatValue(RoundingMode.UP);
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), down);
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_UP), up);
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
            Rational belowDistance = r.subtract(ofExact(below));
            Rational aboveDistance = ofExact(above).subtract(r);
            float closest = lt(belowDistance, aboveDistance) ? below : above;
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), closest);
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_UP), closest);
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_EVEN), closest);
        }

        rs = P.rationals(Interval.of(ZERO, SMALLEST_FLOAT.shiftRight(1)));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), 0.0f);
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_EVEN), 0.0f);
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(SMALLEST_FLOAT.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_DOWN), -0.0f);
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_EVEN), -0.0f);
        }

        rs = filter(
                r -> !r.equals(SMALLEST_FLOAT.shiftRight(1)),
                P.rationals(Interval.of(ZERO, SMALLEST_FLOAT.shiftRight(1)))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_UP), 0.0f);
        }

        rs = filter(
                r -> r != ZERO && !r.equals(SMALLEST_FLOAT.shiftRight(1).negate()),
                P.rationals(Interval.of(SMALLEST_FLOAT.shiftRight(1).negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.floatValue(RoundingMode.HALF_UP), -0.0f);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            float floor = r.floatValue(RoundingMode.FLOOR);
            assertFalse(r.toString(), Float.valueOf(floor).equals(-0.0f));
            assertFalse(r.toString(), floor == Float.POSITIVE_INFINITY);
            float ceiling = r.floatValue(RoundingMode.CEILING);
            assertFalse(r.toString(), ceiling == Float.NEGATIVE_INFINITY);
            float down = r.floatValue(RoundingMode.DOWN);
            assertFalse(r.toString(), down == Float.NEGATIVE_INFINITY);
            assertFalse(r.toString(), down == Float.POSITIVE_INFINITY);
            float up = r.floatValue(RoundingMode.UP);
            assertFalse(r.toString(), Float.valueOf(up).equals(-0.0f));
            float halfDown = r.floatValue(RoundingMode.HALF_DOWN);
            assertFalse(r.toString(), halfDown == Float.NEGATIVE_INFINITY);
            assertFalse(r.toString(), halfDown == Float.POSITIVE_INFINITY);
        }

        Iterable<Rational> rsFail = filter(
                r -> !ofExact(r.floatValue(RoundingMode.FLOOR)).equals(r),
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
            aeq(r.toString(), rounded, r.floatValue(RoundingMode.HALF_EVEN));
            assertTrue(r.toString(), !Float.isNaN(rounded));
            assertTrue(r.toString(), rounded == 0.0f || Math.signum(rounded) == r.signum());
        }

        Iterable<Rational> rs = filter(
                r -> !r.equals(LARGEST_FLOAT.negate()),
                P.rationals(Interval.lessThanOrEqualTo(LARGEST_FLOAT.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue();
            aeq(r.toString(), rounded, Float.NEGATIVE_INFINITY);
        }

        rs = filter(r -> !r.equals(LARGEST_FLOAT), P.rationals(Interval.greaterThanOrEqualTo(LARGEST_FLOAT)));
        for (Rational r : take(LIMIT, rs)) {
            float rounded = r.floatValue();
            aeq(r.toString(), rounded, Float.POSITIVE_INFINITY);
        }

        Iterable<Rational> midpoints = map(
                f -> {
                    Rational lo = ofExact(f);
                    Rational hi = ofExact(FloatingPointUtils.successor(f));
                    assert lo != null;
                    assert hi != null;
                    return lo.add(hi).shiftRight(1);
                },
                filter(f -> !f.equals(-0.0f) && f != Float.MAX_VALUE, P.ordinaryFloats())
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
            Rational belowDistance = r.subtract(ofExact(below));
            Rational aboveDistance = ofExact(above).subtract(r);
            float closest = lt(belowDistance, aboveDistance) ? below : above;
            aeq(r.toString(), r.floatValue(), closest);
        }

        rs = P.rationals(Interval.of(ZERO, SMALLEST_FLOAT.shiftRight(1)));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.floatValue(), 0.0f);
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(SMALLEST_FLOAT.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.floatValue(), -0.0f);
        }
    }

    private static void propertiesFloatValueExact() {
        initialize();
        System.out.println("\t\ttesting floatValueExact() properties...");

        Iterable<Rational> rs = filter(r -> r.equals(ofExact(r.floatValue(RoundingMode.FLOOR))), P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            float f = r.floatValueExact();
            assertTrue(r.toString(), !Float.isNaN(f));
            assertTrue(r.toString(), f == 0.0f || Math.signum(f) == r.signum());
        }

        rs = map(
                Rational::ofExact,
                filter(f -> !Float.isNaN(f) && Float.isFinite(f) && !f.equals(-0.0f), P.floats())
        );
        for (Rational r : take(LIMIT, rs)) {
            float f = r.floatValueExact();
            assertEquals(r.toString(), r, ofExact(f));
            assertTrue(r.toString(), Float.isFinite(f));
            assertTrue(r.toString(), !new Float(f).equals(-0.0f));
        }

        Iterable<Rational> rsFail = filter(
                r -> !ofExact(r.floatValue(RoundingMode.FLOOR)).equals(r),
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
        Rational belowDistance = r.subtract(ofExact(below));
        Rational aboveDistance = ofExact(above).subtract(r);
        return belowDistance.equals(aboveDistance);
    }

    private static void propertiesDoubleValue_RoundingMode() {
        initialize();
        System.out.println("\t\ttesting doubleValue(RoundingMode) properties...");

        Iterable<Pair<Rational, RoundingMode>> ps = filter(
                p -> p.b != RoundingMode.UNNECESSARY || p.a.equals(ofExact(p.a.doubleValue(RoundingMode.FLOOR))),
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            double rounded = p.a.doubleValue(p.b);
            assertTrue(p.toString(), !Double.isNaN(rounded));
            assertTrue(p.toString(), rounded == 0.0 || Math.signum(rounded) == p.a.signum());
        }

        Iterable<Rational> rs = map(
                Rational::ofExact,
                filter(f -> !Double.isNaN(f) && Double.isFinite(f) && !f.equals(-0.0), P.doubles())
        );
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.UNNECESSARY);
            assertEquals(r.toString(), r, ofExact(rounded));
            assertTrue(r.toString(), Double.isFinite(rounded));
            assertTrue(r.toString(), !new Double(rounded).equals(-0.0));
        }

        rs = filter(r -> !r.equals(LARGEST_DOUBLE), P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.FLOOR);
            double successor = FloatingPointUtils.successor(rounded);
            assertTrue(r.toString(), le(ofExact(rounded), r));
            assertTrue(r.toString(), gt(ofExact(successor), r));
            assertTrue(r.toString(), rounded < 0 || Double.isFinite(rounded));
            assertTrue(r.toString(), !new Double(rounded).equals(-0.0));
        }

        rs = filter(
                r -> !r.equals(LARGEST_DOUBLE.negate()),
                P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.CEILING);
            double predecessor = FloatingPointUtils.predecessor(rounded);
            assertTrue(r.toString(), ge(ofExact(rounded), r));
            assertTrue(r.toString(), lt(ofExact(predecessor), r));
            assertTrue(r.toString(), rounded > 0 || Double.isFinite(rounded));
        }

        rs = P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE));
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.DOWN);
            assertTrue(r.toString(), le(ofExact(rounded).abs(), r.abs()));
            assertTrue(r.toString(), Double.isFinite(rounded));
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue(RoundingMode.DOWN);
            double successor = FloatingPointUtils.successor(rounded);
            double predecessor = FloatingPointUtils.predecessor(rounded);
            double down = r.signum() == -1 ? successor : predecessor;
            assertTrue(r.toString(), lt(ofExact(down).abs(), r.abs()));
        }

        rs = filter(
                r -> !r.abs().equals(LARGEST_DOUBLE),
                P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rs)) {
            assertTrue(r.toString(), !new Double(r.doubleValue(RoundingMode.UP)).equals(-0.0));
        }

        rs = filter(r -> !r.equals(SMALLEST_DOUBLE), P.rationals(Interval.of(ZERO, SMALLEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.doubleValue(RoundingMode.FLOOR), 0.0);
            aeq(r.toString(), r.doubleValue(RoundingMode.DOWN), 0.0);

            double rounded = r.doubleValue(RoundingMode.UP);
            double successor = FloatingPointUtils.successor(rounded);
            double predecessor = FloatingPointUtils.predecessor(rounded);
            double up = r.signum() == -1 ? predecessor : successor;
            assertTrue(r.toString(), gt(ofExact(up).abs(), r.abs()));
        }

        rs = filter(
                r -> !r.equals(LARGEST_DOUBLE.negate()),
                P.rationals(Interval.lessThanOrEqualTo(LARGEST_DOUBLE.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            double floor = r.doubleValue(RoundingMode.FLOOR);
            aeq(r.toString(), floor, Double.NEGATIVE_INFINITY);

            double up = r.doubleValue(RoundingMode.UP);
            aeq(r.toString(), up, Double.NEGATIVE_INFINITY);

            double halfUp = r.doubleValue(RoundingMode.HALF_UP);
            aeq(r.toString(), halfUp, Double.NEGATIVE_INFINITY);

            double halfEven = r.doubleValue(RoundingMode.HALF_EVEN);
            aeq(r.toString(), halfEven, Double.NEGATIVE_INFINITY);
        }

        rs = P.rationals(Interval.greaterThanOrEqualTo(LARGEST_DOUBLE));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.doubleValue(RoundingMode.FLOOR), Double.MAX_VALUE);
            aeq(r.toString(), r.doubleValue(RoundingMode.DOWN), Double.MAX_VALUE);
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), Double.MAX_VALUE);
        }

        rs = filter(
                r -> r != ZERO && !r.equals(SMALLEST_DOUBLE.negate()),
                P.rationals(Interval.of(SMALLEST_DOUBLE.negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.doubleValue(RoundingMode.CEILING), -0.0);
            aeq(r.toString(), r.doubleValue(RoundingMode.DOWN), -0.0);
        }

        rs = filter(r -> !r.equals(LARGEST_DOUBLE), P.rationals(Interval.greaterThanOrEqualTo(LARGEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            double ceiling = r.doubleValue(RoundingMode.CEILING);
            aeq(r.toString(), ceiling, Double.POSITIVE_INFINITY);

            double up = r.doubleValue(RoundingMode.UP);
            aeq(r.toString(), up, Double.POSITIVE_INFINITY);

            double halfUp = r.doubleValue(RoundingMode.HALF_UP);
            aeq(r.toString(), halfUp, Double.POSITIVE_INFINITY);

            double halfEven = r.doubleValue(RoundingMode.HALF_EVEN);
            aeq(r.toString(), halfEven, Double.POSITIVE_INFINITY);
        }

        rs = P.rationals(Interval.lessThanOrEqualTo(LARGEST_DOUBLE.negate()));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.doubleValue(RoundingMode.CEILING), -Double.MAX_VALUE);
            aeq(r.toString(), r.doubleValue(RoundingMode.DOWN), -Double.MAX_VALUE);
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), -Double.MAX_VALUE);
        }

        Iterable<Rational> midpoints = map(
                f -> {
                    Rational lo = ofExact(f);
                    Rational hi = ofExact(FloatingPointUtils.successor(f));
                    assert lo != null;
                    assert hi != null;
                    return lo.add(hi).shiftRight(1);
                },
                filter(f -> !f.equals(-0.0) && f != Double.MAX_VALUE, P.ordinaryDoubles())
        );
        for (Rational r : take(LIMIT, midpoints)) {
            double down = r.doubleValue(RoundingMode.DOWN);
            double up = r.doubleValue(RoundingMode.UP);
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), down);
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_UP), up);
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
            Rational belowDistance = r.subtract(ofExact(below));
            Rational aboveDistance = ofExact(above).subtract(r);
            double closest = lt(belowDistance, aboveDistance) ? below : above;
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), closest);
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_UP), closest);
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_EVEN), closest);
        }

        rs = P.rationals(Interval.of(ZERO, SMALLEST_DOUBLE.shiftRight(1)));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), 0.0);
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_EVEN), 0.0);
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(SMALLEST_DOUBLE.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_DOWN), -0.0);
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_EVEN), -0.0);
        }

        rs = filter(
                r -> !r.equals(SMALLEST_DOUBLE.shiftRight(1)),
                P.rationals(Interval.of(ZERO, SMALLEST_DOUBLE.shiftRight(1)))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_UP), 0.0);
        }

        rs = filter(
                r -> r != ZERO && !r.equals(SMALLEST_DOUBLE.shiftRight(1).negate()),
                P.rationals(Interval.of(SMALLEST_DOUBLE.shiftRight(1).negate(), ZERO))
        );
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.doubleValue(RoundingMode.HALF_UP), -0.0);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            double floor = r.doubleValue(RoundingMode.FLOOR);
            assertFalse(r.toString(), Double.valueOf(floor).equals(-0.0));
            assertFalse(r.toString(), floor == Double.POSITIVE_INFINITY);
            double ceiling = r.doubleValue(RoundingMode.CEILING);
            assertFalse(r.toString(), ceiling == Double.NEGATIVE_INFINITY);
            double down = r.doubleValue(RoundingMode.DOWN);
            assertFalse(r.toString(), down == Double.NEGATIVE_INFINITY);
            assertFalse(r.toString(), down == Double.POSITIVE_INFINITY);
            double up = r.doubleValue(RoundingMode.UP);
            assertFalse(r.toString(), Double.valueOf(up).equals(-0.0));
            double halfDown = r.doubleValue(RoundingMode.HALF_DOWN);
            assertFalse(r.toString(), halfDown == Double.NEGATIVE_INFINITY);
            assertFalse(r.toString(), halfDown == Double.POSITIVE_INFINITY);
        }

        Iterable<Rational> rsFail = filter(
                r -> !ofExact(r.doubleValue(RoundingMode.FLOOR)).equals(r),
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
            aeq(r.toString(), rounded, r.doubleValue(RoundingMode.HALF_EVEN));
            assertTrue(r.toString(), !Double.isNaN(rounded));
            assertTrue(r.toString(), rounded == 0.0 || Math.signum(rounded) == r.signum());
        }

        Iterable<Rational> rs = filter(
                r -> !r.equals(LARGEST_DOUBLE.negate()),
                P.rationals(Interval.lessThanOrEqualTo(LARGEST_DOUBLE.negate()))
        );
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue();
            aeq(r.toString(), rounded, Double.NEGATIVE_INFINITY);
        }

        rs = filter(r -> !r.equals(LARGEST_DOUBLE), P.rationals(Interval.greaterThanOrEqualTo(LARGEST_DOUBLE)));
        for (Rational r : take(LIMIT, rs)) {
            double rounded = r.doubleValue();
            aeq(r.toString(), rounded, Double.POSITIVE_INFINITY);
        }

        Iterable<Rational> midpoints = map(
                f -> {
                    Rational lo = ofExact(f);
                    Rational hi = ofExact(FloatingPointUtils.successor(f));
                    assert lo != null;
                    assert hi != null;
                    return lo.add(hi).shiftRight(1);
                },
                filter(f -> !f.equals(-0.0) && f != Double.MAX_VALUE, P.ordinaryDoubles())
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
            Rational belowDistance = r.subtract(ofExact(below));
            Rational aboveDistance = ofExact(above).subtract(r);
            double closest = lt(belowDistance, aboveDistance) ? below : above;
            aeq(r.toString(), r.doubleValue(), closest);
        }

        rs = P.rationals(Interval.of(ZERO, SMALLEST_DOUBLE.shiftRight(1)));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.doubleValue(), 0.0);
        }

        rs = filter(r -> r != ZERO, P.rationals(Interval.of(SMALLEST_DOUBLE.shiftRight(1).negate(), ZERO)));
        for (Rational r : take(LIMIT, rs)) {
            aeq(r.toString(), r.doubleValue(), -0.0);
        }
    }

    private static void propertiesDoubleValueExact() {
        initialize();
        System.out.println("\t\ttesting doubleValueExact() properties...");

        Iterable<Rational> rs = filter(r -> r.equals(ofExact(r.doubleValue(RoundingMode.FLOOR))), P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            double f = r.doubleValueExact();
            assertTrue(r.toString(), !Double.isNaN(f));
            assertTrue(r.toString(), f == 0.0 || Math.signum(f) == r.signum());
        }

        rs = map(
                Rational::ofExact,
                filter(f -> !Double.isNaN(f) && Double.isFinite(f) && !f.equals(-0.0), P.doubles())
        );
        for (Rational r : take(LIMIT, rs)) {
            double f = r.doubleValueExact();
            assertEquals(r.toString(), r, ofExact(f));
            assertTrue(r.toString(), Double.isFinite(f));
            assertTrue(r.toString(), !new Double(f).equals(-0.0));
        }

        Iterable<Rational> rsFail = filter(
                r -> !ofExact(r.doubleValue(RoundingMode.FLOOR)).equals(r),
                P.rationals(Interval.of(LARGEST_DOUBLE.negate(), LARGEST_DOUBLE))
        );
        for (Rational r : take(LIMIT, rsFail)) {
            try {
                r.doubleValueExact();
                fail(r.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesNegate() {
        initialize();
        System.out.println("\t\ttesting negate() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational negativeR = r.negate();
            validate(negativeR);
            assertEquals(r.toString(), r, negativeR.negate());
            assertTrue(r.add(negativeR) == ZERO);
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            Rational negativeR = r.negate();
            assertNotEquals(r.toString(), r, negativeR);
        }
    }

    private static @NotNull Rational invert_simplest(@NotNull Rational r) {
        return of(r.getDenominator(), r.getNumerator());
    }

    private static void propertiesInvert() {
        initialize();
        System.out.println("\t\ttesting invert() properties...");

        Iterable<Rational> rs = filter(r -> r != ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            Rational inverseR = r.invert();
            validate(inverseR);
            assertEquals(r.toString(), inverseR, invert_simplest(r));
            assertEquals(r.toString(), r, inverseR.invert());
            assertTrue(r.multiply(inverseR) == ONE);
            assertTrue(inverseR != ZERO);
        }

        rs = filter(r -> r != ZERO && r.abs() != ONE, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
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

    private static void propertiesAbs() {
        initialize();
        System.out.println("\t\ttesting abs() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational absR = r.abs();
            validate(absR);
            assertEquals(r.toString(), absR, absR.abs());
            assertTrue(r.toString(), ge(absR, ZERO));
        }
    }

    private static void propertiesSignum() {
        initialize();
        System.out.println("\t\ttesting signum() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            int signumR = r.signum();
            assertEquals(r.toString(), signumR, Ordering.compare(r, ZERO).toInt());
            assertTrue(r.toString(), signumR == -1 || signumR == 0 || signumR == 1);
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
            assert p.a != null;
            assert p.b != null;
            Rational sum = p.a.add(p.b);
            validate(sum);
            assertEquals(p.toString(), sum, add_simplest(p.a, p.b));
            assertEquals(p.toString(), sum, p.b.add(p.a));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), ZERO.add(r), r);
            assertEquals(r.toString(), r.add(ZERO), r);
            assertTrue(r.toString(), r.add(r.negate()) == ZERO);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(P.rationals()))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
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
            assert p.a != null;
            assert p.b != null;
            add_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
            p.a.add(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static void propertiesSubtract() {
        initialize();
        System.out.println("\t\ttesting subtract(Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            Rational difference = p.a.subtract(p.b);
            validate(difference);
            assertEquals(p.toString(), difference, p.b.subtract(p.a).negate());
            assertEquals(p.toString(), p.a, difference.add(p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), ZERO.subtract(r), r.negate());
            assertEquals(r.toString(), r.subtract(ZERO), r);
            assertTrue(r.toString(), r.subtract(r) == ZERO);
        }
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
            assert p.a != null;
            assert p.b != null;
            Rational product = p.a.multiply(p.b);
            validate(product);
            assertEquals(
                    p.toString(),
                    new Pair<>(product.getNumerator(), product.getDenominator()),
                    multiply_Rational_Knuth(p.a, p.b)
            );
            assertEquals(p.toString(), product, p.b.multiply(p.a));
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
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational product1 = t.a.multiply(t.b).multiply(t.c);
            Rational product2 = t.a.multiply(t.b.multiply(t.c));
            assertEquals(t.toString(), product1, product2);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(P.rationals()))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
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
            assert p.a != null;
            assert p.b != null;
            multiply_Rational_Knuth(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tKnuth: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
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

        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(P.rationals(), P.bigIntegers());
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational product = p.a.multiply(p.b);
            validate(product);
            assertEquals(p.toString(), product, multiply_BigInteger_simplest(p.a, p.b));
            assertEquals(p.toString(), product, p.a.multiply(of(p.b)));
            assertEquals(p.toString(), product, of(p.b).multiply(p.a));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), ONE.multiply(i), of(i));
            assertTrue(i.toString(), ZERO.multiply(i) == ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.multiply(BigInteger.ONE), r);
            assertTrue(r.toString(), r.multiply(BigInteger.ZERO) == ZERO);
        }

        Iterable<BigInteger> bis = filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers());
        for (BigInteger i : take(LIMIT, bis)) {
            assertTrue(i.toString(), of(i).invert().multiply(i) == ONE);
        }

        Iterable<Rational> rs = P.rationals();
        for (Triple<Rational, Rational, BigInteger> t : take(LIMIT, P.triples(rs, rs, P.bigIntegers()))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = t.a.add(t.b).multiply(t.c);
            Rational expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void compareImplementationsMultiply_BigInteger() {
        initialize();
        System.out.println("\t\tcomparing multiply(BigInteger) implementations...");

        long totalTime = 0;
        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(P.rationals(), P.bigIntegers());
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
            multiply_BigInteger_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
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
            assert p.a != null;
            assert p.b != null;
            Rational product = p.a.multiply(p.b);
            validate(product);
            assertEquals(p.toString(), product, multiply_int_simplest(p.a, p.b));
            assertEquals(p.toString(), product, p.a.multiply(p.b));
            assertEquals(p.toString(), product, p.a.multiply(of(p.b)));
            assertEquals(p.toString(), product, of(p.b).multiply(p.a));
        }

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(Integer.toString(i), ONE.multiply(i), of(i));
            assertTrue(Integer.toString(i), ZERO.multiply(i) == ZERO);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.multiply(1), r);
            assertTrue(r.toString(), r.multiply(0) == ZERO);
        }

        Iterable<Integer> is = filter(i -> i != 0, P.integers());
        for (int i : take(LIMIT, is)) {
            assertTrue(Integer.toString(i), of(i).invert().multiply(i) == ONE);
        }

        Iterable<Rational> rs = P.rationals();
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, P.triples(rs, rs, P.integers()))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = t.a.add(t.b).multiply(t.c);
            Rational expression2 = t.a.multiply(t.c).add(t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void compareImplementationsMultiply_int() {
        initialize();
        System.out.println("\t\tcomparing multiply(int) implementations...");

        long totalTime = 0;
        Iterable<Pair<Rational, Integer>> ps = P.pairs(P.rationals(), P.integers());
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
            multiply_int_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
            p.a.multiply(p.b);
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

        Iterable<Pair<Rational, Rational>> ps = filter(p -> p.b != ZERO, P.pairs(P.rationals()));
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational quotient = p.a.divide(p.b);
            validate(quotient);
            assertEquals(p.toString(), quotient, divide_Rational_simplest(p.a, p.b));
            assertEquals(p.toString(), p.a, quotient.multiply(p.b));
        }

        ps = filter(p -> p.a != ZERO && p.b != ZERO, P.pairs(P.rationals()));
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), p.a.divide(p.b), p.b.divide(p.a).invert());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.divide(ONE), r);
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), ONE.divide(r), r.invert());
            assertTrue(r.toString(), r.divide(r) == ONE);
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
            assert p.a != null;
            assert p.b != null;
            divide_Rational_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
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
            assert p.a != null;
            assert p.b != null;
            Rational quotient = p.a.divide(p.b);
            validate(quotient);
            assertEquals(p.toString(), quotient, divide_BigInteger_simplest(p.a, p.b));
            assertEquals(p.toString(), p.a, quotient.multiply(p.b));
        }

        ps = P.pairs(
                filter(r -> r != ZERO, P.rationals()),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), p.a.divide(p.b), of(p.b).divide(p.a).invert());
        }
        
        Iterable<BigInteger> bis = filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers());
        for (BigInteger i : take(LIMIT, bis)) {
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
            assert p.a != null;
            assert p.b != null;
            divide_BigInteger_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
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
            assert p.a != null;
            assert p.b != null;
            Rational quotient = p.a.divide(p.b);
            validate(quotient);
            assertEquals(p.toString(), quotient, divide_int_simplest(p.a, p.b));
            assertEquals(p.toString(), p.a, quotient.multiply(p.b));
        }

        ps = P.pairs((Iterable<Rational>) filter(r -> r != ZERO, P.rationals()), filter(i -> i != 0, P.integers()));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), p.a.divide(p.b), of(p.b).divide(p.a).invert());
        }

        Iterable<Integer> is = filter(i -> i != 0, P.integers());
        for (int i : take(LIMIT, is)) {
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
            assert p.a != null;
            assert p.b != null;
            divide_int_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
            p.a.divide(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static void propertiesSum() {
        initialize();
        System.out.println("\t\ttesting sum(Iterable<Rational>) properties...");

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            validate(sum(rs));
        }

        Iterable<Pair<List<Rational>, List<Rational>>> ps = filter(
                q -> {
                    assert q.a != null;
                    assert q.b != null;
                    return !q.a.equals(q.b);
                },
                P.dependentPairsLogarithmic(P.lists(P.rationals()), Combinatorics::permutationsIncreasing)
        );

        for (Pair<List<Rational>, List<Rational>> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), sum(p.a), sum(p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), sum(Arrays.asList(r)), r);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), sum(Arrays.asList(p.a, p.b)), p.a.add(p.b));
        }

        Iterable<List<Rational>> failRss = map(p -> {
            assert p.a != null;
            assert p.b != null;
            return toList(insert(p.a, p.b, null));
        }, (Iterable<Pair<List<Rational>, Integer>>) P.dependentPairsLogarithmic(
                P.lists(P.rationals()),
                rs -> range(0, rs.size())
        ));
        for (List<Rational> rs : take(LIMIT, failRss)) {
            try {
                sum(rs);
                fail(rs.toString());
            } catch (AssertionError ignored) {}
        }
    }

    private static void propertiesProduct() {
        initialize();
        System.out.println("\t\ttesting product(Iterable<Rational>) properties...");

        for (List<Rational> rs : take(LIMIT, P.lists(P.rationals()))) {
            validate(product(rs));
        }

        Iterable<Pair<List<Rational>, List<Rational>>> ps = filter(
                q -> {
                    assert q.a != null;
                    assert q.b != null;
                    return !q.a.equals(q.b);
                },
                P.dependentPairsLogarithmic(P.lists(P.rationals()), Combinatorics::permutationsIncreasing)
        );

        for (Pair<List<Rational>, List<Rational>> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), product(p.a), product(p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), product(Arrays.asList(r)), r);
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), product(Arrays.asList(p.a, p.b)), p.a.multiply(p.b));
        }

        Iterable<List<Rational>> failRss = map(p -> {
            assert p.a != null;
            assert p.b != null;
            return toList(insert(p.a, p.b, null));
        }, (Iterable<Pair<List<Rational>, Integer>>) P.dependentPairsLogarithmic(
                P.lists(P.rationals()),
                rs -> range(0, rs.size())
        ));
        for (List<Rational> rs : take(LIMIT, failRss)) {
            try {
                product(rs);
                fail(rs.toString());
            } catch (AssertionError ignored) {}
        }
    }

    private static void propertiesDelta() {
        initialize();
        System.out.println("\t\ttesting delta(Iterable<Rational>) properties...");

        for (List<Rational> rs : take(LIMIT, P.listsAtLeast(1, P.rationals()))) {
            Iterable<Rational> deltas = delta(rs);
            aeq(rs.toString(), length(deltas), length(rs) - 1);
            Iterable<Rational> reversed = reverse(map(Rational::negate, delta(reverse(rs))));
            aeq(rs.toString(), deltas, reversed);
            try {
                deltas.iterator().remove();
            } catch (UnsupportedOperationException ignored) {}
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r.toString(), isEmpty(delta(Arrays.asList(r))));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            aeq(p.toString(), delta(Arrays.asList(p.a, p.b)), Arrays.asList(p.b.subtract(p.a)));
        }

        Iterable<List<Rational>> failRss = map(p -> {
            assert p.a != null;
            assert p.b != null;
            return toList(insert(p.a, p.b, null));
        }, (Iterable<Pair<List<Rational>, Integer>>) P.dependentPairsLogarithmic(
                P.lists(P.rationals()),
                rs -> range(0, rs.size())
        ));
        for (List<Rational> rs : take(LIMIT, failRss)) {
            try {
                toList(delta(rs));
                fail(rs.toString());
            } catch (AssertionError | NullPointerException ignored) {}
        }
    }

    private static void propertiesHarmonicNumber() {
        initialize();
        System.out.println("\t\ttesting harmonicNumber(int) properties...");

        Iterable<Integer> is;
        if (P instanceof ExhaustiveProvider) {
            is = P.positiveIntegers();
        } else {
            is = ((RandomProvider) P).positiveIntegersGeometric(100);
        }
        for (int i : take(SMALL_LIMIT, is)) {
            Rational h = harmonicNumber(i);
            validate(h);
            assertTrue(Integer.toString(i), ge(h, ONE));
        }

        is = map(i -> i + 1, is);
        for (int i : take(SMALL_LIMIT, is)) {
            Rational h = harmonicNumber(i);
            assertTrue(Integer.toString(i), gt(h, harmonicNumber(i - 1)));
            assertFalse(Integer.toString(i), h.isInteger());
        }

        for (int i : take(LIMIT, map(j -> -j, P.naturalIntegers()))) {
            try {
                harmonicNumber(i);
                fail(Integer.toString(i));
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull Rational pow_simplest(@NotNull Rational a, int p) {
        Rational result = ONE;
        for (int i = 0; i < Math.abs(p); i++) {
            result = result.multiply(a);
        }
        return p < 0 ? result.invert() : result;
    }

    private static void propertiesPow() {
        initialize();
        System.out.println("\t\ttesting pow(int) properties...");

        Iterable<Integer> exps;
        if (P instanceof QBarExhaustiveProvider) {
            exps = P.integers();
        } else {
            exps = ((RandomProvider) P).integersGeometric(20);
        }

        Iterable<Pair<Rational, Integer>> ps = filter(p -> p.b >= 0 || p.a != ZERO, P.pairs(P.rationals(), exps));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational r = p.a.pow(p.b);
            validate(r);
            assertEquals(p.toString(), r, pow_simplest(p.a, p.b));
        }

        ps = P.pairs(filter(r -> r != ZERO, P.rationals()), exps);
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational r = p.a.pow(p.b);
            assertEquals(p.toString(), r, p.a.pow(-p.b).invert());
            assertEquals(p.toString(), r, p.a.invert().pow(-p.b));
        }

        Iterable<Integer> pexps;
        if (P instanceof QBarExhaustiveProvider) {
            pexps = P.positiveIntegers();
        } else {
            pexps = ((RandomProvider) P).positiveIntegersGeometric(20);
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
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = t.a.pow(t.b).multiply(t.a.pow(t.c));
            Rational expression2 = t.a.pow(t.b + t.c);
            assertEquals(t.toString(), expression1, expression2);
        }

        ts1 = filter(
                t -> t.a != ZERO || t.c == 0 && t.b >= 0,
                P.triples(P.rationals(), exps, exps)
        );
        for (Triple<Rational, Integer, Integer> t : take(LIMIT, ts1)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = t.a.pow(t.b).divide(t.a.pow(t.c));
            Rational expression2 = t.a.pow(t.b - t.c);
            assertEquals(t.toString(), expression1, expression2);
        }

        ts1 = filter(
                t -> t.a != ZERO || t.b >= 0 && t.c >= 0,
                P.triples(P.rationals(), exps, exps)
        );
        for (Triple<Rational, Integer, Integer> t : take(LIMIT, ts1)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression5 = t.a.pow(t.b).pow(t.c);
            Rational expression6 = t.a.pow(t.b * t.c);
            assertEquals(t.toString(), expression5, expression6);
        }

        Iterable<Triple<Rational, Rational, Integer>> ts2 = filter(
                t -> t.a != ZERO && t.b != ZERO || t.c >= 0,
                P.triples(P.rationals(), P.rationals(), exps)
        );
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, ts2)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = t.a.multiply(t.b).pow(t.c);
            Rational expression2 = t.a.pow(t.c).multiply(t.b.pow(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }

        ts2 = filter(
                t -> t.a != ZERO || t.c >= 0,
                P.triples(P.rationals(), filter(r -> r != ZERO, P.rationals()), exps)
        );
        for (Triple<Rational, Rational, Integer> t : take(LIMIT, ts2)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
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
            exps = ((RandomProvider) P).integersGeometric(20);
        }
        Iterable<Pair<Rational, Integer>> ps = filter(p -> p.b >= 0 || p.a != ZERO, P.pairs(P.rationals(), exps));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
            pow_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
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
            validate(fractionalPart);
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
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return p.c != RoundingMode.UNNECESSARY || p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO);
                },
                P.triples(P.rationals(), P.positiveBigIntegers(), P.roundingModes())
        );
        for (Triple<Rational, BigInteger, RoundingMode> t : take(LIMIT, ts)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational rounded = t.a.roundToDenominator(t.b, t.c);
            validate(rounded);
            assertEquals(t.toString(), t.b.mod(rounded.getDenominator()), BigInteger.ZERO);
            assertTrue(t.toString(), rounded == ZERO || rounded.signum() == t.a.signum());
            assertTrue(t.toString(), lt(t.a.subtract(rounded).abs(), of(BigInteger.ONE, t.b)));
        }

        Iterable<Pair<Rational, RoundingMode>> ps1 = filter(
                p -> {
                    assert p.a != null;
                    return p.b != RoundingMode.UNNECESSARY || p.a.isInteger();
                },
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps1)) {
            assert p.a != null;
            assert p.b != null;
            Rational rounded = p.a.roundToDenominator(BigInteger.ONE, p.b);
            assertEquals(p.toString(), rounded.getNumerator(), p.a.bigIntegerValue(p.b));
            assertEquals(p.toString(), rounded.getDenominator(), BigInteger.ONE);
        }

        Iterable<Pair<Rational, BigInteger>> ps2 = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO);
                },
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assert p.a != null;
            assert p.b != null;
            assertTrue(p.toString(), p.a.roundToDenominator(p.b, RoundingMode.UNNECESSARY).equals(p.a));
        }

        ps2 = P.pairs(P.rationals(), P.positiveBigIntegers());
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assert p.a != null;
            assert p.b != null;
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
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return lt((Rational) p.a.abs().multiply(p.b).fractionalPart(), of(1, 2));
                },
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );

        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assert p.a != null;
            assert p.b != null;
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
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return gt((Rational) p.a.abs().multiply(p.b).fractionalPart(), of(1, 2));
                },
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );

        for (Pair<Rational, BigInteger> p : take(LIMIT, ps2)) {
            assert p.a != null;
            assert p.b != null;
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
                map(i -> i.negate(), P.naturalBigIntegers()),
                P.roundingModes()
        );
        for (Triple<Rational, BigInteger, RoundingMode> t : take(LIMIT, failTs)) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            try {
                t.a.roundToDenominator(t.b, t.c);
                fail(t.toString());
            } catch (ArithmeticException ignored) {}
        }

        Iterable<Pair<Rational, BigInteger>> failPs = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    return !p.b.mod(p.a.getDenominator()).equals(BigInteger.ZERO);
                },
                P.pairs(P.rationals(), P.positiveBigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, failPs)) {
            assert p.a != null;
            assert p.b != null;
            try {
                p.a.roundToDenominator(p.b, RoundingMode.UNNECESSARY);
                fail(p.toString());
            } catch (ArithmeticException ignored) {}
        }
    }

    private static @NotNull Rational shiftLeft_simplest(@NotNull Rational a, int bits) {
        if (bits < 0) {
            return a.divide(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return a.multiply(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private static void propertiesShiftLeft() {
        initialize();
        System.out.println("\t\ttesting shiftLeft(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        Iterable<Pair<Rational, Integer>> ps = P.pairs(P.rationals(), is);
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational shifted = p.a.shiftLeft(p.b);
            validate(shifted);
            assertEquals(p.toString(), shifted, shiftLeft_simplest(p.a, p.b));
            assertEquals(p.toString(), p.a.negate().shiftLeft(p.b), shifted.negate());
            assertEquals(p.toString(), shifted, p.a.shiftRight(-p.b));
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = ((QBarRandomProvider) P).naturalIntegersGeometric(50);
        }
        ps = P.pairs(P.rationals(), is);
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
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
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        Iterable<Pair<Rational, Integer>> ps = P.pairs(P.rationals(), is);
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
            shiftLeft_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
            p.a.shiftLeft(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
    }

    private static @NotNull Rational shiftRight_simplest(@NotNull Rational a, int bits) {
        if (bits < 0) {
            return a.multiply(BigInteger.ONE.shiftLeft(-bits));
        } else {
            return a.divide(BigInteger.ONE.shiftLeft(bits));
        }
    }

    private static void propertiesShiftRight() {
        initialize();
        System.out.println("\t\ttesting shiftRight(int) properties...");

        Iterable<Integer> is;
        if (P instanceof QBarExhaustiveProvider) {
            is = P.integers();
        } else {
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        Iterable<Pair<Rational, Integer>> ps = P.pairs(P.rationals(), is);
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational shifted = p.a.shiftRight(p.b);
            validate(shifted);
            assertEquals(p.toString(), shifted, shiftRight_simplest(p.a, p.b));
            assertEquals(p.toString(), p.a.negate().shiftRight(p.b), shifted.negate());
            assertEquals(p.toString(), shifted, p.a.shiftLeft(-p.b));
        }

        if (P instanceof QBarExhaustiveProvider) {
            is = P.naturalIntegers();
        } else {
            is  = ((QBarRandomProvider) P).naturalIntegersGeometric(50);
        }
        ps = P.pairs(P.rationals(), is);
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
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
            is  = ((QBarRandomProvider) P).integersGeometric(50);
        }
        Iterable<Pair<Rational, Integer>> ps = P.pairs(P.rationals(), is);
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
            shiftRight_simplest(p.a, p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tsimplest: " + ((double) totalTime) / 1e9 + " s");

        totalTime = 0;
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            long time = System.nanoTime();
            assert p.a != null;
            assert p.b != null;
            p.a.shiftRight(p.b);
            totalTime += (System.nanoTime() - time);
        }
        System.out.println("\t\t\tstandard: " + ((double) totalTime) / 1e9 + " s");
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
                p -> {
                    assert p.b != null;
                    return toList(cons(p.a, p.b));
                },
                (Iterable<Pair<BigInteger, List<BigInteger>>>) P.pairs(
                        P.bigIntegers(),
                        P.lists(P.positiveBigIntegers())
                )
        );
        for (List<BigInteger> is : take(LIMIT, iss)) {
            Rational r = fromContinuedFraction(is);
            validate(r);
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
            convergents.forEach(mho.qbar.objects.RationalProperties::validate);
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
        if (P instanceof ExhaustiveProvider) {
            ps = ((ExhaustiveProvider) P).pairsSquareRootOrder(
                    cons(ZERO, P.positiveRationals()),
                    P.rangeUp(BigInteger.valueOf(2))
            );
        } else {
            ps = P.pairs(
                    cons(ZERO, ((QBarRandomProvider) P).positiveRationals(8)),
                    map(i -> BigInteger.valueOf(i + 2), ((RandomProvider) P).naturalIntegersGeometric(20))
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>> pn = p.a.positionalNotation(p.b);
            assert pn.a != null;
            assert pn.b != null;
            assert pn.c != null;
            for (List<BigInteger> is : Arrays.asList(pn.a, pn.b, pn.c)) {
                assertTrue(p.toString(), all(i -> i != null && i.signum() != -1 && lt(i, p.b), is));
            }
            assertTrue(p.toString(), pn.a.isEmpty() || !head(pn.a).equals(BigInteger.ZERO));
            assertFalse(p.toString(), pn.c.isEmpty());
            assertNotEquals(p.toString(), pn.c, Arrays.asList(p.b.subtract(BigInteger.ONE)));
            Pair<List<BigInteger>, List<BigInteger>> minimized = minimize(pn.b, pn.c);
            assertEquals(p.toString(), minimized.a, pn.b);
            assertEquals(p.toString(), minimized.b, pn.c);
            assertEquals(p.toString(), fromPositionalNotation(p.b, pn.a, pn.b, pn.c), p.a);
            assertEquals(
                    p.toString(),
                    pn.c.equals(Arrays.asList(BigInteger.ZERO)),
                    p.a.hasTerminatingBaseExpansion(p.b)
            );
        }

        Iterable<Pair<Rational, BigInteger>> psFail = P.pairs(P.negativeRationals(), P.rangeUp(BigInteger.valueOf(2)));
        for (Pair<Rational, BigInteger> p : take(LIMIT, psFail)) {
            assert p.a != null;
            assert p.b != null;
            try {
                p.a.positionalNotation(p.b);
                fail(p.toString());
            } catch (IllegalArgumentException ignored) {}
        }

        for (Pair<Rational, BigInteger> p : take(LIMIT, P.pairs(P.rationals(), P.rangeDown(BigInteger.ONE)))) {
            assert p.a != null;
            assert p.b != null;
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
        if (P instanceof ExhaustiveProvider) {
            bases = P.rangeUp(BigInteger.valueOf(2));
        } else {
            bases = map(i -> BigInteger.valueOf(i + 2), ((RandomProvider) P).naturalIntegersGeometric(20));
        }
        Iterable<Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>>> ps = P.dependentPairs(
                bases,
                b -> filter(
                        t -> !t.c.isEmpty(),
                        P.triples(P.lists(P.range(BigInteger.ZERO, b.subtract(BigInteger.ONE))))
                )
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assert p.b.a != null;
            assert p.b.b != null;
            assert p.b.c != null;
            Rational r = fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
            validate(r);
            assertNotEquals(p.toString(), r.signum(), -1);
        }

        ps = filter(
                p -> {
                    assert p.a != null;
                    assert p.b != null;
                    assert p.b.a != null;
                    assert p.b.b != null;
                    assert p.b.c != null;
                    if (!p.b.a.isEmpty() && head(p.b.a).equals(BigInteger.ZERO)) return false;
                    Pair<List<BigInteger>, List<BigInteger>> minimized = minimize(p.b.b, p.b.c);
                    assert minimized.a != null;
                    assert minimized.b != null;
                    if (!minimized.a.equals(p.b.b) || !minimized.b.equals(p.b.c)) return false;
                    return !p.b.c.equals(Arrays.asList(p.a.subtract(BigInteger.ONE)));
                },
                ps
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assert p.b.a != null;
            assert p.b.b != null;
            assert p.b.c != null;
            Rational r = fromPositionalNotation(p.a, p.b.a, p.b.b, p.b.c);
            assertEquals(p.toString(), r.positionalNotation(p.a), p.b);
        }

        Iterable<Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>>> psFail = P.pairs(
                P.rangeDown(BigInteger.ONE),
                filter(t -> {
                    assert t.c != null;
                    return !t.c.isEmpty();
                }, P.triples(P.lists(P.bigIntegers())))
        );
        for (Pair<BigInteger, Triple<List<BigInteger>, List<BigInteger>, List<BigInteger>>> p : take(LIMIT, psFail)) {
            assert p.a != null;
            assert p.b != null;
            assert p.b.a != null;
            assert p.b.b != null;
            assert p.b.c != null;
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
            assert p.a != null;
            assert p.b != null;
            assert p.b.a != null;
            assert p.b.b != null;
            assert p.b.c != null;
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
            assert p.a != null;
            assert p.b != null;
            assert p.b.a != null;
            assert p.b.b != null;
            assert p.b.c != null;
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
            assert p.a != null;
            assert p.b != null;
            assert p.b.a != null;
            assert p.b.b != null;
            assert p.b.c != null;
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
            assert p.a != null;
            assert p.b != null;
            assert p.b.a != null;
            assert p.b.b != null;
            try {
                fromPositionalNotation(p.a, p.b.a, p.b.b, new ArrayList<>());
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
        assert positionalNotation.c != null;
        if (positionalNotation.c.equals(Arrays.asList(BigInteger.ZERO))) {
            afterDecimal = positionalNotation.b;
        } else {
            assert positionalNotation.b != null;
            afterDecimal = concat(positionalNotation.b, cycle(positionalNotation.c));
        }
        return new Pair<>(positionalNotation.a, afterDecimal);
    }

    private static void propertiesDigits() {
        initialize();
        System.out.println("\t\ttesting digits(BigInteger) properties...");

        Iterable<Pair<Rational, BigInteger>> ps;
        if (P instanceof ExhaustiveProvider) {
            ps = ((ExhaustiveProvider) P).pairsSquareRootOrder(
                    cons(ZERO, P.positiveRationals()),
                    P.rangeUp(BigInteger.valueOf(2))
            );
        } else {
            ps = P.pairs(
                    cons(ZERO, ((QBarRandomProvider) P).positiveRationals(8)),
                    map(i -> BigInteger.valueOf(i + 2), ((RandomProvider) P).naturalIntegersGeometric(20))
            );
        }
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Pair<List<BigInteger>, Iterable<BigInteger>> digits = p.a.digits(p.b);
            assert digits.a != null;
            assert digits.b != null;
            assertTrue(p.toString(), digits.a.isEmpty() || !head(digits.a).equals(BigInteger.ZERO));
            assertTrue(p.toString(), all(x -> x.signum() != -1 && lt(x, p.b), digits.a));
            assertEquals(p.toString(), MathUtils.fromBigEndianDigits(p.b, digits.a), p.a.floor());
        }
    }

    private static void propertiesEquals() {
        initialize();
        System.out.println("\t\ttesting equals(Object) properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            //noinspection EqualsWithItself
            assertTrue(r.toString(), r.equals(r));
            //noinspection ObjectEqualsNull
            assertFalse(r.toString(), r.equals(null));
        }
    }

    private static void propertiesHashCode() {
        initialize();
        System.out.println("\t\ttesting hashCode() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.hashCode(), r.hashCode());
        }
    }

    private static void propertiesCompareTo() {
        initialize();
        System.out.println("\t\ttesting compareTo(Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            int compare = p.a.compareTo(p.b);
            assertTrue(p.toString(), compare == -1 || compare == 0 || compare == 1);
            assertEquals(p.toString(), p.b.compareTo(p.a), -compare);
            assertEquals(p.toString(), p.a.subtract(p.b).signum(), compare);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.compareTo(r), 0);
        }

        Iterable<Triple<Rational, Rational, Rational>> ts = filter(
                t -> {
                    assert t.a != null;
                    assert t.b != null;
                    return lt(t.a, t.b) && lt(t.b, t.c);
                },
                P.triples(P.rationals())
        );
        for (Triple<Rational, Rational, Rational> t : take(LIMIT, ts)) {
            assert t.a != null;
            assert t.c != null;
            assertEquals(t.toString(), t.a.compareTo(t.c), -1);
        }
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
            validate(or.get());
        }

        Pair<Iterable<String>, Iterable<String>> slashPartition = partition(s -> s.contains("/"), ss);
        assert slashPartition.a != null;
        assert slashPartition.b != null;
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

        for (String s : take(LIMIT, P.strings())) {
            findIn(s);
        }

        Iterable<Pair<String, Integer>> ps = P.dependentPairsLogarithmic(P.strings(), s -> range(0, s.length()));
        Iterable<String> ss = map(
                p -> {
                    assert p.a != null;
                    assert p.a.a != null;
                    assert p.a.b != null;
                    return take(p.a.b, p.a.a) + p.b + drop(p.a.b, p.a.a);
                },
                P.pairs(ps, P.rationals())
        );
        for (String s : take(LIMIT, ss)) {
            Optional<Pair<Rational, Integer>> op = findIn(s);
            Pair<Rational, Integer> p = op.get();
            assertNotNull(s, p.a);
            assertNotNull(s, p.b);
            assertTrue(s, p.b >= 0 && p.b < s.length());
            String before = take(p.b, s);
            assertFalse(s, findIn(before).isPresent());
            String during = p.a.toString();
            assertTrue(s, s.substring(p.b).startsWith(during));
            String after = drop(p.b + during.length(), s);
            assertTrue(s, after.isEmpty() || !read(during + head(after)).isPresent());
        }
    }

    private static void propertiesToString() {
        initialize();
        System.out.println("\t\ttesting toString() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            String s = r.toString();
            assertTrue(isSubsetOf(s, RATIONAL_CHARS));
            Optional<Rational> readR = read(s);
            assertTrue(r.toString(), readR.isPresent());
            assertEquals(r.toString(), readR.get(), r);
        }
    }

    private static void validate(@NotNull Rational r) {
        assertEquals(r.toString(), r.getNumerator().gcd(r.getDenominator()), BigInteger.ONE);
        assertEquals(r.toString(), r.getDenominator().signum(), 1);
        if (r.equals(ZERO)) assertTrue(r.toString(), r == ZERO);
        if (r.equals(ONE)) assertTrue(r.toString(), r == ONE);
    }

    private static <T> void aeq(String message, Iterable<T> xs, Iterable<T> ys) {
        assertTrue(message, equal(xs, ys));
    }

    private static void aeq(String message, int i, int j) {
        assertEquals(message, i, j);
    }

    private static void aeq(String message, long i, long j) {
        assertEquals(message, i, j);
    }

    private static void aeq(String message, float x, float y) {
        assertEquals(message, Float.toString(x), Float.toString(y));
    }

    private static void aeq(String message, double x, double y) {
        assertEquals(message, Double.toString(x), Double.toString(y));
    }

    private static void aeq(String message, BigDecimal x, BigDecimal y) {
        assertEquals(message, x.stripTrailingZeros(), y.stripTrailingZeros());
    }
}
