package mho.qbar.objects;

import mho.wheels.iterables.ExhaustiveProvider;
import mho.wheels.iterables.RandomProvider;
import mho.wheels.math.Combinatorics;
import mho.wheels.math.MathUtils;
import mho.wheels.misc.BigDecimalUtils;
import mho.wheels.misc.Readers;
import mho.wheels.ordering.Ordering;
import mho.wheels.structures.Pair;
import mho.wheels.structures.Triple;
import mho.qbar.iterableProviders.QBarExhaustiveProvider;
import mho.qbar.iterableProviders.QBarIterableProvider;
import mho.qbar.iterableProviders.QBarRandomProvider;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import sun.jvm.hotspot.utilities.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
        for (boolean useRandom : Arrays.asList(false, true)) {
            System.out.println("Testing " + (useRandom ? "randomly" : "exhaustively"));
            USE_RANDOM = useRandom;
            
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
            propertiesBigIntegerValue_RoundingMode();
            propertiesBigIntegerValue();
            propertiesBigIntegerValueExact();
            propertiesByteValueExact();
            propertiesShortValueExact();
            propertiesIntValueExact();
            propertiesLongValueExact();
            propertiesHasTerminatingDecimalExpansion();
            propertiesBigDecimalValue_int_RoundingMode();
            propertiesBigDecimalValue_int();
            propertiesNegate();
            propertiesInvert();
            propertiesAbs();
            propertiesSignum();
            propertiesAdd();
            propertiesSubtract();
            propertiesMultiply_Rational_Rational();
            propertiesMultiply_BigInteger();
            propertiesMultiply_int();
            propertiesDivide_Rational_Rational();
            propertiesDivide_BigInteger();
            propertiesDivide_int();
            propertiesSum();
            propertiesProduct();
            propertiesDelta();
            propertiesHarmonicNumber();
            propertiesPow();
            propertiesFloor();
            propertiesCeiling();
            propertiesFractionalPart();
            propertiesRoundToDenominator();
            propertiesShiftLeft();
            propertiesShiftRight();
//        binaryExponentProperties();
//        toFloatProperties();
//        toFloatRoundingModeProperties();
            propertiesEquals();
            propertiesHashCode();
            propertiesCompareTo();
            propertiesRead();
            propertiesToString();

            System.out.println();
        }
        System.out.println("Done");
    }

    private static void propertiesOf_BigInteger_BigInteger() {
        initialize();
        System.out.println("testing of(BigInteger, BigInteger) properties...");

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
                fail();
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesOf_long_long() {
        initialize();
        System.out.println("testing of(long, long) properties...");

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
                fail();
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesOf_int_int() {
        initialize();
        System.out.println("testing of(int, int) properties...");

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
                fail();
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesOf_BigInteger() {
        initialize();
        System.out.println("testing of(BigInteger) properties...");

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            Rational r = of(i);
            validate(r);
            assertEquals(i.toString(), r.getDenominator(), BigInteger.ONE);
        }
    }

    private static void propertiesOf_long() {
        initialize();
        System.out.println("testing of(long) properties...");

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
        System.out.println("testing of(int) properties...");

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
        System.out.println("testing of(float) properties...");

        Iterable<Float> fs = filter(f -> Float.isFinite(f) && !Float.isNaN(f), P.floats());
        for (float f : take(LIMIT, fs)) {
            Rational r = of(f);
            assert r != null;
            validate(r);
            assertTrue(Float.toString(f), r.hasTerminatingDecimalExpansion());
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
        System.out.println("testing of(double) properties...");

        Iterable<Double> ds = filter(d -> Double.isFinite(d) && !Double.isNaN(d), P.doubles());
        for (double d : take(LIMIT, ds)) {
            Rational r = of(d);
            assert r != null;
            validate(r);
            assertTrue(Double.toString(d), r.hasTerminatingDecimalExpansion());
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
        System.out.println("testing ofExact(float) properties...");

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
        System.out.println("testing ofExact(double) properties...");

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
        System.out.println("testing of(BigDecimal) properties...");

        for (BigDecimal bd : take(LIMIT, P.bigDecimals())) {
            Rational r = of(bd);
            validate(r);
            aeq(bd.toString(), bd, r.bigDecimalValueExact());
            assertTrue(bd.toString(), r.hasTerminatingDecimalExpansion());
        }
    }

    private static void propertiesBigIntegerValue_RoundingMode() {
        initialize();
        System.out.println("testing bigIntegerValue(RoundingMode) properties...");

        Iterable<Pair<Rational, RoundingMode>> ps = filter(
                p -> {
                    assert p.a != null;
                    return p.b != RoundingMode.UNNECESSARY || p.a.getDenominator().equals(BigInteger.ONE);
                },
                P.pairs(P.rationals(), P.roundingModes())
        );
        for (Pair<Rational, RoundingMode> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            BigInteger rounded = p.a.bigIntegerValue(p.b);
            assertTrue(p.toString(), rounded.equals(BigInteger.ZERO) || rounded.signum() == p.a.signum());
            assertTrue(p.toString(), lt(subtract(p.a, of(rounded)).abs(), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).bigIntegerValue(RoundingMode.UNNECESSARY), i);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.bigIntegerValue(RoundingMode.FLOOR), r.floor());
            assertEquals(r.toString(), r.bigIntegerValue(RoundingMode.CEILING), r.ceiling());
            assertTrue(r.toString(), le(of(r.bigIntegerValue(RoundingMode.DOWN)).abs(), r.abs()));
            assertTrue(r.toString(), ge(of(r.bigIntegerValue(RoundingMode.UP)).abs(), r.abs()));
            assertTrue(r.toString(), le(subtract(r, of(r.bigIntegerValue(RoundingMode.HALF_DOWN))).abs(), of(1, 2)));
            assertTrue(r.toString(), le(subtract(r, of(r.bigIntegerValue(RoundingMode.HALF_UP))).abs(), of(1, 2)));
            assertTrue(r.toString(), le(subtract(r, of(r.bigIntegerValue(RoundingMode.HALF_EVEN))).abs(), of(1, 2)));
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

        for (Rational r : take(LIMIT, filter(s -> !s.getDenominator().equals(BigInteger.ONE), P.rationals()))) {
            try {
                r.bigIntegerValue(RoundingMode.UNNECESSARY);
                fail();
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesBigIntegerValue() {
        initialize();
        System.out.println("testing bigIntegerValue() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            BigInteger rounded = r.bigIntegerValue();
            assertTrue(r.toString(), rounded.equals(BigInteger.ZERO) || rounded.signum() == r.signum());
            assertTrue(r.toString(), le(subtract(r, of(r.bigIntegerValue())).abs(), of(1, 2)));
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
        System.out.println("testing bigIntegerValueExact() properties...");

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).bigIntegerValueExact(), i);
        }

        for (Rational r : take(LIMIT, filter(s -> !s.getDenominator().equals(BigInteger.ONE), P.rationals()))) {
            try {
                r.bigIntegerValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesByteValueExact() {
        initialize();
        System.out.println("testing byteValueExact() properties...");

        for (byte b : take(LIMIT, P.bytes())) {
            assertEquals(Byte.toString(b), of(b).byteValueExact(), b);
        }

        for (Rational r : take(LIMIT, filter(s -> !s.getDenominator().equals(BigInteger.ONE), P.rationals()))) {
            try {
                r.byteValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, range(BigInteger.valueOf(Byte.MAX_VALUE).add(BigInteger.ONE)))) {
            try {
                of(i).byteValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> below = rangeBy(
                BigInteger.valueOf(Byte.MIN_VALUE).subtract(BigInteger.ONE),
                BigInteger.valueOf(-1)
        );
        for (BigInteger i : take(LIMIT, below)) {
            try {
                of(i).byteValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesShortValueExact() {
        initialize();
        System.out.println("testing shortValueExact() properties...");

        for (short s : take(LIMIT, P.shorts())) {
            assertEquals(Short.toString(s), of(s).shortValueExact(), s);
        }

        for (Rational r : take(LIMIT, filter(s -> !s.getDenominator().equals(BigInteger.ONE), P.rationals()))) {
            try {
                r.shortValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, range(BigInteger.valueOf(Short.MAX_VALUE).add(BigInteger.ONE)))) {
            try {
                of(i).shortValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> below = rangeBy(
                BigInteger.valueOf(Short.MIN_VALUE).subtract(BigInteger.ONE),
                BigInteger.valueOf(-1)
        );
        for (BigInteger i : take(LIMIT, below)) {
            try {
                of(i).shortValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesIntValueExact() {
        initialize();
        System.out.println("testing intValueExact() properties...");

        for (int i : take(LIMIT, P.integers())) {
            assertEquals(Integer.toString(i), of(i).intValueExact(), i);
        }

        for (Rational r : take(LIMIT, filter(s -> !s.getDenominator().equals(BigInteger.ONE), P.rationals()))) {
            try {
                r.intValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, range(BigInteger.valueOf(Integer.MAX_VALUE).add(BigInteger.ONE)))) {
            try {
                of(i).intValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> below = rangeBy(
                BigInteger.valueOf(Integer.MIN_VALUE).subtract(BigInteger.ONE),
                BigInteger.valueOf(-1)
        );
        for (BigInteger i : take(LIMIT, below)) {
            try {
                of(i).intValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesLongValueExact() {
        initialize();
        System.out.println("testing longValueExact() properties...");

        for (long l : take(LIMIT, P.longs())) {
            assertEquals(Long.toString(l), of(l).longValueExact(), l);
        }

        for (Rational r : take(LIMIT, filter(s -> !s.getDenominator().equals(BigInteger.ONE), P.rationals()))) {
            try {
                r.longValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }

        for (BigInteger i : take(LIMIT, range(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE)))) {
            try {
                of(i).longValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }

        Iterable<BigInteger> below = rangeBy(
                BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE),
                BigInteger.valueOf(-1)
        );
        for (BigInteger i : take(LIMIT, below)) {
            try {
                of(i).longValueExact();
                fail();
            } catch (ArithmeticException ignored) {}
        }
    }

    private static void propertiesHasTerminatingDecimalExpansion() {
        initialize();

        System.out.println("testing hasTerminatingDecimalExpansion() properties...");
        for (Rational r : take(LIMIT, P.rationals())) {
            r.hasTerminatingDecimalExpansion();
        }

        Iterable<Rational> rs = filter(Rational::hasTerminatingDecimalExpansion, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            r.bigDecimalValue(0);
            List<BigInteger> dPrimeFactors = toList(
                    nub(map(p -> p.a, MathUtils.compactPrimeFactors(r.getDenominator())))
            );
            assertTrue(
                    r.toString(),
                    isSubsetOf(dPrimeFactors, Arrays.asList(BigInteger.valueOf(2), BigInteger.valueOf(5)))
            );
        }
    }

    private static void propertiesBigDecimalValue_int_RoundingMode() {
        initialize();
        System.out.println("testing bigDecimalValue(int, RoundingMode)...");

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
            boolean closerToDown = lt(subtract(r, of(down)).abs(), subtract(r, of(up)).abs());
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
        //todo failure tests
    }

    private static void propertiesBigDecimalValue_int() {
        initialize();
        System.out.println("testing bigDecimalValue(int)...");

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
            boolean closerToDown = lt(subtract(r, of(down)).abs(), subtract(r, of(up)).abs());
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
    }

    private static void propertiesNegate() {
        initialize();
        System.out.println("testing negate() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational negativeR = r.negate();
            validate(negativeR);
            assertEquals(r.toString(), r, negativeR.negate());
            assertTrue(add(r, negativeR) == ZERO);
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            Rational negativeR = r.negate();
            assertTrue(r.toString(), !r.equals(negativeR));
        }
    }

    private static void propertiesInvert() {
        initialize();
        System.out.println("testing invert() properties...");

        Iterable<Rational> rs = filter(r -> r != ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            Rational inverseR = r.invert();
            validate(inverseR);
            assertEquals(r.toString(), r, inverseR.invert());
            assertTrue(multiply(r, inverseR) == ONE);
            assertTrue(inverseR != ZERO);
        }

        rs = filter(r -> r != ZERO && r.abs() != ONE, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            Rational inverseR = r.invert();
            assertTrue(r.toString(), !r.equals(inverseR));
        }
    }

    private static void propertiesAbs() {
        initialize();
        System.out.println("testing abs() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational absR = r.abs();
            validate(absR);
            assertEquals(r.toString(), absR, absR.abs());
            assertTrue(r.toString(), ge(absR, ZERO));
        }
    }

    private static void propertiesSignum() {
        initialize();
        System.out.println("testing signum() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            int signumR = r.signum();
            assertEquals(r.toString(), signumR, Ordering.compare(r, ZERO).toInt());
            assertTrue(r.toString(), signumR == -1 || signumR == 0 || signumR == 1);
        }
    }

    private static void propertiesAdd() {
        initialize();
        System.out.println("testing add(Rational, Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            Rational sum = add(p.a, p.b);
            validate(sum);
            assertEquals(p.toString(), sum, add(p.b, p.a));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), add(ZERO, r), r);
            assertEquals(r.toString(), add(r, ZERO), r);
            assertTrue(r.toString(), add(r, r.negate()) == ZERO);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(P.rationals()))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational sum1 = add(add(t.a, t.b), t.c);
            Rational sum2 = add(t.a, add(t.b, t.c));
            assertEquals(t.toString(), sum1, sum2);
        }
    }

    private static void propertiesSubtract() {
        initialize();
        System.out.println("testing subtract(Rational, Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            Rational difference = subtract(p.a, p.b);
            validate(difference);
            assertEquals(p.toString(), difference, subtract(p.b, p.a).negate());
            assertEquals(p.toString(), p.a, add(difference, p.b));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), subtract(ZERO, r), r.negate());
            assertEquals(r.toString(), subtract(r, ZERO), r);
            assertTrue(r.toString(), subtract(r, r) == ZERO);
        }
    }

    private static void propertiesMultiply_Rational_Rational() {
        initialize();
        System.out.println("testing multiply(Rational, Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            Rational product = multiply(p.a, p.b);
            validate(product);
            assertEquals(p.toString(), product, multiply(p.b, p.a));
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), multiply(ONE, r), r);
            assertEquals(r.toString(), multiply(r, ONE), r);
            assertTrue(r.toString(), multiply(ZERO, r) == ZERO);
            assertTrue(r.toString(), multiply(r, ZERO) == ZERO);
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            assertTrue(r.toString(), multiply(r, r.invert()) == ONE);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(P.rationals()))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational product1 = multiply(multiply(t.a, t.b), t.c);
            Rational product2 = multiply(t.a, multiply(t.b, t.c));
            assertEquals(t.toString(), product1, product2);
        }

        for (Triple<Rational, Rational, Rational> t : take(LIMIT, P.triples(P.rationals()))) {
            assert t.a != null;
            assert t.b != null;
            assert t.c != null;
            Rational expression1 = multiply(add(t.a, t.b), t.c);
            Rational expression2 = add(multiply(t.a, t.c), multiply(t.b, t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void propertiesMultiply_BigInteger() {
        initialize();
        System.out.println("testing multiply(BigInteger) properties...");

        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(P.rationals(), P.bigIntegers());
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational product = p.a.multiply(p.b);
            validate(product);
            assertEquals(p.toString(), product, multiply(p.a, of(p.b)));
            assertEquals(p.toString(), product, multiply(of(p.b), p.a));
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
            Rational expression1 = add(t.a, t.b).multiply(t.c);
            Rational expression2 = add(t.a.multiply(t.c), t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void propertiesMultiply_int() {
        initialize();
        System.out.println("testing multiply(int) properties...");

        for (Pair<Rational, Integer> p :take(LIMIT, P.pairs(P.rationals(), P.integers()))) {
            assert p.a != null;
            assert p.b != null;
            Rational product = p.a.multiply(p.b);
            validate(product);
            assertEquals(p.toString(), product, multiply(p.a, of(p.b)));
            assertEquals(p.toString(), product, multiply(of(p.b), p.a));
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
            Rational expression1 = add(t.a, t.b).multiply(t.c);
            Rational expression2 = add(t.a.multiply(t.c), t.b.multiply(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void propertiesDivide_Rational_Rational() {
        initialize();
        System.out.println("testing divide(Rational, Rational) properties...");

        Iterable<Pair<Rational, Rational>> ps = filter(p -> p.b != ZERO, P.pairs(P.rationals()));
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational quotient = divide(p.a, p.b);
            validate(quotient);
            assertEquals(p.toString(), p.a, multiply(quotient, p.b));
        }

        ps = filter(p -> p.a != ZERO && p.b != ZERO, P.pairs(P.rationals()));
        for (Pair<Rational, Rational> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), divide(p.a, p.b), divide(p.b, p.a).invert());
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), divide(r, ONE), r);
        }

        Iterable<Rational> rs = filter(r -> r != ZERO, P.rationals());
        for (Rational r : take(LIMIT, rs)) {
            assertEquals(r.toString(), divide(ONE, r), r.invert());
            assertTrue(r.toString(), divide(r, r) == ONE);
        }
    }

    private static void propertiesDivide_BigInteger() {
        initialize();
        System.out.println("testing divide(BigInteger) properties...");

        Iterable<Pair<Rational, BigInteger>> ps = P.pairs(
                P.rationals(),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational quotient = p.a.divide(p.b);
            validate(quotient);
            assertEquals(p.toString(), p.a, quotient.multiply(p.b));
        }

        ps = P.pairs(
                (Iterable<Rational>) filter(r -> r != ZERO, P.rationals()),
                filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers())
        );
        for (Pair<Rational, BigInteger> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), p.a.divide(p.b), divide(of(p.b), p.a).invert());
        }
        
        Iterable<BigInteger> bis = filter(i -> !i.equals(BigInteger.ZERO), P.bigIntegers());
        for (BigInteger i : take(LIMIT, bis)) {
            assertEquals(i.toString(), ONE.divide(i), of(i).invert());
            assertEquals(i.toString(), of(i).divide(i), ONE);
        }
        
        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.divide(BigInteger.ONE), r);      
        }
    }

    private static void propertiesDivide_int() {
        initialize();
        System.out.println("testing divide(int) properties...");

        Iterable<Pair<Rational, Integer>> ps = P.pairs(P.rationals(), filter(i -> i != 0, P.integers()));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            Rational quotient = p.a.divide(p.b);
            validate(quotient);
            assertEquals(p.toString(), p.a, quotient.multiply(p.b));
        }

        ps = P.pairs((Iterable<Rational>) filter(r -> r != ZERO, P.rationals()), filter(i -> i != 0, P.integers()));
        for (Pair<Rational, Integer> p : take(LIMIT, ps)) {
            assert p.a != null;
            assert p.b != null;
            assertEquals(p.toString(), p.a.divide(p.b), divide(of(p.b), p.a).invert());
        }

        Iterable<Integer> is = filter(i -> i != 0, P.integers());
        for (int i : take(LIMIT, is)) {
            assertEquals(Integer.toString(i), ONE.divide(i), of(i).invert());
            assertEquals(Integer.toString(i), of(i).divide(i), ONE);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.divide(1), r);
        }
    }

    private static void propertiesSum() {
        initialize();
        System.out.println("testing sum(Iterable<Rational>) properties...");

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
            assertEquals(p.toString(), sum(Arrays.asList(p.a, p.b)), add(p.a, p.b));
        }
    }

    private static void propertiesProduct() {
        initialize();
        System.out.println("testing product(Iterable<Rational>) properties...");

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
            assertEquals(p.toString(), product(Arrays.asList(p.a, p.b)), multiply(p.a, p.b));
        }
    }

    private static void propertiesDelta() {
        initialize();
        System.out.println("testing delta(Iterable<Rational>) properties...");

        for (List<Rational> rs : take(LIMIT, filter(ss -> !ss.isEmpty(), P.lists(P.rationals())))) {
            Iterable<Rational> reversed = reverse(map(Rational::negate, delta(reverse(rs))));
            aeq(rs.toString(), delta(rs), reversed);
        }

        for (Rational r : take(LIMIT, P.rationals())) {
            assertTrue(r.toString(), isEmpty(delta(Arrays.asList(r))));
        }

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            aeq(p.toString(), delta(Arrays.asList(p.a, p.b)), Arrays.asList(subtract(p.b, p.a)));
        }
    }

    private static void propertiesHarmonicNumber() {
        initialize();
        System.out.println("testing harmonicNumber(int) properties...");

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
            assertFalse(Integer.toString(i), h.getDenominator().equals(BigInteger.ONE));
        }
    }

    private static void propertiesPow() {
        initialize();
        System.out.println("testing pow(int) properties...");

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
            assertEquals(r.toString(), r.pow(2), multiply(r, r));
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
            Rational expression1 = multiply(t.a.pow(t.b), t.a.pow(t.c));
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
            Rational expression1 = divide(t.a.pow(t.b), t.a.pow(t.c));
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
            Rational expression1 = multiply(t.a, t.b).pow(t.c);
            Rational expression2 = multiply(t.a.pow(t.c), t.b.pow(t.c));
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
            Rational expression1 = divide(t.a, t.b).pow(t.c);
            Rational expression2 = divide(t.a.pow(t.c), t.b.pow(t.c));
            assertEquals(t.toString(), expression1, expression2);
        }
    }

    private static void propertiesFloor() {
        initialize();
        System.out.println("testing floor() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            BigInteger floor = r.floor();
            assertTrue(r.toString(), le(of(floor), r));
            assertTrue(r.toString(), le(subtract(r, of(floor)), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).floor(), i);
        }
    }

    private static void propertiesCeiling() {
        initialize();
        System.out.println("testing ceiling() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            BigInteger ceiling = r.ceiling();
            assertTrue(r.toString(), ge(of(ceiling), r));
            assertTrue(r.toString(), le(subtract(of(ceiling), r), ONE));
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).ceiling(), i);
        }
    }

    private static void propertiesFractionalPart() {
        initialize();
        System.out.println("testing fractionalPart() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            Rational fractionalPart = r.fractionalPart();
            validate(fractionalPart);
            assertTrue(r.toString(), ge(fractionalPart, ZERO));
            assertTrue(r.toString(), lt(fractionalPart, ONE));
            assertEquals(r.toString(), add(of(r.floor()), fractionalPart), r);
        }

        for (BigInteger i : take(LIMIT, P.bigIntegers())) {
            assertEquals(i.toString(), of(i).fractionalPart(), ZERO);
        }
    }

    private static void propertiesRoundToDenominator() {
        initialize();
        System.out.println("testing roundToDenominator(BigInteger, RoundingMode) properties...");

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
            assertTrue(t.toString(), lt(subtract(t.a, rounded).abs(), of(BigInteger.ONE, t.b)));
        }

        Iterable<Pair<Rational, RoundingMode>> ps1 = filter(
                p -> {
                    assert p.a != null;
                    return p.b != RoundingMode.UNNECESSARY || p.a.getDenominator().equals(BigInteger.ONE);
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
                            subtract(p.a, p.a.roundToDenominator(p.b, RoundingMode.HALF_DOWN)).abs(),
                            of(p.b).shiftLeft(1).invert()
                    )
            );
            assertTrue(
                    p.toString(),
                    le(
                            subtract(p.a, p.a.roundToDenominator(p.b, RoundingMode.HALF_UP)).abs(),
                            of(p.b).shiftLeft(1).invert()
                    )
            );
            assertTrue(
                    p.toString(),
                    le(
                            subtract(p.a, p.a.roundToDenominator(p.b, RoundingMode.HALF_EVEN)).abs(),
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
    }

    private static void propertiesShiftLeft() {
        initialize();
        System.out.println("testing shiftLeft(int) properties...");

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

    private static void propertiesShiftRight() {
        initialize();
        System.out.println("testing shiftRight(int) properties...");

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
//
//    private static void binaryExponentProperties() {
//        for (Rational r : positiveRationals().iterate(limit)) {
//            int exponent = r.binaryExponent();
//            Rational power = ONE.shiftLeft(exponent);
//            assertTrue(r.toString(), power.compareTo(r) <= 0);
//            assertTrue(r.toString(), r.compareTo(power.shiftLeft(1)) < 0);
//        }
//    }
//
//    private static void toFloatProperties() {
//        for (Rational r : P.rationals().iterate(limit)) {
//            float f = r.toFloat();
//            assertEquals(r.toString(), f, r.toFloat(RoundingMode.HALF_EVEN));
//        }
//    }
//
//    private static void toFloatRoundingModeProperties() {
//        Iterable<Pair<Rational, RoundingMode>> g = new FilteredIterable<Pair<Rational, RoundingMode>>(
//                P.pairs(P.rationals(), Iterables.roundingModes()),
//                p -> p.b != RoundingMode.UNNECESSARY
//                        || of(p.a.toFloat(RoundingMode.FLOOR)).equals(p.a));
//        for (Pair<Rational, RoundingMode> p : g.iterate(limit)) {
//            float rounded = p.a.toFloat(p.b);
//            assertTrue(p.toString(), !Float.isNaN(rounded));
//            assertTrue(p.toString(), rounded == 0.0 || Math.signum(rounded) == p.a.signum());
//            float successor = MathUtils.successor(rounded);
//            float predecessor = MathUtils.predecessor(rounded);
//            float up = p.a.signum() == -1 ? predecessor : successor;
//            float down = p.a.signum() == -1 ? successor : predecessor;
//            if (p.b == RoundingMode.UNNECESSARY) {
//                assertEquals(p.toString(), p.a, of(rounded));
//                assertTrue(p.toString(), Float.isFinite(rounded));
//                assertTrue(p.toString(), !new Float(rounded).equals(-0.0f));
//            }
//            if (p.b == RoundingMode.FLOOR) {
//                assertTrue(p.toString(), of(rounded).compareTo(p.a) <= 0);
//                assertTrue(p.toString(), of(successor).compareTo(p.a) > 0);
//                if (p.a.compareTo(ZERO) >= 0 && p.a.compareTo(SMALLEST_FLOAT) < 0) {
//                    assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(LARGEST_FLOAT.negate()) < 0) {
//                    assertTrue(p.toString(), rounded < 0 && Float.isInfinite(rounded));
//                }
//                if (p.a.compareTo(LARGEST_FLOAT) >= 0) {
//                    assertEquals(p.toString(), rounded, Float.MAX_VALUE);
//                }
//                assertTrue(p.toString(), rounded < 0 || Float.isFinite(rounded));
//                assertTrue(p.toString(), !new Float(rounded).equals(-0.0f));
//            }
//            if (p.b == RoundingMode.CEILING) {
//                assertTrue(p.toString(), of(rounded).compareTo(p.a) >= 0);
//                assertTrue(p.toString(), of(predecessor).compareTo(p.a) < 0);
//                if (p.a == ZERO) {
//                    assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(ZERO) < 0 && p.a.compareTo(SMALLEST_FLOAT.negate()) > 0) {
//                    assertEquals(p.toString(), rounded, -0.0f);
//                }
//                if (p.a.compareTo(LARGEST_FLOAT) > 0) {
//                    assertTrue(p.toString(), rounded > 0 && Float.isInfinite(rounded));
//                }
//                if (p.a.compareTo(LARGEST_FLOAT.negate()) <= 0) {
//                    assertEquals(p.toString(), rounded, -Float.MAX_VALUE);
//                }
//                assertTrue(p.toString(), rounded > 0 || Float.isFinite(rounded));
//            }
//            if (p.b == RoundingMode.DOWN) {
//                assertTrue(p.toString(), of(rounded).abs().compareTo(p.a.abs()) <= 0);
//                assertTrue(p.toString(), of(up).abs().compareTo(p.a.abs()) > 0);
//                if (p.a.compareTo(ZERO) >= 0 && p.a.compareTo(SMALLEST_FLOAT) < 0) {
//                    assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(ZERO) < 0 && p.a.compareTo(SMALLEST_FLOAT.negate()) > 0) {
//                    assertEquals(p.toString(), rounded, -0.0f);
//                }
//                if (p.a.compareTo(LARGEST_FLOAT) >= 0) {
//                    assertEquals(p.toString(), rounded, Float.MAX_VALUE);
//                }
//                if (p.a.compareTo(LARGEST_FLOAT.negate()) <= 0) {
//                    assertEquals(p.toString(), rounded, -Float.MAX_VALUE);
//                }
//                assertTrue(p.toString(), Float.isFinite(rounded));
//            }
//            if (p.b == RoundingMode.UP) {
//                assertTrue(p.toString(), of(rounded).abs().compareTo(p.a.abs()) >= 0);
//                if (p.a != ZERO) {
//                    assertTrue(p.toString(), of(down).abs().compareTo(p.a.abs()) < 0);
//                }
//                if (p.a == ZERO) {
//                    assertEquals(p.toString(), rounded, 0.0f);
//                }
//                if (p.a.compareTo(LARGEST_FLOAT) > 0) {
//                    assertTrue(p.toString(), rounded > 0 && Float.isInfinite(rounded));
//                }
//                if (p.a.compareTo(LARGEST_FLOAT.negate()) < 0) {
//                    assertTrue(p.toString(), rounded < 0 && Float.isInfinite(rounded));
//                }
//                assertTrue(p.toString(), !new Float(rounded).equals(-0.0f));
//            }
//            if (p.b == RoundingMode.HALF_DOWN || p.b == RoundingMode.HALF_UP || p.b == RoundingMode.HALF_EVEN) {
//                boolean equidistant = false;
//                if (Float.isFinite(rounded) && rounded != Float.MAX_VALUE && rounded != -Float.MAX_VALUE) {
//                    Rational distance = subtract(of(rounded), p.a).abs();
//                    Rational predDistance = subtract(of(predecessor), p.a).abs();
//                    Rational succDistance = subtract(of(successor), p.a).abs();
//                    assertEquals(p.toString(), min(distance, predDistance, succDistance), distance);
//                    if (distance.equals(predDistance) || distance.equals(succDistance)) {
//                        equidistant = true;
//                    }
//                }
//                //TODO
//                if (p.b == RoundingMode.HALF_DOWN) {
//                    if (equidistant) {
//                        assertEquals(p.toString(), rounded, p.a.toFloat(RoundingMode.DOWN));
//                    }
//                } else if (p.b == RoundingMode.HALF_UP) {
//                    if (equidistant) {
//                        assertEquals(p.toString(), rounded, p.a.toFloat(RoundingMode.UP));
//                    }
//                }
//            }
//        }
//    }

    private static void propertiesEquals() {
        initialize();
        System.out.println("testing equals(Object) properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            //noinspection EqualsWithItself
            assertTrue(r.toString(), r.equals(r));
            //noinspection ObjectEqualsNull
            assertFalse(r.toString(), r.equals(null));
        }
    }

    private static void propertiesHashCode() {
        initialize();
        System.out.println("testing hashCode() properties...");

        for (Rational r : take(LIMIT, P.rationals())) {
            assertEquals(r.toString(), r.hashCode(), r.hashCode());
        }
    }

    private static void propertiesCompareTo() {
        initialize();
        System.out.println("testing compareTo(Rational) properties...");

        for (Pair<Rational, Rational> p : take(LIMIT, P.pairs(P.rationals()))) {
            assert p.a != null;
            assert p.b != null;
            int compare = p.a.compareTo(p.b);
            assertTrue(p.toString(), compare == -1 || compare == 0 || compare == 1);
            assertEquals(p.toString(), p.b.compareTo(p.a), -compare);
            assertEquals(p.toString(), subtract(p.a, p.b).signum(), compare);
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
        System.out.println("testing read(String) properties...");

        for (String s : take(LIMIT, P.strings())) {
            read(s);
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

    private static void propertiesToString() {
        initialize();
        System.out.println("testing toString() properties...");

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
